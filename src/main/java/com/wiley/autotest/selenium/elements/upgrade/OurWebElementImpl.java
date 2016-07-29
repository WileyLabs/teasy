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
import org.openqa.selenium.remote.SessionNotFoundException;
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
public class OurWebElementImpl implements OurWebElement, Locatable {

    public WebElement wrappedElement;
    private Locator locator;
    private ElementFinder elementFinder;
    public static final Logger LOGGER = LoggerFactory.getLogger(OurWebElementImpl.class);
    //The duration in milliseconds to sleep between polls. (default value in selenium is 500)
    private static final long SLEEP_IN_MILLISECONDS = 1000;
    private static final long WAIT_TIME_OUT_IN_SECONDS = 5;
    private int repeatLocateElementCounter;
    private static final int MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT = 20;

    public void init(final WebDriver driver, Long timeout) {
        elementFinder = new WebDriverAwareElementFinder(driver, new WebDriverWait(driver, timeout, SLEEP_IN_MILLISECONDS));
    }

    public static WebElement wrap(WebElement element, Locator locator) {
        return new OurWebElementImpl(element, locator);
    }

    public static WebElement wrap(WebElement element, By by) {
        return new OurWebElementImpl(element, new FindElementLocator(getDriver(), by));
    }

    public static WebElement wrapParent(WebElement element) {
        OurWebElement ourWebElement = (OurWebElement) element;
        return new OurWebElementImpl(getParentElement(ourWebElement.getWrappedWebElement()), new FindParentElementLocator(getDriver(), ourWebElement.getLocator().getByLocator()));
    }

    private static WebElement getParentElement(WebElement element) {
        if (isSafari()) {
            return element.findElement(By.xpath("./.."));
        } else {
            return (WebElement) ((JavascriptExecutor) getDriver()).executeScript("return arguments[0].parentNode", element);
        }
    }

    public static List<WebElement> wrap(List<WebElement> elements, SearchContext searchContext, By by) {
        List<WebElement> result = new ArrayList<WebElement>(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            WebElement element = elements.get(i);
            result.add(wrap(element, new FindElementsLocator(searchContext, by, i)));
        }
        return result;
    }

    public static List<WebElement> wrap(List<WebElement> elements, By by) {
        List<WebElement> result = new ArrayList<WebElement>(elements.size());
        for (int i = 0; i < elements.size(); i++) {
            WebElement element = elements.get(i);
            result.add(wrap(element, new FindElementsLocator(getDriver(), by, i)));
        }
        return result;
    }

    public OurWebElementImpl(WebElement element, Locator locator) {
        this.wrappedElement = element instanceof OurWebElementImpl ? ((OurWebElementImpl) element).getWrappedElement() : element;
        this.locator = locator;
        this.repeatLocateElementCounter = 0;
        if (elementFinder == null) {
            init(getDriver(), WAIT_TIME_OUT_IN_SECONDS);
        }
    }

    @Override
    public WebElement getWrappedWebElement() {
        return wrappedElement;
    }

