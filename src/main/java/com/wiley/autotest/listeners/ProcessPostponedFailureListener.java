package com.wiley.autotest.listeners;

import com.wiley.autotest.event.postpone.failure.PostponedFailureException;
import com.wiley.autotest.screenshots.ScreenshotInfo;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.util.List;

import static com.wiley.autotest.screenshots.ScreenShotsPathsHolder.getScreenShotPathsForTest;
import static com.wiley.autotest.utils.TestUtils.getTestName;
import static java.lang.String.format;
import static org.testng.ITestResult.FAILURE;

public class ProcessPostponedFailureListener implements IInvokedMethodListener2 {

    @Override
    public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult, final ITestContext context) {
    }

    @Override
    public void afterInvocation(final IInvokedMethod method, final ITestResult testResult, final ITestContext context) {
        final String testName = getTestName(testResult);
        final Boolean isTestFailed = (Boolean) context.getAttribute(format("%s postpone fail", testName));
        final Boolean isBeforeGroupFailed = (Boolean) context.getAttribute(format("%s postpone fail", "E4BeforeGroups"));
        if ((isTestFailed != null && isTestFailed) || (isBeforeGroupFailed != null && isBeforeGroupFailed)) {
            testResult.setStatus(FAILURE);
            testResult.setThrowable(new PostponedFailureException(getErrorMessage(testName)));
        }
    }

    private static String getErrorMessage(final String testName) {
        final StringBuilder result = new StringBuilder();
        final List<ScreenshotInfo> screenshotInfoList = getScreenShotPathsForTest(testName);
        for (ScreenshotInfo screenshotInfo : screenshotInfoList) {
            result.append(screenshotInfo.getMessage()).append("\n");
        }

        return result.toString();
    }

    @Override
    public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {
    }

    @Override
    public void afterInvocation(final IInvokedMethod method, final ITestResult testResult) {
    }
}
