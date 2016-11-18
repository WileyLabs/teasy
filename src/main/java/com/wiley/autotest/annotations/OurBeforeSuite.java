package com.wiley.autotest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: dfedorov
 * Date: 5/31/12
 * Time: 4:25 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OurBeforeSuite {
    String[] groups() default {};

    boolean alwaysRun() default false;
}
