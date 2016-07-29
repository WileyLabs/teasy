package com.wiley.autotest.selenium.extensions;

import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.WebContainer;
import org.openqa.selenium.WebElement;

public class SomeWebContainer implements WebContainer {
    private boolean initialized = false;

    @Override
    public void init(final WebElement wrappedElement) {
        initialized = true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setErrorSender(final ErrorSender errorSender) {
    }

    @Override
    public WebElement getWrappedWebElement() {
        return null;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
