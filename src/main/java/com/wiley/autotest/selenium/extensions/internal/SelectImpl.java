package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Select;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.math.RandomUtils.nextInt;

class SelectImpl extends AbstractEnabledElement implements Select {
    protected SelectImpl(final WebElement wrappedElement) {
        super(wrappedElement);
    }

    protected SelectImpl(final WebElement wrappedElement, By by) {
        super(wrappedElement, by);
    }

    @Override
    public void selectByText(final String text) {
        wrappedSelect().selectByVisibleText(text);
        waitInIE();
    }

    @Override
    public void selectByTextWithoutWait(final String text) {
        selectByText(text);
    }

    @Override
    public void selectByIndex(final int index) {
        if (isIE()) {
            String value = getOptions().get(index).getText();
            selectByText(value);
        } else {
            wrappedSelect().selectByIndex(index);
        }
    }

    @Override
    public void selectByValue(final String value) {
        if (isIE()) {
            for (WebElement element : getOptions()) {
                if (value.equals(element.getAttribute("value"))) {
                    selectByText(element.getText());
                    break;
                }
            }
        } else {
            wrappedSelect().selectByValue(value);
        }
    }

    @Override
    public void selectByText(final String text, final String errorMessage) {
        try {
            selectByText(text);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    @Override
    public void selectByIndex(final int index, final String errorMessage) {
        try {
            selectByIndex(index);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    @Override
    public void selectByValue(final String value, final String errorMessage) {
        try {
            selectByValue(value);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    @Override
    public String getText() {
        return wrappedSelect().getFirstSelectedOption().getText();
    }

    @Override
    public WebElement getWrappedWebElement() {
        return getWrappedElement();
    }

    @Override
    public void selectByAnotherTextThan(final String text) {
        final org.openqa.selenium.support.ui.Select select = wrappedSelect();
        final List<WebElement> options = select.getOptions();
        for (int i = options.size() - 1; i >= 0; i--) {
            final WebElement each = options.get(i);
            if (!each.getText().equals(text)) {
                select.selectByIndex(i);
                waitInIE();
                return;
            }
        }
    }

    @Override
    public void selectRandom() {
        int count = 0;
        while (count < 5) {
            try {
                final org.openqa.selenium.support.ui.Select wrapped = wrappedSelect();
                int optionsSize = getOptions().size();
                if (optionsSize > 1) {
                    int optionIndex = nextInt(optionsSize);
                    while (getOptions().get(optionIndex).isSelected()) {
                        optionIndex = nextInt(optionsSize);
                    }
                    wrapped.selectByIndex(optionIndex);
                    waitInIE();
                }
                break;
            } catch (StaleElementReferenceException e) {
                TestUtils.waitForSomeTime(1000, EXPLANATION_MESSAGE_FOR_WAIT);
            }
        }
    }

    @Override
    public void selectRandomByAnotherTextThan(String text) {
        int optionsSize = getOptions().size();
        if (optionsSize > 1) {
            int optionIndex = nextInt(optionsSize);
            while (getOptions().get(optionIndex).isSelected() || text.equals(getOptions().get(optionIndex).getText())) {
                optionIndex = nextInt(optionsSize);
            }
            selectByIndex(optionIndex);
        }
    }

    @Override
    public void selectByPartialText(String partialText) {
        for (WebElement option : getOptions()) {
            if (option.getText().contains(partialText)) {
                selectByText(option.getText());
                return;
            }
        }
    }

    private org.openqa.selenium.support.ui.Select wrappedSelect() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement());
    }

    @Override
    public WebElement getSelectedOption() {
        return wrappedSelect().getFirstSelectedOption();
    }

    @Override
    public String getSelectedText() {
        return wrappedSelect().getFirstSelectedOption().getText();
    }

    @Override
    public String getSelectedValue() {
        return getWrappedElement().getAttribute("value");
    }

    @Override
    public List<WebElement> getOptions() {
        try {
            return wrappedSelect().getOptions();
        } catch (UndeclaredThrowableException ignored) {
            //TODO VF fix it in other places
            //Sometimes this test fails in ie due to such exception
            TestUtils.waitForSomeTime(3000, EXPLANATION_MESSAGE_FOR_WAIT);
            return wrappedSelect().getOptions();
        }
    }

    @Override
    @Deprecated
    public List<WebElement> getValues() {
        //TODO VF Need to complete
        return new ArrayList<WebElement>();
    }

    @Override
    public int getSelectedIndex() {
        return Integer.parseInt(getSelectedOption().getAttribute("index"));
    }

    /**
     * Contains logic to avoid StaleElementException
     * That appears from time to time
     * Using our standard mechanism of 5 attempts with sleeping for 1 sec between them.
     */
    @Override
    public void deselectAll() {
        int counter = 0;
        while (counter <= 5) {
            try {
                wrappedSelect().deselectAll();
                break;
            } catch (StaleElementReferenceException e) {
                counter++;
                TestUtils.waitForSomeTime(1000, EXPLANATION_MESSAGE_FOR_WAIT);
            }
        }
    }

    @Override
    public void selectAll() {
        //TODO NT: need to check work
        for (WebElement option : getOptions()) {
            selectByText(option.getText());
        }
    }

    private void waitInIE() {
        if (isIE()) {
            getElementFinder().waitForPageToLoad();
        }
    }
}
