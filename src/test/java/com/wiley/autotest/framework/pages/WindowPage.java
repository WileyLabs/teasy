package com.wiley.autotest.framework.pages;

import com.wiley.page.BasePage;
import com.wiley.elements.TeasyElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;

import java.net.URL;

public class WindowPage extends BasePage {

    public WindowPage checkRefreshElIsDisplayed() {
        getRefreshEl().should().beDisplayed();
        return this;
    }

    public WindowPage refresh() {
        TeasyElement el = getRefreshEl();
        window().refresh();
        //checking that element become stale
        el.should().customCondition(webDriver -> el.isStale());

        return this;
    }

    public WindowPage clickNewWindow() {
        element(By.id("goToNewWindow")).click();
        return this;
    }

    public WindowPage goBack() {
        window().back();
        return this;
    }

    public WindowPage goForward() {
        window().forward();
        return this;
    }

    public WindowPage checkFirstWindowText() {
        element(By.id("mainText")).should().haveText("Hi I am original window");
        return this;
    }

    public WindowPage checkSecondWindowText() {
        element(By.id("mainText")).should().haveText("Hi I am new window");
        return this;
    }

    private TeasyElement getRefreshEl() {
        return element(By.id("refreshEl"));
    }

    public WindowPage navigateTo(String url) {
        window().navigateTo(url);
        return this;
    }

    public WindowPage navigateTo(URL url) {
        window().navigateTo(url);
        return this;
    }

    public WindowPage checkURL(String url) {
        Assertions.assertThat(window().getUrl()).contains(url);
        return this;
    }
}
