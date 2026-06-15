package ic4;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class IC4 {
    
    String getThirdElement_1(String @Refinement("_.length >= 3") [] arr) {
        return arr[10 /* unsound */ ];
    }

    String getThirdElement_2(String @Refinement("_.length == 3") [] arr) {
        return arr[-1 /* unsound */ ];
    }

}
