package com.wiley.autotest.selenium.context;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 22.02.12
 * Time: 15:13
 */
public interface ErrorSender {
    void setPostponedTestFail(final String message);
}
