package com.wiley.elements.custom;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElement;
import com.wiley.elements.find.VisibleElementLookUp;
import com.wiley.elements.types.TeasyElementList;
import com.wiley.holders.DriverHolder;
import org.openqa.selenium.By;

/**
 * Don't forget to add javadoc for a class!
 */
public final class Select {
    private final TeasyElement el;

    public Select(TeasyElement el) {
        this.el = el;
    }

    public Select(By locator) {
        this(new VisibleElementLookUp(DriverHolder.getDriver(),
                new SearchStrategy()).find(locator));
    }

    public void selectByIndex(int index) {
        //add code to check that the option is not ALREADY selected
        getOptions().get(index).click();
    }

    public void selectByVisibleText(String text) {
        for (TeasyElement el : getOptions()) {
            if (el.getText().equals(text)) {
                el.click();
                break;
            }
        }
    }

    private TeasyElementList getOptions() {
        return this.el.elements(By.tagName("option"));
    }
}
