package com.wiley.autotest.framework.tests.annotations;

import com.wiley.autotest.annotations.OurAfterClass;
import com.wiley.autotest.annotations.OurAfterMethod;
import com.wiley.autotest.annotations.OurBeforeClass;
import com.wiley.autotest.annotations.OurBeforeMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class OurBeforeAfterAnnotationsOrderTest extends OurBeforeAfterAnnotationsOrderBase {

    @OurBeforeClass
    public void before_class_test_public() {
        orderMethods.add(BEFORE_CLASS_TEST_PUBLIC);
    }

    @OurBeforeMethod
    public void before_method_test_public() {
        orderMethods.add(BEFORE_METHOD_TEST_PUBLIC);
    }

    @Test
    public void test() {
        Assert.assertEquals(orderMethods.size(), 4, "Incorrect size of called methods");

        Assert.assertEquals(orderMethods.get(0), BEFORE_CLASS_BASE, "Incorrect called method by index 0");
        Assert.assertEquals(orderMethods.get(1), BEFORE_CLASS_TEST_PUBLIC, "Incorrect called method by index 1");
        Assert.assertEquals(orderMethods.get(2), BEFORE_METHOD_BASE, "Incorrect called method by index 2");
        Assert.assertEquals(orderMethods.get(3), BEFORE_METHOD_TEST_PUBLIC, "Incorrect called method by index 3");

        Assert.assertFalse(orderMethods.contains(BEFORE_CLASS_BASE_PRIVATE), "Private method with @OurBeforeClass from super class was called");
        Assert.assertFalse(orderMethods.contains(BEFORE_METHOD_BASE_PRIVATE), "Private method with @OurBeforeMethod from super class was called");
    }

    @OurAfterMethod
    public void after_method_test_public() {
        orderMethods.add(AFTER_METHOD_TEST_PUBLIC);
    }

    @OurAfterClass
    public void after_class_test_public() {
        orderMethods.add(AFTER_CLASS_TEST_PUBLIC);
    }
}
