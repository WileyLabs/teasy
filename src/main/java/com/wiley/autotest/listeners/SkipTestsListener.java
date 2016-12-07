package com.wiley.autotest.listeners;

import com.wiley.autotest.selenium.AbstractSeleniumTest;
import com.wiley.autotest.selenium.Group;
import com.wiley.autotest.spring.Settings;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class SkipTestsListener implements IInvokedMethodListener {

    private static final ClassPathXmlApplicationContext APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(new String[]{"classpath*:/META-INF/spring/context-selenium.xml"});
    private static final Settings SETTINGS = APPLICATION_CONTEXT.getBean(Settings.class);

    private static final String FIREFOX = "firefox";
    private static final String CHROME = "chrome";
    private static final String SAFARI = "safari";
    private static final String IE = "ie";
    private static final String IOS = "ios";
    private static final String ANDROID = "android";
    private static final String WINDOWS = "windows";
    private static final String MAC = "mac";

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        if (iTestResult.getMethod().isTest()) {
            String platform = iTestResult.getMethod().getXmlTest().getParameter("platform");
            if (((platform != null && platform.equals(ANDROID)) || (platform == null && SETTINGS.getPlatform().equals(ANDROID))) && isNoGroupTest(iTestResult, Group.noAndroid)) {
                throw new SkipException("Test skipped because of noAndroid group");
            }

            if (((platform != null && platform.equals(IOS)) || (platform == null && SETTINGS.getPlatform().equals(IOS))) && isNoGroupTest(iTestResult, Group.noIos)) {
                throw new SkipException("Test skipped because of noIOS group");
            }

            if (((platform != null && platform.equals(WINDOWS)) || (platform == null && SETTINGS.getPlatform().equals(WINDOWS))) && isNoGroupTest(iTestResult, Group.noWindows)) {
                throw new SkipException("Test skipped because of noWindows group");
            }

            if (((platform != null && platform.equals(MAC)) || (platform == null && SETTINGS.getPlatform().equals(MAC))) && isNoGroupTest(iTestResult, Group.noMac)) {
                throw new SkipException("Test skipped because of noMac group");
            }

            if (SETTINGS.getDriverName().equals(CHROME) && isNoGroupTest(iTestResult, Group.noChrome)) {
                throw new SkipException("Test skipped because of noChrome group");
            }

            if (SETTINGS.getDriverName().equals(SAFARI) && isNoGroupTest(iTestResult, Group.noSafari)) {
                throw new SkipException("Test skipped because of noSafari group");
            }

            if (SETTINGS.getDriverName().contains(IE) && isNoGroupTest(iTestResult, Group.noIE)) {
                throw new SkipException("Test skipped because of noIE group");
            }

            if (SETTINGS.getDriverName().equals(FIREFOX) && isNoGroupTest(iTestResult, Group.noFF)) {
                throw new SkipException("Test skipped because of noFF group");
            }

            if (iInvokedMethod.getTestMethod().getInstance() instanceof AbstractSeleniumTest) {
                Throwable throwable = ((AbstractSeleniumTest) iInvokedMethod.getTestMethod().getInstance()).getStopTextExecutionThrowable();
                if (throwable != null) {
                    throw new SkipException("Test skipped because preconditions method was failed. " + throwable.getMessage(), throwable);
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
    }

    private static boolean isNoGroupTest(ITestResult iTestResult, String noGroupName) {
        String[] groups = iTestResult.getMethod().getGroups();
        for (String group : groups) {
            if (group.equals(noGroupName)) {
                return true;
            }
        }
        return false;
    }
}