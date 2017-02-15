package com.wiley.autotest.cuanto.listener.testng;

import com.wiley.autotest.cuanto.CuantoAdapterException;
import com.wiley.autotest.cuanto.api.CuantoConnector;
import com.wiley.autotest.cuanto.api.TestOutcome;
import com.wiley.autotest.cuanto.api.TestResult;
import com.wiley.autotest.cuanto.api.TestRun;
import com.wiley.autotest.cuanto.util.ArgumentParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IClass;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class TestNgListener implements IResultListener {

    private static final Logger logger = LoggerFactory.getLogger(TestNgListener.class);
    private static final ThreadLocal<TestNgListenerArguments> testNgListenerArguments = new ThreadLocal<TestNgListenerArguments>() {
                @Override
                protected TestNgListenerArguments initialValue() {
                    return new TestNgListenerArguments();
                }
            };
    private static final ThreadLocal<Long> configDuration = new ThreadLocal<Long>() {
                @Override
                protected Long initialValue() {
                    return 0L;
                }
            };
    // in order to update either the cuantoUrl or the testNgListenerArguments,
    // the thread must synchronize on adapterModificationLock
    private static final Object adapterModificationLock = new Object();
    private static TestNgListenerArguments failoverTestNgListenerArguments;

    /**
     * Construct a TestNgListener.
     * <p/>
     * The following environment variables are used to connect to Cuanto:
     * - cuanto.url: the url at which Cuanto is running; required
     * - cuanto.projectkey: the project key for which the tests run; required
     * - cuanto.testrun: the id of an existing TestRun to use; if null, a new TestRun will be created
     * - cuanto.testrun.properties: testProperties map in the form of key1:val1,key2:val2,...
     * - cuanto.testrun.links: links map in the form of cuanto:http://cuanto.codehaus.org,google:http://www.google.com
     *
     * @throws CuantoAdapterException      if the cuanto.url or cuanto.projectkey are not specified
     * @throws URISyntaxException if cuantoUrl is not a valid URI
     */
    public TestNgListener() throws CuantoAdapterException, URISyntaxException {
        failoverTestNgListenerArguments = getFailoverTestNgListenerArguments();
    }

    /**
     * @return cuantoUrl for the current thread
     */
    public static URI getCuantoUrl() {
        synchronized (adapterModificationLock) {
            URI currentThreadCuantoUrl = getTestNgListenerArguments().getCuantoUrl();
            URI resolvedCuantoUrl = (currentThreadCuantoUrl == null) ? failoverTestNgListenerArguments.getCuantoUrl() : currentThreadCuantoUrl;

            if (!isDefined(resolvedCuantoUrl)) {
                throw new IllegalArgumentException("The Cuanto URL must be defined.");
            }

            return resolvedCuantoUrl;
        }
    }

    /**
     * @return testRunId for the current thread
     */
    public static Long getTestRunId() {
        synchronized (adapterModificationLock) {
            Long currentThreadTestRunId = getTestNgListenerArguments().getTestRunId();
            return (currentThreadTestRunId == null) ? failoverTestNgListenerArguments.getTestRunId() : currentThreadTestRunId;
        }
    }

    /**
     * @return projectKey for the current thread
     */
    public static String getProjectKey() {
        synchronized (adapterModificationLock) {
            String currentThreadProjectKey = getTestNgListenerArguments().getProjectKey();
            String resolvedProjectKey = (currentThreadProjectKey == null) ? failoverTestNgListenerArguments.getProjectKey() : currentThreadProjectKey;

            if (!isDefined(resolvedProjectKey)) {
                throw new IllegalArgumentException("The Project key must be defined.");
            }
            return resolvedProjectKey;
        }
    }

    /**
     * @return links for the current thread
     */
    public static Map<String, String> getLinks() {
        synchronized (adapterModificationLock) {
            Map<String, String> currentThreadLinks = getTestNgListenerArguments().getLinks();
            return (currentThreadLinks == null) ? failoverTestNgListenerArguments.getLinks() : currentThreadLinks;
        }
    }

    /**
     * @return testProperties for the current thread
     */
    public static Map<String, String> getTestProperties() {
        synchronized (adapterModificationLock) {
            Map<String, String> currentThreadTestProperties = getTestNgListenerArguments().getTestProperties();
            return (currentThreadTestProperties == null) ? failoverTestNgListenerArguments.getTestProperties() : currentThreadTestProperties;
        }
    }

    /**
     * @return isCreateTestRun for the current thread
     */
    public static Boolean isCreateTestRun() {
        synchronized (adapterModificationLock) {
            Boolean currentThreadCreateTestRun = getTestNgListenerArguments().isCreateTestRun();
            return (currentThreadCreateTestRun == null) ? failoverTestNgListenerArguments.isCreateTestRun() : currentThreadCreateTestRun;
        }
    }

    /**
     * @return isIncludeConfigDuration for the current thread
     */
    public static Boolean isIncludeConfigDuration() {
        synchronized (adapterModificationLock) {
            Boolean currentThreadIncludeConfigDuration = getTestNgListenerArguments().isIncludeConfigDuration();
            return (currentThreadIncludeConfigDuration == null) ? failoverTestNgListenerArguments.isIncludeConfigDuration() : currentThreadIncludeConfigDuration;
        }
    }

    /**
     * @return the cloned testNgListenerArguments for the current thread
     */
    public static TestNgListenerArguments getTestNgListenerArguments() {
        synchronized (adapterModificationLock) {
            return new TestNgListenerArguments(testNgListenerArguments.get());
        }
    }

    /**
     * @param args to set for the current thread
     */
    public static void setTestNgListenerArguments(TestNgListenerArguments args) {
        synchronized (adapterModificationLock) {
            TestNgListener.testNgListenerArguments.set(args);
        }
    }

    /**
     * Get the stack trace as String.
     *
     * @param throwable from which to get the stack trace
     * @return the stack trace of throwable
     */
    private static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        Writer stacktrace = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stacktrace);
        throwable.printStackTrace(printWriter);
        return stacktrace.toString();
    }

    /**
     * Parse the system environment properties to construct a TestNgListenerArguments to which to fail over.
     *
     * @return failover TestNgListenerArguments
     * @throws URISyntaxException if cuanto.url is a malformed URI
     */
    private static TestNgListenerArguments getFailoverTestNgListenerArguments() throws URISyntaxException {
        TestNgListenerArguments args = new TestNgListenerArguments();

        // parse environment variables
        String cuantoUrl = System.getProperty("cuanto.url");
        String cuantoProjectKey = System.getProperty("cuanto.projectkey");
        String cuantoTestRun = System.getProperty("cuanto.testrun");
        String testRunPropertiesString = System.getProperty("cuanto.testrun.properties");
        String testRunLinksString = System.getProperty("cuanto.testrun.links");
        String cuantoCreateTestRun = System.getProperty("cuanto.testrun.create");
        String includeConfigDuration = System.getProperty("cuanto.includeConfigDuration");

        Map<String, String> testRunProperties = ArgumentParser.parseMap(testRunPropertiesString);
        Map<String, String> testRunLinks = ArgumentParser.parseMap(testRunLinksString);

        args.setCuantoUrl(isDefined(cuantoUrl) ? new URI(cuantoUrl) : null);
        args.setTestRunId(isDefined(cuantoTestRun) ? Long.valueOf(cuantoTestRun) : null);
        args.setCreateTestRun(isDefined(cuantoCreateTestRun) ? Boolean.valueOf(cuantoCreateTestRun) : null);
        args.setIncludeConfigDuration(isDefined(includeConfigDuration) ? Boolean.valueOf(includeConfigDuration) : null);
        args.setProjectKey(isDefined(cuantoProjectKey) ? cuantoProjectKey : null);
        args.setTestProperties(isDefined(testRunProperties) ? testRunProperties : null);
        args.setLinks(isDefined(testRunLinks) ? testRunLinks : null);
        return args;
    }

    /**
     * Determine whether the given Cuanto property is defined.
     * <p/>
     * If the property is null or its string representation trims to an empty string, it is not defined.
     * Otherwise, it is defined.
     *
     * @param property to determine whether it is defined.
     * @return whether the given property is defined.
     */
    private static boolean isDefined(Object property) {
        return property != null && !property.toString().trim().equals("");
    }

    /**
     * {@inheritDoc}
     */
    public void onTestStart(ITestResult iTestResult) {
    }

    /**
     * {@inheritDoc}
     */
    public void onTestSuccess(ITestResult iTestResult) {
        createTestOutcome(iTestResult, TestResult.Pass);
    }

    /**
     * {@inheritDoc}
     */
    public void onTestFailure(ITestResult iTestResult) {
        createTestOutcome(iTestResult, TestResult.Fail);
    }

    /**
     * {@inheritDoc}
     */
    public void onTestSkipped(ITestResult iTestResult) {
        createTestOutcome(iTestResult, TestResult.Skip);
    }

    /**
     * {@inheritDoc}
     */
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        createTestOutcome(iTestResult, TestResult.Pass);
    }

    /**
     * {@inheritDoc}
     */
    public void onStart(ITestContext iTestContext) {
    }

    /**
     * {@inheritDoc}
     */
    public void onFinish(ITestContext iTestContext) {
    }

    /**
     * {@inheritDoc}
     */
    public void onConfigurationSkip(ITestResult iTestResult) {
        if (isIncludeConfigDuration()) {
            incrementTotalDuration(iTestResult);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onConfigurationFailure(ITestResult iTestResult) {
        if (isIncludeConfigDuration()) {
            incrementTotalDuration(iTestResult);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void onConfigurationSuccess(ITestResult iTestResult) {
        if (isIncludeConfigDuration()) {
            incrementTotalDuration(iTestResult);
        }
    }

    private void incrementTotalDuration(ITestResult iTestResult) {
        Long elapsedTime = iTestResult.getEndMillis() - iTestResult.getStartMillis();
        configDuration.set(configDuration.get() + elapsedTime);
    }

    /**
     * Prepare the TestRun to be used to post test outcomes.
     * <p/>
     * If cuanto.testrun is provided, attempt to parse that to Long. If not, create a new TestRun and use its id.
     *
     * @param cuanto Cuanto connector
     * @param args   to use to determine the current test run
     * @return the determined TestRun
     */
    private TestRun determineTestRunId(CuantoConnector cuanto, TestNgListenerArguments args) {

        Long testRunId = getTestRunId();

        if (testRunId == null && isCreateTestRun()) {
            logger.info("TestRun id was not provided. Creating a new TestRun...");
            testRunId = createTestRun(cuanto, getProjectKey());
            logger.info("Created TestRun #" + testRunId);
            args.setTestRunId(testRunId);
            setTestNgListenerArguments(args);
        }

        if (testRunId == null) {
            return null;
        }

        TestRun testRun = cuanto.getTestRun(testRunId);

        Map<String, String> testProperties = getTestProperties();
        if (testProperties != null) {
            for (Map.Entry<String, String> testPropertyEntry : testProperties.entrySet()) {
                testRun.addTestProperty(testPropertyEntry.getKey(), testPropertyEntry.getValue());
            }
        }

        Map<String, String> links = getLinks();
        if (links != null) {
            for (Map.Entry<String, String> linkEntry : links.entrySet()) {
                testRun.addLink(linkEntry.getValue(), linkEntry.getKey());
            }
        }

        cuanto.updateTestRun(testRun);
        return testRun;
    }

    /**
     * Create a new TestRun for the given project key.
     *
     * @param cuanto           the Cuanto connector to use to either retrieve the test run, if applicable, or create a new one
     * @param cuantoProjectKey for which to create a new TestRun
     * @return the id of the created TestRun
     */
    private Long createTestRun(CuantoConnector cuanto, String cuantoProjectKey) {
        TestRun testRun = new TestRun(cuantoProjectKey);
        testRun.setDateExecuted(new Date());
        testRun.setNote("Created by " + this.getClass().getSimpleName());
        return cuanto.addTestRun(testRun);
    }

    /**
     * Create a TestOutcome appropriate for the current test.
     *
     * @param testCaseResult   ITestResult
     * @param cuantoTestResult TestResult
     */
    private void createTestOutcome(ITestResult testCaseResult, TestResult cuantoTestResult) {
        IClass testClass = testCaseResult.getTestClass();

        // todo: TestNG bug???
        // if the test class overrode getTestName(), the customized test name should be used (testClass.getTestName()).
        // however, even if ITest is not implemented, a test returns non-null value.
        // so, just use the testCaseResult.getName() for now, which is just the test method name.
        String testCaseName = testCaseResult.getName();

        // package name in Cuanto is the fully-qualified test class name (e.g., cuanto.foo.MyTest)
        String packageName = testClass.getRealClass().getName();
        Object[] testCaseParameters = testCaseResult.getParameters();

        TestOutcome testOutcome = TestOutcome.newInstance(
                packageName, testCaseName, testCaseParameters, cuantoTestResult);
        String[] tags = testCaseResult.getMethod().getGroups();
        long duration = testCaseResult.getEndMillis() - testCaseResult.getStartMillis();

        TestNgListenerArguments arguments = getTestNgListenerArguments();
        duration += configDuration.get();
        configDuration.set(0l);

        if (cuantoTestResult != TestResult.Pass) {
            testOutcome.setTestOutput(getTestOutput(testCaseResult));
        }
        testOutcome.addTags(Arrays.asList(tags));
        testOutcome.setDuration(duration);

        // because the user may have modified the Cuanto url or the test run arguments,
        // lazily create the cuanto connector and determine the test run to which to submit this test outcome
        TestRun testRun = null;
        CuantoConnector cuanto = null;
        synchronized (adapterModificationLock) {
            String projectKey = getProjectKey();
            String cuantoUrl = getCuantoUrl().toString();
            cuanto = CuantoConnector.newInstance(cuantoUrl, projectKey);
            testRun = determineTestRunId(cuanto, arguments);
        }

        cuanto.addTestOutcome(testOutcome, testRun);
    }

    /**
     * Get the test output to store in the test outcome.
     *
     * @param testCaseResult result of the current test case
     * @return the stacktrace of the resulting exception
     */
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private String getTestOutput(ITestResult testCaseResult) {
        return getStackTrace(testCaseResult.getThrowable());
    }
}
