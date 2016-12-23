package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.context.AbstractElementFinder;
import com.wiley.autotest.selenium.elements.DropDown;
import com.wiley.autotest.utils.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.wiley.autotest.utils.ExecutionUtils.isIE;

/**
 * User: ntyukavkin
 * Date: 21.04.2015
 * Time: 17:23
 * Test Case modified date:
 * Preconditions:
 * Description:
 */
public class DropDownImpl extends AbstractElement implements DropDown {

    protected WebElement dropdown;
    protected WebElement selectedTextElement;
    private WebElement table;

    protected DropDownImpl(final WebElement element) {
        super(element);
        this.dropdown = element;
        this.table = dropdown.findElement(By.cssSelector("[id*='table']"));
        this.selectedTextElement = dropdown.findElement(By.xpath(".//td[contains(@class,'customSelect') and not(@class='customSelectTriangleDown')]"));
    }

    protected DropDownImpl(WebElement element, By locator) {
        super(element, locator);
        this.dropdown = element;
        this.table = dropdown.findElement(By.cssSelector("[id*='table']"));
        this.selectedTextElement = dropdown.findElement(By.xpath(".//td[contains(@class,'customSelect') and not(@class='customSelectTriangleDown')]"));
    }

    public void open() {
        if (isDropDownClosed()) {
            boolean isOpened = false;
            for (int i = 0; i < 5; i++) {
                try {
                    getElementFinder().waitForPageToLoad();
                    getElementFinder().waitForListToLoad();
                    table.click();
                    getElementFinder().waitForElementContainsAttributeValue(table, "class", "Opened");
                    getElementFinder().waitForPageToLoad();
                    getElementFinder().waitForListToLoad();
                    isOpened = true;
                    break;
                } catch (WebDriverException ignored) {
                    LOGGER.error(ignored.getMessage());
                }
            }
            if (!isOpened) {
                AbstractElementFinder.fail("Can't find dropdown options");
            }
        }
    }

    public void openWithoutWait() {
        if (isDropDownClosed()) {
            boolean isOpened = false;
            for (int i = 0; i < 5; i++) {
                try {
                    getElementFinder().waitForPageToLoad();
                    table.click();
                    getElementFinder().waitForElementContainsAttributeValue(table, "class", "Opened");
                    getElementFinder().waitForPageToLoad();
                    isOpened = true;
                    break;
                } catch (WebDriverException ignored) {
                    LOGGER.error(ignored.getMessage());
                }
            }
            if (!isOpened) {
                AbstractElementFinder.fail("Can't find dropdown options");
            }
        }
    }

    public void close() {
        if (!isDropDownClosed()) {
            table.click();
            getElementFinder().waitForElementNotContainsAttributeValue(table, "class", "Opened");
        }
    }

    public String getSelectedText() {
        return selectedTextElement.getText();
    }

    public void selectByText(String value) {
        if (getSelectedText().equals(value)) {
            return;
        }
        open();
        selectText(value);
        close();
    }

    @Override
    public void selectByTextWithoutWait(String text) {
        if (getSelectedText().equals(text)) {
            return;
        }
        openWithoutWait();
        selectText(text);
        close();
    }

    public void unselectByText(String value) {
        open();
        unselectText(value);
        close();
    }

    public void selectByIndex(int index) {
        open();
        selectIndex(index);
        close();
    }

    public void selectAll() {
        open();
        selectAllOptions();
        close();
    }

    public void selectRandom() {
        open();
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(getOptions().size());
        selectIndex(randomIndex);
        close();
    }

    protected void selectText(String text) {
        for (WebElement option : getOptions()) {
            if (option.getText().equals(text)) {
                selectOption(option);
                break;
            }
            String optionText;
            if (Arrays.asList(option.getText().split("\\(")).size() > 2) {
                optionText = option.getText().substring(0, option.getText().lastIndexOf(" (")).trim();
            } else {
                optionText = option.getText().split("\\(")[0].trim();
            }
            if (optionText.equals(text)) {
                selectOption(option);
                break;
            }
        }
    }

    protected void selectTextWithoutWait(String text) {
        for (WebElement option : getOptions()) {
            String optionText;
            if (option.getText().split("\\(").length > 2) {
                optionText = option.getText().substring(0, option.getText().lastIndexOf(" (")).trim();
            } else {
                optionText = option.getText().split("\\(")[0].trim();
            }
            if (optionText.equals(text)) {
                selectOptionWithoutWait(option);
                break;
            }
        }
    }

    protected void unselectText(String text) {
        for (WebElement option : getOptions()) {
            String optionText;
            if (Arrays.asList(option.getText().split("\\(")).size() > 2) {
                optionText = option.getText().substring(0, option.getText().lastIndexOf(" (")).trim();
            } else {
                optionText = option.getText().split("\\(")[0].trim();
            }
            if (optionText.equals(text)) {
                unselectOption(option);
                break;
            }
        }
    }

    protected void selectRandomByAnotherThan(String text) {
        List<String> otherOptions = new ArrayList<String>();
        for (WebElement option : getOptions()) {
            String optionText = option.getText().split("\\(")[0].trim();
            if (!optionText.equals(text)) {
                otherOptions.add(optionText);
            }
        }

        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(otherOptions.size());
        String randomText = otherOptions.get(randomIndex);
        for (WebElement option : getOptions()) {
            if (option.getText().split("\\(")[0].trim().equals(randomText)) {
                selectOption(option);
                break;
            }
        }
    }

