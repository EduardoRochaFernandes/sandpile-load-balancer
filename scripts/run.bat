@echo off
REM ============================================================
REM run.bat — Quick launcher for Sandpile Load Balancer (Windows)
REM ============================================================
REM Usage:
REM   scripts\run.bat                              (Interactive mode)
REM   scripts\run.bat -f 2 -a matrix5.csv -o result.txt
REM ============================================================

SET SCRIPT_DIR=%~dp0
SET RELEASE_DIR=%SCRIPT_DIR%..\releases\final-release_1.0.0
SET JAR=%RELEASE_DIR%\main.jar

IF NOT EXIST "%JAR%" (
    echo [ERROR] JAR not found at: %JAR%
    echo         Make sure you are running this from the project root.
    exit /b 1
)

cd /d "%RELEASE_DIR%"
echo [INFO] Running: java -jar main.jar %*
java -jar main.jar %*
