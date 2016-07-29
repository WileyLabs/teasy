package com.wiley.autotest.selenium.extensions;

import com.google.common.base.Function;
import com.wiley.autotest.selenium.annotations.ErrorMessage;
import com.wiley.autotest.selenium.annotations.WaitForVisibility;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElementImpl;
import org.openqa.selenium.*;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Reporter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class FindOrWaitElementLocator implements ElementLocator {
    private final By by;
    private final Wait<SearchContext> wait;
    private final String errorMessage;
    private Function<SearchContext, List<WebElement>> condition = new Function<SearchContext, List<WebElement>>() {
        @Override
        public List<WebElement> apply(SearchContext context) {
            return context.findElements(by);
        }
    };

    public FindOrWaitElementLocator(final SearchContext context,
                                    final Field field,
                                    final int timeout,
                                    final TimeUnit unit) {
        by = new Annotations(field).buildBy();
        errorMessage = getErrorMessage(field);
        initConditionForWaitAnnotations(field);
        wait = new FluentWait<SearchContext>(context)
                .ignoring(NoSuchElementException.class)
                .withTimeout(timeout, unit);
    }

    public FindOrWaitElementLocator(final SearchContext context,
                                    final By locator,
                                    final Field field,
                                    final int timeout,
                                    final TimeUnit unit) {
        by = locator;
        errorMessage = getErrorMessage(field);
        initConditionForWaitAnnotations(field);
        wait = new FluentWait<SearchContext>(context)
                .ignoring(NoSuchElementException.class)
                .withTimeout(timeout, unit);
    }

    @Override
    public WebElement findElement() {
        try {
            return findElements().get(0);
        } catch (IndexOutOfBoundsException e) {
            reportError();
            throw noSuchElementException();
        }
    }

    @Override
    public List<WebElement> findElements() {
        try {
            return OurWebElementImpl.wrap(wait.until(condition), by);
        } catch (TimeoutException e) {
            return emptyList();
        }
    }

    private NoSuchElementException noSuchElementException() {
        return new NoSuchElementException("Unable to locate element using " + by);
    }

    private void initConditionForWaitAnnotations(final Field field) {
        if (field.isAnnotationPresent(com.wiley.autotest.selenium.annotations.Wait.class)) {
            condition = new Function<SearchContext, List<WebElement>>() {
                @Override
                public List<WebElement> apply(SearchContext context) {
                    final List<WebElement> found = context.findElements(by);
                    if (isEmpty(found)) {
                        throw noSuchElementException();
                    }

                    return found;
                }
            };
        }
        if (field.isAnnotationPresent(WaitForVisibility.class)) {
            condition = new Function<SearchContext, List<WebElement>>() {
                @Override
                public List<WebElement> apply(SearchContext context) {
                    final List<WebElement> found = context.findElements(by);
                    for (final WebElement element : found) {
                        if (!element.isDisplayed()) {
                            return null;
                        }
                    }
                    return isNotEmpty(found) ? found : null;
                }
            };
        }
    }

    private String getErrorMessage(final Field field) {
        if (field.isAnnotationPresent(ErrorMessage.class)) {
            return field.getAnnotation(ErrorMessage.class).value();
        }

        return null;
    }

    private void reportError() {
        if (errorMessage != null) {
            Reporter.log(errorMessage);
        }
    }
}
