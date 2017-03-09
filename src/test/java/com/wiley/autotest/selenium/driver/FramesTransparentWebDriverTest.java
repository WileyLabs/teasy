package com.wiley.autotest.selenium.driver;

import org.mockito.internal.stubbing.answers.ThrowsException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FramesTransparentWebDriverTest {
    private static final String NON_EXISTENT_WINDOW_HANDLER = "NonExistentWindowHandler";
    private static final String MAIN_WINDOW_HANDLER = "mainWindowHandler";
    private WebDriver driver;
    private List<WebElement> frames;
    private WebDriver wrappedDriver;
    private WebDriver.TargetLocator targetLocator;

    @BeforeMethod
    public void setUp() {
        frames = new ArrayList<WebElement>();
        wrappedDriver = mock(WebDriver.class);
        targetLocator = mock(WebDriver.TargetLocator.class);
        whenFrameCalled(targetLocator);
        when(wrappedDriver.switchTo()).thenReturn(targetLocator);
        when(wrappedDriver.getWindowHandle()).thenReturn(MAIN_WINDOW_HANDLER);
        whenFindFrames(wrappedDriver);
        driver = new FramesTransparentWebDriver(wrappedDriver);
    }

    @Test(enabled = false, expectedExceptions = SwitchToFrameOperationNotAllowedException.class)
    public void testDriverDoesNotAllowToSwitchToFrameDirectlyUsingWebElement() {
        driver.switchTo().frame(mock(WebElement.class));
    }

    @Test(enabled = false, expectedExceptions = SwitchToFrameOperationNotAllowedException.class)
    public void testDriverDoesNotAllowToSwitchToFrameDirectlyUsingNameOrId() {
        driver.switchTo().frame(randomAlphabetic(10));
    }

    @Test(enabled = false, expectedExceptions = SwitchToFrameOperationNotAllowedException.class)
    public void testDriverDoesNotAllowToSwitchToFrameDirectlyIndex() {
        driver.switchTo().frame(nextInt());
    }

    @Test(enabled = false)
    public void testFindElementsWhenThereAreDetachedFrames() {
        frames.add(mock(WebElement.class));
        frames.add(mock(WebElement.class, new ThrowsException(new StaleElementReferenceException("stolen"))));

        assertThat(driver.findElements(By.cssSelector("selector")), hasSize(0));
    }

    @Test(enabled = false)
    public void testFindElementsWhenThereAreDetachedFrames2() {
        final List<WebElement> firstLevelFrames = new ArrayList<WebElement>();
        final List<WebElement> secondLevelFrames = new ArrayList<WebElement>();
        final List<WebElement> thirdLevelFrames = new ArrayList<WebElement>();
        final List<WebElement> lastLevelFrames = emptyList();

        firstLevelFrames.add(mock(WebElement.class));

        final WebElement unstableFrame = mock(WebElement.class);
        when(unstableFrame.getTagName())
                .thenReturn("iframe")
                .thenAnswer(new ThrowsException(new StaleElementReferenceException("stolen")));
        secondLevelFrames.add(unstableFrame);

        thirdLevelFrames.add(mock(WebElement.class));

        when(wrappedDriver.findElements(By.tagName("iframe")))
                .thenReturn(firstLevelFrames)
                .thenReturn(secondLevelFrames)
                .thenReturn(thirdLevelFrames)
                .thenReturn(lastLevelFrames);


        assertThat(driver.findElements(By.cssSelector("selector")), hasSize(0));
    }

    @Test(enabled = false, expectedExceptions = {SwitchToWindowException.class})
    public void testSwitchToNonExistentWindow() {
        when(targetLocator.window(NON_EXISTENT_WINDOW_HANDLER)).thenThrow(new NoSuchWindowException("Unable to switch to window"));
        driver.switchTo().window(NON_EXISTENT_WINDOW_HANDLER);
    }

    @Test(enabled = false, expectedExceptions = {SwitchToWindowException.class})
    public void testSwitchToNullWindowHandler() {
        when(targetLocator.window(null)).thenThrow(new NullPointerException());
        driver.switchTo().window(null);
    }

    @Test(enabled = false)
    public void shouldSwitchToMainWindowAfterWindowClose() {
        when(targetLocator.window(any(String.class))).thenReturn(driver);
        driver.switchTo().window("newWindowHandler");
        driver.close();
        verify(targetLocator, times(1)).window(MAIN_WINDOW_HANDLER);
    }

    @Test(enabled = false)
    public void shouldSwitchToMainWindowWhenNoSuchWindowExceptionThrown() {
        when(wrappedDriver.findElements(any(By.class))).thenThrow(new NoSuchWindowException("No such window"));
        driver.findElements(mock(By.class));
        verify(targetLocator, atLeastOnce()).window(MAIN_WINDOW_HANDLER);
    }

    @Test(enabled = false)
    public void shouldReturnEmptyListWhenFindElementsThrowException() {
        when(wrappedDriver.findElements(any(By.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new Exception();
            }
        });
        final List<WebElement> elementsList = driver.findElements(mock(By.class));
        assertThat(elementsList, hasSize(0));
    }

    private void whenFindFrames(final WebDriver mockedDriver) {
        when(mockedDriver.findElements(By.tagName("iframe")))
                .thenReturn(frames)
                .thenReturn(new ArrayList<WebElement>());

        doReturn(emptyList()).when(mockedDriver).findElements(By.tagName("frame"));
    }

    private void whenFrameCalled(final WebDriver.TargetLocator targetLocator) {
        when(targetLocator.frame(any(WebElement.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                ((WebElement) args[0]).getTagName();
                return null;
            }
        });
    }
}
