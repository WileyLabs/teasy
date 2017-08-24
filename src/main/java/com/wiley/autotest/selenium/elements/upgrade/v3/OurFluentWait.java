package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.OurSearchStrategy;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * General fluent waiter handling anything as an input parameter
 */
public class OurFluentWait<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure = false;

    public OurFluentWait(T input) {
        super(input);
    }

    public OurFluentWait(T input, OurSearchStrategy strategy) {
        super(input);
        withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
        this.nullOnFailure = strategy.isNullOnFailure();
    }

    public <R> R waitFor(Function<? super T, R> condition) {
        try {
            return until(condition);
        } catch (Throwable ignoredAndContinue) {
            if (nullOnFailure) {
                //todo add some logging here if necessary
                return null;
            } else {
                throw new AssertionError("Condition: " + condition.toString() + " failed!");
            }
        }
    }
}
