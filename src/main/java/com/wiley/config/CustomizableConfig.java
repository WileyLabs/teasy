package com.wiley.config;

/**
 * Class for init tests run settings(e.g. browser, grid, timeouts and etc.)
 * or environment properties(app url, users and etc.)
 * before start all tests.
 * Method set() will be invoked in {@link InitConfigListener#onExecutionStart()}.
 */
public interface CustomizableConfig {

    void set();
}
