import Lists.map
import Lists.filter
import Arrays.toList

fun decodeAll(data: Array<Int>, xSize: Int, ySize: Int): List<Point> {
    val validData = filter(toList(data)) { it >= 0 }
    return map(validData){ decode(it, xSize, ySize) }
}

fun decode(datapoint: Int, xSize: Int, ySize: Int): Point {
    val x = clamp(0, xSize - 1, datapoint / 256)
    val y = datapoint % ySize
    return Point(x, y)
}

fun clamp(min: Int, max: Int, x: Int): Int {
    if (x <= min) {
        return min 
    } else if (x <= max) {
        return x
    } else {
        return max
    }
}

data class Point(val x: Int, val y: Int)

sealed interface List<out T>
data class Cons<T>(val first: T, val rest: List<T>) : List<T>
object Nil : List<Nothing>

object Lists {

    fun <T, U> map(ls: List<T>, p: (T) -> U): List<U> = TODO()

    fun <T> filter(ls: List<T>, p: (T) -> Boolean): List<T> {
        var rev: List<T> = Nil
        var rem = ls
        while (rem is Cons) {
            if (p(rem.first)) {
                rev = Cons(rem.first, rev)
            }
            rem = rem.rest
        }
        return reverse(rev)
    }

    fun <T> reverse(ls: List<T>): List<T> = TODO()

}

object Arrays {

    fun <T> toList(arr: Array<T>): List<T> {
        var ls: List<T> = Nil
        for (e in arr.reversed()) {
            ls = Cons(e, ls)
        }
        return ls
    }

}
