package lj4

import scala.compiletime.uninitialized


class Car {
  private var _year: Int = uninitialized
  private var _seats: Int = uninitialized

  def year_=(y: Int): Unit = {
    require(1800 < y && y < 2050)
    this._year = y
  }

  def year: Int = _year

}
