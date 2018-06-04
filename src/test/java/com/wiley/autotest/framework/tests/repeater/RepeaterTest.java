package com.wiley.autotest.framework.tests.repeater;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.RepeaterPage;
import com.wiley.autotest.services.StopTestExecutionException;
import org.testng.annotations.Test;

public class RepeaterTest extends BaseTest {

    @Test()
    public void performAction() {
        openPage("repeater.html", RepeaterPage.class)
                .positiveCheck();
    }

    @Test()
    public void performActionWithOutput() {
        openPage("repeater.html", RepeaterPage.class)
                .outputCheck();
    }

    @Test(expectedExceptions = StopTestExecutionException.class)
    public void unableToPerformAction() {
        openPage("repeater.html", RepeaterPage.class)
                .negativeCheck();
    }

}
