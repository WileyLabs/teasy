package com.wiley.autotest.selenium.elements.upgrade.waitfor;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.element.*;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Condition waiter for DOM Element
 */
public class DomElementWaitFor implements ElementWaitFor {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElement element;

    public DomElementWaitFor(TeasyElement element, TeasyFluentWait<WebDriver> fluentWait) {
        this.element = element;
        this.fluentWait = fluentWait;
    }

    public DomElementWaitFor(TeasyElement element, SearchStrategy strategy, TeasyFluentWait<WebDriver> fluentWait) {
        this(element, fluentWait);
        this.fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        this.fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
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
        fluentWait.waitFor(new ElementAttributeContain(element, attributeName, value));
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
