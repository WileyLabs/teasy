package com.wiley.autotest.cuanto.util;

import java.util.*;

/**
 * Parser for string arguments that represent various data structures such as a map or a list.
 *
 * @author Suk-Hyun Cho
 */
public class ArgumentParser {

    private static final char KEY_VALUE_SEPARATOR = ':';
    private static final char ARG_SEPARATOR = ',';

    /**
     * Parse a string in the form of E1,E2,...,En
     * to create a List in the form of [E1, E2, ..., En].
     *
     * @param listArgument from which to create a List
     * @return List parsed from the provided listArgument; empty if listArgument is null or of length zero
     */
    public static List<String> parseList(String listArgument) {
        // use a linked list to preserve the order of elements
        List<String> list = new LinkedList<String>();

        if (listArgument == null || listArgument.length() == 0) {
            return list;
        }

        List<String> parsedElements = Arrays.asList(listArgument.split(String.valueOf(ARG_SEPARATOR)));

        for (String element : parsedElements) {
            if (element != null && !element.equals("")) {
                list.add(element);
            }
        }

        return list;
    }

    /**
     * Parse a string in the form of K1:V1,K2:V2,...Kn:Vn
     * to create a Map in the form of [K1:V1, K2:V2, ... Kn:Vn].
     *
     * @param mapArgument from which to create a Map
     * @return Map parsed from the provided mapArgument; empty if mapArgument is null or of length zero
     */
    public static Map<String, String> parseMap(String mapArgument) {
        // use linked map to preserve the order of key-value pairs
        Map<String, String> map = new LinkedHashMap<String, String>();

        if (mapArgument == null || mapArgument.length() == 0) {
            return map;
        }

        int lastIndex = -1;
        int currentIndex = 0;
        // whether the current token a value (as opposed to a key)
        boolean isTokenValue = false;
        // keep track of the last key found, so that once a value is parsed, the key-value pair can be put in the map
        String lastKey = null;

        while (currentIndex < mapArgument.length()) {
            // a token is any substring from the last index to the current index if the current index has a separator
            char c = mapArgument.charAt(currentIndex);
            if (c == KEY_VALUE_SEPARATOR || c == ARG_SEPARATOR) {
                String token = mapArgument.substring(lastIndex + 1, currentIndex);
                // if the current token is a key or the current index has an argument separator, then process the token
                if (!isTokenValue || c == ARG_SEPARATOR) {
                    lastIndex = currentIndex;
                    lastKey = processMapToken(map, lastKey, token);
                    // this is needed in case an argument separator is found before a key-value separator
                    // (e.g., key1,key2:val2), in which case a null value is used for the key
                    if (lastKey != null && c == ARG_SEPARATOR) {
                        lastKey = processMapToken(map, lastKey, null);
                    }
                }

                isTokenValue = lastKey != null;
            }
            currentIndex++;
        }

        // most of the times, there will be no separator at the end of the argument
        // so, parse the last bit and process the token.
        if (currentIndex != lastIndex) {
            String lastStringSegment = mapArgument.substring(lastIndex + 1);
            lastKey = processMapToken(map, lastKey, lastStringSegment);
            // this is needed in case an argument separator is found before a key-value separator
            // (e.g., key1,key2:val2), in which case a null value is used for the key
            if (lastKey != null) {
                processMapToken(map, lastKey, null);
            }
        }
        return map;
    }

    /**
     * Populate the map or not.
     * <p/>
     * If the last key is null, then the token is a key; so return it.
     * If the last key is not null, then the token is a value; so put the key-value pair in the map and return null,
     * so that lastKey may be reset to null for the next key to be parsed.
     *
     * @param map     to put a key-value pair, if applicable
     * @param lastKey the last key parsed
     * @param token   the current token to process
     * @return the last key (see above)
     */
    private static String processMapToken(Map<String, String> map, String lastKey, String token) {
        if (lastKey == null) {
            return token;
        } else {
            map.put(lastKey, token);
        }
        return null;
    }
}
