package com.wiley.elements;

import com.wiley.elements.conditions.PageLoaded;
import com.wiley.elements.conditions.window.WindowMatcher;
import com.wiley.utils.Report;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;

import java.net.URL;
import java.util.Iterator;

/**
 * Represents browser window
 */
public class TeasyWindow implements Window {

    private TeasyFluentWait<WebDriver> fluentWait;
    private WebDriver driver;

    public TeasyWindow(WebDriver driver) {
        this.driver = driver;
        fluentWait = new TeasyFluentWait<>(driver, new SearchStrategy());
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
            String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState")
                    .toString();
            Report.jenkins("*****ERROR***** TimeoutException occurred while waiting for page to load! " +
                    "document.readyState value is '" + readyState + "' But expected to be 'complete'");
        } catch (WebDriverException e) {
            Report.jenkins("*****ERROR***** WebDriverException occurred while waiting for page to load!");
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
}
