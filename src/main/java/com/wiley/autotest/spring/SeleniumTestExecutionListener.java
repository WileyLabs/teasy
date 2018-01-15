package com.wiley.autotest.spring;

import com.wiley.autotest.annotations.BrowserMobProxy;
import com.wiley.autotest.annotations.FireBug;
import com.wiley.autotest.annotations.NeedRestartDriver;
import com.wiley.autotest.annotations.UnexpectedAlertCapability;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.PageLoadingValidator;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.driver.WebDriverDecorator;
import com.wiley.autotest.services.Configuration;
import com.wiley.autotest.spring.testexecution.TeasyDriver;
import com.wiley.autotest.spring.testexecution.capabilities.FireFoxCaps;
import com.wiley.autotest.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

import static com.wiley.autotest.selenium.SeleniumHolder.*;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private static final String EXTENSIONS_FIREBUG_XPI_PATH = "/extensions/firebug.xpi";
    private static final String EXTENSIONS_NET_EXPORT_XPI_PATH = "/extensions/netExport.xpi";
    private static final String EXTENSIONS_FIRE_STARTER_XPI_PATH = "/extensions/fireStarter.xpi";
    private static final int ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 1;
    private static final String FIREFOX = "firefox";
    private static final String GECKO = "gecko";
    private static final String CHROME = "chrome";
    private static final String SAFARI = "safari";
    private static final String SAFARI_TECHNOLOGY_PREVIEW = "safariTechnologyPreview";
    private static final String IE = "ie";
    private static final String EDGE = "edge";
    private static final String IE11 = "ie11";
    private static final String IE10 = "ie10";
    private static final String IE9 = "ie9";
    private static final String IOS = "ios";
    private static final String ANDROID = "android";
    private static final String WINDOWS = "windows";
    private static final String LINUX = "LINUX";
    private static final String MAC = "mac";
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumTestExecutionListener.class);
    private static final Object SYNC_OBJECT = new Object();
    private static ProxyServer proxyServer;
    private static ThreadLocal<Integer> count = ThreadLocal.withInitial(() -> -1);
    private static ThreadLocal<Integer> driverRestartCount = ThreadLocal.withInitial(() -> 0);
    private static ThreadLocal<Boolean> useProxy = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<UnexpectedAlertBehaviour> alertCapability = ThreadLocal.withInitial(() -> UnexpectedAlertBehaviour.ACCEPT);
    private static ThreadLocal<UnexpectedAlertBehaviour> currentAlertCapability = ThreadLocal.withInitial(() -> UnexpectedAlertBehaviour.ACCEPT);
    private static ThreadLocal<Boolean> isNeedToRestart = ThreadLocal.withInitial(() -> false);
    private static ThreadLocal<Boolean> isUseFireBug = ThreadLocal.withInitial(() -> false);
    private String[] activeProfiles;
    private static final String STABLE_IE_DRIVER_VERSION = "3.4.0";

    @Override
    public void prepareTestInstance(final TestContext context) throws Exception {
        if (activeProfiles == null || activeProfiles.length == 0) {
            activeProfiles = context.getApplicationContext().getEnvironment().getActiveProfiles();
            SeleniumHolder.setActiveProfilesList(Arrays.asList(activeProfiles));
        }

        final Settings settings = getBean(context, Settings.class);
        final Configuration configuration = getBean(context, Configuration.class);
        System.setProperty("http.maxConnections", "1000000");
        System.setProperty("http.keepAlive", "false");
        count.set(count.get() + 1);
        driverRestartCount.set(driverRestartCount.get() + 1);




        //TODO NT - confirm if it the right place to call this method?
        setCurrentAlertCapability();



        boolean isRunWithGrid = settings.isRunTestsWithGrid();
        Integer restartDriverCount = settings.getRestartDriverCount() != null ? settings.getRestartDriverCount() : 0;

        if (restartDriverCount > 0) {
            if (count.get() > (settings.getPlatform().equals(ANDROID) ? ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                    settings.getDriverName().equals(SAFARI) ? SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                            (settings.getDriverName().equals(IE) || settings.getDriverName().equals(IE9) || settings.getDriverName()
                                    .equals(IE10)) ? IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                                    restartDriverCount)) {
                quitWebDriver();
            }
        }

        //for set browser from xml parameters
        if (getWebDriver() != null && !getParameterBrowserName().equals("browser") && !getParameterBrowserName().equals(getDriverName())) {
            quitWebDriver();
        }

        //for set platform from xml parameters
        if (getWebDriver() != null && !getParameterPlatformName().equals("platform") && !getParameterPlatformName().equals(getPlatform())) {
            quitWebDriver();
        }

        if (getWebDriver() == null || isBrowserDead()) {
            FramesTransparentWebDriver driver = null;
            try {
                //TODO VE this should be replaced with the better solution
                if (settings.getDriverName().equals(SAFARI)) {
                    TestUtils.waitForSomeTime(5000, "Wait for create safari driver");
                }

                driver = new FramesTransparentWebDriver(initWebDriver(settings, configuration));

                alertCapability.set(UnexpectedAlertBehaviour.ACCEPT);

                isNeedToRestart.set(false);

                isUseFireBug.set(false);

                //TODO VE this should be replaced with the better solution
                if (settings.getDriverName().equals(SAFARI)) {
                    TestUtils.waitForSomeTime(5000, "Wait for create safari driver");
                }
            } catch (Throwable t) {
                LOGGER.error("*****" + t.getClass().toString() + " occurred when initializing webdriver***** -- ERROR -- " + t.getMessage());
                //TODO VE remove this sleep when the issue become clear
                if (driverRestartCount.get() < 5) {
                    TestUtils.waitForSomeTime(5000, "Wait for retry create driver");
                    LOGGER.error("*****Try to wrap driver, count - " + driverRestartCount.get() + " *****");
                    prepareTestInstance(context);
                } else {
                    throw new WebDriverException("*****Unable to wrap driver after " + driverRestartCount.get() + " attempts!***** " + t.getMessage(), t);
                }
            }

            try {
                SessionId sessionId = ((RemoteWebDriver) driver.getDriver()).getSessionId();
                SeleniumHolder.setSessionId(sessionId);
                String nodeIp = isRunWithGrid ? getNodeIpBySessionId(sessionId, settings.getGridHubUrl()) : InetAddress.getLocalHost().getHostAddress();
                SeleniumHolder.setNodeIp(nodeIp);
            } catch (Throwable ignored) {
                LOGGER.error("*****Throwable occurs when set node id*****", ignored.getMessage());
            }

            addShutdownHook(driver);
            SeleniumHolder.setWebDriver(driver);
            SeleniumHolder.setTimeoutInSeconds(settings.getTimeout());

            AndroidDriver androidDriver = castToAndroidDriver(driver);
            IOSDriver iosDriver = castToIOSDriver(driver);
            if (androidDriver != null || iosDriver != null) {
                SeleniumHolder.setAppiumDriver((AppiumDriver) castToWebDriverDecorator(driver));
                SeleniumHolder.setAndroidDriver(androidDriver);
                SeleniumHolder.setIOSDriver(iosDriver);
            }

            if (configuration.getCustomElementFactoryClass() != null) {
                SeleniumHolder.setCustomElementFactoryClass(configuration.getCustomElementFactoryClass().getName());
            }
        }
    }

    @Override
    public void beforeTestMethod(TestContext context) throws Exception {
        setUseProxy(context);
        setAlertCapability(context);
        setNeedToRestart(context);
        setUseFireBug(context);

        if (getWebDriver() != null && useProxy.get()) {
            quitWebDriver();
        }

        if (getWebDriver() != null && !alertCapability.get().equals(currentAlertCapability.get())) {
            quitWebDriver();
        }

        //for force restart driver before start test
        if (getWebDriver() != null && isNeedToRestart.get()) {
            quitWebDriver();
        }

        if (getWebDriver() != null && isUseFireBug.get()) {
            quitWebDriver();
        }

        if (getWebDriver() == null) {
            prepareTestInstance(context);
        }
    }

    private void setUseProxy(TestContext context) {
        Method testMethod = context.getTestMethod();
        if (testMethod != null && testMethod.getAnnotation(BrowserMobProxy.class) != null) {
            useProxy.set(true);
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

    private void setNeedToRestart(TestContext context) {
        Method testMethod = context.getTestMethod();
        if (testMethod != null && testMethod.getAnnotation(NeedRestartDriver.class) != null) {
            isNeedToRestart.set(true);
        }
    }

    private void setUseFireBug(TestContext context) {
        Method testMethod = context.getTestMethod();
        if (testMethod != null && testMethod.getAnnotation(FireBug.class) != null) {
            isUseFireBug.set(true);
        }
    }

    /**
     * Checks whether browser is dead. Used to catch
     * situations like "Error communicating with the remote browser. It may have died." exceptions
     *
     * @return true if browser is dead
     */
    public static boolean isBrowserDead() {
        try {
            if (((FramesTransparentWebDriver) getWebDriver()).getWrappedDriver() instanceof AppiumDriver) {
                getWebDriver().getPageSource();
            } else {
                getWebDriver().getCurrentUrl();
            }
            return false;
        } catch (Throwable t) {
            LOGGER.error("*****BROWSER IS DEAD ERROR***** ", t);
            return true;
        }
    }

    private void quitWebDriver() {
        count.set(1);
        try {
            getWebDriver().quit();
        } catch (Throwable t) {
            LOGGER.error("*****ERROR***** TRYING TO QUIT DRIVER " + t.getMessage());
        }
        SeleniumHolder.setWebDriver(null);
    }

    private boolean isPortAvailable(int port) {
        try {
            ServerSocket srv = new ServerSocket(port);
            srv.close();
            return true;
        } catch (IOException e) {
            LOGGER.error("IOException occurs", e);
            return false;
        }
    }

    //get random port from interval 6000-6100
    //It's need for run tests on VMs
    private int getRandom() {
        return 6000 + (int) (Math.random() * 100);
    }

    private int getRandomAvailablePort() {
        int random = getRandom();
        for (int i = random; i < 6100; i++) {
            if (isPortAvailable(i)) {
                return i;
            }
        }
        return getRandomAvailablePort();
    }

    private ProxyServer getServer() {
        synchronized (SYNC_OBJECT) {
            if (proxyServer == null) {
                try {
                    proxyServer = new ProxyServer(getRandomAvailablePort());
                    proxyServer.start();
                    return proxyServer;
                } catch (Exception e) {
                    LOGGER.error("Exception occurs", e);
                }
                return null;
            } else {
                return proxyServer;
            }
        }
    }

    private Proxy getProxy() {
        ProxyServer server = getServer();

        server.setCaptureContent(true);
        server.setCaptureHeaders(true);

        SeleniumHolder.setProxyServer(server);

        Proxy proxy = new Proxy();

        try {
            proxy = server.seleniumProxy();
            String localIp = InetAddress.getLocalHost().getHostAddress();
            String proxyStr = String.format("%s:%d", localIp, server.getPort());
            proxy.setHttpProxy(proxyStr);
            proxy.setSslProxy("trustAllSSLCertificates");
            proxy.setFtpProxy(proxyStr);
            SeleniumHolder.setProxy(proxy);
        } catch (UnknownHostException e) {
            LOGGER.error("UnknownHostException occurs", e);
        }
        return proxy;
    }

    private void setProxy(DesiredCapabilities capabilities) {
        if (useProxy.get()) {
            capabilities.setCapability(CapabilityType.PROXY, getProxy());
        }
    }

    private WebDriver initWebDriver(Settings settings, Configuration configuration) throws MalformedURLException {
        return new TeasyDriver(settings, configuration, alertCapability.get()).init();
    }


    private void setCurrentAlertCapability() {
        currentAlertCapability.set(alertCapability.get());
    }

    private void setLoggingPrefs(DesiredCapabilities capabilities) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }

    private FirefoxProfile getFirefoxProfile(Settings settings) {
        final FirefoxProfile profile;
        if (StringUtils.isBlank(settings.getBrowserProfileName())) {
            profile = new FirefoxProfile();
        } else {
            profile = new ProfilesIni().getProfile(settings.getBrowserProfileName());
        }
        profile.setPreference("dom.max_chrome_script_run_time", 999);
        profile.setPreference("dom.max_script_run_time", 999);

        //Disable plugin container, it should fix problem with 'FF plugin-container has stopped working'.
        //But i'm not sure about this on 100%
        profile.setPreference("dom.ipc.plugins.enabled", false);
        profile.setPreference("dom.ipc.plugins.enabled.npctrl.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.npqtplugin.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.npswf32.dll", false);
        profile.setPreference("dom.ipc.plugins.enabled.nptest.dll", false);
        profile.setPreference("dom.ipc.plugins.timeoutSecs", -1);
        //Add this to avoid JAVA plugin certificate warnings
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);
        //   profile.setPreference("security.enable_java", true);
        profile.setPreference("plugin.state.java", 2);

        //for disable  Advocacy/heartbeat in Firefox 37
        //http://selenium2.ru/news/131-rekomenduetsya-otklyuchit-advocacy-heartbeat-v-firefox-37.html
        profile.setPreference("browser.selfsupport.url", "");

        //for use with sso auth
        profile.setPreference("network.http.phishy-userpass-length", 255);
        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");

        if (isUseFireBug.get()) {
            //Using this way of adding extension because otherwise extensions could not be converted to Java File from selenium jar file.
            profile.addExtension(this.getClass(), EXTENSIONS_FIREBUG_XPI_PATH);
            profile.addExtension(this.getClass(), EXTENSIONS_NET_EXPORT_XPI_PATH);
            profile.addExtension(this.getClass(), EXTENSIONS_FIRE_STARTER_XPI_PATH);

            profile.setPreference("extensions.firebug.currentVersion", "2.0.11");
            profile.setPreference("extensions.firebug.DBG_NETEXPORT", false);
            profile.setPreference("extensions.firebug.onByDefault", true);
            profile.setPreference("extensions.firebug.defaultPanelName", "net");
            profile.setPreference("extensions.firebug.net.enableSites", true);
            profile.setPreference("extensions.firebug.net.persistent", true);
            profile.setPreference("extensions.firebug.netexport.alwaysEnableAutoExport", true);
            profile.setPreference("extensions.firebug.netexport.autoExportToFile", true);
            profile.setPreference("extensions.firebug.netexport.autoExportToServer", false);
            profile.setPreference("extensions.firebug.netexport.defaultLogDir", TestUtils.getTempDirectoryLocation());
            profile.setPreference("extensions.firebug.netexport.showPreview", false);
            profile.setPreference("extensions.firebug.netexport.sendToConfirmation", false);
            profile.setPreference("extensions.firebug.netexport.pageLoadedTimeout", 150000);
            profile.setPreference("extensions.firebug.netexport.Automation", true);
        }

        //for debug with mobile user agent
        //profile.setPreference("general.useragent.override", "Android");

        return profile;
    }

    private PageLoadingValidator getValidator(final TestContext context) {
        return context.getApplicationContext().getBean(PageLoadingValidator.class);
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
