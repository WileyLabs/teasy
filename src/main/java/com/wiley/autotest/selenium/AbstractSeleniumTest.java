package com.wiley.autotest.selenium;

import com.wiley.autotest.annotations.*;
import com.wiley.autotest.event.postpone.failure.BeforeAfterGroupFailureEvent;
import com.wiley.autotest.event.postpone.failure.PostponedFailureEvent;
import com.wiley.autotest.event.postpone.failure.ScreenshotOnPostponeFailureSubscriber;
import com.wiley.autotest.event.postpone.failure.StorePostponeFailureSubscriber;
import com.wiley.autotest.listeners.ProcessPostponedFailureListener;
import com.wiley.autotest.listeners.SkipTestsListener;
import com.wiley.autotest.screenshots.Screenshoter;
import com.wiley.autotest.selenium.context.IPage;
import com.wiley.autotest.selenium.context.ScreenshotHelper;
import com.wiley.autotest.selenium.driver.events.listeners.ScreenshotWebDriverEventListener;
import com.wiley.autotest.services.CookiesService;
import com.wiley.autotest.services.PageProvider;
import com.wiley.autotest.services.SeleniumMethodsInvoker;
import com.wiley.autotest.spring.SeleniumTestExecutionListener;
import com.wiley.autotest.utils.JavaUtils;
import com.wiley.autotest.utils.TestUtils;
import net.lightbody.bmp.proxy.ProxyServer;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.testng.IHookCallBack;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;
import static org.testng.Reporter.log;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 1:29 PM
 */

@TestExecutionListeners(SeleniumTestExecutionListener.class)
@Listeners({
        ProcessPostponedFailureListener.class,
        SkipTestsListener.class
})
public abstract class AbstractSeleniumTest extends AbstractTest implements ITest, ScreenshotHelper {

    @Autowired
    private PageProvider pageProvider;

    @Autowired
    private PostponedFailureEvent postponeFailureEvent;

    @Autowired
    private BeforeAfterGroupFailureEvent beforeAfterGroupFailureEvent;

    @Autowired
    protected CookiesService cookiesService;

    protected SeleniumMethodsInvoker methodsInvoker;

    private ScreenshotWebDriverEventListener screenshotWebDriverEventListener;

    public ThreadLocal<String> mainWindowHandle = new ThreadLocal<>();

    private String testName = "";

    private int passCounter;

    @Override
    public String getTestName() {
        return testName;
    }

