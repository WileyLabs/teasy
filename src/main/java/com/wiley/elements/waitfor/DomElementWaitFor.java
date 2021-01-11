package com.wiley.elements.waitfor;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.element.*;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Function;

/**
 * Condition waiter for DOM Element
 */
public class DomElementWaitFor implements ElementWaitFor {

    private final TeasyFluentWait<WebDriver> wait;
    private final TeasyElement element;

    public DomElementWaitFor(TeasyElement element, TeasyFluentWait<WebDriver> wait) {
        this.element = element;
        this.wait = wait;
    }

    public DomElementWaitFor(TeasyElement element, SearchStrategy strategy, TeasyFluentWait<WebDriver> wait) {
        this(element, wait);

        this.wait.withTimeout(Duration.ofSeconds(strategy.getCustomTimeout()));
        this.wait.pollingEvery(Duration.of(strategy.getPoolingEvery(), strategy.getUnit()));
    }

    @Override
    public void displayed() {
        wait.waitFor(new ElementDisplayed(element));
    }

    @Override
    public void absent() {
        wait.waitFor(new ElementAbsent(element));
    }

    @Override
    public void text(String text) {
        wait.waitFor(new ElementHasText(element, text));
    }

    @Override
    public void attribute(String attributeName, String value) {
        wait.waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    @Override
    public void attribute(String attributeName) {
        wait.waitFor(new ElementHasAttribute(element, attributeName));
    }

    @Override
    public void notContainsAttributeValue(String attributeName, String value) {
        wait.waitFor(new ElementAttributeNotContain(element, attributeName, value));
    }

    @Override
    public void containsAttributeValue(String attributeName, String value) {
        wait.waitFor(new ElementAttributeContain(element, attributeName, value));
    }

    @Override
    public void stale() {
        wait.waitFor(new ElementStale(element));
    }

    @Override
    public void clickable() {
        wait.waitFor(new ElementClickable(element));
    }

    @Override
    public void condition(Function<? super WebDriver, ?> condition) {
        wait.waitFor(condition);
    }

}
