package lj4

def getYear(): Int = 2024

def test_correct(): Unit = {
  val a = 1998
  val c = new Car
  c.year = a
  
  val j = c.year
  val k = getYear()
}

def test_buggy(): Unit = {
  val a = 998
  val c = new Car
  c.year = a

  val j = c.year
  val k = getYear()
}
