package com.nsu.lab.cubicequation;

import com.nsu.lab.solver.Solution;
import com.nsu.lab.util.UndefinedResultException;

public class CubicSolution extends Solution {
    @Override
    protected void checkAndThrowExceptionIfAny(Float element) {
        if (element.isInfinite())
            throw new UndefinedResultException("infinity number");

        if (element.isNaN())
            throw new UndefinedResultException("NaN");
    }
}
