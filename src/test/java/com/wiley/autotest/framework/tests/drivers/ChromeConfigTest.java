package com.wiley.autotest.framework.tests.drivers;

import com.wiley.autotest.annotations.OurAfterMethod;
import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.driver.FramesTransparentWebDriver;
import com.wiley.autotest.services.Configuration;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class ChromeConfigTest extends BaseTest {

    @Autowired
    private void setConfiguration(Configuration configuration) {
        getSettings().setDriverName("chrome");
        //set value other than default
        configuration.addCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
    }

    @Test
    public void chrome_config_test() {
        openPage("main.html");
        WebDriver webDriver = ((FramesTransparentWebDriver) SeleniumHolder.getWebDriver()).getWrappedDriver();

        Assert.assertTrue(webDriver instanceof ChromeDriver);

        Assert.assertEquals(((RemoteWebDriver) webDriver).getCapabilities().getCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR), UnexpectedAlertBehaviour.IGNORE.toString());
    }

    @OurAfterMethod
    public void after() {
        SeleniumHolder.getWebDriver().quit();
    }
}
