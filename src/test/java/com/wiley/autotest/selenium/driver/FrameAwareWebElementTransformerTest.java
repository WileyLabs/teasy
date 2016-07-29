package com.wiley.autotest.selenium.driver;

import org.mockito.internal.stubbing.answers.ThrowsException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.testng.annotations.Test;

import java.util.Stack;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;

public class FrameAwareWebElementTransformerTest {
    private final FrameAwareWebElementTransformer transformer =
            new FrameAwareWebElementTransformer(mock(WebDriver.class), new Stack<WebElement>());

    @Test(enabled = false, expectedExceptions = StaleElementReferenceException.class)
    public void testTryToAccessToStolenElement() {
        final WebElement stolenElement = givenStolenElement();
        final WebElement transformedStolenElement = transformer.apply(stolenElement);

        transformedStolenElement.isDisplayed();
    }

    @Test(enabled = false)
    public void testGetWrappedElementOfTransformedElement() {
        final WebElement element = givenElement();
        final WebElement transformedElement = transformer.apply(element);

        assertThat(((WrapsElement) transformedElement).getWrappedElement(), sameInstance(element));
    }

    private WebElement givenElement() {
        return mock(WebElement.class);
    }

    private WebElement givenStolenElement() {
        return mock(WebElement.class, new ThrowsException(new StaleElementReferenceException("stolen")));
    }

}
