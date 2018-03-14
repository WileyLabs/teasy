package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.*;
import org.openqa.selenium.NoSuchElementException;

/**
 * Representation of an abstract block of a Page.
 * Basically it is any area of a page located inside of a mainElement.
 */
public abstract class AbstractBlock extends TeasyElementProvider {

    private final TeasyElement mainElement;

    public AbstractBlock(TeasyElement element) {
        //in case not-found-element is passed it does not make sense to create new block
        mainElement = element;
        if (element instanceof NullTeasyElement) {
            throwException();
        }
    }

    private TeasyElementFinder finder;

    /**
     * Overriding default behavior to make it search only within context of a block
     * <p>
     * do not call this methods directly. it's only needed for inner logic
     */
    @Override
    protected final TeasyElementFinder customFinder(SearchStrategy strategy) {
        return new TeasyElementFinder(SeleniumHolder.getWebDriver(), strategy, mainElement);
    }

    /**
     * Overriding default behavior to make it search only within context of a block
     * <p>
     * do not call this methods directly. it's only needed for inner logic
     */
    @Override
    protected final TeasyElementFinder finder() {
        if (finder == null) {
            finder = new TeasyElementFinder(SeleniumHolder.getWebDriver(), new SearchStrategy(), mainElement);
        }
        return finder;
    }

    /**
     * Gets main element for given block
     *
     * @return TeasyElement representing main element
     */
    protected TeasyElement getMainElement() {
        return mainElement;
    }

    private void throwException() {
        throw new NoSuchElementException("Failed to create Block. Unable to find main element of a block with locator '" + mainElement.getLocator().getBy() + "'");
    }

}

