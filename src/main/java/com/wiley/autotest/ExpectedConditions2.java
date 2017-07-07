package com.wiley.autotest;

import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.TextField;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
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
        return driver -> searchContext.findElement(locator);
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final OurWebElement searchContext, final By locator) {
        return driver -> searchContext.findElements(locator).isEmpty() ? null : searchContext.findElements(locator);
    }
    @Deprecated
    //use presenceOfAllElementsLocatedBy(final OurWebElement searchContext, final By locator)
    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return driver -> searchContext.findElements(locator).isEmpty() ? null : searchContext.findElements(locator);
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsLocatedBy(final By locator) {
        return driver -> driver.findElements(locator).isEmpty() ? null : driver.findElements(locator);
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsInAllFrames(final By locator) {
        return driver -> {
            FramesTransparentWebDriver framesTransparentWebDriver = (FramesTransparentWebDriver) driver;
            List<WebElement> elementsInFrames = framesTransparentWebDriver.findAllElementsInFrames(locator);
            return elementsInFrames.isEmpty() ? null : elementsInFrames;
        };
    }

    public static ExpectedCondition<List<WebElement>> presenceOfAllElementsInAllFrames(OurWebElement context, final By locator) {
        return driver -> {
            FramesTransparentWebDriver framesTransparentWebDriver = (FramesTransparentWebDriver) driver;
            List<WebElement> elementsInFrames = framesTransparentWebDriver.findAllElementsInFrames(context, locator);
            return elementsInFrames.isEmpty() ? null : elementsInFrames;
        };
    }

    public static ExpectedCondition<Boolean> visibilityOf(final WebElement element) {
        return driver -> element.isDisplayed();
    }

    /**
     * Expected condition to look for elements in frames that will return as soon as elements are found in any frame
     *
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElements(final By locator) {
        return driver -> getFirstVisibleWebElements(driver, null, locator);
    }

    /**
     * Expected condition to look for elements inside given Context (inside element) in frames that will return as soon as elements are found in any frame
     *
     * @param searchContext
     * @param locator
     * @return
     */
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElements(final OurWebElement searchContext, final By locator) {
        return driver -> {
            List<WebElement> visibleElements = getFirstVisibleWebElements(driver, searchContext, locator);
            return isNotEmpty(visibleElements) ? visibleElements : null;
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
        return driver -> {
            List<WebElement> visibleElements = getFirstVisibleWebElements(driver, null, locator);
            return isNotEmpty(visibleElements) ? visibleElements : null;
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
    public static ExpectedCondition<List<WebElement>> visibilityOfFirstElementsInAllFrames(final OurWebElement searchContext, final By locator) {
        return driver -> {
            List<WebElement> visibleElements = getFirstVisibleWebElements(driver, searchContext, locator);
            return isNotEmpty(visibleElements) ? visibleElements : null;
        };
    }

    private static List<WebElement> getFirstVisibleWebElements(WebDriver driver, OurWebElement searchContext, By locator) {
        List<WebElement> elements;
        if (searchContext == null) {
            elements = driver.findElements(locator);
        } else {
            elements = searchContext.findElements(locator);
        }

        List<WebElement> visibleElements = elements.stream()
                .filter(element -> element.isDisplayed() || isElementHiddenUnderScroll(element))
                .collect(Collectors.toList());

        if (visibleElements.isEmpty() && searchContext == null) {
            visibleElements = ((FramesTransparentWebDriver) driver)
                    .findAllElementsInFrames(locator)
                    .stream()
                    .filter(element -> element.isDisplayed() || isElementHiddenUnderScroll(element))
                    .collect(Collectors.toList());
        }

        if (visibleElements.isEmpty() && searchContext != null) {
            visibleElements = ((FramesTransparentWebDriver) driver)
                    .findAllElementsInFrames(searchContext, locator)
                    .stream()
                    .filter(element -> element.isDisplayed() || isElementHiddenUnderScroll(element))
                    .collect(Collectors.toList());
        }
        return isNotEmpty(visibleElements) ? visibleElements : null;
    }

    public static ExpectedCondition<WebElement> visibilityOfElementLocatedBy(By locator) {
        return driver -> {
            try {
                final WebElement foundElement = driver.findElement(locator);
                return (foundElement.isDisplayed() || isElementHiddenUnderScroll(foundElement)) ? foundElement : null;
            } catch (Exception e) {
                return null;
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
        return driver -> elementIfInvisible(driver.findElement(locator));
    }

    public static ExpectedCondition<WebElement> invisibleOf(final WebElement element) {
        return driver -> elementIfInvisible(element);
    }

    public static ExpectedCondition<Boolean> absenceElementBy(final By locator) {
        return driver -> {
            try {
                driver.findElement(locator);
                return false;
            } catch (WebDriverException ignored) {
                return true;
            }
        };
    }

    public static ExpectedCondition<Boolean> xLocationNotChanged(final WebElement webElement) {
        return driver -> {
            int startXLocation = webElement.getLocation().getX();
            TestUtils.waitForSomeTime(100, "Wait for element loaded");
            return startXLocation == webElement.getLocation().getX();
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
        return driver -> {
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
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByPartialTitle(final String fullTitle) {
        return driver -> {
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
        };
    }

    public static ExpectedCondition<String> appearingOfWindowWithNewTitle(final Set<String> oldTitle) {
        return driver -> {
            Set<String> windowHandles = driver.getWindowHandles();
            if (windowHandles.containsAll(oldTitle)) {
                windowHandles.removeAll(oldTitle);
                if (!windowHandles.isEmpty()) {
                    return windowHandles.iterator().next();
                }
            }
            return null;
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByUrl(final String url) {
        return driver -> {
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
        };
    }

    public static ExpectedCondition<String> appearingOfWindowByPartialUrl(final String url) {
        return driver -> {
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
        };
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final WebElement element, final String text) {
        return driver -> element.getText().contains(text);
    }

    public static ExpectedCondition<Boolean> textToBePresentInTextField(final TextField textField, final String text) {
        return driver -> textField.getText().contains(text);
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final By by) {
        return driver -> !driver.findElement(by).getText().isEmpty();
    }

    public static ExpectedCondition<Boolean> textToBePresentInElement(final WebElement element) {
        return driver -> !element.getText()
                .isEmpty() || (element.getAttribute("value") != null && !element.getAttribute("value").isEmpty());
    }

    public static ExpectedCondition<Boolean> pageToLoad() {
        return webDriver -> "complete".equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState"));
    }

    public static ExpectedCondition<Boolean> presenceOfElementCount(final By locator, final int expectedNumberOfElements) {
        return driver -> driver.findElements(locator).size() == expectedNumberOfElements;
    }

    public static ExpectedCondition<Boolean> attributeContainsValue(final WebElement element, final String attributeName, final String attributeValue) {
        return driver -> element.getAttribute(attributeName).contains(attributeValue);
    }

    public static ExpectedCondition<Boolean> attributeNotContainsValue(final WebElement element, final String attributeName, final String attributeValue) {
        return driver -> !element.getAttribute(attributeName).contains(attributeValue);
    }

    public static ExpectedCondition<Boolean> elementHasAttribute(final WebElement element, final String attributeName) {
        return driver -> element.getAttribute(attributeName) != null;
    }

    public static ExpectedCondition<Boolean> elementDoesNotHaveAttribute(final WebElement element, final String attributeName) {
        return driver -> element.getAttribute(attributeName) == null;
    }

    public static ExpectedCondition<Boolean> onlyOneWindowIsOpen() {
        return driver -> driver.getWindowHandles().size() == 1;
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