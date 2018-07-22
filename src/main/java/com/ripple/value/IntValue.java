package com.ripple.value;

public class IntValue extends Value {
    private Long value;

    public IntValue() {
        super();
        value = 0L;
    }

    public IntValue(Integer v) {
        super();
        value = v.longValue();
    }

    public IntValue(Long v) {
        super();
        value = v;
    }

    public IntValue(String v) {
        super();
        value = new Long(v);
    }

    @Override
    public boolean isSameValueType(Value value) {
        return (value instanceof IntValue);
    }

    @Override
    public int compareTo(Value value) {
        IntValue intValue = (IntValue) value;
        return this.value.compareTo(intValue.value);
    }

    @Override
    public boolean equals(Object o) {
        IntValue intValue = (IntValue) o;
        return value.equals(intValue.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
