package com.wiley.autotest.services;

import com.wiley.autotest.selenium.context.HelperRegistry;
import com.wiley.autotest.selenium.context.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

/**
 * Created by vefimov on 23.12.2016.
 */
@Component
public class PageProvider {

    @Autowired
    private HelperRegistry registry;

    public <E extends IPage> E get(final Class<E> helperClass) {
        E helper = registry.getPageHelper(helperClass);
        //TODO VE implement proper support of ScreenshotHelper. As for now it's only used for screenshot base tests passing it as null
        helper.init(getWebDriver());
        return helper;
    }

    public <E extends IPage> E get(final Class<E> helperClass, final String urlToOpen) {
        E helper = get(helperClass);
        helper.load(urlToOpen);
        return helper;
    }

    /**
     * Please use get()
     */
    @Deprecated
    public <E extends IPage> E getPage(final Class<E> helperClass) {
        E helper = registry.getPageHelper(helperClass);
        //TODO VE implement proper support of ScreenshotHelper. As for now it's only used for screenshot base tests passing it as null
        helper.init(getWebDriver());
        return helper;
    }

    /**
     * Please use get()
     */
    @Deprecated
    public <E extends IPage> E getPage(final Class<E> helperClass, final String urlToOpen) {
        E helper = get(helperClass);
        helper.load(urlToOpen);
        return helper;
    }
}
