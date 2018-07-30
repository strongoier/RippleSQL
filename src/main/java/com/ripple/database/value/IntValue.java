package com.ripple.database.value;

public class IntValue extends Value {
    private Long value;

    public IntValue() {
        super();
        value = 0L;
    }

    public IntValue(int v) {
        super();
        value = (long) v;
    }

    public IntValue(long v) {
        super();
        value = v;
    }

    public IntValue(String v) {
        super();
        value = new Long(v);
    }

    public long get() {
        return value;
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
        if (!(o instanceof IntValue))
            return false;
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
