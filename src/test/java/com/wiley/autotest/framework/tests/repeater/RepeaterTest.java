package com.wiley.autotest.framework.tests.repeater;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.RepeaterPage;
import com.wiley.autotest.services.StopTestExecutionException;
import org.testng.annotations.Test;

public class RepeaterTest extends BaseTest {

    @Test()
    public void performAction() {
        openPage("repeater.html");
        getPage(RepeaterPage.class)
                .positiveCheck();
    }

    @Test(expectedExceptions = StopTestExecutionException.class)
    public void unableToPerformAction() {
        openPage("repeater.html");
        getPage(RepeaterPage.class)
                .negativeCheck();
    }
}
