package com.wiley.autotest.framework.tests.conditions;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.ConditionsPage;
import org.testng.annotations.Test;

public class ElementsDisplayedTest extends BaseUnitTest {

    @Test
    public void test() {
        openPage("conditions.html", ConditionsPage.class)
                .checkElementsReturnAllVisibleElements();
    }
}
