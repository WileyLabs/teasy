package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class OurShould {

    private FluentWaitCondition<OurWebElement> fluentWait;

    public OurShould(OurWebElement element) {
        fluentWait = new FluentWaitCondition<>(element);
    }

    public OurShould(OurWebElement element, OurSearchStrategy strategy) {
        this(element);
        fluentWait.withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        fluentWait.pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
    }

    public void beDisplayed() {
        fluentWait.waitFor(OurWebElement::isDisplayed);
    }

    public void beAbsent() {
        waitFor((element) -> {
            try {
                return !element.isDisplayed();
            } catch (Throwable ignored) {
                //in case of any exception in OurWebElement considering that element is absent
                return true;
            }
        });
    }

    public void haveText(String text) {
        waitFor((element) -> element.getText().equals(text));
    }

    public void haveAttribute(String attributeName, String value) {
        waitFor((element) -> element.getAttribute(attributeName).equals(value));
    }

    public void notHaveAttribute(String attributeName) {
        waitFor((element) -> element.getAttribute(attributeName) == null);
    }

    public void customCondition(Function<OurWebElement, Boolean> condition) {
        waitFor(condition);
    }

    private void waitFor(Function<OurWebElement, Boolean> condition) {
        fluentWait.waitFor(condition);
    }
}
