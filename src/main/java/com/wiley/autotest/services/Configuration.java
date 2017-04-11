package com.wiley.autotest.services;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

/**
 * User: ntyukavkin
 * Date: 04.04.2017
 * Time: 14:52
 *
 * For provide custom parameters from project.
 */
@Component
public class Configuration {

    private DesiredCapabilities desiredCapabilities;
    private Class<? extends OurWebElement> classOfElement;

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public Class<? extends OurWebElement> getClassOfElement() {
        return classOfElement;
    }

    public void setClassOfElement(Class<? extends OurWebElement> classOfElement) {
        this.classOfElement = classOfElement;
    }
}
