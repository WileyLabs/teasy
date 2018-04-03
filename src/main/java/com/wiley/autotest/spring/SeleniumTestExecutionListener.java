package com.wiley.autotest.spring;

import com.wiley.autotest.annotations.UnexpectedAlertCapability;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.driver.WebDriverDecorator;
import com.wiley.autotest.services.Configuration;
import com.wiley.autotest.spring.testexecution.TeasyDriver;
import com.wiley.autotest.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.httpclient.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;

import static com.wiley.autotest.selenium.SeleniumHolder.*;
import static com.wiley.autotest.spring.testexecution.DriverFactory.*;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private static final int ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 1;
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumTestExecutionListener.class);
    private static final ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> -1);
    private static final ThreadLocal<Integer> driverRestartCount = ThreadLocal.withInitial(() -> 0);
    private static final ThreadLocal<UnexpectedAlertBehaviour> alertCapability = ThreadLocal.withInitial(() -> UnexpectedAlertBehaviour.ACCEPT);
    private static final ThreadLocal<UnexpectedAlertBehaviour> currentAlertCapability = ThreadLocal.withInitial(() -> UnexpectedAlertBehaviour.ACCEPT);
    private String[] activeProfiles;

    @Override
    public void prepareTestInstance(final TestContext context) {
        setActiveSpringProfiles(context);

        final Settings settings = getBean(context, Settings.class);
        final Configuration configuration = getBean(context, Configuration.class);
        System.setProperty("http.maxConnections", "1000000");
        System.setProperty("http.keepAlive", "false");
        count.set(count.get() + 1);
        driverRestartCount.set(driverRestartCount.get() + 1);

        boolean isRunWithGrid = settings.isRunTestsWithGrid();
        Integer restartDriverCount = settings.getRestartDriverCount() != null ? settings.getRestartDriverCount() : 0;

        restartDriverIfItsTimeToRestart(settings, restartDriverCount);
        restartDriverIfNewBrowserTypeWasRequested();
        restartDriverIfNewPlatformWasRequested();


        if (getWebDriver() == null || isBrowserDead()) {
            try {
                FramesTransparentWebDriver driver = createDriver(settings, configuration);
                addShutdownHook(driver);

                setGridParams(settings, isRunWithGrid, driver);
                setDriverParams(settings, driver);
                setMobileParams(driver);
                setCustomElementFactory(configuration);
            } catch (Throwable t) {
                lastTryToCreateDriver(context, t);
            }
        }
    }

    private void setDriverParams(Settings settings, FramesTransparentWebDriver driver) {
        SeleniumHolder.setWebDriver(driver);
        SeleniumHolder.setTimeoutInSeconds(settings.getTimeout());
    }

    private void setActiveSpringProfiles(TestContext context) {
        if (activeProfiles == null || activeProfiles.length == 0) {
            activeProfiles = context.getApplicationContext().getEnvironment().getActiveProfiles();
            SeleniumHolder.setActiveProfilesList(Arrays.asList(activeProfiles));
        }
    }

    private void restartDriverIfItsTimeToRestart(Settings settings, Integer restartDriverCount) {
        if (restartDriverCount > 0) {
            if (count.get() > (settings.getPlatform().equals(ANDROID) ? ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                    settings.getDriverName().equals(SAFARI) ? SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                            (settings.getDriverName().equals(IE) || settings.getDriverName().equals(IE11) || settings.getDriverName()
                                    .equals(IE10)) ? IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                                    restartDriverCount)) {
                quitWebDriver();
            }
        }
    }

    private void restartDriverIfNewPlatformWasRequested() {
        //for set platform from xml parameters
        if (getWebDriver() != null && !getParameterPlatformName().equals("platform") && !getParameterPlatformName().equals(getPlatform())) {
            quitWebDriver();
        }
    }

    private void restartDriverIfNewBrowserTypeWasRequested() {
        //for set browser from xml parameters
        if (getWebDriver() != null && !getParameterBrowserName().equals("browser") && !getParameterBrowserName().equals(getDriverName())) {
            quitWebDriver();
        }
    }

    @NotNull
    private FramesTransparentWebDriver createDriver(Settings settings, Configuration configuration) {
        sleepForSafari(settings);
        TeasyDriver teasyDriver = new TeasyDriver(settings, configuration, alertCapability.get());
        FramesTransparentWebDriver driver = new FramesTransparentWebDriver(teasyDriver.init());
        alertCapability.set(UnexpectedAlertBehaviour.ACCEPT);
        sleepForSafari(settings);
        return driver;
    }

    private void sleepForSafari(Settings settings) {
        if (settings.getDriverName().equals(SAFARI)) {
            TestUtils.waitForSomeTime(5000, "Wait for create safari driver");
        }
    }

    private void lastTryToCreateDriver(TestContext context, Throwable t) {
        LOGGER.error("*****" + t.getClass().toString() + " occurred when initializing webdriver***** -- ERROR -- " + t.getMessage());
        //TODO VE remove this sleep when the issue become clear
        if (driverRestartCount.get() < 5) {
            TestUtils.waitForSomeTime(5000, "Wait for retry create driver");
            new Report("*****Try to wrap driver, count - " + driverRestartCount.get() + " *****").jenkins();
            prepareTestInstance(context);
        } else {
            throw new WebDriverException("*****Unable to wrap driver after " + driverRestartCount.get() + " attempts!***** " + t.getMessage(), t);
        }
    }

    private void setCustomElementFactory(Configuration configuration) {
        if (configuration.getCustomElementFactoryClass() != null) {
            SeleniumHolder.setCustomElementFactoryClass(configuration.getCustomElementFactoryClass().getName());
        }
    }

    private void setMobileParams(FramesTransparentWebDriver driver) {
        AndroidDriver androidDriver = castToAndroidDriver(driver);
        IOSDriver iosDriver = castToIOSDriver(driver);
        if (androidDriver != null || iosDriver != null) {
            SeleniumHolder.setAppiumDriver((AppiumDriver) castToWebDriverDecorator(driver));
            SeleniumHolder.setAndroidDriver(androidDriver);
            SeleniumHolder.setIOSDriver(iosDriver);
        }
    }

    private void setGridParams(Settings settings, boolean isRunWithGrid, FramesTransparentWebDriver driver) {
        try {
            SessionId sessionId = ((RemoteWebDriver) driver.getDriver()).getSessionId();
            SeleniumHolder.setSessionId(sessionId);
            String nodeIp = isRunWithGrid ? getNodeIpBySessionId(sessionId, settings.getGridHubUrl()) : InetAddress.getLocalHost().getHostAddress();
            SeleniumHolder.setNodeIp(nodeIp);
        } catch (Throwable ignored) {
            new Report("*****Throwable occurs when set node id*****", ignored).everywhere();
        }
    }

    @Override
    public void beforeTestMethod(TestContext context) {
        setAlertCapability(context);

        if (getWebDriver() != null && !alertCapability.get().equals(currentAlertCapability.get())) {
            quitWebDriver();
        }

        //TODO NT. Need to clarify it's right place or not.
        currentAlertCapability.set(alertCapability.get());

        if (getWebDriver() == null) {
            prepareTestInstance(context);
        }
    }

    private void setAlertCapability(TestContext context) {
        Method testMethod = context.getTestMethod();
        if (testMethod != null) {
            UnexpectedAlertCapability capability = testMethod.getAnnotation(UnexpectedAlertCapability.class);
            if (capability != null) {
                alertCapability.set(capability.capability());
            }
        }
    }

    /**
     * Checks whether browser is dead. Used to catch
     * situations like "Error communicating with the remote browser. It may have died." exceptions
     *
     * @return true if browser is dead
     */
    private static boolean isBrowserDead() {
        try {
            if (((FramesTransparentWebDriver) getWebDriver()).getWrappedDriver() instanceof AppiumDriver) {
                getWebDriver().getPageSource();
            } else {
                getWebDriver().getCurrentUrl();
            }
            return false;
        } catch (Throwable t) {
            new Report("*****BROWSER IS DEAD ERROR***** ", t).everywhere();
            return true;
        }
    }

    private void quitWebDriver() {
        count.set(1);
        try {
            getWebDriver().quit();
        } catch (Throwable t) {
            new Report("*****ERROR***** TRYING TO QUIT DRIVER ", t).everywhere();
        }
        SeleniumHolder.setWebDriver(null);
    }

    private <T> T getBean(final TestContext context, Class<T> requiredType) {
        return context.getApplicationContext().getBean(requiredType);
    }

    private AndroidDriver castToAndroidDriver(WebDriver driver) {
        WebDriver castToWebDriverDecorator = castToWebDriverDecorator(driver);
        if (castToWebDriverDecorator instanceof AndroidDriver) {
            return (AndroidDriver) castToWebDriverDecorator;
        } else {
            return null;
        }
    }

    private IOSDriver castToIOSDriver(WebDriver driver) {
        WebDriver castToWebDriverDecorator = castToWebDriverDecorator(driver);
        if (castToWebDriverDecorator instanceof IOSDriver) {
            return (IOSDriver) castToWebDriverDecorator;
        } else {
            return null;
        }
    }

    private WebDriver castToWebDriverDecorator(WebDriver driver) {
        return ((WebDriverDecorator) driver).getWrappedDriver();
    }

    private void addShutdownHook(final WebDriver driver) {
        Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
    }

    /**
     * Gets ip address of a node that is currently used by a test
     *
     * @param sessionId  id of a web driver session
     * @param gridHubUrl url of grid hub
     * @return ip of a taken node
     */
    private static String getNodeIpBySessionId(SessionId sessionId, String gridHubUrl) {
        String[] gridHubIpAndPort = gridHubUrl.split("//")[1].split(":");
        String gridIp = gridHubIpAndPort[0];
        int gridPort = Integer.parseInt(gridHubIpAndPort[1].split("/")[0]);
        try {
            Connection.Response execute = Jsoup
                    .connect("http://" + gridIp + ":" + gridPort + "/grid/api/")
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.GET)
                    .execute();

            if (execute.statusCode() != HttpStatus.SC_OK) {
                return gridIp;
            }

            String json = Jsoup
                    .connect("http://" + gridIp + ":" + gridPort + "/grid/api/testsession?session=" + sessionId)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true)
                    .method(Connection.Method.POST)
                    .execute()
                    .parse()
                    .text();

            return new JSONObject(json).get("proxyId").toString().split("//")[1].split(":")[0];
        } catch (JSONException e) {
            new Report("JSONException in getNodeIpBySessionId", e).jenkins();
        } catch (IOException e) {
            new Report("IOException in getNodeIpBySessionId", e).jenkins();
        }
        throw new GridConnectException("Cannot get node id by session from grid api.");
    }
}
