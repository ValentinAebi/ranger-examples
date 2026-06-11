package org.example.arraymap;

import org.checkerframework.checker.index.qual.IndexOrLow;

public class ArrayUtils {
    
    public static <T> @IndexOrLow("#1") int indexOf(T[] array, T elem) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == elem) {
                return i;
            }
        }
        return -1;
    }

}
