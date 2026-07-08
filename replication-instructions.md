# Replication


## Directory structure

```text
/
|_ java-checker-framework/ ......................... Checker Framework version of our examples
|   |_ pom.xml ..................................... Maven configuration file
|   |_ src/main/java/org/example/
|       |_ arraymap/ ............................... Every package corresponds to one example
|       |   |_ ArrayMap.java
|       |   |_ ArrayUtils.java
|       |_ datetime/
|       |   |_ ...
|       |_ ...
|_ licorne/ ........................................ Licorne/Ranger version of our examples
|   |_ arraymap/ ................................... Every subfolder corresponds to one example
|   |   |_ arrays/Array.lic ........................ Some examples are split into packages
|   |   |_ maps/
|   |   |   |_ ArrayMap.lic
|   |   |   |_ Map.lic
|   |   |_ general_aliases.lic
|   |   |_ OrderedCollections.lic
|   |_ datetime/
|   |   |_ ...
|   |_ ...
|_ liquid-java/ .................................... LiquidJava version of our examples
|   |_ src/main/java/
|   |   |_ datetime ................................ Every package corresponds to one example
|   |   |   |_ ...
|   |   |_ ...
|_ scala/ .......................................... Scala version of our examples
|   |_ build.sbt ................................... SBT (Scala Build Tool) configuration file
|   |_ src/main/scala/
|   |   |_ arraymap/ ............................... Every package corresponds to one example
|   |   |   |_ ArrayMap.scala
|   |   |_ ...
|_ scripts/ ........................................ Automation scripts
|   |_ comparison_script.py ........................ Script that collects the data in the formal comments and builds the results table
|   |_ timing_script.py ............................ Script that runs and times the Ranger and Scala type-checkers
|   |_ formal_comments_explanation.txt ............. Description of the system of formal comments that we use
|_ Dockerfile
|_ README.md ....................................... Readme of the GitHub repository containing our examples (please read replication-instructions.md first)
|_ replication-instructions.md ..................... CURRENT FILE
```


## Steps to reproduce

1. Load the Docker image: in this directory, run: 
```sh
docker load -i ranger-image.tar
```
Alternatively, you can build the image from the [Dockerfile](./Dockerfile).

2. Run the image: in this directory, run:
```sh
docker run -it "ranger-image"
```

3. To type-check all Licorne and Scala examples at once, navigate to the `scripts` directory:
```sh
cd /opt/ranger-examples/scripts/
```
then run the timing script:
```sh
python3 timing_script.py
```
The script displays the compilation times while running, and writes the stdout and stderr outputs, as well as the compilation times, to files in the `/opt/ranger-examples/scripts/timing-runs` directory. The Licorne compiler prints errors to stderr, while the stdout file will contain only "Compiler not implemented" messages, which merely mean that the compiler stopped after type-checking because we currently have no backend.

⚠️ **Important note**: in the paper, we indicate that Licorne took 16s to compile all examples, while Scala took 34s. We obtained these results on a Windows machine (Lenovo ThinkPad, Windows 11, 64GB RAM, Intel Core Ultra 9 2.3 GHz). When running the same experiments on the same machine but using the Ubuntu-based Docker image, we got about the same compilation time for Licorne (15s), but the Scala compilation times fell down to about 6.7s. While we don't know the exact reason of this difference, we think that this still supports the claim that we make in the paper that Ranger’s typechecking process is fast enough to be practically usable.

4. To run the script that collects the annotations of all units and builds the results table (table 2 in the paper), navigate to the `scripts` directory:
```sh
cd /opt/ranger-examples/scripts/
```
then run the script:
```sh
python3 comparison_script.py
```
The script additionally runs consistency checks and outputs some warnings to the console, referring to the fact that some units are not implemented in LiquidJava and other units are marked as "buggy" in the implementation in one of the frameworks but not in another framework. This is expected, because some annotations that one tool fails to verify may be inexpressible in another tool. The script also outputs a LaTeX version of the table to the console, and a CSV version to a file in the `out` directory. To display it:
```sh
cat /opt/ranger-examples/scripts/out/table.csv
```

5. To verify a single Licorne program, navigate to the `licorne` directory
```sh
cd /opt/ranger-examples/licorne
```
then run the type-checker:
```sh
java -jar licorne-compiler.jar compile <example name>
```
E.g.:
```sh
java -jar licorne-compiler.jar compile arraymap/
```


## Running the Checker Framework

The [pom.xml](./java-checker-framework/pom.xml) file in the [Checker Framework directory](./java-checker-framework/) ensures that the Checker Framework is run when compiling. To run it, you need [Java 25](https://www.oracle.com/java/technologies/downloads/#java25) and [Maven](https://maven.apache.org/download.cgi). Then just run
```sh
mvn clean compile
```
in the [Checker Framework directory](./java-checker-framework/).


## Verifying programs using LiquidJava

Install the LiquidJava VSCode extension from [its Visual Studio Marketplace page](https://marketplace.visualstudio.com/items?itemName=AlcidesFonseca.liquid-java), and open the file that you want to verify. LiquidJava will display error messages next to the code that it cannot verify. The Visual Studio Marketplace page of LiquidJava also provides additional information about the tool.


## Description of the experiments

We partitioned the code into *units* (most units are functions). Every unit is annotated according to a system of formal comments, whose precise description can be found [here](./scripts/formal_comments_explanation.txt). 
The annotations specify, among others, the number of annotations used in that unit, whether or not the unit contains one or more bug(s), and whether or not the tool flags the unit (i.e. reports one or more bug(s) in that particular unit). 
Our [comparison script](./scripts/comparison_script.py) traverses all files and collects the information specified by the formal comments. It outputs this information as a table, corresponding to table 2 in the paper. 
It dumps the LaTeX code of the table to the console, and produces a [CSV version](./scripts/out/table.csv) in the [`scripts/out`](./scripts/out/) directory.

