package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.v3.*;


/**
 * User: vefimov
 * Date: 27.08.2014
 * Time: 15:41
 */
public class DomTeasyElement extends BaseTeasyElement {

    public DomTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new DomShould(this);
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new DomShould(this, strategy);
    }

    @Override
    public ElementWaitFor waitFor() {
        return new DomElementWaitFor(this);
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new DomElementWaitFor(this, strategy);
    }

}
