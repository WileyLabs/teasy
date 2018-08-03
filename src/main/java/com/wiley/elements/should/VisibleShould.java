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

    public void beDisplayed() {
        waitFor(new ElementDisplayed(element));
    }

    public void beAbsent() {
        waitFor(new ElementAbsent(element));
    }

    public void haveText(String text) {
        waitFor(new ElementHasText(element, text));
    }

    public void haveAnyText() {
        waitFor(new ElementHasAnyText(element));
    }

    public void haveNoText() {
        waitFor(new ElementHasNoText(element));
    }

    public void containsText(String text) {
        waitFor(new ElementContainsText(element, text));
    }

    public void haveAttribute(String attributeName, String value) {
        waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    public void haveAttribute(String attributeName) {
        waitFor(new ElementHasAttribute(element, attributeName));
    }

    public void notHaveAttribute(String attributeName) {
        waitFor(new ElementNotHaveAttribute(element, attributeName));
    }

    public void customCondition(Function<WebDriver, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
