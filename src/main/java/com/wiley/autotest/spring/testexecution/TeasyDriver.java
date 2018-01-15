package com.wiley.autotest.spring.testexecution;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.services.Configuration;
import com.wiley.autotest.spring.Settings;
import com.wiley.autotest.spring.testexecution.capabilities.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.wiley.autotest.selenium.SeleniumHolder.*;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

public class TeasyDriver {

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

    private static final String STABLE_IE_DRIVER_VERSION = "3.4.0";

    private final Settings settings;
    private final Configuration configuration;
    private final UnexpectedAlertBehaviour alertBehaviour;

    public TeasyDriver(Settings settings, Configuration configuration, UnexpectedAlertBehaviour alertBehaviour) {
        this.settings = settings;
        this.configuration = configuration;
        this.alertBehaviour = alertBehaviour;
    }

    public WebDriver init() {
        String browserName = getBrowserName();
        DesiredCapabilities customCaps = getcustomCaps(configuration);
        String platformName = getPlatformName();

        if (settings.isRunTestsWithGrid()) {
            return prepareRemoteDriver(browserName, platformName, customCaps);
        } else {
            if (platformName.equals(WINDOWS)) {
                SeleniumHolder.setPlatform(WINDOWS);

                if (equalsIgnoreCase(browserName, FIREFOX)) {
                    SeleniumHolder.setDriverName(FIREFOX);
                    return firefox(customCaps);
                } else if (equalsIgnoreCase(browserName, GECKO)) {
                    SeleniumHolder.setDriverName(GECKO);
                    return gecko(customCaps);
                } else if (equalsIgnoreCase(browserName, CHROME)) {
                    SeleniumHolder.setDriverName(CHROME);
                    return chrome(settings, customCaps);
                } else if (equalsIgnoreCase(browserName, IE)) {
                    SeleniumHolder.setDriverName(IE);
                    return explorer(customCaps);
                } else if (equalsIgnoreCase(browserName, EDGE)) {
                    SeleniumHolder.setDriverName(EDGE);
                    return edge(customCaps);
                } else if (equalsIgnoreCase(browserName, IE10)) {
                    SeleniumHolder.setDriverName(IE10);
                    return explorer("10", customCaps);
                } else if (equalsIgnoreCase(browserName, IE9)) {
                    SeleniumHolder.setDriverName(IE9);
                    return explorer("9", customCaps);
                }
            }
            if (platformName.equals(MAC)) {
                SeleniumHolder.setPlatform(MAC);

                if (equalsIgnoreCase(browserName, CHROME)) {
                    SeleniumHolder.setDriverName(CHROME);
                    return chrome(settings, customCaps);
                } else if (equalsIgnoreCase(browserName, FIREFOX)) {
                    SeleniumHolder.setDriverName(FIREFOX);
                    return firefox(customCaps);
                } else if (equalsIgnoreCase(browserName, SAFARI_TECHNOLOGY_PREVIEW)) {
                    SeleniumHolder.setDriverName(SAFARI_TECHNOLOGY_PREVIEW);
                    return safariTechnologyPreview(settings);
                }
            }
            if (platformName.equals(LINUX)) {
                SeleniumHolder.setPlatform(LINUX);
                if (equalsIgnoreCase(browserName, CHROME)) {
                    SeleniumHolder.setDriverName(CHROME);
                    return chrome(settings, customCaps);
                } else if (equalsIgnoreCase(browserName, FIREFOX)) {
                    SeleniumHolder.setDriverName(FIREFOX);
                    return firefox(customCaps);
                }
            }
        }
        throwException(browserName, platformName);
        return null;
    }

