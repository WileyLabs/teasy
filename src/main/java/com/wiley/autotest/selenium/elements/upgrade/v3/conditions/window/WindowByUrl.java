package com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window;

import com.wiley.autotest.ExpectedConditions2;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 30/05/2017.
 */
public class WindowByUrl extends WindowFinder {

    public WindowByUrl(String url){
        super(url);
    }

    @Override
    public Function<WebDriver, String> findAndSwitch() {
        return ExpectedConditions2.appearingOfWindowByUrl(locatedBy());
    }
}
