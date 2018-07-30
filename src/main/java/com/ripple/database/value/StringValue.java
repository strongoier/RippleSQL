package com.ripple.database.value;

public class StringValue extends Value {
    private String value;

    public StringValue() {
        super();
        value = "";
    }

    public StringValue(String v) {
        super();
        value = v;
    }

    public String get() {
        return value;
    }

    @Override
    public boolean isSameValueType(Value value) {
        return (value instanceof StringValue);
    }

    @Override
    public int compareTo(Value value) {
        StringValue stringValue = (StringValue) value;
        return this.value.compareTo(stringValue.value);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StringValue))
            return false;
        StringValue stringValue = (StringValue) o;
        return value.equals(stringValue.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
