package com.wiley.autotest.cuanto.api;


import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * A class that represents the TestOutcome of a particular TestCase. A TestOutcome is the outcome of executing a
 * TestCase and it's associated analysis.
 */
public class TestOutcome {
	TestCase testCase;
	TestResult testResult;
	TestRun testRun;
	String testOutput;
	Long duration;
	String owner;
	Bug bug;
	String note;
	Long id;
	AnalysisState analysisState;
	Date startedAt;
	Date finishedAt;
	Date dateCreated;
	Date lastUpdated;
	String projectKey;
	List<String> tags = new ArrayList<String>();
	Boolean isFailureStatusChanged;
	Map<String, String> links = new HashMap<String, String>();
	Map<String, String> testProperties = new HashMap<String, String>();


	TestOutcome() {
		// no public constructor
	}


	/**
	 * Creates a new TestOutcome for the named test case. This is not added to the Cuanto server until
	 * CuantoConnector.addTestOutcome() is called.
	 *
	 * @param testCasePackageName The packageName of the TestCase. A package is a namespace for a particular test. In java
	 *                            (for instance, JUnit and TestNG), it will most often correspond to the fully-qualified
	 *                            java class name of a particular test method. For example, org.myorganization.my.package.TestClassName.
	 * @param testCaseTestName    The name of the TestCase. This is usually the method name of the test.
	 * @param testResult          The result of executing the TestCase.
	 * @return The new TestOutcome object.
	 */
	public static TestOutcome newInstance(String testCasePackageName, String testCaseTestName, TestResult testResult) {
		return newInstance(testCasePackageName, testCaseTestName, (String) null, testResult);
	}


	/**
	 * Creates a new TestOutcome for the named test case. This is not added to the Cuanto server until
	 * CuantoConnector.addTestOutcome() is called.
	 *
	 * @param testCasePackageName The packageName of the TestCase. A package is a namespace for a particular test. In java
	 *                            (for instance, JUnit and TestNG), it will most often correspond to the fully-qualified
	 *                            java class name of a particular test method. For example, org.myorganization.my.package.TestClassName.
	 * @param testCaseTestName    The name of the TestCase. This is usually the method name of the test.
	 * @param testCaseParameters  The parameters of this TestCase. Parameters is a string that should represent the actual
	 *                            parameters passed to the TestCase. In the case of JUnit and TestNG, this is the
	 *                            parameters joined by ", ", for instance "arg1, arg2, arg3".
	 * @param testResult          The result of executing the TestCase.
	 * @return The new TestOutcome object.
	 */
	public static TestOutcome newInstance(String testCasePackageName, String testCaseTestName,
										  String testCaseParameters,
										  TestResult testResult) {
		TestCase testCase = new TestCase();

		testCase = new TestCase();
		testCase.setPackageName(testCasePackageName);
		testCase.setTestName(testCaseTestName);
		testCase.setParameters(testCaseParameters);

		TestOutcome testOutcome = new TestOutcome();
		testOutcome.testCase = testCase;
		testOutcome.testResult = testResult;
		return testOutcome;
	}


	/**
	 * Creates a new TestOutcome for the named test case. This is not added to the Cuanto server until
	 * CuantoConnector.addTestOutcome() is called.
	 *
	 * @param testCasePackageName The packageName of the TestCase. A package is a namespace for a particular test. In java
	 *                            (for instance, JUnit and TestNG), it will most often correspond to the fully-qualified
	 *                            java class name of a particular test method. For example, org.myorganization.my.package.TestClassName.
	 * @param testCaseTestName    The name of the TestCase. This is usually the method name of the test.
	 * @param testCaseParameters  The parameters of this TestCase.
	 * @param testResult          The result of executing the TestCase.
	 * @return The new TestOutcome object.
	 */
	public static TestOutcome newInstance(String testCasePackageName, String testCaseTestName,
										  Object[] testCaseParameters,
										  TestResult testResult) {

		StringBuilder sb = new StringBuilder();
		if (testCaseParameters != null && testCaseParameters.length > 0) {
			Object firstParam = testCaseParameters[0];
			sb.append(resolveTestCaseParameterName(firstParam));
			for (int i = 1; i < testCaseParameters.length; ++i) {
				sb.append(", ");
				Object param = testCaseParameters[i];
				sb.append(resolveTestCaseParameterName(param));
			}
		}

		return newInstance(testCasePackageName, testCaseTestName, sb.toString(), testResult);
	}

