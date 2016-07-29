package com.wiley.autotest.selenium.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Will wait for element to become visible
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface WaitForVisibility {
}
