package com.wiley.autotest.screenshots;

import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Created by mosadchiy on 26.05.2016.
 */
public interface BrowserDependency {
    String GET_HTML_WIDTH_JS = "return top.$('html').width();";
    String GET_HTML_HEIGHT_JS = "return top.$('html').height();";

    void beforeScreenshot(WebDriver driver);

    void afterScreenshot(WebDriver driver);

    BufferedImage cut(WebDriver driver, BufferedImage image);
}