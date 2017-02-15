package com.wiley.autotest.cuanto.api;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the result of executing a TestCase, or perhaps an unexecuted TestCase.
 */
public class TestResult {

    private final String result;

    public static final TestResult Pass = new TestResult("Pass");
    public static final TestResult Fail = new TestResult("Fail");
    public static final TestResult Error = new TestResult("Error");
    public static final TestResult Ignore = new TestResult("Ignore");
    public static final TestResult Skip = new TestResult("Skip");
    public static final TestResult Unexecuted = new TestResult("Unexecuted");

    /**
     * Creates a new TestResult object for this string value. This does not create a TestResult on the Cuanto server, it
     * merely instantiates a TestResult object.
     *
     * @param result The string for this TestResult.
     */
    TestResult(String result) {
        this.result = result;
    }

    /**
     * @return The string value of this TestResult.
     */
    public String toString() {
        return result;
    }

    /**
     * Gets a list of known valid TestResults. These are the default TestResults that Cuanto provides, it is possible
     * that additional TestResults have been defined for the Cuanto instance.
     *
     * @return a list of known valid TestResults.
     */
    public static List<TestResult> getResultList() {
        return Arrays.asList(Pass, Fail, Error, Ignore, Skip, Unexecuted);
    }

    /**
     * Creates a TestResult for a custom result. This probably isn't what you want -- you should favor the static
     * TestResult members on this class or valueOf(). This method is here for the rare case when a Cuanto server has non-default
     * TestResults that aren't named the same as the static members on this class. If you specify a TestResult
     * that doesn't exist, you will get an error when you attempt to create a TestOutcome with that result.
     * Consider yourself warned.
     *
     * @param result The custom result name -- should match a TestResult on the server.
     * @return A TestResult corresponding to the specified name.
     */
    public static TestResult forResult(String result) {
        return new TestResult(result);
    }

    /**
     * Creates a TestResult for this result name. This only works for the static values defined on this TestResult class,
     * not custom TestResults -- see forResult() if you wish to use custom TestResults.
     *
     * @param result The string value of a TestResult. Case-insensitive.
     * @return The TestResult corresponding to the specified string.
     * @throws IllegalArgumentException if the result is not one of the defaults available.
     */
    public static TestResult valueOf(String result) throws IllegalArgumentException {
        for (TestResult testResult : getResultList()) {
            if (testResult.toString().equalsIgnoreCase(result)) {
                return testResult;
            }
        }
        throw new IllegalArgumentException("Unrecognized TestResult: " + result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestResult that = (TestResult) o;

        return !(result != null ? !result.equals(that.result) : that.result != null);

    }

    @Override
    public int hashCode() {
        return result != null ? result.hashCode() : 0;
    }
}
