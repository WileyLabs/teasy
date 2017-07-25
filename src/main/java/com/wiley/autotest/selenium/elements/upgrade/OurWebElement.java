package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.ElementFinder;
import com.wiley.autotest.WebDriverAwareElementFinder;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurElementFinder;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurSearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurShould;
import com.wiley.autotest.selenium.elements.upgrade.v3.OurWaitFor;
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
import java.util.List;

import static com.wiley.autotest.utils.ExecutionUtils.isChrome;
import static com.wiley.autotest.utils.TestUtils.fail;
import static com.wiley.autotest.utils.TestUtils.waitForSomeTime;


/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:41
 */
public class OurWebElement implements IOurWebElement, Locatable {

    private WebElement wrappedElement;
    private Locator locator;
    private ElementFinder elementFinder_TO_BE_REMOVED;
    private OurElementFinder contextFinder;

    //specific element finder which will return null or emptyList in case element is not found
    private OurElementFinder allowNullContextFinder;

    public static final Logger LOGGER = LoggerFactory.getLogger(OurWebElement.class);
    //The duration in milliseconds to sleep between polls. (default value in selenium is 500)
    private static final long SLEEP_IN_MILLISECONDS = 1000;

    //TODO think about making this value configurable
    private static final long TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS = 5;
    private int repeatLocateElementCounter;
    private static final int MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT = 20;

    public OurWebElement(OurWebElementData ourWebElementData) {
        WebElement element = ourWebElementData.getElement();
        OurWebElement searchContext = ourWebElementData.getSearchContext();
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
            this.locator = new FindParentElementLocator(getDriver(), ourWebElement.getLocator().getLocator());
        }

