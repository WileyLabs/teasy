package com.wiley.autotest.selenium.elements.upgrade;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.wiley.autotest.selenium.context.SearchStrategy;
import org.jetbrains.annotations.Nullable;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * General fluent waiter handling anything as an input parameter
 */
public class TeasyFluentWait<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure = false;

    public TeasyFluentWait(T input) {
        this(input, new SearchStrategy());
    }

    public TeasyFluentWait(T input, SearchStrategy strategy) {
        super(input);
        withTimeout(strategy.getCustomTimeout(), TimeUnit.SECONDS);
        pollingEvery(strategy.getPoolingEvery(), strategy.getUnit());
        this.nullOnFailure = strategy.isNullOnFailure();
    }

    @Nullable
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
