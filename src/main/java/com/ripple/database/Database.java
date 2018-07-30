package com.ripple.database;

import com.ripple.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String name;
    private Map<String, Relation> relationMap;

    public Database() {
        name = "default";
        relationMap = new HashMap<>();
    }

    public Database(String n) {
        name = n;
        relationMap = new HashMap<>();
    }

    public Database(String n, Map<String, Relation> map) {
        name = n;
        relationMap = map;
    }

    public String getName() {
        return name;
    }

    public List<String> getRelationNames() {
        return new ArrayList<>(relationMap.keySet());
    }

    public Relation getRelation(String relName) {
        if (!relationMap.containsKey(relName))
            throw new RuntimeException(String.format("No Such Relation(%s) in Database(%s)!!!", relName, name));
        return relationMap.get(relName);
    }

    public Pair<Relation, Attribute> getAttribute(String relName, String attrName) {
        Relation relation = getRelation(relName);
        Attribute attribute = relation.getAttribute(attrName);
        return new Pair<>(relation, attribute);
    }

    public List<Pair<Relation, Attribute>> getAttribute(String attrName) {
        List<Pair<Relation, Attribute>> attributes = new ArrayList<>();
        for (Relation relation : relationMap.values()) {
            Map<String, Attribute> attributeMap = relation.getAttributeMap();
            if (attributeMap.containsKey(attrName))
                attributes.add(new Pair<>(relation, attributeMap.get(attrName)));
        }
        if (attributes.size() == 0)
            throw new RuntimeException(String.format("No Such Attribute(%s)", attrName));
        return attributes;
    }
}
