@echo off

:CHECK_HOME
if not "%MYSQL_HOME%" == "" goto CHECK_MYSQL
echo .
echo Error: MYSQL_HOME is not set correctly.
echo .
goto END

:CHECK_MYSQL

if exist "%MYSQL_HOME%\bin\mysqladmin.exe" goto RUN
echo .
echo Error: %MYSQL_HOME%\bin\mysqladmin.exe not found.
echo .
goto END

:RUN

echo Shutting down MySQL daemon...
start "MySQL" "%MYSQL_HOME%\bin\mysqladmin.exe" -u root shutdown
echo done!
:END

pause