        init(element);
    }

    public void init(WebElement element) {
        this.wrappedElement = element instanceof IOurWebElement ? ((IOurWebElement) element).getWrappedWebElement() : element;
        this.repeatLocateElementCounter = 0;
        if (elementFinder_TO_BE_REMOVED == null) {
            elementFinder_TO_BE_REMOVED = new WebDriverAwareElementFinder(getDriver(), new WebDriverWait(getDriver(), TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS, SLEEP_IN_MILLISECONDS));
        }

        if (contextFinder == null) {
            contextFinder = new OurElementFinder(getDriver(), new OurSearchStrategy(TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS), this);
        }
        if (allowNullContextFinder == null) {
            allowNullContextFinder = new OurElementFinder(getDriver(), new OurSearchStrategy(TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS)
                    .nullOnFailure(), this);
        }
    }

    @Override
    public OurShould should() {
        return new OurShould(this);
    }

    @Override
    public OurShould should(OurSearchStrategy strategy) {
        return new OurShould(this, strategy);
    }

    @Override
    public OurWaitFor waitFor() {
        return new OurWaitFor(this);
    }

    @Override
    public OurWaitFor waitFor(OurSearchStrategy strategy) {
        return new OurWaitFor(this, strategy);
    }

    @Override
    public OurWebElement getParent() {
        return getParent(1);
    }

    @Override
    public OurWebElement getParent(int level) {
        StringBuilder builder = new StringBuilder(".");
        for (int i = 0; i < level; i++) {
            builder.append("/..");
        }
        return OurWebElementFactory.wrap(this, find(By.xpath(builder.toString())), By.xpath(builder.toString()));
    }

    @Override
    public OurWebElement element(By by) {
        return contextFinder.visibleElement(by);
    }

    @Override
    public List<OurWebElement> elements(By by) {
        return contextFinder.visibleElements(by);
    }

    @Override
    public OurWebElement elementOrNull(By by) {
        return allowNullContextFinder.visibleElement(by);
    }

    @Override
    public List<OurWebElement> elementsOrEmpty(By by) {
        return allowNullContextFinder.visibleElements(by);
    }

    @Override
    public OurWebElement domElement(By by) {
        return contextFinder.presentInDomElement(by);
    }

    @Override
    public List<OurWebElement> domElements(By by) {
        return contextFinder.presentInDomElements(by);
    }

    @Override
    public void click() {
        try {
            try {
                wrappedElement.click();
            } catch (StaleElementReferenceException e) {
                clickForStaleElement();
            } catch (UnhandledAlertException ignored) {
                LOGGER.error("*****ERROR*****UnhandledAlertException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator
                        .getLocator());
            } catch (UnreachableBrowserException ignored) {
                //doing this because for FireFox for some reason browser becomes unresponsive after click
                //but actually it is alive so it worth to try to continue test
                //it will fail on the next method after click if some real error happened
                LOGGER.error("*****ERROR*****UnreachableBrowserException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator
                        .getLocator());
            } catch (ElementNotVisibleException needToScroll) {
                clickForNeedToScroll();
            } catch (WebDriverException ignoredOrNeedToScroll) {
                clickForIgnoredScroll(ignoredOrNeedToScroll);
            }
        } catch (Exception e) {
            LOGGER.error("*****FATAL ERROR*****Exception***** DURING CLICK LOGIC. SHOULD BE REFACTORED!!!! -----Locator=" + locator
                    .getLocator(), e);
        }
    }

    private void clickForStaleElement() {
        againLocate();
        click();
    }

    private void clickForNeedToScroll() {
        LOGGER.error("*****ERROR*****ElementNotVisibleException***** during click! Scrolling to element and trying again ---Locator=" + locator
                .getLocator());
        increment();
        scrollIntoView(wrappedElement);
        scrollToElementLocation(wrappedElement);
        click();
    }

    private void clickForIgnoredScroll(WebDriverException ignoredOrNeedToScroll) {
        LOGGER.error("*****ERROR*****WebDriverException***** during click!-----Locator=" + locator.getLocator());
        increment();
        //For Android error text is different and does not have any information related to clickable issue
        String ignoredOrNeedToScrollMessage = ignoredOrNeedToScroll.getMessage();
          if (ignoredOrNeedToScrollMessage.contains("is not clickable at point")) {
             LOGGER.error("*****ERROR*****Element is not clickable at point***** during click! Scrolling to element and trying again. ---Locator=" + locator
                     .getLocator());

             //This was added to fix cases when scrolling does not affect (in chrome when element is half hidden)
             //There is a chance that maximising will solve the case
             if (repeatLocateElementCounter == 10) {
                 maximizeWindow();
                 if (isChrome()) {
                     //Some pages (e.g. in Administration Workspace) are reloaded after maximize window in Chrome
                     waitForSomeTime(3000, "Wait for window maximized");
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

    /**
     * This method *should not* catch StaleElementReferenceException
     * as it is used to define staleness of element
     */
    @Override
    public boolean isEnabled() {
        return wrappedElement.isEnabled();
    }

    @Override
    public boolean isStale() {
        try {
            isEnabled();
            return false;
        } catch (StaleElementReferenceException elementIsStale) {
            return true;
        }
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

    @Deprecated
    /**
     * do not use this method. it will be deleted in future.
     * use element(), domElement(), elementOrNull() depending on your needs
     */
    public WebElement findElement(By by) {
        this.repeatLocateElementCounter = 0;
        return find(by);
    }

    @Deprecated
    /**
     * do not use this method. it will be deleted in future.
     * use elements(), domElements(), elementsOrEmpty() depending on your needs
     */
    public List<WebElement> findElements(By by) {
        this.repeatLocateElementCounter = 0;
        return finds(by);
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
                this == o || o != null && wrappedElement != null && (o instanceof IOurWebElement) && wrappedElement.equals(((IOurWebElement) o)
                .getWrappedWebElement());
    }

    @Override
    public int hashCode() {
        return wrappedElement != null ? wrappedElement.hashCode() : 0;
    }

    @Override
    public WebElement getWrappedWebElement() {
        return wrappedElement;
    }

    @Override
    public String toString(){
        return locator.getLocator().toString();
    }

    private ElementFinder getElementFinder() {
        return elementFinder_TO_BE_REMOVED;
    }

    private int getRepeatLocateElementCounter() {
        return repeatLocateElementCounter;
    }

    /**
     * TODO VE: So far this method is public only because it's needed for some "logic" in FrameTransparentWebDriver
     * it should be made private after better solution is found
     */
    public void againLocate() {
        WebElement againLocateElement = locator.find();
        wrappedElement = againLocateElement instanceof IOurWebElement ? ((IOurWebElement) againLocateElement).getWrappedWebElement() : againLocateElement;
        increment();
    }

    private void increment() {
        if (repeatLocateElementCounter > MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT) {
            fail("Cannot interact properly with element with locator '" + locator.getLocator() + "'"
                    + (!wrappedElement.isDisplayed() ? "Element was not displayed!" : ""));
        } else {
            repeatLocateElementCounter++;
        }
    }

    private void scrollIntoView(WebElement element) {
        executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void scrollToElementLocation(WebElement element) {
        executeScript("scroll(" + (element.getLocation().getX() + element.getSize()
                .getWidth()) + "," + element.getLocation().getY() + ");");
    }

    private void maximizeWindow() {
        try {
            getDriver().manage().window().maximize();
        } catch (WebDriverException ignored) {
            //If a frame is selected and then browser window is maximized, exception is thrown
            //Selenium bug: Issue 3758: Exception upon maximizing browser window with frame selected
        }
    }

    private Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    @Deprecated
    //tobe removed from the framework
    //The method checks the checkbox is selected after click and click one more time if not.
    private void checkElementIsSelected(boolean wasElementSelectedByDefault, WebElement element) {
        try {
            if (wasElementSelectedByDefault == element.isSelected()) {
                element.click();
                elementFinder_TO_BE_REMOVED.waitForPageToLoad();
            }
        } catch (StaleElementReferenceException ignored) {
            //If the element changed its state after click it's an expected behavior
        }
    }

    private boolean isClickWithReload() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 0; i < 10; i++) {
            String methodName = stackTraceElements[i].getMethodName();
            if (methodName.equals("clickWithReload")) {
                return true;
            }
        }
        return false;
    }

    private List<WebElement> finds(By by) {
        try {
            //for catch stale element
            isEnabled();
            return (getFrameTransparentDriver()).findElements(this, by);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return finds(by);
        } catch (UndeclaredThrowableException e) {
            //This checks for findElementByNoThrow, otherwise we call againLocate
            if (((InvocationTargetException) e.getUndeclaredThrowable()).getTargetException() instanceof NoSuchElementException) {
                try {
                    return (getFrameTransparentDriver()).findElements(this, by);
                } catch (UndeclaredThrowableException noSuchElementException) {
                    throw new NoSuchElementException("Unable to find elements " + by.toString() + ", Exception - " + ((InvocationTargetException) noSuchElementException
                            .getUndeclaredThrowable()).getTargetException().getMessage());
                }
            }
            againLocate();
            return finds(by);
        }
    }

    private WebElement find(By by) {
        try {
            //for catch stale element
            isEnabled();
            return (getFrameTransparentDriver()).findElement(this, by);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return find(by);
        } catch (UndeclaredThrowableException e) {
            //This checks for findElementByNoThrow, otherwise we call againLocate
            if (((InvocationTargetException) e.getUndeclaredThrowable()).getTargetException() instanceof NoSuchElementException) {
                try {
                    return getFrameTransparentDriver().findElement(this, by);
                } catch (UndeclaredThrowableException noSuchElementException) {
                    throw new NoSuchElementException("Unable to find element " + by.toString() + ", Exception - " + ((InvocationTargetException) noSuchElementException
                            .getUndeclaredThrowable()).getTargetException().getMessage());
                }
            }
            againLocate();
            return find(by);
        } catch (NoSuchElementException e) {
            return (getFrameTransparentDriver()).findElement(this, by);
        } catch (WebDriverException e) {
            againLocate();
            return find(by);
        }
    }

    private WebElement getParentElement(WebElement element) {
        return (WebElement) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].parentNode", element);
    }

    private FramesTransparentWebDriver getFrameTransparentDriver() {
        return (FramesTransparentWebDriver) getDriver();
    }

    private WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }
}
