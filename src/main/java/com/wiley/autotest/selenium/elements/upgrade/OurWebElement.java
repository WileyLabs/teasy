package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.AbstractElementFinder;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import static com.wiley.autotest.utils.ExecutionUtils.*;
import static com.wiley.autotest.utils.IETestUtils.*;


/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:41
 */
public class OurWebElement implements IOurWebElement, Locatable {

    public WebElement wrappedElement;
    public Locator locator;
    public ElementFinder elementFinder;
    public static final Logger LOGGER = LoggerFactory.getLogger(OurWebElement.class);
    //The duration in milliseconds to sleep between polls. (default value in selenium is 500)
    public static final long SLEEP_IN_MILLISECONDS = 1000;
    public static final long WAIT_TIME_OUT_IN_SECONDS = 5;
    public int repeatLocateElementCounter;
    public static final int MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT = 20;

    public OurWebElement(OurWebElementData ourWebElementData) {
        WebElement element = ourWebElementData.getElement();
        SearchContext searchContext = ourWebElementData.getSearchContext();
        By by = ourWebElementData.getBy();
        Integer index = ourWebElementData.getIndex();
        Locator locator = ourWebElementData.getLocator();

        if (searchContext != null && by != null && index != null) {
            this.locator = new FindElementsLocator(searchContext, by, index);
        } else if (by != null && index != null) {
            this.locator = new FindElementsLocator(getDriver(), by, index);
        } else if (searchContext != null && by != null) {
            this.locator = new FindElementLocator(searchContext, by);
        } else if (by != null) {
            this.locator = new FindElementLocator(getDriver(), by);
        } else if (locator != null) {
            this.locator = locator;
        } else {
            IOurWebElement ourWebElement = (IOurWebElement) element;
            element = getParentElement(ourWebElement.getWrappedWebElement());
            this.locator = new FindParentElementLocator(getDriver(), ourWebElement.getLocator().getByLocator());
        }

        init(element);
    }

    public void init(WebElement element) {
        this.wrappedElement = element instanceof IOurWebElement ? ((IOurWebElement) element).getWrappedWebElement() : element;
        this.repeatLocateElementCounter = 0;
        if (elementFinder == null) {
            elementFinder = new WebDriverAwareElementFinder(getDriver(), new WebDriverWait(getDriver(), WAIT_TIME_OUT_IN_SECONDS, SLEEP_IN_MILLISECONDS));
        }
    }

