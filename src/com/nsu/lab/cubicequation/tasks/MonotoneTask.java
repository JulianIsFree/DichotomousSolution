package com.nsu.lab.cubicequation.tasks;

import com.nsu.lab.util.Util.Pair;

public class MonotoneTask extends CubicTask {
    public final float l, r;
    public final boolean isRising;
    private MonotoneTask(float a, float b, float c, float epsilon, float l, float r, float step, boolean isRising) {
        super(a, b, c, epsilon, step);
        this.l = l;
        this.r = r;
        this.isRising = isRising;
    }

    @Override
    public float f(float x) {
        assert l <= x  && x <= r;
        return super.f(x);
    }

    public static MonotoneTask createFromCubicTaskFinite(CubicTask task, float l, float r) {
        return new MonotoneTask(task.a, task.b, task.c, task.epsilon, l, r, task.step, false);
    }

    public static MonotoneTask createFromCubicTaskInfinityPlus(CubicTask task, float l, float step) {
        Pair<Float, Float> borders = findRightBorder(task, l, step);
        return new MonotoneTask(task.a, task.b, task.c, task.epsilon, borders._1, borders._2, task.step, true);
    }

    public static MonotoneTask createFromCubicTaskInfinityMinus(CubicTask task, float r, float step) {
        Pair<Float, Float> borders = findLeftBorder(task, r, step);
        return new MonotoneTask(task.a, task.b, task.c, task.epsilon, borders._1, borders._2, task.step, true);
    }

    private static Pair<Float, Float> findLeftBorder(CubicTask task, float r, float step) {
        while (task.f(r) > 0) {
            r -= step;
        }
        assert Float.isFinite(r);
        assert task.f(r) <= 0 && task.f(r + step) > 0;
        return new Pair<>(r, r + step);
    }

    private static Pair<Float, Float> findRightBorder(CubicTask task, float l, float step) {
        while (task.f(l) < 0) {
            l += step;
        }
        assert Float.isFinite(l);
        assert task.f(l) <= 0 && task.f(l + step) > 0;
        return new Pair<>(l - step, l);
    }
}
