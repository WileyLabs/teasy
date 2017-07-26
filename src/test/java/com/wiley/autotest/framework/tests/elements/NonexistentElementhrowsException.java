package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.openqa.selenium.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by shekhavtsov on 21/07/2017.
 */
public class NonexistentElementhrowsException extends BaseTest {

    @Autowired
    private TestElementPage testElementPage;

    @Test(expectedExceptions = TimeoutException.class)
    public void test() {
        openPage("mainTestElement.html");
        testElementPage.checkElementNotFound();
    }
}
