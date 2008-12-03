-- MySQL dump 10.11
--
-- Host: localhost    Database: lams
-- ------------------------------------------------------
-- Server version	5.0.45-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `lams_activity_category`
--

DROP TABLE IF EXISTS `lams_activity_category`;
CREATE TABLE `lams_activity_category` (
  `activity_category_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`activity_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_activity_category`
--

LOCK TABLES `lams_activity_category` WRITE;
/*!40000 ALTER TABLE `lams_activity_category` DISABLE KEYS */;
INSERT INTO `lams_activity_category` VALUES (1,'SYSTEM'),(2,'COLLABORATION'),(3,'ASSESSMENT'),(4,'CONTENT'),(5,'SPLIT'),(6,'RESPONSE');
/*!40000 ALTER TABLE `lams_activity_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_activity_learners`
--

DROP TABLE IF EXISTS `lams_activity_learners`;
CREATE TABLE `lams_activity_learners` (
  `user_id` bigint(20) NOT NULL default '0',
  `activity_id` bigint(20) NOT NULL default '0',
  `allowed_to_pass` tinyint(4) NOT NULL default '0',
  KEY `user_id` (`user_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_TABLE_32_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_TABLE_32_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_activity_learners`
--

LOCK TABLES `lams_activity_learners` WRITE;
/*!40000 ALTER TABLE `lams_activity_learners` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_activity_learners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_auth_method_type`
--

DROP TABLE IF EXISTS `lams_auth_method_type`;
CREATE TABLE `lams_auth_method_type` (
  `authentication_method_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`authentication_method_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_auth_method_type`
--

LOCK TABLES `lams_auth_method_type` WRITE;
/*!40000 ALTER TABLE `lams_auth_method_type` DISABLE KEYS */;
INSERT INTO `lams_auth_method_type` VALUES (1,'LAMS'),(2,'WEB_AUTH'),(3,'LDAP');
/*!40000 ALTER TABLE `lams_auth_method_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_authentication_method`
--

DROP TABLE IF EXISTS `lams_authentication_method`;
CREATE TABLE `lams_authentication_method` (
  `authentication_method_id` bigint(20) NOT NULL,
  `authentication_method_type_id` int(3) NOT NULL default '0',
  `authentication_method_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`authentication_method_id`),
  UNIQUE KEY `UQ_lams_authentication_method_1` (`authentication_method_name`),
  KEY `authentication_method_type_id` (`authentication_method_type_id`),
  CONSTRAINT `FK_lams_authorization_method_1` FOREIGN KEY (`authentication_method_type_id`) REFERENCES `lams_auth_method_type` (`authentication_method_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_authentication_method`
--

LOCK TABLES `lams_authentication_method` WRITE;
/*!40000 ALTER TABLE `lams_authentication_method` DISABLE KEYS */;
INSERT INTO `lams_authentication_method` VALUES (1,1,'LAMS-Database'),(2,2,'Oxford-WebAuth'),(3,3,'MQ-LDAP');
/*!40000 ALTER TABLE `lams_authentication_method` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_branch_activity_entry`
--

DROP TABLE IF EXISTS `lams_branch_activity_entry`;
CREATE TABLE `lams_branch_activity_entry` (
  `entry_id` bigint(20) NOT NULL auto_increment,
  `entry_ui_id` int(11) default NULL,
  `group_id` bigint(20) default NULL,
  `sequence_activity_id` bigint(20) default NULL,
  `branch_activity_id` bigint(20) NOT NULL,
  `condition_id` bigint(20) default NULL,
  `open_gate` tinyint(4) default NULL,
  PRIMARY KEY  (`entry_id`),
  KEY `group_id` (`group_id`),
  KEY `sequence_activity_id` (`sequence_activity_id`),
  KEY `branch_activity_id` (`branch_activity_id`),
  KEY `condition_id` (`condition_id`),
  CONSTRAINT `FK_lams_group_activity_1` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`),
  CONSTRAINT `FK_lams_branch_map_sequence` FOREIGN KEY (`sequence_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_branch_map_branch` FOREIGN KEY (`branch_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_branch_activity_entry_4` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_branch_activity_entry`
--

LOCK TABLES `lams_branch_activity_entry` WRITE;
/*!40000 ALTER TABLE `lams_branch_activity_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_branch_activity_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_branch_condition`
--

DROP TABLE IF EXISTS `lams_branch_condition`;
CREATE TABLE `lams_branch_condition` (
  `condition_id` bigint(20) NOT NULL auto_increment,
  `condition_ui_id` int(11) default NULL,
  `order_id` int(11) default NULL,
  `name` varchar(255) NOT NULL,
  `display_name` varchar(255) default NULL,
  `type` varchar(30) NOT NULL,
  `start_value` varchar(255) default NULL,
  `end_value` varchar(255) default NULL,
  `exact_match_value` varchar(255) default NULL,
  PRIMARY KEY  (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_branch_condition`
--

LOCK TABLES `lams_branch_condition` WRITE;
/*!40000 ALTER TABLE `lams_branch_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_branch_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_competence`
--

DROP TABLE IF EXISTS `lams_competence`;
CREATE TABLE `lams_competence` (
  `competence_id` bigint(20) NOT NULL auto_increment,
  `learning_design_id` bigint(20) default NULL,
  `description` text,
  `title` varchar(255) default NULL,
  PRIMARY KEY  (`competence_id`),
  UNIQUE KEY `competence_id` (`competence_id`),
  UNIQUE KEY `learning_design_id` (`learning_design_id`,`title`),
  CONSTRAINT `LearningDesignCompetenceMap` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_competence`
--

LOCK TABLES `lams_competence` WRITE;
/*!40000 ALTER TABLE `lams_competence` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_competence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_competence_mapping`
--

DROP TABLE IF EXISTS `lams_competence_mapping`;
CREATE TABLE `lams_competence_mapping` (
  `competence_mapping_id` bigint(20) NOT NULL auto_increment,
  `competence_id` bigint(20) default NULL,
  `activity_id` bigint(20) default NULL,
  PRIMARY KEY  (`competence_mapping_id`),
  UNIQUE KEY `competence_mapping_id` (`competence_mapping_id`),
  UNIQUE KEY `competence_id` (`competence_id`,`activity_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_competence_mapping_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_competence_mapping_2` FOREIGN KEY (`competence_id`) REFERENCES `lams_competence` (`competence_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_competence_mapping`
--

LOCK TABLES `lams_competence_mapping` WRITE;
/*!40000 ALTER TABLE `lams_competence_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_competence_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_configuration`
--

DROP TABLE IF EXISTS `lams_configuration`;
CREATE TABLE `lams_configuration` (
  `config_key` varchar(30) NOT NULL,
  `config_value` varchar(255) default NULL,
  `description_key` varchar(255) default NULL,
  `header_name` varchar(50) default NULL,
  `format` varchar(30) default NULL,
  `required` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_configuration`
--

LOCK TABLES `lams_configuration` WRITE;
/*!40000 ALTER TABLE `lams_configuration` DISABLE KEYS */;
INSERT INTO `lams_configuration` VALUES ('AdminScreenSize','800x600','config.admin.screen.size','config.header.look.feel','STRING',1),('AllowDirectLessonLaunch','false','config.allow.direct.lesson.launch','config.header.features','BOOLEAN',1),('AllowLiveEdit','true','config.allow.live.edit','config.header.features','BOOLEAN',1),('AuthoringActivitiesColour','true','config.authoring.activities.colour','config.header.look.feel','BOOLEAN',1),('AuthoringClientVersion','2.2.0.200812031438','config.authoring.client.version','config.header.versions','STRING',1),('AuthoringScreenSize','800x600','config.authoring.screen.size','config.header.look.feel','STRING',1),('CleanupPreviewOlderThanDays','7','config.cleanup.preview.older.than.days','config.header.system','LONG',1),('ContentRepositoryPath','C:/lams/repository','config.content.repository.path','config.header.uploads','STRING',1),('CustomTabLink','','config.custom.tab.link','config.header.look.feel','STRING',0),('CustomTabTitle','','config.custom.tab.title','config.header.look.feel','STRING',0),('DefaultFlashTheme','default','config.default.flash.theme','config.header.look.feel','STRING',1),('DefaultHTMLTheme','defaultHTML','config.default.html.theme','config.header.look.feel','STRING',1),('DictionaryDateCreated','2008-12-05','config.dictionary.date.created','config.header.versions','STRING',1),('DumpDir','C:/lams/dump','config.dump.dir','config.header.system','STRING',1),('EARDir','C:/jboss-4.0.2/server/default/deploy/lams.ear','config.ear.dir','config.header.system','STRING',1),('EnableFlash','true','config.flash.enable','config.header.features','BOOLEAN',1),('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs','config.executable.extensions','config.header.uploads','STRING',1),('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/','config.help.url','config.header.system','STRING',1),('LamsSupportEmail','','config.lams.support.email','config.header.email','STRING',0),('LAMS_Community_enable','false','config.community.enable','config.header.features','BOOLEAN',1),('LDAPAddr1Attr','postalAddress','admin.user.address_line_1','config.header.ldap.attributes','STRING',0),('LDAPAddr2Attr','','admin.user.address_line_2','config.header.ldap.attributes','STRING',0),('LDAPAddr3Attr','','admin.user.address_line_3','config.header.ldap.attributes','STRING',0),('LDAPAuthorMap','Teacher;SeniorStaff;Principal','config.ldap.author.map','config.header.ldap.attributes','STRING',0),('LDAPBaseDN',',ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au','config.ldap.base.dn','config.header.ldap','STRING',0),('LDAPBindUserDN','','config.ldap.bind.user.dn','config.header.ldap','STRING',0),('LDAPBindUserPassword','','config.ldap.bind.user.password','config.header.ldap','STRING',0),('LDAPCityAttr','l','admin.user.city','config.header.ldap.attributes','STRING',0),('LDAPCountryAttr','','admin.user.country','config.header.ldap.attributes','STRING',0),('LDAPDayPhoneAttr','telephoneNumber','admin.user.day_phone','config.header.ldap.attributes','STRING',0),('LDAPDisabledAttr','!accountStatus','sysadmin.disabled','config.header.ldap.attributes','STRING',0),('LDAPEmailAttr','mail','admin.user.email','config.header.ldap.attributes','STRING',0),('LDAPEncryptPasswordFromBrowser','true','config.ldap.encrypt.password.from.browser','config.header.ldap','BOOLEAN',1),('LDAPEveningPhoneAttr','homePhone','admin.user.evening_phone','config.header.ldap.attributes','STRING',0),('LDAPFaxAttr','facsimileTelephoneNumber','admin.user.fax','config.header.ldap.attributes','STRING',0),('LDAPFNameAttr','givenName','admin.user.first_name','config.header.ldap.attributes','STRING',0),('LDAPGroupAdminMap','Teacher;SeniorStaff','config.ldap.group.admin.map','config.header.ldap.attributes','STRING',0),('LDAPGroupManagerMap','Principal','config.ldap.group.manager.map','config.header.ldap.attributes','STRING',0),('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.learner.map','config.header.ldap.attributes','STRING',0),('LDAPLNameAttr','sn','admin.user.last_name','config.header.ldap.attributes','STRING',0),('LDAPLocaleAttr','preferredLanguage','admin.organisation.locale','config.header.ldap.attributes','STRING',0),('LDAPLoginAttr','uid','admin.user.login','config.header.ldap.attributes','STRING',0),('LDAPMobileAttr','mobile','admin.user.mobile_phone','config.header.ldap.attributes','STRING',0),('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.monitor.map','config.header.ldap.attributes','STRING',0),('LDAPOnlyOneOrg','true','config.ldap.only.one.org','config.header.ldap','BOOLEAN',1),('LDAPOrgAttr','schoolCode','admin.course','config.header.ldap.attributes','STRING',0),('LDAPOrgField','code','config.ldap.org.field','config.header.ldap.attributes','STRING',0),('LDAPPostcodeAttr','postalCode','admin.user.postcode','config.header.ldap.attributes','STRING',0),('LDAPProviderURL','ldap://192.158.1.1','config.ldap.provider.url','config.header.ldap','STRING',0),('LDAPProvisioningEnabled','false','config.ldap.provisioning.enabled','config.header.ldap','BOOLEAN',1),('LDAPRolesAttr','memberOf','admin.user.roles','config.header.ldap.attributes','STRING',0),('LDAPSearchFilter','(cn={0})','config.ldap.search.filter','config.header.ldap','STRING',0),('LDAPSearchResultsPageSize','100','config.ldap.search.results.page.size','config.header.ldap','LONG',0),('LDAPSecurityAuthentication','simple','config.ldap.security.authentication','config.header.ldap','STRING',0),('LDAPSecurityProtocol','','config.ldap.security.protocol','config.header.ldap','STRING',0),('LDAPStateAttr','st','admin.user.state','config.header.ldap.attributes','STRING',0),('LDAPUpdateOnLogin','true','config.ldap.update.on.login','config.header.ldap','BOOLEAN',1),('LearnerClientVersion','2.2.0.200812031438','config.learner.client.version','config.header.versions','STRING',1),('LearnerProgressBatchSize','10','config.learner.progress.batch.size','config.header.look.feel','LONG',1),('LearnerScreenSize','800x600','config.learner.screen.size','config.header.look.feel','STRING',1),('MonitorClientVersion','2.2.0.200812031438','config.monitor.client.version','config.header.versions','STRING',1),('MonitorScreenSize','800x600','config.monitor.screen.size','config.header.look.feel','STRING',1),('ServerLanguage','en_AU','config.server.language','config.header.look.feel','STRING',1),('ServerPageDirection','LTR','config.server.page.direction','config.header.look.feel','STRING',1),('ServerURL','http://localhost:8080/lams/','config.server.url','config.header.system','STRING',1),('ServerURLContextPath','lams/','config.server.url.context.path','config.header.system','STRING',1),('ServerVersionNumber','2.2.0.200812031438','config.server.version.number','config.header.versions','STRING',1),('SMTPServer','','config.smtp.server','config.header.email','STRING',0),('TempDir','C:/lams/temp','config.temp.dir','config.header.system','STRING',1),('TruststorePassword','','config.ldap.truststore.password','config.header.system','STRING',0),('TruststorePath','','config.ldap.truststore.path','config.header.system','STRING',0),('UploadFileMaxMemorySize','4096','config.upload.file.max.memory.size','config.header.uploads','LONG',1),('UploadFileMaxSize','1048576','config.upload.file.max.size','config.header.uploads','LONG',1),('UploadLargeFileMaxSize','10485760','config.upload.large.file.max.size','config.header.uploads','LONG',1),('UseCacheDebugListener','false','config.use.cache.debug.listener','config.header.system','BOOLEAN',1),('UserInactiveTimeout','86400','config.user.inactive.timeout','config.header.system','LONG',1),('Version','2.2','config.version','config.header.system','STRING',1),('XmppAdmin','admin','config.xmpp.admin','config.header.chat','STRING',0),('XmppConference','conference.shaun.melcoe.mq.edu.au','config.xmpp.conference','config.header.chat','STRING',0),('XmppDomain','shaun.melcoe.mq.edu.au','config.xmpp.domain','config.header.chat','STRING',0),('XmppPassword','','config.xmpp.password','config.header.chat','STRING',0);
/*!40000 ALTER TABLE `lams_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_copy_type`
--

DROP TABLE IF EXISTS `lams_copy_type`;
CREATE TABLE `lams_copy_type` (
  `copy_type_id` tinyint(4) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`copy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_copy_type`
--

LOCK TABLES `lams_copy_type` WRITE;
/*!40000 ALTER TABLE `lams_copy_type` DISABLE KEYS */;
INSERT INTO `lams_copy_type` VALUES (1,'NONE'),(2,'LESSON'),(3,'PREVIEW');
/*!40000 ALTER TABLE `lams_copy_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_credential`
--

DROP TABLE IF EXISTS `lams_cr_credential`;
CREATE TABLE `lams_cr_credential` (
  `credential_id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY  (`credential_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Records the identification properties for a tool.';

--
-- Dumping data for table `lams_cr_credential`
--

LOCK TABLES `lams_cr_credential` WRITE;
/*!40000 ALTER TABLE `lams_cr_credential` DISABLE KEYS */;
INSERT INTO `lams_cr_credential` VALUES (1,'sharedresources','lams-sharedresources');
/*!40000 ALTER TABLE `lams_cr_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node`
--

DROP TABLE IF EXISTS `lams_cr_node`;
CREATE TABLE `lams_cr_node` (
  `node_id` bigint(20) unsigned NOT NULL auto_increment,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `path` varchar(255) default NULL,
  `type` varchar(255) NOT NULL,
  `created_date_time` datetime NOT NULL,
  `next_version_id` bigint(20) unsigned NOT NULL default '1',
  `parent_nv_id` bigint(20) unsigned default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `workspace_id` (`workspace_id`),
  CONSTRAINT `FK_lams_cr_node_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='The main table containing the node definition';

--
-- Dumping data for table `lams_cr_node`
--

LOCK TABLES `lams_cr_node` WRITE;
/*!40000 ALTER TABLE `lams_cr_node` DISABLE KEYS */;
INSERT INTO `lams_cr_node` VALUES (1,1,NULL,'PACKAGENODE','2008-12-03 14:44:36',2,NULL),(2,1,'blueline.gif','FILENODE','2008-12-03 14:44:36',2,1),(3,1,'effective_authoring.swf','FILENODE','2008-12-03 14:44:36',2,1),(4,1,'index.htm','FILENODE','2008-12-03 14:44:36',2,1),(5,1,NULL,'PACKAGENODE','2008-12-03 14:44:36',2,NULL),(6,1,'blueline.gif','FILENODE','2008-12-03 14:44:36',2,5),(7,1,'index.htm','FILENODE','2008-12-03 14:44:36',2,5),(8,1,'whatislams.swf','FILENODE','2008-12-03 14:44:36',2,5),(9,1,NULL,'PACKAGENODE','2008-12-03 14:44:43',2,NULL),(10,1,'blueline.gif','FILENODE','2008-12-03 14:44:43',2,9),(11,1,'index.htm','FILENODE','2008-12-03 14:44:43',2,9),(12,1,'introToMonitor.swf','FILENODE','2008-12-03 14:44:43',2,9),(13,1,NULL,'PACKAGENODE','2008-12-03 14:44:43',2,NULL),(14,1,'blueline.gif','FILENODE','2008-12-03 14:44:43',2,13),(15,1,'index.htm','FILENODE','2008-12-03 14:44:43',2,13),(16,1,'introToAuthoring.swf','FILENODE','2008-12-03 14:44:43',2,13),(17,1,NULL,'PACKAGENODE','2008-12-03 14:44:43',2,NULL),(18,1,'blueline.gif','FILENODE','2008-12-03 14:44:43',2,17),(19,1,'index.htm','FILENODE','2008-12-03 14:44:43',2,17),(20,1,'learnerExperience.swf','FILENODE','2008-12-03 14:44:43',2,17),(21,1,NULL,'PACKAGENODE','2008-12-03 14:44:43',2,NULL),(22,1,'blueline.gif','FILENODE','2008-12-03 14:44:43',2,21),(23,1,'index.htm','FILENODE','2008-12-03 14:44:43',2,21),(24,1,'noflash.swf','FILENODE','2008-12-03 14:44:43',2,21),(25,1,NULL,'PACKAGENODE','2008-12-03 14:44:58',2,NULL),(26,1,'blueline.gif','FILENODE','2008-12-03 14:44:58',2,25),(27,1,'c5_noticeboard.swf','FILENODE','2008-12-03 14:44:58',2,25),(28,1,'index.htm','FILENODE','2008-12-03 14:44:58',2,25),(29,1,NULL,'PACKAGENODE','2008-12-03 14:44:58',2,NULL),(30,1,'blueline.gif','FILENODE','2008-12-03 14:44:58',2,29),(31,1,'c8_shareResources.swf','FILENODE','2008-12-03 14:44:58',2,29),(32,1,'index.htm','FILENODE','2008-12-03 14:44:58',2,29);
/*!40000 ALTER TABLE `lams_cr_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version`
--

DROP TABLE IF EXISTS `lams_cr_node_version`;
CREATE TABLE `lams_cr_node_version` (
  `nv_id` bigint(20) unsigned NOT NULL auto_increment,
  `node_id` bigint(20) unsigned NOT NULL,
  `version_id` bigint(20) unsigned NOT NULL,
  `created_date_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`nv_id`),
  KEY `node_id` (`node_id`),
  CONSTRAINT `FK_lams_cr_node_version_2` FOREIGN KEY (`node_id`) REFERENCES `lams_cr_node` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='Represents a version of a node';

--
-- Dumping data for table `lams_cr_node_version`
--

LOCK TABLES `lams_cr_node_version` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version` DISABLE KEYS */;
INSERT INTO `lams_cr_node_version` VALUES (1,1,1,'2008-12-03 14:44:36',5),(2,2,1,'2008-12-03 14:44:36',5),(3,3,1,'2008-12-03 14:44:36',5),(4,4,1,'2008-12-03 14:44:36',5),(5,5,1,'2008-12-03 14:44:36',5),(6,6,1,'2008-12-03 14:44:36',5),(7,7,1,'2008-12-03 14:44:36',5),(8,8,1,'2008-12-03 14:44:36',5),(9,9,1,'2008-12-03 14:44:43',5),(10,10,1,'2008-12-03 14:44:43',5),(11,11,1,'2008-12-03 14:44:43',5),(12,12,1,'2008-12-03 14:44:43',5),(13,13,1,'2008-12-03 14:44:43',5),(14,14,1,'2008-12-03 14:44:43',5),(15,15,1,'2008-12-03 14:44:43',5),(16,16,1,'2008-12-03 14:44:43',5),(17,17,1,'2008-12-03 14:44:43',5),(18,18,1,'2008-12-03 14:44:43',5),(19,19,1,'2008-12-03 14:44:43',5),(20,20,1,'2008-12-03 14:44:43',5),(21,21,1,'2008-12-03 14:44:43',5),(22,22,1,'2008-12-03 14:44:43',5),(23,23,1,'2008-12-03 14:44:43',5),(24,24,1,'2008-12-03 14:44:43',5),(25,25,1,'2008-12-03 14:44:58',5),(26,26,1,'2008-12-03 14:44:58',5),(27,27,1,'2008-12-03 14:44:58',5),(28,28,1,'2008-12-03 14:44:58',5),(29,29,1,'2008-12-03 14:44:58',5),(30,30,1,'2008-12-03 14:44:58',5),(31,31,1,'2008-12-03 14:44:58',5),(32,32,1,'2008-12-03 14:44:58',5);
/*!40000 ALTER TABLE `lams_cr_node_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version_property`
--

DROP TABLE IF EXISTS `lams_cr_node_version_property`;
CREATE TABLE `lams_cr_node_version_property` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `nv_id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `nv_id` (`nv_id`),
  CONSTRAINT `FK_lams_cr_node_version_property_1` FOREIGN KEY (`nv_id`) REFERENCES `lams_cr_node_version` (`nv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_cr_node_version_property`
--

LOCK TABLES `lams_cr_node_version_property` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version_property` DISABLE KEYS */;
INSERT INTO `lams_cr_node_version_property` VALUES (1,1,'INITIALPATH','index.htm',1),(2,2,'FILENAME','blueline.gif',1),(3,3,'FILENAME','effective_authoring.swf',1),(4,4,'FILENAME','index.htm',1),(5,5,'INITIALPATH','index.htm',1),(6,6,'FILENAME','blueline.gif',1),(7,7,'FILENAME','index.htm',1),(8,8,'FILENAME','whatislams.swf',1),(9,9,'INITIALPATH','index.htm',1),(10,10,'FILENAME','blueline.gif',1),(11,11,'FILENAME','index.htm',1),(12,12,'FILENAME','introToMonitor.swf',1),(13,13,'INITIALPATH','index.htm',1),(14,14,'FILENAME','blueline.gif',1),(15,15,'FILENAME','index.htm',1),(16,16,'FILENAME','introToAuthoring.swf',1),(17,17,'INITIALPATH','index.htm',1),(18,18,'FILENAME','blueline.gif',1),(19,19,'FILENAME','index.htm',1),(20,20,'FILENAME','learnerExperience.swf',1),(21,21,'INITIALPATH','index.htm',1),(22,22,'FILENAME','blueline.gif',1),(23,23,'FILENAME','index.htm',1),(24,24,'FILENAME','noflash.swf',1),(25,25,'INITIALPATH','index.htm',1),(26,26,'FILENAME','blueline.gif',1),(27,27,'FILENAME','c5_noticeboard.swf',1),(28,28,'FILENAME','index.htm',1),(29,29,'INITIALPATH','index.htm',1),(30,30,'FILENAME','blueline.gif',1),(31,31,'FILENAME','c8_shareResources.swf',1),(32,32,'FILENAME','index.htm',1);
/*!40000 ALTER TABLE `lams_cr_node_version_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_workspace`
--

DROP TABLE IF EXISTS `lams_cr_workspace`;
CREATE TABLE `lams_cr_workspace` (
  `workspace_id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Content repository workspace';

--
-- Dumping data for table `lams_cr_workspace`
--

LOCK TABLES `lams_cr_workspace` WRITE;
/*!40000 ALTER TABLE `lams_cr_workspace` DISABLE KEYS */;
INSERT INTO `lams_cr_workspace` VALUES (1,'sharedresourcesworkspace');
/*!40000 ALTER TABLE `lams_cr_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_workspace_credential`
--

DROP TABLE IF EXISTS `lams_cr_workspace_credential`;
CREATE TABLE `lams_cr_workspace_credential` (
  `wc_id` bigint(20) unsigned NOT NULL auto_increment,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `credential_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`wc_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `credential_id` (`credential_id`),
  CONSTRAINT `FK_lams_cr_workspace_credential_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_cr_workspace_credential_2` FOREIGN KEY (`credential_id`) REFERENCES `lams_cr_credential` (`credential_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Maps tools access to workspaces';

--
-- Dumping data for table `lams_cr_workspace_credential`
--

LOCK TABLES `lams_cr_workspace_credential` WRITE;
/*!40000 ALTER TABLE `lams_cr_workspace_credential` DISABLE KEYS */;
INSERT INTO `lams_cr_workspace_credential` VALUES (1,1,1);
/*!40000 ALTER TABLE `lams_cr_workspace_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_css_property`
--

DROP TABLE IF EXISTS `lams_css_property`;
CREATE TABLE `lams_css_property` (
  `property_id` bigint(20) NOT NULL auto_increment,
  `style_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(100) NOT NULL,
  `style_subset` varchar(20) default NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY  (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_css_property`
--

LOCK TABLES `lams_css_property` WRITE;
/*!40000 ALTER TABLE `lams_css_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_css_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_css_style`
--

DROP TABLE IF EXISTS `lams_css_style`;
CREATE TABLE `lams_css_style` (
  `style_id` bigint(20) NOT NULL auto_increment,
  `theme_ve_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`style_id`),
  KEY `theme_ve_id` (`theme_ve_id`),
  CONSTRAINT `FK_lams_css_style_1` FOREIGN KEY (`theme_ve_id`) REFERENCES `lams_css_theme_ve` (`theme_ve_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Groups lams_css_property into a CSSStyleDeclaration.';

--
-- Dumping data for table `lams_css_style`
--

LOCK TABLES `lams_css_style` WRITE;
/*!40000 ALTER TABLE `lams_css_style` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_css_style` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_css_theme_ve`
--

DROP TABLE IF EXISTS `lams_css_theme_ve`;
CREATE TABLE `lams_css_theme_ve` (
  `theme_ve_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(100) NOT NULL,
  `description` varchar(100) default NULL,
  `parent_id` bigint(20) default NULL,
  `theme_flag` tinyint(1) NOT NULL default '0',
  `image_directory` varchar(100) default NULL,
  PRIMARY KEY  (`theme_ve_id`),
  KEY `parent_id` (`parent_id`),
  CONSTRAINT `FK_lams_css_theme_ve_2` FOREIGN KEY (`parent_id`) REFERENCES `lams_css_theme_ve` (`theme_ve_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='Stores both the Flash theme and visual element';

--
-- Dumping data for table `lams_css_theme_ve`
--

LOCK TABLES `lams_css_theme_ve` WRITE;
/*!40000 ALTER TABLE `lams_css_theme_ve` DISABLE KEYS */;
INSERT INTO `lams_css_theme_ve` VALUES (1,'default','Default Flash style',NULL,1,NULL),(2,'defaultHTML','Default HTML style',NULL,1,'css'),(3,'rams','RAMS Default Flash style',NULL,1,NULL),(4,'ramsthemeHTML','RAMS Default HTML sty;e',NULL,1,'ramsthemecss');
/*!40000 ALTER TABLE `lams_css_theme_ve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_event_subscriptions`
--

DROP TABLE IF EXISTS `lams_event_subscriptions`;
CREATE TABLE `lams_event_subscriptions` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `event_uid` bigint(20) default NULL,
  `delivery_method_id` tinyint(3) unsigned default NULL,
  `periodicity` bigint(20) default NULL,
  `last_operation_time` datetime default NULL,
  `last_operation_message` text,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `EventSubscriptionsToUsers` (`user_id`),
  KEY `event_uid` (`event_uid`),
  CONSTRAINT `EventSubscriptionsToUsers` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EventSubscriptionsToEvent` FOREIGN KEY (`event_uid`) REFERENCES `lams_events` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_event_subscriptions`
--

LOCK TABLES `lams_event_subscriptions` WRITE;
/*!40000 ALTER TABLE `lams_event_subscriptions` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_event_subscriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_events`
--

DROP TABLE IF EXISTS `lams_events`;
CREATE TABLE `lams_events` (
  `uid` bigint(20) NOT NULL auto_increment,
  `scope` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `event_session_id` bigint(20) default NULL,
  `triggered` tinyint(4) default NULL,
  `default_subject` varchar(255) default NULL,
  `default_message` text,
  `subject` varchar(255) default NULL,
  `message` text,
  `fail_time` datetime default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `scope` (`scope`,`name`,`event_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_events`
--

LOCK TABLES `lams_events` WRITE;
/*!40000 ALTER TABLE `lams_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_events` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_ext_course_class_map`
--

LOCK TABLES `lams_ext_course_class_map` WRITE;
/*!40000 ALTER TABLE `lams_ext_course_class_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_ext_course_class_map` ENABLE KEYS */;
UNLOCK TABLES;

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
  `orgid` bigint(20) default NULL,
  PRIMARY KEY  (`sid`),
  UNIQUE KEY `serverid` (`serverid`),
  UNIQUE KEY `prefix` (`prefix`),
  KEY `orgid` (`orgid`),
  CONSTRAINT `lams_ext_server_org_map_fk` FOREIGN KEY (`orgid`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_ext_server_org_map`
--

LOCK TABLES `lams_ext_server_org_map` WRITE;
/*!40000 ALTER TABLE `lams_ext_server_org_map` DISABLE KEYS */;
INSERT INTO `lams_ext_server_org_map` VALUES (1,'moodle','moodle','moodle','moodle','mdl','http://localhost/moodle/mod/lamstwo/userinfo.php?ts=%timestamp%&un=%username%&hs=%hash%','http://dummy',0x00,7);
/*!40000 ALTER TABLE `lams_ext_server_org_map` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_ext_user_userid_map`
--

LOCK TABLES `lams_ext_user_userid_map` WRITE;
/*!40000 ALTER TABLE `lams_ext_user_userid_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_ext_user_userid_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_gate_activity_level`
--

DROP TABLE IF EXISTS `lams_gate_activity_level`;
CREATE TABLE `lams_gate_activity_level` (
  `gate_activity_level_id` int(11) NOT NULL default '0',
  `description` varchar(128) NOT NULL,
  PRIMARY KEY  (`gate_activity_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_gate_activity_level`
--

LOCK TABLES `lams_gate_activity_level` WRITE;
/*!40000 ALTER TABLE `lams_gate_activity_level` DISABLE KEYS */;
INSERT INTO `lams_gate_activity_level` VALUES (1,'LEARNER'),(2,'GROUP'),(3,'CLASS');
/*!40000 ALTER TABLE `lams_gate_activity_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_group`
--

DROP TABLE IF EXISTS `lams_group`;
CREATE TABLE `lams_group` (
  `group_id` bigint(20) NOT NULL auto_increment,
  `group_name` varchar(255) NOT NULL,
  `grouping_id` bigint(20) NOT NULL,
  `order_id` int(6) NOT NULL default '1',
  `group_ui_id` int(11) default NULL,
  PRIMARY KEY  (`group_id`),
  KEY `grouping_id` (`grouping_id`),
  CONSTRAINT `FK_lams_learning_group_1` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_group`
--

LOCK TABLES `lams_group` WRITE;
/*!40000 ALTER TABLE `lams_group` DISABLE KEYS */;
INSERT INTO `lams_group` VALUES (1,'Everybody learners',1,1,NULL),(2,'Everybody monitors',1,1,NULL),(3,'Everybody learners',2,1,NULL),(4,'Everybody monitors',2,1,NULL),(5,'Everybody learners',3,1,NULL),(6,'Everybody monitors',3,1,NULL);
/*!40000 ALTER TABLE `lams_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping`
--

DROP TABLE IF EXISTS `lams_grouping`;
CREATE TABLE `lams_grouping` (
  `grouping_id` bigint(20) NOT NULL auto_increment,
  `grouping_ui_id` int(11) default NULL,
  `grouping_type_id` int(11) NOT NULL,
  `number_of_groups` int(11) default NULL,
  `learners_per_group` int(11) default NULL,
  `staff_group_id` bigint(20) default '0',
  `max_number_of_groups` int(3) default NULL,
  `equal_number_of_learners_per_group` tinyint(4) default '0',
  PRIMARY KEY  (`grouping_id`),
  KEY `grouping_type_id` (`grouping_type_id`),
  CONSTRAINT `FK_lams_learning_grouping_1` FOREIGN KEY (`grouping_type_id`) REFERENCES `lams_grouping_type` (`grouping_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_grouping`
--

LOCK TABLES `lams_grouping` WRITE;
/*!40000 ALTER TABLE `lams_grouping` DISABLE KEYS */;
INSERT INTO `lams_grouping` VALUES (1,NULL,3,NULL,NULL,2,NULL,0),(2,NULL,3,NULL,NULL,4,NULL,0),(3,NULL,3,NULL,NULL,6,NULL,0);
/*!40000 ALTER TABLE `lams_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping_support_type`
--

DROP TABLE IF EXISTS `lams_grouping_support_type`;
CREATE TABLE `lams_grouping_support_type` (
  `grouping_support_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`grouping_support_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_grouping_support_type`
--

LOCK TABLES `lams_grouping_support_type` WRITE;
/*!40000 ALTER TABLE `lams_grouping_support_type` DISABLE KEYS */;
INSERT INTO `lams_grouping_support_type` VALUES (1,'NONE'),(2,'OPTIONAL'),(3,'REQUIRED');
/*!40000 ALTER TABLE `lams_grouping_support_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping_type`
--

DROP TABLE IF EXISTS `lams_grouping_type`;
CREATE TABLE `lams_grouping_type` (
  `grouping_type_id` int(11) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY  (`grouping_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_grouping_type`
--

LOCK TABLES `lams_grouping_type` WRITE;
/*!40000 ALTER TABLE `lams_grouping_type` DISABLE KEYS */;
INSERT INTO `lams_grouping_type` VALUES (1,'RANDOM_GROUPING'),(2,'CHOSEN_GROUPING'),(3,'CLASS_GROUPING'),(4,'LEARNER_CHOICE_GROUPING');
/*!40000 ALTER TABLE `lams_grouping_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_input_activity`
--

DROP TABLE IF EXISTS `lams_input_activity`;
CREATE TABLE `lams_input_activity` (
  `activity_id` bigint(20) NOT NULL,
  `input_activity_id` bigint(20) NOT NULL,
  UNIQUE KEY `UQ_lams_input_activity_1` (`activity_id`,`input_activity_id`),
  KEY `activity_id` (`activity_id`),
  KEY `activity_id_2` (`activity_id`),
  CONSTRAINT `FK_lams_input_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_input_activity_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_input_activity`
--

LOCK TABLES `lams_input_activity` WRITE;
/*!40000 ALTER TABLE `lams_input_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_input_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learner_progress`
--

DROP TABLE IF EXISTS `lams_learner_progress`;
CREATE TABLE `lams_learner_progress` (
  `learner_progress_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `lesson_completed_flag` tinyint(1) NOT NULL default '0',
  `waiting_flag` tinyint(4) NOT NULL,
  `start_date_time` datetime NOT NULL,
  `finish_date_time` datetime default NULL,
  `current_activity_id` bigint(20) default NULL,
  `next_activity_id` bigint(20) default NULL,
  `previous_activity_id` bigint(20) default NULL,
  `requires_restart_flag` tinyint(1) NOT NULL,
  PRIMARY KEY  (`learner_progress_id`),
  UNIQUE KEY `IX_lams_learner_progress_1` (`user_id`,`lesson_id`),
  KEY `user_id` (`user_id`),
  KEY `lesson_id` (`lesson_id`),
  KEY `current_activity_id` (`current_activity_id`),
  KEY `next_activity_id` (`next_activity_id`),
  KEY `previous_activity_id` (`previous_activity_id`),
  CONSTRAINT `FK_lams_learner_progress_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learner_progress_2` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_learner_progress_3` FOREIGN KEY (`current_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learner_progress_4` FOREIGN KEY (`next_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learner_progress_5` FOREIGN KEY (`previous_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learner_progress`
--

LOCK TABLES `lams_learner_progress` WRITE;
/*!40000 ALTER TABLE `lams_learner_progress` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_learner_progress` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_activity`
--

DROP TABLE IF EXISTS `lams_learning_activity`;
CREATE TABLE `lams_learning_activity` (
  `activity_id` bigint(20) NOT NULL auto_increment,
  `activity_ui_id` int(11) default NULL,
  `description` text,
  `title` varchar(255) default NULL,
  `help_text` text,
  `xcoord` int(11) default NULL,
  `ycoord` int(11) default NULL,
  `parent_activity_id` bigint(20) default NULL,
  `parent_ui_id` int(11) default NULL,
  `learning_activity_type_id` int(11) NOT NULL default '0',
  `grouping_support_type_id` int(3) NOT NULL,
  `apply_grouping_flag` tinyint(1) NOT NULL,
  `grouping_id` bigint(20) default NULL,
  `grouping_ui_id` int(11) default NULL,
  `order_id` int(11) default NULL,
  `define_later_flag` tinyint(4) NOT NULL default '0',
  `learning_design_id` bigint(20) default NULL,
  `learning_library_id` bigint(20) default NULL,
  `create_date_time` datetime NOT NULL,
  `run_offline_flag` tinyint(1) NOT NULL,
  `max_number_of_options` int(5) default NULL,
  `min_number_of_options` int(5) default NULL,
  `options_instructions` text,
  `tool_id` bigint(20) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `activity_category_id` int(3) NOT NULL,
  `gate_activity_level_id` int(11) default NULL,
  `gate_open_flag` tinyint(1) default NULL,
  `gate_start_time_offset` bigint(38) default NULL,
  `gate_end_time_offset` bigint(38) default NULL,
  `gate_start_date_time` datetime default NULL,
  `gate_end_date_time` datetime default NULL,
  `library_activity_ui_image` varchar(255) default NULL,
  `create_grouping_id` bigint(20) default NULL,
  `create_grouping_ui_id` int(11) default NULL,
  `library_activity_id` bigint(20) default NULL,
  `language_file` varchar(255) default NULL,
  `system_tool_id` bigint(20) default NULL,
  `read_only` tinyint(4) default '0',
  `initialised` tinyint(4) default '0',
  `default_activity_id` bigint(20) default NULL,
  `start_xcoord` int(11) default NULL,
  `start_ycoord` int(11) default NULL,
  `end_xcoord` int(11) default NULL,
  `end_ycoord` int(11) default NULL,
  `stop_after_activity` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`activity_id`),
  KEY `learning_library_id` (`learning_library_id`),
  KEY `learning_design_id` (`learning_design_id`),
  KEY `parent_activity_id` (`parent_activity_id`),
  KEY `learning_activity_type_id` (`learning_activity_type_id`),
  KEY `grouping_id` (`grouping_id`),
  KEY `tool_id` (`tool_id`),
  KEY `gate_activity_level_id` (`gate_activity_level_id`),
  KEY `create_grouping_id` (`create_grouping_id`),
  KEY `library_activity_id` (`library_activity_id`),
  KEY `activity_category_id` (`activity_category_id`),
  KEY `grouping_support_type_id` (`grouping_support_type_id`),
  KEY `system_tool_id` (`system_tool_id`),
  CONSTRAINT `FK_lams_learning_activity_7` FOREIGN KEY (`learning_library_id`) REFERENCES `lams_learning_library` (`learning_library_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_learning_activity_6` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_learning_activity_2` FOREIGN KEY (`parent_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_learning_activity_3` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_learning_activity_6` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_learning_activity_8` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`),
  CONSTRAINT `FK_lams_learning_activity_10` FOREIGN KEY (`gate_activity_level_id`) REFERENCES `lams_gate_activity_level` (`gate_activity_level_id`),
  CONSTRAINT `FK_lams_learning_activity_9` FOREIGN KEY (`create_grouping_id`) REFERENCES `lams_grouping` (`grouping_id`),
  CONSTRAINT `FK_lams_learning_activity_11` FOREIGN KEY (`library_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_learning_activity_12` FOREIGN KEY (`activity_category_id`) REFERENCES `lams_activity_category` (`activity_category_id`),
  CONSTRAINT `FK_lams_learning_activity_13` FOREIGN KEY (`grouping_support_type_id`) REFERENCES `lams_grouping_support_type` (`grouping_support_type_id`),
  CONSTRAINT `FK_lams_learning_activity_14` FOREIGN KEY (`system_tool_id`) REFERENCES `lams_system_tool` (`system_tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learning_activity`
--

LOCK TABLES `lams_learning_activity` WRITE;
/*!40000 ALTER TABLE `lams_learning_activity` DISABLE KEYS */;
INSERT INTO `lams_learning_activity` VALUES (1,NULL,'Forum/Message Board','Forum','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,1,'2008-12-03 14:38:40',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(2,NULL,'Displays a NoticeboardX','NoticeboardX','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,2,'2008-12-03 14:38:46',0,NULL,NULL,NULL,2,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.noticeboard.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(3,NULL,'Allows creation and use of question and answer format','Question and Answer','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,3,'2008-12-03 14:38:51',0,NULL,NULL,NULL,3,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.qa.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(4,NULL,'Uploading of files by learners, for review by teachers.','Submit File','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,4,'2008-12-03 14:38:57',0,NULL,NULL,NULL,4,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.sbmt.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(5,NULL,'Chat Tool','Chat Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,5,'2008-12-03 14:39:05',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(6,NULL,'Share Resources','Share Resources','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,6,'2008-12-03 14:39:14',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(7,NULL,'Allows creation and use of voting format','Voting','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,7,'2008-12-03 14:39:18',0,NULL,NULL,NULL,7,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.vote.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(8,NULL,'Notebook Tool','Notebook Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,8,'2008-12-03 14:39:26',0,NULL,NULL,NULL,8,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.notebook.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(9,NULL,'Survey','Survey','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,9,'2008-12-03 14:39:34',0,NULL,NULL,NULL,9,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.survey.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(10,NULL,'Scribe Tool','Scribe Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,10,'2008-12-03 14:39:43',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(11,NULL,'TaskList','TaskList','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,11,'2008-12-03 14:39:52',0,NULL,NULL,NULL,11,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/latask10/images/icon_taskList.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.taskList.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(12,NULL,'Gmap Tool','Gmap Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,12,'2008-12-03 14:40:01',0,NULL,NULL,NULL,12,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.gmap.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(13,NULL,'Spreadsheet','Spreadsheet','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,13,'2008-12-03 14:40:09',0,NULL,NULL,NULL,13,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(14,NULL,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,14,'2008-12-03 14:40:17',0,NULL,NULL,NULL,14,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.daco.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(15,NULL,'Wiki Tool','Wiki Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,15,'2008-12-03 14:40:25',0,NULL,NULL,NULL,15,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.wiki.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(16,NULL,'Allows creation and use of multiple choice questioning format','Multiple Choice Questions','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,16,'2008-12-03 14:40:30',0,NULL,NULL,NULL,16,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.mc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(17,NULL,'Combined Share Resources and Forum','Discuss Shared Resources','Learners to discuss items they have viewed via Share Resources.',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,17,'2008-12-03 14:40:32',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid17.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(18,NULL,'Share Resources','Share Resources','Share Resources',NULL,NULL,17,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:32',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(19,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,17,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:32',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(20,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,18,'2008-12-03 14:40:33',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_groupreporting.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid18.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(21,NULL,'Chat Tool','Chat Tool','Chat',NULL,NULL,20,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:33',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(22,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,20,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:33',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(23,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,19,'2008-12-03 14:40:34',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_forum_and_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid19.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(24,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,23,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:34',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(25,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,23,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2008-12-03 14:40:34',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(26,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',40,51,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2008-12-03 14:44:36',0,NULL,NULL,NULL,2,17,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(27,8,'Tool for displaying HTML content including external sources such as images and other media.','Visual Authoring','Displays formatted text and links to external sources on a read only page.',33,166,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2008-12-03 14:44:36',0,NULL,NULL,NULL,2,18,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(28,10,'Sharing resource with others.','Authoring Environment','Uploading your resources to share with others.',235,171,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2008-12-03 14:44:36',0,NULL,NULL,NULL,6,19,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(29,12,'Tool for displaying HTML content including external sources such as images and other media.','Summing Up','Displays formatted text and links to external sources on a read only page.',425,170,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2008-12-03 14:44:36',0,NULL,NULL,NULL,2,20,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(30,21,'Sharing resource with others.','Effective Learning','Uploading your resources to share with others.',227,47,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2008-12-03 14:44:36',0,NULL,NULL,NULL,6,21,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(31,1,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',46,213,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,2,22,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(32,4,'Choose one of the four options below to learn more about that particular aspect of LAMS.','Optional Sequences',NULL,254,100,NULL,NULL,13,2,0,NULL,NULL,NULL,0,2,NULL,'2008-12-03 14:44:43',0,NULL,1,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(33,5,NULL,'The Learning Environment (Participate in a Lesson)',NULL,4,48,32,4,8,2,0,NULL,NULL,1,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,34,NULL,NULL,NULL,NULL,0),(34,20,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',5,5,33,5,1,2,0,NULL,NULL,1,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,2,23,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(35,25,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',558,210,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,2,24,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(36,13,'Sharing resource with others.','Learner Wink','Uploading your resources to share with others.',65,5,33,5,1,2,0,NULL,NULL,2,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,6,28,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(37,6,NULL,'The Authoring Environment (Create a Lesson)',NULL,4,105,32,4,8,2,0,NULL,NULL,2,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,43,NULL,NULL,NULL,NULL,0),(38,8,NULL,'The Monitoring Environment (Teach a Lesson)',NULL,4,162,32,4,8,2,0,NULL,NULL,3,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,41,NULL,NULL,NULL,NULL,0),(39,23,'Sharing resource with others.','NoFlash Wink','Uploading your resources to share with others.',124,5,33,5,1,2,0,NULL,NULL,3,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,6,30,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(40,16,NULL,'System Administration [Online Link]',NULL,4,219,32,4,8,2,0,NULL,NULL,4,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,42,NULL,NULL,NULL,NULL,0),(41,15,'Sharing resource with others.','Monitor Wink','Uploading your resources to share with others.',5,5,38,8,1,2,0,NULL,NULL,1,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,6,25,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(42,17,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',5,5,40,16,1,2,0,NULL,NULL,1,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,6,26,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(43,14,'Sharing resource with others.','Author Wink','Uploading your resources to share with others.',5,5,37,6,1,2,0,NULL,NULL,1,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,6,27,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(44,50,'Tool for displaying HTML content including external sources such as images and other media.','Structure of LAMS','Displays formatted text and links to external sources on a read only page.',65,5,38,8,1,2,0,NULL,NULL,2,0,2,NULL,'2008-12-03 14:44:43',0,NULL,NULL,NULL,2,29,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(45,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',19,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,31,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(46,2,'Tool for displaying HTML content including external sources such as images and other media.','Informative Tools','Displays formatted text and links to external sources on a read only page.',190,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,32,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(47,4,'Tool for displaying HTML content including external sources such as images and other media.','Fancy Noticeboard','Displays formatted text and links to external sources on a read only page.',384,34,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,33,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(48,6,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',19,99,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,6,34,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(49,8,'Which activity type would you like to learn about next?  Select an option below then click CHOOSE.  You can come back to this screen and select a different activity type to learn about if you wish.','Activity Types - Choose One',NULL,25,191,NULL,NULL,13,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(50,40,'Tool for displaying HTML content including external sources such as images and other media.','Winding Up','Displays formatted text and links to external sources on a read only page.',268,470,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,35,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(51,9,NULL,'Assessment - Collect Learner Responses',NULL,4,48,49,8,8,2,0,NULL,NULL,1,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,52,NULL,NULL,NULL,NULL,0),(52,13,'Tool for displaying HTML content including external sources such as images and other media.','Assessment Activities','Displays formatted text and links to external sources on a read only page.',5,5,51,9,1,2,0,NULL,NULL,1,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,36,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(53,64,'Spreadsheet.','Spreadsheet','Spreadsheet Tool.',219,107,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,13,37,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(54,74,'Tool for displaying HTML content including external sources such as images and other media.','Dimdim Discussion','Displays formatted text and links to external sources on a read only page.',56,472,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,38,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(55,10,NULL,'Collaborative - Get Learners to Work Together',NULL,4,105,49,8,8,2,0,NULL,NULL,2,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,71,NULL,NULL,NULL,NULL,0),(56,16,'Creates automated assessment questions. e.g. Multiple choice and true/false questions. Can provide feedback and scores.','Demo Multiple Choice','Learner answers a series of automated assessment questions. e.g. Multiple choice and true/false questions. Optional features include feedback on each question and scoring. Questions are weighted for scoring.',65,5,51,9,1,2,0,NULL,NULL,2,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,16,42,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(57,22,'Tool for displaying HTML content including external sources such as images and other media.','Split-Screen Activities','Displays formatted text and links to external sources on a read only page.',65,5,55,10,1,2,0,NULL,NULL,2,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,43,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(58,12,NULL,'Reflective - \'Capture\' Students Learning',NULL,4,162,49,8,8,2,0,NULL,NULL,3,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,70,NULL,NULL,NULL,NULL,0),(59,30,'Tool to create Surveys','Survey','Answer surveys.',124,5,58,12,1,2,0,NULL,NULL,3,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,9,46,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(60,18,'Learners submit files for assessment by the teacher. Scores and comments may be exported as a spreadsheet.','Demo Submit Files','Learners submit files for assessment by the teacher. Scores and comments for each learner are recorded and may be exported as a spreadsheet.',124,5,51,9,1,2,0,NULL,NULL,3,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,4,47,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(61,60,'Combined Share Resources and Forum ','Resources and Forum','The top window has a Share Resources area and the bottom window has a forum for learners to discuss items they have view via the Share Resources area.',124,5,55,10,6,2,0,NULL,NULL,3,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(62,32,'Allows voting format','Voting','Voting help text',184,5,58,12,1,2,0,NULL,NULL,4,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,7,48,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(63,77,'Tool for displaying HTML content including external sources such as images and other media.','New Collaboratives','Displays formatted text and links to external sources on a read only page.',184,5,55,10,1,2,0,NULL,NULL,4,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,49,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(64,36,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',243,5,58,12,1,2,0,NULL,NULL,5,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,50,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(65,67,'Google Mapping Tool','Gmap','Gmap for marking world map points',243,5,55,10,1,2,0,NULL,NULL,5,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,12,51,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(66,34,'Notebook Tool','Notebook','Notebook for notes and reflections',303,5,58,12,1,2,0,NULL,NULL,6,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,8,52,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(67,69,'Wiki Tool','Wiki','Wiki tool for creating wiki pages',303,5,55,10,1,2,0,NULL,NULL,6,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,15,53,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(68,83,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',362,5,58,12,1,2,0,NULL,NULL,7,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,54,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(69,71,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',422,5,58,12,1,2,0,NULL,NULL,8,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,14,55,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(70,15,'Tool for displaying HTML content including external sources such as images and other media.','Reflective Intro','Displays formatted text and links to external sources on a read only page.',5,5,58,12,1,2,0,NULL,NULL,1,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,39,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(71,14,'Tool for displaying HTML content including external sources such as images and other media.','Collaborative Intro','Displays formatted text and links to external sources on a read only page.',5,5,55,10,1,2,0,NULL,NULL,1,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,2,40,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(72,61,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',8,45,61,60,1,2,0,NULL,NULL,1,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,6,41,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(73,28,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',65,5,58,12,1,2,0,NULL,NULL,2,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,3,44,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(74,62,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',8,108,61,60,1,2,0,NULL,NULL,2,0,3,NULL,'2008-12-03 14:44:58',0,NULL,NULL,NULL,1,45,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(75,21,'Sharing resource with others.','Effective Learning','Uploading your resources to share with others.',227,47,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2008-12-03 14:45:17',0,NULL,NULL,NULL,6,56,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(76,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',40,51,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2008-12-03 14:45:17',0,NULL,NULL,NULL,2,57,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(77,8,'Tool for displaying HTML content including external sources such as images and other media.','Visual Authoring','Displays formatted text and links to external sources on a read only page.',33,166,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2008-12-03 14:45:17',0,NULL,NULL,NULL,2,59,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(78,10,'Sharing resource with others.','Authoring Environment','Uploading your resources to share with others.',235,171,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2008-12-03 14:45:17',0,NULL,NULL,NULL,6,60,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(79,12,'Tool for displaying HTML content including external sources such as images and other media.','Summing Up','Displays formatted text and links to external sources on a read only page.',425,170,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2008-12-03 14:45:17',0,NULL,NULL,NULL,2,58,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(80,23,'Sharing resource with others.','NoFlash Wink','Uploading your resources to share with others.',124,5,88,5,1,2,0,NULL,NULL,3,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,6,66,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(81,13,'Sharing resource with others.','Learner Wink','Uploading your resources to share with others.',65,5,88,5,1,2,0,NULL,NULL,2,0,5,NULL,'2008-12-03 14:45:27',0,NULL,NULL,NULL,6,68,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(82,4,'Choose one of the four options below to learn more about that particular aspect of LAMS.','Optional Sequences',NULL,254,100,NULL,NULL,13,2,0,NULL,NULL,NULL,0,5,NULL,'2008-12-03 14:45:26',0,NULL,1,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(83,1,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',46,213,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2008-12-03 14:45:27',0,NULL,NULL,NULL,2,69,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(84,20,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',5,5,88,5,1,2,0,NULL,NULL,1,0,5,NULL,'2008-12-03 14:45:27',0,NULL,NULL,NULL,2,67,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(85,25,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',558,210,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,2,61,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(86,50,'Tool for displaying HTML content including external sources such as images and other media.','Structure of LAMS','Displays formatted text and links to external sources on a read only page.',65,5,92,8,1,2,0,NULL,NULL,2,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,2,64,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(87,15,'Sharing resource with others.','Monitor Wink','Uploading your resources to share with others.',5,5,92,8,1,2,0,NULL,NULL,1,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,6,63,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(88,5,NULL,'The Learning Environment (Participate in a Lesson)',NULL,4,48,82,4,8,2,0,NULL,NULL,1,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,84,NULL,NULL,NULL,NULL,0),(89,14,'Sharing resource with others.','Author Wink','Uploading your resources to share with others.',5,5,91,6,1,2,0,NULL,NULL,1,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,6,65,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(90,17,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',5,5,93,16,1,2,0,NULL,NULL,1,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,6,62,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(91,6,NULL,'The Authoring Environment (Create a Lesson)',NULL,4,105,82,4,8,2,0,NULL,NULL,2,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,89,NULL,NULL,NULL,NULL,0),(92,8,NULL,'The Monitoring Environment (Teach a Lesson)',NULL,4,162,82,4,8,2,0,NULL,NULL,3,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,87,NULL,NULL,NULL,NULL,0),(93,16,NULL,'System Administration [Online Link]',NULL,4,219,82,4,8,2,0,NULL,NULL,4,0,5,NULL,'2008-12-03 14:45:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,90,NULL,NULL,NULL,NULL,0),(94,16,'Creates automated assessment questions. e.g. Multiple choice and true/false questions. Can provide feedback and scores.','Demo Multiple Choice','Learner answers a series of automated assessment questions. e.g. Multiple choice and true/false questions. Optional features include feedback on each question and scoring. Questions are weighted for scoring.',65,5,119,9,1,2,0,NULL,NULL,2,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,16,83,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(95,13,'Tool for displaying HTML content including external sources such as images and other media.','Assessment Activities','Displays formatted text and links to external sources on a read only page.',5,5,119,9,1,2,0,NULL,NULL,1,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,85,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(96,64,'Spreadsheet.','Spreadsheet','Spreadsheet Tool.',219,107,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,13,75,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(97,6,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',19,99,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,6,73,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(98,40,'Tool for displaying HTML content including external sources such as images and other media.','Winding Up','Displays formatted text and links to external sources on a read only page.',268,470,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,71,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(99,74,'Tool for displaying HTML content including external sources such as images and other media.','Dimdim Discussion','Displays formatted text and links to external sources on a read only page.',56,472,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,74,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(100,8,'Which activity type would you like to learn about next?  Select an option below then click CHOOSE.  You can come back to this screen and select a different activity type to learn about if you wish.','Activity Types - Choose One',NULL,25,191,NULL,NULL,13,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(101,28,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',65,5,123,12,1,2,0,NULL,NULL,2,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,3,93,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(102,15,'Tool for displaying HTML content including external sources such as images and other media.','Reflective Intro','Displays formatted text and links to external sources on a read only page.',5,5,123,12,1,2,0,NULL,NULL,1,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,87,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(103,83,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',362,5,123,12,1,2,0,NULL,NULL,7,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,91,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(104,34,'Notebook Tool','Notebook','Notebook for notes and reflections',303,5,123,12,1,2,0,NULL,NULL,6,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,8,92,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(105,60,'Combined Share Resources and Forum ','Resources and Forum','The top window has a Share Resources area and the bottom window has a forum for learners to discuss items they have view via the Share Resources area.',124,5,121,10,6,2,0,NULL,NULL,3,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(106,22,'Tool for displaying HTML content including external sources such as images and other media.','Split-Screen Activities','Displays formatted text and links to external sources on a read only page.',65,5,121,10,1,2,0,NULL,NULL,2,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,79,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(107,77,'Tool for displaying HTML content including external sources such as images and other media.','New Collaboratives','Displays formatted text and links to external sources on a read only page.',184,5,121,10,1,2,0,NULL,NULL,4,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,80,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(108,2,'Tool for displaying HTML content including external sources such as images and other media.','Informative Tools','Displays formatted text and links to external sources on a read only page.',190,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,72,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(109,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',19,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,94,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(110,4,'Tool for displaying HTML content including external sources such as images and other media.','Fancy Noticeboard','Displays formatted text and links to external sources on a read only page.',384,34,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,70,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(111,71,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',422,5,123,12,1,2,0,NULL,NULL,8,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,14,86,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(112,69,'Wiki Tool','Wiki','Wiki tool for creating wiki pages',303,5,121,10,1,2,0,NULL,NULL,6,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,15,76,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(113,67,'Google Mapping Tool','Gmap','Gmap for marking world map points',243,5,121,10,1,2,0,NULL,NULL,5,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,12,82,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(114,36,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',243,5,123,12,1,2,0,NULL,NULL,5,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,89,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(115,32,'Allows voting format','Voting','Voting help text',184,5,123,12,1,2,0,NULL,NULL,4,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,7,88,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(116,30,'Tool to create Surveys','Survey','Answer surveys.',124,5,123,12,1,2,0,NULL,NULL,3,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,9,90,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(117,18,'Learners submit files for assessment by the teacher. Scores and comments may be exported as a spreadsheet.','Demo Submit Files','Learners submit files for assessment by the teacher. Scores and comments for each learner are recorded and may be exported as a spreadsheet.',124,5,119,9,1,2,0,NULL,NULL,3,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,4,84,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(118,14,'Tool for displaying HTML content including external sources such as images and other media.','Collaborative Intro','Displays formatted text and links to external sources on a read only page.',5,5,121,10,1,2,0,NULL,NULL,1,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,2,81,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(119,9,NULL,'Assessment - Collect Learner Responses',NULL,4,48,100,8,8,2,0,NULL,NULL,1,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,95,NULL,NULL,NULL,NULL,0),(120,61,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',8,45,105,60,1,2,0,NULL,NULL,1,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,6,77,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(121,10,NULL,'Collaborative - Get Learners to Work Together',NULL,4,105,100,8,8,2,0,NULL,NULL,2,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,118,NULL,NULL,NULL,NULL,0),(122,62,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',8,108,105,60,1,2,0,NULL,NULL,2,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,1,78,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(123,12,NULL,'Reflective - \'Capture\' Students Learning',NULL,4,162,100,8,8,2,0,NULL,NULL,3,0,6,NULL,'2008-12-03 14:45:35',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,102,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `lams_learning_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_activity_type`
--

DROP TABLE IF EXISTS `lams_learning_activity_type`;
CREATE TABLE `lams_learning_activity_type` (
  `learning_activity_type_id` int(11) NOT NULL default '0',
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`learning_activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learning_activity_type`
--

LOCK TABLES `lams_learning_activity_type` WRITE;
/*!40000 ALTER TABLE `lams_learning_activity_type` DISABLE KEYS */;
INSERT INTO `lams_learning_activity_type` VALUES (1,'TOOL'),(2,'GROUPING'),(3,'GATE_SYNCH'),(4,'GATE_SCHEDULE'),(5,'GATE_PERMISSION'),(6,'PARALLEL'),(7,'OPTIONS'),(8,'SEQUENCE'),(9,'GATE_SYSTEM'),(10,'BRANCHING_CHOSEN'),(11,'BRANCHING_GROUP'),(12,'BRANCHING_TOOL'),(13,'OPTIONS_WITH_SEQUENCES'),(14,'GATE_CONDITION');
/*!40000 ALTER TABLE `lams_learning_activity_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_design`
--

DROP TABLE IF EXISTS `lams_learning_design`;
CREATE TABLE `lams_learning_design` (
  `learning_design_id` bigint(20) NOT NULL auto_increment,
  `learning_design_ui_id` int(11) default NULL,
  `description` text,
  `title` varchar(255) default NULL,
  `first_activity_id` bigint(20) default NULL,
  `max_id` int(11) default NULL,
  `valid_design_flag` tinyint(4) NOT NULL,
  `read_only_flag` tinyint(4) NOT NULL,
  `date_read_only` datetime default NULL,
  `user_id` bigint(20) NOT NULL,
  `help_text` text,
  `online_instructions` text,
  `offline_instructions` text,
  `copy_type_id` tinyint(4) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `version` varchar(56) default NULL,
  `original_learning_design_id` bigint(20) default NULL,
  `workspace_folder_id` bigint(20) default NULL,
  `duration` bigint(38) default NULL,
  `license_id` bigint(20) default NULL,
  `license_text` text,
  `last_modified_date_time` datetime default NULL,
  `content_folder_id` varchar(32) default NULL,
  `edit_override_lock` tinyint(4) default '0',
  `edit_override_user_id` bigint(20) default NULL,
  `design_version` int(11) default '1',
  PRIMARY KEY  (`learning_design_id`),
  KEY `user_id` (`user_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `license_id` (`license_id`),
  KEY `copy_type_id` (`copy_type_id`),
  KEY `edit_override_user_id` (`edit_override_user_id`),
  KEY `idx_design_parent_id` (`original_learning_design_id`),
  KEY `idx_design_first_act` (`first_activity_id`),
  CONSTRAINT `FK_lams_learning_design_3` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learning_design_4` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_learning_design_5` FOREIGN KEY (`license_id`) REFERENCES `lams_license` (`license_id`),
  CONSTRAINT `FK_lams_learning_design_6` FOREIGN KEY (`copy_type_id`) REFERENCES `lams_copy_type` (`copy_type_id`),
  CONSTRAINT `FK_lams_learning_design_7` FOREIGN KEY (`edit_override_user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learning_design`
--

LOCK TABLES `lams_learning_design` WRITE;
/*!40000 ALTER TABLE `lams_learning_design` DISABLE KEYS */;
INSERT INTO `lams_learning_design` VALUES (1,NULL,'Lesson 1 in the LAMS101 Course.\n\nDescribes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','Lesson 1 - What Is LAMS',26,27,1,0,NULL,5,NULL,NULL,NULL,1,'2008-12-03 14:44:36','2.0.100.200803121637',NULL,5,NULL,1,NULL,'2008-12-03 14:44:36','4028efbb18edfbb70118ee3b3b6d00b8',0,NULL,1),(2,NULL,'Second lesson in the LAMS101 course.\nDescribes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','Lesson 2 - LAMS Environments',31,53,1,0,NULL,5,NULL,NULL,NULL,1,'2008-12-03 14:44:43','2.0.100.200803121637',NULL,5,NULL,1,NULL,'2008-12-03 14:44:43','4028efbb18edfbb70118ee6bacc700d2',0,NULL,1),(3,NULL,'Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2\n\nv20081411','Lesson 3 - LAMS Activities',45,86,1,0,NULL,5,NULL,NULL,NULL,1,'2008-12-03 14:44:58','2.1.0.200811141019',NULL,5,NULL,1,NULL,'2008-12-03 14:44:58','4028efbb1935b8d301193606f5000051',0,NULL,1),(4,NULL,'Lesson 1 in the LAMS101 Course.\n\nDescribes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','Lesson 1 - What Is LAMS',76,27,1,1,NULL,5,NULL,NULL,NULL,2,'2008-12-03 14:45:17','2.0.100.200803121637',1,22,NULL,1,NULL,'2008-12-03 14:45:17','4028efbb18edfbb70118ee3b3b6d00b8',0,NULL,1),(5,NULL,'Second lesson in the LAMS101 course.\nDescribes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','Lesson 2 - LAMS Environments',83,53,1,1,NULL,5,NULL,NULL,NULL,2,'2008-12-03 14:45:26','2.0.100.200803121637',2,22,NULL,1,NULL,'2008-12-03 14:45:26','4028efbb18edfbb70118ee6bacc700d2',0,NULL,1),(6,NULL,'Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2\n\nv20081411','Lesson 3 - LAMS Activities',109,86,1,1,NULL,5,NULL,NULL,NULL,2,'2008-12-03 14:45:35','2.1.0.200811141019',3,22,NULL,1,NULL,'2008-12-03 14:45:35','4028efbb1935b8d301193606f5000051',0,NULL,1);
/*!40000 ALTER TABLE `lams_learning_design` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_library`
--

DROP TABLE IF EXISTS `lams_learning_library`;
CREATE TABLE `lams_learning_library` (
  `learning_library_id` bigint(20) NOT NULL auto_increment,
  `description` text,
  `title` varchar(255) default NULL,
  `valid_flag` tinyint(1) NOT NULL default '1',
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY  (`learning_library_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learning_library`
--

LOCK TABLES `lams_learning_library` WRITE;
/*!40000 ALTER TABLE `lams_learning_library` DISABLE KEYS */;
INSERT INTO `lams_learning_library` VALUES (1,'Forum, also known Message Board','Forum',1,'2008-12-03 14:38:40'),(2,'Displays a Noticeboard','Noticeboard',1,'2008-12-03 14:38:46'),(3,'Question and Answer Learning Library Description','Question and Answer',1,'2008-12-03 14:38:51'),(4,'Uploading of files by learners, for review by teachers.','Submit file',1,'2008-12-03 14:38:57'),(5,'Chat Tool','Chat',1,'2008-12-03 14:39:05'),(6,'Share resources','Share resources',1,'2008-12-03 14:39:14'),(7,'Voting Learning Library Description','Voting',1,'2008-12-03 14:39:18'),(8,'Notebook Tool','Notebook',1,'2008-12-03 14:39:26'),(9,'Survey','Survey',1,'2008-12-03 14:39:34'),(10,'Scribe Tool','Scribe',0,'2008-12-03 14:39:43'),(11,'Share taskList','Share taskList',1,'2008-12-03 14:39:52'),(12,'Gmap Tool','Gmap',1,'2008-12-03 14:40:01'),(13,'Spreadsheet Tool','Spreadsheet',1,'2008-12-03 14:40:09'),(14,'Collecting data with custom structure.','Data Collection',1,'2008-12-03 14:40:17'),(15,'Wiki Tool','Wiki',1,'2008-12-03 14:40:25'),(16,'MCQ Learning Library Description','MCQ',1,'2008-12-03 14:40:30'),(17,'Shared Resources and Forum','Resources and Forum',1,'2008-12-03 14:40:32'),(18,'Chat and Scribe','Chat and Scribe',1,'2008-12-03 14:40:33'),(19,'Forum and Scribe','Forum and Scribe',1,'2008-12-03 14:40:34');
/*!40000 ALTER TABLE `lams_learning_library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_transition`
--

DROP TABLE IF EXISTS `lams_learning_transition`;
CREATE TABLE `lams_learning_transition` (
  `transition_id` bigint(20) NOT NULL auto_increment,
  `transition_ui_id` int(11) default NULL,
  `description` text,
  `title` varchar(255) default NULL,
  `to_activity_id` bigint(20) NOT NULL,
  `from_activity_id` bigint(20) NOT NULL,
  `learning_design_id` bigint(20) NOT NULL default '0',
  `create_date_time` datetime NOT NULL,
  `to_ui_id` int(11) default NULL,
  `from_ui_id` int(11) default NULL,
  PRIMARY KEY  (`transition_id`),
  UNIQUE KEY `UQ_transition_activities` (`from_activity_id`,`to_activity_id`),
  KEY `from_activity_id` (`from_activity_id`),
  KEY `to_activity_id` (`to_activity_id`),
  KEY `learning_design_id` (`learning_design_id`),
  CONSTRAINT `FK_learning_transition_3` FOREIGN KEY (`from_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_learning_transition_2` FOREIGN KEY (`to_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lddefn_transition_ibfk_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_learning_transition`
--

LOCK TABLES `lams_learning_transition` WRITE;
/*!40000 ALTER TABLE `lams_learning_transition` DISABLE KEYS */;
INSERT INTO `lams_learning_transition` VALUES (1,11,NULL,NULL,28,27,1,'2008-12-03 14:44:36',10,8),(2,13,NULL,NULL,29,28,1,'2008-12-03 14:44:36',12,10),(3,27,NULL,NULL,27,30,1,'2008-12-03 14:44:36',8,21),(4,23,NULL,NULL,30,26,1,'2008-12-03 14:44:36',21,1),(5,22,NULL,NULL,36,34,2,'2008-12-03 14:44:43',13,20),(6,24,NULL,NULL,39,36,2,'2008-12-03 14:44:43',23,13),(7,53,NULL,NULL,44,41,2,'2008-12-03 14:44:43',50,15),(8,7,NULL,NULL,32,31,2,'2008-12-03 14:44:43',4,1),(9,26,NULL,NULL,35,32,2,'2008-12-03 14:44:43',25,4),(10,65,NULL,NULL,53,48,3,'2008-12-03 14:44:58',64,6),(11,63,NULL,NULL,61,57,3,'2008-12-03 14:44:58',60,22),(12,82,NULL,NULL,67,65,3,'2008-12-03 14:44:58',69,67),(13,38,NULL,NULL,64,62,3,'2008-12-03 14:44:58',36,32),(14,85,NULL,NULL,68,66,3,'2008-12-03 14:44:58',83,34),(15,75,NULL,NULL,50,54,3,'2008-12-03 14:44:58',40,74),(16,42,NULL,NULL,57,71,3,'2008-12-03 14:44:58',22,14),(17,66,NULL,NULL,49,53,3,'2008-12-03 14:44:58',8,64),(18,33,NULL,NULL,62,59,3,'2008-12-03 14:44:58',32,30),(19,79,NULL,NULL,63,61,3,'2008-12-03 14:44:58',77,60),(20,86,NULL,NULL,69,68,3,'2008-12-03 14:44:58',71,83),(21,31,NULL,NULL,59,73,3,'2008-12-03 14:44:58',30,28),(22,19,NULL,NULL,60,56,3,'2008-12-03 14:44:58',18,16),(23,76,NULL,NULL,54,49,3,'2008-12-03 14:44:58',74,8),(24,5,NULL,NULL,47,46,3,'2008-12-03 14:44:58',4,2),(25,7,NULL,NULL,48,47,3,'2008-12-03 14:44:58',6,4),(26,80,NULL,NULL,65,63,3,'2008-12-03 14:44:58',67,77),(27,29,NULL,NULL,73,70,3,'2008-12-03 14:44:58',28,15),(28,39,NULL,NULL,66,64,3,'2008-12-03 14:44:58',34,36),(29,3,NULL,NULL,46,45,3,'2008-12-03 14:44:58',2,1),(30,17,NULL,NULL,56,52,3,'2008-12-03 14:44:58',16,13),(31,23,NULL,NULL,75,76,4,'2008-12-03 14:45:17',21,1),(32,27,NULL,NULL,77,75,4,'2008-12-03 14:45:18',8,21),(33,11,NULL,NULL,78,77,4,'2008-12-03 14:45:18',10,8),(34,13,NULL,NULL,79,78,4,'2008-12-03 14:45:18',12,10),(35,24,NULL,NULL,80,81,5,'2008-12-03 14:45:27',23,13),(36,7,NULL,NULL,82,83,5,'2008-12-03 14:45:27',4,1),(37,22,NULL,NULL,81,84,5,'2008-12-03 14:45:27',13,20),(38,26,NULL,NULL,85,82,5,'2008-12-03 14:45:27',25,4),(39,53,NULL,NULL,86,87,5,'2008-12-03 14:45:27',50,15),(40,17,NULL,NULL,94,95,6,'2008-12-03 14:45:36',16,13),(41,65,NULL,NULL,96,97,6,'2008-12-03 14:45:36',64,6),(42,75,NULL,NULL,98,99,6,'2008-12-03 14:45:36',40,74),(43,76,NULL,NULL,99,100,6,'2008-12-03 14:45:36',74,8),(44,29,NULL,NULL,101,102,6,'2008-12-03 14:45:36',28,15),(45,85,NULL,NULL,103,104,6,'2008-12-03 14:45:36',83,34),(46,63,NULL,NULL,105,106,6,'2008-12-03 14:45:36',60,22),(47,79,NULL,NULL,107,105,6,'2008-12-03 14:45:36',77,60),(48,3,NULL,NULL,108,109,6,'2008-12-03 14:45:36',2,1),(49,5,NULL,NULL,110,108,6,'2008-12-03 14:45:36',4,2),(50,86,NULL,NULL,111,103,6,'2008-12-03 14:45:36',71,83),(51,82,NULL,NULL,112,113,6,'2008-12-03 14:45:36',69,67),(52,39,NULL,NULL,104,114,6,'2008-12-03 14:45:36',34,36),(53,7,NULL,NULL,97,110,6,'2008-12-03 14:45:36',6,4),(54,33,NULL,NULL,115,116,6,'2008-12-03 14:45:36',32,30),(55,38,NULL,NULL,114,115,6,'2008-12-03 14:45:36',36,32),(56,31,NULL,NULL,116,101,6,'2008-12-03 14:45:36',30,28),(57,66,NULL,NULL,100,96,6,'2008-12-03 14:45:36',8,64),(58,19,NULL,NULL,117,94,6,'2008-12-03 14:45:36',18,16),(59,80,NULL,NULL,113,107,6,'2008-12-03 14:45:36',67,77),(60,42,NULL,NULL,106,118,6,'2008-12-03 14:45:36',22,14);
/*!40000 ALTER TABLE `lams_learning_transition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson`
--

DROP TABLE IF EXISTS `lams_lesson`;
CREATE TABLE `lams_lesson` (
  `lesson_id` bigint(20) NOT NULL auto_increment,
  `learning_design_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `create_date_time` datetime NOT NULL,
  `organisation_id` bigint(20) default NULL,
  `class_grouping_id` bigint(20) default NULL,
  `lesson_state_id` int(3) NOT NULL,
  `start_date_time` datetime default NULL,
  `schedule_start_date_time` datetime default NULL,
  `end_date_time` datetime default NULL,
  `schedule_end_date_time` datetime default NULL,
  `previous_state_id` int(3) default NULL,
  `learner_exportport_avail` tinyint(1) default '1',
  `learner_presence_avail` tinyint(1) default '0',
  `learner_im_avail` tinyint(1) default '0',
  `live_edit_enabled` tinyint(1) default '0',
  `locked_for_edit` tinyint(4) default '0',
  `version` int(11) default '1',
  PRIMARY KEY  (`lesson_id`),
  KEY `learning_design_id` (`learning_design_id`),
  KEY `user_id` (`user_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `lesson_state_id` (`lesson_state_id`),
  KEY `class_grouping_id` (`class_grouping_id`),
  CONSTRAINT `FK_lams_lesson_1_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`),
  CONSTRAINT `FK_lams_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_lesson_3` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`),
  CONSTRAINT `FK_lams_lesson_4` FOREIGN KEY (`lesson_state_id`) REFERENCES `lams_lesson_state` (`lesson_state_id`),
  CONSTRAINT `FK_lams_lesson_5` FOREIGN KEY (`class_grouping_id`) REFERENCES `lams_grouping` (`grouping_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_lesson`
--

LOCK TABLES `lams_lesson` WRITE;
/*!40000 ALTER TABLE `lams_lesson` DISABLE KEYS */;
INSERT INTO `lams_lesson` VALUES (1,4,5,'Lesson 1 - What Is LAMS','Lesson 1 in the LAMS101 Course.\n\nDescribes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','2008-12-03 14:45:18',3,1,3,'2008-12-03 14:45:18',NULL,NULL,NULL,NULL,1,0,0,1,0,2),(2,5,5,'Lesson 2 - LAMS Environments','Second lesson in the LAMS101 course.\nDescribes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','2008-12-03 14:45:27',3,2,3,'2008-12-03 14:45:27',NULL,NULL,NULL,NULL,1,0,0,1,0,2),(3,6,5,'Lesson 3 - LAMS Activities','Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2\n\nv20081411','2008-12-03 14:45:36',3,3,3,'2008-12-03 14:45:36',NULL,NULL,NULL,NULL,1,0,0,1,0,2);
/*!40000 ALTER TABLE `lams_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson_learner`
--

DROP TABLE IF EXISTS `lams_lesson_learner`;
CREATE TABLE `lams_lesson_learner` (
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `lesson_id` (`lesson_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_lesson_learner_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_lesson_learner_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_lesson_learner`
--

LOCK TABLES `lams_lesson_learner` WRITE;
/*!40000 ALTER TABLE `lams_lesson_learner` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_lesson_learner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson_state`
--

DROP TABLE IF EXISTS `lams_lesson_state`;
CREATE TABLE `lams_lesson_state` (
  `lesson_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`lesson_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_lesson_state`
--

LOCK TABLES `lams_lesson_state` WRITE;
/*!40000 ALTER TABLE `lams_lesson_state` DISABLE KEYS */;
INSERT INTO `lams_lesson_state` VALUES (1,'CREATED'),(2,'NOT_STARTED'),(3,'STARTED'),(4,'SUSPENDED'),(5,'FINISHED'),(6,'ARCHIVED'),(7,'REMOVED');
/*!40000 ALTER TABLE `lams_lesson_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_license`
--

DROP TABLE IF EXISTS `lams_license`;
CREATE TABLE `lams_license` (
  `license_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `code` varchar(20) NOT NULL,
  `url` varchar(256) default NULL,
  `default_flag` tinyint(1) NOT NULL default '0',
  `picture_url` varchar(256) default NULL,
  PRIMARY KEY  (`license_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_license`
--

LOCK TABLES `lams_license` WRITE;
/*!40000 ALTER TABLE `lams_license` DISABLE KEYS */;
INSERT INTO `lams_license` VALUES (1,'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 2.5','by-nc-sa','http://creativecommons.org/licenses/by-nc-sa/2.5/',1,'/images/license/byncsa.jpg'),(2,'CC Attribution-No Derivatives 2.5','by-nd','http://creativecommons.org/licenses/by-nd/2.5/',0,'/images/license/bynd.jpg'),(3,'CC Attribution-Noncommercial-No Derivatives 2.5','by-nc-nd','http://creativecommons.org/licenses/by-nc-nd/2.5/',0,'/images/license/byncnd.jpg'),(4,'CC Attribution-Noncommercial 2.5','by-nc','http://creativecommons.org/licenses/by-nc/2.5/',0,'/images/license/bync.jpg'),(5,'CC Attribution-ShareAlike 2.5','by-sa','http://creativecommons.org/licenses/by-sa/2.5/',0,'/images/license/byncsa.jpg'),(6,'Other Licensing Agreement','other','',0,'');
/*!40000 ALTER TABLE `lams_license` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_log_event`
--

DROP TABLE IF EXISTS `lams_log_event`;
CREATE TABLE `lams_log_event` (
  `log_event_id` bigint(20) NOT NULL auto_increment,
  `log_event_type_id` int(5) NOT NULL,
  `user_id` bigint(20) default NULL,
  `timestamp` datetime NOT NULL,
  `ref_id` bigint(20) default NULL,
  `message` varchar(255) default NULL,
  PRIMARY KEY  (`log_event_id`),
  KEY `log_event_type_id` (`log_event_type_id`),
  CONSTRAINT `FK_lams_event_log_1` FOREIGN KEY (`log_event_type_id`) REFERENCES `lams_log_event_type` (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_log_event`
--

LOCK TABLES `lams_log_event` WRITE;
/*!40000 ALTER TABLE `lams_log_event` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_log_event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_log_event_type`
--

DROP TABLE IF EXISTS `lams_log_event_type`;
CREATE TABLE `lams_log_event_type` (
  `log_event_type_id` int(5) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_log_event_type`
--

LOCK TABLES `lams_log_event_type` WRITE;
/*!40000 ALTER TABLE `lams_log_event_type` DISABLE KEYS */;
INSERT INTO `lams_log_event_type` VALUES (1,'LEARNER_LESSON_START'),(2,'LEARNER_LESSON_FINISH'),(3,'LEARNER_LESSON_EXIT'),(4,'LEARNER_LESSON_RESUME'),(5,'LEARNER_ACTIVITY_START'),(6,'LEARNER_ACTIVITY_FINISH');
/*!40000 ALTER TABLE `lams_log_event_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_notebook_entry`
--

DROP TABLE IF EXISTS `lams_notebook_entry`;
CREATE TABLE `lams_notebook_entry` (
  `uid` bigint(20) NOT NULL auto_increment,
  `external_id` bigint(20) default NULL,
  `external_id_type` int(11) default NULL,
  `external_signature` varchar(255) default NULL,
  `user_id` int(11) default NULL,
  `title` varchar(255) default NULL,
  `entry` text,
  `create_date` datetime default NULL,
  `last_modified` datetime default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_notebook_entry`
--

LOCK TABLES `lams_notebook_entry` WRITE;
/*!40000 ALTER TABLE `lams_notebook_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_notebook_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_organisation`
--

DROP TABLE IF EXISTS `lams_organisation`;
CREATE TABLE `lams_organisation` (
  `organisation_id` bigint(20) NOT NULL auto_increment,
  `name` varchar(250) NOT NULL,
  `code` varchar(20) default NULL,
  `description` varchar(250) default NULL,
  `parent_organisation_id` bigint(20) default NULL,
  `organisation_type_id` int(3) NOT NULL default '0',
  `create_date` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `workspace_id` bigint(20) default NULL,
  `organisation_state_id` int(3) NOT NULL,
  `admin_add_new_users` tinyint(1) NOT NULL default '0',
  `admin_browse_all_users` tinyint(1) NOT NULL default '0',
  `admin_change_status` tinyint(1) NOT NULL default '0',
  `admin_create_guest` tinyint(1) NOT NULL default '0',
  `locale_id` int(11) default NULL,
  `archived_date` datetime default NULL,
  `ordered_lesson_ids` text,
  PRIMARY KEY  (`organisation_id`),
  KEY `organisation_type_id` (`organisation_type_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `parent_organisation_id` (`parent_organisation_id`),
  KEY `organisation_state_id` (`organisation_state_id`),
  KEY `locale_id` (`locale_id`),
  CONSTRAINT `FK_lams_organisation_1` FOREIGN KEY (`organisation_type_id`) REFERENCES `lams_organisation_type` (`organisation_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_organisation_2` FOREIGN KEY (`workspace_id`) REFERENCES `lams_workspace` (`workspace_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_organisation_3` FOREIGN KEY (`parent_organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_organisation_4` FOREIGN KEY (`organisation_state_id`) REFERENCES `lams_organisation_state` (`organisation_state_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_organisation_5` FOREIGN KEY (`locale_id`) REFERENCES `lams_supported_locale` (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_organisation`
--

LOCK TABLES `lams_organisation` WRITE;
/*!40000 ALTER TABLE `lams_organisation` DISABLE KEYS */;
INSERT INTO `lams_organisation` VALUES (1,'Root',NULL,'Root Organisation',NULL,1,'2008-12-03 14:38:21',1,1,1,0,0,0,0,1,NULL,NULL),(2,'Playpen','PP101','Developers Playpen',1,2,'2008-12-03 14:38:21',1,2,1,0,0,0,0,1,NULL,NULL),(3,'Everybody',NULL,'All People In Course',2,3,'2008-12-03 14:38:21',1,NULL,1,0,0,0,0,1,NULL,'1,2,3'),(4,'Mathematics 1','MATH111','Mathematics 1',1,2,'2008-12-03 14:38:21',1,3,1,0,0,0,0,2,NULL,NULL),(5,'Tutorial Group A','TUTA','Tutorial Group A',4,3,'2008-12-03 14:38:21',1,NULL,1,0,0,0,0,2,NULL,NULL),(6,'Tutorial Group B','TUTB','Tutorial Group B',4,3,'2008-12-03 14:38:21',1,NULL,1,0,0,0,0,2,NULL,NULL),(7,'Moodle','Moodle','Moodle Test',1,2,'2008-12-03 14:38:21',1,50,2,0,0,0,0,1,NULL,NULL);
/*!40000 ALTER TABLE `lams_organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_organisation_state`
--

DROP TABLE IF EXISTS `lams_organisation_state`;
CREATE TABLE `lams_organisation_state` (
  `organisation_state_id` int(3) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`organisation_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_organisation_state`
--

LOCK TABLES `lams_organisation_state` WRITE;
/*!40000 ALTER TABLE `lams_organisation_state` DISABLE KEYS */;
INSERT INTO `lams_organisation_state` VALUES (1,'ACTIVE'),(2,'HIDDEN'),(3,'ARCHIVED'),(4,'REMOVED');
/*!40000 ALTER TABLE `lams_organisation_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_organisation_type`
--

DROP TABLE IF EXISTS `lams_organisation_type`;
CREATE TABLE `lams_organisation_type` (
  `organisation_type_id` int(3) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`organisation_type_id`),
  UNIQUE KEY `UQ_lams_organisation_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_organisation_type`
--

LOCK TABLES `lams_organisation_type` WRITE;
/*!40000 ALTER TABLE `lams_organisation_type` DISABLE KEYS */;
INSERT INTO `lams_organisation_type` VALUES (1,'ROOT ORGANISATION','root all other organisations: controlled by Sysadmin'),(2,'COURSE ORGANISATION','main organisation level - equivalent to an entire course.'),(3,'CLASS','runtime organisation level - lessons are run for classes.');
/*!40000 ALTER TABLE `lams_organisation_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_password_request`
--

DROP TABLE IF EXISTS `lams_password_request`;
CREATE TABLE `lams_password_request` (
  `request_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `request_key` varchar(32) NOT NULL,
  `request_date` datetime NOT NULL,
  PRIMARY KEY  (`request_id`),
  UNIQUE KEY `IX_lams_psswd_rqst_key` (`request_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_password_request`
--

LOCK TABLES `lams_password_request` WRITE;
/*!40000 ALTER TABLE `lams_password_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_password_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_privilege`
--

DROP TABLE IF EXISTS `lams_privilege`;
CREATE TABLE `lams_privilege` (
  `privilege_id` bigint(20) NOT NULL auto_increment,
  `code` varchar(10) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`privilege_id`),
  UNIQUE KEY `IX_lams_privilege_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_privilege`
--

LOCK TABLES `lams_privilege` WRITE;
/*!40000 ALTER TABLE `lams_privilege` DISABLE KEYS */;
INSERT INTO `lams_privilege` VALUES (1,'Z','do anything'),(2,'A','add/remove/modify classes within the course'),(3,'B','create running instances of sequences and assign those to a class'),(4,'C','stop/start running sequences'),(5,'D','monitor the progress of learners'),(6,'E','participates in sequences'),(7,'F','export their progress on each running sequence'),(8,'G','write/create/delete permissions in course content folder'),(9,'H','read course content folder'),(10,'I','create new users'),(11,'J','create guest users'),(12,'K','change status of course'),(13,'L','browse all users in the system'),(14,'M','write/create/delete permissions in all course content folders');
/*!40000 ALTER TABLE `lams_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_progress_attempted`
--

DROP TABLE IF EXISTS `lams_progress_attempted`;
CREATE TABLE `lams_progress_attempted` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_current_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_current_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_progress_attempted`
--

LOCK TABLES `lams_progress_attempted` WRITE;
/*!40000 ALTER TABLE `lams_progress_attempted` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_progress_attempted` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_progress_completed`
--

DROP TABLE IF EXISTS `lams_progress_completed`;
CREATE TABLE `lams_progress_completed` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_completed_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_completed_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_progress_completed`
--

LOCK TABLES `lams_progress_completed` WRITE;
/*!40000 ALTER TABLE `lams_progress_completed` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_progress_completed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_blob_triggers`
--

DROP TABLE IF EXISTS `lams_qtz_blob_triggers`;
CREATE TABLE `lams_qtz_blob_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_blob_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_blob_triggers`
--

LOCK TABLES `lams_qtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `lams_qtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_calendars`
--

DROP TABLE IF EXISTS `lams_qtz_calendars`;
CREATE TABLE `lams_qtz_calendars` (
  `CALENDAR_NAME` varchar(80) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY  (`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_calendars`
--

LOCK TABLES `lams_qtz_calendars` WRITE;
/*!40000 ALTER TABLE `lams_qtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_cron_triggers`
--

DROP TABLE IF EXISTS `lams_qtz_cron_triggers`;
CREATE TABLE `lams_qtz_cron_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `CRON_EXPRESSION` varchar(80) NOT NULL,
  `TIME_ZONE_ID` varchar(80) default NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_cron_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_cron_triggers`
--

LOCK TABLES `lams_qtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `lams_qtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_fired_triggers`
--

DROP TABLE IF EXISTS `lams_qtz_fired_triggers`;
CREATE TABLE `lams_qtz_fired_triggers` (
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(80) default NULL,
  `JOB_GROUP` varchar(80) default NULL,
  `IS_STATEFUL` varchar(1) default NULL,
  `REQUESTS_RECOVERY` varchar(1) default NULL,
  PRIMARY KEY  (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_fired_triggers`
--

LOCK TABLES `lams_qtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `lams_qtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_job_details`
--

DROP TABLE IF EXISTS `lams_qtz_job_details`;
CREATE TABLE `lams_qtz_job_details` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `DESCRIPTION` varchar(120) default NULL,
  `JOB_CLASS_NAME` varchar(128) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `IS_STATEFUL` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY  (`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_job_details`
--

LOCK TABLES `lams_qtz_job_details` WRITE;
/*!40000 ALTER TABLE `lams_qtz_job_details` DISABLE KEYS */;
INSERT INTO `lams_qtz_job_details` VALUES ('Resend Messages Job','DEFAULT','','org.lamsfoundation.lams.events.ResendMessagesJob','0','0','0','0',0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB0200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C770800000010000000007800);
/*!40000 ALTER TABLE `lams_qtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_job_listeners`
--

DROP TABLE IF EXISTS `lams_qtz_job_listeners`;
CREATE TABLE `lams_qtz_job_listeners` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `JOB_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY  (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`),
  CONSTRAINT `lams_qtz_job_listeners_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_job_listeners`
--

LOCK TABLES `lams_qtz_job_listeners` WRITE;
/*!40000 ALTER TABLE `lams_qtz_job_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_job_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_locks`
--

DROP TABLE IF EXISTS `lams_qtz_locks`;
CREATE TABLE `lams_qtz_locks` (
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY  (`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_locks`
--

LOCK TABLES `lams_qtz_locks` WRITE;
/*!40000 ALTER TABLE `lams_qtz_locks` DISABLE KEYS */;
INSERT INTO `lams_qtz_locks` VALUES ('CALENDAR_ACCESS'),('JOB_ACCESS'),('MISFIRE_ACCESS'),('STATE_ACCESS'),('TRIGGER_ACCESS');
/*!40000 ALTER TABLE `lams_qtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `lams_qtz_paused_trigger_grps`;
CREATE TABLE `lams_qtz_paused_trigger_grps` (
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  PRIMARY KEY  (`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_paused_trigger_grps`
--

LOCK TABLES `lams_qtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `lams_qtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_scheduler_state`
--

DROP TABLE IF EXISTS `lams_qtz_scheduler_state`;
CREATE TABLE `lams_qtz_scheduler_state` (
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  `RECOVERER` varchar(80) default NULL,
  PRIMARY KEY  (`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_scheduler_state`
--

LOCK TABLES `lams_qtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `lams_qtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_simple_triggers`
--

DROP TABLE IF EXISTS `lams_qtz_simple_triggers`;
CREATE TABLE `lams_qtz_simple_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(7) NOT NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_simple_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_simple_triggers`
--

LOCK TABLES `lams_qtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `lams_qtz_simple_triggers` DISABLE KEYS */;
INSERT INTO `lams_qtz_simple_triggers` VALUES ('Resend Messages Job trigger','DEFAULT',-1,3600000,1);
/*!40000 ALTER TABLE `lams_qtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_trigger_listeners`
--

DROP TABLE IF EXISTS `lams_qtz_trigger_listeners`;
CREATE TABLE `lams_qtz_trigger_listeners` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `TRIGGER_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`),
  CONSTRAINT `lams_qtz_trigger_listeners_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_trigger_listeners`
--

LOCK TABLES `lams_qtz_trigger_listeners` WRITE;
/*!40000 ALTER TABLE `lams_qtz_trigger_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_trigger_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_triggers`
--

DROP TABLE IF EXISTS `lams_qtz_triggers`;
CREATE TABLE `lams_qtz_triggers` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(120) default NULL,
  `NEXT_FIRE_TIME` bigint(13) default NULL,
  `PREV_FIRE_TIME` bigint(13) default NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) default NULL,
  `CALENDAR_NAME` varchar(80) default NULL,
  `MISFIRE_INSTR` smallint(2) default NULL,
  `JOB_DATA` blob,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `JOB_NAME` (`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `lams_qtz_triggers_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_qtz_triggers`
--

LOCK TABLES `lams_qtz_triggers` WRITE;
/*!40000 ALTER TABLE `lams_qtz_triggers` DISABLE KEYS */;
INSERT INTO `lams_qtz_triggers` VALUES ('Resend Messages Job trigger','DEFAULT','Resend Messages Job','DEFAULT','0',NULL,1228279288046,1228275688046,'WAITING','SIMPLE',1228275688046,0,NULL,0,NULL);
/*!40000 ALTER TABLE `lams_qtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_role`
--

DROP TABLE IF EXISTS `lams_role`;
CREATE TABLE `lams_role` (
  `role_id` int(6) NOT NULL default '0',
  `name` varchar(64) NOT NULL,
  `description` text,
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`role_id`),
  KEY `gname` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_role`
--

LOCK TABLES `lams_role` WRITE;
/*!40000 ALTER TABLE `lams_role` DISABLE KEYS */;
INSERT INTO `lams_role` VALUES (1,'SYSADMIN','LAMS System Adminstrator','2008-12-03 14:38:21'),(2,'GROUP MANAGER','Group Manager','2008-12-03 14:38:21'),(3,'AUTHOR','Authors Learning Designs','2008-12-03 14:38:21'),(4,'MONITOR','Member of Staff','2008-12-03 14:38:21'),(5,'LEARNER','Student','2008-12-03 14:38:21'),(6,'GROUP ADMIN','Group Administrator','2008-12-03 14:38:21'),(7,'AUTHOR ADMIN','Author Administrator','2008-12-03 14:38:21');
/*!40000 ALTER TABLE `lams_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_role_privilege`
--

DROP TABLE IF EXISTS `lams_role_privilege`;
CREATE TABLE `lams_role_privilege` (
  `rp_id` bigint(20) NOT NULL auto_increment,
  `role_id` int(6) NOT NULL,
  `privilege_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`rp_id`),
  KEY `privilege_id` (`privilege_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `FK_lams_role_privilege_1` FOREIGN KEY (`privilege_id`) REFERENCES `lams_privilege` (`privilege_id`),
  CONSTRAINT `FK_lams_role_privilege_2` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_role_privilege`
--

LOCK TABLES `lams_role_privilege` WRITE;
/*!40000 ALTER TABLE `lams_role_privilege` DISABLE KEYS */;
INSERT INTO `lams_role_privilege` VALUES (1,1,1),(2,2,2),(3,2,3),(4,2,4),(5,2,5),(6,2,8),(7,2,9),(8,2,10),(9,2,12),(10,2,13),(11,3,8),(12,3,9),(13,4,3),(14,4,4),(15,4,5),(16,5,6),(17,5,7),(18,6,2),(19,6,10),(20,6,12),(21,6,13),(22,7,14);
/*!40000 ALTER TABLE `lams_role_privilege` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_supported_locale`
--

DROP TABLE IF EXISTS `lams_supported_locale`;
CREATE TABLE `lams_supported_locale` (
  `locale_id` int(11) NOT NULL auto_increment,
  `language_iso_code` varchar(2) NOT NULL COMMENT 'ISO 639-1 Language Code (2 letter version) Java only supports 2 letter properly, not the 3 letter codes.',
  `country_iso_code` varchar(2) default NULL COMMENT 'ISO 3166 Country Code. Cannot use in unique key as allows null.',
  `description` varchar(255) NOT NULL,
  `direction` varchar(3) NOT NULL,
  `fckeditor_code` varchar(10) default NULL,
  PRIMARY KEY  (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='Describes the valid language/country combinations.';

--
-- Dumping data for table `lams_supported_locale`
--

LOCK TABLES `lams_supported_locale` WRITE;
/*!40000 ALTER TABLE `lams_supported_locale` DISABLE KEYS */;
INSERT INTO `lams_supported_locale` VALUES (1,'en','AU','English (Australia)','LTR','en-au'),(2,'es','ES','Español','LTR','es'),(3,'mi','NZ','Māori','LTR','en-au'),(4,'de','DE','Deutsch','LTR','de'),(5,'zh','CN','简体中文','LTR','zh-cn'),(6,'fr','FR','Français','LTR','fr'),(7,'it','IT','Italiano','LTR','it'),(8,'no','NO','Norsk','LTR','no'),(9,'sv','SE','Svenska','LTR','sv'),(10,'ko','KR','한국어','LTR','ko'),(11,'pl','PL','Polski','LTR','pl'),(12,'pt','BR','Português (Brasil)','LTR','pt-br'),(13,'hu','HU','Magyar','LTR','hu'),(14,'bg','BG','Български','LTR','bg'),(15,'cy','GB','Cymraeg (Cymru)','LTR','en-au'),(16,'th','TH','Thai','LTR','th'),(17,'el','GR','Ελληνικά','LTR','el'),(18,'nl','BE','Nederlands (België)','LTR','nl'),(19,'ar','JO','عربي','RTL','ar'),(20,'da','DK','Dansk','LTR','da'),(21,'ru','RU','Русский','LTR','ru'),(22,'vi','VN','Tiếng Việt','LTR','vi'),(23,'zh','TW','Chinese (Taiwan)','LTR','zh'),(24,'ja','JP','日本語','LTR','ja'),(25,'ms','MY','Malay (Malaysia)','LTR','ms'),(26,'tr','TR','Türkçe','LTR','tr');
/*!40000 ALTER TABLE `lams_supported_locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_system_tool`
--

DROP TABLE IF EXISTS `lams_system_tool`;
CREATE TABLE `lams_system_tool` (
  `system_tool_id` bigint(20) NOT NULL auto_increment,
  `learning_activity_type_id` int(11) NOT NULL,
  `tool_display_name` varchar(255) NOT NULL,
  `description` text,
  `learner_url` text,
  `learner_preview_url` text COMMENT 'Learner screen for preview a learning design. ',
  `learner_progress_url` text COMMENT 'Teacher''s view of a learner''s screen.',
  `export_pfolio_learner_url` text,
  `export_pfolio_class_url` text,
  `monitor_url` text,
  `contribute_url` text,
  `help_url` text,
  `create_date_time` datetime NOT NULL,
  `admin_url` text,
  PRIMARY KEY  (`system_tool_id`),
  UNIQUE KEY `UQ_systool_activity_type` (`learning_activity_type_id`),
  KEY `learning_activity_type_id` (`learning_activity_type_id`),
  CONSTRAINT `FK_lams_system_tool` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_system_tool`
--

LOCK TABLES `lams_system_tool` WRITE;
/*!40000 ALTER TABLE `lams_system_tool` DISABLE KEYS */;
INSERT INTO `lams_system_tool` VALUES (1,2,'Grouping','All types of grouping including random and chosen.','learning/grouping.do?method=performGrouping','learning/grouping.do?method=performGrouping','learning/grouping.do?method=viewGrouping&mode=teacher','learning/groupingExportPortfolio?mode=learner','learning/groupingExportPortfolio?mode=teacher','monitoring/grouping.do?method=startGrouping','monitoring/grouping.do?method=startGrouping',NULL,'2008-12-03 14:38:21',NULL),(2,3,'Sync Gate','Gate: Synchronise Learners.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2008-12-03 14:38:21',NULL),(3,4,'Schedule Gate','Gate: Opens/shuts at particular times.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2008-12-03 14:38:21',NULL),(4,5,'Permission Gate','Gate: Opens under teacher or staff control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2008-12-03 14:38:21',NULL),(5,9,'System Gate','Gate: Opens under system control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2008-12-03 14:38:21',NULL),(6,10,'Monitor Chosen Branching','Select between multiple sequence activities, with the branch chosen in monitoring.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/chosenBranching.do?method=assignBranch','monitoring/chosenBranching.do?method=assignBranch',NULL,'2008-12-03 14:38:21',NULL),(7,11,'Group Based Branching','Select between multiple sequence activities, with the branch chosen by an existing group.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/groupedBranching.do?method=viewBranching','monitoring/groupedBranching.do?method=assignBranch',NULL,'2008-12-03 14:38:21',NULL),(8,12,'Tool Output Based Branching','Select between multiple sequence activities, with the branch chosen on results of another activity.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/toolBranching.do?method=viewBranching','monitoring/toolBranching.do?method=viewBranching',NULL,'2008-12-03 14:38:21',NULL),(9,8,'Sequence','A sequence of activities','learning/SequenceActivity.do','learning/SequenceActivity.do','monitoring/complexProgress.do',NULL,'monitoring/sequenceExportPortfolio?mode=teacher','monitoring/sequence.do?method=viewSequence','monitoring/sequence.do?method=viewSequence',NULL,'2008-12-03 14:38:21',NULL),(10,14,'Condition Gate','Gate: Opens if conditions are met','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2008-12-03 14:38:21',NULL);
/*!40000 ALTER TABLE `lams_system_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_text_search_condition`
--

DROP TABLE IF EXISTS `lams_text_search_condition`;
CREATE TABLE `lams_text_search_condition` (
  `condition_id` bigint(20) NOT NULL,
  `text_search_all_words` text,
  `text_search_phrase` text,
  `text_search_any_words` text,
  `text_search_excluded_words` text,
  PRIMARY KEY  (`condition_id`),
  CONSTRAINT `TextSearchConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_text_search_condition`
--

LOCK TABLES `lams_text_search_condition` WRITE;
/*!40000 ALTER TABLE `lams_text_search_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_text_search_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool`
--

DROP TABLE IF EXISTS `lams_tool`;
CREATE TABLE `lams_tool` (
  `tool_id` bigint(20) NOT NULL auto_increment,
  `tool_signature` varchar(64) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `tool_display_name` varchar(255) NOT NULL,
  `description` text,
  `tool_identifier` varchar(64) NOT NULL,
  `tool_version` varchar(10) NOT NULL,
  `learning_library_id` bigint(20) default NULL,
  `default_tool_content_id` bigint(20) default NULL,
  `valid_flag` tinyint(1) NOT NULL default '1',
  `grouping_support_type_id` int(3) NOT NULL,
  `supports_run_offline_flag` tinyint(1) NOT NULL,
  `learner_url` text NOT NULL,
  `learner_preview_url` text COMMENT 'Learner screen for preview a learning design. ',
  `learner_progress_url` text COMMENT 'Teacher''s view of a learner''s screen.',
  `author_url` text NOT NULL,
  `define_later_url` text,
  `export_pfolio_learner_url` text,
  `export_pfolio_class_url` text,
  `monitor_url` text,
  `contribute_url` text,
  `moderation_url` text,
  `help_url` text,
  `create_date_time` datetime NOT NULL,
  `language_file` varchar(255) default NULL,
  `modified_date_time` datetime default NULL,
  `classpath_addition` varchar(255) default NULL,
  `context_file` varchar(255) default NULL,
  `admin_url` text,
  `supports_outputs` tinyint(1) default '0',
  `ext_lms_id` varchar(255) default NULL,
  PRIMARY KEY  (`tool_id`),
  UNIQUE KEY `UQ_lams_tool_sig` (`tool_signature`),
  UNIQUE KEY `UQ_lams_tool_class_name` (`service_name`),
  KEY `learning_library_id` (`learning_library_id`),
  KEY `grouping_support_type_id` (`grouping_support_type_id`),
  CONSTRAINT `FK_lams_tool_1` FOREIGN KEY (`learning_library_id`) REFERENCES `lams_learning_library` (`learning_library_id`),
  CONSTRAINT `FK_lams_tool_2` FOREIGN KEY (`grouping_support_type_id`) REFERENCES `lams_grouping_support_type` (`grouping_support_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool`
--

LOCK TABLES `lams_tool` WRITE;
/*!40000 ALTER TABLE `lams_tool` DISABLE KEYS */;
INSERT INTO `lams_tool` VALUES (1,'lafrum11','forumService','Forum','Forum / Message Boards','forum','20081022',1,1,1,2,1,'tool/lafrum11/learning/viewForum.do?mode=learner','tool/lafrum11/learning/viewForum.do?mode=author','tool/lafrum11/learning/viewForum.do?mode=teacher','tool/lafrum11/authoring.do','tool/lafrum11/defineLater.do','tool/lafrum11/exportPortfolio?mode=learner','tool/lafrum11/exportPortfolio?mode=teacher','tool/lafrum11/monitoring.do','tool/lafrum11/contribute.do','tool/lafrum11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lafrum11','2008-12-03 14:38:40','org.lamsfoundation.lams.tool.forum.ApplicationResources','2008-12-03 14:38:40','lams-tool-lafrum11.jar','/org/lamsfoundation/lams/tool/forum/forumApplicationContext.xml',NULL,1,NULL),(2,'lanb11','nbService','NoticeboardX','Displays a NoticeboardX','nb','20080601',2,2,1,2,1,'tool/lanb11/starter/learner.do?mode=learner','tool/lanb11/starter/learner.do?mode=author','tool/lanb11/starter/learner.do?mode=teacher','tool/lanb11/authoring.do','tool/lanb11/authoring.do?defineLater=true','tool/lanb11/portfolioExport?mode=learner','tool/lanb11/portfolioExport?mode=teacher','tool/lanb11/monitoring.do',NULL,NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lanb11','2008-12-03 14:38:46','org.lamsfoundation.lams.tool.noticeboard.ApplicationResources','2008-12-03 14:38:46','lams-tool-lanb11.jar','/org/lamsfoundation/lams/tool/noticeboard/applicationContext.xml',NULL,0,NULL),(3,'laqa11','qaService','Question and Answer','Q/A Tool','qa','20080926',3,3,1,2,1,'tool/laqa11/learningStarter.do?mode=learner','tool/laqa11/learningStarter.do?mode=author','tool/laqa11/learningStarter.do?mode=teacher','tool/laqa11/authoringStarter.do','tool/laqa11/defineLaterStarter.do','tool/laqa11/exportPortfolio?mode=learner','tool/laqa11/exportPortfolio?mode=teacher','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','http://wiki.lamsfoundation.org/display/lamsdocs/laqa11','2008-12-03 14:38:51','org.lamsfoundation.lams.tool.qa.ApplicationResources','2008-12-03 14:38:51','lams-tool-laqa11.jar','/org/lamsfoundation/lams/tool/qa/qaApplicationContext.xml','tool/laqa11/laqa11admin.do',1,NULL),(4,'lasbmt11','submitFilesService','Submit File','Submit File Tool Description','submitfile','20081107',4,4,1,2,1,'tool/lasbmt11/learner.do?mode=learner','tool/lasbmt11/learner.do?mode=author','tool/lasbmt11/learner.do?mode=teacher','tool/lasbmt11/authoring.do','tool/lasbmt11/definelater.do?mode=teacher','tool/lasbmt11/exportPortfolio?mode=learner','tool/lasbmt11/exportPortfolio?mode=teacher','tool/lasbmt11/monitoring.do','tool/lasbmt11/contribute.do','tool/lasbmt11/moderation.do','http://wiki.lamsfoundation.org/display/lamsdocs/lasbmt11','2008-12-03 14:38:57','org.lamsfoundation.lams.tool.sbmt.ApplicationResources','2008-12-03 14:38:57','lams-tool-lasbmt11.jar','/org/lamsfoundation/lams/tool/sbmt/submitFilesApplicationContext.xml',NULL,0,NULL),(5,'lachat11','chatService','Chat','Chat','chat','20081027',5,5,1,2,1,'tool/lachat11/learning.do?mode=learner','tool/lachat11/learning.do?mode=author','tool/lachat11/learning.do?mode=teacher','tool/lachat11/authoring.do','tool/lachat11/authoring.do?mode=teacher','tool/lachat11/exportPortfolio?mode=learner','tool/lachat11/exportPortfolio?mode=teacher','tool/lachat11/monitoring.do','tool/lachat11/contribute.do','tool/lachat11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lachat11','2008-12-03 14:39:05','org.lamsfoundation.lams.tool.chat.ApplicationResources','2008-12-03 14:39:05','lams-tool-lachat11.jar','/org/lamsfoundation/lams/tool/chat/chatApplicationContext.xml',NULL,1,NULL),(6,'larsrc11','resourceService','Shared Resources','Shared Resources','sharedresources','20081107',6,6,1,2,1,'tool/larsrc11/learning/start.do?mode=learner','tool/larsrc11/learning/start.do?mode=author','tool/larsrc11/learning/start.do?mode=teacher','tool/larsrc11/authoring/start.do','tool/larsrc11/definelater.do','tool/larsrc11/exportPortfolio?mode=learner','tool/larsrc11/exportPortfolio?mode=teacher','tool/larsrc11/monitoring/summary.do','tool/larsrc11/contribute.do','tool/larsrc11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11','2008-12-03 14:39:14','org.lamsfoundation.lams.tool.rsrc.ApplicationResources','2008-12-03 14:39:14','lams-tool-larsrc11.jar','/org/lamsfoundation/lams/tool/rsrc/rsrcApplicationContext.xml',NULL,0,NULL),(7,'lavote11','voteService','Voting','Voting','vote','20080326',7,7,1,2,1,'tool/lavote11/learningStarter.do?mode=learner','tool/lavote11/learningStarter.do?mode=author','tool/lavote11/learningStarter.do?mode=teacher','tool/lavote11/authoringStarter.do','tool/lavote11/defineLaterStarter.do','tool/lavote11/exportPortfolio?mode=learner','tool/lavote11/exportPortfolio?mode=teacher','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','http://wiki.lamsfoundation.org/display/lamsdocs/lavote11','2008-12-03 14:39:18','org.lamsfoundation.lams.tool.vote.ApplicationResources','2008-12-03 14:39:18','lams-tool-lavote11.jar','/org/lamsfoundation/lams/tool/vote/voteApplicationContext.xml',NULL,1,NULL),(8,'lantbk11','notebookService','Notebook','Notebook','notebook','20080929',8,8,1,2,1,'tool/lantbk11/learning.do?mode=learner','tool/lantbk11/learning.do?mode=author','tool/lantbk11/learning.do?mode=teacher','tool/lantbk11/authoring.do','tool/lantbk11/authoring.do?mode=teacher','tool/lantbk11/exportPortfolio?mode=learner','tool/lantbk11/exportPortfolio?mode=teacher','tool/lantbk11/monitoring.do','tool/lantbk11/contribute.do','tool/lantbk11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lantbk11','2008-12-03 14:39:26','org.lamsfoundation.lams.tool.notebook.ApplicationResources','2008-12-03 14:39:26','lams-tool-lantbk11.jar','/org/lamsfoundation/lams/tool/notebook/notebookApplicationContext.xml',NULL,1,NULL),(9,'lasurv11','lasurvSurveyService','Survey','Survey','survey','20081021',9,9,1,2,1,'tool/lasurv11/learning/start.do?mode=learner','tool/lasurv11/learning/start.do?mode=author','tool/lasurv11/learning/start.do?mode=teacher','tool/lasurv11/authoring/start.do','tool/lasurv11/definelater.do','tool/lasurv11/exportPortfolio?mode=learner','tool/lasurv11/exportPortfolio?mode=teacher','tool/lasurv11/monitoring/summary.do','tool/lasurv11/contribute.do','tool/lasurv11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lasurv11','2008-12-03 14:39:34','org.lamsfoundation.lams.tool.survey.ApplicationResources','2008-12-03 14:39:34','lams-tool-lasurv11.jar','/org/lamsfoundation/lams/tool/survey/surveyApplicationContext.xml',NULL,1,NULL),(10,'lascrb11','lascrbScribeService','Scribe','Scribe','scribe','20080229',10,10,1,2,1,'tool/lascrb11/learning.do?mode=learner','tool/lascrb11/learning.do?mode=author','tool/lascrb11/learning.do?mode=teacher','tool/lascrb11/authoring.do','tool/lascrb11/authoring.do?mode=teacher','tool/lascrb11/exportPortfolio?mode=learner','tool/lascrb11/exportPortfolio?mode=teacher','tool/lascrb11/monitoring.do','tool/lascrb11/contribute.do','tool/lascrb11/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lascrb11','2008-12-03 14:39:43','org.lamsfoundation.lams.tool.scribe.ApplicationResources','2008-12-03 14:39:43','lams-tool-lascrb11.jar','/org/lamsfoundation/lams/tool/scribe/scribeApplicationContext.xml',NULL,0,NULL),(11,'latask10','lataskTaskListService','Shared TaskList','Shared TaskList','sharedtaskList','20080211',11,11,1,2,1,'tool/latask10/learning/start.do?mode=learner','tool/latask10/learning/start.do?mode=author','tool/latask10/learning/start.do?mode=teacher','tool/latask10/authoring/start.do','tool/latask10/definelater.do','tool/latask10/exportPortfolio?mode=learner','tool/latask10/exportPortfolio?mode=teacher','tool/latask10/monitoring/summary.do','tool/latask10/contribute.do','tool/latask10/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/latask10','2008-12-03 14:39:52','org.lamsfoundation.lams.tool.taskList.ApplicationResources','2008-12-03 14:39:52','lams-tool-latask10.jar','/org/lamsfoundation/lams/tool/taskList/taskListApplicationContext.xml',NULL,1,NULL),(12,'lagmap10','gmapService','Gmap','Gmap','gmap','20080521',12,12,1,2,1,'tool/lagmap10/learning.do?mode=learner','tool/lagmap10/learning.do?mode=author','tool/lagmap10/learning.do?mode=teacher','tool/lagmap10/authoring.do','tool/lagmap10/authoring.do?mode=teacher','tool/lagmap10/exportPortfolio?mode=learner','tool/lagmap10/exportPortfolio?mode=teacher','tool/lagmap10/monitoring.do','tool/lagmap10/contribute.do','tool/lagmap10/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lagmap10','2008-12-03 14:40:01','org.lamsfoundation.lams.tool.gmap.ApplicationResources','2008-12-03 14:40:01','lams-tool-lagmap10.jar','/org/lamsfoundation/lams/tool/gmap/gmapApplicationContext.xml','tool/lagmap10/lagmap10admin.do',0,NULL),(13,'lasprd10','spreadsheetService','Spreadsheet Tool','Spreadsheet Tool','spreadsheet','20080612',13,13,1,2,1,'tool/lasprd10/learning/start.do?mode=learner','tool/lasprd10/learning/start.do?mode=author','tool/lasprd10/learning/start.do?mode=teacher','tool/lasprd10/authoring/start.do','tool/lasprd10/definelater.do','tool/lasprd10/exportPortfolio?mode=learner','tool/lasprd10/exportPortfolio?mode=teacher','tool/lasprd10/monitoring/summary.do','tool/lasprd10/contribute.do','tool/lasprd10/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lasprd10','2008-12-03 14:40:09','org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources','2008-12-03 14:40:09','lams-tool-lasprd10.jar','/org/lamsfoundation/lams/tool/spreadsheet/spreadsheetApplicationContext.xml',NULL,0,NULL),(14,'ladaco10','dacoService','Data Collection','Collecting data with custom structure.','daco','20081114',14,14,1,2,1,'tool/ladaco10/learning/start.do?mode=learner','tool/ladaco10/learning/start.do?mode=author','tool/ladaco10/learning/start.do?mode=teacher','tool/ladaco10/authoring/start.do','tool/ladaco10/definelater.do','tool/ladaco10/exportPortfolio?mode=learner','tool/ladaco10/exportPortfolio?mode=teacher','tool/ladaco10/monitoring/summary.do','tool/ladaco10/contribute.do','tool/ladaco10/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/ladaco10','2008-12-03 14:40:17','org.lamsfoundation.lams.tool.daco.ApplicationResources','2008-12-03 14:40:17','lams-tool-ladaco10.jar','/org/lamsfoundation/lams/tool/daco/dacoApplicationContext.xml',NULL,1,NULL),(15,'lawiki10','wikiService','Wiki','Wiki','wiki','20080929',15,15,1,2,1,'tool/lawiki10/learning.do?mode=learner','tool/lawiki10/learning.do?mode=author','tool/lawiki10/learning.do?mode=teacher','tool/lawiki10/authoring.do','tool/lawiki10/authoring.do?mode=teacher','tool/lawiki10/exportPortfolio?mode=learner','tool/lawiki10/exportPortfolio?mode=teacher','tool/lawiki10/monitoring.do','tool/lawiki10/contribute.do','tool/lawiki10/moderate.do','http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10','2008-12-03 14:40:25','org.lamsfoundation.lams.tool.wiki.ApplicationResources','2008-12-03 14:40:25','lams-tool-lawiki10.jar','/org/lamsfoundation/lams/tool/wiki/wikiApplicationContext.xml',NULL,1,NULL),(16,'lamc11','mcService','MCQ','Multiple Choice Questions','mc','20070820',16,16,1,2,1,'tool/lamc11/learningStarter.do?mode=learner','tool/lamc11/learningStarter.do?mode=author','tool/lamc11/learningStarter.do?mode=teacher','tool/lamc11/authoringStarter.do','tool/lamc11/defineLaterStarter.do','tool/lamc11/exportPortfolio?mode=learner','tool/lamc11/exportPortfolio?mode=teacher','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','http://wiki.lamsfoundation.org/display/lamsdocs/lamc11','2008-12-03 14:40:30','org.lamsfoundation.lams.tool.mc.ApplicationResources','2008-12-03 14:40:30','lams-tool-lamc11.jar','/org/lamsfoundation/lams/tool/mc/mcApplicationContext.xml',NULL,1,NULL);
/*!40000 ALTER TABLE `lams_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_content`
--

DROP TABLE IF EXISTS `lams_tool_content`;
CREATE TABLE `lams_tool_content` (
  `tool_content_id` bigint(20) NOT NULL auto_increment,
  `tool_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`tool_content_id`),
  KEY `tool_id` (`tool_id`),
  CONSTRAINT `FK_lams_tool_content_1` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool_content`
--

LOCK TABLES `lams_tool_content` WRITE;
/*!40000 ALTER TABLE `lams_tool_content` DISABLE KEYS */;
INSERT INTO `lams_tool_content` VALUES (1,1),(45,1),(78,1),(2,2),(17,2),(18,2),(20,2),(22,2),(23,2),(24,2),(29,2),(31,2),(32,2),(33,2),(35,2),(36,2),(38,2),(39,2),(40,2),(43,2),(49,2),(50,2),(54,2),(57,2),(58,2),(59,2),(61,2),(64,2),(67,2),(69,2),(70,2),(71,2),(72,2),(74,2),(79,2),(80,2),(81,2),(85,2),(87,2),(89,2),(91,2),(94,2),(3,3),(44,3),(93,3),(4,4),(47,4),(84,4),(5,5),(6,6),(19,6),(21,6),(25,6),(26,6),(27,6),(28,6),(30,6),(34,6),(41,6),(56,6),(60,6),(62,6),(63,6),(65,6),(66,6),(68,6),(73,6),(77,6),(7,7),(48,7),(88,7),(8,8),(52,8),(92,8),(9,9),(46,9),(90,9),(10,10),(11,11),(12,12),(51,12),(82,12),(13,13),(37,13),(75,13),(14,14),(55,14),(86,14),(15,15),(53,15),(76,15),(16,16),(42,16),(83,16);
/*!40000 ALTER TABLE `lams_tool_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_import_support`
--

DROP TABLE IF EXISTS `lams_tool_import_support`;
CREATE TABLE `lams_tool_import_support` (
  `id` bigint(20) NOT NULL auto_increment,
  `installed_tool_signature` varchar(15) NOT NULL,
  `supports_tool_signature` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool_import_support`
--

LOCK TABLES `lams_tool_import_support` WRITE;
/*!40000 ALTER TABLE `lams_tool_import_support` DISABLE KEYS */;
INSERT INTO `lams_tool_import_support` VALUES (1,'lafrum11','messageboard'),(2,'lamc11','simpleassessment'),(3,'lanb11','noticeboard'),(4,'lanb11','htmlnb'),(5,'laqa11','qa'),(6,'lasbmt11','reportsubmission'),(7,'lachat11','chat'),(8,'larsrc11','urlcontent'),(11,'lavote11','ranking'),(12,'lantbk11','journal'),(13,'lascrb11','groupreporting'),(14,'lasurv11','survey');
/*!40000 ALTER TABLE `lams_tool_import_support` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_session`
--

DROP TABLE IF EXISTS `lams_tool_session`;
CREATE TABLE `lams_tool_session` (
  `tool_session_id` bigint(20) NOT NULL auto_increment,
  `tool_session_name` varchar(255) NOT NULL,
  `tool_session_type_id` int(3) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `tool_session_state_id` int(3) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `group_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `unique_key` varchar(128) NOT NULL,
  PRIMARY KEY  (`tool_session_id`),
  UNIQUE KEY `UQ_lams_tool_session_1` (`unique_key`),
  KEY `tool_session_state_id` (`tool_session_state_id`),
  KEY `user_id` (`user_id`),
  KEY `tool_session_type_id` (`tool_session_type_id`),
  KEY `activity_id` (`activity_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_tool_session_4` FOREIGN KEY (`tool_session_state_id`) REFERENCES `lams_tool_session_state` (`tool_session_state_id`),
  CONSTRAINT `FK_lams_tool_session_5` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_tool_session_7` FOREIGN KEY (`tool_session_type_id`) REFERENCES `lams_tool_session_type` (`tool_session_type_id`),
  CONSTRAINT `FK_lams_tool_session_8` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_tool_session_1` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool_session`
--

LOCK TABLES `lams_tool_session` WRITE;
/*!40000 ALTER TABLE `lams_tool_session` DISABLE KEYS */;
INSERT INTO `lams_tool_session` VALUES (1,'Everybody learners',1,1,75,1,'2008-12-03 14:45:18',1,NULL,'uq_75_1'),(2,'Everybody learners',1,1,76,1,'2008-12-03 14:45:18',1,NULL,'uq_76_1'),(3,'Everybody learners',1,1,77,1,'2008-12-03 14:45:18',1,NULL,'uq_77_1'),(4,'Everybody learners',1,1,78,1,'2008-12-03 14:45:18',1,NULL,'uq_78_1'),(5,'Everybody learners',1,1,79,1,'2008-12-03 14:45:18',1,NULL,'uq_79_1'),(6,'Everybody learners',1,2,84,1,'2008-12-03 14:45:27',3,NULL,'uq_84_3'),(7,'Everybody learners',1,2,87,1,'2008-12-03 14:45:27',3,NULL,'uq_87_3'),(8,'Everybody learners',1,2,89,1,'2008-12-03 14:45:27',3,NULL,'uq_89_3'),(9,'Everybody learners',1,2,90,1,'2008-12-03 14:45:27',3,NULL,'uq_90_3'),(10,'Everybody learners',1,2,81,1,'2008-12-03 14:45:27',3,NULL,'uq_81_3'),(11,'Everybody learners',1,2,86,1,'2008-12-03 14:45:27',3,NULL,'uq_86_3'),(12,'Everybody learners',1,2,80,1,'2008-12-03 14:45:27',3,NULL,'uq_80_3'),(13,'Everybody learners',1,2,83,1,'2008-12-03 14:45:27',3,NULL,'uq_83_3'),(14,'Everybody learners',1,2,85,1,'2008-12-03 14:45:27',3,NULL,'uq_85_3'),(15,'Everybody learners',1,3,95,1,'2008-12-03 14:45:36',5,NULL,'uq_95_5'),(16,'Everybody learners',1,3,94,1,'2008-12-03 14:45:36',5,NULL,'uq_94_5'),(17,'Everybody learners',1,3,96,1,'2008-12-03 14:45:36',5,NULL,'uq_96_5'),(18,'Everybody learners',1,3,97,1,'2008-12-03 14:45:36',5,NULL,'uq_97_5'),(19,'Everybody learners',1,3,98,1,'2008-12-03 14:45:36',5,NULL,'uq_98_5'),(20,'Everybody learners',1,3,99,1,'2008-12-03 14:45:36',5,NULL,'uq_99_5'),(21,'Everybody learners',1,3,102,1,'2008-12-03 14:45:36',5,NULL,'uq_102_5'),(22,'Everybody learners',1,3,118,1,'2008-12-03 14:45:36',5,NULL,'uq_118_5'),(23,'Everybody learners',1,3,120,1,'2008-12-03 14:45:36',5,NULL,'uq_120_5'),(24,'Everybody learners',1,3,101,1,'2008-12-03 14:45:36',5,NULL,'uq_101_5'),(25,'Everybody learners',1,3,106,1,'2008-12-03 14:45:36',5,NULL,'uq_106_5'),(26,'Everybody learners',1,3,122,1,'2008-12-03 14:45:36',5,NULL,'uq_122_5'),(27,'Everybody learners',1,3,116,1,'2008-12-03 14:45:36',5,NULL,'uq_116_5'),(28,'Everybody learners',1,3,117,1,'2008-12-03 14:45:36',5,NULL,'uq_117_5'),(29,'Everybody learners',1,3,107,1,'2008-12-03 14:45:36',5,NULL,'uq_107_5'),(30,'Everybody learners',1,3,115,1,'2008-12-03 14:45:36',5,NULL,'uq_115_5'),(31,'Everybody learners',1,3,113,1,'2008-12-03 14:45:36',5,NULL,'uq_113_5'),(32,'Everybody learners',1,3,114,1,'2008-12-03 14:45:36',5,NULL,'uq_114_5'),(33,'Everybody learners',1,3,104,1,'2008-12-03 14:45:36',5,NULL,'uq_104_5'),(34,'Everybody learners',1,3,103,1,'2008-12-03 14:45:36',5,NULL,'uq_103_5'),(35,'Everybody learners',1,3,108,1,'2008-12-03 14:45:36',5,NULL,'uq_108_5'),(36,'Everybody learners',1,3,109,1,'2008-12-03 14:45:36',5,NULL,'uq_109_5'),(37,'Everybody learners',1,3,110,1,'2008-12-03 14:45:36',5,NULL,'uq_110_5'),(38,'Everybody learners',1,3,112,1,'2008-12-03 14:45:36',5,NULL,'uq_112_5'),(39,'Everybody learners',1,3,111,1,'2008-12-03 14:45:36',5,NULL,'uq_111_5');
/*!40000 ALTER TABLE `lams_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_session_state`
--

DROP TABLE IF EXISTS `lams_tool_session_state`;
CREATE TABLE `lams_tool_session_state` (
  `tool_session_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`tool_session_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool_session_state`
--

LOCK TABLES `lams_tool_session_state` WRITE;
/*!40000 ALTER TABLE `lams_tool_session_state` DISABLE KEYS */;
INSERT INTO `lams_tool_session_state` VALUES (1,'STARTED'),(2,'ENDED');
/*!40000 ALTER TABLE `lams_tool_session_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_session_type`
--

DROP TABLE IF EXISTS `lams_tool_session_type`;
CREATE TABLE `lams_tool_session_type` (
  `tool_session_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`tool_session_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_tool_session_type`
--

LOCK TABLES `lams_tool_session_type` WRITE;
/*!40000 ALTER TABLE `lams_tool_session_type` DISABLE KEYS */;
INSERT INTO `lams_tool_session_type` VALUES (1,'NON_GROUPED'),(2,'GROUPED');
/*!40000 ALTER TABLE `lams_tool_session_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user`
--

DROP TABLE IF EXISTS `lams_user`;
CREATE TABLE `lams_user` (
  `user_id` bigint(20) NOT NULL auto_increment,
  `login` varchar(255) NOT NULL,
  `password` varchar(50) NOT NULL,
  `title` varchar(32) default NULL,
  `first_name` varchar(128) default NULL,
  `last_name` varchar(128) default NULL,
  `address_line_1` varchar(64) default NULL,
  `address_line_2` varchar(64) default NULL,
  `address_line_3` varchar(64) default NULL,
  `city` varchar(64) default NULL,
  `state` varchar(64) default NULL,
  `postcode` varchar(10) default NULL,
  `country` varchar(64) default NULL,
  `day_phone` varchar(64) default NULL,
  `evening_phone` varchar(64) default NULL,
  `mobile_phone` varchar(64) default NULL,
  `fax` varchar(64) default NULL,
  `email` varchar(128) default NULL,
  `disabled_flag` tinyint(1) NOT NULL default '0',
  `create_date` datetime NOT NULL,
  `authentication_method_id` bigint(20) NOT NULL default '0',
  `workspace_id` bigint(20) default NULL,
  `flash_theme_id` bigint(20) default NULL,
  `html_theme_id` bigint(20) default NULL,
  `chat_id` varchar(255) default NULL COMMENT 'ID used for Jabber',
  `locale_id` int(11) default NULL,
  `portrait_uuid` bigint(20) default NULL,
  `change_password` tinyint(4) default '0',
  `enable_flash` tinyint(1) default '1',
  PRIMARY KEY  (`user_id`),
  UNIQUE KEY `UQ_lams_user_login` (`login`),
  KEY `authentication_method_id` (`authentication_method_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `flash_theme_id` (`flash_theme_id`),
  KEY `html_theme_id` (`html_theme_id`),
  KEY `locale_id` (`locale_id`),
  KEY `login` (`login`),
  KEY `email` (`email`),
  CONSTRAINT `FK_lams_user_1` FOREIGN KEY (`authentication_method_id`) REFERENCES `lams_authentication_method` (`authentication_method_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_2` FOREIGN KEY (`workspace_id`) REFERENCES `lams_workspace` (`workspace_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_4` FOREIGN KEY (`flash_theme_id`) REFERENCES `lams_css_theme_ve` (`theme_ve_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_5` FOREIGN KEY (`html_theme_id`) REFERENCES `lams_css_theme_ve` (`theme_ve_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_6` FOREIGN KEY (`locale_id`) REFERENCES `lams_supported_locale` (`locale_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (1,'sysadmin','a159b7ae81ba3552af61e9731b20870515944538','The','System','Administrator',NULL,NULL,NULL,'Sydney','NSW',NULL,'Australia',NULL,NULL,NULL,NULL,'sysadmin@x.x',0,'2008-12-03 14:38:21',1,51,1,2,NULL,1,NULL,0,1),(5,'test1','b444ac06613fc8d63795be9ad0beaf55011936ac','Dr','One','Test','1','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test1@xx.os',0,'2004-12-23 00:00:00',1,5,1,2,NULL,1,NULL,0,1),(6,'test2','109f4b3c50d7b0df729d299bc6f8e9ef9066971f','Dr','Two','Test','2','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test2@xx.os',0,'2004-12-23 00:00:00',1,6,1,2,NULL,1,NULL,0,1),(7,'test3','3ebfa301dc59196f18593c45e519287a23297589','Dr','Three','Test','3','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test3@xx.os',0,'2004-12-23 00:00:00',1,7,1,2,NULL,1,NULL,0,1),(8,'test4','1ff2b3704aede04eecb51e50ca698efd50a1379b','Dr','Four','Test','4','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test4@xx.os',0,'2004-12-23 00:00:00',1,8,1,2,NULL,1,NULL,0,1);
/*!40000 ALTER TABLE `lams_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_group`
--

DROP TABLE IF EXISTS `lams_user_group`;
CREATE TABLE `lams_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`group_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_user_group_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_user_group_2` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_user_group`
--

LOCK TABLES `lams_user_group` WRITE;
/*!40000 ALTER TABLE `lams_user_group` DISABLE KEYS */;
INSERT INTO `lams_user_group` VALUES (5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(6,1),(6,3),(6,5),(7,1),(7,3),(7,5),(8,1),(8,3),(8,5);
/*!40000 ALTER TABLE `lams_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation`
--

DROP TABLE IF EXISTS `lams_user_organisation`;
CREATE TABLE `lams_user_organisation` (
  `user_organisation_id` bigint(20) NOT NULL auto_increment,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_organisation_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_organisation_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_user_organisation`
--

LOCK TABLES `lams_user_organisation` WRITE;
/*!40000 ALTER TABLE `lams_user_organisation` DISABLE KEYS */;
INSERT INTO `lams_user_organisation` VALUES (1,1,1),(5,2,5),(6,2,6),(7,2,7),(8,2,8),(12,3,5),(13,3,6),(14,3,7),(15,3,8),(19,4,5),(20,4,6),(21,4,7),(22,4,8),(26,5,5),(27,5,6),(28,6,7),(29,6,8);
/*!40000 ALTER TABLE `lams_user_organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation_collapsed`
--

DROP TABLE IF EXISTS `lams_user_organisation_collapsed`;
CREATE TABLE `lams_user_organisation_collapsed` (
  `user_organisation_id` bigint(20) NOT NULL,
  `collapsed` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_1` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_user_organisation_collapsed`
--

LOCK TABLES `lams_user_organisation_collapsed` WRITE;
/*!40000 ALTER TABLE `lams_user_organisation_collapsed` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_user_organisation_collapsed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation_role`
--

DROP TABLE IF EXISTS `lams_user_organisation_role`;
CREATE TABLE `lams_user_organisation_role` (
  `user_organisation_role_id` bigint(20) NOT NULL auto_increment,
  `user_organisation_id` bigint(20) NOT NULL,
  `role_id` int(6) NOT NULL,
  PRIMARY KEY  (`user_organisation_role_id`),
  KEY `role_id` (`role_id`),
  KEY `user_organisation_id` (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_role_2` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_organisation_role_3` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_user_organisation_role`
--

LOCK TABLES `lams_user_organisation_role` WRITE;
/*!40000 ALTER TABLE `lams_user_organisation_role` DISABLE KEYS */;
INSERT INTO `lams_user_organisation_role` VALUES (1,1,1),(5,5,3),(6,6,3),(7,7,3),(8,8,3),(12,5,4),(13,6,4),(14,7,4),(15,8,4),(19,5,5),(20,6,5),(21,7,5),(22,8,5),(26,12,4),(27,13,4),(28,14,4),(33,12,5),(34,13,5),(35,14,5),(36,15,5),(40,19,3),(41,20,3),(42,21,3),(43,22,3),(47,19,4),(48,20,4),(49,21,4),(50,22,4),(54,19,5),(55,20,5),(56,21,5),(57,22,5),(61,26,4),(62,27,4),(63,28,4),(64,29,4),(68,26,5),(69,27,5),(70,28,5),(71,29,5);
/*!40000 ALTER TABLE `lams_user_organisation_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_wkspc_fld_content_type`
--

DROP TABLE IF EXISTS `lams_wkspc_fld_content_type`;
CREATE TABLE `lams_wkspc_fld_content_type` (
  `content_type_id` int(3) NOT NULL auto_increment,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`content_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_wkspc_fld_content_type`
--

LOCK TABLES `lams_wkspc_fld_content_type` WRITE;
/*!40000 ALTER TABLE `lams_wkspc_fld_content_type` DISABLE KEYS */;
INSERT INTO `lams_wkspc_fld_content_type` VALUES (1,'FILE'),(2,'PACKAGE');
/*!40000 ALTER TABLE `lams_wkspc_fld_content_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_wkspc_wkspc_folder`
--

DROP TABLE IF EXISTS `lams_wkspc_wkspc_folder`;
CREATE TABLE `lams_wkspc_wkspc_folder` (
  `id` bigint(20) NOT NULL auto_increment,
  `workspace_id` bigint(20) default NULL,
  `workspace_folder_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_ww_folder_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_workspace` (`workspace_id`),
  CONSTRAINT `FK_lams_ww_folder_2` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_wkspc_wkspc_folder`
--

LOCK TABLES `lams_wkspc_wkspc_folder` WRITE;
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` DISABLE KEYS */;
INSERT INTO `lams_wkspc_wkspc_folder` VALUES (1,1,1),(2,2,2),(3,2,22),(4,3,3),(5,3,23),(7,5,5),(8,6,6),(9,7,7),(10,8,8),(11,50,40),(12,51,45);
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace`
--

DROP TABLE IF EXISTS `lams_workspace`;
CREATE TABLE `lams_workspace` (
  `workspace_id` bigint(20) NOT NULL auto_increment,
  `default_fld_id` bigint(20) default NULL,
  `def_run_seq_fld_id` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`workspace_id`),
  KEY `def_run_seq_fld_id` (`def_run_seq_fld_id`),
  KEY `default_fld_id` (`default_fld_id`),
  CONSTRAINT `FK_lams_workspace_1` FOREIGN KEY (`def_run_seq_fld_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_2` FOREIGN KEY (`default_fld_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_workspace`
--

LOCK TABLES `lams_workspace` WRITE;
/*!40000 ALTER TABLE `lams_workspace` DISABLE KEYS */;
INSERT INTO `lams_workspace` VALUES (1,1,NULL,'ROOT'),(2,2,22,'Developers Playpen'),(3,3,23,'MATH111'),(5,5,NULL,'One Test'),(6,6,NULL,'Two Test'),(7,7,NULL,'Three Test'),(8,8,NULL,'Four Test'),(50,40,41,'Moodle Test'),(51,45,NULL,'System Administrator');
/*!40000 ALTER TABLE `lams_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder`
--

DROP TABLE IF EXISTS `lams_workspace_folder`;
CREATE TABLE `lams_workspace_folder` (
  `workspace_folder_id` bigint(20) NOT NULL auto_increment,
  `parent_folder_id` bigint(20) default NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date_time` datetime default NULL,
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  PRIMARY KEY  (`workspace_folder_id`),
  KEY `parent_folder_id` (`parent_folder_id`),
  KEY `lams_workspace_folder_type_id` (`lams_workspace_folder_type_id`),
  CONSTRAINT `FK_lams_workspace_folder_2` FOREIGN KEY (`parent_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_workspace_folder_4` FOREIGN KEY (`lams_workspace_folder_type_id`) REFERENCES `lams_workspace_folder_type` (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_workspace_folder`
--

LOCK TABLES `lams_workspace_folder` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder` DISABLE KEYS */;
INSERT INTO `lams_workspace_folder` VALUES (1,NULL,'ROOT',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(2,1,'Developers Playpen',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(3,1,'MATH111',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(5,NULL,'One Test',5,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(6,NULL,'Two Test',6,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(7,NULL,'Three Test',7,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(8,NULL,'Four Test',8,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(22,2,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(23,3,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(40,1,'Moodle Test',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(41,40,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(45,NULL,'System Administrator',1,'2006-11-01 00:00:00','2006-11-01 00:00:00',1);
/*!40000 ALTER TABLE `lams_workspace_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder_content`
--

DROP TABLE IF EXISTS `lams_workspace_folder_content`;
CREATE TABLE `lams_workspace_folder_content` (
  `folder_content_id` bigint(20) NOT NULL auto_increment,
  `content_type_id` int(3) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(64) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `workspace_folder_id` bigint(20) NOT NULL,
  `uuid` bigint(20) default NULL,
  `version_id` bigint(20) default NULL,
  `mime_type` varchar(10) NOT NULL,
  PRIMARY KEY  (`folder_content_id`),
  UNIQUE KEY `unique_content_name` (`name`,`workspace_folder_id`,`mime_type`),
  UNIQUE KEY `unique_node_version` (`workspace_folder_id`,`uuid`,`version_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `content_type_id` (`content_type_id`),
  CONSTRAINT `FK_lams_workspace_folder_content_1` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_folder_content_2` FOREIGN KEY (`content_type_id`) REFERENCES `lams_wkspc_fld_content_type` (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_workspace_folder_content`
--

LOCK TABLES `lams_workspace_folder_content` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_workspace_folder_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder_type`
--

DROP TABLE IF EXISTS `lams_workspace_folder_type`;
CREATE TABLE `lams_workspace_folder_type` (
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `lams_workspace_folder_type`
--

LOCK TABLES `lams_workspace_folder_type` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder_type` DISABLE KEYS */;
INSERT INTO `lams_workspace_folder_type` VALUES (1,'NORMAL'),(2,'RUN SEQUENCES');
/*!40000 ALTER TABLE `lams_workspace_folder_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patches`
--

DROP TABLE IF EXISTS `patches`;
CREATE TABLE `patches` (
  `system_name` varchar(30) NOT NULL,
  `patch_level` int(11) NOT NULL,
  `patch_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `patch_in_progress` char(1) NOT NULL default 'F',
  PRIMARY KEY  (`system_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `patches`
--

LOCK TABLES `patches` WRITE;
/*!40000 ALTER TABLE `patches` DISABLE KEYS */;
INSERT INTO `patches` VALUES ('lachat11',20081027,'2008-12-03 03:39:06','F'),('ladaco10',20081114,'2008-12-03 03:40:18','F'),('lafrum11',20081022,'2008-12-03 03:38:41','F'),('lagmap10',20080521,'2008-12-03 03:40:02','F'),('lamc11',20070820,'2008-12-03 03:40:31','F'),('lams',13,'2008-12-03 03:38:21','F'),('lanb11',20080601,'2008-12-03 03:38:46','F'),('lantbk11',20080929,'2008-12-03 03:39:27','F'),('laqa11',20080926,'2008-12-03 03:38:51','F'),('larsrc11',20081107,'2008-12-03 03:39:15','F'),('lasbmt11',20081107,'2008-12-03 03:38:58','F'),('lascrb11',20080229,'2008-12-03 03:39:44','F'),('lasprd10',20080612,'2008-12-03 03:40:10','F'),('lasurv11',20081021,'2008-12-03 03:39:36','F'),('latask10',20080211,'2008-12-03 03:39:54','F'),('lavote11',20080326,'2008-12-03 03:39:19','F'),('lawiki10',20080929,'2008-12-03 03:40:26','F');
/*!40000 ALTER TABLE `patches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_attachment`
--

DROP TABLE IF EXISTS `tl_lachat11_attachment`;
CREATE TABLE `tl_lachat11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `chat_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK9ED6CB2E1A3926E3` (`chat_uid`),
  CONSTRAINT `FK9ED6CB2E1A3926E3` FOREIGN KEY (`chat_uid`) REFERENCES `tl_lachat11_chat` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_attachment`
--

LOCK TABLES `tl_lachat11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lachat11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_chat`
--

DROP TABLE IF EXISTS `tl_lachat11_chat`;
CREATE TABLE `tl_lachat11_chat` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `filtering_enabled` bit(1) default NULL,
  `filter_keywords` text,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_chat`
--

LOCK TABLES `tl_lachat11_chat` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_chat` DISABLE KEYS */;
INSERT INTO `tl_lachat11_chat` VALUES (1,NULL,NULL,NULL,'Chat','Instructions',0x00,0x00,0x00,NULL,'','',0x00,0x00,5,0x00,NULL);
/*!40000 ALTER TABLE `tl_lachat11_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_conditions`
--

DROP TABLE IF EXISTS `tl_lachat11_conditions`;
CREATE TABLE `tl_lachat11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `ChatConditionToChat` (`content_uid`),
  CONSTRAINT `ChatConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ChatConditionToChat` FOREIGN KEY (`content_uid`) REFERENCES `tl_lachat11_chat` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_conditions`
--

LOCK TABLES `tl_lachat11_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lachat11_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_message`
--

DROP TABLE IF EXISTS `tl_lachat11_message`;
CREATE TABLE `tl_lachat11_message` (
  `uid` bigint(20) NOT NULL auto_increment,
  `chat_session_uid` bigint(20) NOT NULL,
  `from_user_uid` bigint(20) default NULL,
  `to_user_uid` bigint(20) default NULL,
  `type` varchar(255) default NULL,
  `body` text,
  `send_date` datetime default NULL,
  `hidden` bit(1) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKCC08C1DC2AF61E05` (`to_user_uid`),
  KEY `FKCC08C1DC9C8469FC` (`chat_session_uid`),
  KEY `FKCC08C1DCCF3BF9B6` (`from_user_uid`),
  CONSTRAINT `FKCC08C1DCCF3BF9B6` FOREIGN KEY (`from_user_uid`) REFERENCES `tl_lachat11_user` (`uid`),
  CONSTRAINT `FKCC08C1DC2AF61E05` FOREIGN KEY (`to_user_uid`) REFERENCES `tl_lachat11_user` (`uid`),
  CONSTRAINT `FKCC08C1DC9C8469FC` FOREIGN KEY (`chat_session_uid`) REFERENCES `tl_lachat11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_message`
--

LOCK TABLES `tl_lachat11_message` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lachat11_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_session`
--

DROP TABLE IF EXISTS `tl_lachat11_session`;
CREATE TABLE `tl_lachat11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `chat_uid` bigint(20) default NULL,
  `jabber_room` varchar(250) default NULL,
  `room_created` bit(1) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK96E446B1A3926E3` (`chat_uid`),
  CONSTRAINT `FK96E446B1A3926E3` FOREIGN KEY (`chat_uid`) REFERENCES `tl_lachat11_chat` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_session`
--

LOCK TABLES `tl_lachat11_session` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lachat11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_user`
--

DROP TABLE IF EXISTS `tl_lachat11_user`;
CREATE TABLE `tl_lachat11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `jabber_id` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `jabber_nickname` varchar(255) default NULL,
  `chat_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK4EB82169C8469FC` (`chat_session_uid`),
  CONSTRAINT `FK4EB82169C8469FC` FOREIGN KEY (`chat_session_uid`) REFERENCES `tl_lachat11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lachat11_user`
--

LOCK TABLES `tl_lachat11_user` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lachat11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_answer_options`
--

DROP TABLE IF EXISTS `tl_ladaco10_answer_options`;
CREATE TABLE `tl_ladaco10_answer_options` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_uid` bigint(20) default NULL,
  `sequence_num` tinyint(3) unsigned default '1',
  `answer_option` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `question_uid` (`question_uid`),
  CONSTRAINT `tl_ladaco10_answer_options_ibfk_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_ladaco10_questions` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_answer_options`
--

LOCK TABLES `tl_ladaco10_answer_options` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_answer_options` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_ladaco10_answer_options` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_answers`
--

DROP TABLE IF EXISTS `tl_ladaco10_answers`;
CREATE TABLE `tl_ladaco10_answers` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_uid` bigint(20) default NULL,
  `question_uid` bigint(20) default NULL,
  `record_id` smallint(5) unsigned default NULL,
  `answer` text,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `file_version_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `user_uid` (`user_uid`),
  KEY `question_uid` (`question_uid`),
  CONSTRAINT `AnswerToQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_ladaco10_questions` (`uid`),
  CONSTRAINT `AnswerToUser` FOREIGN KEY (`user_uid`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_answers`
--

LOCK TABLES `tl_ladaco10_answers` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_answers` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_ladaco10_answers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_attachments`
--

DROP TABLE IF EXISTS `tl_ladaco10_attachments`;
CREATE TABLE `tl_ladaco10_attachments` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `tl_ladaco10_attachments_ibfk_1` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_attachments`
--

LOCK TABLES `tl_ladaco10_attachments` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_ladaco10_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_contents`
--

DROP TABLE IF EXISTS `tl_ladaco10_contents`;
CREATE TABLE `tl_ladaco10_contents` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default '0',
  `lock_on_finished` tinyint(4) default '0',
  `min_records` tinyint(3) unsigned default '0',
  `max_records` tinyint(3) unsigned default '0',
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default '0',
  `define_later` tinyint(4) default '0',
  `content_id` bigint(20) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` tinyint(4) default NULL,
  `learner_entry_notify` tinyint(4) default '0',
  `record_submit_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `create_by` (`create_by`),
  CONSTRAINT `DacoToUser` FOREIGN KEY (`create_by`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_contents`
--

LOCK TABLES `tl_ladaco10_contents` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_contents` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_contents` VALUES (1,NULL,NULL,NULL,'Data Collection',0,0,0,0,'Instructions',NULL,NULL,0,0,14,NULL,0,0,0),(2,NULL,'2008-11-10 12:52:14',1,'Data Collection',0,0,0,0,'<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/5570965/dacoIcon.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(51, 153, 102);\">Data Collection</span></span></p>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<p>The <b>Data Collection </b>tool allows Authors to create database style lists for Learners to populate.</p>\n<p>Each record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</p>','','',0,0,55,'',0,0,0),(3,NULL,'2008-11-10 12:52:14',2,'Data Collection',0,0,0,0,'<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/5570965/dacoIcon.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(51, 153, 102);\">Data Collection</span></span></p>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<p>The <b>Data Collection </b>tool allows Authors to create database style lists for Learners to populate.</p>\n<p>Each record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</p>','','',0,0,86,'',0,0,0);
/*!40000 ALTER TABLE `tl_ladaco10_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_questions`
--

DROP TABLE IF EXISTS `tl_ladaco10_questions`;
CREATE TABLE `tl_ladaco10_questions` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` text,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `is_required` tinyint(4) default '0',
  `question_type` tinyint(3) unsigned default NULL,
  `min_constraint` float default NULL,
  `max_constraint` float default NULL,
  `digits_decimal` tinyint(3) unsigned default NULL,
  `summary` tinyint(3) unsigned default NULL,
  `content_uid` bigint(20) default NULL,
  `session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `create_by` (`create_by`),
  KEY `content_uid` (`content_uid`),
  KEY `session_uid` (`session_uid`),
  CONSTRAINT `tl_ladaco10_questions_ibfk_1` FOREIGN KEY (`session_uid`) REFERENCES `tl_ladaco10_sessions` (`uid`),
  CONSTRAINT `QuestionToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`),
  CONSTRAINT `QuestionToUser` FOREIGN KEY (`create_by`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_questions`
--

LOCK TABLES `tl_ladaco10_questions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_questions` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_questions` VALUES (1,'<div>What is your favourite colour?</div>',NULL,'2008-12-03 14:40:18',0,1,NULL,NULL,NULL,NULL,1,NULL),(2,'<div>What is your favourite colour?</div>',1,'2008-11-07 17:34:13',0,1,NULL,NULL,NULL,NULL,2,NULL),(3,'<div>What is your lucky number?</div>',1,'2008-11-10 12:49:56',0,3,NULL,NULL,NULL,NULL,2,NULL),(4,'<div>What is today\'s date?</div>',1,'2008-11-10 12:50:16',0,4,NULL,NULL,NULL,NULL,2,NULL),(5,'<div>What is your favourite colour?</div>',2,'2008-11-07 17:34:13',0,1,NULL,NULL,NULL,NULL,3,NULL),(6,'<div>What is your lucky number?</div>',2,'2008-11-10 12:49:56',0,3,NULL,NULL,NULL,NULL,3,NULL),(7,'<div>What is today\'s date?</div>',2,'2008-11-10 12:50:16',0,4,NULL,NULL,NULL,NULL,3,NULL);
/*!40000 ALTER TABLE `tl_ladaco10_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_sessions`
--

DROP TABLE IF EXISTS `tl_ladaco10_sessions`;
CREATE TABLE `tl_ladaco10_sessions` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `content_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `session_id` (`session_id`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `SessionToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_sessions`
--

LOCK TABLES `tl_ladaco10_sessions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_sessions` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_sessions` VALUES (1,NULL,NULL,0,3,39,'Everybody learners');
/*!40000 ALTER TABLE `tl_ladaco10_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_users`
--

DROP TABLE IF EXISTS `tl_ladaco10_users`;
CREATE TABLE `tl_ladaco10_users` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `session_uid` (`session_uid`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `UserToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`),
  CONSTRAINT `UserToSession` FOREIGN KEY (`session_uid`) REFERENCES `tl_ladaco10_sessions` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_ladaco10_users`
--

LOCK TABLES `tl_ladaco10_users` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_users` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_users` VALUES (1,5,'Test','One','test1',0,NULL,2),(2,5,'Test','One','test1',0,NULL,3);
/*!40000 ALTER TABLE `tl_ladaco10_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_attachment`
--

DROP TABLE IF EXISTS `tl_lafrum11_attachment`;
CREATE TABLE `tl_lafrum11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `forum_uid` bigint(20) default NULL,
  `message_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK389AD9A2FE939F2A` (`message_uid`),
  KEY `FK389AD9A2131CE31E` (`forum_uid`),
  CONSTRAINT `FK389AD9A2131CE31E` FOREIGN KEY (`forum_uid`) REFERENCES `tl_lafrum11_forum` (`uid`),
  CONSTRAINT `FK389AD9A2FE939F2A` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_attachment`
--

LOCK TABLES `tl_lafrum11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_condition_topics`
--

DROP TABLE IF EXISTS `tl_lafrum11_condition_topics`;
CREATE TABLE `tl_lafrum11_condition_topics` (
  `condition_id` bigint(20) NOT NULL default '0',
  `topic_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`topic_uid`),
  KEY `ForumConditionQuestionToForumQuestion` (`topic_uid`),
  CONSTRAINT `ForumConditionQuestionToForumCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lafrum11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionQuestionToForumQuestion` FOREIGN KEY (`topic_uid`) REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_condition_topics`
--

LOCK TABLES `tl_lafrum11_condition_topics` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_condition_topics` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_condition_topics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_conditions`
--

DROP TABLE IF EXISTS `tl_lafrum11_conditions`;
CREATE TABLE `tl_lafrum11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `ForumConditionToForum` (`content_uid`),
  CONSTRAINT `ForumConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionToForum` FOREIGN KEY (`content_uid`) REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_conditions`
--

LOCK TABLES `tl_lafrum11_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_forum`
--

DROP TABLE IF EXISTS `tl_lafrum11_forum`;
CREATE TABLE `tl_lafrum11_forum` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `allow_anonym` smallint(6) default NULL,
  `run_offline` smallint(6) default NULL,
  `lock_on_finished` smallint(6) default NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` smallint(6) default NULL,
  `define_later` smallint(6) default NULL,
  `content_id` bigint(20) default NULL,
  `allow_edit` smallint(6) default NULL,
  `allow_rich_editor` smallint(6) default NULL,
  `allow_new_topic` smallint(6) default NULL,
  `allow_upload` smallint(6) default NULL,
  `maximum_reply` int(11) default NULL,
  `minimum_reply` int(11) default NULL,
  `limited_of_chars` int(11) default NULL,
  `limited_input_flag` smallint(6) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  `mark_release_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK87917942E42F4351` (`create_by`),
  CONSTRAINT `FK87917942E42F4351` FOREIGN KEY (`create_by`) REFERENCES `tl_lafrum11_forum_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_forum`
--

LOCK TABLES `tl_lafrum11_forum` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_forum` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_forum` VALUES (1,NULL,NULL,NULL,'Forum',0,0,0,'Instructions',NULL,NULL,0,0,1,1,0,1,0,1,0,5000,1,NULL,0,0),(2,'2008-04-11 16:03:30','2008-12-03 14:44:58',1,'LAMS Resources Forum',0,0,0,'<h5>These forum messages relate to the resources above.</h5>','','',0,0,45,1,0,1,0,0,0,5000,1,'',0,0),(3,'2008-04-11 16:03:30','2008-12-03 14:45:35',1,'LAMS Resources Forum',0,0,0,'<h5>These forum messages relate to the resources above.</h5>','','',0,0,78,1,0,1,0,0,0,5000,1,'',0,0);
/*!40000 ALTER TABLE `tl_lafrum11_forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_forum_user`
--

DROP TABLE IF EXISTS `tl_lafrum11_forum_user`;
CREATE TABLE `tl_lafrum11_forum_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `session_id` bigint(20) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK7B83A4A85F0116B6` (`session_id`),
  CONSTRAINT `FK7B83A4A85F0116B6` FOREIGN KEY (`session_id`) REFERENCES `tl_lafrum11_tool_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_forum_user`
--

LOCK TABLES `tl_lafrum11_forum_user` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_forum_user` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_forum_user` VALUES (1,5,'Test','One',NULL,'test1',0);
/*!40000 ALTER TABLE `tl_lafrum11_forum_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_message`
--

DROP TABLE IF EXISTS `tl_lafrum11_message`;
CREATE TABLE `tl_lafrum11_message` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `last_reply_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `modified_by` bigint(20) default NULL,
  `subject` varchar(255) default NULL,
  `body` text,
  `is_authored` smallint(6) default NULL,
  `is_anonymous` smallint(6) default NULL,
  `forum_session_uid` bigint(20) default NULL,
  `parent_uid` bigint(20) default NULL,
  `forum_uid` bigint(20) default NULL,
  `reply_number` int(11) default NULL,
  `hide_flag` smallint(6) default NULL,
  `report_id` bigint(20) default NULL,
  `authored_parent_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK4A6067E8E42F4351` (`create_by`),
  KEY `FK4A6067E897F0DB46` (`report_id`),
  KEY `FK4A6067E8131CE31E` (`forum_uid`),
  KEY `FK4A6067E824089E4D` (`parent_uid`),
  KEY `FK4A6067E89357B45B` (`forum_session_uid`),
  KEY `FK4A6067E8647A7264` (`modified_by`),
  KEY `IX_msg_auth_parent` (`authored_parent_uid`),
  CONSTRAINT `FK4A6067E8131CE31E` FOREIGN KEY (`forum_uid`) REFERENCES `tl_lafrum11_forum` (`uid`),
  CONSTRAINT `FK4A6067E824089E4D` FOREIGN KEY (`parent_uid`) REFERENCES `tl_lafrum11_message` (`uid`),
  CONSTRAINT `FK4A6067E8647A7264` FOREIGN KEY (`modified_by`) REFERENCES `tl_lafrum11_forum_user` (`uid`),
  CONSTRAINT `FK4A6067E89357B45B` FOREIGN KEY (`forum_session_uid`) REFERENCES `tl_lafrum11_tool_session` (`uid`),
  CONSTRAINT `FK4A6067E897F0DB46` FOREIGN KEY (`report_id`) REFERENCES `tl_lafrum11_report` (`uid`),
  CONSTRAINT `FK4A6067E8E42F4351` FOREIGN KEY (`create_by`) REFERENCES `tl_lafrum11_forum_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_message`
--

LOCK TABLES `tl_lafrum11_message` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_message` VALUES (1,'2008-12-03 14:38:41','2008-12-03 14:38:41','2008-12-03 14:38:41',NULL,NULL,'Topic Heading','Topic message',1,0,NULL,NULL,1,0,0,NULL,NULL),(2,'2008-06-06 12:12:18','2008-11-14 14:31:47','2008-12-03 14:44:58',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,NULL,NULL,2,0,0,NULL,NULL),(3,'2008-06-06 12:12:18','2008-11-14 14:31:47','2008-12-03 14:44:58',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,NULL,NULL,2,0,0,NULL,NULL),(4,'2008-06-06 12:12:18','2008-12-03 14:45:35','2008-12-03 14:45:35',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,NULL,NULL,3,0,0,NULL,NULL),(5,'2008-06-06 12:12:18','2008-12-03 14:45:35','2008-12-03 14:45:35',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,NULL,NULL,3,0,0,NULL,NULL),(6,'2008-06-06 12:12:18','2008-12-03 14:45:35','2008-12-03 14:45:36',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,1,NULL,3,0,0,NULL,4),(7,'2008-06-06 12:12:18','2008-12-03 14:45:35','2008-12-03 14:45:36',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,1,NULL,3,0,0,NULL,5);
/*!40000 ALTER TABLE `tl_lafrum11_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_message_seq`
--

DROP TABLE IF EXISTS `tl_lafrum11_message_seq`;
CREATE TABLE `tl_lafrum11_message_seq` (
  `uid` bigint(20) NOT NULL auto_increment,
  `root_message_uid` bigint(20) default NULL,
  `message_uid` bigint(20) default NULL,
  `message_level` smallint(6) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKD2C71F88FE939F2A` (`message_uid`),
  KEY `FKD2C71F8845213B4D` (`root_message_uid`),
  CONSTRAINT `FKD2C71F8845213B4D` FOREIGN KEY (`root_message_uid`) REFERENCES `tl_lafrum11_message` (`uid`),
  CONSTRAINT `FKD2C71F88FE939F2A` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_message_seq`
--

LOCK TABLES `tl_lafrum11_message_seq` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_message_seq` VALUES (1,2,2,0),(2,3,3,0),(3,4,4,0),(4,5,5,0),(5,6,6,0),(6,7,7,0);
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_report`
--

DROP TABLE IF EXISTS `tl_lafrum11_report`;
CREATE TABLE `tl_lafrum11_report` (
  `uid` bigint(20) NOT NULL auto_increment,
  `comment` text,
  `release_date` datetime default NULL,
  `mark` float default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_report`
--

LOCK TABLES `tl_lafrum11_report` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_tool_session`
--

DROP TABLE IF EXISTS `tl_lafrum11_tool_session`;
CREATE TABLE `tl_lafrum11_tool_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `version` int(11) NOT NULL,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `mark_released` smallint(6) default NULL,
  `forum_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK5A04D7AE131CE31E` (`forum_uid`),
  CONSTRAINT `FK5A04D7AE131CE31E` FOREIGN KEY (`forum_uid`) REFERENCES `tl_lafrum11_forum` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lafrum11_tool_session`
--

LOCK TABLES `tl_lafrum11_tool_session` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_tool_session` VALUES (1,0,NULL,NULL,1,0,3,26,'Everybody learners');
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_attachment`
--

DROP TABLE IF EXISTS `tl_lagmap10_attachment`;
CREATE TABLE `tl_lagmap10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `gmap_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKBA2ECCB274028C80` (`gmap_uid`),
  CONSTRAINT `FKBA2ECCB274028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_attachment`
--

LOCK TABLES `tl_lagmap10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lagmap10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_configuration`
--

DROP TABLE IF EXISTS `tl_lagmap10_configuration`;
CREATE TABLE `tl_lagmap10_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_configuration`
--

LOCK TABLES `tl_lagmap10_configuration` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_configuration` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_configuration` VALUES (1,'GmapKey','ABQIAAAAvPAE96y1iioFQOnrP1RCBxQ3ZLSPwrKlL-Fn7FdXNTuNedFYMRT30phEMjEwUhQPccHtJ9JNU1mknQ');
/*!40000 ALTER TABLE `tl_lagmap10_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_gmap`
--

DROP TABLE IF EXISTS `tl_lagmap10_gmap`;
CREATE TABLE `tl_lagmap10_gmap` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `allow_edit_markers` bit(1) default NULL,
  `show_all_markers` bit(1) default NULL,
  `limit_markers` bit(1) default NULL,
  `max_markers` int(11) default NULL,
  `allow_zoom` bit(1) default NULL,
  `allow_terrain` bit(1) default NULL,
  `allow_satellite` bit(1) default NULL,
  `allow_hybrid` bit(1) default NULL,
  `map_center_latitude` double default NULL,
  `map_center_longitude` double default NULL,
  `map_zoom` int(11) default NULL,
  `map_type` varchar(20) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  `default_geocoder_address` varchar(255) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_gmap`
--

LOCK TABLES `tl_lagmap10_gmap` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_gmap` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_gmap` VALUES (1,NULL,NULL,NULL,'Gmap','Instructions',0x00,0x00,'','',0x00,0x00,12,0x01,0x01,0x00,1,0x01,0x00,0x01,0x01,-33.774322,151.111988,13,'G_NORMAL_MAP',0x00,'','Macquarie University, Sydney NSW'),(2,'2008-11-14 14:32:00','2008-11-10 12:52:48',1,'Google Maps Demonstration','<div>\n<div><img width=\"125\" height=\"52\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmap.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(255, 153, 0);\"><b>Google Maps</b></span></span> </div>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>The <b>Google Maps </b>Activity allows sequence Authors to create maps or satellite images with annotated place markers.<br />\nDuring the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><b>NOTE: </b>For the Google Maps activity to function, an API key must be added to your LAMS server.&nbsp; <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Google+Map+Key\" target=\"_blank\">Instructions to do this are here.</a></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>\n</div>',0x00,0x00,'','',0x00,0x00,51,0x01,0x01,0x00,0,0x01,0x00,0x01,0x01,-33.7746794632346,151.11565053463,17,'G_SATELLITE_MAP',0x00,'','Macquarie University, Sydney NSW'),(3,'2008-12-03 14:45:35','2008-11-10 12:52:48',1,'Google Maps Demonstration','<div>\n<div><img width=\"125\" height=\"52\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmap.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(255, 153, 0);\"><b>Google Maps</b></span></span> </div>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>The <b>Google Maps </b>Activity allows sequence Authors to create maps or satellite images with annotated place markers.<br />\nDuring the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><b>NOTE: </b>For the Google Maps activity to function, an API key must be added to your LAMS server.&nbsp; <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Google+Map+Key\" target=\"_blank\">Instructions to do this are here.</a></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>\n</div>',0x00,0x00,'','',0x00,0x00,82,0x01,0x01,0x00,0,0x01,0x00,0x01,0x01,-33.7746794632346,151.11565053463,17,'G_SATELLITE_MAP',0x00,'','Macquarie University, Sydney NSW');
/*!40000 ALTER TABLE `tl_lagmap10_gmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_marker`
--

DROP TABLE IF EXISTS `tl_lagmap10_marker`;
CREATE TABLE `tl_lagmap10_marker` (
  `uid` bigint(20) NOT NULL auto_increment,
  `longitude` double default NULL,
  `latitude` double default NULL,
  `info_window_message` text,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `is_authored` bit(1) default NULL,
  `gmap_uid` bigint(20) default NULL,
  `title` varchar(55) default NULL,
  `created_by` bigint(20) default NULL,
  `updated_by` bigint(20) default NULL,
  `gmap_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK10F2274974028C80` (`gmap_uid`),
  KEY `FK10F22749C5F056D9` (`gmap_session_uid`),
  KEY `FK10F22749EF5F6920` (`updated_by`),
  KEY `FK10F22749529F7FD3` (`created_by`),
  CONSTRAINT `FK10F22749529F7FD3` FOREIGN KEY (`created_by`) REFERENCES `tl_lagmap10_user` (`uid`),
  CONSTRAINT `FK10F2274974028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`),
  CONSTRAINT `FK10F22749C5F056D9` FOREIGN KEY (`gmap_session_uid`) REFERENCES `tl_lagmap10_session` (`uid`),
  CONSTRAINT `FK10F22749EF5F6920` FOREIGN KEY (`updated_by`) REFERENCES `tl_lagmap10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_marker`
--

LOCK TABLES `tl_lagmap10_marker` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_marker` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_marker` VALUES (1,151.115612983704,-33.7751253687174,'Macquarie%20E-Learning%20Centre%20of%20Excellence','2008-11-10 12:39:46','2008-11-10 12:39:46',0x01,2,'MELCOE',NULL,NULL,NULL),(2,151.115612983704,-33.7751253687174,'Macquarie%20E-Learning%20Centre%20of%20Excellence','2008-11-10 12:39:46','2008-11-10 12:39:46',0x01,3,'MELCOE',NULL,NULL,NULL),(3,151.115612983704,-33.7751253687174,'Macquarie%20E-Learning%20Centre%20of%20Excellence','2008-11-10 12:39:46','2008-11-10 12:39:46',0x01,3,'MELCOE',NULL,NULL,1);
/*!40000 ALTER TABLE `tl_lagmap10_marker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_session`
--

DROP TABLE IF EXISTS `tl_lagmap10_session`;
CREATE TABLE `tl_lagmap10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `gmap_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK519D516774028C80` (`gmap_uid`),
  CONSTRAINT `FK519D516774028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_session`
--

LOCK TABLES `tl_lagmap10_session` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_session` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_session` VALUES (1,NULL,NULL,NULL,31,'Everybody learners',3);
/*!40000 ALTER TABLE `tl_lagmap10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_user`
--

DROP TABLE IF EXISTS `tl_lagmap10_user`;
CREATE TABLE `tl_lagmap10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `gmap_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK7CB3F69AC5F056D9` (`gmap_session_uid`),
  CONSTRAINT `FK7CB3F69AC5F056D9` FOREIGN KEY (`gmap_session_uid`) REFERENCES `tl_lagmap10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lagmap10_user`
--

LOCK TABLES `tl_lagmap10_user` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_user` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_user` VALUES (1,5,'Test','test1','One',0x00,NULL);
/*!40000 ALTER TABLE `tl_lagmap10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_content`
--

DROP TABLE IF EXISTS `tl_lamc11_content`;
CREATE TABLE `tl_lamc11_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime default NULL,
  `update_date` datetime default NULL,
  `reflect` tinyint(1) NOT NULL default '0',
  `questions_sequenced` tinyint(1) NOT NULL default '0',
  `created_by` bigint(20) NOT NULL default '0',
  `run_offline` tinyint(1) NOT NULL default '0',
  `define_later` tinyint(1) NOT NULL default '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_in_use` tinyint(1) NOT NULL default '0',
  `retries` tinyint(1) NOT NULL default '0',
  `pass_mark` int(11) default NULL,
  `show_report` tinyint(1) NOT NULL default '0',
  `reflectionSubject` text,
  `showMarks` tinyint(1) NOT NULL default '0',
  `randomize` tinyint(1) NOT NULL default '0',
  `displayAnswers` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_content_1` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_content`
--

LOCK TABLES `tl_lamc11_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_content` VALUES (1,16,'MCQ','Instructions','2008-12-03 14:40:31',NULL,0,0,1,0,0,'','',0,0,0,0,NULL,0,0,1),(2,42,'My Lost Beach Ball','<h2>This is a demonstration Multiple Choice Quiz.</h2>\n<div><b>As you can see, LAMS provides a space for Instructions and Comments (here)<br />\nand lists all the questions and choices for the activity below.</b></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h4>Read the short story below and answer the three questions:</h4>\n<div style=\"text-align: center;\"><b><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">One day my Dad bought me a new <span style=\"color: rgb(255, 0, 0);\">red </span>beach ball.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">My brother and I took it down to the beach to play with.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">We were playing Soccer when he kicked the ball into the water, accidentally.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">The ball floated out to sea before we could rescue it.</span></b></div>\n<div style=\"text-align: left;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><span style=\"font-size: smaller;\"><i> </i></span></div>\n<div style=\"text-align: center;\"><b><span style=\"font-size: smaller;\"><i>HINT</i></span></b><span style=\"font-size: smaller;\"><i>:&nbsp; The correct answers are marked with an asterisk!</i></span></div>',NULL,'2008-04-11 15:37:41',0,0,5,0,0,'','',0,1,3,0,'',1,1,1),(3,83,'My Lost Beach Ball','<h2>This is a demonstration Multiple Choice Quiz.</h2>\n<div><b>As you can see, LAMS provides a space for Instructions and Comments (here)<br />\nand lists all the questions and choices for the activity below.</b></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h4>Read the short story below and answer the three questions:</h4>\n<div style=\"text-align: center;\"><b><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">One day my Dad bought me a new <span style=\"color: rgb(255, 0, 0);\">red </span>beach ball.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">My brother and I took it down to the beach to play with.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">We were playing Soccer when he kicked the ball into the water, accidentally.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">The ball floated out to sea before we could rescue it.</span></b></div>\n<div style=\"text-align: left;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><span style=\"font-size: smaller;\"><i> </i></span></div>\n<div style=\"text-align: center;\"><b><span style=\"font-size: smaller;\"><i>HINT</i></span></b><span style=\"font-size: smaller;\"><i>:&nbsp; The correct answers are marked with an asterisk!</i></span></div>',NULL,'2008-04-11 15:37:41',0,0,5,0,0,'','',0,1,3,0,'',1,1,1);
/*!40000 ALTER TABLE `tl_lamc11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_options_content`
--

DROP TABLE IF EXISTS `tl_lamc11_options_content`;
CREATE TABLE `tl_lamc11_options_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `correct_option` tinyint(1) NOT NULL default '0',
  `mc_que_content_id` bigint(20) NOT NULL,
  `mc_que_option_text` varchar(250) default NULL,
  `displayOrder` int(5) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `mc_que_content_id` (`mc_que_content_id`),
  CONSTRAINT `FK_tl_lamc11_options_content_1` FOREIGN KEY (`mc_que_content_id`) REFERENCES `tl_lamc11_que_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_options_content`
--

LOCK TABLES `tl_lamc11_options_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_options_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_options_content` VALUES (1,0,1,'Answer 1',1),(2,1,1,'Answer 2',2),(3,1,2,'Dad *',3),(4,0,2,'Mum',2),(5,0,2,'Santa Claus',1),(6,0,3,'Yellow',1),(7,1,3,'Red *',2),(8,0,3,'Green',3),(9,0,4,'Rugby',3),(10,1,4,'Soccer *',2),(11,0,4,'Cricket',1),(12,0,5,'Mum',2),(13,0,5,'Santa Claus',1),(14,1,5,'Dad *',3),(15,1,6,'Red *',2),(16,0,6,'Yellow',1),(17,0,6,'Green',3),(18,1,7,'Soccer *',2),(19,0,7,'Rugby',3),(20,0,7,'Cricket',1);
/*!40000 ALTER TABLE `tl_lamc11_options_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_que_content`
--

DROP TABLE IF EXISTS `tl_lamc11_que_content`;
CREATE TABLE `tl_lamc11_que_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question` text,
  `mark` int(5) NOT NULL default '1',
  `display_order` int(5) default NULL,
  `mc_content_id` bigint(20) NOT NULL,
  `feedback` text,
  PRIMARY KEY  (`uid`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc11_que_content_1` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_que_content`
--

LOCK TABLES `tl_lamc11_que_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_que_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_que_content` VALUES (1,'A Sample question?',1,1,1,NULL),(2,'<div>Who bought the ball?</div>',1,1,2,''),(3,'<div>What colour was the ball?</div>',1,2,2,''),(4,'<div>What game was being played on the beach?</div>',1,3,2,''),(5,'<div>Who bought the ball?</div>',1,1,3,''),(6,'<div>What colour was the ball?</div>',1,2,3,''),(7,'<div>What game was being played on the beach?</div>',1,3,3,'');
/*!40000 ALTER TABLE `tl_lamc11_que_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_que_usr`
--

DROP TABLE IF EXISTS `tl_lamc11_que_usr`;
CREATE TABLE `tl_lamc11_que_usr` (
  `uid` bigint(20) NOT NULL auto_increment,
  `que_usr_id` bigint(20) NOT NULL,
  `mc_session_id` bigint(20) NOT NULL,
  `username` varchar(255) default NULL,
  `fullname` varchar(255) default NULL,
  `responseFinalised` tinyint(1) NOT NULL default '0',
  `viewSummaryRequested` tinyint(1) NOT NULL default '0',
  `last_attempt_order` int(11) default NULL,
  `last_attempt_total_mark` int(11) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `mc_session_id` (`mc_session_id`),
  CONSTRAINT `FK_tl_lamc11_que_usr_1` FOREIGN KEY (`mc_session_id`) REFERENCES `tl_lamc11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_que_usr`
--

LOCK TABLES `tl_lamc11_que_usr` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_que_usr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamc11_que_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_session`
--

DROP TABLE IF EXISTS `tl_lamc11_session`;
CREATE TABLE `tl_lamc11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `mc_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime default NULL,
  `session_end_date` datetime default NULL,
  `session_name` varchar(100) default NULL,
  `session_status` varchar(100) default NULL,
  `mc_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_session_1` (`mc_session_id`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc_session_1` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_session`
--

LOCK TABLES `tl_lamc11_session` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_session` DISABLE KEYS */;
INSERT INTO `tl_lamc11_session` VALUES (1,16,'2008-12-03 14:45:36',NULL,'Everybody learners','INCOMPLETE',3);
/*!40000 ALTER TABLE `tl_lamc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lamc11_uploadedfile`;
CREATE TABLE `tl_lamc11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL auto_increment,
  `uuid` varchar(255) NOT NULL,
  `mc_content_id` bigint(20) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  PRIMARY KEY  (`submissionId`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc11_uploadedFile` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_uploadedfile`
--

LOCK TABLES `tl_lamc11_uploadedfile` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_uploadedfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamc11_uploadedfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_usr_attempt`
--

DROP TABLE IF EXISTS `tl_lamc11_usr_attempt`;
CREATE TABLE `tl_lamc11_usr_attempt` (
  `uid` bigint(20) NOT NULL auto_increment,
  `que_usr_id` bigint(20) NOT NULL,
  `mc_que_content_id` bigint(20) NOT NULL,
  `mc_que_option_id` bigint(20) NOT NULL,
  `attempt_time` datetime default NULL,
  `isAttemptCorrect` tinyint(1) NOT NULL default '0',
  `mark` varchar(255) default NULL,
  `passed` tinyint(1) NOT NULL default '0',
  `attemptOrder` int(11) NOT NULL default '1',
  `finished` tinyint(1) default '0',
  PRIMARY KEY  (`uid`),
  KEY `mc_que_content_id` (`mc_que_content_id`),
  KEY `mc_que_option_id` (`mc_que_option_id`),
  KEY `que_usr_id` (`que_usr_id`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_2` FOREIGN KEY (`mc_que_content_id`) REFERENCES `tl_lamc11_que_content` (`uid`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_3` FOREIGN KEY (`mc_que_option_id`) REFERENCES `tl_lamc11_options_content` (`uid`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_4` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_lamc11_que_usr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lamc11_usr_attempt`
--

LOCK TABLES `tl_lamc11_usr_attempt` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_usr_attempt` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamc11_usr_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_attachment`
--

DROP TABLE IF EXISTS `tl_lanb11_attachment`;
CREATE TABLE `tl_lanb11_attachment` (
  `attachment_id` bigint(20) NOT NULL auto_increment,
  `nb_content_uid` bigint(20) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `uuid` bigint(20) NOT NULL,
  `version_id` bigint(20) default NULL,
  `online_file` tinyint(1) NOT NULL,
  PRIMARY KEY  (`attachment_id`),
  KEY `nb_content_uid` (`nb_content_uid`),
  CONSTRAINT `FK_tl_lanb11_attachment_1` FOREIGN KEY (`nb_content_uid`) REFERENCES `tl_lanb11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lanb11_attachment`
--

LOCK TABLES `tl_lanb11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lanb11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_content`
--

DROP TABLE IF EXISTS `tl_lanb11_content`;
CREATE TABLE `tl_lanb11_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `nb_content_id` bigint(20) NOT NULL,
  `title` text,
  `content` text,
  `online_instructions` text,
  `offline_instructions` text,
  `define_later` tinyint(1) default NULL,
  `force_offline` tinyint(1) default NULL,
  `reflect_on_activity` tinyint(1) default NULL,
  `reflect_instructions` text,
  `content_in_use` tinyint(1) default NULL,
  `creator_user_id` bigint(20) default NULL,
  `date_created` datetime default NULL,
  `date_updated` datetime default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `nb_content_id` (`nb_content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lanb11_content`
--

LOCK TABLES `tl_lanb11_content` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_content` DISABLE KEYS */;
INSERT INTO `tl_lanb11_content` VALUES (1,2,'Noticeboard','Content','','',0,0,0,'Reflect on noticeboard',0,NULL,'2008-12-03 14:38:46',NULL),(2,17,'LAMS101 - Lesson One','<div><span class=\"Apple-style-span\" style=\"orphans: 2; word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); widows: 2; text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; font-size-adjust: none; font-stretch: normal;\">\n<h2>So What is LAMS?</h2>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"><b>Congratulations</b> - if you can see this sequence, LAMS is up and running on your server. You&rsquo;ve also figured out how to log in, and participate in a &quot;sequence&quot; - that&rsquo;s what we call a collection of LAMS activities.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"> </span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">In a word, that&rsquo;s what LAMS does - it is a&nbsp;<b>S</b>ystem that&nbsp;<b>M</b>anages sequences of <b>L</b>earning <b>A</b>ctivities. &nbsp;A <b>Sequence </b>is an online digital lesson that simulates the classroom environment in a virtual space.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are first created by <b>Authors, </b>and are then run for different groups of <b>Learners </b>while a <b>Monitor</b> supervises. &nbsp;We&rsquo;ll look more at these <b>Roles </b>in LAMS in Lesson Two of LAMS101.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are made up of activities - each of which has a particular function, either:</span></p>\n<span style=\"font-size: small;\">\n<h4 style=\"text-align: center;\"><span style=\"font-size: medium;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative &nbsp;</b></span><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative &nbsp;</b></span><span style=\"color: rgb(128, 0, 128);\"><b>Assessment &nbsp;</b></span><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span></h4>\n</span>\n<p><span style=\"font-size: small;\">Sequences can include a range of individual tasks, small group work and whole class activities based on both content and collaboration.</span></p>\n<p><span style=\"font-size: small;\">When you use LAMS, not only are you creating online activities for your learners, you also create a structured lesson plan at the same time.&nbsp; We&rsquo;ll talk about this <b>Visual Authoring Environment </b>in a few moments.</span></p>\n<h2>... and what&rsquo;s with the Sheep?</h2>\n<b>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><img width=\"116\" vspace=\"12\" hspace=\"10\" height=\"103\" border=\"0\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image/dolly_sml.gif\" alt=\"dolly the sheep\" /></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Dolly is our LAMS mascot.&nbsp; Back when LAMS was being redesigned for version 2.0, it was the &quot;next-generation&quot; of LAMS.&nbsp; Dolly the sheep was the first mammal to have been successfully cloned from an adult cell, therefore, she was the next generation LAMB (see what we did there?).</p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Our marvelous designer Shelly is the official mother of Dolly and also our <span class=\"nobr\"><a href=\"http://demo.lamscommunity.org/lams/images/css/lams_login_bar.jpg\" rel=\"nofollow\">front page model</a></span>. So all kudos to her!</p>\n<hr />\n&nbsp;</b></span></div>\n<div style=\"text-align: right;\"><i>To progress to the next activity in this sequence, click the button down here.</i></div>','','',0,0,0,'Reflect on noticeboard',0,1,'2008-03-12 16:38:33','2008-06-05 09:52:50'),(3,18,'Creating Your Own Sequences','<h2>LAMS Authoring</h2>\n<div><span style=\"font-size: small;\">LAMS provides you with a <b>visual authoring environment </b>for creating, storing and re-using activity sequences. The drag-and-drop interface is central to the LAMS authoring environment. Teachers drag-and-drop activities into the authoring area and then join the activities together by drawing &quot;Transitions&quot; to produce a learning sequence. This workflow model is what principally distinguishes LAMS from other more content based online learning platforms. LAMS has a wide range of tools designed for use in a range of educational contexts by teachers and students with varying levels of technical expertise.</span></div>\n<h3>Benefits of LAMS</h3>\n<div><span style=\"font-size: small;\">Creating a lesson in LAMS means that:</span></div>\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<ul>\n    <li><span style=\"font-size: small;\"><b>You can plan all aspects of the lesson.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can preview the lesson from the perspective of your learners.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You create a visual overview of the lesson, and can easily identify the learning styles addressed.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can create a standardised set of learning templates that can easily be modified for later use.</b> </span></li>\n</ul>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i><img width=\"116\" height=\"103\" align=\"right\" alt=\"\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//dolly_sml.gif\" /></i>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next animation will demonstrate creating a lesson in LAMS.</b></div>','','',0,0,0,'',0,11,'2008-04-03 16:11:54','2008-05-29 16:55:57'),(4,20,'The tip of the iceberg...','<p><span style=\"font-size: small;\">LAMS has many, <b>many</b> more functions that you can explore.</span></p>\n<p><span style=\"font-size: small;\">The new release of version 2.1 includes increased functionality in many areas, some of which are explored in the rest of this <b>Getting Started</b> course.</span></p>\n<h2 style=\"text-align: center;\"><b>We Hope You Enjoy Exploring LAMS!</b></h2>\n<p style=\"text-align: center;\">&nbsp;</p>\n<div>&nbsp;</div>','','',0,0,0,'',0,1,'2008-04-04 11:30:06','2008-06-05 11:45:40'),(5,22,'LAMS Environments','<div><img width=\"100\" height=\"90\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image/dolly_lrg.gif\" alt=\"\" style=\"margin-right: 10px;\" /></div>\n<h2>There are four main environments in LAMS</h2>\n<h3>The Learning Environment</h3>\n<div>\n<div>The <b>Learning Environment</b> is the <i>&rsquo;doing&rsquo;</i> of LAMS.</div>\n<div>This is what Learners will see when they log in to LAMS and start participating in a sequence.</div>\n<div>The Learning Environment displays the sequences that are created in <b>Authoring.</b></div>\n<h3>&nbsp;</h3>\n<hr />\n<h3>The Authoring Environment</h3>\n<div>The Authoring Environment is where sequences are put together, modified and shared.</div>\n<div>It contains all of the LAMS activities, and is where authors create their lesson designs.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The Monitoring Environment</h3>\n<div><b>Monitors</b><b> </b>are not necessarily the people who Authored the sequence in the first place - they simply <b>run</b> the lesson for learners, and supervise each learner&rsquo;s progress through the lesson. In practice, Teachers are often both Authors and Monitors for their class(es) of students.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The System Administration Environment</h3>\n<div><b>System Administration</b> is for <b>Technical Management</b> of a LAMS server.</div>\n<div>&nbsp;</div>\n<div>From here, users with Administration rights can change nearly every aspect of the LAMS software.&nbsp;</div>\n<h4 style=\"text-align: center;\"><a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Home\" target=\"_new\">LAMS System Administration is discussed in detail on the Documentation Website.<br />\nClick here for details [Online Link].</a></h4>\n<div>&nbsp;</div>\n<hr />\n</div>\n</div>\n</div>\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\"><b>The next activity will allow you to choose from animated tutorials on each three environments.</b></h5>','','',0,0,0,'',0,1,'2008-03-27 15:12:18','2008-06-05 11:47:45'),(6,23,'The Learning Environment','<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>You&rsquo;re Using the Learning Environment Right Now!<br />\n</b></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">... but if you want to check out more of the features,<br />\nview the following animations!</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><br />\nThe First Animation demonstrates a Learner moving through a sequence.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The Second Animation deals with the new <b>&quot;no-flash&quot; interface</b><b>&nbsp;</b>for the Learning Environment</div>','','',0,0,0,'',0,11,'2008-04-11 17:34:57','2008-05-29 17:16:33'),(7,24,'Are You Sure?','<h2 style=\"text-align: center;\"><span style=\"font-size: small;\">You&rsquo;ve reached the end of this sequence... but are you sure you want to leave?</span></h2>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" align=\"center\">\n    <tbody>\n        <tr>\n            <td><span style=\"font-size: small;\"><img width=\"160\" vspace=\"2\" hspace=\"2\" height=\"377\" border=\"1\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image//progbar.png\" alt=\"\" /><br />\n            </span></td>\n            <td>.      .</td>\n            <td>\n            <div><span style=\"font-size: small;\"> You may have clicked the <b>Next Activity</b> button on the last screen,</span> <span style=\"font-size: small;\">which would bring you here.</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If so, and you want to go back and view more of this sequence, double-click the Icon for the <b>Optional Activity </b>in your <b>Progress Bar </b>(see the graphic at right.)</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">Once there, select an option, and click the <b>Choose </b>button.</span></div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If you really have finished, and want to leave the sequence, click <b>Next Activity</b> below.<br />\n            </span></div>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp; <br />\n</span></div>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,1,'2008-05-02 11:42:23','2008-05-02 12:05:42'),(8,29,'The Structure of LAMS','<h4 style=\"text-align: center;\">LAMS is arranged hierarchically, with sequences organised into Groups and Subgroups.</h4>\n<h2>What does this mean?</h2>\n<div><b>Think of it like this:</b></div>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\">\n    <tbody>\n        <tr>\n            <td>\n            <h4 style=\"text-align: center;\">In Real Life</h4>\n            </td>\n            <td>\n            <h4 style=\"text-align: center;\">In Lams</h4>\n            </td>\n        </tr>\n        <tr>\n            <td>\n            <ul>\n                <li>A <b>Professor</b> teaches a <b>Course, </b>and organises smaller <b>classes.</b></li>\n                <li>Individual <b>Tutors </b>teaches a smaller <b>Class </b>taken from the whole course.</li>\n                <li>A <b>Course </b>has at least one lesson plan, that is applied to all students in the course.</li>\n                <li>The smaller classes can have at least <b>one specific lesson plan </b>for those students.</li>\n            </ul>\n            </td>\n            <td>\n            <ul>\n                <li>A <b>Course Manager</b> creates a main <b>Group, </b>and&nbsp;can create several <b>SubGroups.</b></li>\n                <li><b>Author/Monitors </b>are assigned to one or many <b>Subgroups.</b></li>\n                <li>The <b>Group</b> contains one or more <b>Sequences </b>for all <b>Learners </b>in the <b>Group.</b></li>\n                <li>Likewise, the <b>Subgroup</b> can contain one or many <b>Sequences</b> for those <b>Learners.</b></li>\n            </ul>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>There is also the role of <b>Course Administrator.</b> This role allows the user to modify Groups, Subgroups and Learners inside a course. In practice, this is a secretarial role designed to assist the educator running the course.</div>\n<div>&nbsp;</div>\n<hr />\n<h2>A Working Example</h2>\n<div style=\"text-align: center;\"><span style=\"font-size: small;\"><b>&nbsp;This image shows the different sections that make up a Group in LAMS.</b></span></div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><img border=\"2\" align=\"baseline\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//annotated.png\" alt=\"\" style=\"width: 548px; height: 418px;\" /></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i>Click <b>Next Activity </b>to continue.</i></div>','','',0,0,0,'',0,5,'2008-05-02 13:38:25','2008-06-06 12:09:41'),(9,31,'LAMS Activities','<div>\n<div><img width=\"116\" height=\"103\" align=\"left\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\nSo you&rsquo;ve had a look at the <b>Authoring Environment - </b>but what are the next steps?</div>\n<div>&nbsp;</div>\n<div>This LAMS sequence will show you each of the activities available for use in LAMS 2.1, and techniques for getting the most out of their use.</div>\n<div>&nbsp;</div>\n<div><span style=\"background-color: rgb(255, 255, 153);\"><b><i>This activity also discusses NEW activities added in the 2.2 release.</i></b></span></div>\n<div>&nbsp;</div>\n<h2>&nbsp;</h2>\n<h2 style=\"text-align: center;\">Activity Types</h2>\n<div>Earlier, we discussed the four different types of LAMS activities:&nbsp;<span class=\"Apple-style-span\" style=\"word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; orphans: 2; widows: 2; font-size-adjust: none; font-stretch: normal;\"><center>\n<h4><span style=\"font-size: small;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(128, 0, 128);\"><b>Assessement,</b></span></span>&nbsp; <span style=\"color: rgb(0, 0, 0);\">and </span><span style=\"font-size: small;\"><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span>&nbsp;</h4>\n</center><center>\n<div style=\"text-align: left;\">\n<div style=\"text-align: left;\">The rest of this sequence will let you view demonstrations of each of the LAMS activities, and briefly discuss how they might be best used in a lesson.</div>\n</div>\n</center></span></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div style=\"text-align: right;\">&nbsp;<b><i>Click &rsquo;Next Activity&rsquo; to move on.</i></b></div>','','',0,0,0,'',0,8,'2008-04-10 11:56:54','2008-11-14 13:14:33'),(10,32,'Informative Tools','<div>The <span style=\"color: rgb(255, 102, 0);\"><b>Informative </b></span>tools in LAMS allow Authors and Monitors to share information with Learners, and (in the case of Share Resources) allow Learners to share information with each other.</div>\n<div>&nbsp;</div>\n<div>Informative tools display on the Authoring Canvas with an <span style=\"color: rgb(255, 102, 0);\">orange icon</span>.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Noticeboard</h2>\n<div>The <b>Noticeboard </b>provides a simple way of providing information and content to the learners. The activity can display text, images, audio, video, links and other HTML content.</div>\n<div>&nbsp;</div>\n<h5>Example: The activity you&rsquo;re looking at right now is a Noticeboard!</h5>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//larsrc11.PNG\" />Share Resources</h2>\n<div><b>Resource Sharing</b> allows Authors to create lists of resources such as:</div>\n<div>&nbsp;</div>\n<ul>\n    <li>URL hyperlinks</li>\n    <li>Complete websites - created and uploaded to LAMS as a .zip file</li>\n    <li>Individual files, such as documents, PDF&rsquo;s, Audio and Video files</li>\n    <li>and even IMS content packages.</li>\n</ul>\n<div>&nbsp;</div>\n<div>The tool can also be set-up so that Learners can add their own resources for other learners to see.&nbsp;</div>\n<div>&nbsp;</div>\n<h5><span style=\"font-weight: bold;\">Example: The animations in these &rsquo;Getting Started&rsquo; were added to LAMS by creating a zipped website and uploading the file into a Share Resources activity.</span></h5>\n<div>&nbsp;</div>\n<hr />\n<h4 style=\"text-align: left;\">Coming Up Next...</h4>\n<ol>\n    <li style=\"text-align: left;\">Advanced Noticeboard Features</li>\n    <li style=\"text-align: left;\">Share Resources Demonstration</li>\n    <li style=\"text-align: left;\"><span style=\"background-color: rgb(255, 255, 153);\">Spreadsheet Demonstration (New to LAMS 2.2)</span></li>\n    <div style=\"text-align: left;\">&nbsp;</div>\n</ol>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:07:18','2008-11-14 13:14:13'),(11,33,'Noticeboard Activity','<h2>Advanced Noticeboard Tricks</h2>\n<div><b>You can use the noticeboard to embed any web object using HTML.</b></div>\n<h3>For example</h3>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"1\">\n    <tbody>\n        <tr>\n            <td><center>\n            <h4>YouTube Videos</h4>\n            <div><embed src=\"http://www.youtube.com/v/vihvpfXw_C8\" type=\"application/x-shockwave-flash\"></embed></div>\n            </center></td>\n            <td><center>\n            <h4>MP3 Files</h4>\n            <div><embed width=\"200\" height=\"40\" type=\"audio/mpeg\" autostart=\"false\" src=\"http://saturn.melcoe.mq.edu.au/lamspodcast/James_Dalziel_Getting_Best_out_of.mp3\"></embed>\n            <p align=\"center\"><strong>&nbsp;LAMS PODCAST</strong> with <strong>ALAN CARRINGTON</strong> (CLPD Adelaide Uni)<br />\n            and <strong>JAMES DALZIEL</strong> (MELCOE, Macquarie Uni)</p>\n            </div>\n            </center></td>\n        </tr>\n    </tbody>\n</table>\n<h4>&nbsp;</h4>\n<hr />\n<h4>And Even Complete External Websites</h4>\n<center>\n<div><iframe width=\"95%\" height=\"278\" src=\"http://www.melcoe.mq.edu.au/\">LAMS Community</iframe></div>\n<div>&nbsp;</div>\n</center><hr />\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration &rsquo;Share Resources&rsquo; activity.</b></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:35:26','2008-11-14 13:13:28'),(12,35,'CONGRATULATIONS!','<h2><img width=\"240\" height=\"240\" align=\"right\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/diploma.jpeg\" alt=\"\" />!!! CONGRATULATIONS !!!</h2>\n<h4>You&rsquo;ve finished LAMS101:Getting Started!</h4>\n<div>&nbsp;</div>\n<div><b>Get out there and start making sequences!</b></div>\n<div>&nbsp;</div>\n<div>Or, if you want some more ideas,<br />\ncheck out the sequence-sharing goodness<br />\nat the LAMS Community Website.</div>\n<div>&nbsp;</div>\n<div><a target=\"_blank\" href=\"http://lamscommunity.org/\"><b>http://lamscommunity.org/</b></a></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,4,'2008-04-11 16:49:00','2008-11-10 12:54:33'),(13,36,'Assessment Activities','<div>The <b><span style=\"color: rgb(128, 0, 128);\">Assessment </span></b>tools in LAMS provide a vehicle for sequence monitors to collect Learner responses to a topic.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lamc11.PNG\" alt=\"\" /><span style=\"color: rgb(128, 0, 128);\">Multiple Choice</span></h2>\n<div>&nbsp;The <b>Multiple Choice Quiz</b> delivers a series of Multiple Choice questions to learners, and the advanced features of this tool provide a high level of customisation for Authors as to how the quiz is administered.</div>\n<div>&nbsp;</div>\n<div>When the Quiz is complete, LAMS will automatically tally the marks from each learner and display the statistics to sequence monitors.</div>\n<div>&nbsp;</div>\n<div>Authors can also choose to release Learner marks <i>directly </i>after they have completed the quiz, as a type of <b>formative </b>assessment or <i>delay</i> releasing the marks until a Sequence Monitor chooses to release them, for <b>Summative Assessment</b>. The Activity also allows learners to view the top, bottom and average marks from the quiz.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasbmt11.PNG\" /><span style=\"color: rgb(128, 0, 128);\">Submit Files </span></h2>\n<div>The <b>Submit Files </b>Activity provides a vehicle for Authors to collect Student responses which are created in software outside of LAMS.</div>\n<div>&nbsp;</div>\n<div>This could be an essay in .DOC or .PDF format, a digital presentation, or some sort of multimedia file.</div>\n<div>&nbsp;</div>\n<div>Learners log in to LAMS, and are able to upload their file to the LAMS server as an attachment.&nbsp; Sequence Monitors can view these files, comment on them and give each individual file a mark, which is in turn accessible by Learners.</div>\n<div>&nbsp;</div>\n<div>Like the MCQ, sequence authors can choose to allow learners several attempts at submission, allowing for feedback and creating a formative assessment style, or alternatively, lock the activity after submission, which creates a summative assessment style.</div>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\">The next activity is a demonstration <b>Multiple Choice </b>quiz,<br />\nfollowed by a demonstration <b>Submit Files</b> activity.</div>','','',0,0,0,'',0,8,'2008-04-10 14:39:45','2008-11-14 13:14:54'),(14,38,'DimDim Web Conferencing','<div style=\"text-align: center;\">Also available for use in LAMS 2.2 is the <b>DimDim Web Conferencing Activity</b>.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The documentation is included below (You will need internet access to view this page.)</div>\n<hr />\n<div>&nbsp;</div>\n<div align=\"center\"><iframe width=\"95%\" height=\"400\" src=\"http://wiki.lamsfoundation.org/display/lamsdocs/laddim10?decorator=printable\">LAMS Community</iframe></div>','','',0,0,0,'',0,4,'2008-11-10 12:45:56','2008-11-10 12:48:40'),(15,39,'Reflective Activities','<div>Reflective tools ask learners to analyse their own learning throughout a sequence,<br />\nor alternatively, collects responses from students and asks them to consider trends in the resulting data.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//laqa11.PNG\" /> Question and Answer</h2>\n<div>This activity creates a series of Questions and Short-Answer responses for Learners.</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">The sequence Author can pen their own responses to each question, which are displayed to learners after they have submitted their own answers.</p>\n<p style=\"margin-bottom: 0cm;\">The activity can also be configured to display answers collected from all learners in the sequence or group.</p>\n<p style=\"margin-bottom: 0cm;\">The <b>Question and Answer Activity</b> allows teachers to pose a question or questions to learners individually, and after they have entered their response, to see the responses of all their peers presented on a single answer screen.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasurv11.PNG\" /> Survey</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The Survey Tool is similar to Multiple Choice, in that it presents learners with a number of questions and collects their responses. However, unlike Multiple Choice there are no right or wrong answers - the system just collates all answers for analysis by the teacher in the monitoring area.</p>\n<p style=\"margin-bottom: 0cm;\">Question types include single response, multiple response and open text entry, and questions can be mandatory or optional.  For multiple choice questions, Learners can also be provided with the option of adding their own answer to a list.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lavote11.PNG\" /> Voting</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Voting Activity</b> allows teachers to provide learners with a list of options to &quot;Vote&quot; on. It is similar to the Q&amp;A Activity in that Voting shows first one screen with all the available voting options, then a screen showing the individual learner&rsquo;s selection, then a screen showing <i>&quot;progressive&quot;</i> voting results (i.e., a non-editable screen where learners can see results from themselves and other learners over time). Finally, there is Summary Screen of group results.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\">The next three activities demonstrate the Q&amp;A, Survey and Voting activities.</h5>\n</div>\n</div>','','',0,0,0,'',0,8,'2008-04-11 16:26:32','2008-11-14 14:30:21'),(16,40,'Collaborative Activities','<div>The <b><span style=\"color: rgb(255, 153, 0);\">Collaborative </span></b>tools in LAMS allow Learners to team up and solve problems, discuss ideas or share solutions together.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2>&nbsp;<span style=\"color: rgb(255, 153, 0);\"><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lachat11.PNG\" alt=\"\" /> Chat Activity</span></h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Chat Activity</b> runs a live (synchronous) discussion for learners.<br />\nThis is similar to <i>Instant Messaging</i> (IM) software that most learners will be familiar with.</p>\n</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lafrum11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Forum Activity</span></h2>\n<div>&nbsp;</div>\n<div>&nbsp;The <b>Forum Activity</b> provides an asynchronous discussion environment for learners, with discussion threads initially created by the teacher.\n<p style=\"margin-bottom: 0cm;\">Forums can be &rsquo;locked&rsquo; as an activity which is only available for the period of the specific activity (i.e.: no more contributions can be made once the learner clicks &rsquo;Finish&rsquo;, or they can be &rsquo;unlocked&rsquo;, in that learners can add to the Forum throughout the life of the whole sequence.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"126\" height=\"53\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lascrb11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Scribe Activity</span></h2>\n<div>&nbsp;The <b>Scribe Activity</b> is, by default, not enabled as a stand-alone activity.<br />\n<p style=\"margin-bottom: 0cm;\">It allows a group of Learners to collaborate and create a written report with one Learner as the &rsquo;Scribe,&rsquo; or Writer for the report. The activity is most effective when paired with another collaborative activity. <em class=\"western\">In the current release of LAMS (2.2) Scribe is available to be paired with either Chat or Forum.</em></p>\n<h3>Using the Scribe Activity</h3>\n<p style=\"margin-bottom: 0cm;\">The Scribe Activity should always used in conjunction with another activity that gives it context. For instance, if used with Chat, then the Scribe (one particular learner) is expected to collect or summarize the output of the Chat discussion into a succinct set of sentences. Once the scribe has done this, they publish the summary to the rest of the learners who vote whether they agree with the summary as proposed by the scribe. If not, they can ask the scribe to modify the report until all learners agree on the report.</p>\n<p style=\"margin-bottom: 0cm;\">Once the Scribe submits the report, the other learners in the group or class can agree or disagree with the summary. The scribe can finished the scribe task at any time regardless of the number of learners that have agreed with the proposed summaries. The Scribe task can also be finished through the monitoring environment by a teacher or instructor.</p>\n<p style=\"margin-bottom: 0cm;\">Once the scribe task is finished a report of the statements and summary is presented to all learners.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>','','',0,0,0,'',0,8,'2008-04-10 15:41:45','2008-11-14 13:16:43'),(17,43,'Split-Screen Activities','<h2 style=\"text-align: center;\">There are Several Split-Screen activities available to authors<br />\nthat combine several activities on the same screen.</h2>\n<hr />\n<div>&nbsp;</div>\n<div>\n<h2><img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//chatScribe.png\" alt=\"\" /> Chat and Scribe</h2>\n<div>This activity is great for collecting Group Reports.</div>\n<div>&nbsp;</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">It combines a <b><i>Chat</i> Activity </b>with a <b>Scribe Activity</b> for collating the chat group\'s view on questions created by the teacher. When used in small group mode, the tool creates parallel Chat and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</p>\n</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div><br />\n<img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//forumScribe.png\" alt=\"\" />\n<h2>Forum and Scribe</h2>\n<div>The activity ombines a <i>Forum</i> Activity with a <i>Scribe Activity</i> for collating Forum Postings into a written report, which then must be approved by the entire group.</div>\n<div>&nbsp;</div>\n<div>When used in small group mode, the tool creates parallel Forum and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n<h2><img width=\"144\" hspace=\"6\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//shareForum.png\" alt=\"\" />Resource Sharing and Forum</h2>\n<div>This activity allows Learners to submit files and/or URLS to a page viewable by their class or group.</div>\n<div>&nbsp;</div>\n<div>The group then discuss the resources in the Forum.</div>\n<div>&nbsp;</div>\n<div>This is a great way to get Learner\'s evaluating each other\'s work, or to foster some collaborative online research.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration Resource Sharing and Forum activity.</b></div>','','',0,0,0,'',0,1,'2008-04-10 16:29:19','2008-04-11 15:59:24'),(18,49,'Noticeboard','<h2>New Collaborative Activities in LAMS 2.2</h2>\n<h3><b><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmapIcon.png\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Google Maps</span></b></h3>\n<div>The Google Maps tool allows sequence Authors to create maps or satellite images with annotated place markers.&nbsp; During the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><i>A demonstration Gmap activity is coming up.</i></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/wikiIcon.png\" alt=\"\" /> <b><span style=\"color: rgb(255, 153, 0);\">Wiki Activity</span></b></div>\n<div>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>&nbsp;Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p><i>A Wiki Demonstration activity is coming up, which you can explore and edit.</i>&nbsp;</p>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><i><br />\n</i></b></div>','','',0,0,0,'',0,8,'2008-11-14 13:48:47','2008-11-14 13:50:27'),(19,50,'Notebook Activity','<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lantbk11.PNG\" alt=\"\" />Notebook Activity</h2>\n<div>The next LAMS activity to look at is the Notebook activity.</div>\n<div>&nbsp;</div>\n<div>This activity is a bit special because it can be <b>attached to any LAMS activity</b><b> at any point in the lesson.</b><br />\nEach LAMS activity has an option in <i><b>Advanced Properties</b></i> called <i>&quot;attach Notebook with text...&quot;</i></div>\n<div>&nbsp;</div>\n<div>Basically, this allows sequences authors to get Learners to reflect on their knowledge at <b>any point in the sequence - </b>and it&rsquo;s also really easy for monitors to add a notebook to any activity in a sequence without mucking around with transition lines or changing the shape of the sequence.</div>\n<div>&nbsp;</div>\n<div>Of course, the notebook can also be included in sequences as a standalone tool.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-11 16:41:59','2008-11-14 13:51:16'),(20,54,'Data Collection Activity','<h2><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/dacoIcon.png\" alt=\"\" /> Data Collection</h2>\n<div>&nbsp;</div>\n<div><span style=\"border-collapse: separate; color: rgb(0, 0, 0); font-family: Verdana; font-size: 11px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 16px; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px;\" class=\"Apple-style-span\">The Data Collection tool allows Authors to create database style lists for Learners to populate.<br />\nEach record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</span></div>\n<div><b><i>The next Activity demonstrates the use of Data Collection</i></b>.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-11-14 14:26:47','2008-11-14 14:26:47'),(21,57,'LAMS101 - Lesson One','<div><span class=\"Apple-style-span\" style=\"orphans: 2; word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); widows: 2; text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; font-size-adjust: none; font-stretch: normal;\">\n<h2>So What is LAMS?</h2>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"><b>Congratulations</b> - if you can see this sequence, LAMS is up and running on your server. You&rsquo;ve also figured out how to log in, and participate in a &quot;sequence&quot; - that&rsquo;s what we call a collection of LAMS activities.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"> </span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">In a word, that&rsquo;s what LAMS does - it is a&nbsp;<b>S</b>ystem that&nbsp;<b>M</b>anages sequences of <b>L</b>earning <b>A</b>ctivities. &nbsp;A <b>Sequence </b>is an online digital lesson that simulates the classroom environment in a virtual space.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are first created by <b>Authors, </b>and are then run for different groups of <b>Learners </b>while a <b>Monitor</b> supervises. &nbsp;We&rsquo;ll look more at these <b>Roles </b>in LAMS in Lesson Two of LAMS101.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are made up of activities - each of which has a particular function, either:</span></p>\n<span style=\"font-size: small;\">\n<h4 style=\"text-align: center;\"><span style=\"font-size: medium;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative &nbsp;</b></span><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative &nbsp;</b></span><span style=\"color: rgb(128, 0, 128);\"><b>Assessment &nbsp;</b></span><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span></h4>\n</span>\n<p><span style=\"font-size: small;\">Sequences can include a range of individual tasks, small group work and whole class activities based on both content and collaboration.</span></p>\n<p><span style=\"font-size: small;\">When you use LAMS, not only are you creating online activities for your learners, you also create a structured lesson plan at the same time.&nbsp; We&rsquo;ll talk about this <b>Visual Authoring Environment </b>in a few moments.</span></p>\n<h2>... and what&rsquo;s with the Sheep?</h2>\n<b>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><img width=\"116\" vspace=\"12\" hspace=\"10\" height=\"103\" border=\"0\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image/dolly_sml.gif\" alt=\"dolly the sheep\" /></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Dolly is our LAMS mascot.&nbsp; Back when LAMS was being redesigned for version 2.0, it was the &quot;next-generation&quot; of LAMS.&nbsp; Dolly the sheep was the first mammal to have been successfully cloned from an adult cell, therefore, she was the next generation LAMB (see what we did there?).</p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Our marvelous designer Shelly is the official mother of Dolly and also our <span class=\"nobr\"><a href=\"http://demo.lamscommunity.org/lams/images/css/lams_login_bar.jpg\" rel=\"nofollow\">front page model</a></span>. So all kudos to her!</p>\n<hr />\n&nbsp;</b></span></div>\n<div style=\"text-align: right;\"><i>To progress to the next activity in this sequence, click the button down here.</i></div>','','',0,0,0,'Reflect on noticeboard',0,1,'2008-03-12 16:38:33','2008-06-05 09:52:50'),(22,58,'The tip of the iceberg...','<p><span style=\"font-size: small;\">LAMS has many, <b>many</b> more functions that you can explore.</span></p>\n<p><span style=\"font-size: small;\">The new release of version 2.1 includes increased functionality in many areas, some of which are explored in the rest of this <b>Getting Started</b> course.</span></p>\n<h2 style=\"text-align: center;\"><b>We Hope You Enjoy Exploring LAMS!</b></h2>\n<p style=\"text-align: center;\">&nbsp;</p>\n<div>&nbsp;</div>','','',0,0,0,'',0,1,'2008-04-04 11:30:06','2008-06-05 11:45:40'),(23,59,'Creating Your Own Sequences','<h2>LAMS Authoring</h2>\n<div><span style=\"font-size: small;\">LAMS provides you with a <b>visual authoring environment </b>for creating, storing and re-using activity sequences. The drag-and-drop interface is central to the LAMS authoring environment. Teachers drag-and-drop activities into the authoring area and then join the activities together by drawing &quot;Transitions&quot; to produce a learning sequence. This workflow model is what principally distinguishes LAMS from other more content based online learning platforms. LAMS has a wide range of tools designed for use in a range of educational contexts by teachers and students with varying levels of technical expertise.</span></div>\n<h3>Benefits of LAMS</h3>\n<div><span style=\"font-size: small;\">Creating a lesson in LAMS means that:</span></div>\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<ul>\n    <li><span style=\"font-size: small;\"><b>You can plan all aspects of the lesson.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can preview the lesson from the perspective of your learners.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You create a visual overview of the lesson, and can easily identify the learning styles addressed.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can create a standardised set of learning templates that can easily be modified for later use.</b> </span></li>\n</ul>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i><img width=\"116\" height=\"103\" align=\"right\" alt=\"\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//dolly_sml.gif\" /></i>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next animation will demonstrate creating a lesson in LAMS.</b></div>','','',0,0,0,'',0,11,'2008-04-03 16:11:54','2008-05-29 16:55:57'),(24,61,'Are You Sure?','<h2 style=\"text-align: center;\"><span style=\"font-size: small;\">You&rsquo;ve reached the end of this sequence... but are you sure you want to leave?</span></h2>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" align=\"center\">\n    <tbody>\n        <tr>\n            <td><span style=\"font-size: small;\"><img width=\"160\" vspace=\"2\" hspace=\"2\" height=\"377\" border=\"1\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image//progbar.png\" alt=\"\" /><br />\n            </span></td>\n            <td>.      .</td>\n            <td>\n            <div><span style=\"font-size: small;\"> You may have clicked the <b>Next Activity</b> button on the last screen,</span> <span style=\"font-size: small;\">which would bring you here.</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If so, and you want to go back and view more of this sequence, double-click the Icon for the <b>Optional Activity </b>in your <b>Progress Bar </b>(see the graphic at right.)</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">Once there, select an option, and click the <b>Choose </b>button.</span></div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If you really have finished, and want to leave the sequence, click <b>Next Activity</b> below.<br />\n            </span></div>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp; <br />\n</span></div>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,1,'2008-05-02 11:42:23','2008-05-02 12:05:42'),(25,64,'The Structure of LAMS','<h4 style=\"text-align: center;\">LAMS is arranged hierarchically, with sequences organised into Groups and Subgroups.</h4>\n<h2>What does this mean?</h2>\n<div><b>Think of it like this:</b></div>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\">\n    <tbody>\n        <tr>\n            <td>\n            <h4 style=\"text-align: center;\">In Real Life</h4>\n            </td>\n            <td>\n            <h4 style=\"text-align: center;\">In Lams</h4>\n            </td>\n        </tr>\n        <tr>\n            <td>\n            <ul>\n                <li>A <b>Professor</b> teaches a <b>Course, </b>and organises smaller <b>classes.</b></li>\n                <li>Individual <b>Tutors </b>teaches a smaller <b>Class </b>taken from the whole course.</li>\n                <li>A <b>Course </b>has at least one lesson plan, that is applied to all students in the course.</li>\n                <li>The smaller classes can have at least <b>one specific lesson plan </b>for those students.</li>\n            </ul>\n            </td>\n            <td>\n            <ul>\n                <li>A <b>Course Manager</b> creates a main <b>Group, </b>and&nbsp;can create several <b>SubGroups.</b></li>\n                <li><b>Author/Monitors </b>are assigned to one or many <b>Subgroups.</b></li>\n                <li>The <b>Group</b> contains one or more <b>Sequences </b>for all <b>Learners </b>in the <b>Group.</b></li>\n                <li>Likewise, the <b>Subgroup</b> can contain one or many <b>Sequences</b> for those <b>Learners.</b></li>\n            </ul>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>There is also the role of <b>Course Administrator.</b> This role allows the user to modify Groups, Subgroups and Learners inside a course. In practice, this is a secretarial role designed to assist the educator running the course.</div>\n<div>&nbsp;</div>\n<hr />\n<h2>A Working Example</h2>\n<div style=\"text-align: center;\"><span style=\"font-size: small;\"><b>&nbsp;This image shows the different sections that make up a Group in LAMS.</b></span></div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><img border=\"2\" align=\"baseline\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//annotated.png\" alt=\"\" style=\"width: 548px; height: 418px;\" /></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i>Click <b>Next Activity </b>to continue.</i></div>','','',0,0,0,'',0,5,'2008-05-02 13:38:25','2008-06-06 12:09:41'),(26,67,'The Learning Environment','<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>You&rsquo;re Using the Learning Environment Right Now!<br />\n</b></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">... but if you want to check out more of the features,<br />\nview the following animations!</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><br />\nThe First Animation demonstrates a Learner moving through a sequence.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The Second Animation deals with the new <b>&quot;no-flash&quot; interface</b><b>&nbsp;</b>for the Learning Environment</div>','','',0,0,0,'',0,11,'2008-04-11 17:34:57','2008-05-29 17:16:33'),(27,69,'LAMS Environments','<div><img width=\"100\" height=\"90\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image/dolly_lrg.gif\" alt=\"\" style=\"margin-right: 10px;\" /></div>\n<h2>There are four main environments in LAMS</h2>\n<h3>The Learning Environment</h3>\n<div>\n<div>The <b>Learning Environment</b> is the <i>&rsquo;doing&rsquo;</i> of LAMS.</div>\n<div>This is what Learners will see when they log in to LAMS and start participating in a sequence.</div>\n<div>The Learning Environment displays the sequences that are created in <b>Authoring.</b></div>\n<h3>&nbsp;</h3>\n<hr />\n<h3>The Authoring Environment</h3>\n<div>The Authoring Environment is where sequences are put together, modified and shared.</div>\n<div>It contains all of the LAMS activities, and is where authors create their lesson designs.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The Monitoring Environment</h3>\n<div><b>Monitors</b><b> </b>are not necessarily the people who Authored the sequence in the first place - they simply <b>run</b> the lesson for learners, and supervise each learner&rsquo;s progress through the lesson. In practice, Teachers are often both Authors and Monitors for their class(es) of students.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The System Administration Environment</h3>\n<div><b>System Administration</b> is for <b>Technical Management</b> of a LAMS server.</div>\n<div>&nbsp;</div>\n<div>From here, users with Administration rights can change nearly every aspect of the LAMS software.&nbsp;</div>\n<h4 style=\"text-align: center;\"><a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Home\" target=\"_new\">LAMS System Administration is discussed in detail on the Documentation Website.<br />\nClick here for details [Online Link].</a></h4>\n<div>&nbsp;</div>\n<hr />\n</div>\n</div>\n</div>\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\"><b>The next activity will allow you to choose from animated tutorials on each three environments.</b></h5>','','',0,0,0,'',0,1,'2008-03-27 15:12:18','2008-06-05 11:47:45'),(28,70,'Noticeboard Activity','<h2>Advanced Noticeboard Tricks</h2>\n<div><b>You can use the noticeboard to embed any web object using HTML.</b></div>\n<h3>For example</h3>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"1\">\n    <tbody>\n        <tr>\n            <td><center>\n            <h4>YouTube Videos</h4>\n            <div><embed src=\"http://www.youtube.com/v/vihvpfXw_C8\" type=\"application/x-shockwave-flash\"></embed></div>\n            </center></td>\n            <td><center>\n            <h4>MP3 Files</h4>\n            <div><embed width=\"200\" height=\"40\" type=\"audio/mpeg\" autostart=\"false\" src=\"http://saturn.melcoe.mq.edu.au/lamspodcast/James_Dalziel_Getting_Best_out_of.mp3\"></embed>\n            <p align=\"center\"><strong>&nbsp;LAMS PODCAST</strong> with <strong>ALAN CARRINGTON</strong> (CLPD Adelaide Uni)<br />\n            and <strong>JAMES DALZIEL</strong> (MELCOE, Macquarie Uni)</p>\n            </div>\n            </center></td>\n        </tr>\n    </tbody>\n</table>\n<h4>&nbsp;</h4>\n<hr />\n<h4>And Even Complete External Websites</h4>\n<center>\n<div><iframe width=\"95%\" height=\"278\" src=\"http://www.melcoe.mq.edu.au/\">LAMS Community</iframe></div>\n<div>&nbsp;</div>\n</center><hr />\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration &rsquo;Share Resources&rsquo; activity.</b></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:35:26','2008-11-14 13:13:28'),(29,71,'CONGRATULATIONS!','<h2><img width=\"240\" height=\"240\" align=\"right\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/diploma.jpeg\" alt=\"\" />!!! CONGRATULATIONS !!!</h2>\n<h4>You&rsquo;ve finished LAMS101:Getting Started!</h4>\n<div>&nbsp;</div>\n<div><b>Get out there and start making sequences!</b></div>\n<div>&nbsp;</div>\n<div>Or, if you want some more ideas,<br />\ncheck out the sequence-sharing goodness<br />\nat the LAMS Community Website.</div>\n<div>&nbsp;</div>\n<div><a target=\"_blank\" href=\"http://lamscommunity.org/\"><b>http://lamscommunity.org/</b></a></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,4,'2008-04-11 16:49:00','2008-11-10 12:54:33'),(30,72,'Informative Tools','<div>The <span style=\"color: rgb(255, 102, 0);\"><b>Informative </b></span>tools in LAMS allow Authors and Monitors to share information with Learners, and (in the case of Share Resources) allow Learners to share information with each other.</div>\n<div>&nbsp;</div>\n<div>Informative tools display on the Authoring Canvas with an <span style=\"color: rgb(255, 102, 0);\">orange icon</span>.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Noticeboard</h2>\n<div>The <b>Noticeboard </b>provides a simple way of providing information and content to the learners. The activity can display text, images, audio, video, links and other HTML content.</div>\n<div>&nbsp;</div>\n<h5>Example: The activity you&rsquo;re looking at right now is a Noticeboard!</h5>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//larsrc11.PNG\" />Share Resources</h2>\n<div><b>Resource Sharing</b> allows Authors to create lists of resources such as:</div>\n<div>&nbsp;</div>\n<ul>\n    <li>URL hyperlinks</li>\n    <li>Complete websites - created and uploaded to LAMS as a .zip file</li>\n    <li>Individual files, such as documents, PDF&rsquo;s, Audio and Video files</li>\n    <li>and even IMS content packages.</li>\n</ul>\n<div>&nbsp;</div>\n<div>The tool can also be set-up so that Learners can add their own resources for other learners to see.&nbsp;</div>\n<div>&nbsp;</div>\n<h5><span style=\"font-weight: bold;\">Example: The animations in these &rsquo;Getting Started&rsquo; were added to LAMS by creating a zipped website and uploading the file into a Share Resources activity.</span></h5>\n<div>&nbsp;</div>\n<hr />\n<h4 style=\"text-align: left;\">Coming Up Next...</h4>\n<ol>\n    <li style=\"text-align: left;\">Advanced Noticeboard Features</li>\n    <li style=\"text-align: left;\">Share Resources Demonstration</li>\n    <li style=\"text-align: left;\"><span style=\"background-color: rgb(255, 255, 153);\">Spreadsheet Demonstration (New to LAMS 2.2)</span></li>\n    <div style=\"text-align: left;\">&nbsp;</div>\n</ol>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:07:18','2008-11-14 13:14:13'),(31,74,'DimDim Web Conferencing','<div style=\"text-align: center;\">Also available for use in LAMS 2.2 is the <b>DimDim Web Conferencing Activity</b>.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The documentation is included below (You will need internet access to view this page.)</div>\n<hr />\n<div>&nbsp;</div>\n<div align=\"center\"><iframe width=\"95%\" height=\"400\" src=\"http://wiki.lamsfoundation.org/display/lamsdocs/laddim10?decorator=printable\">LAMS Community</iframe></div>','','',0,0,0,'',0,4,'2008-11-10 12:45:56','2008-11-10 12:48:40'),(32,79,'Split-Screen Activities','<h2 style=\"text-align: center;\">There are Several Split-Screen activities available to authors<br />\nthat combine several activities on the same screen.</h2>\n<hr />\n<div>&nbsp;</div>\n<div>\n<h2><img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//chatScribe.png\" alt=\"\" /> Chat and Scribe</h2>\n<div>This activity is great for collecting Group Reports.</div>\n<div>&nbsp;</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">It combines a <b><i>Chat</i> Activity </b>with a <b>Scribe Activity</b> for collating the chat group\'s view on questions created by the teacher. When used in small group mode, the tool creates parallel Chat and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</p>\n</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div><br />\n<img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//forumScribe.png\" alt=\"\" />\n<h2>Forum and Scribe</h2>\n<div>The activity ombines a <i>Forum</i> Activity with a <i>Scribe Activity</i> for collating Forum Postings into a written report, which then must be approved by the entire group.</div>\n<div>&nbsp;</div>\n<div>When used in small group mode, the tool creates parallel Forum and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n<h2><img width=\"144\" hspace=\"6\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//shareForum.png\" alt=\"\" />Resource Sharing and Forum</h2>\n<div>This activity allows Learners to submit files and/or URLS to a page viewable by their class or group.</div>\n<div>&nbsp;</div>\n<div>The group then discuss the resources in the Forum.</div>\n<div>&nbsp;</div>\n<div>This is a great way to get Learner\'s evaluating each other\'s work, or to foster some collaborative online research.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration Resource Sharing and Forum activity.</b></div>','','',0,0,0,'',0,1,'2008-04-10 16:29:19','2008-04-11 15:59:24'),(33,80,'Noticeboard','<h2>New Collaborative Activities in LAMS 2.2</h2>\n<h3><b><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmapIcon.png\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Google Maps</span></b></h3>\n<div>The Google Maps tool allows sequence Authors to create maps or satellite images with annotated place markers.&nbsp; During the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><i>A demonstration Gmap activity is coming up.</i></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/wikiIcon.png\" alt=\"\" /> <b><span style=\"color: rgb(255, 153, 0);\">Wiki Activity</span></b></div>\n<div>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>&nbsp;Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p><i>A Wiki Demonstration activity is coming up, which you can explore and edit.</i>&nbsp;</p>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><i><br />\n</i></b></div>','','',0,0,0,'',0,8,'2008-11-14 13:48:47','2008-11-14 13:50:27'),(34,81,'Collaborative Activities','<div>The <b><span style=\"color: rgb(255, 153, 0);\">Collaborative </span></b>tools in LAMS allow Learners to team up and solve problems, discuss ideas or share solutions together.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2>&nbsp;<span style=\"color: rgb(255, 153, 0);\"><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lachat11.PNG\" alt=\"\" /> Chat Activity</span></h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Chat Activity</b> runs a live (synchronous) discussion for learners.<br />\nThis is similar to <i>Instant Messaging</i> (IM) software that most learners will be familiar with.</p>\n</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lafrum11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Forum Activity</span></h2>\n<div>&nbsp;</div>\n<div>&nbsp;The <b>Forum Activity</b> provides an asynchronous discussion environment for learners, with discussion threads initially created by the teacher.\n<p style=\"margin-bottom: 0cm;\">Forums can be &rsquo;locked&rsquo; as an activity which is only available for the period of the specific activity (i.e.: no more contributions can be made once the learner clicks &rsquo;Finish&rsquo;, or they can be &rsquo;unlocked&rsquo;, in that learners can add to the Forum throughout the life of the whole sequence.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"126\" height=\"53\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lascrb11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Scribe Activity</span></h2>\n<div>&nbsp;The <b>Scribe Activity</b> is, by default, not enabled as a stand-alone activity.<br />\n<p style=\"margin-bottom: 0cm;\">It allows a group of Learners to collaborate and create a written report with one Learner as the &rsquo;Scribe,&rsquo; or Writer for the report. The activity is most effective when paired with another collaborative activity. <em class=\"western\">In the current release of LAMS (2.2) Scribe is available to be paired with either Chat or Forum.</em></p>\n<h3>Using the Scribe Activity</h3>\n<p style=\"margin-bottom: 0cm;\">The Scribe Activity should always used in conjunction with another activity that gives it context. For instance, if used with Chat, then the Scribe (one particular learner) is expected to collect or summarize the output of the Chat discussion into a succinct set of sentences. Once the scribe has done this, they publish the summary to the rest of the learners who vote whether they agree with the summary as proposed by the scribe. If not, they can ask the scribe to modify the report until all learners agree on the report.</p>\n<p style=\"margin-bottom: 0cm;\">Once the Scribe submits the report, the other learners in the group or class can agree or disagree with the summary. The scribe can finished the scribe task at any time regardless of the number of learners that have agreed with the proposed summaries. The Scribe task can also be finished through the monitoring environment by a teacher or instructor.</p>\n<p style=\"margin-bottom: 0cm;\">Once the scribe task is finished a report of the statements and summary is presented to all learners.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>','','',0,0,0,'',0,8,'2008-04-10 15:41:45','2008-11-14 13:16:43'),(35,85,'Assessment Activities','<div>The <b><span style=\"color: rgb(128, 0, 128);\">Assessment </span></b>tools in LAMS provide a vehicle for sequence monitors to collect Learner responses to a topic.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lamc11.PNG\" alt=\"\" /><span style=\"color: rgb(128, 0, 128);\">Multiple Choice</span></h2>\n<div>&nbsp;The <b>Multiple Choice Quiz</b> delivers a series of Multiple Choice questions to learners, and the advanced features of this tool provide a high level of customisation for Authors as to how the quiz is administered.</div>\n<div>&nbsp;</div>\n<div>When the Quiz is complete, LAMS will automatically tally the marks from each learner and display the statistics to sequence monitors.</div>\n<div>&nbsp;</div>\n<div>Authors can also choose to release Learner marks <i>directly </i>after they have completed the quiz, as a type of <b>formative </b>assessment or <i>delay</i> releasing the marks until a Sequence Monitor chooses to release them, for <b>Summative Assessment</b>. The Activity also allows learners to view the top, bottom and average marks from the quiz.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasbmt11.PNG\" /><span style=\"color: rgb(128, 0, 128);\">Submit Files </span></h2>\n<div>The <b>Submit Files </b>Activity provides a vehicle for Authors to collect Student responses which are created in software outside of LAMS.</div>\n<div>&nbsp;</div>\n<div>This could be an essay in .DOC or .PDF format, a digital presentation, or some sort of multimedia file.</div>\n<div>&nbsp;</div>\n<div>Learners log in to LAMS, and are able to upload their file to the LAMS server as an attachment.&nbsp; Sequence Monitors can view these files, comment on them and give each individual file a mark, which is in turn accessible by Learners.</div>\n<div>&nbsp;</div>\n<div>Like the MCQ, sequence authors can choose to allow learners several attempts at submission, allowing for feedback and creating a formative assessment style, or alternatively, lock the activity after submission, which creates a summative assessment style.</div>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\">The next activity is a demonstration <b>Multiple Choice </b>quiz,<br />\nfollowed by a demonstration <b>Submit Files</b> activity.</div>','','',0,0,0,'',0,8,'2008-04-10 14:39:45','2008-11-14 13:14:54'),(36,87,'Reflective Activities','<div>Reflective tools ask learners to analyse their own learning throughout a sequence,<br />\nor alternatively, collects responses from students and asks them to consider trends in the resulting data.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//laqa11.PNG\" /> Question and Answer</h2>\n<div>This activity creates a series of Questions and Short-Answer responses for Learners.</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">The sequence Author can pen their own responses to each question, which are displayed to learners after they have submitted their own answers.</p>\n<p style=\"margin-bottom: 0cm;\">The activity can also be configured to display answers collected from all learners in the sequence or group.</p>\n<p style=\"margin-bottom: 0cm;\">The <b>Question and Answer Activity</b> allows teachers to pose a question or questions to learners individually, and after they have entered their response, to see the responses of all their peers presented on a single answer screen.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasurv11.PNG\" /> Survey</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The Survey Tool is similar to Multiple Choice, in that it presents learners with a number of questions and collects their responses. However, unlike Multiple Choice there are no right or wrong answers - the system just collates all answers for analysis by the teacher in the monitoring area.</p>\n<p style=\"margin-bottom: 0cm;\">Question types include single response, multiple response and open text entry, and questions can be mandatory or optional.  For multiple choice questions, Learners can also be provided with the option of adding their own answer to a list.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lavote11.PNG\" /> Voting</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Voting Activity</b> allows teachers to provide learners with a list of options to &quot;Vote&quot; on. It is similar to the Q&amp;A Activity in that Voting shows first one screen with all the available voting options, then a screen showing the individual learner&rsquo;s selection, then a screen showing <i>&quot;progressive&quot;</i> voting results (i.e., a non-editable screen where learners can see results from themselves and other learners over time). Finally, there is Summary Screen of group results.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\">The next three activities demonstrate the Q&amp;A, Survey and Voting activities.</h5>\n</div>\n</div>','','',0,0,0,'',0,8,'2008-04-11 16:26:32','2008-11-14 14:30:21'),(37,89,'Notebook Activity','<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lantbk11.PNG\" alt=\"\" />Notebook Activity</h2>\n<div>The next LAMS activity to look at is the Notebook activity.</div>\n<div>&nbsp;</div>\n<div>This activity is a bit special because it can be <b>attached to any LAMS activity</b><b> at any point in the lesson.</b><br />\nEach LAMS activity has an option in <i><b>Advanced Properties</b></i> called <i>&quot;attach Notebook with text...&quot;</i></div>\n<div>&nbsp;</div>\n<div>Basically, this allows sequences authors to get Learners to reflect on their knowledge at <b>any point in the sequence - </b>and it&rsquo;s also really easy for monitors to add a notebook to any activity in a sequence without mucking around with transition lines or changing the shape of the sequence.</div>\n<div>&nbsp;</div>\n<div>Of course, the notebook can also be included in sequences as a standalone tool.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-11 16:41:59','2008-11-14 13:51:16'),(38,91,'Data Collection Activity','<h2><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/dacoIcon.png\" alt=\"\" /> Data Collection</h2>\n<div>&nbsp;</div>\n<div><span style=\"border-collapse: separate; color: rgb(0, 0, 0); font-family: Verdana; font-size: 11px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 16px; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px;\" class=\"Apple-style-span\">The Data Collection tool allows Authors to create database style lists for Learners to populate.<br />\nEach record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</span></div>\n<div><b><i>The next Activity demonstrates the use of Data Collection</i></b>.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-11-14 14:26:47','2008-11-14 14:26:47'),(39,94,'LAMS Activities','<div>\n<div><img width=\"116\" height=\"103\" align=\"left\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\nSo you&rsquo;ve had a look at the <b>Authoring Environment - </b>but what are the next steps?</div>\n<div>&nbsp;</div>\n<div>This LAMS sequence will show you each of the activities available for use in LAMS 2.1, and techniques for getting the most out of their use.</div>\n<div>&nbsp;</div>\n<div><span style=\"background-color: rgb(255, 255, 153);\"><b><i>This activity also discusses NEW activities added in the 2.2 release.</i></b></span></div>\n<div>&nbsp;</div>\n<h2>&nbsp;</h2>\n<h2 style=\"text-align: center;\">Activity Types</h2>\n<div>Earlier, we discussed the four different types of LAMS activities:&nbsp;<span class=\"Apple-style-span\" style=\"word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; orphans: 2; widows: 2; font-size-adjust: none; font-stretch: normal;\"><center>\n<h4><span style=\"font-size: small;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(128, 0, 128);\"><b>Assessement,</b></span></span>&nbsp; <span style=\"color: rgb(0, 0, 0);\">and </span><span style=\"font-size: small;\"><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span>&nbsp;</h4>\n</center><center>\n<div style=\"text-align: left;\">\n<div style=\"text-align: left;\">The rest of this sequence will let you view demonstrations of each of the LAMS activities, and briefly discuss how they might be best used in a lesson.</div>\n</div>\n</center></span></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div style=\"text-align: right;\">&nbsp;<b><i>Click &rsquo;Next Activity&rsquo; to move on.</i></b></div>','','',0,0,0,'',0,8,'2008-04-10 11:56:54','2008-11-14 13:14:33');
/*!40000 ALTER TABLE `tl_lanb11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_session`
--

DROP TABLE IF EXISTS `tl_lanb11_session`;
CREATE TABLE `tl_lanb11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `nb_session_id` bigint(20) NOT NULL,
  `nb_session_name` varchar(255) NOT NULL,
  `nb_content_uid` bigint(20) NOT NULL,
  `session_start_date` datetime default NULL,
  `session_end_date` datetime default NULL,
  `session_status` varchar(100) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `nb_session_id` (`nb_session_id`),
  KEY `nb_content_uid` (`nb_content_uid`),
  CONSTRAINT `FK_tl_lanb11_session_1` FOREIGN KEY (`nb_content_uid`) REFERENCES `tl_lanb11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lanb11_session`
--

LOCK TABLES `tl_lanb11_session` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_session` DISABLE KEYS */;
INSERT INTO `tl_lanb11_session` VALUES (1,2,'Everybody learners',21,'2008-12-03 14:45:18',NULL,'NOT_ATTEMPTED'),(2,3,'Everybody learners',23,'2008-12-03 14:45:18',NULL,'NOT_ATTEMPTED'),(3,5,'Everybody learners',22,'2008-12-03 14:45:18',NULL,'NOT_ATTEMPTED'),(4,6,'Everybody learners',26,'2008-12-03 14:45:27',NULL,'NOT_ATTEMPTED'),(5,11,'Everybody learners',25,'2008-12-03 14:45:27',NULL,'NOT_ATTEMPTED'),(6,13,'Everybody learners',27,'2008-12-03 14:45:27',NULL,'NOT_ATTEMPTED'),(7,14,'Everybody learners',24,'2008-12-03 14:45:27',NULL,'NOT_ATTEMPTED'),(8,15,'Everybody learners',35,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(9,19,'Everybody learners',29,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(10,20,'Everybody learners',31,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(11,21,'Everybody learners',36,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(12,22,'Everybody learners',34,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(13,25,'Everybody learners',32,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(14,29,'Everybody learners',33,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(15,32,'Everybody learners',37,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(16,34,'Everybody learners',38,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(17,35,'Everybody learners',30,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(18,36,'Everybody learners',39,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED'),(19,37,'Everybody learners',28,'2008-12-03 14:45:36',NULL,'NOT_ATTEMPTED');
/*!40000 ALTER TABLE `tl_lanb11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_user`
--

DROP TABLE IF EXISTS `tl_lanb11_user`;
CREATE TABLE `tl_lanb11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `nb_session_uid` bigint(20) NOT NULL,
  `username` varchar(255) default NULL,
  `fullname` varchar(255) default NULL,
  `user_status` varchar(50) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `nb_session_uid` (`nb_session_uid`),
  CONSTRAINT `FK_tl_lanb11_user_1` FOREIGN KEY (`nb_session_uid`) REFERENCES `tl_lanb11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lanb11_user`
--

LOCK TABLES `tl_lanb11_user` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lanb11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_attachment`
--

DROP TABLE IF EXISTS `tl_lantbk11_attachment`;
CREATE TABLE `tl_lantbk11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `notebook_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK12090F57FC940906` (`notebook_uid`),
  CONSTRAINT `FK12090F57FC940906` FOREIGN KEY (`notebook_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lantbk11_attachment`
--

LOCK TABLES `tl_lantbk11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lantbk11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_conditions`
--

DROP TABLE IF EXISTS `tl_lantbk11_conditions`;
CREATE TABLE `tl_lantbk11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `NotebookConditionToNotebook` (`content_uid`),
  CONSTRAINT `NotebookConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `NotebookConditionToNotebook` FOREIGN KEY (`content_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lantbk11_conditions`
--

LOCK TABLES `tl_lantbk11_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lantbk11_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_notebook`
--

DROP TABLE IF EXISTS `tl_lantbk11_notebook`;
CREATE TABLE `tl_lantbk11_notebook` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `allow_rich_editor` bit(1) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lantbk11_notebook`
--

LOCK TABLES `tl_lantbk11_notebook` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_notebook` DISABLE KEYS */;
INSERT INTO `tl_lantbk11_notebook` VALUES (1,NULL,NULL,NULL,'Notebook','Instructions',0x00,0x00,0x00,'','',0x00,0x00,8),(2,'2008-11-14 14:32:00','2008-11-10 12:44:29',5,'Notebook Demonstration','<div>Although this looks like a simple text entry box,&nbsp; when a notebook is placed in the right spot, it can really get learners to think about what they&rsquo;ve just learnt.</div>\n<div>&nbsp;</div>\n<div>All notebook entries are private between each individual learner and the sequence monitors.</div>',0x00,0x00,0x00,'','',0x00,0x00,52),(3,'2008-12-03 14:45:35','2008-11-10 12:44:29',5,'Notebook Demonstration','<div>Although this looks like a simple text entry box,&nbsp; when a notebook is placed in the right spot, it can really get learners to think about what they&rsquo;ve just learnt.</div>\n<div>&nbsp;</div>\n<div>All notebook entries are private between each individual learner and the sequence monitors.</div>',0x00,0x00,0x00,'','',0x00,0x00,92);
/*!40000 ALTER TABLE `tl_lantbk11_notebook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_session`
--

DROP TABLE IF EXISTS `tl_lantbk11_session`;
CREATE TABLE `tl_lantbk11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `notebook_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKB7C198E2FC940906` (`notebook_uid`),
  CONSTRAINT `FKB7C198E2FC940906` FOREIGN KEY (`notebook_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lantbk11_session`
--

LOCK TABLES `tl_lantbk11_session` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_session` DISABLE KEYS */;
INSERT INTO `tl_lantbk11_session` VALUES (1,NULL,NULL,NULL,33,'Everybody learners',3);
/*!40000 ALTER TABLE `tl_lantbk11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_user`
--

DROP TABLE IF EXISTS `tl_lantbk11_user`;
CREATE TABLE `tl_lantbk11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `notebook_session_uid` bigint(20) default NULL,
  `entry_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKCB8A58FFA3B0FADF` (`notebook_session_uid`),
  CONSTRAINT `FKCB8A58FFA3B0FADF` FOREIGN KEY (`notebook_session_uid`) REFERENCES `tl_lantbk11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lantbk11_user`
--

LOCK TABLES `tl_lantbk11_user` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lantbk11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_condition_questions`
--

DROP TABLE IF EXISTS `tl_laqa11_condition_questions`;
CREATE TABLE `tl_laqa11_condition_questions` (
  `condition_id` bigint(20) NOT NULL default '0',
  `question_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`question_uid`),
  KEY `QaConditionQuestionToQaQuestion` (`question_uid`),
  CONSTRAINT `QaConditionQuestionToQaCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_laqa11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionQuestionToQaQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_laqa11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_condition_questions`
--

LOCK TABLES `tl_laqa11_condition_questions` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_condition_questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_condition_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_conditions`
--

DROP TABLE IF EXISTS `tl_laqa11_conditions`;
CREATE TABLE `tl_laqa11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `QaConditionToQaContent` (`content_uid`),
  CONSTRAINT `QaConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionToQaContent` FOREIGN KEY (`content_uid`) REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_conditions`
--

LOCK TABLES `tl_laqa11_conditions` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_configuration`
--

DROP TABLE IF EXISTS `tl_laqa11_configuration`;
CREATE TABLE `tl_laqa11_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_configuration`
--

LOCK TABLES `tl_laqa11_configuration` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_configuration` DISABLE KEYS */;
INSERT INTO `tl_laqa11_configuration` VALUES (1,'enableQaWizard','false');
/*!40000 ALTER TABLE `tl_laqa11_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_content`
--

DROP TABLE IF EXISTS `tl_laqa11_content`;
CREATE TABLE `tl_laqa11_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `qa_content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime default NULL,
  `update_date` datetime default NULL,
  `reflect` tinyint(1) NOT NULL default '0',
  `questions_sequenced` tinyint(1) NOT NULL default '0',
  `username_visible` tinyint(1) NOT NULL default '0',
  `created_by` bigint(20) NOT NULL default '0',
  `run_offline` tinyint(1) default '0',
  `define_later` tinyint(1) NOT NULL default '0',
  `synch_in_monitor` tinyint(1) NOT NULL default '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_inUse` tinyint(1) default '0',
  `reflectionSubject` text,
  `lockWhenFinished` tinyint(1) NOT NULL default '1',
  `showOtherAnswers` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_content`
--

LOCK TABLES `tl_laqa11_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_content` VALUES (1,3,'Q&A','Instructions','2008-12-03 14:38:51',NULL,0,0,0,0,0,0,0,NULL,NULL,0,NULL,0,1),(2,44,'Question and Answer Demonstration','<h2><img width=\"116\" hspace=\"6\" height=\"103\" align=\"middle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /><b>This is the Q&amp;A Activity</b></h2>\n<div>&nbsp;</div>\n<div>Up here is a space for Author instructions for Learners, and below is the list of Questions.</div>\n<div>&nbsp;</div>\n<div>Have a crack at answering them, and then watch what happens over the following screens.</div>',NULL,'2008-11-14 14:30:09',0,0,0,5,0,0,0,'','',0,'',0,1),(3,93,'Question and Answer Demonstration','<h2><img width=\"116\" hspace=\"6\" height=\"103\" align=\"middle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /><b>This is the Q&amp;A Activity</b></h2>\n<div>&nbsp;</div>\n<div>Up here is a space for Author instructions for Learners, and below is the list of Questions.</div>\n<div>&nbsp;</div>\n<div>Have a crack at answering them, and then watch what happens over the following screens.</div>',NULL,'2008-11-14 14:30:09',0,0,0,5,0,0,0,'','',0,'',0,1);
/*!40000 ALTER TABLE `tl_laqa11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_content`
--

DROP TABLE IF EXISTS `tl_laqa11_que_content`;
CREATE TABLE `tl_laqa11_que_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question` text,
  `feedback` text,
  `display_order` int(5) default '1',
  `qa_content_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_que_content_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_que_content`
--

LOCK TABLES `tl_laqa11_que_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_que_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_que_content` VALUES (1,'Sample Question 1?',NULL,1,1),(2,'<div><b><span style=\"font-size: small;\">What do you like best so far about LAMS?</span></b></div>\n<div><b><span style=\"font-size: small;\"> <br />\n</span></b></div>','This is the feedback section - it allows Authors and Monitors to give a short response to each question.<BR>This is incredibly useful for formative assessment and learning techniques.',1,2),(3,'<div><span style=\"font-size: small;\"><b>Do you think you can apply LAMS to your own teaching styles?</b></span></div>','',2,2),(4,'<div><b><span style=\"font-size: small;\">What do you like best so far about LAMS?</span></b></div>\n<div><b><span style=\"font-size: small;\"> <br />\n</span></b></div>','This is the feedback section - it allows Authors and Monitors to give a short response to each question.<BR>This is incredibly useful for formative assessment and learning techniques.',1,3),(5,'<div><span style=\"font-size: small;\"><b>Do you think you can apply LAMS to your own teaching styles?</b></span></div>','',2,3);
/*!40000 ALTER TABLE `tl_laqa11_que_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_usr`
--

DROP TABLE IF EXISTS `tl_laqa11_que_usr`;
CREATE TABLE `tl_laqa11_que_usr` (
  `uid` bigint(20) NOT NULL auto_increment,
  `que_usr_id` bigint(20) NOT NULL,
  `username` varchar(255) default NULL,
  `responseFinalized` tinyint(1) NOT NULL default '0',
  `qa_session_id` bigint(20) NOT NULL,
  `fullname` varchar(255) default NULL,
  `learnerFinished` tinyint(1) NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `qa_session_id` (`qa_session_id`),
  CONSTRAINT `FK_tl_laqa11_que_usr_1` FOREIGN KEY (`qa_session_id`) REFERENCES `tl_laqa11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_que_usr`
--

LOCK TABLES `tl_laqa11_que_usr` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_que_usr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_que_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_session`
--

DROP TABLE IF EXISTS `tl_laqa11_session`;
CREATE TABLE `tl_laqa11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `qa_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime default NULL,
  `session_end_date` datetime default NULL,
  `session_name` varchar(100) default NULL,
  `session_status` varchar(100) default NULL,
  `qa_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_session_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_session`
--

LOCK TABLES `tl_laqa11_session` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_session` DISABLE KEYS */;
INSERT INTO `tl_laqa11_session` VALUES (1,24,'2008-12-03 14:45:36',NULL,'Everybody learners','INCOMPLETE',3);
/*!40000 ALTER TABLE `tl_laqa11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_laqa11_uploadedfile`;
CREATE TABLE `tl_laqa11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL auto_increment,
  `uuid` varchar(255) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `qa_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`submissionId`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_uploadedfile_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_uploadedfile`
--

LOCK TABLES `tl_laqa11_uploadedfile` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_uploadedfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_uploadedfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_usr_resp`
--

DROP TABLE IF EXISTS `tl_laqa11_usr_resp`;
CREATE TABLE `tl_laqa11_usr_resp` (
  `response_id` bigint(20) NOT NULL auto_increment,
  `hidden` tinyint(1) default '0',
  `answer` text,
  `time_zone` varchar(255) default NULL,
  `attempt_time` datetime default NULL,
  `que_usr_id` bigint(20) NOT NULL,
  `qa_que_content_id` bigint(20) NOT NULL,
  `visible` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`response_id`),
  KEY `que_usr_id` (`que_usr_id`),
  KEY `qa_que_content_id` (`qa_que_content_id`),
  CONSTRAINT `FK_tl_laqa11_usr_resp_3` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_laqa11_que_usr` (`uid`),
  CONSTRAINT `FK_tl_laqa11_usr_resp_2` FOREIGN KEY (`qa_que_content_id`) REFERENCES `tl_laqa11_que_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_usr_resp`
--

LOCK TABLES `tl_laqa11_usr_resp` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_usr_resp` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_usr_resp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_wizard_category`
--

DROP TABLE IF EXISTS `tl_laqa11_wizard_category`;
CREATE TABLE `tl_laqa11_wizard_category` (
  `uid` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_wizard_category`
--

LOCK TABLES `tl_laqa11_wizard_category` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_wizard_category` DISABLE KEYS */;
INSERT INTO `tl_laqa11_wizard_category` VALUES (1,'Data%20Collection'),(2,'Data%20Organization'),(3,'Data%20Analysis'),(4,'Data%20Transcendence');
/*!40000 ALTER TABLE `tl_laqa11_wizard_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_wizard_cognitive_skill`
--

DROP TABLE IF EXISTS `tl_laqa11_wizard_cognitive_skill`;
CREATE TABLE `tl_laqa11_wizard_cognitive_skill` (
  `uid` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `category_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK3BA4132BCBB0DC8D` (`category_uid`),
  CONSTRAINT `FK3BA4132BCBB0DC8D` FOREIGN KEY (`category_uid`) REFERENCES `tl_laqa11_wizard_category` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_wizard_cognitive_skill`
--

LOCK TABLES `tl_laqa11_wizard_cognitive_skill` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_wizard_cognitive_skill` DISABLE KEYS */;
INSERT INTO `tl_laqa11_wizard_cognitive_skill` VALUES (1,'2.%20Recognition',1),(2,'1.%20Observation',1),(3,'3.%20Recall',1),(4,'Ordering',2),(5,'Comparison',2),(6,'Hierarchy',2),(7,'Classification',2),(8,'Distinction%20between%20facts%20%26%20opinions/judgments',3),(9,'Analysis%20of%20basic%20parts',3),(10,'Clarification',3),(11,'Flush%20out%20relationships',3),(12,'Pattern%20recognition',3),(13,'Error%20and%20conflict%20detection',4),(14,'Summary',4),(15,'Reflection',4),(16,'Hypothesis',4),(17,'Prediction',4),(18,'Explanation',4),(19,'Empathy',4),(20,'Knowledge%20Organization',4),(21,'Application%20and%20Improvement',4),(22,'Assessment/Evaluation',4),(23,'Verification',4),(24,'Conclusion',4);
/*!40000 ALTER TABLE `tl_laqa11_wizard_cognitive_skill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_wizard_question`
--

DROP TABLE IF EXISTS `tl_laqa11_wizard_question`;
CREATE TABLE `tl_laqa11_wizard_question` (
  `uid` bigint(20) NOT NULL auto_increment,
  `cognitive_skill_uid` bigint(20) default NULL,
  `title` text NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKAF08A0C7EFF77FD4` (`cognitive_skill_uid`),
  CONSTRAINT `FKAF08A0C7EFF77FD4` FOREIGN KEY (`cognitive_skill_uid`) REFERENCES `tl_laqa11_wizard_cognitive_skill` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_laqa11_wizard_question`
--

LOCK TABLES `tl_laqa11_wizard_question` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_wizard_question` DISABLE KEYS */;
INSERT INTO `tl_laqa11_wizard_question` VALUES (1,1,'Please%20identify%u2026'),(2,2,'What%20do%20you%20observe%20...'),(3,2,'What%20do%20you%20see%u2026%3F'),(4,3,'Give%20the%20definition%20of%u2026'),(5,3,'What%20is%20the%20meaning%20of%u2026%3F'),(6,3,'What%20has%20been%20said%20by%u2026%20about%u2026%3F'),(7,4,'Find%20a%20criterion%20to%20order%20these%u2026'),(8,4,'Order%20these%u2026'),(9,4,'Find%20the%205%20biggest/smallest%20%u2026'),(10,5,'Compare%20these%u2026%20using%20the%20following%20criteria%u2026'),(11,5,'Find%20the%20pros%20and%20cons%20of%u2026'),(12,5,'Find%20both%20the%20similarities%20and%20the%20differences%20between..%20and%u2026'),(13,6,'Arrange%20these%u2026by%20the%20following%20criterion%u2026'),(14,6,'Sort%20these%u2026%20by%20the%20following%20criterion%u2026'),(15,6,'Put%20these%u2026in%20an%20ascending/descending%20sequence%u2026'),(16,7,'Group%20these%20elements%20using%20the%20following%20criteria%u2026'),(17,7,'Find%20the%20pros%20and%20cons%20of%20these%u2026solutions'),(18,7,'Find%205%20%20advantages%20and%203%20disadvantages%20%20of%20%u2026'),(19,8,'Clarify%20whether%20this%20%u2026describes%20a%20fact%20or%20if%20it%20is%20a%20personal%20opinion'),(20,8,'Provide%20more%20than%20one%20view%20about%20the%20following%u2026'),(21,8,'Provide%20logical%20arguments%20to%20support%20the%20following%20statement'),(22,9,'Identify%20the%20parts/units/characteristics%20of%u2026'),(23,9,'Define%20the%20data%20given%20and%20the%20objectives%20of%u2026.'),(24,9,'Refer%20to%20the%20appropriate%20phases%20of%20the%20solution%20plan%20for%u2026'),(25,10,'What%20do%20we%20mean%20by%u2026%3F'),(26,10,'Provide%20an%20example%20to%20clarify%u2026'),(27,10,'When%20does%20this%u2026have%20a%20meaning%u2026%3F'),(28,11,'Distinguish%20possible%20relationships%20among%20the%20data%u2026'),(29,11,'Is%20there%20any%20reason%20that%20affects%20this%20phenomenon%u2026%3F'),(30,11,'Is%20there%20any%20structure%20within%u2026%3F'),(31,12,'Is%20there%20any%20pattern%20that%20is%20repeated%20in%u2026%3F'),(32,12,'Find%20out%20what%20is%20common%20in%u2026'),(33,12,'Find%20out%20what%20is%20similar%20in%u2026'),(34,13,'Point%20out%20the%20mistakes%20of/in%u2026'),(35,13,'Are%20there%20any%20contradictions%20in%u2026%3F'),(36,13,'What%20is%20missing%20in%u2026%3F'),(37,14,'Form%20an%20abstract%20of%20%u2026%28number%29%20words%20to%20describe%u2026'),(38,14,'Which%20are%20the%20main%20points%20of%20%u2026'),(39,14,'Which%20are%20the%20top%205%20essential%20points%20of%20%u2026'),(40,15,'What%20have%20you%20learnt%20about%u2026%3F'),(41,15,'Are%20there%20any%20points%20that%20you%u2026%3F'),(42,16,'If%20%u2026%20then%u2026'),(43,17,'Could%20you%20make%20any%20predictions%20about%u2026%3F'),(44,17,'If%20we%20do%u2026then%20what%20will%20happen%3F'),(45,17,'In%20the%20case%20of%u2026find%20out%u2026'),(46,18,'Please%20explain%20how%20the%20meaning%20of%u2026'),(47,18,'Please%20explain%20why%20the%20meaning%20of/%u2026'),(48,18,'Please%20explain%20what%20the%20meaning%20of%20%u2026'),(49,18,'Please%20explain%20in%20your%20own%20words%u2026'),(50,18,'Please%20explain%20in%20your%20own%20words%u2026'),(51,19,'Could%20you%20accept%20the%20role%20of%u2026%3F'),(52,19,'What%20are%20the%20possible%20arguments%20of%20others%20for%u2026%3F'),(53,19,'What%20would%20be%20your%20answer%20if%20you%20were%20in%20the%20position%20of%u2026%3F'),(54,20,'Make%20a%20hierarchical%20tree%20to%20describe%u2026'),(55,20,'Can%20you%20form%20a%20sequential%20structure%20of%u2026%3F'),(56,20,'Characterize%20it%20according%20to%20the%20following%20criteria%u2026'),(57,21,'How%20can%20you%20improve%20this%u2026in%20order%20to%u2026%3F'),(58,21,'How%20can%20you%20apply%20this%u2026in%20order%20to%u2026%3F'),(59,22,'Assess%20this%u2026'),(60,22,'Evaluate%20the%u2026'),(61,22,'What%20kind%20of%20criteria%20can%20you%20use%20to%20evaluate%20the%u2026%3F'),(62,23,'Please%20verify%20that%u2026'),(63,23,'Please%20confirm%20that%u2026'),(64,24,'Based%20on%20this%u2026what%20do%20you%20conclude%3F'),(65,24,'Based%20on%20these%u2026what%20do%20you%20conclude%3F'),(66,24,'Are%20there%20any%20exceptions%20to%u2026%3F'),(67,24,'Are%20there%20any%20weak%20points%20in%u2026%3F');
/*!40000 ALTER TABLE `tl_laqa11_wizard_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_attachment`
--

DROP TABLE IF EXISTS `tl_larsrc11_attachment`;
CREATE TABLE `tl_larsrc11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `resource_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK1E7009430E79035` (`resource_uid`),
  CONSTRAINT `FK1E7009430E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_attachment`
--

LOCK TABLES `tl_larsrc11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_larsrc11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_item_instruction`
--

DROP TABLE IF EXISTS `tl_larsrc11_item_instruction`;
CREATE TABLE `tl_larsrc11_item_instruction` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `sequence_id` int(11) default NULL,
  `item_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKA5665013980570ED` (`item_uid`),
  CONSTRAINT `FKA5665013980570ED` FOREIGN KEY (`item_uid`) REFERENCES `tl_larsrc11_resource_item` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_item_instruction`
--

LOCK TABLES `tl_larsrc11_item_instruction` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_item_instruction` VALUES (1,'Use Google to search the web',0,1),(2,'You might need to resize your internet browser to see the whole animation.',0,2),(3,'When the animation is complete, click Finished to go to the next activity.',1,2),(4,'When the animation has stopped, click Finished to go to the next activity.',1,3),(5,'This animation gives a brief overview of LAMS.',0,3),(6,'This animation describes the basics of the Monitoring environment.',0,4),(7,'You will need an active internet connection to view this web page.',0,5),(8,'You might need to resize your browser to see the whole animation.',0,6),(9,'When the animation has completed, click Finished to move to the next activity.',1,6),(10,'This animation will demonstrate the Learner\'s Experience when using LAMS.   You might need to resize your browser to see the whole animation.',0,7),(11,'When the animation has stopped, click Finished to go to the next activity. ',1,7),(12,'When the animation has stopped, click ',1,8),(13,'LAMS 2.1 lets you disable FLASH for the Learning Environment - this is useful if you want to use LAMS on devices that do not support flash.',0,8),(14,'In this animation, you will learn how to use the features of the Noticeboard Activity.',0,9),(15,'This animation explains how to create and use a Resource Sharing activity.',0,10),(16,'This is our online documentation website.  Here you can find the specifications for what kind of files can be added to a Resource Sharing activity.',0,11),(17,'When the animation has stopped, click Finished to go to the next activity.',1,15),(18,'This animation gives a brief overview of LAMS.',0,15),(19,'You might need to resize your internet browser to see the whole animation.',0,16),(20,'When the animation is complete, click Finished to go to the next activity.',1,16),(21,'You will need an active internet connection to view this web page.',0,17),(22,'This animation describes the basics of the Monitoring environment.',0,18),(23,'When the animation has completed, click Finished to move to the next activity.',1,19),(24,'You might need to resize your browser to see the whole animation.',0,19),(25,'When the animation has stopped, click ',1,20),(26,'LAMS 2.1 lets you disable FLASH for the Learning Environment - this is useful if you want to use LAMS on devices that do not support flash.',0,20),(27,'This animation will demonstrate the Learner\'s Experience when using LAMS.   You might need to resize your browser to see the whole animation.',0,21),(28,'When the animation has stopped, click Finished to go to the next activity. ',1,21),(29,'This is our online documentation website.  Here you can find the specifications for what kind of files can be added to a Resource Sharing activity.',0,22),(30,'This animation explains how to create and use a Resource Sharing activity.',0,23),(31,'In this animation, you will learn how to use the features of the Noticeboard Activity.',0,24);
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_item_log`
--

DROP TABLE IF EXISTS `tl_larsrc11_item_log`;
CREATE TABLE `tl_larsrc11_item_log` (
  `uid` bigint(20) NOT NULL auto_increment,
  `access_date` datetime default NULL,
  `resource_item_uid` bigint(20) default NULL,
  `user_uid` bigint(20) default NULL,
  `complete` tinyint(4) default NULL,
  `session_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK693580A438BF8DFE` (`resource_item_uid`),
  KEY `FK693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_larsrc11_user` (`uid`),
  CONSTRAINT `FK693580A438BF8DFE` FOREIGN KEY (`resource_item_uid`) REFERENCES `tl_larsrc11_resource_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_item_log`
--

LOCK TABLES `tl_larsrc11_item_log` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_item_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_larsrc11_item_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_resource`
--

DROP TABLE IF EXISTS `tl_larsrc11_resource`;
CREATE TABLE `tl_larsrc11_resource` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default NULL,
  `lock_on_finished` tinyint(4) default NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default NULL,
  `define_later` tinyint(4) default NULL,
  `content_id` bigint(20) default NULL,
  `allow_add_files` tinyint(4) default NULL,
  `allow_add_urls` tinyint(4) default NULL,
  `mini_view_resource_number` int(11) default NULL,
  `allow_auto_run` tinyint(4) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  `assigment_submit_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK89093BF758092FB` (`create_by`),
  CONSTRAINT `FK89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_larsrc11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_resource`
--

LOCK TABLES `tl_larsrc11_resource` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource` VALUES (1,NULL,NULL,NULL,'Resources',0,0,'Instructions ',NULL,NULL,0,0,6,0,0,0,0,NULL,0,0),(2,NULL,'2008-06-05 11:45:33',1,'Resources',0,0,'<div>Instructions</div>','','',0,0,19,0,0,0,1,'',0,0),(3,NULL,'2008-06-05 10:37:56',2,'So What Is LAMS?',0,0,'<div><span style=\"font-size: small;\"> </span></div>','','',0,0,21,0,0,0,1,'',0,0),(4,NULL,'2008-06-06 11:52:30',3,'Monitor Wink',0,0,'<div>Instructions</div>','','',0,0,25,0,0,0,1,'',0,0),(5,NULL,'2008-06-06 11:53:51',4,'Resources',0,0,'<div>Instructions</div>','','',0,0,26,0,0,0,1,'',0,0),(6,NULL,'2008-06-06 10:56:27',5,'Resources',0,0,'<div>Instructions</div>','','',0,0,27,0,0,0,1,'',0,0),(7,NULL,'2008-06-05 12:03:37',6,'Learning Environment',0,0,'','','',0,0,28,0,0,0,1,'',0,0),(8,NULL,'2008-06-05 12:32:17',7,'Resources',0,0,'<div>Instructions</div>','','',0,0,30,0,0,0,1,'',0,0),(9,NULL,'2008-04-11 15:35:40',8,'Resource Sharing',0,0,'<h2 style=\"text-align: center;\">This is a Resource Sharing Activity.</h2>\n<div style=\"text-align: center;\">You can see that the list below contains a URL, and several Zipped Websites.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>Explore the resources below, and then try adding your own URL to the list - </b><br />\nsimply fill in the fields below, marked &quot;Suggest a new URL&quot;<br />\nand then click the <b>Add</b> button.</div>\n<div style=\"text-align: center;\">&nbsp;</div>','','',0,0,34,0,1,0,0,'',0,0),(10,'2008-04-11 16:01:40','2008-04-11 16:01:40',9,'Resources on LAMS',0,0,'<h2 style=\"text-align: center;\"><b>Here are some interesting websites about LAMS!</b></h2>\n<div style=\"text-align: center;\"><b><br type=\"_moz\" />\n</b></div>','','',0,0,41,0,0,0,0,'',0,0),(11,NULL,'2008-06-05 10:37:56',10,'So What Is LAMS?',0,0,'<div><span style=\"font-size: small;\"> </span></div>','','',0,0,56,0,0,0,1,'',0,0),(12,NULL,'2008-06-05 11:45:33',11,'Resources',0,0,'<div>Instructions</div>','','',0,0,60,0,0,0,1,'',0,0),(13,NULL,'2008-06-06 11:53:51',12,'Resources',0,0,'<div>Instructions</div>','','',0,0,62,0,0,0,1,'',0,0),(14,NULL,'2008-06-06 11:52:30',13,'Monitor Wink',0,0,'<div>Instructions</div>','','',0,0,63,0,0,0,1,'',0,0),(15,NULL,'2008-06-06 10:56:27',14,'Resources',0,0,'<div>Instructions</div>','','',0,0,65,0,0,0,1,'',0,0),(16,NULL,'2008-06-05 12:32:17',15,'Resources',0,0,'<div>Instructions</div>','','',0,0,66,0,0,0,1,'',0,0),(17,NULL,'2008-06-05 12:03:37',16,'Learning Environment',0,0,'','','',0,0,68,0,0,0,1,'',0,0),(18,NULL,'2008-04-11 15:35:40',17,'Resource Sharing',0,0,'<h2 style=\"text-align: center;\">This is a Resource Sharing Activity.</h2>\n<div style=\"text-align: center;\">You can see that the list below contains a URL, and several Zipped Websites.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>Explore the resources below, and then try adding your own URL to the list - </b><br />\nsimply fill in the fields below, marked &quot;Suggest a new URL&quot;<br />\nand then click the <b>Add</b> button.</div>\n<div style=\"text-align: center;\">&nbsp;</div>','','',0,0,73,0,1,0,0,'',0,0),(19,'2008-04-11 16:01:40','2008-04-11 16:01:40',18,'Resources on LAMS',0,0,'<h2 style=\"text-align: center;\"><b>Here are some interesting websites about LAMS!</b></h2>\n<div style=\"text-align: center;\"><b><br type=\"_moz\" />\n</b></div>','','',0,0,77,0,0,0,0,'',0,0);
/*!40000 ALTER TABLE `tl_larsrc11_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_resource_item`
--

DROP TABLE IF EXISTS `tl_larsrc11_resource_item`;
CREATE TABLE `tl_larsrc11_resource_item` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_uuid` bigint(20) default NULL,
  `file_version_id` bigint(20) default NULL,
  `description` varchar(255) default NULL,
  `ims_schema` varchar(255) default NULL,
  `init_item` varchar(255) default NULL,
  `organization_xml` text,
  `title` varchar(255) default NULL,
  `url` text,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `create_by_author` tinyint(4) default NULL,
  `is_hide` tinyint(4) default NULL,
  `item_type` smallint(6) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `open_url_new_window` tinyint(4) default NULL,
  `resource_uid` bigint(20) default NULL,
  `session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKF52D1F93758092FB` (`create_by`),
  KEY `FKF52D1F9330E79035` (`resource_uid`),
  KEY `FKF52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FKF52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_larsrc11_session` (`uid`),
  CONSTRAINT `FKF52D1F9330E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`),
  CONSTRAINT `FKF52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_larsrc11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_resource_item`
--

LOCK TABLES `tl_larsrc11_resource_item` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource_item` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,'Web Search','http://www.google.com ',NULL,'2008-12-03 14:39:15',1,0,1,NULL,NULL,0,1,NULL),(2,1,1,NULL,NULL,'index.htm',NULL,'Introduction to Authoring',NULL,1,'2008-04-04 11:25:16',1,0,3,'application/zip','effective_authoring.zip',0,2,NULL),(3,5,1,NULL,NULL,'index.htm',NULL,'Introducing LAMS!',NULL,2,'2008-06-05 10:35:56',1,0,3,'application/zip','lams101-effective learning.zip',0,3,NULL),(4,9,1,NULL,NULL,'index.htm',NULL,'Monitor Wink',NULL,3,'2008-04-11 12:16:58',1,0,3,'application/x-zip-compressed','introToMonitor.zip',0,4,NULL),(5,NULL,NULL,NULL,NULL,NULL,NULL,'Lams Wiki - System Administration [Online]','http://wiki.lamsfoundation.org/display/lamsdocs/System+Administration/',4,'2008-03-12 16:39:41',1,0,1,NULL,NULL,0,5,NULL),(6,13,1,NULL,NULL,'index.htm',NULL,'Authoring Environment',NULL,5,'2008-04-11 15:01:31',1,0,3,'application/x-zip-compressed','introToAuthoring.zip',0,6,NULL),(7,17,1,NULL,NULL,'index.htm',NULL,'Learner\'s Experience',NULL,6,'2008-05-02 16:38:53',1,0,3,'application/zip','learnerExperience.zip',0,7,NULL),(8,21,1,NULL,NULL,'index.htm',NULL,'No Flash Learner Environment',NULL,7,'2008-04-11 17:36:30',1,0,3,'application/zip','noflash.zip',0,8,NULL),(9,25,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Noticeboard',NULL,8,'2008-04-10 13:52:15',1,0,3,'application/x-zip-compressed','lanb11.zip',0,9,NULL),(10,29,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Resource Sharing',NULL,8,'2008-04-10 13:52:47',1,0,3,'application/x-zip-compressed','lasrsc11.zip',0,9,NULL),(11,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Wiki - Resource Sharing [online]','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11',8,'2008-04-10 13:51:46',1,0,1,NULL,NULL,0,9,NULL),(12,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2007 European Conference - London, UK','http://lams2007.lamsfoundation.org/',9,'2008-04-11 16:01:31',1,0,1,NULL,NULL,1,10,NULL),(13,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2008 Conference - Cadiz, Spain','http://lams2008.lamsfoundation.org/',9,'2008-04-11 16:00:59',1,0,1,NULL,NULL,1,10,NULL),(14,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Documentation Website','http://wiki.lamsfoundation.org/display/lamsdocs/Home',9,'2008-04-11 16:00:25',1,0,1,NULL,NULL,1,10,NULL),(15,5,1,NULL,NULL,'index.htm',NULL,'Introducing LAMS!',NULL,10,'2008-06-05 10:35:56',1,0,3,'application/zip','lams101-effective learning.zip',0,11,NULL),(16,1,1,NULL,NULL,'index.htm',NULL,'Introduction to Authoring',NULL,11,'2008-04-04 11:25:16',1,0,3,'application/zip','effective_authoring.zip',0,12,NULL),(17,NULL,NULL,NULL,NULL,NULL,NULL,'Lams Wiki - System Administration [Online]','http://wiki.lamsfoundation.org/display/lamsdocs/System+Administration/',12,'2008-03-12 16:39:41',1,0,1,NULL,NULL,0,13,NULL),(18,9,1,NULL,NULL,'index.htm',NULL,'Monitor Wink',NULL,13,'2008-04-11 12:16:58',1,0,3,'application/x-zip-compressed','introToMonitor.zip',0,14,NULL),(19,13,1,NULL,NULL,'index.htm',NULL,'Authoring Environment',NULL,14,'2008-04-11 15:01:31',1,0,3,'application/x-zip-compressed','introToAuthoring.zip',0,15,NULL),(20,21,1,NULL,NULL,'index.htm',NULL,'No Flash Learner Environment',NULL,15,'2008-04-11 17:36:30',1,0,3,'application/zip','noflash.zip',0,16,NULL),(21,17,1,NULL,NULL,'index.htm',NULL,'Learner\'s Experience',NULL,16,'2008-05-02 16:38:53',1,0,3,'application/zip','learnerExperience.zip',0,17,NULL),(22,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Wiki - Resource Sharing [online]','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11',17,'2008-04-10 13:51:46',1,0,1,NULL,NULL,0,18,NULL),(23,29,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Resource Sharing',NULL,17,'2008-04-10 13:52:47',1,0,3,'application/x-zip-compressed','lasrsc11.zip',0,18,NULL),(24,25,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Noticeboard',NULL,17,'2008-04-10 13:52:15',1,0,3,'application/x-zip-compressed','lanb11.zip',0,18,NULL),(25,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Documentation Website','http://wiki.lamsfoundation.org/display/lamsdocs/Home',18,'2008-04-11 16:00:25',1,0,1,NULL,NULL,1,19,NULL),(26,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2008 Conference - Cadiz, Spain','http://lams2008.lamsfoundation.org/',18,'2008-04-11 16:00:59',1,0,1,NULL,NULL,1,19,NULL),(27,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2007 European Conference - London, UK','http://lams2007.lamsfoundation.org/',18,'2008-04-11 16:01:31',1,0,1,NULL,NULL,1,19,NULL);
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_session`
--

DROP TABLE IF EXISTS `tl_larsrc11_session`;
CREATE TABLE `tl_larsrc11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `resource_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK24AA78C530E79035` (`resource_uid`),
  CONSTRAINT `FK24AA78C530E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_session`
--

LOCK TABLES `tl_larsrc11_session` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_session` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_session` VALUES (1,NULL,NULL,0,11,1,'Everybody learners'),(2,NULL,NULL,0,12,4,'Everybody learners'),(3,NULL,NULL,0,14,7,'Everybody learners'),(4,NULL,NULL,0,15,8,'Everybody learners'),(5,NULL,NULL,0,13,9,'Everybody learners'),(6,NULL,NULL,0,17,10,'Everybody learners'),(7,NULL,NULL,0,16,12,'Everybody learners'),(8,NULL,NULL,0,18,18,'Everybody learners'),(9,NULL,NULL,0,19,23,'Everybody learners');
/*!40000 ALTER TABLE `tl_larsrc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_user`
--

DROP TABLE IF EXISTS `tl_larsrc11_user`;
CREATE TABLE `tl_larsrc11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `resource_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK30113BFCEC0D3147` (`session_uid`),
  KEY `FK30113BFC309ED320` (`resource_uid`),
  CONSTRAINT `FK30113BFC309ED320` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`),
  CONSTRAINT `FK30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_larsrc11_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_larsrc11_user`
--

LOCK TABLES `tl_larsrc11_user` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_user` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_user` VALUES (1,5,'Test','One','test1',0,NULL,2),(2,5,'Test','One','test1',0,NULL,3),(3,5,'Test','One','test1',0,NULL,4),(4,5,'Test','One','test1',0,NULL,5),(5,5,'Test','One','test1',0,NULL,6),(6,5,'Test','One','test1',0,NULL,7),(7,5,'Test','One','test1',0,NULL,8),(8,5,'Test','One','test1',0,NULL,9),(9,5,'Test','One','test1',0,NULL,10),(10,5,'Test','One','test1',0,NULL,11),(11,5,'Test','One','test1',0,NULL,12),(12,5,'Test','One','test1',0,NULL,13),(13,5,'Test','One','test1',0,NULL,14),(14,5,'Test','One','test1',0,NULL,15),(15,5,'Test','One','test1',0,NULL,16),(16,5,'Test','One','test1',0,NULL,17),(17,5,'Test','One','test1',0,NULL,18),(18,5,'Test','One','test1',0,NULL,19);
/*!40000 ALTER TABLE `tl_larsrc11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_content`
--

DROP TABLE IF EXISTS `tl_lasbmt11_content`;
CREATE TABLE `tl_lasbmt11_content` (
  `content_id` bigint(20) NOT NULL,
  `title` varchar(64) NOT NULL,
  `instruction` text,
  `define_later` smallint(6) NOT NULL,
  `run_offline` smallint(6) NOT NULL,
  `offline_instruction` text,
  `online_instruction` text,
  `content_in_use` smallint(6) default NULL,
  `lock_on_finished` smallint(6) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  `limit_upload` smallint(6) default NULL,
  `limit_upload_number` int(11) default NULL,
  `created` datetime default NULL,
  `created_by` bigint(20) default NULL,
  `updated` datetime default NULL,
  `mark_release_notify` tinyint(4) default '0',
  `file_submit_notify` tinyint(4) default '0',
  PRIMARY KEY  (`content_id`),
  KEY `FKAEF329AC172BC670` (`created_by`),
  CONSTRAINT `FKAEF329AC172BC670` FOREIGN KEY (`created_by`) REFERENCES `tl_lasbmt11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_content`
--

LOCK TABLES `tl_lasbmt11_content` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_content` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_content` VALUES (4,'Submit Files','Instructions',0,0,NULL,NULL,0,0,NULL,0,0,1,NULL,NULL,NULL,0,0),(47,'Essay Submission','<h2>This is the Learner Interface for Submit Files.</h2>\n<div><b>Again, LAMS provides a space for Authors to write Instructions for Learners:</b></div>\n<div>&nbsp;</div>\n<hr />\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Use this activity to submit your essay for Marking.</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">&nbsp;</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Please remember that your attachments should be Word Documents (.doc), Adobe Documents (.pdf), or Text Files (.txt) so that I can read them on my computer.</span><br />\n</span></div>\n<hr />\n<div>&nbsp;</div>\n<div>You have the option of uploading a file here if you would like - have a play with the activity and see what you can do.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>',0,0,'','',0,0,'',0,0,0,'2008-12-03 14:44:58',1,'2008-12-03 14:44:58',0,0),(84,'Essay Submission','<h2>This is the Learner Interface for Submit Files.</h2>\n<div><b>Again, LAMS provides a space for Authors to write Instructions for Learners:</b></div>\n<div>&nbsp;</div>\n<hr />\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Use this activity to submit your essay for Marking.</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">&nbsp;</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Please remember that your attachments should be Word Documents (.doc), Adobe Documents (.pdf), or Text Files (.txt) so that I can read them on my computer.</span><br />\n</span></div>\n<hr />\n<div>&nbsp;</div>\n<div>You have the option of uploading a file here if you would like - have a play with the activity and see what you can do.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>',0,0,'','',0,0,'',0,0,0,'2008-12-03 14:44:58',1,'2008-12-03 14:44:58',0,0);
/*!40000 ALTER TABLE `tl_lasbmt11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_instruction_files`
--

DROP TABLE IF EXISTS `tl_lasbmt11_instruction_files`;
CREATE TABLE `tl_lasbmt11_instruction_files` (
  `uid` bigint(20) NOT NULL auto_increment,
  `uuid` bigint(20) default NULL,
  `version_id` bigint(20) default NULL,
  `type` varchar(20) default NULL,
  `name` varchar(255) default NULL,
  `content_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKA75538F9785A173A` (`content_id`),
  CONSTRAINT `FKA75538F9785A173A` FOREIGN KEY (`content_id`) REFERENCES `tl_lasbmt11_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_instruction_files`
--

LOCK TABLES `tl_lasbmt11_instruction_files` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_instruction_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasbmt11_instruction_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_report`
--

DROP TABLE IF EXISTS `tl_lasbmt11_report`;
CREATE TABLE `tl_lasbmt11_report` (
  `report_id` bigint(20) NOT NULL auto_increment,
  `comments` varchar(250) default NULL,
  `marks` float default NULL,
  `date_marks_released` datetime default NULL,
  PRIMARY KEY  (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_report`
--

LOCK TABLES `tl_lasbmt11_report` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasbmt11_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_session`
--

DROP TABLE IF EXISTS `tl_lasbmt11_session`;
CREATE TABLE `tl_lasbmt11_session` (
  `session_id` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `content_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`session_id`),
  KEY `FKEC8C77C9785A173A` (`content_id`),
  CONSTRAINT `FKEC8C77C9785A173A` FOREIGN KEY (`content_id`) REFERENCES `tl_lasbmt11_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_session`
--

LOCK TABLES `tl_lasbmt11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_session` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_session` VALUES (28,0,84,'Everybody learners');
/*!40000 ALTER TABLE `tl_lasbmt11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_submission_details`
--

DROP TABLE IF EXISTS `tl_lasbmt11_submission_details`;
CREATE TABLE `tl_lasbmt11_submission_details` (
  `submission_id` bigint(20) NOT NULL auto_increment,
  `filePath` varchar(250) default NULL,
  `fileDescription` varchar(250) default NULL,
  `date_of_submission` datetime default NULL,
  `uuid` bigint(20) default NULL,
  `version_id` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `learner_id` bigint(20) default NULL,
  PRIMARY KEY  (`submission_id`),
  KEY `FK1411A53CFFD5A38B` (`learner_id`),
  KEY `FK1411A53C93C861A` (`session_id`),
  CONSTRAINT `FK1411A53C93C861A` FOREIGN KEY (`session_id`) REFERENCES `tl_lasbmt11_session` (`session_id`),
  CONSTRAINT `FK1411A53CFFD5A38B` FOREIGN KEY (`learner_id`) REFERENCES `tl_lasbmt11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_submission_details`
--

LOCK TABLES `tl_lasbmt11_submission_details` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_submission_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasbmt11_submission_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_user`
--

DROP TABLE IF EXISTS `tl_lasbmt11_user`;
CREATE TABLE `tl_lasbmt11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` int(11) default NULL,
  `finished` bit(1) default NULL,
  `session_id` bigint(20) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `last_name` varchar(255) default NULL,
  `content_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasbmt11_user`
--

LOCK TABLES `tl_lasbmt11_user` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_user` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_user` VALUES (1,5,0x00,NULL,'One','test1','Test',47);
/*!40000 ALTER TABLE `tl_lasbmt11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_attachment`
--

DROP TABLE IF EXISTS `tl_lascrb11_attachment`;
CREATE TABLE `tl_lascrb11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `scribe_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK57953706B3FA1495` (`scribe_uid`),
  CONSTRAINT `FK57953706B3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_attachment`
--

LOCK TABLES `tl_lascrb11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lascrb11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_heading`
--

DROP TABLE IF EXISTS `tl_lascrb11_heading`;
CREATE TABLE `tl_lascrb11_heading` (
  `uid` bigint(20) NOT NULL auto_increment,
  `heading` text,
  `scribe_uid` bigint(20) default NULL,
  `display_order` int(11) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK428A22FFB3FA1495` (`scribe_uid`),
  CONSTRAINT `FK428A22FFB3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_heading`
--

LOCK TABLES `tl_lascrb11_heading` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_heading` DISABLE KEYS */;
INSERT INTO `tl_lascrb11_heading` VALUES (1,'Scribe Heading',1,0);
/*!40000 ALTER TABLE `tl_lascrb11_heading` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_report_entry`
--

DROP TABLE IF EXISTS `tl_lascrb11_report_entry`;
CREATE TABLE `tl_lascrb11_report_entry` (
  `uid` bigint(20) NOT NULL auto_increment,
  `entry_text` text,
  `scribe_heading_uid` bigint(20) default NULL,
  `scribe_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK5439FACAEA50D086` (`scribe_heading_uid`),
  KEY `FK5439FACA1C266FAE` (`scribe_session_uid`),
  CONSTRAINT `FK5439FACA1C266FAE` FOREIGN KEY (`scribe_session_uid`) REFERENCES `tl_lascrb11_session` (`uid`),
  CONSTRAINT `FK5439FACAEA50D086` FOREIGN KEY (`scribe_heading_uid`) REFERENCES `tl_lascrb11_heading` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_report_entry`
--

LOCK TABLES `tl_lascrb11_report_entry` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_report_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lascrb11_report_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_scribe`
--

DROP TABLE IF EXISTS `tl_lascrb11_scribe`;
CREATE TABLE `tl_lascrb11_scribe` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `auto_select_scribe` bit(1) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `aggregated_reports` bit(1) default '\0',
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_scribe`
--

LOCK TABLES `tl_lascrb11_scribe` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_scribe` DISABLE KEYS */;
INSERT INTO `tl_lascrb11_scribe` VALUES (1,NULL,NULL,NULL,'Scribe','Instructions',0x00,0x01,0x01,0x00,NULL,'','',0x00,0x00,10,0x00);
/*!40000 ALTER TABLE `tl_lascrb11_scribe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_session`
--

DROP TABLE IF EXISTS `tl_lascrb11_session`;
CREATE TABLE `tl_lascrb11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `version` int(11) NOT NULL,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `scribe_uid` bigint(20) default NULL,
  `appointed_scribe_uid` bigint(20) default NULL,
  `force_complete` bit(1) default NULL,
  `report_submitted` bit(1) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK89732793B3FA1495` (`scribe_uid`),
  KEY `FK89732793E46919FF` (`appointed_scribe_uid`),
  CONSTRAINT `FK89732793E46919FF` FOREIGN KEY (`appointed_scribe_uid`) REFERENCES `tl_lascrb11_user` (`uid`),
  CONSTRAINT `FK89732793B3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_session`
--

LOCK TABLES `tl_lascrb11_session` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lascrb11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_user`
--

DROP TABLE IF EXISTS `tl_lascrb11_user`;
CREATE TABLE `tl_lascrb11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `scribe_session_uid` bigint(20) default NULL,
  `report_approved` bit(1) default NULL,
  `started_activity` bit(1) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK187DAFEE1C266FAE` (`scribe_session_uid`),
  CONSTRAINT `FK187DAFEE1C266FAE` FOREIGN KEY (`scribe_session_uid`) REFERENCES `tl_lascrb11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lascrb11_user`
--

LOCK TABLES `tl_lascrb11_user` WRITE;
/*!40000 ALTER TABLE `tl_lascrb11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lascrb11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_attachment`
--

DROP TABLE IF EXISTS `tl_lasprd10_attachment`;
CREATE TABLE `tl_lasprd10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `spreadsheet_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_2065267438_1E7009430E79035` (`spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_1E7009430E79035` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_attachment`
--

LOCK TABLES `tl_lasprd10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasprd10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_session`
--

DROP TABLE IF EXISTS `tl_lasprd10_session`;
CREATE TABLE `tl_lasprd10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `spreadsheet_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_2065267438_24AA78C530E79035` (`spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_24AA78C530E79035` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_session`
--

LOCK TABLES `tl_lasprd10_session` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_session` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_session` VALUES (1,NULL,NULL,0,3,17,'Everybody learners');
/*!40000 ALTER TABLE `tl_lasprd10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet`;
CREATE TABLE `tl_lasprd10_spreadsheet` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default NULL,
  `is_learner_allowed_to_save` tinyint(4) default NULL,
  `is_marking_enabled` tinyint(4) default NULL,
  `lock_on_finished` tinyint(4) default NULL,
  `instructions` text,
  `code` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default NULL,
  `define_later` tinyint(4) default NULL,
  `content_id` bigint(20) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_2065267438_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_2065267438_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_lasprd10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_spreadsheet`
--

LOCK TABLES `tl_lasprd10_spreadsheet` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_spreadsheet` VALUES (1,NULL,NULL,NULL,'Spreadsheet',0,1,0,0,'Instructions ','',NULL,NULL,0,0,13,NULL,0),(2,NULL,'2008-11-14 14:29:02',1,'Spreadsheet Demonstration',0,1,0,0,'<div>\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Spreadsheet</h2>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>\n<p>The <b>Spreadsheet Activity</b> allows a sequence author to provide data in a spreadsheet format to Learners. <br clear=\"all\" />\nLearners can then perform their own calculations on and manipulations of the data.</p>\n<p>By default, the Spreadsheet tool allows each learner to save their own individual copy of the spreadsheet. The activity can be configured to allow Monitors to mark each Learners spreadsheet.</p>\n<h5>Spreadsheet Functions</h5>\n<p>The Spreadsheet can be viewed in one of several modes. These are changed by clicking the labels in the top-right corner of the applet.</p>\n<ul>\n    <li>Values: shows all the calculated values on the screen (default mode)\n    <ul>\n        <li>Auto: calculates and updates values in real time</li>\n        <li>Manual: Disables automatic calculations (gives better performance)\n        <ul>\n            <li>Click <b>Refresh</b> to update the values in the spreadsheet if Manual mode is selected.</li>\n        </ul>\n        </li>\n    </ul>\n    </li>\n    <li>Formulas: shows the formulas in the spreadsheet.</li>\n</ul>\n<div>&nbsp;</div>\n<div><b>Values </b>displays the <b>actual values</b> of the calculations and data in each cell.</div>\n<div><b>Formulas </b>shows the cacluations behind the cell.</div>\n<h4 style=\"text-align: center;\">Explore the Demonstration Spreadsheet below.</h4>\n<h4>&nbsp;</h4>\n</div>\n</div>','dbCells = [\n  [0,0,\"SALES 3rd Quarter\",\"\"], // A1\n\n  [1,1,\"John\",\"\"], // B2\n  [2,1,\"Paul\",\"\"], // C2\n  [3,1,\"Michael\",\"\"], // D2\n  [5,1,\"MONTHLY Totals\",\"\"], // F2\n  [6,1,\"MONTHLY Average\",\"\"], // G2\n\n  [0,2,\"JULY\",\"\"], // A3\n  [1,2,\"210\",\"\"], // B3\n  [2,2,\"195\",\"\"], // C3\n  [3,2,\"203\",\"\"], // D3\n  [5,2,\"=sum(B3:D3)\",\"\"], // F3\n  [6,2,\"=avg(B3:D3)\",\"\"], // G3\n\n  [0,3,\"AUGUST\",\"\"], // A4\n  [1,3,\"312\",\"\"], // B4\n  [2,3,\"263\",\"\"], // C4\n  [3,3,\"278\",\"\"], // D4\n  [5,3,\"=sum(B4:D4)\",\"\"], // F4\n  [6,3,\"=avg(B4:D4)\",\"\"], // G4\n\n  [0,4,\"SEPTEMBER\",\"\"], // A5\n  [1,4,\"280\",\"\"], // B5\n  [2,4,\"243\",\"\"], // C5\n  [3,4,\"216\",\"\"], // D5\n  [5,4,\"=sum(B5:D5)\",\"\"], // F5\n  [6,4,\"=avg(B5:D5)\",\"\"], // G5\n\n  [0,6,\"Seller Totals\",\"\"], // A7\n  [1,6,\"=sum(B3:B5)\",\"\"], // B7\n  [2,6,\"=sum(C3:C5)\",\"\"], // C7\n  [3,6,\"=sum(D3:D5)\",\"\"], // D7\n  [4,6,\"GRAND Total\",\"\"], // E7\n  [5,6,\"=sum(B3:D5)\",\"\"], // F7\n];\n','','',0,0,37,'',0),(3,NULL,'2008-11-14 14:29:02',2,'Spreadsheet Demonstration',0,1,0,0,'<div>\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Spreadsheet</h2>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>\n<p>The <b>Spreadsheet Activity</b> allows a sequence author to provide data in a spreadsheet format to Learners. <br clear=\"all\" />\nLearners can then perform their own calculations on and manipulations of the data.</p>\n<p>By default, the Spreadsheet tool allows each learner to save their own individual copy of the spreadsheet. The activity can be configured to allow Monitors to mark each Learners spreadsheet.</p>\n<h5>Spreadsheet Functions</h5>\n<p>The Spreadsheet can be viewed in one of several modes. These are changed by clicking the labels in the top-right corner of the applet.</p>\n<ul>\n    <li>Values: shows all the calculated values on the screen (default mode)\n    <ul>\n        <li>Auto: calculates and updates values in real time</li>\n        <li>Manual: Disables automatic calculations (gives better performance)\n        <ul>\n            <li>Click <b>Refresh</b> to update the values in the spreadsheet if Manual mode is selected.</li>\n        </ul>\n        </li>\n    </ul>\n    </li>\n    <li>Formulas: shows the formulas in the spreadsheet.</li>\n</ul>\n<div>&nbsp;</div>\n<div><b>Values </b>displays the <b>actual values</b> of the calculations and data in each cell.</div>\n<div><b>Formulas </b>shows the cacluations behind the cell.</div>\n<h4 style=\"text-align: center;\">Explore the Demonstration Spreadsheet below.</h4>\n<h4>&nbsp;</h4>\n</div>\n</div>','dbCells = [\n  [0,0,\"SALES 3rd Quarter\",\"\"], // A1\n\n  [1,1,\"John\",\"\"], // B2\n  [2,1,\"Paul\",\"\"], // C2\n  [3,1,\"Michael\",\"\"], // D2\n  [5,1,\"MONTHLY Totals\",\"\"], // F2\n  [6,1,\"MONTHLY Average\",\"\"], // G2\n\n  [0,2,\"JULY\",\"\"], // A3\n  [1,2,\"210\",\"\"], // B3\n  [2,2,\"195\",\"\"], // C3\n  [3,2,\"203\",\"\"], // D3\n  [5,2,\"=sum(B3:D3)\",\"\"], // F3\n  [6,2,\"=avg(B3:D3)\",\"\"], // G3\n\n  [0,3,\"AUGUST\",\"\"], // A4\n  [1,3,\"312\",\"\"], // B4\n  [2,3,\"263\",\"\"], // C4\n  [3,3,\"278\",\"\"], // D4\n  [5,3,\"=sum(B4:D4)\",\"\"], // F4\n  [6,3,\"=avg(B4:D4)\",\"\"], // G4\n\n  [0,4,\"SEPTEMBER\",\"\"], // A5\n  [1,4,\"280\",\"\"], // B5\n  [2,4,\"243\",\"\"], // C5\n  [3,4,\"216\",\"\"], // D5\n  [5,4,\"=sum(B5:D5)\",\"\"], // F5\n  [6,4,\"=avg(B5:D5)\",\"\"], // G5\n\n  [0,6,\"Seller Totals\",\"\"], // A7\n  [1,6,\"=sum(B3:B5)\",\"\"], // B7\n  [2,6,\"=sum(C3:C5)\",\"\"], // C7\n  [3,6,\"=sum(D3:D5)\",\"\"], // D7\n  [4,6,\"GRAND Total\",\"\"], // E7\n  [5,6,\"=sum(B3:D5)\",\"\"], // F7\n];\n','','',0,0,75,'',0);
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet_mark`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet_mark`;
CREATE TABLE `tl_lasprd10_spreadsheet_mark` (
  `uid` bigint(20) NOT NULL auto_increment,
  `marks` varchar(255) default NULL,
  `comments` text,
  `date_marks_released` datetime default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_spreadsheet_mark`
--

LOCK TABLES `tl_lasprd10_spreadsheet_mark` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet_mark` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet_mark` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_user`
--

DROP TABLE IF EXISTS `tl_lasprd10_user`;
CREATE TABLE `tl_lasprd10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `spreadsheet_uid` bigint(20) default NULL,
  `user_modified_spreadsheet_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_2065267438_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_2065267438_30113BFC309ED320` (`spreadsheet_uid`),
  KEY `FK_NEW_2065267438_693580A441F9365D` (`user_modified_spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_693580A441F9365D` FOREIGN KEY (`user_modified_spreadsheet_uid`) REFERENCES `tl_lasprd10_user_modified_spreadsheet` (`uid`),
  CONSTRAINT `FK_NEW_2065267438_30113BFC309ED320` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`),
  CONSTRAINT `FK_NEW_2065267438_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_lasprd10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_user`
--

LOCK TABLES `tl_lasprd10_user` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_user` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_user` VALUES (1,5,'Test','One','test1',0,NULL,2,NULL),(2,5,'Test','One','test1',0,NULL,3,NULL);
/*!40000 ALTER TABLE `tl_lasprd10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_user_modified_spreadsheet`
--

DROP TABLE IF EXISTS `tl_lasprd10_user_modified_spreadsheet`;
CREATE TABLE `tl_lasprd10_user_modified_spreadsheet` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_modified_spreadsheet` text,
  `mark_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_lasprd10_user_modified_spreadsheet_1` (`mark_id`),
  CONSTRAINT `FK_tl_lasprd10_user_modified_spreadsheet_1` FOREIGN KEY (`mark_id`) REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasprd10_user_modified_spreadsheet`
--

LOCK TABLES `tl_lasprd10_user_modified_spreadsheet` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_user_modified_spreadsheet` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasprd10_user_modified_spreadsheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_answer`
--

DROP TABLE IF EXISTS `tl_lasurv11_answer`;
CREATE TABLE `tl_lasurv11_answer` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_uid` bigint(20) default NULL,
  `user_uid` bigint(20) default NULL,
  `answer_choices` varchar(255) default NULL,
  `udpate_date` datetime default NULL,
  `answer_text` text,
  PRIMARY KEY  (`uid`),
  KEY `FK6DAAFE3BB1423DC1` (`user_uid`),
  KEY `FK6DAAFE3B25F3BB77` (`question_uid`),
  CONSTRAINT `FK6DAAFE3B25F3BB77` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`),
  CONSTRAINT `FK6DAAFE3BB1423DC1` FOREIGN KEY (`user_uid`) REFERENCES `tl_lasurv11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_answer`
--

LOCK TABLES `tl_lasurv11_answer` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_attachment`
--

DROP TABLE IF EXISTS `tl_lasurv11_attachment`;
CREATE TABLE `tl_lasurv11_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `survey_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKD92A9120D14146E5` (`survey_uid`),
  CONSTRAINT `FKD92A9120D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_attachment`
--

LOCK TABLES `tl_lasurv11_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_condition_questions`
--

DROP TABLE IF EXISTS `tl_lasurv11_condition_questions`;
CREATE TABLE `tl_lasurv11_condition_questions` (
  `condition_id` bigint(20) NOT NULL default '0',
  `question_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`question_uid`),
  KEY `SurveyConditionQuestionToSurveyQuestion` (`question_uid`),
  CONSTRAINT `SurveyConditionQuestionToSurveyCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lasurv11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionQuestionToSurveyQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_condition_questions`
--

LOCK TABLES `tl_lasurv11_condition_questions` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_condition_questions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_condition_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_conditions`
--

DROP TABLE IF EXISTS `tl_lasurv11_conditions`;
CREATE TABLE `tl_lasurv11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `SurveyConditionToSurvey` (`content_uid`),
  CONSTRAINT `SurveyConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionToSurvey` FOREIGN KEY (`content_uid`) REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_conditions`
--

LOCK TABLES `tl_lasurv11_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_option`
--

DROP TABLE IF EXISTS `tl_lasurv11_option`;
CREATE TABLE `tl_lasurv11_option` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` text,
  `sequence_id` int(11) default NULL,
  `question_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK85AB46F26966134F` (`question_uid`),
  CONSTRAINT `FK85AB46F26966134F` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_option`
--

LOCK TABLES `tl_lasurv11_option` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_option` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_option` VALUES (1,'Option 1',0,1),(2,'Option 2',1,1),(3,'Option 3',2,1),(4,'Option 1',0,2),(5,'Option 2',1,2),(6,'Option 3',2,2),(7,'Apple',0,4),(8,'Banana',1,4),(9,'Oranges',2,4),(10,'Lamb!',1,6),(11,'Pony',0,6),(12,'Puppy',2,6),(13,'Oranges',2,8),(14,'Apple',0,8),(15,'Banana',1,8),(16,'Pony',0,9),(17,'Puppy',2,9),(18,'Lamb!',1,9);
/*!40000 ALTER TABLE `tl_lasurv11_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_question`
--

DROP TABLE IF EXISTS `tl_lasurv11_question`;
CREATE TABLE `tl_lasurv11_question` (
  `uid` bigint(20) NOT NULL auto_increment,
  `sequence_id` int(11) default NULL,
  `description` text,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `question_type` smallint(6) default NULL,
  `append_text` smallint(6) default NULL,
  `optional` smallint(6) default NULL,
  `allow_multiple_answer` smallint(6) default NULL,
  `survey_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK872D4F23D14146E5` (`survey_uid`),
  KEY `FK872D4F23E4C99A5F` (`create_by`),
  CONSTRAINT `FK872D4F23E4C99A5F` FOREIGN KEY (`create_by`) REFERENCES `tl_lasurv11_user` (`uid`),
  CONSTRAINT `FK872D4F23D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_question`
--

LOCK TABLES `tl_lasurv11_question` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_question` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_question` VALUES (1,1,'Sample Multiple choice - only one response allowed?',NULL,'2008-12-03 14:39:36',1,0,0,0,1),(2,2,'Sample Multiple choice - multiple response allowed?',NULL,'2008-12-03 14:39:36',2,0,0,1,1),(3,3,'Sample Free text question?',NULL,'2008-12-03 14:39:36',3,0,0,0,1),(4,2,'<div><b>Which fruits do you like?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',2,0,0,1,2),(5,3,'<div><b>What\'s the best thing about LAMS?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',3,0,0,0,2),(6,1,'<div><b>What\'s your favourite baby animal?<br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',1,0,0,0,2),(7,3,'<div><b>What\'s the best thing about LAMS?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',3,0,0,0,3),(8,2,'<div><b>Which fruits do you like?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',2,0,0,1,3),(9,1,'<div><b>What\'s your favourite baby animal?<br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',1,0,0,0,3);
/*!40000 ALTER TABLE `tl_lasurv11_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_session`
--

DROP TABLE IF EXISTS `tl_lasurv11_session`;
CREATE TABLE `tl_lasurv11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `survey_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKF08793B9D14146E5` (`survey_uid`),
  CONSTRAINT `FKF08793B9D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_session`
--

LOCK TABLES `tl_lasurv11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_session` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_session` VALUES (1,NULL,NULL,3,27,'Everybody learners');
/*!40000 ALTER TABLE `tl_lasurv11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_survey`
--

DROP TABLE IF EXISTS `tl_lasurv11_survey`;
CREATE TABLE `tl_lasurv11_survey` (
  `uid` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) default NULL,
  `run_offline` smallint(6) default NULL,
  `lock_on_finished` smallint(6) default NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` smallint(6) default NULL,
  `define_later` smallint(6) default NULL,
  `content_id` bigint(20) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  `show_questions_on_one_page` smallint(6) default NULL,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `answer_submit_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK8CC465D7E4C99A5F` (`create_by`),
  CONSTRAINT `FK8CC465D7E4C99A5F` FOREIGN KEY (`create_by`) REFERENCES `tl_lasurv11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_survey`
--

LOCK TABLES `tl_lasurv11_survey` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_survey` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_survey` VALUES (1,'Survey',0,1,'Instructions',NULL,NULL,0,0,9,NULL,0,1,NULL,NULL,NULL,0),(2,'Survey Demonstration',0,1,'<div><img hspace=\"6\" align=\"left\" width=\"116\" height=\"103\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h2>This is the Survey activity.&nbsp;</h2>\n<h3>&nbsp;</h3>\n<h3>Like all activities, this is the space for Learner Instructions.</h3>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h4><b>Below, you&rsquo;ll see several different types of Survey questions:</b>&nbsp;</h4>\n<ul>\n    <li>Single Response Multiple Choice</li>\n    <li>Multiple Choice Multiple Choice</li>\n    <li>Free Text Questions</li>\n</ul>\n<div>&nbsp;</div>','','',0,0,46,'',0,1,NULL,'2008-04-18 15:51:31',1,0),(3,'Survey Demonstration',0,1,'<div><img hspace=\"6\" align=\"left\" width=\"116\" height=\"103\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h2>This is the Survey activity.&nbsp;</h2>\n<h3>&nbsp;</h3>\n<h3>Like all activities, this is the space for Learner Instructions.</h3>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h4><b>Below, you&rsquo;ll see several different types of Survey questions:</b>&nbsp;</h4>\n<ul>\n    <li>Single Response Multiple Choice</li>\n    <li>Multiple Choice Multiple Choice</li>\n    <li>Free Text Questions</li>\n</ul>\n<div>&nbsp;</div>','','',0,0,90,'',0,1,NULL,'2008-04-18 15:51:31',2,0);
/*!40000 ALTER TABLE `tl_lasurv11_survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_user`
--

DROP TABLE IF EXISTS `tl_lasurv11_user`;
CREATE TABLE `tl_lasurv11_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_uid` bigint(20) default NULL,
  `survey_uid` bigint(20) default NULL,
  `session_finished` smallint(6) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK633F25884F803F63` (`session_uid`),
  KEY `FK633F2588D14146E5` (`survey_uid`),
  CONSTRAINT `FK633F2588D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`),
  CONSTRAINT `FK633F25884F803F63` FOREIGN KEY (`session_uid`) REFERENCES `tl_lasurv11_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lasurv11_user`
--

LOCK TABLES `tl_lasurv11_user` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_user` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_user` VALUES (1,5,'Test','One','test1',NULL,2,0),(2,5,'Test','One','test1',NULL,3,0);
/*!40000 ALTER TABLE `tl_lasurv11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_attachment`
--

DROP TABLE IF EXISTS `tl_latask10_attachment`;
CREATE TABLE `tl_latask10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `taskList_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_174079138_1E7009430E79035` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_1E7009430E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_attachment`
--

LOCK TABLES `tl_latask10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_latask10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_condition`
--

DROP TABLE IF EXISTS `tl_latask10_condition`;
CREATE TABLE `tl_latask10_condition` (
  `condition_uid` bigint(20) NOT NULL auto_increment,
  `sequence_id` int(11) default NULL,
  `taskList_uid` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`condition_uid`),
  KEY `FK_tl_latask10_condition_1` (`taskList_uid`),
  CONSTRAINT `FK_tl_latask10_condition_1` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_condition`
--

LOCK TABLES `tl_latask10_condition` WRITE;
/*!40000 ALTER TABLE `tl_latask10_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_condition_tl_item`
--

DROP TABLE IF EXISTS `tl_latask10_condition_tl_item`;
CREATE TABLE `tl_latask10_condition_tl_item` (
  `uid` bigint(20) NOT NULL,
  `condition_uid` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`,`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_1` (`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_2` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_2` FOREIGN KEY (`uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_1` FOREIGN KEY (`condition_uid`) REFERENCES `tl_latask10_condition` (`condition_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_condition_tl_item`
--

LOCK TABLES `tl_latask10_condition_tl_item` WRITE;
/*!40000 ALTER TABLE `tl_latask10_condition_tl_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_condition_tl_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_item_attachment`
--

DROP TABLE IF EXISTS `tl_latask10_item_attachment`;
CREATE TABLE `tl_latask10_item_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `taskList_item_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_latask10_item_attachment_1` (`taskList_item_uid`),
  KEY `FK_tl_latask10_item_attachment_2` (`create_by`),
  CONSTRAINT `FK_tl_latask10_item_attachment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_tl_latask10_item_attachment_1` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_item_attachment`
--

LOCK TABLES `tl_latask10_item_attachment` WRITE;
/*!40000 ALTER TABLE `tl_latask10_item_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_item_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_item_comment`
--

DROP TABLE IF EXISTS `tl_latask10_item_comment`;
CREATE TABLE `tl_latask10_item_comment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `comment` text,
  `taskList_item_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_latask10_item_comment_3` (`taskList_item_uid`),
  KEY `FK_tl_latask10_item_comment_2` (`create_by`),
  CONSTRAINT `FK_tl_latask10_item_comment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_tl_latask10_item_comment_3` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_item_comment`
--

LOCK TABLES `tl_latask10_item_comment` WRITE;
/*!40000 ALTER TABLE `tl_latask10_item_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_item_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_item_log`
--

DROP TABLE IF EXISTS `tl_latask10_item_log`;
CREATE TABLE `tl_latask10_item_log` (
  `uid` bigint(20) NOT NULL auto_increment,
  `access_date` datetime default NULL,
  `taskList_item_uid` bigint(20) default NULL,
  `user_uid` bigint(20) default NULL,
  `complete` tinyint(4) default NULL,
  `session_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_174079138_693580A438BF8DFE` (`taskList_item_uid`),
  KEY `FK_NEW_174079138_693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK_NEW_174079138_693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_NEW_174079138_693580A438BF8DFE` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_item_log`
--

LOCK TABLES `tl_latask10_item_log` WRITE;
/*!40000 ALTER TABLE `tl_latask10_item_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_item_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_session`
--

DROP TABLE IF EXISTS `tl_latask10_session`;
CREATE TABLE `tl_latask10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `taskList_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_174079138_24AA78C530E79035` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_24AA78C530E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_session`
--

LOCK TABLES `tl_latask10_session` WRITE;
/*!40000 ALTER TABLE `tl_latask10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_tasklist`
--

DROP TABLE IF EXISTS `tl_latask10_tasklist`;
CREATE TABLE `tl_latask10_tasklist` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default NULL,
  `define_later` tinyint(4) default NULL,
  `content_id` bigint(20) default NULL,
  `lock_when_finished` tinyint(4) default NULL,
  `is_sequential_order` tinyint(4) default NULL,
  `minimum_number_tasks` int(11) default NULL,
  `allow_contribute_tasks` tinyint(4) default NULL,
  `is_monitor_verification_required` tinyint(4) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_174079138_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_174079138_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_tasklist`
--

LOCK TABLES `tl_latask10_tasklist` WRITE;
/*!40000 ALTER TABLE `tl_latask10_tasklist` DISABLE KEYS */;
INSERT INTO `tl_latask10_tasklist` VALUES (1,NULL,NULL,NULL,'Task List',0,'Instructions ',NULL,NULL,0,0,11,0,0,0,0,0,NULL,0);
/*!40000 ALTER TABLE `tl_latask10_tasklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_tasklist_item`
--

DROP TABLE IF EXISTS `tl_latask10_tasklist_item`;
CREATE TABLE `tl_latask10_tasklist_item` (
  `uid` bigint(20) NOT NULL auto_increment,
  `sequence_id` int(11) default NULL,
  `description` text,
  `init_item` varchar(255) default NULL,
  `organization_xml` text,
  `title` varchar(255) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `create_by_author` tinyint(4) default NULL,
  `is_required` tinyint(4) default NULL,
  `is_comments_allowed` tinyint(4) default NULL,
  `is_comments_required` tinyint(4) default NULL,
  `is_files_allowed` tinyint(4) default NULL,
  `is_files_required` tinyint(4) default NULL,
  `is_comments_files_allowed` tinyint(4) default NULL,
  `show_comments_to_all` tinyint(4) default NULL,
  `is_child_task` tinyint(4) default NULL,
  `parent_task_name` varchar(255) default NULL,
  `taskList_uid` bigint(20) default NULL,
  `session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_174079138_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_174079138_F52D1F9330E79035` (`taskList_uid`),
  KEY `FK_NEW_174079138_F52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_latask10_session` (`uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F9330E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_tasklist_item`
--

LOCK TABLES `tl_latask10_tasklist_item` WRITE;
/*!40000 ALTER TABLE `tl_latask10_tasklist_item` DISABLE KEYS */;
INSERT INTO `tl_latask10_tasklist_item` VALUES (1,1,NULL,NULL,NULL,'Task number 1',NULL,'2008-12-03 14:39:54',1,0,0,0,0,0,0,1,0,NULL,1,NULL);
/*!40000 ALTER TABLE `tl_latask10_tasklist_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_user`
--

DROP TABLE IF EXISTS `tl_latask10_user`;
CREATE TABLE `tl_latask10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `taskList_uid` bigint(20) default NULL,
  `is_verified_by_monitor` tinyint(4) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_174079138_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_174079138_30113BFC309ED320` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_30113BFC309ED320` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`),
  CONSTRAINT `FK_NEW_174079138_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_latask10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_latask10_user`
--

LOCK TABLES `tl_latask10_user` WRITE;
/*!40000 ALTER TABLE `tl_latask10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_content`
--

DROP TABLE IF EXISTS `tl_lavote11_content`;
CREATE TABLE `tl_lavote11_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime default NULL,
  `update_date` datetime default NULL,
  `maxNominationCount` varchar(20) NOT NULL default '1',
  `allowText` tinyint(1) NOT NULL default '0',
  `reflect` tinyint(1) NOT NULL default '0',
  `created_by` bigint(20) NOT NULL default '0',
  `run_offline` tinyint(1) NOT NULL default '0',
  `define_later` tinyint(1) NOT NULL default '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_in_use` tinyint(1) NOT NULL default '0',
  `lock_on_finish` tinyint(1) NOT NULL default '1',
  `retries` tinyint(1) NOT NULL default '0',
  `reflectionSubject` text,
  `show_results` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_content`
--

LOCK TABLES `tl_lavote11_content` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_content` DISABLE KEYS */;
INSERT INTO `tl_lavote11_content` VALUES (1,7,'Voting','Instructions','2008-12-03 14:39:19',NULL,'1',0,0,1,0,0,'','',0,0,0,NULL,1),(2,48,'Voting Demonstration','<div><img width=\"116\" hspace=\"6\" height=\"103\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /></div>\n<div><b>Here&rsquo;s a demonstration Voting activity.</b></div>\n<div>&nbsp;</div>\n<div>While this particular activity is a little bit... <i>trivial...</i> there&rsquo;s no reason why you can&rsquo;t have a very large nomination form and collate responses from as many learners are on your server.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h3>What&rsquo;s your favourite flavour for ice-cream?</h3>',NULL,'2008-04-11 16:39:37','1',1,0,5,0,0,'','',0,0,0,'',1),(3,88,'Voting Demonstration','<div><img width=\"116\" hspace=\"6\" height=\"103\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /></div>\n<div><b>Here&rsquo;s a demonstration Voting activity.</b></div>\n<div>&nbsp;</div>\n<div>While this particular activity is a little bit... <i>trivial...</i> there&rsquo;s no reason why you can&rsquo;t have a very large nomination form and collate responses from as many learners are on your server.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h3>What&rsquo;s your favourite flavour for ice-cream?</h3>',NULL,'2008-04-11 16:39:37','1',1,0,5,0,0,'','',0,0,0,'',1);
/*!40000 ALTER TABLE `tl_lavote11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_nomination_content`
--

DROP TABLE IF EXISTS `tl_lavote11_nomination_content`;
CREATE TABLE `tl_lavote11_nomination_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `nomination` text,
  `display_order` int(5) default NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tl_lavote11_nomination_content_1` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_nomination_content`
--

LOCK TABLES `tl_lavote11_nomination_content` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_nomination_content` DISABLE KEYS */;
INSERT INTO `tl_lavote11_nomination_content` VALUES (1,'Sample Nomination 1',1,1),(2,'Sample Nomination 2',2,1),(3,'<div><span style=\"font-size: small;\"><span style=\"color: rgb(255, 0, 255);\"><b>Strawberry!</b></span></span></div>',1,2),(4,'<div><span style=\"color: rgb(128, 0, 0);\">\n<div><span style=\"font-size: small;\"><b>Chocolate!</b></span></div>\n</span></div>\n<div><span style=\"color: rgb(128, 0, 0);\">  </span></div>',2,2),(5,'<div><span style=\"font-size: small;\"><b>Vanilla!</b></span></div>\n</span></div>\n',3,2),(6,'<div><span style=\"font-size: small;\"><span style=\"color: rgb(255, 0, 255);\"><b>Strawberry!</b></span></span></div>',1,3),(7,'<div><span style=\"color: rgb(128, 0, 0);\">\n<div><span style=\"font-size: small;\"><b>Chocolate!</b></span></div>\n</span></div>\n<div><span style=\"color: rgb(128, 0, 0);\">  </span></div>',2,3),(8,'<div><span style=\"font-size: small;\"><b>Vanilla!</b></span></div>\n</span></div>\n',3,3);
/*!40000 ALTER TABLE `tl_lavote11_nomination_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_session`
--

DROP TABLE IF EXISTS `tl_lavote11_session`;
CREATE TABLE `tl_lavote11_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `vote_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime default NULL,
  `session_end_date` datetime default NULL,
  `session_name` varchar(100) default NULL,
  `session_status` varchar(100) default NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_session_1` (`vote_session_id`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tl_lavote11_session_1` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_session`
--

LOCK TABLES `tl_lavote11_session` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_session` DISABLE KEYS */;
INSERT INTO `tl_lavote11_session` VALUES (1,30,'2008-12-03 14:45:36',NULL,'Everybody learners','INCOMPLETE',3);
/*!40000 ALTER TABLE `tl_lavote11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lavote11_uploadedfile`;
CREATE TABLE `tl_lavote11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL auto_increment,
  `uuid` varchar(255) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`submissionId`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tablex_111` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_uploadedfile`
--

LOCK TABLES `tl_lavote11_uploadedfile` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_uploadedfile` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavote11_uploadedfile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_usr`
--

DROP TABLE IF EXISTS `tl_lavote11_usr`;
CREATE TABLE `tl_lavote11_usr` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `vote_session_id` bigint(20) NOT NULL,
  `username` varchar(255) default NULL,
  `fullname` varchar(255) default NULL,
  `responseFinalised` tinyint(1) NOT NULL default '0',
  `finalScreenRequested` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`uid`),
  KEY `vote_session_id` (`vote_session_id`),
  CONSTRAINT `FK_tl_lavote11_usr_1` FOREIGN KEY (`vote_session_id`) REFERENCES `tl_lavote11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_usr`
--

LOCK TABLES `tl_lavote11_usr` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_usr` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavote11_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_usr_attempt`
--

DROP TABLE IF EXISTS `tl_lavote11_usr_attempt`;
CREATE TABLE `tl_lavote11_usr_attempt` (
  `uid` bigint(20) NOT NULL auto_increment,
  `que_usr_id` bigint(20) NOT NULL,
  `vote_nomination_content_id` bigint(20) NOT NULL,
  `attempt_time` datetime default NULL,
  `time_zone` varchar(255) default NULL,
  `userEntry` varchar(255) default NULL,
  `singleUserEntry` tinyint(1) NOT NULL default '0',
  `visible` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`uid`),
  KEY `que_usr_id` (`que_usr_id`),
  KEY `vote_nomination_content_id` (`vote_nomination_content_id`),
  CONSTRAINT `FK_tl_lavote11_usr_attempt_2` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_lavote11_usr` (`uid`),
  CONSTRAINT `FK_tl_lavote11_usr_attempt_3` FOREIGN KEY (`vote_nomination_content_id`) REFERENCES `tl_lavote11_nomination_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lavote11_usr_attempt`
--

LOCK TABLES `tl_lavote11_usr_attempt` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_usr_attempt` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavote11_usr_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_attachment`
--

DROP TABLE IF EXISTS `tl_lawiki10_attachment`;
CREATE TABLE `tl_lawiki10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `wiki_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK9406D87760B3B03B` (`wiki_uid`),
  CONSTRAINT `FK9406D87760B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_attachment`
--

LOCK TABLES `tl_lawiki10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lawiki10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_session`
--

DROP TABLE IF EXISTS `tl_lawiki10_session`;
CREATE TABLE `tl_lawiki10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `wiki_uid` bigint(20) default NULL,
  `wiki_main_page_uid` bigint(20) default NULL,
  `content_folder_id` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKF01D63C260B3B03B` (`wiki_uid`),
  KEY `FKF01D63C2A3FF7EC0` (`wiki_main_page_uid`),
  CONSTRAINT `FKF01D63C2A3FF7EC0` FOREIGN KEY (`wiki_main_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`),
  CONSTRAINT `FKF01D63C260B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_session`
--

LOCK TABLES `tl_lawiki10_session` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_session` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_session` VALUES (1,NULL,NULL,NULL,38,'Everybody learners',3,7,'2c94e4901dfaef8e011dfaf36d610071');
/*!40000 ALTER TABLE `tl_lawiki10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_user`
--

DROP TABLE IF EXISTS `tl_lawiki10_user`;
CREATE TABLE `tl_lawiki10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `wiki_session_uid` bigint(20) default NULL,
  `entry_uid` bigint(20) default NULL,
  `wiki_edits` int(11) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKED5D7A1FD8004954` (`wiki_session_uid`),
  CONSTRAINT `FKED5D7A1FD8004954` FOREIGN KEY (`wiki_session_uid`) REFERENCES `tl_lawiki10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_user`
--

LOCK TABLES `tl_lawiki10_user` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lawiki10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki`;
CREATE TABLE `tl_lawiki10_wiki` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `allow_learner_create_pages` bit(1) default NULL,
  `allow_learner_insert_links` bit(1) default NULL,
  `allow_learner_attach_images` bit(1) default NULL,
  `notify_updates` bit(1) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  `minimum_edits` int(11) default NULL,
  `maximum_edits` int(11) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `wiki_main_page_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKED5E3E04A3FF7EC0` (`wiki_main_page_uid`),
  CONSTRAINT `FKED5E3E04A3FF7EC0` FOREIGN KEY (`wiki_main_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_wiki`
--

LOCK TABLES `tl_lawiki10_wiki` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki` VALUES (1,NULL,NULL,NULL,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,15,1),(2,'2008-11-14 14:32:00','2008-11-10 12:53:27',5,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,53,2),(3,'2008-12-03 14:45:35','2008-11-10 12:53:27',5,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,76,5);
/*!40000 ALTER TABLE `tl_lawiki10_wiki` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page`;
CREATE TABLE `tl_lawiki10_wiki_page` (
  `uid` bigint(20) NOT NULL auto_increment,
  `wiki_uid` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `editable` bit(1) default NULL,
  `wiki_current_content` bigint(20) default NULL,
  `added_by` bigint(20) default NULL,
  `wiki_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `title` (`title`,`wiki_session_uid`),
  KEY `FK961AFFEAD8004954` (`wiki_session_uid`),
  KEY `FK961AFFEA60B3B03B` (`wiki_uid`),
  KEY `FK961AFFEA36CBA7DB` (`added_by`),
  KEY `FK961AFFEAE48332B4` (`wiki_current_content`),
  CONSTRAINT `FK961AFFEAE48332B4` FOREIGN KEY (`wiki_current_content`) REFERENCES `tl_lawiki10_wiki_page_content` (`uid`),
  CONSTRAINT `FK961AFFEA36CBA7DB` FOREIGN KEY (`added_by`) REFERENCES `tl_lawiki10_user` (`uid`),
  CONSTRAINT `FK961AFFEA60B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`),
  CONSTRAINT `FK961AFFEAD8004954` FOREIGN KEY (`wiki_session_uid`) REFERENCES `tl_lawiki10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_wiki_page`
--

LOCK TABLES `tl_lawiki10_wiki_page` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page` VALUES (1,1,'Wiki Main Page',0x01,1,NULL,NULL),(2,2,'Wiki Main Page',0x00,2,NULL,NULL),(3,2,'Important Things to Know',0x01,3,NULL,NULL),(4,3,'Important Things to Know',0x01,4,NULL,NULL),(5,3,'Wiki Main Page',0x00,5,NULL,NULL),(6,3,'Important Things to Know',0x01,6,NULL,1),(7,3,'Wiki Main Page',0x00,7,NULL,1);
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page_content`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page_content`;
CREATE TABLE `tl_lawiki10_wiki_page_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `wiki_page_uid` bigint(20) default NULL,
  `body` text,
  `editor` bigint(20) default NULL,
  `edit_date` datetime default NULL,
  `version` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK528051242D44CCF8` (`wiki_page_uid`),
  KEY `FK528051243233D952` (`editor`),
  CONSTRAINT `FK528051243233D952` FOREIGN KEY (`editor`) REFERENCES `tl_lawiki10_user` (`uid`),
  CONSTRAINT `FK528051242D44CCF8` FOREIGN KEY (`wiki_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tl_lawiki10_wiki_page_content`
--

LOCK TABLES `tl_lawiki10_wiki_page_content` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page_content` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page_content` VALUES (1,1,'<div>Wiki Body</div>',NULL,NULL,0),(2,2,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2008-12-03 14:44:58',3),(3,3,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2008-12-03 14:44:58',1),(4,4,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2008-12-03 14:45:35',1),(5,5,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2008-12-03 14:45:35',3),(6,6,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2008-12-03 14:45:36',1),(7,7,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2008-12-03 14:45:36',3);
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page_content` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2008-12-03  3:46:02
