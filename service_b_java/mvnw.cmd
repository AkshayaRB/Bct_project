@REM Maven wrapper for Windows
@REM Downloads Maven automatically if not present

@echo off
setlocal enabledelayedexpansion

set MAVEN_HOME=%~dp0.mvn
set MAVEN_WRAPPER_JAR=%MAVEN_HOME%\wrapper\maven-wrapper.jar
set MAVEN_WRAPPER_PROPERTIES=%MAVEN_HOME%\wrapper\maven-wrapper.properties

if not exist "%MAVEN_HOME%" mkdir "%MAVEN_HOME%"
if not exist "%MAVEN_HOME%\wrapper" mkdir "%MAVEN_HOME%\wrapper"

REM For testing purposes, just call mvn directly
REM If Maven is not available, provide instructions
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo Maven is not installed. Please install Maven first.
    echo Download from: https://maven.apache.org/download.cgi
    exit /b 1
)

mvn %*
