@ECHO OFF
SET INSTDIR=@INSTDIR@
SET MYSQL_HOME=%INSTDIR%\data\db
start /b "" "%MYSQL_HOME%\bin\mysqld.exe" --no-defaults --console --port=13306 --max_allowed_packet=1M --key_buffer=16K --net_buffer_length=2K --basedir="%MYSQL_HOME%" --thread_stack=128K --read_rnd_buffer_size=256K --socket=mysql.sock --sort_buffer_size=64K --table_cache=4 --read_buffer_size=256K --datadir="%MYSQL_HOME%\data" --pid-file="%MYSQL_HOME%\data\MysqldResource.pid"

REM Wait 10 seconds before we execute the script
PING localhost -n 11 > NUL
SET UPDATE_SQL_FILE="%INSTDIR%\update-sql-config.sql"
"%MYSQL_HOME%\bin\mysql.exe" -u lams -plamsdemo lams --port=13306 -e %UPDATE_SQL_FILE%

REM Once the SQL script is processed, then turn off MySQL gracefully 
REM wait 5 seconds and then kill MySQLd
PING localhost -n 6 > NUL
SET /p PID=<"%MYSQL_HOME%\data\MysqldResource.pid"
"%MYSQL_HOME%\c-mxj-utils\kill" %PID%
REM EXIT