package lj4

def getYear(): Int = 2024

def test_correct(): Unit = {    //> Test::test_correct
  val a = 1998
  val c = new Car
  c.year = a
  
  val j = c.year
  val k = getYear()
}

def test_buggy(): Unit = {    //> Test::test_buggy
  val a = 998
  val c = new Car
  c.year = a

  val j = c.year
  val k = getYear()
}
