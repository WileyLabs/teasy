package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import com.wiley.autotest.selenium.extensions.ElementFactory;
import com.wiley.autotest.selenium.extensions.ExtendedElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;

import static java.text.MessageFormat.format;

public class DefaultElementFactory implements ElementFactory {

    @Override
    public <T extends Element> T create(final Class<T> classOfElement, final WebElement wrappedElement) {
        try {
            return findImplementationFor(classOfElement).getDeclaredConstructor(WebElement.class).newInstance(wrappedElement);
        } catch (InstantiationException e) {
            throw new ExtendedElementException("InstantiationException is happened! " + e);
        } catch (IllegalAccessException e) {
            throw new ExtendedElementException("IllegalAccessException is happened! " + e);
        } catch (InvocationTargetException e) {
            throw new ExtendedElementException("InvocationTargetException is happened! " + e);
        } catch (NoSuchMethodException e) {
            throw new ExtendedElementException("NoSuchMethodException is happened! " + e);
        }
    }

    @Override
    public <T extends Element> T create(final Class<T> classOfElement, final WebElement wrappedElement, final By by) {
        try {
            return findImplementationFor(classOfElement).getDeclaredConstructor(WebElement.class, By.class).newInstance(wrappedElement, by);
        } catch (InstantiationException e) {
            throw new ExtendedElementException("InstantiationException is happened! " + e);
        } catch (IllegalAccessException e) {
            throw new ExtendedElementException("IllegalAccessException is happened! " + e);
        } catch (InvocationTargetException e) {
            throw new ExtendedElementException("InvocationTargetException is happened! " + e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new ExtendedElementException("NoSuchMethodException is happened! " + e);
        }
    }

    @Override
    public <T extends Element> T create(final Class<T> classOfElement, final TeasyWebElement wrappedElement) {
        try {
            return findImplementationFor(classOfElement).getDeclaredConstructor(TeasyWebElement.class).newInstance(wrappedElement);
        } catch (InstantiationException e) {
            throw new ExtendedElementException("InstantiationException is happened! " + e);
        } catch (IllegalAccessException e) {
            throw new ExtendedElementException("IllegalAccessException is happened! " + e);
        } catch (InvocationTargetException e) {
            throw new ExtendedElementException("InvocationTargetException is happened! " + e);
        } catch (NoSuchMethodException e) {
            throw new ExtendedElementException("NoSuchMethodException is happened! " + e);
        }
    }

    @Override
    public <T extends Element> T create(final Class<T> classOfElement, final TeasyWebElement wrappedElement, final By by) {
        try {
            return findImplementationFor(classOfElement).getDeclaredConstructor(TeasyWebElement.class, By.class).newInstance(wrappedElement, by);
        } catch (InstantiationException e) {
            throw new ExtendedElementException("InstantiationException is happened! " + e);
        } catch (IllegalAccessException e) {
            throw new ExtendedElementException("IllegalAccessException is happened! " + e);
        } catch (InvocationTargetException e) {
            throw new ExtendedElementException("InvocationTargetException is happened! " + e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new ExtendedElementException("NoSuchMethodException is happened! " + e);
        }
    }

    private <T extends Element> Class<T> findImplementationFor(final Class<T> classOfElement) {
        try {
            if (classOfElement.equals(Button.class) ||
                    classOfElement.equals(Link.class) ||
                    classOfElement.equals(CheckBox.class) ||
                    classOfElement.equals(RadioButton.class) ||
                    classOfElement.equals(Select.class) ||
                    classOfElement.equals(TextField.class)) {
                return (Class<T>) Class.forName(format("{0}.{1}Impl", getClass().getPackage().getName(), classOfElement.getSimpleName()));
            } else {
                return (Class<T>) Class.forName(classOfElement.getName());
            }
        } catch (ClassNotFoundException e) {
            throw new ExtendedElementException("ClassNotFoundException is happened! " + e);
        }
    }
}
