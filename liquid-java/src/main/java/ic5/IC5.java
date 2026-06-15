package ic5;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class IC5 {
    
    boolean lessThan(double[] arr1, double @Refinement("arr1.length == arr2.length") [] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] < arr2[i + 2 /* unsound */ ]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

}
