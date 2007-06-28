-- This is used for a local database only (so @SQL_HOST@) always ends up as localhost. For separate MySQL servers, the user and the db must be created manually.

SET FOREIGN_KEY_CHECKS=0;
DROP DATABASE IF EXISTS @DB_NAME@;
CREATE DATABASE @DB_NAME@ DEFAULT CHARACTER SET utf8;
SET FOREIGN_KEY_CHECKS=1;
GRANT ALL PRIVILEGES ON *.* TO @DB_USER@@@SQL_HOST@ IDENTIFIED BY '@DB_PASS@';
REVOKE PROCESS,SUPER ON *.* from @DB_USER@@@SQL_HOST@;
USE @DB_NAME@;
