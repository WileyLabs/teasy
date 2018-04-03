package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

/**
 * Base abstraction representing capabilities for Teasy driver initialization
 * all caps should implement a support for custom capabilities that could be passed
 * from the projects to provide custom configurations for particular browser
 */
public abstract class TeasyCaps {
    final DesiredCapabilities customCaps;

    public TeasyCaps(DesiredCapabilities customCaps) {
        this.customCaps = customCaps;
    }

    public abstract DesiredCapabilities get();

    void setLoggingPrefs(DesiredCapabilities caps) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }
}
