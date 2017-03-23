package com.wiley.autotest.screenshots;

import com.wiley.autotest.selenium.driver.WebDriverDecorator;
import com.wiley.autotest.selenium.driver.events.listeners.ScreenshotWebDriverEventListener;
import com.wiley.autotest.utils.ExecutionUtils;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Created by mosadchiy on 26.05.2016.
 * It's a support class which has default implementation of methods in ScreenshotProvider interface
 * (otherwise you can override this methods in your tests). It's used in each screenshot-comparison test
 */
public class DefaultScreenshotProvider implements ScreenshotProvider {
    private static final int MIN_HEIGHT = 300;

    private static final String SET_HTML_HEIGHT_JS = "top.$('html').height(%s);";
    private static final String GET_HTML_HEIGHT_JS = "return top.$(document).height() - top.$('iframe[name=\"container\"]').height() + top.$('iframe[name=\"container\"]').contents().height();";

    private static final String SET_HTML_WIDTH_JS = "top.$('html').width(top.$('html').width());";
    private static final String GET_HTML_WIDTH_JS = "return top.$(document).width()";

    private static final String RESTORE_HTML_SIZE_JS = "top.$('html').css('height','').css('width', '');";

    private static final String SET_OVERFLOW_HIDDEN_JS;
    private static final String RESTORE_OVERFLOW_HIDDEN_JS;

    private static final String SET_BOTTOM_JS;
    private static final String RESTORE_BOTTOM_JS;

    static {
        final String overflowJs =
                "top.$('body').css('overflow','%s');" +
                        "top.$('.modal').css('overflow','%s');" +
                        "var content=top.$('iframe[name=\"container\"]').contents();" +
                        "top.$('body',content).css('overflow','%s');" +
                        "top.$('.modal',content).css('overflow','%s');";
        SET_OVERFLOW_HIDDEN_JS = String.format(overflowJs, "hidden", "hidden", "hidden", "hidden");
        RESTORE_OVERFLOW_HIDDEN_JS = String.format(overflowJs, "", "", "", "");

        final String bottomJs = "$(arguments[0]).css('bottom','%s')";
        SET_BOTTOM_JS = String.format(bottomJs, "auto");
        RESTORE_BOTTOM_JS = String.format(bottomJs, "");
    }

    private ScreenshotWebDriverEventListener eventListener;

    public void setEventListener(ScreenshotWebDriverEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public List<ScreenshotLocator> getIncludeLocators() {
        return null;
    }

    @Override
    public List<ScreenshotLocator> getExcludeLocators() {
        return null;
    }

    @Override
    public List<Substitution> getSubstitutions() {
        return null;
    }

    @Override
    public void beforeScreenshot(WebDriver driver) {
        if (eventListener != null) {
            eventListener.beforeScreenshot();
        }
        ((JavascriptExecutor) driver).executeScript(SET_OVERFLOW_HIDDEN_JS);
        WebDriver.Window window = driver.manage().window();
        window.setSize(new Dimension(window.getSize().width, MIN_HEIGHT));
        ((JavascriptExecutor) driver).executeScript(String.format(SET_HTML_HEIGHT_JS, getMaxWindowHeight(driver)));
        ((JavascriptExecutor) driver).executeScript(SET_HTML_WIDTH_JS);
        getBrowserDependency().beforeScreenshot(driver);
    }

    public void afterScreenshot(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript(RESTORE_HTML_SIZE_JS);
        ((JavascriptExecutor) driver).executeScript(RESTORE_OVERFLOW_HIDDEN_JS);
        getBrowserDependency().afterScreenshot(driver);
    }

    public int getMaxWindowHeight(WebDriver driver) {
        return Math.max(getIntExecuteScript(driver, GET_HTML_HEIGHT_JS), getModalMaxHeight(driver));
    }

    @Override
    public BufferedImage cut(WebDriver driver, BufferedImage image) {
        int width = DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_WIDTH_JS);
        int height = DefaultScreenshotProvider.getIntExecuteScript(driver, GET_HTML_HEIGHT_JS);
        if (image.getWidth() < width) {
            width = image.getWidth();
        }
        if (image.getHeight() < height) {
            height = image.getHeight();
        }
        if (width != image.getWidth() || height != image.getHeight()) {
            image = image.getSubimage(0, 0, width, height);
        }
        return getBrowserDependency().cut(driver, image);
    }

    public static int getIntExecuteScript(WebDriver driver, String js) {
        return ((Long) ((JavascriptExecutor) driver).executeScript(js)).intValue();
    }

    private BrowserDependency getBrowserDependency() {
        return ExecutionUtils.isIE() ? new ScreenshotByIE() : new ScreenshotBy();
    }

    private int getModalMaxHeight(WebDriver driver) {
        int maxHeight = getModalMaxHeightInFrame(driver);
        try {
            WebDriver decoratedDriver = ((WebDriverDecorator) driver).getDriver();
            decoratedDriver.switchTo().frame(driver.findElement(By.cssSelector("iframe[name='container']")));
            maxHeight = Math.max(maxHeight, getModalMaxHeightInFrame(driver));
            decoratedDriver.switchTo().defaultContent();
        } catch (NoSuchElementException e) {
            // Frame is not founded
        }
        return maxHeight;
    }

    private int getModalMaxHeightInFrame(WebDriver driver) {
        int maxHeight = 0;
        List<WebElement> modalWindows = driver.findElements(By.className("modal"));
        for (WebElement element : modalWindows) {
            if (element.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript(SET_BOTTOM_JS, element);
                maxHeight = Math.max(maxHeight, element.getSize().height);
                ((JavascriptExecutor) driver).executeScript(RESTORE_BOTTOM_JS, element);
            }
        }
        return maxHeight;
    }
}
