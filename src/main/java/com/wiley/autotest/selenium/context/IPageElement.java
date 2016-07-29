package com.wiley.autotest.selenium.context;

import org.openqa.selenium.WebDriver;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 19:08
 */
public interface IPageElement {
    void init(WebDriver driver, ScreenshotHelper screenshotHelper);

    void handleRedirect();

    ErrorSender getErrorSender();
}
