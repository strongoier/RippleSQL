package com.ripple.query;

import com.ripple.database.Attribute;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.List;

public class NopReduceOperator extends ReduceOperator {
    @Override
    public String toString() {
        return "Nop";
    }

    @Override
    public void fromString(String config) {}

    @Override
    public List<Attribute> setup(List<Attribute> attrs) {
        return attrs;
    }

    @Override
    public void reduce(Text key, Iterable<Text> values, Reducer.Context context) throws IOException, InterruptedException {
        for (Text value : values)
            context.write(key, value);
    }
}
