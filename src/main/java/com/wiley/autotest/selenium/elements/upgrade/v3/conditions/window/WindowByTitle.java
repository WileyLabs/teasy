package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

import com.wiley.autotest.ExpectedConditions2;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class WindowByTitle extends WindowFinder {

    public WindowByTitle(String title){
        super(title);
    }

    @Override
    public Function<WebDriver, String> findAndSwitch() {
        waitForChrome();
        return ExpectedConditions2.appearingOfWindowAndSwitchToIt(locatedBy());
    }
}
