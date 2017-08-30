package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.EnabledElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import org.openqa.selenium.By;

abstract class AbstractEnabledElement extends AbstractElement implements EnabledElement {

    protected AbstractEnabledElement(final TeasyWebElement wrappedElement) {
        super(wrappedElement);
    }

    protected AbstractEnabledElement(final TeasyWebElement wrappedElement, final By by) {
        super(wrappedElement, by);
    }

    @Override
    public boolean isEnabled() {
        return getWrappedElement().isDisplayed() && getWrappedElement().isEnabled();
    }

    @Override
    public TeasyWebElement getWrappedWebElement() {
        return getWrappedElement();
    }
}
