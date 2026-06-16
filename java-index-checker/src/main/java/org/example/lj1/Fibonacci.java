package org.example.lj1;

import org.checkerframework.checker.index.qual.NonNegative;

// Problem here: the Index Checker cannot express @GreaterOrEq("n")
public class Fibonacci {

    public static @NonNegative int fib_original(@NonNegative int n) {
        if (n <= 1)
            return 0;
        else
            return fib_original(n - 1) + fib_original(n - 2);
    }

    public static @NonNegative int fib_base_case_fixed(@NonNegative int n) {
        if (n <= 1)
            return n;
        else
            return fib_original(n - 1) + fib_original(n - 2);
    }

}
