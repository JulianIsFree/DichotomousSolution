package com.nsu.lab.util;

public class UnexpectedTaskException extends RuntimeException {
    public UnexpectedTaskException(Class expected, Class real)  {
        super("Expected " + expected.toString() + ", but found " + real.toString());
    }
}