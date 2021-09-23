package com.nsu.lab.quadraticequaction;

import com.nsu.lab.solver.Solver;
import com.nsu.lab.solver.Task;
import com.nsu.lab.util.UnexpectedTaskException;
import com.nsu.lab.util.Util;

import java.util.Arrays;
import java.util.Collections;

public class QuadraticSolver implements Solver {
    private static QuadraticSolver solver = null;
    public static QuadraticSolver getSolver() {
        if (solver == null)
            solver = new QuadraticSolver();

        return solver;
    }

    private QuadraticSolver() {}

    @Override
    public QuadraticSolution solve(Task t) {
        if (!(t instanceof QuadraticTask))
            throw new UnexpectedTaskException(QuadraticTask.class, t.getClass());

        QuadraticTask task = (QuadraticTask)t;
        QuadraticSolution solution = new QuadraticSolution();

        int numberOfSolutions = numberOfSolutions(task);

        float a2 = 2 * task.a;
        float base = -task.b / a2;

        switch (numberOfSolutions) {
            case 2:
                float d = (float) Math.sqrt(task.discriminant());
                solution.add(base - d / a2);
                solution.add(base + d / a2);
                break;
            case 1:
                solution.add(base);
                break;
            case 0:
                break;
            default:
                return Util.shouldNotReachHere("default case");
        }

        Collections.sort(solution);
        return solution;
    }

    @Override
    public int numberOfPossibleSolutions(Task t) {
        if (!(t instanceof QuadraticTask))
            throw new UnexpectedTaskException(QuadraticTask.class, t.getClass());

        return numberOfSolutions((QuadraticTask) t);
    }

    private int numberOfSolutions(QuadraticTask t) {
        float disc = t.discriminant();

        if (disc < 0)
            return 0;

        if (disc > 0)
            return 2;

        return 1;
    }
}
