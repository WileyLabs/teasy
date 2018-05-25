package com.wiley.autotest.spring.testexecution.capabilities;

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

    public ChromeCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour, boolean isHeadless) {
        super(customCaps);
        this.alertBehaviour = alertBehaviour;
        this.isHeadless = isHeadless;
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

        //TODO NT we probably set a correct platform here or don't set it at all.
        options.setCapability("platform", Platform.WINDOWS);
        setLoggingPrefs(options);
        return options;
    }
}
