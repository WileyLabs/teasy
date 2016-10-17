package com.wiley.autotest.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: vefimov
 * Date: 21.02.13
 * Time: 15:35
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OurAfterGroups {
    String[] value() default {};
    boolean alwaysRun() default false;
}
