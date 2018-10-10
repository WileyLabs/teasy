package com.wiley.autotest.spring.testexecution;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.spring.testexecution.capabilities.*;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Factory to create Driver for usage with Selenium Grid
 */
public class RemoteDriverFactory implements DriverFactory {

    private final String browserName;
    private final String platformName;
    private final boolean pureCapsRequired;
    private final DesiredCapabilities customCaps;
    private final UnexpectedAlertBehaviour alertBehaviour;
    private final boolean isHeadless;
    private final URL gridUrl;

    public RemoteDriverFactory(String browserName, String platformName, boolean pureCapsRequired,
                               DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour,
                               boolean isHeadless, URL gridUrl) {
        this.browserName = browserName;
        this.platformName = platformName;
        this.pureCapsRequired = pureCapsRequired;
        this.customCaps = customCaps;
        this.alertBehaviour = alertBehaviour;
        this.isHeadless = isHeadless;
        this.gridUrl = gridUrl;
    }

    @Override
    public WebDriver get() {
        switch (platformName.toLowerCase().trim()) {
            case WINDOWS: {
                return windowsDriver();
            }
            case MAC: {
                return macDriver();
            }
            case LINUX: {
                return linuxDriver();
            }
            case ANDROID: {
                return androidDriver();
            }
            case IOS: {
                return iosDriver();
            }
            default: {
                return throwException(browserName, platformName);
            }
        }
    }

    private WebDriver macDriver() {
        SeleniumHolder.setPlatform(MAC);
        String browserName = this.browserName.toLowerCase().trim();
        Capabilities caps = customCaps;
        if (!this.pureCapsRequired) {
            switch (browserName) {
                case CHROME: {
                    caps = new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless).get();
                    break;
                }
                case FIREFOX: {
                    caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                case SAFARI_TECHNOLOGY_PREVIEW: {
                    caps = new SafariTechPreviewCaps(customCaps).get();
                    break;
                }
                default: {
                    return throwException(this.browserName, MAC);
                }
            }
        }
        return createRemoteDriver(caps, browserName);
    }

    private WebDriver iosDriver() {
        SeleniumHolder.setPlatform(IOS);
        String browserName = this.browserName.toLowerCase().trim();
        Capabilities caps = customCaps;
        if (!this.pureCapsRequired) {
            switch (browserName) {
                case SAFARI: {
                    caps = new IosSafariCaps(customCaps).get();
                    break;
                }
                case NATIVE_APP: {
                    caps = new IosNativeAppCaps(customCaps).get();
                    break;
                }
                default: {
                    return throwException(this.browserName, IOS);
                }
            }
        }
        return createIosDriver(caps, browserName);
    }

    private WebDriver linuxDriver() {
        SeleniumHolder.setPlatform(LINUX);
        String browserName = this.browserName.toLowerCase().trim();
        Capabilities caps = customCaps;
        if (!this.pureCapsRequired) {
            switch (browserName) {
                case CHROME: {
                    caps = new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless).get();
                    break;
                }
                case FIREFOX: {
                    caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                case GECKO: {
                    caps = new GeckoCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                default: {
                    return throwException(this.browserName, LINUX);
                }
            }
        }
        return createRemoteDriver(caps, browserName);
    }

    private WebDriver androidDriver() {
        SeleniumHolder.setPlatform(ANDROID);
        String browserName = this.browserName.toLowerCase().trim();
        Capabilities caps = customCaps;
        if (!this.pureCapsRequired) {
            switch (browserName) {
                case CHROME: {
                    caps = new AndroidChromeCaps(customCaps).get();
                    break;
                }
                case NATIVE_APP: {
                    caps = new AndroidNativeAppCaps(customCaps).get();
                    break;
                }
                default: {
                    return throwException(this.browserName, ANDROID);
                }
            }
        }
        return createAndroidDriver(caps, browserName);
    }

    private WebDriver windowsDriver() {
        SeleniumHolder.setPlatform(WINDOWS);
        String browserName = this.browserName.toLowerCase().trim();
        Capabilities caps = customCaps;
        if (!this.pureCapsRequired) {
            switch (browserName) {
                case CHROME: {
                    caps = new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless).get();
                    break;
                }
                case FIREFOX: {
                    caps = new FireFoxCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                case GECKO: {
                    caps = new GeckoCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                case EDGE: {
                    caps = new EdgeCaps(customCaps, this.alertBehaviour).get();
                    break;
                }
                case IE: {
                    caps = new IECaps(customCaps, "", this.alertBehaviour).get();
                    break;
                }
                case IE11: {
                    caps = new IECaps(customCaps, "11", this.alertBehaviour).get();
                    break;
                }
                case IE10: {
                    caps = new IECaps(customCaps, "10", this.alertBehaviour).get();
                    break;
                }
                default: {
                    return throwException(this.browserName, WINDOWS);
                }
            }
        }
        return createRemoteDriver(caps, browserName);
    }

    private RemoteWebDriver createRemoteDriver(Capabilities caps, String name) {
        return tuneDriver(new RemoteWebDriver(this.gridUrl, caps), name);
    }

    private AppiumDriver createAndroidDriver(Capabilities caps, String name) {
        return (AppiumDriver) tuneDriver(new AndroidDriver(this.gridUrl, caps), name);
    }

    private AppiumDriver createIosDriver(Capabilities caps, String name) {
        return (AppiumDriver) tuneDriver(new IOSDriver(this.gridUrl, caps), name);
    }

    /**
     * Final tuning for a driver
     * <p>
     * Setting a file detector for a driver
     * and setting a driver name to SeleniumHolder
     *
     * @param driver - instance of a RemoteWebDriver
     * @param name   - driver name
     * @return driver with LocalFileDetector as a fileDetector
     */
    private RemoteWebDriver tuneDriver(RemoteWebDriver driver, String name) {
        SeleniumHolder.setDriverName(name);
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }

    private WebDriver throwException(String browserName, String platformName) {
        throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
    }
}
