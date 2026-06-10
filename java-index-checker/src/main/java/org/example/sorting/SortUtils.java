package org.example.sorting;

public class SortUtils {
    
    public static <T extends Comparable<T>> boolean less(T firstElement, T secondElement) {
        return firstElement.compareTo(secondElement) < 0;
    }

}
