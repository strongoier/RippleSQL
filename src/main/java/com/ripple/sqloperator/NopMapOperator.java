package com.ripple.sqloperator;

import com.ripple.database.Attribute;
import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.List;

/*
    Nop
 */

public class NopMapOperator extends MapOperator {
    @Override
    public String toString() {
        return "Nop";
    }

    @Override
    public void fromString(String config) {}

    public List<Attribute> setup(List<Attribute> attrs) {
        return attrs;
    }

    @Override
    public List<Pair<Object, Text>> map(List<Pair<Object, Text>> input) {
        return input;
    }
}
