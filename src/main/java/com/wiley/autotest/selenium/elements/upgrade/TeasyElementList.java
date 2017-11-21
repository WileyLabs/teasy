package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.should.GeneralListShould;
import com.wiley.autotest.selenium.elements.upgrade.should.ListShould;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents list of TeasyElements adding elements-related behavior to standard ArrayList
 */
public class TeasyElementList extends ArrayList<TeasyElement> {

    public TeasyElementList(@NotNull Collection<? extends TeasyElement> c) {
        super(c);
    }

    public ListShould should() {
        return new GeneralListShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }
}
