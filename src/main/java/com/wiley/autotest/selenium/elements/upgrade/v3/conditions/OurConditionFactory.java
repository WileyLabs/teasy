package com.wiley.autotest.selenium.elements.upgrade.v3.conditions;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.context.SearchStrategy;

/**
 * Created by vefimov on 25/05/2017.
 */
public class OurConditionFactory {

    private OurWebElement context;

    public OurConditionFactory(OurWebElement context) {
        this.context = context;
    }

    public OurCondition get(SearchStrategy.FrameStrategy frameStrategy) {
        if (context == null) {
            switch (frameStrategy) {
                case FIRST_FOUND:
                    return new FirstFound();
                case IN_ALL_FRAMES:
                    return new FirstFoundInAllFrames();
                default:
                    return new FirstFound();
            }
        } else {
            switch (frameStrategy) {
                case FIRST_FOUND:
                    return new FirstFoundInContext(context);
                case IN_ALL_FRAMES:
                    return new FirstFoundInAllFramesInContext(context);
                default:
                    return new FirstFoundInContext(context);
            }
        }

    }
}
