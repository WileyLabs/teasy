package com.wiley.autotest.selenium.context;


/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:48 PM
 */
public interface HelperRegistry {
    <E extends IPage> E getPageHelper(Class<E> helperClass);

    <E extends IComponent> E getComponentHelper(Class<E> helperClass);

    <E> E getBean(Class<E> beanClass);
}
