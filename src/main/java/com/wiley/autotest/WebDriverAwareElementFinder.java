package com.wiley.autotest;

import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.selenium.elements.TextField;
import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class WebDriverAwareElementFinder implements ElementFinder {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverAwareElementFinder.class);

    public WebDriverAwareElementFinder(final WebDriver webDriver, final WebDriverWait webDriverWait) {
        this.driver = webDriver;
        this.wait = webDriverWait;
    }

    private FramesTransparentWebDriver getFrameTransparentWebDriver() {
        return (FramesTransparentWebDriver) ((EventFiringWebDriver) driver).getWrappedDriver();
    }

    @Override
    public void waitForCondition(ExpectedCondition<Boolean> condition, long timeout) {
        waitFor(condition, timeout);
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

    @Override
    public List<WebElement> findElementsByInFrames(final By locator) {
        return getFrameTransparentWebDriver().findElementsInFrames(locator);
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedByInFrames(locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedByInFrames(locator));
    }

    @Override
    public Boolean waitForTextToBePresentInElement(final By locator) {
        return waitFor(ExpectedConditions2.textToBePresentInElement(locator));
    }

    @Override
    public Boolean waitForAbsenceOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.absenceElementBy(locator));
    }

    @Override
    public void waitForWindowToBeAppearedAndSwitchToIt(final String title) {
        waitFor(ExpectedConditions2.appearingOfWindowAndSwitchToIt(title));
    }

    @Override
    public void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(final String fullTitle) {
        waitFor(ExpectedConditions2.appearingOfWindowByPartialTitle(fullTitle));
    }

    @Override
    public void waitForNewPopUpWindowAndSwitchToIt(WebElement webElement) {
        final Set<String> currentWindowHandles = driver.getWindowHandles();
        webElement.click();
        final String windowHandle = waitFor(ExpectedConditions2.appearingOfWindowWithNewTitle(currentWindowHandles));
        driver.switchTo().window(windowHandle);
    }

    @Override
    public String waitForNewPopUpWindow(Set<String> currentWindowHandles) {
        return waitFor(ExpectedConditions2.appearingOfWindowWithNewTitle(currentWindowHandles), 3);
    }

    @Override
    public void waitForWindowToBeAppearedByUrlAndSwitchToIt(final String url) {
        waitFor(ExpectedConditions2.appearingOfWindowByUrl(url));
    }

    @Override
    public void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url) {
        waitFor(ExpectedConditions2.appearingOfWindowByPartialUrl(url));
    }

    @Override
    public void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeoutInSec) {
        waitFor(ExpectedConditions2.appearingOfWindowByPartialUrl(url), timeoutInSec);
    }

    @Override
    public Boolean waitForTextToBePresentIn(final WebElement element) {
        return waitFor(ExpectedConditions2.textToBePresentInElement(element));
    }

    @Override
    public WebElement waitForInvisibilityOfElement(final WebElement element) {
        return waitFor(ExpectedConditions2.invisibleOf(element));
    }

    @Override
    public void waitForPageToLoad() {
        try {
            try {
                waitFor(ExpectedConditions2.pageToLoad());
            } catch (TimeoutException expected) {
                String readyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").toString();
                LOGGER.error("*****ERROR***** TimeoutException occurred while waiting for page to load! return document.readyState value is '" + readyState + "' But expected to be 'complete'");
            } catch (WebDriverException e) {
                LOGGER.error("*****ERROR***** WebDriverException occurred while waiting for page to load!");
            }
        } catch (Throwable unexpectedThrowable) {
            //TODO this catch should be removed by September 1 2014! Adding it to find out the reason of browsers being not closed after test
            LOGGER.error("*****ERROR*****!!!*****ERROR*****Throwable occurred was throws during waiting for page to load!", unexpectedThrowable);
        }
    }

    @Override
    public void waitForListToLoad() {
        List<WebElement> loading = findElementsBy(By.cssSelector(".loading"));
        try {
            if (!loading.isEmpty() && !(loading.size() == 1 && loading.get(0).getText().isEmpty())) {
                waitForStalenessOf(loading.get(0), 30);
            }
        } catch (StaleElementReferenceException ignored) {
            LOGGER.info("****StaleElementReferenceException occurs in waitForListToLoad****");
        }
        try {
            if (findElementsBy(By.xpath("//*[text()='Assignment Policies' or text()='Assignment Name & Description']")).isEmpty()) {
                waitFor(ExpectedConditions2.isListLoaded(), 30);
            }
        } catch (WebDriverException ignored) {
            LOGGER.error("*****WebDriverException occurs in waitForListToLoad****");
        }
    }

    @Override
    public void waitUntilOnlyOneWindowIsOpen() {
        waitFor(ExpectedConditions.numberOfWindowsToBe(1));
    }

    @Override
    public boolean isTextPresentInElement(final By locator, final String text) {
        return ExpectedConditions.textToBePresentInElementLocated(locator, text).apply(driver);
    }

    @Override
    public Boolean waitForTextToBePresentIn(final WebElement element, final String text) {
        return waitFor(ExpectedConditions.textToBePresentInElement(element, text));
    }

    @Override
    public Boolean waitForTextToBePresentIn(final TextField textField, final String text) {
        return waitFor(ExpectedConditions.textToBePresentInElement(textField.getWrappedWebElement(), text));
    }

    @Override
    public Boolean waitForTextToBePresentInElement(final By locator, final String text) {
        return waitFor(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Override
    public WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeout) {
        return waitFor(ExpectedConditions.presenceOfElementLocated(locator), timeout);
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeoutInSec) {
        return waitFor(ExpectedConditions.presenceOfAllElementsLocatedBy(locator), timeoutInSec);
    }

    @Override
    public WebElement waitForPresenceOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Override
    public void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeout) {
        waitFor(ExpectedConditions.numberOfElementsToBe(locator, expectedNumberOfElements), timeout);
    }

    @Override
    public void waitForVisibilityOf(final WebElement element) {
        waitFor(ExpectedConditions.visibilityOf(element));
    }

    @Override
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedBy(locator));
    }

    @Override
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeout) {
        return waitFor(ExpectedConditions.visibilityOfElementLocated(locator), timeout);
    }

    @Override
    public WebElement waitForElementToBeClickable(final By locator) {
        return waitFor(ExpectedConditions.elementToBeClickable(locator));
    }

    @Override
    public void waitForElementToBeClickable(WebElement element) {
        waitFor(ExpectedConditions.elementToBeClickable(element));
    }

    @Override
    public Boolean waitForInvisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Override
    public Boolean waitForStalenessOf(final WebElement webElement) {
        return waitFor(ExpectedConditions.stalenessOf(webElement));
    }

    @Override
    public Boolean waitForStalenessOf(final WebElement webElement, final long timeout) {
        return waitFor(ExpectedConditions.stalenessOf(webElement), timeout);
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        return waitFor(ExpectedConditions.attributeContains(element, attributeName, attributeValue));
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeout) {
        return waitFor(ExpectedConditions.attributeContains(element, attributeName, attributeValue), timeout);
    }

    @Override
    public Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName) {
        return waitFor(ExpectedConditions.attributeToBeNotEmpty(element, attributeName));
    }

    @Override
    public Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        return waitFor(ExpectedConditions.attributeToBeNotEmpty(element, attributeName), timeout);
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        return waitFor(ExpectedConditions.not(ExpectedConditions.attributeToBeNotEmpty(element, attributeName)));
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        return waitFor(ExpectedConditions.not(ExpectedConditions.attributeToBeNotEmpty(element, attributeName)), timeout);
    }

    @Override
    public Boolean waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        return waitFor(ExpectedConditions.not(ExpectedConditions.attributeContains(element, attributeName, attributeValue)));
    }

    @Override
    public void setTimeout(final long timeout) {
        wait = new WebDriverWait(driver, timeout);
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

    private <T> T waitFor(final ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    private <T> T waitFor(final ExpectedCondition<T> condition, final long timeout) {
        WebDriverWait customWait = new WebDriverWait(driver, timeout);
        return customWait.until(condition);
    }
}
