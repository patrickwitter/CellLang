#!/bin/bash

export CLASSPATH=$CLASSPATH:.
jflex Lexer.jflex
cup -parser CellParser Parser.cup
javac -classpath ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar" *.java


#Run this code to test whether there are any errors 