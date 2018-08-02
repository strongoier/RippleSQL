package com.ripple.database.function;

import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;

public class CountFunction implements Function {
    @Override
    public boolean compatibleWith(Class cls) {
        return true;
    }

    @Override
    public Value map(Value[] values) {
        return new IntValue(values.length);
    }

    @Override
    public String toString() {
        return "COUNT";
    }
}
