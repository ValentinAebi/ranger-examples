package org.example.ic9;

import org.checkerframework.checker.index.qual.IndexFor;


public class Client {

    public static void clearIndex1(ArrayWrapper<? extends Object> a, @IndexFor("#1") int i) {
        a.set(i, null);
    }

    public static void clearIndex2(ArrayWrapper<? extends Object> a, int i) {
        if (0 <= i && i < a.size()) {
            a.set(i, null);
        }
    }
    
}
