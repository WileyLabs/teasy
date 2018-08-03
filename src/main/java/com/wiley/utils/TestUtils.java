package com.wiley.utils;

public final class TestUtils {

    private TestUtils() {
    }

    /**
     * Simple sleep
     *
     * @param milliseconds - time to sleep in ms
     * @param reason       - explain why do you need to sleep. Be as specific as possible
     *                     as sleeps are considered as a bad practise and should only be added when
     *                     there is really no other solution
     */
    public static void sleep(long milliseconds, String reason) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignored) {
        }
    }
}
