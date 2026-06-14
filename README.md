# Range verification examples

This repository contains example programs demonstrating refinement types, in particular dependent range types. It serves as a comparison of the [Licorne](https://github.com/ValentinAebi/licorne-lang) experimental programming language with the following type refinement systems:

- The Java Checker Framework: [https://checkerframework.org/](https://checkerframework.org/) (specifically the Index Checker and the Constants Checker)
- LiquidJava: [https://liquid-java.github.io/](https://liquid-java.github.io/)

Examples `ic4`, `ic5`, `ic7`, and `ic9` are taken from the [manual of the Checker Framework](https://checkerframework.org/manual/) (sections 11.4, 11.5, 11.7, and 11.9, respectively). Examples `lj1` and `lj2` are taken from the [examples repository of LiquidJava](https://github.com/liquid-java/liquidjava-examples), `lj3` from the [Open VSX page of the LiquidJava VS Code extension](https://open-vsx.org/extension/AlcidesFonseca/liquid-java), and `lj4` from the [test suite of LiquidJava](https://github.com/liquid-java/liquidjava/tree/main/liquidjava-example/src/main/java/testSuite).

## Examples table

Click on the color circles to go to the corresponding example code. Click on the example row header to go to the original version of the example (except for the examples that we created ourselves).

|                                                                                                                                      | Licorne                                      | Java Checker Framework                                                                                          | LiquidJava                                                                 | L.o.c. (Licorne) ** |
|--------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------|------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|-----------------------------|
|Motivating example                                                                                                                    |[🟢](./licorne/motivation/Decoder.lic)        |[🟡 (5)](./java-index-checker/src/main/java/org/example/motivatingexample/Decoder.java)                          |[🔴 (generics)](./liquid-java/src/main/java/motivatingexample/Decoder.java) | 78 |
|[Mergesort](https://github.com/TheAlgorithms/Java/blob/master/src/main/java/com/thealgorithms/sorts/MergeSort.java)                   |[🟡 (1)](./licorne/mergesort/MergeSort.lic)   |[🟡 (3)](./java-index-checker/src/main/java/org/example/sorting/MergeSort.java)                                  |[❌](./liquid-java/src/main/java/sorting/MergeSort.java)                    | 84 |
|[ArrayMap](https://github.com/plume-lib/plume-util/blob/master/src/main/java/org/plumelib/util/ArrayMap.java)*                        |[🟢](./licorne/arraymap/maps/ArrayMap.lic)    |[🟢](./java-index-checker/src/main/java/org/example/arraymap/ArrayMap.java)                                      |                                                                            | 92 |
|Datetime                                                                                                                              |[🟢](./licorne/datetime/)                     | [🔵](./java-index-checker/src/main/java/org/example/datetime)                                                    |                                                                            | 81 |
|filterLessThan                                                                                                                        |[🟢](./licorne/filterLessThan/Example.lic)    |[🔴🟠 (possibly Java version)](./java-index-checker/src/main/java/org/example/filterlessthan/FilterLessThan.java)|                                                                            | 47 |
|[IC4](https://checkerframework.org/manual/#index-minlen)                                                                              |[🟢](./licorne/ic4/checker_framework_11_4.lic)|[🟢](./java-index-checker/src/main/java/org/example/ic4/IC4.java)                                                 |                                                                           | 23 |
|[IC5](https://checkerframework.org/manual/#index-samelen)                                                                             |[🟢](./licorne/ic5/checker_framework_11_5.lic)|[🟢](./java-index-checker/src/main/java/org/example/ic5/IC5.java)                                                 |                                                                           | 36 |
|[IC7](https://checkerframework.org/manual/#index-substringindex)                                                                      |[🟢](./licorne/ic7/checker_framework_11_7.lic)|[🟢](./java-index-checker/src/main/java/org/example/ic7/IC7.java)                                                 |                                                                           | 41 |
|[IC9](https://checkerframework.org/manual/#index-annotating-fixed-size)                                                               |[🟢](./licorne/ic9/ArrayWrapper.lic)          |[🟢](./java-index-checker/src/main/java/org/example/ic9/ArrayWrapper.java)                                        |                                                                           | 46 |
|[LJ1](https://github.com/liquid-java/liquidjava-examples/blob/main/user_study_23/part3-liquidJava/together1/src/together1/Test1.java) |[🟢](./licorne/lj1/fibonacci.lic)             |                                                                                                                  |[🔴 (unknown)](./liquid-java/src/main/java/lj1/Test1.java)                 | 10 |
|[LJ2](https://github.com/liquid-java/liquidjava-examples/blob/main/user_study_23/part3-liquidJava/together2/src/together2/Test1.java) |[🟢](./licorne/lj2/sum.lic)                   |                                                                                                                  |[🟢](./liquid-java/src/main/java/lj2/Test2.java)                           | 11 |
|[LJ3](https://open-vsx.org/extension/AlcidesFonseca/liquid-java#refinements)                                                          |[🟢](./licorne/lj3/example.lic)               |                                                                                                                  |[🟢](./liquid-java/src/main/java/lj3/Test3.java)                           | 10 |
|[LJ4](https://github.com/liquid-java/liquidjava/tree/main/liquidjava-example/src/main/java/testSuite/classes/car_correct)             |[🟢](./licorne/lj4/Test.lic)                  |                                                                                                                  |[🟢](./liquid-java/src/main/java/lj4/Test.java)                            | 25 |

*: the ArrayMap example is inspired from the mentioned source (where it contains annotations of the Checker Framework) but significantly simplified. In particular, in our implementation, the size of the array-map is fixed.

**: excluding empty lines

🟢 = works without casts or assertions

🔵 = works with less precision

🟡 (n) = works with n casts or assertions

🟠 (cause of failure) = too conservative

🔴 (cause of unsoundness) = unsound (on a faulty variation of the program)

❌ (what went wrong) = could not be encoded


## Running the type-checkers

Licorne: in the [`licorne` directory](./licorne), run `java -jar licorne-compiler.jar compile <files or directory>`, e.g. `java -jar .\licorne-compiler.jar compile .\arraymap\` (you need [Java](https://www.oracle.com/de/java/technologies/downloads), we tested with version 25)

Checker Framework: in the [`java-index-checker` directory](./java-index-checker), run `mvn clean compile` (you will need [Maven](https://maven.apache.org/))

LiquidJava: the simplest way seems to be to use the [VS Code extension](https://open-vsx.org/extension/AlcidesFonseca/liquid-java)

