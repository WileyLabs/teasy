package com.wiley.autotest.selenium;

import com.wiley.autotest.selenium.context.HelperRegistry;
import com.wiley.autotest.selenium.context.IPage;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:50 PM
 */
public class SpringBasedHelperRegistry implements HelperRegistry, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public <E extends IPage> E getPageHelper(final Class<E> helperClass) {
        for (Object bean : applicationContext.getBeansOfType(helperClass).values()) {
            if (bean.getClass() == helperClass) {
                return (E) bean;
            }
        }
        return null;
    }

    @Override
    public <E> E getBean(Class<E> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
