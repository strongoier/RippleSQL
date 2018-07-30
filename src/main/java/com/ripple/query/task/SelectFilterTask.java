package com.ripple.query.task;

import com.ripple.query.operator.FilterOperator;
import com.ripple.query.operator.SelectMapOperator;

public class SelectFilterTask extends MapReduceTask {
    // can be null
    public SelectMapOperator selectOperator;
    // can be null
    public FilterOperator filterOperator;
}
