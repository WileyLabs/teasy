package com.wiley.autotest.selenium.annotations;

/**
 * User: abekman
 * Date: 01.08.12
 * Time: 11:13
 */

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Report {
    String value() default "";
}
