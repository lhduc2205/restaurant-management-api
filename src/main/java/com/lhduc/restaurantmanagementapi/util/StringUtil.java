package com.lhduc.restaurantmanagementapi.util;

/**
 * Utility class for common String operations.
 */
public class StringUtil {
    private StringUtil() {}

    /**
     * Checks if a given string is null, empty, or consists only of whitespace characters.
     *
     * @param str The string to check.
     * @return {@code true} if the string is null, empty, or blank; otherwise, {@code false}.
     */
    public static boolean isNull(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }

    /**
     * Checks if a given string is not null, not empty, and not consisting only of whitespace characters.
     *
     * @param str The string to check.
     * @return {@code true} if the string is not null, not empty, and not blank; otherwise, {@code false}.
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }
}
