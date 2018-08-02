package com.ripple.database.function;

import com.ripple.database.value.FloatValue;
import com.ripple.database.value.IntValue;
import com.ripple.database.value.Value;

public class AvgFunction implements Function {
    @Override
    public boolean compatibleWith(Class cls) {
        return cls == IntValue.class || cls == FloatValue.class;
    }

    @Override
    public Value map(Value[] values) {
        if (values[0] instanceof IntValue) {
            double result = 0;
            for (Value value : values) {
                result += ((IntValue) value).get();
            }
            return new FloatValue(result / values.length);
        } else if (values[0] instanceof FloatValue) {
            double result = 0;
            for (Value value : values) {
                result += ((FloatValue) value).get();
            }
            return new FloatValue(result / values.length);
        }
        return null;
    }

    @Override
    public String toString() {
        return "AVG";
    }
}
