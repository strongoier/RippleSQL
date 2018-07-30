package com.ripple.query.selectfilter;

import com.ripple.database.Attribute;

import java.util.ArrayList;
import java.util.List;

public class SelectFilterTask {
    // can be empty
    public List<MapOperator> mapOperators = new ArrayList<>();
    // can be null
    public FormatMapOperator mapFormatConfig = null;

    // must not be null
    public ReduceOperator reduceOperator = null;

    // must not be empty
    public List<String> inputPaths = new ArrayList<>();
    // must not be null
    public String outputPath = null;

    // attributes after task
    public List<Attribute> attributes;
}
