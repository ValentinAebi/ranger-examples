package org.example.maxpos;

import org.checkerframework.checker.index.qual.NonNegative;

public class PositiveMax {
    
    public static @NonNegative int maxPos(int[] a) {
        var k = 0;
        var max = 0;
        while (k < a.length) {
            var v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            k += 1;
        };
        return max;
    }

}
