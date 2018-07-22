package com.ripple.value;

public class FloatValue extends Value {
    private Double value;

    public FloatValue() {
        super();
        value = 0.0;
    }

    public FloatValue(Float v) {
        super();
        value = v.doubleValue();
    }

    public FloatValue(Double v) {
        super();
        value = v;
    }

    public FloatValue(String v) {
        super();
        value = new Double(v);
    }

    @Override
    public boolean isSameValueType(Value value) {
        return (value instanceof FloatValue);
    }

    @Override
    public int compareTo(Value value) {
        FloatValue floatValue = (FloatValue) value;
        return this.value.compareTo(floatValue.value);
    }

    @Override
    public boolean equals(Object o) {
        FloatValue floatValue = (FloatValue) o;
        return value.equals(floatValue.value);
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
