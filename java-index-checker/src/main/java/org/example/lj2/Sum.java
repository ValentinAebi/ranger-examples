package org.example.lj2;

import org.checkerframework.checker.index.qual.NonNegative;

// Problem here: the Index Checker cannot express @GreaterOrEq("n")
public class Sum {

    public static @NonNegative int sum_original(int n) {
        if (n <= 1)
            return 0;
        else {
            int t1 = sum_original(n - 1);
            return n + t1;
        }
    }

    public static @NonNegative int sum_fixed(int n) {
        if (n <= 1)
            return 0;
        else {
            int t1 = sum_fixed(n - 1);
            return n + t1;
        }
    }

}
