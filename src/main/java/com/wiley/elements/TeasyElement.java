package com.wiley.elements;

import com.wiley.elements.should.Should;
import com.wiley.elements.types.Locatable;
import com.wiley.elements.types.TeasyElementList;
import com.wiley.elements.waitfor.ElementWaitFor;
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
     * use {@link #elements(By) or {@link #domElements(By)}
     */
    @Override
    @Deprecated
    List<WebElement> findElements(By by);

    /**
     * Do not use this method. it does not fit TeasyElement concept and is only here because
     * it is originally a method of WebElement
     * use {@link #element(By) or {@link #domElements(By))}
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
     * Gets {@link Locatable} interface
     * Used for interactions with location of an element
     *
     * @return instance of {@link Locatable}
     */
    Locatable getLocatable();

    /**
     * {@link #should(SearchStrategy)} with default {@link SearchStrategy}
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
     * {@link #waitFor(SearchStrategy)} with default {@link SearchStrategy}
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
     * {@link #element(By, SearchStrategy)} with default {@link SearchStrategy}
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
     * {@link #elements(By, SearchStrategy)} with default {@link SearchStrategy}
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
     * {@link #domElement(By, SearchStrategy)} with default {@link SearchStrategy}
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
     * {@link #domElements(By, SearchStrategy)} with default {@link SearchStrategy}
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
     * @return {@link TeasyElementList}
     */
    TeasyElementList domElements(By by, SearchStrategy strategy);
}
