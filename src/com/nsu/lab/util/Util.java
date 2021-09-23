package com.nsu.lab.util;

public final class Util {
    public static <RT> RT notImplemented() {
        return notImplemented("");
    }

    public static <RT, AT> RT notImplemented(AT obj) {
        StackTraceElement[] methods = Thread.currentThread().getStackTrace();
        String fullName = methods[2].getClassName() + "::" + methods[2].getMethodName();
        throw new RuntimeException("Not implemented: " + fullName + " " + obj.toString());
    }


    public static <RT, AT> RT shouldNotReachHere() {
        return shouldNotReachHere("");
    }

    public static <RT, AT> RT shouldNotReachHere(AT obj) {
        StackTraceElement[] methods = Thread.currentThread().getStackTrace();
        String fullName = methods[2].getClassName() + "::" + methods[2].getMethodName();
        throw new RuntimeException("Should not reach here: " + fullName + " "+ obj.toString());
    }

    public static class Pair<A,B> {
        public A _1;
        public B _2;
        public Pair(A _1, B _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Pair))
                return false;

            Pair p = (Pair)obj;
            if (this == p)
                return true;

            return _1.equals(p._1) && _2.equals(p._2);
        }

        @Override
        public int hashCode() {
            return 31 * _1.hashCode() + _2.hashCode();
        }
    }
}
