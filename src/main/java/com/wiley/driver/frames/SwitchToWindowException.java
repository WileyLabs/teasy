package com.wiley.driver.frames;

/**
 * User: dfedorov
 * Date: 9/5/12
 * Time: 12:57 PM
 */
public class SwitchToWindowException extends RuntimeException {
    public SwitchToWindowException() {
    }

    public SwitchToWindowException(String message) {
        super(message);
    }

    public SwitchToWindowException(String message, Throwable cause) {
        super(message, cause);
    }
}
