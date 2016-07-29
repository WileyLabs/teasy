package com.wiley.autotest.annotations;

import org.openqa.selenium.UnexpectedAlertBehaviour;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: ntyukavkin
 * Date: 5/29/14
 * Time: 14:13 AM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UnexpectedAlertCapability {

    UnexpectedAlertBehaviour capability() default UnexpectedAlertBehaviour.ACCEPT;
}
