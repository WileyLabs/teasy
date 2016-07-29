package com.wiley.autotest.selenium.context;

import org.openqa.selenium.WebDriver;

/**
 * @author alexey.a.semenov@gmail.com
 */
public interface PageLoadingValidator {
    void assertLoaded(WebDriver driver);
}
