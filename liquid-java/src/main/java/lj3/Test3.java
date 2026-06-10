package lj3;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

public class Test3 {
    
    @Refinement("x > 0") // x must be greater than 0
    int x;

    @Refinement("0 <= _ && _ <= 100") // y must be between 0 and 100
    int y;

    @Refinement(value="z % 2 == 0 ? z >= 0 : z < 0", msg="z must be positive if even, negative if odd")
    int z;

    @Refinement("_ >= 0")
    int absDiv(int a, @Refinement(value="b != 0", msg="cannot divide by zero") int b) {
        int res = a / b;
        return res >= 0 ? res : -res;
    }

}
