package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

import com.wiley.autotest.ExpectedConditions2;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class WindowByPartialUrl extends WindowFinder {

    public WindowByPartialUrl(String locatedBy) {
        super(locatedBy);
    }

    @Override
    public Function<WebDriver, String> findAndSwitch() {
        waitForChrome();
        return ExpectedConditions2.appearingOfWindowByPartialUrl(locatedBy());
    }
}
