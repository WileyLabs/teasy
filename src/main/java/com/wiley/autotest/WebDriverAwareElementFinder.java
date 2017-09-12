package com.wiley.autotest;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.TextField;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.PageLoaded;
import com.wiley.autotest.utils.TestUtils;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.wiley.autotest.ExpectedConditions2.*;
import static com.wiley.autotest.ExpectedConditions2.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class WebDriverAwareElementFinder implements ElementFinder {
    private WebDriver driver;
    private WebDriverWait wait;
    private Integer waitTimeout = 1;
    private static final long POOLLING_EVERY_DURATION_IN_SEC = 2;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverAwareElementFinder.class);

    public WebDriverAwareElementFinder(final WebDriver webDriver, final WebDriverWait webDriverWait, Integer waitTimeout) {
        this.driver = webDriver;
        this.wait = webDriverWait;
        if (waitTimeout != null) {
            this.waitTimeout = waitTimeout;
        }
    }

    public WebDriverAwareElementFinder(final WebDriver webDriver, final WebDriverWait webDriverWait) {
        this.driver = webDriver;
        this.wait = webDriverWait;
    }

    private FramesTransparentWebDriver getFrameTransparentWebDriver() {
        return (FramesTransparentWebDriver) driver;
    }

    @Override
    public WebElement findElementBy(final By locator) {
        return driver.findElement(locator);
    }

    @Override
    public WebElement findElementBy(final SearchContext searchContext, final By locator) {
        return searchContext.findElement(locator);
    }

    @Override
    @Deprecated
    public List<WebElement> findElementsBy(final By locator) {
        return driver.findElements(locator);
    }

    @Override
    @Deprecated
    public List<WebElement> findElementsBy(final SearchContext searchContext, final By locator) {
        return searchContext.findElements(locator);
    }

    public Alert waitForAlert() {
        return waitFor(ExpectedConditions.alertIsPresent());
    }

    @Override
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfElementLocatedBy(locator));
    }

    @Override
    public void waitForCondition(ExpectedCondition<Boolean> condition, long timeOutInSeconds) {
        waitFor(condition, timeOutInSeconds);
    }

    @Override
    public Boolean waitForTextToBePresentInElement(final By locator, final String text) {
        return waitFor(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Override
    public Boolean waitForTextToBePresentInElement(final By locator) {
        return waitFor(ExpectedConditions2.textToBePresentInElement(locator));
    }

    @Override
    public WebElement waitForElementToBeClickable(final By locator) {
        return waitFor(elementToBeClickable(locator));
    }

    @Override
    public void waitForElementToBeClickable(WebElement element) {
        waitFor(elementToBeClickable(element));
    }

    @Override
    public void waitForVisibilityOf(final WebElement element) {
        waitFor(ExpectedConditions2.visibilityOf(element));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfFirstElementsInAllFrames(locator));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator, final long timeOutInSeconds) {
        return waitFor(ExpectedConditions2.visibilityOfFirstElementsInAllFrames(locator), timeOutInSeconds);
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsInAllFrames(locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator, final long timeOutInSeconds) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsInAllFrames(locator), timeOutInSeconds);
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedBy(locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeOutInSeconds) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedBy(locator), timeOutInSeconds);
    }

    @Override
    public WebElement waitForPresenceOfElementLocatedBy(final By locator) {
        return waitFor(presenceOfElementLocated(locator));
    }

    @Override
    public WebElement waitForPresenceOfElementLocatedBy(final SearchContext searchContext, final By locator) {
        return waitFor(ExpectedConditions2.presenceOfElementLocatedBy(searchContext, locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedBy(searchContext, locator));
    }

    @Override
    public Boolean waitForAbsenceOfElementLocatedBy(final By locator) {
        return waitFor(absenceElementBy(locator));
    }

    @Override
    public Boolean waitForAbsenceOfElementLocatedBy(final By locator, long timeoutInSec) {
        return waitFor(absenceElementBy(locator), timeoutInSec);
    }

    @Override
    public WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return waitFor(presenceOfElementLocated(locator), timeOutInSeconds);
    }

    @Override
    public boolean isTextPresentInElement(final By locator, final String text) {
        return ExpectedConditions.textToBePresentInElementLocated(locator, text).apply(driver);
    }

    @Override
    public Boolean waitForStalenessOf(final WebElement webElement) {
        return waitFor(stalenessOf(webElement));
    }

    @Override
    public Boolean waitForStalenessOf(final WebElement webElement, final long timeOutInSeconds) {
        return waitFor(stalenessOf(webElement), timeOutInSeconds);
    }

    @Override
    public void waitForWindowToBeAppearedAndSwitchToIt(final String title) {
        waitFor(appearingOfWindowAndSwitchToIt(title));
    }

    @Override
    public void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(final String fullTitle) {
        waitFor(appearingOfWindowByPartialTitle(fullTitle));
    }

    @Override
    public void waitForNewPopUpWindowAndSwitchToIt(WebElement webElement) {
        final Set<String> currentWindowHandles = driver.getWindowHandles();
        webElement.click();
        final String windowHandle = waitFor(appearingOfWindowWithNewTitle(currentWindowHandles));
        driver.switchTo().window(windowHandle);
    }

    @Override
    public String waitForNewPopUpWindow(Set<String> currentWindowHandles) {
        return waitFor(appearingOfWindowWithNewTitle(currentWindowHandles), 3);
    }

    @Override
    public void closeCurrentBrowserWindow() {
        driver.close();
    }

    @Override
    public void switchToLastWindow() {
        Iterator<String> iterator = driver.getWindowHandles().iterator();
        String window = null;
        while (iterator.hasNext()) {
            window = iterator.next();
        }
        driver.switchTo().window(window);
    }

    @Override
    public void waitForWindowToBeAppearedByUrlAndSwitchToIt(final String url) {
        waitFor(appearingOfWindowByUrl(url));
    }

    @Override
    public void waitUntilOnlyOneWindowIsOpen() {
        waitFor(onlyOneWindowIsOpen());
    }

    @Override
    public void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url) {
        waitFor(appearingOfWindowByPartialUrl(url));
    }

    @Override
    public void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeOutInSeconds) {
        waitFor(appearingOfWindowByPartialUrl(url), timeOutInSeconds);
    }

    @Override
    public Boolean waitForTextToBePresentIn(final WebElement element, final String text) {
        return waitFor(textToBePresentInElement(element, text));
    }

    @Override
    public Boolean waitForTextToBePresentIn(final TextField textField, final String text) {
        return waitFor(textToBePresentInTextField(textField, text));
    }

    @Override
    public Boolean waitForTextToBePresentIn(final WebElement element) {
        return waitFor(textToBePresentInElement(element));
    }

    @Override
    public void waitForPageToLoad() {
        if (((FramesTransparentWebDriver) driver).getWrappedDriver() instanceof AppiumDriver) {
            return;
        }
        try {
            waitFor(new PageLoaded());
        } catch (TimeoutException expected) {
            String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
            LOGGER.error("*****ERROR***** TimeoutException occurred while waiting for page to load! return document.readyState value is '" + readyState + "' But expected to be 'complete'");
        } catch (WebDriverException e) {
            LOGGER.error("*****ERROR***** WebDriverException occurred while waiting for page to load!");
        }
    }

    @Override
    public void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeOutInSeconds) {
        waitFor(ExpectedConditions2.presenceOfElementCount(locator, expectedNumberOfElements), timeOutInSeconds);
    }

    @Override
    public Boolean waitForInvisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Override
    public WebElement waitForInvisibilityOfElement(final WebElement element) {
        return waitFor(ExpectedConditions2.invisibleOf(element));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfFirstElements(locator));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        throw new RuntimeException("REPLACE WITH NEW APPROACH IMMEDIATELY! METHOD IMPLEMENTATION WAD DELETED! CONTACT Vladimir Efimov vefimov@wiley.com");

