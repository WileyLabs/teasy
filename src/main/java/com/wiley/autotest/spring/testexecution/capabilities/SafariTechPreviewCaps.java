package com.wiley.autotest.spring.testexecution.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Caps for Safari Technology Preview on Mac
 */
public class SafariTechPreviewCaps extends TeasyCaps {

    public SafariTechPreviewCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public DesiredCapabilities get() {
        DesiredCapabilities caps = DesiredCapabilities.safari();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseTechnologyPreview(true);
        caps.setCapability(SafariOptions.CAPABILITY, safariOptions);
        setLoggingPrefs(caps);
        if (!this.customCaps.asMap().isEmpty()) {
            caps.merge(this.customCaps);
        }
        return caps;
    }
}
