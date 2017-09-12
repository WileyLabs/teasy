package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.elements.upgrade.v3.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.Should;
import com.wiley.autotest.selenium.elements.upgrade.v3.VisibleElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.v3.VisibleShould;
import com.wiley.autotest.selenium.context.SearchStrategy;

import static com.wiley.autotest.utils.TestUtils.waitForSomeTime;


/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:41
 */
public class VisibleTeasyElement extends BaseTeasyElement {

    public VisibleTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new VisibleShould(this);
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new VisibleShould(this, strategy);
    }

    @Override
    public ElementWaitFor waitFor() {
        return new VisibleElementWaitFor(this);
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new VisibleElementWaitFor(this, strategy);
    }

}
