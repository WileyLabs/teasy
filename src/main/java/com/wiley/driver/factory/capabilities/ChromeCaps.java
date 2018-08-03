package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;

/**
 * Caps for Chrome browser on any platform (Windows, Linux, Mac)
 */
public class ChromeCaps extends TeasyCaps {

    private final UnexpectedAlertBehaviour alertBehaviour;
    private final boolean isHeadless;
    private final Platform platform;

    public ChromeCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour, boolean isHeadless, Platform platform) {
        super(customCaps);
        this.alertBehaviour = alertBehaviour;
        this.isHeadless = isHeadless;
        this.platform = platform;
    }

    public ChromeOptions get() {
        ChromeOptions caps = getChromeOptions();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        //To view pdf in chrome
        options.setExperimentalOption("excludeSwitches", Arrays.asList("test-type", "--ignore-certificate-errors"));
        if (this.isHeadless) {
            options.addArguments("headless");
        }
        options.setCapability(ChromeOptions.CAPABILITY, options);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability("platform", platform);
        setLoggingPrefs(options);
        return options;
    }
}
