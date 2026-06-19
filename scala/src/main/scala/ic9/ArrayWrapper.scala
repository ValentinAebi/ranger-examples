package ic9


class ArrayWrapper[T] private(private val delegate: Array[Any]) {

  def this(size: Int) =
    this(new Array[Any](size))    //> ArrayWrapper::constructor aux-annot=1

  export delegate.length as size  //> ArrayWrapper::size

  export delegate.update as set   //> ArrayWrapper::set
  
  export delegate.apply as get    //> ArrayWrapper::get

}
