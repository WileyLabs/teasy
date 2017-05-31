package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.OurElementProvider;
import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.upgrade.IOurWebElement;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractWebContainer extends OurElementProvider implements WebContainer {

    private static final long WAIT_TIME_OUT_IN_SECONDS = 10;

    @DoNotSearch
    private WebElement wrappedElement;
    private ErrorSender errorSender;

    @Override
    public void init(final WebElement wrappedElement) {
        this.wrappedElement = wrappedElement;
        elementFinder = new WebDriverAwareElementFinder(getDriver(), new WebDriverWait(getDriver(), WAIT_TIME_OUT_IN_SECONDS, SLEEP_IN_MILLISECONDS));
    }

    public WebElement getWrappedElement() {
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
    public WebElement getWrappedWebElement() {
        return wrappedElement;
    }

    protected WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }

    protected Object executeScript(final String script, final Object... args) {
        castToOurWebElement(args);
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    private static void castToOurWebElement(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof IOurWebElement) {
                args[i] = ((IOurWebElement) args[i]).getWrappedWebElement();
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
