package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 30/05/2017.
 */
public abstract class WindowCondition {

    private String value;

    public WindowCondition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract Function<WebDriver, String> findAndSwitch();

}
