package com.wiley.autotest.cuanto.api;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PropertySet;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;


public class CuantoAntTask extends org.apache.tools.ant.Task {
	URL url;
	String proxyHost;
	String proxyPort;
	File resultFile;
	String testType; // here just for backward compatibility
	String testProject;
	String milestone; // deprecated
	String targetEnv; // deprecated
	String build; // deprecated
	String date;
	String dateFormat;
	String action = "submit";

	protected static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	List<FileSet> filesets = new ArrayList<FileSet>();
	//Map<String, String> links = new HashMap<String, String>();
	List<Link> links = new ArrayList<Link>();
	List<Property> properties = new ArrayList<Property>();


	public void execute() {
		if (testProject == null) {
			throw new BuildException("The project attribute needs to be specified");
		}

		action = action.toLowerCase().trim();

		if (action.equals("submit")) {
			try {
				submit();
			} catch (FileNotFoundException e) {
				throw new BuildException("File not found", e);
			}
		}
	}


	private void submit() throws FileNotFoundException {
		CuantoConnector cuantoClient = getCuantoClient();

		TestRun testRun = new TestRun();
		testRun.projectKey = testProject;

		if (date != null) {
			String dateFormatToUse = dateFormat != null ? dateFormat : DEFAULT_DATE_FORMAT;
			try {
				testRun.dateExecuted = new SimpleDateFormat(dateFormatToUse).parse(date);
			} catch (ParseException e) {
				log("Error parsing date: " + e.getMessage(), Project.MSG_ERR);
			}
		} else {
			testRun.setDateExecuted(new Date());
		}

		//testRun.links = links;
		for (Link link : links) {
			if (link == null) {
				log("null link!", Project.MSG_WARN);
			}
			if (link.getUrl() == null) {
				log("null link URL", Project.MSG_WARN);
			}
			if (link.getDescription() == null) {
				log("null link description", Project.MSG_WARN);
			}
			testRun.addLink(link.getUrl(), link.getDescription());
		}

		// process deprecated attributes
		processDeprecatedAttributes(testRun);

		for (Property prop : properties) {
			testRun.testProperties.put(prop.getName(), prop.getValue());
		}

		cuantoClient.addTestRun(testRun);

		log("Submitting results to " + url);

		Date startTime = new Date();
		cuantoClient.importTestFiles(getFilesToSubmit(), testRun);
		Date endTime = new Date();
		Long duration = (endTime.getTime() - startTime.getTime()) / 1000;
		log("Submitting results took " + duration + " second(s)");
	}


	private List<File> getFilesToSubmit() {
		List<File> files = new ArrayList<File>();

		if (filesets.size() > 0) {
			for (FileSet theFileSet : filesets) {
				DirectoryScanner ds = theFileSet.getDirectoryScanner(getProject());
				File basedir = ds.getBasedir();
				for (String filename : ds.getIncludedFiles()) {
					files.add(new File(basedir, filename));
				}
			}
		} else {
			if (resultFile == null) {
				throw new BuildException("No files found!");
			}
			files.add(resultFile);
		}
		return files;
	}


	private CuantoConnector getCuantoClient() {
		CuantoConnector cuantoConnector;
		if (proxyHost != null && proxyPort != null) {
			cuantoConnector = CuantoConnector.newInstance(url.toString(), testProject, proxyHost,
				Integer.valueOf(proxyPort));
		} else {
			cuantoConnector = CuantoConnector.newInstance(url.toString(), testProject);
		}
		return cuantoConnector;
		//todo: deal with DateFormat
	}


	private void processDeprecatedAttributes(TestRun testRun) {
		if (build != null) {
			testRun.testProperties.put("Build", build);
			logDeprecatedUsage("build");
		}
		if (milestone != null) {
			testRun.testProperties.put("Milestone", milestone);
			logDeprecatedUsage("milestone");
		}
		if (targetEnv != null) {
			testRun.testProperties.put("Target Environment", targetEnv);
			logDeprecatedUsage("targetEnv");
		}
	}


	private void logDeprecatedUsage(String propName) {
		log("Cuanto task attribute " + propName + " is deprecated, use a nested property node instead",
			Project.MSG_WARN);
	}


	public FileSet createFileset() {
		FileSet fset = new FileSet();
		filesets.add(fset);
		return fset;
	}


	public Link createLink() {
		Link link = new Link();
		links.add(link);
		return link;
	}


	public Property createProperty() {
		Property prop = new Property();
		properties.add(prop);
		return prop;
	}


	public void addConfiguredPropertySet(PropertySet propSet) {
		log("Processing " + propSet.size() + " properties from propertyset", Project.MSG_VERBOSE);
		Properties props = propSet.getProperties();
		for (Object propkeyObj : props.keySet()) {
			String propKey = (String) propkeyObj;
			String propVal = props.getProperty(propKey);
			log("Adding property " + propKey + " with value " + propVal, Project.MSG_VERBOSE);
			Property prop = new Property();
			prop.setName(propKey);
			prop.setValue(propVal);
			properties.add(prop);
		}
	}


	public URL getUrl() {
		return url;
	}


	public void setUrl(URL url) {
		this.url = url;
	}


	public String getProxyHost() {
		return proxyHost;
	}


	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}


	public String getProxyPort() {
		return proxyPort;
	}


	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}


	public File getResultFile() {
		return resultFile;
	}


	public void setResultFile(File resultFile) {
		this.resultFile = resultFile;
	}


	public String getTestType() {
		return testType;
	}


	public void setTestType(String testType) {
		this.testType = testType;
	}


	public String getTestProject() {
		return testProject;
	}


	public void setTestProject(String testProject) {
		this.testProject = testProject;
	}


	public String getMilestone() {
		return milestone;
	}


	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}


	public String getTargetEnv() {
		return targetEnv;
	}


	public void setTargetEnv(String targetEnv) {
		this.targetEnv = targetEnv;
	}


	public String getBuild() {
		return build;
	}


	public void setBuild(String build) {
		this.build = build;
	}


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
		this.date = date;
	}


	public String getDateFormat() {
		return dateFormat;
	}


	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}


	public class Link {
		String description;
		String url;


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getUrl() {
			return url;
		}


		public void setUrl(String url) {
			this.url = url;
		}
	}
}
