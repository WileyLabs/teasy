package com.wiley.driver.frames;

import com.wiley.elements.TeasyElement;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.List;
import java.util.Set;

public abstract class WebDriverDecorator extends EventFiringWebDriver implements WebDriver, JavascriptExecutor, TakesScreenshot, HasInputDevices {

    private final WebDriver driver;

    public WebDriverDecorator(final WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    @Override
    public Object executeScript(final String script, final Object... args) {
        castToTeasyElement(args);
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    @Override
    public Object executeAsyncScript(final String script, final Object... args) {
        castToTeasyElement(args);
        return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
    }

    private void castToTeasyElement(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof TeasyElement) {
                args[i] = ((TeasyElement) args[i]).getWrappedWebElement();
            }
        }
    }

    @Override
    public <X> X getScreenshotAs(final OutputType<X> target) throws WebDriverException {
        if (driver.getClass() == RemoteWebDriver.class) {
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            return ((TakesScreenshot) augmentedDriver).getScreenshotAs(target);
        } else {
            return ((TakesScreenshot) driver).getScreenshotAs(target);
        }
    }

    @Override
    public void get(final String url) {
        driver.get(url);
    }

    @Override
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return driver.getTitle();
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return driver.findElements(by);
    }

    @Override
    public WebElement findElement(final By by) {
        return driver.findElement(by);
    }

    @Override
    public String getPageSource() {
        return driver.getPageSource();
    }

    @Override
    public void close() {
        driver.close();
    }

    @Override
    public void quit() {
        driver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return driver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return driver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return driver.navigate();
    }

    @Override
    public Options manage() {
        return driver.manage();
    }

    @Override
    public Keyboard getKeyboard() {
        return ((HasInputDevices) driver).getKeyboard();
    }

    @Override
    public Mouse getMouse() {
        return ((HasInputDevices) driver).getMouse();
    }
}
