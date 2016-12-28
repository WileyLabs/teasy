package com.wiley.autotest.selenium;

import com.wiley.autotest.annotations.*;
import com.wiley.autotest.services.MethodsInvoker;
import com.wiley.autotest.spring.Settings;
import com.wiley.autotest.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Base test that connects spring with our framework
 */
@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/context-selenium.xml",
        "classpath*:/META-INF/spring/component-scan.xml"
})
public class AbstractTest extends AbstractTestNGSpringContextTests {

    private MethodsInvoker methodsInvoker;

    @Autowired
    private Settings settings;

    @Autowired
    private ParamsProvider parameterProvider;

    @Autowired
    private ParamsProvider parameterProviderForGroup;

    private ThreadLocal<Throwable> stopTextExecutionThrowableHolder = new ThreadLocal<>();

    @Autowired
    public void setMethodsInvoker(MethodsInvoker methodsInvokerValue) {
        this.methodsInvoker = methodsInvokerValue;
    }


    /**
     * Very specific method to handle methods marked with @OurBefore** @OurAfter** annotations
     * currently needed for WileyPlus project but potentially could be used by other so keeping it here.
     *
     * Override it in your project-specific base test and add a logic you want to be executed for all methods described above.
     *
     * @param method - method annotated with OurBefore/After
     */
    public void handleBeforeAfterAnnotations(final Method method) {
    }


    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new MethodsInvoker().invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context, this.getClass());
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context, this.getClass());
        }
    }


    @BeforeClass(alwaysRun = true)
    public void doBeforeClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeClass.class);
    }

    @BeforeMethod(alwaysRun = true)
    public void doBeforeMethods(final Method test, final ITestContext context) {
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeMethod.class);
    }

    @AfterMethod(alwaysRun = true)
    public void doAfterMethods() {
        parameterProvider.clear();
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterMethod.class);
    }

    @AfterClass(alwaysRun = true)
    public void doAfterClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterClass.class);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new MethodsInvoker().invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context, this.getClass());
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context, this.getClass());
        }
    }

    @BeforeGroups
    public void doBeforeGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new MethodsInvoker().invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context, this.getClass());
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context, this.getClass());
        }
    }

    @AfterGroups
    public void doAfterGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new MethodsInvoker().invokeGroupMethodsByAnnotation(OurAfterGroups.class, context, this.getClass());
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurAfterGroups.class, context, this.getClass());
        }
        if (getParameterProviderForGroup() != null) {
            getParameterProviderForGroup().clear();
        }
    }

    public Settings getSettings() {
        return settings;
    }

    protected final Object getParameter(final String key) {
        return parameterProvider.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    protected final Object getParameterForGroup(final String key) {
        return parameterProviderForGroup.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    public ParamsProvider getParameterProviderForGroup() {
        return parameterProviderForGroup;
    }

    protected void setParameter(final String key, final Object value) {
        parameterProvider.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }

    protected void setParameterForGroup(final String key, final Object value) {
        parameterProviderForGroup.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }


    public Throwable getStopTextExecutionThrowable() {
        return stopTextExecutionThrowableHolder.get();
    }

    public void setStopTextExecutionThrowable(Throwable throwable) {
        stopTextExecutionThrowableHolder.set(throwable);
    }


}
