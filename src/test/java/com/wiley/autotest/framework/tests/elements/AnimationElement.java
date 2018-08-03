package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.testng.annotations.Test;

public class AnimationElement extends BaseUnitTest {

    @Test
    public void test() {
        openPage("mainTestElement.html", TestElementPage.class)
                .checkAnimationElement();
    }
}
