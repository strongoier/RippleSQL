package com.ripple.util;

public class StringUtil {
    public static String removeSpaces(String str) {
        return str.replaceAll("\\s+", "");
    }
}
