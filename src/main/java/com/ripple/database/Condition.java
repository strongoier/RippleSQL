package com.ripple.database;

import com.ripple.algooperator.AlgoOperator;
import com.ripple.value.Value;

public class Condition {
    private Attribute leftAttribute;
    private AlgoOperator algoOperator;
    private boolean rightIsAttribute;
    private Attribute rightAttribute;
    private Value rightValue;

    public Condition(Attribute lattr, AlgoOperator op, Attribute rattr) {
        leftAttribute = lattr;
        algoOperator = op;
        rightIsAttribute = true;
        rightAttribute = rattr;
        rightValue = null;
    }

    public Condition(Attribute lattr, AlgoOperator op, Value rv) {
        leftAttribute = lattr;
        algoOperator = op;
        rightIsAttribute = false;
        rightAttribute = null;
        rightValue = rv;
    }

    public Attribute getLeftAttribute() {
        return leftAttribute;
    }

    public AlgoOperator getAlgoOperator() {
        return algoOperator;
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
