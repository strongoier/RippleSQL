package com.ripple.database.function;

import com.ripple.database.value.Value;

public class MinFunction implements Function {
    @Override
    public boolean compatibleWith(Class cls) {
        return true;
    }

    @Override
    public Value map(Value[] values) {
        Value result = values[0];
        for (int i = 1; i < values.length; ++i) {
            if (values[i].compareTo(result) < 0) {
                result = values[i];
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "MIN";
    }
}
