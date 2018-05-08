package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Element;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractElement implements Element {

    private final TeasyElement el;
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractElement.class);

    public AbstractElement(final TeasyElement element) {
        this.el = element;
    }

    public boolean isDisplayed() {
        return this.el.isDisplayed();
    }

    public String getText() {
        return this.el.getText();
    }

    @Override
    public TeasyElement getWrappedElement() {
        return el;
    }
}
