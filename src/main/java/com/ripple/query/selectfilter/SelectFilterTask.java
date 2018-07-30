package com.ripple.query.selectfilter;

import com.ripple.query.MapReduceTask;

public class SelectFilterTask extends MapReduceTask {
    // can be null
    public SelectMapOperator selectOperator;
    // can be null
    public FilterMapOperator filterOperator;
}
