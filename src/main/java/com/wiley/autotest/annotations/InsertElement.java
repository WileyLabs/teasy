package com.wiley.autotest.annotations;

import com.wiley.autotest.screenshots.Selector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by mosadchiy on 26.05.2016.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InsertElement {
    Selector by();
    String locator();
}