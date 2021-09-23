package com.nsu.lab.cubicequation.tasks;

import com.nsu.lab.quadraticequaction.QuadraticSolution;
import com.nsu.lab.quadraticequaction.QuadraticSolver;
import com.nsu.lab.quadraticequaction.QuadraticTask;
import com.nsu.lab.solver.Task;

/**
 * Equation of type: x^3 + a*x^2 + b*x + c = 0,
 * where x^3 factor is always 1, which is ensured by constructor
 */
public class CubicTask implements Task {
    final float a;
    final float b;
    final float c;
    public final float epsilon;
    public final float step;

    // a*x^3 + b*x^2 + c*x + d = f(x)
    public CubicTask(float a, float b, float c, float d, float epsilon, float step) {
        assert  a != 0;
        assert  Float.isFinite(a) &&
                Float.isFinite(b) &&
                Float.isFinite(c) &&
                Float.isFinite(d);

        this.epsilon = epsilon;
        this.a = b / a;
        this.b = c / a;
        this.c = d / a;
        this.step = step;
    }

    // x^3 + a*x^2 + b*x + c = f(x)
    public CubicTask(float a, float b, float c, float epsilon, float step) {
        this.epsilon = epsilon;
        assert  Float.isFinite(a) &&
                Float.isFinite(b) &&
                Float.isFinite(c);
        this.a = a;
        this.b = b;
        this.c = c;
        this.step = step;
    }

    @Override
    public float f(float x) {
        return x * x * x + a * x * x + b * x + c;
    }

    public QuadraticTask getDerivative() {
        return new QuadraticTask(3, 2 * a, b);
    }

    public float getx1() {
        QuadraticTask t = getDerivative();
        QuadraticSolution s = QuadraticSolver.getSolver().solve(t);
        assert s.size() == 2;
        assert s.get(0) < s.get(1);
        return s.get(0);
    }

    public float getx2() {
        QuadraticTask t = getDerivative();
        QuadraticSolution s = QuadraticSolver.getSolver().solve(t);
        assert s.size() == 2;
        assert s.get(0) < s.get(1);
        return s.get(1);
    }

    @Override
    public String toString() {
        return String.format("x^3 + %.2f*x^2 + %.2f*x + %.2f", a, b, c);
    }
}
