package com.wiley.autotest.spring.testexecution;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.spring.testexecution.capabilities.*;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

/**
 * Factory to create driver for a single-threaded usage
 */
public class StandaloneDriverFactory implements DriverFactory {

    private final String browserName;
    private final String platformName;
    private final DesiredCapabilities customCaps;
    private final UnexpectedAlertBehaviour alertBehaviour;
    private final boolean isHeadless;
    private final URL gridUrl;

    StandaloneDriverFactory(String browserName, String platformName, DesiredCapabilities customCaps,
                            UnexpectedAlertBehaviour alertBehaviour, boolean isHeadless, final URL gridUrl) {
        this.browserName = browserName;
        this.platformName = platformName;
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
            default: {
                return throwException(browserName, platformName);
            }
        }
    }

    private WebDriver linuxDriver() {
        SeleniumHolder.setPlatform(LINUX);
        switch (browserName.toLowerCase().trim()) {
            case CHROME: {
                return chrome(customCaps);
            }
            case FIREFOX: {
                return firefox(customCaps);
            }
            default: {
                return throwException(browserName, LINUX);
            }
        }
    }

    private WebDriver macDriver() {
        SeleniumHolder.setPlatform(MAC);
        switch (browserName.toLowerCase().trim()) {
            case CHROME: {
                return chrome(customCaps);
            }
            case FIREFOX: {
                return firefox(customCaps);
            }
            case SAFARI_TECHNOLOGY_PREVIEW: {
                return safariTechnologyPreview();
            }
            default: {
                return throwException(browserName, MAC);
            }
        }
    }

    private WebDriver windowsDriver() {
        SeleniumHolder.setPlatform(WINDOWS);
        switch (browserName.toLowerCase().trim()) {
            case FIREFOX: {
                return firefox(customCaps);
            }
            case GECKO: {
                SeleniumHolder.setDriverName(GECKO);
                return gecko(customCaps);
            }
            case CHROME: {
                return chrome(customCaps);
            }
            case EDGE: {
                return edge(customCaps);
            }
            case IE: {
                return ie(customCaps);
            }
            default: {
                return throwException(browserName, MAC);
            }
        }
    }

    private WebDriver throwException(String browserName, String platformName) {
        throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
    }

    private WebDriver firefox(DesiredCapabilities customCaps) {
        SeleniumHolder.setDriverName(FIREFOX);
        return new FirefoxDriver(new FireFoxCaps(customCaps, this.alertBehaviour).get());
    }

    private WebDriver gecko(DesiredCapabilities customCaps) {
        FirefoxDriverManager.getInstance().setup();
        return new FirefoxDriver(new GeckoCaps(customCaps, this.alertBehaviour).get());
    }

    private WebDriver chrome(DesiredCapabilities customCaps) {
        SeleniumHolder.setDriverName(CHROME);
        ChromeDriverManager.getInstance().setup();
        return new ChromeDriver(new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless).get());
    }

    private WebDriver ie(DesiredCapabilities customCaps) {
        SeleniumHolder.setDriverName(IE);
        InternetExplorerDriverManager.getInstance().version(STABLE_IE_DRIVER_VERSION).setup();
        return new InternetExplorerDriver(new IECaps(customCaps, "", this.alertBehaviour).get());
    }

    private WebDriver edge(DesiredCapabilities customCaps) {
        SeleniumHolder.setDriverName(EDGE);
        EdgeDriverManager.getInstance().setup();
        return new EdgeDriver(new EdgeCaps(customCaps, this.alertBehaviour).get());
    }

    private WebDriver safariTechnologyPreview() {
        SeleniumHolder.setDriverName(SAFARI_TECHNOLOGY_PREVIEW);
        RemoteWebDriver driver = new RemoteWebDriver(this.gridUrl, new SafariTechPreviewCaps(customCaps).get());
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }
}
