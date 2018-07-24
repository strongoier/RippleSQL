package com.ripple.database;

public class Attribute {
    private String relationName;
    private String attributeName;
    private int index;
    private Class type;

    public Attribute(String attrName) {
        relationName = null;
        attributeName = attrName;
        index = -1;
        type = null;
    }

    public Attribute(String relName, String attrName) {
        relationName = relName;
        attributeName = attrName;
        index = -1;
        type = null;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        if (this.relationName != null)
            throw new RuntimeException(String.format("Reset Attribute(%s:%s)'s Relation!!!", this.relationName, this.attributeName));
        this.relationName = relationName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(relationName).append(' ').append(attributeName).append(' ').append(index).append(' ').append(type);
        return builder.toString();
    }
}