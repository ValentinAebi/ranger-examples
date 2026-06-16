package ic4;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class IC4 {
    
    String getThirdElement_1_correct(String @Refinement("_.length >= 3") [] arr) {
        return arr[2];
    }

    String getThirdElement_1_buggy(String @Refinement("_.length >= 3") [] arr) {
        return arr[3];
    }

    String getThirdElement_2_correct(String @Refinement("_.length == 3") [] arr) {
        return arr[2];
    }

    String getThirdElement_2_buggy(String @Refinement("_.length == 3") [] arr) {
        return arr[3];
    }

}
