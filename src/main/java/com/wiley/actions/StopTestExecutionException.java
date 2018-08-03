package com.wiley.actions;

/**
 * User: ntyukavkin
 * Date: 10.02.2016
 * Time: 14:32
 */
public class StopTestExecutionException extends RuntimeException {

    public StopTestExecutionException() {
    }

    public StopTestExecutionException(String message) {
        super(message);
    }

    public StopTestExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public StopTestExecutionException(Throwable cause) {
        super(cause);
    }

    public StopTestExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
