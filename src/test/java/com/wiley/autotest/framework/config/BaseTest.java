package com.wiley.autotest.framework.config;

import com.wiley.autotest.selenium.AbstractSeleniumTest;
import com.wiley.autotest.selenium.context.IPage;
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

    public <E extends IPage> E openPage(String fileName, Class<E> page) {
        return getPage(page, "file://" + getClass().getResource(settings.getHost()).getPath() + fileName);
    }
}
