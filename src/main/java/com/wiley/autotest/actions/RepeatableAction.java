package com.wiley.autotest.actions;

import com.wiley.autotest.services.StopTestExecutionException;
import com.wiley.autotest.utils.TestUtils;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Repeats action limited number of times until a boolean condition is true
 * waits for a timeout between attempts to perform an action
 * <p>
 * If the condition does not become true after all attempts - AssertionError is thrown
 */
public class RepeatableAction<T> {

    private Actions action;
    private Conditions condition;
    private Supplier<Boolean> supplier;
    private final int numberOfAttempts;
    private final int millisecondsBetweenAttempts;
    private int attemptCounter = 0;
    private String errorMessage;
    private Supplier<T> output;
    private Function<T, Boolean> dependedCondition;

    /**
     * By default will try 5 types and sleep 3 seconds after each attempt
     *
     * @param action    - any function you want to be performed
     * @param condition - any boolean function to check after action
     */
    public RepeatableAction(Actions action, Conditions condition) {
        this(action, condition, 5, 3000);
    }

    /**
     * By default will try 5 types and sleep 3 seconds after each attempt
     *
     * @param supplier - any boolean function you want to be performed
     */
    public RepeatableAction(Supplier<Boolean> supplier) {
        this(supplier, 5, 3000);
    }

    public RepeatableAction(Supplier<Boolean> supplier, int numberOfAttempts, int millisecondsBetweenAttempts) {
        this.supplier = supplier;
        this.numberOfAttempts = numberOfAttempts;
        this.millisecondsBetweenAttempts = millisecondsBetweenAttempts;
    }

    public RepeatableAction(Actions action, Conditions condition, int numberOfAttempts, int millisecondsBetweenAttempts) {
        this.action = action;
        this.condition = condition;
        this.numberOfAttempts = numberOfAttempts;
        this.millisecondsBetweenAttempts = millisecondsBetweenAttempts;
    }

    public RepeatableAction(Supplier<T> output, Function<T, Boolean> condition, int millisecondsBetweenAttempts) {
        this(output, condition, 5, millisecondsBetweenAttempts);
    }

    public RepeatableAction(Supplier<T> output, Function<T, Boolean> condition, int numberOfAttempts, int millisecondsBetweenAttempts) {
        this.output = output;
        this.dependedCondition = condition;
        this.numberOfAttempts = numberOfAttempts;
        this.millisecondsBetweenAttempts = millisecondsBetweenAttempts;
    }

    public RepeatableAction<T> message(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public void perform() {
        attemptCounter++;
        boolean localCondition;
        if (supplier != null) {
            localCondition = supplier.get();
        } else {
            action.execute();
            localCondition = condition.isTrue();
        }
        if (localCondition) {
            attemptCounter = 0;
        } else {
            if (attemptCounter > numberOfAttempts) {
                attemptCounter = 0;
                throw new StopTestExecutionException(getErrorMessage());
            }
            TestUtils.waitForSomeTime(millisecondsBetweenAttempts, "Sleeping inside action repeater");
            perform();
        }
    }

    public T performAndGet() {
        T result = null;
        for (int i = 0; i < numberOfAttempts; i++) {
            T localResult = output.get();
            if (dependedCondition.apply(localResult)) {
                result = localResult;
                break;
            } else {
                TestUtils.waitForSomeTime(millisecondsBetweenAttempts, "Sleeping inside action repeater");
            }
        }
        if (result == null) {
            throw new StopTestExecutionException(getErrorMessage());
        } else return result;
    }

    private String getErrorMessage() {
        String baseMessage = "Unable to perform actions after " + numberOfAttempts + " attempts";
        if (errorMessage != null) {
            baseMessage = baseMessage.concat("\n").concat(errorMessage);
        }
        return baseMessage;
    }
}
