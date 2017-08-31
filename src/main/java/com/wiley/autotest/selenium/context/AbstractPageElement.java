package com.wiley.autotest.selenium.context;

import com.wiley.autotest.event.EventFilter;
import com.wiley.autotest.event.Subscriber;
import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.event.postpone.failure.ScreenshotOnPostponeFailureSubscriber;
import com.wiley.autotest.selenium.AllureStep2TestNG;
import com.wiley.autotest.selenium.ParamsProvider;
import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.elements.CheckBox;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.Window;
import com.wiley.autotest.selenium.extensions.internal.DefaultElementFactory;
import com.wiley.autotest.spring.Settings;
import com.wiley.autotest.utils.DriverUtils;
import com.wiley.autotest.utils.TestUtils;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 18:46
 */
public abstract class AbstractPageElement<P extends AbstractPageElement> extends TeasyElementProvider implements IPageElement, ErrorSender {

    public static final int TIMEOUT_TO_WAIT_FOR_WINDOW = 2;
    public static final int TIMEOUT_TO_WAIT_FOR_ABSENCE_OF_ELEMENT = 2000;
    //VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    private static final long SLEEP_IN_MILLISECONDS = 1000;
    private static final String DIGITS_WITHIN_PARENTHESIS = "\\(\\d+";

    private WebDriver driver;

    @Autowired
    private HelperRegistry registry;

    @Autowired
    private ParamsProvider parameterProvider;

    @Autowired
    private ParamsProvider parameterProviderForGroup;

    @Autowired
    private PostponedFailureEvent postponeFailureEvent;

    @Autowired
    private Long timeout;

    @Autowired
    private Settings settings;

    private DefaultElementFactory elementFactory;

    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Method is called by framework to complete navigation to the helper.
     * It is the best place to execute your wait-expressions to ensure that page has been completely loaded.
     */
    public void handleRedirect() {
    }

    public final void init(WebDriver driver) {
        super.init(driver, timeout);
        this.driver = driver;
        elementFactory = new DefaultElementFactory();
        init();
    }

    protected Long getTimeout() {
        return timeout;
    }

    protected Settings getSettings() {
        return settings;
    }

    protected void init() {
    }

    protected <E extends IPage> E redirectTo(final Class<E> target) {
        final E page = getHelper(target);
        page.handleRedirect();
        waitForPageToLoad();
        return page;
    }

    protected final <E extends IPage> E redirectToWithoutWaitToLoad(final Class<E> target) {
        final E page = getHelper(target);
        page.handleRedirect();
        return page;
    }

    protected final <E extends IPage> E getHelper(final Class<E> target) {
        final E helper = registry.getPageHelper(target);
        helper.init(getDriver());
        return AllureStep2TestNG.addInterceptor(target, helper);
    }

