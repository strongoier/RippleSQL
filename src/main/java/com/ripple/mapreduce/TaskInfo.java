package com.ripple.mapreduce;

import com.ripple.sqloperator.FormatOperator;
import com.ripple.sqloperator.MapOperator;
import com.ripple.sqloperator.ReduceOperator;

import java.util.List;

public class TaskInfo {
    // must not be empty
    public List<MapOperator> mapOperators;
    // can be null
    public FormatOperator mapFormatConfig;

    // must not be null
    public ReduceOperator reduceOperator;

    // must not be empty
    public List<String> inputPaths;
    // must not be null
    public String outputPath;
}
