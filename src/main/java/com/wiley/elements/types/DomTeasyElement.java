package com.wiley.elements.types;

import com.wiley.elements.SearchStrategy;
import com.wiley.elements.TeasyElementData;
import com.wiley.elements.TeasyFluentWait;
import com.wiley.elements.should.DomShould;
import com.wiley.elements.should.Should;
import com.wiley.elements.waitfor.DomElementWaitFor;
import com.wiley.elements.waitfor.ElementWaitFor;
import com.wiley.holders.DriverHolder;

import static com.wiley.holders.DriverHolder.*;

/**
 * Reprsents element that is present in DOM (not necessarily visible)
 */
public class DomTeasyElement extends BaseTeasyElement {

    public DomTeasyElement(TeasyElementData teasyElementData) {
        super(teasyElementData);
    }

    @Override
    public Should should() {
        return new DomShould(this, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public Should should(SearchStrategy strategy) {
        return new DomShould(this, strategy, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public ElementWaitFor waitFor() {
        return new DomElementWaitFor(this, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

    @Override
    public ElementWaitFor waitFor(SearchStrategy strategy) {
        return new DomElementWaitFor(this, strategy, new TeasyFluentWait<>(getDriver(), new SearchStrategy()));
    }

}
