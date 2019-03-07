package com.wiley.driver.factory;

import com.wiley.driver.factory.capabilities.*;
import com.wiley.holders.DriverHolder;
import com.wiley.holders.TestParamsHolder;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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
            case SAFARI_TECH_PREVIEW: {
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
                new FireFoxCaps(customCaps, this.alertBehaviour, platform).get()
        );
    }

    private WebDriver gecko(DesiredCapabilities customCaps, Platform platform) {
        DriverHolder.setDriverName(GECKO);
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(
                new GeckoCaps(customCaps, this.alertBehaviour, platform).get()
        );
    }

    private WebDriver chrome(DesiredCapabilities customCaps, Platform platform) {
        DriverHolder.setDriverName(CHROME);
        WebDriverManager.chromedriver().setup();
        ChromeDriverService service = ChromeDriverService.createDefaultService();
        ChromeDriver chromeDriver = new ChromeDriver(
                service,
                new ChromeCaps(customCaps, this.alertBehaviour, this.isHeadless, platform).get()
        );
        TestParamsHolder.setChromePort(service.getUrl().getPort());
        return chromeDriver;
    }

    private WebDriver ie(DesiredCapabilities customCaps) {
        DriverHolder.setDriverName(IE);
        WebDriverManager.iedriver().version(STABLE_IE_DRIVER_VERSION).setup();
        return new InternetExplorerDriver(
                new IECaps(customCaps, "", this.alertBehaviour).get());
    }

    private WebDriver edge(DesiredCapabilities customCaps) {
        DriverHolder.setDriverName(EDGE);
        WebDriverManager.edgedriver().setup();
        return new EdgeDriver(
                new EdgeCaps(customCaps, this.alertBehaviour).get());
    }

    private WebDriver safariTechnologyPreview() {
        DriverHolder.setDriverName(SAFARI_TECH_PREVIEW_DIVER_NAME);
        RemoteWebDriver driver = new RemoteWebDriver(
                this.gridUrl,
                new SafariTechPreviewCaps(customCaps).get());
        driver.setFileDetector(new LocalFileDetector());
        return driver;
    }
}
