package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.function.Function;

/**
 * Created by vefimov on 18/04/2017.
 */
public class FluentWaitCondition extends FluentWait<WebDriver> {

    //default condition should stop execution in case of failure
    private boolean nullOnFailure = false;

    public FluentWaitCondition(WebDriver input) {
        super(input);
    }

    void setNullOnFailure(boolean nullOnFailure) {
        this.nullOnFailure = nullOnFailure;
    }

    <T> void waitFor(Function<WebDriver, T> condition) {
        try {
            until(condition);
        } catch (Throwable ignoredAndContinue) {
            if (nullOnFailure) {
                //todo add some logging here if necessary
            } else {
                throw new AssertionError("Unable to perform: " + condition.toString());
            }
        }
    }
}
