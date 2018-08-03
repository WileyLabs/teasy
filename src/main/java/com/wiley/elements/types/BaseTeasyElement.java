package com.wiley.elements.types;

import com.wiley.driver.frames.FramesTransparentWebDriver;
import com.wiley.elements.*;
import com.wiley.elements.find.DomElementLookUp;
import com.wiley.elements.find.DomElementsLookUp;
import com.wiley.elements.find.VisibleElementLookUp;
import com.wiley.elements.find.VisibleElementsLookUp;
import com.wiley.elements.types.locate.LocatableFactory;
import com.wiley.utils.Report;
import com.wiley.utils.TestUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import static com.wiley.holders.DriverHolder.getDriver;
import static com.wiley.utils.ExecutionUtils.isChrome;
import static com.wiley.utils.JsActions.executeScript;

/**
 * General implementation with all basic logic of an element
 */
public abstract class BaseTeasyElement implements TeasyElement, org.openqa.selenium.interactions.internal.Locatable {

    private WebElement wrappedElement;
    private Locatable locatable;
    private int repeatLocateElementCounter;

    //TODO think about making this value configurable
    private static final long TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS = 5;
    private static final int MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT = 20;

    BaseTeasyElement(TeasyElementData elementData) {
        this.locatable = new LocatableFactory(elementData, getDriver()).get();
        this.repeatLocateElementCounter = 0;
        this.wrappedElement = getWrappedElement(elementData);
    }

    private WebElement getWrappedElement(TeasyElementData elementData) {
        WebElement element = elementData.getElement();
        if (elementData.getSearchContext() == null
                && elementData.getBy() == null
                && elementData.getIndex() == null) {
            //parent element (by is null - as a sign that we should take parent)
            element = getParentElement(((TeasyElement) element).getWrappedWebElement());
        }
        return element instanceof TeasyElement
                ? ((TeasyElement) element).getWrappedWebElement()
                : element;
    }

    @Override
    public TeasyElement getParent() {
        return getParent(1);
    }

    @Override
    public TeasyElement getParent(int level) {
        StringBuilder parentXpath = new StringBuilder(".");
        for (int i = 0; i < level; i++) {
            parentXpath.append("/..");
        }
        return new DomElementLookUp(getDriver(), getAgainLocateStrategy(), this).find(By.xpath(parentXpath
                .toString()));
    }

    @Override
    public TeasyElement element(By by) {
        return element(by, getAgainLocateStrategy());
    }

    @Override
    public TeasyElement element(By by, SearchStrategy strategy) {
        return new VisibleElementLookUp(getDriver(), strategy, this).find(by);
    }

    @Override
    public TeasyElementList elements(By by) {
        return elements(by, getAgainLocateStrategy());
    }

    @Override
    public TeasyElementList elements(By by, SearchStrategy strategy) {
        return new VisibleElementsLookUp(getDriver(),
                getAgainLocateStrategy(),
                this).find(by);
    }

    @Override
    public TeasyElement domElement(By by) {
        return domElement(by, getAgainLocateStrategy());
    }

    @Override
    public TeasyElement domElement(By by, SearchStrategy strategy) {
        return new DomElementLookUp(getDriver(), strategy, this).find(by);
    }

    @Override
    public TeasyElementList domElements(By by) {
        return domElements(by, getAgainLocateStrategy());
    }

    @Override
    public TeasyElementList domElements(By by, SearchStrategy strategy) {
        return new DomElementsLookUp(getDriver(), strategy, this).find(by);
    }

    @Override
    public void click() {
        try {
            try {
                wrappedElement.click();
            } catch (StaleElementReferenceException e) {
                clickForStaleElement();
            } catch (UnhandledAlertException ignored) {
                Report.jenkins("*****UnhandledAlertException***** during click! Doing nothing just trying to continue the test. Locatable="
                        + locatable.getBy());
            } catch (UnreachableBrowserException ignored) {
                //doing this because for FireFox for some reason browser becomes unresponsive after click
                //but actually it is alive so it worth to try to continue test
                //it will fail on the next method after click if some real error happened
                Report.jenkins("*****ERROR*****UnreachableBrowserException***** during click! Doing nothing just trying to continue the test. Locatable=" +
                        locatable.getBy());
            } catch (ElementNotVisibleException needToScroll) {
                clickForNeedToScroll();
            } catch (WebDriverException ignoredOrNeedToScroll) {
                clickForIgnoredScroll(ignoredOrNeedToScroll);
            }
        } catch (Exception e) {
            Report.jenkins("*****UNKNOWN ERROR*****Exception***** DURING CLICK LOGIC. SHOULD BE REFACTORED!!!! Locatable="
                    + locatable.getBy(), e);
        }
    }

