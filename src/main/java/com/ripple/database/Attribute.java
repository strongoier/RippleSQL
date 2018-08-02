package com.ripple.database;

import com.ripple.database.function.Function;

public class Attribute {
    private String relationName;
    private String attributeName;
    private int index;
    private Class type;
    private Function function;

    public Attribute(String attrName) {
        relationName = null;
        attributeName = attrName;
        index = -1;
        type = null;
        function = null;
    }

    public Attribute(String relName, String attrName) {
        relationName = relName;
        attributeName = attrName;
        index = -1;
        type = null;
        function = null;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        if (this.relationName != null) {
            if (!this.relationName.equals(relationName))
                throw new RuntimeException(String.format("Reset Attribute(%s:%s)'s Relation!!!", this.relationName, this.attributeName));
        }
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

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String typeString = type.toString();
        builder.append(index).append(": ").append(attributeName).append(' ').append(typeString.substring(typeString.lastIndexOf('.') + 1));
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Attribute))
            return false;
        Attribute attr = (Attribute) o;
        if (relationName.equals(attr.relationName) && attributeName.equals(attr.attributeName) && function == attr.function)
            return true;
        return false;
    }

    public boolean resembles(Attribute a) {
        return relationName.equals(a.relationName) && attributeName.equals(a.attributeName);
    }
}
