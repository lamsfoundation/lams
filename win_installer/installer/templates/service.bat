@echo off
REM JBoss, the OpenSource webOS
REM
REM Distributable under LGPL license.
REM See terms of license at gnu.org.
REM
REM -------------------------------------------------------------------------
REM JBoss Service Script for Windows
REM -------------------------------------------------------------------------


@if not "%ECHO%" == "" echo %ECHO%
@if "%OS%" == "Windows_NT" setlocal
set DIRNAME=%CD%

REM
REM VERSION, VERSION_MAJOR and VERSION_MINOR are populated
REM during the build with ant filter.
REM
set SVCNAME=LAMS
set SVCDISP=LAMS Server
set SVCDESC=Learning Activity Management System (LAMS)
set NOPAUSE=Y

REM Suppress killing service on logoff event
set LAMS_HOME=@INSTDIR@
set JBOSS_HOME=%LAMS_HOME%\jboss-5.1
set JRE_HOME=%LAMS_HOME%\jre
set WILDFIRE_HOME=%LAMS_HOME%\wildfire
set MYSQL_HOME=%LAMS_HOME%\data\db
set "JAVA_OPTS=-Xms128M -Xmx512M -XX:MaxPermSize=256M"
chdir /D %JBOSS_HOME%\bin

REM Figure out the running mode

if /I "%1" == "install"   goto cmdInstall
if /I "%1" == "uninstall" goto cmdUninstall
if /I "%1" == "start"     goto cmdStart
if /I "%1" == "stop"      goto cmdStop
if /I "%1" == "restart"   goto cmdRestart
if /I "%1" == "signal"    goto cmdSignal
echo Usage: service install^|uninstall^|start^|stop^|restart^|signal
goto cmdEnd

REM jbosssvc retun values
REM ERR_RET_USAGE           1
REM ERR_RET_VERSION         2
REM ERR_RET_INSTALL         3
REM ERR_RET_REMOVE          4
REM ERR_RET_PARAMS          5
REM ERR_RET_MODE            6

:errExplain
if errorlevel 1 echo Invalid command line parameters
if errorlevel 2 echo Failed installing %SVCDISP%
if errorlevel 4 echo Failed removing %SVCDISP%
if errorlevel 6 echo Unknown service mode for %SVCDISP%
goto cmdEnd

:cmdInstall
jbosssvc.exe -imwdc %SVCNAME% "%DIRNAME%" "%SVCDISP%" "%SVCDESC%" service.bat
if not errorlevel 0 goto errExplain
echo Service %SVCDISP% installed
goto cmdEnd

:cmdUninstall
jbosssvc.exe -u %SVCNAME%
if not errorlevel 0 goto errExplain
echo Service %SVCDISP% removed
goto cmdEnd

:cmdStart
REM Executed on service start
del .r.lock 2>&1 | findstr /C:"being used" > nul
if not errorlevel 1 (
  echo Could not continue. Locking file already in use.
  goto cmdEnd
)
echo Y > .r.lock
REM Start wildfire server
start /b ""  "%WILDFIRE_HOME%\wildfire.exe" 
jbosssvc.exe -p 1 "Starting %SVCDISP%" > run.log
call "%JBOSS_HOME%\bin\run.bat" -b 0.0.0.0 < .r.lock >> run.log 2>&1
jbosssvc.exe -p 1 "Shutdown %SVCDISP% service" >> run.log
del .r.lock
REM Stop MySQL 
set /p MYSQL_PID=<"%MYSQL_HOME%\data\MysqldResource.pid"
"%MYSQL_HOME%\c-mxj-utils\kill.exe" %MYSQL_PID%
REM Stop Wildfire
taskkill /f /t /FI "IMAGENAME eq wildfire.exe"
goto cmdEnd

:cmdStop
REM Executed on service stop
echo Y > .s.lock
jbosssvc.exe -p 1 "Shutting down %SVCDISP%" > shutdown.log
call "%JBOSS_HOME%\bin\shutdown" -S < .s.lock >> shutdown.log 2>&1
jbosssvc.exe -p 1 "Shutdown %SVCDISP% service" >> shutdown.log
REM Stop MySQL 
set /p MYSQL_PID=<"%MYSQL_HOME%\data\MysqldResource.pid"
"%MYSQL_HOME%\c-mxj-utils\kill.exe" %MYSQL_PID%
REM Stop Wildfire
taskkill /f /t /FI "IMAGENAME eq wildfire.exe"
del .s.lock
goto cmdEnd

:cmdRestart
REM Executed manually from command line
REM Note: We can only stop and start
echo Y > .s.lock
jbosssvc.exe -p 1 "Shutting down %SVCDISP%" >> shutdown.log
call shutdown -S < .s.lock >> shutdown.log 2>&1
del .s.lock
:waitRun
REM Delete lock file
del .r.lock > nul 2>&1
REM Wait one second if lock file exist
jbosssvc.exe -s 1
if exist ".r.lock" goto waitRun
echo Y > .r.lock
jbosssvc.exe -p 1 "Restarting %SVCDISP%" >> run.log
call run.bat < .r.lock >> run.log 2>&1
jbosssvc.exe -p 1 "Shutdown %SVCDISP% service" >> run.log
del .r.lock
goto cmdEnd

:cmdSignal
REM Send signal to the service.
REM Requires jbosssch.dll to be loaded in JVM
@if not ""%2"" == """" goto execSignal
echo Missing signal parameter.
echo Usage: service signal [0...9]
goto cmdEnd
:execSignal
jbosssvc.exe -k%2 %SVCNAME%
goto cmdEnd

:cmdEnd
