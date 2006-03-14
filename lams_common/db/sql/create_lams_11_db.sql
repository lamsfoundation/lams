-- MySQL Script
-- Modify your server name
-- Modify username and password for the lams db
-- Create lams database for LAMS
-- 13/DEC/2004

SET FOREIGN_KEY_CHECKS=0;
DROP DATABASE IF EXISTS lams;
CREATE DATABASE lams DEFAULT CHARACTER SET utf8;
SET FOREIGN_KEY_CHECKS=1;

--USE mysql;

--DELETE FROM user WHERE user='';
-- DELETE FROM user WHERE user IS NULL;

--DELETE FROM db WHERE user='';
-- DELETE FROM db WHERE user IS NULL;

--GRANT ALL PRIVILEGES ON *.* TO lams@localhost IDENTIFIED BY 'lamsdemo';
--REVOKE PROCESS,SUPER ON *.* from lams@localhost;

--The hostname below should be replaced with the actual host name
--GRANT ALL PRIVILEGES ON *.* TO lams@'mams-yoichi' IDENTIFIED BY 'lamsdemo';
--REVOKE PROCESS,SUPER ON *.* from lams@'mams-yoichi';

--GRANT ALL PRIVILEGES ON *.* TO lams@'%' IDENTIFIED BY 'lamsdemo';
--REVOKE PROCESS,SUPER ON *.* from lams@'%';

--FLUSH PRIVILEGES;

--GRANT ALL PRIVILEGES ON lams.* TO lams@localhost IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
--The hostname below should be replaced with the actual host name
--GRANT ALL PRIVILEGES ON lams.* TO lams@'mams-yoichi' IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
--GRANT ALL PRIVILEGES ON lams.* TO lams@'%' IDENTIFIED BY 'lamsdemo' WITH GRANT OPTION;
	
--FLUSH PRIVILEGES;

USE lams;

SET FOREIGN_KEY_CHECKS=1;