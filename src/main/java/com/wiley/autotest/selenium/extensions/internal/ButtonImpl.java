package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Button;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

class ButtonImpl extends AbstractEnabledElement implements Button {
    protected ButtonImpl(final OurWebElement wrappedElement) {
        super(wrappedElement);
    }

    protected ButtonImpl(final OurWebElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedWebElement());
    }

    @Override
    public void click() {
        getWrappedWebElement().click();
    }

    @Override
    public void clickWithReload() {
        int iterationCount = 0;
        OurWebElement element = getWrappedWebElement();
        while (iterationCount < 3) {
            element.click();
            try {
                element.waitFor().stale();
                return;
            } catch (TimeoutException ignored) {
            }
            //To avoid WebDriverException: unknown error: Runtime.evaluate threw exception: Error: element is not attached to the page document.
            //This happened in chrome after staleness element.
            try {
                element.isEnabled();
            } catch (WebDriverException ignored) {
                return;
            }
            TestUtils.waitForSomeTime(3000, EXPLANATION_MESSAGE_FOR_WAIT);
            iterationCount++;
        }
        clickWithJavaScript();
        try {
            element.waitFor().stale();
            return;
        } catch (TimeoutException ignored) {
        }
        throw new WebDriverException("Button is not reloaded after click");
    }

    @Override
    public boolean isClickable() {
        return isEnabled();
    }

    @Override
    public OurWebElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
