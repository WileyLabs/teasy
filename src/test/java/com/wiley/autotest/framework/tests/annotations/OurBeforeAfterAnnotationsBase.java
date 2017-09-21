package com.wiley.autotest.framework.tests.annotations;

import com.wiley.autotest.selenium.AbstractWebServiceTest;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ntyukavkin
 * Date: 30.05.2017
 * Time: 17:42
 */
public class OurBeforeAfterAnnotationsBase extends AbstractWebServiceTest {

    List<String> orderMethods = new ArrayList<>();

    static final String BEFORE_CLASS_BASE = "before_class_base";
    static final String BEFORE_CLASS_BASE_PRIVATE = "before_class_base_private";

    static final String BEFORE_METHOD_BASE = "before_method_base";
    static final String BEFORE_METHOD_BASE_PRIVATE = "before_method_base_private";

    static final String BEFORE_CLASS_TEST_PUBLIC = "before_class_test_public";
    static final String BEFORE_CLASS_TEST_PRIVATE = "before_class_test_private";

    static final String BEFORE_METHOD_TEST_PUBLIC = "before_method_test_public";
    static final String BEFORE_METHOD_TEST_PRIVATE = "before_method_test_private";

    static final String AFTER_METHOD_TEST_PUBLIC = "after_method_test_public";
    static final String AFTER_METHOD_TEST_PRIVATE = "after_method_test_private";

    static final String AFTER_METHOD_BASE_PUBLIC = "after_method_base_public";

    static final String AFTER_CLASS_TEST_PUBLIC = "after_class_test_public";
    static final String AFTER_CLASS_TEST_PRIVATE = "after_class_test_private";

    static final String AFTER_CLASS_BASE_PUBLIC = "after_class_base_public";

    static final String AFTER_METHOD_BASE_PRIVATE = "after_method_base_private";
    static final String AFTER_CLASS_BASE_PRIVATE = "after_class_base_private";
}
