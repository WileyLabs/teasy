package com.wiley.autotest.framework.tests.annotations;

import com.wiley.autotest.annotations.OurAfterClass;
import com.wiley.autotest.annotations.OurAfterMethod;
import com.wiley.autotest.annotations.OurBeforeClass;
import com.wiley.autotest.annotations.OurBeforeMethod;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class OurBeforeAfterAnnotationsOrderBase extends OurBeforeAfterAnnotationsBase {

    @OurBeforeClass
    public void before_class_base() {
        orderMethods.add(BEFORE_CLASS_BASE);
    }

    @OurBeforeClass
    private void before_class_base_private() {
        orderMethods.add(BEFORE_CLASS_BASE_PRIVATE);
    }

    @OurBeforeMethod
    public void before_method_base() {
        orderMethods.add(BEFORE_METHOD_BASE);
    }

    @OurBeforeMethod
    private void before_method_base_private() {
        orderMethods.add(BEFORE_METHOD_BASE_PRIVATE);
    }

    @OurAfterMethod
    private void after_method_base_private() {
        orderMethods.add(AFTER_METHOD_BASE_PRIVATE);
    }

    @OurAfterMethod
    public void after_method_base_public() {
        orderMethods.add(AFTER_METHOD_BASE_PUBLIC);
    }

    @OurAfterClass
    private void after_class_base_private() {
        orderMethods.add(AFTER_CLASS_BASE_PRIVATE);
    }

    @OurAfterClass
    public void after_class_base_public() {
        orderMethods.add(AFTER_CLASS_BASE_PUBLIC);
    }

    @AfterSuite
    public void after_suite() {
        Assert.assertEquals(orderMethods.size(), 8, "Incorrect size of called methods");

        Assert.assertEquals(orderMethods.get(0), BEFORE_CLASS_BASE, "Incorrect called method by index 0");
        Assert.assertEquals(orderMethods.get(1), BEFORE_CLASS_TEST_PUBLIC, "Incorrect called method by index 1");
        Assert.assertEquals(orderMethods.get(2), BEFORE_METHOD_BASE, "Incorrect called method by index 2");
        Assert.assertEquals(orderMethods.get(3), BEFORE_METHOD_TEST_PUBLIC, "Incorrect called method by index 3");
        Assert.assertEquals(orderMethods.get(4), AFTER_METHOD_TEST_PUBLIC, "Incorrect called method by index 4");
        Assert.assertEquals(orderMethods.get(5), AFTER_METHOD_BASE_PUBLIC, "Incorrect called method by index 5");
        Assert.assertEquals(orderMethods.get(6), AFTER_CLASS_TEST_PUBLIC, "Incorrect called method by index 6");
        Assert.assertEquals(orderMethods.get(7), AFTER_CLASS_BASE_PUBLIC, "Incorrect called method by index 7");

        Assert.assertFalse(orderMethods.contains(BEFORE_CLASS_BASE_PRIVATE), "Private method with @OurBeforeClass from super class was called");
        Assert.assertFalse(orderMethods.contains(BEFORE_METHOD_BASE_PRIVATE), "Private method with @OurBeforeMethod from super class was called");

        Assert.assertFalse(orderMethods.contains(AFTER_METHOD_BASE_PRIVATE), "Private method with @OurAfterMethod from super class was called");
        Assert.assertFalse(orderMethods.contains(AFTER_CLASS_BASE_PRIVATE), "Private method with @OurAfterClass from super class was called");
    }
}
