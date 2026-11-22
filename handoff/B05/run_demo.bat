@echo off
if not exist out mkdir out
javac -d out src\domain\reflection\*.java
java -cp out domain.reflection.ReflectionDriver