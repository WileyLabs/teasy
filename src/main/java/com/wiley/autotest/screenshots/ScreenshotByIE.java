package com.wiley.autotest.screenshots;

import com.wiley.autotest.utils.DriverUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;

/**
 * Created by mosadchiy on 26.05.2016.
 */
public class ScreenshotByIE implements BrowserDependency {

    private static final int BROWSER_GAP = 2; // IE Web Driver reduces browser width to 2 pixels before getting screenshot.

    private static final String SET_MODAL_WINDOWS_WIDTH_JS;
    private static final String RESTORE_MODAL_WINDOWS_WIDTH_JS;

    static {
        final String modalSizeJs =
                "with(top.$('.modal')){%s};" +
                        "with(top.$('.modal',top.$('iframe#container.stn-timer').contents())){%s};";
        SET_MODAL_WINDOWS_WIDTH_JS = String.format(modalSizeJs, "width(width())", "width(width())");
        RESTORE_MODAL_WINDOWS_WIDTH_JS = String.format(modalSizeJs, "css('width','')", "css('width','')");
    }

    @Override
    public void beforeScreenshot(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(SET_MODAL_WINDOWS_WIDTH_JS);
        DriverUtils.setWindowSize(driver,
                DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_WIDTH_JS) + BROWSER_GAP,
                DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_HEIGHT_JS) + BROWSER_GAP);
    }

    @Override
    public void afterScreenshot(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(RESTORE_MODAL_WINDOWS_WIDTH_JS);
    }

    @Override
    public BufferedImage cut(WebDriver driver, BufferedImage image) {
        return image.getSubimage(0, 0, image.getWidth(), image.getHeight() - 2);
    }
}