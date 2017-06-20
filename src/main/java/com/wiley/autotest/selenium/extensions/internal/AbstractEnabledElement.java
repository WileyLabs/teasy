package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.EnabledElement;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

abstract class AbstractEnabledElement extends AbstractElement implements EnabledElement {

    protected AbstractEnabledElement(final OurWebElement wrappedElement) {
        super(wrappedElement);
    }

    protected AbstractEnabledElement(final OurWebElement wrappedElement, final By by) {
        super(wrappedElement, by);
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isDisplayed() && getWrappedElement().isEnabled();
    }

    @Override
    public OurWebElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
