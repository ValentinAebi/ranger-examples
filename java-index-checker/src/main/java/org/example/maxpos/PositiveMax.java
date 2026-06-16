package org.example.maxpos;

import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;

public class PositiveMax {
    
    public static @NonNegative int maxPos_correct(int[] a) {
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

    public static @NonNegative int maxPos_buggy(int[] a) {
        var k = 0;
        var max = 0;
        // ERROR: should be <
        while (k <= a.length) {
            var v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            k += 1;
        };
        return max;
    }

    public static @NonNegative int maxPos_moreComplex_correct(int[] a, @IndexFor("#1") int start, @Positive int incEven, @Positive int incOdd) {
        var k = 0;
        var max = start;
        while (k < a.length) {
            var v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            if (k % 2 == 0) {
                k += incEven;
            } else {
                k += incOdd;
            }
        };
        return max;
    }

    public static @NonNegative int maxPos_moreComplex_buggy(int[] a, @IndexFor("#1") int start, @Positive int incEven, int incOdd) {
        var k = 0;
        var max = start;
        while (k < a.length) {
            var v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            if (k % 2 == 0) {
                k += incEven;
            } else {
                k += incOdd;  // ERROR: incOdd may be negative
            }
        };
        return max;
    }

}
