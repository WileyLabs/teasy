package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 30/05/2017.
 */
public abstract class WindowFinder {

    private String locatedBy;

    public WindowFinder(String locatedBy) {
        this.locatedBy = locatedBy;
    }

    public String locatedBy() {
        return locatedBy;
    }

    public abstract Function<WebDriver, String> findAndSwitch();

}
