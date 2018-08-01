package com.ripple.database.func;

import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;

public class CountFunc implements Func {
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
