/*
 * Copyright (C) 2012 by John Wiley & Sons Inc. All Rights Reserved.
 *
 * Created: 01.08.12
 */
package com.wiley.autotest.selenium;

import com.wiley.autotest.annotations.Report;
import com.wiley.autotest.selenium.context.AbstractPageElement;
import com.wiley.autotest.utils.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * User: abekman
 * Date: 01.08.12
 * Time: 10:48
 */
public final class ReportAnnotationsWrapperCreator {

    /**
     * private constructor for utils class
     */
    private ReportAnnotationsWrapperCreator() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getReportingProxy(Class<T> klass, T original) {
        return (T) Enhancer.create(klass, new ReportingMethodInterceptor<T>(original));
    }

    private static final class ReportingMethodInterceptor<T> implements MethodInterceptor {

        private T original;

        private ReportingMethodInterceptor(T original) {
            this.original = original;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if (method.isAnnotationPresent(Report.class) &&
                    original instanceof AbstractPageElement) {
                String value = method.getAnnotation(Report.class).value();
                if (value != null && !value.isEmpty()) {
                    ((AbstractPageElement) original).report(value + "<br/>");
                } else {
                    String methodName = StringUtils.splitCamelCase(method.getName());
                    String report = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                    ((AbstractPageElement) original).report(report + "<br/>");
                }
            }
            Object result = methodProxy.invoke(original, objects);
            if (result == original) {
                return o;
            }
            return result;
        }
    }
}
