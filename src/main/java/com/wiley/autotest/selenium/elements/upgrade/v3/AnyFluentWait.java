package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.support.ui.FluentWait;

import java.util.function.Function;

/**
 * General fluent waiter handling anything as an input parameter
 */
public class AnyFluentWait<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure = false;

    public AnyFluentWait(T input) {
        super(input);
    }

    public void setNullOnFailure(boolean nullOnFailure) {
        this.nullOnFailure = nullOnFailure;
    }

    public <R> R waitFor(Function<T, R> condition) {
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
