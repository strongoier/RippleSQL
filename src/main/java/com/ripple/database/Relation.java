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
        List<Attribute> attributes = new ArrayList<>(attributeMap.values());
        Collections.sort(attributes, Comparator.comparing(Attribute::getIndex));
        for (Attribute attribute : attributes) {
            builder.append(attribute).append('\n');
        }
        return builder.toString();
    }

    public Attribute getAttribute(String attrName) {
        if (!attributeMap.containsKey(attrName))
            throw new RuntimeException(String.format("No Such Attribute(%s) in Relation(%s)!!!", attrName, name));
        return attributeMap.get(attrName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Relation))
            return false;
        Relation rel = (Relation) o;
        if (name.equals(rel.name))
            return true;
        return false;
    }
}
