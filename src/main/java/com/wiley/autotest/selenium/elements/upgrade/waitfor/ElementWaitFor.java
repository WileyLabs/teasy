package com.wiley.autotest.selenium.elements.upgrade.waitfor;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 04/09/2017.
 */
public interface ElementWaitFor {
    void displayed();

    void absent();

    void text(String text);

    void attribute(String attributeName, String value);

    void attribute(String attributeName);

    void notContainsAttributeValue(String attributeName, String value);

    void containsAttributeValue(String attributeName, String value);

    void stale();

    void clickable();

    void condition(Function<? super WebDriver, ?> condition);
}
