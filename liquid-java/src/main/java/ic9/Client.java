package ic9;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class Client {

    // crashes LiquidJava
    /* public static void clearIndex1(ArrayWrapper<? extends Object> a, @Refinement("0 <= _ && _ < a.size()") int i) {
        a.set(i , null);
    } */

    public static void clearIndex2(ArrayWrapper<? extends Object> a, int i) {
        if (0 <= i && i < a.size()) {
            a.set(i, null);
        }
    }
    
}
