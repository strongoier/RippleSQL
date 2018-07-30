package com.ripple.database;

import com.ripple.database.binop.BinOp;
import com.ripple.database.value.Value;

public class Condition {
    private Attribute leftAttribute;
    private BinOp binOp;
    private boolean rightIsAttribute;
    private Attribute rightAttribute;
    private Value rightValue;

    public Condition(Attribute lattr, BinOp op, Attribute rattr) {
        leftAttribute = lattr;
        binOp = op;
        rightIsAttribute = true;
        rightAttribute = rattr;
        rightValue = null;
    }

    public Condition(Attribute lattr, BinOp op, Value rv) {
        leftAttribute = lattr;
        binOp = op;
        rightIsAttribute = false;
        rightAttribute = null;
        rightValue = rv;
    }

    public Attribute getLeftAttribute() {
        return leftAttribute;
    }

    public BinOp getBinOp() {
        return binOp;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Condition))
            return false;
        Condition cond = (Condition) o;
        if (!leftAttribute.equals(cond.leftAttribute)
                || binOp != cond.binOp
                || rightIsAttribute != cond.rightIsAttribute)
            return false;
        if (rightIsAttribute)
            return rightAttribute.equals(cond.rightAttribute);
        return rightValue.equals(cond.rightValue);
    }
}
