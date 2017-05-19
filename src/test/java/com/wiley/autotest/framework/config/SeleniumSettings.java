package com.wiley.autotest.framework.config;

import com.wiley.autotest.spring.CustomPropertyPlaceholderConfigurer;
import com.wiley.autotest.spring.Settings;

/**
 * User: ntyukavkin
 * Date: 19.05.2017
 * Time: 14:35
 */
public class SeleniumSettings extends Settings {

    private CustomPropertyPlaceholderConfigurer propertyConfigurer;

    public void init(){
        propertyConfigurer.copyTo("", this);
    }

    public void setPropertyConfigurer(final CustomPropertyPlaceholderConfigurer propertyConfigurer) {
        this.propertyConfigurer = propertyConfigurer;
    }
}
