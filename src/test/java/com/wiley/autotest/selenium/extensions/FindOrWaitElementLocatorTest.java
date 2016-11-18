package com.wiley.autotest.selenium.extensions;

import com.wiley.autotest.annotations.Wait;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.collect.Iterables.getOnlyElement;
import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class FindOrWaitElementLocatorTest {
    private final int timeout = 100;
    private final TimeUnit timeoutUnit = MILLISECONDS;
    @Mock
    private SearchContext searchContext;

    @FindBy(id = "element")
    private WebElement element;

    @FindBy(id = "elementToWait")
    @Wait
    private WebElement elementToWait;

    @FindBy(id = "elements")
    private List<WebElement> elements;

    @FindBy(id = "elementToWait")
    @Wait
    private List<WebElement> elementsToWait;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
    }

    @Test(enabled = false)
    public void testFindExistingElement() throws NoSuchFieldException {
        final WebElement given = givenElementOnPageFor(field("element"));

        final WebElement found = whenSearchElementFor(field("element"));

        assertThat(found, sameInstance(given));
        verify(searchContext, only()).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testFindNotExistedElementWithoutWait() throws NoSuchFieldException {
        givenThereIsNoElementOnPageFor(field("element"));

        boolean elementNotFound = false;
        try {
            whenSearchElementFor(field("element"));
        } catch (NoSuchElementException e) {
            elementNotFound = true;
        }
        assertThat("Element not found", elementNotFound);
        verify(searchContext, only()).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testFindNotExistedElementWithWait() throws NoSuchFieldException {
        givenThereIsNoElementOnPageFor(field("elementToWait"));

        boolean elementNotFound = false;
        try {
            whenSearchElementFor(field("elementToWait"));
        } catch (NoSuchElementException e) {
            elementNotFound = true;
        }
        assertThat("Element not found", elementNotFound);
        verify(searchContext, atLeast(2)).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testWaitForElementWhichWillBeLoadedLater() throws NoSuchFieldException {
        final WebElement given = givenElementWhichWillBeLoadedLaterFor(field("elementToWait"));

        final WebElement found = whenSearchElementFor(field("elementToWait"));

        assertThat(found, sameInstance(given));
    }

    @Test(enabled = false)
    public void testFindExistingElements() throws NoSuchFieldException {
        final List<WebElement> given = givenElementsOnPageFor(field("elements"));

        final List<WebElement> found = whenSearchElementsFor(field("elements"));

        assertThat(found, sameInstance(given));
        verify(searchContext, only()).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testFindNotExistedElementsWithoutWait() throws NoSuchFieldException {
        givenThereIsNoElementOnPageFor(field("elements"));

        final List<WebElement> found = whenSearchElementsFor(field("elements"));

        assertThat(found, hasSize(0));
        verify(searchContext, only()).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testFindNotExistedElementsWithWait() throws NoSuchFieldException {
        givenThereIsNoElementOnPageFor(field("elementsToWait"));

        final List<WebElement> found = whenSearchElementsFor(field("elementsToWait"));

        assertThat(found, hasSize(0));
        verify(searchContext, atLeast(2)).findElements(Matchers.<By>any());
    }

    @Test(enabled = false)
    public void testWaitForElementsWhichWillBeLoadedLater() throws NoSuchFieldException {
        final List<WebElement> given = givenElementsWhichWillBeLoadedLaterFor(field("elementsToWait"));

        final List<WebElement> found = whenSearchElementsFor(field("elementsToWait"));

        assertThat(found, sameInstance(given));
    }

    private void givenThereIsNoElementOnPageFor(final Field field) {
        final By by = buildByFor(field);
        when(searchContext.findElement(by)).thenThrow(new NoSuchElementException("No such element"));
        when(searchContext.findElements(by)).thenReturn(Collections.<WebElement>emptyList());
    }

    private By buildByFor(final Field field) {
        return new Annotations(field).buildBy();
    }

    private WebElement whenSearchElementFor(final Field field) {
        return locatorFor(field).findElement();
    }

    private List<WebElement> whenSearchElementsFor(final Field field) {
        return locatorFor(field).findElements();
    }

    private FindOrWaitElementLocator locatorFor(final Field field) {
        return new FindOrWaitElementLocator(searchContext, field, timeout, timeoutUnit);
    }

    private List<WebElement> givenElementsOnPageFor(final Field field) {
        final By by = buildByFor(field);
        final List<WebElement> mockedElements = asList(mock(WebElement.class));
        when(searchContext.findElements(by)).thenReturn(mockedElements);
        return mockedElements;
    }

    private WebElement givenElementOnPageFor(final Field field) {
        return getOnlyElement(givenElementsOnPageFor(field));
    }

    private WebElement givenElementWhichWillBeLoadedLaterFor(final Field field) {
        return getOnlyElement(givenElementsWhichWillBeLoadedLaterFor(field));
    }

    private List<WebElement> givenElementsWhichWillBeLoadedLaterFor(final Field field) {
        final By by = buildByFor(field);
        final List<WebElement> mockedElement = asList(mock(WebElement.class));
        when(searchContext.findElements(by))
                .thenReturn(Collections.<WebElement>emptyList())
                .thenReturn(mockedElement);
        return mockedElement;
    }

    private Field field(final String name) throws NoSuchFieldException {
        return getClass().getDeclaredField(name);
    }
}
