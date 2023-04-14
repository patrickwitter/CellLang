@echo off
call jflex Lexer.jflex
call cup -parser CellParser Parser.cup
call javac -classpath ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar" *.java
call java -cp ".;C:\cup\lib\java-cup-11b.jar;lib3652.jar" Main -w CellInterpreter -