//        return waitFor(ExpectedConditions2.visibilityOfFirstElements(searchContext, locator));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator, long timeOutInSeconds) {
        throw new RuntimeException("REPLACE WITH NEW APPROACH IMMEDIATELY! METHOD IMPLEMENTATION WAD DELETED! CONTACT Vladimir Efimov vefimov@wiley.com");

        //        return waitFor(ExpectedConditions2.visibilityOfFirstElements(searchContext, locator), timeOutInSeconds);
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator, long timeOutInSeconds) {
        return waitFor(ExpectedConditions2.visibilityOfFirstElements(locator), timeOutInSeconds);
    }

    @Override
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeOutInSeconds) {
        return waitFor(visibilityOfElementLocated(locator), timeOutInSeconds);
    }

    @Override
    public Boolean waitForElementNotChangeXLocation(final WebElement webElement) {
        return waitFor(xLocationNotChanged(webElement));
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        return waitFor(attributeContainsValue(element, attributeName, attributeValue));
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeOutInSeconds) {
        return waitFor(attributeContainsValue(element, attributeName, attributeValue), timeOutInSeconds);
    }

    @Override
    public Boolean waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        return waitFor(attributeNotContainsValue(element, attributeName, attributeValue));
    }

    @Override
    public Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName) {
        return waitFor(elementHasAttribute(element, attributeName));
    }

    @Override
    public Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        return waitFor(elementHasAttribute(element, attributeName), timeOutInSeconds);
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        return waitFor(elementDoesNotHaveAttribute(element, attributeName));
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds) {
        return waitFor(elementDoesNotHaveAttribute(element, attributeName), timeOutInSeconds);
    }

    @Override
    public void setTimeout(final long timeOutInSeconds) {
        wait = new WebDriverWait(driver, timeOutInSeconds);
    }

    private <T> T waitFor(final ExpectedCondition<T> condition) {
        waitForMobile();
        return wait.pollingEvery(POOLLING_EVERY_DURATION_IN_SEC, TimeUnit.SECONDS).until(condition);
    }

    private <T> T waitFor(final ExpectedCondition<T> condition, final long timeOutInSeconds) {
        WebDriverWait customWait = new WebDriverWait(driver, timeOutInSeconds);
        waitForMobile();
        return customWait.until(condition);
    }

    private void waitForMobile() {
        if (SeleniumHolder.getAppiumDriver() != null) {
            TestUtils.waitForSomeTime(waitTimeout, "Default sleep for all mobile operations. In 2017 it's needed to make appium tests more stable");
            try {
                driver.getPageSource();
            } catch (Exception ignored) {
            }
        }
    }
}
