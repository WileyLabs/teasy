package com.wiley.page;

import com.wiley.elements.*;
import com.wiley.elements.find.*;
import com.wiley.elements.types.NullTeasyElement;
import com.wiley.elements.types.TeasyElementList;
import com.wiley.elements.waitfor.CustomWaitFor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import static com.wiley.holders.DriverHolder.getDriver;

/**
 * Representation of an abstract block of a Page.
 * Basically it is any area of a page located inside of a mainElement.
 */
public abstract class AbstractBlock {

    private final TeasyElement mainElement;

    public AbstractBlock(TeasyElement element) {
        //in case not-found-element is passed it does not make sense to create new block
        mainElement = element;
        if (element instanceof NullTeasyElement) {
            throwException();
        }
    }

    /**
     * Gets main element for given block
     *
     * @return TeasyElement representing main element
     */
    protected TeasyElement getMainElement() {
        return mainElement;
    }

    public CustomWaitFor waitFor() {
        return waitFor(new SearchStrategy());
    }

    public CustomWaitFor waitFor(SearchStrategy strategy) {
        return new CustomWaitFor(strategy);
    }

    public TeasyElement element(final By locator) {
        return element(locator, new SearchStrategy());
    }

    public TeasyElement element(final By locator, SearchStrategy strategy) {
        return new VisibleElementLookUp(getDriver(), strategy, mainElement).find(locator);
    }

    public TeasyElementList elements(final By locator) {
        return elements(locator, new SearchStrategy());
    }

    public TeasyElementList elements(final By locator, SearchStrategy strategy) {
        return new VisibleElementsLookUp(getDriver(), strategy, mainElement).find(locator);
    }

    public TeasyElement domElement(By locator) {
        return domElement(locator, new SearchStrategy());
    }

    public TeasyElement domElement(By locator, SearchStrategy strategy) {
        return new DomElementLookUp(getDriver(), strategy, mainElement).find(locator);
    }

    public TeasyElementList domElements(By locator) {
        return domElements(locator, new SearchStrategy());
    }

    public TeasyElementList domElements(By locator, SearchStrategy strategy) {
        return new DomElementsLookUp(getDriver(), strategy, mainElement).find(locator);
    }

    public Alert alert() {
        return alert(new SearchStrategy());
    }

    public Alert alert(SearchStrategy strategy) {
        return new AlertLookUp(getDriver(), strategy).find();
    }

    public Window window() {
        return new TeasyWindow(getDriver());
    }

    private void throwException() {
        throw new NoSuchElementException("Failed to create Block. Unable to find main element of a block with locator '" + mainElement
                .getLocatable()
                .getBy() + "'");
    }
}

