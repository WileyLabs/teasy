package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.TeasyElementProvider;
import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.upgrade.CustomWebElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractWebContainer extends TeasyElementProvider implements WebContainer {

    private static final long WAIT_TIME_OUT_IN_SECONDS = 10;

    @DoNotSearch
    private TeasyElement wrappedElement;
    private ErrorSender errorSender;

    @Override
    public void init(final TeasyElement wrappedElement) {
        this.wrappedElement = wrappedElement;
        elementFinder = new WebDriverAwareElementFinder(getDriver(), new WebDriverWait(getDriver(), WAIT_TIME_OUT_IN_SECONDS, SLEEP_IN_MILLISECONDS));
    }

    public TeasyElement getWrappedElement() {
        return wrappedElement;
    }

    public ErrorSender getErrorSender() {
        return errorSender;
    }

    @Override
    public final boolean isVisible() {
        return wrappedElement.isDisplayed();
    }

    @Override
    public String getText() {
        return wrappedElement.getText();
    }

    @Override
    public void setErrorSender(final ErrorSender errorSender) {
        this.errorSender = errorSender;
    }

    @Override
    public TeasyElement getWrappedWebElement() {
        return wrappedElement;
    }

    protected WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }

    protected Object executeScript(final String script, final Object... args) {
        castToCustomWebElement(args);
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    private static void castToCustomWebElement(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof CustomWebElement) {
                args[i] = ((CustomWebElement) args[i]).getWrappedWebElement();
            }
        }
    }

    protected void clickOkButtonInConfirm(int timeoutForConfirm) {
        try {
            Alert alert = waitForAlertPresence(timeoutForConfirm);
            alert.accept();
        } catch (Exception ignored) {
        }
    }

    protected Alert waitForAlertPresence(int timeoutForAlert) {
        return (new WebDriverWait(getDriver(), timeoutForAlert, SLEEP_IN_MILLISECONDS)).until(ExpectedConditions.alertIsPresent());
    }
}
