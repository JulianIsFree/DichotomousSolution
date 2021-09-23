package com.nsu.lab.solver;

public interface Solver {
    Solution solve(Task t);
    int numberOfPossibleSolutions(Task t);
}
