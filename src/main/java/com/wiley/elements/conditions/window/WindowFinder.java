package com.wiley.elements.conditions.window;

import com.wiley.utils.TestUtils;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

import static com.wiley.utils.ExecutionUtils.isChrome;

public abstract class WindowFinder {

    private String locatedBy;

    public WindowFinder(String locatedBy) {
        this.locatedBy = locatedBy;
    }

    public String locatedBy() {
        return locatedBy;
    }

    public abstract Function<WebDriver, String> findAndSwitch();

    //TODO NT: In chrome test hangs before switch to new window, to avoid this add timeout
    //TODO VE: not sure if this is still actual (to be commented in next releases to check)
    protected void waitForChrome() {
        if (isChrome()) {
            TestUtils.sleep(3000, "Wait for window is appear in chrome");
        }
    }
}
