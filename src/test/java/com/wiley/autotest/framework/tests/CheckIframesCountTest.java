package com.wiley.autotest.framework.tests;

import com.wiley.autotest.framework.config.BaseTest;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 19.05.2017
 * Time: 14:17
 */
public class CheckIframesCountTest extends BaseTest {

    @Test
    public void test() {
        openPage("main.html")
                .checkIframeIsPresent()
                .checkIframeCountIsTwo();
    }
}
