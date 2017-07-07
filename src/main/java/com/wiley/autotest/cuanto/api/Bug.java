package com.wiley.autotest.cuanto.api;

/**
 * Represents a Bug associated with a TestOutcome.
 */
public class Bug {

	private String title;
	private String url;
	private Long id;

	Bug() {
	}

	/**
	 * Instantiate a new Bug for this title and URL.
	 * @param title The title of the bug.
	 * @param url The URL associated with the bug.
	 */
	public Bug(String title, String url) {
		this.title = title;
		this.url = url;
	}

	Bug(String title, String url, Long id) {
		this.title = title;
		this.url = url;
	}

	/**
	 * @return The title of the bug.
	 */
	public String getTitle() {
		return title;
	}

	void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return The URL of the bug.
	 */
	public String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return The ID of the bug. This is only populated if the Bug was retrieved from the Cuanto server (as part of a TestOutcome).
	 */
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		this.id = id;
	}
}
