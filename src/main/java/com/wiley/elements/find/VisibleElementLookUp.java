package com.wiley.elements.find;

import com.wiley.elements.*;
import com.wiley.elements.conditions.FramesConditionFactory;
import com.wiley.elements.types.TeasyElementType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static com.wiley.elements.types.TeasyElementType.NULL;
import static com.wiley.elements.types.TeasyElementType.VISIBLE;
/**
 * Finding a visible element
 */
public class VisibleElementLookUp implements LookUpWithLocator<TeasyElement> {

    private final TeasyElement context;
    private final TeasyFluentWait<WebDriver> fluentWait;
    private final FramesConditionFactory conditionFactory;
    private final SearchStrategy strategy;

    public VisibleElementLookUp(WebDriver driver, SearchStrategy strategy) {
        this(driver, strategy, null);
    }

    public VisibleElementLookUp(WebDriver driver, SearchStrategy strategy, TeasyElement context) {
        this.fluentWait = new TeasyFluentWait<>(driver, strategy);
        this.strategy = strategy;
        this.context = context;
        this.conditionFactory = new FramesConditionFactory(context, strategy.getFrameStrategy());
    }

    @Override
    public TeasyElement find(By locator) {
        WebElement webElement;
        try {
            webElement = fluentWait.waitFor(conditionFactory.get().visibility(locator));
        } catch (AssertionError ignoredToReturnEmptyList) {
            webElement = null;
        }
        if (webElement == null) {
            //this is needed to return null when the user explicitly set to receive null in case of failure
            if (strategy.isNullOnFailure()) {
                return null;
            }
        }
        TeasyElementData data = new TeasyElementData(context, webElement, locator);
        TeasyElementType type = webElement == null ? NULL : VISIBLE;
        return ElementWrapper.wrap(data, type);
    }
}
