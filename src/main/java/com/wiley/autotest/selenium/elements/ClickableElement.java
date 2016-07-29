package com.wiley.autotest.selenium.elements;

public interface ClickableElement extends Element {
    void click();

    void clickWithReload();

    void clickWithJavaScript();

    boolean isClickable();
}
