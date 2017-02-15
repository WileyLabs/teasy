package com.wiley.autotest.cuanto.api;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Represents a group of related tests that were executed together.
 */
public class TestRun {
	String projectKey;
	String note;
	Date dateCreated;
	Date dateExecuted;
	Date lastUpdated;
	Boolean valid = true;
	Map<String, String> links = new HashMap<String, String>();
	Map<String, String> testProperties = new HashMap<String, String>();
	List<String> tags = new ArrayList<String>();
	Long id;


	TestRun() {
	}


	/**
	 * Creates a new TestRun object with the specified dateExecuted. Note the TestRun is not added to the Cuanto server
	 * until you call CuantoConnector.addTestRun().
	 *
	 * @param dateExecuted The timestamp for when the TestRun executed.
	 */
	public TestRun(Date dateExecuted) {
		if (dateExecuted == null) {
			throw new NullPointerException("null is not a valid value for dateExecuted");
		}
		this.dateExecuted = dateExecuted;
	}


	public TestRun(String projectKey) {
		this.projectKey = projectKey;
	}


	static TestRun fromJSON(String json) throws ParseException {
		JSONObject jsonTestRun = JSONObject.fromObject(json);
		return fromJSON(jsonTestRun);
	}


	static TestRun fromJSON(JSONObject jsonTestRun) throws ParseException {

		TestRun testRun = new TestRun();

		if (!jsonTestRun.getJSONObject("project").isNullObject()) {
			testRun.setProjectKey(jsonTestRun.getJSONObject("project").getString("projectKey"));
		}

		if (jsonTestRun.has("id")) {
			testRun.setId(jsonTestRun.getLong("id"));
		}

		if (jsonTestRun.has("dateExecuted")) {
			testRun.setDateExecuted(parseJsonDate(jsonTestRun.getString("dateExecuted")));
		}

		if (jsonTestRun.has("dateCreated")) {
			testRun.setDateCreated(parseJsonDate(jsonTestRun.getString("dateCreated")));
		}

		if (jsonTestRun.has("lastUpdated")) {
			testRun.setLastUpdated(parseJsonDate(jsonTestRun.getString("lastUpdated")));
		}

		if (jsonTestRun.has("note")) {
			testRun.setNote(jsonTestRun.getString("note"));
		}

		if (jsonTestRun.has("links")) {
			JSONObject links = jsonTestRun.getJSONObject("links");
			if (!links.isNullObject()) {
				for (Object urlObj : links.keySet()) {
					String url = (String) urlObj;
					testRun.addLink(url, links.getString(url));
				}
			}
		}

		if (jsonTestRun.has("testProperties")) {
			JSONArray props = jsonTestRun.getJSONArray("testProperties");
			for (Object propObj : props) {
				JSONObject prop = (JSONObject) propObj;
				testRun.addTestProperty(prop.getString("name"), prop.getString("value"));
			}
		}

		if ((jsonTestRun.get("tags") != null) && !(jsonTestRun.get("tags") instanceof JSONNull)) {
			JSONArray tagArray = jsonTestRun.getJSONArray("tags");
			testRun.addTags(tagArray);
		}

		return testRun;
	}


	/**
	 * Gets a JSON representation of this TestRun.
	 *
	 * @return The JSON string representing the TestRun.
	 */
	public String toJSON() {
		JSONObject jsonTestRun = JSONObject.fromObject(toJsonMap());
		return jsonTestRun.toString();
	}


