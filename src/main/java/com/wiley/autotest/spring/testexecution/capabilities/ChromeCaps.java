package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Arrays;
import java.util.logging.Level;

import static com.wiley.autotest.selenium.SeleniumHolder.setProxy;

public class ChromeCaps extends TeasyCaps {

    private final DesiredCapabilities customCaps;
    private final UnexpectedAlertBehaviour alertBehaviour;
    private final boolean isHeadless;

    public ChromeCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour, boolean isHeadless) {
        this.customCaps = customCaps;
        this.alertBehaviour = alertBehaviour;
        this.isHeadless = isHeadless;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getChromeCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }
    private DesiredCapabilities getChromeCaps() {
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);

        //To view pdf in chrome
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", Arrays.asList("test-type", "--ignore-certificate-errors"));

        if (this.isHeadless) {
            options.addArguments("headless");
        }

        caps.setCapability(ChromeOptions.CAPABILITY, options);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        //TODO NT we probably set a correct platform here or don't set it at all.
        caps.setPlatform(Platform.WINDOWS);
        setLoggingPrefs(caps);
        return caps;
    }

}
