package com.wiley.autotest.selenium.elements;

import com.wiley.autotest.selenium.elements.upgrade.OurWebElement;
import com.wiley.autotest.selenium.extensions.ElementFactory;
import com.wiley.autotest.selenium.extensions.internal.DefaultElementFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * User: maxim
 * Date: 14.02.12
 */
public class WebElementWrapper {


    //TODO VE refactor this to keep only OurWebElement!

    public final ElementFactory elementFactory = new DefaultElementFactory();
    public WebElement webElement;
    public OurWebElement element;

    public WebElementWrapper(final WebElement webElement) {
        this.webElement = webElement;
    }

    public WebElementWrapper(final OurWebElement element) {
        this.element = element;
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

    public <T extends Element> T getElement(Class<T> elementType, By by) {
        return elementFactory.create(elementType, webElement, by);
    }

}
