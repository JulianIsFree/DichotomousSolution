package com.nsu.lab.util;

public class UndefinedResultException extends RuntimeException {
    public UndefinedResultException(String what) {
        super("Undefined result in QuadraticSolution: " + what);
    }
}