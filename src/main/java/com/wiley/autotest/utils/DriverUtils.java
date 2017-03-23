package com.wiley.autotest.utils;

import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.spring.Settings;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class DriverUtils {

    private static final Settings SELENIUM_SETTINGS = new ClassPathXmlApplicationContext(new String[]{"classpath*:/META-INF/spring/context-selenium.xml"}).getBean(Settings.class);
    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<WebDriver>();

    private DriverUtils() {
    }

    public static WebDriver getFFDriver() {
        Settings ffSettings = new Settings();
        BeanUtils.copyProperties(SELENIUM_SETTINGS, ffSettings);
        ffSettings.setDriverName("firefox");
        driverHolder.set(new FramesTransparentWebDriver(new FirefoxDriver(getFireFoxDesiredCapabilities(ffSettings))));
        return driverHolder.get();
    }

    private static DesiredCapabilities getFireFoxDesiredCapabilities(Settings settings) {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
        capabilities.setCapability(FirefoxDriver.PROFILE, getFirefoxProfile(settings));
        return capabilities;
    }

    private static FirefoxProfile getFirefoxProfile(Settings settings) {
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
        return profile;
    }


    public static void setWindowSize(final WebDriver driver, final int width, final int height) {
        try {
            WebDriver.Window window = driver.manage().window();
            Dimension dimension = window.getSize();
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            int newWidth = dimension.width;
            if (width > 0) {
                newWidth = width + dimension.width - ((Long) javascriptExecutor.executeScript("return top.innerWidth;")).intValue();
            }
            int newHeight = dimension.height;
            if (height > 0) {
                newHeight = height + dimension.height - ((Long) javascriptExecutor.executeScript("return top.innerHeight;")).intValue();
            }
            if (dimension.width != newWidth || dimension.height != newHeight) {
                window.setSize(new Dimension(newWidth, newHeight));
            }
        } catch (WebDriverException ignored) {
            //If a frame is selected and then browser window is maximized, exception is thrown
            //Selenium bug: Issue 3758: Exception upon maximizing browser window with frame selected
        }
    }

    public static void setOuterWindowSize(final WebDriver driver, final int width, final int height) {
        WebDriver.Window window = driver.manage().window();
        Dimension dimension = window.getSize();
        int newWidth = dimension.width;
        if (width > 0) {
            newWidth = width;
        }
        int newHeight = dimension.height;
        if (height > 0) {
            newHeight = height;
        }
        if (dimension.width != newWidth || dimension.height != newHeight) {
            window.setSize(new Dimension(newWidth, newHeight));
        }
    }

    public static void moveMousePositionTo(final WebDriver driver, By locator) {
        new Actions(driver).moveToElement(driver.findElement(locator), 0, 0).perform();
    }
}
