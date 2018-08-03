package com.wiley.elements;

import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * General fluent waiter handling anything as an input parameter
 */
public class TeasyFluentWait<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure;

    public TeasyFluentWait(T input, SearchStrategy strategy) {
        super(input);
        withTimeout(Duration.ofSeconds(strategy.getCustomTimeout()));
        pollingEvery(Duration.of(strategy.getPoolingEvery(), strategy.getUnit()));
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
