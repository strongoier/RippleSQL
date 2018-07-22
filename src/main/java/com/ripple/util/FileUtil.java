package com.ripple.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static String readAll(Path path) throws IOException {
        InputStream stream = Files.newInputStream(path);
        int len = stream.available();
        byte[] bytes = new byte[len];
        stream.read(bytes);
        return new String(bytes, "UTF-8");
    }
}
