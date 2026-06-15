package maxpos;

import liquidjava.specification.Refinement;


public class PositiveMax {
    
    public static @Refinement("_ >= 0") int maxPos(int[] a) {
        @Refinement("0 <= _ && _ < a.length") int k = 0;
        @Refinement("_ >= 0") int max = 0;
        while (k < a.length) {
            int v = a[k];
            if (v > 0 && v > max) {
                max = v;
            };
            k += 1;
        };
        return max;
    }

}
