package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Element;
import com.wiley.autotest.selenium.elements.WebContainer;
import com.wiley.autotest.selenium.extensions.ElementFactory;
import com.wiley.autotest.selenium.extensions.ExtendedElementException;
import com.wiley.autotest.selenium.extensions.ExtendedFieldDecorator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.PageFactory.initElements;

public class ExtendedLocatingElementHandler<T extends Element> implements InvocationHandler {
    private final Class<T> classOfElements;
    private final ElementLocator locator;
    private ElementFactory elementFactory;

    public ExtendedLocatingElementHandler(final Class<T> classOfElement, final ElementLocator elementLocator, final ElementFactory factory) {
        this.classOfElements = classOfElement;
        this.locator = elementLocator;
        this.elementFactory = factory;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final List<WebElement> wrappedElements = locator.findElements();

        final List<T> realElements = new ArrayList<T>(wrappedElements.size());
        for (final WebElement each : wrappedElements) {
            if (WebContainer.class.isAssignableFrom(classOfElements)) {
                final WebContainer container = createInstanceOf((Class<? extends WebContainer>) classOfElements);
                container.init(each);
                realElements.add((T) container);
                initElements(new ExtendedFieldDecorator(each, elementFactory, null), container);
            } else {
                realElements.add(elementFactory.create(classOfElements, each));
            }
        }
        try {
            return method.invoke(realElements, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @SuppressWarnings("unchecked")
    private WebContainer createInstanceOf(final Class<? extends WebContainer> aClass) {
        try {
            return aClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            throw new ExtendedElementException("InstantiationException is happened " + e);
        } catch (IllegalAccessException e) {
            throw new ExtendedElementException("IllegalAccessException is happened " + e);
        } catch (NoSuchMethodException e) {
            throw new ExtendedElementException("NoSuchMethodException is happened " + e);
        } catch (InvocationTargetException e) {
            throw new ExtendedElementException("InvocationTargetException is happened " + e);
        }
    }
}
