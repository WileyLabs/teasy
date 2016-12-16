package com.wiley.autotest.selenium.context;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.utils.ExecutionUtils;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrap;
import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapList;
import static java.util.Collections.emptyList;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 15.02.12
 * Time: 13:02
 */
public class AbstractElementFinder {
    public static final String EXPLANATION_MESSAGE_FOR_WAIT = "Wait for retry find element";
    protected ElementFinder elementFinder;
    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractElementFinder.class);
    //VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    protected static final long SLEEP_IN_MILLISECONDS = 1000;

    protected WebElement element(final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator).get(0);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return null;
        }
    }

    protected List<WebElement> elements(final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return emptyList();
        }
    }

    protected List<WebElement> elementsInFrames(final By locator) {
        try {
            return waitForVisibilityOfAllElementsLocatedByInFrames(locator);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return emptyList();
        }
    }


    protected Button button(By locator) {
        return getElementNew(Button.class, locator, generateErrorMessage());
    }

    protected Button button(SearchContext searchContext, By locator) {
        return getElementNew(Button.class, searchContext, locator);
    }

    protected List<Button> buttons(By locator) {
        return getElementListNew(Button.class, locator, generateErrorMessage());
    }

    protected List<Button> buttons(SearchContext searchContext, By locator) {
        return getElementListNew(Button.class, searchContext, locator);
    }

    protected Link link(By locator) {
        return getElementNew(Link.class, locator, generateErrorMessage());
    }

    protected Link link(SearchContext searchContext, By locator) {
        return getElementNew(Link.class, searchContext, locator, generateErrorMessage());
    }

    protected List<Link> links(By locator) {
        return getElementListNew(Link.class, locator, generateErrorMessage());
    }

    protected CheckBox checkBox(By locator) {
        return getElementNew(CheckBox.class, locator, generateErrorMessage());
    }

    protected CheckBox checkBox(SearchContext searchContext, By locator) {
        return getElementNew(CheckBox.class, searchContext, locator, generateErrorMessage());
    }

    protected List<CheckBox> checkBoxes(By locator) {
        return getElementListNew(CheckBox.class, locator, generateErrorMessage());
    }

    protected RadioButton radioButton(By locator) {
        return getElementNew(RadioButton.class, locator, generateErrorMessage());
    }

    protected RadioButton radioButton(SearchContext searchContext, By locator) {
        return getElementNew(RadioButton.class, searchContext, locator, generateErrorMessage());
    }

    protected List<RadioButton> radioButtons(By locator) {
        return getElementListNew(RadioButton.class, locator, generateErrorMessage());
    }

    protected Select select(By locator) {
        return getElementNew(Select.class, locator, generateErrorMessage());
    }

    protected Select select(SearchContext searchContext, By locator) {
        return getElementNew(Select.class, searchContext, locator, generateErrorMessage());
    }

    protected List<Select> selects(By locator) {
        return getElementListNew(Select.class, locator, generateErrorMessage());
    }

    protected TextField textField(By locator) {
        return getElementNew(TextField.class, locator, generateErrorMessage());
    }

    protected TextField textField(SearchContext searchContext, By locator) {
        return getElementNew(TextField.class, searchContext, locator, generateErrorMessage());
    }


    protected List<TextField> textFields(By locator) {
        return getElementListNew(TextField.class, locator, generateErrorMessage());
    }

    /**
     * Use this method when you need element which is present in DOM but you are not sure if it's visible or not
     * call it from a private getter method on Page
     * use "inDOM" as a postfix for a method name
     */
    protected WebElement domElement(By locator) {
        try {
            return waitForPresenceOfElementLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return null;
        }
    }

    protected List<WebElement> domElements(By locator) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return emptyList();
        }
    }

    protected List<WebElement> domElementsInFrames(By locator) {
        try {
            return waitForPresenceOfAllElementsLocatedByInFrames(locator);
        } catch (WebDriverException e) {
            fail(generateErrorMessage());
            return emptyList();
        }
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
        public UnsupportedConditionException(String message) {
            super(message);
        }
    }

    @Deprecated
    /**
     * use button(By locator)
     */
    protected Button waitForButton(By locator, String errorMessage) {
        return getElement(Button.class, locator, errorMessage);
    }

//TODO VE: do not delete this methods untill Jan 2017
//    protected Button waitForButton(SearchContext searchContext, By locator, String errorMessage) {
//        return getElement(Button.class, searchContext, locator, errorMessage);
//    }
//
//    protected Button waitForButton(By locator, long timeout, String errorMessage) {
//        return getElement(Button.class, locator, timeout, errorMessage);
//    }

    @Deprecated
    /**
     * use link(By locator)
     */
    protected Link waitForLink(By locator, String errorMessage) {
        return getElement(Link.class, locator, errorMessage);
    }

