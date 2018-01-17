package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.testng.annotations.Test;

public class AnimationElement extends BaseTest {

    @Test
    public void test() {
        openPage("mainTestElement.html", TestElementPage.class)
                .checkAnimationElement();
    }
}
