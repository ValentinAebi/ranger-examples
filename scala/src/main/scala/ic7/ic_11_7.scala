package ic7


def removeSubstring(original: String, removed: String): String = {
  val i = original.indexOf(removed)
  if (i == -1) {
    original.substring(0, i) + original.substring(i + removed.length())
  } else {
    original
  }
}
