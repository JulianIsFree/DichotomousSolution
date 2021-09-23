package com.nsu.lab;

import com.nsu.lab.cubicequation.CubicSolver;
import com.nsu.lab.cubicequation.tasks.CubicTask;

public class Main {
    public static void main(String[] args) {
        // a b c e s
        assert args.length == 5;
        float a = Float.valueOf(args[0]);
        float b = Float.valueOf(args[1]);
        float c = Float.valueOf(args[2]);
        float e = Float.valueOf(args[3]);
        float s = Float.valueOf(args[4]);
        CubicTask cubicTask = new CubicTask(a, b, c, e, s);
        System.out.println(CubicSolver.getSolver().numberOfPossibleSolutions(cubicTask));
        System.out.println(CubicSolver.getSolver().solve(cubicTask));
    }
}
