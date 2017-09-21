package com.wiley.autotest.selenium.elements.upgrade.v3.conditions;

import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.context.SearchStrategy;

/**
 * Created by vefimov on 25/05/2017.
 */
public class FramesConditionFactory {

    private TeasyElement context;
    private SearchStrategy.FrameStrategy frameStrategy;

    public FramesConditionFactory(TeasyElement context, SearchStrategy.FrameStrategy frameStrategy) {
        this.context = context;
        this.frameStrategy = frameStrategy;
    }

    public FramesConditionFactory(SearchStrategy.FrameStrategy frameStrategy) {
        this(null, frameStrategy);
    }

    public ElementCondition get() {
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
