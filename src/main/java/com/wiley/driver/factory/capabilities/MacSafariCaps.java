package com.wiley.driver.factory.capabilities;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

public class MacSafariCaps extends TeasyCaps {
    public MacSafariCaps(DesiredCapabilities customCaps) {
        super(customCaps);
    }

    @Override
    public MutableCapabilities get() {
        return new SafariOptions().merge(customCaps);
    }
}