//TODO VE: do not delete this methods untill Jan 2017
//    protected Link waitForLink(By locator, long timeout, String errorMessage) {
//        return getElement(Link.class, locator, errorMessage);
//    }

    @Deprecated
    /**
     * use links(By locator)
     */
    protected List<Link> waitForLinkList(By locator, String errorMessage) {
        return getElementList(Link.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use checkBox(By locator)
     */
    protected CheckBox waitForCheckBox(By locator, String errorMessage) {
        return getElement(CheckBox.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use checkBoxes(By locator)
     */
    protected List<CheckBox> waitForCheckBoxList(By locator, String errorMessage) {
        return getElementList(CheckBox.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use select(By locator)
     */
    protected Select waitForSelect(By locator, String errorMessage) {
        return getElement(Select.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use selects(By locator)
     */
    protected List<Select> waitForSelectList(By locator, String errorMessage) {
        return getElementList(Select.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use radioButton(By locator)
     */
    protected RadioButton waitForRadioButton(By locator, String errorMessage) {
        return getElement(RadioButton.class, locator, errorMessage);
    }

    //TODO VE: do not delete this methods untill Jan 2017
//    protected RadioButton waitForRadioButton(By locator, long timeout, String errorMessage) {
//        return getElement(RadioButton.class, locator, errorMessage);
//    }
    @Deprecated
    /**
     * use radioButtons(By locator)
     */
    protected List<RadioButton> waitForRadioButtonList(By locator, String errorMessage) {
        return getElementList(RadioButton.class, locator, errorMessage);
    }

    @Deprecated
    /**
     * use textField(By locator)
     */
    protected TextField waitForTextField(By locator, String errorMessage) {
        return getElement(TextField.class, locator, errorMessage);
    }

//TODO VE: do not delete this methods untill Jan 2017
//    protected TextField waitForTextField(By locator, long timeout, String errorMessage) {
//        return getElement(TextField.class, locator, errorMessage);
//    }

    @Deprecated
    /**
     * use textFields(By locator)
     */
    protected List<TextField> waitForTextFieldList(By locator, String errorMessage) {
        return getElementList(TextField.class, locator, errorMessage);
    }


    private <T extends Element> T getElementNew(Class<T> elementType, SearchContext searchContext, By by) {
        if (!isIE()) {
            return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
        } else {
            try {
                return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
            } catch (Exception e) {
                TestUtils.waitForSomeTime(5000, EXPLANATION_MESSAGE_FOR_WAIT);
                return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
            }
        }
    }

    private <T extends Element> T getElementNew(Class<T> elementType, SearchContext searchContext, By by, String errorMessage) {
        try {
            if (!isIE()) {
                return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
            } else {
                try {
                    return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
                } catch (Exception e) {
                    TestUtils.waitForSomeTime(5000, EXPLANATION_MESSAGE_FOR_WAIT);
                    return getWebElementWrapper(searchContext.findElement(by)).getElement(elementType, by);
                }
            }
        } catch (NoSuchElementException elementNotFound) {
            fail(errorMessage);
            return null;
        }
    }

    private <T extends Element> List<T> getElementListNew(Class<T> elementType, By by, String errorMessage) {
        List<T> result = new ArrayList<T>();
        List<WebElement> webElementList = waitForVisibilityOfAllElementsLocatedBy(by, errorMessage);
        for (WebElement webElement : webElementList) {
            result.add(getWebElementWrapper(webElement).getElement(elementType, by));
        }
        return result;
    }

    private <T extends Element> List<T> getElementListNew(Class<T> elementType, SearchContext searchContext, By by) {
        List<T> result = new ArrayList<T>();
        List<WebElement> webElementList = searchContext.findElements(by);
        for (WebElement webElement : webElementList) {
            result.add(getWebElementWrapper(webElement).getElement(elementType, by));
        }
        return result;
    }

    private <T extends Element> T getElementNew(Class<T> elementType, By by, String errorMessage) {
        if (!isIE()) {
            return getWebElementWrapper(waitForVisibilityOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
        } else {
            try {
                return getWebElementWrapper(waitForVisibilityOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
            } catch (Exception e) {
                TestUtils.waitForSomeTime(5000, EXPLANATION_MESSAGE_FOR_WAIT);
                return getWebElementWrapper(waitForVisibilityOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
            }
        }
    }

    @Deprecated
    /**
     * Use getElementNew()
     */
    protected <T extends Element> T getElement(Class<T> elementType, By by, String errorMessage) {
        if (!isIE()) {
            return getWebElementWrapper(waitForPresenceOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
        } else {
            try {
                return getWebElementWrapper(waitForVisibilityOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
            } catch (Exception e) {
                TestUtils.waitForSomeTime(5000, EXPLANATION_MESSAGE_FOR_WAIT);
                return getWebElementWrapper(waitForVisibilityOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
            }
        }
    }

    @Deprecated
    /**
     * Use getElementListNew()
     */
    protected <T extends Element> List<T> getElementList(Class<T> elementType, By by, String errorMessage) {
        List<T> result = new ArrayList<T>();
        List<WebElement> webElementList = waitForPresenceOfAllElementsLocatedBy(by, errorMessage);
        for (WebElement webElement : webElementList) {
            result.add(getWebElementWrapper(webElement).getElement(elementType, by));
        }
        return result;
    }

    //TODO VE: do not delete this methods untill Jan 2017
    private <T extends Element> T getElement(Class<T> elementType, By by, long timeout, String errorMessage) {
        return getWebElementWrapper(waitForPresenceOfElementLocatedBy(by, timeout, errorMessage)).getElement(elementType, by);
    }

    //TODO VE: do not delete this methods untill Jan 2017
    private <T extends Element> T getElement(Class<T> elementType, SearchContext searchContext, By by, String errorMessage) {
        return getWebElementWrapper(waitForPresenceOfElementLocatedBy(by, errorMessage)).getElement(elementType, by);
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
        String nameOfMethodThatCalledMe = stackTrace[3].getMethodName();

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

    @Deprecated
    /**
     * This method is valid but now it's used in many places incorrectly. All usages should be verified and fixed
     * then deprecated annotation will be removed
     */
    protected final List<WebElement> findElementsBy(final By locator) {
        return wrapList(elementFinder.findElementsBy(locator), locator);
    }

    protected final List<WebElement> findElementsByInFrames(final By locator) {
        return wrapList(elementFinder.findElementsByInFrames(locator), locator);
    }

    protected final List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator) {
        return wrapList(elementFinder.waitForVisibilityOfAllElementsLocatedByInFrames(locator), locator);
    }

    protected final List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator) {
        return wrapList(elementFinder.waitForPresenceOfAllElementsLocatedByInFrames(locator), locator);
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

    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeout) {
        return wrap(elementFinder.waitForPresenceOfElementLocatedBy(locator, timeout), locator);
    }

    @Deprecated
    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, final String errorMessage) {
        try {
            return waitForPresenceOfElementLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
        return null;
    }

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

    @Deprecated
    /**
     * Use elements/selects/textFields/checkBoxes - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, String errorMessage) {
        try {
            return waitForPresenceOfAllElementsLocatedBy(locator);
        } catch (TimeoutException e) {
            fail(errorMessage);
        }
        return null;
    }

    @Deprecated
    /**
     * Instead of this method create a 1 line private getter with meaningful name in Page
     * and call element(By locator) instead of this method. Error message will be automatically generated based on this.
     * This method will be removed by Jan 2017.
     */
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

    @Deprecated
    /**
     * Instead of this method create a 1 line private getter with meaningful name in Page
     * and call elements(By locator) instead of this method. Error message will be automatically generated based on this.
     * This method will be removed by Jan 2017.
     */
    protected final List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator, final String errorMessage) {
        try {
            return waitForVisibilityOfAllElementsLocatedBy(locator);
        } catch (WebDriverException e) {
            fail(errorMessage);
            return emptyList();
        }
    }

    private Boolean waitForInvisibilityOfElementLocatedBy(final By locator) {
        return elementFinder.waitForInvisibilityOfElementLocatedBy(locator);
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

    public boolean isIE() {
        return ExecutionUtils.isIE();
    }

    public boolean isSafari() {
        return ExecutionUtils.isSafari();
    }

    public boolean isFF() {
        return ExecutionUtils.isFF();
    }

    public boolean isChrome() {
        return ExecutionUtils.isChrome();
    }

    public boolean isWindows() {
        return ExecutionUtils.isWindows();
    }

    public boolean isAndroid() {
        return ExecutionUtils.isAndroid();
    }

    public boolean isMac() {
        return ExecutionUtils.isMac();
    }

    public boolean isIOs() {
        return ExecutionUtils.isIOs();
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

