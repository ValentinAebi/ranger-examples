package datetime

type Hours = Int
type Minutes = Int
type Seconds = Int


final case class Time(hours: Hours, minutes: Minutes, seconds: Seconds) {

  def deltaTime(h: Int, min: Int, sec: Int): Time = {
    val sSum = this.seconds + sec
    val seconds = (sSum % 60 + 60) % 60
    val mSum = this.minutes + min + (sSum - seconds) / 60
    val minutes = (mSum % 60 + 60) % 60
    val hSum = this.hours + h + (mSum - minutes) / 60
    val hours = (hSum % 24 + 24) % 24
    Time(hours, minutes, seconds)
  }

  def deltaHours(h: Int): Time = deltaTime(h, 0, 0)

  def deltaMinutes(m: Int): Time = deltaTime(0, m, 0)

  def deltaSeconds(s: Int): Time = deltaTime(0, 0, s)

}


class MutTime(
               private var hours: Hours,
               private var minutes: Minutes,
               private var seconds: Seconds
             ) {

  def moveBy(h: Int, m: Int, s: Int): Unit = {
    val sSum = this.seconds + s
    this.seconds = (sSum % 60 + 60) % 60
    val mSum = this.minutes + m + (sSum / 60)
    this.minutes = (mSum % 60 + 60) % 60
    val hSum = this.hours + h + (mSum / 60)
    // ERROR: should be normalized to 24, not 60
    this.hours = (hSum % 60 + 60) % 60;
  }

  def moveByHours(h: Int): Unit = {
    moveBy(h, 0, 0)
  }

  def moveByMinutes(m: Int): Unit = {
    moveBy(0, m, 0)
  }

  def moveBySeconds(s: Int): Unit = {
    moveBy(0, 0, s)
  }

}
