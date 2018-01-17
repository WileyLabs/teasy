package com.wiley.autotest.selenium;

import com.wiley.autotest.services.ParamsHolder;
import com.wiley.autotest.spring.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractSeleniumTest.class);

    @Autowired
    private Settings settings;

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

    protected final Object getParameter(final String key) {
        return ParamsHolder.getParameter(key);
    }

    protected final Object getParameterForGroup(final String key) {
        return ParamsHolder.getParameterForGroup(key);
    }

    protected void setParameter(final String key, final Object value) {
        ParamsHolder.setParameter(key, value);
    }

    protected void setParameterForGroup(final String key, final Object value) {
        ParamsHolder.setParameterForGroup(key, value);
    }

    public Throwable getStopTextExecutionThrowable() {
        return stopTextExecutionThrowableHolder.get();
    }

    public void setStopTextExecutionThrowable(Throwable throwable) {
        stopTextExecutionThrowableHolder.set(throwable);
    }
}
