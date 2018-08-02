package com.ripple.database.function;

import com.ripple.database.value.Value;

public interface Function {
    boolean compatibleWith(Class cls);
    Value map(Value[] values);

    Function sumFunc = new SumFunction();
    Function maxFunc = new MaxFunction();
    Function minFunc = new MinFunction();
    Function avgFunc = new AvgFunction();
    Function countFunc = new CountFunction();
}
