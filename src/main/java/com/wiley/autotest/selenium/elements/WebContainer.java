package com.wiley.autotest.selenium.elements;

import org.openqa.selenium.WebElement;

public interface WebContainer extends Element {
    void init(WebElement wrappedElement);
}
