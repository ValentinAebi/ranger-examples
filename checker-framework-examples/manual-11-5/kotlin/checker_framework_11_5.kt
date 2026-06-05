
fun lessThan(arr1: Array<Double>, arr2: Array<Double>): Boolean {
    for (i in arr1.indices) {
        if (arr1[i] < arr2[i]) {
            return true;
        } else if (arr1[i] > arr2[i]) {
            return false;
        }
    }
    return false
}
