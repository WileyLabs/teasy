package com.wiley.autotest.framework.tests.conditions;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.ConditionsPage;
import org.testng.annotations.Test;

public class ElementsDisplayedTest extends BaseTest {

    @Test
    public void test() {
        openPage("conditions.html", ConditionsPage.class)
                .checkElementsReturnAllVisibleElements();
    }
}
