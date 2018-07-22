package com.ripple.relation;

public class Attribute {
    private String relationName;
    private String attributeName;

    public Attribute(String attrName) {
        relationName = null;
        attributeName = attrName;
    }

    public Attribute(String relName, String attrName) {
        relationName = relName;
        attributeName = attrName;
    }

    public String getRelationName() {
        return relationName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
