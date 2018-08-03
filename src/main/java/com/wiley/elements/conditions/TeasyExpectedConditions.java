package com.wiley.elements.conditions;

import com.wiley.driver.frames.FramesTransparentWebDriver;
import com.wiley.elements.TeasyElement;
import com.wiley.utils.ExecutionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

/**
 * Library of custom expected conditions
 */
public final class TeasyExpectedConditions {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeasyExpectedConditions.class);

    private TeasyExpectedConditions() {
    }

    public static ExpectedCondition<WebElement> presenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                List<WebElement> els = searchContext.findElements(locator);
                return els.isEmpty() ? null : els.get(0);
            }

            @Override
            public String toString() {
                return String.format("presence of element located by %s -> %s", searchContext, locator);
            }
        };
    }

    public static ExpectedCondition<WebElement> presenceOfElementLocatedBy(final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                List<WebElement> els = driver.findElements(locator);
                return els.isEmpty() ? null : els.get(0);
            }

            @Override
            public String toString() {
                return String.format("presence of element located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final TeasyElement searchContext, final By locator) {
        return new ExpectedCondition<List<WebElement>>() {
            @Override
            public List<WebElement> apply(final WebDriver driver) {
                List<WebElement> els = searchContext.findElements(locator);
                return els.isEmpty() ? null : els;
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
                List<WebElement> els = driver.findElements(locator);
                return els.isEmpty() ? null : els;
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

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsInAllFrames(TeasyElement context, final By locator) {
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

    public static ExpectedCondition<WebElement> presenceOfElementInAllFrames(TeasyElement context, final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                FramesTransparentWebDriver framesTransparentWebDriver = (FramesTransparentWebDriver) driver;
                List<WebElement> elementsInFrames = framesTransparentWebDriver.findAllElementsInFrames(locator);
                return elementsInFrames.isEmpty() ? null : elementsInFrames.get(0);
            }

            @Override
            public String toString() {
                return String.format("presence of all elements located by %s -> %s in all frames", context, locator);
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
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElements(final TeasyElement searchContext, final By locator) {
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
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElementsInAllFrames(final TeasyElement searchContext, final By locator) {
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

    public static ExpectedCondition<WebElement> visibilityOfFirstElementInAllFrames(final TeasyElement searchContext, final By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                List<WebElement> visibleElements = getFirstVisibleWebElements(driver, searchContext, locator);
                return isNotEmpty(visibleElements) ? visibleElements.get(0) : null;
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s -> %s in all frames", searchContext, locator);
            }
        };
    }

    private static List<WebElement> getFirstVisibleWebElements(WebDriver driver, TeasyElement searchContext, By locator) {
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
     *
     * @param element - our element
     * @return - true if element is available for the user.
     */
    private static boolean isAvailable(WebElement element) {
        return element.isDisplayed() || isElementHiddenUnderScroll(element)
                && !element.getCssValue("visibility").equals("hidden");
    }

    public static ExpectedCondition<WebElement> visibilityOfElementLocatedBy(By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                return getFirstVisibleWebElement(driver, locator);
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s", locator);
            }
        };
    }

    public static ExpectedCondition<WebElement> visibilityOfElementLocatedBy(final TeasyElement searchContext, By locator) {
        return new ExpectedCondition<WebElement>() {
            @Override
            public WebElement apply(final WebDriver driver) {
                return getFirstVisibleWebElement(searchContext, locator);
            }

            @Override
            public String toString() {
                return String.format("visibility of element located by %s -> %s", searchContext, locator);
            }
        };
    }

    private static WebElement getFirstVisibleWebElement(SearchContext searchContext, By locator) {
        try {
            final WebElement foundElement = searchContext.findElement(locator);
            if (isAvailable(foundElement)) {
                return foundElement;
            } else {
                List<WebElement> elements = searchContext.findElements(locator);
                for (WebElement element : elements) {
                    if (isAvailable(element)) {
                        return element;
                    }
                }
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Trick with zero coordinates for not-displayed element works only in FF
     */
    private static boolean isElementHiddenUnderScroll(WebElement element) {
        return ExecutionUtils.isFF() && element.getLocation().getX() > 0 && element.getLocation()
                .getY() > 0;
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
                            LOGGER.error("UnsupportedEncodingException occured while decoding url - " + driver
                                    .getCurrentUrl());
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