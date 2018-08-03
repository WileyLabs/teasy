package com.wiley.assertions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

public class TeasyError {

    private final Throwable throwable;
    private final MethodType methodType;
    private String screenshotFilePath;

    public TeasyError(Throwable throwable, MethodType methodType) {
        this.methodType = methodType;
        this.throwable = throwable instanceof InvocationTargetException ? ((InvocationTargetException) throwable).getTargetException() : throwable;
    }

    public String getErrorMessage() {
        String message;
        if (throwable != null && throwable.getMessage() != null) {
            message = throwable.getMessage();
        } else if (throwable != null && throwable.getCause() != null) {
            message = throwable.getCause().getMessage();
        } else {
            message = null;
        }

        if (message == null) {
            message = methodType.desc() + " failed";
        }
        return message;
    }

    public String getStackTraceAsString() {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public String getScreenshotFilePath() {
        return screenshotFilePath;
    }

    public void setScreenshotFilePath(String screenshotFilePath) {
        this.screenshotFilePath = screenshotFilePath;
    }
}
