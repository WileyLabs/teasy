package com.wiley.autotest.services;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: ntyukavkin
 * Date: 04.04.2017
 * Time: 14:52
 * <p>
 * For provide custom parameters from project.
 */
@Component
public class Configuration {

    private DesiredCapabilities desiredCapabilities;
    private Map<String, Object> capabilities = new HashMap<>();
    private Class<? extends TeasyElement> classOfElement;

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public Map<String, Object> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Map<String, Object> capabilities) {
        this.capabilities = capabilities;
    }

    public void addCapability(String key, Object value) {
        this.capabilities.put(key, value);
    }

    public Class<? extends TeasyElement> getClassOfElement() {
        return classOfElement;
    }

    public void setClassOfElement(Class<? extends TeasyElement> classOfElement) {
        this.classOfElement = classOfElement;
    }
}
