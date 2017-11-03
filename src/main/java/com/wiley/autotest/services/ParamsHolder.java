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
 *  ParamsHolder.setParameter("some.key", anyObjectFromPage);
 *  return this;
 * }
 * <p>
 * In test class (or wherever you need the data) simply call
 * <p>
 * Object objectFromPage = ParamsHolder.getParameter("some.key");
 */
public class ParamsHolder {

    private static ThreadLocal<Map<String, Object>> paramMap = ThreadLocal.withInitial(HashMap::new);
    private static ThreadLocal<Map<String, Object>> paramMapForGroup = ThreadLocal.withInitial(HashMap::new);

    private ParamsHolder() {
    }

    public static void setParameter(String key, Object value) {
        paramMap.get().put(key, value);
    }

    public static void setParameterForGroup(String key, Object value) {
        paramMapForGroup.get().put(key, value);
    }

    public static Object getParameter(String key) {
        return paramMap.get().get(key);
    }

    public static Object getParameterForGroup(String key) {
        return paramMapForGroup.get().get(key);
    }

    public static void clear() {
        paramMap.get().clear();
    }

    public static void clearForGroup() {
        paramMap.get().clear();
    }
}
