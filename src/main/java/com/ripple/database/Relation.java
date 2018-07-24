package com.ripple.database;

import java.util.*;

public class Relation {
    private String name;
    private Map<String, Attribute> attributeMap;

    public Relation() {
        name = null;
        attributeMap = new HashMap<>();
    }

    public Relation(String n) {
        name = n;
        attributeMap = new HashMap<>();
    }

    public Relation(String n, Map<String, Attribute> a) {
        name = n;
        attributeMap = a;
    }

    public String getName() {
        return name;
    }

    public Map<String, Attribute> getAttributeMap() {
        return attributeMap;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name:\n\t").append(name).append("\nattributeMap:\n");
        for (Attribute attribute : attributeMap.values())
            builder.append('\t').append(attribute).append('\n');
        return builder.toString();
    }

    public Attribute getAttribute(String attrName) {
        if (!attributeMap.containsKey(attrName))
            throw new RuntimeException(String.format("No Such Attribute(%s) in Relation(%s)!!!", attrName, name));
        return attributeMap.get(attrName);
    }
}
