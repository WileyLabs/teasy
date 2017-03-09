package com.wiley.autotest.selenium.context;

/**
 * User: ntyukavkin
 * Date: 09.03.2017
 * Time: 16:47
 * Description:
 */
public enum SearchStrategy {
    /**
     * Default value.
     * Search in all frames until there are elements.
     * Return first found elements from one frame or fail with error.
     */
    SEARCH_FIRST_ELEMENTS,
    /**
     * Search in all frames every time.
     * Return elements from all frames or fail with error.
     */
    SEARCH_IN_ALL_FRAMES
}
