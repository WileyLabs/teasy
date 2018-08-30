package com.wiley.autotest.framework.tests.elements;

import com.wiley.autotest.framework.config.BaseUnitTest;
import com.wiley.autotest.framework.pages.TestElementPage;
import org.testng.annotations.Test;

public class DragAndDropElement extends BaseUnitTest {
    @Test
    public void testDragAndDrop(){
        openPage("dragAndDrop.html", TestElementPage.class)
                .checkDragAndDrop();
    }
}
