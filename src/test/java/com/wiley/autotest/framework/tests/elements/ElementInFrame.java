package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.testng.annotations.Test;

public class ElementInFrame extends BaseTest {

    @Test
    public void test() {
        openPage("mainTestElement.html");
        getPage(TestElementPage.class)
                .checkElementInFrame();
    }
}
