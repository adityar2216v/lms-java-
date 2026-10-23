@echo off
cd /d "%~dp0"
title SmartLearn India — Smart Learning Management System
echo ==================================================================
echo         SmartLearn India — Smart Learning Management System       
echo                   Learn Smarter. Grow Further.                    
echo ==================================================================
echo.

if not exist bin (
    echo Compiling SmartLearn India source files...
    mkdir bin
    javac -encoding UTF-8 -d bin src\com\lms\gui\model\*.java src\com\lms\gui\db\*.java src\com\lms\gui\theme\*.java src\com\lms\gui\views\*.java src\com\lms\gui\test\*.java src\com\lms\gui\*.java
)

echo Launching SmartLearn India Desktop Portal...
java -Dawt.useSystemAAFontSettings=lcd -Dswing.aatext=true -Dsun.java2d.dpiaware=true -jar SmartLearn-India-LMS.jar

if errorlevel 1 (
    echo Falling back to compiled classes...
    java -Dawt.useSystemAAFontSettings=lcd -Dswing.aatext=true -Dsun.java2d.dpiaware=true -cp bin com.lms.gui.LmsGuiApplication
)

pause
