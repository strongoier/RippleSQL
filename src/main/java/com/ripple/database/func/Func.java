package com.ripple.database.func;

public interface Func {
    Func sumFunc = new SumFunc();
    Func maxFunc = new MaxFunc();
    Func minFunc = new MinFunc();
    Func avgFunc = new AvgFunc();
    Func countFunc = new CountFunc();
}
