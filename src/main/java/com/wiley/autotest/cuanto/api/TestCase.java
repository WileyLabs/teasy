package com.wiley.autotest.cuanto.api;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * A class that represents a Cuanto server-side TestCase.
 */
public class TestCase {

    String projectKey;
    String packageName;
    String testName;
    String fullName;
    String parameters;
    String description;
    Long id;

	TestCase() {
	}

    /**
     * Gets the projectKey for this TestCases' project. Each TestCase is associated with a single project on the Cuanto
     * server. This value will only be set if the TestCase was retrieved from the Cuanto server.
     *
     * @return The projectKey of the TestCase.
     */
    public String getProjectKey() {
        return projectKey;
    }

    /**
     * Gets the packageName associated with this test. A package is a namespace for a particular test. In java (for
     * instance, JUnit and TestNG), it will most often correspond to the fully-qualified java class name of a particular
     * test method. For example, org.myorganization.my.package.TestClassName.
     *
     * @return The package name of this TestCase.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Gets the TestName associated with this TestCase. This will almost always be the name of the test method.
     *
     * @return The TestName of this TestCase.
     */
    public String getTestName() {
        return testName;
    }

    /**
     * Gets the fullName associated with this TestCase. The full name is usually the packageName plus the testName,
     * concatenated with a "."  Only TestCases retrieved from the Cuanto server will have this value set.
     *
     * @return The fullName of this TestCase or null if the TestCase was not retrieved from the server.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the parameters associated with this TestCase. Parameters is a string that should represent the actual parameters
     * passed to the TestCase. In the case of JUnit and TestNG, this is the parameters joined by ", ", for instance "arg1,
     * arg2, arg3".
     *
     * @return The parameters associated with this TestCase.
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * Gets the description of this TestCase. Description is an arbitrary string that can be associated with a TestCase.
     *
     * @return The description associated with this TestCase.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the server-assigned ID for this TestCase. TestCases will not have this value assigned unless they were retrieved
     * from the Cuanto server.
     *
     * @return The server-assigned ID for this TestCase or null if the TestCase was not retrieved from the server.
     */
    public Long getId() {
        return id;
    }

    void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    void setTestName(String testName) {
        this.testName = testName;
    }

    void setFullName(String fullName) {
        this.fullName = fullName;
    }

    void setParameters(String parameters) {
        this.parameters = parameters;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setId(Long id) {
        this.id = id;
    }

    /**
     * Determine if this TestCase is equal to another TestCase.
     *
     * @param otherTestCase The TestCase for comparison.
     * @return true if this TestCase is equal to the other TestCase, false otherwise.
     */
    @Override
    public boolean equals(Object otherTestCase) {
        if (this == otherTestCase) {
            return true;
        }
        if (otherTestCase == null || getClass() != otherTestCase.getClass()) {
            return false;
        }

        TestCase testCase = (TestCase) otherTestCase;

        if (description != null ? !description.equals(testCase.description) : testCase.description != null) {
            return false;
        }
        if (fullName != null ? !fullName.equals(testCase.fullName) : testCase.fullName != null) {
            return false;
        }
        if (id != null ? !id.equals(testCase.id) : testCase.id != null) {
            return false;
        }
        if (packageName != null ? !packageName.equals(testCase.packageName) : testCase.packageName != null) {
            return false;
        }
        if (parameters != null ? !parameters.equals(testCase.parameters) : testCase.parameters != null) {
            return false;
        }
        return !(projectKey != null ? !projectKey.equals(testCase.projectKey) : testCase.projectKey != null) && !(testName != null ? !testName.equals(testCase.testName) : testCase.testName != null);
    }

    @Override
    public int hashCode() {
        int result = projectKey != null ? projectKey.hashCode() : 0;
        result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
        result = 31 * result + (testName != null ? testName.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (parameters != null ? parameters.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    static TestCase fromJSON(String jsonString) {
        JSONObject jsonTestCase = JSONObject.fromObject(jsonString);
        return fromJSON(jsonTestCase);
    }

    static TestCase fromJSON(JSONObject jsonTestCase) {
        TestCase testCase = new TestCase();
        if (!(jsonTestCase.get("packageName") instanceof JSONNull)) {
            testCase.setPackageName(jsonTestCase.getString("packageName"));
        }
        testCase.setTestName(jsonTestCase.getString("testName"));

        if (!(jsonTestCase.get("parameters") instanceof JSONNull)) {
            testCase.setParameters(jsonTestCase.getString("parameters"));
        }
        if (!(jsonTestCase.get("description") instanceof JSONNull)) {
            testCase.setDescription(jsonTestCase.getString("description"));
        }
        if (!(jsonTestCase.get("id") instanceof JSONNull)) {
            testCase.setId(jsonTestCase.getLong("id"));
        }
        if (!(jsonTestCase.get("fullName") instanceof JSONNull)) {
            testCase.setFullName(jsonTestCase.getString("fullName"));
        }
        return testCase;
    }
}
