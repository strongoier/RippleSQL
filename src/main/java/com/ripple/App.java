package com.ripple;

import com.ripple.query.QueryManager;
import com.ripple.util.FileUtil;
import com.ripple.util.StringUtil;
import com.ripple.value.IntValue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) throws Exception {
        Path conf = Paths.get("file/relations.json");
        QueryManager queryManager = new QueryManager(StringUtil.removeSpaces(FileUtil.readAll(conf)));
    }
}
