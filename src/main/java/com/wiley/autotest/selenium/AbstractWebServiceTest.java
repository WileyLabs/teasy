package com.wiley.autotest.selenium;

import com.wiley.autotest.annotations.*;
import com.wiley.autotest.event.postpone.failure.BeforeAfterGroupFailureEvent;
import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.event.postpone.failure.StorePostponeFailureSubscriber;
import com.wiley.autotest.listeners.ProcessPostponedFailureListener;
import com.wiley.autotest.services.WebServiceMethodsInvoker;
import com.wiley.autotest.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

/**
 * Very basic test for All REST web service tests.
 */
@Listeners({
        ProcessPostponedFailureListener.class
})
public class AbstractWebServiceTest extends AbstractTest implements ITest {

    @Autowired
    private PostponedFailureEvent postponeFailureEvent;

    @Autowired
    private BeforeAfterGroupFailureEvent beforeAfterGroupFailureEvent;

    protected WebServiceMethodsInvoker methodsInvoker;

    private String testName = "";

    @Autowired
    public void setMethodsInvoker(WebServiceMethodsInvoker methodsInvokerValue) {
        this.methodsInvoker = methodsInvokerValue;
    }

    @Override
    public String getTestName() {
        return testName;
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new WebServiceMethodsInvoker().invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context);
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void doBeforeClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeClass.class);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(final Method test, final Object[] params, final ITestContext context) {
        testName = TestUtils.getTestName(test);
        postponeFailureEvent.subscribe(new StorePostponeFailureSubscriber(context, testName));
    }

    @BeforeMethod(alwaysRun = true)
    public void setBugId(final Method test) {
        Bug bugAnnotation = test.getAnnotation(Bug.class);
        if (bugAnnotation != null && bugAnnotation.id() != null && !bugAnnotation.id().isEmpty()) {
            SeleniumHolder.setBugId(bugAnnotation.id());
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void doBeforeMethods(final Method test, final ITestContext context) {
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeMethod.class);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        getParameterProvider().clear();
        postponeFailureEvent.unsubscribeAll();
        SeleniumHolder.setBugId(null);
    }

    @AfterMethod(alwaysRun = true)
    public void doAfterMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterMethod.class);
    }

    @AfterClass(alwaysRun = true)
    public void doAfterClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterClass.class);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new WebServiceMethodsInvoker().invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context);
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context);
        }
    }

    @BeforeGroups
    public void doBeforeGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new WebServiceMethodsInvoker().invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context);
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context);
        }
    }

    @AfterGroups
    public void doAfterGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new WebServiceMethodsInvoker().invokeGroupMethodsByAnnotation(OurAfterGroups.class, context);
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurAfterGroups.class, context);
        }
        if (getParameterProviderForGroup() != null) {
            getParameterProviderForGroup().clear();
        }
    }

    public void setPostponedTestFail(final String message) {
        postponeFailureEvent.fire(message);
    }

    public void setPostponedBeforeAfterGroupFail(final String message, ITestContext context) {
        if (beforeAfterGroupFailureEvent.getSubscribers().isEmpty()) {
            addSubscribersForBeforeAfterGroupFailureEvents(context);
        }
        beforeAfterGroupFailureEvent.fire(message);
    }

    public void addSubscribersForBeforeAfterGroupFailureEvents(ITestContext context) {
        beforeAfterGroupFailureEvent.subscribe(new StorePostponeFailureSubscriber(context, "OurBeforeGroups"));
    }
}
