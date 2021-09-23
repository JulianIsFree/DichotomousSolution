package com.nsu.lab.solver;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Solution extends ArrayList<Float>{
    protected abstract void checkAndThrowExceptionIfAny(Float element);

    @Override
    public final boolean add(Float element) {
        checkAndThrowExceptionIfAny(element);
        return super.add(element);
    }

    @Override
    public final void add(int index, Float element) {
        checkAndThrowExceptionIfAny(element);
        super.add(index, element);
    }
}
