package com.wiley.autotest.spring.testexecution;

import com.wiley.autotest.services.Configuration;
import com.wiley.autotest.spring.Settings;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.wiley.autotest.selenium.SeleniumHolder.*;

public class TeasyDriver {

    private final Settings settings;
    private final Configuration configuration;
    private final UnexpectedAlertBehaviour alertBehaviour;

    public TeasyDriver(Settings settings, Configuration configuration, UnexpectedAlertBehaviour alertBehaviour) {
        this.settings = settings;
        this.configuration = configuration;
        this.alertBehaviour = alertBehaviour;
    }

    public WebDriver init() {
        String browser = getBrowserName();
        DesiredCapabilities customCaps = getCustomCaps(configuration);
        String platform = getPlatformName();

        DriverFactory driverFactory;
        URL gridUrl = getGridHubUrl();
        boolean isHeadless = settings.isHeadlessBrowser();
        if (settings.isRunTestsWithGrid()) {
            driverFactory = new RemoteDriverFactory(browser, platform, customCaps, alertBehaviour, isHeadless, gridUrl);
        } else {
            driverFactory = new StandaloneDriverFactory(browser, platform, customCaps, alertBehaviour, isHeadless, gridUrl);
        }

        return driverFactory.get();
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

    @NotNull
    private URL getGridHubUrl() {
        URL url;
        try {
            url = new URL(this.settings.getGridHubUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error during gridhuburl creation. For url " + this.settings.getGridHubUrl());
        }
        return url;
    }

    private DesiredCapabilities getCustomCaps(Configuration configuration) {
        DesiredCapabilities caps = new DesiredCapabilities();

        if (configuration.getDesiredCapabilities() != null) {
            caps.merge(configuration.getDesiredCapabilities());
        } else if (!configuration.getCapabilities().isEmpty()) {
            for (Map.Entry<String, Object> capability : configuration.getCapabilities().entrySet()) {
                caps.setCapability(capability.getKey(), capability.getValue());
            }
        }
        return caps;
    }
}
