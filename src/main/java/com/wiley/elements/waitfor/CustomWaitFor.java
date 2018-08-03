package com.wiley.elements.waitfor;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyFluentWait;

import java.util.function.Function;

/**
 * Waiter for a custom condition represented by a function
 */
public class CustomWaitFor {

    private final SearchStrategy strategy;

    public CustomWaitFor(SearchStrategy strategy) {
        this.strategy = strategy;
    }

    public <T> void condition(Function<? super T, ?> condition, T input) {
        new TeasyFluentWait<>(input, strategy).waitFor(condition);
    }
}
