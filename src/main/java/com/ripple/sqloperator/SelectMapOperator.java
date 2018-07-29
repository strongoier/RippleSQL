package com.ripple.sqloperator;

import com.ripple.database.Attribute;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectMapOperator extends MapOperator {
    private List<Integer> indexs = new ArrayList<>();

    @Override
    public String toString() {
        return String.join("\t", indexs.stream().map((i)->i.toString()).collect(Collectors.toList()));
    }

    @Override
    public void fromString(String config) {
        indexs = Arrays.stream(config.split("\t")).map(Integer::parseInt).collect(Collectors.toList());
    }

    public List<Attribute> setup(List<Attribute> needs, List<Attribute> attrs) {
        indexs = new ArrayList<>();
        for (Attribute need : needs)
            for (int i = 0; i < attrs.size(); ++i)
                if (need.equals(attrs.get(i)))
                    indexs.add(i);
        return needs;
    }

    @Override
    public List<Pair<Object, Text>> map(List<Pair<Object, Text>> input) {
        List<Pair<Object, Text>> results = new ArrayList<>();
        for (Pair<Object, Text> pair : input) {
            String[] line = pair.getValue().toString().split("\t");
            List<String> value = new ArrayList<>();
            for (int i : indexs)
                value.add(line[i]);
            results.add(new Pair<>(pair.getKey(), new Text(String.join("\t", value))));
        }
        return results;
    }
}
