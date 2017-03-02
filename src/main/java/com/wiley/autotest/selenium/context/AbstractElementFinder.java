package com.wiley.autotest.selenium.context;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.util.*;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapList;
import static com.wiley.autotest.utils.ExecutionUtils.isChrome;
import static com.wiley.autotest.utils.ExecutionUtils.isSafari;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 15.02.12
 * Time: 13:02
 */
public class AbstractElementFinder {
    private static final String EXPLANATION_MESSAGE_FOR_WAIT = "Wait for retry find element";
    protected ElementFinder elementFinder;
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementFinder.class);
    //VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    protected static final long SLEEP_IN_MILLISECONDS = 1000;

    protected WebElement element(final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator).get(0);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in element()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected WebElement element(final SearchContext searchContext, final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(searchContext, locator).get(0);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in element()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected List<WebElement> elements(final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in elements()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected List<WebElement> elements(final SearchContext searchContext, final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(searchContext, locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in elements()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected final WebElement elementOrNull(final By locator) {
        return findElementByNoThrow(locator);
    }

    protected final WebElement elementOrNull(final SearchContext searchContext, final By locator) {
        return findElementByNoThrow(searchContext, locator);
    }

    protected Button button(By locator) {
        return getElement(Button.class, locator);
    }

    protected Button button(SearchContext searchContext, By locator) {
        return getElement(Button.class, searchContext, locator);
    }

    protected List<Button> buttons(By locator) {
        return getElements(Button.class, locator);
    }

    protected List<Button> buttons(SearchContext searchContext, By locator) {
        return getElements(Button.class, searchContext, locator);
    }

    protected Link link(By locator) {
        return getElement(Link.class, locator);
    }

    protected Link link(SearchContext searchContext, By locator) {
        return getElement(Link.class, searchContext, locator);
    }

    protected List<Link> links(By locator) {
        return getElements(Link.class, locator);
    }

    protected List<Link> links(SearchContext searchContext, By locator) {
        return getElements(Link.class, searchContext, locator);
    }

    protected CheckBox checkBox(By locator) {
        return getElement(CheckBox.class, locator);
    }

    protected CheckBox checkBox(SearchContext searchContext, By locator) {
        return getElement(CheckBox.class, searchContext, locator);
    }

    protected List<CheckBox> checkBoxes(By locator) {
        return getElements(CheckBox.class, locator);
    }

    protected List<CheckBox> checkBoxes(SearchContext searchContext, By locator) {
        return getElements(CheckBox.class, searchContext, locator);
    }

    protected RadioButton radioButton(By locator) {
        return getElement(RadioButton.class, locator);
    }

    protected RadioButton radioButton(SearchContext searchContext, By locator) {
        return getElement(RadioButton.class, searchContext, locator);
    }

    protected List<RadioButton> radioButtons(By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected List<RadioButton> radioButtons(SearchContext searchContext, By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected Select select(By locator) {
        return getElement(Select.class, locator);
    }

    protected Select select(SearchContext searchContext, By locator) {
        return getElement(Select.class, searchContext, locator);
    }

    protected List<Select> selects(By locator) {
        return getElements(Select.class, locator);
    }

    protected List<Select> selects(SearchContext searchContext, By locator) {
        return getElements(Select.class, locator);
    }

    protected TextField textField(By locator) {
        return getElement(TextField.class, locator);
    }

    protected TextField textField(SearchContext searchContext, By locator) {
        return getElement(TextField.class, searchContext, locator);
    }


    protected List<TextField> textFields(By locator) {
        return getElements(TextField.class, locator);
    }

    protected List<TextField> textFields(SearchContext searchContext, By locator) {
        return getElements(TextField.class, locator);
    }

    /**
     * Use this method when you need element which is present in DOM but you are not sure if it's visible or not
     * call it from a private getter method on Page
     * use "inDOM" as a postfix for a method name
     */
    protected WebElement domElement(By locator) {
        try {
            return waitForPresenceOfElementLocatedBy(locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in domElement()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected WebElement domElement(SearchContext searchContext, By locator) {
        try {
            return waitForPresenceOfElementLocatedBy(searchContext, locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in domElement()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected List<WebElement> domElements(By locator) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in domElements()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    protected List<WebElement> domElements(SearchContext searchContext, By locator) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(searchContext, locator);
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error("****WebDriverException in domElements()****", wde);
            fail(generateErrorMessage() + "\nException:" + wde.getMessage());
        }
        return null;
    }

    /**
     * @param locator- locator for element you would like to find
     * @return list of webElements or empty list in case no such element were found by given locator
     */
    protected List<WebElement> domElementsOrEmpty(final By locator) {
        return wrapList(elementFinder.findElementsBy(locator), locator);
    }

    /**
     * This method is to be used for different conditions you want to wait for to happen with element using LOCATOR
     *
     * @param condition - condition to happen with element
     *                  for Locator "STALE" condition does not make sense so throwing an exception in case someone mistakenly decide to use it
     * @param locator   - locator for element you would like condition to happen
     */
    protected void condition(Condition condition, By locator) {
        condition(condition, null, locator);
    }

    /**
     * This method is to be used for different conditions you want to wait for to happen with ELEMENT
     *
     * @param condition - condition to happen with element
     *                  for element "PRESENT_IN_DOM" and "ABSENT_IN_DOM" condition does not make sense so throwing an exception in case someone mistakenly decide to use it
     * @param element   - element you would like condition to happen
     */
    protected void condition(Condition condition, WebElement element) {
        condition(condition, null, element);
    }

    protected void condition(Condition condition, ConditionParams params, WebElement element) {
        switch (condition) {
            case VISIBLE: {
                elementFinder.waitForVisibilityOf(element);
                break;
            }
            case NOT_VISIBLE: {
                elementFinder.waitForInvisibilityOfElement(element);
                break;
            }
            case PRESENT_IN_DOM: {
                throw new UnsupportedConditionException("PRESENT_IN_DOM condition is not applicable for Element.");
            }
            case ABSENT_IN_DOM: {
                throw new UnsupportedConditionException("ABSENT_IN_DOM condition is not applicable for Element. Use STALE instead.");
            }
            case STALE: {
                waitForStalenessOf(element);
                break;
            }
            case HAS_TEXT: {
                if (params != null) {
                    waitForTextToBePresentIn(element, params.getText());
                }
                break;
            }
        }
    }

    protected void condition(Condition condition, ConditionParams params, By locator) {
        switch (condition) {
            case VISIBLE: {
                waitForVisibilityOfElementLocatedBy(locator);
                break;
            }
            case NOT_VISIBLE: {
                waitForInvisibilityOfElementLocatedBy(locator);
                break;
            }
            case PRESENT_IN_DOM: {
                waitForPresenceOfElementLocatedBy(locator);
                break;
            }
            case ABSENT_IN_DOM: {
                waitForAbsenceOfElementLocatedBy(locator);
                break;
            }
            case STALE: {
                throw new UnsupportedConditionException("STALE condition is not applicable for By. It is only applicable for existing WebElement");
            }
        }
    }

    public enum Condition {
        VISIBLE,
        NOT_VISIBLE,
        PRESENT_IN_DOM,
        ABSENT_IN_DOM,
        STALE,
        HAS_TEXT
    }

    public class ConditionParams {
        String text;

        public ConditionParams(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public ConditionParams setText(String text) {
            this.text = text;
            return this;
        }
    }

    protected final void customCondition(final ExpectedCondition<Boolean> condition, long timeout) {
        elementFinder.waitForCondition(condition, timeout);
    }

    private class UnsupportedConditionException extends RuntimeException {
        UnsupportedConditionException(String message) {
            super(message);
        }
    }

    protected <T extends Element> T getElement(Class<T> elementType, By by) {
        try {
            return getWebElementWrapper(element(by)).getElement(elementType, by);
        } catch (Exception e) {
            return null;
        }
    }

    protected <T extends Element> T getElement(Class<T> elementType, SearchContext searchContext, By by) {
        try {
            return getWebElementWrapper(element(searchContext, by)).getElement(elementType, by);
        } catch (Exception e) {
            return null;
        }
    }

    protected <T extends Element> List<T> getElements(Class<T> elementType, By by) {
        List<T> result = new ArrayList<>();
        elements(by).stream().forEach(element -> result.add(getWebElementWrapper(element).getElement(elementType, by)));
        return result;
    }

    protected <T extends Element> List<T> getElements(Class<T> elementType, SearchContext searchContext, By by) {
        List<T> result = new ArrayList<>();
        elements(searchContext, by).stream().forEach(element -> result.add(getWebElementWrapper(element).getElement(elementType, by)));
        return result;
    }

    /**
     * Takes the 4th method from a StackTrace chain. It should be the method from Page that called method
     * Split method by words based on camel case naming convention
     * Make first letter of first word as upper case and all other words as lower case
     * for example for "generateErrorMessage" method should return "Generate error message"
     *
     * @return String of words separated by spaces
     */
    protected String generateErrorMessage() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length < 4) {
            return "***Code issue during generating error message for assert! Check the code at AbstractElementFinder";
        }

        Optional<StackTraceElement> first = Arrays.asList(stackTrace).stream().filter(stackTraceElement -> stackTraceElement.getClassName().toLowerCase().endsWith("page")).findFirst();

        String nameOfMethodThatCalledMe;
        if (first.isPresent()) {
            nameOfMethodThatCalledMe = first.get().getMethodName();
        } else {
            nameOfMethodThatCalledMe = stackTrace[3].getMethodName();
        }

        String[] splitName = nameOfMethodThatCalledMe.split("(?<=[a-z])(?=[A-Z])");

        StringBuilder errorMessage = new StringBuilder();
        //make first letter of first word upper cased
        errorMessage.append(Character.toUpperCase(splitName[0].charAt(0))).append(splitName[0].substring(1));
        for (int i = 1; i < splitName.length; i++) {
            //make first letter of each of next words lower cased
            errorMessage.append(" ").append(Character.toLowerCase(splitName[i].charAt(0))).append(splitName[i].substring(1));
        }

        return errorMessage.toString() + " failed";
    }

    public void init(final WebDriver driver, Long timeout) {
        elementFinder = new WebDriverAwareElementFinder(driver, new WebDriverWait(driver, timeout, SLEEP_IN_MILLISECONDS));
    }

    protected WebElementWrapper getWebElementWrapper(final WebElement wrappedElement) {
        return new WebElementWrapper(wrappedElement);
    }

    /**
     * use {@link #elementOrNull(By)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement findElementByNoThrow(final By locator) {
        try {
            return wrap(elementFinder.findElementBy(locator), locator);
        } catch (WebDriverException e) {
            //TODO hotfix for safari as it seems that this method does not work correct without timeout
            if (isSafari()) {
                TestUtils.waitForSafari();
                try {
                    return wrap(elementFinder.findElementBy(locator), locator);
                } catch (WebDriverException ignoreSafari) {
                    return null;
                }
            }
            return null;
        }
    }

    /**
     * use {@link #elementOrNull(SearchContext, By)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement findElementByNoThrow(final SearchContext searchContext, final By locator) {
        try {
            return wrap(elementFinder.findElementBy(searchContext, locator), locator);
        } catch (Exception e) {
            //TODO hotfix for safari as it seems that this method does not work correct without timeout
            if (isSafari()) {
                TestUtils.waitForSafari();
                try {
                    return wrap(elementFinder.findElementBy(searchContext, locator), locator);
                } catch (Exception ignoreSafari) {
                    return null;
                }
            }
            return null;
        }
    }

    /**
     * This method is valid but now it's used in many places incorrectly. All usages should be verified and fixed
     * then deprecated annotation will be removed
     */
    @Deprecated
    protected final List<WebElement> findElementsBy(final By locator) {
        return wrapList(elementFinder.findElementsBy(locator), locator);
    }

    protected final void waitForWindowToBeAppearedAndSwitchToIt(final String title) {
        try {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedAndSwitchToIt(title);
        } catch (WebDriverException e) {
            fail("Unable to locate window with title '" + title + "'");
        }
    }

    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeoutInSec) {
        waitWindowIsAppearInChrome();
        elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url, timeoutInSec);
    }

    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url) {
        try {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url);
        } catch (WebDriverException e) {
            fail("Unable to locate window with url '" + url + "'");
        }
    }

    protected final void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(final String partialTitle) {
        try {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(partialTitle);
        } catch (WebDriverException e) {
            fail("Unable to locate window with title '" + partialTitle + "'");
        }
    }

    protected final void clickButtonAndWaitForNewPopUpWindowAndSwitchToIt(WebElement webElement) {
        elementFinder.waitForNewPopUpWindowAndSwitchToIt(webElement);
    }

    protected final String waitForNewPopUpWindow(Set<String> windowHandles) {
        return elementFinder.waitForNewPopUpWindow(windowHandles);
    }

    protected final void closeBrowserWindow() {
        elementFinder.closeCurrentBrowserWindow();
    }

    protected final boolean isTextPresentInElement(final By locator, final String text) {
        return elementFinder.isTextPresentInElement(locator, text);
    }

    private WebElement waitForPresenceOfElementLocatedBy(final By locator) {
        return wrap(elementFinder.waitForPresenceOfElementLocatedBy(locator), locator);
    }

    private WebElement waitForPresenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return wrap(elementFinder.waitForPresenceOfElementLocatedBy(searchContext, locator), locator);
    }

    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeout) {
        return wrap(elementFinder.waitForPresenceOfElementLocatedBy(locator, timeout), locator);
    }

    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, final String errorMessage) {
        try {
            return waitForPresenceOfElementLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeout, final String errorMessage) {
        try {
            return waitForPresenceOfElementLocatedBy(locator, timeout);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

    @Deprecated
    protected WebElement waitForPresenceOfElementLocatedByLinkText(final String linkText) {
        return waitForPresenceOfElementLocatedBy(AbstractPage.getLinkByXpath(linkText));
    }

    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedByLinkText(final String linkText, final String errorMessage) {
        try {
            return waitForPresenceOfElementLocatedBy(AbstractPage.getLinkByXpath(linkText));
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

    @Deprecated
    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return wrapList(elementFinder.waitForPresenceOfAllElementsLocatedBy(locator), locator);
    }

    @Deprecated
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeoutInSec) {
        try {
            return wrapList(elementFinder.waitForPresenceOfAllElementsLocatedBy(locator, timeoutInSec), locator);
        } catch (TimeoutException ignored) {
            return new ArrayList<WebElement>();
        }
    }

    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return wrapList(elementFinder.waitForPresenceOfAllElementsLocatedBy(searchContext, locator), locator);
    }

    /**
     * Use elements/selects/textFields/checkBoxes - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, String errorMessage) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(locator);
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
        return null;
    }

    /**
     * Instead of this method create a 1 line private getter with meaningful name in Page
     * and call element(By locator) instead of this method. Error message will be automatically generated based on this.
     * This method will be removed by Jan 2017.
     */
    @Deprecated
    protected final WebElement waitForVisibilityOfElementLocatedBy(final By locator, final String errorMessage) {
        try {
            return waitForVisibilityOfElementLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
            return null;
        }
    }

    @Deprecated
    protected final WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeout) {
        return wrap(elementFinder.waitForVisibilityOfElementLocatedBy(locator, timeout), locator);
    }

    private WebElement waitForVisibilityOfElementLocatedBy(final By locator) {
        return wrap(elementFinder.waitForVisibilityOfElementLocatedBy(locator), locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator) {
        return wrapList(elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator), locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return wrapList(elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator), locator);
    }

    /**
     * Instead of this method create a 1 line private getter with meaningful name in Page
     * and call elements(By locator) instead of this method. Error message will be automatically generated based on this.
     * This method will be removed by Jan 2017.
     */
    @Deprecated
    protected final List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator, final String errorMessage) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
            return null;
        }
    }

    private WebElement waitForInvisibilityOfElementLocatedBy(final By locator) {
        return wrap(elementFinder.waitForInvisibilityOfElementLocatedBy(locator), locator);
    }

    private void waitForAbsenceOfElementLocatedBy(final By locator) {
        elementFinder.waitForAbsenceOfElementLocatedBy(locator);
    }

    protected final WebElement waitForElementByNoThrow(final By locator, long timeout) {
        try {
            return waitForPresenceOfElementLocatedBy(locator, timeout);
        } catch (WebDriverException e) {
            return null;
        }
    }

    protected final Boolean waitForTextToBePresentInElement(final By locator, final String text) {
        return elementFinder.waitForTextToBePresentInElement(locator, text);
    }

    protected final Boolean waitForTextToBePresentInElement(final By locator) {
        return elementFinder.waitForTextToBePresentInElement(locator);
    }

    protected final Boolean waitForTextToBePresentIn(final WebElement element, final String text) {
        try {
            return elementFinder.waitForTextToBePresentIn(element, text);
        } catch (TimeoutException e) {
            fail("TimeoutException during waitForTextToBePresentIn(). Actual " + element.getText() + ". Expected text to be present " + text + ".");
        }
        return null;
    }

    protected final Boolean waitForTextToBePresentIn(final TextField textField, final String text) {
        return elementFinder.waitForTextToBePresentIn(textField, text);
    }

    protected final Boolean waitForTextToBePresentIn(final WebElement element) {
        return elementFinder.waitForTextToBePresentIn(element);
    }

    private WebElement waitForElementToBeClickable(final By locator) {
        return wrap(elementFinder.waitForElementToBeClickable(locator), locator);
    }

    protected final WebElement waitForElementToBeClickable(final By locator, final String errorMessage) {
        try {
            return waitForElementToBeClickable(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

    private void waitForElementToBeClickable(final WebElement element) {
        elementFinder.waitForElementToBeClickable(element);
    }

    protected final void waitForElementToBeClickable(final WebElement element, final String errorMessage) {
        try {
            waitForElementToBeClickable(element);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
    }

    protected final void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeout) {
        elementFinder.waitForPresenceOfElementCount(locator, expectedNumberOfElements, timeout);
    }

    protected final void waitForPageToLoad() {
        elementFinder.waitForPageToLoad();
        if (isSafari()) {
            TestUtils.waitForSafari();
        }
    }

    protected final void waitForListToLoad() {
        elementFinder.waitForListToLoad();
    }

    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue);
    }

    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeout) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue, timeout);
    }

    protected final void waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementNotContainsAttributeValue(element, attributeName, attributeValue);
    }

    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementContainsAttribute(element, attributeName);
    }

    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        elementFinder.waitForElementContainsAttribute(element, attributeName, timeout);
    }

    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName);
    }

    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName, timeout);
    }

    /**
     * We catch such exception because actually the browser is shown.
     *
     * @param webElement - current element
     * @return - true if element is Stale
     */
    protected final Boolean waitForStalenessOf(final WebElement webElement) {
        try {
            return elementFinder.waitForStalenessOf(webElement);
        } catch (UnreachableBrowserException ignored) {
            return true;
        }
    }

    /**
     * We catch such exception because actually the browser is shown.
     *
     * @param webElement - current element
     * @return - true if element is Stale
     */
    protected final Boolean waitForStalenessOf(final WebElement webElement, long timeout) {
        try {
            return elementFinder.waitForStalenessOf(webElement, timeout);
        } catch (UnreachableBrowserException ignored) {
            return true;
        }
    }

    protected final Boolean waitForStalenessOf(final WebElement webElement, String errorMessage) {
        try {
            return waitForStalenessOf(webElement);
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
        return false;
    }

    public static void fail(final String errorMessage) {
        Reporter.log("ERROR: " + errorMessage);
        throw new AssertionError(errorMessage);
    }

    protected final void fail(final String errorMessage, Throwable cause) {
        Reporter.log("ERROR: " + errorMessage + cause.getMessage());
        throw new Error(errorMessage + cause.getMessage(), cause);
        //throw new AssertionError(errorMessage + cause.getMessage(), cause);//since 1.7
    }

    protected final void setTimeout(final long timeout) {
        elementFinder.setTimeout(timeout);
    }

    public void switchToLastWindow() {
        elementFinder.switchToLastWindow();
    }

    public void waitUntilOnlyOneWindowIsOpen(String errorMessage) {
        try {
            elementFinder.waitUntilOnlyOneWindowIsOpen();
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
    }

    //TODO NT: In chrome test hangs before switch to new window, to avoid this add timeout
    private void waitWindowIsAppearInChrome() {
        if (isChrome()) {
            TestUtils.waitForSomeTime(3000, "Wait for window is appear in chrome");
        }
    }
}

