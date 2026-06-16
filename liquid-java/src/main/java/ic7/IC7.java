package ic7;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class IC7 {
    
    public static String removeSubstring_correct(String original, String removed) {
        int i = original.indexOf(removed);
        if (i != -1) {
            // crashes LiquidJava
            //return original.substring(0, i) + original.substring(i + removed.length());
        }
        return original;
    }

}
