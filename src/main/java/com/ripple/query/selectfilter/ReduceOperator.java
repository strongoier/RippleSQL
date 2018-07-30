package com.ripple.query.selectfilter;

import com.ripple.database.Attribute;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;

public abstract class ReduceOperator {
    public abstract String toString();

    public abstract void fromString(String config);

    public abstract List<Attribute> setup(List<Attribute> attrs);

    public abstract void reduce(Text key, Iterable<Text> values, Reducer.Context context) throws IOException, InterruptedException;
}
