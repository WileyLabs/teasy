package com.wiley.autotest.cuanto.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CuantoConnector is the primary class for interacting with the Cuanto server remotely. A CuantoConnector instance
 * is always associated with a particular project, which is set when creating a new instance of the CuantoConnector.
 */
public class CuantoConnector {

	/**
	 * The Date format which this connector and it's associated objects use and expect for JSON serialization and
	 * deserialization.
	 */
	public static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private static final String HTTP_USER_AGENT = "Java CuantoConnector 2.8.0; Jakarta Commons-HttpClient/3.1";

	private static final String HTTP_GET = "get";
	private static final String HTTP_POST = "post";

	private String projectKey;
	private String cuantoUrl;
	private String proxyHost;
	private Integer proxyPort;


	private CuantoConnector() {
		// must use factory method to instantiate
	}


	/**
	 * Creates a new instance of CuantoConnector that connects to the specified URL.
	 *
	 * @param cuantoServerUrl The URL of the Cuanto server instance.
	 * @return The new CuantoConnector instance.
	 */
	public static CuantoConnector newInstance(String cuantoServerUrl) {
		return newInstance(cuantoServerUrl, null, null, null);
	}


	/**
	 * Creates a new instance of CuantoConnector that connects to the specified URL via a HTTP proxy server.
	 *
	 * @param cuantoServerUrl The URL of the Cuanto server instance.
	 * @param proxyHost       The hostname of the HTTP proxy.
	 * @param proxyPort       The port for the HTTP proxy.
	 * @return The new CuantoConnector instance.
	 */
	public static CuantoConnector newInstance(String cuantoServerUrl, String proxyHost, Integer proxyPort) {
		return newInstance(cuantoServerUrl, null, proxyHost, proxyPort);
	}



	/**
	 * Creates a new instance of CuantoConnector that connects to the specified URL and Cuanto project.
	 *
	 * @param cuantoServerUrl The URL of the Cuanto server instance.
	 * @param projectKey      The key for the project that this client will be utilizing.
	 * @return The new CuantoConnector instance.
	 */
	public static CuantoConnector newInstance(String cuantoServerUrl, String projectKey) {
		return newInstance(cuantoServerUrl, projectKey, null, null);
	}


	/**
	 * Creates a new instance of CuantoConnector that connects to the specified URL and Cuanto project via a HTTP proxy
	 * server.
	 *
	 * @param cuantoServerUrl The URL of the Cuanto server instance.
	 * @param projectKey      The key for the project that this client will be utilizing.
	 * @param proxyHost       The hostname of the HTTP proxy.
	 * @param proxyPort       The port for the HTTP proxy.
	 * @return The new CuantoConnector instance.
	 */
	public static CuantoConnector newInstance(String cuantoServerUrl, String projectKey, String proxyHost,
											  Integer proxyPort) {
		CuantoConnector connector = new CuantoConnector();
		connector.setCuantoUrl(cuantoServerUrl);
		connector.setProjectKey(projectKey);
		connector.setProxyHost(proxyHost);
		connector.setProxyPort(proxyPort);
		return connector;
	}


