package org.example.lj2;

import org.checkerframework.checker.index.qual.NonNegative;


public class Test2 {
    
    // cannot express @GreaterOrEq("n")
    public static @NonNegative int sum(int n) {
        if(n <= 1)
            return 0;
        else {
            int t1 = sum(n-1);
            return n + t1;
        }
    }
    
}
