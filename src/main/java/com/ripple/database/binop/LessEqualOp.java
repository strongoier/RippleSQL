package com.ripple.database.binop;

import com.ripple.database.value.Value;

public class LessEqualOp implements BinOp {
    @Override
    public boolean apply(Value a, Value b) {
        return a.compareTo(b) <= 0;
    }
}
