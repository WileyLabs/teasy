package com.wiley.autotest.selenium.context;

import com.wiley.autotest.annotations.Report;
import com.wiley.autotest.event.EventFilter;
import com.wiley.autotest.event.Subscriber;
import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.event.postpone.failure.ScreenshotOnPostponeFailureSubscriber;
import com.wiley.autotest.selenium.ParamsProvider;
import com.wiley.autotest.selenium.ReportAnnotationsWrapperCreator;
import com.wiley.autotest.selenium.elements.CheckBox;
import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.extensions.ExtendedFieldDecorator;
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
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.wiley.autotest.selenium.elements.upgrade.OurWebElementFactory.wrapParent;
import static com.wiley.autotest.utils.ExecutionUtils.isIE;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 18:46
 */
public abstract class AbstractPageElement<P extends AbstractPageElement> extends AbstractElementFinder implements IPageElement, ErrorSender {

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

    private ScreenshotHelper screenshotHelper;

    protected WebDriver getDriver() {
        return driver;
    }

    protected HelperRegistry getRegistry() {
        return registry;
    }

    /**
     * Method is called by framework to complete navigation to the helper.
     * It is the best place to execute your wait-expressions to ensure that page has been completely loaded.
     */
    public void handleRedirect() {
    }

    public final void init(WebDriver driver, ScreenshotHelper screenshotHelper) {
        super.init(driver, timeout);
        this.driver = driver;
        this.screenshotHelper = screenshotHelper;
        elementFactory = new DefaultElementFactory();
        initFindByAnnotations(this);
        init();
    }

    protected Long getTimeout() {
        return timeout;
    }

    protected Settings getSettings() {
        return settings;
    }

    protected ScreenshotHelper getScreenshotHelper() {
        return screenshotHelper;
    }

    protected void init() {
    }

    protected final <E> E initFindByAnnotations(final E abstractPageElement) {
        PageFactory.initElements(new ExtendedFieldDecorator(driver, elementFactory, this), abstractPageElement);
        return abstractPageElement;
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
        final E helper = getRegistry().getPageHelper(target);
        helper.init(getDriver(), screenshotHelper);
        return ReportAnnotationsWrapperCreator.getReportingProxy(target, helper);
    }

    protected final <E extends IComponent> E getComponent(final Class<E> target) {
        final E helper = getRegistry().getBean(target);
        helper.init(getDriver(), screenshotHelper);
        return ReportAnnotationsWrapperCreator.getReportingProxy(target, helper);
    }

    protected final <E extends IPage> E navigateTo(final Class<E> target, final String url) {
        getDriver().get(url);
        getDriver().manage().getCookies().forEach(cookie -> reportWithStep(cookie.toString()));
        return redirectTo(target);
    }

    protected final <E extends IComponent> E redirectTo(final Class<E> target, final ComponentProvider componentProvider) {
        final E component = getHelper(target, componentProvider);
        component.handleRedirect();
        return component;
    }

    protected final <E extends IComponent> E getHelper(final Class<E> target, final ComponentProvider componentProvider) {
        final E helper = getRegistry().getComponentHelper(target);
        helper.init(getDriver(), screenshotHelper);
        helper.setComponentProvider(componentProvider);
        return helper;
    }

    protected final String getCurrentUrl() {
        return driver.getCurrentUrl();
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
        if (isIE()) {
            //for IE 9 it's enough to check value of loaded. It's impossible to get naturalWidth property of any image. (null is returned)
            return loaded;
        } else {
            final Long naturalWidth = (Long) executeScript("return arguments[0].naturalWidth", image);
            return !(loaded == null || !loaded || naturalWidth == null || naturalWidth.equals(Long.valueOf(0)));
        }
    }

    @Deprecated
    /**
     * use {@link OurWebElement#getParent()}
     */
    public WebElement getParentElement(final WebElement webElement) {
        return getParentElement(webElement, 1);
    }

    @Deprecated
    /**
     * use {@link OurWebElement#getParent()}
     */
    public WebElement getParentElement(final WebElement webElement, int level) {
        StringBuilder builder = new StringBuilder(".");
        for (int i = 0; i < level; i++) {
            builder
                    .append("/..");
        }
        return webElement.findElement(By.xpath(builder.toString()));
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

    public P report(final String message) {
        Reporter.log(message);
        return (P) this;
    }

    @Step("{0}")
    public P reportWithStep(final String message) {
        Reporter.log(message);
        return (P) this;
    }

    public ErrorSender getErrorSender() {
        return this;
    }

    @Deprecated
    //TODO: VE - should be deleted
    protected void clickRandomElementInList(final List<WebElement> list) {
        getRandomElementInList(list).click();
    }

    @Deprecated
    //TODO: VE - should be moved to utils
    public static <T> T getRandomElementInList(final List<T> list) {
        if (list != null && !list.isEmpty()) {
            return getRandomElementsInList(list, 1).get(0);
        }
        fail("List is null or empty! It's impossible to get random element!");
        return null;
    }

     @Deprecated
    //TODO: VE - should be moved to utils
    protected static <T> List<T> getRandomElementsInList(final List<T> sourceList, final int itemNumberToSelect) {
        final ArrayList<T> resultArray = new ArrayList<T>(sourceList.size());
        resultArray.addAll(sourceList);
        Collections.shuffle(resultArray);

        if (sourceList.size() <= itemNumberToSelect) {
            return resultArray;
        }

        return resultArray.subList(0, itemNumberToSelect);
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
        reportWithStep("set window width " + width);
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

    @Deprecated
    //TODO: VE - should be moved to utils
    public List<String> getTextFromWebElementList(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            scrollIntoView(eachElement);
            resultList.add(eachElement.getText().trim());
        }
        return resultList;
    }

    @Deprecated
    //TODO: VE - should be moved to utils
    public List<String> getTextWithoutQuestionsCountFromFilterOptionList(final List<CheckBox> checkBoxList) {
        final List<String> resultList = new ArrayList<String>();
        for (CheckBox checkBox : checkBoxList) {
            scrollIntoView(checkBox.getWrappedWebElement());
            resultList.add(checkBox.getText().split(DIGITS_WITHIN_PARENTHESIS)[0].trim());
        }
        return resultList;
    }

    @Deprecated
    //TODO: VE - should be moved to utils
    // Method is similar to getTextFromWebElementList except for scrolling into view when iterating elements
    public List<String> getTextFromWebElementListWithoutScroll(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            resultList.add(eachElement.getText());
        }
        return resultList;
    }

