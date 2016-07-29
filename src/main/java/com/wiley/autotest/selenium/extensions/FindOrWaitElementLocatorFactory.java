package com.wiley.autotest.selenium.extensions;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FindOrWaitElementLocatorFactory implements ElementLocatorFactory {
    public static final int DEFAULT_TIMEOUT_FOR_WAIT_ANNOTATION = 10;
    private final SearchContext context;

    public FindOrWaitElementLocatorFactory(final SearchContext context) {
        this.context = context;
    }


    @Override
    public ElementLocator createLocator(final Field field) {
        return new FindOrWaitElementLocator(context, field, DEFAULT_TIMEOUT_FOR_WAIT_ANNOTATION, SECONDS);
    }
}
