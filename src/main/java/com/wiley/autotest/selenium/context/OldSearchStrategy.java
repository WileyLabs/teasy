package com.wiley.autotest.selenium.context;

/**
 * User: ntyukavkin
 * Date: 09.03.2017
 * Time: 16:47
 * Description:
 */
@Deprecated
/**
 * use {@link SearchStrategy}
 */
public enum OldSearchStrategy {
    /**
     * Default value.
     * Search in all frames until there are elements.
     * Return first found elements from one frame.
     */
    FIRST_ELEMENTS,
    /**
     * Search in all frames every time.
     * Return elements from all frames.
     */
    IN_ALL_FRAMES
}
