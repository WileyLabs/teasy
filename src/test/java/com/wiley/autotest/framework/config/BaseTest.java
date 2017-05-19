package com.wiley.autotest.framework.config;

import com.wiley.autotest.framework.pages.BasePage;
import com.wiley.autotest.selenium.AbstractSeleniumTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

/**
 * User: ntyukavkin
 * Date: 19.05.2017
 * Time: 14:17
 */
@ContextConfiguration(locations = {
        "classpath:/spring/settings.xml"
})
public class BaseTest extends AbstractSeleniumTest {

    @Autowired
    private SeleniumSettings settings;

    public BasePage openPage(String fileName) {
        return getPage(BasePage.class, "file://" + getClass().getResource(settings.getHost()).getPath() + fileName);
    }
}
