package com.wiley.autotest.framework.tests.conditions;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.ConditionsPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class ElementsDisplayedTest extends BaseTest {

    @Autowired
    private ConditionsPage page;

    @Test
    public void test() {
        openPage("conditions.html");
        page.checkElementsReturnAllVisibleElements();
    }
}
