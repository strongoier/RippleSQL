package com.ripple;

import com.ripple.config.JsonConfigReader;
import com.ripple.query.QueryManager;

public class App {
    public static void main(String[] args) throws Exception {
        QueryManager manager = new QueryManager();
        manager.setConfigReader(new JsonConfigReader("file/relations.json"));
        manager.initialize();
        manager.activeDatabase("default");
    }
}
