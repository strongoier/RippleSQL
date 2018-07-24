package com.ripple.database;

import com.ripple.operator.Operator;
import com.ripple.value.Value;

public class Condition {
    private Attribute leftAttribute;
    private Operator operator;
    private boolean rightIsAttribute;
    private Attribute rightAttribute;
    private Value rightValue;

    public Condition(Attribute lattr, Operator op, Attribute rattr) {
        leftAttribute = lattr;
        operator = op;
        rightIsAttribute = true;
        rightAttribute = rattr;
        rightValue = null;
    }

    public Condition(Attribute lattr, Operator op, Value rv) {
        leftAttribute = lattr;
        operator = op;
        rightIsAttribute = false;
        rightAttribute = null;
        rightValue = rv;
    }

    public Attribute getLeftAttribute() {
        return leftAttribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public boolean isRightIsAttribute() {
        return rightIsAttribute;
    }

    public Attribute getRightAttribute() {
        return rightAttribute;
    }

    public Value getRightValue() {
        return rightValue;
    }
}
