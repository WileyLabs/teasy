package com.wiley.driver.factory.capabilities;

import com.wiley.driver.factory.DriverFactory;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Chrome browser on Android
 */
public class AndroidNativeAppCaps extends TeasyCaps {

    public AndroidNativeAppCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = new DesiredCapabilities();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        if (this.customCaps.getBrowserName().equals(DriverFactory.NATIVE_APP)) {
            this.customCaps.setBrowserName(DriverFactory.EMPTY);
        }
        return caps;
    }
}
