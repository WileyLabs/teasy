package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Caps for Chrome browser on Android
 */
public class AndroidChromeCaps extends TeasyCaps {

    public AndroidChromeCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getAndroidChromeCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getAndroidChromeCaps() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("newCommandTimeout", "900");
        caps.setPlatform(Platform.ANDROID);
        caps.setCapability(CapabilityType.BROWSER_NAME, "chrome");
        return caps;
    }
}
