package com.wiley.autotest.screenshots;

import com.wiley.autotest.utils.DriverUtils;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Created by mosadchiy on 26.05.2016.
 */
public class ScreenshotBy implements BrowserDependency {

    @Override
    public void beforeScreenshot(WebDriver driver) {
        DriverUtils.setWindowSize(driver,
                DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_WIDTH_JS),
                DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_HEIGHT_JS));
    }

    @Override
    public void afterScreenshot(WebDriver driver) {

    }

    @Override
    public BufferedImage cut(WebDriver driver, BufferedImage image) {
        return image;
    }
}
