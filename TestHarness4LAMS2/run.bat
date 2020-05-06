@echo off
rem -------------------------------------------------------------------------
rem TestHarness Bootstrap Script for Win32
rem -------------------------------------------------------------------------



set JVM_MEM=-XX:MaxPermSize=256m -Xmx512m

@if not "%ECHO%" == ""  echo %ECHO%
@if "%OS%" == "Windows_NT"  setlocal

set DIRNAME=.\
if "%OS%" == "Windows_NT" set DIRNAME=%~dp0%

rem Read all command line arguments

set ARGS=
:loop
if [%1] == [] goto endloop
        set ARGS=%ARGS% %1
        shift
        goto loop
:endloop

if not "%JAVA_HOME%" == "" goto ADD_TOOLS

set JAVA=java

echo JAVA_HOME is not set.  Unexpected results may occur.
echo Set JAVA_HOME to the directory of your local JDK to avoid this message.

:ADD_TOOLS

set JAVA=%JAVA_HOME%\bin\java

rem Setup TestHarness sepecific properties
set TESTHARNESS_HOME=%DIRNAME%
set JAVA_OPTS=%JAVA_OPTS% %JVM_MEM%
set TESTHARNESS_CLASSPATH=;%TESTHARNESS_HOME%build\;%TESTHARNESS_HOME%lib\BrowserLauncher2-all-10rc4.jar;%TESTHARNESS_HOME%lib\commons-codec-1.1.jar;%TESTHARNESS_HOME%lib\httpunit.jar;%TESTHARNESS_HOME%lib\js.jar;%TESTHARNESS_HOME%lib\junit.jar;%TESTHARNESS_HOME%lib\log4j-1.2.13.jar;%TESTHARNESS_HOME%lib\nekohtml-0.9.5.jar;%TESTHARNESS_HOME%lib\Tidy.jar;%TESTHARNESS_HOME%lib\xercesImpl.jar;%TESTHARNESS_HOME%lib\xml-apis.jar

echo ===============================================================================
echo .
echo   TestHarness Bootstrap Environment
echo .
echo   JAVA: %JAVA%
echo .
echo   JAVA_OPTS: %JAVA_OPTS%
echo .
echo   CLASSPATH: %TESTHARNESS_CLASSPATH%
echo .
echo   TESTHARNESS_HOME: %TESTHARNESS_HOME%
echo ===============================================================================
echo .

"%JAVA%" %JAVA_OPTS% -classpath "%TESTHARNESS_CLASSPATH%" org.lamsfoundation.testharness.Main test.properties

:END
if "%NOPAUSE%" == "" pause

:END_NO_PAUSE
