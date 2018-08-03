package com.wiley.autotest.framework.tests.repeater;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.RepeaterPage;
import com.wiley.actions.StopTestExecutionException;
import org.testng.annotations.Test;

public class RepeaterTest extends BaseUnitTest {

    @Test()
    public void performAction() {
        openPage("repeater.html", RepeaterPage.class)
                .positiveCheck();
    }

    @Test(expectedExceptions = StopTestExecutionException.class)
    public void unableToPerformAction() {
        openPage("repeater.html", RepeaterPage.class)
                .negativeCheck();
    }
}
