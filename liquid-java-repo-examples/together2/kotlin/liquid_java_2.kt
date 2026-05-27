
fun sum(n: Int): Int {
    if (n <= 1) {
        return n
    } else {
        val t1 = sum(n-1)
        return n + t1
    }
}

