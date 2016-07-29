package com.wiley.autotest.selenium.context;

/**
 * User: dfedorov
 * Date: 1/5/12
 * Time: 12:36 PM
 */
public interface IPage extends IPageElement {
    void load();

    void load(String path);
}
