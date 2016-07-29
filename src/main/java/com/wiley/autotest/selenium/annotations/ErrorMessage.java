package com.wiley.autotest.selenium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * User: dfedorov
 * Date: 4/5/12
 * Time: 9:21 AM
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface ErrorMessage {
    String value();
}