    @Deprecated
    //TODO: VE - should be moved to utils
    protected List<String> getTextFromWebElementListWithoutSpaces(final List<WebElement> webElementList) {
        final List<String> resultList = new ArrayList<String>();
        for (WebElement eachElement : webElementList) {
            resultList.add(eachElement.getText().trim());
        }
        return resultList;
    }

    @Deprecated
    //TODO: VE - should be moved to utils
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
    //TODO: VE - will be removed from framework
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
    //TODO: VE - will be removed from framework
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
    //TODO: VE - will be removed from framework
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

    @Deprecated
    //TODO: VE - will be removed from framework
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
    //TODO: VE - will be removed from framework
    protected boolean compareStringLists(List<String> list1, List<String> list2) {
        return compareStringListsAndGetDifferent(list1, list2).isEmpty();
    }

    @Deprecated
    //TODO: VE - will be removed from framework
    protected List<String> compareStringListsAndGetDifferent(List<String> list1, List<String> list2) {
        List<String> similar = new ArrayList<String>(list1);
        List<String> different = new ArrayList<String>();
        different.addAll(list1);
        different.addAll(list2);
        similar.retainAll(list2);
        different.removeAll(similar);
        return different;
    }

    protected String getCurrentWindowTitle() {
        return driver.getTitle();
    }

    public void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Deprecated
    //TODO: VE - will be removed from framework
    @Step
    @Report("Get Attributes list from Web element list by attribute name")
    protected List<String> getAttributesFromListWebElement(List<WebElement> elements, String attributeName) {
        List<String> result = new ArrayList<String>();
        for (WebElement element : elements) {
            result.add(element.getAttribute(attributeName));
        }
        return result;
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
            fail(e.getMessage());
        }
    }

    protected void assertNull(Object object, String errorMessage) {
        try {
            Assert.assertNull(object, errorMessage);
        } catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    protected void assertTrue(boolean condition, String errorMessage) {
        try {
            Assert.assertTrue(condition, errorMessage);
        } catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    protected void assertFalse(boolean condition, String errorMessage) {
        try {
            Assert.assertFalse(condition, errorMessage);
        } catch (AssertionError e) {
            fail(e.getMessage());
        }
    }

    public static void assertEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            fail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
        }
    }

    protected void assertNotEquals(Object actual, Object expected, String errorMessage) {
        try {
            Assert.assertNotEquals(actual, expected, errorMessage);
        } catch (AssertionError e) {
            fail(e.getMessage() + ": expected [" + expected + "] but found [" + actual + "]");
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
            fail(errorMessage);
        }
    }

    protected void assertElementIsDisplayed(SearchContext searchContext, By locator, String errorMessage) {
        try {
            elementFinder.waitForVisibilityOfAllElementsLocatedBy(searchContext, locator).get(0);
        } catch (WebDriverException e) {
            fail(errorMessage);
        }
    }

    protected void assertElementsAreAbsent(By locator, String errorMessage) {
        TestUtils.waitForSomeTime(TIMEOUT_TO_WAIT_FOR_ABSENCE_OF_ELEMENT, "Wait for elements are absent");
        elementFinder.findElementsBy(locator).forEach(webElement -> assertFalse(webElement.isDisplayed(), errorMessage));
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

    protected void assertElementIsDisplayed(SearchContext searchContext, By locator) {
        assertElementIsDisplayed(searchContext, locator, generateErrorMessage());
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
        postponedAssertTrue(Math.abs(actualDate.minuteOfHour().get() - expectedDate.minuteOfHour().get()) <= 1, "Incorrect field 'minute' in " + dateFieldName + ". Actual - " + actualDate.minuteOfHour().get() + " . Expected - " + expectedDate.minuteOfHour().get());
        postponedAssertEquals(actualDate.get(DateTimeFieldType.halfdayOfDay()), expectedDate.get(DateTimeFieldType.halfdayOfDay()), "Incorrect field 'halfdayOfDay' in start date" + dateFieldName);
    }
}
