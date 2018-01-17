package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.context.SearchStrategy;
import com.wiley.autotest.selenium.elements.upgrade.should.DomShould;
import com.wiley.autotest.selenium.elements.upgrade.should.Should;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.DomElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.ElementWaitFor;

/**
 * Reprsents element that is present in DOM (not necessarily visible)
 */
public class DomTeasyElement extends BaseTeasyElement {

    public DomTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new DomShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new DomShould(this, strategy, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public ElementWaitFor waitFor() {
        return new DomElementWaitFor(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new DomElementWaitFor(this, strategy, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

}
