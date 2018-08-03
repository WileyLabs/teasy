package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Caps for Safari Technology Preview on Mac
 */
public class SafariTechPreviewCaps extends TeasyCaps {

    public SafariTechPreviewCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    public SafariOptions get() {
        SafariOptions options = new SafariOptions();
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setUseTechnologyPreview(true);
        options.setCapability(SafariOptions.CAPABILITY, safariOptions);
        setLoggingPrefs(options);
        if (!this.customCaps.asMap().isEmpty()) {
            options.merge(this.customCaps);
        }
        return options;
    }
}
