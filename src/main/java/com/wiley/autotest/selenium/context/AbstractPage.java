package com.wiley.autotest.selenium.context;

import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.selenium.AllureStep2TestNG;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.services.ParamsHolder;
import com.wiley.autotest.spring.Settings;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:34 PM
 */
public abstract class AbstractPage extends TeasyElementProvider implements IPageElement, ErrorSender, IPage {

    @Autowired
    private HelperRegistry registry;

    @Autowired
    private PostponedFailureEvent postponeFailureEvent;

    @Autowired
    private Long timeout;

    @Autowired
    private Settings settings;

    public AbstractPage() {
    }

    public void load(final String pathString) {
        if (isNotBlank(pathString)) {
            SeleniumHolder.getWebDriver().get(pathString);
        }
    }

    /**
     * Method is called by framework to complete navigation to the helper.
     * It is the best place to execute your wait-expressions to ensure that page has been completely loaded.
     */
    public void handleRedirect() {
    }

    public final void init(WebDriver driver) {
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
