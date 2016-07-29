package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertTrue;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class SelectImplTest {
    @Test(enabled = false)
    public void testGetText() {
        final String selectedOption = randomAlphanumeric(10);
        final Select given = givenSelectElementWith(selectedOption);

        assertThat(given.getText(), equalTo(selectedOption));
    }

    @Test(enabled = false)
    public void testLastOptionMustBeSelectedWhenUserSelectsByAnotherText() {
        final WebElement firstOption = option();
        final WebElement secondOption = option();
        final WebElement thirdOption = option();
        final Select given = givenSelectElementWith(firstOption, secondOption, thirdOption);

        given.selectByAnotherTextThan(secondOption.getText());

        verify(thirdOption).click();
//        verify(given.getWrappedWebElement()).sendKeys(thirdOption.getText());
    }

    private Select givenSelectElementWith(final String textOfSelectedOption) {
        final WebElement wrappedElement = wrappedSelectElement();
        final List<WebElement> options = options(textOfSelectedOption);
        setOptions(wrappedElement, options);
        return new SelectImpl(wrappedElement);
    }

    private Select givenSelectElementWith(final WebElement... options) {
        final WebElement wrappedElement = wrappedSelectElement();
        for (int i = 0; i < options.length; i++) {
            when(options[i].getAttribute("index")).thenReturn(valueOf(i));
        }
        setOptions(wrappedElement, asList(options));
        return new SelectImpl(wrappedElement);
    }

    private List<WebElement> options(final String textOfSelectedOption) {
        final List<WebElement> options = new ArrayList<WebElement>();
        options.add(option());
        options.add(option());
        options.add(selectedOptionWith(textOfSelectedOption));
        options.add(option());
        return options;
    }

    private WebElement selectedOptionWith(final String text) {
        final WebElement option = option();
        when(option.isSelected()).thenReturn(true);
        when(option.getText()).thenReturn(text);
        return option;
    }

    private WebElement option() {
        final WebElement option = mock(WebElement.class);
        when(option.isSelected()).thenReturn(false);
        when(option.getText()).thenReturn(randomAlphanumeric(10));
        return option;
    }

    private void setOptions(final WebElement wrappedElement, final List<WebElement> options) {
        when(wrappedElement.findElements(By.tagName("option"))).thenReturn(options);
    }

    private WebElement wrappedSelectElement() {
        final WebElement wrappedElement = mock(WebElement.class);
        when(wrappedElement.getTagName()).thenReturn("select");
        return wrappedElement;
    }
}
