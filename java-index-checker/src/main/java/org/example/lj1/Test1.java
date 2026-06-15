package org.example.lj1;

import org.checkerframework.checker.index.qual.NonNegative;


public class Test1 {

    // cannot express @GreaterOrEq("n")
    public static @NonNegative int fibonacci(@NonNegative int n){
        if(n <= 1)
            return 0;
        else
            return fibonacci(n-1) + fibonacci(n-2);
    }
	
}
