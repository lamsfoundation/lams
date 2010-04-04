-- MySQL dump 10.13  Distrib 5.1.43, for apple-darwin9.5.0 (i386)
--
-- Host: localhost    Database: lams2
-- ------------------------------------------------------
-- Server version	5.1.43

USE lams2;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_activity_category` (
  `activity_category_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`activity_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_activity_category`
--

LOCK TABLES `lams_activity_category` WRITE;
/*!40000 ALTER TABLE `lams_activity_category` DISABLE KEYS */;
INSERT INTO `lams_activity_category` VALUES (1,'SYSTEM'),(2,'COLLABORATION'),(3,'ASSESSMENT'),(4,'CONTENT'),(5,'SPLIT'),(6,'RESPONSE');
/*!40000 ALTER TABLE `lams_activity_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_activity_evaluation`
--

DROP TABLE IF EXISTS `lams_activity_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_activity_evaluation` (
  `activity_evaluation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `tool_output_definition` varchar(255) NOT NULL,
  PRIMARY KEY (`activity_evaluation_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_activity_evaluation_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_activity_evaluation`
--

LOCK TABLES `lams_activity_evaluation` WRITE;
/*!40000 ALTER TABLE `lams_activity_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_activity_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_activity_learners`
--

DROP TABLE IF EXISTS `lams_activity_learners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_activity_learners` (
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `activity_id` bigint(20) NOT NULL DEFAULT '0',
  `allowed_to_pass` tinyint(4) NOT NULL DEFAULT '0',
  KEY `user_id` (`user_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_TABLE_32_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_TABLE_32_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_auth_method_type` (
  `authentication_method_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY (`authentication_method_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_authentication_method` (
  `authentication_method_id` bigint(20) NOT NULL,
  `authentication_method_type_id` int(3) NOT NULL DEFAULT '0',
  `authentication_method_name` varchar(255) NOT NULL,
  PRIMARY KEY (`authentication_method_id`),
  UNIQUE KEY `UQ_lams_authentication_method_1` (`authentication_method_name`),
  KEY `authentication_method_type_id` (`authentication_method_type_id`),
  CONSTRAINT `FK_lams_authorization_method_1` FOREIGN KEY (`authentication_method_type_id`) REFERENCES `lams_auth_method_type` (`authentication_method_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_branch_activity_entry` (
  `entry_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `entry_ui_id` int(11) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `sequence_activity_id` bigint(20) DEFAULT NULL,
  `branch_activity_id` bigint(20) NOT NULL,
  `condition_id` bigint(20) DEFAULT NULL,
  `open_gate` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`entry_id`),
  KEY `group_id` (`group_id`),
  KEY `sequence_activity_id` (`sequence_activity_id`),
  KEY `branch_activity_id` (`branch_activity_id`),
  KEY `condition_id` (`condition_id`),
  CONSTRAINT `FK_lams_group_activity_1` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`),
  CONSTRAINT `FK_lams_branch_map_sequence` FOREIGN KEY (`sequence_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_branch_map_branch` FOREIGN KEY (`branch_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`),
  CONSTRAINT `FK_lams_branch_activity_entry_4` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_branch_condition` (
  `condition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `condition_ui_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `type` varchar(30) NOT NULL,
  `start_value` varchar(255) DEFAULT NULL,
  `end_value` varchar(255) DEFAULT NULL,
  `exact_match_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`condition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_competence` (
  `competence_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_id` bigint(20) DEFAULT NULL,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`competence_id`),
  UNIQUE KEY `competence_id` (`competence_id`),
  UNIQUE KEY `learning_design_id` (`learning_design_id`,`title`),
  CONSTRAINT `LearningDesignCompetenceMap` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_competence_mapping` (
  `competence_mapping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `competence_id` bigint(20) DEFAULT NULL,
  `activity_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`competence_mapping_id`),
  UNIQUE KEY `competence_mapping_id` (`competence_mapping_id`),
  UNIQUE KEY `competence_id` (`competence_id`,`activity_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_competence_mapping_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_competence_mapping_2` FOREIGN KEY (`competence_id`) REFERENCES `lams_competence` (`competence_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_configuration` (
  `config_key` varchar(30) NOT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  `description_key` varchar(255) DEFAULT NULL,
  `header_name` varchar(50) DEFAULT NULL,
  `format` varchar(30) DEFAULT NULL,
  `required` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_configuration`
--

LOCK TABLES `lams_configuration` WRITE;
/*!40000 ALTER TABLE `lams_configuration` DISABLE KEYS */;
INSERT INTO `lams_configuration` VALUES ('AdminScreenSize','1024x768','config.admin.screen.size','config.header.look.feel','STRING',1),('AllowDirectLessonLaunch','false','config.allow.direct.lesson.launch','config.header.features','BOOLEAN',1),('AllowLiveEdit','true','config.allow.live.edit','config.header.features','BOOLEAN',1),('AuthoringActivitiesColour','true','config.authoring.activities.colour','config.header.look.feel','BOOLEAN',1),('AuthoringClientVersion','2.3.4.201004011554','config.authoring.client.version','config.header.versions','STRING',1),('AuthoringScreenSize','1024x768','config.authoring.screen.size','config.header.look.feel','STRING',1),('CleanupPreviewOlderThanDays','7','config.cleanup.preview.older.than.days','config.header.system','LONG',1),('ContentRepositoryPath','/usr/local/lams/repository','config.content.repository.path','config.header.uploads','STRING',1),('CustomTabLink','','config.custom.tab.link','config.header.look.feel','STRING',0),('CustomTabTitle','','config.custom.tab.title','config.header.look.feel','STRING',0),('DefaultFlashTheme','default','config.default.flash.theme','config.header.look.feel','STRING',1),('DefaultHTMLTheme','defaultHTML','config.default.html.theme','config.header.look.feel','STRING',1),('DictionaryDateCreated','2010-02-10','config.dictionary.date.created','config.header.versions','STRING',1),('DumpDir','/usr/local/lams/dump','config.dump.dir','config.header.system','STRING',1),('EARDir','/usr/local/lams/jboss-4.0.2/server/default/deploy/lams.ear/','config.ear.dir','config.header.system','STRING',1),('EnableFlash','true','config.flash.enable','config.header.features','BOOLEAN',1),('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs','config.executable.extensions','config.header.uploads','STRING',1),('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/','config.help.url','config.header.system','STRING',1),('LamsSupportEmail','','config.lams.support.email','config.header.email','STRING',0),('LAMS_Community_enable','false','config.community.enable','config.header.features','BOOLEAN',1),('LDAPAddr1Attr','postalAddress','admin.user.address_line_1','config.header.ldap.attributes','STRING',0),('LDAPAddr2Attr','','admin.user.address_line_2','config.header.ldap.attributes','STRING',0),('LDAPAddr3Attr','','admin.user.address_line_3','config.header.ldap.attributes','STRING',0),('LDAPAuthorMap','Teacher;SeniorStaff;Principal','config.ldap.author.map','config.header.ldap.attributes','STRING',0),('LDAPBaseDN','ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au','config.ldap.base.dn','config.header.ldap','STRING',0),('LDAPBindUserDN','','config.ldap.bind.user.dn','config.header.ldap','STRING',0),('LDAPBindUserPassword','','config.ldap.bind.user.password','config.header.ldap','STRING',0),('LDAPCityAttr','l','admin.user.city','config.header.ldap.attributes','STRING',0),('LDAPCountryAttr','','admin.user.country','config.header.ldap.attributes','STRING',0),('LDAPDayPhoneAttr','telephoneNumber','admin.user.day_phone','config.header.ldap.attributes','STRING',0),('LDAPDisabledAttr','!accountStatus','sysadmin.disabled','config.header.ldap.attributes','STRING',0),('LDAPEmailAttr','mail','admin.user.email','config.header.ldap.attributes','STRING',0),('LDAPEncryptPasswordFromBrowser','true','config.ldap.encrypt.password.from.browser','config.header.ldap','BOOLEAN',1),('LDAPEveningPhoneAttr','homePhone','admin.user.evening_phone','config.header.ldap.attributes','STRING',0),('LDAPFaxAttr','facsimileTelephoneNumber','admin.user.fax','config.header.ldap.attributes','STRING',0),('LDAPFNameAttr','givenName','admin.user.first_name','config.header.ldap.attributes','STRING',0),('LDAPGroupAdminMap','Teacher;SeniorStaff','config.ldap.group.admin.map','config.header.ldap.attributes','STRING',0),('LDAPGroupManagerMap','Principal','config.ldap.group.manager.map','config.header.ldap.attributes','STRING',0),('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.learner.map','config.header.ldap.attributes','STRING',0),('LDAPLNameAttr','sn','admin.user.last_name','config.header.ldap.attributes','STRING',0),('LDAPLocaleAttr','preferredLanguage','admin.organisation.locale','config.header.ldap.attributes','STRING',0),('LDAPLoginAttr','uid','admin.user.login','config.header.ldap.attributes','STRING',0),('LDAPMobileAttr','mobile','admin.user.mobile_phone','config.header.ldap.attributes','STRING',0),('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.monitor.map','config.header.ldap.attributes','STRING',0),('LDAPOnlyOneOrg','true','config.ldap.only.one.org','config.header.ldap','BOOLEAN',1),('LDAPOrgAttr','schoolCode','admin.course','config.header.ldap.attributes','STRING',0),('LDAPOrgField','code','config.ldap.org.field','config.header.ldap.attributes','STRING',0),('LDAPPostcodeAttr','postalCode','admin.user.postcode','config.header.ldap.attributes','STRING',0),('LDAPProviderURL','ldap://192.168.111.15','config.ldap.provider.url','config.header.ldap','STRING',0),('LDAPProvisioningEnabled','false','config.ldap.provisioning.enabled','config.header.ldap','BOOLEAN',1),('LDAPRolesAttr','memberOf','admin.user.roles','config.header.ldap.attributes','STRING',0),('LDAPSearchFilter','(cn={0})','config.ldap.search.filter','config.header.ldap','STRING',0),('LDAPSearchResultsPageSize','100','config.ldap.search.results.page.size','config.header.ldap','LONG',0),('LDAPSecurityAuthentication','simple','config.ldap.security.authentication','config.header.ldap','STRING',0),('LDAPSecurityProtocol','','config.ldap.security.protocol','config.header.ldap','STRING',0),('LDAPStateAttr','st','admin.user.state','config.header.ldap.attributes','STRING',0),('LDAPUpdateOnLogin','true','config.ldap.update.on.login','config.header.ldap','BOOLEAN',1),('LearnerClientVersion','2.3.4.201004011554','config.learner.client.version','config.header.versions','STRING',1),('LearnerProgressBatchSize','10','config.learner.progress.batch.size','config.header.look.feel','LONG',1),('LearnerScreenSize','1024x768','config.learner.screen.size','config.header.look.feel','STRING',1),('MonitorClientVersion','2.3.4.201004011554','config.monitor.client.version','config.header.versions','STRING',1),('MonitorScreenSize','1024x768','config.monitor.screen.size','config.header.look.feel','STRING',1),('Red5RecordingsUrl','','config.red5.recordings.url','config.header.red5','STRING',0),('Red5ServerUrl','','config.red5.server.url','config.header.red5','STRING',0),('ServerLanguage','en_AU','config.server.language','config.header.look.feel','STRING',1),('ServerPageDirection','LTR','config.server.page.direction','config.header.look.feel','STRING',1),('ServerURL','http://localhost:8080/lams/','config.server.url','config.header.system','STRING',1),('ServerURLContextPath','lams/','config.server.url.context.path','config.header.system','STRING',1),('ServerVersionNumber','2.3.4.201004011554','config.server.version.number','config.header.versions','STRING',1),('SMTPPassword','','config.smtp.password','config.header.email','STRING',0),('SMTPServer','','config.smtp.server','config.header.email','STRING',0),('SMTPUser','','config.smtp.user','config.header.email','STRING',0),('TempDir','/usr/local/lams/temp','config.temp.dir','config.header.system','STRING',1),('TruststorePassword','','config.ldap.truststore.password','config.header.system','STRING',0),('TruststorePath','','config.ldap.truststore.path','config.header.system','STRING',0),('UploadFileMaxMemorySize','4096','config.upload.file.max.memory.size','config.header.uploads','LONG',1),('UploadFileMaxSize','1048576','config.upload.file.max.size','config.header.uploads','LONG',1),('UploadLargeFileMaxSize','10485760','config.upload.large.file.max.size','config.header.uploads','LONG',1),('UseCacheDebugListener','false','config.use.cache.debug.listener','config.header.system','BOOLEAN',1),('UserInactiveTimeout','86400','config.user.inactive.timeout','config.header.system','LONG',1),('Version','2.3.4','config.version','config.header.system','STRING',1),('XmppAdmin','admin','config.xmpp.admin','config.header.chat','STRING',0),('XmppConference','conference.localhost','config.xmpp.conference','config.header.chat','STRING',0),('XmppDomain','localhost','config.xmpp.domain','config.header.chat','STRING',0),('XmppPassword','wildfire','config.xmpp.password','config.header.chat','STRING',0);
/*!40000 ALTER TABLE `lams_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_copy_type`
--

DROP TABLE IF EXISTS `lams_copy_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_copy_type` (
  `copy_type_id` tinyint(4) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`copy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_credential` (
  `credential_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`credential_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Records the identification properties for a tool.';
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_node` (
  `node_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `path` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `created_date_time` datetime NOT NULL,
  `next_version_id` bigint(20) unsigned NOT NULL DEFAULT '1',
  `parent_nv_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `workspace_id` (`workspace_id`),
  CONSTRAINT `FK_lams_cr_node_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='The main table containing the node definition';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_cr_node`
--

LOCK TABLES `lams_cr_node` WRITE;
/*!40000 ALTER TABLE `lams_cr_node` DISABLE KEYS */;
INSERT INTO `lams_cr_node` VALUES (1,1,NULL,'PACKAGENODE','2010-04-01 16:03:09',2,NULL),(2,1,'content/0803.jpg','FILENODE','2010-04-01 16:03:09',2,1),(3,1,'content/0809.jpg','FILENODE','2010-04-01 16:03:09',2,1),(4,1,'content/0810.jpg','FILENODE','2010-04-01 16:03:09',2,1),(5,1,'content/0812.jpg','FILENODE','2010-04-01 16:03:09',2,1),(6,1,'content/apple3.swf','FILENODE','2010-04-01 16:03:09',2,1),(7,1,'content/apple4.swf','FILENODE','2010-04-01 16:03:09',2,1),(8,1,'content/apple_green.swf','FILENODE','2010-04-01 16:03:09',2,1),(9,1,'content/apple_green2.swf','FILENODE','2010-04-01 16:03:09',2,1),(10,1,'content/ASSET_24052005213047.swf','FILENODE','2010-04-01 16:03:09',2,1),(11,1,'content/beans.swf','FILENODE','2010-04-01 16:03:09',2,1),(12,1,'content/beer_can.swf','FILENODE','2010-04-01 16:03:09',2,1),(13,1,'content/brain.swf','FILENODE','2010-04-01 16:03:09',2,1),(14,1,'content/broccoli.swf','FILENODE','2010-04-01 16:03:09',2,1),(15,1,'content/butter_in_dish.swf','FILENODE','2010-04-01 16:03:09',2,1),(16,1,'content/carrott.swf','FILENODE','2010-04-01 16:03:09',2,1),(17,1,'content/confused boy.jpg','FILENODE','2010-04-01 16:03:09',2,1),(18,1,'content/corn.swf','FILENODE','2010-04-01 16:03:09',2,1),(19,1,'content/fish.swf','FILENODE','2010-04-01 16:03:09',2,1),(20,1,'content/flour.swf','FILENODE','2010-04-01 16:03:09',2,1),(21,1,'content/ham_joint.swf','FILENODE','2010-04-01 16:03:09',2,1),(22,1,'content/man_reading_papers.swf','FILENODE','2010-04-01 16:03:09',2,1),(23,1,'content/milk_carton.swf','FILENODE','2010-04-01 16:03:09',2,1),(24,1,'content/milk_carton2.swf','FILENODE','2010-04-01 16:03:09',2,1),(25,1,'content/porridge_bowl.swf','FILENODE','2010-04-01 16:03:09',2,1),(26,1,'content/potato.swf','FILENODE','2010-04-01 16:03:09',2,1),(27,1,'content/potato2.swf','FILENODE','2010-04-01 16:03:09',2,1),(28,1,'content/rice_packet.swf','FILENODE','2010-04-01 16:03:09',2,1),(29,1,'embedfunction.js','FILENODE','2010-04-01 16:03:09',2,1),(30,1,'gateway.swf','FILENODE','2010-04-01 16:03:09',2,1),(31,1,'imsmanifest.xml','FILENODE','2010-04-01 16:03:09',2,1),(32,1,'launch.html','FILENODE','2010-04-01 16:03:09',2,1),(33,1,'loinfo.xml','FILENODE','2010-04-01 16:03:09',2,1),(34,1,'main.swf','FILENODE','2010-04-01 16:03:09',2,1),(35,1,'MLAB_flash_setvariables.as','FILENODE','2010-04-01 16:03:09',2,1),(36,1,'sclo.xml','FILENODE','2010-04-01 16:03:09',2,1),(37,1,'window-closer.html','FILENODE','2010-04-01 16:03:09',2,1),(38,1,NULL,'FILENODE','2010-04-01 16:03:10',2,NULL),(39,1,NULL,'FILENODE','2010-04-01 16:03:10',2,NULL),(40,1,NULL,'PACKAGENODE','2010-04-01 16:03:16',2,NULL),(41,1,'blueline.gif','FILENODE','2010-04-01 16:03:16',2,40),(42,1,'c5_noticeboard.swf','FILENODE','2010-04-01 16:03:16',2,40),(43,1,'index.htm','FILENODE','2010-04-01 16:03:16',2,40),(44,1,NULL,'PACKAGENODE','2010-04-01 16:03:16',2,NULL),(45,1,'blueline.gif','FILENODE','2010-04-01 16:03:16',2,44),(46,1,'c8_shareResources.swf','FILENODE','2010-04-01 16:03:16',2,44),(47,1,'index.htm','FILENODE','2010-04-01 16:03:16',2,44),(48,1,NULL,'PACKAGENODE','2010-04-01 16:03:24',2,NULL),(49,1,'blueline.gif','FILENODE','2010-04-01 16:03:24',2,48),(50,1,'index.htm','FILENODE','2010-04-01 16:03:24',2,48),(51,1,'introToMonitor.swf','FILENODE','2010-04-01 16:03:24',2,48),(52,1,NULL,'PACKAGENODE','2010-04-01 16:03:24',2,NULL),(53,1,'blueline.gif','FILENODE','2010-04-01 16:03:24',2,52),(54,1,'index.htm','FILENODE','2010-04-01 16:03:24',2,52),(55,1,'introToAuthoring.swf','FILENODE','2010-04-01 16:03:24',2,52),(56,1,NULL,'PACKAGENODE','2010-04-01 16:03:24',2,NULL),(57,1,'blueline.gif','FILENODE','2010-04-01 16:03:24',2,56),(58,1,'index.htm','FILENODE','2010-04-01 16:03:24',2,56),(59,1,'learnerExperience.swf','FILENODE','2010-04-01 16:03:24',2,56),(60,1,NULL,'PACKAGENODE','2010-04-01 16:03:24',2,NULL),(61,1,'blueline.gif','FILENODE','2010-04-01 16:03:24',2,60),(62,1,'index.htm','FILENODE','2010-04-01 16:03:24',2,60),(63,1,'noflash.swf','FILENODE','2010-04-01 16:03:24',2,60),(64,1,NULL,'PACKAGENODE','2010-04-01 16:03:32',2,NULL),(65,1,'blueline.gif','FILENODE','2010-04-01 16:03:32',2,64),(66,1,'effective_authoring.swf','FILENODE','2010-04-01 16:03:32',2,64),(67,1,'index.htm','FILENODE','2010-04-01 16:03:32',2,64),(68,1,NULL,'PACKAGENODE','2010-04-01 16:03:33',2,NULL),(69,1,'blueline.gif','FILENODE','2010-04-01 16:03:33',2,68),(70,1,'index.htm','FILENODE','2010-04-01 16:03:33',2,68),(71,1,'whatislams.swf','FILENODE','2010-04-01 16:03:33',2,68);
/*!40000 ALTER TABLE `lams_cr_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version`
--

DROP TABLE IF EXISTS `lams_cr_node_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_node_version` (
  `nv_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `node_id` bigint(20) unsigned NOT NULL,
  `version_id` bigint(20) unsigned NOT NULL,
  `created_date_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`nv_id`),
  KEY `node_id` (`node_id`),
  CONSTRAINT `FK_lams_cr_node_version_2` FOREIGN KEY (`node_id`) REFERENCES `lams_cr_node` (`node_id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='Represents a version of a node';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_cr_node_version`
--

LOCK TABLES `lams_cr_node_version` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version` DISABLE KEYS */;
INSERT INTO `lams_cr_node_version` VALUES (1,1,1,'2010-04-01 16:03:09',5),(2,2,1,'2010-04-01 16:03:09',5),(3,3,1,'2010-04-01 16:03:09',5),(4,4,1,'2010-04-01 16:03:09',5),(5,5,1,'2010-04-01 16:03:09',5),(6,6,1,'2010-04-01 16:03:09',5),(7,7,1,'2010-04-01 16:03:09',5),(8,8,1,'2010-04-01 16:03:09',5),(9,9,1,'2010-04-01 16:03:09',5),(10,10,1,'2010-04-01 16:03:09',5),(11,11,1,'2010-04-01 16:03:09',5),(12,12,1,'2010-04-01 16:03:09',5),(13,13,1,'2010-04-01 16:03:09',5),(14,14,1,'2010-04-01 16:03:09',5),(15,15,1,'2010-04-01 16:03:09',5),(16,16,1,'2010-04-01 16:03:09',5),(17,17,1,'2010-04-01 16:03:09',5),(18,18,1,'2010-04-01 16:03:09',5),(19,19,1,'2010-04-01 16:03:09',5),(20,20,1,'2010-04-01 16:03:09',5),(21,21,1,'2010-04-01 16:03:09',5),(22,22,1,'2010-04-01 16:03:09',5),(23,23,1,'2010-04-01 16:03:09',5),(24,24,1,'2010-04-01 16:03:09',5),(25,25,1,'2010-04-01 16:03:09',5),(26,26,1,'2010-04-01 16:03:09',5),(27,27,1,'2010-04-01 16:03:09',5),(28,28,1,'2010-04-01 16:03:09',5),(29,29,1,'2010-04-01 16:03:09',5),(30,30,1,'2010-04-01 16:03:09',5),(31,31,1,'2010-04-01 16:03:09',5),(32,32,1,'2010-04-01 16:03:09',5),(33,33,1,'2010-04-01 16:03:09',5),(34,34,1,'2010-04-01 16:03:09',5),(35,35,1,'2010-04-01 16:03:09',5),(36,36,1,'2010-04-01 16:03:09',5),(37,37,1,'2010-04-01 16:03:09',5),(38,38,1,'2010-04-01 16:03:10',5),(39,39,1,'2010-04-01 16:03:10',5),(40,40,1,'2010-04-01 16:03:16',5),(41,41,1,'2010-04-01 16:03:16',5),(42,42,1,'2010-04-01 16:03:16',5),(43,43,1,'2010-04-01 16:03:16',5),(44,44,1,'2010-04-01 16:03:16',5),(45,45,1,'2010-04-01 16:03:16',5),(46,46,1,'2010-04-01 16:03:16',5),(47,47,1,'2010-04-01 16:03:16',5),(48,48,1,'2010-04-01 16:03:24',5),(49,49,1,'2010-04-01 16:03:24',5),(50,50,1,'2010-04-01 16:03:24',5),(51,51,1,'2010-04-01 16:03:24',5),(52,52,1,'2010-04-01 16:03:24',5),(53,53,1,'2010-04-01 16:03:24',5),(54,54,1,'2010-04-01 16:03:24',5),(55,55,1,'2010-04-01 16:03:24',5),(56,56,1,'2010-04-01 16:03:24',5),(57,57,1,'2010-04-01 16:03:24',5),(58,58,1,'2010-04-01 16:03:24',5),(59,59,1,'2010-04-01 16:03:24',5),(60,60,1,'2010-04-01 16:03:24',5),(61,61,1,'2010-04-01 16:03:24',5),(62,62,1,'2010-04-01 16:03:24',5),(63,63,1,'2010-04-01 16:03:24',5),(64,64,1,'2010-04-01 16:03:32',5),(65,65,1,'2010-04-01 16:03:32',5),(66,66,1,'2010-04-01 16:03:32',5),(67,67,1,'2010-04-01 16:03:32',5),(68,68,1,'2010-04-01 16:03:33',5),(69,69,1,'2010-04-01 16:03:33',5),(70,70,1,'2010-04-01 16:03:33',5),(71,71,1,'2010-04-01 16:03:33',5);
/*!40000 ALTER TABLE `lams_cr_node_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version_property`
--

DROP TABLE IF EXISTS `lams_cr_node_version_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_node_version_property` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `nv_id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `nv_id` (`nv_id`),
  CONSTRAINT `FK_lams_cr_node_version_property_1` FOREIGN KEY (`nv_id`) REFERENCES `lams_cr_node_version` (`nv_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_cr_node_version_property`
--

LOCK TABLES `lams_cr_node_version_property` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version_property` DISABLE KEYS */;
INSERT INTO `lams_cr_node_version_property` VALUES (1,1,'INITIALPATH','launch.html',1),(2,2,'FILENAME','0803.jpg',1),(3,3,'FILENAME','0809.jpg',1),(4,4,'FILENAME','0810.jpg',1),(5,5,'FILENAME','0812.jpg',1),(6,6,'FILENAME','apple3.swf',1),(7,7,'FILENAME','apple4.swf',1),(8,8,'FILENAME','apple_green.swf',1),(9,9,'FILENAME','apple_green2.swf',1),(10,10,'FILENAME','ASSET_24052005213047.swf',1),(11,11,'FILENAME','beans.swf',1),(12,12,'FILENAME','beer_can.swf',1),(13,13,'FILENAME','brain.swf',1),(14,14,'FILENAME','broccoli.swf',1),(15,15,'FILENAME','butter_in_dish.swf',1),(16,16,'FILENAME','carrott.swf',1),(17,17,'FILENAME','confused boy.jpg',1),(18,18,'FILENAME','corn.swf',1),(19,19,'FILENAME','fish.swf',1),(20,20,'FILENAME','flour.swf',1),(21,21,'FILENAME','ham_joint.swf',1),(22,22,'FILENAME','man_reading_papers.swf',1),(23,23,'FILENAME','milk_carton.swf',1),(24,24,'FILENAME','milk_carton2.swf',1),(25,25,'FILENAME','porridge_bowl.swf',1),(26,26,'FILENAME','potato.swf',1),(27,27,'FILENAME','potato2.swf',1),(28,28,'FILENAME','rice_packet.swf',1),(29,29,'FILENAME','embedfunction.js',1),(30,30,'FILENAME','gateway.swf',1),(31,31,'FILENAME','imsmanifest.xml',1),(32,32,'FILENAME','launch.html',1),(33,33,'FILENAME','loinfo.xml',1),(34,34,'FILENAME','main.swf',1),(35,35,'FILENAME','MLAB_flash_setvariables.as',1),(36,36,'FILENAME','sclo.xml',1),(37,37,'FILENAME','window-closer.html',1),(38,38,'FILENAME','matchingpairs.swf',1),(39,38,'TYPE','application/x-shockwave-flash',1),(40,39,'FILENAME','face2.swf',1),(41,39,'TYPE','application/x-shockwave-flash',1),(42,40,'INITIALPATH','index.htm',1),(43,41,'FILENAME','blueline.gif',1),(44,42,'FILENAME','c5_noticeboard.swf',1),(45,43,'FILENAME','index.htm',1),(46,44,'INITIALPATH','index.htm',1),(47,45,'FILENAME','blueline.gif',1),(48,46,'FILENAME','c8_shareResources.swf',1),(49,47,'FILENAME','index.htm',1),(50,48,'INITIALPATH','index.htm',1),(51,49,'FILENAME','blueline.gif',1),(52,50,'FILENAME','index.htm',1),(53,51,'FILENAME','introToMonitor.swf',1),(54,52,'INITIALPATH','index.htm',1),(55,53,'FILENAME','blueline.gif',1),(56,54,'FILENAME','index.htm',1),(57,55,'FILENAME','introToAuthoring.swf',1),(58,56,'INITIALPATH','index.htm',1),(59,57,'FILENAME','blueline.gif',1),(60,58,'FILENAME','index.htm',1),(61,59,'FILENAME','learnerExperience.swf',1),(62,60,'INITIALPATH','index.htm',1),(63,61,'FILENAME','blueline.gif',1),(64,62,'FILENAME','index.htm',1),(65,63,'FILENAME','noflash.swf',1),(66,64,'INITIALPATH','index.htm',1),(67,65,'FILENAME','blueline.gif',1),(68,66,'FILENAME','effective_authoring.swf',1),(69,67,'FILENAME','index.htm',1),(70,68,'INITIALPATH','index.htm',1),(71,69,'FILENAME','blueline.gif',1),(72,70,'FILENAME','index.htm',1),(73,71,'FILENAME','whatislams.swf',1);
/*!40000 ALTER TABLE `lams_cr_node_version_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_workspace`
--

DROP TABLE IF EXISTS `lams_cr_workspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_workspace` (
  `workspace_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`workspace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Content repository workspace';
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_cr_workspace_credential` (
  `wc_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `credential_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`wc_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `credential_id` (`credential_id`),
  CONSTRAINT `FK_lams_cr_workspace_credential_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_cr_workspace_credential_2` FOREIGN KEY (`credential_id`) REFERENCES `lams_cr_credential` (`credential_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Maps tools access to workspaces';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_cr_workspace_credential`
--

LOCK TABLES `lams_cr_workspace_credential` WRITE;
/*!40000 ALTER TABLE `lams_cr_workspace_credential` DISABLE KEYS */;
INSERT INTO `lams_cr_workspace_credential` VALUES (1,1,1);
/*!40000 ALTER TABLE `lams_cr_workspace_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_event_subscriptions`
--

DROP TABLE IF EXISTS `lams_event_subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_event_subscriptions` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `event_uid` bigint(20) DEFAULT NULL,
  `delivery_method_id` tinyint(3) unsigned DEFAULT NULL,
  `periodicity` bigint(20) DEFAULT NULL,
  `last_operation_time` datetime DEFAULT NULL,
  `last_operation_message` text,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `EventSubscriptionsToUsers` (`user_id`),
  KEY `event_uid` (`event_uid`),
  CONSTRAINT `EventSubscriptionsToUsers` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `EventSubscriptionsToEvent` FOREIGN KEY (`event_uid`) REFERENCES `lams_events` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_events` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `scope` varchar(128) NOT NULL,
  `name` varchar(128) NOT NULL,
  `event_session_id` bigint(20) DEFAULT NULL,
  `triggered` tinyint(4) DEFAULT NULL,
  `default_subject` varchar(255) DEFAULT NULL,
  `default_message` text,
  `subject` varchar(255) DEFAULT NULL,
  `message` text,
  `fail_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `scope` (`scope`,`name`,`event_session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_ext_course_class_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `courseid` varchar(255) NOT NULL,
  `classid` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY (`sid`),
  KEY `classid` (`classid`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_course_class_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_course_class_map_fk` FOREIGN KEY (`classid`) REFERENCES `lams_organisation` (`organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_ext_server_org_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `serverid` varchar(255) NOT NULL,
  `serverkey` text NOT NULL,
  `servername` varchar(255) NOT NULL,
  `serverdesc` text,
  `prefix` varchar(11) NOT NULL,
  `userinfo_url` text NOT NULL,
  `server_url` varchar(255) DEFAULT NULL,
  `timeout_url` text NOT NULL,
  `disabled` bit(1) NOT NULL,
  `orgid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `serverid` (`serverid`),
  UNIQUE KEY `prefix` (`prefix`),
  KEY `orgid` (`orgid`),
  CONSTRAINT `lams_ext_server_org_map_fk` FOREIGN KEY (`orgid`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_ext_server_org_map`
--

LOCK TABLES `lams_ext_server_org_map` WRITE;
/*!40000 ALTER TABLE `lams_ext_server_org_map` DISABLE KEYS */;
INSERT INTO `lams_ext_server_org_map` VALUES (1,'moodle','moodle','moodle','moodle','mdl','http://localhost/moodle/mod/lamstwo/userinfo.php?ts=%timestamp%&un=%username%&hs=%hash%','','http://dummy',0x00,7);
/*!40000 ALTER TABLE `lams_ext_server_org_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_ext_server_tool_map`
--

DROP TABLE IF EXISTS `lams_ext_server_tool_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_ext_server_tool_map` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `unique_adapter_map` (`ext_server_org_map_id`,`tool_id`),
  KEY `lams_ext_server_tool_map_fk2` (`tool_id`),
  CONSTRAINT `lams_ext_server_tool_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_server_tool_map_fk2` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_ext_server_tool_map`
--

LOCK TABLES `lams_ext_server_tool_map` WRITE;
/*!40000 ALTER TABLE `lams_ext_server_tool_map` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_ext_server_tool_map` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_ext_user_userid_map`
--

DROP TABLE IF EXISTS `lams_ext_user_userid_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_ext_user_userid_map` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `external_username` varchar(250) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `ext_server_org_map_id` int(11) NOT NULL,
  PRIMARY KEY (`sid`),
  KEY `user_id` (`user_id`),
  KEY `ext_server_org_map_id` (`ext_server_org_map_id`),
  CONSTRAINT `lams_ext_user_userid_map_fk1` FOREIGN KEY (`ext_server_org_map_id`) REFERENCES `lams_ext_server_org_map` (`sid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `lams_ext_user_userid_map_fk` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_gate_activity_level` (
  `gate_activity_level_id` int(11) NOT NULL DEFAULT '0',
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`gate_activity_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_gate_activity_level`
--

LOCK TABLES `lams_gate_activity_level` WRITE;
/*!40000 ALTER TABLE `lams_gate_activity_level` DISABLE KEYS */;
INSERT INTO `lams_gate_activity_level` VALUES (1,'LEARNER'),(2,'GROUP'),(3,'CLASS');
/*!40000 ALTER TABLE `lams_gate_activity_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_gradebook_user_activity`
--

DROP TABLE IF EXISTS `lams_gradebook_user_activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_gradebook_user_activity` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` text,
  `marked_in_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `activity_id` (`activity_id`,`user_id`),
  KEY `FK_lams_gradebook_user_activity_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_gradebook_user_activity_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_gradebook_user_activity`
--

LOCK TABLES `lams_gradebook_user_activity` WRITE;
/*!40000 ALTER TABLE `lams_gradebook_user_activity` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_gradebook_user_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_gradebook_user_lesson`
--

DROP TABLE IF EXISTS `lams_gradebook_user_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_gradebook_user_lesson` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double DEFAULT NULL,
  `feedback` text,
  PRIMARY KEY (`uid`),
  KEY `lesson_id` (`lesson_id`,`user_id`),
  KEY `FK_lams_gradebook_user_lesson_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_gradebook_user_lesson`
--

LOCK TABLES `lams_gradebook_user_lesson` WRITE;
/*!40000 ALTER TABLE `lams_gradebook_user_lesson` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_gradebook_user_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_group`
--

DROP TABLE IF EXISTS `lams_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_group` (
  `group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) NOT NULL,
  `grouping_id` bigint(20) NOT NULL,
  `order_id` int(6) NOT NULL DEFAULT '1',
  `group_ui_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`group_id`),
  KEY `grouping_id` (`grouping_id`),
  CONSTRAINT `FK_lams_learning_group_1` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_group`
--

LOCK TABLES `lams_group` WRITE;
/*!40000 ALTER TABLE `lams_group` DISABLE KEYS */;
INSERT INTO `lams_group` VALUES (1,'LAMS Demo Course learners',1,1,NULL),(2,'LAMS Demo Course monitors',1,1,NULL),(3,'LAMS Demo Course learners',2,1,NULL),(4,'LAMS Demo Course monitors',2,1,NULL),(5,'LAMS Demo Course learners',3,1,NULL),(6,'LAMS Demo Course monitors',3,1,NULL),(7,'LAMS Demo Course learners',4,1,NULL),(8,'LAMS Demo Course monitors',4,1,NULL);
/*!40000 ALTER TABLE `lams_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping`
--

DROP TABLE IF EXISTS `lams_grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_grouping` (
  `grouping_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `grouping_ui_id` int(11) DEFAULT NULL,
  `grouping_type_id` int(11) NOT NULL,
  `number_of_groups` int(11) DEFAULT NULL,
  `learners_per_group` int(11) DEFAULT NULL,
  `staff_group_id` bigint(20) DEFAULT '0',
  `max_number_of_groups` int(3) DEFAULT NULL,
  `equal_number_of_learners_per_group` tinyint(4) DEFAULT '0',
  `view_students_before_selection` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`grouping_id`),
  KEY `grouping_type_id` (`grouping_type_id`),
  CONSTRAINT `FK_lams_learning_grouping_1` FOREIGN KEY (`grouping_type_id`) REFERENCES `lams_grouping_type` (`grouping_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_grouping`
--

LOCK TABLES `lams_grouping` WRITE;
/*!40000 ALTER TABLE `lams_grouping` DISABLE KEYS */;
INSERT INTO `lams_grouping` VALUES (1,NULL,3,NULL,NULL,2,NULL,0,0),(2,NULL,3,NULL,NULL,4,NULL,0,0),(3,NULL,3,NULL,NULL,6,NULL,0,0),(4,NULL,3,NULL,NULL,8,NULL,0,0);
/*!40000 ALTER TABLE `lams_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping_support_type`
--

DROP TABLE IF EXISTS `lams_grouping_support_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_grouping_support_type` (
  `grouping_support_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY (`grouping_support_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_grouping_type` (
  `grouping_type_id` int(11) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY (`grouping_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_input_activity` (
  `activity_id` bigint(20) NOT NULL,
  `input_activity_id` bigint(20) NOT NULL,
  UNIQUE KEY `UQ_lams_input_activity_1` (`activity_id`,`input_activity_id`),
  KEY `activity_id` (`activity_id`),
  KEY `activity_id_2` (`activity_id`),
  CONSTRAINT `FK_lams_input_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_input_activity_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learner_progress` (
  `learner_progress_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `lesson_completed_flag` tinyint(1) NOT NULL DEFAULT '0',
  `waiting_flag` tinyint(4) NOT NULL,
  `start_date_time` datetime NOT NULL,
  `finish_date_time` datetime DEFAULT NULL,
  `current_activity_id` bigint(20) DEFAULT NULL,
  `next_activity_id` bigint(20) DEFAULT NULL,
  `previous_activity_id` bigint(20) DEFAULT NULL,
  `requires_restart_flag` tinyint(1) NOT NULL,
  PRIMARY KEY (`learner_progress_id`),
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
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learning_activity` (
  `activity_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activity_ui_id` int(11) DEFAULT NULL,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `help_text` text,
  `xcoord` int(11) DEFAULT NULL,
  `ycoord` int(11) DEFAULT NULL,
  `parent_activity_id` bigint(20) DEFAULT NULL,
  `parent_ui_id` int(11) DEFAULT NULL,
  `learning_activity_type_id` int(11) NOT NULL DEFAULT '0',
  `grouping_support_type_id` int(3) NOT NULL,
  `apply_grouping_flag` tinyint(1) NOT NULL,
  `grouping_id` bigint(20) DEFAULT NULL,
  `grouping_ui_id` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  `define_later_flag` tinyint(4) NOT NULL DEFAULT '0',
  `learning_design_id` bigint(20) DEFAULT NULL,
  `learning_library_id` bigint(20) DEFAULT NULL,
  `create_date_time` datetime NOT NULL,
  `run_offline_flag` tinyint(1) NOT NULL,
  `max_number_of_options` int(5) DEFAULT NULL,
  `min_number_of_options` int(5) DEFAULT NULL,
  `options_instructions` text,
  `tool_id` bigint(20) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `activity_category_id` int(3) NOT NULL,
  `gate_activity_level_id` int(11) DEFAULT NULL,
  `gate_open_flag` tinyint(1) DEFAULT NULL,
  `gate_start_time_offset` bigint(38) DEFAULT NULL,
  `gate_end_time_offset` bigint(38) DEFAULT NULL,
  `gate_start_date_time` datetime DEFAULT NULL,
  `gate_end_date_time` datetime DEFAULT NULL,
  `library_activity_ui_image` varchar(255) DEFAULT NULL,
  `create_grouping_id` bigint(20) DEFAULT NULL,
  `create_grouping_ui_id` int(11) DEFAULT NULL,
  `library_activity_id` bigint(20) DEFAULT NULL,
  `language_file` varchar(255) DEFAULT NULL,
  `system_tool_id` bigint(20) DEFAULT NULL,
  `read_only` tinyint(4) DEFAULT '0',
  `initialised` tinyint(4) DEFAULT '0',
  `default_activity_id` bigint(20) DEFAULT NULL,
  `start_xcoord` int(11) DEFAULT NULL,
  `start_ycoord` int(11) DEFAULT NULL,
  `end_xcoord` int(11) DEFAULT NULL,
  `end_ycoord` int(11) DEFAULT NULL,
  `stop_after_activity` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`activity_id`),
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
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_learning_activity`
--

LOCK TABLES `lams_learning_activity` WRITE;
/*!40000 ALTER TABLE `lams_learning_activity` DISABLE KEYS */;
INSERT INTO `lams_learning_activity` VALUES (1,NULL,'Forum/Message Board','Forum','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,1,'2010-04-01 15:56:16',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(2,NULL,'Displays a NoticeboardX','NoticeboardX','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,2,'2010-04-01 15:56:24',0,NULL,NULL,NULL,2,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.noticeboard.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(3,NULL,'Allows creation and use of question and answer format','Question and Answer','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,3,'2010-04-01 15:56:30',0,NULL,NULL,NULL,3,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.qa.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(4,NULL,'Uploading of files by learners, for review by teachers.','Submit File','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,4,'2010-04-01 15:56:35',0,NULL,NULL,NULL,4,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.sbmt.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(5,NULL,'Chat Tool','Chat Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,5,'2010-04-01 15:56:41',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(6,NULL,'Share Resources','Share Resources','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,6,'2010-04-01 15:56:47',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(7,NULL,'Allows creation and use of voting format','Voting','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,7,'2010-04-01 15:56:53',0,NULL,NULL,NULL,7,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.vote.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(8,NULL,'Notebook Tool','Notebook Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,8,'2010-04-01 15:56:58',0,NULL,NULL,NULL,8,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.notebook.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(9,NULL,'Survey','Survey','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,9,'2010-04-01 15:57:03',0,NULL,NULL,NULL,9,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.survey.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(10,NULL,'Scribe Tool','Scribe Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,10,'2010-04-01 15:57:08',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(11,NULL,'TaskList','TaskList','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,11,'2010-04-01 15:57:15',0,NULL,NULL,NULL,11,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/latask10/images/icon_taskList.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.taskList.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(12,NULL,'Gmap Tool','Gmap Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,12,'2010-04-01 15:57:22',0,NULL,NULL,NULL,12,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.gmap.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(13,NULL,'Spreadsheet','Spreadsheet','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,13,'2010-04-01 15:57:27',0,NULL,NULL,NULL,13,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(14,NULL,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,14,'2010-04-01 15:57:33',0,NULL,NULL,NULL,14,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.daco.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(15,NULL,'Wiki Tool','Wiki Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,15,'2010-04-01 15:57:38',0,NULL,NULL,NULL,15,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.wiki.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(16,NULL,'ImageGallery','ImageGallery','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,16,'2010-04-01 15:57:45',0,NULL,NULL,NULL,16,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laimag10/images/icon_imageGallery.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.imageGallery.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(17,NULL,'Dimdim Tool','Dimdim Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,17,'2010-04-01 15:57:52',0,NULL,NULL,NULL,17,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laddim10/images/icon_dimdim.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.dimdim.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(18,NULL,'VideoRecorder Tool','VideoRecorder Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,18,'2010-04-01 15:57:58',0,NULL,NULL,NULL,18,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavidr10/images/icon_videoRecorder.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.videoRecorder.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(19,NULL,'Mindmap Tool','Mindmap Tool','Mindmap Tool for creating mindmaps.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,19,'2010-04-01 15:58:03',0,NULL,NULL,NULL,19,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamind10/images/icon_mindmap.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.mindmap.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(20,NULL,'Assessment','Assessment','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,20,'2010-04-01 15:58:09',0,NULL,NULL,NULL,20,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laasse10/images/icon_assessment.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.assessment.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(21,NULL,'Pixlr Tool','Pixlr Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,21,'2010-04-01 15:58:17',0,NULL,NULL,NULL,21,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lapixl10/images/icon_pixlr.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.pixlr.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(22,NULL,'Allows creation and use of multiple choice questioning format','Multiple Choice Questions','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,22,'2010-04-01 15:58:21',0,NULL,NULL,NULL,22,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.mc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(23,NULL,'Combined Share Resources and Forum','Discuss Shared Resources','Learners to discuss items they have viewed via Share Resources.',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,23,'2010-04-01 15:58:23',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid23.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(24,NULL,'Share Resources','Share Resources','Share Resources',NULL,NULL,23,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:23',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(25,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,23,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:23',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(26,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,24,'2010-04-01 15:58:24',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_groupreporting.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid24.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(27,NULL,'Chat Tool','Chat Tool','Chat',NULL,NULL,26,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:24',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(28,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,26,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:24',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(29,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,25,'2010-04-01 15:58:25',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_forum_and_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid25.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(30,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,29,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:25',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(31,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,29,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2010-04-01 15:58:25',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(32,163,'Read noticeboard','Your Amazing Brain',NULL,7,32,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,2,23,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(33,227,'Read the attached information','Brain Needs',NULL,800,15,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,2,24,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(34,287,'Chat','Chat',NULL,910,249,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,5,25,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(35,278,'Share resources','Brain Needs',NULL,938,113,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,6,26,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(36,291,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',115,127,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,6,27,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(37,293,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',252,22,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,1,28,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(38,295,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',387,122,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,6,29,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(39,296,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',509,23,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,1,30,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(40,299,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',648,110,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,6,31,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(41,307,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',670,252,NULL,NULL,1,2,0,NULL,NULL,NULL,0,1,NULL,'2010-04-01 16:03:10',0,NULL,NULL,NULL,3,32,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(42,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',19,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,33,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(43,2,'Tool for displaying HTML content including external sources such as images and other media.','Informative Tools','Displays formatted text and links to external sources on a read only page.',190,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,34,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(44,4,'Tool for displaying HTML content including external sources such as images and other media.','Fancy Noticeboard','Displays formatted text and links to external sources on a read only page.',384,34,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,35,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(45,6,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',19,99,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,6,36,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(46,8,'Which activity type would you like to learn about next?  Select an option below then click CHOOSE.  You can come back to this screen and select a different activity type to learn about if you wish.','Activity Types - Choose One',NULL,25,191,NULL,NULL,13,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(47,40,'Tool for displaying HTML content including external sources such as images and other media.','Winding Up','Displays formatted text and links to external sources on a read only page.',268,470,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,37,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(48,9,NULL,'Assessment - Collect Learner Responses',NULL,4,48,46,8,8,2,0,NULL,NULL,1,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,49,NULL,NULL,NULL,NULL,0),(49,13,'Tool for displaying HTML content including external sources such as images and other media.','Assessment Activities','Displays formatted text and links to external sources on a read only page.',5,5,48,9,1,2,0,NULL,NULL,1,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,38,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(50,64,'Spreadsheet.','Spreadsheet','Spreadsheet Tool.',219,107,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,13,39,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(51,74,'Tool for displaying HTML content including external sources such as images and other media.','Dimdim Discussion','Displays formatted text and links to external sources on a read only page.',56,472,NULL,NULL,1,2,0,NULL,NULL,NULL,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,40,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(52,10,NULL,'Collaborative - Get Learners to Work Together',NULL,4,105,46,8,8,2,0,NULL,NULL,2,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,68,NULL,NULL,NULL,NULL,0),(53,16,'Creates automated assessment questions. e.g. Multiple choice and true/false questions. Can provide feedback and scores.','Demo Multiple Choice','Learner answers a series of automated assessment questions. e.g. Multiple choice and true/false questions. Optional features include feedback on each question and scoring. Questions are weighted for scoring.',65,5,48,9,1,2,0,NULL,NULL,2,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,22,44,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(54,22,'Tool for displaying HTML content including external sources such as images and other media.','Split-Screen Activities','Displays formatted text and links to external sources on a read only page.',65,5,52,10,1,2,0,NULL,NULL,2,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,45,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(55,12,NULL,'Reflective - \'Capture\' Students Learning',NULL,4,162,46,8,8,2,0,NULL,NULL,3,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,67,NULL,NULL,NULL,NULL,0),(56,30,'Tool to create Surveys','Survey','Answer surveys.',124,5,55,12,1,2,0,NULL,NULL,3,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,9,48,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(57,18,'Learners submit files for assessment by the teacher. Scores and comments may be exported as a spreadsheet.','Demo Submit Files','Learners submit files for assessment by the teacher. Scores and comments for each learner are recorded and may be exported as a spreadsheet.',124,5,48,9,1,2,0,NULL,NULL,3,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,4,49,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(58,60,'Combined Share Resources and Forum ','Resources and Forum','The top window has a Share Resources area and the bottom window has a forum for learners to discuss items they have view via the Share Resources area.',124,5,52,10,6,2,0,NULL,NULL,3,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(59,32,'Allows voting format','Voting','Voting help text',184,5,55,12,1,2,0,NULL,NULL,4,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,7,50,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(60,77,'Tool for displaying HTML content including external sources such as images and other media.','New Collaboratives','Displays formatted text and links to external sources on a read only page.',184,5,52,10,1,2,0,NULL,NULL,4,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,51,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(61,36,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',243,5,55,12,1,2,0,NULL,NULL,5,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,52,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(62,67,'Google Mapping Tool','Gmap','Gmap for marking world map points',243,5,52,10,1,2,0,NULL,NULL,5,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,12,53,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(63,34,'Notebook Tool','Notebook','Notebook for notes and reflections',303,5,55,12,1,2,0,NULL,NULL,6,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,8,54,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(64,69,'Wiki Tool','Wiki','Wiki tool for creating wiki pages',303,5,52,10,1,2,0,NULL,NULL,6,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,15,55,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(65,83,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',362,5,55,12,1,2,0,NULL,NULL,7,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,56,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(66,71,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',422,5,55,12,1,2,0,NULL,NULL,8,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,14,57,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(67,15,'Tool for displaying HTML content including external sources such as images and other media.','Reflective Intro','Displays formatted text and links to external sources on a read only page.',5,5,55,12,1,2,0,NULL,NULL,1,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,41,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(68,14,'Tool for displaying HTML content including external sources such as images and other media.','Collaborative Intro','Displays formatted text and links to external sources on a read only page.',5,5,52,10,1,2,0,NULL,NULL,1,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,2,42,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(69,61,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',8,45,58,60,1,2,0,NULL,NULL,1,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,6,43,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(70,28,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',65,5,55,12,1,2,0,NULL,NULL,2,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,3,46,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(71,62,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',8,108,58,60,1,2,0,NULL,NULL,2,0,2,NULL,'2010-04-01 16:03:17',0,NULL,NULL,NULL,1,47,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(72,1,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',46,213,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,2,58,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(73,4,'Choose one of the four options below to learn more about that particular aspect of LAMS.','Optional Sequences',NULL,254,100,NULL,NULL,13,2,0,NULL,NULL,NULL,0,3,NULL,'2010-04-01 16:03:24',0,NULL,1,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(74,5,NULL,'The Learning Environment (Participate in a Lesson)',NULL,4,48,73,4,8,2,0,NULL,NULL,1,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,75,NULL,NULL,NULL,NULL,0),(75,20,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',5,5,74,5,1,2,0,NULL,NULL,1,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,2,59,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(76,25,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',558,210,NULL,NULL,1,2,0,NULL,NULL,NULL,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,2,60,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(77,13,'Sharing resource with others.','Learner Wink','Uploading your resources to share with others.',65,5,74,5,1,2,0,NULL,NULL,2,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,6,64,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(78,6,NULL,'The Authoring Environment (Create a Lesson)',NULL,4,105,73,4,8,2,0,NULL,NULL,2,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,84,NULL,NULL,NULL,NULL,0),(79,8,NULL,'The Monitoring Environment (Teach a Lesson)',NULL,4,162,73,4,8,2,0,NULL,NULL,3,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,82,NULL,NULL,NULL,NULL,0),(80,23,'Sharing resource with others.','NoFlash Wink','Uploading your resources to share with others.',124,5,74,5,1,2,0,NULL,NULL,3,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,6,66,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(81,16,NULL,'System Administration [Online Link]',NULL,4,219,73,4,8,2,0,NULL,NULL,4,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,0,83,NULL,NULL,NULL,NULL,0),(82,15,'Sharing resource with others.','Monitor Wink','Uploading your resources to share with others.',5,5,79,8,1,2,0,NULL,NULL,1,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,6,61,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(83,17,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',5,5,81,16,1,2,0,NULL,NULL,1,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,6,62,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(84,14,'Sharing resource with others.','Author Wink','Uploading your resources to share with others.',5,5,78,6,1,2,0,NULL,NULL,1,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,6,63,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(85,50,'Tool for displaying HTML content including external sources such as images and other media.','Structure of LAMS','Displays formatted text and links to external sources on a read only page.',65,5,79,8,1,2,0,NULL,NULL,2,0,3,NULL,'2010-04-01 16:03:24',0,NULL,NULL,NULL,2,65,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(86,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',40,51,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2010-04-01 16:03:33',0,NULL,NULL,NULL,2,67,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(87,8,'Tool for displaying HTML content including external sources such as images and other media.','Visual Authoring','Displays formatted text and links to external sources on a read only page.',33,166,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2010-04-01 16:03:33',0,NULL,NULL,NULL,2,68,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(88,10,'Sharing resource with others.','Authoring Environment','Uploading your resources to share with others.',235,171,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2010-04-01 16:03:33',0,NULL,NULL,NULL,6,69,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(89,12,'Tool for displaying HTML content including external sources such as images and other media.','Summing Up','Displays formatted text and links to external sources on a read only page.',425,170,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2010-04-01 16:03:33',0,NULL,NULL,NULL,2,70,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(90,21,'Sharing resource with others.','Effective Learning','Uploading your resources to share with others.',227,47,NULL,NULL,1,2,0,NULL,NULL,NULL,0,4,NULL,'2010-04-01 16:03:33',0,NULL,NULL,NULL,6,71,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(91,287,'Chat','Chat',NULL,910,249,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,5,75,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(92,278,'Share resources','Brain Needs',NULL,938,113,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,6,80,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(93,299,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',648,110,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,6,81,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(94,296,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',509,23,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,1,77,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(95,227,'Read the attached information','Brain Needs',NULL,800,15,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,2,79,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(96,307,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',670,252,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,3,76,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(97,295,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',387,122,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:04',0,NULL,NULL,NULL,6,72,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(98,293,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',252,22,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:04',0,NULL,NULL,NULL,1,73,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(99,291,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',115,127,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:05',0,NULL,NULL,NULL,6,78,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(100,163,'Read noticeboard','Your Amazing Brain',NULL,7,32,NULL,NULL,1,2,0,NULL,NULL,NULL,0,5,NULL,'2010-04-01 16:04:04',0,NULL,NULL,NULL,2,74,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(101,16,'Creates automated assessment questions. e.g. Multiple choice and true/false questions. Can provide feedback and scores.','Demo Multiple Choice','Learner answers a series of automated assessment questions. e.g. Multiple choice and true/false questions. Optional features include feedback on each question and scoring. Questions are weighted for scoring.',65,5,126,9,1,2,0,NULL,NULL,2,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,22,95,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(102,13,'Tool for displaying HTML content including external sources such as images and other media.','Assessment Activities','Displays formatted text and links to external sources on a read only page.',5,5,126,9,1,2,0,NULL,NULL,1,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,96,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(103,64,'Spreadsheet.','Spreadsheet','Spreadsheet Tool.',219,107,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,13,85,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(104,6,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',19,99,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,6,82,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(105,40,'Tool for displaying HTML content including external sources such as images and other media.','Winding Up','Displays formatted text and links to external sources on a read only page.',268,470,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,106,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(106,74,'Tool for displaying HTML content including external sources such as images and other media.','Dimdim Discussion','Displays formatted text and links to external sources on a read only page.',56,472,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,83,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(107,8,'Which activity type would you like to learn about next?  Select an option below then click CHOOSE.  You can come back to this screen and select a different activity type to learn about if you wish.','Activity Types - Choose One',NULL,25,191,NULL,NULL,13,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,3,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(108,28,'Each learner answers question(s) and then sees answers from all learners collated on the next page.','Q and A','Each learner answers one or more questions in short answer format and then sees answers from all learners collated on the next page.',65,5,130,12,1,2,0,NULL,NULL,2,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,3,105,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(109,15,'Tool for displaying HTML content including external sources such as images and other media.','Reflective Intro','Displays formatted text and links to external sources on a read only page.',5,5,130,12,1,2,0,NULL,NULL,1,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,98,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(110,83,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',362,5,130,12,1,2,0,NULL,NULL,7,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,99,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(111,34,'Notebook Tool','Notebook','Notebook for notes and reflections',303,5,130,12,1,2,0,NULL,NULL,6,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,8,101,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(112,60,'Combined Share Resources and Forum ','Resources and Forum','The top window has a Share Resources area and the bottom window has a forum for learners to discuss items they have view via the Share Resources area.',124,5,128,10,6,2,0,NULL,NULL,3,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(113,22,'Tool for displaying HTML content including external sources such as images and other media.','Split-Screen Activities','Displays formatted text and links to external sources on a read only page.',65,5,128,10,1,2,0,NULL,NULL,2,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,92,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(114,77,'Tool for displaying HTML content including external sources such as images and other media.','New Collaboratives','Displays formatted text and links to external sources on a read only page.',184,5,128,10,1,2,0,NULL,NULL,4,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,93,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(115,2,'Tool for displaying HTML content including external sources such as images and other media.','Informative Tools','Displays formatted text and links to external sources on a read only page.',190,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,86,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(116,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',19,33,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,84,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(117,4,'Tool for displaying HTML content including external sources such as images and other media.','Fancy Noticeboard','Displays formatted text and links to external sources on a read only page.',384,34,NULL,NULL,1,2,0,NULL,NULL,NULL,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,87,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(118,71,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',422,5,130,12,1,2,0,NULL,NULL,8,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,14,104,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(119,69,'Wiki Tool','Wiki','Wiki tool for creating wiki pages',303,5,128,10,1,2,0,NULL,NULL,6,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,15,88,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(120,67,'Google Mapping Tool','Gmap','Gmap for marking world map points',243,5,128,10,1,2,0,NULL,NULL,5,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,12,91,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(121,36,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',243,5,130,12,1,2,0,NULL,NULL,5,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,100,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(122,32,'Allows voting format','Voting','Voting help text',184,5,130,12,1,2,0,NULL,NULL,4,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,7,103,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(123,30,'Tool to create Surveys','Survey','Answer surveys.',124,5,130,12,1,2,0,NULL,NULL,3,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,9,102,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(124,18,'Learners submit files for assessment by the teacher. Scores and comments may be exported as a spreadsheet.','Demo Submit Files','Learners submit files for assessment by the teacher. Scores and comments for each learner are recorded and may be exported as a spreadsheet.',124,5,126,9,1,2,0,NULL,NULL,3,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,4,97,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(125,14,'Tool for displaying HTML content including external sources such as images and other media.','Collaborative Intro','Displays formatted text and links to external sources on a read only page.',5,5,128,10,1,2,0,NULL,NULL,1,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,2,94,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(126,9,NULL,'Assessment - Collect Learner Responses',NULL,4,48,107,8,8,2,0,NULL,NULL,1,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,102,NULL,NULL,NULL,NULL,0),(127,61,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',8,45,112,60,1,2,0,NULL,NULL,1,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,6,90,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(128,10,NULL,'Collaborative - Get Learners to Work Together',NULL,4,105,107,8,8,2,0,NULL,NULL,2,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,125,NULL,NULL,NULL,NULL,0),(129,62,'Online threaded discussion tool (asynchronous).','Forum','Discussion tool useful for long running collaborations and situations where learners are not all on line at the same time.',8,108,112,60,1,2,0,NULL,NULL,2,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,1,89,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(130,12,NULL,'Reflective - \'Capture\' Students Learning',NULL,4,162,107,8,8,2,0,NULL,NULL,3,0,6,NULL,'2010-04-01 16:04:14',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,109,NULL,NULL,NULL,NULL,0),(131,50,'Tool for displaying HTML content including external sources such as images and other media.','Structure of LAMS','Displays formatted text and links to external sources on a read only page.',65,5,143,8,1,2,0,NULL,NULL,2,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,2,109,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(132,15,'Sharing resource with others.','Monitor Wink','Uploading your resources to share with others.',5,5,143,8,1,2,0,NULL,NULL,1,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,6,108,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(133,25,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',558,210,NULL,NULL,1,2,0,NULL,NULL,NULL,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,2,114,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(134,4,'Choose one of the four options below to learn more about that particular aspect of LAMS.','Optional Sequences',NULL,254,100,NULL,NULL,13,2,0,NULL,NULL,NULL,0,7,NULL,'2010-04-01 16:04:26',0,NULL,1,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(135,23,'Sharing resource with others.','NoFlash Wink','Uploading your resources to share with others.',124,5,139,5,1,2,0,NULL,NULL,3,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,6,111,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(136,13,'Sharing resource with others.','Learner Wink','Uploading your resources to share with others.',65,5,139,5,1,2,0,NULL,NULL,2,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,6,113,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(137,20,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',5,5,139,5,1,2,0,NULL,NULL,1,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,2,112,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(138,1,'Tool for displaying HTML content including external sources such as images and other media.','Noticeboard','Displays formatted text and links to external sources on a read only page.',46,213,NULL,NULL,1,2,0,NULL,NULL,NULL,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,2,115,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(139,5,NULL,'The Learning Environment (Participate in a Lesson)',NULL,4,48,134,4,8,2,0,NULL,NULL,1,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,137,NULL,NULL,NULL,NULL,0),(140,14,'Sharing resource with others.','Author Wink','Uploading your resources to share with others.',5,5,142,6,1,2,0,NULL,NULL,1,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,6,107,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(141,17,'Sharing resource with others.','Share Resources','Uploading your resources to share with others.',5,5,144,16,1,2,0,NULL,NULL,1,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,6,110,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(142,6,NULL,'The Authoring Environment (Create a Lesson)',NULL,4,105,134,4,8,2,0,NULL,NULL,2,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,140,NULL,NULL,NULL,NULL,0),(143,8,NULL,'The Monitoring Environment (Teach a Lesson)',NULL,4,162,134,4,8,2,0,NULL,NULL,3,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,132,NULL,NULL,NULL,NULL,0),(144,16,NULL,'System Administration [Online Link]',NULL,4,219,134,4,8,2,0,NULL,NULL,4,0,7,NULL,'2010-04-01 16:04:26',0,NULL,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,1,141,NULL,NULL,NULL,NULL,0),(145,8,'Tool for displaying HTML content including external sources such as images and other media.','Visual Authoring','Displays formatted text and links to external sources on a read only page.',33,166,NULL,NULL,1,2,0,NULL,NULL,NULL,0,8,NULL,'2010-04-01 16:05:08',0,NULL,NULL,NULL,2,119,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(146,21,'Sharing resource with others.','Effective Learning','Uploading your resources to share with others.',227,47,NULL,NULL,1,2,0,NULL,NULL,NULL,0,8,NULL,'2010-04-01 16:05:08',0,NULL,NULL,NULL,6,118,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(147,10,'Sharing resource with others.','Authoring Environment','Uploading your resources to share with others.',235,171,NULL,NULL,1,2,0,NULL,NULL,NULL,0,8,NULL,'2010-04-01 16:05:08',0,NULL,NULL,NULL,6,116,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(148,1,'Tool for displaying HTML content including external sources such as images and other media.','Introduction','Displays formatted text and links to external sources on a read only page.',40,51,NULL,NULL,1,2,0,NULL,NULL,NULL,0,8,NULL,'2010-04-01 16:05:08',0,NULL,NULL,NULL,2,120,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0),(149,12,'Tool for displaying HTML content including external sources such as images and other media.','Summing Up','Displays formatted text and links to external sources on a read only page.',425,170,NULL,NULL,1,2,0,NULL,NULL,NULL,0,8,NULL,'2010-04-01 16:05:08',0,NULL,NULL,NULL,2,117,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,NULL,NULL,0,1,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `lams_learning_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_activity_type`
--

DROP TABLE IF EXISTS `lams_learning_activity_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learning_activity_type` (
  `learning_activity_type_id` int(11) NOT NULL DEFAULT '0',
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`learning_activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_learning_activity_type`
--

LOCK TABLES `lams_learning_activity_type` WRITE;
/*!40000 ALTER TABLE `lams_learning_activity_type` DISABLE KEYS */;
INSERT INTO `lams_learning_activity_type` VALUES (1,'TOOL'),(2,'GROUPING'),(3,'GATE_SYNCH'),(4,'GATE_SCHEDULE'),(5,'GATE_PERMISSION'),(6,'PARALLEL'),(7,'OPTIONS'),(8,'SEQUENCE'),(9,'GATE_SYSTEM'),(10,'BRANCHING_CHOSEN'),(11,'BRANCHING_GROUP'),(12,'BRANCHING_TOOL'),(13,'OPTIONS_WITH_SEQUENCES'),(14,'GATE_CONDITION'),(15,'FLOATING');
/*!40000 ALTER TABLE `lams_learning_activity_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_design`
--

DROP TABLE IF EXISTS `lams_learning_design`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learning_design` (
  `learning_design_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_ui_id` int(11) DEFAULT NULL,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `first_activity_id` bigint(20) DEFAULT NULL,
  `floating_activity_id` bigint(20) DEFAULT NULL,
  `max_id` int(11) DEFAULT NULL,
  `valid_design_flag` tinyint(4) NOT NULL,
  `read_only_flag` tinyint(4) NOT NULL,
  `date_read_only` datetime DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `help_text` text,
  `online_instructions` text,
  `offline_instructions` text,
  `copy_type_id` tinyint(4) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `version` varchar(56) DEFAULT NULL,
  `original_learning_design_id` bigint(20) DEFAULT NULL,
  `workspace_folder_id` bigint(20) DEFAULT NULL,
  `duration` bigint(38) DEFAULT NULL,
  `license_id` bigint(20) DEFAULT NULL,
  `license_text` text,
  `last_modified_date_time` datetime DEFAULT NULL,
  `content_folder_id` varchar(32) DEFAULT NULL,
  `edit_override_lock` tinyint(4) DEFAULT '0',
  `edit_override_user_id` bigint(20) DEFAULT NULL,
  `design_version` int(11) DEFAULT '1',
  PRIMARY KEY (`learning_design_id`),
  KEY `user_id` (`user_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `license_id` (`license_id`),
  KEY `copy_type_id` (`copy_type_id`),
  KEY `edit_override_user_id` (`edit_override_user_id`),
  KEY `idx_design_parent_id` (`original_learning_design_id`),
  KEY `idx_design_first_act` (`first_activity_id`),
  KEY `idx_design_floating_act` (`floating_activity_id`),
  CONSTRAINT `FK_lams_learning_design_3` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learning_design_4` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_learning_design_5` FOREIGN KEY (`license_id`) REFERENCES `lams_license` (`license_id`),
  CONSTRAINT `FK_lams_learning_design_6` FOREIGN KEY (`copy_type_id`) REFERENCES `lams_copy_type` (`copy_type_id`),
  CONSTRAINT `FK_lams_learning_design_7` FOREIGN KEY (`edit_override_user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_learning_design`
--

LOCK TABLES `lams_learning_design` WRITE;
/*!40000 ALTER TABLE `lams_learning_design` DISABLE KEYS */;
INSERT INTO `lams_learning_design` VALUES (1,NULL,'','Your Amazing Brain',32,NULL,308,1,0,NULL,5,NULL,NULL,NULL,1,'2010-04-01 16:03:10','1.0.2',NULL,46,NULL,NULL,NULL,'2010-04-01 16:03:10','8a8a8c181072e804011073639e9c004e',0,NULL,1),(2,NULL,'Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2\n\nv20081411','Lesson 3 - LAMS Activities',42,NULL,86,1,0,NULL,5,NULL,NULL,NULL,1,'2010-04-01 16:03:17','2.1.0.200811141019',NULL,46,NULL,1,NULL,'2010-04-01 16:03:17','4028efbb1935b8d301193606f5000051',0,NULL,1),(3,NULL,'Second lesson in the LAMS101 course.\nDescribes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','Lesson 2 - LAMS Environments',72,NULL,53,1,0,NULL,5,NULL,NULL,NULL,1,'2010-04-01 16:03:24','2.0.100.200803121637',NULL,46,NULL,1,NULL,'2010-04-01 16:03:24','4028efbb18edfbb70118ee6bacc700d2',0,NULL,1),(4,NULL,'Lesson 1 in the LAMS101 Course.\n\nDescribes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','Lesson 1 - What Is LAMS',86,NULL,27,1,0,NULL,5,NULL,NULL,NULL,1,'2010-04-01 16:03:33','2.0.100.200803121637',NULL,46,NULL,1,NULL,'2010-04-01 16:03:33','4028efbb18edfbb70118ee3b3b6d00b8',0,NULL,1),(5,NULL,'','Your Amazing Brain',100,NULL,308,1,1,NULL,5,NULL,NULL,NULL,2,'2010-04-01 16:04:04','1.0.2',1,23,NULL,NULL,NULL,'2010-04-01 16:04:04','8a8a8c181072e804011073639e9c004e',0,NULL,1),(6,NULL,'Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2\n\nv20081411','Lesson 3 - LAMS Activities',116,NULL,86,1,1,NULL,5,NULL,NULL,NULL,2,'2010-04-01 16:04:14','2.1.0.200811141019',2,23,NULL,1,NULL,'2010-04-01 16:04:14','4028efbb1935b8d301193606f5000051',0,NULL,1),(7,NULL,'Second lesson in the LAMS101 course.\nDescribes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','Lesson 2 - LAMS Environments',138,NULL,53,1,1,NULL,5,NULL,NULL,NULL,2,'2010-04-01 16:04:26','2.0.100.200803121637',3,23,NULL,1,NULL,'2010-04-01 16:04:26','4028efbb18edfbb70118ee6bacc700d2',0,NULL,1),(8,NULL,'Lesson 1 in the LAMS101 Course.\n\nDescribes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','Lesson 1 - What Is LAMS',148,NULL,27,1,1,NULL,5,NULL,NULL,NULL,2,'2010-04-01 16:05:08','2.0.100.200803121637',4,23,NULL,1,NULL,'2010-04-01 16:05:08','4028efbb18edfbb70118ee3b3b6d00b8',0,NULL,1);
/*!40000 ALTER TABLE `lams_learning_design` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_library`
--

DROP TABLE IF EXISTS `lams_learning_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learning_library` (
  `learning_library_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `valid_flag` tinyint(1) NOT NULL DEFAULT '1',
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY (`learning_library_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_learning_library`
--

LOCK TABLES `lams_learning_library` WRITE;
/*!40000 ALTER TABLE `lams_learning_library` DISABLE KEYS */;
INSERT INTO `lams_learning_library` VALUES (1,'Forum, also known Message Board','Forum',1,'2010-04-01 15:56:16'),(2,'Displays a Noticeboard','Noticeboard',1,'2010-04-01 15:56:24'),(3,'Question and Answer Learning Library Description','Question and Answer',1,'2010-04-01 15:56:30'),(4,'Uploading of files by learners, for review by teachers.','Submit file',1,'2010-04-01 15:56:35'),(5,'Chat Tool','Chat',1,'2010-04-01 15:56:41'),(6,'Share resources','Share resources',1,'2010-04-01 15:56:47'),(7,'Voting Learning Library Description','Voting',1,'2010-04-01 15:56:53'),(8,'Notebook Tool','Notebook',1,'2010-04-01 15:56:58'),(9,'Survey','Survey',1,'2010-04-01 15:57:03'),(10,'Scribe Tool','Scribe',0,'2010-04-01 15:57:08'),(11,'Share taskList','Share taskList',1,'2010-04-01 15:57:15'),(12,'Gmap Tool','Gmap',1,'2010-04-01 15:57:22'),(13,'Spreadsheet Tool','Spreadsheet',1,'2010-04-01 15:57:27'),(14,'Collecting data with custom structure.','Data Collection',1,'2010-04-01 15:57:33'),(15,'Wiki Tool','Wiki',1,'2010-04-01 15:57:38'),(16,'Share imageGallery','Share imageGallery',1,'2010-04-01 15:57:45'),(17,'Dimdim Tool','Dimdim',0,'2010-04-01 15:57:52'),(18,'VideoRecorder Tool','VideoRecorder',0,'2010-04-01 15:57:58'),(19,'Mindmap Tool','Mindmap',1,'2010-04-01 15:58:03'),(20,'Assessment','Assessment',1,'2010-04-01 15:58:09'),(21,'Pixlr Tool','Pixlr',1,'2010-04-01 15:58:17'),(22,'MCQ Learning Library Description','MCQ',1,'2010-04-01 15:58:21'),(23,'Shared Resources and Forum','Resources and Forum',1,'2010-04-01 15:58:23'),(24,'Chat and Scribe','Chat and Scribe',1,'2010-04-01 15:58:24'),(25,'Forum and Scribe','Forum and Scribe',1,'2010-04-01 15:58:25');
/*!40000 ALTER TABLE `lams_learning_library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_transition`
--

DROP TABLE IF EXISTS `lams_learning_transition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_learning_transition` (
  `transition_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transition_ui_id` int(11) DEFAULT NULL,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `to_activity_id` bigint(20) NOT NULL,
  `from_activity_id` bigint(20) NOT NULL,
  `learning_design_id` bigint(20) NOT NULL DEFAULT '0',
  `create_date_time` datetime NOT NULL,
  `to_ui_id` int(11) DEFAULT NULL,
  `from_ui_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`transition_id`),
  UNIQUE KEY `UQ_transition_activities` (`from_activity_id`,`to_activity_id`),
  KEY `from_activity_id` (`from_activity_id`),
  KEY `to_activity_id` (`to_activity_id`),
  KEY `learning_design_id` (`learning_design_id`),
  CONSTRAINT `FK_learning_transition_3` FOREIGN KEY (`from_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_learning_transition_2` FOREIGN KEY (`to_activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `lddefn_transition_ibfk_1` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=79 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_learning_transition`
--

LOCK TABLES `lams_learning_transition` WRITE;
/*!40000 ALTER TABLE `lams_learning_transition` DISABLE KEYS */;
INSERT INTO `lams_learning_transition` VALUES (1,297,NULL,NULL,38,37,1,'2010-04-01 16:03:10',295,293),(2,298,NULL,NULL,39,38,1,'2010-04-01 16:03:10',296,295),(3,308,NULL,NULL,41,34,1,'2010-04-01 16:03:10',307,287),(4,305,NULL,NULL,34,35,1,'2010-04-01 16:03:10',287,278),(5,294,NULL,NULL,37,36,1,'2010-04-01 16:03:10',293,291),(6,300,NULL,NULL,40,39,1,'2010-04-01 16:03:10',299,296),(7,292,NULL,NULL,36,32,1,'2010-04-01 16:03:10',291,163),(8,280,NULL,NULL,35,33,1,'2010-04-01 16:03:10',278,227),(9,301,NULL,NULL,33,40,1,'2010-04-01 16:03:10',227,299),(10,65,NULL,NULL,50,45,2,'2010-04-01 16:03:17',64,6),(11,63,NULL,NULL,58,54,2,'2010-04-01 16:03:17',60,22),(12,82,NULL,NULL,64,62,2,'2010-04-01 16:03:17',69,67),(13,38,NULL,NULL,61,59,2,'2010-04-01 16:03:17',36,32),(14,85,NULL,NULL,65,63,2,'2010-04-01 16:03:17',83,34),(15,75,NULL,NULL,47,51,2,'2010-04-01 16:03:17',40,74),(16,42,NULL,NULL,54,68,2,'2010-04-01 16:03:17',22,14),(17,66,NULL,NULL,46,50,2,'2010-04-01 16:03:17',8,64),(18,33,NULL,NULL,59,56,2,'2010-04-01 16:03:17',32,30),(19,79,NULL,NULL,60,58,2,'2010-04-01 16:03:17',77,60),(20,86,NULL,NULL,66,65,2,'2010-04-01 16:03:17',71,83),(21,31,NULL,NULL,56,70,2,'2010-04-01 16:03:17',30,28),(22,19,NULL,NULL,57,53,2,'2010-04-01 16:03:17',18,16),(23,76,NULL,NULL,51,46,2,'2010-04-01 16:03:17',74,8),(24,5,NULL,NULL,44,43,2,'2010-04-01 16:03:17',4,2),(25,7,NULL,NULL,45,44,2,'2010-04-01 16:03:17',6,4),(26,80,NULL,NULL,62,60,2,'2010-04-01 16:03:17',67,77),(27,29,NULL,NULL,70,67,2,'2010-04-01 16:03:17',28,15),(28,39,NULL,NULL,63,61,2,'2010-04-01 16:03:17',34,36),(29,3,NULL,NULL,43,42,2,'2010-04-01 16:03:17',2,1),(30,17,NULL,NULL,53,49,2,'2010-04-01 16:03:17',16,13),(31,22,NULL,NULL,77,75,3,'2010-04-01 16:03:24',13,20),(32,24,NULL,NULL,80,77,3,'2010-04-01 16:03:24',23,13),(33,53,NULL,NULL,85,82,3,'2010-04-01 16:03:24',50,15),(34,7,NULL,NULL,73,72,3,'2010-04-01 16:03:24',4,1),(35,26,NULL,NULL,76,73,3,'2010-04-01 16:03:24',25,4),(36,11,NULL,NULL,88,87,4,'2010-04-01 16:03:33',10,8),(37,13,NULL,NULL,89,88,4,'2010-04-01 16:03:33',12,10),(38,27,NULL,NULL,87,90,4,'2010-04-01 16:03:33',8,21),(39,23,NULL,NULL,90,86,4,'2010-04-01 16:03:33',21,1),(40,305,NULL,NULL,91,92,5,'2010-04-01 16:04:05',287,278),(41,300,NULL,NULL,93,94,5,'2010-04-01 16:04:05',299,296),(42,280,NULL,NULL,92,95,5,'2010-04-01 16:04:05',278,227),(43,308,NULL,NULL,96,91,5,'2010-04-01 16:04:05',307,287),(44,297,NULL,NULL,97,98,5,'2010-04-01 16:04:05',295,293),(45,294,NULL,NULL,98,99,5,'2010-04-01 16:04:05',293,291),(46,301,NULL,NULL,95,93,5,'2010-04-01 16:04:05',227,299),(47,292,NULL,NULL,99,100,5,'2010-04-01 16:04:05',291,163),(48,298,NULL,NULL,94,97,5,'2010-04-01 16:04:05',296,295),(49,17,NULL,NULL,101,102,6,'2010-04-01 16:04:14',16,13),(50,65,NULL,NULL,103,104,6,'2010-04-01 16:04:14',64,6),(51,75,NULL,NULL,105,106,6,'2010-04-01 16:04:14',40,74),(52,76,NULL,NULL,106,107,6,'2010-04-01 16:04:14',74,8),(53,29,NULL,NULL,108,109,6,'2010-04-01 16:04:14',28,15),(54,85,NULL,NULL,110,111,6,'2010-04-01 16:04:14',83,34),(55,63,NULL,NULL,112,113,6,'2010-04-01 16:04:14',60,22),(56,79,NULL,NULL,114,112,6,'2010-04-01 16:04:14',77,60),(57,3,NULL,NULL,115,116,6,'2010-04-01 16:04:14',2,1),(58,5,NULL,NULL,117,115,6,'2010-04-01 16:04:14',4,2),(59,86,NULL,NULL,118,110,6,'2010-04-01 16:04:14',71,83),(60,82,NULL,NULL,119,120,6,'2010-04-01 16:04:14',69,67),(61,39,NULL,NULL,111,121,6,'2010-04-01 16:04:14',34,36),(62,7,NULL,NULL,104,117,6,'2010-04-01 16:04:14',6,4),(63,33,NULL,NULL,122,123,6,'2010-04-01 16:04:14',32,30),(64,38,NULL,NULL,121,122,6,'2010-04-01 16:04:14',36,32),(65,31,NULL,NULL,123,108,6,'2010-04-01 16:04:14',30,28),(66,66,NULL,NULL,107,103,6,'2010-04-01 16:04:14',8,64),(67,19,NULL,NULL,124,101,6,'2010-04-01 16:04:14',18,16),(68,80,NULL,NULL,120,114,6,'2010-04-01 16:04:14',67,77),(69,42,NULL,NULL,113,125,6,'2010-04-01 16:04:14',22,14),(70,53,NULL,NULL,131,132,7,'2010-04-01 16:04:26',50,15),(71,26,NULL,NULL,133,134,7,'2010-04-01 16:04:26',25,4),(72,24,NULL,NULL,135,136,7,'2010-04-01 16:04:26',23,13),(73,22,NULL,NULL,136,137,7,'2010-04-01 16:04:26',13,20),(74,7,NULL,NULL,134,138,7,'2010-04-01 16:04:26',4,1),(75,27,NULL,NULL,145,146,8,'2010-04-01 16:05:08',8,21),(76,11,NULL,NULL,147,145,8,'2010-04-01 16:05:08',10,8),(77,23,NULL,NULL,146,148,8,'2010-04-01 16:05:08',21,1),(78,13,NULL,NULL,149,147,8,'2010-04-01 16:05:08',12,10);
/*!40000 ALTER TABLE `lams_learning_transition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson`
--

DROP TABLE IF EXISTS `lams_lesson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_lesson` (
  `lesson_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `learning_design_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `create_date_time` datetime NOT NULL,
  `organisation_id` bigint(20) DEFAULT NULL,
  `class_grouping_id` bigint(20) DEFAULT NULL,
  `lesson_state_id` int(3) NOT NULL,
  `start_date_time` datetime DEFAULT NULL,
  `schedule_start_date_time` datetime DEFAULT NULL,
  `end_date_time` datetime DEFAULT NULL,
  `schedule_end_date_time` datetime DEFAULT NULL,
  `previous_state_id` int(3) DEFAULT NULL,
  `learner_exportport_avail` tinyint(1) DEFAULT '1',
  `learner_presence_avail` tinyint(1) DEFAULT '0',
  `learner_im_avail` tinyint(1) DEFAULT '0',
  `live_edit_enabled` tinyint(1) DEFAULT '0',
  `locked_for_edit` tinyint(4) DEFAULT '0',
  `marks_released` tinyint(4) DEFAULT '0',
  `version` int(11) DEFAULT '1',
  PRIMARY KEY (`lesson_id`),
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_lesson`
--

LOCK TABLES `lams_lesson` WRITE;
/*!40000 ALTER TABLE `lams_lesson` DISABLE KEYS */;
INSERT INTO `lams_lesson` VALUES (1,5,5,'Your Amazing Brain',NULL,'2010-04-01 16:04:05',4,1,3,'2010-04-01 16:04:05',NULL,NULL,NULL,NULL,1,0,0,1,0,0,2),(2,6,5,'Lesson 3 - LAMS Activities','Final lesson in the LAMS101 course. Discusses all of the tools available in LAMS and gives demonstrations of each in action.This is a new version of this sequence, which discusses the new activities added as part of LAMS 2.2v20081411','2010-04-01 16:04:14',4,2,3,'2010-04-01 16:04:15',NULL,NULL,NULL,NULL,1,0,0,1,0,0,2),(3,7,5,'Lesson 2 - LAMS Environments','Second lesson in the LAMS101 course.Describes each environment of the LAMS system:  Learning, Authoring, Monitoring and System Administration.  This lesson also demonstrates how LAMS can replicate a \'real-world\' educational structure.','2010-04-01 16:04:26',4,3,3,'2010-04-01 16:04:27',NULL,NULL,NULL,NULL,1,0,0,1,0,0,2),(4,8,5,'Lesson 1 - What is LAMS?','Lesson 1 in the LAMS101 Course.Describes what LAMS is, and demonstrates effective use of the Learning and Monitoring environments.','2010-04-01 16:05:08',4,4,3,'2010-04-01 16:05:08',NULL,NULL,NULL,NULL,1,1,1,1,0,0,2);
/*!40000 ALTER TABLE `lams_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson_learner`
--

DROP TABLE IF EXISTS `lams_lesson_learner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_lesson_learner` (
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `lesson_id` (`lesson_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_lesson_learner_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_lesson_learner_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_lesson_state` (
  `lesson_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`lesson_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_license` (
  `license_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `code` varchar(20) NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  `default_flag` tinyint(1) NOT NULL DEFAULT '0',
  `picture_url` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`license_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_log_event` (
  `log_event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_event_type_id` int(5) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `timestamp` datetime NOT NULL,
  `ref_id` bigint(20) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`log_event_id`),
  KEY `log_event_type_id` (`log_event_type_id`),
  CONSTRAINT `FK_lams_event_log_1` FOREIGN KEY (`log_event_type_id`) REFERENCES `lams_log_event_type` (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_log_event_type` (
  `log_event_type_id` int(5) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_notebook_entry` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `external_id` bigint(20) DEFAULT NULL,
  `external_id_type` int(11) DEFAULT NULL,
  `external_signature` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `entry` text,
  `create_date` datetime DEFAULT NULL,
  `last_modified` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_organisation` (
  `organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL,
  `code` varchar(20) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `parent_organisation_id` bigint(20) DEFAULT NULL,
  `organisation_type_id` int(3) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `created_by` bigint(20) NOT NULL,
  `workspace_id` bigint(20) DEFAULT NULL,
  `organisation_state_id` int(3) NOT NULL,
  `admin_add_new_users` tinyint(1) NOT NULL DEFAULT '0',
  `admin_browse_all_users` tinyint(1) NOT NULL DEFAULT '0',
  `admin_change_status` tinyint(1) NOT NULL DEFAULT '0',
  `admin_create_guest` tinyint(1) NOT NULL DEFAULT '0',
  `enable_monitor_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  `enable_learner_gradebook` tinyint(1) NOT NULL DEFAULT '0',
  `locale_id` int(11) DEFAULT NULL,
  `archived_date` datetime DEFAULT NULL,
  `ordered_lesson_ids` text,
  PRIMARY KEY (`organisation_id`),
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_organisation`
--

LOCK TABLES `lams_organisation` WRITE;
/*!40000 ALTER TABLE `lams_organisation` DISABLE KEYS */;
INSERT INTO `lams_organisation` VALUES (1,'Root',NULL,'Root Organisation',NULL,1,'2010-04-01 15:55:04',1,1,1,0,0,0,0,0,0,1,NULL,NULL),(2,'Playpen','PP101','Developers Playpen',1,2,'2010-04-01 15:55:04',1,2,1,0,0,0,0,0,0,1,NULL,NULL),(3,'Everybody',NULL,'All People In Course',2,3,'2010-04-01 15:55:04',1,NULL,1,0,0,0,0,0,0,1,NULL,NULL),(4,'LAMS Demo Course','LAMS 101','A LAMS demo course',1,2,'2010-04-01 15:55:04',1,3,1,0,0,0,0,1,1,1,NULL,NULL),(5,'Tutorial Group A','TUTA','Tutorial Group A',4,3,'2010-04-01 15:55:04',1,NULL,1,0,0,0,0,0,0,2,NULL,NULL),(6,'Tutorial Group B','TUTB','Tutorial Group B',4,3,'2010-04-01 15:55:04',1,NULL,1,0,0,0,0,0,0,2,NULL,NULL),(7,'Moodle','Moodle','Moodle Test',1,2,'2010-04-01 15:55:04',1,50,2,0,0,0,0,0,0,1,NULL,NULL);
/*!40000 ALTER TABLE `lams_organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_organisation_state`
--

DROP TABLE IF EXISTS `lams_organisation_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_organisation_state` (
  `organisation_state_id` int(3) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`organisation_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_organisation_type` (
  `organisation_type_id` int(3) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`organisation_type_id`),
  UNIQUE KEY `UQ_lams_organisation_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_password_request` (
  `request_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `request_key` varchar(32) NOT NULL,
  `request_date` datetime NOT NULL,
  PRIMARY KEY (`request_id`),
  UNIQUE KEY `IX_lams_psswd_rqst_key` (`request_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_password_request`
--

LOCK TABLES `lams_password_request` WRITE;
/*!40000 ALTER TABLE `lams_password_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_password_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_planner_node_role`
--

DROP TABLE IF EXISTS `lams_planner_node_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_planner_node_role` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_uid` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` int(6) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `node_uid` (`node_uid`,`user_id`,`role_id`),
  KEY `FK_planner_node_role_user` (`user_id`),
  KEY `FK_planner_node_role_role` (`role_id`),
  CONSTRAINT `FK_planner_node_role_user` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_planner_node_role_node` FOREIGN KEY (`node_uid`) REFERENCES `lams_planner_nodes` (`uid`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_planner_node_role_role` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_planner_node_role`
--

LOCK TABLES `lams_planner_node_role` WRITE;
/*!40000 ALTER TABLE `lams_planner_node_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_planner_node_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_planner_nodes`
--

DROP TABLE IF EXISTS `lams_planner_nodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_planner_nodes` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_uid` bigint(20) DEFAULT NULL,
  `order_id` tinyint(3) unsigned NOT NULL,
  `locked` tinyint(1) NOT NULL DEFAULT '0',
  `content_folder_id` varchar(32) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `brief_desc` text,
  `full_desc` text,
  `file_uuid` bigint(20) unsigned DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `parent_uid` (`parent_uid`,`order_id`),
  CONSTRAINT `FK_lams_planner_node_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_planner_nodes` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_planner_nodes`
--

LOCK TABLES `lams_planner_nodes` WRITE;
/*!40000 ALTER TABLE `lams_planner_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_planner_nodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_planner_recent_learning_designs`
--

DROP TABLE IF EXISTS `lams_planner_recent_learning_designs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_planner_recent_learning_designs` (
  `user_id` bigint(20) NOT NULL,
  `learning_design_id` bigint(20) NOT NULL,
  `last_modified_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`learning_design_id`),
  KEY `FK_lams_planner_recent_learning_designs_2` (`learning_design_id`),
  CONSTRAINT `FK_lams_planner_recent_learning_designs_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_planner_recent_learning_designs_2` FOREIGN KEY (`learning_design_id`) REFERENCES `lams_learning_design` (`learning_design_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_planner_recent_learning_designs`
--

LOCK TABLES `lams_planner_recent_learning_designs` WRITE;
/*!40000 ALTER TABLE `lams_planner_recent_learning_designs` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_planner_recent_learning_designs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_presence_chat_msgs`
--

DROP TABLE IF EXISTS `lams_presence_chat_msgs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_presence_chat_msgs` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) DEFAULT NULL,
  `from_user` varchar(255) DEFAULT NULL,
  `to_user` varchar(255) DEFAULT NULL,
  `date_sent` datetime DEFAULT NULL,
  `message` varchar(1023) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_presence_chat_msgs`
--

LOCK TABLES `lams_presence_chat_msgs` WRITE;
/*!40000 ALTER TABLE `lams_presence_chat_msgs` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_presence_chat_msgs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_privilege`
--

DROP TABLE IF EXISTS `lams_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_privilege` (
  `privilege_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(10) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`privilege_id`),
  UNIQUE KEY `IX_lams_privilege_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_progress_attempted` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_current_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_current_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_progress_completed` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `completed_date_time` datetime DEFAULT NULL,
  `start_date_time` datetime DEFAULT NULL,
  PRIMARY KEY (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_completed_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_completed_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_progress_completed`
--

LOCK TABLES `lams_progress_completed` WRITE;
/*!40000 ALTER TABLE `lams_progress_completed` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_progress_completed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `lams_qtz_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_BLOB_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_blob_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_BLOB_TRIGGERS`
--

LOCK TABLES `lams_qtz_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_CALENDARS`
--

DROP TABLE IF EXISTS `lams_qtz_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_CALENDARS` (
  `CALENDAR_NAME` varchar(80) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_CALENDARS`
--

LOCK TABLES `lams_qtz_CALENDARS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `lams_qtz_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_CRON_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `CRON_EXPRESSION` varchar(80) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_cron_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_CRON_TRIGGERS`
--

LOCK TABLES `lams_qtz_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_CRON_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `lams_qtz_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_FIRED_TRIGGERS` (
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(80) DEFAULT NULL,
  `JOB_GROUP` varchar(80) DEFAULT NULL,
  `IS_STATEFUL` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_FIRED_TRIGGERS`
--

LOCK TABLES `lams_qtz_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_FIRED_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_JOB_DETAILS`
--

DROP TABLE IF EXISTS `lams_qtz_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_JOB_DETAILS` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(128) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `IS_STATEFUL` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_JOB_DETAILS`
--

LOCK TABLES `lams_qtz_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_JOB_DETAILS` DISABLE KEYS */;
INSERT INTO `lams_qtz_JOB_DETAILS` VALUES ('Resend Messages Job','DEFAULT','','org.lamsfoundation.lams.events.ResendMessagesJob','0','0','0','0',0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB0200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C770800000010000000007800);
/*!40000 ALTER TABLE `lams_qtz_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_JOB_LISTENERS`
--

DROP TABLE IF EXISTS `lams_qtz_JOB_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_JOB_LISTENERS` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `JOB_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`),
  CONSTRAINT `lams_qtz_job_listeners_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_JOB_LISTENERS`
--

LOCK TABLES `lams_qtz_JOB_LISTENERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_JOB_LISTENERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_JOB_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_LOCKS`
--

DROP TABLE IF EXISTS `lams_qtz_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_LOCKS` (
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_LOCKS`
--

LOCK TABLES `lams_qtz_LOCKS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_LOCKS` DISABLE KEYS */;
INSERT INTO `lams_qtz_LOCKS` VALUES ('CALENDAR_ACCESS'),('JOB_ACCESS'),('MISFIRE_ACCESS'),('STATE_ACCESS'),('TRIGGER_ACCESS');
/*!40000 ALTER TABLE `lams_qtz_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `lams_qtz_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_PAUSED_TRIGGER_GRPS` (
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `lams_qtz_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `lams_qtz_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_SCHEDULER_STATE` (
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  `RECOVERER` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_SCHEDULER_STATE`
--

LOCK TABLES `lams_qtz_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `lams_qtz_SCHEDULER_STATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `lams_qtz_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_SIMPLE_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(7) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_simple_triggers_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_SIMPLE_TRIGGERS`
--

LOCK TABLES `lams_qtz_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_SIMPLE_TRIGGERS` DISABLE KEYS */;
INSERT INTO `lams_qtz_SIMPLE_TRIGGERS` VALUES ('Resend Messages Job trigger','DEFAULT',-1,3600000,1);
/*!40000 ALTER TABLE `lams_qtz_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_TRIGGERS`
--

DROP TABLE IF EXISTS `lams_qtz_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `IS_VOLATILE` varchar(1) NOT NULL,
  `DESCRIPTION` varchar(120) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(80) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `JOB_NAME` (`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `lams_qtz_triggers_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_job_details` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_TRIGGERS`
--

LOCK TABLES `lams_qtz_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_TRIGGERS` DISABLE KEYS */;
INSERT INTO `lams_qtz_TRIGGERS` VALUES ('Resend Messages Job trigger','DEFAULT','Resend Messages Job','DEFAULT','0',NULL,1270101626285,1270098026285,'WAITING','SIMPLE',1270098026285,0,NULL,0,NULL);
/*!40000 ALTER TABLE `lams_qtz_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_TRIGGER_LISTENERS`
--

DROP TABLE IF EXISTS `lams_qtz_TRIGGER_LISTENERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_qtz_TRIGGER_LISTENERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `TRIGGER_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`),
  CONSTRAINT `lams_qtz_trigger_listeners_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_triggers` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_qtz_TRIGGER_LISTENERS`
--

LOCK TABLES `lams_qtz_TRIGGER_LISTENERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_TRIGGER_LISTENERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_qtz_TRIGGER_LISTENERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_registration`
--

DROP TABLE IF EXISTS `lams_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_registration` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_name` varchar(255) NOT NULL,
  `organisation` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `server_country` varchar(2) NOT NULL,
  `public_directory` tinyint(1) DEFAULT '1',
  `enable_lams_community` tinyint(1) DEFAULT '0',
  `server_key` varchar(255) NOT NULL,
  `server_id` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_registration`
--

LOCK TABLES `lams_registration` WRITE;
/*!40000 ALTER TABLE `lams_registration` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_registration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_role`
--

DROP TABLE IF EXISTS `lams_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_role` (
  `role_id` int(6) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `description` text,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`role_id`),
  KEY `gname` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_role`
--

LOCK TABLES `lams_role` WRITE;
/*!40000 ALTER TABLE `lams_role` DISABLE KEYS */;
INSERT INTO `lams_role` VALUES (1,'SYSADMIN','LAMS System Adminstrator','2010-04-01 15:55:04'),(2,'GROUP MANAGER','Group Manager','2010-04-01 15:55:04'),(3,'AUTHOR','Authors Learning Designs','2010-04-01 15:55:04'),(4,'MONITOR','Member of Staff','2010-04-01 15:55:04'),(5,'LEARNER','Student','2010-04-01 15:55:04'),(6,'GROUP ADMIN','Group Administrator','2010-04-01 15:55:04'),(7,'AUTHOR ADMIN','Author Administrator','2010-04-01 15:55:04');
/*!40000 ALTER TABLE `lams_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_role_privilege`
--

DROP TABLE IF EXISTS `lams_role_privilege`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_role_privilege` (
  `rp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(6) NOT NULL,
  `privilege_id` bigint(20) NOT NULL,
  PRIMARY KEY (`rp_id`),
  KEY `privilege_id` (`privilege_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `FK_lams_role_privilege_1` FOREIGN KEY (`privilege_id`) REFERENCES `lams_privilege` (`privilege_id`),
  CONSTRAINT `FK_lams_role_privilege_2` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_supported_locale` (
  `locale_id` int(11) NOT NULL AUTO_INCREMENT,
  `language_iso_code` varchar(2) NOT NULL COMMENT 'ISO 639-1 Language Code (2 letter version) Java only supports 2 letter properly, not the 3 letter codes.',
  `country_iso_code` varchar(2) DEFAULT NULL COMMENT 'ISO 3166 Country Code. Cannot use in unique key as allows null.',
  `description` varchar(255) NOT NULL,
  `direction` varchar(3) NOT NULL,
  `fckeditor_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='Describes the valid language/country combinations.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_supported_locale`
--

LOCK TABLES `lams_supported_locale` WRITE;
/*!40000 ALTER TABLE `lams_supported_locale` DISABLE KEYS */;
INSERT INTO `lams_supported_locale` VALUES (1,'en','AU','English (Australia)','LTR','en-au'),(2,'es','ES','Español','LTR','es'),(3,'mi','NZ','Māori','LTR','en-au'),(4,'de','DE','Deutsch','LTR','de'),(5,'zh','CN','简体中文','LTR','zh-cn'),(6,'fr','FR','Français','LTR','fr'),(7,'it','IT','Italiano','LTR','it'),(8,'no','NO','Norsk','LTR','no'),(9,'sv','SE','Svenska','LTR','sv'),(10,'ko','KR','한국어','LTR','ko'),(11,'pl','PL','Polski','LTR','pl'),(12,'pt','BR','Português (Brasil)','LTR','pt-br'),(13,'hu','HU','Magyar','LTR','hu'),(14,'bg','BG','Български','LTR','bg'),(15,'cy','GB','Cymraeg (Cymru)','LTR','en-au'),(16,'th','TH','Thai','LTR','th'),(17,'el','GR','Ελληνικά','LTR','el'),(18,'nl','BE','Nederlands (België)','LTR','nl'),(19,'ar','JO','عربي','RTL','ar'),(20,'da','DK','Dansk','LTR','da'),(21,'ru','RU','Русский','LTR','ru'),(22,'vi','VN','Tiếng Việt','LTR','vi'),(23,'zh','TW','Chinese (Taiwan)','LTR','zh'),(24,'ja','JP','日本語','LTR','ja'),(25,'ms','MY','Malay (Malaysia)','LTR','ms'),(26,'tr','TR','Türkçe','LTR','tr'),(27,'ca','ES','Català','LTR','ca');
/*!40000 ALTER TABLE `lams_supported_locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_system_tool`
--

DROP TABLE IF EXISTS `lams_system_tool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_system_tool` (
  `system_tool_id` bigint(20) NOT NULL AUTO_INCREMENT,
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
  `pedagogical_planner_url` text,
  PRIMARY KEY (`system_tool_id`),
  UNIQUE KEY `UQ_systool_activity_type` (`learning_activity_type_id`),
  KEY `learning_activity_type_id` (`learning_activity_type_id`),
  CONSTRAINT `FK_lams_system_tool` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_system_tool`
--

LOCK TABLES `lams_system_tool` WRITE;
/*!40000 ALTER TABLE `lams_system_tool` DISABLE KEYS */;
INSERT INTO `lams_system_tool` VALUES (1,2,'Grouping','All types of grouping including random and chosen.','learning/grouping.do?method=performGrouping','learning/grouping.do?method=performGrouping','learning/grouping.do?method=viewGrouping&mode=teacher','learning/groupingExportPortfolio?mode=learner','learning/groupingExportPortfolio?mode=teacher','monitoring/grouping.do?method=startGrouping','monitoring/grouping.do?method=startGrouping',NULL,'2010-04-01 15:55:04',NULL,'pedagogicalPlanner/grouping.do?method=initGrouping'),(2,3,'Sync Gate','Gate: Synchronise Learners.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2010-04-01 15:55:04',NULL,NULL),(3,4,'Schedule Gate','Gate: Opens/shuts at particular times.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2010-04-01 15:55:04',NULL,NULL),(4,5,'Permission Gate','Gate: Opens under teacher or staff control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2010-04-01 15:55:04',NULL,NULL),(5,9,'System Gate','Gate: Opens under system control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2010-04-01 15:55:04',NULL,NULL),(6,10,'Monitor Chosen Branching','Select between multiple sequence activities, with the branch chosen in monitoring.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/chosenBranching.do?method=assignBranch','monitoring/chosenBranching.do?method=assignBranch',NULL,'2010-04-01 15:55:04',NULL,NULL),(7,11,'Group Based Branching','Select between multiple sequence activities, with the branch chosen by an existing group.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/groupedBranching.do?method=viewBranching','monitoring/groupedBranching.do?method=assignBranch',NULL,'2010-04-01 15:55:04',NULL,NULL),(8,12,'Tool Output Based Branching','Select between multiple sequence activities, with the branch chosen on results of another activity.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/toolBranching.do?method=viewBranching','monitoring/toolBranching.do?method=viewBranching',NULL,'2010-04-01 15:55:04',NULL,NULL),(9,8,'Sequence','A sequence of activities','learning/SequenceActivity.do','learning/SequenceActivity.do','monitoring/complexProgress.do',NULL,'monitoring/sequenceExportPortfolio?mode=teacher','monitoring/sequence.do?method=viewSequence','monitoring/sequence.do?method=viewSequence',NULL,'2010-04-01 15:55:04',NULL,NULL),(10,14,'Condition Gate','Gate: Opens if conditions are met','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2010-04-01 15:55:04',NULL,NULL),(11,15,'Floating Activities','A collection of floating activities',NULL,NULL,NULL,'monitoring/floatingExportPortfolio?mode=learner','monitoring/floatingExportPortfolio?mode=teacher','monitoring/floating.do?method=viewFloating','monitoring/floating.do?method=viewFloating',NULL,'2010-04-01 15:55:04',NULL,NULL);
/*!40000 ALTER TABLE `lams_system_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_text_search_condition`
--

DROP TABLE IF EXISTS `lams_text_search_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_text_search_condition` (
  `condition_id` bigint(20) NOT NULL,
  `text_search_all_words` text,
  `text_search_phrase` text,
  `text_search_any_words` text,
  `text_search_excluded_words` text,
  PRIMARY KEY (`condition_id`),
  CONSTRAINT `TextSearchConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_text_search_condition`
--

LOCK TABLES `lams_text_search_condition` WRITE;
/*!40000 ALTER TABLE `lams_text_search_condition` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_text_search_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_theme`
--

DROP TABLE IF EXISTS `lams_theme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_theme` (
  `theme_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `image_directory` varchar(100) DEFAULT NULL,
  `theme_type` tinyint(11) DEFAULT NULL,
  PRIMARY KEY (`theme_id`),
  UNIQUE KEY `UQ_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_theme`
--

LOCK TABLES `lams_theme` WRITE;
/*!40000 ALTER TABLE `lams_theme` DISABLE KEYS */;
INSERT INTO `lams_theme` VALUES (1,'default','Default Flash style',NULL,2),(2,'defaultHTML','Default HTML style','css',1),(3,'highContrast','High contrast HTML theme','css',1);
/*!40000 ALTER TABLE `lams_theme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool`
--

DROP TABLE IF EXISTS `lams_tool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool` (
  `tool_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_signature` varchar(64) NOT NULL,
  `service_name` varchar(255) NOT NULL,
  `tool_display_name` varchar(255) NOT NULL,
  `description` text,
  `tool_identifier` varchar(64) NOT NULL,
  `tool_version` varchar(10) NOT NULL,
  `learning_library_id` bigint(20) DEFAULT NULL,
  `default_tool_content_id` bigint(20) DEFAULT NULL,
  `valid_flag` tinyint(1) NOT NULL DEFAULT '1',
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
  `pedagogical_planner_url` text,
  `help_url` text,
  `create_date_time` datetime NOT NULL,
  `language_file` varchar(255) DEFAULT NULL,
  `modified_date_time` datetime DEFAULT NULL,
  `admin_url` text,
  `supports_outputs` tinyint(1) DEFAULT '0',
  `ext_lms_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tool_id`),
  UNIQUE KEY `UQ_lams_tool_sig` (`tool_signature`),
  UNIQUE KEY `UQ_lams_tool_class_name` (`service_name`),
  KEY `learning_library_id` (`learning_library_id`),
  KEY `grouping_support_type_id` (`grouping_support_type_id`),
  CONSTRAINT `FK_lams_tool_1` FOREIGN KEY (`learning_library_id`) REFERENCES `lams_learning_library` (`learning_library_id`),
  CONSTRAINT `FK_lams_tool_2` FOREIGN KEY (`grouping_support_type_id`) REFERENCES `lams_grouping_support_type` (`grouping_support_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_tool`
--

LOCK TABLES `lams_tool` WRITE;
/*!40000 ALTER TABLE `lams_tool` DISABLE KEYS */;
INSERT INTO `lams_tool` VALUES (1,'lafrum11','forumService','Forum','Forum / Message Boards','forum','20081118',1,1,1,2,1,'tool/lafrum11/learning/viewForum.do?mode=learner','tool/lafrum11/learning/viewForum.do?mode=author','tool/lafrum11/learning/viewForum.do?mode=teacher','tool/lafrum11/authoring.do','tool/lafrum11/defineLater.do','tool/lafrum11/exportPortfolio?mode=learner','tool/lafrum11/exportPortfolio?mode=teacher','tool/lafrum11/monitoring.do','tool/lafrum11/contribute.do','tool/lafrum11/moderate.do','tool/lafrum11/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/lafrum11','2010-04-01 15:56:16','org.lamsfoundation.lams.tool.forum.ApplicationResources','2010-04-01 15:56:16',NULL,1,NULL),(2,'lanb11','nbService','NoticeboardX','Displays a NoticeboardX','nb','20081209',2,2,1,2,1,'tool/lanb11/starter/learner.do?mode=learner','tool/lanb11/starter/learner.do?mode=author','tool/lanb11/starter/learner.do?mode=teacher','tool/lanb11/authoring.do','tool/lanb11/authoring.do?defineLater=true','tool/lanb11/portfolioExport?mode=learner','tool/lanb11/portfolioExport?mode=teacher','tool/lanb11/monitoring.do',NULL,NULL,'tool/lanb11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lanb11','2010-04-01 15:56:24','org.lamsfoundation.lams.tool.noticeboard.ApplicationResources','2010-04-01 15:56:24',NULL,0,NULL),(3,'laqa11','qaService','Question and Answer','Q/A Tool','qa','20081126',3,3,1,2,1,'tool/laqa11/learningStarter.do?mode=learner','tool/laqa11/learningStarter.do?mode=author','tool/laqa11/learningStarter.do?mode=teacher','tool/laqa11/authoringStarter.do','tool/laqa11/defineLaterStarter.do','tool/laqa11/exportPortfolio?mode=learner','tool/laqa11/exportPortfolio?mode=teacher','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','tool/laqa11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/laqa11','2010-04-01 15:56:30','org.lamsfoundation.lams.tool.qa.ApplicationResources','2010-04-01 15:56:30','tool/laqa11/laqa11admin.do',1,NULL),(4,'lasbmt11','submitFilesService','Submit File','Submit File Tool Description','submitfile','20091028',4,4,1,2,1,'tool/lasbmt11/learner.do?mode=learner','tool/lasbmt11/learner.do?mode=author','tool/lasbmt11/learner.do?mode=teacher','tool/lasbmt11/authoring.do','tool/lasbmt11/definelater.do?mode=teacher','tool/lasbmt11/exportPortfolio?mode=learner','tool/lasbmt11/exportPortfolio?mode=teacher','tool/lasbmt11/monitoring.do','tool/lasbmt11/contribute.do','tool/lasbmt11/moderation.do','tool/lasbmt11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lasbmt11','2010-04-01 15:56:35','org.lamsfoundation.lams.tool.sbmt.ApplicationResources','2010-04-01 15:56:35',NULL,0,NULL),(5,'lachat11','chatService','Chat','Chat','chat','20081125',5,5,1,2,1,'tool/lachat11/learning.do?mode=learner','tool/lachat11/learning.do?mode=author','tool/lachat11/learning.do?mode=teacher','tool/lachat11/authoring.do','tool/lachat11/authoring.do?mode=teacher','tool/lachat11/exportPortfolio?mode=learner','tool/lachat11/exportPortfolio?mode=teacher','tool/lachat11/monitoring.do','tool/lachat11/contribute.do','tool/lachat11/moderate.do','tool/lachat11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lachat11','2010-04-01 15:56:41','org.lamsfoundation.lams.tool.chat.ApplicationResources','2010-04-01 15:56:41',NULL,1,NULL),(6,'larsrc11','resourceService','Shared Resources','Shared Resources','sharedresources','20090115',6,6,1,2,1,'tool/larsrc11/learning/start.do?mode=learner','tool/larsrc11/learning/start.do?mode=author','tool/larsrc11/learning/start.do?mode=teacher','tool/larsrc11/authoring/start.do','tool/larsrc11/definelater.do','tool/larsrc11/exportPortfolio?mode=learner','tool/larsrc11/exportPortfolio?mode=teacher','tool/larsrc11/monitoring/summary.do','tool/larsrc11/contribute.do','tool/larsrc11/moderate.do','tool/larsrc11/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11','2010-04-01 15:56:47','org.lamsfoundation.lams.tool.rsrc.ApplicationResources','2010-04-01 15:56:47',NULL,0,NULL),(7,'lavote11','voteService','Voting','Voting','vote','20081203',7,7,1,2,1,'tool/lavote11/learningStarter.do?mode=learner','tool/lavote11/learningStarter.do?mode=author','tool/lavote11/learningStarter.do?mode=teacher','tool/lavote11/authoringStarter.do','tool/lavote11/defineLaterStarter.do','tool/lavote11/exportPortfolio?mode=learner','tool/lavote11/exportPortfolio?mode=teacher','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','tool/lavote11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lavote11','2010-04-01 15:56:53','org.lamsfoundation.lams.tool.vote.ApplicationResources','2010-04-01 15:56:53',NULL,1,NULL),(8,'lantbk11','notebookService','Notebook','Notebook','notebook','20081118',8,8,1,2,1,'tool/lantbk11/learning.do?mode=learner','tool/lantbk11/learning.do?mode=author','tool/lantbk11/learning.do?mode=teacher','tool/lantbk11/authoring.do','tool/lantbk11/authoring.do?mode=teacher','tool/lantbk11/exportPortfolio?mode=learner','tool/lantbk11/exportPortfolio?mode=teacher','tool/lantbk11/monitoring.do','tool/lantbk11/contribute.do','tool/lantbk11/moderate.do','tool/lantbk11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lantbk11','2010-04-01 15:56:58','org.lamsfoundation.lams.tool.notebook.ApplicationResources','2010-04-01 15:56:58',NULL,1,NULL),(9,'lasurv11','lasurvSurveyService','Survey','Survey','survey','20081021',9,9,1,2,1,'tool/lasurv11/learning/start.do?mode=learner','tool/lasurv11/learning/start.do?mode=author','tool/lasurv11/learning/start.do?mode=teacher','tool/lasurv11/authoring/start.do','tool/lasurv11/definelater.do','tool/lasurv11/exportPortfolio?mode=learner','tool/lasurv11/exportPortfolio?mode=teacher','tool/lasurv11/monitoring/summary.do','tool/lasurv11/contribute.do','tool/lasurv11/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lasurv11','2010-04-01 15:57:03','org.lamsfoundation.lams.tool.survey.ApplicationResources','2010-04-01 15:57:03',NULL,1,NULL),(10,'lascrb11','lascrbScribeService','Scribe','Scribe','scribe','20090226',10,10,1,2,1,'tool/lascrb11/learning.do?mode=learner','tool/lascrb11/learning.do?mode=author','tool/lascrb11/learning.do?mode=teacher','tool/lascrb11/authoring.do','tool/lascrb11/authoring.do?mode=teacher','tool/lascrb11/exportPortfolio?mode=learner','tool/lascrb11/exportPortfolio?mode=teacher','tool/lascrb11/monitoring.do','tool/lascrb11/contribute.do','tool/lascrb11/moderate.do','tool/lascrb11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lascrb11','2010-04-01 15:57:08','org.lamsfoundation.lams.tool.scribe.ApplicationResources','2010-04-01 15:57:08',NULL,0,NULL),(11,'latask10','lataskTaskListService','Shared TaskList','Shared TaskList','sharedtaskList','20090113',11,11,1,2,1,'tool/latask10/learning/start.do?mode=learner','tool/latask10/learning/start.do?mode=author','tool/latask10/learning/start.do?mode=teacher','tool/latask10/authoring/start.do','tool/latask10/definelater.do','tool/latask10/exportPortfolio?mode=learner','tool/latask10/exportPortfolio?mode=teacher','tool/latask10/monitoring/summary.do','tool/latask10/contribute.do','tool/latask10/moderate.do','tool/latask10/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/latask10','2010-04-01 15:57:15','org.lamsfoundation.lams.tool.taskList.ApplicationResources','2010-04-01 15:57:15',NULL,1,NULL),(12,'lagmap10','gmapService','Gmap','Gmap','gmap','20080521',12,12,1,2,1,'tool/lagmap10/learning.do?mode=learner','tool/lagmap10/learning.do?mode=author','tool/lagmap10/learning.do?mode=teacher','tool/lagmap10/authoring.do','tool/lagmap10/authoring.do?mode=teacher','tool/lagmap10/exportPortfolio?mode=learner','tool/lagmap10/exportPortfolio?mode=teacher','tool/lagmap10/monitoring.do','tool/lagmap10/contribute.do','tool/lagmap10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lagmap10','2010-04-01 15:57:22','org.lamsfoundation.lams.tool.gmap.ApplicationResources','2010-04-01 15:57:22','tool/lagmap10/lagmap10admin.do',0,NULL),(13,'lasprd10','spreadsheetService','Spreadsheet Tool','Spreadsheet Tool','spreadsheet','20080612',13,13,1,2,1,'tool/lasprd10/learning/start.do?mode=learner','tool/lasprd10/learning/start.do?mode=author','tool/lasprd10/learning/start.do?mode=teacher','tool/lasprd10/authoring/start.do','tool/lasprd10/definelater.do','tool/lasprd10/exportPortfolio?mode=learner','tool/lasprd10/exportPortfolio?mode=teacher','tool/lasprd10/monitoring/summary.do','tool/lasprd10/contribute.do','tool/lasprd10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lasprd10','2010-04-01 15:57:27','org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources','2010-04-01 15:57:27',NULL,0,NULL),(14,'ladaco10','dacoService','Data Collection','Collecting data with custom structure.','daco','20090326',14,14,1,2,1,'tool/ladaco10/learning/start.do?mode=learner','tool/ladaco10/learning/start.do?mode=author','tool/ladaco10/learning/start.do?mode=teacher','tool/ladaco10/authoring/start.do','tool/ladaco10/definelater.do','tool/ladaco10/exportPortfolio?mode=learner','tool/ladaco10/exportPortfolio?mode=teacher','tool/ladaco10/monitoring/summary.do','tool/ladaco10/contribute.do','tool/ladaco10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/ladaco10','2010-04-01 15:57:33','org.lamsfoundation.lams.tool.daco.ApplicationResources','2010-04-01 15:57:33',NULL,1,NULL),(15,'lawiki10','wikiService','Wiki','Wiki','wiki','20090121',15,15,1,2,1,'tool/lawiki10/learning.do?mode=learner','tool/lawiki10/learning.do?mode=author','tool/lawiki10/learning.do?mode=teacher','tool/lawiki10/authoring.do','tool/lawiki10/authoring.do?mode=teacher','tool/lawiki10/exportPortfolio?mode=learner','tool/lawiki10/exportPortfolio?mode=teacher','tool/lawiki10/monitoring.do','tool/lawiki10/contribute.do','tool/lawiki10/moderate.do','tool/lawiki10/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10','2010-04-01 15:57:38','org.lamsfoundation.lams.tool.wiki.ApplicationResources','2010-04-01 15:57:38',NULL,1,NULL),(16,'laimag10','laimagImageGalleryService','ImageGallery','ImageGallery','imageGallery','20090819',16,16,1,2,1,'tool/laimag10/learning/start.do?mode=learner','tool/laimag10/learning/start.do?mode=author','tool/laimag10/learning/start.do?mode=teacher','tool/laimag10/authoring/start.do','tool/laimag10/definelater.do','tool/laimag10/exportPortfolio?mode=learner','tool/laimag10/exportPortfolio?mode=teacher','tool/laimag10/monitoring/summary.do','tool/laimag10/contribute.do','tool/laimag10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/laimag10','2010-04-01 15:57:45','org.lamsfoundation.lams.tool.imageGallery.ApplicationResources','2010-04-01 15:57:45','tool/laimag10/laimag10admin/start.do',1,NULL),(17,'laddim10','dimdimService','Dimdim','Dimdim','dimdim','20080811',17,17,1,2,1,'tool/laddim10/learning.do?mode=learner','tool/laddim10/learning.do?mode=author','tool/laddim10/learning.do?mode=teacher','tool/laddim10/authoring.do','tool/laddim10/authoring.do?mode=teacher','tool/laddim10/exportPortfolio?mode=learner','tool/laddim10/exportPortfolio?mode=teacher','tool/laddim10/monitoring.do','tool/laddim10/contribute.do','tool/laddim10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/laddim10','2010-04-01 15:57:52','org.lamsfoundation.lams.tool.dimdim.ApplicationResources','2010-04-01 15:57:52','tool/laddim10/admin/view.do',0,NULL),(18,'lavidr10','videoRecorderService','VideoRecorder','VideoRecorder','videoRecorder','20081208',18,18,1,2,1,'tool/lavidr10/learning.do?mode=learner','tool/lavidr10/learning.do?mode=author','tool/lavidr10/learning.do?mode=teacher','tool/lavidr10/authoring.do','tool/lavidr10/authoring.do?mode=teacher','tool/lavidr10/exportPortfolio?mode=learner','tool/lavidr10/exportPortfolio?mode=teacher','tool/lavidr10/monitoring.do','tool/lavidr10/contribute.do','tool/lavidr10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lavidr10','2010-04-01 15:57:58','org.lamsfoundation.lams.tool.videoRecorder.ApplicationResources','2010-04-01 15:57:58',NULL,1,NULL),(19,'lamind10','mindmapService','Mindmap','Mindmap','mindmap','20090202',19,19,1,2,1,'tool/lamind10/learning.do?mode=learner','tool/lamind10/learning.do?mode=author','tool/lamind10/learning.do?mode=teacher','tool/lamind10/authoring.do','tool/lamind10/authoring.do?mode=teacher','tool/lamind10/exportPortfolio?mode=learner','tool/lamind10/exportPortfolio?mode=teacher','tool/lamind10/monitoring.do','tool/lamind10/contribute.do','tool/lamind10/moderate.do','tool/lamind10/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lamind10','2010-04-01 15:58:03','org.lamsfoundation.lams.tool.mindmap.ApplicationResources','2010-04-01 15:58:03',NULL,1,NULL),(20,'laasse10','laasseAssessmentService','Assessment','Assessment','assessment','20100225',20,20,1,2,1,'tool/laasse10/learning/start.do?mode=learner','tool/laasse10/learning/start.do?mode=author','tool/laasse10/learning/start.do?mode=teacher','tool/laasse10/authoring/start.do','tool/laasse10/definelater.do','tool/laasse10/exportPortfolio?mode=learner','tool/laasse10/exportPortfolio?mode=teacher','tool/laasse10/monitoring/summary.do','tool/laasse10/contribute.do','tool/laasse10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/laasse10','2010-04-01 15:58:09','org.lamsfoundation.lams.tool.assessment.ApplicationResources','2010-04-01 15:58:09',NULL,1,NULL),(21,'lapixl10','pixlrService','Pixlr','Pixlr','pixlr','20081217',21,21,1,2,1,'tool/lapixl10/learning.do?mode=learner','tool/lapixl10/learning.do?mode=author','tool/lapixl10/learning.do?mode=teacher','tool/lapixl10/authoring.do','tool/lapixl10/authoring.do?mode=teacher','tool/lapixl10/exportPortfolio?mode=learner','tool/lapixl10/exportPortfolio?mode=teacher','tool/lapixl10/monitoring.do','tool/lapixl10/contribute.do','tool/lapixl10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lapixl10','2010-04-01 15:58:17','org.lamsfoundation.lams.tool.pixlr.ApplicationResources','2010-04-01 15:58:17','tool/lapixl10/lapixl10admin.do',0,NULL),(22,'lamc11','mcService','MCQ','Multiple Choice Questions','mc','20081127',22,22,1,2,1,'tool/lamc11/learningStarter.do?mode=learner','tool/lamc11/learningStarter.do?mode=author','tool/lamc11/learningStarter.do?mode=teacher','tool/lamc11/authoringStarter.do','tool/lamc11/defineLaterStarter.do','tool/lamc11/exportPortfolio?mode=learner','tool/lamc11/exportPortfolio?mode=teacher','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','tool/lamc11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lamc11','2010-04-01 15:58:21','org.lamsfoundation.lams.tool.mc.ApplicationResources','2010-04-01 15:58:21',NULL,1,NULL);
/*!40000 ALTER TABLE `lams_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_content`
--

DROP TABLE IF EXISTS `lams_tool_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool_content` (
  `tool_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_id` bigint(20) NOT NULL,
  PRIMARY KEY (`tool_content_id`),
  KEY `tool_id` (`tool_id`),
  CONSTRAINT `FK_lams_tool_content_1` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_tool_content`
--

LOCK TABLES `lams_tool_content` WRITE;
/*!40000 ALTER TABLE `lams_tool_content` DISABLE KEYS */;
INSERT INTO `lams_tool_content` VALUES (1,1),(28,1),(30,1),(47,1),(73,1),(77,1),(89,1),(2,2),(23,2),(24,2),(33,2),(34,2),(35,2),(37,2),(38,2),(40,2),(41,2),(42,2),(45,2),(51,2),(52,2),(56,2),(58,2),(59,2),(60,2),(65,2),(67,2),(68,2),(70,2),(74,2),(79,2),(83,2),(84,2),(86,2),(87,2),(92,2),(93,2),(94,2),(96,2),(98,2),(99,2),(100,2),(106,2),(109,2),(112,2),(114,2),(115,2),(117,2),(119,2),(120,2),(3,3),(32,3),(46,3),(76,3),(105,3),(4,4),(49,4),(97,4),(5,5),(25,5),(75,5),(6,6),(26,6),(27,6),(29,6),(31,6),(36,6),(43,6),(61,6),(62,6),(63,6),(64,6),(66,6),(69,6),(71,6),(72,6),(78,6),(80,6),(81,6),(82,6),(90,6),(107,6),(108,6),(110,6),(111,6),(113,6),(116,6),(118,6),(7,7),(50,7),(103,7),(8,8),(54,8),(101,8),(9,9),(48,9),(102,9),(10,10),(11,11),(12,12),(53,12),(91,12),(13,13),(39,13),(85,13),(14,14),(57,14),(104,14),(15,15),(55,15),(88,15),(16,16),(17,17),(18,18),(19,19),(20,20),(21,21),(22,22),(44,22),(95,22);
/*!40000 ALTER TABLE `lams_tool_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_import_support`
--

DROP TABLE IF EXISTS `lams_tool_import_support`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool_import_support` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `installed_tool_signature` varchar(15) NOT NULL,
  `supports_tool_signature` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool_session` (
  `tool_session_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_session_name` varchar(255) NOT NULL,
  `tool_session_type_id` int(3) NOT NULL,
  `lesson_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `tool_session_state_id` int(3) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `unique_key` varchar(128) NOT NULL,
  PRIMARY KEY (`tool_session_id`),
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
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_tool_session`
--

LOCK TABLES `lams_tool_session` WRITE;
/*!40000 ALTER TABLE `lams_tool_session` DISABLE KEYS */;
INSERT INTO `lams_tool_session` VALUES (1,'LAMS Demo Course learners',1,1,91,1,'2010-04-01 16:04:05',1,NULL,'uq_91_1'),(2,'LAMS Demo Course learners',1,1,92,1,'2010-04-01 16:04:05',1,NULL,'uq_92_1'),(3,'LAMS Demo Course learners',1,1,93,1,'2010-04-01 16:04:05',1,NULL,'uq_93_1'),(4,'LAMS Demo Course learners',1,1,94,1,'2010-04-01 16:04:05',1,NULL,'uq_94_1'),(5,'LAMS Demo Course learners',1,1,95,1,'2010-04-01 16:04:06',1,NULL,'uq_95_1'),(6,'LAMS Demo Course learners',1,1,96,1,'2010-04-01 16:04:06',1,NULL,'uq_96_1'),(7,'LAMS Demo Course learners',1,1,97,1,'2010-04-01 16:04:06',1,NULL,'uq_97_1'),(8,'LAMS Demo Course learners',1,1,98,1,'2010-04-01 16:04:06',1,NULL,'uq_98_1'),(9,'LAMS Demo Course learners',1,1,99,1,'2010-04-01 16:04:06',1,NULL,'uq_99_1'),(10,'LAMS Demo Course learners',1,1,100,1,'2010-04-01 16:04:06',1,NULL,'uq_100_1'),(11,'LAMS Demo Course learners',1,2,102,1,'2010-04-01 16:04:15',3,NULL,'uq_102_3'),(12,'LAMS Demo Course learners',1,2,101,1,'2010-04-01 16:04:15',3,NULL,'uq_101_3'),(13,'LAMS Demo Course learners',1,2,103,1,'2010-04-01 16:04:15',3,NULL,'uq_103_3'),(14,'LAMS Demo Course learners',1,2,104,1,'2010-04-01 16:04:15',3,NULL,'uq_104_3'),(15,'LAMS Demo Course learners',1,2,105,1,'2010-04-01 16:04:15',3,NULL,'uq_105_3'),(16,'LAMS Demo Course learners',1,2,106,1,'2010-04-01 16:04:15',3,NULL,'uq_106_3'),(17,'LAMS Demo Course learners',1,2,109,1,'2010-04-01 16:04:15',3,NULL,'uq_109_3'),(18,'LAMS Demo Course learners',1,2,125,1,'2010-04-01 16:04:15',3,NULL,'uq_125_3'),(19,'LAMS Demo Course learners',1,2,127,1,'2010-04-01 16:04:15',3,NULL,'uq_127_3'),(20,'LAMS Demo Course learners',1,2,108,1,'2010-04-01 16:04:15',3,NULL,'uq_108_3'),(21,'LAMS Demo Course learners',1,2,113,1,'2010-04-01 16:04:15',3,NULL,'uq_113_3'),(22,'LAMS Demo Course learners',1,2,129,1,'2010-04-01 16:04:15',3,NULL,'uq_129_3'),(23,'LAMS Demo Course learners',1,2,123,1,'2010-04-01 16:04:15',3,NULL,'uq_123_3'),(24,'LAMS Demo Course learners',1,2,124,1,'2010-04-01 16:04:15',3,NULL,'uq_124_3'),(25,'LAMS Demo Course learners',1,2,114,1,'2010-04-01 16:04:15',3,NULL,'uq_114_3'),(26,'LAMS Demo Course learners',1,2,122,1,'2010-04-01 16:04:15',3,NULL,'uq_122_3'),(27,'LAMS Demo Course learners',1,2,120,1,'2010-04-01 16:04:15',3,NULL,'uq_120_3'),(28,'LAMS Demo Course learners',1,2,121,1,'2010-04-01 16:04:15',3,NULL,'uq_121_3'),(29,'LAMS Demo Course learners',1,2,111,1,'2010-04-01 16:04:15',3,NULL,'uq_111_3'),(30,'LAMS Demo Course learners',1,2,110,1,'2010-04-01 16:04:15',3,NULL,'uq_110_3'),(31,'LAMS Demo Course learners',1,2,115,1,'2010-04-01 16:04:15',3,NULL,'uq_115_3'),(32,'LAMS Demo Course learners',1,2,116,1,'2010-04-01 16:04:15',3,NULL,'uq_116_3'),(33,'LAMS Demo Course learners',1,2,117,1,'2010-04-01 16:04:15',3,NULL,'uq_117_3'),(34,'LAMS Demo Course learners',1,2,119,1,'2010-04-01 16:04:15',3,NULL,'uq_119_3'),(35,'LAMS Demo Course learners',1,2,118,1,'2010-04-01 16:04:16',3,NULL,'uq_118_3'),(36,'LAMS Demo Course learners',1,3,132,1,'2010-04-01 16:04:27',5,NULL,'uq_132_5'),(37,'LAMS Demo Course learners',1,3,137,1,'2010-04-01 16:04:27',5,NULL,'uq_137_5'),(38,'LAMS Demo Course learners',1,3,140,1,'2010-04-01 16:04:27',5,NULL,'uq_140_5'),(39,'LAMS Demo Course learners',1,3,141,1,'2010-04-01 16:04:27',5,NULL,'uq_141_5'),(40,'LAMS Demo Course learners',1,3,131,1,'2010-04-01 16:04:27',5,NULL,'uq_131_5'),(41,'LAMS Demo Course learners',1,3,133,1,'2010-04-01 16:04:27',5,NULL,'uq_133_5'),(42,'LAMS Demo Course learners',1,3,136,1,'2010-04-01 16:04:27',5,NULL,'uq_136_5'),(43,'LAMS Demo Course learners',1,3,135,1,'2010-04-01 16:04:27',5,NULL,'uq_135_5'),(44,'LAMS Demo Course learners',1,3,138,1,'2010-04-01 16:04:27',5,NULL,'uq_138_5'),(45,'LAMS Demo Course learners',1,4,145,1,'2010-04-01 16:05:08',7,NULL,'uq_145_7'),(46,'LAMS Demo Course learners',1,4,146,1,'2010-04-01 16:05:08',7,NULL,'uq_146_7'),(47,'LAMS Demo Course learners',1,4,147,1,'2010-04-01 16:05:08',7,NULL,'uq_147_7'),(48,'LAMS Demo Course learners',1,4,148,1,'2010-04-01 16:05:08',7,NULL,'uq_148_7'),(49,'LAMS Demo Course learners',1,4,149,1,'2010-04-01 16:05:08',7,NULL,'uq_149_7');
/*!40000 ALTER TABLE `lams_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_session_state`
--

DROP TABLE IF EXISTS `lams_tool_session_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool_session_state` (
  `tool_session_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`tool_session_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_tool_session_type` (
  `tool_session_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`tool_session_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `password` varchar(50) NOT NULL,
  `title` varchar(32) DEFAULT NULL,
  `first_name` varchar(128) DEFAULT NULL,
  `last_name` varchar(128) DEFAULT NULL,
  `address_line_1` varchar(64) DEFAULT NULL,
  `address_line_2` varchar(64) DEFAULT NULL,
  `address_line_3` varchar(64) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `state` varchar(64) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `country` varchar(64) DEFAULT NULL,
  `day_phone` varchar(64) DEFAULT NULL,
  `evening_phone` varchar(64) DEFAULT NULL,
  `mobile_phone` varchar(64) DEFAULT NULL,
  `fax` varchar(64) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `disabled_flag` tinyint(1) NOT NULL DEFAULT '0',
  `create_date` datetime NOT NULL,
  `authentication_method_id` bigint(20) NOT NULL DEFAULT '0',
  `workspace_id` bigint(20) DEFAULT NULL,
  `flash_theme_id` bigint(20) DEFAULT NULL,
  `html_theme_id` bigint(20) DEFAULT NULL,
  `chat_id` varchar(255) DEFAULT NULL COMMENT 'ID used for Jabber',
  `locale_id` int(11) DEFAULT NULL,
  `portrait_uuid` bigint(20) DEFAULT NULL,
  `change_password` tinyint(4) DEFAULT '0',
  `enable_flash` tinyint(1) DEFAULT '1',
  `lams_community_token` varchar(255) DEFAULT NULL,
  `lams_community_username` varchar(255) DEFAULT NULL,
  `timezone` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
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
  CONSTRAINT `FK_lams_user_4` FOREIGN KEY (`flash_theme_id`) REFERENCES `lams_theme` (`theme_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_5` FOREIGN KEY (`html_theme_id`) REFERENCES `lams_theme` (`theme_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_6` FOREIGN KEY (`locale_id`) REFERENCES `lams_supported_locale` (`locale_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (1,'sysadmin','a159b7ae81ba3552af61e9731b20870515944538','The','System','Administrator',NULL,NULL,NULL,'Sydney','NSW',NULL,'Australia',NULL,NULL,NULL,NULL,'sysadmin@x.x',0,'2010-04-01 15:55:04',1,51,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(2,'test','a94a8fe5ccb19ba61c4c0873d391e987982fbbd3','Dr','Testing','LDAP',NULL,NULL,NULL,'Sydney','NSW',NULL,'Australia',NULL,NULL,NULL,NULL,'test@xx.xx.xx',0,'2010-04-01 15:55:04',3,NULL,1,2,NULL,3,NULL,0,1,NULL,NULL,28),(3,'lamskh01','829c3804401b0727f70f73d4415e162400cbe57b','Mr','Jacky','Fang',NULL,NULL,NULL,'Sydney','NSW',NULL,'Australia',NULL,NULL,NULL,NULL,'jfang@melcoe.mq.edu.au',0,'2010-04-01 15:55:04',2,NULL,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(4,'mmm','91223fd10ce86fc852b449583aa2196c304bf6e0','Ms','Mary','Morgan','99','First Ave',NULL,'Parramatta','NSW',NULL,'Australia','0295099999','0298939999','0499999999','0299999999','mmmmmmm@xx.os',0,'2004-12-23 00:00:00',1,4,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(5,'test1','b444ac06613fc8d63795be9ad0beaf55011936ac','Dr','One','Test','1','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test1@xx.os',0,'2004-12-23 00:00:00',1,5,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(6,'test2','109f4b3c50d7b0df729d299bc6f8e9ef9066971f','Dr','Two','Test','2','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test2@xx.os',0,'2004-12-23 00:00:00',1,6,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(7,'test3','3ebfa301dc59196f18593c45e519287a23297589','Dr','Three','Test','3','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test3@xx.os',0,'2004-12-23 00:00:00',1,7,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(8,'test4','1ff2b3704aede04eecb51e50ca698efd50a1379b','Dr','Four','Test','4','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test4@xx.os',0,'2004-12-23 00:00:00',1,8,1,2,NULL,1,NULL,0,1,NULL,NULL,28);
/*!40000 ALTER TABLE `lams_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_group`
--

DROP TABLE IF EXISTS `lams_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`group_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_user_group_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_user_group_2` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_group`
--

LOCK TABLES `lams_user_group` WRITE;
/*!40000 ALTER TABLE `lams_user_group` DISABLE KEYS */;
INSERT INTO `lams_user_group` VALUES (2,1),(2,3),(2,5),(2,7),(3,1),(3,3),(3,5),(3,7),(4,1),(4,3),(4,5),(4,7),(5,1),(5,2),(5,3),(5,4),(5,5),(5,6),(5,7),(5,8),(6,1),(6,3),(6,5),(6,7),(6,8),(7,1),(7,3),(7,5),(7,7),(7,8),(8,1),(8,3),(8,5),(8,7),(8,8);
/*!40000 ALTER TABLE `lams_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation`
--

DROP TABLE IF EXISTS `lams_user_organisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_user_organisation` (
  `user_organisation_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `organisation_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_organisation_id`),
  KEY `organisation_id` (`organisation_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_user_organisation_1` FOREIGN KEY (`organisation_id`) REFERENCES `lams_organisation` (`organisation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_organisation_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_organisation`
--

LOCK TABLES `lams_user_organisation` WRITE;
/*!40000 ALTER TABLE `lams_user_organisation` DISABLE KEYS */;
INSERT INTO `lams_user_organisation` VALUES (1,1,1),(2,2,2),(3,2,3),(4,2,4),(5,2,5),(6,2,6),(7,2,7),(8,2,8),(9,3,2),(10,3,3),(11,3,4),(12,3,5),(13,3,6),(14,3,7),(15,3,8),(16,4,2),(17,4,3),(18,4,4),(19,4,5),(20,4,6),(21,4,7),(22,4,8),(23,5,2),(24,5,3),(25,5,4),(26,5,5),(27,5,6),(28,6,7),(29,6,8);
/*!40000 ALTER TABLE `lams_user_organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation_collapsed`
--

DROP TABLE IF EXISTS `lams_user_organisation_collapsed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_user_organisation_collapsed` (
  `user_organisation_id` bigint(20) NOT NULL,
  `collapsed` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_1` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_user_organisation_role` (
  `user_organisation_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_organisation_id` bigint(20) NOT NULL,
  `role_id` int(6) NOT NULL,
  PRIMARY KEY (`user_organisation_role_id`),
  KEY `role_id` (`role_id`),
  KEY `user_organisation_id` (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_role_2` FOREIGN KEY (`role_id`) REFERENCES `lams_role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_user_organisation_role_3` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_user_organisation_role`
--

LOCK TABLES `lams_user_organisation_role` WRITE;
/*!40000 ALTER TABLE `lams_user_organisation_role` DISABLE KEYS */;
INSERT INTO `lams_user_organisation_role` VALUES (1,1,1),(2,2,3),(3,3,3),(4,4,3),(5,5,3),(6,6,3),(7,7,3),(8,8,3),(9,2,4),(10,3,4),(11,4,4),(12,5,4),(13,6,4),(14,7,4),(15,8,4),(16,2,5),(17,3,5),(18,4,5),(19,5,5),(20,6,5),(21,7,5),(22,8,5),(23,9,4),(24,10,4),(25,11,4),(26,12,4),(27,13,4),(28,14,4),(29,16,4),(30,9,5),(31,10,5),(32,11,5),(33,12,5),(34,13,5),(35,14,5),(36,15,5),(37,16,3),(38,17,3),(39,18,3),(40,19,3),(41,20,3),(42,21,3),(43,22,3),(44,16,4),(45,17,4),(46,18,4),(47,19,4),(48,20,4),(49,21,4),(50,22,4),(51,16,5),(52,17,5),(53,18,5),(54,19,5),(55,20,5),(56,21,5),(57,22,5),(58,23,4),(59,24,4),(60,25,4),(61,26,4),(62,27,4),(63,28,4),(64,29,4),(65,23,5),(66,24,5),(67,25,5),(68,26,5),(69,27,5),(70,28,5),(71,29,5);
/*!40000 ALTER TABLE `lams_user_organisation_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_wkspc_fld_content_type`
--

DROP TABLE IF EXISTS `lams_wkspc_fld_content_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_wkspc_fld_content_type` (
  `content_type_id` int(3) NOT NULL AUTO_INCREMENT,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY (`content_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_wkspc_wkspc_folder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `workspace_id` bigint(20) DEFAULT NULL,
  `workspace_folder_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_ww_folder_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_workspace` (`workspace_id`),
  CONSTRAINT `FK_lams_ww_folder_2` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_wkspc_wkspc_folder`
--

LOCK TABLES `lams_wkspc_wkspc_folder` WRITE;
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` DISABLE KEYS */;
INSERT INTO `lams_wkspc_wkspc_folder` VALUES (1,1,1),(2,2,2),(3,2,22),(4,3,3),(5,3,23),(6,4,4),(7,5,5),(8,6,6),(9,7,7),(10,8,8),(11,50,40),(12,51,45),(13,52,46);
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace`
--

DROP TABLE IF EXISTS `lams_workspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_workspace` (
  `workspace_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `default_fld_id` bigint(20) DEFAULT NULL,
  `def_run_seq_fld_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`workspace_id`),
  KEY `def_run_seq_fld_id` (`def_run_seq_fld_id`),
  KEY `default_fld_id` (`default_fld_id`),
  CONSTRAINT `FK_lams_workspace_1` FOREIGN KEY (`def_run_seq_fld_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_2` FOREIGN KEY (`default_fld_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_workspace`
--

LOCK TABLES `lams_workspace` WRITE;
/*!40000 ALTER TABLE `lams_workspace` DISABLE KEYS */;
INSERT INTO `lams_workspace` VALUES (1,1,NULL,'ROOT'),(2,2,22,'Developers Playpen'),(3,3,23,'LAMS Demo Course'),(4,4,NULL,'Mary Morgan'),(5,5,NULL,'One Test'),(6,6,NULL,'Two Test'),(7,7,NULL,'Three Test'),(8,8,NULL,'Four Test'),(50,40,41,'Moodle Test'),(51,45,NULL,'System Administrator'),(52,46,NULL,'Public Folder');
/*!40000 ALTER TABLE `lams_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder`
--

DROP TABLE IF EXISTS `lams_workspace_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_workspace_folder` (
  `workspace_folder_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_folder_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date_time` datetime DEFAULT NULL,
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  PRIMARY KEY (`workspace_folder_id`),
  KEY `parent_folder_id` (`parent_folder_id`),
  KEY `lams_workspace_folder_type_id` (`lams_workspace_folder_type_id`),
  CONSTRAINT `FK_lams_workspace_folder_2` FOREIGN KEY (`parent_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_workspace_folder_4` FOREIGN KEY (`lams_workspace_folder_type_id`) REFERENCES `lams_workspace_folder_type` (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_workspace_folder`
--

LOCK TABLES `lams_workspace_folder` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder` DISABLE KEYS */;
INSERT INTO `lams_workspace_folder` VALUES (1,NULL,'ROOT',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(2,1,'Developers Playpen',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(3,1,'LAMS Demo Course',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(4,NULL,'Mary Morgan',4,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(5,NULL,'One Test',5,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(6,NULL,'Two Test',6,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(7,NULL,'Three Test',7,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(8,NULL,'Four Test',8,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(22,2,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(23,3,'LAMS Demo Course Run Sequences',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(40,1,'Moodle Test',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(41,40,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(45,NULL,'System Administrator',1,'2006-11-01 00:00:00','2006-11-01 00:00:00',1),(46,1,'Public Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',3);
/*!40000 ALTER TABLE `lams_workspace_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder_content`
--

DROP TABLE IF EXISTS `lams_workspace_folder_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_workspace_folder_content` (
  `folder_content_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_type_id` int(3) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(64) NOT NULL,
  `create_date_time` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `workspace_folder_id` bigint(20) NOT NULL,
  `uuid` bigint(20) DEFAULT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `mime_type` varchar(10) NOT NULL,
  PRIMARY KEY (`folder_content_id`),
  UNIQUE KEY `unique_content_name` (`name`,`workspace_folder_id`,`mime_type`),
  UNIQUE KEY `unique_node_version` (`workspace_folder_id`,`uuid`,`version_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  KEY `content_type_id` (`content_type_id`),
  CONSTRAINT `FK_lams_workspace_folder_content_1` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_workspace_folder_content_2` FOREIGN KEY (`content_type_id`) REFERENCES `lams_wkspc_fld_content_type` (`content_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `lams_workspace_folder_type` (
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lams_workspace_folder_type`
--

LOCK TABLES `lams_workspace_folder_type` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder_type` DISABLE KEYS */;
INSERT INTO `lams_workspace_folder_type` VALUES (1,'NORMAL'),(2,'RUN SEQUENCES'),(3,'PUBLIC SEQUENCES');
/*!40000 ALTER TABLE `lams_workspace_folder_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patches`
--

DROP TABLE IF EXISTS `patches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patches` (
  `system_name` varchar(30) NOT NULL,
  `patch_level` int(11) NOT NULL,
  `patch_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `patch_in_progress` char(1) NOT NULL DEFAULT 'F',
  PRIMARY KEY (`system_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patches`
--

LOCK TABLES `patches` WRITE;
/*!40000 ALTER TABLE `patches` DISABLE KEYS */;
INSERT INTO `patches` VALUES ('laasse10',20100225,'2010-04-01 04:58:12','F'),('lachat11',20081125,'2010-04-01 04:56:42','F'),('ladaco10',20090326,'2010-04-01 04:57:35','F'),('laddim10',20080811,'2010-04-01 04:57:53','F'),('lafrum11',20081118,'2010-04-01 04:56:20','F'),('lagmap10',20080521,'2010-04-01 04:57:23','F'),('laimag10',20090819,'2010-04-01 04:57:48','F'),('lamc11',20081127,'2010-04-01 04:58:22','F'),('lamind10',20090202,'2010-04-01 04:58:04','F'),('lams',2030402,'2010-04-01 04:55:04','F'),('lanb11',20081209,'2010-04-01 04:56:25','F'),('lantbk11',20081118,'2010-04-01 04:56:59','F'),('lapixl10',20081217,'2010-04-01 04:58:17','F'),('laqa11',20081126,'2010-04-01 04:56:31','F'),('larsrc11',20090115,'2010-04-01 04:56:49','F'),('lasbmt11',20091028,'2010-04-01 04:56:36','F'),('lascrb11',20090226,'2010-04-01 04:57:10','F'),('lasprd10',20080612,'2010-04-01 04:57:28','F'),('lasurv11',20081021,'2010-04-01 04:57:05','F'),('latask10',20090113,'2010-04-01 04:57:18','F'),('lavidr10',20081208,'2010-04-01 04:57:59','F'),('lavote11',20081203,'2010-04-01 04:56:54','F'),('lawiki10',20090121,'2010-04-01 04:57:40','F');
/*!40000 ALTER TABLE `patches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_assessment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT NULL,
  `time_limit` int(11) DEFAULT '0',
  `attempts_allowed` int(11) DEFAULT '1',
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT NULL,
  `define_later` tinyint(4) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `allow_question_feedback` tinyint(4) DEFAULT NULL,
  `allow_overall_feedback` tinyint(4) DEFAULT NULL,
  `allow_right_answers` tinyint(4) DEFAULT NULL,
  `allow_wrong_answers` tinyint(4) DEFAULT NULL,
  `allow_grades_after_attempt` tinyint(4) DEFAULT NULL,
  `allow_history_responses` tinyint(4) DEFAULT NULL,
  `questions_per_page` int(11) DEFAULT '0',
  `shuffled` tinyint(4) DEFAULT NULL,
  `attempt_completion_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_1720029621_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_1720029621_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_assessment`
--

LOCK TABLES `tl_laasse10_assessment` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment` DISABLE KEYS */;
INSERT INTO `tl_laasse10_assessment` VALUES (1,NULL,NULL,NULL,'Assessment',0,0,1,'Instructions ',NULL,NULL,0,0,20,0,0,0,0,0,0,0,0,0);
/*!40000 ALTER TABLE `tl_laasse10_assessment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment_overall_feedback`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment_overall_feedback`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_assessment_overall_feedback` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `assessment_uid` bigint(20) DEFAULT NULL,
  `sequence_id` int(11) DEFAULT NULL,
  `grade_boundary` int(11) DEFAULT NULL,
  `feedback` text,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_assessment_overall_feedback_1` (`assessment_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_overall_feedback_1` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_assessment_overall_feedback`
--

LOCK TABLES `tl_laasse10_assessment_overall_feedback` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment_overall_feedback` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_assessment_overall_feedback` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment_question`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_assessment_question` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_type` smallint(6) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `question` text,
  `sequence_id` int(11) DEFAULT NULL,
  `default_grade` int(11) DEFAULT '1',
  `penalty_factor` float DEFAULT '0',
  `general_feedback` text,
  `feedback` text,
  `multiple_answers_allowed` tinyint(4) DEFAULT '0',
  `feedback_on_correct` text,
  `feedback_on_partially_correct` text,
  `feedback_on_incorrect` text,
  `shuffle` tinyint(4) DEFAULT NULL,
  `case_sensitive` tinyint(4) DEFAULT NULL,
  `correct_answer` tinyint(4) DEFAULT '0',
  `allow_rich_editor` tinyint(4) DEFAULT '0',
  `create_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `assessment_uid` bigint(20) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1720029621_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_1720029621_F52D1F9330E79035` (`assessment_uid`),
  KEY `FK_NEW_1720029621_F52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laasse10_session` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F9330E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_assessment_question`
--

LOCK TABLES `tl_laasse10_assessment_question` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment_question` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_assessment_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment_result`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_assessment_result` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `assessment_uid` bigint(20) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  `user_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `maximum_grade` int(11) DEFAULT NULL,
  `grade` float DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_laasse10_assessment_result_2` (`user_uid`),
  KEY `FK_tl_laasse10_assessment_result_3` (`assessment_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_result_3` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_result_2` FOREIGN KEY (`user_uid`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_assessment_result`
--

LOCK TABLES `tl_laasse10_assessment_result` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_assessment_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment_unit`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment_unit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_assessment_unit` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_uid` bigint(20) DEFAULT NULL,
  `sequence_id` int(11) DEFAULT NULL,
  `multiplier` float DEFAULT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_assessment_unit_1` (`question_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_unit_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_assessment_unit`
--

LOCK TABLES `tl_laasse10_assessment_unit` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment_unit` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_assessment_unit` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_attachment`
--

DROP TABLE IF EXISTS `tl_laasse10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `assessment_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1720029621_1E7009430E79035` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_1E7009430E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_attachment`
--

LOCK TABLES `tl_laasse10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_option_answer`
--

DROP TABLE IF EXISTS `tl_laasse10_option_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_option_answer` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_result_uid` bigint(20) DEFAULT NULL,
  `question_option_uid` bigint(20) DEFAULT NULL,
  `answer_boolean` tinyint(1) DEFAULT NULL,
  `answer_int` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_option_answer_1` (`question_result_uid`),
  CONSTRAINT `FK_tl_laasse10_option_answer_1` FOREIGN KEY (`question_result_uid`) REFERENCES `tl_laasse10_question_result` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_option_answer`
--

LOCK TABLES `tl_laasse10_option_answer` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_option_answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_option_answer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_question_option`
--

DROP TABLE IF EXISTS `tl_laasse10_question_option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_question_option` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_uid` bigint(20) DEFAULT NULL,
  `sequence_id` int(11) DEFAULT NULL,
  `question` text,
  `option_string` text,
  `option_float` float DEFAULT NULL,
  `accepted_error` float DEFAULT NULL,
  `grade` float DEFAULT NULL,
  `feedback` text,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_question_option_1` (`question_uid`),
  CONSTRAINT `FK_tl_laasse10_question_option_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_question_option`
--

LOCK TABLES `tl_laasse10_question_option` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_question_option` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_question_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_question_result`
--

DROP TABLE IF EXISTS `tl_laasse10_question_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_question_result` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `assessment_question_uid` bigint(20) DEFAULT NULL,
  `result_uid` bigint(20) DEFAULT NULL,
  `answer_string` text,
  `answer_float` float DEFAULT NULL,
  `answer_boolean` tinyint(1) DEFAULT NULL,
  `submitted_option_uid` bigint(20) DEFAULT NULL,
  `mark` float DEFAULT NULL,
  `penalty` float DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1720029621_693580A438BF8DFE` (`assessment_question_uid`),
  KEY `FK_tl_laasse10_question_result_1` (`result_uid`),
  CONSTRAINT `FK_tl_laasse10_question_result_1` FOREIGN KEY (`result_uid`) REFERENCES `tl_laasse10_assessment_result` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_693580A438BF8DFE` FOREIGN KEY (`assessment_question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_question_result`
--

LOCK TABLES `tl_laasse10_question_result` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_question_result` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_question_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_session`
--

DROP TABLE IF EXISTS `tl_laasse10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `assessment_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1720029621_24AA78C530E79035` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_24AA78C530E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_session`
--

LOCK TABLES `tl_laasse10_session` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_user`
--

DROP TABLE IF EXISTS `tl_laasse10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laasse10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `assessment_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1720029621_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_1720029621_30113BFC309ED320` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_30113BFC309ED320` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laasse10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laasse10_user`
--

LOCK TABLES `tl_laasse10_user` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laasse10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_attachment`
--

DROP TABLE IF EXISTS `tl_lachat11_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `chat_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK9ED6CB2E1A3926E3` (`chat_uid`),
  CONSTRAINT `FK9ED6CB2E1A3926E3` FOREIGN KEY (`chat_uid`) REFERENCES `tl_lachat11_chat` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_chat` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `filtering_enabled` bit(1) DEFAULT NULL,
  `filter_keywords` text,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lachat11_chat`
--

LOCK TABLES `tl_lachat11_chat` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_chat` DISABLE KEYS */;
INSERT INTO `tl_lachat11_chat` VALUES (1,NULL,NULL,NULL,'Chat','Instructions',0x00,0x00,0x00,NULL,'','',0x00,0x00,5,0x00,NULL),(2,'2007-04-13 01:58:15','2007-03-09 09:44:33',5,'Brain Chat','<font size=\"3\" face=\"Verdana\">Now it&rsquo;s time to discuss what you&rsquo;ve learnt.&nbsp; Use <strong><em>Chat</em></strong> to find out what other members of your group learnt and enjoyed about the session as well as telling them what you have found interesting and enjoyed.<br />\n</font>',0x00,0x00,0x00,'','','',0x00,0x00,25,0x00,''),(3,'2010-04-01 16:04:05','2007-03-09 09:44:33',5,'Brain Chat','<font size=\"3\" face=\"Verdana\">Now it&rsquo;s time to discuss what you&rsquo;ve learnt.&nbsp; Use <strong><em>Chat</em></strong> to find out what other members of your group learnt and enjoyed about the session as well as telling them what you have found interesting and enjoyed.<br />\n</font>',0x00,0x00,0x00,'','','',0x00,0x00,75,0x00,'');
/*!40000 ALTER TABLE `tl_lachat11_chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_conditions`
--

DROP TABLE IF EXISTS `tl_lachat11_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `ChatConditionToChat` (`content_uid`),
  CONSTRAINT `ChatConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ChatConditionToChat` FOREIGN KEY (`content_uid`) REFERENCES `tl_lachat11_chat` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_message` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `chat_session_uid` bigint(20) NOT NULL,
  `from_user_uid` bigint(20) DEFAULT NULL,
  `to_user_uid` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `body` text,
  `send_date` datetime DEFAULT NULL,
  `hidden` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKCC08C1DC2AF61E05` (`to_user_uid`),
  KEY `FKCC08C1DC9C8469FC` (`chat_session_uid`),
  KEY `FKCC08C1DCCF3BF9B6` (`from_user_uid`),
  CONSTRAINT `FKCC08C1DCCF3BF9B6` FOREIGN KEY (`from_user_uid`) REFERENCES `tl_lachat11_user` (`uid`),
  CONSTRAINT `FKCC08C1DC2AF61E05` FOREIGN KEY (`to_user_uid`) REFERENCES `tl_lachat11_user` (`uid`),
  CONSTRAINT `FKCC08C1DC9C8469FC` FOREIGN KEY (`chat_session_uid`) REFERENCES `tl_lachat11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `chat_uid` bigint(20) DEFAULT NULL,
  `jabber_room` varchar(250) DEFAULT NULL,
  `room_created` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK96E446B1A3926E3` (`chat_uid`),
  CONSTRAINT `FK96E446B1A3926E3` FOREIGN KEY (`chat_uid`) REFERENCES `tl_lachat11_chat` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lachat11_session`
--

LOCK TABLES `tl_lachat11_session` WRITE;
/*!40000 ALTER TABLE `tl_lachat11_session` DISABLE KEYS */;
INSERT INTO `tl_lachat11_session` VALUES (1,NULL,NULL,NULL,1,'LAMS Demo Course learners',3,'ff80808127b7bdae0127b7c138870010@conference.localhost',0x00);
/*!40000 ALTER TABLE `tl_lachat11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lachat11_user`
--

DROP TABLE IF EXISTS `tl_lachat11_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lachat11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `jabber_id` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `jabber_nickname` varchar(255) DEFAULT NULL,
  `chat_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK4EB82169C8469FC` (`chat_session_uid`),
  CONSTRAINT `FK4EB82169C8469FC` FOREIGN KEY (`chat_session_uid`) REFERENCES `tl_lachat11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_answer_options` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_uid` bigint(20) DEFAULT NULL,
  `sequence_num` tinyint(3) unsigned DEFAULT '1',
  `answer_option` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `question_uid` (`question_uid`),
  CONSTRAINT `tl_ladaco10_answer_options_ibfk_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_ladaco10_questions` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_answers` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_uid` bigint(20) DEFAULT NULL,
  `question_uid` bigint(20) DEFAULT NULL,
  `record_id` smallint(5) unsigned DEFAULT NULL,
  `answer` text,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `file_version_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `user_uid` (`user_uid`),
  KEY `question_uid` (`question_uid`),
  CONSTRAINT `AnswerToQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_ladaco10_questions` (`uid`),
  CONSTRAINT `AnswerToUser` FOREIGN KEY (`user_uid`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_attachments` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `tl_ladaco10_attachments_ibfk_1` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_contents` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT '0',
  `lock_on_finished` tinyint(4) DEFAULT '0',
  `min_records` tinyint(3) unsigned DEFAULT '0',
  `max_records` tinyint(3) unsigned DEFAULT '0',
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT '0',
  `define_later` tinyint(4) DEFAULT '0',
  `content_id` bigint(20) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` tinyint(4) DEFAULT NULL,
  `learner_entry_notify` tinyint(4) DEFAULT '0',
  `record_submit_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `create_by` (`create_by`),
  CONSTRAINT `DacoToUser` FOREIGN KEY (`create_by`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_ladaco10_contents`
--

LOCK TABLES `tl_ladaco10_contents` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_contents` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_contents` VALUES (1,NULL,NULL,NULL,'Data Collection',0,0,0,0,'Instructions',NULL,NULL,0,0,14,NULL,0,0,0),(2,NULL,'2008-11-10 12:52:14',1,'Data Collection',0,0,0,0,'<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/5570965/dacoIcon.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(51, 153, 102);\">Data Collection</span></span></p>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<p>The <b>Data Collection </b>tool allows Authors to create database style lists for Learners to populate.</p>\n<p>Each record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</p>','','',0,0,57,'',0,0,0),(3,NULL,'2008-11-10 12:52:14',2,'Data Collection',0,0,0,0,'<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/5570965/dacoIcon.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(51, 153, 102);\">Data Collection</span></span></p>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<p>The <b>Data Collection </b>tool allows Authors to create database style lists for Learners to populate.</p>\n<p>Each record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</p>','','',0,0,104,'',0,0,0);
/*!40000 ALTER TABLE `tl_ladaco10_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_questions`
--

DROP TABLE IF EXISTS `tl_ladaco10_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_questions` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` text,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_required` tinyint(4) DEFAULT '0',
  `question_type` tinyint(3) unsigned DEFAULT NULL,
  `min_constraint` float DEFAULT NULL,
  `max_constraint` float DEFAULT NULL,
  `digits_decimal` tinyint(3) unsigned DEFAULT NULL,
  `summary` tinyint(3) unsigned DEFAULT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `create_by` (`create_by`),
  KEY `content_uid` (`content_uid`),
  KEY `session_uid` (`session_uid`),
  CONSTRAINT `tl_ladaco10_questions_ibfk_1` FOREIGN KEY (`session_uid`) REFERENCES `tl_ladaco10_sessions` (`uid`),
  CONSTRAINT `QuestionToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`),
  CONSTRAINT `QuestionToUser` FOREIGN KEY (`create_by`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_ladaco10_questions`
--

LOCK TABLES `tl_ladaco10_questions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_questions` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_questions` VALUES (1,'<div>What is your favourite colour?</div>',NULL,'2010-04-01 15:57:35',0,1,NULL,NULL,NULL,NULL,1,NULL),(2,'<div>What is your favourite colour?</div>',1,'2008-11-07 17:34:13',0,1,NULL,NULL,NULL,NULL,2,NULL),(3,'<div>What is your lucky number?</div>',1,'2008-11-10 12:49:56',0,3,NULL,NULL,NULL,NULL,2,NULL),(4,'<div>What is today\'s date?</div>',1,'2008-11-10 12:50:16',0,4,NULL,NULL,NULL,NULL,2,NULL),(5,'<div>What is your favourite colour?</div>',2,'2008-11-07 17:34:13',0,1,NULL,NULL,NULL,NULL,3,NULL),(6,'<div>What is your lucky number?</div>',2,'2008-11-10 12:49:56',0,3,NULL,NULL,NULL,NULL,3,NULL),(7,'<div>What is today\'s date?</div>',2,'2008-11-10 12:50:16',0,4,NULL,NULL,NULL,NULL,3,NULL);
/*!40000 ALTER TABLE `tl_ladaco10_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_sessions`
--

DROP TABLE IF EXISTS `tl_ladaco10_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_sessions` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  UNIQUE KEY `session_id` (`session_id`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `SessionToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_ladaco10_sessions`
--

LOCK TABLES `tl_ladaco10_sessions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_sessions` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_sessions` VALUES (1,NULL,NULL,0,3,35,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_ladaco10_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_users`
--

DROP TABLE IF EXISTS `tl_ladaco10_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_ladaco10_users` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `session_uid` (`session_uid`),
  KEY `content_uid` (`content_uid`),
  CONSTRAINT `UserToDaco` FOREIGN KEY (`content_uid`) REFERENCES `tl_ladaco10_contents` (`uid`),
  CONSTRAINT `UserToSession` FOREIGN KEY (`session_uid`) REFERENCES `tl_ladaco10_sessions` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_ladaco10_users`
--

LOCK TABLES `tl_ladaco10_users` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_users` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_users` VALUES (1,5,'Test','One','test1',0,NULL,2),(2,5,'Test','One','test1',0,NULL,3);
/*!40000 ALTER TABLE `tl_ladaco10_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laddim10_attachment`
--

DROP TABLE IF EXISTS `tl_laddim10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laddim10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `dimdim_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `fk_laddim10_dimdim_attachment_to_dimdim` (`dimdim_uid`),
  CONSTRAINT `fk_laddim10_dimdim_attachment_to_dimdim` FOREIGN KEY (`dimdim_uid`) REFERENCES `tl_laddim10_dimdim` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laddim10_attachment`
--

LOCK TABLES `tl_laddim10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_laddim10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laddim10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laddim10_config`
--

DROP TABLE IF EXISTS `tl_laddim10_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laddim10_config` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laddim10_config`
--

LOCK TABLES `tl_laddim10_config` WRITE;
/*!40000 ALTER TABLE `tl_laddim10_config` DISABLE KEYS */;
INSERT INTO `tl_laddim10_config` VALUES (1,'server_url','http://172.20.100.253/');
/*!40000 ALTER TABLE `tl_laddim10_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laddim10_dimdim`
--

DROP TABLE IF EXISTS `tl_laddim10_dimdim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laddim10_dimdim` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `max_attendee_mikes` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laddim10_dimdim`
--

LOCK TABLES `tl_laddim10_dimdim` WRITE;
/*!40000 ALTER TABLE `tl_laddim10_dimdim` DISABLE KEYS */;
INSERT INTO `tl_laddim10_dimdim` VALUES (1,NULL,NULL,NULL,'Dimdim','Instructions',0x00,0x00,0x00,NULL,'','',0x00,0x00,17,1);
/*!40000 ALTER TABLE `tl_laddim10_dimdim` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laddim10_session`
--

DROP TABLE IF EXISTS `tl_laddim10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laddim10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `dimdim_uid` bigint(20) DEFAULT NULL,
  `meeting_created` bit(1) DEFAULT NULL,
  `meeting_key` varchar(255) DEFAULT NULL,
  `max_attendee_mikes` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `fk_laddim10_dimdim_session_to_dimdim` (`dimdim_uid`),
  CONSTRAINT `fk_laddim10_dimdim_session_to_dimdim` FOREIGN KEY (`dimdim_uid`) REFERENCES `tl_laddim10_dimdim` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laddim10_session`
--

LOCK TABLES `tl_laddim10_session` WRITE;
/*!40000 ALTER TABLE `tl_laddim10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laddim10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laddim10_user`
--

DROP TABLE IF EXISTS `tl_laddim10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laddim10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `dimdim_session_uid` bigint(20) DEFAULT NULL,
  `notebook_entry_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `fk_laddim10_dimdim_user_to_dimdim_session` (`dimdim_session_uid`),
  CONSTRAINT `fk_laddim10_dimdim_user_to_dimdim_session` FOREIGN KEY (`dimdim_session_uid`) REFERENCES `tl_laddim10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laddim10_user`
--

LOCK TABLES `tl_laddim10_user` WRITE;
/*!40000 ALTER TABLE `tl_laddim10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laddim10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_attachment`
--

DROP TABLE IF EXISTS `tl_lafrum11_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `forum_uid` bigint(20) DEFAULT NULL,
  `message_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK389AD9A2FE939F2A` (`message_uid`),
  KEY `FK389AD9A2131CE31E` (`forum_uid`),
  CONSTRAINT `FK389AD9A2131CE31E` FOREIGN KEY (`forum_uid`) REFERENCES `tl_lafrum11_forum` (`uid`),
  CONSTRAINT `FK389AD9A2FE939F2A` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_condition_topics` (
  `condition_id` bigint(20) NOT NULL DEFAULT '0',
  `topic_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`condition_id`,`topic_uid`),
  KEY `ForumConditionQuestionToForumQuestion` (`topic_uid`),
  CONSTRAINT `ForumConditionQuestionToForumCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lafrum11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionQuestionToForumQuestion` FOREIGN KEY (`topic_uid`) REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `ForumConditionToForum` (`content_uid`),
  CONSTRAINT `ForumConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionToForum` FOREIGN KEY (`content_uid`) REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_forum` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `allow_anonym` smallint(6) DEFAULT NULL,
  `run_offline` smallint(6) DEFAULT NULL,
  `lock_on_finished` smallint(6) DEFAULT NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` smallint(6) DEFAULT NULL,
  `define_later` smallint(6) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `allow_edit` smallint(6) DEFAULT NULL,
  `allow_rich_editor` smallint(6) DEFAULT NULL,
  `allow_new_topic` smallint(6) DEFAULT NULL,
  `allow_upload` smallint(6) DEFAULT NULL,
  `maximum_reply` int(11) DEFAULT NULL,
  `minimum_reply` int(11) DEFAULT NULL,
  `limited_of_chars` int(11) DEFAULT NULL,
  `limited_input_flag` smallint(6) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  `mark_release_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK87917942E42F4351` (`create_by`),
  CONSTRAINT `FK87917942E42F4351` FOREIGN KEY (`create_by`) REFERENCES `tl_lafrum11_forum_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_forum`
--

LOCK TABLES `tl_lafrum11_forum` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_forum` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_forum` VALUES (1,NULL,NULL,NULL,'Forum',0,0,0,'Instructions',NULL,NULL,0,0,1,1,0,1,0,1,0,5000,1,NULL,0,0),(2,'2007-02-23 16:04:55','2010-04-01 16:03:10',1,'Your Amazing Brain',0,0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nAs you have just seen, the brain is an amazing organ!&nbsp; Now is your chance tell each other what you found most interesting about the brain.&nbsp; Click on the link to access the forum.</font>','','',0,0,28,1,0,1,0,0,0,5000,1,'',0,0),(3,'2007-02-23 16:05:08','2010-04-01 16:03:10',1,'How Long Did You Take',0,0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nCompare the time you took to complete the game with your friends&rsquo; times.</font>','','',0,0,30,1,0,1,0,0,0,5000,1,'',0,0),(4,'2008-04-11 16:03:30','2010-04-01 16:03:17',1,'LAMS Resources Forum',0,0,0,'<h5>These forum messages relate to the resources above.</h5>','','',0,0,47,1,0,1,0,0,0,5000,1,'',0,0),(5,'2007-02-23 16:04:55','2010-04-01 16:04:04',1,'Your Amazing Brain',0,0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nAs you have just seen, the brain is an amazing organ!&nbsp; Now is your chance tell each other what you found most interesting about the brain.&nbsp; Click on the link to access the forum.</font>','','',0,0,73,1,0,1,0,0,0,5000,1,'',0,0),(6,'2007-02-23 16:05:08','2010-04-01 16:04:05',1,'How Long Did You Take',0,0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nCompare the time you took to complete the game with your friends&rsquo; times.</font>','','',0,0,77,1,0,1,0,0,0,5000,1,'',0,0),(7,'2008-04-11 16:03:30','2010-04-01 16:04:14',1,'LAMS Resources Forum',0,0,0,'<h5>These forum messages relate to the resources above.</h5>','','',0,0,89,1,0,1,0,0,0,5000,1,'',0,0);
/*!40000 ALTER TABLE `tl_lafrum11_forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_forum_user`
--

DROP TABLE IF EXISTS `tl_lafrum11_forum_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_forum_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK7B83A4A85F0116B6` (`session_id`),
  CONSTRAINT `FK7B83A4A85F0116B6` FOREIGN KEY (`session_id`) REFERENCES `tl_lafrum11_tool_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_message` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `last_reply_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `modified_by` bigint(20) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `body` text,
  `is_authored` smallint(6) DEFAULT NULL,
  `is_anonymous` smallint(6) DEFAULT NULL,
  `forum_session_uid` bigint(20) DEFAULT NULL,
  `parent_uid` bigint(20) DEFAULT NULL,
  `forum_uid` bigint(20) DEFAULT NULL,
  `reply_number` int(11) DEFAULT NULL,
  `hide_flag` smallint(6) DEFAULT NULL,
  `report_id` bigint(20) DEFAULT NULL,
  `authored_parent_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_message`
--

LOCK TABLES `tl_lafrum11_message` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_message` VALUES (1,'2010-04-01 15:56:20','2010-04-01 15:56:20','2010-04-01 15:56:20',NULL,NULL,'Topic Heading','Topic message',1,0,NULL,NULL,1,0,0,NULL,NULL),(2,'2007-01-30 16:32:57','2007-01-31 16:48:07','2010-04-01 16:03:10',1,NULL,'The Amazing Brain','Please enter the most interesting thing you found out about the brain.',1,0,NULL,NULL,2,0,0,NULL,NULL),(3,'2007-01-30 16:40:00','2007-01-31 16:48:07','2010-04-01 16:03:10',1,NULL,'How Long Did You Take?','<font size=\"3\" face=\"Verdana\">Compare your times, who was the fastest?</font>',1,0,NULL,NULL,3,0,0,NULL,NULL),(4,'2008-06-06 12:12:18','2008-11-14 14:31:47','2010-04-01 16:03:17',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,NULL,NULL,4,0,0,NULL,NULL),(5,'2008-06-06 12:12:18','2008-11-14 14:31:47','2010-04-01 16:03:17',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,NULL,NULL,4,0,0,NULL,NULL),(6,'2007-01-30 16:32:57','2010-04-01 16:04:04','2010-04-01 16:04:04',1,NULL,'The Amazing Brain','Please enter the most interesting thing you found out about the brain.',1,0,NULL,NULL,5,0,0,NULL,NULL),(7,'2007-01-30 16:40:00','2010-04-01 16:04:05','2010-04-01 16:04:05',1,NULL,'How Long Did You Take?','<font size=\"3\" face=\"Verdana\">Compare your times, who was the fastest?</font>',1,0,NULL,NULL,6,0,0,NULL,NULL),(8,'2007-01-30 16:40:00','2010-04-01 16:04:05','2010-04-01 16:04:05',1,NULL,'How Long Did You Take?','<font size=\"3\" face=\"Verdana\">Compare your times, who was the fastest?</font>',1,0,1,NULL,6,0,0,NULL,7),(9,'2007-01-30 16:32:57','2010-04-01 16:04:04','2010-04-01 16:04:06',1,NULL,'The Amazing Brain','Please enter the most interesting thing you found out about the brain.',1,0,2,NULL,5,0,0,NULL,6),(10,'2008-06-06 12:12:18','2010-04-01 16:04:14','2010-04-01 16:04:14',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,NULL,NULL,7,0,0,NULL,NULL),(11,'2008-06-06 12:12:18','2010-04-01 16:04:14','2010-04-01 16:04:14',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,NULL,NULL,7,0,0,NULL,NULL),(12,'2008-06-06 12:12:18','2010-04-01 16:04:14','2010-04-01 16:04:15',1,NULL,'2008 Conference','<div><b><i>What do you hope to see at the upcoming 2008 Conference in Cadiz?</i></b></div>',1,0,3,NULL,7,0,0,NULL,10),(13,'2008-06-06 12:12:18','2010-04-01 16:04:14','2010-04-01 16:04:15',1,NULL,'2007 Conference','<div><b><i>What were the best things about the 2007 conference?</i></b></div>\n<div><b><i>Which presentations did you get the most out of?</i></b></div>',1,0,3,NULL,7,0,0,NULL,11);
/*!40000 ALTER TABLE `tl_lafrum11_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_message_seq`
--

DROP TABLE IF EXISTS `tl_lafrum11_message_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_message_seq` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `root_message_uid` bigint(20) DEFAULT NULL,
  `message_uid` bigint(20) DEFAULT NULL,
  `message_level` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKD2C71F88FE939F2A` (`message_uid`),
  KEY `FKD2C71F8845213B4D` (`root_message_uid`),
  CONSTRAINT `FKD2C71F8845213B4D` FOREIGN KEY (`root_message_uid`) REFERENCES `tl_lafrum11_message` (`uid`),
  CONSTRAINT `FKD2C71F88FE939F2A` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_message_seq`
--

LOCK TABLES `tl_lafrum11_message_seq` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_message_seq` VALUES (1,2,2,0),(2,3,3,0),(3,4,4,0),(4,5,5,0),(5,6,6,0),(6,7,7,0),(7,8,8,0),(8,9,9,0),(9,10,10,0),(10,11,11,0),(11,12,12,0),(12,13,13,0);
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_report`
--

DROP TABLE IF EXISTS `tl_lafrum11_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_report` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text,
  `release_date` datetime DEFAULT NULL,
  `mark` float DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_report`
--

LOCK TABLES `tl_lafrum11_report` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_timestamp`
--

DROP TABLE IF EXISTS `tl_lafrum11_timestamp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_timestamp` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_uid` bigint(20) NOT NULL,
  `timestamp_date` datetime NOT NULL,
  `forum_user_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `message_uid` (`message_uid`),
  KEY `forum_user_uid` (`forum_user_uid`),
  KEY `ForumUserFK` (`forum_user_uid`),
  KEY `MessageFK` (`message_uid`),
  CONSTRAINT `MessageFK` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`),
  CONSTRAINT `ForumUserFK` FOREIGN KEY (`forum_user_uid`) REFERENCES `tl_lafrum11_forum_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_timestamp`
--

LOCK TABLES `tl_lafrum11_timestamp` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_timestamp` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_timestamp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_tool_session`
--

DROP TABLE IF EXISTS `tl_lafrum11_tool_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lafrum11_tool_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `mark_released` smallint(6) DEFAULT NULL,
  `forum_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK5A04D7AE131CE31E` (`forum_uid`),
  CONSTRAINT `FK5A04D7AE131CE31E` FOREIGN KEY (`forum_uid`) REFERENCES `tl_lafrum11_forum` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lafrum11_tool_session`
--

LOCK TABLES `tl_lafrum11_tool_session` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_tool_session` VALUES (1,0,NULL,NULL,1,0,6,4,'LAMS Demo Course learners'),(2,0,NULL,NULL,1,0,5,8,'LAMS Demo Course learners'),(3,0,NULL,NULL,1,0,7,22,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_attachment`
--

DROP TABLE IF EXISTS `tl_lagmap10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `gmap_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKBA2ECCB274028C80` (`gmap_uid`),
  CONSTRAINT `FKBA2ECCB274028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_configuration` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(30) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_gmap` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `allow_edit_markers` bit(1) DEFAULT NULL,
  `show_all_markers` bit(1) DEFAULT NULL,
  `limit_markers` bit(1) DEFAULT NULL,
  `max_markers` int(11) DEFAULT NULL,
  `allow_zoom` bit(1) DEFAULT NULL,
  `allow_terrain` bit(1) DEFAULT NULL,
  `allow_satellite` bit(1) DEFAULT NULL,
  `allow_hybrid` bit(1) DEFAULT NULL,
  `map_center_latitude` double DEFAULT NULL,
  `map_center_longitude` double DEFAULT NULL,
  `map_zoom` int(11) DEFAULT NULL,
  `map_type` varchar(20) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `default_geocoder_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lagmap10_gmap`
--

LOCK TABLES `tl_lagmap10_gmap` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_gmap` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_gmap` VALUES (1,NULL,NULL,NULL,'Gmap','Instructions',0x00,0x00,'','',0x00,0x00,12,0x01,0x01,0x00,1,0x01,0x00,0x01,0x01,-33.774322,151.111988,13,'G_NORMAL_MAP',0x00,'','Macquarie University, Sydney NSW'),(2,'2008-11-14 14:32:00','2008-11-10 12:52:48',1,'Google Maps Demonstration','<div>\n<div><img width=\"125\" height=\"52\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmap.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(255, 153, 0);\"><b>Google Maps</b></span></span> </div>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>The <b>Google Maps </b>Activity allows sequence Authors to create maps or satellite images with annotated place markers.<br />\nDuring the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><b>NOTE: </b>For the Google Maps activity to function, an API key must be added to your LAMS server.&nbsp; <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Google+Map+Key\" target=\"_blank\">Instructions to do this are here.</a></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>\n</div>',0x00,0x00,'','',0x00,0x00,53,0x01,0x01,0x00,0,0x01,0x00,0x01,0x01,-33.7746794632346,151.11565053463,17,'G_SATELLITE_MAP',0x00,'','Macquarie University, Sydney NSW'),(3,'2010-04-01 16:04:14','2008-11-10 12:52:48',1,'Google Maps Demonstration','<div>\n<div><img width=\"125\" height=\"52\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmap.png\" alt=\"\" /> <span style=\"font-size: large;\"><span style=\"color: rgb(255, 153, 0);\"><b>Google Maps</b></span></span> </div>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>The <b>Google Maps </b>Activity allows sequence Authors to create maps or satellite images with annotated place markers.<br />\nDuring the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><b>NOTE: </b>For the Google Maps activity to function, an API key must be added to your LAMS server.&nbsp; <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Google+Map+Key\" target=\"_blank\">Instructions to do this are here.</a></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>\n</div>',0x00,0x00,'','',0x00,0x00,91,0x01,0x01,0x00,0,0x01,0x00,0x01,0x01,-33.7746794632346,151.11565053463,17,'G_SATELLITE_MAP',0x00,'','Macquarie University, Sydney NSW');
/*!40000 ALTER TABLE `tl_lagmap10_gmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_marker`
--

DROP TABLE IF EXISTS `tl_lagmap10_marker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_marker` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `info_window_message` text,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `is_authored` bit(1) DEFAULT NULL,
  `gmap_uid` bigint(20) DEFAULT NULL,
  `title` varchar(55) DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated_by` bigint(20) DEFAULT NULL,
  `gmap_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK10F2274974028C80` (`gmap_uid`),
  KEY `FK10F22749C5F056D9` (`gmap_session_uid`),
  KEY `FK10F22749EF5F6920` (`updated_by`),
  KEY `FK10F22749529F7FD3` (`created_by`),
  CONSTRAINT `FK10F22749529F7FD3` FOREIGN KEY (`created_by`) REFERENCES `tl_lagmap10_user` (`uid`),
  CONSTRAINT `FK10F2274974028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`),
  CONSTRAINT `FK10F22749C5F056D9` FOREIGN KEY (`gmap_session_uid`) REFERENCES `tl_lagmap10_session` (`uid`),
  CONSTRAINT `FK10F22749EF5F6920` FOREIGN KEY (`updated_by`) REFERENCES `tl_lagmap10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `gmap_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK519D516774028C80` (`gmap_uid`),
  CONSTRAINT `FK519D516774028C80` FOREIGN KEY (`gmap_uid`) REFERENCES `tl_lagmap10_gmap` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lagmap10_session`
--

LOCK TABLES `tl_lagmap10_session` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_session` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_session` VALUES (1,NULL,NULL,NULL,27,'LAMS Demo Course learners',3);
/*!40000 ALTER TABLE `tl_lagmap10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_user`
--

DROP TABLE IF EXISTS `tl_lagmap10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lagmap10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `gmap_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK7CB3F69AC5F056D9` (`gmap_session_uid`),
  CONSTRAINT `FK7CB3F69AC5F056D9` FOREIGN KEY (`gmap_session_uid`) REFERENCES `tl_lagmap10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lagmap10_user`
--

LOCK TABLES `tl_lagmap10_user` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_user` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_user` VALUES (1,5,'Test','test1','One',0x00,NULL);
/*!40000 ALTER TABLE `tl_lagmap10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_attachment`
--

DROP TABLE IF EXISTS `tl_laimag10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_attachment` (
  `attachment_uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `imageGallery_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`attachment_uid`),
  KEY `FK_NEW_1821149711_1E7009430E79035` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_1E7009430E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imagegallery` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_attachment`
--

LOCK TABLES `tl_laimag10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_configuration`
--

DROP TABLE IF EXISTS `tl_laimag10_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_configuration` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(30) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_configuration`
--

LOCK TABLES `tl_laimag10_configuration` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_configuration` DISABLE KEYS */;
INSERT INTO `tl_laimag10_configuration` VALUES (1,'mediumImageDimensions','640'),(2,'thumbnailImageDimensions','100');
/*!40000 ALTER TABLE `tl_laimag10_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_imageGallery`
--

DROP TABLE IF EXISTS `tl_laimag10_imageGallery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_imageGallery` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT NULL,
  `lock_on_finished` tinyint(4) DEFAULT NULL,
  `instructions` text,
  `next_image_title` bigint(20) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT NULL,
  `define_later` tinyint(4) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `allow_comment_images` tinyint(4) DEFAULT NULL,
  `allow_share_images` tinyint(4) DEFAULT NULL,
  `allow_vote` tinyint(4) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  `allow_rank` tinyint(4) DEFAULT NULL,
  `image_submit_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_1821149711_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_1821149711_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_imageGallery`
--

LOCK TABLES `tl_laimag10_imageGallery` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_imageGallery` DISABLE KEYS */;
INSERT INTO `tl_laimag10_imageGallery` VALUES (1,NULL,NULL,NULL,'Image Gallery',0,0,'Instructions ',1,NULL,NULL,0,0,16,0,0,0,NULL,0,0,0);
/*!40000 ALTER TABLE `tl_laimag10_imageGallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_imageGallery_item`
--

DROP TABLE IF EXISTS `tl_laimag10_imageGallery_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_imageGallery_item` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` text,
  `title` varchar(255) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by_author` tinyint(4) DEFAULT NULL,
  `sequence_id` int(11) DEFAULT NULL,
  `is_hide` tinyint(4) DEFAULT NULL,
  `imageGallery_uid` bigint(20) DEFAULT NULL,
  `original_file_uuid` bigint(20) DEFAULT NULL,
  `original_image_width` int(11) DEFAULT NULL,
  `original_image_height` int(11) DEFAULT NULL,
  `medium_file_uuid` bigint(20) DEFAULT NULL,
  `medium_image_width` int(11) DEFAULT NULL,
  `medium_image_height` int(11) DEFAULT NULL,
  `thumbnail_file_uuid` bigint(20) DEFAULT NULL,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1821149711_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_1821149711_F52D1F9330E79035` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_F52D1F9330E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imagegallery` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_imageGallery_item`
--

LOCK TABLES `tl_laimag10_imageGallery_item` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_imageGallery_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_imageGallery_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_image_comment`
--

DROP TABLE IF EXISTS `tl_laimag10_image_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_image_comment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text,
  `imageGallery_item_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_laimag10_image_comment_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_comment_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_comment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_comment_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imagegallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_image_comment`
--

LOCK TABLES `tl_laimag10_image_comment` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_image_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_image_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_image_rating`
--

DROP TABLE IF EXISTS `tl_laimag10_image_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_image_rating` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating` int(11) DEFAULT NULL,
  `imageGallery_item_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_laimag10_image_rating_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_rating_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_rating_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_rating_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imagegallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_image_rating`
--

LOCK TABLES `tl_laimag10_image_rating` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_image_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_image_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_image_vote`
--

DROP TABLE IF EXISTS `tl_laimag10_image_vote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_image_vote` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `is_voted` tinyint(4) DEFAULT NULL,
  `imageGallery_item_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_laimag10_image_vote_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_vote_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_vote_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_vote_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imagegallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_image_vote`
--

LOCK TABLES `tl_laimag10_image_vote` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_image_vote` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_image_vote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_item_log`
--

DROP TABLE IF EXISTS `tl_laimag10_item_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_item_log` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_date` datetime DEFAULT NULL,
  `imageGallery_item_uid` bigint(20) DEFAULT NULL,
  `user_uid` bigint(20) DEFAULT NULL,
  `complete` tinyint(4) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1821149711_693580A438BF8DFE` (`imageGallery_item_uid`),
  KEY `FK_NEW_1821149711_693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK_NEW_1821149711_693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_693580A438BF8DFE` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imagegallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_item_log`
--

LOCK TABLES `tl_laimag10_item_log` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_item_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_item_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_session`
--

DROP TABLE IF EXISTS `tl_laimag10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `imageGallery_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1821149711_24AA78C530E79035` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_24AA78C530E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imagegallery` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_session`
--

LOCK TABLES `tl_laimag10_session` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_user`
--

DROP TABLE IF EXISTS `tl_laimag10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laimag10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `imageGallery_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_1821149711_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_1821149711_30113BFC309ED320` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_30113BFC309ED320` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imagegallery` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laimag10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laimag10_user`
--

LOCK TABLES `tl_laimag10_user` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laimag10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_content`
--

DROP TABLE IF EXISTS `tl_lamc11_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `reflect` tinyint(1) NOT NULL DEFAULT '0',
  `questions_sequenced` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL DEFAULT '0',
  `run_offline` tinyint(1) NOT NULL DEFAULT '0',
  `define_later` tinyint(1) NOT NULL DEFAULT '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_in_use` tinyint(1) NOT NULL DEFAULT '0',
  `retries` tinyint(1) NOT NULL DEFAULT '0',
  `pass_mark` int(11) DEFAULT NULL,
  `show_report` tinyint(1) NOT NULL DEFAULT '0',
  `reflectionSubject` text,
  `showMarks` tinyint(1) NOT NULL DEFAULT '0',
  `randomize` tinyint(1) NOT NULL DEFAULT '0',
  `displayAnswers` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_content_1` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamc11_content`
--

LOCK TABLES `tl_lamc11_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_content` VALUES (1,22,'MCQ','Instructions','2010-04-01 15:58:22',NULL,0,0,1,0,0,'','',0,0,0,0,NULL,0,0,1),(2,44,'My Lost Beach Ball','<h2>This is a demonstration Multiple Choice Quiz.</h2>\n<div><b>As you can see, LAMS provides a space for Instructions and Comments (here)<br />\nand lists all the questions and choices for the activity below.</b></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h4>Read the short story below and answer the three questions:</h4>\n<div style=\"text-align: center;\"><b><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">One day my Dad bought me a new <span style=\"color: rgb(255, 0, 0);\">red </span>beach ball.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">My brother and I took it down to the beach to play with.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">We were playing Soccer when he kicked the ball into the water, accidentally.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">The ball floated out to sea before we could rescue it.</span></b></div>\n<div style=\"text-align: left;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><span style=\"font-size: smaller;\"><i> </i></span></div>\n<div style=\"text-align: center;\"><b><span style=\"font-size: smaller;\"><i>HINT</i></span></b><span style=\"font-size: smaller;\"><i>:&nbsp; The correct answers are marked with an asterisk!</i></span></div>',NULL,'2008-04-11 15:37:41',0,0,5,0,0,'','',0,1,3,0,'',1,1,1),(3,95,'My Lost Beach Ball','<h2>This is a demonstration Multiple Choice Quiz.</h2>\n<div><b>As you can see, LAMS provides a space for Instructions and Comments (here)<br />\nand lists all the questions and choices for the activity below.</b></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h4>Read the short story below and answer the three questions:</h4>\n<div style=\"text-align: center;\"><b><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">One day my Dad bought me a new <span style=\"color: rgb(255, 0, 0);\">red </span>beach ball.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">My brother and I took it down to the beach to play with.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">We were playing Soccer when he kicked the ball into the water, accidentally.</span><br />\n</b></div>\n<div style=\"text-align: center;\"><b><span style=\"font-family: Comic Sans MS;\">The ball floated out to sea before we could rescue it.</span></b></div>\n<div style=\"text-align: left;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><span style=\"font-size: smaller;\"><i> </i></span></div>\n<div style=\"text-align: center;\"><b><span style=\"font-size: smaller;\"><i>HINT</i></span></b><span style=\"font-size: smaller;\"><i>:&nbsp; The correct answers are marked with an asterisk!</i></span></div>',NULL,'2008-04-11 15:37:41',0,0,5,0,0,'','',0,1,3,0,'',1,1,1);
/*!40000 ALTER TABLE `tl_lamc11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_options_content`
--

DROP TABLE IF EXISTS `tl_lamc11_options_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_options_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `correct_option` tinyint(1) NOT NULL DEFAULT '0',
  `mc_que_content_id` bigint(20) NOT NULL,
  `mc_que_option_text` varchar(250) DEFAULT NULL,
  `displayOrder` int(5) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `mc_que_content_id` (`mc_que_content_id`),
  CONSTRAINT `FK_tl_lamc11_options_content_1` FOREIGN KEY (`mc_que_content_id`) REFERENCES `tl_lamc11_que_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamc11_options_content`
--

LOCK TABLES `tl_lamc11_options_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_options_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_options_content` VALUES (1,0,1,'Answer 1',1),(2,1,1,'Answer 2',2),(3,1,2,'Dad *',3),(4,0,2,'Mum',2),(5,0,2,'Santa Claus',1),(6,0,3,'Yellow',1),(7,1,3,'Red *',2),(8,0,3,'Green',3),(9,0,4,'Rugby',3),(10,1,4,'Soccer *',2),(11,0,4,'Cricket',1),(12,0,5,'Santa Claus',1),(13,0,5,'Mum',2),(14,1,5,'Dad *',3),(15,0,6,'Yellow',1),(16,1,6,'Red *',2),(17,0,6,'Green',3),(18,0,7,'Cricket',1),(19,1,7,'Soccer *',2),(20,0,7,'Rugby',3);
/*!40000 ALTER TABLE `tl_lamc11_options_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_que_content`
--

DROP TABLE IF EXISTS `tl_lamc11_que_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_que_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question` text,
  `mark` int(5) NOT NULL DEFAULT '1',
  `display_order` int(5) DEFAULT NULL,
  `mc_content_id` bigint(20) NOT NULL,
  `feedback` text,
  PRIMARY KEY (`uid`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc11_que_content_1` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_que_usr` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `que_usr_id` bigint(20) NOT NULL,
  `mc_session_id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `responseFinalised` tinyint(1) NOT NULL DEFAULT '0',
  `viewSummaryRequested` tinyint(1) NOT NULL DEFAULT '0',
  `last_attempt_order` int(11) DEFAULT NULL,
  `last_attempt_total_mark` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `mc_session_id` (`mc_session_id`),
  CONSTRAINT `FK_tl_lamc11_que_usr_1` FOREIGN KEY (`mc_session_id`) REFERENCES `tl_lamc11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `mc_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_name` varchar(100) DEFAULT NULL,
  `session_status` varchar(100) DEFAULT NULL,
  `mc_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_session_1` (`mc_session_id`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc_session_1` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamc11_session`
--

LOCK TABLES `tl_lamc11_session` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_session` DISABLE KEYS */;
INSERT INTO `tl_lamc11_session` VALUES (1,12,'2010-04-01 16:04:15',NULL,'LAMS Demo Course learners','INCOMPLETE',3);
/*!40000 ALTER TABLE `tl_lamc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lamc11_uploadedfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) NOT NULL,
  `mc_content_id` bigint(20) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  PRIMARY KEY (`submissionId`),
  KEY `mc_content_id` (`mc_content_id`),
  CONSTRAINT `FK_tl_lamc11_uploadedFile` FOREIGN KEY (`mc_content_id`) REFERENCES `tl_lamc11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamc11_usr_attempt` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `que_usr_id` bigint(20) NOT NULL,
  `mc_que_content_id` bigint(20) NOT NULL,
  `mc_que_option_id` bigint(20) NOT NULL,
  `attempt_time` datetime DEFAULT NULL,
  `isAttemptCorrect` tinyint(1) NOT NULL DEFAULT '0',
  `mark` varchar(255) DEFAULT NULL,
  `passed` tinyint(1) NOT NULL DEFAULT '0',
  `attemptOrder` int(11) NOT NULL DEFAULT '1',
  `finished` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `mc_que_content_id` (`mc_que_content_id`),
  KEY `mc_que_option_id` (`mc_que_option_id`),
  KEY `que_usr_id` (`que_usr_id`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_2` FOREIGN KEY (`mc_que_content_id`) REFERENCES `tl_lamc11_que_content` (`uid`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_3` FOREIGN KEY (`mc_que_option_id`) REFERENCES `tl_lamc11_options_content` (`uid`),
  CONSTRAINT `FK_tl_lamc11_usr_attempt_4` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_lamc11_que_usr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamc11_usr_attempt`
--

LOCK TABLES `tl_lamc11_usr_attempt` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_usr_attempt` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamc11_usr_attempt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_attachment`
--

DROP TABLE IF EXISTS `tl_lamind10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `mindmap_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_972920762_12090F57FC940906` (`mindmap_uid`),
  CONSTRAINT `FK_NEW_972920762_12090F57FC940906` FOREIGN KEY (`mindmap_uid`) REFERENCES `tl_lamind10_mindmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_attachment`
--

LOCK TABLES `tl_lamind10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamind10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_mindmap`
--

DROP TABLE IF EXISTS `tl_lamind10_mindmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_mindmap` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `multiuser_mode` bit(1) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `export_content` text,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_mindmap`
--

LOCK TABLES `tl_lamind10_mindmap` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_mindmap` DISABLE KEYS */;
INSERT INTO `tl_lamind10_mindmap` VALUES (1,NULL,NULL,NULL,'Mindmap','Instructions',0x00,0x00,0x00,'','',0x00,0x00,19,NULL,0x00,NULL);
/*!40000 ALTER TABLE `tl_lamind10_mindmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_node`
--

DROP TABLE IF EXISTS `tl_lamind10_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_node` (
  `node_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `unique_id` bigint(20) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `node_text` varchar(100) DEFAULT NULL,
  `node_color` varchar(6) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `mindmap_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`node_id`),
  KEY `MindmapNode_Parent_FK` (`parent_id`),
  KEY `MindmapNode_User_FK` (`user_id`),
  KEY `MindmapNode_Mindmap_FK` (`mindmap_id`),
  CONSTRAINT `MindmapNode_Mindmap_FK` FOREIGN KEY (`mindmap_id`) REFERENCES `tl_lamind10_mindmap` (`uid`),
  CONSTRAINT `MindmapNode_Parent_FK` FOREIGN KEY (`parent_id`) REFERENCES `tl_lamind10_node` (`node_id`),
  CONSTRAINT `MindmapNode_User_FK` FOREIGN KEY (`user_id`) REFERENCES `tl_lamind10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_node`
--

LOCK TABLES `tl_lamind10_node` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamind10_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_request`
--

DROP TABLE IF EXISTS `tl_lamind10_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_request` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `unique_id` bigint(20) DEFAULT NULL,
  `global_id` bigint(20) DEFAULT NULL,
  `request_type` tinyint(4) DEFAULT NULL,
  `node_id` bigint(20) DEFAULT NULL,
  `node_child_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `mindmap_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `mindmap_id` (`mindmap_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tl_lamind10_request_fk` FOREIGN KEY (`mindmap_id`) REFERENCES `tl_lamind10_mindmap` (`uid`),
  CONSTRAINT `tl_lamind10_request_fk1` FOREIGN KEY (`user_id`) REFERENCES `tl_lamind10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_request`
--

LOCK TABLES `tl_lamind10_request` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamind10_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_session`
--

DROP TABLE IF EXISTS `tl_lamind10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `mindmap_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_972920762_B7C198E2FC940906` (`mindmap_uid`),
  CONSTRAINT `FK_NEW_972920762_B7C198E2FC940906` FOREIGN KEY (`mindmap_uid`) REFERENCES `tl_lamind10_mindmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_session`
--

LOCK TABLES `tl_lamind10_session` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamind10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_user`
--

DROP TABLE IF EXISTS `tl_lamind10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lamind10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `mindmap_session_uid` bigint(20) DEFAULT NULL,
  `entry_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_972920762_CB8A58FFA3B0FADF` (`mindmap_session_uid`),
  CONSTRAINT `FK_NEW_972920762_CB8A58FFA3B0FADF` FOREIGN KEY (`mindmap_session_uid`) REFERENCES `tl_lamind10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lamind10_user`
--

LOCK TABLES `tl_lamind10_user` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamind10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_attachment`
--

DROP TABLE IF EXISTS `tl_lanb11_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lanb11_attachment` (
  `attachment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nb_content_uid` bigint(20) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `uuid` bigint(20) NOT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `online_file` tinyint(1) NOT NULL,
  PRIMARY KEY (`attachment_id`),
  KEY `nb_content_uid` (`nb_content_uid`),
  CONSTRAINT `FK_tl_lanb11_attachment_1` FOREIGN KEY (`nb_content_uid`) REFERENCES `tl_lanb11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lanb11_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `nb_content_id` bigint(20) NOT NULL,
  `title` text,
  `content` text,
  `online_instructions` text,
  `offline_instructions` text,
  `define_later` tinyint(1) DEFAULT NULL,
  `force_offline` tinyint(1) DEFAULT NULL,
  `reflect_on_activity` tinyint(1) DEFAULT NULL,
  `reflect_instructions` text,
  `content_in_use` tinyint(1) DEFAULT NULL,
  `creator_user_id` bigint(20) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `date_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `nb_content_id` (`nb_content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lanb11_content`
--

LOCK TABLES `tl_lanb11_content` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_content` DISABLE KEYS */;
INSERT INTO `tl_lanb11_content` VALUES (1,2,'Noticeboard','Content','','',0,0,0,'Reflect on noticeboard',0,NULL,'2010-04-01 15:56:25',NULL),(2,23,'Welcome!','<p><font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\">These activities will introduce you to how fantastic your brain is. </font></p>\n<p><font size=\"3\" face=\"Verdana\">In this session you will:</font></p>\n<ul>\n    <li><font size=\"3\" face=\"Verdana\"> learn facts about your brain;  <br />\n    </font></li>\n    <li><font size=\"3\" face=\"Verdana\">play a games to learn about memory;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">play a game to learn about concentration;<br />\n    </font></li>\n    <li><font size=\"3\" face=\"Verdana\">learn about things you can do to improve how brain works;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">learn about things you can do to improve your learning.</font></li>\n</ul>\n<font size=\"3\" face=\"Verdana\"><br />\nAfter this session you will:<br />\n<br />\n</font>\n<ul>\n    <li><font size=\"3\" face=\"Verdana\">have a clearer understand about the workings of the brain;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">have an understanding of how good your memory is;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">have an understanding of how good your concentration is;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">understand what your brain needs to function properly;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">understand you changes you can make to improve the function of your brain.<br />\n    </font></li>\n</ul>\n<font size=\"3\" face=\"Verdana\"><br />\n</font>','','',0,0,0,'',0,9,'2007-01-30 14:23:27','2007-04-05 11:00:54'),(3,24,'Brain Needs','<p><font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\">In this activity you will read about what your brain needs to function at its best and create an action plan of things that you can do to help your brain to work most effectively.</font></p>\n<p>&nbsp;</p>\n<p><font size=\"3\" face=\"Verdana\"></font></p>\n<img width=\"380\" height=\"320\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/brain.jpg\" alt=\"\" />\n<p>&nbsp;</p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>','','',0,0,0,'',0,16,'2007-01-30 14:23:30','2007-02-28 10:17:52'),(4,33,'LAMS Activities','<div>\n<div><img width=\"116\" height=\"103\" align=\"left\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\nSo you&rsquo;ve had a look at the <b>Authoring Environment - </b>but what are the next steps?</div>\n<div>&nbsp;</div>\n<div>This LAMS sequence will show you each of the activities available for use in LAMS 2.1, and techniques for getting the most out of their use.</div>\n<div>&nbsp;</div>\n<div><span style=\"background-color: rgb(255, 255, 153);\"><b><i>This activity also discusses NEW activities added in the 2.2 release.</i></b></span></div>\n<div>&nbsp;</div>\n<h2>&nbsp;</h2>\n<h2 style=\"text-align: center;\">Activity Types</h2>\n<div>Earlier, we discussed the four different types of LAMS activities:&nbsp;<span class=\"Apple-style-span\" style=\"word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; orphans: 2; widows: 2; font-size-adjust: none; font-stretch: normal;\"><center>\n<h4><span style=\"font-size: small;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(128, 0, 128);\"><b>Assessement,</b></span></span>&nbsp; <span style=\"color: rgb(0, 0, 0);\">and </span><span style=\"font-size: small;\"><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span>&nbsp;</h4>\n</center><center>\n<div style=\"text-align: left;\">\n<div style=\"text-align: left;\">The rest of this sequence will let you view demonstrations of each of the LAMS activities, and briefly discuss how they might be best used in a lesson.</div>\n</div>\n</center></span></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div style=\"text-align: right;\">&nbsp;<b><i>Click &rsquo;Next Activity&rsquo; to move on.</i></b></div>','','',0,0,0,'',0,8,'2008-04-10 11:56:54','2008-11-14 13:14:33'),(5,34,'Informative Tools','<div>The <span style=\"color: rgb(255, 102, 0);\"><b>Informative </b></span>tools in LAMS allow Authors and Monitors to share information with Learners, and (in the case of Share Resources) allow Learners to share information with each other.</div>\n<div>&nbsp;</div>\n<div>Informative tools display on the Authoring Canvas with an <span style=\"color: rgb(255, 102, 0);\">orange icon</span>.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Noticeboard</h2>\n<div>The <b>Noticeboard </b>provides a simple way of providing information and content to the learners. The activity can display text, images, audio, video, links and other HTML content.</div>\n<div>&nbsp;</div>\n<h5>Example: The activity you&rsquo;re looking at right now is a Noticeboard!</h5>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//larsrc11.PNG\" />Share Resources</h2>\n<div><b>Resource Sharing</b> allows Authors to create lists of resources such as:</div>\n<div>&nbsp;</div>\n<ul>\n    <li>URL hyperlinks</li>\n    <li>Complete websites - created and uploaded to LAMS as a .zip file</li>\n    <li>Individual files, such as documents, PDF&rsquo;s, Audio and Video files</li>\n    <li>and even IMS content packages.</li>\n</ul>\n<div>&nbsp;</div>\n<div>The tool can also be set-up so that Learners can add their own resources for other learners to see.&nbsp;</div>\n<div>&nbsp;</div>\n<h5><span style=\"font-weight: bold;\">Example: The animations in these &rsquo;Getting Started&rsquo; were added to LAMS by creating a zipped website and uploading the file into a Share Resources activity.</span></h5>\n<div>&nbsp;</div>\n<hr />\n<h4 style=\"text-align: left;\">Coming Up Next...</h4>\n<ol>\n    <li style=\"text-align: left;\">Advanced Noticeboard Features</li>\n    <li style=\"text-align: left;\">Share Resources Demonstration</li>\n    <li style=\"text-align: left;\"><span style=\"background-color: rgb(255, 255, 153);\">Spreadsheet Demonstration (New to LAMS 2.2)</span></li>\n    <div style=\"text-align: left;\">&nbsp;</div>\n</ol>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:07:18','2008-11-14 13:14:13'),(6,35,'Noticeboard Activity','<h2>Advanced Noticeboard Tricks</h2>\n<div><b>You can use the noticeboard to embed any web object using HTML.</b></div>\n<h3>For example</h3>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"1\">\n    <tbody>\n        <tr>\n            <td><center>\n            <h4>YouTube Videos</h4>\n            <div><embed src=\"http://www.youtube.com/v/vihvpfXw_C8\" type=\"application/x-shockwave-flash\"></embed></div>\n            </center></td>\n            <td><center>\n            <h4>MP3 Files</h4>\n            <div><embed width=\"200\" height=\"40\" type=\"audio/mpeg\" autostart=\"false\" src=\"http://saturn.melcoe.mq.edu.au/lamspodcast/James_Dalziel_Getting_Best_out_of.mp3\"></embed>\n            <p align=\"center\"><strong>&nbsp;LAMS PODCAST</strong> with <strong>ALAN CARRINGTON</strong> (CLPD Adelaide Uni)<br />\n            and <strong>JAMES DALZIEL</strong> (MELCOE, Macquarie Uni)</p>\n            </div>\n            </center></td>\n        </tr>\n    </tbody>\n</table>\n<h4>&nbsp;</h4>\n<hr />\n<h4>And Even Complete External Websites</h4>\n<center>\n<div><iframe width=\"95%\" height=\"278\" src=\"http://www.melcoe.mq.edu.au/\">LAMS Community</iframe></div>\n<div>&nbsp;</div>\n</center><hr />\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration &rsquo;Share Resources&rsquo; activity.</b></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:35:26','2008-11-14 13:13:28'),(7,37,'CONGRATULATIONS!','<h2><img width=\"240\" height=\"240\" align=\"right\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/diploma.jpeg\" alt=\"\" />!!! CONGRATULATIONS !!!</h2>\n<h4>You&rsquo;ve finished LAMS101:Getting Started!</h4>\n<div>&nbsp;</div>\n<div><b>Get out there and start making sequences!</b></div>\n<div>&nbsp;</div>\n<div>Or, if you want some more ideas,<br />\ncheck out the sequence-sharing goodness<br />\nat the LAMS Community Website.</div>\n<div>&nbsp;</div>\n<div><a target=\"_blank\" href=\"http://lamscommunity.org/\"><b>http://lamscommunity.org/</b></a></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,4,'2008-04-11 16:49:00','2008-11-10 12:54:33'),(8,38,'Assessment Activities','<div>The <b><span style=\"color: rgb(128, 0, 128);\">Assessment </span></b>tools in LAMS provide a vehicle for sequence monitors to collect Learner responses to a topic.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lamc11.PNG\" alt=\"\" /><span style=\"color: rgb(128, 0, 128);\">Multiple Choice</span></h2>\n<div>&nbsp;The <b>Multiple Choice Quiz</b> delivers a series of Multiple Choice questions to learners, and the advanced features of this tool provide a high level of customisation for Authors as to how the quiz is administered.</div>\n<div>&nbsp;</div>\n<div>When the Quiz is complete, LAMS will automatically tally the marks from each learner and display the statistics to sequence monitors.</div>\n<div>&nbsp;</div>\n<div>Authors can also choose to release Learner marks <i>directly </i>after they have completed the quiz, as a type of <b>formative </b>assessment or <i>delay</i> releasing the marks until a Sequence Monitor chooses to release them, for <b>Summative Assessment</b>. The Activity also allows learners to view the top, bottom and average marks from the quiz.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasbmt11.PNG\" /><span style=\"color: rgb(128, 0, 128);\">Submit Files </span></h2>\n<div>The <b>Submit Files </b>Activity provides a vehicle for Authors to collect Student responses which are created in software outside of LAMS.</div>\n<div>&nbsp;</div>\n<div>This could be an essay in .DOC or .PDF format, a digital presentation, or some sort of multimedia file.</div>\n<div>&nbsp;</div>\n<div>Learners log in to LAMS, and are able to upload their file to the LAMS server as an attachment.&nbsp; Sequence Monitors can view these files, comment on them and give each individual file a mark, which is in turn accessible by Learners.</div>\n<div>&nbsp;</div>\n<div>Like the MCQ, sequence authors can choose to allow learners several attempts at submission, allowing for feedback and creating a formative assessment style, or alternatively, lock the activity after submission, which creates a summative assessment style.</div>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\">The next activity is a demonstration <b>Multiple Choice </b>quiz,<br />\nfollowed by a demonstration <b>Submit Files</b> activity.</div>','','',0,0,0,'',0,8,'2008-04-10 14:39:45','2008-11-14 13:14:54'),(9,40,'DimDim Web Conferencing','<div style=\"text-align: center;\">Also available for use in LAMS 2.2 is the <b>DimDim Web Conferencing Activity</b>.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The documentation is included below (You will need internet access to view this page.)</div>\n<hr />\n<div>&nbsp;</div>\n<div align=\"center\"><iframe width=\"95%\" height=\"400\" src=\"http://wiki.lamsfoundation.org/display/lamsdocs/laddim10?decorator=printable\">LAMS Community</iframe></div>','','',0,0,0,'',0,4,'2008-11-10 12:45:56','2008-11-10 12:48:40'),(10,41,'Reflective Activities','<div>Reflective tools ask learners to analyse their own learning throughout a sequence,<br />\nor alternatively, collects responses from students and asks them to consider trends in the resulting data.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//laqa11.PNG\" /> Question and Answer</h2>\n<div>This activity creates a series of Questions and Short-Answer responses for Learners.</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">The sequence Author can pen their own responses to each question, which are displayed to learners after they have submitted their own answers.</p>\n<p style=\"margin-bottom: 0cm;\">The activity can also be configured to display answers collected from all learners in the sequence or group.</p>\n<p style=\"margin-bottom: 0cm;\">The <b>Question and Answer Activity</b> allows teachers to pose a question or questions to learners individually, and after they have entered their response, to see the responses of all their peers presented on a single answer screen.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasurv11.PNG\" /> Survey</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The Survey Tool is similar to Multiple Choice, in that it presents learners with a number of questions and collects their responses. However, unlike Multiple Choice there are no right or wrong answers - the system just collates all answers for analysis by the teacher in the monitoring area.</p>\n<p style=\"margin-bottom: 0cm;\">Question types include single response, multiple response and open text entry, and questions can be mandatory or optional.  For multiple choice questions, Learners can also be provided with the option of adding their own answer to a list.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lavote11.PNG\" /> Voting</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Voting Activity</b> allows teachers to provide learners with a list of options to &quot;Vote&quot; on. It is similar to the Q&amp;A Activity in that Voting shows first one screen with all the available voting options, then a screen showing the individual learner&rsquo;s selection, then a screen showing <i>&quot;progressive&quot;</i> voting results (i.e., a non-editable screen where learners can see results from themselves and other learners over time). Finally, there is Summary Screen of group results.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\">The next three activities demonstrate the Q&amp;A, Survey and Voting activities.</h5>\n</div>\n</div>','','',0,0,0,'',0,8,'2008-04-11 16:26:32','2008-11-14 14:30:21'),(11,42,'Collaborative Activities','<div>The <b><span style=\"color: rgb(255, 153, 0);\">Collaborative </span></b>tools in LAMS allow Learners to team up and solve problems, discuss ideas or share solutions together.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2>&nbsp;<span style=\"color: rgb(255, 153, 0);\"><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lachat11.PNG\" alt=\"\" /> Chat Activity</span></h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Chat Activity</b> runs a live (synchronous) discussion for learners.<br />\nThis is similar to <i>Instant Messaging</i> (IM) software that most learners will be familiar with.</p>\n</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lafrum11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Forum Activity</span></h2>\n<div>&nbsp;</div>\n<div>&nbsp;The <b>Forum Activity</b> provides an asynchronous discussion environment for learners, with discussion threads initially created by the teacher.\n<p style=\"margin-bottom: 0cm;\">Forums can be &rsquo;locked&rsquo; as an activity which is only available for the period of the specific activity (i.e.: no more contributions can be made once the learner clicks &rsquo;Finish&rsquo;, or they can be &rsquo;unlocked&rsquo;, in that learners can add to the Forum throughout the life of the whole sequence.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"126\" height=\"53\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lascrb11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Scribe Activity</span></h2>\n<div>&nbsp;The <b>Scribe Activity</b> is, by default, not enabled as a stand-alone activity.<br />\n<p style=\"margin-bottom: 0cm;\">It allows a group of Learners to collaborate and create a written report with one Learner as the &rsquo;Scribe,&rsquo; or Writer for the report. The activity is most effective when paired with another collaborative activity. <em class=\"western\">In the current release of LAMS (2.2) Scribe is available to be paired with either Chat or Forum.</em></p>\n<h3>Using the Scribe Activity</h3>\n<p style=\"margin-bottom: 0cm;\">The Scribe Activity should always used in conjunction with another activity that gives it context. For instance, if used with Chat, then the Scribe (one particular learner) is expected to collect or summarize the output of the Chat discussion into a succinct set of sentences. Once the scribe has done this, they publish the summary to the rest of the learners who vote whether they agree with the summary as proposed by the scribe. If not, they can ask the scribe to modify the report until all learners agree on the report.</p>\n<p style=\"margin-bottom: 0cm;\">Once the Scribe submits the report, the other learners in the group or class can agree or disagree with the summary. The scribe can finished the scribe task at any time regardless of the number of learners that have agreed with the proposed summaries. The Scribe task can also be finished through the monitoring environment by a teacher or instructor.</p>\n<p style=\"margin-bottom: 0cm;\">Once the scribe task is finished a report of the statements and summary is presented to all learners.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>','','',0,0,0,'',0,8,'2008-04-10 15:41:45','2008-11-14 13:16:43'),(12,45,'Split-Screen Activities','<h2 style=\"text-align: center;\">There are Several Split-Screen activities available to authors<br />\nthat combine several activities on the same screen.</h2>\n<hr />\n<div>&nbsp;</div>\n<div>\n<h2><img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//chatScribe.png\" alt=\"\" /> Chat and Scribe</h2>\n<div>This activity is great for collecting Group Reports.</div>\n<div>&nbsp;</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">It combines a <b><i>Chat</i> Activity </b>with a <b>Scribe Activity</b> for collating the chat group\'s view on questions created by the teacher. When used in small group mode, the tool creates parallel Chat and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</p>\n</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div><br />\n<img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//forumScribe.png\" alt=\"\" />\n<h2>Forum and Scribe</h2>\n<div>The activity ombines a <i>Forum</i> Activity with a <i>Scribe Activity</i> for collating Forum Postings into a written report, which then must be approved by the entire group.</div>\n<div>&nbsp;</div>\n<div>When used in small group mode, the tool creates parallel Forum and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n<h2><img width=\"144\" hspace=\"6\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//shareForum.png\" alt=\"\" />Resource Sharing and Forum</h2>\n<div>This activity allows Learners to submit files and/or URLS to a page viewable by their class or group.</div>\n<div>&nbsp;</div>\n<div>The group then discuss the resources in the Forum.</div>\n<div>&nbsp;</div>\n<div>This is a great way to get Learner\'s evaluating each other\'s work, or to foster some collaborative online research.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration Resource Sharing and Forum activity.</b></div>','','',0,0,0,'',0,1,'2008-04-10 16:29:19','2008-04-11 15:59:24'),(13,51,'Noticeboard','<h2>New Collaborative Activities in LAMS 2.2</h2>\n<h3><b><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmapIcon.png\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Google Maps</span></b></h3>\n<div>The Google Maps tool allows sequence Authors to create maps or satellite images with annotated place markers.&nbsp; During the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><i>A demonstration Gmap activity is coming up.</i></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/wikiIcon.png\" alt=\"\" /> <b><span style=\"color: rgb(255, 153, 0);\">Wiki Activity</span></b></div>\n<div>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>&nbsp;Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p><i>A Wiki Demonstration activity is coming up, which you can explore and edit.</i>&nbsp;</p>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><i><br />\n</i></b></div>','','',0,0,0,'',0,8,'2008-11-14 13:48:47','2008-11-14 13:50:27'),(14,52,'Notebook Activity','<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lantbk11.PNG\" alt=\"\" />Notebook Activity</h2>\n<div>The next LAMS activity to look at is the Notebook activity.</div>\n<div>&nbsp;</div>\n<div>This activity is a bit special because it can be <b>attached to any LAMS activity</b><b> at any point in the lesson.</b><br />\nEach LAMS activity has an option in <i><b>Advanced Properties</b></i> called <i>&quot;attach Notebook with text...&quot;</i></div>\n<div>&nbsp;</div>\n<div>Basically, this allows sequences authors to get Learners to reflect on their knowledge at <b>any point in the sequence - </b>and it&rsquo;s also really easy for monitors to add a notebook to any activity in a sequence without mucking around with transition lines or changing the shape of the sequence.</div>\n<div>&nbsp;</div>\n<div>Of course, the notebook can also be included in sequences as a standalone tool.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-11 16:41:59','2008-11-14 13:51:16'),(15,56,'Data Collection Activity','<h2><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/dacoIcon.png\" alt=\"\" /> Data Collection</h2>\n<div>&nbsp;</div>\n<div><span style=\"border-collapse: separate; color: rgb(0, 0, 0); font-family: Verdana; font-size: 11px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 16px; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px;\" class=\"Apple-style-span\">The Data Collection tool allows Authors to create database style lists for Learners to populate.<br />\nEach record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</span></div>\n<div><b><i>The next Activity demonstrates the use of Data Collection</i></b>.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-11-14 14:26:47','2008-11-14 14:26:47'),(16,58,'LAMS Environments','<div><img width=\"100\" height=\"90\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image/dolly_lrg.gif\" alt=\"\" style=\"margin-right: 10px;\" /></div>\n<h2>There are four main environments in LAMS</h2>\n<h3>The Learning Environment</h3>\n<div>\n<div>The <b>Learning Environment</b> is the <i>&rsquo;doing&rsquo;</i> of LAMS.</div>\n<div>This is what Learners will see when they log in to LAMS and start participating in a sequence.</div>\n<div>The Learning Environment displays the sequences that are created in <b>Authoring.</b></div>\n<h3>&nbsp;</h3>\n<hr />\n<h3>The Authoring Environment</h3>\n<div>The Authoring Environment is where sequences are put together, modified and shared.</div>\n<div>It contains all of the LAMS activities, and is where authors create their lesson designs.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The Monitoring Environment</h3>\n<div><b>Monitors</b><b> </b>are not necessarily the people who Authored the sequence in the first place - they simply <b>run</b> the lesson for learners, and supervise each learner&rsquo;s progress through the lesson. In practice, Teachers are often both Authors and Monitors for their class(es) of students.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The System Administration Environment</h3>\n<div><b>System Administration</b> is for <b>Technical Management</b> of a LAMS server.</div>\n<div>&nbsp;</div>\n<div>From here, users with Administration rights can change nearly every aspect of the LAMS software.&nbsp;</div>\n<h4 style=\"text-align: center;\"><a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Home\" target=\"_new\">LAMS System Administration is discussed in detail on the Documentation Website.<br />\nClick here for details [Online Link].</a></h4>\n<div>&nbsp;</div>\n<hr />\n</div>\n</div>\n</div>\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\"><b>The next activity will allow you to choose from animated tutorials on each three environments.</b></h5>','','',0,0,0,'',0,1,'2008-03-27 15:12:18','2008-06-05 11:47:45'),(17,59,'The Learning Environment','<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>You&rsquo;re Using the Learning Environment Right Now!<br />\n</b></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">... but if you want to check out more of the features,<br />\nview the following animations!</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><br />\nThe First Animation demonstrates a Learner moving through a sequence.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The Second Animation deals with the new <b>&quot;no-flash&quot; interface</b><b>&nbsp;</b>for the Learning Environment</div>','','',0,0,0,'',0,11,'2008-04-11 17:34:57','2008-05-29 17:16:33'),(18,60,'Are You Sure?','<h2 style=\"text-align: center;\"><span style=\"font-size: small;\">You&rsquo;ve reached the end of this sequence... but are you sure you want to leave?</span></h2>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" align=\"center\">\n    <tbody>\n        <tr>\n            <td><span style=\"font-size: small;\"><img width=\"160\" vspace=\"2\" hspace=\"2\" height=\"377\" border=\"1\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image//progbar.png\" alt=\"\" /><br />\n            </span></td>\n            <td>.      .</td>\n            <td>\n            <div><span style=\"font-size: small;\"> You may have clicked the <b>Next Activity</b> button on the last screen,</span> <span style=\"font-size: small;\">which would bring you here.</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If so, and you want to go back and view more of this sequence, double-click the Icon for the <b>Optional Activity </b>in your <b>Progress Bar </b>(see the graphic at right.)</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">Once there, select an option, and click the <b>Choose </b>button.</span></div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If you really have finished, and want to leave the sequence, click <b>Next Activity</b> below.<br />\n            </span></div>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp; <br />\n</span></div>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,1,'2008-05-02 11:42:23','2008-05-02 12:05:42'),(19,65,'The Structure of LAMS','<h4 style=\"text-align: center;\">LAMS is arranged hierarchically, with sequences organised into Groups and Subgroups.</h4>\n<h2>What does this mean?</h2>\n<div><b>Think of it like this:</b></div>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\">\n    <tbody>\n        <tr>\n            <td>\n            <h4 style=\"text-align: center;\">In Real Life</h4>\n            </td>\n            <td>\n            <h4 style=\"text-align: center;\">In Lams</h4>\n            </td>\n        </tr>\n        <tr>\n            <td>\n            <ul>\n                <li>A <b>Professor</b> teaches a <b>Course, </b>and organises smaller <b>classes.</b></li>\n                <li>Individual <b>Tutors </b>teaches a smaller <b>Class </b>taken from the whole course.</li>\n                <li>A <b>Course </b>has at least one lesson plan, that is applied to all students in the course.</li>\n                <li>The smaller classes can have at least <b>one specific lesson plan </b>for those students.</li>\n            </ul>\n            </td>\n            <td>\n            <ul>\n                <li>A <b>Course Manager</b> creates a main <b>Group, </b>and&nbsp;can create several <b>SubGroups.</b></li>\n                <li><b>Author/Monitors </b>are assigned to one or many <b>Subgroups.</b></li>\n                <li>The <b>Group</b> contains one or more <b>Sequences </b>for all <b>Learners </b>in the <b>Group.</b></li>\n                <li>Likewise, the <b>Subgroup</b> can contain one or many <b>Sequences</b> for those <b>Learners.</b></li>\n            </ul>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>There is also the role of <b>Course Administrator.</b> This role allows the user to modify Groups, Subgroups and Learners inside a course. In practice, this is a secretarial role designed to assist the educator running the course.</div>\n<div>&nbsp;</div>\n<hr />\n<h2>A Working Example</h2>\n<div style=\"text-align: center;\"><span style=\"font-size: small;\"><b>&nbsp;This image shows the different sections that make up a Group in LAMS.</b></span></div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><img border=\"2\" align=\"baseline\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//annotated.png\" alt=\"\" style=\"width: 548px; height: 418px;\" /></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i>Click <b>Next Activity </b>to continue.</i></div>','','',0,0,0,'',0,5,'2008-05-02 13:38:25','2008-06-06 12:09:41'),(20,67,'LAMS101 - Lesson One','<div><span class=\"Apple-style-span\" style=\"orphans: 2; word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); widows: 2; text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; font-size-adjust: none; font-stretch: normal;\">\n<h2>So What is LAMS?</h2>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"><b>Congratulations</b> - if you can see this sequence, LAMS is up and running on your server. You&rsquo;ve also figured out how to log in, and participate in a &quot;sequence&quot; - that&rsquo;s what we call a collection of LAMS activities.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"> </span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">In a word, that&rsquo;s what LAMS does - it is a&nbsp;<b>S</b>ystem that&nbsp;<b>M</b>anages sequences of <b>L</b>earning <b>A</b>ctivities. &nbsp;A <b>Sequence </b>is an online digital lesson that simulates the classroom environment in a virtual space.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are first created by <b>Authors, </b>and are then run for different groups of <b>Learners </b>while a <b>Monitor</b> supervises. &nbsp;We&rsquo;ll look more at these <b>Roles </b>in LAMS in Lesson Two of LAMS101.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are made up of activities - each of which has a particular function, either:</span></p>\n<span style=\"font-size: small;\">\n<h4 style=\"text-align: center;\"><span style=\"font-size: medium;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative &nbsp;</b></span><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative &nbsp;</b></span><span style=\"color: rgb(128, 0, 128);\"><b>Assessment &nbsp;</b></span><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span></h4>\n</span>\n<p><span style=\"font-size: small;\">Sequences can include a range of individual tasks, small group work and whole class activities based on both content and collaboration.</span></p>\n<p><span style=\"font-size: small;\">When you use LAMS, not only are you creating online activities for your learners, you also create a structured lesson plan at the same time.&nbsp; We&rsquo;ll talk about this <b>Visual Authoring Environment </b>in a few moments.</span></p>\n<h2>... and what&rsquo;s with the Sheep?</h2>\n<b>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><img width=\"116\" vspace=\"12\" hspace=\"10\" height=\"103\" border=\"0\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image/dolly_sml.gif\" alt=\"dolly the sheep\" /></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Dolly is our LAMS mascot.&nbsp; Back when LAMS was being redesigned for version 2.0, it was the &quot;next-generation&quot; of LAMS.&nbsp; Dolly the sheep was the first mammal to have been successfully cloned from an adult cell, therefore, she was the next generation LAMB (see what we did there?).</p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Our marvelous designer Shelly is the official mother of Dolly and also our <span class=\"nobr\"><a href=\"http://demo.lamscommunity.org/lams/images/css/lams_login_bar.jpg\" rel=\"nofollow\">front page model</a></span>. So all kudos to her!</p>\n<hr />\n&nbsp;</b></span></div>\n<div style=\"text-align: right;\"><i>To progress to the next activity in this sequence, click the button down here.</i></div>','','',0,0,0,'Reflect on noticeboard',0,1,'2008-03-12 16:38:33','2008-06-05 09:52:50'),(21,68,'Creating Your Own Sequences','<h2>LAMS Authoring</h2>\n<div><span style=\"font-size: small;\">LAMS provides you with a <b>visual authoring environment </b>for creating, storing and re-using activity sequences. The drag-and-drop interface is central to the LAMS authoring environment. Teachers drag-and-drop activities into the authoring area and then join the activities together by drawing &quot;Transitions&quot; to produce a learning sequence. This workflow model is what principally distinguishes LAMS from other more content based online learning platforms. LAMS has a wide range of tools designed for use in a range of educational contexts by teachers and students with varying levels of technical expertise.</span></div>\n<h3>Benefits of LAMS</h3>\n<div><span style=\"font-size: small;\">Creating a lesson in LAMS means that:</span></div>\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<ul>\n    <li><span style=\"font-size: small;\"><b>You can plan all aspects of the lesson.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can preview the lesson from the perspective of your learners.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You create a visual overview of the lesson, and can easily identify the learning styles addressed.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can create a standardised set of learning templates that can easily be modified for later use.</b> </span></li>\n</ul>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i><img width=\"116\" height=\"103\" align=\"right\" alt=\"\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//dolly_sml.gif\" /></i>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next animation will demonstrate creating a lesson in LAMS.</b></div>','','',0,0,0,'',0,11,'2008-04-03 16:11:54','2008-05-29 16:55:57'),(22,70,'The tip of the iceberg...','<p><span style=\"font-size: small;\">LAMS has many, <b>many</b> more functions that you can explore.</span></p>\n<p><span style=\"font-size: small;\">The new release of version 2.1 includes increased functionality in many areas, some of which are explored in the rest of this <b>Getting Started</b> course.</span></p>\n<h2 style=\"text-align: center;\"><b>We Hope You Enjoy Exploring LAMS!</b></h2>\n<p style=\"text-align: center;\">&nbsp;</p>\n<div>&nbsp;</div>','','',0,0,0,'',0,1,'2008-04-04 11:30:06','2008-06-05 11:45:40'),(23,74,'Welcome!','<p><font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\">These activities will introduce you to how fantastic your brain is. </font></p>\n<p><font size=\"3\" face=\"Verdana\">In this session you will:</font></p>\n<ul>\n    <li><font size=\"3\" face=\"Verdana\"> learn facts about your brain;  <br />\n    </font></li>\n    <li><font size=\"3\" face=\"Verdana\">play a games to learn about memory;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">play a game to learn about concentration;<br />\n    </font></li>\n    <li><font size=\"3\" face=\"Verdana\">learn about things you can do to improve how brain works;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">learn about things you can do to improve your learning.</font></li>\n</ul>\n<font size=\"3\" face=\"Verdana\"><br />\nAfter this session you will:<br />\n<br />\n</font>\n<ul>\n    <li><font size=\"3\" face=\"Verdana\">have a clearer understand about the workings of the brain;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">have an understanding of how good your memory is;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">have an understanding of how good your concentration is;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">understand what your brain needs to function properly;</font></li>\n    <li><font size=\"3\" face=\"Verdana\">understand you changes you can make to improve the function of your brain.<br />\n    </font></li>\n</ul>\n<font size=\"3\" face=\"Verdana\"><br />\n</font>','','',0,0,0,'',0,9,'2007-01-30 14:23:27','2007-04-05 11:00:54'),(24,79,'Brain Needs','<p><font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>\n<p><font size=\"3\" face=\"Verdana\">In this activity you will read about what your brain needs to function at its best and create an action plan of things that you can do to help your brain to work most effectively.</font></p>\n<p>&nbsp;</p>\n<p><font size=\"3\" face=\"Verdana\"></font></p>\n<img width=\"380\" height=\"320\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/brain.jpg\" alt=\"\" />\n<p>&nbsp;</p>\n<p><font size=\"3\" face=\"Verdana\"><br />\n</font></p>','','',0,0,0,'',0,16,'2007-01-30 14:23:30','2007-02-28 10:17:52'),(25,83,'DimDim Web Conferencing','<div style=\"text-align: center;\">Also available for use in LAMS 2.2 is the <b>DimDim Web Conferencing Activity</b>.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The documentation is included below (You will need internet access to view this page.)</div>\n<hr />\n<div>&nbsp;</div>\n<div align=\"center\"><iframe width=\"95%\" height=\"400\" src=\"http://wiki.lamsfoundation.org/display/lamsdocs/laddim10?decorator=printable\">LAMS Community</iframe></div>','','',0,0,0,'',0,4,'2008-11-10 12:45:56','2008-11-10 12:48:40'),(26,84,'LAMS Activities','<div>\n<div><img width=\"116\" height=\"103\" align=\"left\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\nSo you&rsquo;ve had a look at the <b>Authoring Environment - </b>but what are the next steps?</div>\n<div>&nbsp;</div>\n<div>This LAMS sequence will show you each of the activities available for use in LAMS 2.1, and techniques for getting the most out of their use.</div>\n<div>&nbsp;</div>\n<div><span style=\"background-color: rgb(255, 255, 153);\"><b><i>This activity also discusses NEW activities added in the 2.2 release.</i></b></span></div>\n<div>&nbsp;</div>\n<h2>&nbsp;</h2>\n<h2 style=\"text-align: center;\">Activity Types</h2>\n<div>Earlier, we discussed the four different types of LAMS activities:&nbsp;<span class=\"Apple-style-span\" style=\"word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; orphans: 2; widows: 2; font-size-adjust: none; font-stretch: normal;\"><center>\n<h4><span style=\"font-size: small;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative,</b></span></span>&nbsp; <span style=\"font-size: small;\"><span style=\"color: rgb(128, 0, 128);\"><b>Assessement,</b></span></span>&nbsp; <span style=\"color: rgb(0, 0, 0);\">and </span><span style=\"font-size: small;\"><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span>&nbsp;</h4>\n</center><center>\n<div style=\"text-align: left;\">\n<div style=\"text-align: left;\">The rest of this sequence will let you view demonstrations of each of the LAMS activities, and briefly discuss how they might be best used in a lesson.</div>\n</div>\n</center></span></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div style=\"text-align: right;\">&nbsp;<b><i>Click &rsquo;Next Activity&rsquo; to move on.</i></b></div>','','',0,0,0,'',0,8,'2008-04-10 11:56:54','2008-11-14 13:14:33'),(27,86,'Informative Tools','<div>The <span style=\"color: rgb(255, 102, 0);\"><b>Informative </b></span>tools in LAMS allow Authors and Monitors to share information with Learners, and (in the case of Share Resources) allow Learners to share information with each other.</div>\n<div>&nbsp;</div>\n<div>Informative tools display on the Authoring Canvas with an <span style=\"color: rgb(255, 102, 0);\">orange icon</span>.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Noticeboard</h2>\n<div>The <b>Noticeboard </b>provides a simple way of providing information and content to the learners. The activity can display text, images, audio, video, links and other HTML content.</div>\n<div>&nbsp;</div>\n<h5>Example: The activity you&rsquo;re looking at right now is a Noticeboard!</h5>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//larsrc11.PNG\" />Share Resources</h2>\n<div><b>Resource Sharing</b> allows Authors to create lists of resources such as:</div>\n<div>&nbsp;</div>\n<ul>\n    <li>URL hyperlinks</li>\n    <li>Complete websites - created and uploaded to LAMS as a .zip file</li>\n    <li>Individual files, such as documents, PDF&rsquo;s, Audio and Video files</li>\n    <li>and even IMS content packages.</li>\n</ul>\n<div>&nbsp;</div>\n<div>The tool can also be set-up so that Learners can add their own resources for other learners to see.&nbsp;</div>\n<div>&nbsp;</div>\n<h5><span style=\"font-weight: bold;\">Example: The animations in these &rsquo;Getting Started&rsquo; were added to LAMS by creating a zipped website and uploading the file into a Share Resources activity.</span></h5>\n<div>&nbsp;</div>\n<hr />\n<h4 style=\"text-align: left;\">Coming Up Next...</h4>\n<ol>\n    <li style=\"text-align: left;\">Advanced Noticeboard Features</li>\n    <li style=\"text-align: left;\">Share Resources Demonstration</li>\n    <li style=\"text-align: left;\"><span style=\"background-color: rgb(255, 255, 153);\">Spreadsheet Demonstration (New to LAMS 2.2)</span></li>\n    <div style=\"text-align: left;\">&nbsp;</div>\n</ol>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:07:18','2008-11-14 13:14:13'),(28,87,'Noticeboard Activity','<h2>Advanced Noticeboard Tricks</h2>\n<div><b>You can use the noticeboard to embed any web object using HTML.</b></div>\n<h3>For example</h3>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"1\">\n    <tbody>\n        <tr>\n            <td><center>\n            <h4>YouTube Videos</h4>\n            <div><embed src=\"http://www.youtube.com/v/vihvpfXw_C8\" type=\"application/x-shockwave-flash\"></embed></div>\n            </center></td>\n            <td><center>\n            <h4>MP3 Files</h4>\n            <div><embed width=\"200\" height=\"40\" type=\"audio/mpeg\" autostart=\"false\" src=\"http://saturn.melcoe.mq.edu.au/lamspodcast/James_Dalziel_Getting_Best_out_of.mp3\"></embed>\n            <p align=\"center\"><strong>&nbsp;LAMS PODCAST</strong> with <strong>ALAN CARRINGTON</strong> (CLPD Adelaide Uni)<br />\n            and <strong>JAMES DALZIEL</strong> (MELCOE, Macquarie Uni)</p>\n            </div>\n            </center></td>\n        </tr>\n    </tbody>\n</table>\n<h4>&nbsp;</h4>\n<hr />\n<h4>And Even Complete External Websites</h4>\n<center>\n<div><iframe width=\"95%\" height=\"278\" src=\"http://www.melcoe.mq.edu.au/\">LAMS Community</iframe></div>\n<div>&nbsp;</div>\n</center><hr />\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration &rsquo;Share Resources&rsquo; activity.</b></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-10 13:35:26','2008-11-14 13:13:28'),(29,92,'Split-Screen Activities','<h2 style=\"text-align: center;\">There are Several Split-Screen activities available to authors<br />\nthat combine several activities on the same screen.</h2>\n<hr />\n<div>&nbsp;</div>\n<div>\n<h2><img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//chatScribe.png\" alt=\"\" /> Chat and Scribe</h2>\n<div>This activity is great for collecting Group Reports.</div>\n<div>&nbsp;</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">It combines a <b><i>Chat</i> Activity </b>with a <b>Scribe Activity</b> for collating the chat group\'s view on questions created by the teacher. When used in small group mode, the tool creates parallel Chat and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</p>\n</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div><br />\n<img width=\"144\" hspace=\"8\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//forumScribe.png\" alt=\"\" />\n<h2>Forum and Scribe</h2>\n<div>The activity ombines a <i>Forum</i> Activity with a <i>Scribe Activity</i> for collating Forum Postings into a written report, which then must be approved by the entire group.</div>\n<div>&nbsp;</div>\n<div>When used in small group mode, the tool creates parallel Forum and Scribe areas for each small group, and then shows the outcome of each group collated on a whole class page.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n<h2><img width=\"144\" hspace=\"6\" height=\"173\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//shareForum.png\" alt=\"\" />Resource Sharing and Forum</h2>\n<div>This activity allows Learners to submit files and/or URLS to a page viewable by their class or group.</div>\n<div>&nbsp;</div>\n<div>The group then discuss the resources in the Forum.</div>\n<div>&nbsp;</div>\n<div>This is a great way to get Learner\'s evaluating each other\'s work, or to foster some collaborative online research.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next activity is a demonstration Resource Sharing and Forum activity.</b></div>','','',0,0,0,'',0,1,'2008-04-10 16:29:19','2008-04-11 15:59:24'),(30,93,'Noticeboard','<h2>New Collaborative Activities in LAMS 2.2</h2>\n<h3><b><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/gmapIcon.png\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Google Maps</span></b></h3>\n<div>The Google Maps tool allows sequence Authors to create maps or satellite images with annotated place markers.&nbsp; During the running of the activity, Learners can add their own markers to the map and view markers placed by other learners.</div>\n<div>&nbsp;</div>\n<div><i>A demonstration Gmap activity is coming up.</i></div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<div><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/wikiIcon.png\" alt=\"\" /> <b><span style=\"color: rgb(255, 153, 0);\">Wiki Activity</span></b></div>\n<div>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>&nbsp;Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p><i>A Wiki Demonstration activity is coming up, which you can explore and edit.</i>&nbsp;</p>\n<hr />\n</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><i><br />\n</i></b></div>','','',0,0,0,'',0,8,'2008-11-14 13:48:47','2008-11-14 13:50:27'),(31,94,'Collaborative Activities','<div>The <b><span style=\"color: rgb(255, 153, 0);\">Collaborative </span></b>tools in LAMS allow Learners to team up and solve problems, discuss ideas or share solutions together.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2>&nbsp;<span style=\"color: rgb(255, 153, 0);\"><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lachat11.PNG\" alt=\"\" /> Chat Activity</span></h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Chat Activity</b> runs a live (synchronous) discussion for learners.<br />\nThis is similar to <i>Instant Messaging</i> (IM) software that most learners will be familiar with.</p>\n</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lafrum11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Forum Activity</span></h2>\n<div>&nbsp;</div>\n<div>&nbsp;The <b>Forum Activity</b> provides an asynchronous discussion environment for learners, with discussion threads initially created by the teacher.\n<p style=\"margin-bottom: 0cm;\">Forums can be &rsquo;locked&rsquo; as an activity which is only available for the period of the specific activity (i.e.: no more contributions can be made once the learner clicks &rsquo;Finish&rsquo;, or they can be &rsquo;unlocked&rsquo;, in that learners can add to the Forum throughout the life of the whole sequence.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"126\" height=\"53\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lascrb11.PNG\" alt=\"\" />&nbsp; <span style=\"color: rgb(255, 153, 0);\">Scribe Activity</span></h2>\n<div>&nbsp;The <b>Scribe Activity</b> is, by default, not enabled as a stand-alone activity.<br />\n<p style=\"margin-bottom: 0cm;\">It allows a group of Learners to collaborate and create a written report with one Learner as the &rsquo;Scribe,&rsquo; or Writer for the report. The activity is most effective when paired with another collaborative activity. <em class=\"western\">In the current release of LAMS (2.2) Scribe is available to be paired with either Chat or Forum.</em></p>\n<h3>Using the Scribe Activity</h3>\n<p style=\"margin-bottom: 0cm;\">The Scribe Activity should always used in conjunction with another activity that gives it context. For instance, if used with Chat, then the Scribe (one particular learner) is expected to collect or summarize the output of the Chat discussion into a succinct set of sentences. Once the scribe has done this, they publish the summary to the rest of the learners who vote whether they agree with the summary as proposed by the scribe. If not, they can ask the scribe to modify the report until all learners agree on the report.</p>\n<p style=\"margin-bottom: 0cm;\">Once the Scribe submits the report, the other learners in the group or class can agree or disagree with the summary. The scribe can finished the scribe task at any time regardless of the number of learners that have agreed with the proposed summaries. The Scribe task can also be finished through the monitoring environment by a teacher or instructor.</p>\n<p style=\"margin-bottom: 0cm;\">Once the scribe task is finished a report of the statements and summary is presented to all learners.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;&nbsp;</div>\n<div style=\"text-align: right;\"><span style=\"font-weight: bold;\"><br />\n</span></div>','','',0,0,0,'',0,8,'2008-04-10 15:41:45','2008-11-14 13:16:43'),(32,96,'Assessment Activities','<div>The <b><span style=\"color: rgb(128, 0, 128);\">Assessment </span></b>tools in LAMS provide a vehicle for sequence monitors to collect Learner responses to a topic.</div>\n<div>&nbsp;&nbsp;</div>\n<hr />\n<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lamc11.PNG\" alt=\"\" /><span style=\"color: rgb(128, 0, 128);\">Multiple Choice</span></h2>\n<div>&nbsp;The <b>Multiple Choice Quiz</b> delivers a series of Multiple Choice questions to learners, and the advanced features of this tool provide a high level of customisation for Authors as to how the quiz is administered.</div>\n<div>&nbsp;</div>\n<div>When the Quiz is complete, LAMS will automatically tally the marks from each learner and display the statistics to sequence monitors.</div>\n<div>&nbsp;</div>\n<div>Authors can also choose to release Learner marks <i>directly </i>after they have completed the quiz, as a type of <b>formative </b>assessment or <i>delay</i> releasing the marks until a Sequence Monitor chooses to release them, for <b>Summative Assessment</b>. The Activity also allows learners to view the top, bottom and average marks from the quiz.</div>\n<div>&nbsp;</div>\n<hr />\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasbmt11.PNG\" /><span style=\"color: rgb(128, 0, 128);\">Submit Files </span></h2>\n<div>The <b>Submit Files </b>Activity provides a vehicle for Authors to collect Student responses which are created in software outside of LAMS.</div>\n<div>&nbsp;</div>\n<div>This could be an essay in .DOC or .PDF format, a digital presentation, or some sort of multimedia file.</div>\n<div>&nbsp;</div>\n<div>Learners log in to LAMS, and are able to upload their file to the LAMS server as an attachment.&nbsp; Sequence Monitors can view these files, comment on them and give each individual file a mark, which is in turn accessible by Learners.</div>\n<div>&nbsp;</div>\n<div>Like the MCQ, sequence authors can choose to allow learners several attempts at submission, allowing for feedback and creating a formative assessment style, or alternatively, lock the activity after submission, which creates a summative assessment style.</div>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\">The next activity is a demonstration <b>Multiple Choice </b>quiz,<br />\nfollowed by a demonstration <b>Submit Files</b> activity.</div>','','',0,0,0,'',0,8,'2008-04-10 14:39:45','2008-11-14 13:14:54'),(33,98,'Reflective Activities','<div>Reflective tools ask learners to analyse their own learning throughout a sequence,<br />\nor alternatively, collects responses from students and asks them to consider trends in the resulting data.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//laqa11.PNG\" /> Question and Answer</h2>\n<div>This activity creates a series of Questions and Short-Answer responses for Learners.</div>\n<div>\n<p style=\"margin-bottom: 0cm;\">The sequence Author can pen their own responses to each question, which are displayed to learners after they have submitted their own answers.</p>\n<p style=\"margin-bottom: 0cm;\">The activity can also be configured to display answers collected from all learners in the sequence or group.</p>\n<p style=\"margin-bottom: 0cm;\">The <b>Question and Answer Activity</b> allows teachers to pose a question or questions to learners individually, and after they have entered their response, to see the responses of all their peers presented on a single answer screen.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lasurv11.PNG\" /> Survey</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The Survey Tool is similar to Multiple Choice, in that it presents learners with a number of questions and collects their responses. However, unlike Multiple Choice there are no right or wrong answers - the system just collates all answers for analysis by the teacher in the monitoring area.</p>\n<p style=\"margin-bottom: 0cm;\">Question types include single response, multiple response and open text entry, and questions can be mandatory or optional.  For multiple choice questions, Learners can also be provided with the option of adding their own answer to a list.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>\n<div>&nbsp;</div>\n<h2><img width=\"128\" hspace=\"2\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lavote11.PNG\" /> Voting</h2>\n<div>\n<p style=\"margin-bottom: 0cm;\">The <b>Voting Activity</b> allows teachers to provide learners with a list of options to &quot;Vote&quot; on. It is similar to the Q&amp;A Activity in that Voting shows first one screen with all the available voting options, then a screen showing the individual learner&rsquo;s selection, then a screen showing <i>&quot;progressive&quot;</i> voting results (i.e., a non-editable screen where learners can see results from themselves and other learners over time). Finally, there is Summary Screen of group results.</p>\n</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\">The next three activities demonstrate the Q&amp;A, Survey and Voting activities.</h5>\n</div>\n</div>','','',0,0,0,'',0,8,'2008-04-11 16:26:32','2008-11-14 14:30:21'),(34,99,'Data Collection Activity','<h2><img width=\"124\" height=\"51\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/dacoIcon.png\" alt=\"\" /> Data Collection</h2>\n<div>&nbsp;</div>\n<div><span style=\"border-collapse: separate; color: rgb(0, 0, 0); font-family: Verdana; font-size: 11px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 16px; orphans: 2; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px;\" class=\"Apple-style-span\">The Data Collection tool allows Authors to create database style lists for Learners to populate.<br />\nEach record list is highly flexible, and the activity can be configured to allow multiple records to exist for each learner.</span></div>\n<div><b><i>The next Activity demonstrates the use of Data Collection</i></b>.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-11-14 14:26:47','2008-11-14 14:26:47'),(35,100,'Notebook Activity','<h2><img width=\"128\" height=\"55\" align=\"absmiddle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lantbk11.PNG\" alt=\"\" />Notebook Activity</h2>\n<div>The next LAMS activity to look at is the Notebook activity.</div>\n<div>&nbsp;</div>\n<div>This activity is a bit special because it can be <b>attached to any LAMS activity</b><b> at any point in the lesson.</b><br />\nEach LAMS activity has an option in <i><b>Advanced Properties</b></i> called <i>&quot;attach Notebook with text...&quot;</i></div>\n<div>&nbsp;</div>\n<div>Basically, this allows sequences authors to get Learners to reflect on their knowledge at <b>any point in the sequence - </b>and it&rsquo;s also really easy for monitors to add a notebook to any activity in a sequence without mucking around with transition lines or changing the shape of the sequence.</div>\n<div>&nbsp;</div>\n<div>Of course, the notebook can also be included in sequences as a standalone tool.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>','','',0,0,0,'',0,8,'2008-04-11 16:41:59','2008-11-14 13:51:16'),(36,106,'CONGRATULATIONS!','<h2><img width=\"240\" height=\"240\" align=\"right\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image/diploma.jpeg\" alt=\"\" />!!! CONGRATULATIONS !!!</h2>\n<h4>You&rsquo;ve finished LAMS101:Getting Started!</h4>\n<div>&nbsp;</div>\n<div><b>Get out there and start making sequences!</b></div>\n<div>&nbsp;</div>\n<div>Or, if you want some more ideas,<br />\ncheck out the sequence-sharing goodness<br />\nat the LAMS Community Website.</div>\n<div>&nbsp;</div>\n<div><a target=\"_blank\" href=\"http://lamscommunity.org/\"><b>http://lamscommunity.org/</b></a></div>\n<div>&nbsp;</div>','','',0,0,0,'',0,4,'2008-04-11 16:49:00','2008-11-10 12:54:33'),(37,109,'The Structure of LAMS','<h4 style=\"text-align: center;\">LAMS is arranged hierarchically, with sequences organised into Groups and Subgroups.</h4>\n<h2>What does this mean?</h2>\n<div><b>Think of it like this:</b></div>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\">\n    <tbody>\n        <tr>\n            <td>\n            <h4 style=\"text-align: center;\">In Real Life</h4>\n            </td>\n            <td>\n            <h4 style=\"text-align: center;\">In Lams</h4>\n            </td>\n        </tr>\n        <tr>\n            <td>\n            <ul>\n                <li>A <b>Professor</b> teaches a <b>Course, </b>and organises smaller <b>classes.</b></li>\n                <li>Individual <b>Tutors </b>teaches a smaller <b>Class </b>taken from the whole course.</li>\n                <li>A <b>Course </b>has at least one lesson plan, that is applied to all students in the course.</li>\n                <li>The smaller classes can have at least <b>one specific lesson plan </b>for those students.</li>\n            </ul>\n            </td>\n            <td>\n            <ul>\n                <li>A <b>Course Manager</b> creates a main <b>Group, </b>and&nbsp;can create several <b>SubGroups.</b></li>\n                <li><b>Author/Monitors </b>are assigned to one or many <b>Subgroups.</b></li>\n                <li>The <b>Group</b> contains one or more <b>Sequences </b>for all <b>Learners </b>in the <b>Group.</b></li>\n                <li>Likewise, the <b>Subgroup</b> can contain one or many <b>Sequences</b> for those <b>Learners.</b></li>\n            </ul>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>There is also the role of <b>Course Administrator.</b> This role allows the user to modify Groups, Subgroups and Learners inside a course. In practice, this is a secretarial role designed to assist the educator running the course.</div>\n<div>&nbsp;</div>\n<hr />\n<h2>A Working Example</h2>\n<div style=\"text-align: center;\"><span style=\"font-size: small;\"><b>&nbsp;This image shows the different sections that make up a Group in LAMS.</b></span></div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><img border=\"2\" align=\"baseline\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//annotated.png\" alt=\"\" style=\"width: 548px; height: 418px;\" /></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i>Click <b>Next Activity </b>to continue.</i></div>','','',0,0,0,'',0,5,'2008-05-02 13:38:25','2008-06-06 12:09:41'),(38,112,'The Learning Environment','<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>You&rsquo;re Using the Learning Environment Right Now!<br />\n</b></div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">... but if you want to check out more of the features,<br />\nview the following animations!</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<hr />\n<div style=\"text-align: center;\"><br />\nThe First Animation demonstrates a Learner moving through a sequence.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\">The Second Animation deals with the new <b>&quot;no-flash&quot; interface</b><b>&nbsp;</b>for the Learning Environment</div>','','',0,0,0,'',0,11,'2008-04-11 17:34:57','2008-05-29 17:16:33'),(39,114,'Are You Sure?','<h2 style=\"text-align: center;\"><span style=\"font-size: small;\">You&rsquo;ve reached the end of this sequence... but are you sure you want to leave?</span></h2>\n<div>&nbsp;</div>\n<table width=\"200\" cellspacing=\"1\" cellpadding=\"1\" border=\"0\" align=\"center\">\n    <tbody>\n        <tr>\n            <td><span style=\"font-size: small;\"><img width=\"160\" vspace=\"2\" hspace=\"2\" height=\"377\" border=\"1\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image//progbar.png\" alt=\"\" /><br />\n            </span></td>\n            <td>.      .</td>\n            <td>\n            <div><span style=\"font-size: small;\"> You may have clicked the <b>Next Activity</b> button on the last screen,</span> <span style=\"font-size: small;\">which would bring you here.</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If so, and you want to go back and view more of this sequence, double-click the Icon for the <b>Optional Activity </b>in your <b>Progress Bar </b>(see the graphic at right.)</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">Once there, select an option, and click the <b>Choose </b>button.</span></div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\">&nbsp;</div>\n            <div style=\"text-align: left;\"><span style=\"font-size: small;\">If you really have finished, and want to leave the sequence, click <b>Next Activity</b> below.<br />\n            </span></div>\n            </td>\n        </tr>\n    </tbody>\n</table>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp; <br />\n</span></div>\n<div style=\"text-align: left;\"><span style=\"font-size: small;\">&nbsp;</span></div>\n<div style=\"text-align: right;\">&nbsp;</div>','','',0,0,0,'',0,1,'2008-05-02 11:42:23','2008-05-02 12:05:42'),(40,115,'LAMS Environments','<div><img width=\"100\" height=\"90\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee6bacc700d2/Image/dolly_lrg.gif\" alt=\"\" style=\"margin-right: 10px;\" /></div>\n<h2>There are four main environments in LAMS</h2>\n<h3>The Learning Environment</h3>\n<div>\n<div>The <b>Learning Environment</b> is the <i>&rsquo;doing&rsquo;</i> of LAMS.</div>\n<div>This is what Learners will see when they log in to LAMS and start participating in a sequence.</div>\n<div>The Learning Environment displays the sequences that are created in <b>Authoring.</b></div>\n<h3>&nbsp;</h3>\n<hr />\n<h3>The Authoring Environment</h3>\n<div>The Authoring Environment is where sequences are put together, modified and shared.</div>\n<div>It contains all of the LAMS activities, and is where authors create their lesson designs.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The Monitoring Environment</h3>\n<div><b>Monitors</b><b> </b>are not necessarily the people who Authored the sequence in the first place - they simply <b>run</b> the lesson for learners, and supervise each learner&rsquo;s progress through the lesson. In practice, Teachers are often both Authors and Monitors for their class(es) of students.</div>\n<div>&nbsp;</div>\n<div><hr />\n<h3>The System Administration Environment</h3>\n<div><b>System Administration</b> is for <b>Technical Management</b> of a LAMS server.</div>\n<div>&nbsp;</div>\n<div>From here, users with Administration rights can change nearly every aspect of the LAMS software.&nbsp;</div>\n<h4 style=\"text-align: center;\"><a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/Home\" target=\"_new\">LAMS System Administration is discussed in detail on the Documentation Website.<br />\nClick here for details [Online Link].</a></h4>\n<div>&nbsp;</div>\n<hr />\n</div>\n</div>\n</div>\n<div>&nbsp;</div>\n<h5 style=\"text-align: right;\"><b>The next activity will allow you to choose from animated tutorials on each three environments.</b></h5>','','',0,0,0,'',0,1,'2008-03-27 15:12:18','2008-06-05 11:47:45'),(41,117,'The tip of the iceberg...','<p><span style=\"font-size: small;\">LAMS has many, <b>many</b> more functions that you can explore.</span></p>\n<p><span style=\"font-size: small;\">The new release of version 2.1 includes increased functionality in many areas, some of which are explored in the rest of this <b>Getting Started</b> course.</span></p>\n<h2 style=\"text-align: center;\"><b>We Hope You Enjoy Exploring LAMS!</b></h2>\n<p style=\"text-align: center;\">&nbsp;</p>\n<div>&nbsp;</div>','','',0,0,0,'',0,1,'2008-04-04 11:30:06','2008-06-05 11:45:40'),(42,119,'Creating Your Own Sequences','<h2>LAMS Authoring</h2>\n<div><span style=\"font-size: small;\">LAMS provides you with a <b>visual authoring environment </b>for creating, storing and re-using activity sequences. The drag-and-drop interface is central to the LAMS authoring environment. Teachers drag-and-drop activities into the authoring area and then join the activities together by drawing &quot;Transitions&quot; to produce a learning sequence. This workflow model is what principally distinguishes LAMS from other more content based online learning platforms. LAMS has a wide range of tools designed for use in a range of educational contexts by teachers and students with varying levels of technical expertise.</span></div>\n<h3>Benefits of LAMS</h3>\n<div><span style=\"font-size: small;\">Creating a lesson in LAMS means that:</span></div>\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<ul>\n    <li><span style=\"font-size: small;\"><b>You can plan all aspects of the lesson.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can preview the lesson from the perspective of your learners.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You create a visual overview of the lesson, and can easily identify the learning styles addressed.</b> </span></li>\n    <li><span style=\"font-size: small;\"><b>You can create a standardised set of learning templates that can easily be modified for later use.</b> </span></li>\n</ul>\n<div>&nbsp;</div>\n<hr />\n<div style=\"text-align: right;\">&nbsp;</div>\n<div style=\"text-align: right;\"><i><img width=\"116\" height=\"103\" align=\"right\" alt=\"\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image//dolly_sml.gif\" /></i>&nbsp;</div>\n<div style=\"text-align: right;\"><b>The next animation will demonstrate creating a lesson in LAMS.</b></div>','','',0,0,0,'',0,11,'2008-04-03 16:11:54','2008-05-29 16:55:57'),(43,120,'LAMS101 - Lesson One','<div><span class=\"Apple-style-span\" style=\"orphans: 2; word-spacing: 0px; font-family: Verdana; font-style: normal; font-variant: normal; font-weight: normal; font-size: 11px; line-height: 16px; text-transform: none; color: rgb(0, 0, 0); widows: 2; text-indent: 0px; white-space: normal; letter-spacing: normal; border-collapse: separate; font-size-adjust: none; font-stretch: normal;\">\n<h2>So What is LAMS?</h2>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"><b>Congratulations</b> - if you can see this sequence, LAMS is up and running on your server. You&rsquo;ve also figured out how to log in, and participate in a &quot;sequence&quot; - that&rsquo;s what we call a collection of LAMS activities.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\"> </span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">In a word, that&rsquo;s what LAMS does - it is a&nbsp;<b>S</b>ystem that&nbsp;<b>M</b>anages sequences of <b>L</b>earning <b>A</b>ctivities. &nbsp;A <b>Sequence </b>is an online digital lesson that simulates the classroom environment in a virtual space.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are first created by <b>Authors, </b>and are then run for different groups of <b>Learners </b>while a <b>Monitor</b> supervises. &nbsp;We&rsquo;ll look more at these <b>Roles </b>in LAMS in Lesson Two of LAMS101.</span></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><span style=\"font-size: small;\">Sequences are made up of activities - each of which has a particular function, either:</span></p>\n<span style=\"font-size: small;\">\n<h4 style=\"text-align: center;\"><span style=\"font-size: medium;\"><span style=\"color: rgb(255, 102, 0);\"><b>Informative &nbsp;</b></span><span style=\"color: rgb(255, 204, 0);\"><b>Collaborative &nbsp;</b></span><span style=\"color: rgb(128, 0, 128);\"><b>Assessment &nbsp;</b></span><span style=\"color: rgb(51, 153, 102);\"><b>Reflective</b></span></span></h4>\n</span>\n<p><span style=\"font-size: small;\">Sequences can include a range of individual tasks, small group work and whole class activities based on both content and collaboration.</span></p>\n<p><span style=\"font-size: small;\">When you use LAMS, not only are you creating online activities for your learners, you also create a structured lesson plan at the same time.&nbsp; We&rsquo;ll talk about this <b>Visual Authoring Environment </b>in a few moments.</span></p>\n<h2>... and what&rsquo;s with the Sheep?</h2>\n<b>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\"><img width=\"116\" vspace=\"12\" hspace=\"10\" height=\"103\" border=\"0\" align=\"left\" src=\"/lams//www/secure/4028efbb18edfbb70118ee3b3b6d00b8/Image/dolly_sml.gif\" alt=\"dolly the sheep\" /></p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Dolly is our LAMS mascot.&nbsp; Back when LAMS was being redesigned for version 2.0, it was the &quot;next-generation&quot; of LAMS.&nbsp; Dolly the sheep was the first mammal to have been successfully cloned from an adult cell, therefore, she was the next generation LAMB (see what we did there?).</p>\n<p style=\"margin: 16px 0px; padding: 0px; font-weight: normal; font-size: 11px; color: rgb(0, 0, 0); line-height: 16px; font-family: Verdana,arial,sans-serif;\">Our marvelous designer Shelly is the official mother of Dolly and also our <span class=\"nobr\"><a href=\"http://demo.lamscommunity.org/lams/images/css/lams_login_bar.jpg\" rel=\"nofollow\">front page model</a></span>. So all kudos to her!</p>\n<hr />\n&nbsp;</b></span></div>\n<div style=\"text-align: right;\"><i>To progress to the next activity in this sequence, click the button down here.</i></div>','','',0,0,0,'Reflect on noticeboard',0,1,'2008-03-12 16:38:33','2008-06-05 09:52:50');
/*!40000 ALTER TABLE `tl_lanb11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_session`
--

DROP TABLE IF EXISTS `tl_lanb11_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lanb11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `nb_session_id` bigint(20) NOT NULL,
  `nb_session_name` varchar(255) NOT NULL,
  `nb_content_uid` bigint(20) NOT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_status` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `nb_session_id` (`nb_session_id`),
  KEY `nb_content_uid` (`nb_content_uid`),
  CONSTRAINT `FK_tl_lanb11_session_1` FOREIGN KEY (`nb_content_uid`) REFERENCES `tl_lanb11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lanb11_session`
--

LOCK TABLES `tl_lanb11_session` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_session` DISABLE KEYS */;
INSERT INTO `tl_lanb11_session` VALUES (1,5,'LAMS Demo Course learners',24,'2010-04-01 16:04:06',NULL,'NOT_ATTEMPTED'),(2,10,'LAMS Demo Course learners',23,'2010-04-01 16:04:06',NULL,'NOT_ATTEMPTED'),(3,11,'LAMS Demo Course learners',32,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(4,15,'LAMS Demo Course learners',36,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(5,16,'LAMS Demo Course learners',25,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(6,17,'LAMS Demo Course learners',33,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(7,18,'LAMS Demo Course learners',31,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(8,21,'LAMS Demo Course learners',29,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(9,25,'LAMS Demo Course learners',30,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(10,28,'LAMS Demo Course learners',35,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(11,30,'LAMS Demo Course learners',34,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(12,31,'LAMS Demo Course learners',27,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(13,32,'LAMS Demo Course learners',26,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(14,33,'LAMS Demo Course learners',28,'2010-04-01 16:04:15',NULL,'NOT_ATTEMPTED'),(15,37,'LAMS Demo Course learners',38,'2010-04-01 16:04:27',NULL,'NOT_ATTEMPTED'),(16,40,'LAMS Demo Course learners',37,'2010-04-01 16:04:27',NULL,'NOT_ATTEMPTED'),(17,41,'LAMS Demo Course learners',39,'2010-04-01 16:04:27',NULL,'NOT_ATTEMPTED'),(18,44,'LAMS Demo Course learners',40,'2010-04-01 16:04:27',NULL,'NOT_ATTEMPTED'),(19,45,'LAMS Demo Course learners',42,'2010-04-01 16:05:08',NULL,'NOT_ATTEMPTED'),(20,48,'LAMS Demo Course learners',43,'2010-04-01 16:05:08',NULL,'NOT_ATTEMPTED'),(21,49,'LAMS Demo Course learners',41,'2010-04-01 16:05:08',NULL,'NOT_ATTEMPTED');
/*!40000 ALTER TABLE `tl_lanb11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_user`
--

DROP TABLE IF EXISTS `tl_lanb11_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lanb11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `nb_session_uid` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `user_status` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `nb_session_uid` (`nb_session_uid`),
  CONSTRAINT `FK_tl_lanb11_user_1` FOREIGN KEY (`nb_session_uid`) REFERENCES `tl_lanb11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lantbk11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `notebook_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK12090F57FC940906` (`notebook_uid`),
  CONSTRAINT `FK12090F57FC940906` FOREIGN KEY (`notebook_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lantbk11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `NotebookConditionToNotebook` (`content_uid`),
  CONSTRAINT `NotebookConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `NotebookConditionToNotebook` FOREIGN KEY (`content_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lantbk11_notebook` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `allow_rich_editor` bit(1) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lantbk11_notebook`
--

LOCK TABLES `tl_lantbk11_notebook` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_notebook` DISABLE KEYS */;
INSERT INTO `tl_lantbk11_notebook` VALUES (1,NULL,NULL,NULL,'Notebook','Instructions',0x00,0x00,0x00,'','',0x00,0x00,8),(2,'2008-11-14 14:32:00','2008-11-10 12:44:29',5,'Notebook Demonstration','<div>Although this looks like a simple text entry box,&nbsp; when a notebook is placed in the right spot, it can really get learners to think about what they&rsquo;ve just learnt.</div>\n<div>&nbsp;</div>\n<div>All notebook entries are private between each individual learner and the sequence monitors.</div>',0x00,0x00,0x00,'','',0x00,0x00,54),(3,'2010-04-01 16:04:14','2008-11-10 12:44:29',5,'Notebook Demonstration','<div>Although this looks like a simple text entry box,&nbsp; when a notebook is placed in the right spot, it can really get learners to think about what they&rsquo;ve just learnt.</div>\n<div>&nbsp;</div>\n<div>All notebook entries are private between each individual learner and the sequence monitors.</div>',0x00,0x00,0x00,'','',0x00,0x00,101);
/*!40000 ALTER TABLE `tl_lantbk11_notebook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_session`
--

DROP TABLE IF EXISTS `tl_lantbk11_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lantbk11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `notebook_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKB7C198E2FC940906` (`notebook_uid`),
  CONSTRAINT `FKB7C198E2FC940906` FOREIGN KEY (`notebook_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lantbk11_session`
--

LOCK TABLES `tl_lantbk11_session` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_session` DISABLE KEYS */;
INSERT INTO `tl_lantbk11_session` VALUES (1,NULL,NULL,NULL,29,'LAMS Demo Course learners',3);
/*!40000 ALTER TABLE `tl_lantbk11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_user`
--

DROP TABLE IF EXISTS `tl_lantbk11_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lantbk11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `notebook_session_uid` bigint(20) DEFAULT NULL,
  `entry_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKCB8A58FFA3B0FADF` (`notebook_session_uid`),
  CONSTRAINT `FKCB8A58FFA3B0FADF` FOREIGN KEY (`notebook_session_uid`) REFERENCES `tl_lantbk11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lantbk11_user`
--

LOCK TABLES `tl_lantbk11_user` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lantbk11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_attachment`
--

DROP TABLE IF EXISTS `tl_lapixl10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lapixl10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `pixlr_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK951F889ACB8ADA74` (`pixlr_uid`),
  CONSTRAINT `FK951F889ACB8ADA74` FOREIGN KEY (`pixlr_uid`) REFERENCES `tl_lapixl10_pixlr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lapixl10_attachment`
--

LOCK TABLES `tl_lapixl10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lapixl10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_configuration`
--

DROP TABLE IF EXISTS `tl_lapixl10_configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lapixl10_configuration` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(30) DEFAULT NULL,
  `config_value` text,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lapixl10_configuration`
--

LOCK TABLES `tl_lapixl10_configuration` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_configuration` DISABLE KEYS */;
INSERT INTO `tl_lapixl10_configuration` VALUES (1,'LanguageCSV','en,es,fr,de,it,ru,ro,zh-cn,pt-br,sv,pl,th');
/*!40000 ALTER TABLE `tl_lapixl10_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_pixlr`
--

DROP TABLE IF EXISTS `tl_lapixl10_pixlr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lapixl10_pixlr` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `allow_view_others_images` bit(1) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `image_file_name` text,
  `image_width` bigint(20) DEFAULT NULL,
  `image_height` bigint(20) DEFAULT NULL,
  `reflect_instructions` text,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lapixl10_pixlr`
--

LOCK TABLES `tl_lapixl10_pixlr` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_pixlr` DISABLE KEYS */;
INSERT INTO `tl_lapixl10_pixlr` VALUES (1,NULL,NULL,NULL,'Pixlr','Instructions',0x00,0x00,0x00,0x01,'','',0x00,0x00,21,'blank.jpg',400,450,NULL);
/*!40000 ALTER TABLE `tl_lapixl10_pixlr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_session`
--

DROP TABLE IF EXISTS `tl_lapixl10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lapixl10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `pixlr_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKE5C05E7FCB8ADA74` (`pixlr_uid`),
  CONSTRAINT `FKE5C05E7FCB8ADA74` FOREIGN KEY (`pixlr_uid`) REFERENCES `tl_lapixl10_pixlr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lapixl10_session`
--

LOCK TABLES `tl_lapixl10_session` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lapixl10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_user`
--

DROP TABLE IF EXISTS `tl_lapixl10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lapixl10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `pixlr_session_uid` bigint(20) DEFAULT NULL,
  `entry_uid` bigint(20) DEFAULT NULL,
  `image_file_name` text,
  `imageHeight` bigint(20) DEFAULT NULL,
  `imageWidth` bigint(20) DEFAULT NULL,
  `imageHidden` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK9A39C08236E23005` (`pixlr_session_uid`),
  CONSTRAINT `FK9A39C08236E23005` FOREIGN KEY (`pixlr_session_uid`) REFERENCES `tl_lapixl10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lapixl10_user`
--

LOCK TABLES `tl_lapixl10_user` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lapixl10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_condition_questions`
--

DROP TABLE IF EXISTS `tl_laqa11_condition_questions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_condition_questions` (
  `condition_id` bigint(20) NOT NULL DEFAULT '0',
  `question_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`condition_id`,`question_uid`),
  KEY `QaConditionQuestionToQaQuestion` (`question_uid`),
  CONSTRAINT `QaConditionQuestionToQaCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_laqa11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionQuestionToQaQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_laqa11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `QaConditionToQaContent` (`content_uid`),
  CONSTRAINT `QaConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionToQaContent` FOREIGN KEY (`content_uid`) REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_configuration` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(30) DEFAULT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `qa_content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `reflect` tinyint(1) NOT NULL DEFAULT '0',
  `questions_sequenced` tinyint(1) NOT NULL DEFAULT '0',
  `username_visible` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL DEFAULT '0',
  `run_offline` tinyint(1) DEFAULT '0',
  `define_later` tinyint(1) NOT NULL DEFAULT '0',
  `synch_in_monitor` tinyint(1) NOT NULL DEFAULT '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_inUse` tinyint(1) DEFAULT '0',
  `reflectionSubject` text,
  `lockWhenFinished` tinyint(1) NOT NULL DEFAULT '1',
  `showOtherAnswers` tinyint(1) NOT NULL DEFAULT '1',
  `allow_rich_editor` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laqa11_content`
--

LOCK TABLES `tl_laqa11_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_content` VALUES (1,3,'Q&A','Instructions','2010-04-01 15:56:31',NULL,0,0,0,0,0,0,0,NULL,NULL,0,NULL,0,1,0),(2,32,'Reflective Journal','<font size=\"3\" face=\"Arial\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nNow you&rsquo;ve had a chance to chat about what you&rsquo;ve learnt with the group, it&rsquo;s now your chance to record your thoughts.&nbsp; <br />\n<br />\nSome questions have been created to help you, please respond to each on eas best you can.</font>',NULL,'2007-03-15 12:21:41',0,0,0,5,0,0,0,'','',0,'',1,1,0),(3,46,'Question and Answer Demonstration','<h2><img width=\"116\" hspace=\"6\" height=\"103\" align=\"middle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /><b>This is the Q&amp;A Activity</b></h2>\n<div>&nbsp;</div>\n<div>Up here is a space for Author instructions for Learners, and below is the list of Questions.</div>\n<div>&nbsp;</div>\n<div>Have a crack at answering them, and then watch what happens over the following screens.</div>',NULL,'2008-11-14 14:30:09',0,0,0,5,0,0,0,'','',0,'',0,1,0),(4,76,'Reflective Journal','<font size=\"3\" face=\"Arial\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nNow you&rsquo;ve had a chance to chat about what you&rsquo;ve learnt with the group, it&rsquo;s now your chance to record your thoughts.&nbsp; <br />\n<br />\nSome questions have been created to help you, please respond to each on eas best you can.</font>',NULL,'2007-03-15 12:21:41',0,0,0,5,0,0,0,'','',0,'',1,1,0),(5,105,'Question and Answer Demonstration','<h2><img width=\"116\" hspace=\"6\" height=\"103\" align=\"middle\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /><b>This is the Q&amp;A Activity</b></h2>\n<div>&nbsp;</div>\n<div>Up here is a space for Author instructions for Learners, and below is the list of Questions.</div>\n<div>&nbsp;</div>\n<div>Have a crack at answering them, and then watch what happens over the following screens.</div>',NULL,'2008-11-14 14:30:09',0,0,0,5,0,0,0,'','',0,'',0,1,0);
/*!40000 ALTER TABLE `tl_laqa11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_content`
--

DROP TABLE IF EXISTS `tl_laqa11_que_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_que_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question` text,
  `feedback` text,
  `display_order` int(5) DEFAULT '1',
  `qa_content_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_que_content_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laqa11_que_content`
--

LOCK TABLES `tl_laqa11_que_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_que_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_que_content` VALUES (1,'Sample Question 1?',NULL,1,1),(2,'<font size=\"4\" face=\"Arial\">What have you learnt?</font>','',1,2),(3,'<font size=\"4\">How did you learn it, what did you do?&nbsp; <br />\n<br />\nFor example:<br />\n<br />\n</font>\n<ul>\n    <li><font size=\"4\">Answer questions</font></li>\n    <li><font size=\"4\">Read</font></li>\n    <li><font size=\"4\">Discuss with your group</font></li>\n</ul>','',2,2),(4,'<font size=\"4\" face=\"Arial\">How and where could you use this in the future?</font>','',3,2),(5,'<div><b><span style=\"font-size: small;\">What do you like best so far about LAMS?</span></b></div>\n<div><b><span style=\"font-size: small;\"> <br />\n</span></b></div>','This is the feedback section - it allows Authors and Monitors to give a short response to each question.<BR>This is incredibly useful for formative assessment and learning techniques.',1,3),(6,'<div><span style=\"font-size: small;\"><b>Do you think you can apply LAMS to your own teaching styles?</b></span></div>','',2,3),(7,'<font size=\"4\" face=\"Arial\">What have you learnt?</font>','',1,4),(8,'<font size=\"4\">How did you learn it, what did you do?&nbsp; <br />\n<br />\nFor example:<br />\n<br />\n</font>\n<ul>\n    <li><font size=\"4\">Answer questions</font></li>\n    <li><font size=\"4\">Read</font></li>\n    <li><font size=\"4\">Discuss with your group</font></li>\n</ul>','',2,4),(9,'<font size=\"4\" face=\"Arial\">How and where could you use this in the future?</font>','',3,4),(10,'<div><b><span style=\"font-size: small;\">What do you like best so far about LAMS?</span></b></div>\n<div><b><span style=\"font-size: small;\"> <br />\n</span></b></div>','This is the feedback section - it allows Authors and Monitors to give a short response to each question.<BR>This is incredibly useful for formative assessment and learning techniques.',1,5),(11,'<div><span style=\"font-size: small;\"><b>Do you think you can apply LAMS to your own teaching styles?</b></span></div>','',2,5);
/*!40000 ALTER TABLE `tl_laqa11_que_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_usr`
--

DROP TABLE IF EXISTS `tl_laqa11_que_usr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_que_usr` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `que_usr_id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `responseFinalized` tinyint(1) NOT NULL DEFAULT '0',
  `qa_session_id` bigint(20) NOT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `learnerFinished` tinyint(1) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `qa_session_id` (`qa_session_id`),
  CONSTRAINT `FK_tl_laqa11_que_usr_1` FOREIGN KEY (`qa_session_id`) REFERENCES `tl_laqa11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `qa_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_name` varchar(100) DEFAULT NULL,
  `session_status` varchar(100) DEFAULT NULL,
  `qa_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_session_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_laqa11_session`
--

LOCK TABLES `tl_laqa11_session` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_session` DISABLE KEYS */;
INSERT INTO `tl_laqa11_session` VALUES (1,6,'2010-04-01 16:04:06',NULL,'LAMS Demo Course learners','INCOMPLETE',4),(2,20,'2010-04-01 16:04:15',NULL,'LAMS Demo Course learners','INCOMPLETE',5);
/*!40000 ALTER TABLE `tl_laqa11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_laqa11_uploadedfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `qa_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`submissionId`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_uploadedfile_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_usr_resp` (
  `response_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hidden` tinyint(1) DEFAULT '0',
  `answer` text,
  `time_zone` varchar(255) DEFAULT NULL,
  `attempt_time` datetime DEFAULT NULL,
  `que_usr_id` bigint(20) NOT NULL,
  `qa_que_content_id` bigint(20) NOT NULL,
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`response_id`),
  KEY `que_usr_id` (`que_usr_id`),
  KEY `qa_que_content_id` (`qa_que_content_id`),
  CONSTRAINT `FK_tl_laqa11_usr_resp_3` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_laqa11_que_usr` (`uid`),
  CONSTRAINT `FK_tl_laqa11_usr_resp_2` FOREIGN KEY (`qa_que_content_id`) REFERENCES `tl_laqa11_que_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_wizard_category` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_wizard_cognitive_skill` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `category_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK3BA4132BCBB0DC8D` (`category_uid`),
  CONSTRAINT `FK3BA4132BCBB0DC8D` FOREIGN KEY (`category_uid`) REFERENCES `tl_laqa11_wizard_category` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_laqa11_wizard_question` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `cognitive_skill_uid` bigint(20) DEFAULT NULL,
  `title` text NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKAF08A0C7EFF77FD4` (`cognitive_skill_uid`),
  CONSTRAINT `FKAF08A0C7EFF77FD4` FOREIGN KEY (`cognitive_skill_uid`) REFERENCES `tl_laqa11_wizard_cognitive_skill` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `resource_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK1E7009430E79035` (`resource_uid`),
  CONSTRAINT `FK1E7009430E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_item_instruction` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `sequence_id` int(11) DEFAULT NULL,
  `item_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKA5665013980570ED` (`item_uid`),
  CONSTRAINT `FKA5665013980570ED` FOREIGN KEY (`item_uid`) REFERENCES `tl_larsrc11_resource_item` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_larsrc11_item_instruction`
--

LOCK TABLES `tl_larsrc11_item_instruction` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_item_instruction` VALUES (1,'Use Google to search the web',0,1),(2,'<font size=3>Open the learning object by clicking on the <b>Next</b> button and work though the learning object.</font>',0,2),(3,'<font size=3>When you have finished you can click on the <I><B>search by structure</I></B> tabs and discover more interesting facts about the brain.  Now click on <i><b>finish</i></b>.</font>',3,3),(4,'<font size=3>Click on the word <I><B> Function</I></B> at the bottom of the page.</font>',0,3),(5,'<font size=3>Explore which parts of the brain deal with different emotions.  Look at: <font color=red>self control, self awareness and flight or fight.</font>',1,3),(6,'<font size=3>Make a note of <i><b >at least</i></b> two things you find interesting about the brain.</font>',2,3),(7,'<font size=3>Make a note of how long it takes you to find all 12 pairs!</font>',1,4),(8,'<font size=3>Click on the cards to turn them over and find the matching pairs.',0,4),(9,'<font size=3>Wait until the face finished ramdonizing.  Memorize the face, you only have 6 seconds so pay attention!</font>',0,5),(10,'<font size=3>Click on <b>finish</b> when you have had enough goes.</font>',1,5),(11,'In this animation, you will learn how to use the features of the Noticeboard Activity.',0,6),(12,'This is our online documentation website.  Here you can find the specifications for what kind of files can be added to a Resource Sharing activity.',0,7),(13,'This animation explains how to create and use a Resource Sharing activity.',0,8),(14,'This animation describes the basics of the Monitoring environment.',0,12),(15,'You will need an active internet connection to view this web page.',0,13),(16,'You might need to resize your browser to see the whole animation.',0,14),(17,'When the animation has completed, click Finished to move to the next activity.',1,14),(18,'This animation will demonstrate the Learner\'s Experience when using LAMS.   You might need to resize your browser to see the whole animation.',0,15),(19,'When the animation has stopped, click Finished to go to the next activity. ',1,15),(20,'When the animation has stopped, click ',1,16),(21,'LAMS 2.1 lets you disable FLASH for the Learning Environment - this is useful if you want to use LAMS on devices that do not support flash.',0,16),(22,'You might need to resize your internet browser to see the whole animation.',0,17),(23,'When the animation is complete, click Finished to go to the next activity.',1,17),(24,'When the animation has stopped, click Finished to go to the next activity.',1,18),(25,'This animation gives a brief overview of LAMS.',0,18),(26,'<font size=3>Click on the cards to turn them over and find the matching pairs.',0,19),(27,'<font size=3>Make a note of how long it takes you to find all 12 pairs!</font>',1,19),(28,'<font size=3>When you have finished you can click on the <I><B>search by structure</I></B> tabs and discover more interesting facts about the brain.  Now click on <i><b>finish</i></b>.</font>',3,20),(29,'<font size=3>Make a note of <i><b >at least</i></b> two things you find interesting about the brain.</font>',2,20),(30,'<font size=3>Click on the word <I><B> Function</I></B> at the bottom of the page.</font>',0,20),(31,'<font size=3>Explore which parts of the brain deal with different emotions.  Look at: <font color=red>self control, self awareness and flight or fight.</font>',1,20),(32,'<font size=3>Open the learning object by clicking on the <b>Next</b> button and work though the learning object.</font>',0,21),(33,'<font size=3>Wait until the face finished ramdonizing.  Memorize the face, you only have 6 seconds so pay attention!</font>',0,22),(34,'<font size=3>Click on <b>finish</b> when you have had enough goes.</font>',1,22),(35,'In this animation, you will learn how to use the features of the Noticeboard Activity.',0,23),(36,'This is our online documentation website.  Here you can find the specifications for what kind of files can be added to a Resource Sharing activity.',0,24),(37,'This animation explains how to create and use a Resource Sharing activity.',0,25),(38,'You might need to resize your browser to see the whole animation.',0,29),(39,'When the animation has completed, click Finished to move to the next activity.',1,29),(40,'This animation describes the basics of the Monitoring environment.',0,30),(41,'You will need an active internet connection to view this web page.',0,31),(42,'LAMS 2.1 lets you disable FLASH for the Learning Environment - this is useful if you want to use LAMS on devices that do not support flash.',0,32),(43,'When the animation has stopped, click ',1,32),(44,'When the animation has stopped, click Finished to go to the next activity. ',1,33),(45,'This animation will demonstrate the Learner\'s Experience when using LAMS.   You might need to resize your browser to see the whole animation.',0,33),(46,'When the animation is complete, click Finished to go to the next activity.',1,34),(47,'You might need to resize your internet browser to see the whole animation.',0,34),(48,'When the animation has stopped, click Finished to go to the next activity.',1,35),(49,'This animation gives a brief overview of LAMS.',0,35);
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_item_log`
--

DROP TABLE IF EXISTS `tl_larsrc11_item_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_item_log` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_date` datetime DEFAULT NULL,
  `resource_item_uid` bigint(20) DEFAULT NULL,
  `user_uid` bigint(20) DEFAULT NULL,
  `complete` tinyint(4) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK693580A438BF8DFE` (`resource_item_uid`),
  KEY `FK693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_larsrc11_user` (`uid`),
  CONSTRAINT `FK693580A438BF8DFE` FOREIGN KEY (`resource_item_uid`) REFERENCES `tl_larsrc11_resource_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_resource` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT NULL,
  `lock_on_finished` tinyint(4) DEFAULT NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT NULL,
  `define_later` tinyint(4) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `allow_add_files` tinyint(4) DEFAULT NULL,
  `allow_add_urls` tinyint(4) DEFAULT NULL,
  `mini_view_resource_number` int(11) DEFAULT NULL,
  `allow_auto_run` tinyint(4) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  `assigment_submit_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK89093BF758092FB` (`create_by`),
  CONSTRAINT `FK89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_larsrc11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_larsrc11_resource`
--

LOCK TABLES `tl_larsrc11_resource` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource` VALUES (1,NULL,NULL,NULL,'Resources',0,0,'Instructions ',NULL,NULL,0,0,6,0,0,0,0,NULL,0,0),(2,NULL,'2007-03-09 09:44:09',1,'Brain Needs',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nOpen the learning object and find out what your brain needs to function properly. <br />\n<br />\nRead carefully, there will be a short test on this later!!!!!</font>','','',0,0,26,0,0,0,0,'',0,0),(3,NULL,'2007-02-28 10:20:31',2,'Your Amazing Brain',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nClick on the Brain Fact link and follow the instructions at the top of the screen.<br />\n<br />\nThis will take you to an interactive brain, click on the <em><strong>Function</strong></em> option and explore which part of your brain deals with different emotions such as anger and happiness. Make some notes as you go along as you will do a short quiz about this later!</font>','','',0,0,27,0,0,0,0,'',0,0),(4,NULL,'2007-02-23 15:59:33',3,'Matching Pairs',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nYou will now access a resource where you will play a game. This game is all about concentration. Have a go and see how good your concentration is!!!!!<br />\n<br />\n</font>','','',0,0,29,0,0,0,0,'',0,0),(5,NULL,'2007-02-28 10:24:33',4,'Remember the Faces',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nThis game looks at how well you can remember pictures and recreate them.&nbsp; When you open the link you will see a face, you will then have <font color=\"#ff0000\"><strong>6</strong></font> seconds to memorise.&nbsp; You must use your memory to re-create the face you have just seen. <br />\n<br />\nHave a play, have more than one go an see if you can improve you memory!<br />\n</font>','','',0,0,31,0,0,0,0,'',0,0),(6,NULL,'2008-04-11 15:35:40',5,'Resource Sharing',0,0,'<h2 style=\"text-align: center;\">This is a Resource Sharing Activity.</h2>\n<div style=\"text-align: center;\">You can see that the list below contains a URL, and several Zipped Websites.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>Explore the resources below, and then try adding your own URL to the list - </b><br />\nsimply fill in the fields below, marked &quot;Suggest a new URL&quot;<br />\nand then click the <b>Add</b> button.</div>\n<div style=\"text-align: center;\">&nbsp;</div>','','',0,0,36,0,1,0,0,'',0,0),(7,'2008-04-11 16:01:40','2008-04-11 16:01:40',6,'Resources on LAMS',0,0,'<h2 style=\"text-align: center;\"><b>Here are some interesting websites about LAMS!</b></h2>\n<div style=\"text-align: center;\"><b><br type=\"_moz\" />\n</b></div>','','',0,0,43,0,0,0,0,'',0,0),(8,NULL,'2008-06-06 11:52:30',7,'Monitor Wink',0,0,'<div>Instructions</div>','','',0,0,61,0,0,0,1,'',0,0),(9,NULL,'2008-06-06 11:53:51',8,'Resources',0,0,'<div>Instructions</div>','','',0,0,62,0,0,0,1,'',0,0),(10,NULL,'2008-06-06 10:56:27',9,'Resources',0,0,'<div>Instructions</div>','','',0,0,63,0,0,0,1,'',0,0),(11,NULL,'2008-06-05 12:03:37',10,'Learning Environment',0,0,'','','',0,0,64,0,0,0,1,'',0,0),(12,NULL,'2008-06-05 12:32:17',11,'Resources',0,0,'<div>Instructions</div>','','',0,0,66,0,0,0,1,'',0,0),(13,NULL,'2008-06-05 11:45:33',12,'Resources',0,0,'<div>Instructions</div>','','',0,0,69,0,0,0,1,'',0,0),(14,NULL,'2008-06-05 10:37:56',13,'So What Is LAMS?',0,0,'<div><span style=\"font-size: small;\"> </span></div>','','',0,0,71,0,0,0,1,'',0,0),(15,NULL,'2007-02-23 15:59:33',14,'Matching Pairs',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" alt=\"\" /><br />\n<br />\n<br />\nYou will now access a resource where you will play a game. This game is all about concentration. Have a go and see how good your concentration is!!!!!<br />\n<br />\n</font>','','',0,0,72,0,0,0,0,'',0,0),(16,NULL,'2007-02-28 10:20:31',15,'Your Amazing Brain',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nClick on the Brain Fact link and follow the instructions at the top of the screen.<br />\n<br />\nThis will take you to an interactive brain, click on the <em><strong>Function</strong></em> option and explore which part of your brain deals with different emotions such as anger and happiness. Make some notes as you go along as you will do a short quiz about this later!</font>','','',0,0,78,0,0,0,0,'',0,0),(17,NULL,'2007-03-09 09:44:09',16,'Brain Needs',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nOpen the learning object and find out what your brain needs to function properly. <br />\n<br />\nRead carefully, there will be a short test on this later!!!!!</font>','','',0,0,80,0,0,0,0,'',0,0),(18,NULL,'2007-02-28 10:24:33',17,'Remember the Faces',0,0,'<font size=\"3\" face=\"Verdana\"><img width=\"710\" height=\"74\" alt=\"\" src=\"/lams/www/secure/8a8a8c181072e804011073639e9c004e/Image/YAB.jpg\" /><br />\n<br />\n<br />\nThis game looks at how well you can remember pictures and recreate them.&nbsp; When you open the link you will see a face, you will then have <font color=\"#ff0000\"><strong>6</strong></font> seconds to memorise.&nbsp; You must use your memory to re-create the face you have just seen. <br />\n<br />\nHave a play, have more than one go an see if you can improve you memory!<br />\n</font>','','',0,0,81,0,0,0,0,'',0,0),(19,NULL,'2008-04-11 15:35:40',18,'Resource Sharing',0,0,'<h2 style=\"text-align: center;\">This is a Resource Sharing Activity.</h2>\n<div style=\"text-align: center;\">You can see that the list below contains a URL, and several Zipped Websites.</div>\n<div style=\"text-align: center;\">&nbsp;</div>\n<div style=\"text-align: center;\"><b>Explore the resources below, and then try adding your own URL to the list - </b><br />\nsimply fill in the fields below, marked &quot;Suggest a new URL&quot;<br />\nand then click the <b>Add</b> button.</div>\n<div style=\"text-align: center;\">&nbsp;</div>','','',0,0,82,0,1,0,0,'',0,0),(20,'2008-04-11 16:01:40','2008-04-11 16:01:40',19,'Resources on LAMS',0,0,'<h2 style=\"text-align: center;\"><b>Here are some interesting websites about LAMS!</b></h2>\n<div style=\"text-align: center;\"><b><br type=\"_moz\" />\n</b></div>','','',0,0,90,0,0,0,0,'',0,0),(21,NULL,'2008-06-06 10:56:27',20,'Resources',0,0,'<div>Instructions</div>','','',0,0,107,0,0,0,1,'',0,0),(22,NULL,'2008-06-06 11:52:30',21,'Monitor Wink',0,0,'<div>Instructions</div>','','',0,0,108,0,0,0,1,'',0,0),(23,NULL,'2008-06-06 11:53:51',22,'Resources',0,0,'<div>Instructions</div>','','',0,0,110,0,0,0,1,'',0,0),(24,NULL,'2008-06-05 12:32:17',23,'Resources',0,0,'<div>Instructions</div>','','',0,0,111,0,0,0,1,'',0,0),(25,NULL,'2008-06-05 12:03:37',24,'Learning Environment',0,0,'','','',0,0,113,0,0,0,1,'',0,0),(26,NULL,'2008-06-05 11:45:33',25,'Resources',0,0,'<div>Instructions</div>','','',0,0,116,0,0,0,1,'',0,0),(27,NULL,'2008-06-05 10:37:56',26,'So What Is LAMS?',0,0,'<div><span style=\"font-size: small;\"> </span></div>','','',0,0,118,0,0,0,1,'',0,0);
/*!40000 ALTER TABLE `tl_larsrc11_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_resource_item`
--

DROP TABLE IF EXISTS `tl_larsrc11_resource_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_resource_item` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_uuid` bigint(20) DEFAULT NULL,
  `file_version_id` bigint(20) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `ims_schema` varchar(255) DEFAULT NULL,
  `init_item` varchar(255) DEFAULT NULL,
  `organization_xml` text,
  `title` varchar(255) DEFAULT NULL,
  `url` text,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by_author` tinyint(4) DEFAULT NULL,
  `is_hide` tinyint(4) DEFAULT NULL,
  `item_type` smallint(6) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `open_url_new_window` tinyint(4) DEFAULT NULL,
  `resource_uid` bigint(20) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKF52D1F93758092FB` (`create_by`),
  KEY `FKF52D1F9330E79035` (`resource_uid`),
  KEY `FKF52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FKF52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_larsrc11_session` (`uid`),
  CONSTRAINT `FKF52D1F9330E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`),
  CONSTRAINT `FKF52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_larsrc11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_larsrc11_resource_item`
--

LOCK TABLES `tl_larsrc11_resource_item` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource_item` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,'Web Search','http://www.google.com ',NULL,'2010-04-01 15:56:49',1,0,1,NULL,NULL,0,1,NULL),(2,1,1,NULL,'ADL SCORM 1.2','launch.html','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<organizations version=\"imscp1\">\n  <item identifier=\"ORG-139671ed-4b47-4739-8e6d-cd08168a8ead\" title=\"Brain Needs\" default=\"false\">\n    <item identifier=\"ITEM-07a723b1-8b29-4a30-be69-4d1a0d869662\" title=\"Start Brain Needs\" resource=\"launch.html\" default=\"true\" />\n  </item>\n</organizations>\n\n','Brain Needs',NULL,1,'2007-02-07 16:29:25',1,0,4,'application/zip','Brain_Needs.zip',0,2,NULL),(3,NULL,NULL,NULL,NULL,NULL,NULL,'Brain Facts','http://www.bbc.co.uk/science/humanbody/body/interactives/organs/brainmap/',2,'2006-12-06 12:09:55',1,0,1,NULL,NULL,0,3,NULL),(4,38,1,NULL,NULL,NULL,NULL,'Matching Pairs',NULL,3,'2007-01-31 15:10:06',1,0,2,'application/x-shockwave-flash','matchingpairs.swf',0,4,NULL),(5,39,1,NULL,NULL,NULL,NULL,'Remember the Faces',NULL,4,'2007-01-31 14:59:08',1,0,2,'application/x-shockwave-flash','face2.swf',0,5,NULL),(6,40,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Noticeboard',NULL,5,'2008-04-10 13:52:15',1,0,3,'application/x-zip-compressed','lanb11.zip',0,6,NULL),(7,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Wiki - Resource Sharing [online]','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11',5,'2008-04-10 13:51:46',1,0,1,NULL,NULL,0,6,NULL),(8,44,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Resource Sharing',NULL,5,'2008-04-10 13:52:47',1,0,3,'application/x-zip-compressed','lasrsc11.zip',0,6,NULL),(9,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Documentation Website','http://wiki.lamsfoundation.org/display/lamsdocs/Home',6,'2008-04-11 16:00:25',1,0,1,NULL,NULL,1,7,NULL),(10,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2008 Conference - Cadiz, Spain','http://lams2008.lamsfoundation.org/',6,'2008-04-11 16:00:59',1,0,1,NULL,NULL,1,7,NULL),(11,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2007 European Conference - London, UK','http://lams2007.lamsfoundation.org/',6,'2008-04-11 16:01:31',1,0,1,NULL,NULL,1,7,NULL),(12,48,1,NULL,NULL,'index.htm',NULL,'Monitor Wink',NULL,7,'2008-04-11 12:16:58',1,0,3,'application/x-zip-compressed','introToMonitor.zip',0,8,NULL),(13,NULL,NULL,NULL,NULL,NULL,NULL,'Lams Wiki - System Administration [Online]','http://wiki.lamsfoundation.org/display/lamsdocs/System+Administration/',8,'2008-03-12 16:39:41',1,0,1,NULL,NULL,0,9,NULL),(14,52,1,NULL,NULL,'index.htm',NULL,'Authoring Environment',NULL,9,'2008-04-11 15:01:31',1,0,3,'application/x-zip-compressed','introToAuthoring.zip',0,10,NULL),(15,56,1,NULL,NULL,'index.htm',NULL,'Learner\'s Experience',NULL,10,'2008-05-02 16:38:53',1,0,3,'application/zip','learnerExperience.zip',0,11,NULL),(16,60,1,NULL,NULL,'index.htm',NULL,'No Flash Learner Environment',NULL,11,'2008-04-11 17:36:30',1,0,3,'application/zip','noflash.zip',0,12,NULL),(17,64,1,NULL,NULL,'index.htm',NULL,'Introduction to Authoring',NULL,12,'2008-04-04 11:25:16',1,0,3,'application/zip','effective_authoring.zip',0,13,NULL),(18,68,1,NULL,NULL,'index.htm',NULL,'Introducing LAMS!',NULL,13,'2008-06-05 10:35:56',1,0,3,'application/zip','lams101-effective learning.zip',0,14,NULL),(19,38,1,NULL,NULL,NULL,NULL,'Matching Pairs',NULL,14,'2007-01-31 15:10:06',1,0,2,'application/x-shockwave-flash','matchingpairs.swf',0,15,NULL),(20,NULL,NULL,NULL,NULL,NULL,NULL,'Brain Facts','http://www.bbc.co.uk/science/humanbody/body/interactives/organs/brainmap/',15,'2006-12-06 12:09:55',1,0,1,NULL,NULL,0,16,NULL),(21,1,1,NULL,'ADL SCORM 1.2','launch.html','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<organizations version=\"imscp1\">\n  <item identifier=\"ORG-139671ed-4b47-4739-8e6d-cd08168a8ead\" title=\"Brain Needs\" default=\"false\">\n    <item identifier=\"ITEM-07a723b1-8b29-4a30-be69-4d1a0d869662\" title=\"Start Brain Needs\" resource=\"launch.html\" default=\"true\" />\n  </item>\n</organizations>\n\n','Brain Needs',NULL,16,'2007-02-07 16:29:25',1,0,4,'application/zip','Brain_Needs.zip',0,17,NULL),(22,39,1,NULL,NULL,NULL,NULL,'Remember the Faces',NULL,17,'2007-01-31 14:59:08',1,0,2,'application/x-shockwave-flash','face2.swf',0,18,NULL),(23,40,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Noticeboard',NULL,18,'2008-04-10 13:52:15',1,0,3,'application/x-zip-compressed','lanb11.zip',0,19,NULL),(24,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Wiki - Resource Sharing [online]','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11',18,'2008-04-10 13:51:46',1,0,1,NULL,NULL,0,19,NULL),(25,44,1,NULL,NULL,'index.htm',NULL,'LAMS Tutorial - Resource Sharing',NULL,18,'2008-04-10 13:52:47',1,0,3,'application/x-zip-compressed','lasrsc11.zip',0,19,NULL),(26,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS Documentation Website','http://wiki.lamsfoundation.org/display/lamsdocs/Home',19,'2008-04-11 16:00:25',1,0,1,NULL,NULL,1,20,NULL),(27,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2008 Conference - Cadiz, Spain','http://lams2008.lamsfoundation.org/',19,'2008-04-11 16:00:59',1,0,1,NULL,NULL,1,20,NULL),(28,NULL,NULL,NULL,NULL,NULL,NULL,'LAMS 2007 European Conference - London, UK','http://lams2007.lamsfoundation.org/',19,'2008-04-11 16:01:31',1,0,1,NULL,NULL,1,20,NULL),(29,52,1,NULL,NULL,'index.htm',NULL,'Authoring Environment',NULL,20,'2008-04-11 15:01:31',1,0,3,'application/x-zip-compressed','introToAuthoring.zip',0,21,NULL),(30,48,1,NULL,NULL,'index.htm',NULL,'Monitor Wink',NULL,21,'2008-04-11 12:16:58',1,0,3,'application/x-zip-compressed','introToMonitor.zip',0,22,NULL),(31,NULL,NULL,NULL,NULL,NULL,NULL,'Lams Wiki - System Administration [Online]','http://wiki.lamsfoundation.org/display/lamsdocs/System+Administration/',22,'2008-03-12 16:39:41',1,0,1,NULL,NULL,0,23,NULL),(32,60,1,NULL,NULL,'index.htm',NULL,'No Flash Learner Environment',NULL,23,'2008-04-11 17:36:30',1,0,3,'application/zip','noflash.zip',0,24,NULL),(33,56,1,NULL,NULL,'index.htm',NULL,'Learner\'s Experience',NULL,24,'2008-05-02 16:38:53',1,0,3,'application/zip','learnerExperience.zip',0,25,NULL),(34,64,1,NULL,NULL,'index.htm',NULL,'Introduction to Authoring',NULL,25,'2008-04-04 11:25:16',1,0,3,'application/zip','effective_authoring.zip',0,26,NULL),(35,68,1,NULL,NULL,'index.htm',NULL,'Introducing LAMS!',NULL,26,'2008-06-05 10:35:56',1,0,3,'application/zip','lams101-effective learning.zip',0,27,NULL);
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_session`
--

DROP TABLE IF EXISTS `tl_larsrc11_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `resource_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK24AA78C530E79035` (`resource_uid`),
  CONSTRAINT `FK24AA78C530E79035` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_larsrc11_session`
--

LOCK TABLES `tl_larsrc11_session` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_session` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_session` VALUES (1,NULL,NULL,0,17,2,'LAMS Demo Course learners'),(2,NULL,NULL,0,18,3,'LAMS Demo Course learners'),(3,NULL,NULL,0,15,7,'LAMS Demo Course learners'),(4,NULL,NULL,0,16,9,'LAMS Demo Course learners'),(5,NULL,NULL,0,19,14,'LAMS Demo Course learners'),(6,NULL,NULL,0,20,19,'LAMS Demo Course learners'),(7,NULL,NULL,0,22,36,'LAMS Demo Course learners'),(8,NULL,NULL,0,21,38,'LAMS Demo Course learners'),(9,NULL,NULL,0,23,39,'LAMS Demo Course learners'),(10,NULL,NULL,0,25,42,'LAMS Demo Course learners'),(11,NULL,NULL,0,24,43,'LAMS Demo Course learners'),(12,NULL,NULL,0,27,46,'LAMS Demo Course learners'),(13,NULL,NULL,0,26,47,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_larsrc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_user`
--

DROP TABLE IF EXISTS `tl_larsrc11_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_larsrc11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `resource_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK30113BFCEC0D3147` (`session_uid`),
  KEY `FK30113BFC309ED320` (`resource_uid`),
  CONSTRAINT `FK30113BFC309ED320` FOREIGN KEY (`resource_uid`) REFERENCES `tl_larsrc11_resource` (`uid`),
  CONSTRAINT `FK30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_larsrc11_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_larsrc11_user`
--

LOCK TABLES `tl_larsrc11_user` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_user` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_user` VALUES (1,5,'Test','One','test1',0,NULL,2),(2,5,'Test','One','test1',0,NULL,3),(3,5,'Test','One','test1',0,NULL,4),(4,5,'Test','One','test1',0,NULL,5),(5,5,'Test','One','test1',0,NULL,6),(6,5,'Test','One','test1',0,NULL,7),(7,5,'Test','One','test1',0,NULL,8),(8,5,'Test','One','test1',0,NULL,9),(9,5,'Test','One','test1',0,NULL,10),(10,5,'Test','One','test1',0,NULL,11),(11,5,'Test','One','test1',0,NULL,12),(12,5,'Test','One','test1',0,NULL,13),(13,5,'Test','One','test1',0,NULL,14),(14,5,'Test','One','test1',0,NULL,15),(15,5,'Test','One','test1',0,NULL,16),(16,5,'Test','One','test1',0,NULL,17),(17,5,'Test','One','test1',0,NULL,18),(18,5,'Test','One','test1',0,NULL,19),(19,5,'Test','One','test1',0,NULL,20),(20,5,'Test','One','test1',0,NULL,21),(21,5,'Test','One','test1',0,NULL,22),(22,5,'Test','One','test1',0,NULL,23),(23,5,'Test','One','test1',0,NULL,24),(24,5,'Test','One','test1',0,NULL,25),(25,5,'Test','One','test1',0,NULL,26),(26,5,'Test','One','test1',0,NULL,27);
/*!40000 ALTER TABLE `tl_larsrc11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_content`
--

DROP TABLE IF EXISTS `tl_lasbmt11_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_content` (
  `content_id` bigint(20) NOT NULL,
  `title` varchar(64) NOT NULL,
  `instruction` text,
  `define_later` smallint(6) NOT NULL,
  `run_offline` smallint(6) NOT NULL,
  `offline_instruction` text,
  `online_instruction` text,
  `content_in_use` smallint(6) DEFAULT NULL,
  `lock_on_finished` smallint(6) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  `limit_upload` smallint(6) DEFAULT NULL,
  `limit_upload_number` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `mark_release_notify` tinyint(4) DEFAULT '0',
  `file_submit_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`content_id`),
  KEY `FKAEF329AC172BC670` (`created_by`),
  CONSTRAINT `FKAEF329AC172BC670` FOREIGN KEY (`created_by`) REFERENCES `tl_lasbmt11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasbmt11_content`
--

LOCK TABLES `tl_lasbmt11_content` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_content` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_content` VALUES (4,'Submit Files','Instructions',0,0,NULL,NULL,0,0,NULL,0,0,1,NULL,NULL,NULL,0,0),(49,'Essay Submission','<h2>This is the Learner Interface for Submit Files.</h2>\n<div><b>Again, LAMS provides a space for Authors to write Instructions for Learners:</b></div>\n<div>&nbsp;</div>\n<hr />\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Use this activity to submit your essay for Marking.</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">&nbsp;</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Please remember that your attachments should be Word Documents (.doc), Adobe Documents (.pdf), or Text Files (.txt) so that I can read them on my computer.</span><br />\n</span></div>\n<hr />\n<div>&nbsp;</div>\n<div>You have the option of uploading a file here if you would like - have a play with the activity and see what you can do.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>',0,0,'','',0,0,'',0,0,0,'2010-04-01 16:03:17',1,'2010-04-01 16:03:17',0,0),(97,'Essay Submission','<h2>This is the Learner Interface for Submit Files.</h2>\n<div><b>Again, LAMS provides a space for Authors to write Instructions for Learners:</b></div>\n<div>&nbsp;</div>\n<hr />\n<div><span style=\"font-size: small;\">&nbsp;</span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Use this activity to submit your essay for Marking.</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">&nbsp;</span></span></div>\n<div><span style=\"font-size: small;\"><span style=\"font-family: Comic Sans MS;\">Please remember that your attachments should be Word Documents (.doc), Adobe Documents (.pdf), or Text Files (.txt) so that I can read them on my computer.</span><br />\n</span></div>\n<hr />\n<div>&nbsp;</div>\n<div>You have the option of uploading a file here if you would like - have a play with the activity and see what you can do.</div>\n<div>&nbsp;</div>\n<hr />\n<div>&nbsp;</div>',0,0,'','',0,0,'',0,0,0,'2010-04-01 16:03:17',1,'2010-04-01 16:03:17',0,0);
/*!40000 ALTER TABLE `tl_lasbmt11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_instruction_files`
--

DROP TABLE IF EXISTS `tl_lasbmt11_instruction_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_instruction_files` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` bigint(20) DEFAULT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKA75538F9785A173A` (`content_id`),
  CONSTRAINT `FKA75538F9785A173A` FOREIGN KEY (`content_id`) REFERENCES `tl_lasbmt11_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_report` (
  `report_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `comments` text,
  `marks` float DEFAULT NULL,
  `date_marks_released` datetime DEFAULT NULL,
  `mark_file_uuid` bigint(20) DEFAULT NULL,
  `mark_file_version_id` bigint(20) DEFAULT NULL,
  `mark_file_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_session` (
  `session_id` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`session_id`),
  KEY `FKEC8C77C9785A173A` (`content_id`),
  CONSTRAINT `FKEC8C77C9785A173A` FOREIGN KEY (`content_id`) REFERENCES `tl_lasbmt11_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasbmt11_session`
--

LOCK TABLES `tl_lasbmt11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_session` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_session` VALUES (24,0,97,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_lasbmt11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_submission_details`
--

DROP TABLE IF EXISTS `tl_lasbmt11_submission_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_submission_details` (
  `submission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filePath` varchar(250) DEFAULT NULL,
  `fileDescription` varchar(250) DEFAULT NULL,
  `date_of_submission` datetime DEFAULT NULL,
  `uuid` bigint(20) DEFAULT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `learner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`submission_id`),
  KEY `FK1411A53CFFD5A38B` (`learner_id`),
  KEY `FK1411A53C93C861A` (`session_id`),
  CONSTRAINT `FK1411A53C93C861A` FOREIGN KEY (`session_id`) REFERENCES `tl_lasbmt11_session` (`session_id`),
  CONSTRAINT `FK1411A53CFFD5A38B` FOREIGN KEY (`learner_id`) REFERENCES `tl_lasbmt11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasbmt11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `finished` bit(1) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasbmt11_user`
--

LOCK TABLES `tl_lasbmt11_user` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_user` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_user` VALUES (1,5,0x00,NULL,'One','test1','Test',49);
/*!40000 ALTER TABLE `tl_lasbmt11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_attachment`
--

DROP TABLE IF EXISTS `tl_lascrb11_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `scribe_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK57953706B3FA1495` (`scribe_uid`),
  CONSTRAINT `FK57953706B3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_heading` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `heading` text,
  `scribe_uid` bigint(20) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK428A22FFB3FA1495` (`scribe_uid`),
  CONSTRAINT `FK428A22FFB3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_report_entry` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `entry_text` text,
  `scribe_heading_uid` bigint(20) DEFAULT NULL,
  `scribe_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK5439FACAEA50D086` (`scribe_heading_uid`),
  KEY `FK5439FACA1C266FAE` (`scribe_session_uid`),
  CONSTRAINT `FK5439FACA1C266FAE` FOREIGN KEY (`scribe_session_uid`) REFERENCES `tl_lascrb11_session` (`uid`),
  CONSTRAINT `FK5439FACAEA50D086` FOREIGN KEY (`scribe_heading_uid`) REFERENCES `tl_lascrb11_heading` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_scribe` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `auto_select_scribe` bit(1) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `aggregated_reports` bit(1) DEFAULT b'0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `scribe_uid` bigint(20) DEFAULT NULL,
  `appointed_scribe_uid` bigint(20) DEFAULT NULL,
  `force_complete` bit(1) DEFAULT NULL,
  `report_submitted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK89732793B3FA1495` (`scribe_uid`),
  KEY `FK89732793E46919FF` (`appointed_scribe_uid`),
  CONSTRAINT `FK89732793E46919FF` FOREIGN KEY (`appointed_scribe_uid`) REFERENCES `tl_lascrb11_user` (`uid`),
  CONSTRAINT `FK89732793B3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lascrb11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `scribe_session_uid` bigint(20) DEFAULT NULL,
  `report_approved` bit(1) DEFAULT NULL,
  `started_activity` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK187DAFEE1C266FAE` (`scribe_session_uid`),
  CONSTRAINT `FK187DAFEE1C266FAE` FOREIGN KEY (`scribe_session_uid`) REFERENCES `tl_lascrb11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `spreadsheet_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_2065267438_1E7009430E79035` (`spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_1E7009430E79035` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `spreadsheet_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_2065267438_24AA78C530E79035` (`spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_24AA78C530E79035` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasprd10_session`
--

LOCK TABLES `tl_lasprd10_session` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_session` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_session` VALUES (1,NULL,NULL,0,3,13,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_lasprd10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_spreadsheet` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT NULL,
  `is_learner_allowed_to_save` tinyint(4) DEFAULT NULL,
  `is_marking_enabled` tinyint(4) DEFAULT NULL,
  `lock_on_finished` tinyint(4) DEFAULT NULL,
  `instructions` text,
  `code` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT NULL,
  `define_later` tinyint(4) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_2065267438_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_2065267438_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_lasprd10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasprd10_spreadsheet`
--

LOCK TABLES `tl_lasprd10_spreadsheet` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_spreadsheet` VALUES (1,NULL,NULL,NULL,'Spreadsheet',0,1,0,0,'Instructions ','',NULL,NULL,0,0,13,NULL,0),(2,NULL,'2008-11-14 14:29:02',1,'Spreadsheet Demonstration',0,1,0,0,'<div>\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Spreadsheet</h2>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>\n<p>The <b>Spreadsheet Activity</b> allows a sequence author to provide data in a spreadsheet format to Learners. <br clear=\"all\" />\nLearners can then perform their own calculations on and manipulations of the data.</p>\n<p>By default, the Spreadsheet tool allows each learner to save their own individual copy of the spreadsheet. The activity can be configured to allow Monitors to mark each Learners spreadsheet.</p>\n<h5>Spreadsheet Functions</h5>\n<p>The Spreadsheet can be viewed in one of several modes. These are changed by clicking the labels in the top-right corner of the applet.</p>\n<ul>\n    <li>Values: shows all the calculated values on the screen (default mode)\n    <ul>\n        <li>Auto: calculates and updates values in real time</li>\n        <li>Manual: Disables automatic calculations (gives better performance)\n        <ul>\n            <li>Click <b>Refresh</b> to update the values in the spreadsheet if Manual mode is selected.</li>\n        </ul>\n        </li>\n    </ul>\n    </li>\n    <li>Formulas: shows the formulas in the spreadsheet.</li>\n</ul>\n<div>&nbsp;</div>\n<div><b>Values </b>displays the <b>actual values</b> of the calculations and data in each cell.</div>\n<div><b>Formulas </b>shows the cacluations behind the cell.</div>\n<h4 style=\"text-align: center;\">Explore the Demonstration Spreadsheet below.</h4>\n<h4>&nbsp;</h4>\n</div>\n</div>','dbCells = [\n  [0,0,\"SALES 3rd Quarter\",\"\"], // A1\n\n  [1,1,\"John\",\"\"], // B2\n  [2,1,\"Paul\",\"\"], // C2\n  [3,1,\"Michael\",\"\"], // D2\n  [5,1,\"MONTHLY Totals\",\"\"], // F2\n  [6,1,\"MONTHLY Average\",\"\"], // G2\n\n  [0,2,\"JULY\",\"\"], // A3\n  [1,2,\"210\",\"\"], // B3\n  [2,2,\"195\",\"\"], // C3\n  [3,2,\"203\",\"\"], // D3\n  [5,2,\"=sum(B3:D3)\",\"\"], // F3\n  [6,2,\"=avg(B3:D3)\",\"\"], // G3\n\n  [0,3,\"AUGUST\",\"\"], // A4\n  [1,3,\"312\",\"\"], // B4\n  [2,3,\"263\",\"\"], // C4\n  [3,3,\"278\",\"\"], // D4\n  [5,3,\"=sum(B4:D4)\",\"\"], // F4\n  [6,3,\"=avg(B4:D4)\",\"\"], // G4\n\n  [0,4,\"SEPTEMBER\",\"\"], // A5\n  [1,4,\"280\",\"\"], // B5\n  [2,4,\"243\",\"\"], // C5\n  [3,4,\"216\",\"\"], // D5\n  [5,4,\"=sum(B5:D5)\",\"\"], // F5\n  [6,4,\"=avg(B5:D5)\",\"\"], // G5\n\n  [0,6,\"Seller Totals\",\"\"], // A7\n  [1,6,\"=sum(B3:B5)\",\"\"], // B7\n  [2,6,\"=sum(C3:C5)\",\"\"], // C7\n  [3,6,\"=sum(D3:D5)\",\"\"], // D7\n  [4,6,\"GRAND Total\",\"\"], // E7\n  [5,6,\"=sum(B3:D5)\",\"\"], // F7\n];\n','','',0,0,39,'',0),(3,NULL,'2008-11-14 14:29:02',2,'Spreadsheet Demonstration',0,1,0,0,'<div>\n<h2 style=\"color: rgb(255, 102, 0); text-align: left;\"><img width=\"128\" height=\"55\" align=\"absmiddle\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//lanb11.PNG\" />Spreadsheet</h2>\n<p style=\"text-align: center;\"><span style=\"font-size: small;\"><b><span style=\"background-color: rgb(255, 255, 0);\">New to LAMS 2.2!</span></b></span></p>\n<div>\n<p>The <b>Spreadsheet Activity</b> allows a sequence author to provide data in a spreadsheet format to Learners. <br clear=\"all\" />\nLearners can then perform their own calculations on and manipulations of the data.</p>\n<p>By default, the Spreadsheet tool allows each learner to save their own individual copy of the spreadsheet. The activity can be configured to allow Monitors to mark each Learners spreadsheet.</p>\n<h5>Spreadsheet Functions</h5>\n<p>The Spreadsheet can be viewed in one of several modes. These are changed by clicking the labels in the top-right corner of the applet.</p>\n<ul>\n    <li>Values: shows all the calculated values on the screen (default mode)\n    <ul>\n        <li>Auto: calculates and updates values in real time</li>\n        <li>Manual: Disables automatic calculations (gives better performance)\n        <ul>\n            <li>Click <b>Refresh</b> to update the values in the spreadsheet if Manual mode is selected.</li>\n        </ul>\n        </li>\n    </ul>\n    </li>\n    <li>Formulas: shows the formulas in the spreadsheet.</li>\n</ul>\n<div>&nbsp;</div>\n<div><b>Values </b>displays the <b>actual values</b> of the calculations and data in each cell.</div>\n<div><b>Formulas </b>shows the cacluations behind the cell.</div>\n<h4 style=\"text-align: center;\">Explore the Demonstration Spreadsheet below.</h4>\n<h4>&nbsp;</h4>\n</div>\n</div>','dbCells = [\n  [0,0,\"SALES 3rd Quarter\",\"\"], // A1\n\n  [1,1,\"John\",\"\"], // B2\n  [2,1,\"Paul\",\"\"], // C2\n  [3,1,\"Michael\",\"\"], // D2\n  [5,1,\"MONTHLY Totals\",\"\"], // F2\n  [6,1,\"MONTHLY Average\",\"\"], // G2\n\n  [0,2,\"JULY\",\"\"], // A3\n  [1,2,\"210\",\"\"], // B3\n  [2,2,\"195\",\"\"], // C3\n  [3,2,\"203\",\"\"], // D3\n  [5,2,\"=sum(B3:D3)\",\"\"], // F3\n  [6,2,\"=avg(B3:D3)\",\"\"], // G3\n\n  [0,3,\"AUGUST\",\"\"], // A4\n  [1,3,\"312\",\"\"], // B4\n  [2,3,\"263\",\"\"], // C4\n  [3,3,\"278\",\"\"], // D4\n  [5,3,\"=sum(B4:D4)\",\"\"], // F4\n  [6,3,\"=avg(B4:D4)\",\"\"], // G4\n\n  [0,4,\"SEPTEMBER\",\"\"], // A5\n  [1,4,\"280\",\"\"], // B5\n  [2,4,\"243\",\"\"], // C5\n  [3,4,\"216\",\"\"], // D5\n  [5,4,\"=sum(B5:D5)\",\"\"], // F5\n  [6,4,\"=avg(B5:D5)\",\"\"], // G5\n\n  [0,6,\"Seller Totals\",\"\"], // A7\n  [1,6,\"=sum(B3:B5)\",\"\"], // B7\n  [2,6,\"=sum(C3:C5)\",\"\"], // C7\n  [3,6,\"=sum(D3:D5)\",\"\"], // D7\n  [4,6,\"GRAND Total\",\"\"], // E7\n  [5,6,\"=sum(B3:D5)\",\"\"], // F7\n];\n','','',0,0,85,'',0);
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet_mark`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet_mark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_spreadsheet_mark` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `marks` varchar(255) DEFAULT NULL,
  `comments` text,
  `date_marks_released` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `spreadsheet_uid` bigint(20) DEFAULT NULL,
  `user_modified_spreadsheet_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_2065267438_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_2065267438_30113BFC309ED320` (`spreadsheet_uid`),
  KEY `FK_NEW_2065267438_693580A441F9365D` (`user_modified_spreadsheet_uid`),
  CONSTRAINT `FK_NEW_2065267438_693580A441F9365D` FOREIGN KEY (`user_modified_spreadsheet_uid`) REFERENCES `tl_lasprd10_user_modified_spreadsheet` (`uid`),
  CONSTRAINT `FK_NEW_2065267438_30113BFC309ED320` FOREIGN KEY (`spreadsheet_uid`) REFERENCES `tl_lasprd10_spreadsheet` (`uid`),
  CONSTRAINT `FK_NEW_2065267438_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_lasprd10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasprd10_user_modified_spreadsheet` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_modified_spreadsheet` text,
  `mark_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_lasprd10_user_modified_spreadsheet_1` (`mark_id`),
  CONSTRAINT `FK_tl_lasprd10_user_modified_spreadsheet_1` FOREIGN KEY (`mark_id`) REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_answer` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `question_uid` bigint(20) DEFAULT NULL,
  `user_uid` bigint(20) DEFAULT NULL,
  `answer_choices` varchar(255) DEFAULT NULL,
  `udpate_date` datetime DEFAULT NULL,
  `answer_text` text,
  PRIMARY KEY (`uid`),
  KEY `FK6DAAFE3BB1423DC1` (`user_uid`),
  KEY `FK6DAAFE3B25F3BB77` (`question_uid`),
  CONSTRAINT `FK6DAAFE3B25F3BB77` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`),
  CONSTRAINT `FK6DAAFE3BB1423DC1` FOREIGN KEY (`user_uid`) REFERENCES `tl_lasurv11_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `survey_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKD92A9120D14146E5` (`survey_uid`),
  CONSTRAINT `FKD92A9120D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_condition_questions` (
  `condition_id` bigint(20) NOT NULL DEFAULT '0',
  `question_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`condition_id`,`question_uid`),
  KEY `SurveyConditionQuestionToSurveyQuestion` (`question_uid`),
  CONSTRAINT `SurveyConditionQuestionToSurveyCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lasurv11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionQuestionToSurveyQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `SurveyConditionToSurvey` (`content_uid`),
  CONSTRAINT `SurveyConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionToSurvey` FOREIGN KEY (`content_uid`) REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_option` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` text,
  `sequence_id` int(11) DEFAULT NULL,
  `question_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK85AB46F26966134F` (`question_uid`),
  CONSTRAINT `FK85AB46F26966134F` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasurv11_option`
--

LOCK TABLES `tl_lasurv11_option` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_option` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_option` VALUES (1,'Option 1',0,1),(2,'Option 2',1,1),(3,'Option 3',2,1),(4,'Option 1',0,2),(5,'Option 2',1,2),(6,'Option 3',2,2),(7,'Puppy',2,5),(8,'Pony',0,5),(9,'Lamb!',1,5),(10,'Oranges',2,6),(11,'Banana',1,6),(12,'Apple',0,6),(13,'Lamb!',1,8),(14,'Pony',0,8),(15,'Puppy',2,8),(16,'Oranges',2,9),(17,'Apple',0,9),(18,'Banana',1,9);
/*!40000 ALTER TABLE `tl_lasurv11_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_question`
--

DROP TABLE IF EXISTS `tl_lasurv11_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_question` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `sequence_id` int(11) DEFAULT NULL,
  `description` text,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `question_type` smallint(6) DEFAULT NULL,
  `append_text` smallint(6) DEFAULT NULL,
  `optional` smallint(6) DEFAULT NULL,
  `allow_multiple_answer` smallint(6) DEFAULT NULL,
  `survey_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK872D4F23D14146E5` (`survey_uid`),
  KEY `FK872D4F23E4C99A5F` (`create_by`),
  CONSTRAINT `FK872D4F23E4C99A5F` FOREIGN KEY (`create_by`) REFERENCES `tl_lasurv11_user` (`uid`),
  CONSTRAINT `FK872D4F23D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasurv11_question`
--

LOCK TABLES `tl_lasurv11_question` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_question` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_question` VALUES (1,1,'Sample Multiple choice - only one response allowed?',NULL,'2010-04-01 15:57:05',1,0,0,0,1),(2,2,'Sample Multiple choice - multiple response allowed?',NULL,'2010-04-01 15:57:05',2,0,0,1,1),(3,3,'Sample Free text question?',NULL,'2010-04-01 15:57:05',3,0,0,0,1),(4,3,'<div><b>What\'s the best thing about LAMS?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',3,0,0,0,2),(5,1,'<div><b>What\'s your favourite baby animal?<br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',1,0,0,0,2),(6,2,'<div><b>Which fruits do you like?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',1,'2008-03-12 16:40:31',2,0,0,1,2),(7,3,'<div><b>What\'s the best thing about LAMS?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',3,0,0,0,3),(8,1,'<div><b>What\'s your favourite baby animal?<br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',1,0,0,0,3),(9,2,'<div><b>Which fruits do you like?<br />\n</b></div>\n<div><b><br type=\"_moz\" />\n</b></div>',2,'2008-03-12 16:40:31',2,0,0,1,3);
/*!40000 ALTER TABLE `tl_lasurv11_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_session`
--

DROP TABLE IF EXISTS `tl_lasurv11_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `survey_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKF08793B9D14146E5` (`survey_uid`),
  CONSTRAINT `FKF08793B9D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasurv11_session`
--

LOCK TABLES `tl_lasurv11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_session` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_session` VALUES (1,NULL,NULL,3,23,'LAMS Demo Course learners');
/*!40000 ALTER TABLE `tl_lasurv11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_survey`
--

DROP TABLE IF EXISTS `tl_lasurv11_survey`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_survey` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` smallint(6) DEFAULT NULL,
  `lock_on_finished` smallint(6) DEFAULT NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` smallint(6) DEFAULT NULL,
  `define_later` smallint(6) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  `show_questions_on_one_page` smallint(6) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `answer_submit_notify` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK8CC465D7E4C99A5F` (`create_by`),
  CONSTRAINT `FK8CC465D7E4C99A5F` FOREIGN KEY (`create_by`) REFERENCES `tl_lasurv11_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lasurv11_survey`
--

LOCK TABLES `tl_lasurv11_survey` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_survey` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_survey` VALUES (1,'Survey',0,1,'Instructions',NULL,NULL,0,0,9,NULL,0,1,NULL,NULL,NULL,0),(2,'Survey Demonstration',0,1,'<div><img hspace=\"6\" align=\"left\" width=\"116\" height=\"103\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h2>This is the Survey activity.&nbsp;</h2>\n<h3>&nbsp;</h3>\n<h3>Like all activities, this is the space for Learner Instructions.</h3>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h4><b>Below, you&rsquo;ll see several different types of Survey questions:</b>&nbsp;</h4>\n<ul>\n    <li>Single Response Multiple Choice</li>\n    <li>Multiple Choice Multiple Choice</li>\n    <li>Free Text Questions</li>\n</ul>\n<div>&nbsp;</div>','','',0,0,48,'',0,1,NULL,'2008-04-18 15:51:31',1,0),(3,'Survey Demonstration',0,1,'<div><img hspace=\"6\" align=\"left\" width=\"116\" height=\"103\" alt=\"\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" /></div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h2>This is the Survey activity.&nbsp;</h2>\n<h3>&nbsp;</h3>\n<h3>Like all activities, this is the space for Learner Instructions.</h3>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h4><b>Below, you&rsquo;ll see several different types of Survey questions:</b>&nbsp;</h4>\n<ul>\n    <li>Single Response Multiple Choice</li>\n    <li>Multiple Choice Multiple Choice</li>\n    <li>Free Text Questions</li>\n</ul>\n<div>&nbsp;</div>','','',0,0,102,'',0,1,NULL,'2008-04-18 15:51:31',2,0);
/*!40000 ALTER TABLE `tl_lasurv11_survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_user`
--

DROP TABLE IF EXISTS `tl_lasurv11_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lasurv11_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `survey_uid` bigint(20) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK633F25884F803F63` (`session_uid`),
  KEY `FK633F2588D14146E5` (`survey_uid`),
  CONSTRAINT `FK633F2588D14146E5` FOREIGN KEY (`survey_uid`) REFERENCES `tl_lasurv11_survey` (`uid`),
  CONSTRAINT `FK633F25884F803F63` FOREIGN KEY (`session_uid`) REFERENCES `tl_lasurv11_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `taskList_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_174079138_1E7009430E79035` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_1E7009430E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_condition` (
  `condition_uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `sequence_id` int(11) DEFAULT NULL,
  `taskList_uid` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`condition_uid`),
  KEY `FK_tl_latask10_condition_1` (`taskList_uid`),
  CONSTRAINT `FK_tl_latask10_condition_1` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_condition_tl_item` (
  `uid` bigint(20) NOT NULL,
  `condition_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`,`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_1` (`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_2` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_2` FOREIGN KEY (`uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_1` FOREIGN KEY (`condition_uid`) REFERENCES `tl_latask10_condition` (`condition_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_item_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `taskList_item_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_latask10_item_attachment_1` (`taskList_item_uid`),
  KEY `FK_tl_latask10_item_attachment_2` (`create_by`),
  CONSTRAINT `FK_tl_latask10_item_attachment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_tl_latask10_item_attachment_1` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_item_comment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text,
  `taskList_item_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_tl_latask10_item_comment_3` (`taskList_item_uid`),
  KEY `FK_tl_latask10_item_comment_2` (`create_by`),
  CONSTRAINT `FK_tl_latask10_item_comment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_tl_latask10_item_comment_3` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_item_log` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `access_date` datetime DEFAULT NULL,
  `taskList_item_uid` bigint(20) DEFAULT NULL,
  `user_uid` bigint(20) DEFAULT NULL,
  `complete` tinyint(4) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_174079138_693580A438BF8DFE` (`taskList_item_uid`),
  KEY `FK_NEW_174079138_693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK_NEW_174079138_693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_latask10_user` (`uid`),
  CONSTRAINT `FK_NEW_174079138_693580A438BF8DFE` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_tasklist_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `taskList_uid` bigint(20) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_174079138_24AA78C530E79035` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_24AA78C530E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_latask10_session`
--

LOCK TABLES `tl_latask10_session` WRITE;
/*!40000 ALTER TABLE `tl_latask10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_taskList`
--

DROP TABLE IF EXISTS `tl_latask10_taskList`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_taskList` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `run_offline` tinyint(4) DEFAULT NULL,
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) DEFAULT NULL,
  `define_later` tinyint(4) DEFAULT NULL,
  `content_id` bigint(20) DEFAULT NULL,
  `lock_when_finished` tinyint(4) DEFAULT NULL,
  `is_sequential_order` tinyint(4) DEFAULT NULL,
  `minimum_number_tasks` int(11) DEFAULT NULL,
  `allow_contribute_tasks` tinyint(4) DEFAULT NULL,
  `is_monitor_verification_required` tinyint(4) DEFAULT NULL,
  `reflect_instructions` varchar(255) DEFAULT NULL,
  `reflect_on_activity` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_174079138_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_174079138_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_latask10_taskList`
--

LOCK TABLES `tl_latask10_taskList` WRITE;
/*!40000 ALTER TABLE `tl_latask10_taskList` DISABLE KEYS */;
INSERT INTO `tl_latask10_taskList` VALUES (1,NULL,NULL,NULL,'Task List',0,'Instructions ',NULL,NULL,0,0,11,0,0,0,0,0,NULL,0);
/*!40000 ALTER TABLE `tl_latask10_taskList` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_taskList_item`
--

DROP TABLE IF EXISTS `tl_latask10_taskList_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_taskList_item` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `sequence_id` int(11) DEFAULT NULL,
  `description` text,
  `init_item` varchar(255) DEFAULT NULL,
  `organization_xml` text,
  `title` varchar(255) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_by_author` tinyint(4) DEFAULT NULL,
  `is_required` tinyint(4) DEFAULT NULL,
  `is_comments_allowed` tinyint(4) DEFAULT NULL,
  `is_comments_required` tinyint(4) DEFAULT NULL,
  `is_files_allowed` tinyint(4) DEFAULT NULL,
  `is_files_required` tinyint(4) DEFAULT NULL,
  `is_comments_files_allowed` tinyint(4) DEFAULT NULL,
  `show_comments_to_all` tinyint(4) DEFAULT NULL,
  `is_child_task` tinyint(4) DEFAULT NULL,
  `parent_task_name` varchar(255) DEFAULT NULL,
  `taskList_uid` bigint(20) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_174079138_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_174079138_F52D1F9330E79035` (`taskList_uid`),
  KEY `FK_NEW_174079138_F52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_latask10_session` (`uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F9330E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_latask10_taskList_item`
--

LOCK TABLES `tl_latask10_taskList_item` WRITE;
/*!40000 ALTER TABLE `tl_latask10_taskList_item` DISABLE KEYS */;
INSERT INTO `tl_latask10_taskList_item` VALUES (1,1,NULL,NULL,NULL,'Task number 1',NULL,'2010-04-01 15:57:18',1,0,0,0,0,0,0,1,0,NULL,1,NULL);
/*!40000 ALTER TABLE `tl_latask10_taskList_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_user`
--

DROP TABLE IF EXISTS `tl_latask10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_latask10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `session_finished` smallint(6) DEFAULT NULL,
  `session_uid` bigint(20) DEFAULT NULL,
  `taskList_uid` bigint(20) DEFAULT NULL,
  `is_verified_by_monitor` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_174079138_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_174079138_30113BFC309ED320` (`taskList_uid`),
  CONSTRAINT `FK_NEW_174079138_30113BFC309ED320` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_tasklist` (`uid`),
  CONSTRAINT `FK_NEW_174079138_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_latask10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_latask10_user`
--

LOCK TABLES `tl_latask10_user` WRITE;
/*!40000 ALTER TABLE `tl_latask10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_latask10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_attachment`
--

DROP TABLE IF EXISTS `tl_lavidr10_attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `videoRecorder_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_75587508_12090F57FC940906` (`videoRecorder_uid`),
  CONSTRAINT `FK_NEW_75587508_12090F57FC940906` FOREIGN KEY (`videoRecorder_uid`) REFERENCES `tl_lavidr10_videorecorder` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_attachment`
--

LOCK TABLES `tl_lavidr10_attachment` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_comment`
--

DROP TABLE IF EXISTS `tl_lavidr10_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_comment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `comment` text,
  `videoRecorder_recording_uid` bigint(20) DEFAULT NULL,
  `videoRecorder_session_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_comment`
--

LOCK TABLES `tl_lavidr10_comment` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_conditions`
--

DROP TABLE IF EXISTS `tl_lavidr10_conditions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`condition_id`),
  KEY `VideoRecorderConditionToVideoRecorder` (`content_uid`),
  CONSTRAINT `VideoRecorderConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `VideoRecorderConditionToVideoRecorder` FOREIGN KEY (`content_uid`) REFERENCES `tl_lavidr10_videorecorder` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_conditions`
--

LOCK TABLES `tl_lavidr10_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_conditions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_rating`
--

DROP TABLE IF EXISTS `tl_lavidr10_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_rating` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `rating` float DEFAULT NULL,
  `videoRecorder_recording_uid` bigint(20) DEFAULT NULL,
  `videoRecorder_session_uid` bigint(20) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_rating`
--

LOCK TABLES `tl_lavidr10_rating` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_rating` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_rating` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_recording`
--

DROP TABLE IF EXISTS `tl_lavidr10_recording`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_recording` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` varchar(1027) DEFAULT NULL,
  `rating` float DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `is_local` bit(1) DEFAULT NULL,
  `is_just_sound` bit(1) DEFAULT NULL,
  `videoRecorder_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_recording`
--

LOCK TABLES `tl_lavidr10_recording` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_recording` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_recording` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_session`
--

DROP TABLE IF EXISTS `tl_lavidr10_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `videoRecorder_uid` bigint(20) DEFAULT NULL,
  `content_folder_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_75587508_B7C198E2FC940906` (`videoRecorder_uid`),
  CONSTRAINT `FK_NEW_75587508_B7C198E2FC940906` FOREIGN KEY (`videoRecorder_uid`) REFERENCES `tl_lavidr10_videorecorder` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_session`
--

LOCK TABLES `tl_lavidr10_session` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_user`
--

DROP TABLE IF EXISTS `tl_lavidr10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `videoRecorder_session_uid` bigint(20) DEFAULT NULL,
  `entry_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK_NEW_75587508_CB8A58FFA3B0FADF` (`videoRecorder_session_uid`),
  CONSTRAINT `FK_NEW_75587508_CB8A58FFA3B0FADF` FOREIGN KEY (`videoRecorder_session_uid`) REFERENCES `tl_lavidr10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_user`
--

LOCK TABLES `tl_lavidr10_user` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavidr10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_videoRecorder`
--

DROP TABLE IF EXISTS `tl_lavidr10_videoRecorder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavidr10_videoRecorder` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `author_recording_id` bigint(20) DEFAULT NULL,
  `allow_use_voice` bit(1) DEFAULT NULL,
  `allow_use_camera` bit(1) DEFAULT NULL,
  `allow_learner_video_visibility` bit(1) DEFAULT NULL,
  `allow_learner_video_export` bit(1) DEFAULT NULL,
  `allow_comments` bit(1) DEFAULT NULL,
  `allow_ratings` bit(1) DEFAULT NULL,
  `export_offline` bit(1) DEFAULT NULL,
  `export_all` bit(1) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavidr10_videoRecorder`
--

LOCK TABLES `tl_lavidr10_videoRecorder` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_videoRecorder` DISABLE KEYS */;
INSERT INTO `tl_lavidr10_videoRecorder` VALUES (1,NULL,NULL,NULL,'VideoRecorder','Instructions',0x00,'',0x00,0x00,'','',0x00,0x00,18,NULL,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01);
/*!40000 ALTER TABLE `tl_lavidr10_videoRecorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_content`
--

DROP TABLE IF EXISTS `tl_lavote11_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `content_id` bigint(20) NOT NULL,
  `title` text,
  `instructions` text,
  `creation_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `maxNominationCount` varchar(20) NOT NULL DEFAULT '1',
  `allowText` tinyint(1) NOT NULL DEFAULT '0',
  `reflect` tinyint(1) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL DEFAULT '0',
  `run_offline` tinyint(1) NOT NULL DEFAULT '0',
  `define_later` tinyint(1) NOT NULL DEFAULT '0',
  `offline_instructions` text,
  `online_instructions` text,
  `content_in_use` tinyint(1) NOT NULL DEFAULT '0',
  `lock_on_finish` tinyint(1) NOT NULL DEFAULT '1',
  `retries` tinyint(1) NOT NULL DEFAULT '0',
  `reflectionSubject` text,
  `show_results` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavote11_content`
--

LOCK TABLES `tl_lavote11_content` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_content` DISABLE KEYS */;
INSERT INTO `tl_lavote11_content` VALUES (1,7,'Voting','Instructions','2010-04-01 15:56:54',NULL,'1',0,0,1,0,0,'','',0,0,0,NULL,1),(2,50,'Voting Demonstration','<div><img width=\"116\" hspace=\"6\" height=\"103\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /></div>\n<div><b>Here&rsquo;s a demonstration Voting activity.</b></div>\n<div>&nbsp;</div>\n<div>While this particular activity is a little bit... <i>trivial...</i> there&rsquo;s no reason why you can&rsquo;t have a very large nomination form and collate responses from as many learners are on your server.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h3>What&rsquo;s your favourite flavour for ice-cream?</h3>',NULL,'2008-04-11 16:39:37','1',1,0,5,0,0,'','',0,0,0,'',1),(3,103,'Voting Demonstration','<div><img width=\"116\" hspace=\"6\" height=\"103\" align=\"left\" src=\"/lams//www/secure/4028efbb1935b8d301193606f5000051/Image//dolly_sml.gif\" alt=\"\" /></div>\n<div><b>Here&rsquo;s a demonstration Voting activity.</b></div>\n<div>&nbsp;</div>\n<div>While this particular activity is a little bit... <i>trivial...</i> there&rsquo;s no reason why you can&rsquo;t have a very large nomination form and collate responses from as many learners are on your server.</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<h3>What&rsquo;s your favourite flavour for ice-cream?</h3>',NULL,'2008-04-11 16:39:37','1',1,0,5,0,0,'','',0,0,0,'',1);
/*!40000 ALTER TABLE `tl_lavote11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_nomination_content`
--

DROP TABLE IF EXISTS `tl_lavote11_nomination_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_nomination_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `nomination` text,
  `display_order` int(5) DEFAULT NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tl_lavote11_nomination_content_1` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `vote_session_id` bigint(20) NOT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `session_end_date` datetime DEFAULT NULL,
  `session_name` varchar(100) DEFAULT NULL,
  `session_status` varchar(100) DEFAULT NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UQ_tl_lamc11_session_1` (`vote_session_id`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tl_lavote11_session_1` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lavote11_session`
--

LOCK TABLES `tl_lavote11_session` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_session` DISABLE KEYS */;
INSERT INTO `tl_lavote11_session` VALUES (1,26,'2010-04-01 16:04:15',NULL,'LAMS Demo Course learners','INCOMPLETE',3);
/*!40000 ALTER TABLE `tl_lavote11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lavote11_uploadedfile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_uploadedfile` (
  `submissionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(255) NOT NULL,
  `isOnline_File` tinyint(1) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY (`submissionId`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tablex_111` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_usr` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `vote_session_id` bigint(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `fullname` varchar(255) DEFAULT NULL,
  `responseFinalised` tinyint(1) NOT NULL DEFAULT '0',
  `finalScreenRequested` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  KEY `vote_session_id` (`vote_session_id`),
  CONSTRAINT `FK_tl_lavote11_usr_1` FOREIGN KEY (`vote_session_id`) REFERENCES `tl_lavote11_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lavote11_usr_attempt` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `que_usr_id` bigint(20) NOT NULL,
  `vote_nomination_content_id` bigint(20) NOT NULL,
  `attempt_time` datetime DEFAULT NULL,
  `time_zone` varchar(255) DEFAULT NULL,
  `userEntry` varchar(255) DEFAULT NULL,
  `singleUserEntry` tinyint(1) NOT NULL DEFAULT '0',
  `visible` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`uid`),
  KEY `que_usr_id` (`que_usr_id`),
  KEY `vote_nomination_content_id` (`vote_nomination_content_id`),
  CONSTRAINT `FK_tl_lavote11_usr_attempt_2` FOREIGN KEY (`que_usr_id`) REFERENCES `tl_lavote11_usr` (`uid`),
  CONSTRAINT `FK_tl_lavote11_usr_attempt_3` FOREIGN KEY (`vote_nomination_content_id`) REFERENCES `tl_lavote11_nomination_content` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_attachment` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `file_version_id` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_uuid` bigint(20) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `wiki_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK9406D87760B3B03B` (`wiki_uid`),
  CONSTRAINT `FK9406D87760B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_session` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `session_end_date` datetime DEFAULT NULL,
  `session_start_date` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `session_id` bigint(20) DEFAULT NULL,
  `session_name` varchar(250) DEFAULT NULL,
  `wiki_uid` bigint(20) DEFAULT NULL,
  `wiki_main_page_uid` bigint(20) DEFAULT NULL,
  `content_folder_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKF01D63C260B3B03B` (`wiki_uid`),
  KEY `FKF01D63C2A3FF7EC0` (`wiki_main_page_uid`),
  CONSTRAINT `FKF01D63C2A3FF7EC0` FOREIGN KEY (`wiki_main_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`),
  CONSTRAINT `FKF01D63C260B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lawiki10_session`
--

LOCK TABLES `tl_lawiki10_session` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_session` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_session` VALUES (1,NULL,NULL,NULL,34,'LAMS Demo Course learners',3,7,'ff80808127b7bdae0127b7c160710011');
/*!40000 ALTER TABLE `tl_lawiki10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_user`
--

DROP TABLE IF EXISTS `tl_lawiki10_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `finishedActivity` bit(1) DEFAULT NULL,
  `wiki_session_uid` bigint(20) DEFAULT NULL,
  `entry_uid` bigint(20) DEFAULT NULL,
  `wiki_edits` int(11) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKED5D7A1FD8004954` (`wiki_session_uid`),
  CONSTRAINT `FKED5D7A1FD8004954` FOREIGN KEY (`wiki_session_uid`) REFERENCES `tl_lawiki10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_wiki` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `instructions` text,
  `run_offline` bit(1) DEFAULT NULL,
  `lock_on_finished` bit(1) DEFAULT NULL,
  `allow_learner_create_pages` bit(1) DEFAULT NULL,
  `allow_learner_insert_links` bit(1) DEFAULT NULL,
  `allow_learner_attach_images` bit(1) DEFAULT NULL,
  `notify_updates` bit(1) DEFAULT NULL,
  `reflect_on_activity` bit(1) DEFAULT NULL,
  `reflect_instructions` text,
  `minimum_edits` int(11) DEFAULT NULL,
  `maximum_edits` int(11) DEFAULT NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) DEFAULT NULL,
  `define_later` bit(1) DEFAULT NULL,
  `tool_content_id` bigint(20) DEFAULT NULL,
  `wiki_main_page_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FKED5E3E04A3FF7EC0` (`wiki_main_page_uid`),
  CONSTRAINT `FKED5E3E04A3FF7EC0` FOREIGN KEY (`wiki_main_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lawiki10_wiki`
--

LOCK TABLES `tl_lawiki10_wiki` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki` VALUES (1,NULL,NULL,NULL,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,15,1),(2,'2008-11-14 14:32:00','2008-11-10 12:53:27',5,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,55,2),(3,'2010-04-01 16:04:14','2008-11-10 12:53:27',5,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,88,4);
/*!40000 ALTER TABLE `tl_lawiki10_wiki` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_wiki_page` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `wiki_uid` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `editable` bit(1) DEFAULT NULL,
  `wiki_current_content` bigint(20) DEFAULT NULL,
  `added_by` bigint(20) DEFAULT NULL,
  `wiki_session_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `wiki_unique_key` (`wiki_uid`,`title`,`wiki_session_uid`),
  KEY `wiki_page_index_1` (`wiki_session_uid`),
  KEY `wiki_page_index_2` (`wiki_uid`),
  KEY `wiki_page_index_3` (`added_by`),
  KEY `wiki_page_index_4` (`wiki_current_content`),
  CONSTRAINT `wiki_page_fk_4` FOREIGN KEY (`wiki_current_content`) REFERENCES `tl_lawiki10_wiki_page_content` (`uid`),
  CONSTRAINT `wiki_page_fk_1` FOREIGN KEY (`wiki_session_uid`) REFERENCES `tl_lawiki10_session` (`uid`),
  CONSTRAINT `wiki_page_fk_2` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`),
  CONSTRAINT `wiki_page_fk_3` FOREIGN KEY (`added_by`) REFERENCES `tl_lawiki10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lawiki10_wiki_page`
--

LOCK TABLES `tl_lawiki10_wiki_page` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page` VALUES (1,1,'Wiki Main Page',0x01,1,NULL,NULL),(2,2,'Wiki Main Page',0x00,2,NULL,NULL),(3,2,'Important Things to Know',0x01,3,NULL,NULL),(4,3,'Wiki Main Page',0x00,4,NULL,NULL),(5,3,'Important Things to Know',0x01,5,NULL,NULL),(6,3,'Important Things to Know',0x01,6,NULL,1),(7,3,'Wiki Main Page',0x00,7,NULL,1);
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page_content`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tl_lawiki10_wiki_page_content` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT,
  `wiki_page_uid` bigint(20) DEFAULT NULL,
  `body` text,
  `editor` bigint(20) DEFAULT NULL,
  `edit_date` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  KEY `FK528051242D44CCF8` (`wiki_page_uid`),
  KEY `FK528051243233D952` (`editor`),
  CONSTRAINT `FK528051243233D952` FOREIGN KEY (`editor`) REFERENCES `tl_lawiki10_user` (`uid`),
  CONSTRAINT `FK528051242D44CCF8` FOREIGN KEY (`wiki_page_uid`) REFERENCES `tl_lawiki10_wiki_page` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tl_lawiki10_wiki_page_content`
--

LOCK TABLES `tl_lawiki10_wiki_page_content` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page_content` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page_content` VALUES (1,1,'<div>Wiki Body</div>',NULL,NULL,0),(2,2,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2010-04-01 16:03:17',3),(3,3,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2010-04-01 16:03:17',1),(4,4,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2010-04-01 16:04:14',3),(5,5,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2010-04-01 16:04:14',1),(6,6,'<div>\n<p>There are some important things you should know about Wiki\'s:</p>\n<ul>\n    <li>They are <b>not static</b>. In LAMS, authors can allow Learners to edit existing Wiki content. They can add or remove text and images, etc, but the important thing to note is that</li>\n    <li><b>Nothing on a wiki is ever \'lost\'</b>.  The LAMS Wiki tool allows monitors (and learners if permitted) to view the <em>history</em> of a page. This shows all of the changes that have been made to the page; who made them and when; and, for monitors, a link to <em>revert</em> the page to a previous state.</li>\n</ul>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div>&nbsp;</div>\n<div style=\"text-align: right;\"><b><span style=\"background-color: rgb(255, 255, 0);\"><i>You can edit this page by using the Edit link at the top right of your screen.</i></span></b></div>\n</div>',NULL,'2010-04-01 16:04:15',1),(7,7,'<div>\n<div>&nbsp;</div>\n<div style=\"text-align: center;\"><span style=\"background-color: rgb(255, 255, 0);\"><span style=\"font-size: small;\"><b>New to LAMS 2.2</b></span>!</span></div>\n<p><img border=\"0\" align=\"absmiddle\" src=\"http://wiki.lamsfoundation.org/download/attachments/6750276/wikiIcon.PNG\" alt=\"\" /> <span style=\"color: rgb(255, 153, 0);\"><span style=\"font-size: large;\">Wiki Activity</span></span></p>\n<p>The <b>Wiki Tool</b> allows for authors to create content pages that can link to each other and, optionally, allow Learners to make collaborative edits to the content provided.</p>\n<p>Wiki activities allow authors and learners to create <b>sub pages,</b> which can contain more refined content on the topic.</p>\n<p>Here are some links to other pages in this wiki activity:</p>\n<ul>\n    <li><a href=\"javascript:changeWikiPage(&quot;Important Things to Know&quot;)\">Important things to know about Wiki.</a></li>\n</ul>\n<div>&nbsp;</div>\n<div>If you want to explore this activity further, you should read the <a href=\"http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10learning\" target=\"_blank\">Wiki Learner&rsquo;s guide</a>, available online.</div>\n<p>&nbsp;</p>\n</div>',NULL,'2010-04-01 16:04:15',3);
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

-- Dump completed on 2010-04-01 16:21:35
