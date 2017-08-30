package com.wiley.autotest;

import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.TextField;
import com.wiley.autotest.selenium.elements.upgrade.TeasyWebElement;
import com.wiley.autotest.utils.ExecutionUtils;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public final class ExpectedConditions2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpectedConditions2.class);

    private ExpectedConditions2() {
    }

    public static ExpectedCondition<WebElement> presenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                return searchContext.findElement(locator);
            }

            @Override
            public String toString() {
                return String.format("presence of element located by %s -> %s", searchContext, locator);
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final TeasyWebElement searchContext, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                return searchContext.findElements(locator).isEmpty() ? null : searchContext.findElements(locator);
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s -> %s", searchContext, locator);
            }
        };
    }

    @Deprecated
    //use presenceOfAllElementsLocatedBy(final TeasyWebElement searchContext, final By locator)
    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                return searchContext.findElements(locator).isEmpty() ? null : searchContext.findElements(locator);
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s -> %s", searchContext, locator);
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                return driver.findElements(locator).isEmpty() ? null : driver.findElements(locator);
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsInAllFrames(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                FramesTransparentWebDriver framesTransparentWebDriver = (FramesTransparentWebDriver) driver;
                List<WebElement> elementsInFrames = framesTransparentWebDriver.findAllElementsInFrames(locator);
                return elementsInFrames.isEmpty() ? null : elementsInFrames;
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s in all frames", locator);
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsInAllFrames(TeasyWebElement context, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                FramesTransparentWebDriver framesTransparentWebDriver = (FramesTransparentWebDriver) driver;
                List<WebElement> elementsInFrames = framesTransparentWebDriver.findAllElementsInFrames(locator);
                return elementsInFrames.isEmpty() ? null : elementsInFrames;
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s -> %s in all frames", context, locator);
            }
        };
    }

    public static ExpectedCondition<Boolean> visibilityOf(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return element.isDisplayed();
            }

            @Override
            public String toString() {
                return String.format("visibility of element %s", element);
            }
        };
    }

    /**
     * Expected condition to look for elements in frames that will return as soon as elements are found in any frame
     *
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElements(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                return getFirstVisibleWebElements(driver, null, locator);
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s", locator);
            }
        };
    }

    /**
     * Expected condition to look for elements inside given Context (inside element) in frames that will return as soon as elements are found in any frame
     *
     * @param searchContext
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElements(final TeasyWebElement searchContext, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                List<WebElement> visibleElements = getFirstVisibleWebElements(driver, searchContext, locator);
                return isNotEmpty(visibleElements) ? visibleElements : null;
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s -> %s", searchContext, locator);
            }
        };
    }

    /**
     * Expected condition to look for elements in ALL frames that will loop through ALL frames
     * waiting until there is at least 1 visible element in frame (then it will go to next frame)
     *
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElementsInAllFrames(final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                List<WebElement> visibleElements = getFirstVisibleWebElements(driver, null, locator);
                return isNotEmpty(visibleElements) ? visibleElements : null;
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s in all frames", locator);
            }
        };
    }

    /**
     * Expected condition to look for elements inside given Context (inside element) in ALL frames
     * that will loop through ALL frames inside given context
     * waiting until there is at least 1 visible element in frame (then it will go to next frame)
     *
     * @param searchContext
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElementsInAllFrames(final TeasyWebElement searchContext, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                List<WebElement> visibleElements = getFirstVisibleWebElements(driver, searchContext, locator);
                return isNotEmpty(visibleElements) ? visibleElements : null;
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s -> %s in all frames", searchContext, locator);
            }
        };
    }

    private static List<WebElement> getFirstVisibleWebElements(WebDriver driver, TeasyWebElement searchContext, By locator) {
        List<WebElement> elements;
        if (searchContext == null) {
            elements = driver.findElements(locator);
        } else {
            elements = searchContext.findElements(locator);
        }

        List<WebElement> visibleElements = elements.stream()
                .filter(element -> isAvailable(element))
                .collect(Collectors.toList());

        if (visibleElements.isEmpty() && searchContext == null) {
            visibleElements = ((FramesTransparentWebDriver) driver)
                    .findAllElementsInFrames(locator)
                    .stream()
                    .filter(element -> isAvailable(element))
                    .collect(Collectors.toList());
        }

        if (visibleElements.isEmpty() && searchContext != null) {
            visibleElements = ((FramesTransparentWebDriver) driver)
                    .findAllElementsInFrames(searchContext, locator)
                    .stream()
                    .filter(element -> isAvailable(element))
                    .collect(Collectors.toList());
        }
        return isNotEmpty(visibleElements) ? visibleElements : null;
    }

    /**
     * Defines whether it's possible to work with element
     * i.e. is displayed or located under scroll.
     * @param element - our element
     * @return - true if element is available for the user.
     */
    private static boolean isAvailable(WebElement element) {
        return element.isDisplayed()  || isElementHiddenUnderScroll(element)
                && !element.getCssValue("visibility").equals("hidden");
    }

    public static ExpectedCondition<WebElement> visibilityOfElementLocatedBy(By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                try {
                    final WebElement foundElement = driver.findElement(locator);
                    return (isAvailable(foundElement)) ? foundElement : null;
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s", locator);
            }
        };
    }

    /**
     * Trick with zero coordinates for not-displayed element works only in FF
     */
    private static boolean isElementHiddenUnderScroll(WebElement element) {
        return ExecutionUtils.isFF() && element.getLocation().getX() > 0 && element.getLocation().getY() > 0;
    }

    public static ExpectedCondition<WebElement> invisibleOf(final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                return elementIfInvisible(driver.findElement(locator));
            }

            @Override
            public String toString() {
                return String.format("invisibility of element located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<WebElement> invisibleOf(final WebElement element) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                return elementIfInvisible(element);
            }

            @Override
            public String toString() {
                return String.format("invisibility of element %s", element);
            }
        };
    }

    public static ExpectedCondition<Boolean> absenceElementBy(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                try {
                    driver.findElement(locator);
                    return false;
                } catch (WebDriverException ignored) {
                    return true;
                }
            }

            @Override
            public String toString() {
                return String.format("absence of element located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<Boolean> xLocationNotChanged(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                int startXLocation = element.getLocation().getX();
                TestUtils.waitForSomeTime(100, "Wait for element loaded");
                return startXLocation == element.getLocation().getX();
            }

            @Override
            public String toString() {
                return String.format("x location of element %s not changed", element);
            }
        };
    }

    /**
     * We don't know the actual window title without switching to one.
     * This method was always used to make sure that the window appeared. After it we switched to appeared window.
     * Switching between windows is rather time consuming operation
     * <p>
     * To avoid the double switching to the window we are switching to window in this method
     * <p>
     * The same approach is applies to all ExpectedConditions for windows
     *
     * @param title - title of window
     * @return - handle of expected window
     */
    public static ExpectedCondition<String> appearingOfWindowAndSwitchToIt(final String title) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(final WebDriver driver) {
                final String initialHandle = driver.getWindowHandle();
                for (final String handle : driver.getWindowHandles()) {
                    if (needToSwitch(initialHandle, handle)) {
                        driver.switchTo().window(handle);
                        if (driver.getTitle().equals(title)) {
                            return handle;
                        }
                    }
                }
                driver.switchTo().window(initialHandle);
                return null;
            }

            @Override
            public String toString() {
                return String.format("appearing of window by title %s and switch to it", title);
            }
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByPartialTitle(final String fullTitle) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(final WebDriver driver) {
                final String initialHandle = driver.getWindowHandle();
                for (final String handle : driver.getWindowHandles()) {
                    if (needToSwitch(initialHandle, handle)) {
                        driver.switchTo().window(handle);
                        if (fullTitle.contains(driver.getTitle().split("\\(")[0].trim())) {
                            return handle;
                        }
                    }
                }
                driver.switchTo().window(initialHandle);
                return null;
            }

            @Override
            public String toString() {
                return String.format("appearing of window by partial title %s and switch to it", fullTitle);
            }
        };
    }

    public static ExpectedCondition<String> appearingOfWindowWithNewTitle(final Set<String> oldTitle) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(final WebDriver driver) {
                Set<String> windowHandles = driver.getWindowHandles();
                if (windowHandles.containsAll(oldTitle)) {
                    windowHandles.removeAll(oldTitle);
                    if (!windowHandles.isEmpty()) {
                        return windowHandles.iterator().next();
                    }
                }
                return null;
            }

            @Override
            public String toString() {
                return String.format("appearing of window with new title. Old windows titles %s", oldTitle);
            }
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByUrl(final String url) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(final WebDriver driver) {
                final String initialHandle = driver.getWindowHandle();
                for (final String handle : driver.getWindowHandles()) {
                    if (needToSwitch(initialHandle, handle)) {
                        driver.switchTo().window(handle);
                        try {
                            if (url.equals(URLDecoder.decode(driver.getCurrentUrl(), "UTF-8"))) {
                                return handle;
                            }
                        } catch (UnsupportedEncodingException e) {
                            LOGGER.error("UnsupportedEncodingException occured while decoding url - " + driver.getCurrentUrl());
                        }
                    }
                }
                driver.switchTo().window(initialHandle);
                return null;
            }

            @Override
            public String toString() {
                return String.format("appearing of window by url %s and switch to it", url);
            }
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByPartialUrl(final String url) {
        return new ExpectedCondition<String>() {
            @Override
            public String apply(final WebDriver driver) {
                final String initialHandle = driver.getWindowHandle();
                for (final String handle : driver.getWindowHandles()) {
                    if (needToSwitch(initialHandle, handle)) {
                        driver.switchTo().window(handle);
                        if (driver.getCurrentUrl().contains(url)) {
                            return handle;
                        }
                    }
                }
                driver.switchTo().window(initialHandle);
                return null;
            }

            @Override
            public String toString() {
                return String.format("appearing of window by partial url %s and switch to it", url);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final WebElement element, final String text) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return element.getText().contains(text);
            }

            @Override
            public String toString() {
                return String.format("text %s to be present in element %s", text, element);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInTextField(final TextField textField, final String text) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return textField.getText().contains(text);
            }

            @Override
            public String toString() {
                return String.format("text %s to be present in textField %s", text, textField);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final By locator) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return !driver.findElement(locator).getText().isEmpty();
            }

            @Override
            public String toString() {
                return String.format("text to be present in element located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return !element.getText()
                        .isEmpty() || (element.getAttribute("value") != null && !element.getAttribute("value").isEmpty());
            }

            @Override
            public String toString() {
                return String.format("text to be present in element %s", element);
            }
        };
    }

    public static ExpectedCondition<Boolean> presenceOfElementCount(final By locator, final int expectedNumberOfElements) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return driver.findElements(locator).size() == expectedNumberOfElements;
            }

            @Override
            public String toString() {
                return String.format("presence of %s elements located by %s", expectedNumberOfElements, locator);
            }
        };
    }

    public static ExpectedCondition<Boolean> attributeContainsValue(final WebElement element, final String attributeName, final String attributeValue) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return element.getAttribute(attributeName).contains(attributeValue);
            }

            @Override
            public String toString() {
                return String.format("element %s contains attribute %s with value %s", element, attributeName, attributeValue);
            }
        };
    }

    public static ExpectedCondition<Boolean> attributeNotContainsValue(final WebElement element, final String attributeName, final String attributeValue) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return !element.getAttribute(attributeName).contains(attributeValue);
            }

            @Override
            public String toString() {
                return String.format("element %s not contains attribute %s with value %s", element, attributeName, attributeValue);
            }
        };
    }

    public static ExpectedCondition<Boolean> elementHasAttribute(final WebElement element, final String attributeName) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return element.getAttribute(attributeName) != null;
            }

            @Override
            public String toString() {
                return String.format("element %s contains attribute %s", element, attributeName);
            }
        };
    }

    public static ExpectedCondition<Boolean> elementDoesNotHaveAttribute(final WebElement element, final String attributeName) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return element.getAttribute(attributeName) == null;
            }

            @Override
            public String toString() {
                return String.format("element %s not contains attribute %s", element, attributeName);
            }
        };
    }

    public static ExpectedCondition<Boolean> onlyOneWindowIsOpen() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(final WebDriver driver) {
                return driver.getWindowHandles().size() == 1;
            }

            @Override
            public String toString() {
                return "only one window is opened";
            }
        };
    }

    private static boolean needToSwitch(String initialHandle, String handle) {
        if (handle.isEmpty()) {
            LOGGER.error("*****EMPTY WINDOW HANDLE*****Attempt to switch to window with '' handle detected! Ignoring it.");
            return false;
        }
        return !initialHandle.equals(handle);
    }

    private static WebElement elementIfInvisible(final WebElement element) {
        return !element.isDisplayed() ? element : null;
    }
}