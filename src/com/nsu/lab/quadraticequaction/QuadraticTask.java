package com.nsu.lab.quadraticequaction;

import com.nsu.lab.solver.Task;

public class QuadraticTask implements Task {
    final float a;
    final float b;
    final float c;

    public QuadraticTask(float a, float b, float c) {
        assert a != 0;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public float f(float x) {
        return a * x * x + b * x + c;
    }

    public float discriminant() {
        return b * b - 4 * a * c;
    }

}
