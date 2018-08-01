package com.ripple.query.task;

import com.ripple.query.operator.FuncOperator;
import com.ripple.query.operator.SelectMapOperator;

public class GroupByFuncTask extends MapReduceTask {
    // must not be null
    public SelectMapOperator selectValueOperator;
    // can be null
    public SelectMapOperator selectKeyOperator;
    // must not be null
    public FuncOperator funcOperator;
}
