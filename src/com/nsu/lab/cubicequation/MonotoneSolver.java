package com.nsu.lab.cubicequation;

import com.nsu.lab.cubicequation.tasks.MonotoneTask;
import com.nsu.lab.solver.Solution;
import com.nsu.lab.solver.Solver;
import com.nsu.lab.solver.Task;
import com.nsu.lab.util.UnexpectedTaskException;

public class MonotoneSolver implements Solver {
    private static MonotoneSolver solver = null;
    public static MonotoneSolver getSolver() {
        if (solver == null)
            solver = new MonotoneSolver();

        return solver;
    }

    private MonotoneSolver() {}

    @Override
    public Solution solve(Task t) {
        if (!(t instanceof MonotoneTask))
            throw new UnexpectedTaskException(Task.class, t.getClass());

        MonotoneTask task = (MonotoneTask)t;

        float l = task.l;
        float r = task.r;
        float e = task.epsilon;

        assert task.f(l) <= 0 && task.f(r) >= 0;
        float c, fc;
        do {
            c = (l+r)/2;
            fc = task.f(c);
            if (task.isRising) {
                if (fc < -e)
                    l = c;
                if (fc > e)
                    r = c;
            } else {
                if (fc > e)
                    l = c;
                if (fc < -e)
                    r = c;
            }
        } while(Math.abs(fc) >= e);

        CubicSolution solution = new CubicSolution();
        solution.add(c);
        return solution;
    }

    @Override
    public int numberOfPossibleSolutions(Task t) {
        if (!(t instanceof MonotoneTask))
            throw new UnexpectedTaskException(Task.class, t.getClass());
        return 1;
    }
}
