package lj3


val x: Int = ???

val y: Int = ???

val z: Int = ???    //> LJ3::ternary-ref

def absDiv(a: Int, b: Int): Int = { //> LJ3::absDiv_correct
  val res = a / b                   //> LJ3::absDiv_buggy
  if res >= 0 then res else -res
}
