package com.wiley.autotest.framework.config;

import com.wiley.actions.StopTestExecutionException;
import com.wiley.assertions.MethodType;
import com.wiley.basetests.SeleniumBaseTest;
import com.wiley.holders.AssertionsHolder;
import com.wiley.page.BasePage;
import org.openqa.selenium.NoSuchElementException;
import org.testng.ITestResult;

/**
 * User: ntyukavkin
 * Date: 19.05.2017
 * Time: 14:17
 */
public class BaseUnitTest extends SeleniumBaseTest {

    public <E extends BasePage> E openPage(String fileName, Class<E> page) {
        return get(page, "file://" + getClass().getResource("/html/framework/").getPath() + fileName);
    }

    protected void setThrowable(ITestResult testResult, MethodType methodType) {
        final Throwable testResultThrowable = testResult.getThrowable();

        if (!(testResultThrowable.getCause() instanceof StopTestExecutionException)
                && !(testResultThrowable.getCause() instanceof NoSuchElementException)) {
            AssertionsHolder.softAssert().add(testResultThrowable, MethodType.TEST);
        }
    }
}
