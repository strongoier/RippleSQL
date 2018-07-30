package com.ripple.query.operator;

import com.ripple.database.Attribute;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.List;

/*
    RelationName\tAttributeName\tValueClassName\tindex
 */

public class SetKeyOperator {
    private int index = -1;

    @Override
    public String toString() {
        return String.format("%d", index);
    }

    public void fromString(String config) throws ClassNotFoundException {
        index = Integer.parseInt(config);
    }

    public List<Attribute> setup(Attribute keyAttribute, List<Attribute> attrs) {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(null);
        for (int i = 0; i < attrs.size(); ++i) {
            Attribute attr = attrs.get(i);
            if (attr.equals(keyAttribute)) {
                index = i;
                attributes.set(0, attr);
            } else {
                attributes.add(attr);
            }
        }
        return attributes;
    }

    public boolean hasSetup() {
        return index >= 0;
    }

    public Pair<Text, Text> format(String[] values) {
        if (hasSetup()) {
            Text key = new Text(values[index]);
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < values.length; ++i)
                if (i != index)
                    valueBuilder.append(values[i]).append('\t');
            Text value = new Text(valueBuilder.toString());
            return new Pair<>(key, value);
        } else {
            Text key = new Text(values[0]);
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 1; i < values.length; ++i)
                valueBuilder.append(values[i]).append('\t');
            Text value = new Text(valueBuilder.toString());
            return new Pair<>(key, value);
        }
    }
}
