package com.ripple.database;

import com.ripple.util.Pair;

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

    public Map<String, Relation> checkRelations(final List<String> relationNames) {
        Map<String, Relation> relations = new HashMap<>();
        for (String relationName : relationNames)
            relations.put(relationName, getRelation(relationName));
        return relations;
    }

    public Map<Relation, Map<String, Attribute>> checkAttributes(List<Attribute> attrs, final Map<String, Relation> relations) {
        Map<Relation, Map<String, Attribute>> attributes = new HashMap<>();
        if (attrs.size() == 1 && attrs.get(0).getAttributeName().equals("*")) {
            for (Relation relation : relations.values())
                attributes.put(relation, relation.getAttributeMap());
            return attributes;
        }
        for (Attribute attr : attrs) {
            String relName = attr.getRelationName();
            String attrName = attr.getAttributeName();
            Pair<Relation, Attribute> relAttr = (relName != null)
                    ? getAttribute(relName, attrName, relations)
                    : getAttribute(attrName, relations);
            Relation relation = relAttr.getKey();
            Attribute attribute = relAttr.getValue();
            if (!attributes.containsKey(relation))
                attributes.put(relation, new HashMap<>());
            attributes.get(relation).put(attribute.getAttributeName(), attribute);
        }
        return attributes;
    }

    private Relation getRelation(String relName) {
        if (!relationMap.containsKey(relName))
            throw new RuntimeException(String.format("No Such Relation(%s) in Database(%s)!!!", relName, name));
        return relationMap.get(relName);
    }

    private Pair<Relation, Attribute> getAttribute(String relName, String attrName, final Map<String, Relation> relations) {
        Relation relation = getRelation(relName);
        if (!relations.containsKey(relName))
            throw new RuntimeException(String.format("No Such Relation(%s:%s) in SQL From Statement!!!", relName, attrName));
        Attribute attribute = relation.getAttribute(attrName);
        return new Pair<>(relation, attribute);
    }

    private Pair<Relation, Attribute> getAttribute(String attrName, final Map<String, Relation> relations) {
        Relation relation = null;
        Attribute attribute = null;
        for (Relation curRel : relations.values()) {
            if (!curRel.getAttributeMap().containsKey(attrName))
                continue;
            if (attribute == null) {
                relation = curRel;
                attribute = curRel.getAttributeMap().get(attrName);
                continue;
            }
            throw new RuntimeException(String.format("More Than One Attribute(%s)", attrName));
        }
        if (attribute == null)
            throw new RuntimeException(String.format("No Such Attribute(%s)", attrName));
        return new Pair<>(relation, attribute);
    }
}
