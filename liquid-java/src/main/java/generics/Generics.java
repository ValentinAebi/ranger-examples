package generics;

import java.util.List;
import java.util.ArrayList;

import liquidjava.specification.Refinement;


public class Generics {
    
    public static void main(String[] args) {

        // OK
        @Refinement("_ >= 0") Integer i;
        i = -10;

        // not OK
        List<@Refinement("_ >= 0") Integer> ls = new ArrayList<>();
        ls.add(-10);
    }

}
