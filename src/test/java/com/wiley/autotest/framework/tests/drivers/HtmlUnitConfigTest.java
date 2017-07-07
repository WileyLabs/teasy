package com.wiley.autotest.framework.tests.drivers;

import com.wiley.autotest.annotations.OurAfterMethod;
import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.spring.Settings;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class HtmlUnitConfigTest extends BaseTest {

    @Autowired
    private void setSettings(Settings settings) {
        settings.setDriverName("htmlunit");
    }

    @Test
    public void htmlunit_config_test() {
        openPage("main.html");
        WebDriver webDriver = ((FramesTransparentWebDriver) SeleniumHolder.getWebDriver()).getWrappedDriver();

        Assert.assertTrue(webDriver instanceof HtmlUnitDriver);
    }

    @OurAfterMethod
    public void after() {
        SeleniumHolder.getWebDriver().quit();
    }
}
