// Original code taken from https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/sorts/MergeSort.java
// The Checker Framework annotations, as well as some modifications to make the code compiant to it, were implemented by Arnaud Fauconnet as part of an assignment for the Software analysis course (USI Università della Svizerra Italiana, 2024).

package org.example.sorting;

import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexFor;

import static org.example.sorting.SortUtils.less;


public class MergeSort {

    @SuppressWarnings("rawtypes") private static Comparable[] aux;

    
    public <T extends Comparable<T>> T[] sort(T[] unsorted) {
        if (unsorted.length == 0) // adding this logic to avoid annoying warnings
            return unsorted;
        aux = new Comparable[unsorted.length];
        doSort(unsorted, 0, unsorted.length - 1);
        return unsorted;
    }

    private static <T extends Comparable<T>> void doSort(T[] arr, @IndexFor({"#1", "aux"}) int left, @IndexFor({"#1", "aux"}) int right) {
        if (left < right) {
            int mid = (left + right) >>> 1;
            doSort(arr, left, mid);
            if (mid + 1 < right) // trivial logic that would get handled by the next recursive call, but checker was complaining :)
                doSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }
    
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(T[] arr, @IndexFor({"#1", "aux"}) int left, @IndexFor({"#1", "aux"}) int mid, @IndexFor({"#1", "aux"}) int right) {
        @IndexOrHigh("aux") int i = left, j = mid + 1;

        assert left <= right: "@AssumeAssertion(index): left should be less than or equal right";
        System.arraycopy(arr, left, aux, left, right + 1 - left);

        for (@IndexOrHigh("arr") int k = left; k <= right; k++) {
            if (j > right) {
                assert i < aux.length: "@AssumeAssertion(index): i is always less than the length of aux";
                arr[k] = (T) aux[i++];
            } else if (i > mid) {
                arr[k] = (T) aux[j++];
            } else if (less(aux[j], aux[i])) {
                arr[k] = (T) aux[j++];
            } else {
                arr[k] = (T) aux[i++];
            }
        }
    }
}
