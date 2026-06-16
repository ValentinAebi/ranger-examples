package lj2;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

@RefinementAlias("Nat(int x) {x >= 0}")
public class Sum {

    @Refinement("Nat(_) && _ >= n")
    public static int sum_original(int n) {
        if (n <= 1)
            return 0;
        else {
            int t1 = sum_original(n - 1);
            return n + t1;
        }
    }

    @Refinement("Nat(_) && _ >= n")
    public static int sum_fixed(int n) {
        if (n < 0) {
            return 0;
        } else if (n <= 1)
            return n;
        else {
            int t1 = sum_fixed(n - 1);
            return n + t1;
        }
    }

}
