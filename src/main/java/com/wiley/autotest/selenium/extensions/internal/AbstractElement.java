package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.Element;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractElement implements Element {

    private static final long WAIT_TIME_OUT_IN_SECONDS = 10;
    private TeasyWebElement wrappedElement;
    private By locator;
    private ErrorSender errorSender;
    private ElementFinder elementFinder;
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractElement.class);
    //TODO VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    private static final long SLEEP_IN_MILLISECONDS = 1000;
    protected static final String EXPLANATION_MESSAGE_FOR_WAIT = "Wait for reload element";

    protected AbstractElement(final TeasyWebElement element) {
        this.wrappedElement = element;
        init(getDriver(), WAIT_TIME_OUT_IN_SECONDS);
    }

    protected AbstractElement(final TeasyWebElement element, final By locator) {
        this(element);
        this.locator = locator;
    }

    public void init(final WebDriver driver, Long timeout) {
        elementFinder = new WebDriverAwareElementFinder(driver, new WebDriverWait(driver, timeout, SLEEP_IN_MILLISECONDS));
    }

    //TODO VE rename this to something more meaningful
    public TeasyWebElement getWrappedElement() {
        return wrappedElement;
    }

    public ErrorSender getErrorSender() {
        return errorSender;
    }

    @Override
    public boolean isVisible() {
        return wrappedElement.isDisplayed();
    }

    @Override
    public String getText() {
        return wrappedElement.getText();
    }

    @Override
    public void setErrorSender(ErrorSender sender) {
        this.errorSender = sender;
    }

    @Override
    public TeasyWebElement getWrappedWebElement() {
        return wrappedElement;
    }


    protected void scrollIntoView(WebElement element) {
        executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }

    public By getLocator() {
        return locator;
    }

    public ElementFinder getElementFinder() {
        return elementFinder;
    }

    protected Object executeScript(final String script, final Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }
}
