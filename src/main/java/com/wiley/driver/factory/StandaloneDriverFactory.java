package com.wiley.driver.factory;

import com.wiley.driver.factory.capabilities.*;
import com.wiley.holders.DriverHolder;
import com.wiley.holders.TestParamsHolder;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
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
    private final UnexpectedAlertBehaviour alertBehaviour = UnexpectedAlertBehaviour.ACCEPT;
    private final boolean isHeadless;
    private final URL gridUrl;

    StandaloneDriverFactory(String browserName, String platformName, DesiredCapabilities customCaps, boolean isHeadless, final URL gridUrl) {
        this.browserName = browserName;
        this.platformName = platformName;
        this.customCaps = customCaps;
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
        TestParamsHolder.setPlatform(LINUX);
        switch (browserName.toLowerCase().trim()) {
            case CHROME: {
                return chrome(customCaps, Platform.LINUX);
            }
            case FIREFOX: {
                return firefox(customCaps, Platform.LINUX);
            }
            case GECKO: {
                return gecko(customCaps, Platform.LINUX);
            }
            default: {
                return throwException(browserName, LINUX);
            }
        }
    }

    private WebDriver macDriver() {
        TestParamsHolder.setPlatform(MAC);
        switch (browserName.toLowerCase().trim()) {
            case CHROME: {
                return chrome(customCaps, Platform.MAC);
            }
            case FIREFOX: {
                return firefox(customCaps, Platform.MAC);
            }
            case GECKO: {
                return gecko(customCaps, Platform.MAC);
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
        TestParamsHolder.setPlatform(WINDOWS);
        switch (browserName.toLowerCase().trim()) {
            case FIREFOX: {
                return firefox(customCaps, Platform.WINDOWS);
            }
            case GECKO: {
                return gecko(customCaps, Platform.WINDOWS);
            }
            case CHROME: {
                return chrome(customCaps, Platform.WINDOWS);
            }
            case EDGE: {
                return edge(customCaps);
            }
            case IE: {
                return ie(customCaps);
            }
            default: {
                return throwException(browserName, WINDOWS);
            }
        }
    }

    private WebDriver throwException(String browserName, String platformName) {
        throw new RuntimeException("Not supported browser: " + browserName + ", for platform: " + platformName);
    }

    private WebDriver firefox(DesiredCapabilities customCaps, Platform platform) {
        DriverHolder.setDriverName(FIREFOX);
        return new FirefoxDriver(
                new FirefoxOptions(
                        new FireFoxCaps(customCaps, this.alertBehaviour, platform).get()
                )
        );
    }

    private WebDriver gecko(DesiredCapabilities customCaps, Platform platform) {
        DriverHolder.setDriverName(GECKO);
        FirefoxDriverManager.getInstance().setup();
        return new FirefoxDriver(
                new FirefoxOptions(
                        new GeckoCaps(customCaps, this.alertBehaviour, platform).get()
                )
        );
    }

    private WebDriver chrome(DesiredCapabilities customCaps, Platform platform) {
        DriverHolder.setDriverName(CHROME);
        ChromeDriverManager.getInstance().setup();
        ChromeDriverService defaultService = ChromeDriverService.createDefaultService();
        ChromeDriver chromeDriver = new ChromeDriver(defaultService,
                new ChromeOptions().merge(
                        new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless, platform).get()
                )
        );
        TestParamsHolder.setChromePort(defaultService.getUrl().getPort());
        return chromeDriver;
    }

    private WebDriver ie(DesiredCapabilities customCaps) {
        DriverHolder.setDriverName(IE);
        InternetExplorerDriverManager.getInstance().version(STABLE_IE_DRIVER_VERSION).setup();
        return new InternetExplorerDriver(
                new InternetExplorerOptions(
                        new IECaps(customCaps, "", this.alertBehaviour).get()
                )
        );
    }

    private WebDriver edge(DesiredCapabilities customCaps) {
        DriverHolder.setDriverName(EDGE);
        EdgeDriverManager.getInstance().setup();
        return new EdgeDriver(
                new EdgeOptions().merge(
                        new EdgeCaps(customCaps, this.alertBehaviour).get()
                )
        );
    }

    private WebDriver safariTechnologyPreview() {
        DriverHolder.setDriverName(SAFARI_TECHNOLOGY_PREVIEW);
        RemoteWebDriver driver = new RemoteWebDriver(this.gridUrl, new SafariTechPreviewCaps(customCaps).get());
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }
}
