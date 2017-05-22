package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.support.ui.FluentWait;

import java.util.function.Function;

/**
 * Created by vefimov on 18/04/2017.
 */
class FluentWaitCondition<T> extends FluentWait<T> {

    //default condition should stop execution in case of failure
    private boolean continueOnFailure = false;

    FluentWaitCondition(T input) {
        super(input);
    }

    void continueOnFailure() {
        continueOnFailure = true;
    }

    void waitFor(Function<T, Boolean> condition) {
        if (continueOnFailure) {
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
