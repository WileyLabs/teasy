package com.wiley.basetests;

import com.wiley.assertions.MethodType;
import com.wiley.holders.AssertionsHolder;
import com.wiley.page.BasePage;
import com.wiley.page.PageProvider;
import org.testng.ITestResult;

import static com.wiley.driver.WebDriverFactory.initDriver;

/**
 * Base test for Selenium tests
 */
public class SeleniumBaseTest extends BaseTest {

    protected <T extends BasePage> T get(Class<T> page) {
        return PageProvider.get(page);
    }

    protected <T extends BasePage> T get(Class<T> page, String url) {
        initDriver();
        return PageProvider.get(page, url);
    }

    protected void setThrowable(ITestResult testResult, MethodType methodType) {
        final Throwable testResultThrowable = testResult.getThrowable();

        AssertionsHolder.softAssert().add(testResultThrowable, MethodType.TEST);
    }
}
