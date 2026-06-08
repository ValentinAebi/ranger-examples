# Ranges verification examples

This repository contains example programs demonstrating refinement types, and in particular dependent range types. Three frameworks are used:

- The Licorne experimental programming language: [https://github.com/ValentinAebi/licorne-lang](https://github.com/ValentinAebi/licorne-lang)
- The Java Checker Framework: [https://checkerframework.org/](https://checkerframework.org/) (in particular the Index Checker)
- LiquidJava: [https://liquid-java.github.io/](https://liquid-java.github.io/)

Some examples are taken from the [manual of the Checker Framework](https://checkerframework.org/manual/). Others are taken from the [examples repository of LiquidJava](https://github.com/liquid-java/liquidjava-examples).


## Running the type-checkers

Licorne: in the [`licorne` directory](./licorne), run `java -jar licorne-compiler.jar compile <files or directory>`, e.g. `java -jar .\licorne-compiler.jar compile .\arraymap\*.lic`

Checker Framework: in the [`java-index-checker` directory](./java-index-checker), run `mvn clean compile`

