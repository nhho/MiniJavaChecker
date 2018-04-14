# MiniJavaChecker

a checker that builds symbol table and checks type for MiniJava program

## Background

MiniJava appears in *Modern Compiler Implementation in Java (Second Edition)*. More details can be found [here](http://www.cambridge.org/resources/052182060X/MCIIJ2e/). The parser is amended from the files in [here](http://www.cambridge.org/resources/052182060X/
).

## Reference

The grammar follows from this [reference](http://www.cs.tufts.edu/~sguyer/classes/comp181-2006/minijava.html), which is a mirror copy of the respective section from the textbook, with the following exception:

* `double` is allowed as a data type

* overloading is allowed with the rules in Java

## Usage

* run `Makefile` with the `make` command to compile all the files.

* run `java Task1Main Y < input_file`
  * `input_file` should contain a syntactically valid program in MiniJava Variant
  * unknown identifier and redeclaration of identifier will be shown as error
  * the appearance of the identifiers with value `Y` will be shown with some information

* run `java Task2Main < input_file`
  * `input_file` should contain a syntactically valid program in MiniJava Variant which should not cause error in `Task1Main`
  * type mismatch and failure in resolving method will be shown as error
