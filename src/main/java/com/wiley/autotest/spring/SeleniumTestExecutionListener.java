package com.wiley.autotest.spring;

import com.wiley.autotest.annotations.BrowserMobProxy;
import com.wiley.autotest.annotations.FireBug;
import com.wiley.autotest.annotations.NeedRestartDriver;
import com.wiley.autotest.annotations.UnexpectedAlertCapability;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.PageLoadingValidator;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.driver.events.listeners.PageValidatorEventListener;
import com.wiley.autotest.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import net.lightbody.bmp.proxy.ProxyServer;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Arrays;

import static com.wiley.autotest.selenium.SeleniumHolder.*;

public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

    private static final String EXTENSIONS_FIREBUG_XPI_PATH = "/extensions/firebug.xpi";
    private static final String EXTENSIONS_NET_EXPORT_XPI_PATH = "/extensions/netExport.xpi";
    private static final String EXTENSIONS_FIRE_STARTER_XPI_PATH = "/extensions/fireStarter.xpi";
    private static final int ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 5;
    private static final int SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT = 1;
    private static final String FIREFOX = "firefox";
    private static final String CHROME = "chrome";
    private static final String SAFARI = "safari";
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
    private static ProxyServer proxyServer;
    private static final Object SYNC_OBJECT = new Object();

    private static ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    private static ThreadLocal<Integer> driverRestartCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    private static ThreadLocal<Boolean> useProxy = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private static ThreadLocal<UnexpectedAlertBehaviour> alertCapability = new ThreadLocal<UnexpectedAlertBehaviour>() {
        @Override
        protected UnexpectedAlertBehaviour initialValue() {
            return UnexpectedAlertBehaviour.ACCEPT;
        }
    };
    private static ThreadLocal<UnexpectedAlertBehaviour> currentAlertCapability = new ThreadLocal<UnexpectedAlertBehaviour>() {
        @Override
        protected UnexpectedAlertBehaviour initialValue() {
            return UnexpectedAlertBehaviour.ACCEPT;
        }
    };
    private static ThreadLocal<Boolean> isNeedToRestart = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    private static ThreadLocal<Boolean> isUseFireBug = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public void prepareTestInstance(final TestContext context) throws Exception {
        final Settings settings = getSeleniumSettings(context);
        String findInAllFramesProperty = settings.getProperty("find.elements.in.all.frames");
        boolean isFindElementsInAllFrames = false;
        if (findInAllFramesProperty != null && !findInAllFramesProperty.isEmpty()) {
            isFindElementsInAllFrames = Boolean.parseBoolean(findInAllFramesProperty);
        }
        System.setProperty("http.maxConnections", "1000000");
        System.setProperty("http.keepAlive", "false");
        count.set(count.get() + 1);
        driverRestartCount.set(driverRestartCount.get() + 1);

        boolean isFFDriver = settings.getDriverName().equals(FIREFOX);
        boolean isRunWithGrid = settings.isRunTestsWithGrid();
        Integer restartDriverCount = settings.getRestartDriverCount() != null ? settings.getRestartDriverCount() : 0;

        if (restartDriverCount > 0) {
            if (count.get() > (settings.getPlatform().equals(ANDROID) ? ANDROID_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                    settings.getDriverName().equals(SAFARI) ? SAFARI_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
                            (settings.getDriverName().equals(IE) || settings.getDriverName().equals(IE9) || settings.getDriverName().equals(IE10)) ? IE_WEB_DRIVER_NUMBER_OF_TESTS_LIMIT :
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
            EventFiringWebDriver driver = null;
            try {
                //TODO VE this should be replaced with the better solution
                if (settings.getDriverName().equals(SAFARI)) {
                    TestUtils.waitForSomeTime(5000, "Wait for create safari driver");
                }

                driver = new EventFiringWebDriver(new FramesTransparentWebDriver(initWebDriver(settings), isFindElementsInAllFrames));

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
                SessionId sessionId = ((RemoteWebDriver) ((FramesTransparentWebDriver) driver.getWrappedDriver()).getDriver()).getSessionId();
                SeleniumHolder.setSessionId(sessionId);
                String nodeIp = isRunWithGrid ? getNodeIpBySessionId(sessionId, settings.getGridHubUrl()) : InetAddress.getLocalHost().getHostAddress();
                SeleniumHolder.setNodeIp(nodeIp);
            } catch (Throwable ignored) {
                LOGGER.error("*****Throwable occurs when set node id*****", ignored.getMessage());
            }

            addShutdownHook(driver, isFFDriver, isRunWithGrid, SeleniumHolder.getNodeIp());
            SeleniumHolder.setWebDriver(driver);

            String classFromSettings = settings.getProperty("our.webelement.class");
            if (classFromSettings != null && !classFromSettings.isEmpty()) {
                SeleniumHolder.setOurWebElementClass(classFromSettings);
            } else {
                SeleniumHolder.setOurWebElementClass("com.wiley.autotest.selenium.elements.upgrade.OurWebElement");
            }

            if (false) {
                driver.register(new PageValidatorEventListener(getValidator(context)));
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

        prepareTestInstance(context);
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

    private void quitWebDriver() {
        count.set(1);
        try {
            String nodeIp = SeleniumHolder.getNodeIp();
            getWebDriver().quit();
            killPluginContainer(nodeIp);
        } catch (Throwable t) {
            LOGGER.error("*****ERROR***** TRYING TO QUIT DRIVER " + t.getMessage());
        }
        SeleniumHolder.setWebDriver(null);
    }

    /**
     * Checks whether browser is dead. Used to catch
     * situations like "Error communicating with the remote browser. It may have died." exceptions
     *
     * @return true if browser is dead
     */
    public static boolean isBrowserDead() {
        try {
            getWebDriver().getCurrentUrl();
            return false;
        } catch (Throwable t) {
            LOGGER.error("*****BROWSER IS DEAD ERROR***** " + t.getMessage());
            return true;
        }
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

    private WebDriver initWebDriver(Settings settings) throws MalformedURLException {
        String browserName;
        //for set browser from xml parameters
        if (!getParameterBrowserName().equals("browser")) {
            browserName = getParameterBrowserName();
            setParameterBrowserName("browser");
        } else if (getDriverName() != null) {
            browserName = getDriverName();
        } else {
            browserName = settings.getDriverName();
        }
        if (settings.isRunTestsWithGrid()) {
            String platformName;
            if (!getParameterPlatformName().equals("platform")) {
                platformName = getParameterPlatformName();
                setParameterPlatformName("platform");
            } else if (getPlatform() != null) {
                platformName = getPlatform();
            } else {
                platformName = settings.getPlatform();
            }

            DesiredCapabilities desiredCapabilities;
            if (platformName.equals(WINDOWS)) {
                if (StringUtils.equalsIgnoreCase(browserName, CHROME)) {
                    desiredCapabilities = getChromeDesiredCapabilities();
                    SeleniumHolder.setDriverName(CHROME);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, IE)) {
                    desiredCapabilities = getIEDesiredCapabilities();
                    SeleniumHolder.setDriverName(IE);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, IE11)) {
                    desiredCapabilities = getIEDesiredCapabilities("11");
                    SeleniumHolder.setDriverName(IE11);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, IE10)) {
                    desiredCapabilities = getIEDesiredCapabilities("10");
                    SeleniumHolder.setDriverName(IE10);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, IE9)) {
                    desiredCapabilities = getIEDesiredCapabilities("9");
                    SeleniumHolder.setDriverName(IE9);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, EDGE)) {
                    desiredCapabilities = getEdgeDesiredCapabilities();
                    SeleniumHolder.setDriverName(EDGE);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else if (StringUtils.equalsIgnoreCase(browserName, FIREFOX)) {
                    desiredCapabilities = getFireFoxDesiredCapabilities(settings);
                    SeleniumHolder.setDriverName(FIREFOX);
                    SeleniumHolder.setPlatform(WINDOWS);
                } else {
                    throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
                }
            } else if (StringUtils.equalsIgnoreCase(platformName, ANDROID)) {
                if (StringUtils.equalsIgnoreCase(browserName, CHROME)) {
                    desiredCapabilities = getAndroidChromeDesiredCapabilities();
                    SeleniumHolder.setDriverName(CHROME);
                    SeleniumHolder.setPlatform(ANDROID);
                    AppiumDriver remoteWebDriver = new AndroidDriver(new URL(settings.getGridHubUrl()), desiredCapabilities);
                    remoteWebDriver.setFileDetector(new LocalFileDetector());
                    return remoteWebDriver;
                } else {
                    throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
                }
            } else if (StringUtils.equalsIgnoreCase(platformName, MAC)) {
                if (StringUtils.equalsIgnoreCase(browserName, SAFARI)) {
                    desiredCapabilities = getSafariDesiredCapabilities();
                    SeleniumHolder.setDriverName(SAFARI);
                    SeleniumHolder.setPlatform(MAC);
                } else {
                    throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
                }
            } else if (StringUtils.equalsIgnoreCase(platformName, IOS)) {
                if (StringUtils.equalsIgnoreCase(browserName, SAFARI)) {
                    desiredCapabilities = getIOSSafariDesiredCapabilities();
                    SeleniumHolder.setDriverName(SAFARI);
                    SeleniumHolder.setPlatform(IOS);
                } else if (StringUtils.equalsIgnoreCase(browserName, CHROME)) {
                    desiredCapabilities = getIOSChromeDesiredCapabilities();
                    SeleniumHolder.setDriverName(CHROME);
                    SeleniumHolder.setPlatform(IOS);
                } else {
                    throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
                }
            } else if (platformName.equalsIgnoreCase(LINUX)) {
                if (StringUtils.equalsIgnoreCase(browserName, CHROME)) {
                    desiredCapabilities = getChromeDesiredCapabilities();
                    SeleniumHolder.setDriverName(CHROME);
                    SeleniumHolder.setPlatform(LINUX);
                } else if (StringUtils.equalsIgnoreCase(browserName, FIREFOX)) {
                    desiredCapabilities = getFireFoxDesiredCapabilities(settings);
                    SeleniumHolder.setDriverName(FIREFOX);
                    SeleniumHolder.setPlatform(LINUX);
                } else {
                    throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
                }
            } else {
                throw new RuntimeException("Not supported platform: " + platformName);
            }

            RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(settings.getGridHubUrl()), desiredCapabilities);
            remoteWebDriver.setFileDetector(new LocalFileDetector());
            return remoteWebDriver;
        } else {
            if (StringUtils.equalsIgnoreCase(browserName, FIREFOX)) {
                SeleniumHolder.setDriverName(FIREFOX);
                SeleniumHolder.setPlatform(WINDOWS);
                return firefox(settings);
            } else if (StringUtils.equalsIgnoreCase(browserName, CHROME)) {
                SeleniumHolder.setDriverName(CHROME);
                SeleniumHolder.setPlatform(WINDOWS);
                return chrome();
            } else if (StringUtils.equalsIgnoreCase(browserName, IE)) {
                SeleniumHolder.setDriverName(IE);
                SeleniumHolder.setPlatform(WINDOWS);
                return explorer();
            } else if (StringUtils.equalsIgnoreCase(browserName, EDGE)) {
                SeleniumHolder.setDriverName(EDGE);
                SeleniumHolder.setPlatform(WINDOWS);
                return edge();
            } else if (StringUtils.equalsIgnoreCase(browserName, IE10)) {
                SeleniumHolder.setDriverName(IE10);
                SeleniumHolder.setPlatform(WINDOWS);
                return explorer("10");
            } else if (StringUtils.equalsIgnoreCase(browserName, IE9)) {
                SeleniumHolder.setDriverName(IE9);
                SeleniumHolder.setPlatform(WINDOWS);
                return explorer("9");
            } else if (StringUtils.equalsIgnoreCase(browserName, SAFARI)) {
                SeleniumHolder.setDriverName(SAFARI);
                SeleniumHolder.setPlatform(MAC);
                return safari();
            } else {
                throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + settings.getPlatform());
            }
        }
    }

    private DesiredCapabilities getAndroidChromeDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Android");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("newCommandTimeout", "900");
        capabilities.setPlatform(Platform.ANDROID);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, CHROME);
        return capabilities;
    }

    private DesiredCapabilities getIOSSafariDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "Wiley_SQA_iPad_AM");
        capabilities.setCapability("udid", "17ea71ec1700dde72a474b4e215d5aecd6172acd");
        capabilities.setCapability("app", "Safari");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "8.3");
        capabilities.setCapability("newCommandTimeout", "900");
        return capabilities;
    }

    private DesiredCapabilities getIOSChromeDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", "iPad");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "7.1");
        capabilities.setCapability("browserName", CHROME);
        return capabilities;
    }

    private WebDriver firefox(final Settings settings) {
        return new FirefoxDriver(getFireFoxDesiredCapabilities(settings));
    }

    private DesiredCapabilities getFireFoxDesiredCapabilities(Settings settings) {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        setAlertBehaviorCapabilities(capabilities);
        capabilities.setCapability(FirefoxDriver.PROFILE, getFirefoxProfile(settings));
        capabilities.setPlatform(Platform.WINDOWS);
        setProxy(capabilities);
        return capabilities;
    }

    private DesiredCapabilities getIEDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
        capabilities.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
        capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        capabilities.setCapability(CapabilityType.SUPPORTS_ALERTS, true);
        capabilities.setPlatform(Platform.WINDOWS);
        setAlertBehaviorCapabilities(capabilities);
        setProxy(capabilities);
        //Found that setting this capability could increase IE tests speed. Should be checked.
        //   capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
        return capabilities;
    }

    private DesiredCapabilities getIEDesiredCapabilities(String version) {
        DesiredCapabilities capabilities = getIEDesiredCapabilities();
        capabilities.setVersion(version);
        return capabilities;
    }

    private DesiredCapabilities getEdgeDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.edge();
        capabilities.setPlatform(Platform.WINDOWS);
        setAlertBehaviorCapabilities(capabilities);
        setProxy(capabilities);
        return capabilities;
    }

    private DesiredCapabilities getChromeDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        //Added to avoid yellow warning in chrome 35
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("test-type");
        //For view pdf in chrome
        options.setExperimentalOption("excludeSwitches", Arrays.asList("test-type", "ignore-certificate-errors"));
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        capabilities.setPlatform(Platform.WINDOWS);
        setAlertBehaviorCapabilities(capabilities);
        setProxy(capabilities);
        return capabilities;
    }

    private DesiredCapabilities getSafariDesiredCapabilities() {
        DesiredCapabilities capabilities = DesiredCapabilities.safari();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseCleanSession(true);
        capabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
        capabilities.setPlatform(Platform.MAC);
        setProxy(capabilities);
        return capabilities;
    }

    private void setProxy(DesiredCapabilities capabilities) {
        if (useProxy.get()) {
            capabilities.setCapability(CapabilityType.PROXY, getProxy());
        }
    }

    private void setAlertBehaviorCapabilities(DesiredCapabilities capabilities) {
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, alertCapability.get());
        currentAlertCapability.set(alertCapability.get());
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
        //   profile.setPreference("security.enable_java", true);
        profile.setPreference("plugin.state.java", 2);

        //for disable  Advocacy/heartbeat in Firefox 37
        //http://selenium2.ru/news/131-rekomenduetsya-otklyuchit-advocacy-heartbeat-v-firefox-37.html
        profile.setPreference("browser.selfsupport.url", "");

        //for use with sso auth
        profile.setPreference("network.http.phishy-userpass-length", 255);
        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "http://,https://");

        if (isUseFireBug.get()) {
            try {
                //Using this way of adding extension because otherwise extensions could not be converted to Java File from selenium jar file.
                profile.addExtension(this.getClass(), EXTENSIONS_FIREBUG_XPI_PATH);
                profile.addExtension(this.getClass(), EXTENSIONS_NET_EXPORT_XPI_PATH);
                profile.addExtension(this.getClass(), EXTENSIONS_FIRE_STARTER_XPI_PATH);
            } catch (IOException e) {
                throw new RuntimeException("Could not load required extensions, did you download them to the above location? ", e);
            }
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

    private WebDriver chrome() {
        ChromeDriverManager.getInstance().setup();
        return new ChromeDriver(getChromeDesiredCapabilities());
    }

    private WebDriver explorer() {
        InternetExplorerDriverManager.getInstance().setup();
        return new InternetExplorerDriver(getIEDesiredCapabilities());
    }

    private WebDriver explorer(String version) {
        InternetExplorerDriverManager.getInstance().setup();
        return new InternetExplorerDriver(getIEDesiredCapabilities(version));
    }

    private WebDriver edge() {
        EdgeDriverManager.getInstance().setup();
        return new EdgeDriver(getEdgeDesiredCapabilities());
    }

    private WebDriver safari() {
        return new SafariDriver();
    }

    private PageLoadingValidator getValidator(final TestContext context) {
        return context.getApplicationContext().getBean(PageLoadingValidator.class);
    }

    private Settings getSeleniumSettings(final TestContext context) {
        return context.getApplicationContext().getBean(Settings.class);
    }

    private void addShutdownHook(final WebDriver driver, final boolean isFFDriver, final boolean isRunWithGrid, final String nodeIp) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (isFFDriver) {
                    if (isRunWithGrid) {
                        killPluginContainer(nodeIp);
                    }
                    killOnLocalNode();
                }
                driver.quit();
            }
        });
    }

    /**
     * https://code.google.com/p/selenium/issues/detail?id=7506
     * <p>
     * Special methods to kill plugin-container for FF only.
     * This error appears when test has flash and browser window was closed.
     */
    private void killPluginContainer(String nodeIp) {
        sendKillPostRequestOnNode(nodeIp);
    }

    private void sendKillPostRequestOnNode(String nodeUrl) {
        try {
            if (InetAddress.getLocalHost().getHostAddress().equals(nodeUrl)) {
                return;
            }
            HttpClient httpclient = new DefaultHttpClient();
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost(nodeUrl)
                    .setPort(6099)
                    .setPath("/service/actions/cmd")
                    .addParameter("commands", "--taskkill /F /IM plugin-container.exe --taskkill /F /IM WerFault.exe")
                    .build();
            HttpPost httppost = new HttpPost(uri);
            HttpResponse response = httpclient.execute(httppost);
            LOGGER.info("Kill plugin container on '" + nodeUrl + "' with status - " + response.getStatusLine().getStatusCode());
        } catch (URISyntaxException e) {
            LOGGER.error("*****URISyntaxException occurs when kill plugin container. NodeUrl - " + nodeUrl + "*****", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("*****ClientProtocolException occurs when kill plugin container. NodeUrl - " + nodeUrl + "*****", e);
        } catch (IOException e) {
            LOGGER.error("*****IOException occurs when kill plugin container. NodeUrl - " + nodeUrl + "*****", e);
        }
    }

    private void killOnLocalNode() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM plugin-container.exe");
            Runtime.getRuntime().exec("taskkill /F /IM WerFault.exe");
        } catch (IOException e) {
            LOGGER.error("*****IOException occurs when kill plugin container.*****", e);
        }
    }

    private static String getNodeIpBySessionId(SessionId sessionId, String gridHubUrl) {
        String[] gridHubIpAndPort = gridHubUrl.split("//")[1].split(":");
        String gridIp = gridHubIpAndPort[0];
        int gridPort = Integer.parseInt(gridHubIpAndPort[1].split("/")[0]);
        try {
            HttpHost host = new HttpHost(gridIp, gridPort);
            HttpClient client = HttpClientBuilder.create().build();
            URL testSessionApi = new URL("http://" + gridIp + ":" + gridPort + "/grid/api/testsession?session=" + sessionId);
            BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", testSessionApi.toExternalForm());
            HttpResponse response = client.execute(host, r);
            JSONObject object = extractObject(response);
            String[] nodeIpAndPort = ((String) object.get("proxyId")).split("//")[1].split(":");
            return nodeIpAndPort[0];
        } catch (JSONException e) {
            LOGGER.error("*****JSONException occurs in getNodeIpBySessionId()*****", e);
            throw new GridConnectException("*****JSONException occurs in getNodeIpBySessionId()*****", e);
        } catch (MalformedURLException e) {
            LOGGER.error("*****MalformedURLException occurs in getNodeIpBySessionId()*****", e);
            throw new GridConnectException("*****MalformedURLException occurs in getNodeIpBySessionId()*****", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("*****ClientProtocolException occurs in getNodeIpBySessionId()*****", e);
            throw new GridConnectException("*****ClientProtocolException occurs in getNodeIpBySessionId()*****", e);
        } catch (IOException e) {
            LOGGER.error("*****IOException occurs in getNodeIpBySessionId()*****", e);
            throw new GridConnectException("*****IOException occurs in getNodeIpBySessionId()*****", e);
        }
    }

    private static JSONObject extractObject(HttpResponse resp) throws IOException, JSONException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            stringBuffer.append(line);
        }
        rd.close();
        return new JSONObject(stringBuffer.toString());
    }
}
