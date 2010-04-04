-- MySQL dump 10.13  Distrib 5.1.43, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: lams2
-- ------------------------------------------------------
-- Server version	5.1.43

-- Ernie's inserts
SET FOREIGN_KEY_CHECKS=0; 
DROP DATABASE IF EXISTS lams2;
CREATE DATABASE lams2 DEFAULT CHARACTER SET utf8; 
SET FOREIGN_KEY_CHECKS=1;
GRANT ALL PRIVILEGES ON *.* TO lams@localhost IDENTIFIED BY 'lamsdemo';
REVOKE PROCESS,SUPER ON *.* from lams@localhost;
-- done