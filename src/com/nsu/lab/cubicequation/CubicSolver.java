package com.nsu.lab.cubicequation;

import com.nsu.lab.cubicequation.tasks.CubicTask;
import com.nsu.lab.cubicequation.tasks.MonotoneTask;
import com.nsu.lab.quadraticequaction.QuadraticSolution;
import com.nsu.lab.quadraticequaction.QuadraticSolver;
import com.nsu.lab.quadraticequaction.QuadraticTask;
import com.nsu.lab.solver.Solver;
import com.nsu.lab.solver.Task;
import com.nsu.lab.util.UnexpectedTaskException;
import com.nsu.lab.solver.Solution;
import com.nsu.lab.util.Util;

import java.util.Collections;

public class CubicSolver implements Solver {
    /**
     * """Definition section and trivial case"""
     *
     * def f(x) = this.f(x)
     * def f'(x) = df / dx
     * IF D(f'(x)) <= 0 then there is only one root
     * if (f(0) < 0) then root in [0, +inf)
     *  else then root in (-inf, 0]
     * ELSE
     * def x1, x2 | f'(x1) = f'(x2) = 0
     * assumes f(x1) > f(x2)
     * You may say (x1, x2) are left and right roots of f'(x) = 0, which is quadratic equation
     * def e := epsilon
     * You may say this is an accuracy
     *
     * """Calculating performing"""
     *
     * If f(x1) < -e
     *  then is only one root in [x2, +inf) NN
     * Else If |f(x1)| <= e then
     *  there are 2 roots x1 and one in [x2, +inf) EN
     * Else If f(x1) > e then
     * If f(x2) < -e then
     *  there are 3 roots for one in every range: (-inf, x1], [x1, x2], [x2, +inf)
     * else If |f(x2)| <= e then
     *  there are 2 roots: x2 and one in (-inf, x1]
     * else If f(x2) > e
     *  there is 1 root: (-inf, x1]
     */
    enum SolutionCase {
        ONE,
        PP, // f(x1) is positive, f(x2) is positive too
        PN, // in x1 is positive, in x2 is negative
        PE, // in x1 is positive, in x2 is less than epsilon by modulo
        NN, // clear
        EN; // in x1 is less than epsilon by modulo, in x2 is negative
        static SolutionCase getCase(CubicTask task) {
            QuadraticTask derivativeTask = task.getDerivative();
            float disc = derivativeTask.discriminant();

            if (disc <= 0)
                return ONE;

            QuadraticTask derivative = task.getDerivative();
            QuadraticSolution derivativeSolution = QuadraticSolver.getSolver().solve(derivative);
            assert derivativeSolution.size() == 2;

            float x1 = derivativeSolution.remove(0);
            float x2 = derivativeSolution.remove(0);
            if (task.f(x1) < task.f(x2)) {
                float tmp = x1;
                x1 = x2;
                x2 = tmp;
            }

            float fx1 = task.f(x1);
            float fx2 = task.f(x2);
            float e = task.epsilon;
            if (fx1 < -e) {
                return NN;
            } else if (Math.abs(fx1) <= e) {
                return EN;
            } else if (fx1 > e) {
                if (fx2 < -e) {
                    return PN;
                } else if (Math.abs(fx2) <= e) {
                    return PE;
                } else if (fx2 > e) {
                    return PP;
                } else {
                    Util.shouldNotReachHere("Unmatched f(x2): " + x2 + ":" + fx2 + "=" + task.toString());
                }
            } else {
                Util.shouldNotReachHere("Unmatched f(x1): " + x1 + ":" + fx1 + "=" + task.toString());
            }

            return Util.shouldNotReachHere("Every case must be matched already:\n" +
                    String.format("x1=%.2f: f(x1)=%.2f=%s\n", x1, fx1, task.toString()) +
                    String.format("x2=%.2f: f(x2)=%.2f=%s\n", x2, fx2, task.toString()));
        }

        static int getNumberOfSolutions(SolutionCase solutionCase) {
            switch (solutionCase) {
                case ONE:
                case PP:
                case NN:
                    return 1;
                case PE:
                case EN:
                    return 2;
                case PN:
                    return 3;
                default:
                    return Util.shouldNotReachHere("Unsupported case: " + solutionCase);
            }
        }
    }

    private static CubicSolver solver = null;
    public static CubicSolver getSolver() {
        if (solver == null)
            solver = new CubicSolver();

        return solver;
    }

    @Override
    public Solution solve(Task t) {
        if (!(t instanceof CubicTask))
            throw new UnexpectedTaskException(CubicTask.class, t.getClass());

        CubicTask task = (CubicTask)t;
        SolutionCase solutionCase = SolutionCase.getCase(task);

        return generateSolution(task, solutionCase);
    }

    private Solution generateSolution(CubicTask task, SolutionCase solutionCase) {
        final float x1;
        final float x2;

        MonotoneTask localTask, localTask1, localTask2;
        CubicSolution solution = new CubicSolution();
        switch (solutionCase) {
            case ONE:
                if (task.f(0) <= 0)
                    localTask = MonotoneTask.createFromCubicTaskInfinityPlus(task, 0, task.step);
                else
                    localTask = MonotoneTask.createFromCubicTaskInfinityMinus(task, 0, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                break;
            case PP:
                x1 = task.getx1();
                localTask = MonotoneTask.createFromCubicTaskInfinityMinus(task, x1, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                break;
            case PN:
                x1 = task.getx1();
                x2 = task.getx2();
                localTask = MonotoneTask.createFromCubicTaskInfinityMinus(task, x1, task.step);
                localTask1 = MonotoneTask.createFromCubicTaskFinite(task, x1, x2);
                localTask2 = MonotoneTask.createFromCubicTaskInfinityPlus(task, x2, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                solution.addAll(MonotoneSolver.getSolver().solve(localTask1));
                solution.addAll(MonotoneSolver.getSolver().solve(localTask2));
                break;
            case PE:
                x1 = task.getx1();
                x2 = task.getx2();
                localTask = MonotoneTask.createFromCubicTaskInfinityMinus(task, x1, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                solution.add(x2);
                break;
            case NN:
                x2 = task.getx2();
                localTask = MonotoneTask.createFromCubicTaskInfinityPlus(task, x2, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                break;
            case EN:
                x1 = task.getx1();
                x2 = task.getx2();
                localTask = MonotoneTask.createFromCubicTaskInfinityPlus(task, x2, task.step);
                solution.addAll(MonotoneSolver.getSolver().solve(localTask));
                solution.add(x1);
                break;
            default:
                Util.shouldNotReachHere("CubicSolution::solve default case");
        }

        Collections.sort(solution);
        return solution;
    }

    private int numberOfSolutions(CubicTask task) {
        return SolutionCase.getNumberOfSolutions(SolutionCase.getCase(task));
    }

    @Override
    public int numberOfPossibleSolutions(Task t) {
        if (!(t instanceof CubicTask))
            throw new UnexpectedTaskException(CubicTask.class, t.getClass());
        return numberOfSolutions((CubicTask) t);
    }

    public static void main(String[] args) {
        float epsilon = 0.00000000001f;
        float a = 0f;
        float b = -0.0000000001f;
        float c = 0f;
        CubicTask task = new CubicTask(a, b, c, epsilon, 1);
        int numberOfSolutions = CubicSolver.getSolver().numberOfPossibleSolutions(task);
        System.out.println(numberOfSolutions);
    }
}
