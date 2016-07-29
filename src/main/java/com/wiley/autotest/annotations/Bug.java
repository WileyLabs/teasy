package com.wiley.autotest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * This annotation should be used to mark tests that are failing because of functional bug of application.
 * <p/>
 * JIRA bug id should be provided as "id" parameter
 * <p/>
 * User: vefimov
 * Date: 15.01.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, TYPE})
public @interface Bug {

    String id();
}
