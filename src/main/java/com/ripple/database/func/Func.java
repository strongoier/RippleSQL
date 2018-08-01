package com.ripple.database.func;

import com.ripple.database.value.Value;

public interface Func {
    boolean compatibleWith(Class cls);
    Value map(Value[] values);

    Func sumFunc = new SumFunc();
    Func maxFunc = new MaxFunc();
    Func minFunc = new MinFunc();
    Func avgFunc = new AvgFunc();
    Func countFunc = new CountFunc();
}
