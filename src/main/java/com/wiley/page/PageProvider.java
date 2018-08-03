package com.wiley.page;

import com.wiley.holders.DriverHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * Entry point to Page classes
 */
public class PageProvider {

    private PageProvider() {
    }

    private static ThreadLocal<Map<String, Object>> map = ThreadLocal.withInitial(HashMap::new);

    public static <T extends BasePage> T get(Class<T> target) {
        return getPage(target);
    }

    public static <T extends BasePage> T get(Class<T> target, String urlToOpen) {
        T page = getPage(target);
        page.open(urlToOpen);
        return page;
    }

    private static <T extends BasePage> T getPage(Class<T> page) {
        String className = page.getName();
        if (map.get().containsKey(className)) {
            T reInitPage = (T) map.get().get(className);
            reInitPage.init(DriverHolder.getDriver());
            return reInitPage;
        } else {
            Object value;
            try {
                value = Class.forName(className).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException("Unable to find class [" + page.getName() + "] in classpath");
            }
            ((T) value).init(DriverHolder.getDriver());
            map.get().put(className, value);
            return (T) value;
        }
    }
}
