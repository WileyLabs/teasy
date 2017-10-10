package com.wiley.autotest.actions;

import com.wiley.autotest.utils.TestUtils;

/**
 * Repeats action limited number of times until a boolean condition is true
 * waits for a timeout between attempts to perform an action
 * <p>
 * If the condition does not become true after all attempts - AssertionError is thrown
 */
public class RepeatableAction {
    private final Actions action;
    private final Conditions condition;
    private final int numberOfAttempts;
    private final int millisecondsBetweenAttempts;
    private int attemptCounter = 0;

    /**
     * By default will try 5 types and sleep 3 seconds after each attempt
     *
     * @param action    - any function you want yo be performed
     * @param condition - any boolean function to check after action
     */
    public RepeatableAction(Actions action, Conditions condition) {
        this(action, condition, 5, 3000);
    }

    public RepeatableAction(Actions action, Conditions condition, int numberOfAttempts, int millisecondsBetweenAttempts) {
        this.action = action;
        this.condition = condition;
        this.numberOfAttempts = numberOfAttempts;
        this.millisecondsBetweenAttempts = millisecondsBetweenAttempts;
    }

    public void perform() {
        attemptCounter++;
        action.execute();

        if (condition.isTrue()) {
            attemptCounter = 0;
        } else {
            if (attemptCounter > numberOfAttempts) {
                attemptCounter = 0;
                TestUtils.fail("Unable to perform actions after " + numberOfAttempts + " attempts");
            }
            TestUtils.waitForSomeTime(millisecondsBetweenAttempts, "Sleeping inside action repeater");
            perform();
        }
    }
}
