package com.wiley.autotest.selenium.elements;

/**
 * User: ntyukavkin
 * Date: 21.04.2015
 * Time: 17:22
 * Test Case modified date:
 * Preconditions:
 * Description:
 */
public interface DropDown extends Select {
    public void open();

    public void close();

    public void unselectByText(String text);
}
