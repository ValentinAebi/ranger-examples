
public class Test {

    boolean lessThan(int[] arr1, int /*@SameLen("#1")*/ [] arr2) {
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] < arr2[i]) {
                return true;
            } else if (arr1[i] > arr2[i]) {
                return false;
            }
        }
        return false;
    }

}
