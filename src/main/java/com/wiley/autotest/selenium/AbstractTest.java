package com.wiley.autotest.selenium;

import com.wiley.autotest.spring.Settings;
import com.wiley.autotest.utils.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

import java.lang.reflect.Method;

/**
 * Base test that connects spring with our framework
 */
@ContextConfiguration(locations = {
        "classpath*:/META-INF/spring/context-selenium.xml",
        "classpath*:/META-INF/spring/component-scan.xml"
})
public class AbstractTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private Settings settings;

    @Autowired
    private ParamsProvider parameterProvider;

    @Autowired
    private ParamsProvider parameterProviderForGroup;

    private ThreadLocal<Throwable> stopTextExecutionThrowableHolder = new ThreadLocal<>();

    /**
     * Very specific method to handle methods marked with @OurBefore** @OurAfter** annotations
     * currently needed for WileyPlus project but potentially could be used by other so keeping it here.
     * <p>
     * Override it in your project-specific base test and add a logic you want to be executed for all methods described above.
     *
     * @param method - method annotated with OurBefore/After
     */
    public void handleBeforeAfterAnnotations(final Method method) {
    }

    public Settings getSettings() {
        return settings;
    }

    public ParamsProvider getParameterProviderForGroup() {
        return parameterProviderForGroup;
    }

    public ParamsProvider getParameterProvider() {
        return parameterProvider;
    }

    protected final Object getParameter(final String key) {
        return parameterProvider.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    protected final Object getParameterForGroup(final String key) {
        return parameterProviderForGroup.get(TestUtils.modifyKeyForCurrentThread(key));
    }

    protected void setParameter(final String key, final Object value) {
        parameterProvider.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }

    protected void setParameterForGroup(final String key, final Object value) {
        parameterProviderForGroup.put(TestUtils.modifyKeyForCurrentThread(key), value);
    }

    public Throwable getStopTextExecutionThrowable() {
        return stopTextExecutionThrowableHolder.get();
    }

    public void setStopTextExecutionThrowable(Throwable throwable) {
        stopTextExecutionThrowableHolder.set(throwable);
    }
}
