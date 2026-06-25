package maxpos;

import liquidjava.specification.Refinement;

public class PositiveMax {
    
    // @Refinement("_ >= 0") causes crash
    public static int maxPos_correct(int[] a) {   //> PositiveMax::maxPos_correct p=(1,0,0/0) r=none
        int k = 0;
        int max = 0;
        while (k < a.length) {
            int v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            k += 1;
        };
        return max;
    }

    // @Refinement("_ >= 0") causes crash
    public static int maxPos_buggy(int[] a) {   //> PositiveMax::maxPos_buggy p=(1,0,0/0) r=none BUG
        int k = 0;
        int max = 0;
        while (k < a.length) {
            int v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            k += 1;
        };
        return max;
    }

    // @Refinement("_ >= 0") causes crash
    public static int maxPos_moreComplex_correct(
        int[] a,
        @Refinement("0 <= _ && _ < length(a)") int start,
        @Refinement("_ > 0") int incEven,
        @Refinement("_ > 0") int incOdd
    ) {    //> PositiveMax::maxPos_moreComplex_correct p=(4,3,3/3) r=none
        int k = 0;
        int max = start;
        while (k < a.length) {
            int v = a[k];
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

    // @Refinement("_ >= 0") causes crash
    public static int maxPos_moreComplex_buggy(
        int[] a,
        @Refinement("0 <= _ && _ < length(a)") int start,
        @Refinement("_ > 0") int incEven,
        int incOdd
    ) {    //> PositiveMax::maxPos_moreComplex_buggy p=(4,2,2/2) r=none BUG
        int k = 0;
        int max = start;
        while (k < a.length) {
            int v = a[k];
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
}