    private void clickForStaleElement() {
        againLocate();
        click();
    }

    private void clickForNeedToScroll() {
        Report.jenkins("ElementNotVisibleException***** during click! Scrolling to element and trying again ---Locatable="
                + locatable.getBy());
        increment();
        scrollIntoView(wrappedElement);
        scrollToElementLocation(wrappedElement);
        click();
    }

    private void clickForIgnoredScroll(WebDriverException ignoredOrNeedToScroll) {
        Report.jenkins("*****WebDriverException***** during click!-----Locatable=" + locatable.getBy());
        increment();
        //For Android error text is different and does not have any information related to clickable issue
        String ignoredOrNeedToScrollMessage = ignoredOrNeedToScroll.getMessage();
        if (ignoredOrNeedToScrollMessage.contains("is not clickable at point")) {
            Report.jenkins("*****Element is not clickable at point***** during click! Scrolling to element and trying again. ---Locatable="
                    + locatable.getBy());

            //This was added to fix cases when scrolling does not affect (in chrome when element is half hidden)
            //There is a chance that maximising will solve the case
            if (repeatLocateElementCounter == 10) {
                new TeasyWindow(getDriver()).maximize();
                if (isChrome()) {
                    //Some pages (e.g. in Administration Workspace) are reloaded after maximize window in Chrome
                    TestUtils.sleep(3000, "Wait for window maximized");
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
            new Actions(getDriver())
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
        try {
            return wrappedElement.isEnabled();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return isEnabled();
        }
    }

    @Override
    public boolean isStale() {
        try {
            wrappedElement.isEnabled();
            return false;
        } catch (StaleElementReferenceException elementIsStale) {
            return true;
        }
    }

    @Override
    public String getText() {
        try {
            String text;
            if (isInputTextField()) {
                text = wrappedElement.getText().isEmpty() ? wrappedElement.getAttribute("value") : wrappedElement.getText();
            } else {
                text = wrappedElement.getText();
            }
            return text;
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getText();
        }
    }

    private boolean isInputTextField() {
        return wrappedElement.getTagName().equals("input")
                && wrappedElement.getAttribute("type") != null
                && wrappedElement.getAttribute("type").equals("text");
    }

    public WebElement findElement(By by) {
        this.repeatLocateElementCounter = 0;
        return find(by);
    }

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
        try {
            return wrappedElement.getSize();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getSize();
        }
    }

    @Override
    public Rectangle getRect() {
        try {
            return wrappedElement.getRect();
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getRect();
        }
    }

    @Override
    public String getCssValue(String s) {
        try {
            return wrappedElement.getCssValue(s);
        } catch (StaleElementReferenceException e) {
            againLocate();
            return getCssValue(s);
        }
    }

    @Override
    public Coordinates getCoordinates() {
        return ((org.openqa.selenium.interactions.internal.Locatable) getWrappedWebElement()).getCoordinates();
    }

    @Override
    public Locatable getLocatable() {
        return locatable;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) {
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
                this == o || o != null && wrappedElement != null && (o instanceof TeasyElement) && wrappedElement
                .equals(((TeasyElement) o).getWrappedWebElement());
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
        return locatable.getBy().toString();
    }

    private void againLocate() {
        WebElement againLocateElement = locatable.find();
        wrappedElement = againLocateElement instanceof TeasyElement ? ((TeasyElement) againLocateElement)
                .getWrappedWebElement() : againLocateElement;
        increment();
    }

    private void increment() {
        if (repeatLocateElementCounter > MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT) {
            throw new RuntimeException("Cannot interact properly with element with locatable '"
                    + locatable.getBy() + "'"
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
        return (FramesTransparentWebDriver) getDriver();
    }

    private SearchStrategy getAgainLocateStrategy() {
        return new SearchStrategy(TIMEOUT_FOR_AGAIN_LOCATE_IN_SECONDS);
    }
}
