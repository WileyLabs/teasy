package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Gecko browser
 */
public class GeckoCaps extends TeasyCaps {

    private final UnexpectedAlertBehaviour alertBehaviour;
    private final Platform platform;

    public GeckoCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour, Platform platform) {
        super(customCaps);
        this.alertBehaviour = alertBehaviour;
        this.platform = platform;
    }

    public FirefoxOptions get() {
        FirefoxOptions caps = getGeckoOptions();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private FirefoxOptions getGeckoOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        options.setCapability(FirefoxDriver.MARIONETTE, true);
        options.setCapability("platform", platform);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        setLoggingPrefs(options);
        return options;
    }
}
