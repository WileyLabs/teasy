package com.wiley.autotest.framework.tests.conditions;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.ConditionsPage;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

public class EmptyElementsList extends BaseUnitTest {

    @Test(expectedExceptions = NoSuchElementException.class)
    public void exceptionShouldBeThrowsForEmptyListAndConditionCall() {
        openPage("emptyList.html", ConditionsPage.class)
                .callConditionForEmptyList();
    }

    @Test
    public void noExceptionShouldBeThrownForEmptyListAndAbsent() {
        openPage("emptyList.html", ConditionsPage.class)
                .callAbsentConditionForEmptyList();
    }
}
