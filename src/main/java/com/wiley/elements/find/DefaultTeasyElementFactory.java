package com.wiley.elements.find;

import com.wiley.elements.TeasyElementData;
import com.wiley.elements.types.DomTeasyElement;
import com.wiley.elements.types.NullTeasyElement;
import com.wiley.elements.types.VisibleTeasyElement;

/**
 * Default factory for Teasy Element. If you want to provide you own implementations
 * you will need to pass similar factory to the {@link com.wiley.config.Configuration}
 */
public class DefaultTeasyElementFactory extends TeasyElementFactory {

    public DefaultTeasyElementFactory(TeasyElementData elementData) {
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
