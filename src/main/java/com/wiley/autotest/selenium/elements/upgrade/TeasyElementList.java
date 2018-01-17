package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.should.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Represents list of TeasyElements adding elements-related behavior to standard ArrayList
 */
public class TeasyElementList extends ArrayList<TeasyElement> {

    public TeasyElementList() {
        super();
    }

    public TeasyElementList(@NotNull Collection<? extends TeasyElement> teasyElements) {
        super(teasyElements);
    }

    public ListShould should() {
        return new GeneralListShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }
}
