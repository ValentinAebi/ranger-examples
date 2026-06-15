
def maxPos(a: Array[Int]): Int = {
    var k = 0
    var max = 0
    while (k < a.length) {
        val v = a(k)
        if (v > 0 && v > max) {
            max = v
        }
        k += 1
    }
    max
}
