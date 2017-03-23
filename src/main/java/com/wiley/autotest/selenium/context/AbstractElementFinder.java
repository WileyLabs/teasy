package com.wiley.autotest.selenium.context;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.actions.Actions;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    protected List<WebElement> elements(final By locator, SearchStrategy searchStrategy, final long timeoutInSeconds) {
        return getElementsOrWebDriverException(() -> {
            switch (searchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds);
                case IN_ALL_FRAMES:
                    return waitForVisibilityOfAllElementsLocatedByInFrames(locator, timeoutInSeconds);
            }
            throw new EnumConstantNotPresentException(searchStrategy.getDeclaringClass(), " enum constant is not recognized");
        });
    }

    protected List<WebElement> elements(final By locator, SearchStrategy searchStrategy) {
        return getElementsOrWebDriverException(() -> {
            switch (searchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForVisibilityOfAllElementsLocatedBy(locator);
                case IN_ALL_FRAMES:
                    return waitForVisibilityOfAllElementsLocatedByInFrames(locator);
            }
            throw new EnumConstantNotPresentException(searchStrategy.getDeclaringClass(), " enum constant is not recognized");
        });
    }

    protected WebElement element(final By locator) {
        return getElementOrWebDriverException(() -> waitForVisibilityOfAllElementsLocatedBy(locator).get(0));
    }

    protected WebElement element(final By locator, final long timeoutInSeconds) {
        return getElementOrWebDriverException(() -> waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds).get(0));
    }

    protected WebElement element(final SearchContext searchContext, final By locator) {
        return getElementOrWebDriverException(() -> waitForVisibilityOfAllElementsLocatedBy(searchContext, locator).get(0));
    }

    protected List<WebElement> elements(final By locator) {
        return elements(locator, SearchStrategy.FIRST_ELEMENTS);
    }

    protected List<WebElement> elements(final By locator, final long timeoutInSeconds) {
        return elements(locator, SearchStrategy.FIRST_ELEMENTS, timeoutInSeconds);
    }

    protected List<WebElement> elements(final SearchContext searchContext, final By locator) {
        return getElementsOrWebDriverException(() -> waitForVisibilityOfAllElementsLocatedBy(searchContext, locator));
    }

    protected final WebElement elementOrNull(final By locator) {
        return findElementByNoThrow(locator);
    }

    protected final WebElement elementOrNull(final By locator, long timeoutInSeconds) {
        return waitForElementByNoThrow(locator, timeoutInSeconds);
    }

    protected final WebElement elementOrNull(final SearchContext searchContext, final By locator) {
        return findElementByNoThrow(searchContext, locator);
    }

    /**
     * Use this method when you need element which is present in DOM but you are not sure if it's visible or not
     * call it from a private getter method on Page
     * use "inDOM" as a postfix for a method name
     */
    protected List<WebElement> domElements(By locator, SearchStrategy searchStrategy, long timeoutInSeconds) {
        return getDomElementsOrWebDriverException(() -> {
            switch (searchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForPresenceOfAllElementsLocatedBy(locator, timeoutInSeconds);
                case IN_ALL_FRAMES:
                    return waitForPresenceOfAllElementsLocatedByInFrames(locator, timeoutInSeconds);
            }
            throw new EnumConstantNotPresentException(searchStrategy.getDeclaringClass(), " enum constant is not recognized");
        });
    }

    protected List<WebElement> domElements(By locator, SearchStrategy searchStrategy) {
        return getDomElementsOrWebDriverException(() -> {
            switch (searchStrategy) {
                case FIRST_ELEMENTS:
                    return waitForPresenceOfAllElementsLocatedBy(locator);
                case IN_ALL_FRAMES:
                    return waitForPresenceOfAllElementsLocatedByInFrames(locator);
            }
            throw new EnumConstantNotPresentException(searchStrategy.getDeclaringClass(), " enum constant is not recognized");
        });
    }

    protected WebElement domElement(By locator) {
        return getDomElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(locator));
    }

    protected WebElement domElement(By locator, final long timeoutInSeconds) {
        return getDomElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(locator, timeoutInSeconds));
    }

    protected WebElement domElement(SearchContext searchContext, By locator) {
        return getDomElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(searchContext, locator));
    }

    protected List<WebElement> domElements(By locator) {
        return domElements(locator, SearchStrategy.FIRST_ELEMENTS);
    }

    protected List<WebElement> domElements(By locator, long timeoutInSeconds) {
        return domElements(locator, SearchStrategy.FIRST_ELEMENTS, timeoutInSeconds);
    }

    protected List<WebElement> domElements(SearchContext searchContext, By locator) {
        return getDomElementsOrWebDriverException(() -> waitForPresenceOfAllElementsLocatedBy(searchContext, locator));
    }

    /**
     * @param locator- locator for element you would like to find
     * @return list of webElements or empty list in case no such element were found by given locator
     */
    protected List<WebElement> domElementsOrEmpty(final By locator) {
        return elementFinder.findElementsBy(locator);
    }

    protected Button button(By locator) {
        return getElement(Button.class, locator);
    }

    protected Button button(By locator, long timeoutInSeconds) {
        return getElement(Button.class, locator, timeoutInSeconds);
    }

    protected Button button(SearchContext searchContext, By locator) {
        return getElement(Button.class, searchContext, locator);
    }

    protected List<Button> buttons(By locator) {
        return getElements(Button.class, locator);
    }

    protected List<Button> buttons(By locator, long timeoutInSeconds) {
        return getElements(Button.class, locator, timeoutInSeconds);
    }

    protected List<Button> buttons(SearchContext searchContext, By locator) {
        return getElements(Button.class, searchContext, locator);
    }

    protected Link link(By locator) {
        return getElement(Link.class, locator);
    }

    protected Link link(By locator, long timeoutInSeconds) {
        return getElement(Link.class, locator, timeoutInSeconds);
    }

    protected Link link(SearchContext searchContext, By locator) {
        return getElement(Link.class, searchContext, locator);
    }

    protected List<Link> links(By locator) {
        return getElements(Link.class, locator);
    }

    protected List<Link> links(By locator, long timeoutInSeconds) {
        return getElements(Link.class, locator, timeoutInSeconds);
    }

    protected List<Link> links(SearchContext searchContext, By locator) {
        return getElements(Link.class, searchContext, locator);
    }

    protected CheckBox checkBox(By locator) {
        return getElement(CheckBox.class, locator);
    }

    protected CheckBox checkBox(By locator, long timeoutInSeconds) {
        return getElement(CheckBox.class, locator, timeoutInSeconds);
    }

    protected CheckBox checkBox(SearchContext searchContext, By locator) {
        return getElement(CheckBox.class, searchContext, locator);
    }

    protected List<CheckBox> checkBoxes(By locator) {
        return getElements(CheckBox.class, locator);
    }

    protected List<CheckBox> checkBoxes(By locator, long timeoutInSeconds) {
        return getElements(CheckBox.class, locator, timeoutInSeconds);
    }

    protected List<CheckBox> checkBoxes(SearchContext searchContext, By locator) {
        return getElements(CheckBox.class, searchContext, locator);
    }

    protected RadioButton radioButton(By locator) {
        return getElement(RadioButton.class, locator);
    }

    protected RadioButton radioButton(By locator, long timeoutInSeconds) {
        return getElement(RadioButton.class, locator, timeoutInSeconds);
    }

    protected RadioButton radioButton(SearchContext searchContext, By locator) {
        return getElement(RadioButton.class, searchContext, locator);
    }

    protected List<RadioButton> radioButtons(By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected List<RadioButton> radioButtons(By locator, long timeoutInSeconds) {
        return getElements(RadioButton.class, locator, timeoutInSeconds);
    }

    protected List<RadioButton> radioButtons(SearchContext searchContext, By locator) {
        return getElements(RadioButton.class, searchContext, locator);
    }

    protected Select select(By locator) {
        return getElement(Select.class, locator);
    }

    protected Select select(By locator, long timeoutInSeconds) {
        return getElement(Select.class, locator, timeoutInSeconds);
    }

    protected Select select(SearchContext searchContext, By locator) {
        return getElement(Select.class, searchContext, locator);
    }

    protected List<Select> selects(By locator) {
        return getElements(Select.class, locator);
    }

    protected List<Select> selects(By locator, long timeoutInSeconds) {
        return getElements(Select.class, locator, timeoutInSeconds);
    }

    protected List<Select> selects(SearchContext searchContext, By locator) {
        return getElements(Select.class, searchContext, locator);
    }

    protected TextField textField(By locator) {
        return getElement(TextField.class, locator);
    }

    protected TextField textField(By locator, long timeoutInSeconds) {
        return getElement(TextField.class, locator, timeoutInSeconds);
    }

    protected TextField textField(SearchContext searchContext, By locator) {
        return getElement(TextField.class, searchContext, locator);
    }

    protected List<TextField> textFields(By locator) {
        return getElements(TextField.class, locator);
    }

    protected List<TextField> textFields(By locator, long timeoutInSeconds) {
        return getElements(TextField.class, locator, timeoutInSeconds);
    }

    protected List<TextField> textFields(SearchContext searchContext, By locator) {
        return getElements(TextField.class, searchContext, locator);
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

    protected final void customCondition(final ExpectedCondition<Boolean> condition, long timeOutInSeconds) {
        elementFinder.waitForCondition(condition, timeOutInSeconds);
    }

    private class UnsupportedConditionException extends RuntimeException {
        UnsupportedConditionException(String message) {
            super(message);
        }
    }

    protected <T extends Element> T getElement(Class<T> elementType, By by) {
        return getElementOrException(() -> getWebElementWrapper(element(by)).getElement(elementType, by));
    }

    protected <T extends Element> T getElement(Class<T> elementType, By by, long timeoutInSeconds) {
        return getElementOrException(() -> getWebElementWrapper(element(by, timeoutInSeconds)).getElement(elementType, by));
    }

    protected <T extends Element> T getElement(Class<T> elementType, SearchContext searchContext, By by) {
        return getElementOrException(() -> getWebElementWrapper(element(searchContext, by)).getElement(elementType, by));
    }

    protected <T extends Element> List<T> getElements(Class<T> elementType, By by) {
        return elements(by).stream().map(element -> getWebElementWrapper(element).getElement(elementType, by)).collect(Collectors.toList());
    }

    protected <T extends Element> List<T> getElements(Class<T> elementType, By by, long timeoutInSeconds) {
        return elements(by, timeoutInSeconds).stream().map(element -> getWebElementWrapper(element).getElement(elementType, by)).collect(Collectors.toList());
    }

    protected <T extends Element> List<T> getElements(Class<T> elementType, SearchContext searchContext, By by) {
        return elements(searchContext, by).stream().map(element -> getWebElementWrapper(element).getElement(elementType, by)).collect(Collectors.toList());
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

    public void init(final WebDriver driver, Long timeOutInSeconds) {
        elementFinder = new WebDriverAwareElementFinder(driver, new WebDriverWait(driver, timeOutInSeconds, SLEEP_IN_MILLISECONDS));
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
            return elementFinder.findElementBy(locator);
        } catch (WebDriverException e) {
            //TODO hotfix for safari as it seems that this method does not work correct without timeout
            if (isSafari()) {
                TestUtils.waitForSafari();
                try {
                    return elementFinder.findElementBy(locator);
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
            return elementFinder.findElementBy(searchContext, locator);
        } catch (Exception e) {
            //TODO hotfix for safari as it seems that this method does not work correct without timeout
            if (isSafari()) {
                TestUtils.waitForSafari();
                try {
                    return elementFinder.findElementBy(searchContext, locator);
                } catch (Exception ignoreSafari) {
                    return null;
                }
            }
            return null;
        }
    }

    /**
     * use {@link #elementOrNull(By, long)}
     * this method will be removed
     */
    @Deprecated
    protected final WebElement waitForElementByNoThrow(final By locator, long timeOutInSeconds) {
        try {
            return waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds);
        } catch (WebDriverException e) {
            return null;
        }
    }

    /**
     * This method is valid but now it's used in many places incorrectly. All usages should be verified and fixed
     * then deprecated annotation will be removed
     */
    @Deprecated
    protected final List<WebElement> findElementsBy(final By locator) {
        return elementFinder.findElementsBy(locator);
    }

    protected final void waitForWindowToBeAppearedAndSwitchToIt(final String title) {
        windowOrWebDriverException(title, () -> {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedAndSwitchToIt(title);
        });
    }

    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeoutInSeconds) {
        waitWindowIsAppearInChrome();
        elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url, timeoutInSeconds);
    }

    protected final void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url) {
        windowOrWebDriverException(url, () -> {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(url);

        });
    }

    protected final void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(final String partialTitle) {
        windowOrWebDriverException(partialTitle, () -> {
            waitWindowIsAppearInChrome();
            elementFinder.waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(partialTitle);
        });
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
        return elementFinder.waitForPresenceOfElementLocatedBy(locator);
    }

    private WebElement waitForPresenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return elementFinder.waitForPresenceOfElementLocatedBy(searchContext, locator);
    }

    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds);
    }

    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, final String errorMessage) {
        return getElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(locator), errorMessage);
    }

    /**
     * Use element/select/textField/checkBox - when you need VISIBLE element
     * Use domElement - when you need element which is PRESENT IN DOM
     */
    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeOutInSeconds, final String errorMessage) {
        return getElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(locator, timeOutInSeconds), errorMessage);
    }

    @Deprecated
    protected WebElement waitForPresenceOfElementLocatedByLinkText(final String linkText) {
        return waitForPresenceOfElementLocatedBy(AbstractPage.getLinkByXpath(linkText));
    }

    @Deprecated
    protected final WebElement waitForPresenceOfElementLocatedByLinkText(final String linkText, final String errorMessage) {
        return getElementOrWebDriverException(() -> waitForPresenceOfElementLocatedBy(AbstractPage.getLinkByXpath(linkText)), errorMessage);
    }

    @Deprecated
    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return elementFinder.waitForPresenceOfAllElementsLocatedBy(locator);
    }

    @Deprecated
    protected final List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeoutInSeconds) {
        try {
            return elementFinder.waitForPresenceOfAllElementsLocatedBy(locator, timeoutInSeconds);
        } catch (TimeoutException ignored) {
            return new ArrayList<>();
        }
    }

    protected List<WebElement> waitForPresenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return elementFinder.waitForPresenceOfAllElementsLocatedBy(searchContext, locator);
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
        return getElementOrWebDriverException(() -> waitForVisibilityOfElementLocatedBy(locator), errorMessage);
    }

    @Deprecated
    protected final WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForVisibilityOfElementLocatedBy(locator, timeOutInSeconds);
    }

    private WebElement waitForVisibilityOfElementLocatedBy(final By locator) {
        return elementFinder.waitForVisibilityOfElementLocatedBy(locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator) {
        return elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator, long timeoutInSeconds) {
        return elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator, timeoutInSeconds);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator) {
        return elementFinder.waitForVisibilityOfAllElementsLocatedByInFrames(locator);
    }

    private List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForVisibilityOfAllElementsLocatedByInFrames(locator, timeOutInSeconds);
    }

    private List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator) {
        return elementFinder.waitForPresenceOfAllElementsLocatedByInFrames(locator);
    }

    private List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator, long timeOutInSeconds) {
        return elementFinder.waitForPresenceOfAllElementsLocatedByInFrames(locator, timeOutInSeconds);
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

    private Boolean waitForInvisibilityOfElementLocatedBy(final By locator) {
        return elementFinder.waitForInvisibilityOfElementLocatedBy(locator);
    }

    private void waitForAbsenceOfElementLocatedBy(final By locator) {
        elementFinder.waitForAbsenceOfElementLocatedBy(locator);
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
        return elementFinder.waitForElementToBeClickable(locator);
    }

    protected final WebElement waitForElementToBeClickable(final By locator, final String errorMessage) {
        return getElementOrWebDriverException(() -> waitForElementToBeClickable(locator), errorMessage);
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

    protected final void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeOutInSeconds) {
        elementFinder.waitForPresenceOfElementCount(locator, expectedNumberOfElements, timeOutInSeconds);
    }

    protected final void waitForPageToLoad() {
        elementFinder.waitForPageToLoad();
        if (isSafari()) {
            TestUtils.waitForSafari();
        }
    }

    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue);
    }

    protected final void waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeOutInSeconds) {
        elementFinder.waitForElementContainsAttributeValue(element, attributeName, attributeValue, timeOutInSeconds);
    }

    protected final void waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        elementFinder.waitForElementNotContainsAttributeValue(element, attributeName, attributeValue);
    }

    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementContainsAttribute(element, attributeName);
    }

    protected final void waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        elementFinder.waitForElementContainsAttribute(element, attributeName, timeOutInSeconds);
    }

    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName);
    }

    protected final void waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        elementFinder.waitForElementNotContainsAttribute(element, attributeName, timeOutInSeconds);
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
    protected final Boolean waitForStalenessOf(final WebElement webElement, long timeOutInSeconds) {
        try {
            return elementFinder.waitForStalenessOf(webElement, timeOutInSeconds);
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

    protected final void setTimeout(final long timeOutInSeconds) {
        elementFinder.setTimeout(timeOutInSeconds);
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

    private WebElement getElementOrWebDriverException(Supplier<WebElement> webElementSupplier) {
        return (WebElement) getSupplierObject((Supplier) webElementSupplier, (String) "****WebDriverException in element()****");
    }

    private WebElement getDomElementOrWebDriverException(Supplier<WebElement> webElementSupplier) {
        return (WebElement) getSupplierObject((Supplier) webElementSupplier, (String) "****WebDriverException in domElement()****");
    }

    @SuppressWarnings("unchecked")
    private List<WebElement> getElementsOrWebDriverException(Supplier<List<WebElement>> webElementsSupplier) {
        return (List<WebElement>) getSupplierObject((Supplier) webElementsSupplier, (String) "****WebDriverException in elements()****");
    }

    @SuppressWarnings("unchecked")
    private List<WebElement> getDomElementsOrWebDriverException(Supplier<List<WebElement>> webElementsSupplier) {
        return (List<WebElement>) getSupplierObject((Supplier) webElementsSupplier, (String) "****WebDriverException in domElements()****");
    }

    private Object getSupplierObject(Supplier webElementSupplier, String loggerMessage) {
        return getSupplierObject(webElementSupplier, loggerMessage, generateErrorMessage());
    }

    private WebElement getElementOrWebDriverException(Supplier<WebElement> webElementSupplier, String errorMessage) {
        return (WebElement) getSupplierObject(webElementSupplier, "", errorMessage);
    }

    private Object getSupplierObject(Supplier webElementSupplier, String loggerMessage, String errorMessage) {
        try {
            return webElementSupplier.get();
        } catch (TimeoutException time) {
            fail(generateErrorMessage());
        } catch (WebDriverException wde) {
            LOGGER.error(loggerMessage, wde);
            fail(errorMessage + "\nException: " + wde.getMessage() + "\nStackTrace: " + Arrays.toString(wde.getStackTrace()));
        }
        return null;
    }

    private void windowOrWebDriverException(String title, Actions action) {
        try {
            action.execute();
        } catch (WebDriverException e) {
            fail("Unable to locate window with '" + title + "'");
        }
    }

    private <T extends Element> T getElementOrException(Supplier<T> elementSupplier) {
        try {
            return elementSupplier.get();
        } catch (Exception e) {
            return null;
        }
    }
}

