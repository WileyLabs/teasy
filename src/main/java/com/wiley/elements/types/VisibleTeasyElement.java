package com.wiley.elements.types;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElementData;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.should.Should;
import com.wiley.elements.should.VisibleShould;
import com.wiley.elements.waitfor.ElementWaitFor;
import com.wiley.elements.waitfor.VisibleElementWaitFor;
import com.wiley.holders.DriverHolder;

import static com.wiley.holders.DriverHolder.*;

/**
 * Represents element that is displayed for the user
 */
public class VisibleTeasyElement extends BaseTeasyElement {

    public VisibleTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new VisibleShould(this, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new VisibleShould(this, strategy, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public ElementWaitFor waitFor() {
        return new VisibleElementWaitFor(this, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new VisibleElementWaitFor(this, strategy, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }
}
