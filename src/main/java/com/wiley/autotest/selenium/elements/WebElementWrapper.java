package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.extensions.ElementFactory;
import com.wiley.autotest.selenium.extensions.internal.DefaultElementFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: maxim
 * Date: 14.02.12
 */
public class WebElementWrapper {
    private final ElementFactory elementFactory = new DefaultElementFactory();
    private final WebElement webElement;

    public WebElementWrapper(final WebElement webElement) {
        this.webElement = webElement;
    }

    public Select getSelect() {
        return elementFactory.create(Select.class, webElement);
    }

    public Button getButton() {
        return elementFactory.create(Button.class, webElement);
    }

    public Link getLink() {
        return elementFactory.create(Link.class, webElement);
    }

    public TextField getTextField() {
        return elementFactory.create(TextField.class, webElement);
    }

    public RadioButton getRadioButton() {
        return elementFactory.create(RadioButton.class, webElement);
    }

    public CheckBox getCheckBox() {
        return elementFactory.create(CheckBox.class, webElement);
    }

    public DropDown getDropDown() {
        return elementFactory.create(DropDown.class, webElement);
    }

    public <T extends Element> T getElement(Class<T> elementType, By by) {
        return elementFactory.create(elementType, webElement, by);
    }
}
