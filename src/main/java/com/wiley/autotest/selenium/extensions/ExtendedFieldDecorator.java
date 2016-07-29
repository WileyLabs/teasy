package com.wiley.autotest.selenium.extensions;

import com.wiley.autotest.selenium.context.ErrorSender;
import com.wiley.autotest.selenium.elements.DoNotSearch;
import com.wiley.autotest.selenium.elements.Element;
import com.wiley.autotest.selenium.elements.WebContainer;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.extensions.internal.ExtendedLocatingElementHandler;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;

import java.lang.reflect.*;
import java.util.List;

public class ExtendedFieldDecorator extends DefaultFieldDecorator {
    private final ElementFactory elementFactory;
    private final ErrorSender errorSender;
    private final SearchContext context;

    public ExtendedFieldDecorator(final SearchContext searchContext,
                                  final ElementFactory elementFactoryParameter,
                                  final ErrorSender errorSenderParameter) {
        super(new FindOrWaitElementLocatorFactory(searchContext));
        this.context = searchContext;
        this.elementFactory = elementFactoryParameter;
        this.errorSender = errorSenderParameter;
    }

    @Override
    public Object decorate(final ClassLoader loader, final Field field) {
        if (isIgnored(field)) {
            return null;
        } else if (WebContainer.class.isAssignableFrom(field.getType())) {
            return decorateWebContainer(loader, field);
        } else if (Element.class.isAssignableFrom(field.getType())) {
            return decorateElement(loader, field);
        } else if (isDecoratableCustomElementList(field)) {
            return decorateCustomElementList(loader, field);
        }
        return super.decorate(loader, field);
    }

    @SuppressWarnings("unchecked")
    private <T extends Element> List<T> decorateCustomElementList(final ClassLoader loader, final Field field) {
        final InvocationHandler handler = new ExtendedLocatingElementHandler<T>((Class<T>) getGenericClassOf(field), locatorFor(field), elementFactory);
        return (List<T>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
    }

    private <T extends Element> T decorateElement(final ClassLoader loader, final Field field) {
        final WebElement wrappedElement = proxyWebElement(loader, field);
        final T element = elementFactory.create((Class<T>) field.getType(), wrappedElement);
        element.setErrorSender(errorSender);
        return element;
    }

    private <T extends WebContainer> T decorateWebContainer(final ClassLoader loader, final Field field) {
        /* todo AbstractWebContainer must implement Element interface. */
        final WebElement wrappedElement = proxyWebElement(loader, field);
        final WebContainer container = createInstanceFor(field);
        container.init(wrappedElement);
        PageFactory.initElements(new ExtendedFieldDecorator(wrappedElement, elementFactory, errorSender), container);
        container.setErrorSender(errorSender);
        return (T) container;
    }

    private WebElement proxyWebElement(final ClassLoader loader, final Field field) {
        return proxyForLocator(loader, locatorFor(field));
    }

    private ElementLocator locatorFor(final Field field) {
        return factory.createLocator(field);
    }

    private boolean isIgnored(final Field field) {
        return field.isAnnotationPresent(DoNotSearch.class);
    }

    private boolean isDecoratableCustomElementList(final Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }

        final Type genericType = field.getGenericType();
        if (isGeneric(genericType)) {
            return false;
        }
        final Class<?> listType = getGenericClassOf(field);
        return Element.class.isAssignableFrom(listType);
    }

    private Class<?> getGenericClassOf(final Field field) {
        final Type genericType = field.getGenericType();
        return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
    }

    private boolean isGeneric(final Type genericType) {
        return !(genericType instanceof ParameterizedType);
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstanceFor(final Field field) {
        try {
            return (T) field.getType().getDeclaredConstructor().newInstance();
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

    @Override
    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementHandler(locator);

        WebElement proxy;
        proxy = (WebElement) Proxy.newProxyInstance(loader, new Class[]{OurWebElement.class, WrapsElement.class, Locatable.class}, handler);
        return proxy;
    }
}