    @Override
    public void click() {
        try {
            try {
                clickByBrowser();
            } catch (StaleElementReferenceException e) {
                clickForStaleElement();
            } catch (UnhandledAlertException ignored) {
                LOGGER.error("*****ERROR*****UnhandledAlertException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator.getByLocator());
            } catch (UnreachableBrowserException ignored) {
                //doing this because for FireFox for some reason browser becomes unresponsive after click
                //but actually it is alive so it worth to try to continue test
                //it will fail on the next method after click if some real error happened
                LOGGER.error("*****ERROR*****UnreachableBrowserException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator.getByLocator());
            } catch (ElementNotVisibleException needToScroll) {
                clickForNeedToScroll();
            } catch (WebDriverException ignoredOrNeedToScroll) {
                clickForIgnoredScroll(ignoredOrNeedToScroll);
            }
        } catch (Exception e) {
            LOGGER.error("*****FATAL ERROR*****Exception***** DURING CLICK LOGIC. SHOULD BE REFACTORED!!!! -----Locator=" + locator.getByLocator(), e);
        }
    }

    public void clickForStaleElement() {
        againLocate();
        click();
    }

    public void clickByBrowser() {
        if (isIE()) {
            clickInIE();
        } else if (isSafari()) {
            clickInSafari();
        } else {
            wrappedElement.click();
        }
    }

    public void clickForNeedToScroll() {
        LOGGER.error("*****ERROR*****ElementNotVisibleException***** during click! Scrolling to element and trying again ---Locator=" + locator.getByLocator());
        increment();
        scrollIntoView(wrappedElement);
        scrollToElementLocation(wrappedElement);
        click();
    }

    public void clickForIgnoredScroll(WebDriverException ignoredOrNeedToScroll) {
        LOGGER.error("*****ERROR*****WebDriverException***** during click!-----Locator=" + locator.getByLocator());
        increment();
        //For Android error text is different and does not have any information related to clickable issue
        String ignoredOrNeedToScrollMessage = ignoredOrNeedToScroll.getMessage();
        if (isAndroid() || ignoredOrNeedToScrollMessage.contains("is not clickable at point")) {
            LOGGER.error("*****ERROR*****Element is not clickable at point***** during click! Scrolling to element and trying again. ---Locator=" + locator.getByLocator());
            if (isAndroid()) {
                //set size of page to 80%
                executeScript("document.body.style.transform='scale(0.8)'");
            }

            //This was added to fix cases when scrolling does not affect (in chrome when element is half hidden)
            //There is a chance that maximising will solve the case
            if (repeatLocateElementCounter == 10) {
                maximizeWindow();
                if (isChrome()) {
                    //Some pages (e.g. in Administration Workspace) are reloaded after maximize window in Chrome
                    TestUtils.waitForSomeTime(3000, "Wait for window maximized");
                    againLocate();
                }
            }

            scrollIntoView(wrappedElement);
            scrollToElementLocation(wrappedElement);
        }
        if (ignoredOrNeedToScrollMessage.contains("Error: element is not attached to the page document")) {
            againLocate();
        }
        if (ignoredOrNeedToScrollMessage.contains("unknown error: no element reference returned by script")) {
            againLocate();
            executeScript("arguments[0].click();", wrappedElement);
        } else if (ignoredOrNeedToScrollMessage.contains("Other element would receive the click")) {
            //TODO NT: workaround for 2.49.1
            //If dropdown option element have bigger size then dropdown we get error 'Element is not clickable at point... Other element would receive the click...'
            Actions actions = new Actions(getDriver());
            actions
                    .moveToElement(wrappedElement, 0, 0)
                    .click()
                    .perform();
        } else {
            click();
        }
    }

    @Override
    public void submit() {
        wrappedElement.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        try {
            wrappedElement.sendKeys(charSequences);
        } catch (StaleElementReferenceException e) {
            againLocate();
            sendKeys(charSequences);
        }
    }

    @Override
    public void clear() {
        try {
            wrappedElement.clear();
        } catch (StaleElementReferenceException e) {
            againLocate();
            clear();
        }
    }

    @Override
    public String getTagName() {
        try {
            return wrappedElement.getTagName();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getTagName();
        }
    }

    @Override
    public String getAttribute(String s) {
        try {
            return wrappedElement.getAttribute(s);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getAttribute(s);
        }
    }

    @Override
    public boolean isSelected() {
        try {
            return wrappedElement.isSelected();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return isSelected();
        }
    }

    @Override
    public boolean isEnabled() {
        return wrappedElement.isEnabled();
    }

    @Override
    public String getText() {
        try {
            return wrappedElement.getText();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getText();
        }
    }

    @Override
    public List<WebElement> findElements(By by) {
        try {
            List<WebElement> elements = finds(by);
            List<WebElement> result = new ArrayList<>(elements.size());
            for (int i = 0; i < elements.size(); i++) {
                WebElement element = elements.get(i);
                result.add(OurWebElementFactory.wrap(this, element, by, i));
            }
            return result;
        } catch (UndeclaredThrowableException e) {
            againLocate();
            return findElements(by);
        }
    }

    @Override
    public WebElement findElement(By by) {
        try {
            return OurWebElementFactory.wrap(this, getWrappedWebElement().findElement(by), by);
        } catch (UndeclaredThrowableException e) {
            againLocate();
            return findElement(by);
        }
    }

    @Override
    public boolean isDisplayed() {
        try {
            return wrappedElement.isDisplayed();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return isDisplayed();
        }
    }

    @Override
    public Point getLocation() {
        try {
            return wrappedElement.getLocation();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getLocation();
        }
    }

    @Override
    public Dimension getSize() {
        return wrappedElement.getSize();
    }

    @Override
    public Rectangle getRect() {
        return wrappedElement.getRect();
    }

    @Override
    public String getCssValue(String s) {
        return wrappedElement.getCssValue(s);
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) getWrappedWebElement()).getCoordinates();
    }

    @Override
    public Locator getLocator() {
        return locator;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        if (getDriver().getClass() == RemoteWebDriver.class) {
            WebDriver augmentedDriver = new Augmenter().augment(getDriver());
            return ((TakesScreenshot) augmentedDriver).getScreenshotAs(target);
        } else {
            return ((TakesScreenshot) getDriver()).getScreenshotAs(target);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && wrappedElement != null && wrappedElement.equals(o) ||
                this == o || o != null && wrappedElement != null && (o instanceof IOurWebElement) && wrappedElement.equals(((IOurWebElement) o).getWrappedWebElement());
    }

    @Override
    public int hashCode() {
        return wrappedElement != null ? wrappedElement.hashCode() : 0;
    }

    @Override
    public WebElement getWrappedWebElement() {
        return wrappedElement;
    }

    public void againLocate() {
        if (isSafari()) {
            TestUtils.waitForSafari();
        }
        WebElement againLocateElement = locator.locate();
        wrappedElement = againLocateElement instanceof IOurWebElement ? ((IOurWebElement) againLocateElement).getWrappedWebElement() : againLocateElement;
        increment();
    }

    public void increment() {
        if (repeatLocateElementCounter > MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT) {
            AbstractElementFinder.fail("Cannot interact properly with element with locator '" + locator.getByLocator() + "'" + (!wrappedElement.isDisplayed() ? "Element was not displayed!" : ""));
        } else {
            repeatLocateElementCounter++;
        }
    }

    public WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }

