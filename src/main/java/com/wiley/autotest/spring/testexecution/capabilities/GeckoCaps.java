package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class GeckoCaps extends TeasyCaps{

    private final DesiredCapabilities customCaps;
    private final UnexpectedAlertBehaviour alertBehaviour;

    public GeckoCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour) {
        this.customCaps = customCaps;
        this.alertBehaviour = alertBehaviour;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getGeckoCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getGeckoCaps() {
        DesiredCapabilities caps = DesiredCapabilities.firefox();
        caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        caps.setCapability(FirefoxDriver.MARIONETTE, true);

        //TODO NT we probably set a correct platform here or don't set it at all.
        caps.setPlatform(Platform.WINDOWS);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        setLoggingPrefs(caps);
        return caps;
    }
}
