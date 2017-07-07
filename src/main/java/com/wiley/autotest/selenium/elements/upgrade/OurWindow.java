package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.v3.FluentWaitFinder;
import com.wiley.autotest.selenium.elements.upgrade.v3.conditions.window.WindowMatcher;
import org.openqa.selenium.WebDriver;

import java.util.Iterator;

/**
 * Created by vefimov on 30/05/2017.
 */
public class OurWindow implements Window {

    @Override
    public void switchToLast() {
        WebDriver driver = SeleniumHolder.getWebDriver();
        Iterator<String> iterator = driver.getWindowHandles().iterator();
        String window = null;
        while (iterator.hasNext()) {
            window = iterator.next();
        }
        driver.switchTo().window(window);
    }

    @Override
    public void close() {
        SeleniumHolder.getWebDriver().close();
    }

    @Override
    public void waitForScriptsToLoad() {

    }

    @Override
    public void switchTo(WindowMatcher matcher) {
        FluentWaitFinder finder = new FluentWaitFinder(SeleniumHolder.getWebDriver());
        finder.waitFor(matcher.get().findAndSwitch());
    }

}
