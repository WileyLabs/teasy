package com.wiley.driver;

import com.wiley.config.Configuration;
import com.wiley.driver.factory.TeasyDriver;
import com.wiley.driver.frames.FramesTransparentWebDriver;
import com.wiley.driver.frames.WebDriverDecorator;
import com.wiley.holders.DriverHolder;
import com.wiley.holders.TestParamsHolder;
import com.wiley.utils.Report;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.net.InetAddress;
import java.net.URL;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * User: ntyukavkin
 * Date: 12.04.2018
 * Time: 17:22
 */
public class WebDriverFactory {

    private static final int START_COUNT = 0;
    private static final ThreadLocal<Integer> tryToCreateDriverCount = ThreadLocal.withInitial(() -> START_COUNT);
    private static final ThreadLocal<Integer> restartDriverAfterNumberOfTests = ThreadLocal.withInitial(() -> START_COUNT);

    public static void initDriver() {
        restartDriverAfterNumberOfTests.set(restartDriverAfterNumberOfTests.get() + 1);
        if (getDriver() != null && (isBrowserDead() || isNeedToRestartDriver())) {
            quitWebDriver();
        }
        if (getDriver() == null) {
            try {
                FramesTransparentWebDriver driver = createDriver();

                addShutdownHook(driver);

                setGridParams(driver);
                setDriverParams(driver);
                setMobileParams(driver);
            } catch (Throwable t) {
                lastTryToCreateDriver(t);
            }
        }
    }

    private static FramesTransparentWebDriver createDriver() {
        TeasyDriver teasyDriver = new TeasyDriver();
        return new FramesTransparentWebDriver(teasyDriver.init());
    }

    private static void lastTryToCreateDriver(Throwable t) {
        if (tryToCreateDriverCount.get() < Configuration.tryToStartDriverCount) {
            tryToCreateDriverCount.set(tryToCreateDriverCount.get() + 1);
            initDriver();
        } else {
            Integer passCount = tryToCreateDriverCount.get();
            tryToCreateDriverCount.set(START_COUNT);
            throw new WebDriverException("Unable to init driver after " + passCount + " attempts! Cause: " + t.getMessage(), t);
        }
    }

    private static void setDriverParams(FramesTransparentWebDriver driver) {
        DriverHolder.setDriver(driver);
    }

    private static void setMobileParams(FramesTransparentWebDriver driver) {
        AndroidDriver androidDriver = castToAndroidDriver(driver);
        IOSDriver iosDriver = castToIOSDriver(driver);
        if (androidDriver != null || iosDriver != null) {
            DriverHolder.setAppiumDriver((AppiumDriver) castToWebDriverDecorator(driver));
            DriverHolder.setAndroidDriver(androidDriver);
            DriverHolder.setIOSDriver(iosDriver);
        }
    }

    private static void setGridParams(FramesTransparentWebDriver driver) {
        try {
            SessionId sessionId = ((RemoteWebDriver) driver.getDriver()).getSessionId();
            TestParamsHolder.setSessionId(sessionId);
            String nodeIp = Configuration.runWithGrid ? new GridApi(new URL(Configuration.gridHubUrl), sessionId).getNodeIp() : InetAddress.getLocalHost().getHostAddress();
            TestParamsHolder.setNodeIP(nodeIp);
        } catch (Throwable ignored) {
            Report.jenkins("Throwable occurs when set node id.", ignored);
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
            if (((FramesTransparentWebDriver) getDriver()).getWrappedDriver() instanceof AppiumDriver) {
                getDriver().getPageSource();
            } else {
                getDriver().getCurrentUrl();
            }
            return false;
        } catch (Throwable t) {
            Report.jenkins("*****BROWSER IS DEAD ERROR***** ", t);
            return true;
        }
    }

    private static boolean isNeedToRestartDriver() {
        return restartDriverAfterNumberOfTests.get() > Configuration.restartCount;
    }

    private static void quitWebDriver() {
        restartDriverAfterNumberOfTests.set(START_COUNT);
        try {
            getDriver().quit();
        } catch (Throwable t) {
            Report.jenkins("*****TRYING TO QUIT DRIVER***** ", t);
        }
        DriverHolder.setDriver(null);
    }

    private static AndroidDriver castToAndroidDriver(WebDriver driver) {
        WebDriver castToWebDriverDecorator = castToWebDriverDecorator(driver);
        if (castToWebDriverDecorator instanceof AndroidDriver) {
            return (AndroidDriver) castToWebDriverDecorator;
        } else {
            return null;
        }
    }

    private static IOSDriver castToIOSDriver(WebDriver driver) {
        WebDriver castToWebDriverDecorator = castToWebDriverDecorator(driver);
        if (castToWebDriverDecorator instanceof IOSDriver) {
            return (IOSDriver) castToWebDriverDecorator;
        } else {
            return null;
        }
    }

    private static WebDriver castToWebDriverDecorator(WebDriver driver) {
        return ((WebDriverDecorator) driver).getWrappedDriver();
    }

    private static void addShutdownHook(final WebDriver driver) {
        Runtime.getRuntime().addShutdownHook(new Thread(driver::quit));
    }
}
