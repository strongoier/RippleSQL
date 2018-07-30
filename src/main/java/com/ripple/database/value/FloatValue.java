package com.ripple.database.value;

public class FloatValue extends Value {
    private Double value;

    public FloatValue() {
        super();
        value = 0.0;
    }

    public FloatValue(float v) {
        super();
        value = (double) v;
    }

    public FloatValue(double v) {
        super();
        value = v;
    }

    public FloatValue(String v) {
        super();
        value = new Double(v);
    }

    public double get() {
        return value;
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
        if (!(o instanceof FloatValue))
            return false;
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
