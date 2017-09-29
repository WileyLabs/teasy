package com.wiley.autotest.framework.tests.domElements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestDomElementPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by shekhavtsov on 20/07/2017.
 */
public class DomElementsReturnAllElements extends BaseTest {

    @Autowired
    private TestDomElementPage testDomElementPage;

    @Test
    public void test() {
        openPage("mainTestElement.html");
        testDomElementPage.checkAnimationDomElement();
    }
}
