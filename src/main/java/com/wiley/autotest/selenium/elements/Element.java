package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.context.ErrorSender;
import org.openqa.selenium.WebElement;

/**
 * Base interface for objects like Buttons, Links, CheckBoxes, Selects etc.
 */
public interface Element {
    boolean isVisible();

    String getText();

    @Deprecated
    void setErrorSender(ErrorSender errorSender);

    WebElement getWrappedWebElement();
}
