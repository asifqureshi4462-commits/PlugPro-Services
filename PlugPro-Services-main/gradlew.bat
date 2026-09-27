@echo off
setlocal

set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

if exist "%WRAPPER_JAR%" (
    java -Xmx2048m -Dfile.encoding=UTF-8 -jar "%WRAPPER_JAR%" %*
    exit /b %ERRORLEVEL%
)

where gradle >nul 2>nul
if %ERRORLEVEL% == 0 (
    gradle %*
    exit /b %ERRORLEVEL%
)

echo Gradle wrapper jar not found and no system Gradle installation found.
echo Open this project in Android Studio, which will offer to regenerate the wrapper automatically.
exit /b 1
