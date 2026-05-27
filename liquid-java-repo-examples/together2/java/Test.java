
/*@RefinementAlias("Nat(int x) {x >= 0}")*/
public class Test {
	
    // @Refinement("Nat(_) && _ >= n")
    public static int sum(int n) {
        if(n <= 1)
            return 0;
        else {
            int t1 = sum(n-1);
            return n + t1;
        }
    }

}
