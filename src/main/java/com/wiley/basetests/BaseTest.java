package com.wiley.basetests;

import com.wiley.assertions.MethodType;
import com.wiley.assertions.SoftAssert;
import com.wiley.config.InitConfigListener;
import com.wiley.holders.AssertionsHolder;
import com.wiley.holders.TestParamsHolder;
import org.testng.*;
import org.testng.annotations.Listeners;

/**
 * Abstract base class for a TestNG tests
 * Should be extended by all tests in a project
 *
 * It processes failed assertions and calls
 * {@link SoftAssert#assertAll()}
 */
@Listeners(InitConfigListener.class)
public abstract class BaseTest implements IConfigurable, IHookable {

    private static final Object SYNC = new Object();

    @Override
    public void run(IConfigureCallBack callBack, ITestResult testResult) {
        synchronized (SYNC) {
            AssertionsHolder.setSoftAssert(new SoftAssert());

            TestParamsHolder.setTestName(testResult.getName());
        }

        callBack.runConfigurationMethod(testResult);

        synchronized (SYNC) {
            if (testResult.getThrowable() != null) {
                setThrowable(testResult, MethodType.CONFIG);
            }
        }
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        synchronized (SYNC) {
            if (AssertionsHolder.softAssert() == null
                    || (TestParamsHolder.getTestName() != null
                    && TestParamsHolder.getTestName().equals(testResult.getName()))) {
                AssertionsHolder.setSoftAssert(new SoftAssert());
            }

            TestParamsHolder.setTestName(testResult.getName());
        }

        callBack.runTestMethod(testResult);

        synchronized (SYNC) {
            if (testResult.getThrowable() != null) {
                setThrowable(testResult, MethodType.TEST);
            }

            AssertionsHolder.softAssert().assertAll();
        }
    }

    protected abstract void setThrowable(ITestResult testResult, MethodType methodType);
}
