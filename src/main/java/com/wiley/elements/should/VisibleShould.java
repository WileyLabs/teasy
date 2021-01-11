package com.wiley.elements.should;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.conditions.element.*;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.function.Function;

/**
 * Assertions for Visible Element
 */
public class VisibleShould implements Should {

    private final TeasyFluentWait<WebDriver> fluentWait;
    private final TeasyElement element;

    public VisibleShould(TeasyElement element, TeasyFluentWait<WebDriver> fluentWait) {
        this.element = element;
        this.fluentWait = fluentWait;
    }

    public VisibleShould(TeasyElement element, SearchStrategy strategy, TeasyFluentWait<WebDriver> fluentWait) {
        this(element, fluentWait);
        this.fluentWait.withTimeout(Duration.ofSeconds(strategy.getCustomTimeout()));
        this.fluentWait.pollingEvery(Duration.of(strategy.getPoolingEvery(), strategy.getUnit()));
    }

    @Override
    public void beDisplayed() {
        waitFor(new ElementDisplayed(element));
    }

    @Override
    public void beAbsent() {
        waitFor(new ElementAbsent(element));
    }

    @Override
    public void haveText(String text) {
        waitFor(new ElementHasText(element, text));
    }

    @Override
    public void haveAnyText() {
        waitFor(new ElementHasAnyText(element));
    }

    @Override
    public void haveNoText() {
        waitFor(new ElementHasNoText(element));
    }

    @Override
    public void containsText(String text) {
        waitFor(new ElementContainsText(element, text));
    }

    @Override
    public void haveAttribute(String attributeName, String value) {
        waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    @Override
    public void haveAttribute(String attributeName) {
        waitFor(new ElementHasAttribute(element, attributeName));
    }

    @Override
    public void notHaveAttribute(String attributeName) {
        waitFor(new ElementNotHaveAttribute(element, attributeName));
    }

    @Override
    public void customCondition(Function<WebDriver, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
