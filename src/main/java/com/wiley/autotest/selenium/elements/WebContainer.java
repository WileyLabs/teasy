package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;

public interface WebContainer extends Element {
    void init(OurWebElement wrappedElement);
}
