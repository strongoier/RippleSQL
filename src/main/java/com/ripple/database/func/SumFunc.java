package com.ripple.database.func;

import com.ripple.database.value.FloatValue;
import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;

public class SumFunc implements Func {
    @Override
    public boolean compatibleWith(Class cls) {
        return cls == IntValue.class || cls == FloatValue.class;
    }

    @Override
    public Value map(Value[] values) {
        if (values[0] instanceof IntValue) {
            long result = 0;
            for (Value value : values) {
                result += ((IntValue) value).get();
            }
            return new IntValue(result);
        } else if (values[0] instanceof FloatValue) {
            double result = 0;
            for (Value value : values) {
                result += ((FloatValue) value).get();
            }
            return new FloatValue(result);
        }
        return null;
    }

    @Override
    public String toString() {
        return "SUM";
    }
}
