package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions.*;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class OurWaitFor {

    private OurFluentWait<WebDriver> fluentWait;
    private OurWebElement element;

    public OurWaitFor(OurWebElement element) {
        this.element = element;
        fluentWait = new OurFluentWait<>(SeleniumHolder.getWebDriver());
    }

    public OurWaitFor(OurWebElement element, OurSearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void displayed() {
        waitFor(new ElementDisplayed(element));
    }

    public void absent() {
        waitFor(new ElementAbsent(element));
    }

    public void text(String text) {
        waitFor(new ElementTextEquals(element, text));
    }

    public void attribute(String attributeName, String value) {
        waitFor(new ElementAttributeValue(element, attributeName, value));
    }

    public void attribute(String attributeName) {
        waitFor(new ElementHasAttribute(element, attributeName));
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        waitFor(new ElementAttributeNotContain(element, attributeName, value));
    }

    public void stale() {
        waitFor(new ElementStale(element));
    }

    public void clickable() {
        waitFor(new ElementClickable(element));
    }

    public void customCondition(Function<?, ?> condition) {
        new OurFluentWait(SeleniumHolder.getWebDriver()).waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
