package com.ripple.database.binop;

import com.ripple.database.value.Value;

public interface BinOp {
    boolean apply(Value a, Value b);

    BinOp eqOp = new EqualOp();
    BinOp neOp = new NotEqualOp();
    BinOp ltOp = new LessOp();
    BinOp gtOp = new GreaterOp();
    BinOp leOp = new LessEqualOp();
    BinOp geOp = new GreaterEqualOp();
}
