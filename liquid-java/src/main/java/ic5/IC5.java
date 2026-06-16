package ic5;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class IC5 {
    
    boolean lessThan_correct(double[] arr1, double @Refinement("arr1.length == arr2.length") [] arr2) {
        for (@Refinement("0 <= i && i < arr1.length") int i = 0; i < arr1.length; i++) {
            if (arr1[i] < arr2[i]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

    boolean lessThan_buggy(double[] arr1, double @Refinement("arr1.length == arr2.length") [] arr2) {
        for (@Refinement("0 <= i && i < arr1.length") int i = 0; i < arr1.length; i--) {
            if (arr1[i] < arr2[i]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

}
