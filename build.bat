@echo off
java -jar lib\jflex-full-1.9.1.jar -d gen src\lexer\lexer.flex
if errorlevel 1 exit /b 1

javac -encoding UTF-8 -d bin gen\Lexer.java src\Token.java src\AutomatoPilha.java src\Main.java
if errorlevel 1 exit /b 1

java -cp bin Main %1