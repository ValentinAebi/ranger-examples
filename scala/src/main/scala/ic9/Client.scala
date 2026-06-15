package ic9


def clearIndex1(a: ArrayWrapper[?], i: Int): Unit = {
  a.set(i, null)
}

def clearIndex2(a: ArrayWrapper[?], i: Int): Unit = {
  if (0 <= i && i < a.size) {
    a.set(i, null)
  }
}
