package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Caps for Safari Technology Preview on Mac
 */
public class SafariTechPreviewCaps extends TeasyCaps {

    public SafariTechPreviewCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public MutableCapabilities get() {
        DesiredCapabilities safariCaps = DesiredCapabilities.safari();
        SafariOptions options = new SafariOptions();
        options.setUseTechnologyPreview(true);
        safariCaps.setCapability(SafariOptions.CAPABILITY, options);
        setLoggingPrefs(safariCaps);
        if (!this.customCaps.asMap().isEmpty()) {
            safariCaps.merge(this.customCaps);
        }
        return safariCaps;
    }
}
