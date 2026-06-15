package lj4

def getYear(): Int = 2024

@main def main(): Unit = {
  val a = 1998
  val c = new Car
  c.year = a
  
  val j = c.year
  val k = getYear()
  
}
