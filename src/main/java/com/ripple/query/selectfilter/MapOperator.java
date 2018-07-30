package com.ripple.query.selectfilter;

import com.ripple.util.Pair;
import org.apache.hadoop.io.Text;

import java.util.List;

public abstract class MapOperator {
    public abstract String toString();

    public abstract void fromString(String config);

    public abstract List<Pair<Object, Text>> map(List<Pair<Object, Text>> input);
}
