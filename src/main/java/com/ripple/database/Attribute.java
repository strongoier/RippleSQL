package com.ripple.database;

import com.ripple.database.func.Func;

public class Attribute {
    private String relationName;
    private String attributeName;
    private int index;
    private Class type;
    private Func func;

    public Attribute(String attrName) {
        relationName = null;
        attributeName = attrName;
        index = -1;
        type = null;
        func = null;
    }

    public Attribute(String relName, String attrName) {
        relationName = relName;
        attributeName = attrName;
        index = -1;
        type = null;
        func = null;
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

    public Func getFunc() {
        return func;
    }

    public void setFunc(Func func) {
        this.func = func;
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
        if (relationName.equals(attr.relationName) && attributeName.equals(attr.attributeName) && func == attr.func)
            return true;
        return false;
    }

    public boolean resembles(Attribute a) {
        return relationName.equals(a.relationName) && attributeName.equals(a.attributeName);
    }
}
