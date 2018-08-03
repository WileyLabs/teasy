package com.wiley.basetests;

import com.wiley.assertions.MethodType;
import com.wiley.holders.AssertionsHolder;
import com.wiley.page.BasePage;
import com.wiley.page.PageProvider;
import org.testng.ITestResult;

import static com.wiley.driver.WebDriverFactory.initDriver;

/**
 * Base test for Appium tests
 */
public class MobileBaseTest extends BaseTest {

    protected <T extends BasePage> T openApp(Class<T> page) {
        initDriver();
        return PageProvider.get(page);
    }

    protected void setThrowable(ITestResult testResult, MethodType methodType) {
        final Throwable testResultThrowable = testResult.getThrowable();

        AssertionsHolder.softAssert().add(testResultThrowable, methodType);
    }
}
