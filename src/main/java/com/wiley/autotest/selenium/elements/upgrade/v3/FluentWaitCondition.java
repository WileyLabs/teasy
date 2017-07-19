package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.support.ui.FluentWait;

import java.util.function.Function;

/**
 * Created by vefimov on 18/04/2017.
 */
public class FluentWaitCondition<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure = false;

    public FluentWaitCondition(T input) {
        super(input);
    }

    void setNullOnFailure(boolean nullOnFailure) {
        this.nullOnFailure = nullOnFailure;
    }

    void waitFor(Function<T, Boolean> condition) {
        if (nullOnFailure) {
            try {
                until(condition);
            } catch (Throwable ignoredAndContinue) {
                //todo add some logging here
            }
        } else {
            until(condition);
        }
    }


}