    //Use only for filter's
    protected void selectValue(String value) {
        for (WebElement option : getOptions()) {
            List<WebElement> optionElements = option.findElements(By.cssSelector("[class*='value']"));
            if (!optionElements.isEmpty() && optionElements.get(0).getAttribute("class").split("value_")[1].equals(value)) {
                selectOption(option);
                break;
            }
        }
    }

    protected void selectIndex(int index) {
        WebElement option = getOptions().get(index);
        selectOption(option);
    }

    protected void selectAllOptions() {
        for (WebElement option : getOptions()) {
            selectOption(option);
        }
    }

    protected void unselectAllOptions() {
        for (WebElement option : getSelectedOptions()) {
            unselectOption(option);
        }
    }

    private void selectOption(WebElement option) {
        if (option.findElements(By.tagName("label")).isEmpty()) {
            if (isIE()) {
                executeScript("arguments[0].click();", option);
            } else {
                option.click();
            }
        } else {
            if (!option.findElement(By.tagName("input")).isSelected()) {
                if (isIE()) {
                    executeScript("arguments[0].click();", option.findElement(By.tagName("label")));
                } else {
                    option.findElement(By.tagName("label")).click();
                }
            }
        }
        try {
            getElementFinder().waitForAbsenceOfElementLocatedBy(By.cssSelector("[id*='Loader']"));
        }catch (WebDriverException e){
        }
        getElementFinder().waitForListToLoad();
    }

    private void selectOptionWithoutWait(WebElement option) {
        if (option.findElements(By.tagName("label")).isEmpty()) {
            if (isIE()) {
                executeScript("arguments[0].click();", option);
            } else {
                option.click();
            }
        } else {
            if (!option.findElement(By.tagName("input")).isSelected()) {
                if (isIE()) {
                    executeScript("arguments[0].click();", option.findElement(By.tagName("label")));
                } else {
                    option.findElement(By.tagName("label")).click();
                }
            }
        }
    }

    private void unselectOption(WebElement option) {
        if (option.findElements(By.tagName("label")).isEmpty()) {
            option.click();
        } else {
            if (option.findElement(By.tagName("input")).isSelected()) {
                option.findElement(By.tagName("label")).click();
            }
        }
        getElementFinder().waitForListToLoad();
    }

    private boolean isDropDownClosed() {
        return getWrappedElement().findElements(By.cssSelector("[class*='Opened']")).isEmpty() || !table.getAttribute("class").contains("Opened");
    }

    @Override
    public void selectByValue(String value) {
        //TODO NT: need to complete
    }

    @Override
    public void selectByText(String key, String errorMessage) {
        //TODO NT: need to complete
    }

    @Override
    public void selectByIndex(int index, String errorMessage) {
        //TODO NT: need to complete
    }

    @Override
    public void selectByValue(String value, String errorMessage) {
        //TODO NT: need to complete
    }

    @Override
    public void selectByAnotherTextThan(String text) {
        open();
        selectRandomByAnotherThan(text);
        close();
    }

    @Override
    public boolean isEnabled() {
        return !getWrappedElement().findElement(By.xpath("./..")).getAttribute("class").equals("customSelectLinkDisabled");
    }

    @Override
    public String getSelectedValue() {
        //TODO NT: need to complete
        return getSelectedText();
    }

    @Override
    public WebElement getSelectedOption() {
        WebElement selectedOption = null;
        open();
        List<WebElement> optionList = getOptions();
        for (WebElement option : optionList) {
            if (!option.findElements(By.cssSelector("[checked='checked']")).isEmpty()) {
                selectedOption = option;
                break;
            }
        }
        close();
        return selectedOption;
    }

    public List<WebElement> getOptions() {
//TODO VF Delete it if it will be work fine after 20.10.15
        if (dropdown.findElements(By.xpath("//*[contains(@id,'options')]//tr//td[contains(@class,'Option')]")).isEmpty()) {
            //    By unknown reason sometimes there is empty list w/o waiting
            TestUtils.waitForSomeTime(3000, EXPLANATION_MESSAGE_FOR_WAIT);
        }
        return dropdown.findElements(By.xpath("//*[contains(@id,'options')]//tr//td[contains(@class,'Option')]"));
    }

    public List<WebElement> getValues() {
        return dropdown.findElements(By.xpath("//div[contains(@class,'value')]"));
    }

    public List<WebElement> getSelectedOptions() {
        List<WebElement> selectedOptions = new ArrayList<WebElement>();
        List<WebElement> options = dropdown.findElements(By.xpath("//*[contains(@id,'options')]//tr//td[contains(@class,'Option')]"));
        for (WebElement option : options) {
            if (!option.findElements(By.cssSelector("[checked='checked']")).isEmpty()) {
                selectedOptions.add(option);
            }
        }
        return selectedOptions;
    }

    @Override
    public int getSelectedIndex() {
        //TODO NT: need to complete
        return 0;
    }

    @Override
    public void deselectAll() {
        unselectAllOptions();
    }

    @Override
    public void selectRandomByAnotherTextThan(String text) {
        open();
        selectRandomByAnotherThan(text);
        close();
    }

    @Override
    public void selectByPartialText(String partialText) {
        //TODO NT: need to complete
    }


}
