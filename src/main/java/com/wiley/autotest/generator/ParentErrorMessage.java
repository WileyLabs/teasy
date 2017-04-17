package com.wiley.autotest.generator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by vfedorenko on 05/04/2017.
 * Takes the 4th method from a StackTrace chain. It should be the method from Page that called method
 * Split method by words based on camel case naming convention
 * Make first letter of first word as upper case and all other words as lower case
 * for example for "generate" method should return "Generate error message"
 */
@Component
@Qualifier("parent")
public class ParentErrorMessage implements ErrorMessage {
    /**
     * @return String of words separated by spaces
     */
    public String generate() {
        return generate(4);
    }
}
