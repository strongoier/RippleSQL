package com.ripple.config;

import com.ripple.database.Attribute;
import com.ripple.database.Database;
import com.ripple.database.Relation;
import com.ripple.util.FileUtil;
import com.ripple.util.StringUtil;
import com.ripple.value.FloatValue;
import com.ripple.value.IntValue;
import com.ripple.value.StringValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class JsonConfigReader extends ConfigReader {
    private Map<String, Class> classMap;
    private Path configJsonFile;

    public JsonConfigReader(String conf) {
        classMap = new HashMap<>();
        classMap.put("Int", IntValue.class);
        classMap.put("Float", FloatValue.class);
        classMap.put("String", StringValue.class);
        configJsonFile = Paths.get(conf);
    }

    public JsonConfigReader(Path path) {
        classMap = new HashMap<>();
        classMap.put("Int", IntValue.class);
        classMap.put("Float", FloatValue.class);
        classMap.put("String", StringValue.class);
        configJsonFile = path;
    }

    @Override
    public Map<String, Database> getDatabases() throws IOException {
        String configuration = StringUtil.removeSpaces(FileUtil.readAll(configJsonFile));
        Map<String, Database> databaseMap = new HashMap<>();
        JSONArray databaseArray = new JSONObject(configuration).getJSONArray("databases");
        for (Object tmp1 : databaseArray) {
            JSONObject databaseObject = (JSONObject) tmp1;
            String databaseName = databaseObject.getString("name");
            JSONArray relationArray = databaseObject.getJSONArray("relations");
            Map<String, Relation> relationMap = new HashMap<>();
            for (Object tmp2 : relationArray) {
                JSONObject relationObject = (JSONObject) tmp2;
                String relationName = relationObject.getString("name");
                JSONArray attributeArray = relationObject.getJSONArray("attributes");
                Map<String, Attribute> attributeMap = new HashMap<>();
                for (int i = 0; i < attributeArray.length(); ++i) {
                    JSONObject attributeObject = attributeArray.getJSONObject(i);
                    String attributeName = attributeObject.getString("name");
                    Class type = classMap.get(attributeObject.getString("type"));
                    Attribute attribute = new Attribute(relationName, attributeName);
                    attribute.setIndex(i);
                    attribute.setType(type);
                    attributeMap.put(attributeName, attribute);
                }
                relationMap.put(relationName, new Relation(relationName, attributeMap));
            }
            databaseMap.put(databaseName, new Database(databaseName, relationMap));
        }
        return databaseMap;
    }
}
