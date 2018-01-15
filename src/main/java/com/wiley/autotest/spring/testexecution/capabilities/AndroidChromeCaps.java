package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AndroidChromeCaps extends TeasyCaps {

    private final DesiredCapabilities customCaps;
    private static final String CHROME = "chrome";

    public AndroidChromeCaps(DesiredCapabilities customCaps) {
        this.customCaps = customCaps;
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = getAndroidChromeCaps();
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }

    private DesiredCapabilities getAndroidChromeCaps() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("newCommandTimeout", "900");
        capabilities.setPlatform(Platform.ANDROID);
        capabilities.setCapability(CapabilityType.BROWSER_NAME, CHROME);
        return capabilities;
    }
}
