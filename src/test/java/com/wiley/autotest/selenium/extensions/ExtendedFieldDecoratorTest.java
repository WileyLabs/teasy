package com.wiley.autotest.selenium.extensions;

import com.wiley.autotest.selenium.elements.Button;
import com.wiley.autotest.selenium.elements.DoNotSearch;
import com.wiley.autotest.selenium.elements.TextField;
import com.wiley.autotest.selenium.extensions.internal.DefaultElementFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openqa.selenium.By.name;

public class ExtendedFieldDecoratorTest {
    private FieldDecorator fieldDecorator;
    private WebDriver webDriver;

    @BeforeMethod
    public void setUp() {
        webDriver = mock(WebDriver.class);
        fieldDecorator = new ExtendedFieldDecorator(webDriver, new DefaultElementFactory(), null);
    }

    @DataProvider
    private Object[][] ignoredFields() {
        return new Object[][]{
                {"ignoredWebElement"},
                {"ignoredButton"},
                {"ignoredWebContainer"}
        };
    }

    @Test(enabled = false, dataProvider = "ignoredFields")
    public void testDecorateFieldMarkedAsDoNotSearch(final String fieldName) throws NoSuchFieldException {
        final Object decorated = decorateField(fieldName);

        assertThat(decorated, is(nullValue()));
    }

    @Test(enabled = false)
    public void testDecorateSimpleElement() throws NoSuchFieldException {
        final WebElement decorated = decorateField("simpleElement");

        assertThat(decorated, is(notNullValue()));
    }

    @Test(enabled = false)
    public void testDecorateSomeWebContainer() throws NoSuchFieldException {
        final SomeWebContainer decorated = decorateField("someWebContainer");

        assertThat(decorated, is(notNullValue()));
        assertThat("WebContainer has been initialized", decorated.isInitialized());
    }

    @Test(enabled = false)
    public void testDecorateSomeElementSuchAsButton() throws NoSuchFieldException {
        final Button decorated = decorateField("someButton");

        assertThat(decorated, is(notNullValue()));
    }

    @Test(enabled = false)
    public void testDecorateListOfElements() throws NoSuchFieldException {
        final WebElement wrappedElement = givenElementOnPageWith(name("textField"));
        final List<TextField> decorated = decorateField("listOfTextFields");

        assertThat("Single element found", decorated.size(), is(1));

        when(wrappedElement.isDisplayed()).thenReturn(true);
        assertThat("Element is displayed", getOnlyElement(decorated).isVisible());

        when(wrappedElement.isDisplayed()).thenReturn(false);
        assertThat("Element is not displayed", !getOnlyElement(decorated).isVisible());
    }

    @Test(enabled = false)
    public void testDecorateListOfWebElements() throws NoSuchFieldException {
        final List<TextField> decorated = decorateField("listOfWebElements");

        assertThat(decorated, is(notNullValue()));
    }

    private WebElement givenElementOnPageWith(final By locator) {
        final WebElement element = mock(WebElement.class);
        when(webDriver.findElements(locator)).thenReturn(Arrays.asList(element));
        return element;
    }

    private <T> T decorateField(final String fieldName) throws NoSuchFieldException {
        return (T) fieldDecorator.decorate(getClass().getClassLoader(), getField(fieldName));
    }

    private Field getField(final String fieldName) throws NoSuchFieldException {
        return Page.class.getDeclaredField(fieldName);
    }

    private class Page {
        private WebElement simpleElement;

        private SomeWebContainer someWebContainer;

        private Button someButton;

        @DoNotSearch
        private WebElement ignoredWebElement;

        @DoNotSearch
        private Button ignoredButton;

        @DoNotSearch
        private SomeWebContainer ignoredWebContainer;

        @FindBy(name = "textField")
        private List<TextField> listOfTextFields;

        @FindBy(name = "listOfWebElements")
        private List<WebElement> listOfWebElements;
    }
}
