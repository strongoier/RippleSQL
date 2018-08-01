package com.ripple.query.operator;

import com.ripple.database.Attribute;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.List;

public class SetKeyOperator {
    private int index = -1;

    @Override
    public String toString() {
        return String.format("%d", index);
    }

    public void fromString(String config) {
        index = Integer.parseInt(config);
    }

    public void setup(Attribute keyAttribute, List<Attribute> attrs) {
        for (int i = 0; i < attrs.size(); ++i) {
            if (attrs.get(i).equals(keyAttribute)) {
                index = i;
                break;
            }
        }
    }

    public boolean hasSetup() {
        return index >= 0;
    }

    public Pair<Text, Text> format(String[] values) {
        if (hasSetup()) {
            Text key = new Text(values[index]);
            StringBuilder valueBuilder = new StringBuilder();
            for (int i = 0; i < values.length; ++i)
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
