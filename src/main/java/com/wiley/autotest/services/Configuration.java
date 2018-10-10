package com.wiley.autotest.services;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElementFactory;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Customization of different areas of a framework
 */
@Component
public class Configuration {

    private DesiredCapabilities desiredCapabilities;
    private boolean pureMode;
    private Map<String, Object> capabilities = new HashMap<>();
    private Class<? extends TeasyElementFactory> customElementFactoryClass;

    public DesiredCapabilities getDesiredCapabilities() {
        return desiredCapabilities;
    }

    public void setDesiredCapabilities(DesiredCapabilities desiredCapabilities) {
        this.desiredCapabilities = desiredCapabilities;
    }

    public void setPureMode(boolean pure) {
        this.pureMode = pure;
    }

    /**
     * If true then a Teasy capabilities wrapper will be ignored during the webdriver initialization process.
     * The standalone driver launcher is not supporting this mode. {@link UnsupportedOperationException} will be thrown.
     */
    public boolean isPureMode() {
        return pureMode;
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

    public Class<? extends TeasyElementFactory> getCustomElementFactoryClass() {
        return customElementFactoryClass;
    }

    public void setCustomElementFactoryClass(Class<? extends TeasyElementFactory> customElementFactoryClass) {
        this.customElementFactoryClass = customElementFactoryClass;
    }
}
