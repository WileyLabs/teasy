package com.wiley.autotest.utils;

import java.lang.reflect.Method;

/**
 * Created by mosadchiy on 26.05.2016.
 */

public class JavaUtils {
    public static Method findMethodByAnnotation(Class clazz) throws NoSuchMethodException {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            String methodName = stackTraceElement.getMethodName();
            try {
                Method method = clazz.getClassLoader().loadClass(stackTraceElement.getClassName()).getDeclaredMethod(methodName);
                if (method.getAnnotation(clazz) != null) {
                    return method;
                }
            } catch (Exception e) {
                continue;
            }
        }
        throw new NoSuchMethodException();
    }
}