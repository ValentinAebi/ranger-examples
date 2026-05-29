
def sum(n: Int): Int = {
    if (n <= 1) {
        n
    } else {
        val t1 = sum(n-1)
        n + t1
    }
}
