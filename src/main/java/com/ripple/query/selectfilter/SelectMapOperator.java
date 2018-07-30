package com.ripple.query.selectfilter;

import com.ripple.database.Attribute;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SelectMapOperator {
    private Integer[] indexs;

    @Override
    public String toString() {
        return String.join("\t", Arrays.stream(indexs).map(i->i.toString()).collect(Collectors.toList()));
    }

    public void fromString(String config) {
        indexs = Arrays.stream(config.split("\t")).map(Integer::parseInt).toArray(Integer[]::new);
    }

    public void setup(List<Attribute> selectAttributes, List<Attribute> attrs) {
        indexs = new Integer[selectAttributes.size()];
        for (int i = 0; i < selectAttributes.size(); ++i)
            for (int index = 0; index < attrs.size(); ++index)
                if (selectAttributes.get(i).equals(attrs.get(index)))
                    indexs[i] = index;
    }

    public String[] map(String[] input) {
        String[] result = new String[indexs.length];
        for (int i = 0; i < indexs.length; ++i)
            result[i] = input[indexs[i]];
        return result;
    }
}
