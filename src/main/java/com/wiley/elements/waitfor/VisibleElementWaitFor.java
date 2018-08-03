package com.wiley.elements.waitfor;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.element.*;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Function;

/**
 * Condition waiter for Visible Element
 */
public class VisibleElementWaitFor implements ElementWaitFor {

    private final TeasyFluentWait<WebDriver> wait;
    private final TeasyElement element;

    public VisibleElementWaitFor(TeasyElement element, TeasyFluentWait<WebDriver> wait) {
        this.element = element;
        this.wait = wait;
    }

    public VisibleElementWaitFor(TeasyElement element, SearchStrategy strategy, TeasyFluentWait<WebDriver> wait) {
        this(element, wait);
        this.wait.withTimeout(Duration.ofSeconds(strategy.getCustomTimeout()));
        this.wait.pollingEvery(Duration.of(strategy.getPoolingEvery(), strategy.getUnit()));
    }

    public void displayed() {
        wait.waitFor(new ElementDisplayed(element));
    }

    public void absent() {
        wait.waitFor(new ElementAbsent(element));
    }

    public void text(String text) {
        wait.waitFor(new ElementHasText(element, text));
    }

    public void attribute(String attributeName, String value) {
        wait.waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    public void attribute(String attributeName) {
        wait.waitFor(new ElementHasAttribute(element, attributeName));
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        wait.waitFor(new ElementAttributeNotContain(element, attributeName, value));
    }

    public void containsAttributeValue(String attributeName, String value) {
        wait.waitFor(new ElementAttributeContain(element, attributeName, value));
    }

    public void stale() {
        wait.waitFor(new ElementStale(element));
    }

    public void clickable() {
        wait.waitFor(new ElementClickable(element));
    }

    public void condition(Function<? super WebDriver, ?> condition) {
        wait.waitFor(condition);
    }

}
