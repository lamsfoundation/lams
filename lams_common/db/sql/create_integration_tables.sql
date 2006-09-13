--
-- Table structure for table `lams_ext_server_org_map`
--
DROP TABLE IF EXISTS `lams_ext_server_org_map`;
CREATE TABLE `lams_ext_server_org_map` (
  `sid` int(11) NOT NULL auto_increment,
  `serverid` varchar(255) NOT NULL,
  `serverkey` text NOT NULL,
  `servername` varchar(255) NOT NULL,
  `serverdesc` text,
  `prefix` varchar(11) NOT NULL,
  `userinfo_url` text NOT NULL,
  `timeout_url` text NOT NULL,
  `disabled` bit(1) NOT NULL,
  `orgid` bigint(20) NOT NULL,
  PRIMARY KEY  (`sid`),
  UNIQUE KEY `serverid` (`serverid`),
  UNIQUE KEY `prefix` (`prefix`),
  KEY `orgid` (`orgid`),
  CONSTRAINT `lams_ext_server_org_map_fk` FOREIGN KEY (`orgid`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;

--
-- Table structure for table `lams_ext_course_class_map`
--
DROP TABLE IF EXISTS `lams_ext_course_class_map`;
CREATE TABLE `lams_ext_course_class_map` (
  `sid` int(11) NOT NULL auto_increment,
  `courseid` varchar(255) NOT NULL,
  `classid` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY  (`sid`),
  KEY `classid` (`classid`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_course_class_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_course_class_map_fk` FOREIGN KEY (`classid`) REFERENCES `lams_organisation` (`organisation_id`)
) TYPE=InnoDB;


--
-- Table structure for table `lams_ext_user_userid_map`
--
DROP TABLE IF EXISTS `lams_ext_user_userid_map`;
CREATE TABLE `lams_ext_user_userid_map` (
  `sid` int(11) NOT NULL auto_increment,
  `external_username` varchar(250) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY  (`sid`),
  KEY `user_id` (`user_id`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_user_userid_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_user_userid_map_fk` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) TYPE=InnoDB;