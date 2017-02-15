package com.wiley.autotest.cuanto.listener.testng;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestNgListenerArguments {
    // Cuanto url
    private URI cuantoUrl;

    // TestRun.id
    private Long testRunId;

    // TestRun.projectKey
    private String projectKey;

    // TestRun.links
    private Map<String, String> links;

    // TestRun.properties
    private Map<String, String> testProperties;

    // whether to create a new TestRun if testRunId is null
    private Boolean createTestRun;

    // whether to consider configuration methods when calculating test run time
    private Boolean includeConfigDuration;

    public TestNgListenerArguments() {
    }

    public TestNgListenerArguments(TestNgListenerArguments arguments) {
        cuantoUrl = arguments.getCuantoUrl();
        testRunId = arguments.getTestRunId();
        projectKey = arguments.getProjectKey();
        createTestRun = arguments.isCreateTestRun();
        includeConfigDuration = arguments.isIncludeConfigDuration();

        Map<String, String> originalLinks = arguments.getLinks();
        if (originalLinks != null) {
            links = new LinkedHashMap<String, String>(originalLinks);
        }

        Map<String, String> originalTestProperties = arguments.getTestProperties();
        if (originalTestProperties != null) {
            testProperties = new LinkedHashMap<String, String>(originalTestProperties);
        }
    }

    public URI getCuantoUrl() {
        return cuantoUrl;
    }

    public void setCuantoUrl(URI cuantoUrl) {
        this.cuantoUrl = cuantoUrl;
    }

    public Long getTestRunId() {
        return testRunId;
    }

    public void setTestRunId(Long testRunId) {
        this.testRunId = testRunId;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public Map<String, String> getTestProperties() {
        return testProperties;
    }

    public void setTestProperties(Map<String, String> testProperties) {
        this.testProperties = testProperties;
    }

    public Boolean isCreateTestRun() {
        return createTestRun;
    }

    public void setCreateTestRun(Boolean createTestRun) {
        this.createTestRun = createTestRun;
    }

    public Boolean isIncludeConfigDuration() {
        return includeConfigDuration;
    }

    public void setIncludeConfigDuration(Boolean includeConfigDuration) {
        this.includeConfigDuration = includeConfigDuration;
    }
}
