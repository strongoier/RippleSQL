package com.ripple.query;

import com.ripple.relation.Relation;
import com.ripple.value.FloatValue;
import com.ripple.value.IntValue;
import com.ripple.value.StringValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryManager {
    private Map<String, Constructor> constructors;
    private Map<String, Relation> relations;

    public QueryManager(String configuration) throws NoSuchMethodException {
        constructors = new HashMap<>();
        constructors.put("Int", IntValue.class.getDeclaredConstructor(String.class));
        constructors.put("Float", FloatValue.class.getDeclaredConstructor(String.class));
        constructors.put("String", StringValue.class.getDeclaredConstructor(String.class));
        relations = new HashMap<>();
        JSONArray rels = new JSONObject(configuration).getJSONArray("relations");
        for (Object element : rels) {
            JSONObject rel = (JSONObject) element;
            String name = rel.getString("name");
            JSONArray attrs = rel.getJSONArray("attributes");
            List<Constructor> attributes = new ArrayList<>();
            for (Object attr : attrs)
                attributes.add(constructors.get(attr));
            relations.put(name, new Relation(name, attributes));
        }
        System.out.println(relations.size());
        for (Relation relation : relations.values())
            System.out.println(relation);
    }
}
