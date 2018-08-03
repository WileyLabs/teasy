package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Edge browser
 */
public class EdgeCaps extends TeasyCaps {

    private final UnexpectedAlertBehaviour alertBehaviour;

    public EdgeCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour) {
        super(customCaps);
        this.alertBehaviour = alertBehaviour;
    }

    public EdgeOptions get() {
        EdgeOptions caps = getEdgeOptions();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.setCapability("platform", Platform.WINDOWS);
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        setLoggingPrefs(options);
        return options;
    }
}
