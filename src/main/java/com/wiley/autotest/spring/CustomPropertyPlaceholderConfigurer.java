package com.wiley.autotest.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 25.01.12
 * Time: 13:00
 */
public class CustomPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    private List<CustomPropertyPlaceholderConfigurer> environments;
    protected Properties properties;
    private String environment;

    public void init() {
        postProcessBeanFactory(new DefaultListableBeanFactory());
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setEnvironments(List<CustomPropertyPlaceholderConfigurer> environments) {
        this.environments = environments;
    }

    public String getProperty(String key) {
        if (properties != null) {
            String value = properties.getProperty(key);
            if (value != null) {
                return value;
            }
            if (environments != null) {
                for (CustomPropertyPlaceholderConfigurer configurer : environments) {
                    value = configurer.getProperty(key);
                    if (value != null) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void processProperties(final ConfigurableListableBeanFactory beanFactoryToProcess, final Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        properties = props;
        for (final Map.Entry entry : properties.entrySet()) {
            resolveSubstitution((String) entry.getKey(), (String) entry.getValue());
        }
    }

    private void resolveSubstitution(final String key, final String value) {
        if (value.contains(placeholderPrefix)) {
            properties.setProperty(key, resolveSubstitution(value));
        }
    }

    private String resolveSubstitution(final String value) {
        final int sIndex = value.lastIndexOf(placeholderPrefix);
        if (sIndex >= 0) {
            final int eIndex = value.indexOf(placeholderSuffix, sIndex);
            if (eIndex > 0) {
                String substitute = value.substring(sIndex + 2, eIndex);
                String property = resolvePlaceholder(substitute, properties, 1);
                if (property != null && !property.equals(value)) {
                    return resolveSubstitution(value.replace(placeholderPrefix + substitute + placeholderSuffix, property));
                }
            }
        }
        return value;
    }

    public void copyTo(String environment, Properties prop) {
        List<CustomPropertyPlaceholderConfigurer> list = new ArrayList<CustomPropertyPlaceholderConfigurer>();
        findEnvironments(environment, list);
        for (CustomPropertyPlaceholderConfigurer configurer : list) {
            if (configurer.properties != null) {
                CollectionUtils.mergePropertiesIntoMap(configurer.properties, prop);
            }
        }
    }

    private boolean findEnvironments(String environment, List<CustomPropertyPlaceholderConfigurer> list) {
        list.add(this);
        if (environments == null) {
            return environment.equals(this.environment);
        }
        for (CustomPropertyPlaceholderConfigurer configurer : environments) {
            if (configurer.findEnvironments(environment, list)) {
                return true;
            }
            list.subList(list.indexOf(this) + 1, list.size()).clear();
        }
        return false;
    }
}