	/**
	 * Gets the TestCase associated with this TestOutcome.
	 *
	 * @return The TestCase associated with this TestOutcome.
	 */
	public TestCase getTestCase() {
		return testCase;
	}


	/**
	 * Gets the TestResult associated with this TestOutcome.
	 *
	 * @return The TestResult associated with this TestOutcome.
	 */
	public TestResult getTestResult() {
		return testResult;
	}


	/**
	 * Gets the test output associated with this TestOutcome, if it is available. TestOutcomes retrieved from the Cuanto
	 * server do not return the test output by default. You can fetch the test output for a particular TestOutcome by
	 * calling CuantoConnector.getTestOutput(). Note that normally output is only recorded for test failures.
	 *
	 * @return The test output if available, null otherwise.
	 */
	public String getTestOutput() {
		return testOutput;
	}


	/**
	 * Gets the duration of this TestOutcome's execution in milliseconds.
	 *
	 * @return The duration of this TestOutcome in milliseconds.
	 */
	public Long getDuration() {
		return duration;
	}


	/**
	 * Gets the owner of this TestOutcome. Owner is just an arbitrary string, it can be set to anything.
	 *
	 * @return The owner of this TestOutcome.
	 */
	public String getOwner() {
		return owner;
	}


	/**
	 * Gets the bug associated with this TestOutcome, if any.
	 *
	 * @return The bug associated with this TestOutcome or null if there isn't one.
	 */
	public Bug getBug() {
		return bug;
	}


	/**
	 * Gets the note associated with this TestOutcome or null if there isn't one.
	 *
	 * @return The note associated with this TestOutcome or null if there isn't one.
	 */
	public String getNote() {
		return note;
	}


	/**
	 * Gets the server-assigned ID for this TestOutcome. This will only be populated if the TestOutcome was fetched from
	 * the Cuanto server.
	 *
	 * @return The server-assigned ID for this TestOutcome, or null if there isn't one.
	 */
	public Long getId() {
		return id;
	}


	/**
	 * Gets the analysis state associated with this TestOutcome, if any.
	 *
	 * @return The analysis state for this TestOutcome.
	 */
	public AnalysisState getAnalysisState() {
		return analysisState;
	}


	/**
	 * Gets the time this TestOutcome's execution was started, if available.
	 *
	 * @return The time this TestOutcome's execution was started, or null if unavailable.
	 */
	public Date getStartedAt() {
		return startedAt;
	}


	/**
	 * Gets the time this TestOutcome's execution was finished, if available.
	 *
	 * @return The time this TestOutcome's execution was finished, or null if unavailable.
	 */
	public Date getFinishedAt() {
		return finishedAt;
	}


	/**
	 * Gets the time this TestOutcome was created on the Cuanto server.
	 *
	 * @return The time this TestOutcome was created on the Cuanto server, or null if not available.
	 */
	public Date getDateCreated() {
		return dateCreated;
	}


	/**
	 * Gets the time this TestOutcome was last updated on the Cuanto server.
	 *
	 * @return The time this TestOutcome was last updated on the Cuanto server, or null if not available.
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}


	void setTestCase(TestCase testCase) {
		this.testCase = testCase;
	}


	/**
	 * Sets the TestResult for this TestOutcome.
	 *
	 * @param testResult The TestResult.
	 */
	public void setTestResult(TestResult testResult) {
		this.testResult = testResult;
	}


	/**
	 * Sets the output for this TestOutcome. By convention, the output should only be provided if the test failed or
	 * otherwise did not successfully pass.
	 *
	 * @param testOutput The output of the test failure.
	 */
	public void setTestOutput(String testOutput) {
		this.testOutput = testOutput;
	}