    protected final <E extends IPage> E navigateTo(final Class<E> target, final String url) {
        getDriver().get(url);
        getDriver().manage().getCookies().forEach(cookie -> reportWithStep(cookie.toString()));
        return redirectTo(target);
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window.
     *
     * @param script - The JavaScript to execute
     * @param args   - The arguments to the script. May be empty
     * @return Boolean, Long, String, List or WebElement. Or null.
     */
    protected final Object executeScript(final String script, final Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
    }

    /**
     * Check if image loaded
     *
     * @param image - image element
     * @return true if image loaded correctly
     */
    protected final boolean isImageLoaded(final WebElement image) {
        final Boolean loaded = (Boolean) executeScript("return arguments[0].complete", image);
        final Long naturalWidth = (Long) executeScript("return arguments[0].naturalWidth", image);
        return !(loaded == null || !loaded || naturalWidth == null || naturalWidth.equals(Long.valueOf(0)));
    }

    protected final Object getParameterForGroup(final String key) {
        return parameterProviderForGroup.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    protected void setParameterForGroup(final String key, final Object value) {
        parameterProviderForGroup.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }

    protected void setParameter(final String key, final Object value) {
        parameterProvider.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }

    public Object getParameter(final String key) {
        return parameterProvider.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    public void setPostponedTestFail(final String message) {
        postponeFailureEvent.fire(message);
    }

    public void setPostponedTestFailWithoutScreenshot(final String message) {
        postponeFailureEvent.fire(message, new EventFilter() {
            @Override
            public <MsgType> void fire(Subscriber<MsgType> each, MsgType message) {
                if (!(each instanceof ScreenshotOnPostponeFailureSubscriber)) {
                    each.notify(message);
                }
            }
        });
    }

    public ErrorSender getErrorSender() {
        return this;
    }

    protected void hoverOverElement(WebElement webElement) {
        maximizeWindow();
        Actions builder = new Actions(getDriver());
        Actions hoverOverWebElement = builder.moveToElement(webElement);
        hoverOverWebElement.perform();
    }

    public void maximizeWindow() {
        try {
            getDriver().manage().window().maximize();
            waitForPageToLoad();
        } catch (WebDriverException ignored) {
            //If a frame is selected and then browser window is maximized, exception is thrown
            //Selenium bug: Issue 3758: Exception upon maximizing browser window with frame selected
        }
    }

    public P setWindowWidth(int width) {
        new Report("set window width " + width).allure();
        return setWindowSize(width, -1);
    }

    public P setWindowHeight(int height) {
        return setWindowSize(-1, height);
    }

    public P setWindowSize(int width, int height) {
        DriverUtils.setWindowSize(getDriver(), width, height);
        waitForPageToLoad();
        return (P) this;
    }

    protected Alert getAlert() {
        try {
            return getDriver().switchTo().alert();
        } catch (NoAlertPresentException e) {
            return null;
        }
    }

    protected Alert getAlert(int timeoutForWait) {
        try {
            Alert alert = waitForAlertPresence(timeoutForWait);

            //For some reason in Chrome v.30 alerts are not properly interacted with the first try
            //We are trying to interact with it once more to avoid failures
            if (getAlert() != null) {
                return alert;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    @Deprecated// will be removed
    protected String getCurrentWindowTitle() {
        return driver.getTitle();
    }

    public void scrollIntoView(TeasyElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }


// ============ ASSERTIONS ===========

    protected <T> void assertThat(T actual, Matcher<? super T> matcher, String errorMessage) {
        try {
            MatcherAssert.assertThat(actual, matcher);
        } catch (AssertionError e) {
            fail(errorMessage, e);
        }
    }

    protected <T> void postponedAssertThat(T actual, Matcher<? super T> matcher, String errorMessage) {
        try {
            MatcherAssert.assertThat(actual, matcher);
        } catch (AssertionError e) {
            setPostponedTestFail(errorMessage);
        }
    }

    protected void assertNotNull(Object object, String errorMessage) {
        try {
            Assert.assertNotNull(object, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage());
        }
    }

    protected void assertNull(Object object, String errorMessage) {
        try {
            Assert.assertNull(object, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage());
        }
    }

    protected void assertTrue(boolean condition, String errorMessage) {
        try {
            Assert.assertTrue(condition, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage());
        }
    }

    protected void assertFalse(boolean condition, String errorMessage) {
        try {
            Assert.assertFalse(condition, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage());
        }
    }

    public static void assertEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
        }
    }

    protected void assertNotEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertNotEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            TestUtils.fail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
        }
    }

    protected void postponedAssertNotNull(Object object, String errorMessage) {
        try {
            Assert.assertNotNull(object, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage());
        }
    }

    protected void postponedAssertNull(Object object, String errorMessage) {
        try {
            Assert.assertNull(object, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage());
        }
    }

    protected void postponedAssertTrue(boolean condition, String errorMessage) {
        try {
            Assert.assertTrue(condition, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage());
        }
    }

    protected void postponedAssertFalse(boolean condition, String errorMessage) {
        try {
            Assert.assertFalse(condition, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage());
        }
    }

    protected void postponedAssertEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
        }
    }

