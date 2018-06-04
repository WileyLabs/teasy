package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.actions.Actions;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.elements.upgrade.conditions.PageLoaded;
import com.wiley.autotest.selenium.elements.upgrade.conditions.window.WindowMatcher;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.CustomWaitFor;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;

import java.net.URL;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Represents browser window
 */
public class TeasyWindow implements Window {

    private TeasyFluentWait<WebDriver> fluentWait;
    private WebDriver driver;

    public TeasyWindow(WebDriver driver) {
        this.driver = driver;
        fluentWait = new TeasyFluentWait<>(driver);
    }

    @Override
    public void switchToLast() {
        Iterator<String> iterator = driver.getWindowHandles().iterator();
        String window = null;
        while (iterator.hasNext()) {
            window = iterator.next();
        }
        driver.switchTo().window(window);
    }

    /**
     * To be sure that new window appeared after an action.
     * For example, after clicking on a link with new
     * window target.
     *
     * Example usage: switchToLastAfter(() -> element.click());
     *
     * @param action - an action to do before new window appeared
     *               and before switching.
     */
    @Override
    public void switchToLastAfter(Actions action) {
        int windowCount = driver.getWindowHandles().size();
        action.execute();
        new CustomWaitFor().condition(newWindowAppeared(), windowCount);
        switchToLast();
    }

    /**
     * To be sure that new window appeared after click on element.
     * For example, after clicking on a link with new
     * window target.
     *
     * @param element - an element to click to new window switch.
     */
    @Override
    public void switchToLastAfter(TeasyElement element) {
        switchToLastAfter(element::click);
    }

    @Override
    public void switchTo(WindowMatcher matcher) {
        fluentWait.waitFor(matcher.get().findAndSwitch());
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void back() {
        driver.navigate().back();
    }

    @Override
    public void forward() {
        driver.navigate().forward();
    }

    @Override
    public void refresh() {
        driver.navigate().refresh();
    }

    @Override
    public void navigateTo(String url) {
        driver.navigate().to(url);
    }

    @Override
    public void navigateTo(URL url) {
        driver.navigate().to(url);
    }

    @Override
    public void waitForScriptsToLoad() {
        if (driver instanceof AppiumDriver) {
            return;
        }
        try {
            fluentWait.waitFor(new PageLoaded());
        } catch (TimeoutException expected) {
            String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            new Report("*****ERROR***** TimeoutException occurred while waiting for page to load! return document.readyState value is '"
                    + readyState + "' But expected to be 'complete'").jenkins();
        } catch (WebDriverException e) {
            new Report("*****ERROR***** WebDriverException occurred while waiting for page to load!").jenkins();
        }
    }

    @Override
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public void changeSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public void maximize() {
        driver.manage().window().maximize();
    }

    @Override
    public void scrollTo(TeasyElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private Function<Integer, Boolean> newWindowAppeared() {
        return count -> {
            int currentWindowsCount = driver.getWindowHandles().size();
            return count != currentWindowsCount;
        };
    }
}
