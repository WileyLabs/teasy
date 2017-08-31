package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.SearchStrategy;

import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class CustomWaitFor {

    public <T> void condition(Function<? super T, ?> condition, T input) {
        new TeasyFluentWait(input).waitFor(condition);
    }

    public <T> void condition(Function<? super T, ?> condition, T input, SearchStrategy strategy) {
        new TeasyFluentWait(input, strategy).waitFor(condition);
    }

}
