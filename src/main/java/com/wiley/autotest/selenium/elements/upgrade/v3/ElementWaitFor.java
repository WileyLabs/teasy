package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class ElementWaitFor {

    private OurFluentWait<WebDriver> fluentWait;
    private TeasyWebElement element;

    public ElementWaitFor(TeasyWebElement element) {
        this.element = element;
        fluentWait = new OurFluentWait<>(SeleniumHolder.getWebDriver());
    }

    public ElementWaitFor(TeasyWebElement element, SearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void displayed() {
        fluentWait.waitFor(new ElementDisplayed(element));
    }

    public void absent() {
        fluentWait.waitFor(new ElementAbsent(element));
    }

    public void text(String text) {
        fluentWait.waitFor(new ElementTextEquals(element, text));
    }

    public void attribute(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    public void attribute(String attributeName) {
        fluentWait.waitFor(new ElementHasAttribute(element, attributeName));
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        fluentWait.waitFor(new ElementAttributeNotContain(element, attributeName, value));
    }

    public void containsAttributeValue(String attributeName, String value) {
        fluentWait.waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName).contains(value);
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' contains attribute '%s' ith value '%s'", element.toString(), attributeName, value);
            }
        });
    }

    public void stale() {
        fluentWait.waitFor(new ElementStale(element));
    }

    public void clickable() {
        fluentWait.waitFor(new ElementClickable(element));
    }

    public void condition(Function<? super WebDriver, ?> condition) {
        fluentWait.waitFor(condition);
    }

}
