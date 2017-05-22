package com.wiley.autotest.selenium.elements.upgrade.v3;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.function.Function;

/**
 * Created by vefimov on 18/04/2017.
 */
class FluentWaitFinder extends FluentWait<WebDriver> {

    //default condition should stop execution in case of failure
    private boolean continueOnFailure = false;

    FluentWaitFinder(WebDriver input) {
        super(input);
    }

    void continueOnFailure() {
        continueOnFailure = true;
    }


    //todo proper error message should be automatically generated here and in FluentWaitCondition as well
    <T> T waitFor(Function<WebDriver, T> condition) {
        if (continueOnFailure) {
            try {
                return until(condition);
            } catch (Throwable ignoredAndContinue) {
                //todo add some logging here if necessary
                return null;
            }
        } else {
            return until(condition);
        }
    }


}