    @Autowired
    public void setMethodsInvoker(SeleniumMethodsInvoker methodsInvokerValue) {
        this.methodsInvoker = methodsInvokerValue;
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new SeleniumMethodsInvoker().invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context);
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurBeforeSuite.class, context);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void initScreenshotWebDriverEventListener() {
        screenshotWebDriverEventListener = new ScreenshotWebDriverEventListener();
        ((EventFiringWebDriver) getWebDriver()).register(screenshotWebDriverEventListener);
    }

    @BeforeClass(alwaysRun = true)
    public void doBeforeClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeClass.class);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(final Method test, final Object[] params, final ITestContext context) {
        testName = TestUtils.getTestName(test);
        postponeFailureEvent.subscribe(new ScreenshotOnPostponeFailureSubscriber(testName));
        postponeFailureEvent.subscribe(new StorePostponeFailureSubscriber(context, testName));
        passCounter = 0;
    }

    @BeforeMethod(alwaysRun = true)
    public void setBugId(final Method test) {
        Bug bugAnnotation = test.getAnnotation(Bug.class);
        if (bugAnnotation != null && bugAnnotation.id() != null && !bugAnnotation.id().isEmpty()) {
            SeleniumHolder.setBugId(bugAnnotation.id());
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void doBeforeMethods(final Method test, final ITestContext context) {
        mainWindowHandle.set(getWebDriver().getWindowHandle());
        methodsInvoker.invokeMethodsByAnnotation(this, OurBeforeMethod.class);
    }

    @BeforeTest(alwaysRun = true)
    @Parameters("browser")
    public void setBrowserForGrid(@Optional("browser") String browser) {
        SeleniumHolder.setParameterBrowserName(browser);
    }

    @BeforeTest(alwaysRun = true)
    @Parameters("platform")
    public void setPlatformForGrid(@Optional("platform") String platform) {
        SeleniumHolder.setParameterPlatformName(platform);
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        getParameterProvider().clear();
        postponeFailureEvent.unsubscribeAll();
        SeleniumHolder.setBugId(null);
    }

    @AfterMethod(alwaysRun = true)
    public void doAfterMethods() {
        //TODO VE: sometimes cookies may be needed so it's worth to make it optional/configurable
        cookiesService.deleteAllCookies();
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterMethod.class);
    }

    @AfterClass(alwaysRun = true)
    public void doAfterClassMethods() {
        methodsInvoker.invokeMethodsByAnnotation(this, OurAfterClass.class);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(final ITestContext context) {
        if (methodsInvoker == null) {
            new SeleniumMethodsInvoker().invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context);
        } else {
            methodsInvoker.invokeSuiteMethodsByAnnotation(OurAfterSuite.class, context);
        }
    }

    @BeforeGroups
    public void doBeforeGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new SeleniumMethodsInvoker().invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context);
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurBeforeGroups.class, context);
        }
    }

    @AfterGroups
    public void doAfterGroups(final ITestContext context) {
        if (methodsInvoker == null) {
            new SeleniumMethodsInvoker().invokeGroupMethodsByAnnotation(OurAfterGroups.class, context);
        } else {
            methodsInvoker.invokeGroupMethodsByAnnotation(OurAfterGroups.class, context);
        }
        if (getParameterProviderForGroup() != null) {
            getParameterProviderForGroup().clear();
        }
    }

    public void addSubscribersForBeforeAfterGroupFailureEvents(ITestContext context) {
        beforeAfterGroupFailureEvent.subscribe(new ScreenshotOnPostponeFailureSubscriber("OurBeforeGroups"));
        beforeAfterGroupFailureEvent.subscribe(new StorePostponeFailureSubscriber(context, "OurBeforeGroups"));
    }

    public <E extends IPage> E getPage(final Class<E> helperClass) {
        return pageProvider.get(helperClass, this);
    }

    public <E extends IPage> E getPage(final Class<E> helperClass, final String urlToOpen) {
        return pageProvider.get(helperClass, this, urlToOpen);
    }

    public void setPostponedTestFail(final String message) {
        postponeFailureEvent.fire(message);
    }

    public void setPostponedBeforeAfterGroupFail(final String message, ITestContext context) {
        if (beforeAfterGroupFailureEvent.getSubscribers().isEmpty()) {
            addSubscribersForBeforeAfterGroupFailureEvents(context);
        }
        beforeAfterGroupFailureEvent.fire(message);
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        super.run(callBack, testResult);
        if (testResult.getThrowable() != null) {
            takeScreenshot(testResult);
        }
    }

    private void takeScreenshot(final ITestResult testResult) {
        try {
            new Screenshoter().takeScreenshot(testResult);
        } catch (Exception e) {
            log("Couldn't take screenshot. Error: " + e.getMessage());
        }
    }

    public void takeScreenshot(final String errorMessage, final String testName) {
        try {
            new Screenshoter().takeScreenshot(errorMessage, testName);
        } catch (Exception e) {
            log("Couldn't take screenshot. Error: " + e.getMessage());
        }
    }

    public ProxyServer getProxyServer() {
        return SeleniumHolder.getProxyServer();
    }

    /**
     * This method is used for sub-methods not marked with @Test TestNG annotation, but have @LoginTo and @LoginAs annotation.
     * This lets us to use multiple logins for different users in one particular TestNG test method.
     * For test methods marked with @Test TestNG annotation please use login() method.
     */
    protected Method findMethodByAnnotation(Class clazz) throws NoSuchMethodException {
        try {
            return JavaUtils.findMethodByAnnotation(clazz);
        } catch (NoSuchMethodException e) {
            Method[] methods = this.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getAnnotation(clazz) != null) {
                    return method;
                }
            }
        }
        throw new NoSuchMethodException();
    }

    private String getScreenshotRelativePath() {
//        try {
//            Method method = findMethodByAnnotation(Test.class);
//            return getSettings().getEnvironment().getRunOn().toLowerCase() +
//                    "/" + SeleniumHolder.getDriverName() +
//                    "/" + getClass().getName().replaceFirst(".*functional\\.", "").replace('.', '/') +
//                    "@" + method.getName();
//
//        } catch (NoSuchMethodException e) {
//            throw new AssertionError("Test method cannot be founded");
//        }
        return "";
    }

    public String getScreenshotPath() {
        return getClass().getResource("/").getPath().replaceFirst("e4testlink-selenium-tests.*", "screenshots/" + getScreenshotRelativePath());
    }

    public String getComparativePath() {
        String screenshotRelativePath = getScreenshotRelativePath();
        if (screenshotRelativePath.length() <= ScreenshotHelper.MAX_FOLDER_NAME_LENGTH) {
            screenshotRelativePath = screenshotRelativePath.replaceAll("/", ".");
        }
        return getClass().getResource("/").getPath().replaceFirst("e4testlink-selenium-tests.*", "screenshots/comparative/" + screenshotRelativePath);
    }

    public int nextPass() {
        return ++passCounter;
    }

    @Override
    public ScreenshotWebDriverEventListener getScreenshotWebDriverEventListener() {
        return screenshotWebDriverEventListener;
    }

    public Method getTestMethod() {
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Test.class) != null) {
                return method;
            }
        }
        return null;
    }
}
