package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.should.Should;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.ElementWaitFor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Teasy representation of a {@link WebElement}
 */
public interface TeasyElement extends WebElement {

    /**
     * Do not use this method. it does not fit TeasyElement concept and is only here because
     * it is originally a method of WebElement.
     * use elements() or domElements(), depending on your needs
     */
    @Override
    @Deprecated
    List<WebElement> findElements(By by);

    /**
     * Do not use this method. it does not fit TeasyElement concept and is only here because
     * it is originally a method of WebElement
     * use element() or domElement() depending on your needs
     */
    @Override
    @Deprecated
    WebElement findElement(By by);

    /**
     * Gets the original web element
     *
     * @return pure {@link WebElement}
     */
    WebElement getWrappedWebElement();

    /**
     * Tells you whether element is stale (i.e. was detached from DOM)
     *
     * @return true - in case of a stale element, false - otherwise
     */
    boolean isStale();

    /**
     * Gets {@link Locator} interface
     * Used for interactions with location of an element
     *
     * @return instance of {@link Locator}
     */
    Locator getLocator();

    /**
     * Calls assertion engine with default settings
     *
     * @return instance of {@link Should}
     */
    Should should();

    /**
     * Calls assertion engine with custom settings passed via {@link SearchStrategy}
     *
     * @param strategy - custom settings for assertion
     * @return instance of {@link Should}
     */
    Should should(SearchStrategy strategy);

    /**
     * Calls waiting engine with default settings
     *
     * @return instance of {@link ElementWaitFor}
     */
    ElementWaitFor waitFor();

    /**
     * Calls waiting engine with custom settings passed via {@link SearchStrategy}
     *
     * @param strategy - custom settings for waiting
     * @return instance of {@link ElementWaitFor}
     */
    ElementWaitFor waitFor(SearchStrategy strategy);

    /**
     * Gets parent element
     *
     * @return - {@link TeasyElement}
     */
    TeasyElement getParent();

    /**
     * Gets parent element of N-th level
     * For the <el3> from below if we call getParent(2)
     * will return TeasyElement for <el1>
     * <el>
     * <el1>
     * <el2>
     * <el3>example DOM structure</el3>
     * </el2>
     * </el1>
     * </el>
     *
     * @param level - levels up of a parenting
     * @return - {@link TeasyElement}
     */
    TeasyElement getParent(int level);

    /**
     * Finds first visible element using locator {@link By}
     * and default search strategy
     *
     * @param by - locator
     * @return - first found visible element
     */
    TeasyElement element(By by);

    /**
     * Finds first visible element using locator {@link By}
     * and custom search strategy {@link SearchStrategy}
     *
     * @param by       - locator
     * @param strategy - custom search strategy
     * @return - {@link TeasyElement}
     */
    TeasyElement element(By by, SearchStrategy strategy);

    /**
     * Finds first found visible elements using locator {@link By}
     *
     * @param by - locator
     * @return - {@link TeasyElementList}
     */
    TeasyElementList elements(By by);

    /**
     * Finds visible elements using locator {@link By}
     * and custom search strategy {@link SearchStrategy}
     *
     * @param by       - locator
     * @param strategy - custom search strategy
     * @return - {@link TeasyElementList}
     */
    TeasyElementList elements(By by, SearchStrategy strategy);

    /**
     * Finds first dom element using locator {@link By}
     * note:
     * dom element is just an element present in dom
     * which is not necessarily visible
     *
     * @param by - locator
     * @return - {@link TeasyElement}
     */
    TeasyElement domElement(By by);

    /**
     * Finds first dom element using locator {@link By}
     * and custom search strategy {@link SearchStrategy}
     * note:
     * dom element is just an element present in dom
     * which is not necessarily visible
     *
     * @param by       - locator
     * @param strategy - custom search strategy
     * @return - {@link TeasyElement}
     */
    TeasyElement domElement(By by, SearchStrategy strategy);

    /**
     * Finds first found dom elements using locator {@link By}
     * note:
     * dom element is just an element present in dom
     * which is not necessarily visible
     *
     * @param by - locator
     * @return {@link TeasyElement}
     */
    TeasyElementList domElements(By by);

    /**
     * Finds dom elements using locator {@link By}
     * and custom search strategy {@link SearchStrategy}
     * note:
     * dom element is just an element present in dom
     * which is not necessarily visible
     *
     * @param by       - locator
     * @param strategy - custom search strategy
     * @return
     */
    TeasyElementList domElements(By by, SearchStrategy strategy);
}
