package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;

public class IosSafariCaps extends TeasyCaps {

    private final DesiredCapabilities customCaps;

    public IosSafariCaps(DesiredCapabilities customCaps) {
        this.customCaps = customCaps;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getIOSSafariCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getIOSSafariCaps() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "Safari");
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("newCommandTimeout", "900");
        return capabilities;
    }
}
