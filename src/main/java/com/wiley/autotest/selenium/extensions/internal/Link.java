package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;

import static com.wiley.autotest.utils.JsActions.executeScript;

public class Link extends AbstractElement {
    public Link(final TeasyElement element) {
        super(element);
    }

    public void click() {
        getWrappedElement().click();
    }

    public void clickWithJavaScript() {
        executeScript("arguments[0].click();", getWrappedElement());
    }

    public void clickWithReload() {
        int iterationCount = 0;
        TeasyElement element = getWrappedElement();
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
            TestUtils.waitForSomeTime(3000,
                    "Sleeping as we don't know what happened. Then - retrying.");
            iterationCount++;
        }
        clickWithJavaScript();
        try {
            element.waitFor().stale();
            return;
        } catch (TimeoutException ignored) {
        }
        throw new WebDriverException("Link is not reloaded after click");
    }

    public boolean isClickable() {
        return true;
    }
}
