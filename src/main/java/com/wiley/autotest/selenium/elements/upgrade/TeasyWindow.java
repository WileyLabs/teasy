package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window.WindowMatcher;
import com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions.PageLoaded;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.util.Iterator;

/**
 * Created by vefimov on 30/05/2017.
 */
public class TeasyWindow implements Window {

    private OurFluentWait<WebDriver> fluentWait;
    private WebDriver driver;

    public TeasyWindow(WebDriver driver) {
        this.driver = driver;
        fluentWait = new OurFluentWait<>(driver);
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
}
