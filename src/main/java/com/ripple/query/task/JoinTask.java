package com.ripple.query.task;

import com.ripple.query.operator.FilterOperator;
import com.ripple.query.operator.JoinMapOperator;
import com.ripple.query.task.MapReduceTask;

public class JoinTask extends MapReduceTask {
    // must not be null
    public JoinMapOperator joinMapOperator;
    // can be null
    public FilterOperator filterOperator;
}
