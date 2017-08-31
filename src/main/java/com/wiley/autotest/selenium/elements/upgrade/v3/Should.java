package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.v3.expectedconditions.*;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class Should {

    private OurFluentWait<WebDriver> fluentWait;
    private TeasyElement element;

    public Should(TeasyElement element) {
        this.element = element;
        fluentWait = new OurFluentWait<>(SeleniumHolder.getWebDriver());
    }

    public Should(TeasyElement element, SearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
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
