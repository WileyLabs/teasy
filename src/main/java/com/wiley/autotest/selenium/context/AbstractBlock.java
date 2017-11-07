package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.By;

/**
 * Representation of an abstract block of a Page.
 * Basically it is any area of a page located inside of a mainElement.
 */
public abstract class AbstractBlock extends TeasyElementProvider {

    protected final TeasyElement mainElement;

    public AbstractBlock(TeasyElement element) {
        mainElement = element;
    }

    public AbstractBlock(By locator) {
        mainElement = element(locator);
    }
}

