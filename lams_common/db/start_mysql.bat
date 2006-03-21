@echo off

:CHECK_HOME
if not "%MYSQL_HOME%" == "" goto CHECK_MYSQL
echo .
echo Error: MYSQL_HOME is not set correctly.
echo .
goto END

:CHECK_MYSQL

if exist "%MYSQL_HOME%\bin\mysqld-nt.exe" goto RUN
echo .
echo Error: %MYSQL_HOME%\bin\mysqld-nt.exe not found.
echo .
goto END

:RUN

echo Starting MySQL daemon stand-alone...
start "MySQL" "%MYSQL_HOME%\bin\mysqld-nt.exe" --standalone -l --default-table-type=InnoDB
echo done!
:END

pause
