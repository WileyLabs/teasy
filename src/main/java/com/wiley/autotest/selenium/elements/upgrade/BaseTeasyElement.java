package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.Report;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.upgrade.v3.TeasyElementFinder;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;
import static com.wiley.autotest.utils.ExecutionUtils.isChrome;
import static com.wiley.autotest.utils.TestUtils.fail;
import static com.wiley.autotest.utils.TestUtils.waitForSomeTime;


/**
 * General implementation with all basic logic of an element
 */
public abstract class BaseTeasyElement implements TeasyElement, Locatable {

    private WebElement wrappedElement;
    private Locator locator;
    private TeasyElementFinder contextFinder;

    //TODO think about making this value configurable
    private static final long TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS = 5;
    private int repeatLocateElementCounter;
    private static final int MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT = 20;

    BaseTeasyElement(TeasyElementData elementData) {
        WebElement element = elementData.getElement();
        TeasyElement searchContext = elementData.getSearchContext();
        By by = elementData.getBy();
        Integer index = elementData.getIndex();

        if (searchContext == null && by == null && index == null) {
            //parent element (locator is null - as a sign that we should take parent)
            TeasyElement ourWebElement = (TeasyElement) element;
            element = getParentElement(ourWebElement.getWrappedWebElement());
        }
        this.locator = new ElementLocatorFactory(elementData, getWebDriver()).getLocator();

        init(element);
    }

    public void init(WebElement element) {
        this.wrappedElement = element instanceof TeasyElement ? ((TeasyElement) element).getWrappedWebElement() : element;
        this.repeatLocateElementCounter = 0;

        if (contextFinder == null) {
            contextFinder = new TeasyElementFinder(getWebDriver(), new SearchStrategy(TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS), this);
        }
    }

    @Override
    public TeasyElement getParent() {
        return getParent(1);
    }

    @Override
    public TeasyElement getParent(int level) {
        StringBuilder builder = new StringBuilder(".");
        for (int i = 0; i < level; i++) {
            builder.append("/..");
        }
        return TeasyElementWrapper.wrap(this, find(By.xpath(builder.toString())), By.xpath(builder.toString()), TeasyElementType.DOM);
    }

    @Override
    public TeasyElement element(By by) {
        return contextFinder.visibleElement(by);
    }

    @Override
    public TeasyElement element(By by, SearchStrategy strategy) {
        return customContextFinder(strategy).visibleElement(by);
    }

    @Override
    public List<TeasyElement> elements(By by) {
        return contextFinder.visibleElements(by);
    }

    @Override
    public List<TeasyElement> elements(By by, SearchStrategy strategy) {
        return customContextFinder(strategy).visibleElements(by);
    }

    @Override
    public TeasyElement domElement(By by) {
        return contextFinder.presentInDomElement(by);
    }

    @Override
    public TeasyElement domElement(By by, SearchStrategy strategy) {
        return customContextFinder(strategy).presentInDomElement(by);
    }

    @Override
    public List<TeasyElement> domElements(By by) {
        return contextFinder.presentInDomElements(by);
    }

    @Override
    public List<TeasyElement> domElements(By by, SearchStrategy strategy) {
        return customContextFinder(strategy).presentInDomElements(by);
    }

    TeasyElementFinder customContextFinder(SearchStrategy strategy) {
        return new TeasyElementFinder(getWebDriver(), strategy, this);
    }

    @Override
    public void click() {
        try {
            try {
                wrappedElement.click();
            } catch (StaleElementReferenceException e) {
                clickForStaleElement();
            } catch (UnhandledAlertException ignored) {
                new Report("*****UnhandledAlertException***** during click! Doing nothing just trying to continue the test. Locator=" + locator
                        .getLocator()).jenkins();
            } catch (UnreachableBrowserException ignored) {
                //doing this because for FireFox for some reason browser becomes unresponsive after click
                //but actually it is alive so it worth to try to continue test
                //it will fail on the next method after click if some real error happened
                new Report("*****ERROR*****UnreachableBrowserException***** during click! Doing nothing just trying to continue the test. Locator=" + locator
                        .getLocator()).jenkins();
            } catch (ElementNotVisibleException needToScroll) {
                clickForNeedToScroll();
            } catch (WebDriverException ignoredOrNeedToScroll) {
                clickForIgnoredScroll(ignoredOrNeedToScroll);
            }
        } catch (Exception e) {
            new Report("*****UNKNOWN ERROR*****Exception***** DURING CLICK LOGIC. SHOULD BE REFACTORED!!!! Locator=" + locator.getLocator(), e).jenkins();
        }
    }

    private void clickForStaleElement() {
        againLocate();
        click();
    }

    private void clickForNeedToScroll() {
        new Report("ElementNotVisibleException***** during click! Scrolling to element and trying again ---Locator=" + locator.getLocator()).jenkins();
        increment();
        scrollIntoView(wrappedElement);
        scrollToElementLocation(wrappedElement);
        click();
    }

    private void clickForIgnoredScroll(WebDriverException ignoredOrNeedToScroll) {
        new Report("*****WebDriverException***** during click!-----Locator=" + locator.getLocator()).jenkins();
        increment();
        //For Android error text is different and does not have any information related to clickable issue
        String ignoredOrNeedToScrollMessage = ignoredOrNeedToScroll.getMessage();
        if (ignoredOrNeedToScrollMessage.contains("is not clickable at point")) {
            new Report("*****Element is not clickable at point***** during click! Scrolling to element and trying again. ---Locator=" + locator
                    .getLocator()).jenkins();

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
            Actions actions = new Actions(getWebDriver());
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
    public <X> X getScreenshotAs(OutputType<X> target) {
        if (getWebDriver().getClass() == RemoteWebDriver.class) {
            WebDriver augmentedDriver = new Augmenter().augment(getWebDriver());
            return ((TakesScreenshot) augmentedDriver).getScreenshotAs(target);
        } else {
            return ((TakesScreenshot) getWebDriver()).getScreenshotAs(target);
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && wrappedElement != null && wrappedElement.equals(o) ||
                this == o || o != null && wrappedElement != null && (o instanceof TeasyElement) && wrappedElement.equals(((TeasyElement) o)
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
    public String toString() {
        return locator.getLocator().toString();
    }

    private void againLocate() {
        WebElement againLocateElement = locator.find();
        wrappedElement = againLocateElement instanceof TeasyElement ? ((TeasyElement) againLocateElement).getWrappedWebElement() : againLocateElement;
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
            getWebDriver().manage().window().maximize();
        } catch (WebDriverException ignored) {
            //If a frame is selected and then browser window is maximized, exception is thrown
            //Selenium bug: Issue 3758: Exception upon maximizing browser window with frame selected
        }
    }

    private Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getWebDriver()).executeScript(script, args);
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
        return (WebElement) executeScript("return arguments[0].parentNode", element);
    }

    private FramesTransparentWebDriver getFrameTransparentDriver() {
        return (FramesTransparentWebDriver) getWebDriver();
    }

}
