package com.ripple.algooperator;

import com.ripple.value.Value;

import java.util.function.BiFunction;

public interface AlgoOperator extends BiFunction<Value, Value, Boolean> {
    AlgoOperator noOp = (a, b) -> true;
    AlgoOperator eqOp = (a, b) -> a.equals(b);
    AlgoOperator neOp = (a, b) -> !a.equals(b);
    AlgoOperator ltOp = (a, b) -> a.compareTo(b) < 0;
    AlgoOperator gtOp = (a, b) -> a.compareTo(b) > 0;
    AlgoOperator leOp = (a, b) -> a.compareTo(b) <= 0;
    AlgoOperator geOp = (a, b) -> a.compareTo(b) >= 0;
}
