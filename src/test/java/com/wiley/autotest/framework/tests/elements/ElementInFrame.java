package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;


public class ElementInFrame extends BaseTest {

    @Autowired
    private TestElementPage testElementPage;

    @Test
    public void test() {
        openPage("mainTestElement.html");
        testElementPage.checkElementInFrame();
    }

}