	Map toJsonMap() {
		Map jsonMap = new HashMap();
		if (this.projectKey != null) {
			jsonMap.put("projectKey", this.projectKey);
		}
		
		if (this.id != null) {
			jsonMap.put("id", this.id);
		}
		if (this.dateExecuted != null){
			jsonMap.put("dateExecuted", toJsonDate(this.dateExecuted));
		}
		if (this.valid != null) {
			jsonMap.put("valid", this.valid);
		}

		if (this.note != null) {
			jsonMap.put("note", this.note);
		}
		if (this.links != null && this.links.size() > 0) {
			jsonMap.put("links", links);
		}
		if (this.testProperties != null && this.testProperties.size() > 0) {
			jsonMap.put("testProperties", testProperties);
		}
		return jsonMap;
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
	 * Adds or updates a TestProperty. This change is not reflected on the Cuanto service until you create or update the
	 * TestRun with the CuantoConnector.
	 *
	 * @param name  The name of the property
	 * @param value The value of the property
	 * @return The TestRun associated with the TestProperty
	 */
	public TestRun addTestProperty(String name, String value) {
		testProperties.put(name, value);
		return this;
	}


	/**
	 * Deletes a TestProperty from this TestRun object. This change is not reflected on the Cuanto server until you create
	 * or update the TestRun with the CuantoConnector.
	 *
	 * @param name The TestProperty name to delete.
	 */
	public void deleteTestProperty(String name) {
		testProperties.remove(name);
	}


	/**
	 * Adds or updates a Link. This change is not reflected on the Cuanto server until you create or update the TestRun with
	 * the CuantoConnector.
	 *
	 * @param url         The url of the link
	 * @param description The description of the link
	 * @return The TestRun associated with this link
	 */
	public TestRun addLink(String url, String description) {
		links.put(url, description);
		return this;
	}


	/**
	 * Get the tags associated with this test run.
	 *
	 * @return An unmodifiable List of the tags.
	 */
	public List<String> getTags() {
		return Collections.unmodifiableList(tags);
	}


	void addTags(List<String> tags) {
		this.tags.addAll(tags);
	}


	void addTag(String tag) {
		tags.add(tag);
	}


	/**
	 * Deletes a Link from this TestRun object. This change is not reflected on the Cuanto server until you create or update
	 * the TestRun with the CuantoConnector.
	 *
	 * @param url The url of the link to delete.
	 */
	public void deleteLink(String url) {
		links.remove(url);
	}


	/**
	 * Gets the projectKey for this TestRun's project.
	 *
	 * @return The projectKey associated with this TestRun's project.
	 */
	public String getProjectKey() {
		return projectKey;
	}


	/**
	 * Gets the note associated with this TestRun.
	 *
	 * @return The note associated with this TestRun.
	 */
	public String getNote() {
		return note;
	}


	/**
	 * Gets the date this TestRun was added to the Cuanto server. This will only be populated if the TestRun was
	 * retrieved from the Cuanto server.
	 *
	 * @return The date this TestRun was added to the Cuanto server or null if this TestRun was not retrieved from the
	 *         server.
	 */
	public Date getDateCreated() {
		return dateCreated;
	}


	/**
	 * Gets the date this TestRun was executed.
	 *
	 * @return The date this TestRun was executed.
	 */
	public Date getDateExecuted() {
		return dateExecuted;
	}


	/**
	 * Gets whether this TestRun is considered valid. Validity is determined by a user setting the TestRun's valid flag.
	 * For example, a user may flag a TestRun as invalid if the execution aborted partway through or if an environmental
	 * problem caused a massive number of failures.
	 *
	 * @return Whether this TestRun is considered valid.
	 */
	public Boolean isValid() {
		return valid;
	}


	/**
	 * Gets any hyperlinks associated with this TestRun.
	 *
	 * @return A map where each entry's key is a URL and it's value is a description of that URL.
	 */
	public Map getLinks() {
		return Collections.unmodifiableMap(links);
	}


	/**
	 * Gets any TestProperties associated with this TestRun.
	 *
	 * @return A map where each entry's key is a property name and it's value is the property's value.
	 */
	public Map getTestProperties() {
		return Collections.unmodifiableMap(testProperties);
	}


	/**
	 * Gets TestProperties associated with this TestRun.
	 *
	 * @param testProperties A map where each entry's key is a property name and it's value is the property's value.
	 */
	public void setTestProperties(Map<String, String> testProperties) {
		this.testProperties = testProperties;
	}


	/**
	 * Gets the server-assigned ID of this TestRun. Populated only if the TestRun was retrieved from the Cuanto server.
	 *
	 * @return The server-assigned ID of this TestRun or null if the TestRun was not retrieved from the Cuanto server.
	 */
	public Long getId() {
		return id;
	}


	void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}


	/**
	 * Sets a note to be associated with this TestRun. The TestRun will not be updated on the server until you call
	 * CuantoConnector.addTestRun() or CuantoConnector.updateTestRun() with this TestRun as an argument.
	 *
	 * @param note The note to associate with the TestRun.
	 */
	public void setNote(String note) {
		this.note = note;
	}


	void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}


	/**
	 * Sets the date this TestRun was executed.
	 *
	 * @param dateExecuted The date this TestRun was executed.
	 */
	public void setDateExecuted(Date dateExecuted) {
		this.dateExecuted = dateExecuted;
	}


	/**
	 * Sets whether this TestRun should be considered valid. Validity is determined by a user setting the TestRun's valid flag.
	 * For example, a user may flag a TestRun as invalid if the execution aborted partway through or if an environmental
	 * problem caused a massive number of failures.
	 *
	 * @param valid If the TestRun should be considered valid.
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}


	void setId(Long id) {
		this.id = id;
	}


	/**
	 * Gets the last time this TestRun was updated on the Cuanto server.
	 *
	 * @return The last time this TestRun was updated on the Cuanto server.
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}


	void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
