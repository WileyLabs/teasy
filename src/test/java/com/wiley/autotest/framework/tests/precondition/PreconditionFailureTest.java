package com.wiley.autotest.framework.tests.precondition;

import com.wiley.autotest.annotations.OurBeforeMethod;
import com.wiley.autotest.framework.config.BaseTest;
import com.wiley.autotest.utils.TestUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PreconditionFailureTest extends BaseTest {

    @OurBeforeMethod
    public void setUp() {
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.get(2);
    }

    @Test
    public void test() {

    }
}
