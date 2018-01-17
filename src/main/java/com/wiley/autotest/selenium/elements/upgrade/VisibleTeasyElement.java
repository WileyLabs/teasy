package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.ElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.should.Should;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.VisibleElementWaitFor;
import com.wiley.autotest.selenium.elements.upgrade.should.VisibleShould;
import com.wiley.autotest.selenium.context.SearchStrategy;

import static com.wiley.autotest.utils.TestUtils.waitForSomeTime;


/**
 * Represents element that is displayed for the user
 */
public class VisibleTeasyElement extends BaseTeasyElement {

    public VisibleTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new VisibleShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new VisibleShould(this, strategy, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public ElementWaitFor waitFor() {
        return new VisibleElementWaitFor(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new VisibleElementWaitFor(this, strategy, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
    }

}
