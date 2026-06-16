package org.example.ic5;

import org.checkerframework.checker.index.qual.SameLen;

public class IC5 {

    boolean lessThan_correct(double[] arr1, double @SameLen("#1") [] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] < arr2[i]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

    boolean lessThan_buggy(double[] arr1, double @SameLen("#1") [] arr2) {
        for (int i = 0; i < arr1.length; i--) {     // ERROR: should be i++
            if (arr1[i] < arr2[i]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

}
