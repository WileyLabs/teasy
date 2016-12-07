package com.wiley.autotest.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebElement;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * User: dfedorov
 * Date: 3/20/12
 * Time: 10:56 AM
 */
public final class SortingMatchers {

    private static final String DEFAULT_PATTERN = "MMM dd hh:mm a";
    private static final int MAX_DIGIT_COUNT = 10;
    private static final int MAX_PERCENTAGE_OF_INCORRECT_SORTING = 5;
    private static String pattern;

    /**
     * private constructor for utils class
     */
    private SortingMatchers() {
    }

    public static Matcher<List<WebElement>> sortedInAscendingOrder() {
        return sorted(TextComparator.ASC);
    }

    public static Matcher<List<WebElement>> sortedAsSymbolsInAscendingOrder() {
        return sorted(TextComparatorAsSymbols.ASC);
    }

    public static Matcher<List<WebElement>> sortedAsSymbolsInDescendingOrder() {
        return sorted(TextComparatorAsSymbols.DESC);
    }

    public static Matcher<List<WebElement>> sortedInDescendingOrder() {
        return sorted(TextComparator.DESC);
    }

    public static Matcher<List<String>> sortedStringsInAscendingOrder() {
        return sortedStrings(TextComparator.ASC);
    }

    public static Matcher<List<String>> sortedStringsInDescendingOrder() {
        return sortedStrings(TextComparator.DESC);
    }

    public static Matcher<List<WebElement>> sorted(final Comparator<String> comparator) {
        return new TypeSafeMatcher<List<WebElement>>() {
            @Override
            protected boolean matchesSafely(final List<WebElement> webElements) {
                return isWebElementsSortedByText(webElements, comparator);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static Matcher<List<String>> sortedStrings(final Comparator<String> comparator) {
        return new TypeSafeMatcher<List<String>>() {
            @Override
            protected boolean matchesSafely(final List<String> webElements) {
                return isStringsSorted(webElements, comparator);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    public static Matcher<List<WebElement>> sortedDatesInAscendingOrder() {
        return sortedDates(DateComparator.ASC, DEFAULT_PATTERN);
    }

    public static Matcher<List<WebElement>> sortedDatesInDescendingOrder() {
        return sortedDates(DateComparator.DESC, DEFAULT_PATTERN);
    }

    public static Matcher<List<WebElement>> sortedDatesInAscendingOrder(final String pattern) {
        return sortedDates(DateComparator.ASC, pattern);
    }

    public static Matcher<List<WebElement>> sortedDatesInDescendingOrder(final String pattern) {
        return sortedDates(DateComparator.DESC, pattern);
    }

    private static Matcher<List<WebElement>> sortedDates(final Comparator<String> comparator, final String pattern) {
        return new TypeSafeMatcher<List<WebElement>>() {
            @Override
            protected boolean matchesSafely(final List<WebElement> webElements) {
                synchronized (SortingMatchers.class) {
                    SortingMatchers.pattern = pattern;
                    return isWebElementsSortedByText(webElements, comparator);
                }
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private static boolean isWebElementsSortedByText(final List<WebElement> webElements, final Comparator<String> comparator) {
        final List<String> elementsTextList = getElementsText(webElements);
        return isStringsSorted(elementsTextList, comparator);
    }

    private static boolean isStringsSorted(List<String> elementsTextList, Comparator<String> comparator) {
        int numberOfIncorrectSorting = 0;
        for (int i = 0; i < elementsTextList.size() - 1; i++) {
            if (comparator.compare(elementsTextList.get(i), elementsTextList.get(i + 1)) > 0) {
                numberOfIncorrectSorting++;
            }
        }
        return ((double) (numberOfIncorrectSorting * 100 / elementsTextList.size())) < MAX_PERCENTAGE_OF_INCORRECT_SORTING;
    }

    private static List<String> getElementsText(List<WebElement> webElements) {
        final List<String> elementsTextList = new ArrayList<String>(webElements.size());
        for (WebElement element : webElements) {
            elementsTextList.add(element.getText());
        }

        return elementsTextList;
    }

    private enum TextComparator implements Comparator<String> {
        ASC {
            public int compare(String o1, String o2) {
                return getStringForSorting(o1).toLowerCase().compareTo(getStringForSorting(o2).toLowerCase());
            }
        },
        DESC {
            public int compare(String o1, String o2) {
                return -ASC.compare(o1, o2);
            }
        };

        private static String getStringForSorting(String stringForSorting) {
            final Pattern digitsPattern = Pattern.compile("\\d+");
            final java.util.regex.Matcher matcher = digitsPattern.matcher(stringForSorting);
            final StringBuffer buf = new StringBuffer();
            while (matcher.find()) {
                matcher.appendReplacement(buf, appendZeros(matcher.group()));
            }
            matcher.appendTail(buf);
            return buf.toString();
        }

        private static String appendZeros(String str) {
            final int length = str.length();
            String result = str;
            if (length < MAX_DIGIT_COUNT) {
                for (int i = 0; i < MAX_DIGIT_COUNT - length; i++) {
                    result = "0" + result;
                }
            }
            return result;
        }

    }

    private enum DateComparator implements Comparator<String> {
        ASC {
            public int compare(String o1, String o2) {
                final DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.US);
                final Date date1;
                final Date date2;
                try {
                    date1 = dateFormat.parse(o1);
                    date2 = dateFormat.parse(o2);
                } catch (ParseException e) {
                    throw new AssertionError("Incorrect date format " + e);
                }
                return date1.compareTo(date2);
            }
        },
        DESC {
            public int compare(String o1, String o2) {
                return -ASC.compare(o1, o2);
            }
        }
    }

    private enum TextComparatorAsSymbols implements Comparator<String> {
        ASC {
            public int compare(String o1, String o2) {
                int n1 = o1.length(), n2 = o2.length();
                for (int i1 = 0, i2 = 0; i1 < n1 && i2 < n2; i1++, i2++) {
                    char c1 = o1.charAt(i1);
                    char c2 = o2.charAt(i2);
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                }
                return n1 - n2;
            }
        },
        DESC {
            public int compare(String o1, String o2) {
                return -ASC.compare(o1, o2);
            }
        }
    }
}
