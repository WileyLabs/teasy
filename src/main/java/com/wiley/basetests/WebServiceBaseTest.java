package com.wiley.basetests;

import com.wiley.assertions.MethodType;
import com.wiley.holders.AssertionsHolder;
import org.testng.ITestResult;

/**
 * Base test for Rest Assured tests
 */
public class WebServiceBaseTest extends BaseTest {

    protected void setThrowable(ITestResult testResult, MethodType methodType) {
        final Throwable testResultThrowable = testResult.getThrowable();

        AssertionsHolder.softAssert().add(testResultThrowable, methodType);
    }
}
