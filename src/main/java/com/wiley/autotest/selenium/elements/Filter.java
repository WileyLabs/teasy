package com.wiley.autotest.selenium.elements;

/**
 * User: ntyukavkin
 * Date: 21.04.2015
 * Time: 17:27
 * Test Case modified date:
 * Preconditions:
 * Description:
 */
public interface Filter extends Select {

    public void open();

    public void close();

    void deselectAllAndSelectByText(String text);

    void deselectAndSelectByText(String textToDeselect, String textToSelect);
}
