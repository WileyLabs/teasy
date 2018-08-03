package com.wiley.elements.conditions.window;

import com.wiley.elements.conditions.TeasyExpectedConditions;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class WindowByPartialUrl extends WindowFinder {

    public WindowByPartialUrl(String locatedBy) {
        super(locatedBy);
    }

    @Override
    public Function<WebDriver, String> findAndSwitch() {
        waitForChrome();
        return TeasyExpectedConditions.appearingOfWindowByPartialUrl(locatedBy());
    }
}
