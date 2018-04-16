package com.wiley.autotest.selenium.context;

import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.selenium.AllureStep2TestNG;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.services.ParamsHolder;
import com.wiley.autotest.spring.Settings;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 07.02.12
 * Time: 18:46
 */
public abstract class AbstractPageElement<P extends AbstractPageElement> extends TeasyElementProvider implements IPageElement, ErrorSender {

    private WebDriver driver;

    @Autowired
    private HelperRegistry registry;

    @Autowired
    private PostponedFailureEvent postponeFailureEvent;

    @Autowired
    private Long timeout;

    @Autowired
    private Settings settings;

    /**
     * Method is called by framework to complete navigation to the helper.
     * It is the best place to execute your wait-expressions to ensure that page has been completely loaded.
     */
    public void handleRedirect() {
    }

    public final void init(WebDriver driver) {
        this.driver = driver;
        init();
    }

    protected Long getTimeout() {
        return timeout;
    }

    protected Settings getSettings() {
        return settings;
    }

    protected void init() {
    }

    protected <E extends IPage> E redirectTo(final Class<E> target) {
        final E page = getHelper(target);
        page.handleRedirect();
        window().waitForScriptsToLoad();
        return page;
    }

    protected final <E extends IPage> E redirectToWithoutWaitToLoad(final Class<E> target) {
        final E page = getHelper(target);
        page.handleRedirect();
        return page;
    }

    protected final <E extends IPage> E getHelper(final Class<E> target) {
        final E helper = registry.getPageHelper(target);
        helper.init(SeleniumHolder.getWebDriver());
        return AllureStep2TestNG.addInterceptor(target, helper);
    }

    protected final <E extends IPage> E navigateTo(final Class<E> target, final String url) {
        SeleniumHolder.getWebDriver().get(url);
        return redirectTo(target);
    }

    /**
     * Executes JavaScript in the context of the currently selected frame or window.
     *
     * @param script - The JavaScript to execute
     * @param args   - The arguments to the script. May be empty
     * @return Boolean, Long, String, List or WebElement. Or null.
     */
    protected final Object executeScript(final String script, final Object... args) {
        return ((JavascriptExecutor) driver).executeScript(script, args);
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

    public void setPostponedTestFail(final String message) {
        postponeFailureEvent.fire(message);
    }
}