    private WebDriver prepareRemoteDriver(String browserName, String platformName, DesiredCapabilities customCaps) {
        //TODO ve this initialization is probably not needed
        DesiredCapabilities caps = new DesiredCapabilities();

        if (platformName.equals(WINDOWS)) {
            SeleniumHolder.setPlatform(WINDOWS);

            if (equalsIgnoreCase(browserName, CHROME)) {
                caps = new ChromeCaps(customCaps, this.alertBehaviour, settings.isHeadlessBrowser()).get();
                SeleniumHolder.setDriverName(CHROME);
            } else if (equalsIgnoreCase(browserName, IE)) {
                //TODO NT - what to pass for the "version" field in this case?
                caps = new IECaps(customCaps, "", this.alertBehaviour).get();
                SeleniumHolder.setDriverName(IE);
            } else if (equalsIgnoreCase(browserName, IE11)) {
                caps = new IECaps(customCaps, "11", this.alertBehaviour).get();
                SeleniumHolder.setDriverName(IE11);
            } else if (equalsIgnoreCase(browserName, IE10)) {
                caps = new IECaps(customCaps, "10", this.alertBehaviour).get();
                SeleniumHolder.setDriverName(IE10);
            } else if (equalsIgnoreCase(browserName, IE9)) {
                caps = new IECaps(customCaps, "9", this.alertBehaviour).get();
                SeleniumHolder.setDriverName(IE9);
            } else if (equalsIgnoreCase(browserName, EDGE)) {
                caps = new EdgeCaps(customCaps, this.alertBehaviour).get();
                SeleniumHolder.setDriverName(EDGE);
            } else if (equalsIgnoreCase(browserName, GECKO)) {
                caps = new GeckoCaps(customCaps, this.alertBehaviour).get();
                SeleniumHolder.setDriverName(GECKO);
            } else if (equalsIgnoreCase(browserName, FIREFOX)) {
                caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
                SeleniumHolder.setDriverName(FIREFOX);
            } else {
                throwException(browserName, platformName);
            }
        } else if (equalsIgnoreCase(platformName, ANDROID)) {
            SeleniumHolder.setPlatform(ANDROID);
            if (equalsIgnoreCase(browserName, CHROME)) {
                return androidChrome(customCaps);
            } else if (customCaps != null && !customCaps.asMap().isEmpty()) {
                AppiumDriver remoteWebDriver = new AndroidDriver(getGridhubUrl(settings), customCaps);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                return remoteWebDriver;
            } else {
                throwException(browserName, platformName);
            }
        } else if (equalsIgnoreCase(platformName, MAC)) {
            SeleniumHolder.setPlatform(MAC);
            if (equalsIgnoreCase(browserName, CHROME)) {
                caps = new ChromeCaps(customCaps, this.alertBehaviour, settings.isHeadlessBrowser()).get();
                SeleniumHolder.setDriverName(CHROME);
            } else if (equalsIgnoreCase(browserName, SAFARI_TECHNOLOGY_PREVIEW)) {
                SeleniumHolder.setDriverName(SAFARI_TECHNOLOGY_PREVIEW);
                return safariTechnologyPreview(settings);
            } else {
                return throwException(browserName, platformName);
            }
        } else if (equalsIgnoreCase(platformName, IOS)) {
            SeleniumHolder.setPlatform(IOS);
            if (equalsIgnoreCase(browserName, SAFARI)) {
                SeleniumHolder.setDriverName(SAFARI);
                return iosSafari(customCaps);
            } else if (!customCaps.asMap().isEmpty()) {
                AppiumDriver remoteWebDriver = new IOSDriver(getGridhubUrl(settings), customCaps);
                remoteWebDriver.setFileDetector(new LocalFileDetector());
                return remoteWebDriver;
            } else {
                throwException(browserName, platformName);
            }
        } else if (platformName.equalsIgnoreCase(LINUX)) {
            SeleniumHolder.setPlatform(LINUX);
            if (equalsIgnoreCase(browserName, CHROME)) {
                caps = new ChromeCaps(customCaps, this.alertBehaviour, settings.isHeadlessBrowser()).get();
                SeleniumHolder.setDriverName(CHROME);
            } else if (equalsIgnoreCase(browserName, FIREFOX)) {
                caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
                SeleniumHolder.setDriverName(FIREFOX);
            } else {
                throwException(browserName, platformName);
            }
        } else {
            throw new RuntimeException("Not supported platform: " + platformName);
        }

        if (!customCaps.asMap().isEmpty()) {
//            caps.merge(customCaps);
        }

        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(getGridhubUrl(settings), caps);
        remoteWebDriver.setFileDetector(new LocalFileDetector());
        return remoteWebDriver;
    }

    @NotNull
    private WebDriver iosSafari(DesiredCapabilities customCaps) {
        DesiredCapabilities caps;
        caps = new IosSafariCaps(customCaps).get();

        AppiumDriver remoteWebDriver;
        remoteWebDriver = new IOSDriver(getGridhubUrl(settings), caps);
        remoteWebDriver.setFileDetector(new LocalFileDetector());
        return remoteWebDriver;
    }

    @NotNull
    private WebDriver androidChrome(DesiredCapabilities customCaps) {
        DesiredCapabilities caps;
        SeleniumHolder.setDriverName(CHROME);
        caps = new AndroidChromeCaps(customCaps).get();
        AppiumDriver remoteWebDriver = new AndroidDriver(getGridhubUrl(settings), caps);
        remoteWebDriver.setFileDetector(new LocalFileDetector());
        return remoteWebDriver;
    }

