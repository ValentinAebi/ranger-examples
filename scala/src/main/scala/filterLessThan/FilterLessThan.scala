package filterLessThan


def filterLessThan_adHoc(a: Int, ls: List[Int]): List[Int] = ls match {
  case head :: next if head < a => head :: filterLessThan_adHoc(a, next)
  case head :: next => filterLessThan_adHoc(a, next)
  case Nil => Nil
}

def filterLessThan_better(a: Int, ls: List[Int]): List[Int] =
  ls.filter(_ < a)
