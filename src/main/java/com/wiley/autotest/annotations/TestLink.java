package com.wiley.autotest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: dfedorov
 * Date: 3/19/12
 * Time: 2:37 PM
 */
@Retention(RUNTIME)
@Target({METHOD})
public @interface TestLink {

    String rationalId() default "";

    String testRailId() default "";

    String testLinkId() default "";

    String testCaseVersion();
}
