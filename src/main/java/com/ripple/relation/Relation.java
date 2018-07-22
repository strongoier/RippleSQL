package com.ripple.relation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Relation {
    private String name;
    private List<Constructor> attributes;

    public Relation() {
        name = null;
        attributes = new ArrayList<>();
    }

    public Relation(String n) {
        name = n;
        attributes = new ArrayList<>();
    }

    public Relation(String n, List<Constructor> a) {
        name = n;
        attributes = a;
    }

    public String getName() {
        return name;
    }

    public List<Constructor> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(name).append("; attributes:");
        for (Constructor a : attributes)
            builder.append(' ').append(a);
        return builder.toString();
    }
}
