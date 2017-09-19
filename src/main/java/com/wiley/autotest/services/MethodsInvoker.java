package com.wiley.autotest.services;

import com.wiley.autotest.annotations.OurAfterGroups;
import com.wiley.autotest.annotations.OurAfterSuite;
import com.wiley.autotest.annotations.OurBeforeGroups;
import com.wiley.autotest.annotations.OurBeforeSuite;
import com.wiley.autotest.selenium.AbstractTest;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.ArrayUtils.contains;
import static org.apache.commons.lang.ArrayUtils.isEmpty;
import static org.testng.Reporter.log;

/**
 * User: dfedorov
 * Date: 7/26/12
 * Time: 9:32 AM
 */
@Service
public abstract class MethodsInvoker {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodsInvoker.class);
    private static final String UNABLE_TO_CREATE_TEST_CLASS_INSTANCE = "Unable to create test class instance. ";
    protected static ThreadLocal<Integer> retryCount = ThreadLocal.withInitial(() -> 0);

    abstract void invokeMethod(final AbstractTest instance, final Method method, TestClassContext context, boolean isBeforeAfterGroup);

    /**
     * Called when using all project-specific annotations (E4BeforeGroups, E4AfterMethod, etc.)
     * We decided to catch all kind of exceptions here because
     * based on (https://groups.google.com/forum/#!topic/testng-users/0JhqmewMezM) we think that tests get skipped
     * in case of failure in any before/after method
     *
     * @param context
     * @param isBeforeAfterGroup
     */
    protected void invokeMethodsByAnnotation(final TestClassContext context, boolean isBeforeAfterGroup) {
        try {

            //for invoke all public methods with @OurBefore... and @OurAfter... from super or this class
            Method[] publicMethods = context.getTestClass().getMethods();

            //for invoke private methods with @OurBefore... and @OurAfter... from this class
            Method[] privateMethods = context.getTestClass().getDeclaredMethods();

            //remove public methods of test class
            List<Method> filteredMethods = Arrays.asList(publicMethods).stream()
                    .filter(name -> !name.getDeclaringClass().getName().contains(context.getTestClass().getName()))
                    .collect(Collectors.toList());

            //paste all methods from test class to top of the list
            for (int i = 0; i < privateMethods.length; i++) {
                filteredMethods.add(i, privateMethods[i]);
            }

            Method[] allMethods = new Method[filteredMethods.size()];
            filteredMethods.toArray(allMethods);

            //reverse methods array for correct invoke order if TestClass extends SuperClass with @Before methods
            if (context.getAnnotationToInvokeMethod().getName().contains("OurBefore")) {
                ArrayUtils.reverse(allMethods);
            }
            for (Method method : allMethods) {
                if (isMethodShouldBeInvoked(method, context)) {
                    AbstractTest testInstance = context.getTestInstance();
                    if (testInstance != null || (testInstance = createTestClassInstance(context.getTestClass())) != null) {
                        //hack for E4 project only (@LoginAs and @LoginTo) by default the method will do nothing until you override it
                        //and add some specific logic. For example handle your special annotations
                        testInstance.handleBeforeAfterAnnotations(method);

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
            } catch (NullPointerException ignored) {
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

    protected <T extends AbstractTest> T createTestClassInstance(final Class<T> testClass) {
        try {
            List<String> configurationLocationsList = new ArrayList<>();

            String[] baseConfigurationLocations = AbstractTest.class.getAnnotation(ContextConfiguration.class).locations();
            configurationLocationsList.addAll(Arrays.asList(baseConfigurationLocations));

            //Load spring configuration file from each project for run OurBefore\After methods.
            //Without it @Autowired fields not load in OurBefore\After methods.
            ContextConfiguration testContextConfiguration = testClass.getAnnotation(ContextConfiguration.class);
            if (testContextConfiguration != null) {
                String[] testContextConfigurationLocations = testContextConfiguration.locations();
                configurationLocationsList.addAll(Arrays.asList(testContextConfigurationLocations));
            }

            final String[] locations = configurationLocationsList.stream().toArray(String[]::new);

            final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(locations);

            final T instance = applicationContext.getAutowireCapableBeanFactory().createBean(testClass);
            final TestContextManager testContextManager = new TestContextManager(testClass);
            testContextManager.prepareTestInstance(instance);
            return instance;
        } catch (Exception e) {
            log(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE + e.getMessage());
            LOGGER.error(UNABLE_TO_CREATE_TEST_CLASS_INSTANCE, e);
        }

        return null;
    }

    protected static final class TestClassContext {
        private final Class testClass;
        private final AbstractTest testInstance;
        private final Class<? extends Annotation> annotationClassToInvokeMethods;
        private String[] includedGroups;
        private String[] excludedGroups;
        private ITestContext testContext;

        protected TestClassContext(Class testClass, AbstractTest testInstance, Class<? extends Annotation> annotationClassToInvokeMethods) {
            this.testClass = testClass;
            this.testInstance = testInstance;
            this.annotationClassToInvokeMethods = annotationClassToInvokeMethods;
        }

        protected TestClassContext(Class testClass, AbstractTest testInstance, Class<? extends Annotation> annotationClassToInvokeMethods, ITestContext testContext) {
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
        }

        public Class getTestClass() {
            return testClass;
        }

        public AbstractTest getTestInstance() {
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

        ITestContext getTestContext() {
            return testContext;
        }
    }
}
