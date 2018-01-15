package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class EdgeCaps extends TeasyCaps {

    private final DesiredCapabilities customCaps;
    private final UnexpectedAlertBehaviour alertBehaviour;

    public EdgeCaps(DesiredCapabilities customCaps, UnexpectedAlertBehaviour alertBehaviour) {
        this.customCaps = customCaps;
        this.alertBehaviour = alertBehaviour;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getEdgeCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getEdgeCaps() {
        DesiredCapabilities caps = DesiredCapabilities.edge();
        caps.setPlatform(Platform.WINDOWS);
        caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, this.alertBehaviour);
        return caps;
    }
}
