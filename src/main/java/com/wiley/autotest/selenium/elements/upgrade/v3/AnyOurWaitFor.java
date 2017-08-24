package com.wiley.autotest.selenium.elements.upgrade.v3;

import com.wiley.autotest.selenium.context.OurSearchStrategy;

import java.util.function.Function;

/**
 * Created by vefimov on 26/04/2017.
 */
public class AnyOurWaitFor {

    public <T> void customCondition(Function<? super T, ?> condition, T input) {
        new OurFluentWait(input).waitFor(condition);
    }

    public <T> void customCondition(Function<? super T, ?> condition, T input, OurSearchStrategy strategy) {
        OurFluentWait wait = new OurFluentWait(input, strategy);
        wait.waitFor(condition);
    }

}
