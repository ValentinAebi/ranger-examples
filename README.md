# Ranges verification examples

This repository contains example programs demonstrating refinement types, and in particular dependent range types. Three frameworks are used:

- The Licorne experimental programming language: [https://github.com/ValentinAebi/licorne-lang](https://github.com/ValentinAebi/licorne-lang)
- The Java Checker Framework: [https://checkerframework.org/](https://checkerframework.org/) (in particular the Index Checker)
- LiquidJava: [https://liquid-java.github.io/](https://liquid-java.github.io/)

Examples `ic4`, `ic5`, `ic7`, and `ic9` are taken from the [manual of the Checker Framework](https://checkerframework.org/manual/). Examples `lj1` and `lj2` are taken from the [examples repository of LiquidJava](https://github.com/liquid-java/liquidjava-examples), `lj3` from the [Open VSX page of the LiquidJava VS Code extension](https://open-vsx.org/extension/AlcidesFonseca/liquid-java), and `lj4` from the [test suite of LiquidJava](https://github.com/liquid-java/liquidjava/tree/main/liquidjava-example/src/main/java/testSuite).


## Running the type-checkers

Licorne: in the [`licorne` directory](./licorne), run `java -jar licorne-compiler.jar compile <files or directory>`, e.g. `java -jar .\licorne-compiler.jar compile .\arraymap\`

Checker Framework: in the [`java-index-checker` directory](./java-index-checker), run `mvn clean compile`

LiquidJava: the simplest way seems to be to use the [VS Code extension](https://open-vsx.org/extension/AlcidesFonseca/liquid-java)

