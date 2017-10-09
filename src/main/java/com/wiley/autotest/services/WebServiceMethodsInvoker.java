package com.wiley.autotest.services;

import com.wiley.autotest.annotations.RetryPrecondition;
import com.wiley.autotest.selenium.AbstractTest;
import com.wiley.autotest.selenium.AbstractWebServiceTest;
import com.wiley.autotest.selenium.Report;
import org.springframework.stereotype.Service;
import org.testng.ITestContext;
import org.testng.TestRunner;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.String.format;
import static org.testng.Reporter.log;

/**
 * User: dfedorov
 * Date: 7/26/12
 * Time: 9:32 AM
 */
@Service
public class WebServiceMethodsInvoker extends MethodsInvoker {

    public <T extends Annotation> void invokeSuiteMethodsByAnnotation(final Class<T> annotationClass, final ITestContext testContext) {
        invokeGroupMethodsByAnnotation(annotationClass, testContext);
    }

    public <T extends Annotation> void invokeGroupMethodsByAnnotation(final Class<T> annotationClass, final ITestContext testContext) {
        final TestClassContext testClassContext = new TestClassContext(((TestRunner) testContext).getTest().getXmlClasses().get(0).getSupportClass(), null, annotationClass, testContext);
        invokeMethodsByAnnotation(testClassContext, true);
    }

    public <T extends Annotation> void invokeMethodsByAnnotation(final AbstractWebServiceTest testObject, final Class<T> annotationClass) {
        invokeMethodsByAnnotation(new TestClassContext(testObject.getClass(), testObject, annotationClass), false);
    }

    @Override
    void invokeMethod(AbstractTest instance, Method method, TestClassContext context, boolean isBeforeAfterGroup) {
        AbstractWebServiceTest abstractWebServiceTest = (AbstractWebServiceTest) instance;
        try {
            method.setAccessible(true);
            method.invoke(instance);
        } catch (Throwable e) {
            final Writer result = new StringWriter();
            final PrintWriter printWriter = new PrintWriter(result);
            ((InvocationTargetException) e).getTargetException().printStackTrace(printWriter);
            String errorMessage = format("Precondition method '%s' failed ", method.getName()) + "\n " + result.toString();
            if (isBeforeAfterGroup) {
                abstractWebServiceTest.setPostponedBeforeAfterGroupFail(errorMessage, context.getTestContext());
            } else {
                abstractWebServiceTest.setPostponedTestFail(errorMessage);
            }

            if (method.getAnnotation(RetryPrecondition.class) != null) {
                retryCount.set(retryCount.get() + 1);
                if (method.getAnnotation(RetryPrecondition.class).retryCount() >= retryCount.get()) {
                    new Report("*****ERROR***** Method '" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "' is failed. Retrying it. Current retryCount is " + retryCount.get()).jenkins();
                    invokeMethod(instance, method, context, isBeforeAfterGroup);
                }
            }

            log(errorMessage);

            if (method.getAnnotation(RetryPrecondition.class) == null) {
                throw new StopTestExecutionException(errorMessage, e);
            }
        }
    }
}
