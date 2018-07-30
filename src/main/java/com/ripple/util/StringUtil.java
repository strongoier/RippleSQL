package com.ripple.util;

import java.util.Date;

public class StringUtil {
    public static String removeSpaces(String str) {
        return str.replaceAll("\\s+", "");
    }

    public static String getDate() {
        return new Date().toString().replaceAll("[ :]", "-");
    }
}
