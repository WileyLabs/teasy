package com.wiley.autotest;

import com.wiley.autotest.selenium.elements.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.Set;

public interface ElementFinder {
    void waitForCondition(ExpectedCondition<Boolean> condition, long timeOutInSeconds);

    WebElement findElementBy(By locator);
    WebElement findElementBy(SearchContext searchContext, By locator);
    List<WebElement> findElementsBy(By locator);
    List<WebElement> findElementsBy(SearchContext searchContext, final By locator);

    List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator);
    List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator);
    List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final SearchContext searchContext, final By locator, long timeOutInSeconds);
    List<WebElement> waitForVisibilityOfAllElementsLocatedBy(final By locator, final long timeOutInSeconds);
    WebElement waitForVisibilityOfElementLocatedBy(final By locator);
    WebElement waitForVisibilityOfElementLocatedBy(final By locator, final long timeOutInSeconds);
    void waitForVisibilityOf(WebElement element);
    List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator);
    List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator, final long timeOutInSeconds);
    List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator);
    List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator, final long timeOutInSeconds);

    WebElement waitForPresenceOfElementLocatedBy(By locator);
    WebElement waitForPresenceOfElementLocatedBy(By locator, long timeOutInSeconds);
    WebElement waitForPresenceOfElementLocatedBy(final SearchContext searchContext, final By locator);
    List<WebElement> waitForPresenceOfAllElementsLocatedBy(By locator, long timeOutInSeconds);
    List<WebElement> waitForPresenceOfAllElementsLocatedBy(By locator);
    List<WebElement> waitForPresenceOfAllElementsLocatedBy(final SearchContext searchContext, final By locator);
    void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeOutInSeconds);

    Boolean waitForTextToBePresentInElement(By locator, String text);
    Boolean waitForTextToBePresentInElement(By locator);
    Boolean waitForTextToBePresentIn(WebElement element, String text);
    Boolean waitForTextToBePresentIn(TextField textField, String text);
    Boolean waitForTextToBePresentIn(WebElement element);

    Boolean waitForAbsenceOfElementLocatedBy(By locator);
    Boolean waitForInvisibilityOfElementLocatedBy(By locator);
    WebElement waitForInvisibilityOfElement(WebElement element);

    WebElement waitForElementToBeClickable(By locator);
    void waitForElementToBeClickable(WebElement element);

    boolean isTextPresentInElement(By locator, String text);

    Boolean waitForStalenessOf(WebElement webElement);
    Boolean waitForStalenessOf(WebElement webElement, long timeOutInSeconds);

    void switchToLastWindow();

    void waitForWindowToBeAppearedByUrlAndSwitchToIt(String url);
    void waitForWindowToBeAppearedAndSwitchToIt(String title);
    void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(String url, long timeOutInSeconds);
    void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(String url);
    void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(String fullTitle);
    void waitForNewPopUpWindowAndSwitchToIt(WebElement webElement);
    String waitForNewPopUpWindow(Set<String> currentWindowHandles);

    void waitUntilOnlyOneWindowIsOpen();

    void closeCurrentBrowserWindow();

    void setTimeout(long timeOutInSeconds);

    void waitForPageToLoad();

    Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue);
    Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeOutInSeconds);
    Boolean waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue);
    Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName);
    Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds);
    Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName);
    Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeOutInSeconds);
}
