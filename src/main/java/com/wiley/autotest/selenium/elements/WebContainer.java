package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;

public interface WebContainer extends Element {
    void init(TeasyWebElement wrappedElement);
}
