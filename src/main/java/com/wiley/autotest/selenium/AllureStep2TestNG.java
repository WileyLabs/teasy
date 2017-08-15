/*
 * Copyright (C) 2012 by John Wiley & Sons Inc. All Rights Reserved.
 *
 * Created: 01.08.12
 */
package com.wiley.autotest.selenium;

import com.wiley.autotest.utils.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.testng.Reporter;
import ru.yandex.qatools.allure.annotations.Step;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Proxy class to transfer all Allure step annotations to TestNG report
 * (needed for Cuanto tool only)
 * It adds a method interceptor and transfers @Step annotation value or parsed method name
 * to testNg
 */
public final class AllureStep2TestNG {

    private AllureStep2TestNG() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T addInterceptor(Class<T> klass, T original) {
        return (T) Enhancer.create(klass, new StepAnnotationInterceptor<>(original));
    }

    private static final class StepAnnotationInterceptor<T> implements MethodInterceptor {

        private T original;

        private StepAnnotationInterceptor(T original) {
            this.original = original;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if (method.isAnnotationPresent(Step.class)) {
                Reporter.getCurrentTestResult().setAttribute("lastMethodTime", System.currentTimeMillis());
                String testName = Reporter.getCurrentTestResult().getTestName() + ": ";
                String methodArgs = objects.length == 0 ? "" : Arrays.asList(objects).toString();
                String methodName = StringUtils.splitCamelCase(method.getName());
                String report = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                new Report(testName + report + methodArgs).jenkins();
            }
            Object result = methodProxy.invoke(original, objects);
            if (result == original) {
                return o;
            }
            return result;
        }
    }
}
