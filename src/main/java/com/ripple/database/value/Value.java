package com.ripple.database.value;

public abstract class Value implements Comparable<Value> {
    public abstract boolean isSameValueType(Value value);

    @Override
    public abstract int compareTo(Value value);

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract String toString();

    @Override
    public abstract int hashCode();
}
