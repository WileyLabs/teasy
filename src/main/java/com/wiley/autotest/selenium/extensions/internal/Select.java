package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.upgrade.DomTeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementData;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang.math.RandomUtils.nextInt;

public class Select extends AbstractElement {
    public Select(final TeasyElement element) {
        super(element);
    }

    public void selectByText(final String text) {
        wrappedSelect().selectByVisibleText(text);
    }

    public void selectByTextWithoutWait(final String text) {
        selectByText(text);
    }

    public void selectByIndex(final int index) {
        wrappedSelect().selectByIndex(index);
    }

    public void selectByValue(final String value) {
        wrappedSelect().selectByValue(value);
    }

    public void selectByText(final String text, final String errorMessage) {
        try {
            selectByText(text);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    public void selectByIndex(final int index, final String errorMessage) {
        try {
            selectByIndex(index);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    public void selectByValue(final String value, final String errorMessage) {
        try {
            selectByValue(value);
        } catch (NoSuchElementException e) {
            Reporter.log("ERROR: " + errorMessage);
            throw new AssertionError(e);
        }
    }

    public String getText() {
        return wrappedSelect().getFirstSelectedOption().getText();
    }

    public void selectByAnotherTextThan(final String text) {
        final org.openqa.selenium.support.ui.Select select = wrappedSelect();
        final List<WebElement> options = select.getOptions();
        for (int i = options.size() - 1; i >= 0; i--) {
            final WebElement each = options.get(i);
            if (!each.getText().equals(text)) {
                select.selectByIndex(i);
                return;
            }
        }
    }

    public void selectRandom() {
        int count = 0;
        while (count < 5) {
            count++;
            try {
                final org.openqa.selenium.support.ui.Select wrapped = wrappedSelect();
                int optionsSize = getOptions().size();
                if (optionsSize > 1) {
                    int optionIndex = nextInt(optionsSize);
                    while (getOptions().get(optionIndex).isSelected()) {
                        optionIndex = nextInt(optionsSize);
                    }
                    wrapped.selectByIndex(optionIndex);
                }
                break;
            } catch (StaleElementReferenceException e) {
                TestUtils.waitForSomeTime(1000,
                        "StaleElementReferenceException. Sleeping 1 sec before retry.");
            }
        }
    }

    public void selectRandomByAnotherTextThan(String text) {
        int optionsSize = getOptions().size();
        if (optionsSize > 1) {
            int optionIndex = nextInt(optionsSize);
            while (getOptions().get(optionIndex)
                    .isSelected() || text.equals(getOptions().get(optionIndex).getText())) {
                optionIndex = nextInt(optionsSize);
            }
            selectByIndex(optionIndex);
        }
    }

    public void selectByPartialText(String partialText) {
        for (TeasyElement option : getOptions()) {
            if (option.getText().contains(partialText)) {
                selectByText(option.getText());
                return;
            }
        }
    }

    private org.openqa.selenium.support.ui.Select wrappedSelect() {
        return new org.openqa.selenium.support.ui.Select(getWrappedElement().getWrappedWebElement());
    }

    public TeasyElement getSelectedOption() {
        return new DomTeasyElement(new TeasyElementData(wrappedSelect().getFirstSelectedOption(), By
                .tagName("option")));
    }

    public String getSelectedText() {
        return wrappedSelect().getFirstSelectedOption().getText();
    }

    public String getSelectedValue() {
        return getWrappedElement().getAttribute("value");
    }

    public List<TeasyElement> getOptions() {
        try {
            return wrappedSelect().getOptions()
                    .stream()
                    .map(option -> new DomTeasyElement(new TeasyElementData(option, By.tagName("option"))))
                    .collect(Collectors.toList());
        } catch (UndeclaredThrowableException ignored) {
            TestUtils.waitForSomeTime(3000,
                    "Unknown issue. Sleeping 3 sec before retry.");
            return wrappedSelect().getOptions()
                    .stream()
                    .map(option -> new DomTeasyElement(new TeasyElementData(option, By.tagName("option"))))
                    .collect(Collectors.toList());
        }
    }

    public int getSelectedIndex() {
        return Integer.parseInt(getSelectedOption().getAttribute("index"));
    }

    /**
     * Contains logic to avoid StaleElementException
     * That appears from time to time
     * Using our standard mechanism of 5 attempts with sleeping for 1 sec between them.
     */

    public void deselectAll() {
        int counter = 0;
        while (counter <= 5) {
            try {
                wrappedSelect().deselectAll();
                break;
            } catch (StaleElementReferenceException e) {
                counter++;
                TestUtils.waitForSomeTime(1000,
                        "StaleElementReferenceException. Sleeping 1 sec before retry.");
            }
        }
    }

    public void selectAll() {
        //TODO NT: need to check work
        for (TeasyElement option : getOptions()) {
            selectByText(option.getText());
        }
    }
}
