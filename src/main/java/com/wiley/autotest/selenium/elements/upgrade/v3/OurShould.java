package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class OurShould {

    private WebDriverFluentWait fluentWait;
    private OurWebElement element;

    public OurShould(OurWebElement element) {
        this.element = element;
        fluentWait = new WebDriverFluentWait(SeleniumHolder.getWebDriver());
    }

    public OurShould(OurWebElement element, OurSearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void beDisplayed() {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.isDisplayed();
            }

            @Override
            public String toString() {
                return String.format("element '%s' should be displayed", element.toString());
            }
        });
    }

    public void beAbsent() {
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
                return String.format("element '%s' should be absent", element.toString());
            }
        });
    }

    public void haveText(String text) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getText().equals(text);
            }

            @Override
            public String toString() {
                return String.format("element '%s' should have text '%s'", element.toString(), text);
            }
        });
    }

    public void haveAttribute(String attributeName, String value) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName).equals(value);
            }

            @Override
            public String toString() {
                return String.format("element '%s' should have attribute '%s' with value '%s'", element.toString(), attributeName, value);
            }
        });
    }

    public void haveAttribute(String attributeName) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName) != null;
            }

            @Override
            public String toString() {
                return String.format("element '%s' should have attribute '%s'", element.toString(), attributeName);
            }
        });
    }

    public void notHaveAttribute(String attributeName) {
        waitFor(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return element.getAttribute(attributeName) == null;
            }

            @Override
            public String toString() {
                return String.format("element '%s' should not have attribute '%s'", element.toString(), attributeName);
            }
        });
    }

    public void customCondition(Function<WebDriver, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<WebDriver, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
