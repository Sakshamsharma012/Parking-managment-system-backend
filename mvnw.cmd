@REM Maven Wrapper startup batch script
@echo off
setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_HOME=%MAVEN_PROJECTBASEDIR%.mvn\maven
set MAVEN_ZIP=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\apache-maven-3.9.6-bin.zip

IF NOT EXIST "%MAVEN_HOME%\bin\mvn.cmd" (
    echo Downloading Apache Maven 3.9.6...
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.zip' -OutFile '%MAVEN_ZIP%'"
    echo Extracting Maven...
    powershell -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%MAVEN_PROJECTBASEDIR%.mvn' -Force"
    ren "%MAVEN_PROJECTBASEDIR%.mvn\apache-maven-3.9.6" maven
    del "%MAVEN_ZIP%" 2>nul
)

"%MAVEN_HOME%\bin\mvn.cmd" %*
