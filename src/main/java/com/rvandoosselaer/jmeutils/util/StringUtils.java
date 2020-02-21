package com.rvandoosselaer.jmeutils.util;

import org.apache.commons.text.WordUtils;

/**
 * Utility class for Strings.
 *
 * @author rvandoosselaer
 */
public class StringUtils {

    /**
     * Converts a string to camelcase.
     * toCamelCase(null) : ""
     * toCamelCase("hello") : "hello"
     * toCamelCase("my name is Remy") : "myNameIsRemy"
     * toCamelCase("I am FINE") : "iAmFine"
     * toCamelCase("     "): ""
     *
     * @param string the input string
     * @return camelcase representation of the given string
     */
    public static String toCamelCase(String string) {
        String s = WordUtils.capitalizeFully(string);
        if (s != null) {
            s = s.replaceAll("\\s", "");
            return s.length() > 1 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s.toLowerCase();
        }
        return "";
    }

    /**
     * Returns the given string, with a maximum of {@code max} characters.
     * Characters after the {@code max} will be stripped.
     *
     * @param string the input string
     * @param max    the maximum allowed length of the output string
     * @return stripped string
     */
    public static String max(String string, int max) {
        return string != null && string.length() > max ? string.substring(0, max) : string;
    }

}
