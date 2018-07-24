package com.ripple.sqloperator;

import com.ripple.database.Attribute;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.List;

/*
    RelationName\tAttributeName\tValueClassName\tindex
 */

public class FormatOperator {
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

    public List<Pair<Text, Text>> format(List<Pair<Object, Text>> input) {
        List<Pair<Text, Text>> results = new ArrayList<>();
        if (hasSetup()) {
            for (Pair<Object, Text> pair : input) {
                String[] line = pair.getValue().toString().split("\t");
                Text key = new Text(line[index]);
                StringBuilder valueBuilder = new StringBuilder();
                for (int i = 0; i < line.length; ++i)
                    if (i != index)
                        valueBuilder.append(line[i]).append('\t');
                Text value = new Text(valueBuilder.toString());
                results.add(new Pair<>(key, value));
            }
        } else {
            for (Pair<Object, Text> pair : input) {
                String line = pair.getValue().toString();
                int i = line.indexOf('\t');
                results.add(new Pair<>(new Text(line.substring(0, i)), new Text(line.substring(i + 1))));
            }
        }
        return results;
    }
}
