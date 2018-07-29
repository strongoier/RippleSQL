package com.ripple.mapreduce;

import com.ripple.sqloperator.FormatOperator;
import com.ripple.sqloperator.MapOperator;
import com.ripple.sqloperator.ReduceOperator;

import java.util.ArrayList;
import java.util.List;

public class TaskInfo {
    // must not be empty
    public List<MapOperator> mapOperators = new ArrayList<>();
    // can be null
    public FormatOperator mapFormatConfig = null;

    // must not be null
    public ReduceOperator reduceOperator = null;

    // must not be empty
    public List<String> inputPaths = new ArrayList<>();
    // must not be null
    public String outputPath = null;
}
