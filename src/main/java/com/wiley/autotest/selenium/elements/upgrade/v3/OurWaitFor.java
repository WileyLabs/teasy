package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class OurWaitFor {

    private WebDriverFluentWait fluentWait;
    private OurWebElement element;

    public OurWaitFor(OurWebElement element) {
        this.element = element;
        fluentWait = new WebDriverFluentWait(SeleniumHolder.getWebDriver());
    }

    public OurWaitFor(OurWebElement element, OurSearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void displayed() {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.isDisplayed();
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' displayed", element.toString());
            }
        });
    }

    public void absent() {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return !element.isDisplayed();
                } catch (Throwable ignored) {
                    //in case of any exception in OurWebElement considering that element is absent
                    return true;
                }
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' absent", element.toString());
            }
        });
    }

    public void text(String text) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getText().equals(text);
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' have text '%s'", element.toString(), text);
            }
        });
    }

    public void attribute(String attributeName, String value) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName).equals(value);
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' have attribute '%s' with value '%s'", element.toString(), attributeName, value);
            }
        });
    }

    public void attribute(String attributeName) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName) != null;
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' have attribute '%s'", element.toString(), attributeName);
            }
        });
    }

    public void notContainsAttributeValue(String attributeName, String value) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !element.getAttribute(attributeName).contains(value);
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' not contains attribute '%s' ith value '%s'", element.toString(), attributeName, value);
            }
        });
    }

    public void containsAttributeValue(String attributeName, String value) {
        waitFor(new ExpectedCondition<Boolean>() {
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
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    element.isEnabled();
                    return false;
                } catch (StaleElementReferenceException expectedWhenStale) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' stale", element.toString());
            }
        });
    }

    public void clickable() {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.isDisplayed() && element.isEnabled();
            }

            @Override
            public String toString() {
                return String.format("wait for element '%s' clickable", element.toString());
            }
        });
    }

    public void customCondition(Function<?, ?> condition) {
        new AnyFluentWait(SeleniumHolder.getWebDriver()).waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
