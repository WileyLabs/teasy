package com.wiley.autotest.selenium.extensions.internal;

import com.wiley.autotest.selenium.elements.Filter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Random;

/**
 * User: ntyukavkin
 * Date: 21.04.2015
 * Time: 17:28
 * Test Case modified date:
 * Preconditions:
 * Description:
 */
public class FilterImpl extends DropDownImpl implements Filter {

    protected FilterImpl(WebElement element) {
        super(element);
    }

    protected FilterImpl(WebElement element, By locator) {
        super(element, locator);
    }

    @Override
    public void selectByText(String value) {
        if (getSelectedText().equals(value)) {
            return;
        }
        open();
        selectText(value);
        clickApplyFilterButton();
        close();
    }

    public void selectByTextWithoutWait(String value) {
        openWithoutWait();
        selectTextWithoutWait(value);
        clickApplyFilterButtonWithoutLoad();
        close();
    }

    @Override
    public void selectByIndex(int index) {
        open();
        selectIndex(index);
        clickApplyFilterButton();
        close();
    }

    public void selectByValue(String value) {
        open();
        makeSureFilterOptionsAppeared();
        selectValue(value);
        clickApplyFilterButton();
        close();
    }

    /**
     * At present time 'Select all' link is displayed all the time if filter is active and clickable,
     * therefore to make sure that options are displayed we are waiting for this link.
     * Added 45 sec timeout because on stg 30 is not enough
     */
    private void makeSureFilterOptionsAppeared() {
        getElementFinder().waitForVisibilityOfElementLocatedBy(By.linkText("Select all"), 45);
    }

    @Override
    public void selectAll() {
        open();
        selectAllOptions();
        clickApplyFilterButton();
        close();
    }

    @Override
    public void selectRandom() {
        open();
        Random randomGenerator = new Random();
        int randomIndex = randomGenerator.nextInt(getOptions().size());
        selectIndex(randomIndex);
        clickApplyFilterButton();
        close();
    }

    @Override
    public void selectByAnotherTextThan(String text) {
        open();
        unselectAllOptions();
        selectRandomByAnotherThan(text);
        clickApplyFilterButton();
        close();
    }

    public String getSelectedText() {
        open();
        String selectedText;
        //TODO NT: need to rewrite
        if (!dropdown.findElements(By.cssSelector("[checked='checked']")).isEmpty()) {
            WebElement checked = dropdown.findElement(By.cssSelector("[checked='checked']"));
            selectedText = checked.findElement(By.xpath("./..")).getText().split("\\(")[0].trim();
        } else {
            selectedText = selectedTextElement.getText();
        }
        close();
        return selectedText;
    }

    private void clickApplyFilterButton() {
        dropdown.findElement(By.cssSelector("[id*='ApplyButton']")).click();
        getElementFinder().waitForListToLoad();
    }

    private void clickApplyFilterButtonWithoutLoad() {
        dropdown.findElement(By.cssSelector("[id*='ApplyButton']")).click();
    }

    @Override
    public void deselectAll() {
        open();
        unselectAllOptions();
        clickApplyFilterButton();
        close();
    }

    public void deselectAllAndSelectByText(String text) {
        open();
        unselectAllOptions();
        selectText(text);
        clickApplyFilterButton();
        close();
    }

    public void deselectAndSelectByText(String textToDeselect, String textToSelect) {
        open();
        unselectText(textToDeselect);
        selectText(textToSelect);
        clickApplyFilterButton();
        close();
    }

    @Override
    public boolean isEnabled() {
        String filterClass = getWrappedElement().findElement(By.xpath("./..")).getAttribute("class");
        return !(filterClass.contains("customSelectLink") && filterClass.contains("disabled"));
    }
}
