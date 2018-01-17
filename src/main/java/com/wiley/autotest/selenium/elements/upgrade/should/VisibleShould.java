package com.wiley.autotest.selenium.elements.upgrade.should;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyFluentWait;
import com.wiley.autotest.selenium.elements.upgrade.conditions.element.*;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
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
        this.fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        this.fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void beDisplayed() {
        waitFor(new ElementDisplayed(element));
    }

    public void beAbsent() {
        waitFor(new ElementAbsent(element));
    }

    public void haveText(String text) {
        waitFor(new ElementTextEquals(element, text));
    }

    public void haveAnyText() {
        waitFor(new ElementHasAnyText(element));
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
