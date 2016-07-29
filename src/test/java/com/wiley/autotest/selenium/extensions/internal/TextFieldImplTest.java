package com.wiley.autotest.selenium.extensions.internal;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TextFieldImplTest {
    @Test(enabled = false)
    public void testGetText() {
        final String text = RandomStringUtils.randomAlphanumeric(10);
        final com.wiley.autotest.selenium.extensions.internal.TextFieldImpl textField = givenTextFieldWithText(text);

        assertThat(textField.getText(), equalTo(text));
    }

    private com.wiley.autotest.selenium.extensions.internal.TextFieldImpl givenTextFieldWithText(final String value) {
        final WebElement wrappedElement = mock(WebElement.class);
        when(wrappedElement.getAttribute("value")).thenReturn(value);
        return new com.wiley.autotest.selenium.extensions.internal.TextFieldImpl(wrappedElement);
    }
}
