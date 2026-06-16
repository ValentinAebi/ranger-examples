package ic7


def removeSubstring_correct_1(original: String, removed: String): String = {
  val i = original.indexOf(removed)
  if (i == -1) {    // checking using !=
    original.substring(0, i) + original.substring(i + removed.length())
  } else {
    original
  }
}

def removeSubstring_correct_2(original: String, removed: String): String = {
  val i = original.indexOf(removed)
  if (i > -1) {      // <- checking using >
    original.substring(0, i) + original.substring(i + removed.length())
  } else {
    original
  }
}

def removeSubstring_buggy(original: String, removed: String): String = {
  val i = original.indexOf(removed)
  // // ERROR: missing check for i == -1
  original.substring(0, i) + original.substring(i + removed.length())
}
