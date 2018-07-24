package com.ripple.mapreduce;

public abstract class Task {
    protected Class mapClass;
    protected Class reduceClass;

    public abstract void setup();

    public Class getMapClass() {
        return mapClass;
    }

    public Class getReduceClass() {
        return reduceClass;
    }
}
