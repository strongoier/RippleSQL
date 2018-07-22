package com.ripple.operator;

import com.ripple.value.Value;

import java.util.function.BiFunction;

public interface Operator extends BiFunction<Value, Value, Boolean> {
    Operator noOp = (a, b) -> true;
    Operator eqOp = (a, b) -> a.equals(b);
    Operator neOp = (a, b) -> !a.equals(b);
    Operator ltOp = (a, b) -> a.compareTo(b) < 0;
    Operator gtOp = (a, b) -> a.compareTo(b) > 0;
    Operator leOp = (a, b) -> a.compareTo(b) <= 0;
    Operator geOp = (a, b) -> a.compareTo(b) >= 0;
}
