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

import static com.wiley.autotest.ExpectedConditions2.*;
import static com.wiley.autotest.ExpectedConditions2.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

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
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfElementLocatedBy(locator));
    }

    @Override
    public void waitForCondition(ExpectedCondition<Boolean> condition, long timeout) {
        waitFor(condition, timeout);
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
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedBy(locator));
    }

    @Override
    public List<WebElement> waitForPresenceOfAllElementsLocatedBy(final By locator, long timeoutInSec) {
        return waitFor(ExpectedConditions2.presenceOfAllElementsLocatedBy(locator), timeoutInSec);
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
    public WebElement waitForPresenceOfElementLocatedBy(final By locator, long timeout) {
        return waitFor(presenceOfElementLocated(locator), timeout);
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
    public Boolean waitForStalenessOf(final WebElement webElement, final long timeout) {
        return waitFor(stalenessOf(webElement), timeout);
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
    public void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(final String url, long timeoutInSec) {
        waitFor(appearingOfWindowByPartialUrl(url), timeoutInSec);
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
    public void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeout) {
        waitFor(ExpectedConditions2.presenceOfElementCount(locator, expectedNumberOfElements), timeout);
    }

    @Override
    public WebElement waitForInvisibilityOfElementLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.invisibleOf(locator));
    }

    @Override
    public WebElement waitForInvisibilityOfElement(final WebElement element) {
        return waitFor(ExpectedConditions2.invisibleOf(element));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedBy(locator));
    }

    @Override
    public List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator) {
        return waitFor(ExpectedConditions2.visibilityOfAllElementsLocatedBy(searchContext, locator));
    }

    @Override
    public WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeout) {
        return waitFor(visibilityOfElementLocated(locator), timeout);
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue) {
        return waitFor(attributeContainsValue(element, attributeName, attributeValue));
    }

    @Override
    public Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeout) {
        return waitFor(attributeContainsValue(element, attributeName, attributeValue), timeout);
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
    public Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        return waitFor(elementHasAttribute(element, attributeName), timeout);
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName) {
        return waitFor(elementDoesNotHaveAttribute(element, attributeName));
    }

    @Override
    public Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeout) {
        return waitFor(elementDoesNotHaveAttribute(element, attributeName), timeout);
    }

    private <T> T waitFor(final ExpectedCondition<T> condition) {
        return wait.until(condition);
    }

    private <T> T waitFor(final ExpectedCondition<T> condition, final long timeout) {
        WebDriverWait customWait = new WebDriverWait(driver, timeout);
        return customWait.until(condition);
    }

    @Override
    public void setTimeout(final long timeout) {
        wait = new WebDriverWait(driver, timeout);
    }
}
