package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.extensions.FindOrWaitElementLocator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 17:38
 */
public abstract class AbstractComponent<P extends AbstractComponent> extends AbstractPageElement<P> implements IComponent {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ComponentProvider componentProvider;

    public ComponentProvider getComponentProvider() {
        return componentProvider;
    }

    public Logger getLog() {
        return log;
    }

    public void setComponentProvider(final ComponentProvider componentProviderParameter) {
        this.componentProvider = componentProviderParameter;
    }

    protected void initWebElementField(final String fieldName, final By locator) {
        try {
            final Field field = getClass().getDeclaredField(fieldName);
            final InvocationHandler handler = new LocatingElementHandler(new FindOrWaitElementLocator(getDriver(), locator, field, getTimeout().intValue(), TimeUnit.SECONDS));
            field.setAccessible(true);
            field.set(this, Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{WebElement.class, WrapsElement.class, Locatable.class}, handler));
        } catch (NoSuchFieldException e) {
            fail("Unable to find field");
        } catch (IllegalAccessException e) {
            fail("Unable to init field");
        }
    }
}
