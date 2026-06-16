package arraymap

import scala.reflect.ClassTag
import scala.util.boundary

class ArrayMap[K: ClassTag, V: ClassTag] private(
                                                  private val keys: Array[Option[K]],
                                                  private val values: Array[Option[V]],
                                                  private var _currSize: Int
                                                ) {

  def this(capacity: Int) = this(new Array(capacity), new Array(capacity), 0)

  export keys.length as capacity

  def currSize: Int = _currSize

  def apply(k: K): Option[V] = {
    val idx = keys.indexOf(Some(k))
    Option.when(idx != -1) {
      values(idx)
    }.flatten
  }

  def update(k: K, v: V): Boolean = {
    val preSize = currSize
    val canAdd = preSize < capacity
    if (canAdd) {
      boundary {
        // ERROR: should be until instead of to (or better: for i <- keys.indices)
        for (i <- 0 to keys.length) {
          if (keys(i) == null) {
            keys(i) = Some(k)
            values(i) = Some(v)
          }
        }
      }
      _currSize = preSize + 1
    }
    canAdd
  }

  def remove(k: K): Boolean = {
    val preSize = currSize
    if (preSize > 0) {
      val idx = keys.indexOf(Some(k))
      if idx == -1 then false
      else {
        keys(idx) = None
        values(idx) = None
        _currSize = preSize - 1
        true
      }
    } else false
  }

}
