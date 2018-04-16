package com.wiley.autotest.selenium.context;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.*;
import com.wiley.autotest.selenium.elements.upgrade.*;
import com.wiley.autotest.selenium.elements.upgrade.waitfor.CustomWaitFor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.wiley.autotest.selenium.SeleniumHolder.getWebDriver;

/**
 * Entry point to search for all elements on the screen
 */
public abstract class TeasyElementProvider {

    public static final Logger LOGGER = LoggerFactory.getLogger(TeasyElementProvider.class);

    //default finder that uses timeout from pom
    private TeasyElementFinder finder;

    //VE added this to avoid No buffer space available exception. To be replaced with default value of 500 if does not work.
    protected static final long SLEEP_IN_MILLISECONDS = 1000;

    protected TeasyElementFinder customFinder(SearchStrategy strategy) {
        return new TeasyElementFinder(getWebDriver(), strategy);
    }

    protected TeasyElementFinder finder() {
        if (finder == null) {
            finder = new TeasyElementFinder(getWebDriver(), new SearchStrategy());
        }
        return finder;
    }

    protected CustomWaitFor waitFor() {
        return new CustomWaitFor();
    }

    protected TeasyElement element(final By locator) {
        return finder().visibleElement(locator);
    }

    protected TeasyElement element(final By locator, SearchStrategy strategy) {
        return customFinder(strategy).visibleElement(locator);
    }

    protected TeasyElementList elements(final By locator) {
        return new TeasyElementList(finder().visibleElements(locator), locator);
    }

    protected TeasyElementList elements(final By locator, SearchStrategy strategy) {
        return new TeasyElementList(customFinder(strategy).visibleElements(locator), locator);
    }

    protected TeasyElement domElement(By locator) {
        return finder().presentInDomElement(locator);
    }

    protected TeasyElement domElement(By locator, SearchStrategy strategy) {
        return customFinder(strategy).presentInDomElement(locator);
    }

    protected TeasyElementList domElements(By locator) {
        return new TeasyElementList(finder().presentInDomElements(locator), locator);
    }

    protected TeasyElementList domElements(By locator, SearchStrategy strategy) {
        return new TeasyElementList(customFinder(strategy).presentInDomElements(locator), locator);
    }

    protected Alert alert() {
        return finder().alert();
    }

    protected Alert alert(SearchStrategy strategy) {
        return customFinder(strategy).alert();
    }

    protected Window window() {
        return new TeasyWindow(SeleniumHolder.getWebDriver());
    }

    protected Button button(By locator) {
        return getElement(Button.class, locator);
    }

    protected Button button(TeasyElement searchContext, By locator) {
        return getElement(Button.class, searchContext, locator);
    }

    protected List<Button> buttons(By locator) {
        return getElements(Button.class, locator);
    }

    protected List<Button> buttons(TeasyElement searchContext, By locator) {
        return getElements(Button.class, searchContext, locator);
    }

    protected Link link(By locator) {
        return getElement(Link.class, locator);
    }

    protected Link link(TeasyElement searchContext, By locator) {
        return getElement(Link.class, searchContext, locator);
    }

    protected List<Link> links(By locator) {
        return getElements(Link.class, locator);
    }

    protected List<Link> links(TeasyElement searchContext, By locator) {
        return getElements(Link.class, searchContext, locator);
    }

    protected CheckBox checkBox(By locator) {
        return getElement(CheckBox.class, locator);
    }

    protected CheckBox checkBox(TeasyElement searchContext, By locator) {
        return getElement(CheckBox.class, searchContext, locator);
    }

    protected List<CheckBox> checkBoxes(By locator) {
        return getElements(CheckBox.class, locator);
    }

    protected List<CheckBox> checkBoxes(TeasyElement searchContext, By locator) {
        return getElements(CheckBox.class, searchContext, locator);
    }

    protected RadioButton radioButton(By locator) {
        return getElement(RadioButton.class, locator);
    }

    protected RadioButton radioButton(TeasyElement searchContext, By locator) {
        return getElement(RadioButton.class, searchContext, locator);
    }

    protected List<RadioButton> radioButtons(By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected List<RadioButton> radioButtons(TeasyElement searchContext, By locator) {
        return getElements(RadioButton.class, locator);
    }

    protected Select select(By locator) {
        return getElement(Select.class, locator);
    }

    protected Select select(TeasyElement searchContext, By locator) {
        return getElement(Select.class, searchContext, locator);
    }

    protected List<Select> selects(By locator) {
        return getElements(Select.class, locator);
    }

    protected List<Select> selects(TeasyElement searchContext, By locator) {
        return getElements(Select.class, locator);
    }

    protected TextField textField(By locator) {
        return getElement(TextField.class, locator);
    }

    protected TextField textField(TeasyElement searchContext, By locator) {
        return getElement(TextField.class, searchContext, locator);
    }

    protected List<TextField> textFields(By locator) {
        return getElements(TextField.class, locator);
    }

    protected List<TextField> textFields(TeasyElement searchContext, By locator) {
        return getElements(TextField.class, searchContext, locator);
    }

    private <T extends Element> T getElement(Class<T> elementType, By by) {
        return new WebElementWrapper(element(by)).getElement(elementType, by);
    }

    private <T extends Element> T getElement(Class<T> elementType, TeasyElement searchContext, By by) {
        return new WebElementWrapper(searchContext.element(by)).getElement(elementType, by);
    }

    private <T extends Element> List<T> getElements(Class<T> elementType, By by) {
        List<T> result = new ArrayList<>();
        elements(by)
                .forEach(element -> result.add(new WebElementWrapper(element).getElement(elementType, by)));
        return result;
    }

    private <T extends Element> List<T> getElements(Class<T> elementType, TeasyElement searchContext, By by) {
        List<T> result = new ArrayList<>();
        searchContext.elements(by)
                .forEach(element -> result.add(new WebElementWrapper(element).getElement(elementType, by)));
        return result;
    }
}

