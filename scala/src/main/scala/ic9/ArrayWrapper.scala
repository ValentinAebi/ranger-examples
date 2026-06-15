package ic9


class ArrayWrapper[T] private(private val delegate: Array[Any]) {

  def this(size: Int) =
    this(new Array[Any](size))

  export delegate.length as size

  export delegate.update as set
  
  export delegate.apply as get

}
