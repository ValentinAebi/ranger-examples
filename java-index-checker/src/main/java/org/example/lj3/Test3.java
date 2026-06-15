package org.example.lj3;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.common.value.qual.IntRange;


public class Test3 {
    
    @NonNegative int x;

    @IntRange(from = 0, to = 100) int y;

    // not expressible using the Checker Framework: @Refinement(value="z % 2 == 0 ? z >= 0 : z < 0", msg="z must be positive if even, negative if odd")
    int z;

    @NonNegative int absDiv(int a, /* not expressible using the Checker Framework: @Refinement(value="b != 0", msg="cannot divide by zero")*/ int b) {
        int res = a / b;
        // return res >= 0 ? res : -res;   // does not work with the Checker Framework
        return Math.abs(res);
    }

}
