package com.wiley.autotest.spring;

public class GridConnectException extends RuntimeException {

    public GridConnectException() {
    }

    public GridConnectException(String message) {
        super(message);
    }

    public GridConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public GridConnectException(Throwable cause) {
        super(cause);
    }

    public GridConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
