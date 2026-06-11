// Original code taken from https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/sorts/MergeSort.java
// Most of the Checker Framework annotations, as well as some modifications to make the code compiant to it, were implemented by Arnaud Fauconnet as part of an assignment for the Software analysis course (USI Università della Svizerra Italiana, 2024).
// Additional modifications were added by the repository's author, including the removal of the private static field to make the code closer to the Licorne implementation of it (which also makes it thread-safe).

package org.example.sorting;

import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.IndexFor;

import static org.example.sorting.SortUtils.less;

import java.util.Arrays;


public class MergeSort {
    
    public <T extends Comparable<T>> T[] sort(T[] unsorted) {
        if (unsorted.length == 0) // adding this logic to avoid annoying warnings
            return unsorted;
        T[] aux = Arrays.copyOf(unsorted, unsorted.length);
        assert aux.length == unsorted.length : "@AssumeAssertion(index): semantics of copyOf";
        doSort(unsorted, aux, 0, unsorted.length - 1);
        return unsorted;
    }

    private static <T extends Comparable<T>> void doSort(T[] arr, T[] aux, @IndexFor({"#1", "#2"}) int left, @IndexFor({"#1", "#2"}) int right) {
        if (left < right) {
            int mid = (left + right) >>> 1;
            doSort(arr, aux, left, mid);
            if (mid + 1 < right) // trivial logic that would get handled by the next recursive call, but checker was complaining :)
                doSort(arr, aux, mid + 1, right);
            merge(arr, aux, left, mid, right);
        }
    }

    private static <T extends Comparable<T>> void merge(T[] arr, T[] aux, @IndexFor({"#1", "#2"}) int left, @IndexFor({"#1", "#2"}) int mid, @IndexFor({"#1", "#2"}) int right) {
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
