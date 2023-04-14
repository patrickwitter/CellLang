#!/bin/bash

export CLASSPATH=$CLASSPATH:.
jflex Lexer.jflex
cup -parser CellParser Parser.cup
javac -classpath ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar" *.java
java -cp ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar" Main -w CellInterpreter -
