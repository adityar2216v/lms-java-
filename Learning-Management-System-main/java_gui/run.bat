@echo off
cd /d "%~dp0"
title EduPro Learning Management System (Desktop GUI)
echo ========================================================
echo       Starting EduPro LMS Desktop GUI Application       
echo ========================================================
echo.

if not exist bin (
    echo Compiling Java source files...
    mkdir bin
    javac -encoding UTF-8 -d bin src\com\lms\gui\model\*.java src\com\lms\gui\db\*.java src\com\lms\gui\theme\*.java src\com\lms\gui\views\*.java src\com\lms\gui\*.java
)

echo Launching EduPro LMS Desktop Window...
java -jar EduPro-LMS-Desktop.jar

if errorlevel 1 (
    echo Falling back to class files...
    java -cp bin com.lms.gui.LmsGuiApplication
)

pause
