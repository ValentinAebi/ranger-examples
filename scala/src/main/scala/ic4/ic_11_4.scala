package ic4


def getThirdElement_correct(arr: Array[String]) = {    //> IC4::getThirdElement_1_correct
  arr(2)                                            //> IC4::getThirdElement_2_correct
}

def getThirdElement_buggy(arr: Array[String]) = {      //> IC4::getThirdElement_1_buggy
  arr(3)                                            //> IC4::getThirdElement_2_buggy
}
