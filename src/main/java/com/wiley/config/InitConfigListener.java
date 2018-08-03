package com.wiley.config;

import com.wiley.utils.Report;
import org.reflections.Reflections;
import org.testng.IExecutionListener;

import java.util.Iterator;
import java.util.Set;

/**
 * Listener for invoked all implementations of {@link CustomizableConfig#set()}.
 * Will be run before all tests started.
 */
public class InitConfigListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        try {
            Set<Class<? extends CustomizableConfig>> conf = new Reflections("com.wiley").getSubTypesOf(CustomizableConfig.class);
            Iterator<Class<? extends CustomizableConfig>> iterator = conf.iterator();
            while (iterator.hasNext()) {
                Class<? extends CustomizableConfig> next = iterator.next();
                Object instance = Class.forName(next.getName()).newInstance();
                ((CustomizableConfig) instance).set();
            }
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ignored) {
            Report.jenkins("Exception when try to invoke method set() from AbstractSetConfig.", ignored);
        }
    }

    @Override
    public void onExecutionFinish() {
    }
}