	/**
	 * Sets the duration of the execution for this TestOutcome.
	 *
	 * @param duration The duration of the execution in milliseconds.
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}


	/**
	 * Sets the owner of this TestOutcome. Owner is just an arbitrary string, it can be set to anything.
	 *
	 * @param owner The owner.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}


	/**
	 * Sets the bug associated with this TestOutcome.
	 *
	 * @param bug The bug.
	 */
	public void setBug(Bug bug) {
		this.bug = bug;
	}


	/**
	 * Sets the note associated with this TestOutcome.
	 *
	 * @param note The note.
	 */
	public void setNote(String note) {
		this.note = note;
	}


	void setId(Long id) {
		this.id = id;
	}


	/**
	 * Sets the analysis state associated with this TestOutcome.
	 *
	 * @param analysisState The analysis state.
	 */
	public void setAnalysisState(AnalysisState analysisState) {
		this.analysisState = analysisState;
	}


	/**
	 * Sets the time this TestOutcome's execution started.
	 *
	 * @param startedAt The time this TestOutcome's execution started.
	 */
	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}


	/**
	 * Sets the time this TestOutcome's execution finished.
	 *
	 * @param finishedAt The time this TestOutcome's execution finished.
	 */
	public void setFinishedAt(Date finishedAt) {
		this.finishedAt = finishedAt;
	}


	void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


	/**
	 * Gets the TestRun associated with this TestOutcome, if any.
	 *
	 * @return The TestRun associated with this TestOutcome, or null if there is none.
	 */
	public TestRun getTestRun() {
		return testRun;
	}


	void setTestRun(TestRun testRun) {
		this.testRun = testRun;
	}


	void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}


	String getProjectKey() {
		return projectKey;
	}

	public Boolean getFailureStatusChanged() {
		return isFailureStatusChanged;
	}

	void setFailureStatusChanged(Boolean failureStatusChanged) {
		isFailureStatusChanged = failureStatusChanged;
	}

	static TestOutcome fromJSON(String jsonString) throws ParseException {
		JSONObject jsonOutcome = JSONObject.fromObject(jsonString);
		return fromJSON(jsonOutcome);
	}


	static TestOutcome fromJSON(JSONObject jsonOutcome) throws ParseException {
		TestOutcome testOutcome = new TestOutcome();
		testOutcome.setId(jsonOutcome.getLong("id"));

		TestCase testCase = new TestCase();
		JSONObject jsonTestCase = jsonOutcome.getJSONObject("testCase");
		testCase = TestCase.fromJSON(jsonTestCase);
		testOutcome.setTestCase(testCase);

		testOutcome.setTestResult(TestResult.forResult(jsonOutcome.getString("result")));

		if (!jsonOutcome.getJSONObject("bug").isNullObject()) {
			Bug bug = new Bug();
			JSONObject jsonBug = jsonOutcome.getJSONObject("bug");
			if (!(jsonBug.get("title") instanceof JSONNull)) {
				bug.setTitle(jsonBug.getString("title"));
			}
			if (!(jsonBug.get("url") instanceof JSONNull)) {
				bug.setUrl(jsonBug.getString("url"));
			}
			if (!(jsonBug.get("id") instanceof JSONNull)) {
				bug.setId(jsonBug.getLong("id"));
			}
			testOutcome.setBug(bug);
		}

		if (!(jsonOutcome.get("duration") instanceof JSONNull)) {
			testOutcome.setDuration(jsonOutcome.getLong("duration"));
		}

		if (!(jsonOutcome.get("startedAt") instanceof JSONNull)) {
			testOutcome.setStartedAt(parseJsonDate(jsonOutcome.getString("startedAt")));
		}
		if (!(jsonOutcome.get("finishedAt") instanceof JSONNull)) {
			testOutcome.setFinishedAt(parseJsonDate(jsonOutcome.getString("finishedAt")));
		}
		if (!(jsonOutcome.get("dateCreated") instanceof JSONNull)) {
			testOutcome.setDateCreated(parseJsonDate(jsonOutcome.getString("dateCreated")));
		}
		if (!(jsonOutcome.get("lastUpdated") instanceof JSONNull)) {
			testOutcome.setLastUpdated(parseJsonDate(jsonOutcome.getString("lastUpdated")));
		}

		if (!(jsonOutcome.get("analysisState") instanceof JSONNull)) {
			testOutcome.setAnalysisState(AnalysisState.forState(jsonOutcome.getString("analysisState")));
		}

		if (!(jsonOutcome.get("owner") instanceof JSONNull)) {
			testOutcome.setOwner(jsonOutcome.getString("owner"));
		}

		if (!(jsonOutcome.get("note") instanceof JSONNull)) {
			testOutcome.setNote(jsonOutcome.getString("note"));
		}

		if (!jsonOutcome.getJSONObject("testRun").isNullObject()) {
			testOutcome.setTestRun(TestRun.fromJSON(jsonOutcome.getJSONObject("testRun").toString()));
		}

		if ((jsonOutcome.get("tags") != null) && !(jsonOutcome.get("tags") instanceof JSONNull)) {
			JSONArray tagArray = jsonOutcome.getJSONArray("tags");
			testOutcome.addTags(tagArray);
		}

		if (!(jsonOutcome.get("isFailureStatusChanged") instanceof JSONNull)) {
			testOutcome.setFailureStatusChanged(
				jsonOutcome.getBoolean("isFailureStatusChanged"));
		}

		if (!(jsonOutcome.get("testProperties") instanceof JSONNull)) {
			JSONObject propObj = jsonOutcome.getJSONObject("testProperties");
			for (Object propKeyObj : propObj.keySet()) {
				String propName = (String) propKeyObj;
				String propValue = propObj.getString(propName);
				testOutcome.addTestProperty(propName, propValue);
			}
		}

		if (!(jsonOutcome.get("links") instanceof JSONNull)) {
			JSONObject linkObj = jsonOutcome.getJSONObject("links");
			for (Object linkKeyObj : linkObj.keySet()) {
				String linkUrl = (String) linkKeyObj;
				String linkDesc = linkObj.getString(linkUrl);
				testOutcome.addLink(linkUrl, linkDesc);
			}
		}
		return testOutcome;
	}


	/**
	 * Creates a JSON representation of this TestOutcome.
	 *
	 * @return a JSON string that represents this TestOutcome.
	 */
	public String toJSON() {
		Map jsonMap = new HashMap();
		if (this.getProjectKey() != null) {
			jsonMap.put("projectKey", this.getProjectKey());
		}

		if (this.getId() != null) {
			jsonMap.put("id", this.getId());
		}
		if (this.bug != null) {
			Map bugMap = new HashMap();
			if (this.bug.getId() != null) {
				bugMap.put("id", this.bug.getId());
			}
			if (this.bug.getTitle() != null) {
				bugMap.put("title", this.bug.getTitle());
			}
			if (this.bug.getUrl() != null) {
				bugMap.put("url", this.bug.getUrl());
			}
			jsonMap.put("bug", bugMap);
		}

		String analysisStr = null;
		if (this.analysisState != null) {
			analysisStr = this.analysisState.toString();
		}
		jsonMap.put("analysisState", analysisStr);

		Map testCaseMap = new HashMap();
		if (this.testCase.getId() != null) {
			testCaseMap.put("id", this.testCase.getId());
		} else {
			testCaseMap.put("testName", this.testCase.getTestName());
			testCaseMap.put("packageName", this.testCase.getPackageName());

			if (this.testCase.getParameters() != null) {
				testCaseMap.put("parameters", this.testCase.getParameters());
			}

			if (this.testCase.getDescription() != null) {
				testCaseMap.put("description", this.testCase.getDescription());
			}
		}
		jsonMap.put("testCase", testCaseMap);

		jsonMap.put("result", this.testResult.toString());
		if (this.owner != null) {
			jsonMap.put("owner", this.owner);
		}

		if (this.note != null) {
			jsonMap.put("note", this.note);
		}

		if (this.testOutput != null) {
			jsonMap.put("testOutput", this.testOutput);
		}

		if (this.duration != null) {
			jsonMap.put("duration", this.duration);
		}

		if (this.startedAt != null) {
			jsonMap.put("startedAt", toJsonDate(startedAt));
		}

		if (this.finishedAt != null) {
			jsonMap.put("finishedAt", toJsonDate(finishedAt));
		}

		if (this.isFailureStatusChanged != null) {
			jsonMap.put("isFailureStatusChanged", isFailureStatusChanged);
		}

		if (this.testRun != null) {
			Map runMap = new HashMap();
			runMap.put("id", this.testRun.getId());
			jsonMap.put("testRun", runMap);
		}

		if (this.tags.size() > 0) {
			jsonMap.put("tags", tags);
		}

		if (this.testProperties.size() > 0) {
			jsonMap.put("testProperties", this.testProperties);
		}

		if (this.links.size() > 0) {
			jsonMap.put("links", this.links);
		}

		JSONObject jsonTestOutcome = JSONObject.fromObject(jsonMap);
		return jsonTestOutcome.toString();
	}


	/**
	 * Get all the Tags for this TestOutcome.
	 *
	 * @return an unmodifiable list of all the Tags.
	 */
	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}


	/**
	 * Add a tag to this TestOutcome.
	 *
	 * @param tag The tag to add.
	 */
	public void addTag(String tag) {
		tags.add(tag);
	}


	/**
	 * Add tags to this TestOutcome.
	 *
	 * @param tags The tags to add.
	 */
	public void addTags(List<String> tags) {
		this.tags.addAll(tags);
	}


	/**
	 * Adds or updates a TestProperty. This change is not reflected on the Cuanto service until you create or update the
	 * TestOutcome with the CuantoConnector.
	 *
	 * @param name  The name of the property
	 * @param value The value of the property
	 * @return The TestOutcome associated with the TestProperty
	 */
	public TestOutcome addTestProperty(String name, String value) {
		testProperties.put(name, value);
		return this;
	}


	/**
	 * Deletes a TestProperty from this TestOutcome object. This change is not reflected on the Cuanto server until you create
	 * or update the TestOutcome with the CuantoConnector.
	 *
	 * @param name The TestProperty name to delete.
	 */
	public void deleteTestProperty(String name) {
		testProperties.remove(name);
	}


	/**
	 * Adds or updates a Link. This change is not reflected on the Cuanto server until you create or update the TestOutcome with
	 * the CuantoConnector.
	 *
	 * @param url         The url of the link
	 * @param description The description of the link
	 * @return The TestOutcome associated with this link
	 */
	public TestOutcome addLink(String url, String description) {
		links.put(url, description);
		return this;
	}


	private static Date parseJsonDate(String dateString) throws ParseException {
		if (dateString == null) {
			return null;
		} else {
			return new SimpleDateFormat(CuantoConnector.JSON_DATE_FORMAT).parse(dateString);
		}
	}


	private String toJsonDate(Date date) {
		if (date == null) {
			return null;
		} else {
			return new SimpleDateFormat(CuantoConnector.JSON_DATE_FORMAT).format(date);
		}
	}


	/**
	 * Resolve the test case parameter name to some user-friendly String.
	 * <p/>
	 * If the parameter is null, then "null" will be its name.
	 *
	 * @param param to resolve the parameter name
	 * @return user-friendly String describing the specified parameter
	 */
	private static String resolveTestCaseParameterName(Object param) {
		return param == null
			? "null"
			: param.toString();
	}


	public enum Sort {
		ANALYSIS_STATE,
		BUG,
		DATE_CREATED,
		DURATION,
		FINISHED_AT,
		FULL_NAME,
		ID,
		LAST_UPDATED,
		NOTE,
		OWNER,
		STARTED_AT,
		TEST_OUTPUT,
		TEST_RESULT
	}
}
