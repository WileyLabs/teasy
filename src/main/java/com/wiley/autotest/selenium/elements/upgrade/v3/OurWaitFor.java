package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class OurWaitFor {

    private FluentWaitCondition<WebElement> fluentWait;

    public OurWaitFor(WebElement element) {
        fluentWait = new FluentWaitCondition<>(element);
    }

    public OurWaitFor(WebElement element, SearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void displayed() {
        waitFor(WebElement::isDisplayed);
    }

    public void absent() {
        waitFor((element) -> {
            try {
                return !element.isDisplayed();
            } catch (Throwable ignored) {
                //in case of any exception in OurWebElement considering that element is absent
                return true;
            }
        });
    }

    public void text(String text) {
        waitFor((element) -> element.getText().equals(text));
    }

    public void attribute(String attributeName, String value) {
        waitFor((element) -> element.getAttribute(attributeName).equals(value));
    }

    public void stale() {
        waitFor((element) -> {
            try {
                element.isEnabled();
                return false;
            } catch (StaleElementReferenceException expectedWhenStale) {
                return true;
            }
        });
    }

    public void clickable() {
        waitFor((element -> element.isDisplayed() && element.isEnabled()));
    }


    public void notContainsAttributeValue(String attributeName, String value) {
       waitFor(element -> !element.getAttribute(attributeName).contains(value));
    }

    /**
     * TODO: ve consider making this method to accept Function<?, Boolean>
     */
    public void customCondition(Function<WebElement, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebElement, Boolean> condition) {
        fluentWait.waitFor(condition);
    }

}
