import scala.util.boundary

def lessThan(arr1: Array[Int], arr2: Array[Int]): Boolean = boundary {
    for (i <- arr1.indices) {
        if (arr1(i) < arr2(i)) {
            boundary.break(true);
        } else if (arr1(i) > arr2(i)) {
            boundary.break(false)
        }
    }
    false
}
