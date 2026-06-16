// Adapted from https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/sorts/MergeSort.java
package sorting;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

import static sorting.SortUtils.less;

import java.util.Arrays;

/*
 * We are currently unable to verify this example using LiquidJava. After LiquidJava crashed on the initial version of this example, 
 * we tried to simplify the code (e.g. by replacing generics with ints). We ended up with this version, where LiquidJava does not 
 * crash anymore, but fails to verify. We also tried to replace the refinement aliases with their complete definition at use-site, 
 * but in that case the verifier crashes.
 */


//@RefinementAlias("IndexFor(int idx, Object[] arr) { 0 <= idx && idx < arr.length }")
@SuppressWarnings("rawtypes")
class MergeSort {

    public int[] sort(int[] unsorted) {
        if (unsorted.length <= 0) {
            return unsorted;
        }
        int[] tempArray = Arrays.copyOf(unsorted, unsorted.length);
        doSort(unsorted, tempArray, 0, unsorted.length - 1);
        return unsorted;
    }

    private void doSort(int[] arr, int[] tempArray, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int left, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            doSort(arr, tempArray, left, mid);
            doSort(arr, tempArray, mid + 1, right);
            merge(arr, tempArray, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")
    private void merge(int[] arr, int[] tempArray, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int left, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int mid, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray) && left < _") int right) {
        int i = left;
        int j = mid + 1;
        System.arraycopy(arr, left, tempArray, left, right + 1 - left);

        for (int k = left; k <= right; k++) {
            if (j > right) {
                arr[k] = tempArray[i++];
            } else if (i > mid) {
                arr[k] = tempArray[j--];    // ERROR: should be j-- here
            } else if (less(tempArray[j], tempArray[i])) {
                arr[k] = tempArray[j++];
            } else {
                arr[k] = tempArray[i++];
            }
        }
    }
}