    public void clickInSafari() {
        int iterationCount = 0;
        TestUtils.waitForSomeTime(1000, "Wait for click for Safari");
        wrappedElement.click();
        try {
            if (!isInputOrSelectOrCheckboxElement(wrappedElement) && !isClickWithReload()) {
                while (iterationCount < 5) {
                    try {
                        elementFinder.waitForStalenessOf(wrappedElement, 1);
                        return;
                    } catch (TimeoutException ignored) {
                    }
                    iterationCount++;
                }
            }
        } catch (NoSuchWindowException ignored) {
        }
    }

    public void clickInIE() {
        TestUtils.waitForSomeTime(500, "Wait for click for IE");
        if (isDisabledElement(wrappedElement)) {
            wrappedElement.click();
            elementFinder.waitForPageToLoad();
        } else if (isActionElements(wrappedElement)) {
            final boolean isSelected = wrappedElement.isSelected();
            wrappedElement.click();
            elementFinder.waitForPageToLoad();
            //Check the checkbox is selected then click one more time if is not.
            checkElementIsSelected(isSelected, wrappedElement);
        } else if (isNotOptionInput(wrappedElement) && isNotCheckBoxLiSpan(wrappedElement) && isNotLink(wrappedElement) && isNotTable(wrappedElement) && isNotTd(wrappedElement)) {
            wrappedElement.sendKeys(Keys.ENTER);
            elementFinder.waitForPageToLoad();
        } else if (isLiTag(wrappedElement) || isTdTag(wrappedElement) || isDivTag(wrappedElement) || !isNotTable(wrappedElement)) {
            executeScript("arguments[0].click();", wrappedElement);
            elementFinder.waitForPageToLoad();
        } else if (isLink(wrappedElement)) {
            wrappedElement.sendKeys(Keys.ENTER);
        } else {
            wrappedElement.click();
            elementFinder.waitForPageToLoad();
        }
        TestUtils.waitForSomeTime(500, "");
    }

    public void scrollIntoView(WebElement element) {
        executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElementLocation(WebElement element) {
        executeScript("scroll(" + (element.getLocation().getX() + element.getSize().getWidth()) + "," + element.getLocation().getY() + ");");
    }

    public void maximizeWindow() {
        try {
            getDriver().manage().window().maximize();
        } catch (WebDriverException ignored) {
            //If a frame is selected and then browser window is maximized, exception is thrown
            //Selenium bug: Issue 3758: Exception upon maximizing browser window with frame selected
        }
    }

    public Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    //The method checks the checkbox is selected after click and click one more time if not.
    public void checkElementIsSelected(boolean wasElementSelectedByDefault, WebElement element) {
        try {
            if (wasElementSelectedByDefault == element.isSelected()) {
                element.click();
                elementFinder.waitForPageToLoad();
            }
        } catch (StaleElementReferenceException ignored) {
            //If the element changed its state after click it's an expected behavior
        }
    }

    public boolean isClickWithReload() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < 10; i++) {
            String methodName = stackTraceElements[i].getMethodName();
            if (methodName.equals("clickWithReload")) {
                return true;
            }
        }
        return false;
    }

    public List<WebElement> finds(By by) {
        try {
            //for catch stale element
            isEnabled();
            return wrappedElement.findElements(by);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return finds(by);
        } catch (UndeclaredThrowableException e) {
            //This checks for findElementByNoThrow, otherwise we call againLocate
            if (((InvocationTargetException) e.getUndeclaredThrowable()).getTargetException() instanceof NoSuchElementException) {
                try {
                    return wrappedElement.findElements(by);
                } catch (UndeclaredThrowableException noSuchElementException) {
                    throw new NoSuchElementException("Unable to locate elements " + by.toString() + ", Exception - " + ((InvocationTargetException) noSuchElementException.getUndeclaredThrowable()).getTargetException().getMessage());
                }
            }
            againLocate();
            return finds(by);
        }
    }

    public WebElement find(By by) {
        try {
            //for catch stale element
            isEnabled();
            return wrappedElement.findElement(by);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return find(by);
        } catch (UndeclaredThrowableException e) {
            //This checks for findElementByNoThrow, otherwise we call againLocate
            if (((InvocationTargetException) e.getUndeclaredThrowable()).getTargetException() instanceof NoSuchElementException) {
                try {
                    return wrappedElement.findElement(by);
                } catch (UndeclaredThrowableException noSuchElementException) {
                    throw new NoSuchElementException("Unable to locate element " + by.toString() + ", Exception - " + ((InvocationTargetException) noSuchElementException.getUndeclaredThrowable()).getTargetException().getMessage());
                }
            }
            againLocate();
            return find(by);
        } catch (NoSuchElementException e) {
            return wrappedElement.findElement(by);
        } catch (WebDriverException e) {
            againLocate();
            return find(by);
        }
    }

    public WebElement getParentElement(WebElement element) {
        if (isSafari()) {
            return element.findElement(By.xpath("./.."));
        } else {
            return (WebElement) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].parentNode", element);
        }
    }
}
