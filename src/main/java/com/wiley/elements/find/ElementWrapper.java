package com.wiley.elements.find;

import com.wiley.config.Configuration;
import com.wiley.elements.*;
import com.wiley.elements.types.TeasyElementType;

import java.lang.reflect.InvocationTargetException;

class ElementWrapper {

    static TeasyElement wrap(TeasyElementData data, TeasyElementType type) {
        try {
            TeasyElementFactory elementFactory;
            if (Configuration.elementFactoryClass != null) {
                elementFactory = (TeasyElementFactory) Class.forName(Configuration.elementFactoryClass)
                        .getDeclaredConstructor(TeasyElementData.class).newInstance(data);
            } else {
                elementFactory = new DefaultTeasyElementFactory(data);
            }

            switch (type) {
                case VISIBLE: {
                    return elementFactory.getVisibleElement() != null
                            ? elementFactory.getVisibleElement()
                            : new DefaultTeasyElementFactory(data).getVisibleElement();
                }
                case DOM: {
                    return elementFactory.getDomElement() != null
                            ? elementFactory.getDomElement()
                            : new DefaultTeasyElementFactory(data).getDomElement();
                }
                case NULL: {
                    return elementFactory.getNullElement() != null
                            ? elementFactory.getNullElement()
                            : new DefaultTeasyElementFactory(data).getNullElement();
                }
                default: {
                    throw new ClassNotFoundException("Cannot create instance of TeasyElement for type '" + type + "'");
                }
            }
        } catch (InstantiationException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new WrapElementException(e);
        }
    }
}
