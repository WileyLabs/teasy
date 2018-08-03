package com.wiley.driver.factory;

import com.wiley.config.Configuration;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class TeasyDriver {

    public TeasyDriver() {
    }

    public WebDriver init() {
        DriverFactory driverFactory;
        URL gridUrl = getGridHubUrl();
        boolean isHeadless = Configuration.headless;
        if (Configuration.runWithGrid) {
            driverFactory = new RemoteDriverFactory(Configuration.browser, Configuration.platform, Configuration.customCaps, isHeadless, gridUrl);
        } else {
            driverFactory = new StandaloneDriverFactory(Configuration.browser, Configuration.platform, Configuration.customCaps, isHeadless, gridUrl);
        }

        return driverFactory.get();
    }

    private URL getGridHubUrl() {
        URL url;
        try {
            url = new URL(Configuration.gridHubUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error during gridhuburl creation. For url " + Configuration.gridHubUrl);
        }
        return url;
    }
}
