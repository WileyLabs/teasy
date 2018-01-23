package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Chrome browser on Android
 */
public class IosNativeAppCaps extends TeasyCaps {

    public IosNativeAppCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = new DesiredCapabilities();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }
}
