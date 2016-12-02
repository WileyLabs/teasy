package com.wiley.autotest.services;

import com.wiley.autotest.annotations.*;
import com.wiley.autotest.selenium.AbstractSeleniumTest;
import com.wiley.autotest.selenium.Group;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.utils.DriverUtils;
import org.apache.commons.lang.ArrayUtils;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.TestRunner;
import org.testng.annotations.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang.ArrayUtils.contains;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.testng.Reporter.log;

/**
 * User: dfedorov
 * Date: 7/26/12
 * Time: 9:32 AM
 */
@Service
public class MethodsInvoker {

    private static final String FIREFOX = "firefox";
    private static final String CHROME = "chrome";
    private static final String SAFARI = "safari";
    private static final String IE = "ie";
    private static final String IOS = "ios";
    private static final String ANDROID = "android";
    private static final String WINDOWS = "windows";
    private static final String MAC = "mac";

    @Autowired
    public void setCookiesService(CookiesService cookiesService) {
        this.cookiesService = cookiesService;
    }

    private CookiesService cookiesService;

    private static ThreadLocal<Integer> retryCount = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private static ThreadLocal<Boolean> isFFDriver = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodsInvoker.class);
    private static final String UNABLE_TO_CREATE_TEST_CLASS_INSTANCE = "Unable to create test class instance. ";

    public <T extends Annotation> void invokeSuiteMethodsByAnnotation(final Class<T> annotationClass, final ITestContext testContext, Class<? extends AbstractSeleniumTest> baseClass) {
        invokeGroupMethodsByAnnotation(annotationClass, testContext, baseClass);
    }

    public <T extends Annotation> void invokeGroupMethodsByAnnotation(final Class<T> annotationClass, final ITestContext testContext, Class<? extends AbstractSeleniumTest> baseClass) {
        initialize();
        final TestClassContext testClassContext = new TestClassContext(((TestRunner) testContext).getTest().getXmlClasses().get(0).getSupportClass(), null, annotationClass, testContext, baseClass);
        invokeMethodsByAnnotation(testClassContext, true);
    }

    public <T extends Annotation> void invokeMethodsByAnnotation(final AbstractSeleniumTest testObject, final Class<T> annotationClass) {
        invokeMethodsByAnnotation(new TestClassContext(testObject.getClass(), testObject, annotationClass), false);
    }

    private void initialize() {
        if (cookiesService == null) {
            cookiesService = new CookiesService();
        }
    }

    /**
     * Called when using all project-specific annotations (E4BeforeGroups, E4AfterMethod, etc.)
     * We decided to catch all kind of exceptions here because
     * based on (https://groups.google.com/forum/#!topic/testng-users/0JhqmewMezM) we think that tests get skipped
     * in case of failure in any before/after method
     *
     * @param context
     * @param isBeforeAfterGroup
     */
    private void invokeMethodsByAnnotation(final TestClassContext context, boolean isBeforeAfterGroup) {
        try {
            final Method[] methods = context.getTestClass().getMethods();
            for (Method method : methods) {
                if (isMethodShouldBeInvoked(method, context)) {
                    AbstractSeleniumTest testInstance = context.getTestInstance();
                    if (testInstance != null || (testInstance = createTestClassInstance(context.getTestClass(), context.getBaseClass())) != null) {
                        //default body is empty, for @LoginAs and @LoginTo only (E4 projects)
                        testInstance.updateLoginData(method);
                        invokeMethod(testInstance, method, context, isBeforeAfterGroup);
                    }
                }
            }
        } catch (StopTestExecutionException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e.getCause()).getTargetException();
                LOGGER.error("*****StopTestExecutionException*****" + context.getTestClass() + " " + targetException.getCause());
            } else {
                LOGGER.error("*****StopTestExecutionException*****" + context.getTestClass() + " " + e.getCause());
            }
            try {
                context.getTestInstance().setStopTextExecutionThrowable(e);
            } catch (NullPointerException npe) {
            }
        } catch (Throwable t) {
            LOGGER.error("*****THROWABLE*****" + context.getTestClass() + " " + t.getCause());
        }
    }

    private boolean isMethodShouldBeInvoked(final Method method, final TestClassContext context) {
        final Annotation methodAnnotation = method.getAnnotation(context.getAnnotationToInvokeMethod());
        if (methodAnnotation == null) {
            return false;
        }

        boolean alwaysRun = getAlwaysRunFromAnnotation(methodAnnotation);
        if (alwaysRun) {
            return true;
        }

        final String[] groups = getGroupsFromAnnotation(methodAnnotation);
        for (String group : groups) {
            if (contains(context.getExcludedGroups(), group)) {
                return false;
            }
        }

        for (String group : groups) {
            if (contains(context.getIncludedGroups(), group)) {
                return true;
            }
        }

        List<String> groupsFromMethods = new ArrayList<>();
        for (ITestNGMethod testNGMethod : context.getTestContext().getAllTestMethods()) {
            groupsFromMethods.addAll(Arrays.asList(testNGMethod.getGroups()));
        }

        boolean isContainsGroup = false;
        for (String group : groups) {
            if (groupsFromMethods.contains(group)) {
                isContainsGroup = true;
                break;
            }
        }

        return isEmpty(context.getExcludedGroups()) && isEmpty(context.getIncludedGroups()) && isContainsGroup;
    }

    private String[] getGroupsFromAnnotation(final Annotation methodAnnotation) {
        if (methodAnnotation.annotationType() == OurBeforeSuite.class) {
            return ((OurBeforeSuite) methodAnnotation).groups();
        }
        if (methodAnnotation.annotationType() == OurAfterSuite.class) {
            return ((OurAfterSuite) methodAnnotation).groups();
        }

        if (methodAnnotation.annotationType() == OurBeforeGroups.class) {
            return ((OurBeforeGroups) methodAnnotation).value();
        }
        if (methodAnnotation.annotationType() == OurAfterGroups.class) {
            return ((OurAfterGroups) methodAnnotation).value();
        }

        return new String[0];
    }

    private boolean getAlwaysRunFromAnnotation(final Annotation methodAnnotation) {
        if (methodAnnotation.annotationType() == OurBeforeSuite.class) {
            return ((OurBeforeSuite) methodAnnotation).alwaysRun();
        }
        if (methodAnnotation.annotationType() == OurBeforeGroups.class) {
            return ((OurBeforeGroups) methodAnnotation).alwaysRun();
        }
        if (methodAnnotation.annotationType() == OurAfterGroups.class) {
            return ((OurAfterGroups) methodAnnotation).alwaysRun();
        }

        return methodAnnotation.annotationType() != OurAfterSuite.class || ((OurAfterSuite) methodAnnotation).alwaysRun();
    }

    protected <T extends AbstractSeleniumTest> T createTestClassInstance(final Class<T> testClass, Class<T> baseClass) {
        try {
            final String[] locations = (String[]) ArrayUtils.addAll(baseClass.getAnnotation(ContextConfiguration.class).locations(), AbstractSeleniumTest.class.getAnnotation(ContextConfiguration.class).locations());
            final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(locations);

            final T instance = applicationContext.getAutowireCapableBeanFactory().createBean(testClass);
            final TestContextManager testContextManager = new TestContextManager(testClass);
            testContextManager.prepareTestInstance(instance);
            return instance;
        } catch (BeansException e) {
            log(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE + e.getMessage());
            LOGGER.error(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE, e);
        } catch (IllegalStateException e) {
            log(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE + e.getMessage());
            LOGGER.error(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE, e);
        } catch (Exception e) {
            log(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE + e.getMessage());
            LOGGER.error(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE, e);
        }

        return null;
    }

    private void invokeMethod(final AbstractSeleniumTest instance, final Method method, TestClassContext context, boolean isBeforeAfterGroup) {
        final WebDriver mainDriver = SeleniumHolder.getWebDriver();
        final String mainDriverName = SeleniumHolder.getDriverName();

        if (instance.getTestMethod() != null && isSkippedTest(instance)) {
            return;
        }

        //For IE and Safari browser and @FireFoxOnly annotation setting specifically prepared FF driver
        if ((SeleniumHolder.getDriverName().contains(IE) ||
                SeleniumHolder.getDriverName().equals(SAFARI) ||
                SeleniumHolder.getPlatform().equals(ANDROID))
                && method.getAnnotation(FireFoxOnly.class) != null) {
            SeleniumHolder.setWebDriver(DriverUtils.getFFDriver());
            SeleniumHolder.setDriverName(FIREFOX);
            isFFDriver.set(true);
        }

        //Set FirefoxOnly for all after methods in safari, ie, android
        if ((SeleniumHolder.getDriverName().equals(SAFARI) ||
                SeleniumHolder.getDriverName().contains(IE) ||
                SeleniumHolder.getPlatform().equals(ANDROID)) &&
                (method.getAnnotation(OurAfterMethod.class) != null ||
                        method.getAnnotation(OurAfterClass.class) != null ||
                        method.getAnnotation(OurAfterGroups.class) != null ||
                        method.getAnnotation(OurAfterSuite.class) != null)) {
            SeleniumHolder.setWebDriver(DriverUtils.getFFDriver());
            SeleniumHolder.setDriverName(FIREFOX);
            isFFDriver.set(true);
        }

        try {
            method.invoke(instance);
        } catch (Throwable e) {
            instance.takeScreenshot(e.getMessage(), method.getName());
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            ((InvocationTargetException) e).getTargetException().printStackTrace(printWriter);
            String errorMessage = format("Precondition method '%s' failed ", method.getName()) + "\n " +
                    result.toString();
            if (isBeforeAfterGroup) {
                instance.setPostponedBeforeAfterGroupFail(errorMessage, context.getTestContext());
            } else {
                instance.setPostponedTestFail(errorMessage);
            }

            if (method.getAnnotation(RetryPrecondition.class) != null) {
                retryCount.set(retryCount.get() + 1);
                if (method.getAnnotation(RetryPrecondition.class).retryCount() >= retryCount.get()) {
                    LOGGER.error("*****ERROR***** Method '" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "' is failed. Retrying it. Current retryCount is " + retryCount.get());
                    invokeMethod(instance, method, context, isBeforeAfterGroup);
                }
            }

            log(errorMessage);

            if (method.getAnnotation(RetryPrecondition.class) == null) {
                throw new StopTestExecutionException(errorMessage, e);
            }
        } finally {
            if (isFFDriver.get() && SeleniumHolder.getDriverName().equals(FIREFOX)) {
                SeleniumHolder.getWebDriver().quit();
            }
            SeleniumHolder.setDriverName(mainDriverName);
            SeleniumHolder.setWebDriver(mainDriver);
        }
    }

    public boolean isSkippedTest(AbstractSeleniumTest instance) {
        Method method = instance.getTestMethod();
        String platform = SeleniumHolder.getPlatform();
        String driverName = SeleniumHolder.getDriverName();
        if (((platform != null && platform.equals(ANDROID)) || (platform == null && platform.equals(ANDROID))) && isNoGroupTest(method, Group.noAndroid)) {
            return true;
        }

        if (((platform != null && platform.equals(IOS)) || (platform == null && platform.equals(IOS))) && isNoGroupTest(method, Group.noIos)) {
            return true;
        }

        if (((platform != null && platform.equals(WINDOWS)) || (platform == null && platform.equals(WINDOWS))) && isNoGroupTest(method, Group.noWindows)) {
            return true;
        }

        if (((platform != null && platform.equals(MAC)) || (platform == null && platform.equals(MAC))) && isNoGroupTest(method, Group.noMac)) {
            return true;
        }

        if (driverName.equals(CHROME) && isNoGroupTest(method, Group.noChrome)) {
            return true;
        }

        if (driverName.equals(SAFARI) && isNoGroupTest(method, Group.noSafari)) {
            return true;
        }

        if (driverName.contains(IE) && isNoGroupTest(method, Group.noIE)) {
            return true;
        }

        if (driverName.equals(FIREFOX) && isNoGroupTest(method, Group.noFF)) {
            return true;
        }

        return false;
    }

    private boolean isNoGroupTest(Method method, String noGroupName) {
        String[] groups = method.getAnnotation(Test.class).groups();
        for (String group : groups) {
            if (group.equals(noGroupName)) {
                return true;
            }
        }
        return false;
    }

    private static final class TestClassContext {
        private final Class testClass;
        private final AbstractSeleniumTest testInstance;
        private final Class<? extends Annotation> annotationClassToInvokeMethods;
        private String[] includedGroups;
        private String[] excludedGroups;
        private ITestContext testContext;
        private Class<? extends AbstractSeleniumTest> baseClass;

        private TestClassContext(Class testClass, AbstractSeleniumTest testInstance, Class<? extends Annotation> annotationClassToInvokeMethods) {
            this.testClass = testClass;
            this.testInstance = testInstance;
            this.annotationClassToInvokeMethods = annotationClassToInvokeMethods;
        }

        private TestClassContext(Class testClass, AbstractSeleniumTest testInstance, Class<? extends Annotation> annotationClassToInvokeMethods,
                                 ITestContext testContext, Class<? extends AbstractSeleniumTest> baseClass) {
            this.testClass = testClass;
            this.testInstance = testInstance;
            this.annotationClassToInvokeMethods = annotationClassToInvokeMethods;
            if (testContext.getExcludedGroups() == null) {
                this.excludedGroups = new String[0];
            } else {
                this.excludedGroups = Arrays.copyOf(testContext.getExcludedGroups(), testContext.getExcludedGroups().length);
            }
            if (testContext.getIncludedGroups() == null) {
                this.includedGroups = new String[0];
            } else {
                this.includedGroups = Arrays.copyOf(testContext.getIncludedGroups(), testContext.getIncludedGroups().length);
            }
            this.testContext = testContext;
            this.baseClass = baseClass;
        }

        public Class getTestClass() {
            return testClass;
        }

        public AbstractSeleniumTest getTestInstance() {
            return testInstance;
        }

        public Class<? extends Annotation> getAnnotationToInvokeMethod() {
            return annotationClassToInvokeMethods;
        }

        public String[] getIncludedGroups() {
            return includedGroups;
        }

        public String[] getExcludedGroups() {
            return excludedGroups;
        }

        private ITestContext getTestContext() {
            return testContext;
        }

        public Class<? extends AbstractSeleniumTest> getBaseClass() {
            return baseClass;
        }
    }
}