    private WebDriver throwException(String browserName, String platformName) {
        throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
    }

    /**
     * TODO: NT VE - provide comment for the purpose of this method.
     * the logic is not very clear
     *
     * @return
     */
    private String getPlatformName() {
        String platformName;
        if (!getParameterPlatformName().equals("platform")) {
            platformName = getParameterPlatformName();
            setParameterPlatformName("platform");
        } else if (!getPlatform().isEmpty() && !getPlatform().equals("platform")) {
            platformName = getPlatform();
        } else {
            platformName = settings.getPlatform();
        }
        return platformName;
    }

    /**
     * TODO: NT VE - provide comment for the purpose of this method.
     * the logic is not very clear
     *
     * @return
     */
    private String getBrowserName() {
        String browserName;
        //for set browser from xml parameters
        if (!getParameterBrowserName().equals("browser")) {
            browserName = getParameterBrowserName();
            setParameterBrowserName("browser");
        } else if (!getDriverName().isEmpty() && getDriverName().equals(settings.getDriverName())) {
            browserName = getDriverName();
        } else {
            browserName = settings.getDriverName();
        }
        return browserName;
    }

    private WebDriver firefox(DesiredCapabilities customCaps) {
        DesiredCapabilities caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
        return new FirefoxDriver(caps);
    }

    private WebDriver gecko(DesiredCapabilities customCaps) {
        FirefoxDriverManager.getInstance().setup();
        DesiredCapabilities caps = new GeckoCaps(customCaps, this.alertBehaviour).get();
        return new FirefoxDriver(caps);
    }

    private WebDriver chrome(Settings settings, DesiredCapabilities customCaps) {
        ChromeDriverManager.getInstance().setup();
        DesiredCapabilities caps = new ChromeCaps(customCaps, this.alertBehaviour, settings.isHeadlessBrowser()).get();
        return new ChromeDriver(caps);
    }

    private WebDriver explorer(DesiredCapabilities customCaps) {
        InternetExplorerDriverManager.getInstance().version(STABLE_IE_DRIVER_VERSION).setup();
        DesiredCapabilities caps = new IECaps(customCaps, STABLE_IE_DRIVER_VERSION, this.alertBehaviour).get();
        return new InternetExplorerDriver(caps);
    }

    private WebDriver explorer(String version, DesiredCapabilities customCaps) {
        InternetExplorerDriverManager.getInstance().version(STABLE_IE_DRIVER_VERSION).setup();
        DesiredCapabilities desiredCapabilities = new IECaps(customCaps, version, this.alertBehaviour).get();
        return new InternetExplorerDriver(desiredCapabilities);
    }

    private WebDriver edge(DesiredCapabilities customCaps) {
        EdgeDriverManager.getInstance().setup();
        DesiredCapabilities desiredCapabilities = new EdgeCaps(customCaps, this.alertBehaviour).get();
        return new EdgeDriver(desiredCapabilities);
    }

    private WebDriver safariTechnologyPreview(Settings settings) {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.safari();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseCleanSession(true);
        safariOptions.setUseTechnologyPreview(true);
        desiredCapabilities.setCapability(SafariOptions.CAPABILITY, safariOptions);
        RemoteWebDriver remoteWebDriver = new RemoteWebDriver(getGridhubUrl(settings), desiredCapabilities);
        remoteWebDriver.setFileDetector(new LocalFileDetector());
        return remoteWebDriver;
    }

    @NotNull
    private URL getGridhubUrl(Settings settings) {
        URL url;
        try {
            url = new URL(settings.getGridHubUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error during gridhuburl creation. For url " + settings.getGridHubUrl());
        }
        return url;
    }

    public DesiredCapabilities getcustomCaps(Configuration configuration) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (configuration.getDesiredCapabilities() != null) {
            desiredCapabilities.merge(configuration.getDesiredCapabilities());
        } else if (!configuration.getCapabilities().isEmpty()) {
            desiredCapabilities = new DesiredCapabilities();
            for (Map.Entry<String, Object> capability : configuration.getCapabilities().entrySet()) {
                desiredCapabilities.setCapability(capability.getKey(), capability.getValue());
            }
        }
        return desiredCapabilities;
    }

    private DesiredCapabilities getIOSSafariDesiredCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "Safari");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("newCommandTimeout", "900");
        return capabilities;
    }
}
