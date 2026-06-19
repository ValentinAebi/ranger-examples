package lj1


def fib_original(n: Int): Int = {   //> Fibonacci::fib_original
  if (n <= 1) {
    0
  } else {
    fib_original(n - 1) + fib_original(n - 2)
  }
}

def fib_base_case_fixed(n: Int): Int = {  //> Fibonacci::fib_base_case_fixed
  if (n <= 1) {
    n
  } else {
    fib_original(n - 1) + fib_original(n - 2)
  }
}
