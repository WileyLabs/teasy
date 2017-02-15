package com.wiley.autotest.cuanto.api;

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * A class that represents a server-side Cuanto project.
 */
public class Project {

    String name;
    Long id;
    String projectGroup;
    String projectKey;
    String bugUrlPattern;
    String testType;

    Project() {
    }

    /**
     * Creates a new Project.
     *
     * @param name          The name of the Project.
     * @param projectGroup  The group the project belongs to.
     * @param projectKey    The unique projectKey of the project.
     * @param bugUrlPattern <p></p>The bug URL pattern is used for referencing an external bug tracking system. Use this
     *                      field to describe the URL scheme for the bug tracking system by using the text {BUG} to
     *                      substitute for the bug number or bug identifier in the bug tracking system's bug URLs.</p>
     *                      <p/>
     *                      <p>For example, iNf your Bugzilla tracking system has URLs like http://bugzilla/show_bug.cgi?id=14587,
     *                      you would use a bug URL pattern of http://bugzilla/show_bug.cgi?id={BUG}. A JIRA bug tracking
     *                      system might have URLs like http://jira/browse/CUANTO-34, then the bug tracking pattern would
     *                      be http://jira/browse/{BUG}.
     * @param testType      At present, JUnit, TestNG, NUnit and Manual are valid values.
     */
    public Project(String name, String projectGroup, String projectKey, String bugUrlPattern, String testType) {
        this.name = name;
        this.projectGroup = projectGroup;
        this.projectKey = projectKey;
        this.bugUrlPattern = bugUrlPattern;
        this.testType = testType;
    }

    /**
     * Creates a new Project.
     *
     * @param name         The name of the Project.
     * @param projectGroup The group the project belongs to.
     * @param projectKey   The unique projectKey of the project.
     * @param testType     At present, JUnit, TestNG, NUnit and Manual are valid values.
     */
    public Project(String name, String projectGroup, String projectKey, String testType) {
        this(name, projectGroup, projectKey, null, testType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectGroup() {
        return projectGroup;
    }

    public void setProjectGroup(String projectGroup) {
        this.projectGroup = projectGroup;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getBugUrlPattern() {
        return bugUrlPattern;
    }

    public void setBugUrlPattern(String bugUrlPattern) {
        this.bugUrlPattern = bugUrlPattern;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    static Project fromJSON(String json) throws ParseException {
        JSONObject jsonProject = JSONObject.fromObject(json);
        return fromJSON(jsonProject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if (bugUrlPattern != null ? !bugUrlPattern.equals(project.bugUrlPattern) : project.bugUrlPattern != null) {
            return false;
        }
        if (id != null ? !id.equals(project.id) : project.id != null) {
            return false;
        }
        if (name != null ? !name.equals(project.name) : project.name != null) {
            return false;
        }
        if (projectGroup != null ? !projectGroup.equals(project.projectGroup) : project.projectGroup != null) {
            return false;
        }
        return !(projectKey != null ? !projectKey.equals(project.projectKey) : project.projectKey != null) && !(testType != null ? !testType.equals(project.testType) : project.testType != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (projectGroup != null ? projectGroup.hashCode() : 0);
        result = 31 * result + (projectKey != null ? projectKey.hashCode() : 0);
        result = 31 * result + (bugUrlPattern != null ? bugUrlPattern.hashCode() : 0);
        result = 31 * result + (testType != null ? testType.hashCode() : 0);
        return result;
    }

    static Project fromJSON(JSONObject jsonProject) throws ParseException {
        Project project = new Project();

        if (jsonProject.has("id")) {
            project.setId(jsonProject.getLong("id"));
        }

        if (jsonProject.has("name")) {
            project.setName(jsonProject.getString("name"));
        }

        if (jsonProject.has("projectGroup")) {
            JSONObject jsonProjectGroup = jsonProject.getJSONObject("projectGroup");
            if (jsonProjectGroup.size() > 0) {
                project.setProjectGroup(jsonProjectGroup.getString("name"));
            }
        }

        if (jsonProject.has("projectKey")) {
            project.setProjectKey(jsonProject.getString("projectKey"));
        }

        if (jsonProject.has("bugUrlPattern")) {
            project.setBugUrlPattern(jsonProject.getString("bugUrlPattern"));
        }

        if (jsonProject.has("testType")) {
            JSONObject jsonTestType = jsonProject.getJSONObject("testType");
            project.setTestType(jsonTestType.getString("name"));
        }

        return project;
    }

    public String toJSON() {
        JSONObject jsonProject = JSONObject.fromObject(toJsonMap());
        return jsonProject.toString();
    }

    Map toJsonMap() {
        Map jsonMap = new HashMap();
        if (this.projectKey != null) {
            jsonMap.put("projectKey", this.projectKey);
        }

        if (this.name != null) {
            jsonMap.put("name", this.name);
        }

        if (this.id != null) {
            jsonMap.put("id", this.id);
        }
        if (this.projectGroup != null) {
            jsonMap.put("projectGroup", this.projectGroup);
        }
        if (this.projectKey != null) {
            jsonMap.put("projectKey", this.projectKey);
        }

        if (this.bugUrlPattern != null) {
            jsonMap.put("bugUrlPattern", this.bugUrlPattern);
        }

        if (this.testType != null) {
            jsonMap.put("testType", this.testType);
        }

        return jsonMap;
    }
}
