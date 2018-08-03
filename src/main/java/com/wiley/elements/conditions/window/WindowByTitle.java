package com.wiley.elements.conditions.window;

import com.wiley.elements.conditions.TeasyExpectedConditions;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

public class WindowByTitle extends WindowFinder {

    public WindowByTitle(String title){
        super(title);
    }

    @Override
    public Function<WebDriver, String> findAndSwitch() {
        waitForChrome();
        return TeasyExpectedConditions.appearingOfWindowAndSwitchToIt(locatedBy());
    }
}
