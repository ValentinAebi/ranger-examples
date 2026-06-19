
def maxPos_correct(a: Array[Int]): Int = {      //> PositiveMax::maxPos_correct
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

def maxPos_buggy(a: Array[Int]): Int = {        //> PositiveMax::maxPos_buggy
    var k = 0
    var max = 0
    // ERROR: should be <
    while (k <= a.length) {
        val v = a(k)
        if (v > 0 && v > max) {
            max = v
        }
        k += 1
    }
    max
}

def maxPos_moreComplex(a: Array[Int], start: Int, incEven: Int, incOdd: Int): Int = {   //> PositiveMax::maxPos_moreComplex_correct
    var k = start                                                                       //> PositiveMax::maxPos_moreComplex_buggy
    var max = 0
    while (k < a.length) {
        val v = a(k)
        if (v > 0 && v > max) {
            max = v
        }
        if (k % 2 == 0) {
            k += incEven
        } else {
            k += incOdd
        }
    }
    max
}
