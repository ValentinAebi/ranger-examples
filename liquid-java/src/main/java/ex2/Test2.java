package ex2;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


@RefinementAlias("Nat(int x) {x >= 0}")
public class Test2 {
    
/**
    * The sum of all numbers between 0 and n
    * @param n
    * @return a positive value that represents the sum of all numbers between 0 and n, or 0 if n is negative 
    */
    @Refinement("Nat(_) && _ >= n")
    public static int sum(int n) {
        if(n <= 1)
            return 0;
        else {
            int t1 = sum(n-1);
            return n + t1;
        }
    }
    
}
