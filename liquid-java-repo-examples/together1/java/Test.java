
/*@RefinementAlias("Nat(int x) {x >= 0}")*/
/*@RefinementAlias("GreaterEqualThan(int x, int y) {x >= y}")*/
public class Test {

    /*@Refinement( "_ >= 0 && GreaterEqualThan(_, n)")*/
    public static int fibonacci(/*@Refinement("Nat(n)")*/ int n){
        if(n <= 1)
            return 0;
        else
            return fibonacci(n-1) + fibonacci(n-2);
    }
	
}
