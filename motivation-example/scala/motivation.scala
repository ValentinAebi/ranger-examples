import Lists.*

def decodeAll(data: Array[Int], xSize: Int, ySize: Int) = {
    val validData = filter(data.toList, _ >= 0)
    map(validData, decode(_, xSize, ySize))
}

def decode(datapoint: Int, xSize: Int, ySize: Int) = {
    val x = clamp(0, xSize - 1, datapoint / 256)
    val y = datapoint % ySize
    Point(x, y)
}

def clamp(min: Int, max: Int, x: Int) =
   if x <= min then min
   else if x <= max then x
   else max

case class Point(x: Int, y: Int)

object Lists {

    def map[T, U](ls: List[T], f: T => U): List[U] = ???

    def filter[T](ls: List[T], p: T => Boolean) = {
        var rev = List.empty[T /*annotation!*/]
        var rem = ls
        while (rem.nonEmpty) {
            if (p(rem.head)) {
                rev = rem.head :: rev
            }
            rem = rem.tail
        }
        reverse(rev)
    }

    def reverse[T](ls: List[T]): List[T] = ???

}

