package com.ripple.query.task;

import com.ripple.query.operator.SetKeyOperator;

public class OrderByTask extends MapReduceTask {
    // must not be null
    public SetKeyOperator setKeyOperator;
    // must not be null
    public Class orderByType;
}
