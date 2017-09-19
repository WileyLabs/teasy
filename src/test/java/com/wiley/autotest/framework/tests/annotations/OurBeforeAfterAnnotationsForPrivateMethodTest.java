package com.wiley.autotest.framework.tests.annotations;

import com.wiley.autotest.annotations.OurBeforeClass;
import com.wiley.autotest.annotations.OurBeforeMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class OurBeforeAfterAnnotationsForPrivateMethodTest extends OurBeforeAfterAnnotationsBase {

    @OurBeforeClass
    private void before_class_test_private() {
        orderMethods.add(BEFORE_CLASS_TEST_PRIVATE);
    }

    @OurBeforeMethod
    private void before_method_test_private() {
        orderMethods.add(BEFORE_METHOD_TEST_PRIVATE);
    }

    @Test
    public void test() {
        Assert.assertEquals(orderMethods.size(), 2, "Incorrect size of called methods");

        Assert.assertEquals(orderMethods.get(0), BEFORE_CLASS_TEST_PRIVATE, "Incorrect called method by index 0");
        Assert.assertEquals(orderMethods.get(1), BEFORE_METHOD_TEST_PRIVATE, "Incorrect called method by index 1");
    }
}
