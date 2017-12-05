package com.wiley.autotest.framework.tests.repeater;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.RepeaterPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

public class RepeaterTest extends BaseTest {

    @Autowired
    private RepeaterPage page;

    @Test(expectedExceptions = AssertionError.class)
    public void unableToPerformAction() {
        openPage("repeater.html");
        page.negativeCheck();
    }

    @Test()
    public void performAction() {
        openPage("repeater.html");
        page.positiveCheck();
    }
}
