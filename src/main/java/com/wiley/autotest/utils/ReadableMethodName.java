package com.wiley.autotest.utils;

/**
 * Method name representation in a readable format like in a sentence
 * for camelCaseMethodLikeThis it will return 'Camel case method like this"
 */
public class ReadableMethodName {
    private String name;

    public ReadableMethodName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String nameWithSpaces = name.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
        return nameWithSpaces.substring(0, 1).toUpperCase() + nameWithSpaces.substring(1);
    }
}
