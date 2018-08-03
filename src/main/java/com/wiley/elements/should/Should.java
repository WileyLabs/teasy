package com.wiley.elements.should;

import org.openqa.selenium.WebDriver;

import java.util.function.Function;

/**
 * Created by vefimov on 04/09/2017.
 */
public interface Should {

    void beDisplayed();

    void beAbsent();

    void haveText(String text);

    void haveAnyText();

    void haveNoText();

    void containsText(String text);

    void haveAttribute(String attributeName, String value);

    void haveAttribute(String attributeName);

    void notHaveAttribute(String attributeName);

    void customCondition(Function<WebDriver, Boolean> condition);
}
