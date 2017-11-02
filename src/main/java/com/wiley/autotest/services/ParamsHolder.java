package com.wiley.autotest.services;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ntyukavkin
 * Date: 01.11.2017
 * Time: 15:19
 */
public class ParamsHolder {

    private static ThreadLocal<Map<String, Object>> paramMap = ThreadLocal.withInitial(HashMap::new);
    private static ThreadLocal<Map<String, Object>> paramMapForGroup = ThreadLocal.withInitial(HashMap::new);

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