	/**
	 * Gets the TestRun from the Cuanto server.
	 *
	 * @param testRunId The TestRun to retrieve.
	 * @return The retrieved TestRun.
	 */
	public TestRun getTestRun(Long testRunId) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestRun/" + testRunId.toString());

		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return TestRun.fromJSON(getResponseBodyAsString(get));
			} else {
				throw new RuntimeException("Getting the TestRun failed with HTTP status code " + httpStatus + ":\n" +
					getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response", e);
		}
	}


	/**
	 * Creates a new TestRun on the Cuanto server using the values provided. A TestRun represents tests that were
	 * executed together. The projectKey will be assigned the same projectKey as this CuantoConnector. The testRun
	 * passed in will have it's id value assigned to the server-assigned ID of the created TestRun.
	 *
	 * @param testRun The test run to create.
	 * @return The server-assigned ID of the created TestRun.
	 */
	public Long addTestRun(TestRun testRun) {
		testRun.setProjectKey(getProjectKey());
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/addTestRun");
		try {
			post.setRequestEntity(new StringRequestEntity(testRun.toJSON(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus == HttpStatus.SC_CREATED) {
				TestRun created = TestRun.fromJSON(getResponseBodyAsString(post));
				testRun.setProjectKey(this.projectKey);
				testRun.setId(created.getId());
				return created.getId();
			} else {
				throw new RuntimeException("Adding the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing server response", e);
		}
	}


	public Long addProject(Project project) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/addProject");
		try {
			post.setRequestEntity(new StringRequestEntity(project.toJSON(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus == HttpStatus.SC_CREATED) {
				Project created = Project.fromJSON(getResponseBodyAsString(post));
				project.setId(created.getId());
				return created.getId();
			} else {
				throw new RuntimeException("Adding the Project failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing server response", e);
		}
	}


	/**
	 * @return An HTTP client, optionally configured to use a proxy.
	 */
	private HttpClient getHttpClient() {
		HttpClient client = new HttpClient();
		if (getProxyHost() != null && getProxyPort() != null) {
			client.getHostConfiguration().setProxy(getProxyHost(), getProxyPort());
		}
		return client;
	}


	/**
	 * @param methodType HTTP_GET or HTTP_POST
	 * @param url        The URL for this method to contact
	 * @return The HTTP method configured with the correct User-Agent header.
	 */
	private HttpMethod getHttpMethod(String methodType, String url) {
		HttpMethod method;
		if (methodType.toLowerCase().equals(HTTP_GET)) {
			method = new GetMethod(url);
		} else if (methodType.toLowerCase() == HTTP_POST) {
			method = new PostMethod(url);
		} else {
			throw new RuntimeException("Unknown HTTP method: ${methodType}");
		}
		method.setRequestHeader("User-Agent", HTTP_USER_AGENT);
		return method;
	}


	/**
	 * Updates the TestRun with this id on the Cuanto Server to have all the properties specified in this TestRun. If the
	 * TestRun argument does not already have an id, it needs to be retrieved from the server as you can't set the ID on a
	 * TestRun directly. You can either retrieve the TestRun from the server by querying by ID or other values. If the
	 * TestRun does not already exist, then use createTestRun instead.
	 *
	 * @param testRun a TestRun with the updated values.
	 */
	public void updateTestRun(TestRun testRun) {
		if (testRun == null) {
			throw new NullPointerException("null is not a valid testRunId");
		}

		testRun.setProjectKey(getProjectKey());
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/updateTestRun");
		try {
			post.setRequestEntity(new StringRequestEntity(testRun.toJSON(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus != HttpStatus.SC_OK) {
				throw new RuntimeException("Adding the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Creates a new TestOutcome for the specified TestRun on the Cuanto server using the details provided.  The ID value on
	 * the testOutcome argument will be set upon successful creation.
	 *
	 * @param testOutcome The TestOutcome to be created on the Cuanto server.
	 * @param testRun     The TestRun to which the TestOutcome should be added.
	 * @return The server-assigned ID of the TestOutcome.
	 */
	public Long addTestOutcome(TestOutcome testOutcome, TestRun testRun) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/addTestOutcome");
		try {
			testOutcome.setTestRun(testRun);
			testOutcome.setProjectKey(this.projectKey);
			post.setRequestEntity(new StringRequestEntity(testOutcome.toJSON(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus == HttpStatus.SC_CREATED) {
				TestOutcome fetchedOutcome = TestOutcome.fromJSON(getResponseBodyAsString(post));
				testOutcome.setId(fetchedOutcome.getId());
				testOutcome.getTestCase().setId(fetchedOutcome.getTestCase().getId());
				return fetchedOutcome.getId();
			} else {
				throw new RuntimeException("Adding the TestOutcome failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Error parsing server response", e);
		}
	}


	/**
	 * Creates a new TestOutcome that is not associated with any TestRun. This is probably not what you want, use
	 * createTestOutcome(TestOutcomeDetails testOutcomeDetails, Long testRunId) instead.
	 *
	 * @param testOutcome The details that should be assigned to the new TestOutcome.
	 * @return The server-assigned ID of the TestOutcome.
	 */

	public Long addTestOutcome(TestOutcome testOutcome) {
		return addTestOutcome(testOutcome, null);
	}


	/**
	 * Adds a result file to a TestRun on the Cuanto server. This adds a test result file to the specified TestRun. A
	 * test result file is not an arbitrary file, but rather it needs to be a file that is in the correct result file
	 * format for the Cuanto project type. For instance, if the Cuanto project is a JUnit project, then it needs to be
	 * a JUnit result file.
	 * @param file A test result file.
	 * @param testRun The TestRun for adding results.
	 * @throws FileNotFoundException If the file is not found.
	 */
	public void importTestFile(File file, TestRun testRun) throws FileNotFoundException {
		List<File> files = new ArrayList<File>();
		files.add(file);
		importTestFiles(files, testRun);
	}


	/**
	 * Adds result files to a TestRun on the Cuanto server. This adds test result files to the specified TestRun. A
	 * test result file is not an arbitrary file, but rather it needs to be a file that is in the correct result file
	 * format for the Cuanto project type. For instance, if the Cuanto project is a JUnit project, then it needs to be
	 * a JUnit result file.
	 * @param files Test Result files to add.
	 * @param testRun The TestRun for adding results.
	 * @throws FileNotFoundException If any of the files are not found.
	 */
	public void importTestFiles(List<File> files, TestRun testRun) throws FileNotFoundException {
		String fullUri = cuantoUrl + "/testRun/submitFile";
		PostMethod post = (PostMethod)getHttpMethod(HTTP_POST, fullUri);

		if (testRun.id == null) {
			addTestRun(testRun);
		}

		post.addRequestHeader("Cuanto-TestRun-Id", testRun.id.toString());

		List<FilePart> parts = new ArrayList<FilePart>();
		for (File file : files) {
			parts.add(new FilePart(file.getName(), file));
		}
		Part[] fileParts = parts.toArray(new Part[]{});
		post.setRequestEntity(new MultipartRequestEntity(fileParts, post.getParams()));

		int responseCode;
		String responseText;
		try {
			HttpClient hclient = getHttpClient();
			responseCode = hclient.executeMethod(post);
			responseText = getResponseBodyAsString(post);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			post.releaseConnection();
		}
		if (responseCode != HttpStatus.SC_OK) {
			throw new RuntimeException("HTTP Response code " + responseCode + ": " + responseText);
		}
	}



	/**
	 * Updates a TestOutcome on the Cuanto server with the details provided.
	 *
	 * @param testOutcome The new details that will replace the corresponding values of the existing TestOutcome.
	 */
	public void updateTestOutcome(TestOutcome testOutcome) {
		if (testOutcome.getId() == null) {
			throw new IllegalArgumentException(
				"The specified TestOutcome has no ID value. Any TestOutcome you wish to" +
					" update should be fetched from the server first.");
		}
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/updateTestOutcome");
		try {
			post.setRequestEntity(new StringRequestEntity(testOutcome.toJSON(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus != HttpStatus.SC_CREATED) {
				throw new RuntimeException("Adding the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Gets the specified TestOutcome from the server.
	 *
	 * @param testOutcomeId The ID of the TestOutcome to retrieve.
	 * @return The retrieved TestOutcome.
	 */
	public TestOutcome getTestOutcome(Long testOutcomeId) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET,
			getCuantoUrl() + "/api/getTestOutcome/" + testOutcomeId.toString());

		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return TestOutcome.fromJSON(getResponseBodyAsString(get));
			} else {
				throw new RuntimeException(
					"Getting the TestOutcome failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}

	}


	/**
	 * Gets all TestOutcomes for the specified TestCase in the specified TestRun. In most normal Cuanto usages, a TestRun
	 * will only have a single TestOutcome per TestCase. TestOutcomes will be in descending order by their finishedAt
	 * values (if they have them) or dateCreated otherwise.
	 *
	 * @param testRun  The TestRun to search.
	 * @param testCase The TestCase for which to retrieve TestOutcomes.
	 * @return A list of all the TestOutcomes for the specified TestCase and TestRun.
	 */
	public List<TestOutcome> getTestCaseOutcomesForTestRun(TestCase testCase, TestRun testRun) {
		if (testRun.id == null) {
			throw new IllegalArgumentException(
				"The TestRun has no id. Query for the TestRun before getting it's TestOutcomes.");
		}
		if (testCase.id == null) {
			throw new IllegalArgumentException(
				"The TestCase has no id. Query for the TestCase before getting it's TestOutcomes.");
		}
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestCaseOutcomesForTestRun");
		get.setQueryString(new NameValuePair[]{
			new NameValuePair("testRun", testRun.id.toString()),
			new NameValuePair("testCase", testCase.id.toString())
		});
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonResponse = JSONObject.fromObject(getResponseBodyAsString(get));
				JSONArray jsonOutcomes = jsonResponse.getJSONArray("testOutcomes");
				List<TestOutcome> outcomesToReturn = new ArrayList<TestOutcome>(jsonOutcomes.size());
				for (Object obj : jsonOutcomes) {
					JSONObject jsonOutcome = (JSONObject) obj;
					outcomesToReturn.add(TestOutcome.fromJSON(jsonOutcome));
				}
				return outcomesToReturn;
			} else {
				throw new RuntimeException(
					"Getting the TestOutcome failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}
	}


	/**
	 * Gets TestOutcomes for the specified TestRun.
	 *
	 * @param testRun The TestRun for which to retrieve TestOutcomes.
	 * @param offset Zero-based index of the first record to return
	 * @param max The maximum number of records to return. You may request a maximum of 100 at a time.
	 * @param sort The TestOutcome field on which to sort. Secondary sort will always be on the TestOutcome's fullName
	 *        (asc) or if the fullName is the primary sort, secondary sort will be by dateCreated (asc).
	 * @param order The order in which to sort -- legal values are "asc" or "desc".
	 * @return The TestOutcomes for the specified TestRun, in the order they were added to the server. If less than
	 * <i>max</i> TestOutcomes are returned, the List will be the size of the number returned.
	 * @throws IllegalArgumentException - if the max is > 100, order is an unknown value, or TestRun has no id.
	 */
	public List<TestOutcome> getTestOutcomesForTestRun(TestRun testRun, Integer offset, Integer max,
													   TestOutcome.Sort sort, String order) throws IllegalArgumentException {
		if (testRun.id == null) {
			throw new IllegalArgumentException(
				"The TestRun has no id. Query for the TestRun before getting it's TestOutcomes.");
		}
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestOutcomes");
		TestOutcome.Sort secondarySort = sort == TestOutcome.Sort.FULL_NAME ?
			TestOutcome.Sort.DATE_CREATED : TestOutcome.Sort.FULL_NAME;

		final String realOrder = order.toLowerCase().trim();
		if (!realOrder.equals("asc") && !realOrder.equals("desc")) {
			throw new IllegalArgumentException("Unknown order: " + order);
		}

		if (max == null) {
			throw new NullPointerException("Null is not a valid value for max");
		} else if (max > 100) {
			throw new IllegalArgumentException("max must not exceed 100");
		}

		get.setQueryString(new NameValuePair[]{
			new NameValuePair("id", testRun.id.toString()),
			new NameValuePair("sort", sort.toString()),
			new NameValuePair("sort", secondarySort.toString()),
			new NameValuePair("order", realOrder),
			new NameValuePair("order", "asc"),
			new NameValuePair("max", max.toString()),
			new NameValuePair("offset", offset.toString())
		});
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonResponse = JSONObject.fromObject(getResponseBodyAsString(get));
				JSONArray jsonOutcomes = jsonResponse.getJSONArray("testOutcomes");
				List<TestOutcome> outcomesToReturn = new ArrayList<TestOutcome>(jsonOutcomes.size());
				for (Object obj : jsonOutcomes) {
					JSONObject jsonOutcome = (JSONObject) obj;
					outcomesToReturn.add(TestOutcome.fromJSON(jsonOutcome));
				}
				return outcomesToReturn;
			} else {
				throw new RuntimeException(
					"Getting the TestOutcome failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}
	}


	/**
	 * Counts how many total TestOutcomes are in TestRun.
	 * @param testRun The TestRun.
	 * @return The number of TestOutomes.
	 */
	public Integer countTestOutcomesForTestRun(TestRun testRun) {
		if (testRun.id == null) {
			throw new IllegalArgumentException(
				"The TestRun has no id. Query for the TestRun before getting it's TestOutcomes.");
		}

		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/countTestOutcomes");
		get.setQueryString(new NameValuePair[]{
			new NameValuePair("id", testRun.id.toString()),
		});
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonResponse = JSONObject.fromObject(getResponseBodyAsString(get));
				return jsonResponse.getInt("count");
			} else {
				throw new RuntimeException(
					"Counting the TestOutcomes failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets all TestOutcomes for the specified TestCase - returned in descending ordered by dateCreated.
	 *
	 * @param testCase The TestCase for which to fetch TestOutcomes.
	 * @return The TestOutcomes for the specified TestCase, in descending order by dateCreated.
	 */
	public List<TestOutcome> getAllTestOutcomesForTestCase(TestCase testCase) {
		if (testCase.id == null) {
			throw new IllegalArgumentException(
				"The TestCase has no id. Query for the TestCase before getting it's TestOutcomes.");
		}
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestOutcomes");
		get.setQueryString(new NameValuePair[]{
			new NameValuePair("testCase", testCase.id.toString()),
			new NameValuePair("sort", "dateCreated"),
			new NameValuePair("order", "desc"),
			new NameValuePair("sort", "finishedAt"),
			new NameValuePair("order", "desc")
		});
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonResponse = JSONObject.fromObject(getResponseBodyAsString(get));
				JSONArray jsonOutcomes = jsonResponse.getJSONArray("testOutcomes");
				List<TestOutcome> outcomesToReturn = new ArrayList<TestOutcome>(jsonOutcomes.size());
				for (Object obj : jsonOutcomes) {
					JSONObject jsonOutcome = (JSONObject) obj;
					outcomesToReturn.add(TestOutcome.fromJSON(jsonOutcome));
				}
				return outcomesToReturn;
			} else {
				throw new RuntimeException(
					"Getting the TestOutcome failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}
	}


	/**
	 * Gets all TestRuns that include the specified TestProperties. The properties can be a subset of a TestRun's
	 * properties, but all of the specified properties must match for a TestRun to be returned.
	 *
	 * @param testProperties The properties for which to search. This is a Map with property names as the keys and the
	 *                       property values as the values.
	 * @return All TestRuns that contain the specified properties. A zero-length array is returned if no matching TestRuns
	 *         are found.
	 */
	public List<TestRun> getTestRunsWithProperties(Map<String, String> testProperties) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/getTestRunsWithProperties");
		try {
			Map jsonMap = new HashMap();

			String pk = getProjectKey();
			if (pk != null && !pk.trim().isEmpty()) {
				jsonMap.put("projectKey", getProjectKey());
			}
			jsonMap.put("testProperties", JSONObject.fromObject(testProperties));
			JSONObject jsonToPost = JSONObject.fromObject(jsonMap);
			post.setRequestEntity(new StringRequestEntity(jsonToPost.toString(), "application/json", null));
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonReturned = JSONObject.fromObject(getResponseBodyAsString(post));
				List<TestRun> testRuns = new ArrayList<TestRun>();
				if (jsonReturned.has("testRuns")) {
					JSONArray returnedRuns = jsonReturned.getJSONArray("testRuns");
					for (Object run : returnedRuns) {
						JSONObject jsonRun = (JSONObject) run;
						testRuns.add(TestRun.fromJSON(jsonRun));
					}
				} else {
					throw new RuntimeException("JSON response didn't have testRuns node");
				}
				return testRuns;
			} else {
				throw new RuntimeException("Getting the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}
	}


	/**
	 * Gets a test case on the server that corresponds to the specified values.
	 *
	 * @param packageName A test package is the namespace for a particular test. In the case of JUnit or TestNG, it would
	 *                    be the fully qualified class name, e.g. org.myorg.MyTestClass
	 * @param testName    The name of the test, in JUnit or TestNG this would be the method name.
	 * @param parameters  A string representing the parameters for this test, if it is a parameterized test. Otherwise this
	 *                    should be null. The server will attempt to locate the TestCase that has these parameters. If the
	 *                    parameters don't match, a TestCase will not be returned.
	 * @return The found TestCase or null if no match is found.
	 */
	public TestCase getTestCase(String packageName, String testName, String parameters) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestCase");
		get.setQueryString(new NameValuePair[]{
			new NameValuePair("projectKey", this.projectKey),
			new NameValuePair("packageName", packageName),
			new NameValuePair("testName", testName),
			new NameValuePair("parameters", parameters)
		});

		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return TestCase.fromJSON(getResponseBodyAsString(get));
			} else {
				throw new RuntimeException("Getting the TestCase failed with HTTP status code " + httpStatus + ":\n" +
					getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Gets a test case on the server that corresponds to the specified values.
	 *
	 * @param testPackage A test package is the namespace for a particular test. In the case of JUnit or TestNG, it would
	 *                    be the fully qualified class name, e.g. org.myorg.MyTestClass.
	 * @param testName    The name of the test, in JUnit or TestNG this would be the method name.
	 * @return The found TestCase or null if no match is found.
	 */
	public TestCase getTestCase(String testPackage, String testName) {
		return getTestCase(testPackage, testName, null);
	}


	/**
	 * Fetches the test output for the specified test outcome from the Cuanto server.
	 *
	 * @param testOutcome The test outcome for which to retrieve output.
	 * @return The output for the given test outcome.
	 */
	public String getTestOutput(TestOutcome testOutcome) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getTestOutput/" +
			testOutcome.id.toString());
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return getResponseBodyAsString(get);
			} else {
				throw new RuntimeException(
					"Getting the TestOutcome failed with HTTP status code " + httpStatus + ":\n" +
						getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Gets all TestRuns for the current project from the Cuanto server.
	 * @return All of the TestRuns for the current project in descending order by dateExecuted.
	 */
	public List<TestRun> getAllTestRuns() {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getAllTestRuns");
		get.setQueryString(new NameValuePair[]{
			new NameValuePair("projectKey", this.projectKey)
		});
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONObject jsonReturned = JSONObject.fromObject(getResponseBodyAsString(get));
				List<TestRun> testRuns = new ArrayList<TestRun>();
				if (jsonReturned.has("testRuns")) {
					JSONArray returnedRuns = jsonReturned.getJSONArray("testRuns");
					for (Object run : returnedRuns) {
						JSONObject jsonRun = (JSONObject) run;
						testRuns.add(TestRun.fromJSON(jsonRun));
					}
				} else {
					throw new RuntimeException("JSON response didn't have testRuns node");
				}
				return testRuns;
			} else {
				throw new RuntimeException("Getting the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(get));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response: " + e.getMessage(), e);
		}
	}


	/**
	 * Gets a Project by the Project's ID value.
	 * @param projectId The ID of the Project.
	 * @return The Project corresponding to the specified ID.
	 */
	public Project getProject(Long projectId) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getProject/" + projectId.toString());

		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return Project.fromJSON(getResponseBodyAsString(get));
			} else {
				throw new RuntimeException("Getting the Project failed with HTTP status code " + httpStatus + ":\n" +
					getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response", e);
		}
	}


	/**
	 * Gets a Project by the project's projectKey value.
	 * @param projectKey The projectKey of the Project.
	 * @return The Project corresponding to the specified projectKey.
	 */
	public Project getProject(String projectKey) {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getProject/?projectKey=" + projectKey.toString());

		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				return Project.fromJSON(getResponseBodyAsString(get));
			} else {
				throw new RuntimeException("Getting the Project failed with HTTP status code " + httpStatus + ":\n" +
					getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response", e);
		}
	}


	/**
	 * Gets a list of all projects ordered by project group and project name.
	 *
	 * @return A List of Projects.
	 */
	public List<Project> getAllProjects() {
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getAllProjects");
		return getProjects(get);
	}

	/**
	 * Gets a list of all projects corresponding to the specified project group, ordered by project name.
	 * @param projectGroup The unique String identifying the project's group.
	 * @return A List of Projects.
	 */
	public List<Project> getProjectsForGroup(String projectGroup) {
		if (projectGroup == null) {
			throw new NullPointerException("No projectGroup argument was provided.");
		}
		String escapedGroup = projectGroup.replaceAll(" ", "+");
		GetMethod get = (GetMethod) getHttpMethod(HTTP_GET, getCuantoUrl() + "/api/getProjectsForGroup?name=" + escapedGroup);
		return getProjects(get);
	}


	private List<Project> getProjects(GetMethod get) {
		try {
			int httpStatus = getHttpClient().executeMethod(get);
			if (httpStatus == HttpStatus.SC_OK) {
				JSONArray jsonProjects = JSONArray.fromObject(getResponseBodyAsString(get));

				List<Project> allProjects = new ArrayList<Project>();

				for (int i = 0; i < jsonProjects.size(); i++)
				{
					JSONObject jsonObject = jsonProjects.getJSONObject(i);
					Project proj = Project.fromJSON(jsonObject);
					allProjects.add(proj);
				}
				return allProjects;
			} else {
				throw new RuntimeException("Getting the Project failed with HTTP status code " + httpStatus + ":\n" +
					getResponseBodyAsString(get));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException("Unable to parse JSON response", e);
		}
	}



	/**
	 * Delete a Project by its ID value.
	 * @param projectId The ID of the Project to delete.
	 */
	public void deleteProject(Long projectId) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/deleteProject/" + projectId);
		deleteProject(post);
	}


	/**
	 * Delete a Project by its projectKey value.
	 * @param projectKey The projectKey of the Project to delete.
	 */
	public void deleteProject(String projectKey) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/deleteProject?projectKey=" + projectKey);
		deleteProject(post);
	}


	protected void deleteProject(PostMethod post) {
		try {
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus != HttpStatus.SC_OK) {
				throw new RuntimeException("Deleting the Project failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}





	/**
	 * Gets the URL of the Cuanto server that this instance was configured to communicate with.
	 *
	 * @return The URL of the Cuanto server
	 */
	public String getCuantoUrl() {
		return cuantoUrl;
	}


	/**
	 * Set the URL of the Cuanto server that this instance should communicate with.
	 *
	 * @param cuantoUrl The URL of the Cuanto server.
	 */
	void setCuantoUrl(String cuantoUrl) {
		try {
			new URL(cuantoUrl);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		if (cuantoUrl.endsWith("/")) {
			cuantoUrl = cuantoUrl.substring(0, cuantoUrl.lastIndexOf('/'));
		}
		this.cuantoUrl = cuantoUrl;
	}


	private String getProxyHost() {
		return proxyHost;
	}


	private void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}


	private Integer getProxyPort() {
		return proxyPort;
	}


	private void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}


	/**
	 * Gets the project key for the project this connector is configured to connect to.
	 *
	 * @return The project key.
	 */
	public String getProjectKey() {
		return projectKey;
	}


	void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}


	/**
	 * This is here to substitute for HttpMethod.getResponseBodyAsString(), which logs an annoying error message each time
	 * it's called.
	 *
	 * @param method The method for which to get the response.
	 * @return The full response body as a String.
	 * @throws IOException If something bad happened.
	 */
	private String getResponseBodyAsString(HttpMethod method) throws IOException {
		InputStreamReader reader = new InputStreamReader(method.getResponseBodyAsStream());
		StringWriter writer = new StringWriter();
		int in;
		while ((in = reader.read()) != -1) {
			writer.write(in);
		}
		reader.close();
		return writer.toString();
	}


	public void deleteTestRun(TestRun testRun) {
		PostMethod post = (PostMethod) getHttpMethod(HTTP_POST, getCuantoUrl() + "/api/deleteTestRun/" + testRun.id);
		try {
			int httpStatus = getHttpClient().executeMethod(post);
			if (httpStatus != HttpStatus.SC_OK) {
				throw new RuntimeException("Deleting the TestRun failed with HTTP status code " + httpStatus + ": \n" +
					getResponseBodyAsString(post));
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
