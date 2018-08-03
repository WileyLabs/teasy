package com.wiley.autotest.framework.tests.window;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.WindowPage;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class WindowBehavior extends BaseUnitTest {

    @Test
    public void refresh() {
        openPage("window.html", WindowPage.class)
                .checkRefreshElIsDisplayed()
                .refresh()
                .checkRefreshElIsDisplayed();
    }

    @Test
    public void forwardBackward() {
        openPage("window.html", WindowPage.class)
                .clickNewWindow()
                .checkSecondWindowText()
                .goBack()
                .checkFirstWindowText()
                .goForward()
                .checkSecondWindowText();
    }

    @Test
    public void navigateTo() throws MalformedURLException {
        openPage("window.html", WindowPage.class)
                .navigateTo("http://www.wiley.com")
                .checkURL("www.wiley.com")
                .navigateTo(new URL("http://www.wiley.ru"))
                .checkURL("www.wiley.ru");
    }
}
