/*
 * Copyright (C) 2012 by John Wiley & Sons Inc. All Rights Reserved.
 *
 * Created: 01.08.12
 */
package com.wiley.autotest.selenium;

import com.wiley.autotest.utils.ReadableMethodName;
import com.wiley.autotest.utils.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import ru.yandex.qatools.allure.annotations.Step;

import java.lang.reflect.Method;

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
                String value = method.getAnnotation(Step.class).value();
                if (value.isEmpty()) {
                    value = new ReadableMethodName(method.getName()).toString();
                }
                new Report(value).testNG();
            }
            Object result = methodProxy.invoke(original, objects);
            if (result == original) {
                return o;
            }
            return result;
        }
    }
}