    protected void postponedAssertNotEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertNotEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            setPostponedTestFail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
        }
    }

    protected void assertElementIsAbsent(By locator, String errorMessage) {
        TestUtils.waitForSomeTime(TIMEOUT_TO_WAIT_FOR_ABSENCE_OF_ELEMENT, "Wait for absence element");
        WebElement element = findElementByNoThrow(locator);
        assertTrue(element == null || !element.isDisplayed(), errorMessage);
    }

    protected void assertElementIsDisplayed(By locator, String errorMessage) {
        WebElement element = waitForPresenceOfElementLocatedBy(locator, errorMessage);
        assertTrue(element.isDisplayed(), errorMessage);
    }

    protected void assertElementsAreDisplayed(By locator, String errorMessage) {
        try {
            elementFinder.waitForVisibilityOfAllElementsLocatedBy(locator);
        } catch (WebDriverException e) {
            TestUtils.fail(errorMessage);
        }
    }

    protected void assertElementsAreAbsent(By locator, String errorMessage) {
        TestUtils.waitForSomeTime(TIMEOUT_TO_WAIT_FOR_ABSENCE_OF_ELEMENT, "Wait for elements are absent");
        elementFinder.findElementsBy(locator)
                .forEach(webElement -> assertFalse(webElement.isDisplayed(), errorMessage));
    }

    protected void assertContainsAll(List<? extends Object> actual, List<? extends Object> expected, String errorMessage) {
        expected.stream()
                .filter(object -> !actual.contains(object))
                .forEach(object -> fail(errorMessage + " Element '" + object + "' not found in given list"));
    }

    protected void assertNotNull(Object object) {
        assertNotNull(object, generateErrorMessage());
    }

    protected void assertNull(Object object) {
        assertNull(object, generateErrorMessage());
    }

    protected void assertTrue(boolean condition) {
        assertTrue(condition, generateErrorMessage());
    }

    protected void assertFalse(boolean condition) {
        assertFalse(condition, generateErrorMessage());
    }

    protected void assertEquals(Object actual, Object expected) {
        assertEquals(actual, expected, generateErrorMessage());
    }

    protected void assertNotEquals(Object actual, Object expected) {
        assertNotEquals(actual, expected, generateErrorMessage());
    }

    protected void postponedAssertNotNull(Object object) {
        postponedAssertNotNull(object, generateErrorMessage());
    }

    protected void postponedAssertNull(Object object) {
        postponedAssertNull(object, generateErrorMessage());
    }

    protected void postponedAssertTrue(boolean condition) {
        postponedAssertTrue(condition, generateErrorMessage());
    }

    protected void postponedAssertFalse(boolean condition) {
        postponedAssertFalse(condition, generateErrorMessage());
    }

    protected void postponedAssertEquals(Object actual, Object expected) {
        postponedAssertEquals(actual, expected, generateErrorMessage());
    }

    protected void postponedAssertNotEquals(Object actual, Object expected) {
        postponedAssertNotEquals(actual, expected, generateErrorMessage());
    }

    protected void assertElementIsAbsent(By locator) {
        assertElementIsAbsent(locator, generateErrorMessage());
    }

    protected void assertElementIsDisplayed(By locator) {
        assertElementIsDisplayed(locator, generateErrorMessage());
    }

    protected void assertElementsAreDisplayed(By locator) {
        assertElementsAreDisplayed(locator, generateErrorMessage());
    }

    protected void assertElementsAreAbsent(By locator) {
        assertElementsAreAbsent(locator, generateErrorMessage());
    }

    protected void postponedAssertDateEquals(DateTime actualDate, DateTime expectedDate, String dateFieldName) {
        postponedAssertEquals(actualDate.year().get(), expectedDate.year().get(), "Incorrect field 'year' in " + dateFieldName);
        postponedAssertEquals(actualDate.monthOfYear().get(), expectedDate.monthOfYear().get(), "Incorrect field 'month' in " + dateFieldName);
        postponedAssertEquals(actualDate.dayOfMonth().get(), expectedDate.dayOfMonth().get(), "Incorrect field 'day' in " + dateFieldName);
        postponedAssertEquals(actualDate.hourOfDay().get(), expectedDate.hourOfDay().get(), "Incorrect field 'hour' in " + dateFieldName);
        postponedAssertTrue(Math.abs(actualDate.minuteOfHour().get() - expectedDate.minuteOfHour().get()) <= 1,
                "Incorrect field 'minute' in " + dateFieldName + ". Actual - " + actualDate.minuteOfHour().get()
                        + " . Expected - " + expectedDate.minuteOfHour().get());
        postponedAssertEquals(actualDate.get(DateTimeFieldType.halfdayOfDay()), expectedDate.get(DateTimeFieldType.halfdayOfDay()),
                "Incorrect field 'halfdayOfDay' in start date" + dateFieldName);
    }

    // OLD code that is going to be removed by September 2017.
    // Currently kept to give users some time to switch to new implementation


    @Deprecated
    /**
     * use {@link Window#getUrl()}
     */
    protected final String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    @Deprecated
    //Use element().should().beVisible()
    protected void assertElementIsDisplayed(SearchContext searchContext, By locator) {
        assertElementIsDisplayed(searchContext, locator, generateErrorMessage());
    }

    @Deprecated
    //use element().should().beDisplayed();
    protected void assertElementIsDisplayed(SearchContext searchContext, By locator, String errorMessage) {
        throw new RuntimeException("REPLACE WITH NEW APPROACH IMMEDIATELY! METHOD IMPLEMENTATION WAS DELETED! CONTACT Vladimir Efimov vefimov@wiley.com");
//
//
//        try {
//            elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator).get(0);
//        } catch (WebDriverException e) {
//            TestUtils.fail(errorMessage);
//        }
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected void checkConfirmText(String confirmText) {
        postponedAssertEquals(waitForAlertPresence().getText().trim(), confirmText.trim(), "Alert text is incorrect.");
    }

    @Deprecated
    //TODO: VE - will be removed from framework
    protected Alert waitForAlertPresence() {
        return (new WebDriverWait(driver, timeout, SLEEP_IN_MILLISECONDS)).until(ExpectedConditions.alertIsPresent());
    }

    @Deprecated
    //TODO: VE - will be removed from framework
    protected Alert waitForAlertPresence(int timeoutForAlert) {
        return (new WebDriverWait(driver, timeoutForAlert, SLEEP_IN_MILLISECONDS)).until(ExpectedConditions.alertIsPresent());
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected boolean compareStringLists(List<String> list1, List<String> list2) {
        return compareStringListsAndGetDifferent(list1, list2).isEmpty();
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected List<String> compareStringListsAndGetDifferent(List<String> list1, List<String> list2) {
        List<String> similar = new ArrayList<String>(list1);
        List<String> different = new ArrayList<String>();
        different.addAll(list1);
        different.addAll(list2);
        similar.retainAll(list2);
        different.removeAll(similar);
        return different;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected List<String> getAttributesFromListWebElement(List<WebElement> elements, String attributeName) {
        List<String> result = new ArrayList<String>();
        for (WebElement element : elements) {
            result.add(element.getAttribute(attributeName));
        }
        return result;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    public List<String> getTextFromWebElementList(final List<TeasyElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (TeasyElement eachElement : webElementList) {
            scrollIntoView(eachElement);
            resultList.add(eachElement.getText().trim());
        }
        return resultList;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    public List<String> getTextWithoutQuestionsCountFromFilterOptionList(final List<CheckBox> checkBoxList) {
        final List<String> resultList = new ArrayList<String>();
        for (CheckBox checkBox : checkBoxList) {
            scrollIntoView(checkBox.getWrappedWebElement());
            resultList.add(checkBox.getText().split(DIGITS_WITHIN_PARENTHESIS)[0].trim());
        }
        return resultList;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    // Method is similar to getTextFromWebElementList except for scrolling into view when iterating elements
    public List<String> getTextFromWebElementListWithoutScroll(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            resultList.add(eachElement.getText());
        }
        return resultList;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected List<String> getTextFromWebElementListWithoutSpaces(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            resultList.add(eachElement.getText().trim());
        }
        return resultList;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected List<String> getTextFromWebElementListWithoutEmptyString(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            if (!eachElement.getText().trim().isEmpty()) {
                resultList.add(eachElement.getText().trim());
            }
        }
        return resultList;
    }

    @Deprecated
    //TODO: VE - should be moved to utils
    protected boolean isWindowDisplayedByPartialUrl(String partialUrl) {
        try {
            waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(partialUrl, TIMEOUT_TO_WAIT_FOR_WINDOW);
            return true;
        } catch (WebDriverException e) {
            return false;
        }
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected void clickOkButtonInConfirm() {
        Alert alert = waitForAlertPresence();
        alert.accept();
        //For some reason in Chrome v.30 alerts are not properly interacted with the first try
        //We are trying to interact with it once more to avoid failures
        if (getAlert() != null) {
            alert.accept();
        }
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected void clickOkButtonInConfirm(int timeoutForConfirm) {
        try {
            Alert alert = waitForAlertPresence(timeoutForConfirm);
            alert.accept();
            //For some reason in Chrome v.30 alerts are not properly interacted with the first try
            //We are trying to interact with it once more to avoid failures
            if (getAlert() != null) {
                alert.accept();
            }
        } catch (Exception ignored) {
        }
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected void clickCancelButtonInConfirm() {
        try {
            Alert alert = waitForAlertPresence();
            alert.dismiss();

            //For some reason in Chrome v.30 alerts are not properly interacted with the first try
            //We are trying to interact with it once more to avoid failures
            if (getAlert() != null) {
                alert.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected void clickRandomElementInList(final List<WebElement> list) {
        getRandomElementInList(list).click();
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    public static <T> T getRandomElementInList(final List<T> list) {
        if (list != null && !list.isEmpty()) {
            return getRandomElementsInList(list, 1).get(0);
        }
        fail("List is null or empty! It's impossible to get random element!");
        return null;
    }

    @Deprecated
    //Will be removed from framework. Do not use this method. Create something similar in your project instead.
    //TODO VE - delete this by Sept 2017
    protected static <T> List<T> getRandomElementsInList(final List<T> sourceList, final int itemNumberToSelect) {
        final ArrayList<T> resultArray = new ArrayList<T>(sourceList.size());
        resultArray.addAll(sourceList);
        Collections.shuffle(resultArray);

        if (sourceList.size() <= itemNumberToSelect) {
            return resultArray;
        }

        return resultArray.subList(0, itemNumberToSelect);
    }

    @Deprecated
    /**
     * use {@link TeasyElement#getParent()}
     */
    public WebElement getParentElement(final WebElement webElement) {
        return getParentElement(webElement, 1);
    }

    @Deprecated
    /**
     * use {@link TeasyElement#getParent()}
     */
    public WebElement getParentElement(final WebElement webElement, int level) {
        StringBuilder builder = new StringBuilder(".");
        for (int i = 0; i < level; i++) {
            builder
                    .append("/..");
        }
        return webElement.findElement(By.xpath(builder.toString()));
    }

    @Deprecated
    /**
     * Use {@link Report#testNG()}
     */
    public P report(final String message) {
        Reporter.log(message);
        return (P) this;
    }

    @Deprecated
    @Step("{0}")
    /**
     * Use {@link Report#allure()}
     */
    public P reportWithStep(final String message) {
        Reporter.log(message);
        return (P) this;
    }
}
