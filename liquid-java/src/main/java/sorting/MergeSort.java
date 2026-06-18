// Adapted from https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/sorts/MergeSort.java
package sorting;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;

import static sorting.SortUtils.less;

import java.util.Arrays;
import java.util.function.BiFunction;

/*
 * We are currently unable to verify this example using LiquidJava. After LiquidJava crashed on the initial version of this example, 
 * we tried to simplify the code (e.g. by replacing generics with ints). We ended up with this version, where LiquidJava does not 
 * crash anymore, but fails to verify. We also tried to replace the refinement aliases with their complete definition at use-site, 
 * but in that case the verifier crashed.
 */


@RefinementAlias("IndexFor(int idx, Object[] arr) { 0 <= idx && idx < arr.length }")
@SuppressWarnings("rawtypes")
class MergeSort {

    public static void sort(int[] unsorted, BiFunction<Integer, Integer, Boolean> lessThan) {     //> MergeSort::sort p=(2,0,0/0) r=none FAIL
        if (unsorted.length <= 0) {
            return;
        }
        int[] tempArray = Arrays.copyOf(unsorted, unsorted.length);
        doSort(unsorted, tempArray, lessThan, 0, unsorted.length - 1);
    }

    private static void doSort(int[] arr, int[] tempArray, BiFunction<Integer, Integer, Boolean> lessThan, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int left, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int right) {  //> MergeSort::doSort p=(5,2,8/8) r=none FAIL
        if (left < right) {
            int mid = (left + right) / 2;
            doSort(arr, tempArray, lessThan, left, mid);
            doSort(arr, tempArray, lessThan, mid + 1, right);
            merge(arr, tempArray, lessThan, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")  //> MergeSort::merge p=(6,3,13/13) r=none BUG
    private static void merge(int[] arr, int[] tempArray, BiFunction<Integer, Integer, Boolean> lessThan, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int left, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray)") int mid, @Refinement("IndexFor(_, arr) && IndexFor(_, tempArray) && left < _") int right) {
        int i = left;
        int j = mid + 1;
        System.arraycopy(arr, left, tempArray, left, right + 1 - left);

        for (int k = left; k <= right; k++) {
            if (j > right) {
                arr[k] = tempArray[i++];
            } else if (i > mid) {
                arr[k] = tempArray[j--];    // ERROR: should be j-- here
            } else if (lessThan.apply(tempArray[j], tempArray[i])) {
                arr[k] = tempArray[j++];
            } else {
                arr[k] = tempArray[i++];
            }
        }
    }
}
