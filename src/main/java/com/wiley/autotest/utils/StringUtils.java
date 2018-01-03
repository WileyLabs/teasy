package com.wiley.autotest.utils;

/**
 * Created by IntelliJ IDEA.
 * User: maxim
 * Date: 10.02.12
 * Time: 8:26
 */
//Will be deleted. If you need this class - copy it to your project.
@Deprecated
public final class StringUtils {

    /**
     * private constructor for utils class
     */
    private StringUtils() {
    }

    /**
     * Delete hyphens from a string.
     *
     * @param textWithHyphens
     * @return
     */
    public static String deleteHyphens(final String textWithHyphens) {
        return textWithHyphens.replaceAll("\n|\r\n", " ");
    }

    public static String replaceIllegalCharacter(String string) {
        return string.replace("‐", "-").replace("\n", " ").replace("&#8212;", "—").replace("&amp;", "&");
    }

    public static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

}
