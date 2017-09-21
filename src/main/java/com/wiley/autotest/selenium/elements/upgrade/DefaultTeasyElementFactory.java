package com.wiley.autotest.selenium.elements.upgrade;

/**
 * Default factory for Teasy Element. If you want to provide you own implementations
 * you will need to pass similar factory to the {@link com.wiley.autotest.services.Configuration}
 */
public class DefaultTeasyElementFactory extends TeasyElementFactory {

    DefaultTeasyElementFactory(TeasyElementData elementData) {
        super(elementData);
    }

    @Override
    public VisibleTeasyElement getVisibleElement() {
        return new VisibleTeasyElement(elementData);
    }

    @Override
    public DomTeasyElement getDomElement() {
        return new DomTeasyElement(elementData);
    }

    @Override
    public NullTeasyElement getNullElement() {
        return new NullTeasyElement(elementData);
    }
}
