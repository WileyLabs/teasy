package com.wiley.autotest;

import com.wiley.autotest.selenium.elements.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.Set;

public interface ElementFinder {

    WebElement findElementBy(By locator);

    WebElement findElementBy(SearchContext searchContext, By locator);

    List<WebElement> findElementsBy(By locator);

    List<WebElement> findElementsBy(SearchContext searchContext, final By locator);

    List<WebElement> findElementsByInFrames(final By locator);

    List<WebElement> waitForVisibilityOfAllElementsLocatedByInFrames(final By locator);

    List<WebElement> waitForPresenceOfAllElementsLocatedByInFrames(final By locator);

    void waitForCondition(ExpectedCondition<Boolean> condition, long timeout);

    Boolean waitForTextToBePresentInElement(By locator, String text);

    Boolean waitForTextToBePresentInElement(By locator);

    void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(String url, long timeoutInSec);

    Boolean waitForTextToBePresentIn(WebElement element, String text);

    Boolean waitForTextToBePresentIn(TextField textField, String text);

    Boolean waitForTextToBePresentIn(WebElement element);

    WebElement waitForPresenceOfElementLocatedBy(By locator);

    List<WebElement> waitForPresenceOfAllElementsLocatedBy(By locator);

    Boolean waitForAbsenceOfElementLocatedBy(By locator);

    WebElement waitForPresenceOfElementLocatedBy(By locator, long timeout);

    List<WebElement> waitForPresenceOfAllElementsLocatedBy(By locator, long timeout);

    WebElement waitForVisibilityOfElementLocatedBy(By locator);

    void waitForVisibilityOf(WebElement element);

    Boolean waitForInvisibilityOfElementLocatedBy(By locator);

    WebElement waitForInvisibilityOfElement(WebElement element);

    WebElement waitForVisibilityOfElementLocatedBy(final By locator, long timeout);

    List<WebElement> waitForVisibilityOfAllElementsLocatedBy(By locator);

    WebElement waitForElementToBeClickable(By locator);

    void waitForElementToBeClickable(WebElement element);

    boolean isTextPresentInElement(By locator, String text);

    Boolean waitForStalenessOf(WebElement webElement);

    Boolean waitForStalenessOf(WebElement webElement, long timeout);

    void waitForWindowToBeAppearedAndSwitchToIt(String title);

    void switchToLastWindow();

    void waitForWindowToBeAppearedByUrlAndSwitchToIt(String url);

    void waitUntilOnlyOneWindowIsOpen();

    void waitForWindowToBeAppearedByPartialUrlAndSwitchToIt(String url);

    void waitForWindowToBeAppearedByPartialTitleAndSwitchToIt(String fullTitle);

    void waitForNewPopUpWindowAndSwitchToIt(WebElement webElement);

    String waitForNewPopUpWindow(Set<String> currentWindowHandles);

    void closeCurrentBrowserWindow();

    void setTimeout(long timeout);

    void waitForPageToLoad();

    void waitForListToLoad();

    void waitForPresenceOfElementCount(By locator, int expectedNumberOfElements, long timeout);

    Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue);

    Boolean waitForElementContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue, long timeout);

    Boolean waitForElementNotContainsAttributeValue(final WebElement element, final String attributeName, final String attributeValue);

    Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName);

    Boolean waitForElementContainsAttribute(final WebElement element, final String attributeName, long timeout);

    Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName);

    Boolean waitForElementNotContainsAttribute(final WebElement element, final String attributeName, long timeout);
}