    @Override
    public void click() {
        try {
            try {
                if (isIE()) {
                    clickInIE();
                } else if (isSafari()) {
                    clickInSafari();
                } else {
                    wrappedElement.click();
                }
            } catch (StaleElementReferenceException e) {
                againLocate();
                click();
            } catch (UnhandledAlertException ignored) {
                LOGGER.error("*****ERROR*****UnhandledAlertException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator.getByLocator());
            } catch (UnreachableBrowserException ignored) {
                //doing this because for FireFox for some reason browser becomes unresponsive after click
                //but actually it is alive so it worth to try to continue test
                //it will fail on the next method after click if some real error happened
                LOGGER.error("*****ERROR*****UnreachableBrowserException***** during click! Doing nothing just trying to continue the test. ---Locator=" + locator.getByLocator());
            } catch (ElementNotVisibleException needToScroll) {
                LOGGER.error("*****ERROR*****ElementNotVisibleException***** during click! Scrolling to element and trying again ---Locator=" + locator.getByLocator());
                increment();
                scrollIntoView(wrappedElement);
                scrollToElementLocation(wrappedElement);
                click();
            } catch (TimeoutException ignored) {
                //hangs in chrome after create system announcements, refresh page should be resolved it.
                LOGGER.error("*****ERROR*****TimeoutException***** during click!-----Locator=" + locator.getByLocator());
                getDriver().navigate().refresh();
            } catch (WebDriverException ignoredOrNeedToScroll) {
                LOGGER.error("*****ERROR*****WebDriverException***** during click!-----Locator=" + locator.getByLocator());
                closeAnnouncementsPopUpWindow();
                increment();
                //For Android error text is different and does not have any information related to clickable issue
                String ignoredOrNeedToScrollMessage = ignoredOrNeedToScroll.getMessage();
                if (isAndroid() || ignoredOrNeedToScrollMessage.contains("Element is not clickable at point")) {
                    LOGGER.error("*****ERROR*****Element is not clickable at point***** during click! Scrolling to element and trying again. ---Locator=" + locator.getByLocator());
                    if (isAndroid()) {
                        //set size of page to 80%
                        executeScript("document.body.style.transform='scale(0.8)'");
                    }

                    //Fix 'Walk Me' button override element
                    if (ignoredOrNeedToScrollMessage.contains("Other element would receive the click") &&
                            ignoredOrNeedToScrollMessage.contains("walkme-title walkme-override walkme-css-reset")) {
                        executeScript("scroll(0,0);");
                    }

                    //This was added to fix cases when scrolling does not affect (in chrome when element is half hidden)
                    //There is a chance that maximising will solve the case
                    if (repeatLocateElementCounter == 10) {
                        maximizeWindow();
                        if (isChrome()) {
                            //Some pages (e.g. in Administration Workspace) are reloaded after maximize window in Chrome
                            TestUtils.waitForSomeTime(3000);
                            againLocate();
                        }
                    }

                    scrollIntoView(wrappedElement);
                    scrollToElementLocation(wrappedElement);

                    //In ORION sometimes hangs in chrome, need place after scrollIntoView;
                    if (ignoredOrNeedToScrollMessage.contains("Other element would receive the click")) {
                        executeScript("scroll(0,0);");
                    }

                }
                if (ignoredOrNeedToScrollMessage.contains("Error: element is not attached to the page document")) {
                    againLocate();
                }
                List<WebElement> tipCloseButtonList = elementFinder.findElementsBy(By.xpath("//*[@class='customTipCloseLink']"));
                for (WebElement closeBtn : tipCloseButtonList) {
                    if (closeBtn.isDisplayed()) {
                        closeBtn.click();
                    }
                }
                //TODO NT: If we get 'cannot press more then one button' error uncomment this.
                //TODO: In some case we can get 'Cannot interact properly with element', for example in clickWithReload.
                if (ignoredOrNeedToScrollMessage.toLowerCase().contains("cannot press more then one button or an already pressed button")) {
                    LOGGER.error("*****ERROR*****Cannot press more then one button or an already pressed button. ---Locator=" + locator.getByLocator());
//                    executeScript("arguments[0].click();", wrappedElement);
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
        } catch (Exception e) {
            LOGGER.error("*****FATAL ERROR*****Exception***** DURING CLICK LOGIC. SHOULD BE REFACTORED!!!! -----Locator=" + locator.getByLocator(), e);
            // not completely clear but what if after element click we had WebDriverException
            // but click was performed and there element became stale..
        }
        if (isAndroid()) {
            try {
                //set size of page to 100% after click
                executeScript("document.body.style.transform='scale(1)'");
            } catch (UnhandledAlertException ignored) {
            } catch (NoSuchWindowException ignored) {
            } catch (SessionNotFoundException ignored) {
            }
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
        return wrap(finds(by), this, by);
    }

    @Override
    public WebElement findElement(By by) {
        return wrap(find(by), new FindElementLocator(this, by));
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
    public String getCssValue(String s) {
        return wrappedElement.getCssValue(s);
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) getWrappedWebElement()).getCoordinates();
    }

    protected void againLocate() {
        if (isSafari()) {
            TestUtils.waitForSafari();
        }
        WebElement againLocateElement = locator.locate();
        wrappedElement = againLocateElement instanceof OurWebElementImpl ? ((OurWebElementImpl) againLocateElement).getWrappedElement() : againLocateElement;
        increment();
    }

    private void increment() {
        if (repeatLocateElementCounter > MAX_NUMBER_OF_REPEAT_LOCATE_ELEMENT) {
            AbstractElementFinder.fail("Cannot interact properly with element with locator '" + locator.getByLocator() + "'" + (!wrappedElement.isDisplayed() ? "Element was not displayed!" : ""));
        } else {
            repeatLocateElementCounter++;
        }
    }

    public WebElement getWrappedElement() {
        return wrappedElement;
    }

    protected static WebDriver getDriver() {
        return SeleniumHolder.getWebDriver();
    }

    protected void scrollIntoView(WebElement element) {
        executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void scrollToElementLocation(WebElement element) {
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


    private Object executeScript(String script, Object... args) {
        return ((JavascriptExecutor) getDriver()).executeScript(script, args);
    }

    //The method checks the checkbox is selected after click and click one more time if not.
    private void checkElementIsSelected(boolean wasElementSelectedByDefault, WebElement element) {
        try {
            if (wasElementSelectedByDefault == element.isSelected()) {
                element.click();
                elementFinder.waitForPageToLoad();
            }
        } catch (StaleElementReferenceException ignored) {
            //If the element changed its state after click it's an expected behavior
        }
    }

    private void clickInIE() {
        TestUtils.waitForSomeTime(500);
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
        TestUtils.waitForSomeTime(500);
    }

    public boolean isDivWithParentA(WebElement element) {
        return isDivTag(element) && getParentElement(element).getTagName().equals("a");
    }

    private void clickInSafari() {
        int iterationCount = 0;
        TestUtils.waitForSomeTime(1000);
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

    @Override
    public Locator getLocator() {
        return locator;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && wrappedElement != null && wrappedElement.equals(o) ||
                this == o || o != null && wrappedElement != null && (o instanceof OurWebElementImpl) && wrappedElement.equals(((OurWebElementImpl) o).getWrappedElement());
    }

    @Override
    public int hashCode() {
        return wrappedElement != null ? wrappedElement.hashCode() : 0;
    }

    private List<WebElement> finds(By by) {
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
                return wrappedElement.findElements(by);
            }
            againLocate();
            return finds(by);
        }
    }

    private WebElement find(By by) {
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
                return wrappedElement.findElement(by);
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

    private void closeAnnouncementsPopUpWindow() {
        //it was added because sometimes pop up appears with delay in chrome
        if (isChrome() || isIE()) {
            TestUtils.waitForSomeTime(2000);
        }
        try {
            List<WebElement> homeTabElementList = elementFinder.findElementsBy(By.id("home-tab"));
            if (!homeTabElementList.isEmpty()) {
                List<WebElement> popUps = elementFinder.findElementsBy(homeTabElementList.get(0), By.id("highPriorityAnnouncementWindow"));
                if (!popUps.isEmpty()) {
                    List<WebElement> loaderElementList = elementFinder.findElementsBy(By.cssSelector(".announcement-loader-high-priority"));
                    if (!loaderElementList.isEmpty()) {
                        elementFinder.waitForInvisibilityOfElementLocatedBy(By.cssSelector(".announcement-loader-high-priority"));
                    }
                    List<WebElement> closeButtons = elementFinder.findElementsBy(popUps.get(0), By.id("heightPriorityCancel"));
                    if (!closeButtons.isEmpty()) {
                        if (closeButtons.get(0).isDisplayed()) {
                            //http://code.google.com/p/chromedriver/issues/detail?id=1158 occurs here for Welcome Pop up
                            try {
                                closeButtons.get(0).click();
                            } catch (WebDriverException e) {
                                executeScript("arguments[0].click();", closeButtons.get(0));
                            }
                        }
                    }
                }
            }
        } catch (WebDriverException ignoredOrNeedToScroll) {
            if (ignoredOrNeedToScroll.getMessage().contains("unknown error: no element reference returned by script")) {
                increment();
                againLocate();
                closeAnnouncementsPopUpWindow();
            }
        }
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
}
