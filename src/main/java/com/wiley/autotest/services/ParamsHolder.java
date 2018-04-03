package com.wiley.autotest.services;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to transfer any data in scope of
 * - test
 * - group of tests (using testNG group)
 * <p>
 * As we adopt fluent approach to our PagObjects this class helps to retrieve data from page
 * and then get it anywhere you need
 * <p>
 * Example:
 * <p>
 * In page class add a method like
 * public SomePage setSomeData() {
 * ParamsHolder.setParameter("some.key", anyObjectFromPage);
 * return this;
 * }
 * <p>
 * In test class (or wherever you need the data) simply call
 * <p>
 * Object objectFromPage = ParamsHolder.getParameter("some.key");
 */
public class ParamsHolder {

    private static ThreadLocal<Map<String, Object>> params = ThreadLocal.withInitial(HashMap::new);
    private static ThreadLocal<Map<String, Object>> paramsForGroup = ThreadLocal.withInitial(HashMap::new);

    private ParamsHolder() {
    }

    /**
     * use new short version of this method {@link ParamsHolder#set(String, Object)}
     * this method will be deleted
     */
    @Deprecated
    public static void setParameter(String key, Object value) {
        params.get().put(key, value);
    }

    /**
     * just a shortcut for setParameter();
     */
    public static void set(String key, Object value) {
        setParameter(key, value);
    }

    /**
     * just a shortcut for getParameter();
     */
    public static Object get(String key) {
        return getParameter(key);
    }

    /**
     * use new short version of this method {@link ParamsHolder#get(String)}
     * this method will be deleted
     */
    @Deprecated
    public static Object getParameter(String key) {
        return params.get().get(key);
    }

    /**
     * When you are using TestNG groups feature and you want params to be passed between all tests for group
     * this method will be deleted
     */
    @Deprecated
    public static void setParameterForGroup(String key, Object value) {
        paramsForGroup.get().put(key, value);
    }

    /**
     * just a shortcut for setParameterForGroup();
     */
    public static void setForGroup(String key, Object value) {
        setParameterForGroup(key, value);
    }

    /**
     * When you are using TestNG groups feature and you want params to be passed between all tests for group
     * this method will be deleted
     */
    @Deprecated
    public static Object getParameterForGroup(String key) {
        return paramsForGroup.get().get(key);
    }

    /**
     * just a shortcut for getParameterForGroup();
     */
    public static Object getForGroup(String key) {
        return getParameterForGroup(key);
    }

    public static void clear() {
        params.get().clear();
    }

    public static void clearForGroup() {
        params.get().clear();
    }
}
