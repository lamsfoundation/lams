-- MySQL dump 10.11
--
-- Host: localhost    Database: lams
-- ------------------------------------------------------
-- Server version	5.0.75-0ubuntu10

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_activity_category` (
  `activity_category_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`activity_category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_activity_evaluation` (
  `activity_evaluation_id` bigint(20) NOT NULL auto_increment,
  `activity_id` bigint(20) NOT NULL,
  `tool_output_definition` varchar(255) NOT NULL,
  PRIMARY KEY  (`activity_evaluation_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_activity_evaluation_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_activity_learners` (
  `user_id` bigint(20) NOT NULL default '0',
  `activity_id` bigint(20) NOT NULL default '0',
  `allowed_to_pass` tinyint(4) NOT NULL default '0',
  KEY `user_id` (`user_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_TABLE_32_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_TABLE_32_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_auth_method_type` (
  `authentication_method_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`authentication_method_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_authentication_method` (
  `authentication_method_id` bigint(20) NOT NULL,
  `authentication_method_type_id` int(3) NOT NULL default '0',
  `authentication_method_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`authentication_method_id`),
  UNIQUE KEY `UQ_lams_authentication_method_1` (`authentication_method_name`),
  KEY `authentication_method_type_id` (`authentication_method_type_id`),
  CONSTRAINT `FK_lams_authorization_method_1` FOREIGN KEY (`authentication_method_type_id`) REFERENCES `lams_auth_method_type` (`authentication_method_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_branch_condition`
--

LOCK TABLES `lams_branch_condition` WRITE;
/*!40000 ALTER TABLE `lams_branch_condition` DISABLE KEYS */;
INSERT INTO `lams_branch_condition` VALUES (1,NULL,1,'text.search.output.definition.videoRecorder#28','text search output definition videoRecorder default condition','OUTPUT_STRING',NULL,NULL,NULL),(2,NULL,1,'text.search.output.definition.videoRecorder#28','text search output definition videoRecorder default condition','OUTPUT_STRING',NULL,NULL,NULL);
/*!40000 ALTER TABLE `lams_branch_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_competence`
--

DROP TABLE IF EXISTS `lams_competence`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_configuration` (
  `config_key` varchar(30) NOT NULL,
  `config_value` varchar(255) default NULL,
  `description_key` varchar(255) default NULL,
  `header_name` varchar(50) default NULL,
  `format` varchar(30) default NULL,
  `required` tinyint(4) NOT NULL default '0',
  PRIMARY KEY  (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_configuration`
--

LOCK TABLES `lams_configuration` WRITE;
/*!40000 ALTER TABLE `lams_configuration` DISABLE KEYS */;
INSERT INTO `lams_configuration` VALUES ('AdminScreenSize','1024x768','config.admin.screen.size','config.header.look.feel','STRING',1),('AllowDirectLessonLaunch','false','config.allow.direct.lesson.launch','config.header.features','BOOLEAN',1),('AllowLiveEdit','true','config.allow.live.edit','config.header.features','BOOLEAN',1),('AuthoringActivitiesColour','true','config.authoring.activities.colour','config.header.look.feel','BOOLEAN',1),('AuthoringClientVersion','2.3.0.200905041845','config.authoring.client.version','config.header.versions','STRING',1),('AuthoringScreenSize','1024x768','config.authoring.screen.size','config.header.look.feel','STRING',1),('CleanupPreviewOlderThanDays','7','config.cleanup.preview.older.than.days','config.header.system','LONG',1),('ContentRepositoryPath','/var/opt/lams/repository','config.content.repository.path','config.header.uploads','STRING',1),('CustomTabLink','','config.custom.tab.link','config.header.look.feel','STRING',0),('CustomTabTitle','','config.custom.tab.title','config.header.look.feel','STRING',0),('DefaultFlashTheme','default','config.default.flash.theme','config.header.look.feel','STRING',1),('DefaultHTMLTheme','defaultHTML','config.default.html.theme','config.header.look.feel','STRING',1),('DictionaryDateCreated','2008-12-05','config.dictionary.date.created','config.header.versions','STRING',1),('DumpDir','/var/opt/lams/dump','config.dump.dir','config.header.system','STRING',1),('EARDir','/usr/local/jboss-4.0.2/server/default/deploy/lams.ear/','config.ear.dir','config.header.system','STRING',1),('EnableFlash','true','config.flash.enable','config.header.features','BOOLEAN',1),('ExecutableExtensions','.bat,.bin,.com,.cmd,.exe,.msi,.msp,.ocx,.pif,.scr,.sct,.sh,.shs,.vbs','config.executable.extensions','config.header.uploads','STRING',1),('HelpURL','http://wiki.lamsfoundation.org/display/lamsdocs/','config.help.url','config.header.system','STRING',1),('LamsSupportEmail','','config.lams.support.email','config.header.email','STRING',0),('LAMS_Community_enable','false','config.community.enable','config.header.features','BOOLEAN',1),('LDAPAddr1Attr','postalAddress','admin.user.address_line_1','config.header.ldap.attributes','STRING',0),('LDAPAddr2Attr','','admin.user.address_line_2','config.header.ldap.attributes','STRING',0),('LDAPAddr3Attr','','admin.user.address_line_3','config.header.ldap.attributes','STRING',0),('LDAPAuthorMap','Teacher;SeniorStaff;Principal','config.ldap.author.map','config.header.ldap.attributes','STRING',0),('LDAPBaseDN','ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au','config.ldap.base.dn','config.header.ldap','STRING',0),('LDAPBindUserDN','','config.ldap.bind.user.dn','config.header.ldap','STRING',0),('LDAPBindUserPassword','','config.ldap.bind.user.password','config.header.ldap','STRING',0),('LDAPCityAttr','l','admin.user.city','config.header.ldap.attributes','STRING',0),('LDAPCountryAttr','','admin.user.country','config.header.ldap.attributes','STRING',0),('LDAPDayPhoneAttr','telephoneNumber','admin.user.day_phone','config.header.ldap.attributes','STRING',0),('LDAPDisabledAttr','!accountStatus','sysadmin.disabled','config.header.ldap.attributes','STRING',0),('LDAPEmailAttr','mail','admin.user.email','config.header.ldap.attributes','STRING',0),('LDAPEncryptPasswordFromBrowser','true','config.ldap.encrypt.password.from.browser','config.header.ldap','BOOLEAN',1),('LDAPEveningPhoneAttr','homePhone','admin.user.evening_phone','config.header.ldap.attributes','STRING',0),('LDAPFaxAttr','facsimileTelephoneNumber','admin.user.fax','config.header.ldap.attributes','STRING',0),('LDAPFNameAttr','givenName','admin.user.first_name','config.header.ldap.attributes','STRING',0),('LDAPGroupAdminMap','Teacher;SeniorStaff','config.ldap.group.admin.map','config.header.ldap.attributes','STRING',0),('LDAPGroupManagerMap','Principal','config.ldap.group.manager.map','config.header.ldap.attributes','STRING',0),('LDAPLearnerMap','Student;SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.learner.map','config.header.ldap.attributes','STRING',0),('LDAPLNameAttr','sn','admin.user.last_name','config.header.ldap.attributes','STRING',0),('LDAPLocaleAttr','preferredLanguage','admin.organisation.locale','config.header.ldap.attributes','STRING',0),('LDAPLoginAttr','uid','admin.user.login','config.header.ldap.attributes','STRING',0),('LDAPMobileAttr','mobile','admin.user.mobile_phone','config.header.ldap.attributes','STRING',0),('LDAPMonitorMap','SchoolSupportStaff;Teacher;SeniorStaff;Principal','config.ldap.monitor.map','config.header.ldap.attributes','STRING',0),('LDAPOnlyOneOrg','true','config.ldap.only.one.org','config.header.ldap','BOOLEAN',1),('LDAPOrgAttr','schoolCode','admin.course','config.header.ldap.attributes','STRING',0),('LDAPOrgField','code','config.ldap.org.field','config.header.ldap.attributes','STRING',0),('LDAPPostcodeAttr','postalCode','admin.user.postcode','config.header.ldap.attributes','STRING',0),('LDAPProviderURL','ldap://192.168.111.15','config.ldap.provider.url','config.header.ldap','STRING',0),('LDAPProvisioningEnabled','false','config.ldap.provisioning.enabled','config.header.ldap','BOOLEAN',1),('LDAPRolesAttr','memberOf','admin.user.roles','config.header.ldap.attributes','STRING',0),('LDAPSearchFilter','(cn={0})','config.ldap.search.filter','config.header.ldap','STRING',0),('LDAPSearchResultsPageSize','100','config.ldap.search.results.page.size','config.header.ldap','LONG',0),('LDAPSecurityAuthentication','simple','config.ldap.security.authentication','config.header.ldap','STRING',0),('LDAPSecurityProtocol','','config.ldap.security.protocol','config.header.ldap','STRING',0),('LDAPStateAttr','st','admin.user.state','config.header.ldap.attributes','STRING',0),('LDAPUpdateOnLogin','true','config.ldap.update.on.login','config.header.ldap','BOOLEAN',1),('LearnerClientVersion','2.3.0.200905041845','config.learner.client.version','config.header.versions','STRING',1),('LearnerProgressBatchSize','10','config.learner.progress.batch.size','config.header.look.feel','LONG',1),('LearnerScreenSize','1024x768','config.learner.screen.size','config.header.look.feel','STRING',1),('MonitorClientVersion','2.3.0.200905041845','config.monitor.client.version','config.header.versions','STRING',1),('MonitorScreenSize','1024x768','config.monitor.screen.size','config.header.look.feel','STRING',1),('Red5RecordingsUrl','http://localhost:8080/streams/','config.red5.recordings.url','config.header.red5','STRING',1),('Red5ServerUrl','rtmp://localhost/','config.red5.server.url','config.header.red5','STRING',1),('ServerLanguage','en_AU','config.server.language','config.header.look.feel','STRING',1),('ServerPageDirection','LTR','config.server.page.direction','config.header.look.feel','STRING',1),('ServerURL','http://localhost:8080/lams/','config.server.url','config.header.system','STRING',1),('ServerURLContextPath','lams/','config.server.url.context.path','config.header.system','STRING',1),('ServerVersionNumber','2.3.0.200905041845','config.server.version.number','config.header.versions','STRING',1),('SMTPPassword','','config.smtp.password','config.header.email','STRING',0),('SMTPServer','','config.smtp.server','config.header.email','STRING',0),('SMTPUser','','config.smtp.user','config.header.email','STRING',0),('TempDir','/var/opt/lams/temp','config.temp.dir','config.header.system','STRING',1),('TruststorePassword','','config.ldap.truststore.password','config.header.system','STRING',0),('TruststorePath','','config.ldap.truststore.path','config.header.system','STRING',0),('UploadFileMaxMemorySize','4096','config.upload.file.max.memory.size','config.header.uploads','LONG',1),('UploadFileMaxSize','1048576','config.upload.file.max.size','config.header.uploads','LONG',1),('UploadLargeFileMaxSize','10485760','config.upload.large.file.max.size','config.header.uploads','LONG',1),('UseCacheDebugListener','false','config.use.cache.debug.listener','config.header.system','BOOLEAN',1),('UserInactiveTimeout','86400','config.user.inactive.timeout','config.header.system','LONG',1),('Version','2.3','config.version','config.header.system','STRING',1),('XmppAdmin','admin','config.xmpp.admin','config.header.chat','STRING',0),('XmppConference','conference.shaun.melcoe.mq.edu.au','config.xmpp.conference','config.header.chat','STRING',0),('XmppDomain','shaun.melcoe.mq.edu.au','config.xmpp.domain','config.header.chat','STRING',0),('XmppPassword','wildfire','config.xmpp.password','config.header.chat','STRING',0);
/*!40000 ALTER TABLE `lams_configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_copy_type`
--

DROP TABLE IF EXISTS `lams_copy_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_copy_type` (
  `copy_type_id` tinyint(4) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`copy_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_cr_credential` (
  `credential_id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY  (`credential_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Records the identification properties for a tool.';
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_credential`
--

LOCK TABLES `lams_cr_credential` WRITE;
/*!40000 ALTER TABLE `lams_cr_credential` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node`
--

DROP TABLE IF EXISTS `lams_cr_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='The main table containing the node definition';
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_node`
--

LOCK TABLES `lams_cr_node` WRITE;
/*!40000 ALTER TABLE `lams_cr_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version`
--

DROP TABLE IF EXISTS `lams_cr_node_version`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_cr_node_version` (
  `nv_id` bigint(20) unsigned NOT NULL auto_increment,
  `node_id` bigint(20) unsigned NOT NULL,
  `version_id` bigint(20) unsigned NOT NULL,
  `created_date_time` datetime NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`nv_id`),
  KEY `node_id` (`node_id`),
  CONSTRAINT `FK_lams_cr_node_version_2` FOREIGN KEY (`node_id`) REFERENCES `lams_cr_node` (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Represents a version of a node';
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_node_version`
--

LOCK TABLES `lams_cr_node_version` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_node_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_node_version_property`
--

DROP TABLE IF EXISTS `lams_cr_node_version_property`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_cr_node_version_property` (
  `id` bigint(20) unsigned NOT NULL auto_increment,
  `nv_id` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY  (`id`),
  KEY `nv_id` (`nv_id`),
  CONSTRAINT `FK_lams_cr_node_version_property_1` FOREIGN KEY (`nv_id`) REFERENCES `lams_cr_node_version` (`nv_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_node_version_property`
--

LOCK TABLES `lams_cr_node_version_property` WRITE;
/*!40000 ALTER TABLE `lams_cr_node_version_property` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_node_version_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_workspace`
--

DROP TABLE IF EXISTS `lams_cr_workspace`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_cr_workspace` (
  `workspace_id` bigint(20) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY  (`workspace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Content repository workspace';
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_workspace`
--

LOCK TABLES `lams_cr_workspace` WRITE;
/*!40000 ALTER TABLE `lams_cr_workspace` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_cr_workspace_credential`
--

DROP TABLE IF EXISTS `lams_cr_workspace_credential`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_cr_workspace_credential` (
  `wc_id` bigint(20) unsigned NOT NULL auto_increment,
  `workspace_id` bigint(20) unsigned NOT NULL,
  `credential_id` bigint(20) unsigned NOT NULL,
  PRIMARY KEY  (`wc_id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `credential_id` (`credential_id`),
  CONSTRAINT `FK_lams_cr_workspace_credential_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_cr_workspace` (`workspace_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_cr_workspace_credential_2` FOREIGN KEY (`credential_id`) REFERENCES `lams_cr_credential` (`credential_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Maps tools access to workspaces';
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_cr_workspace_credential`
--

LOCK TABLES `lams_cr_workspace_credential` WRITE;
/*!40000 ALTER TABLE `lams_cr_workspace_credential` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_cr_workspace_credential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_css_property`
--

DROP TABLE IF EXISTS `lams_css_property`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_css_property` (
  `property_id` bigint(20) NOT NULL auto_increment,
  `style_id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(100) NOT NULL,
  `style_subset` varchar(20) default NULL,
  `type` tinyint(4) NOT NULL,
  PRIMARY KEY  (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_css_style` (
  `style_id` bigint(20) NOT NULL auto_increment,
  `theme_ve_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`style_id`),
  KEY `theme_ve_id` (`theme_ve_id`),
  CONSTRAINT `FK_lams_css_style_1` FOREIGN KEY (`theme_ve_id`) REFERENCES `lams_css_theme_ve` (`theme_ve_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Groups lams_css_property into a CSSStyleDeclaration.';
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_events` (
  `uid` bigint(20) NOT NULL auto_increment,
  `scope` varchar(128) NOT NULL,
  `name` varchar(128) NOT NULL,
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_gate_activity_level` (
  `gate_activity_level_id` int(11) NOT NULL default '0',
  `description` varchar(128) NOT NULL,
  PRIMARY KEY  (`gate_activity_level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_gradebook_user_activity` (
  `uid` bigint(20) NOT NULL auto_increment,
  `activity_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double default NULL,
  `feedback` text,
  `marked_in_gradebook` tinyint(1) NOT NULL default '0',
  PRIMARY KEY  (`uid`),
  KEY `activity_id` (`activity_id`,`user_id`),
  KEY `FK_lams_gradebook_user_activity_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_lams_gradebook_user_activity_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_gradebook_user_lesson` (
  `uid` bigint(20) NOT NULL auto_increment,
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `mark` double default NULL,
  `feedback` text,
  PRIMARY KEY  (`uid`),
  KEY `lesson_id` (`lesson_id`,`user_id`),
  KEY `FK_lams_gradebook_user_lesson_2` (`user_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_gradebook_user_lesson_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_group` (
  `group_id` bigint(20) NOT NULL auto_increment,
  `group_name` varchar(255) NOT NULL,
  `grouping_id` bigint(20) NOT NULL,
  `order_id` int(6) NOT NULL default '1',
  `group_ui_id` int(11) default NULL,
  PRIMARY KEY  (`group_id`),
  KEY `grouping_id` (`grouping_id`),
  CONSTRAINT `FK_lams_learning_group_1` FOREIGN KEY (`grouping_id`) REFERENCES `lams_grouping` (`grouping_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_group`
--

LOCK TABLES `lams_group` WRITE;
/*!40000 ALTER TABLE `lams_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping`
--

DROP TABLE IF EXISTS `lams_grouping`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_grouping` (
  `grouping_id` bigint(20) NOT NULL auto_increment,
  `grouping_ui_id` int(11) default NULL,
  `grouping_type_id` int(11) NOT NULL,
  `number_of_groups` int(11) default NULL,
  `learners_per_group` int(11) default NULL,
  `staff_group_id` bigint(20) default '0',
  `max_number_of_groups` int(3) default NULL,
  `equal_number_of_learners_per_group` tinyint(4) default '0',
  `view_students_before_selection` tinyint(4) default '0',
  PRIMARY KEY  (`grouping_id`),
  KEY `grouping_type_id` (`grouping_type_id`),
  CONSTRAINT `FK_lams_learning_grouping_1` FOREIGN KEY (`grouping_type_id`) REFERENCES `lams_grouping_type` (`grouping_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_grouping`
--

LOCK TABLES `lams_grouping` WRITE;
/*!40000 ALTER TABLE `lams_grouping` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_grouping_support_type`
--

DROP TABLE IF EXISTS `lams_grouping_support_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_grouping_support_type` (
  `grouping_support_type_id` int(3) NOT NULL,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`grouping_support_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_grouping_type` (
  `grouping_type_id` int(11) NOT NULL,
  `description` varchar(128) NOT NULL,
  PRIMARY KEY  (`grouping_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_input_activity` (
  `activity_id` bigint(20) NOT NULL,
  `input_activity_id` bigint(20) NOT NULL,
  UNIQUE KEY `UQ_lams_input_activity_1` (`activity_id`,`input_activity_id`),
  KEY `activity_id` (`activity_id`),
  KEY `activity_id_2` (`activity_id`),
  CONSTRAINT `FK_lams_input_activity_1` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_lams_input_activity_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_learning_activity`
--

LOCK TABLES `lams_learning_activity` WRITE;
/*!40000 ALTER TABLE `lams_learning_activity` DISABLE KEYS */;
INSERT INTO `lams_learning_activity` VALUES (1,NULL,'Forum/Message Board','Forum','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,1,'2009-05-04 18:45:45',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(2,NULL,'Displays a NoticeboardX','NoticeboardX','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,2,'2009-05-04 18:45:49',0,NULL,NULL,NULL,2,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lanb11/images/icon_htmlnb.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.noticeboard.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(3,NULL,'Allows creation and use of question and answer format','Question and Answer','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,3,'2009-05-04 18:45:53',0,NULL,NULL,NULL,3,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laqa11/images/icon_questionanswer.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.qa.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(4,NULL,'Uploading of files by learners, for review by teachers.','Submit File','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,4,'2009-05-04 18:46:01',0,NULL,NULL,NULL,4,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasbmt11/images/icon_reportsubmission.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.sbmt.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(5,NULL,'Chat Tool','Chat Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,5,'2009-05-04 18:46:07',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(6,NULL,'Share Resources','Share Resources','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,6,'2009-05-04 18:46:15',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(7,NULL,'Allows creation and use of voting format','Voting','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,7,'2009-05-04 18:46:19',0,NULL,NULL,NULL,7,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavote11/images/icon_ranking.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.vote.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(8,NULL,'Notebook Tool','Notebook Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,8,'2009-05-04 18:46:24',0,NULL,NULL,NULL,8,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lantbk11/images/icon_notebook.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.notebook.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(9,NULL,'Survey','Survey','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,9,'2009-05-04 18:46:29',0,NULL,NULL,NULL,9,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasurv11/images/icon_survey.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.survey.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(10,NULL,'Scribe Tool','Scribe Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,10,'2009-05-04 18:46:34',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(11,NULL,'TaskList','TaskList','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,11,'2009-05-04 18:46:39',0,NULL,NULL,NULL,11,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/latask10/images/icon_taskList.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.taskList.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(12,NULL,'Gmap Tool','Gmap Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,12,'2009-05-04 18:46:44',0,NULL,NULL,NULL,12,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lagmap10/images/icon_gmap.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.gmap.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(13,NULL,'Spreadsheet','Spreadsheet','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,13,'2009-05-04 18:46:50',0,NULL,NULL,NULL,13,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lasprd10/images/icon_spreadsheet.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(14,NULL,'Collecting data with custom structure.','Data Collection','Asking questions with custom, limited answers.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,14,'2009-05-04 18:46:55',0,NULL,NULL,NULL,14,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/ladaco10/images/icon_daco.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.daco.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(15,NULL,'Wiki Tool','Wiki Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,15,'2009-05-04 18:47:00',0,NULL,NULL,NULL,15,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lawiki10/images/icon_wiki.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.wiki.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(17,NULL,'Allows creation and use of multiple choice questioning format','Multiple Choice Questions','Help text',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,17,'2009-05-04 18:47:09',0,NULL,NULL,NULL,17,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamc11/images/icon_mcq.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.mc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(18,NULL,'Combined Share Resources and Forum','Discuss Shared Resources','Learners to discuss items they have viewed via Share Resources.',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,18,'2009-05-04 18:47:11',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_urlcontentmessageboard.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid18.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(19,NULL,'Share Resources','Share Resources','Share Resources',NULL,NULL,18,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:11',0,NULL,NULL,NULL,6,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/larsrc11/images/icon_rsrc.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.rsrc.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(20,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,18,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:11',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(21,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,19,'2009-05-04 18:47:12',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_groupreporting.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid19.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(22,NULL,'Chat Tool','Chat Tool','Chat',NULL,NULL,21,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:12',0,NULL,NULL,NULL,5,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lachat11/images/icon_chat.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.chat.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(23,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,21,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:12',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(24,NULL,'Combined Chat and Scribe','Prepare Group Report','Learners to prepare group report, discussing report in chat',NULL,NULL,NULL,NULL,6,2,0,NULL,NULL,NULL,0,NULL,20,'2009-05-04 18:47:12',0,NULL,NULL,NULL,NULL,NULL,5,NULL,NULL,NULL,NULL,NULL,NULL,'images/icon_forum_and_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.library.llid20.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(25,NULL,'Forum/Message Board','Forum','Forum/Message Board',NULL,NULL,24,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:12',0,NULL,NULL,NULL,1,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lafrum11/images/icon_forum.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.forum.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(26,NULL,'Scribe Tool','Scribe Tool','Scribe',NULL,NULL,24,NULL,1,2,0,NULL,NULL,NULL,0,NULL,NULL,'2009-05-04 18:47:12',0,NULL,NULL,NULL,10,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lascrb11/images/icon_scribe.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.scribe.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(32,NULL,'Assessment','Assessment','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,26,'2009-05-04 18:58:12',0,NULL,NULL,NULL,23,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laasse10/images/icon_assessment.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.assessment.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(33,NULL,'ImageGallery','ImageGallery','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,27,'2009-05-04 18:58:28',0,NULL,NULL,NULL,24,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/laimag10/images/icon_imageGallery.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.imageGallery.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(34,NULL,'Mindmap Tool','Mindmap Tool','Mindmap Tool for creating mindmaps.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,28,'2009-05-04 18:58:41',0,NULL,NULL,NULL,25,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lamind10/images/icon_mindmap.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.mindmap.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(35,NULL,'Pixlr Tool','Pixlr Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,29,'2009-05-04 18:58:54',0,NULL,NULL,NULL,26,NULL,4,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lapixl10/images/icon_pixlr.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.pixlr.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0),(37,NULL,'VideoRecorder Tool','VideoRecorder Tool','Put some help text here.',NULL,NULL,NULL,NULL,1,2,0,NULL,NULL,NULL,0,NULL,31,'2009-05-04 19:00:31',0,NULL,NULL,NULL,28,NULL,6,NULL,NULL,NULL,NULL,NULL,NULL,'tool/lavidr10/images/icon_videoRecorder.swf',NULL,NULL,NULL,'org.lamsfoundation.lams.tool.videoRecorder.ApplicationResources',NULL,0,0,NULL,NULL,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `lams_learning_activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_activity_type`
--

DROP TABLE IF EXISTS `lams_learning_activity_type`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_learning_activity_type` (
  `learning_activity_type_id` int(11) NOT NULL default '0',
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`learning_activity_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_learning_design` (
  `learning_design_id` bigint(20) NOT NULL auto_increment,
  `learning_design_ui_id` int(11) default NULL,
  `description` text,
  `title` varchar(255) default NULL,
  `first_activity_id` bigint(20) default NULL,
  `floating_activity_id` bigint(20) default NULL,
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
  KEY `idx_design_floating_act` (`floating_activity_id`),
  CONSTRAINT `FK_lams_learning_design_3` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_learning_design_4` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_learning_design_5` FOREIGN KEY (`license_id`) REFERENCES `lams_license` (`license_id`),
  CONSTRAINT `FK_lams_learning_design_6` FOREIGN KEY (`copy_type_id`) REFERENCES `lams_copy_type` (`copy_type_id`),
  CONSTRAINT `FK_lams_learning_design_7` FOREIGN KEY (`edit_override_user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_learning_design`
--

LOCK TABLES `lams_learning_design` WRITE;
/*!40000 ALTER TABLE `lams_learning_design` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_learning_design` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_library`
--

DROP TABLE IF EXISTS `lams_learning_library`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_learning_library` (
  `learning_library_id` bigint(20) NOT NULL auto_increment,
  `description` text,
  `title` varchar(255) default NULL,
  `valid_flag` tinyint(1) NOT NULL default '1',
  `create_date_time` datetime NOT NULL,
  PRIMARY KEY  (`learning_library_id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_learning_library`
--

LOCK TABLES `lams_learning_library` WRITE;
/*!40000 ALTER TABLE `lams_learning_library` DISABLE KEYS */;
INSERT INTO `lams_learning_library` VALUES (1,'Forum, also known Message Board','Forum',1,'2009-05-04 18:45:45'),(2,'Displays a Noticeboard','Noticeboard',1,'2009-05-04 18:45:48'),(3,'Question and Answer Learning Library Description','Question and Answer',1,'2009-05-04 18:45:53'),(4,'Uploading of files by learners, for review by teachers.','Submit file',1,'2009-05-04 18:46:01'),(5,'Chat Tool','Chat',1,'2009-05-04 18:46:07'),(6,'Share resources','Share resources',1,'2009-05-04 18:46:15'),(7,'Voting Learning Library Description','Voting',1,'2009-05-04 18:46:19'),(8,'Notebook Tool','Notebook',1,'2009-05-04 18:46:24'),(9,'Survey','Survey',1,'2009-05-04 18:46:29'),(10,'Scribe Tool','Scribe',0,'2009-05-04 18:46:34'),(11,'Share taskList','Share taskList',1,'2009-05-04 18:46:39'),(12,'Gmap Tool','Gmap',1,'2009-05-04 18:46:44'),(13,'Spreadsheet Tool','Spreadsheet',1,'2009-05-04 18:46:50'),(14,'Collecting data with custom structure.','Data Collection',1,'2009-05-04 18:46:55'),(15,'Wiki Tool','Wiki',1,'2009-05-04 18:47:00'),(17,'MCQ Learning Library Description','MCQ',1,'2009-05-04 18:47:09'),(18,'Shared Resources and Forum','Resources and Forum',1,'2009-05-04 18:47:11'),(19,'Chat and Scribe','Chat and Scribe',1,'2009-05-04 18:47:12'),(20,'Forum and Scribe','Forum and Scribe',1,'2009-05-04 18:47:12'),(26,'Assessment','Assessment',1,'2009-05-04 18:58:12'),(27,'Share imageGallery','Share imageGallery',1,'2009-05-04 18:58:28'),(28,'Mindmap Tool','Mindmap',1,'2009-05-04 18:58:41'),(29,'Pixlr Tool','Pixlr',1,'2009-05-04 18:58:54'),(31,'VideoRecorder Tool','VideoRecorder',1,'2009-05-04 19:00:31');
/*!40000 ALTER TABLE `lams_learning_library` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_learning_transition`
--

DROP TABLE IF EXISTS `lams_learning_transition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_learning_transition`
--

LOCK TABLES `lams_learning_transition` WRITE;
/*!40000 ALTER TABLE `lams_learning_transition` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_learning_transition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson`
--

DROP TABLE IF EXISTS `lams_lesson`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `marks_released` tinyint(4) default '0',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_lesson`
--

LOCK TABLES `lams_lesson` WRITE;
/*!40000 ALTER TABLE `lams_lesson` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_lesson` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_lesson_learner`
--

DROP TABLE IF EXISTS `lams_lesson_learner`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_lesson_learner` (
  `lesson_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  KEY `lesson_id` (`lesson_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `FK_lams_lesson_learner_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`),
  CONSTRAINT `FK_lams_lesson_learner_2` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_lesson_state` (
  `lesson_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`lesson_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_license` (
  `license_id` bigint(20) NOT NULL,
  `name` varchar(200) NOT NULL,
  `code` varchar(20) NOT NULL,
  `url` varchar(256) default NULL,
  `default_flag` tinyint(1) NOT NULL default '0',
  `picture_url` varchar(256) default NULL,
  PRIMARY KEY  (`license_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_log_event_type` (
  `log_event_type_id` int(5) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`log_event_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `enable_monitor_gradebook` tinyint(1) NOT NULL default '0',
  `enable_learner_gradebook` tinyint(1) NOT NULL default '0',
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
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_organisation`
--

LOCK TABLES `lams_organisation` WRITE;
/*!40000 ALTER TABLE `lams_organisation` DISABLE KEYS */;
INSERT INTO `lams_organisation` VALUES (1,'Root',NULL,'Root Organisation',NULL,1,'2009-05-04 18:45:26',1,1,1,0,0,0,0,0,0,1,NULL,NULL),(2,'Playpen','PP101','Developers Playpen',1,2,'2009-05-04 18:45:26',1,2,1,0,0,0,0,0,0,1,NULL,NULL),(3,'Everybody',NULL,'All People In Course',2,3,'2009-05-04 18:45:26',1,NULL,1,0,0,0,0,0,0,1,NULL,NULL),(4,'Mathematics 1','MATH111','Mathematics 1',1,2,'2009-05-04 18:45:26',1,3,1,0,0,0,0,0,0,2,NULL,NULL),(5,'Tutorial Group A','TUTA','Tutorial Group A',4,3,'2009-05-04 18:45:26',1,NULL,1,0,0,0,0,0,0,2,NULL,NULL),(6,'Tutorial Group B','TUTB','Tutorial Group B',4,3,'2009-05-04 18:45:26',1,NULL,1,0,0,0,0,0,0,2,NULL,NULL),(7,'Moodle','Moodle','Moodle Test',1,2,'2009-05-04 18:45:26',1,50,2,0,0,0,0,0,0,1,NULL,NULL);
/*!40000 ALTER TABLE `lams_organisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_organisation_state`
--

DROP TABLE IF EXISTS `lams_organisation_state`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_organisation_state` (
  `organisation_state_id` int(3) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`organisation_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_organisation_type` (
  `organisation_type_id` int(3) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`organisation_type_id`),
  UNIQUE KEY `UQ_lams_organisation_type_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_password_request` (
  `request_id` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) NOT NULL,
  `request_key` varchar(32) NOT NULL,
  `request_date` datetime NOT NULL,
  PRIMARY KEY  (`request_id`),
  UNIQUE KEY `IX_lams_psswd_rqst_key` (`request_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_password_request`
--

LOCK TABLES `lams_password_request` WRITE;
/*!40000 ALTER TABLE `lams_password_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_password_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_planner_nodes`
--

DROP TABLE IF EXISTS `lams_planner_nodes`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_planner_nodes` (
  `uid` bigint(20) NOT NULL auto_increment,
  `parent_uid` bigint(20) default NULL,
  `order_id` tinyint(3) unsigned NOT NULL,
  `locked` tinyint(1) NOT NULL default '0',
  `content_folder_id` varchar(32) default NULL,
  `title` varchar(255) NOT NULL,
  `brief_desc` text,
  `full_desc` text,
  `file_uuid` bigint(20) unsigned default NULL,
  `file_name` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `parent_uid` (`parent_uid`,`order_id`),
  CONSTRAINT `FK_lams_planner_node_parent` FOREIGN KEY (`parent_uid`) REFERENCES `lams_planner_nodes` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_planner_nodes`
--

LOCK TABLES `lams_planner_nodes` WRITE;
/*!40000 ALTER TABLE `lams_planner_nodes` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_planner_nodes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_presence_chat_msgs`
--

DROP TABLE IF EXISTS `lams_presence_chat_msgs`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_presence_chat_msgs` (
  `uid` bigint(20) NOT NULL auto_increment,
  `room_name` varchar(255) default NULL,
  `from_user` varchar(255) default NULL,
  `to_user` varchar(255) default NULL,
  `date_sent` datetime default NULL,
  `message` varchar(1023) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_privilege` (
  `privilege_id` bigint(20) NOT NULL auto_increment,
  `code` varchar(10) NOT NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`privilege_id`),
  UNIQUE KEY `IX_lams_privilege_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_progress_attempted` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `start_date_time` datetime default NULL,
  PRIMARY KEY  (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_current_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_current_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_progress_completed` (
  `learner_progress_id` bigint(20) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `completed_date_time` datetime default NULL,
  `start_date_time` datetime default NULL,
  PRIMARY KEY  (`learner_progress_id`,`activity_id`),
  KEY `learner_progress_id` (`learner_progress_id`),
  KEY `activity_id` (`activity_id`),
  CONSTRAINT `FK_lams_progress_completed_1` FOREIGN KEY (`learner_progress_id`) REFERENCES `lams_learner_progress` (`learner_progress_id`),
  CONSTRAINT `FK_lams_progress_completed_2` FOREIGN KEY (`activity_id`) REFERENCES `lams_learning_activity` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_BLOB_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_BLOB_TRIGGERS_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_TRIGGERS` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_CALENDARS` (
  `CALENDAR_NAME` varchar(80) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY  (`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_CRON_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `CRON_EXPRESSION` varchar(80) NOT NULL,
  `TIME_ZONE_ID` varchar(80) default NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_CRON_TRIGGERS_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_TRIGGERS` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_FIRED_TRIGGERS` (
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_JOB_DETAILS` (
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_JOB_LISTENERS` (
  `JOB_NAME` varchar(80) NOT NULL,
  `JOB_GROUP` varchar(80) NOT NULL,
  `JOB_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY  (`JOB_NAME`,`JOB_GROUP`,`JOB_LISTENER`),
  CONSTRAINT `lams_qtz_JOB_LISTENERS_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_JOB_DETAILS` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_LOCKS` (
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY  (`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_PAUSED_TRIGGER_GRPS` (
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  PRIMARY KEY  (`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_SCHEDULER_STATE` (
  `INSTANCE_NAME` varchar(80) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  `RECOVERER` varchar(80) default NULL,
  PRIMARY KEY  (`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_SIMPLE_TRIGGERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(7) NOT NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `lams_qtz_SIMPLE_TRIGGERS_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_TRIGGERS` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_TRIGGERS` (
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
  CONSTRAINT `lams_qtz_TRIGGERS_ibfk_1` FOREIGN KEY (`JOB_NAME`, `JOB_GROUP`) REFERENCES `lams_qtz_JOB_DETAILS` (`JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_qtz_TRIGGERS`
--

LOCK TABLES `lams_qtz_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `lams_qtz_TRIGGERS` DISABLE KEYS */;
INSERT INTO `lams_qtz_TRIGGERS` VALUES ('Resend Messages Job trigger','DEFAULT','Resend Messages Job','DEFAULT','0',NULL,1241431325957,1241427725957,'WAITING','SIMPLE',1241427725957,0,NULL,0,NULL);
/*!40000 ALTER TABLE `lams_qtz_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_qtz_TRIGGER_LISTENERS`
--

DROP TABLE IF EXISTS `lams_qtz_TRIGGER_LISTENERS`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_qtz_TRIGGER_LISTENERS` (
  `TRIGGER_NAME` varchar(80) NOT NULL,
  `TRIGGER_GROUP` varchar(80) NOT NULL,
  `TRIGGER_LISTENER` varchar(80) NOT NULL,
  PRIMARY KEY  (`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_LISTENER`),
  CONSTRAINT `lams_qtz_TRIGGER_LISTENERS_ibfk_1` FOREIGN KEY (`TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `lams_qtz_TRIGGERS` (`TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_registration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `site_name` varchar(255) NOT NULL,
  `organisation` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `server_country` varchar(2) NOT NULL,
  `public_directory` tinyint(1) default '1',
  `enable_lams_community` tinyint(1) default '0',
  `server_key` varchar(255) NOT NULL,
  `server_id` varchar(255) NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_role` (
  `role_id` int(6) NOT NULL default '0',
  `name` varchar(64) NOT NULL,
  `description` text,
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`role_id`),
  KEY `gname` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_role`
--

LOCK TABLES `lams_role` WRITE;
/*!40000 ALTER TABLE `lams_role` DISABLE KEYS */;
INSERT INTO `lams_role` VALUES (1,'SYSADMIN','LAMS System Adminstrator','2009-05-04 18:45:26'),(2,'GROUP MANAGER','Group Manager','2009-05-04 18:45:26'),(3,'AUTHOR','Authors Learning Designs','2009-05-04 18:45:26'),(4,'MONITOR','Member of Staff','2009-05-04 18:45:26'),(5,'LEARNER','Student','2009-05-04 18:45:26'),(6,'GROUP ADMIN','Group Administrator','2009-05-04 18:45:26'),(7,'AUTHOR ADMIN','Author Administrator','2009-05-04 18:45:26');
/*!40000 ALTER TABLE `lams_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_role_privilege`
--

DROP TABLE IF EXISTS `lams_role_privilege`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_supported_locale` (
  `locale_id` int(11) NOT NULL auto_increment,
  `language_iso_code` varchar(2) NOT NULL COMMENT 'ISO 639-1 Language Code (2 letter version) Java only supports 2 letter properly, not the 3 letter codes.',
  `country_iso_code` varchar(2) default NULL COMMENT 'ISO 3166 Country Code. Cannot use in unique key as allows null.',
  `description` varchar(255) NOT NULL,
  `direction` varchar(3) NOT NULL,
  `fckeditor_code` varchar(10) default NULL,
  PRIMARY KEY  (`locale_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='Describes the valid language/country combinations.';
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `pedagogical_planner_url` text,
  PRIMARY KEY  (`system_tool_id`),
  UNIQUE KEY `UQ_systool_activity_type` (`learning_activity_type_id`),
  KEY `learning_activity_type_id` (`learning_activity_type_id`),
  CONSTRAINT `FK_lams_system_tool` FOREIGN KEY (`learning_activity_type_id`) REFERENCES `lams_learning_activity_type` (`learning_activity_type_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_system_tool`
--

LOCK TABLES `lams_system_tool` WRITE;
/*!40000 ALTER TABLE `lams_system_tool` DISABLE KEYS */;
INSERT INTO `lams_system_tool` VALUES (1,2,'Grouping','All types of grouping including random and chosen.','learning/grouping.do?method=performGrouping','learning/grouping.do?method=performGrouping','learning/grouping.do?method=viewGrouping&mode=teacher','learning/groupingExportPortfolio?mode=learner','learning/groupingExportPortfolio?mode=teacher','monitoring/grouping.do?method=startGrouping','monitoring/grouping.do?method=startGrouping',NULL,'2009-05-04 18:45:26',NULL,'pedagogicalPlanner/grouping.do?method=initGrouping'),(2,3,'Sync Gate','Gate: Synchronise Learners.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2009-05-04 18:45:26',NULL,NULL),(3,4,'Schedule Gate','Gate: Opens/shuts at particular times.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2009-05-04 18:45:26',NULL,NULL),(4,5,'Permission Gate','Gate: Opens under teacher or staff control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2009-05-04 18:45:26',NULL,NULL),(5,9,'System Gate','Gate: Opens under system control.','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2009-05-04 18:45:26',NULL,NULL),(6,10,'Monitor Chosen Branching','Select between multiple sequence activities, with the branch chosen in monitoring.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/chosenBranching.do?method=assignBranch','monitoring/chosenBranching.do?method=assignBranch',NULL,'2009-05-04 18:45:26',NULL,NULL),(7,11,'Group Based Branching','Select between multiple sequence activities, with the branch chosen by an existing group.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/groupedBranching.do?method=viewBranching','monitoring/groupedBranching.do?method=assignBranch',NULL,'2009-05-04 18:45:26',NULL,NULL),(8,12,'Tool Output Based Branching','Select between multiple sequence activities, with the branch chosen on results of another activity.','learning/branching.do?method=performBranching','learning/branching.do?method=performBranching','monitoring/complexProgress.do',NULL,'monitoring/branchingExportPortfolio?mode=teacher','monitoring/toolBranching.do?method=viewBranching','monitoring/toolBranching.do?method=viewBranching',NULL,'2009-05-04 18:45:26',NULL,NULL),(9,8,'Sequence','A sequence of activities','learning/SequenceActivity.do','learning/SequenceActivity.do','monitoring/complexProgress.do',NULL,'monitoring/sequenceExportPortfolio?mode=teacher','monitoring/sequence.do?method=viewSequence','monitoring/sequence.do?method=viewSequence',NULL,'2009-05-04 18:45:26',NULL,NULL),(10,14,'Condition Gate','Gate: Opens if conditions are met','learning/gate.do?method=knockGate','learning/gate.do?method=knockGate',NULL,NULL,'monitoring/gateExportPortfolio?mode=teacher','monitoring/gate.do?method=viewGate','monitoring/gate.do?method=viewGate',NULL,'2009-05-04 18:45:26',NULL,NULL),(11,15,'Floating Activities','A collection of floating activities',NULL,NULL,NULL,'monitoring/floatingExportPortfolio?mode=learner','monitoring/floatingExportPortfolio?mode=teacher','monitoring/floating.do?method=viewFloating','monitoring/floating.do?method=viewFloating',NULL,'2009-05-04 18:45:26',NULL,NULL);
/*!40000 ALTER TABLE `lams_system_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_text_search_condition`
--

DROP TABLE IF EXISTS `lams_text_search_condition`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_text_search_condition` (
  `condition_id` bigint(20) NOT NULL,
  `text_search_all_words` text,
  `text_search_phrase` text,
  `text_search_any_words` text,
  `text_search_excluded_words` text,
  PRIMARY KEY  (`condition_id`),
  CONSTRAINT `TextSearchConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_text_search_condition`
--

LOCK TABLES `lams_text_search_condition` WRITE;
/*!40000 ALTER TABLE `lams_text_search_condition` DISABLE KEYS */;
INSERT INTO `lams_text_search_condition` VALUES (1,'LAMS',NULL,NULL,NULL),(2,'LAMS',NULL,NULL,NULL);
/*!40000 ALTER TABLE `lams_text_search_condition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool`
--

DROP TABLE IF EXISTS `lams_tool`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `pedagogical_planner_url` text,
  `help_url` text,
  `create_date_time` datetime NOT NULL,
  `language_file` varchar(255) default NULL,
  `modified_date_time` datetime default NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_tool`
--

LOCK TABLES `lams_tool` WRITE;
/*!40000 ALTER TABLE `lams_tool` DISABLE KEYS */;
INSERT INTO `lams_tool` VALUES (1,'lafrum11','forumService','Forum','Forum / Message Boards','forum','20081118',1,1,1,2,1,'tool/lafrum11/learning/viewForum.do?mode=learner','tool/lafrum11/learning/viewForum.do?mode=author','tool/lafrum11/learning/viewForum.do?mode=teacher','tool/lafrum11/authoring.do','tool/lafrum11/defineLater.do','tool/lafrum11/exportPortfolio?mode=learner','tool/lafrum11/exportPortfolio?mode=teacher','tool/lafrum11/monitoring.do','tool/lafrum11/contribute.do','tool/lafrum11/moderate.do','tool/lafrum11/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/lafrum11','2009-05-04 18:45:45','org.lamsfoundation.lams.tool.forum.ApplicationResources','2009-05-04 18:45:45',NULL,1,NULL),(2,'lanb11','nbService','NoticeboardX','Displays a NoticeboardX','nb','20081209',2,2,1,2,1,'tool/lanb11/starter/learner.do?mode=learner','tool/lanb11/starter/learner.do?mode=author','tool/lanb11/starter/learner.do?mode=teacher','tool/lanb11/authoring.do','tool/lanb11/authoring.do?defineLater=true','tool/lanb11/portfolioExport?mode=learner','tool/lanb11/portfolioExport?mode=teacher','tool/lanb11/monitoring.do',NULL,NULL,'tool/lanb11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lanb11','2009-05-04 18:45:48','org.lamsfoundation.lams.tool.noticeboard.ApplicationResources','2009-05-04 18:45:48',NULL,0,NULL),(3,'laqa11','qaService','Question and Answer','Q/A Tool','qa','20081126',3,3,1,2,1,'tool/laqa11/learningStarter.do?mode=learner','tool/laqa11/learningStarter.do?mode=author','tool/laqa11/learningStarter.do?mode=teacher','tool/laqa11/authoringStarter.do','tool/laqa11/defineLaterStarter.do','tool/laqa11/exportPortfolio?mode=learner','tool/laqa11/exportPortfolio?mode=teacher','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','tool/laqa11/monitoringStarter.do','tool/laqa11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/laqa11','2009-05-04 18:45:53','org.lamsfoundation.lams.tool.qa.ApplicationResources','2009-05-04 18:45:53','tool/laqa11/laqa11admin.do',1,NULL),(4,'lasbmt11','submitFilesService','Submit File','Submit File Tool Description','submitfile','20090113',4,4,1,2,1,'tool/lasbmt11/learner.do?mode=learner','tool/lasbmt11/learner.do?mode=author','tool/lasbmt11/learner.do?mode=teacher','tool/lasbmt11/authoring.do','tool/lasbmt11/definelater.do?mode=teacher','tool/lasbmt11/exportPortfolio?mode=learner','tool/lasbmt11/exportPortfolio?mode=teacher','tool/lasbmt11/monitoring.do','tool/lasbmt11/contribute.do','tool/lasbmt11/moderation.do','tool/lasbmt11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lasbmt11','2009-05-04 18:46:01','org.lamsfoundation.lams.tool.sbmt.ApplicationResources','2009-05-04 18:46:01',NULL,0,NULL),(5,'lachat11','chatService','Chat','Chat','chat','20081125',5,5,1,2,1,'tool/lachat11/learning.do?mode=learner','tool/lachat11/learning.do?mode=author','tool/lachat11/learning.do?mode=teacher','tool/lachat11/authoring.do','tool/lachat11/authoring.do?mode=teacher','tool/lachat11/exportPortfolio?mode=learner','tool/lachat11/exportPortfolio?mode=teacher','tool/lachat11/monitoring.do','tool/lachat11/contribute.do','tool/lachat11/moderate.do','tool/lachat11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lachat11','2009-05-04 18:46:07','org.lamsfoundation.lams.tool.chat.ApplicationResources','2009-05-04 18:46:07',NULL,1,NULL),(6,'larsrc11','resourceService','Shared Resources','Shared Resources','sharedresources','20090115',6,6,1,2,1,'tool/larsrc11/learning/start.do?mode=learner','tool/larsrc11/learning/start.do?mode=author','tool/larsrc11/learning/start.do?mode=teacher','tool/larsrc11/authoring/start.do','tool/larsrc11/definelater.do','tool/larsrc11/exportPortfolio?mode=learner','tool/larsrc11/exportPortfolio?mode=teacher','tool/larsrc11/monitoring/summary.do','tool/larsrc11/contribute.do','tool/larsrc11/moderate.do','tool/larsrc11/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/larsrc11','2009-05-04 18:46:15','org.lamsfoundation.lams.tool.rsrc.ApplicationResources','2009-05-04 18:46:15',NULL,0,NULL),(7,'lavote11','voteService','Voting','Voting','vote','20081203',7,7,1,2,1,'tool/lavote11/learningStarter.do?mode=learner','tool/lavote11/learningStarter.do?mode=author','tool/lavote11/learningStarter.do?mode=teacher','tool/lavote11/authoringStarter.do','tool/lavote11/defineLaterStarter.do','tool/lavote11/exportPortfolio?mode=learner','tool/lavote11/exportPortfolio?mode=teacher','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','tool/lavote11/monitoringStarter.do','tool/lavote11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lavote11','2009-05-04 18:46:19','org.lamsfoundation.lams.tool.vote.ApplicationResources','2009-05-04 18:46:19',NULL,1,NULL),(8,'lantbk11','notebookService','Notebook','Notebook','notebook','20081118',8,8,1,2,1,'tool/lantbk11/learning.do?mode=learner','tool/lantbk11/learning.do?mode=author','tool/lantbk11/learning.do?mode=teacher','tool/lantbk11/authoring.do','tool/lantbk11/authoring.do?mode=teacher','tool/lantbk11/exportPortfolio?mode=learner','tool/lantbk11/exportPortfolio?mode=teacher','tool/lantbk11/monitoring.do','tool/lantbk11/contribute.do','tool/lantbk11/moderate.do','tool/lantbk11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lantbk11','2009-05-04 18:46:24','org.lamsfoundation.lams.tool.notebook.ApplicationResources','2009-05-04 18:46:24',NULL,1,NULL),(9,'lasurv11','lasurvSurveyService','Survey','Survey','survey','20081021',9,9,1,2,1,'tool/lasurv11/learning/start.do?mode=learner','tool/lasurv11/learning/start.do?mode=author','tool/lasurv11/learning/start.do?mode=teacher','tool/lasurv11/authoring/start.do','tool/lasurv11/definelater.do','tool/lasurv11/exportPortfolio?mode=learner','tool/lasurv11/exportPortfolio?mode=teacher','tool/lasurv11/monitoring/summary.do','tool/lasurv11/contribute.do','tool/lasurv11/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lasurv11','2009-05-04 18:46:29','org.lamsfoundation.lams.tool.survey.ApplicationResources','2009-05-04 18:46:29',NULL,1,NULL),(10,'lascrb11','lascrbScribeService','Scribe','Scribe','scribe','20090226',10,10,1,2,1,'tool/lascrb11/learning.do?mode=learner','tool/lascrb11/learning.do?mode=author','tool/lascrb11/learning.do?mode=teacher','tool/lascrb11/authoring.do','tool/lascrb11/authoring.do?mode=teacher','tool/lascrb11/exportPortfolio?mode=learner','tool/lascrb11/exportPortfolio?mode=teacher','tool/lascrb11/monitoring.do','tool/lascrb11/contribute.do','tool/lascrb11/moderate.do','tool/lascrb11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lascrb11','2009-05-04 18:46:34','org.lamsfoundation.lams.tool.scribe.ApplicationResources','2009-05-04 18:46:34',NULL,0,NULL),(11,'latask10','lataskTaskListService','Shared TaskList','Shared TaskList','sharedtaskList','20090113',11,11,1,2,1,'tool/latask10/learning/start.do?mode=learner','tool/latask10/learning/start.do?mode=author','tool/latask10/learning/start.do?mode=teacher','tool/latask10/authoring/start.do','tool/latask10/definelater.do','tool/latask10/exportPortfolio?mode=learner','tool/latask10/exportPortfolio?mode=teacher','tool/latask10/monitoring/summary.do','tool/latask10/contribute.do','tool/latask10/moderate.do','tool/latask10/authoring/initPedagogicalPlannerForm.do','http://wiki.lamsfoundation.org/display/lamsdocs/latask10','2009-05-04 18:46:39','org.lamsfoundation.lams.tool.taskList.ApplicationResources','2009-05-04 18:46:39',NULL,1,NULL),(12,'lagmap10','gmapService','Gmap','Gmap','gmap','20080521',12,12,1,2,1,'tool/lagmap10/learning.do?mode=learner','tool/lagmap10/learning.do?mode=author','tool/lagmap10/learning.do?mode=teacher','tool/lagmap10/authoring.do','tool/lagmap10/authoring.do?mode=teacher','tool/lagmap10/exportPortfolio?mode=learner','tool/lagmap10/exportPortfolio?mode=teacher','tool/lagmap10/monitoring.do','tool/lagmap10/contribute.do','tool/lagmap10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lagmap10','2009-05-04 18:46:44','org.lamsfoundation.lams.tool.gmap.ApplicationResources','2009-05-04 18:46:44','tool/lagmap10/lagmap10admin.do',0,NULL),(13,'lasprd10','spreadsheetService','Spreadsheet Tool','Spreadsheet Tool','spreadsheet','20080612',13,13,1,2,1,'tool/lasprd10/learning/start.do?mode=learner','tool/lasprd10/learning/start.do?mode=author','tool/lasprd10/learning/start.do?mode=teacher','tool/lasprd10/authoring/start.do','tool/lasprd10/definelater.do','tool/lasprd10/exportPortfolio?mode=learner','tool/lasprd10/exportPortfolio?mode=teacher','tool/lasprd10/monitoring/summary.do','tool/lasprd10/contribute.do','tool/lasprd10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lasprd10','2009-05-04 18:46:50','org.lamsfoundation.lams.tool.spreadsheet.ApplicationResources','2009-05-04 18:46:50',NULL,0,NULL),(14,'ladaco10','dacoService','Data Collection','Collecting data with custom structure.','daco','20090326',14,14,1,2,1,'tool/ladaco10/learning/start.do?mode=learner','tool/ladaco10/learning/start.do?mode=author','tool/ladaco10/learning/start.do?mode=teacher','tool/ladaco10/authoring/start.do','tool/ladaco10/definelater.do','tool/ladaco10/exportPortfolio?mode=learner','tool/ladaco10/exportPortfolio?mode=teacher','tool/ladaco10/monitoring/summary.do','tool/ladaco10/contribute.do','tool/ladaco10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/ladaco10','2009-05-04 18:46:55','org.lamsfoundation.lams.tool.daco.ApplicationResources','2009-05-04 18:46:55',NULL,1,NULL),(15,'lawiki10','wikiService','Wiki','Wiki','wiki','20090121',15,15,1,2,1,'tool/lawiki10/learning.do?mode=learner','tool/lawiki10/learning.do?mode=author','tool/lawiki10/learning.do?mode=teacher','tool/lawiki10/authoring.do','tool/lawiki10/authoring.do?mode=teacher','tool/lawiki10/exportPortfolio?mode=learner','tool/lawiki10/exportPortfolio?mode=teacher','tool/lawiki10/monitoring.do','tool/lawiki10/contribute.do','tool/lawiki10/moderate.do','tool/lawiki10/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lawiki10','2009-05-04 18:47:00','org.lamsfoundation.lams.tool.wiki.ApplicationResources','2009-05-04 18:47:00',NULL,1,NULL),(17,'lamc11','mcService','MCQ','Multiple Choice Questions','mc','20081127',17,17,1,2,1,'tool/lamc11/learningStarter.do?mode=learner','tool/lamc11/learningStarter.do?mode=author','tool/lamc11/learningStarter.do?mode=teacher','tool/lamc11/authoringStarter.do','tool/lamc11/defineLaterStarter.do','tool/lamc11/exportPortfolio?mode=learner','tool/lamc11/exportPortfolio?mode=teacher','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','tool/lamc11/monitoringStarter.do','tool/lamc11/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lamc11','2009-05-04 18:47:09','org.lamsfoundation.lams.tool.mc.ApplicationResources','2009-05-04 18:47:09',NULL,1,NULL),(23,'laasse10','laasseAssessmentService','Assessment','Assessment','assessment','20090109',26,23,1,2,1,'tool/laasse10/learning/start.do?mode=learner','tool/laasse10/learning/start.do?mode=author','tool/laasse10/learning/start.do?mode=teacher','tool/laasse10/authoring/start.do','tool/laasse10/definelater.do','tool/laasse10/exportPortfolio?mode=learner','tool/laasse10/exportPortfolio?mode=teacher','tool/laasse10/monitoring/summary.do','tool/laasse10/contribute.do','tool/laasse10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/laasse10','2009-05-04 18:58:12','org.lamsfoundation.lams.tool.assessment.ApplicationResources','2009-05-04 18:58:12',NULL,1,NULL),(24,'laimag10','laimagImageGalleryService','ImageGallery','ImageGallery','imageGallery','20081007',27,24,1,2,1,'tool/laimag10/learning/start.do?mode=learner','tool/laimag10/learning/start.do?mode=author','tool/laimag10/learning/start.do?mode=teacher','tool/laimag10/authoring/start.do','tool/laimag10/definelater.do','tool/laimag10/exportPortfolio?mode=learner','tool/laimag10/exportPortfolio?mode=teacher','tool/laimag10/monitoring/summary.do','tool/laimag10/contribute.do','tool/laimag10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/laimag10','2009-05-04 18:58:28','org.lamsfoundation.lams.tool.imageGallery.ApplicationResources','2009-05-04 18:58:28','tool/laimag10/laimag10admin/start.do',1,NULL),(25,'lamind10','mindmapService','Mindmap','Mindmap','mindmap','20090202',28,25,1,2,1,'tool/lamind10/learning.do?mode=learner','tool/lamind10/learning.do?mode=author','tool/lamind10/learning.do?mode=teacher','tool/lamind10/authoring.do','tool/lamind10/authoring.do?mode=teacher','tool/lamind10/exportPortfolio?mode=learner','tool/lamind10/exportPortfolio?mode=teacher','tool/lamind10/monitoring.do','tool/lamind10/contribute.do','tool/lamind10/moderate.do','tool/lamind10/pedagogicalPlanner.do','http://wiki.lamsfoundation.org/display/lamsdocs/lamind10','2009-05-04 18:58:41','org.lamsfoundation.lams.tool.mindmap.ApplicationResources','2009-05-04 18:58:41',NULL,1,NULL),(26,'lapixl10','pixlrService','Pixlr','Pixlr','pixlr','20081217',29,26,1,2,1,'tool/lapixl10/learning.do?mode=learner','tool/lapixl10/learning.do?mode=author','tool/lapixl10/learning.do?mode=teacher','tool/lapixl10/authoring.do','tool/lapixl10/authoring.do?mode=teacher','tool/lapixl10/exportPortfolio?mode=learner','tool/lapixl10/exportPortfolio?mode=teacher','tool/lapixl10/monitoring.do','tool/lapixl10/contribute.do','tool/lapixl10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lapixl10','2009-05-04 18:58:54','org.lamsfoundation.lams.tool.pixlr.ApplicationResources','2009-05-04 18:58:54','tool/lapixl10/lapixl10admin.do',0,NULL),(28,'lavidr10','videoRecorderService','VideoRecorder','VideoRecorder','videoRecorder','20081208',31,28,1,2,1,'tool/lavidr10/learning.do?mode=learner','tool/lavidr10/learning.do?mode=author','tool/lavidr10/learning.do?mode=teacher','tool/lavidr10/authoring.do','tool/lavidr10/authoring.do?mode=teacher','tool/lavidr10/exportPortfolio?mode=learner','tool/lavidr10/exportPortfolio?mode=teacher','tool/lavidr10/monitoring.do','tool/lavidr10/contribute.do','tool/lavidr10/moderate.do',NULL,'http://wiki.lamsfoundation.org/display/lamsdocs/lavidr10','2009-05-04 19:00:31','org.lamsfoundation.lams.tool.videoRecorder.ApplicationResources','2009-05-04 19:00:31',NULL,1,NULL);
/*!40000 ALTER TABLE `lams_tool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_content`
--

DROP TABLE IF EXISTS `lams_tool_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_tool_content` (
  `tool_content_id` bigint(20) NOT NULL auto_increment,
  `tool_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`tool_content_id`),
  KEY `tool_id` (`tool_id`),
  CONSTRAINT `FK_lams_tool_content_1` FOREIGN KEY (`tool_id`) REFERENCES `lams_tool` (`tool_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_tool_content`
--

LOCK TABLES `lams_tool_content` WRITE;
/*!40000 ALTER TABLE `lams_tool_content` DISABLE KEYS */;
INSERT INTO `lams_tool_content` VALUES (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10),(11,11),(12,12),(13,13),(14,14),(15,15),(17,17),(23,23),(33,23),(24,24),(32,24),(25,25),(31,25),(26,26),(30,26),(28,28),(29,28);
/*!40000 ALTER TABLE `lams_tool_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_import_support`
--

DROP TABLE IF EXISTS `lams_tool_import_support`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_tool_import_support` (
  `id` bigint(20) NOT NULL auto_increment,
  `installed_tool_signature` varchar(15) NOT NULL,
  `supports_tool_signature` varchar(50) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_tool_session`
--

LOCK TABLES `lams_tool_session` WRITE;
/*!40000 ALTER TABLE `lams_tool_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_tool_session_state`
--

DROP TABLE IF EXISTS `lams_tool_session_state`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_tool_session_state` (
  `tool_session_state_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`tool_session_state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_tool_session_type` (
  `tool_session_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`tool_session_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `lams_community_token` varchar(255) default NULL,
  `lams_community_username` varchar(255) default NULL,
  `timezone` tinyint(4) default NULL,
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
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_user`
--

LOCK TABLES `lams_user` WRITE;
/*!40000 ALTER TABLE `lams_user` DISABLE KEYS */;
INSERT INTO `lams_user` VALUES (1,'sysadmin','a159b7ae81ba3552af61e9731b20870515944538','The','System','Administrator',NULL,NULL,NULL,'Sydney','NSW',NULL,'Australia',NULL,NULL,NULL,NULL,'sysadmin@x.x',0,'2009-05-04 18:45:26',1,51,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(5,'test1','b444ac06613fc8d63795be9ad0beaf55011936ac','Dr','One','Test','1','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test1@xx.os',0,'2004-12-23 00:00:00',1,5,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(6,'test2','109f4b3c50d7b0df729d299bc6f8e9ef9066971f','Dr','Two','Test','2','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test2@xx.os',0,'2004-12-23 00:00:00',1,6,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(7,'test3','3ebfa301dc59196f18593c45e519287a23297589','Dr','Three','Test','3','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test3@xx.os',0,'2004-12-23 00:00:00',1,7,1,2,NULL,1,NULL,0,1,NULL,NULL,28),(8,'test4','1ff2b3704aede04eecb51e50ca698efd50a1379b','Dr','Four','Test','4','Test Ave',NULL,'Nowhere','NSW',NULL,'Australia','0211111111','0211111112','0411111111','0211111113','test4@xx.os',0,'2004-12-23 00:00:00',1,8,1,2,NULL,1,NULL,0,1,NULL,NULL,28);
/*!40000 ALTER TABLE `lams_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_group`
--

DROP TABLE IF EXISTS `lams_user_group`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_user_group` (
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`user_id`,`group_id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `FK_lams_user_group_1` FOREIGN KEY (`user_id`) REFERENCES `lams_user` (`user_id`),
  CONSTRAINT `FK_lams_user_group_2` FOREIGN KEY (`group_id`) REFERENCES `lams_group` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_user_group`
--

LOCK TABLES `lams_user_group` WRITE;
/*!40000 ALTER TABLE `lams_user_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `lams_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_user_organisation`
--

DROP TABLE IF EXISTS `lams_user_organisation`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_user_organisation_collapsed` (
  `user_organisation_id` bigint(20) NOT NULL,
  `collapsed` tinyint(1) NOT NULL default '1',
  PRIMARY KEY  (`user_organisation_id`),
  CONSTRAINT `FK_lams_user_organisation_collapsed_1` FOREIGN KEY (`user_organisation_id`) REFERENCES `lams_user_organisation` (`user_organisation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_wkspc_fld_content_type` (
  `content_type_id` int(3) NOT NULL auto_increment,
  `description` varchar(64) NOT NULL,
  PRIMARY KEY  (`content_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_wkspc_wkspc_folder` (
  `id` bigint(20) NOT NULL auto_increment,
  `workspace_id` bigint(20) default NULL,
  `workspace_folder_id` bigint(20) default NULL,
  PRIMARY KEY  (`id`),
  KEY `workspace_id` (`workspace_id`),
  KEY `workspace_folder_id` (`workspace_folder_id`),
  CONSTRAINT `FK_lams_ww_folder_1` FOREIGN KEY (`workspace_id`) REFERENCES `lams_workspace` (`workspace_id`),
  CONSTRAINT `FK_lams_ww_folder_2` FOREIGN KEY (`workspace_folder_id`) REFERENCES `lams_workspace_folder` (`workspace_folder_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_wkspc_wkspc_folder`
--

LOCK TABLES `lams_wkspc_wkspc_folder` WRITE;
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` DISABLE KEYS */;
INSERT INTO `lams_wkspc_wkspc_folder` VALUES (1,1,1),(2,2,2),(3,2,22),(4,3,3),(5,3,23),(7,5,5),(8,6,6),(9,7,7),(10,8,8),(11,50,40),(12,51,45),(13,52,46);
/*!40000 ALTER TABLE `lams_wkspc_wkspc_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace`
--

DROP TABLE IF EXISTS `lams_workspace`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_workspace`
--

LOCK TABLES `lams_workspace` WRITE;
/*!40000 ALTER TABLE `lams_workspace` DISABLE KEYS */;
INSERT INTO `lams_workspace` VALUES (1,1,NULL,'ROOT'),(2,2,22,'Developers Playpen'),(3,3,23,'MATH111'),(5,5,NULL,'One Test'),(6,6,NULL,'Two Test'),(7,7,NULL,'Three Test'),(8,8,NULL,'Four Test'),(50,40,41,'Moodle Test'),(51,45,NULL,'System Administrator'),(52,46,NULL,'Public Folder');
/*!40000 ALTER TABLE `lams_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder`
--

DROP TABLE IF EXISTS `lams_workspace_folder`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `lams_workspace_folder`
--

LOCK TABLES `lams_workspace_folder` WRITE;
/*!40000 ALTER TABLE `lams_workspace_folder` DISABLE KEYS */;
INSERT INTO `lams_workspace_folder` VALUES (1,NULL,'ROOT',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(2,1,'Developers Playpen',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(3,1,'MATH111',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(5,NULL,'One Test',5,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(6,NULL,'Two Test',6,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(7,NULL,'Three Test',7,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(8,NULL,'Four Test',8,'2004-12-23 00:00:00','2004-12-23 00:00:00',1),(22,2,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(23,3,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(40,1,'Moodle Test',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(41,40,'Lesson Sequence Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',2),(45,NULL,'System Administrator',1,'2006-11-01 00:00:00','2006-11-01 00:00:00',1),(46,1,'Public Folder',1,'2004-12-23 00:00:00','2004-12-23 00:00:00',3);
/*!40000 ALTER TABLE `lams_workspace_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lams_workspace_folder_content`
--

DROP TABLE IF EXISTS `lams_workspace_folder_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `lams_workspace_folder_type` (
  `lams_workspace_folder_type_id` int(3) NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`lams_workspace_folder_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `patches` (
  `system_name` varchar(30) NOT NULL,
  `patch_level` int(11) NOT NULL,
  `patch_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `patch_in_progress` char(1) NOT NULL default 'F',
  PRIMARY KEY  (`system_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `patches`
--

LOCK TABLES `patches` WRITE;
/*!40000 ALTER TABLE `patches` DISABLE KEYS */;
INSERT INTO `patches` VALUES ('laasse10',20090109,'2009-05-04 08:58:12','F'),('lachat11',20081125,'2009-05-04 08:46:07','F'),('ladaco10',20090326,'2009-05-04 08:46:55','F'),('lafrum11',20081118,'2009-05-04 08:45:45','F'),('lagmap10',20080521,'2009-05-04 08:46:44','F'),('laimag10',20081007,'2009-05-04 08:58:28','F'),('lamc11',20081127,'2009-05-04 08:47:10','F'),('lamind10',20090202,'2009-05-04 08:58:41','F'),('lams',16,'2009-05-04 08:45:26','F'),('lanb11',20081209,'2009-05-04 08:45:49','F'),('lantbk11',20081118,'2009-05-04 08:46:24','F'),('lapixl10',20081217,'2009-05-04 08:58:54','F'),('laqa11',20081126,'2009-05-04 08:45:57','F'),('larsrc11',20090115,'2009-05-04 08:46:16','F'),('lasbmt11',20090113,'2009-05-04 08:46:02','F'),('lascrb11',20090226,'2009-05-04 08:46:34','F'),('lasprd10',20080612,'2009-05-04 08:46:50','F'),('lasurv11',20081021,'2009-05-04 08:46:29','F'),('latask10',20090113,'2009-05-04 08:46:40','F'),('lavidr10',20081208,'2009-05-04 09:00:31','F'),('lavote11',20081203,'2009-05-04 08:46:19','F'),('lawiki10',20090121,'2009-05-04 08:47:00','F');
/*!40000 ALTER TABLE `patches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_assessment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default NULL,
  `time_limit` int(11) default '0',
  `attempts_allowed` int(11) default '1',
  `instructions` text,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default NULL,
  `define_later` tinyint(4) default NULL,
  `content_id` bigint(20) default NULL,
  `allow_question_feedback` tinyint(4) default NULL,
  `allow_overall_feedback` tinyint(4) default NULL,
  `allow_right_wrong_answers` tinyint(4) default NULL,
  `allow_grades_after_attempt` tinyint(4) default NULL,
  `allow_history_responses` tinyint(4) default NULL,
  `questions_per_page` int(11) default '0',
  `shuffled` tinyint(4) default NULL,
  `attempt_completion_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_1720029621_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_1720029621_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_laasse10_assessment`
--

LOCK TABLES `tl_laasse10_assessment` WRITE;
/*!40000 ALTER TABLE `tl_laasse10_assessment` DISABLE KEYS */;
INSERT INTO `tl_laasse10_assessment` VALUES (1,NULL,NULL,NULL,'Assessment',0,0,1,'Instructions ',NULL,NULL,0,0,23,0,0,0,0,0,0,0,0);
/*!40000 ALTER TABLE `tl_laasse10_assessment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laasse10_assessment_overall_feedback`
--

DROP TABLE IF EXISTS `tl_laasse10_assessment_overall_feedback`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_assessment_overall_feedback` (
  `uid` bigint(20) NOT NULL auto_increment,
  `assessment_uid` bigint(20) default NULL,
  `sequence_id` int(11) default NULL,
  `grade_boundary` int(11) default NULL,
  `feedback` text,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_assessment_overall_feedback_1` (`assessment_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_overall_feedback_1` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_assessment_question` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_type` smallint(6) default NULL,
  `title` varchar(255) default NULL,
  `question` text,
  `sequence_id` int(11) default NULL,
  `default_grade` int(11) default '1',
  `penalty_factor` float default '0.1',
  `general_feedback` text,
  `feedback` text,
  `multiple_answers_allowed` tinyint(4) default '0',
  `feedback_on_correct` text,
  `feedback_on_partially_correct` text,
  `feedback_on_incorrect` text,
  `shuffle` tinyint(4) default NULL,
  `case_sensitive` tinyint(4) default NULL,
  `correct_answer` tinyint(4) default '0',
  `create_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `assessment_uid` bigint(20) default NULL,
  `session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1720029621_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_1720029621_F52D1F9330E79035` (`assessment_uid`),
  KEY `FK_NEW_1720029621_F52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laasse10_session` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F9330E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_assessment_result` (
  `uid` bigint(20) NOT NULL auto_increment,
  `assessment_uid` bigint(20) default NULL,
  `start_date` datetime default NULL,
  `finish_date` datetime default NULL,
  `user_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `maximum_grade` int(11) default NULL,
  `grade` float default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_laasse10_assessment_result_2` (`user_uid`),
  KEY `FK_tl_laasse10_assessment_result_3` (`assessment_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_result_3` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_result_2` FOREIGN KEY (`user_uid`) REFERENCES `tl_laasse10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_assessment_unit` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_uid` bigint(20) default NULL,
  `sequence_id` int(11) default NULL,
  `multiplier` float default NULL,
  `unit` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_assessment_unit_1` (`question_uid`),
  CONSTRAINT `FK_tl_laasse10_assessment_unit_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `assessment_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1720029621_1E7009430E79035` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_1E7009430E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_option_answer` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_result_uid` bigint(20) default NULL,
  `question_option_uid` bigint(20) default NULL,
  `answer_boolean` tinyint(1) default NULL,
  `answer_int` int(11) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_option_answer_1` (`question_result_uid`),
  CONSTRAINT `FK_tl_laasse10_option_answer_1` FOREIGN KEY (`question_result_uid`) REFERENCES `tl_laasse10_question_result` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_question_option` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question_uid` bigint(20) default NULL,
  `sequence_id` int(11) default NULL,
  `question` text,
  `option_string` text,
  `option_float` float default NULL,
  `accepted_error` float default NULL,
  `grade` float default NULL,
  `feedback` text,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `FK_tl_laasse10_question_option_1` (`question_uid`),
  CONSTRAINT `FK_tl_laasse10_question_option_1` FOREIGN KEY (`question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_question_result` (
  `uid` bigint(20) NOT NULL auto_increment,
  `assessment_question_uid` bigint(20) default NULL,
  `result_uid` bigint(20) default NULL,
  `answer_string` text,
  `answer_float` float default NULL,
  `answer_boolean` tinyint(1) default NULL,
  `submitted_option_uid` bigint(20) default NULL,
  `mark` float default NULL,
  `penalty` float default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1720029621_693580A438BF8DFE` (`assessment_question_uid`),
  KEY `FK_tl_laasse10_question_result_1` (`result_uid`),
  CONSTRAINT `FK_tl_laasse10_question_result_1` FOREIGN KEY (`result_uid`) REFERENCES `tl_laasse10_assessment_result` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_693580A438BF8DFE` FOREIGN KEY (`assessment_question_uid`) REFERENCES `tl_laasse10_assessment_question` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `assessment_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1720029621_24AA78C530E79035` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_24AA78C530E79035` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laasse10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `assessment_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1720029621_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_1720029621_30113BFC309ED320` (`assessment_uid`),
  CONSTRAINT `FK_NEW_1720029621_30113BFC309ED320` FOREIGN KEY (`assessment_uid`) REFERENCES `tl_laasse10_assessment` (`uid`),
  CONSTRAINT `FK_NEW_1720029621_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laasse10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lachat11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `ChatConditionToChat` (`content_uid`),
  CONSTRAINT `ChatConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ChatConditionToChat` FOREIGN KEY (`content_uid`) REFERENCES `tl_lachat11_chat` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  `create_date` datetime default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `user_uid` (`user_uid`),
  KEY `question_uid` (`question_uid`),
  CONSTRAINT `AnswerToQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_ladaco10_questions` (`uid`),
  CONSTRAINT `AnswerToUser` FOREIGN KEY (`user_uid`) REFERENCES `tl_ladaco10_users` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_ladaco10_contents`
--

LOCK TABLES `tl_ladaco10_contents` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_contents` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_contents` VALUES (1,NULL,NULL,NULL,'Data Collection',0,0,0,0,'Instructions',NULL,NULL,0,0,14,NULL,0,0,0);
/*!40000 ALTER TABLE `tl_ladaco10_contents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_questions`
--

DROP TABLE IF EXISTS `tl_ladaco10_questions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_ladaco10_questions`
--

LOCK TABLES `tl_ladaco10_questions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_questions` DISABLE KEYS */;
INSERT INTO `tl_ladaco10_questions` VALUES (1,'<div>What is your favourite colour?</div>',NULL,'2009-05-04 18:46:55',0,1,NULL,NULL,NULL,NULL,1,NULL);
/*!40000 ALTER TABLE `tl_ladaco10_questions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_sessions`
--

DROP TABLE IF EXISTS `tl_ladaco10_sessions`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_ladaco10_sessions`
--

LOCK TABLES `tl_ladaco10_sessions` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_sessions` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_ladaco10_sessions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_ladaco10_users`
--

DROP TABLE IF EXISTS `tl_ladaco10_users`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_ladaco10_users`
--

LOCK TABLES `tl_ladaco10_users` WRITE;
/*!40000 ALTER TABLE `tl_ladaco10_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_ladaco10_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_attachment`
--

DROP TABLE IF EXISTS `tl_lafrum11_attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lafrum11_condition_topics` (
  `condition_id` bigint(20) NOT NULL default '0',
  `topic_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`topic_uid`),
  KEY `ForumConditionQuestionToForumQuestion` (`topic_uid`),
  CONSTRAINT `ForumConditionQuestionToForumCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lafrum11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionQuestionToForumQuestion` FOREIGN KEY (`topic_uid`) REFERENCES `tl_lafrum11_message` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lafrum11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `ForumConditionToForum` (`content_uid`),
  CONSTRAINT `ForumConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ForumConditionToForum` FOREIGN KEY (`content_uid`) REFERENCES `tl_lafrum11_forum` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lafrum11_forum`
--

LOCK TABLES `tl_lafrum11_forum` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_forum` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_forum` VALUES (1,NULL,NULL,NULL,'Forum',0,0,0,'Instructions',NULL,NULL,0,0,1,1,0,1,0,1,0,5000,1,NULL,0,0);
/*!40000 ALTER TABLE `tl_lafrum11_forum` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_forum_user`
--

DROP TABLE IF EXISTS `tl_lafrum11_forum_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lafrum11_forum_user`
--

LOCK TABLES `tl_lafrum11_forum_user` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_forum_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_forum_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_message`
--

DROP TABLE IF EXISTS `tl_lafrum11_message`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lafrum11_message`
--

LOCK TABLES `tl_lafrum11_message` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message` DISABLE KEYS */;
INSERT INTO `tl_lafrum11_message` VALUES (1,'2009-05-04 18:45:45','2009-05-04 18:45:45','2009-05-04 18:45:45',NULL,NULL,'Topic Heading','Topic message',1,0,NULL,NULL,1,0,0,NULL,NULL);
/*!40000 ALTER TABLE `tl_lafrum11_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_message_seq`
--

DROP TABLE IF EXISTS `tl_lafrum11_message_seq`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lafrum11_message_seq`
--

LOCK TABLES `tl_lafrum11_message_seq` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_message_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lafrum11_report`
--

DROP TABLE IF EXISTS `tl_lafrum11_report`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lafrum11_report` (
  `uid` bigint(20) NOT NULL auto_increment,
  `comment` text,
  `release_date` datetime default NULL,
  `mark` float default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lafrum11_timestamp` (
  `uid` bigint(20) NOT NULL auto_increment,
  `message_uid` bigint(20) NOT NULL,
  `timestamp_date` datetime NOT NULL,
  `forum_user_uid` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `uid` (`uid`),
  KEY `message_uid` (`message_uid`),
  KEY `forum_user_uid` (`forum_user_uid`),
  KEY `ForumUserFK` (`forum_user_uid`),
  KEY `MessageFK` (`message_uid`),
  CONSTRAINT `MessageFK` FOREIGN KEY (`message_uid`) REFERENCES `tl_lafrum11_message` (`uid`),
  CONSTRAINT `ForumUserFK` FOREIGN KEY (`forum_user_uid`) REFERENCES `tl_lafrum11_forum_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lafrum11_tool_session`
--

LOCK TABLES `tl_lafrum11_tool_session` WRITE;
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lafrum11_tool_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_attachment`
--

DROP TABLE IF EXISTS `tl_lagmap10_attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lagmap10_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lagmap10_gmap`
--

LOCK TABLES `tl_lagmap10_gmap` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_gmap` DISABLE KEYS */;
INSERT INTO `tl_lagmap10_gmap` VALUES (1,NULL,NULL,NULL,'Gmap','Instructions',0x00,0x00,'','',0x00,0x00,12,0x01,0x01,0x00,1,0x01,0x00,0x01,0x01,-33.774322,151.111988,13,'G_NORMAL_MAP',0x00,'','Macquarie University, Sydney NSW');
/*!40000 ALTER TABLE `tl_lagmap10_gmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_marker`
--

DROP TABLE IF EXISTS `tl_lagmap10_marker`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lagmap10_marker`
--

LOCK TABLES `tl_lagmap10_marker` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_marker` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lagmap10_marker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_session`
--

DROP TABLE IF EXISTS `tl_lagmap10_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lagmap10_session`
--

LOCK TABLES `tl_lagmap10_session` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lagmap10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lagmap10_user`
--

DROP TABLE IF EXISTS `tl_lagmap10_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lagmap10_user`
--

LOCK TABLES `tl_lagmap10_user` WRITE;
/*!40000 ALTER TABLE `tl_lagmap10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lagmap10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_attachment`
--

DROP TABLE IF EXISTS `tl_laimag10_attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_attachment` (
  `attachment_uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `imageGallery_uid` bigint(20) default NULL,
  PRIMARY KEY  (`attachment_uid`),
  KEY `FK_NEW_1821149711_1E7009430E79035` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_1E7009430E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imageGallery` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_imageGallery` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `run_offline` tinyint(4) default NULL,
  `lock_on_finished` tinyint(4) default NULL,
  `instructions` text,
  `next_image_title` bigint(20) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` tinyint(4) default NULL,
  `define_later` tinyint(4) default NULL,
  `content_id` bigint(20) default NULL,
  `allow_comment_images` tinyint(4) default NULL,
  `allow_share_images` tinyint(4) default NULL,
  `allow_vote` tinyint(4) default NULL,
  `reflect_instructions` varchar(255) default NULL,
  `reflect_on_activity` smallint(6) default NULL,
  `allow_rank` tinyint(4) default NULL,
  `image_submit_notify` tinyint(4) default '0',
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `content_id` (`content_id`),
  KEY `FK_NEW_1821149711_89093BF758092FB` (`create_by`),
  CONSTRAINT `FK_NEW_1821149711_89093BF758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_laimag10_imageGallery`
--

LOCK TABLES `tl_laimag10_imageGallery` WRITE;
/*!40000 ALTER TABLE `tl_laimag10_imageGallery` DISABLE KEYS */;
INSERT INTO `tl_laimag10_imageGallery` VALUES (1,NULL,NULL,NULL,'Image Gallery',0,0,'Instructions ',1,NULL,NULL,0,0,24,0,0,0,NULL,0,0,0);
/*!40000 ALTER TABLE `tl_laimag10_imageGallery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laimag10_imageGallery_item`
--

DROP TABLE IF EXISTS `tl_laimag10_imageGallery_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_imageGallery_item` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` text,
  `title` varchar(255) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `create_by_author` tinyint(4) default NULL,
  `sequence_id` int(11) default NULL,
  `is_hide` tinyint(4) default NULL,
  `imageGallery_uid` bigint(20) default NULL,
  `session_uid` bigint(20) default NULL,
  `original_file_uuid` bigint(20) default NULL,
  `original_image_width` int(11) default NULL,
  `original_image_height` int(11) default NULL,
  `medium_file_uuid` bigint(20) default NULL,
  `medium_image_width` int(11) default NULL,
  `medium_image_height` int(11) default NULL,
  `thumbnail_file_uuid` bigint(20) default NULL,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1821149711_F52D1F93758092FB` (`create_by`),
  KEY `FK_NEW_1821149711_F52D1F9330E79035` (`imageGallery_uid`),
  KEY `FK_NEW_1821149711_F52D1F93EC0D3147` (`session_uid`),
  CONSTRAINT `FK_NEW_1821149711_F52D1F93EC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laimag10_session` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_F52D1F9330E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imageGallery` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_image_comment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `comment` text,
  `imageGallery_item_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_laimag10_image_comment_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_comment_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_comment_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_comment_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imageGallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_image_rating` (
  `uid` bigint(20) NOT NULL auto_increment,
  `rating` int(11) default NULL,
  `imageGallery_item_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_laimag10_image_rating_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_rating_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_rating_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_rating_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imageGallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_image_vote` (
  `uid` bigint(20) NOT NULL auto_increment,
  `is_voted` tinyint(4) default NULL,
  `imageGallery_item_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_laimag10_image_vote_3` (`imageGallery_item_uid`),
  KEY `FK_tl_laimag10_image_vote_2` (`create_by`),
  CONSTRAINT `FK_tl_laimag10_image_vote_2` FOREIGN KEY (`create_by`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_tl_laimag10_image_vote_3` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imageGallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_item_log` (
  `uid` bigint(20) NOT NULL auto_increment,
  `access_date` datetime default NULL,
  `imageGallery_item_uid` bigint(20) default NULL,
  `user_uid` bigint(20) default NULL,
  `complete` tinyint(4) default NULL,
  `session_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1821149711_693580A438BF8DFE` (`imageGallery_item_uid`),
  KEY `FK_NEW_1821149711_693580A441F9365D` (`user_uid`),
  CONSTRAINT `FK_NEW_1821149711_693580A441F9365D` FOREIGN KEY (`user_uid`) REFERENCES `tl_laimag10_user` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_693580A438BF8DFE` FOREIGN KEY (`imageGallery_item_uid`) REFERENCES `tl_laimag10_imageGallery_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `imageGallery_uid` bigint(20) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1821149711_24AA78C530E79035` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_24AA78C530E79035` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imageGallery` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laimag10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `session_finished` smallint(6) default NULL,
  `session_uid` bigint(20) default NULL,
  `imageGallery_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_1821149711_30113BFCEC0D3147` (`session_uid`),
  KEY `FK_NEW_1821149711_30113BFC309ED320` (`imageGallery_uid`),
  CONSTRAINT `FK_NEW_1821149711_30113BFC309ED320` FOREIGN KEY (`imageGallery_uid`) REFERENCES `tl_laimag10_imageGallery` (`uid`),
  CONSTRAINT `FK_NEW_1821149711_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_laimag10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamc11_content`
--

LOCK TABLES `tl_lamc11_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_content` VALUES (1,17,'MCQ','Instructions','2009-05-04 18:47:10',NULL,0,0,1,0,0,'','',0,0,0,0,NULL,0,0,1);
/*!40000 ALTER TABLE `tl_lamc11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_options_content`
--

DROP TABLE IF EXISTS `tl_lamc11_options_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamc11_options_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `correct_option` tinyint(1) NOT NULL default '0',
  `mc_que_content_id` bigint(20) NOT NULL,
  `mc_que_option_text` varchar(250) default NULL,
  `displayOrder` int(5) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `mc_que_content_id` (`mc_que_content_id`),
  CONSTRAINT `FK_tl_lamc11_options_content_1` FOREIGN KEY (`mc_que_content_id`) REFERENCES `tl_lamc11_que_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamc11_options_content`
--

LOCK TABLES `tl_lamc11_options_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_options_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_options_content` VALUES (1,0,1,'Answer 1',1),(2,1,1,'Answer 2',2);
/*!40000 ALTER TABLE `tl_lamc11_options_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_que_content`
--

DROP TABLE IF EXISTS `tl_lamc11_que_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamc11_que_content`
--

LOCK TABLES `tl_lamc11_que_content` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_que_content` DISABLE KEYS */;
INSERT INTO `tl_lamc11_que_content` VALUES (1,'A Sample question?',1,1,1,NULL);
/*!40000 ALTER TABLE `tl_lamc11_que_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_que_usr`
--

DROP TABLE IF EXISTS `tl_lamc11_que_usr`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamc11_session`
--

LOCK TABLES `tl_lamc11_session` WRITE;
/*!40000 ALTER TABLE `tl_lamc11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lamc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamc11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lamc11_uploadedfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `mindmap_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_972920762_12090F57FC940906` (`mindmap_uid`),
  CONSTRAINT `FK_NEW_972920762_12090F57FC940906` FOREIGN KEY (`mindmap_uid`) REFERENCES `tl_lamind10_mindmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_mindmap` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `multiuser_mode` bit(1) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `last_action_id` bigint(20) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamind10_mindmap`
--

LOCK TABLES `tl_lamind10_mindmap` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_mindmap` DISABLE KEYS */;
INSERT INTO `tl_lamind10_mindmap` VALUES (1,NULL,NULL,NULL,'Mindmap','Instructions',0x00,0x00,0x00,'','',0x00,0x00,25,NULL,0x00,NULL),(2,'2009-05-04 19:05:07',NULL,NULL,'Mindmap','Instructions',0x00,0x00,0x00,'','',0x00,0x00,31,NULL,0x00,NULL);
/*!40000 ALTER TABLE `tl_lamind10_mindmap` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_node`
--

DROP TABLE IF EXISTS `tl_lamind10_node`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_node` (
  `node_id` bigint(20) NOT NULL auto_increment,
  `unique_id` bigint(20) default NULL,
  `parent_id` bigint(20) default NULL,
  `node_text` varchar(100) default NULL,
  `node_color` varchar(6) default NULL,
  `user_id` bigint(20) default NULL,
  `mindmap_id` bigint(20) default NULL,
  PRIMARY KEY  (`node_id`),
  KEY `MindmapNode_Parent_FK` (`parent_id`),
  KEY `MindmapNode_User_FK` (`user_id`),
  KEY `MindmapNode_Mindmap_FK` (`mindmap_id`),
  CONSTRAINT `MindmapNode_Mindmap_FK` FOREIGN KEY (`mindmap_id`) REFERENCES `tl_lamind10_mindmap` (`uid`),
  CONSTRAINT `MindmapNode_Parent_FK` FOREIGN KEY (`parent_id`) REFERENCES `tl_lamind10_node` (`node_id`),
  CONSTRAINT `MindmapNode_User_FK` FOREIGN KEY (`user_id`) REFERENCES `tl_lamind10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lamind10_node`
--

LOCK TABLES `tl_lamind10_node` WRITE;
/*!40000 ALTER TABLE `tl_lamind10_node` DISABLE KEYS */;
INSERT INTO `tl_lamind10_node` VALUES (1,1,NULL,'Main Idea','ffffff',NULL,2),(2,2,1,'Sub idea 1','ffffff',NULL,2),(3,3,1,'Sub idea 2','ffffff',NULL,2);
/*!40000 ALTER TABLE `tl_lamind10_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lamind10_request`
--

DROP TABLE IF EXISTS `tl_lamind10_request`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_request` (
  `uid` bigint(20) NOT NULL auto_increment,
  `unique_id` bigint(20) default NULL,
  `global_id` bigint(20) default NULL,
  `request_type` tinyint(4) default NULL,
  `node_id` bigint(20) default NULL,
  `node_child_id` bigint(20) default NULL,
  `user_id` bigint(20) default NULL,
  `mindmap_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `mindmap_id` (`mindmap_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `tl_lamind10_request_fk` FOREIGN KEY (`mindmap_id`) REFERENCES `tl_lamind10_mindmap` (`uid`),
  CONSTRAINT `tl_lamind10_request_fk1` FOREIGN KEY (`user_id`) REFERENCES `tl_lamind10_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `mindmap_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_972920762_B7C198E2FC940906` (`mindmap_uid`),
  CONSTRAINT `FK_NEW_972920762_B7C198E2FC940906` FOREIGN KEY (`mindmap_uid`) REFERENCES `tl_lamind10_mindmap` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lamind10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `mindmap_session_uid` bigint(20) default NULL,
  `entry_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_972920762_CB8A58FFA3B0FADF` (`mindmap_session_uid`),
  CONSTRAINT `FK_NEW_972920762_CB8A58FFA3B0FADF` FOREIGN KEY (`mindmap_session_uid`) REFERENCES `tl_lamind10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lanb11_content`
--

LOCK TABLES `tl_lanb11_content` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_content` DISABLE KEYS */;
INSERT INTO `tl_lanb11_content` VALUES (1,2,'Noticeboard','Content','','',0,0,0,'Reflect on noticeboard',0,NULL,'2009-05-04 18:45:49',NULL);
/*!40000 ALTER TABLE `tl_lanb11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_session`
--

DROP TABLE IF EXISTS `tl_lanb11_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lanb11_session`
--

LOCK TABLES `tl_lanb11_session` WRITE;
/*!40000 ALTER TABLE `tl_lanb11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lanb11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lanb11_user`
--

DROP TABLE IF EXISTS `tl_lanb11_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lantbk11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `NotebookConditionToNotebook` (`content_uid`),
  CONSTRAINT `NotebookConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `NotebookConditionToNotebook` FOREIGN KEY (`content_uid`) REFERENCES `tl_lantbk11_notebook` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lantbk11_notebook`
--

LOCK TABLES `tl_lantbk11_notebook` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_notebook` DISABLE KEYS */;
INSERT INTO `tl_lantbk11_notebook` VALUES (1,NULL,NULL,NULL,'Notebook','Instructions',0x00,0x00,0x00,'','',0x00,0x00,8);
/*!40000 ALTER TABLE `tl_lantbk11_notebook` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_session`
--

DROP TABLE IF EXISTS `tl_lantbk11_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lantbk11_session`
--

LOCK TABLES `tl_lantbk11_session` WRITE;
/*!40000 ALTER TABLE `tl_lantbk11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lantbk11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lantbk11_user`
--

DROP TABLE IF EXISTS `tl_lantbk11_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lapixl10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `pixlr_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK951F889ACB8ADA74` (`pixlr_uid`),
  CONSTRAINT `FK951F889ACB8ADA74` FOREIGN KEY (`pixlr_uid`) REFERENCES `tl_lapixl10_pixlr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lapixl10_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` text,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lapixl10_pixlr` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `reflect_on_activity` bit(1) default NULL,
  `allow_view_others_images` bit(1) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `image_file_name` text,
  `image_width` bigint(20) default NULL,
  `image_height` bigint(20) default NULL,
  `reflect_instructions` text,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lapixl10_pixlr`
--

LOCK TABLES `tl_lapixl10_pixlr` WRITE;
/*!40000 ALTER TABLE `tl_lapixl10_pixlr` DISABLE KEYS */;
INSERT INTO `tl_lapixl10_pixlr` VALUES (1,NULL,NULL,NULL,'Pixlr','Instructions',0x00,0x00,0x00,0x01,'','',0x00,0x00,26,'blank.jpg',400,450,NULL),(2,'2009-05-04 19:05:01',NULL,NULL,'Pixlr','Instructions',0x00,0x00,0x00,0x01,'','',0x00,0x00,30,'blank.jpg',400,450,NULL);
/*!40000 ALTER TABLE `tl_lapixl10_pixlr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lapixl10_session`
--

DROP TABLE IF EXISTS `tl_lapixl10_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lapixl10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `pixlr_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKE5C05E7FCB8ADA74` (`pixlr_uid`),
  CONSTRAINT `FKE5C05E7FCB8ADA74` FOREIGN KEY (`pixlr_uid`) REFERENCES `tl_lapixl10_pixlr` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lapixl10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `pixlr_session_uid` bigint(20) default NULL,
  `entry_uid` bigint(20) default NULL,
  `image_file_name` text,
  `imageHeight` bigint(20) default NULL,
  `imageWidth` bigint(20) default NULL,
  `imageHidden` bit(1) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK9A39C08236E23005` (`pixlr_session_uid`),
  CONSTRAINT `FK9A39C08236E23005` FOREIGN KEY (`pixlr_session_uid`) REFERENCES `tl_lapixl10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_condition_questions` (
  `condition_id` bigint(20) NOT NULL default '0',
  `question_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`question_uid`),
  KEY `QaConditionQuestionToQaQuestion` (`question_uid`),
  CONSTRAINT `QaConditionQuestionToQaCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_laqa11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionQuestionToQaQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_laqa11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `QaConditionToQaContent` (`content_uid`),
  CONSTRAINT `QaConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `QaConditionToQaContent` FOREIGN KEY (`content_uid`) REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_configuration` (
  `uid` bigint(20) NOT NULL auto_increment,
  `config_key` varchar(30) default NULL,
  `config_value` varchar(255) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_laqa11_content`
--

LOCK TABLES `tl_laqa11_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_content` VALUES (1,3,'Q&A','Instructions','2009-05-04 18:45:55',NULL,0,0,0,0,0,0,0,NULL,NULL,0,NULL,0,1);
/*!40000 ALTER TABLE `tl_laqa11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_content`
--

DROP TABLE IF EXISTS `tl_laqa11_que_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_que_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `question` text,
  `feedback` text,
  `display_order` int(5) default '1',
  `qa_content_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `qa_content_id` (`qa_content_id`),
  CONSTRAINT `FK_tl_laqa11_que_content_1` FOREIGN KEY (`qa_content_id`) REFERENCES `tl_laqa11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_laqa11_que_content`
--

LOCK TABLES `tl_laqa11_que_content` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_que_content` DISABLE KEYS */;
INSERT INTO `tl_laqa11_que_content` VALUES (1,'Sample Question 1?',NULL,1,1);
/*!40000 ALTER TABLE `tl_laqa11_que_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_que_usr`
--

DROP TABLE IF EXISTS `tl_laqa11_que_usr`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_laqa11_session`
--

LOCK TABLES `tl_laqa11_session` WRITE;
/*!40000 ALTER TABLE `tl_laqa11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_laqa11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_laqa11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_laqa11_uploadedfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_wizard_category` (
  `uid` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_wizard_cognitive_skill` (
  `uid` bigint(20) NOT NULL auto_increment,
  `title` varchar(255) NOT NULL,
  `category_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK3BA4132BCBB0DC8D` (`category_uid`),
  CONSTRAINT `FK3BA4132BCBB0DC8D` FOREIGN KEY (`category_uid`) REFERENCES `tl_laqa11_wizard_category` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_laqa11_wizard_question` (
  `uid` bigint(20) NOT NULL auto_increment,
  `cognitive_skill_uid` bigint(20) default NULL,
  `title` text NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKAF08A0C7EFF77FD4` (`cognitive_skill_uid`),
  CONSTRAINT `FKAF08A0C7EFF77FD4` FOREIGN KEY (`cognitive_skill_uid`) REFERENCES `tl_laqa11_wizard_cognitive_skill` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_larsrc11_item_instruction` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` varchar(255) default NULL,
  `sequence_id` int(11) default NULL,
  `item_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FKA5665013980570ED` (`item_uid`),
  CONSTRAINT `FKA5665013980570ED` FOREIGN KEY (`item_uid`) REFERENCES `tl_larsrc11_resource_item` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_larsrc11_item_instruction`
--

LOCK TABLES `tl_larsrc11_item_instruction` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_item_instruction` VALUES (1,'Use Google to search the web',0,1);
/*!40000 ALTER TABLE `tl_larsrc11_item_instruction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_item_log`
--

DROP TABLE IF EXISTS `tl_larsrc11_item_log`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_larsrc11_resource`
--

LOCK TABLES `tl_larsrc11_resource` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource` VALUES (1,NULL,NULL,NULL,'Resources',0,0,'Instructions ',NULL,NULL,0,0,6,0,0,0,0,NULL,0,0);
/*!40000 ALTER TABLE `tl_larsrc11_resource` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_resource_item`
--

DROP TABLE IF EXISTS `tl_larsrc11_resource_item`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_larsrc11_resource_item`
--

LOCK TABLES `tl_larsrc11_resource_item` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` DISABLE KEYS */;
INSERT INTO `tl_larsrc11_resource_item` VALUES (1,NULL,NULL,NULL,NULL,NULL,NULL,'Web Search','http://www.google.com ',NULL,'2009-05-04 18:46:16',1,0,1,NULL,NULL,0,1,NULL);
/*!40000 ALTER TABLE `tl_larsrc11_resource_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_session`
--

DROP TABLE IF EXISTS `tl_larsrc11_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_larsrc11_session`
--

LOCK TABLES `tl_larsrc11_session` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_larsrc11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_larsrc11_user`
--

DROP TABLE IF EXISTS `tl_larsrc11_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_larsrc11_user`
--

LOCK TABLES `tl_larsrc11_user` WRITE;
/*!40000 ALTER TABLE `tl_larsrc11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_larsrc11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_content`
--

DROP TABLE IF EXISTS `tl_lasbmt11_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasbmt11_content`
--

LOCK TABLES `tl_lasbmt11_content` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_content` DISABLE KEYS */;
INSERT INTO `tl_lasbmt11_content` VALUES (4,'Submit Files','Instructions',0,0,NULL,NULL,0,0,NULL,0,0,1,NULL,NULL,NULL,0,0);
/*!40000 ALTER TABLE `tl_lasbmt11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_instruction_files`
--

DROP TABLE IF EXISTS `tl_lasbmt11_instruction_files`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasbmt11_report` (
  `report_id` bigint(20) NOT NULL auto_increment,
  `comments` varchar(250) default NULL,
  `marks` float default NULL,
  `date_marks_released` datetime default NULL,
  PRIMARY KEY  (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasbmt11_session` (
  `session_id` bigint(20) NOT NULL,
  `status` int(11) NOT NULL,
  `content_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  PRIMARY KEY  (`session_id`),
  KEY `FKEC8C77C9785A173A` (`content_id`),
  CONSTRAINT `FKEC8C77C9785A173A` FOREIGN KEY (`content_id`) REFERENCES `tl_lasbmt11_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasbmt11_session`
--

LOCK TABLES `tl_lasbmt11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasbmt11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasbmt11_submission_details`
--

DROP TABLE IF EXISTS `tl_lasbmt11_submission_details`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasbmt11_user`
--

LOCK TABLES `tl_lasbmt11_user` WRITE;
/*!40000 ALTER TABLE `tl_lasbmt11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasbmt11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lascrb11_attachment`
--

DROP TABLE IF EXISTS `tl_lascrb11_attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lascrb11_heading` (
  `uid` bigint(20) NOT NULL auto_increment,
  `heading` text,
  `scribe_uid` bigint(20) default NULL,
  `display_order` int(11) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK428A22FFB3FA1495` (`scribe_uid`),
  CONSTRAINT `FK428A22FFB3FA1495` FOREIGN KEY (`scribe_uid`) REFERENCES `tl_lascrb11_scribe` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasprd10_session`
--

LOCK TABLES `tl_lasprd10_session` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasprd10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasprd10_spreadsheet`
--

LOCK TABLES `tl_lasprd10_spreadsheet` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` DISABLE KEYS */;
INSERT INTO `tl_lasprd10_spreadsheet` VALUES (1,NULL,NULL,NULL,'Spreadsheet',0,1,0,0,'Instructions ','',NULL,NULL,0,0,13,NULL,0);
/*!40000 ALTER TABLE `tl_lasprd10_spreadsheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_spreadsheet_mark`
--

DROP TABLE IF EXISTS `tl_lasprd10_spreadsheet_mark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasprd10_spreadsheet_mark` (
  `uid` bigint(20) NOT NULL auto_increment,
  `marks` varchar(255) default NULL,
  `comments` text,
  `date_marks_released` datetime default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasprd10_user`
--

LOCK TABLES `tl_lasprd10_user` WRITE;
/*!40000 ALTER TABLE `tl_lasprd10_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasprd10_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasprd10_user_modified_spreadsheet`
--

DROP TABLE IF EXISTS `tl_lasprd10_user_modified_spreadsheet`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasprd10_user_modified_spreadsheet` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_modified_spreadsheet` text,
  `mark_id` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_tl_lasprd10_user_modified_spreadsheet_1` (`mark_id`),
  CONSTRAINT `FK_tl_lasprd10_user_modified_spreadsheet_1` FOREIGN KEY (`mark_id`) REFERENCES `tl_lasprd10_spreadsheet_mark` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasurv11_condition_questions` (
  `condition_id` bigint(20) NOT NULL default '0',
  `question_uid` bigint(20) NOT NULL default '0',
  PRIMARY KEY  (`condition_id`,`question_uid`),
  KEY `SurveyConditionQuestionToSurveyQuestion` (`question_uid`),
  CONSTRAINT `SurveyConditionQuestionToSurveyCondition` FOREIGN KEY (`condition_id`) REFERENCES `tl_lasurv11_conditions` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionQuestionToSurveyQuestion` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasurv11_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `SurveyConditionToSurvey` (`content_uid`),
  CONSTRAINT `SurveyConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `SurveyConditionToSurvey` FOREIGN KEY (`content_uid`) REFERENCES `tl_lasurv11_survey` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lasurv11_option` (
  `uid` bigint(20) NOT NULL auto_increment,
  `description` text,
  `sequence_id` int(11) default NULL,
  `question_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK85AB46F26966134F` (`question_uid`),
  CONSTRAINT `FK85AB46F26966134F` FOREIGN KEY (`question_uid`) REFERENCES `tl_lasurv11_question` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasurv11_option`
--

LOCK TABLES `tl_lasurv11_option` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_option` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_option` VALUES (1,'Option 1',0,1),(2,'Option 2',1,1),(3,'Option 3',2,1),(4,'Option 1',0,2),(5,'Option 2',1,2),(6,'Option 3',2,2);
/*!40000 ALTER TABLE `tl_lasurv11_option` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_question`
--

DROP TABLE IF EXISTS `tl_lasurv11_question`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasurv11_question`
--

LOCK TABLES `tl_lasurv11_question` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_question` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_question` VALUES (1,1,'Sample Multiple choice - only one response allowed?',NULL,'2009-05-04 18:46:29',1,0,0,0,1),(2,2,'Sample Multiple choice - multiple response allowed?',NULL,'2009-05-04 18:46:29',2,0,0,1,1),(3,3,'Sample Free text question?',NULL,'2009-05-04 18:46:29',3,0,0,0,1);
/*!40000 ALTER TABLE `tl_lasurv11_question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_session`
--

DROP TABLE IF EXISTS `tl_lasurv11_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasurv11_session`
--

LOCK TABLES `tl_lasurv11_session` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_survey`
--

DROP TABLE IF EXISTS `tl_lasurv11_survey`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasurv11_survey`
--

LOCK TABLES `tl_lasurv11_survey` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_survey` DISABLE KEYS */;
INSERT INTO `tl_lasurv11_survey` VALUES (1,'Survey',0,1,'Instructions',NULL,NULL,0,0,9,NULL,0,1,NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `tl_lasurv11_survey` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lasurv11_user`
--

DROP TABLE IF EXISTS `tl_lasurv11_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lasurv11_user`
--

LOCK TABLES `tl_lasurv11_user` WRITE;
/*!40000 ALTER TABLE `tl_lasurv11_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lasurv11_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_attachment`
--

DROP TABLE IF EXISTS `tl_latask10_attachment`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_NEW_174079138_1E7009430E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_taskList` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_latask10_condition` (
  `condition_uid` bigint(20) NOT NULL auto_increment,
  `sequence_id` int(11) default NULL,
  `taskList_uid` bigint(20) default NULL,
  `name` varchar(255) default NULL,
  PRIMARY KEY  (`condition_uid`),
  KEY `FK_tl_latask10_condition_1` (`taskList_uid`),
  CONSTRAINT `FK_tl_latask10_condition_1` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_taskList` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_latask10_condition_tl_item` (
  `uid` bigint(20) NOT NULL,
  `condition_uid` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`,`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_1` (`condition_uid`),
  KEY `FK_tl_latask10_taskList_item_condition_2` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_2` FOREIGN KEY (`uid`) REFERENCES `tl_latask10_taskList_item` (`uid`),
  CONSTRAINT `FK_tl_latask10_taskList_item_condition_1` FOREIGN KEY (`condition_uid`) REFERENCES `tl_latask10_condition` (`condition_uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_tl_latask10_item_attachment_1` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_taskList_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_tl_latask10_item_comment_3` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_taskList_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_NEW_174079138_693580A438BF8DFE` FOREIGN KEY (`taskList_item_uid`) REFERENCES `tl_latask10_taskList_item` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_NEW_174079138_24AA78C530E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_taskList` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_latask10_taskList` (
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_latask10_taskList_item` (
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
  CONSTRAINT `FK_NEW_174079138_F52D1F9330E79035` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_taskList` (`uid`),
  CONSTRAINT `FK_NEW_174079138_F52D1F93758092FB` FOREIGN KEY (`create_by`) REFERENCES `tl_latask10_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_latask10_taskList_item`
--

LOCK TABLES `tl_latask10_taskList_item` WRITE;
/*!40000 ALTER TABLE `tl_latask10_taskList_item` DISABLE KEYS */;
INSERT INTO `tl_latask10_taskList_item` VALUES (1,1,NULL,NULL,NULL,'Task number 1',NULL,'2009-05-04 18:46:40',1,0,0,0,0,0,0,1,0,NULL,1,NULL);
/*!40000 ALTER TABLE `tl_latask10_taskList_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_latask10_user`
--

DROP TABLE IF EXISTS `tl_latask10_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
  CONSTRAINT `FK_NEW_174079138_30113BFC309ED320` FOREIGN KEY (`taskList_uid`) REFERENCES `tl_latask10_taskList` (`uid`),
  CONSTRAINT `FK_NEW_174079138_30113BFCEC0D3147` FOREIGN KEY (`session_uid`) REFERENCES `tl_latask10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_attachment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `file_version_id` bigint(20) default NULL,
  `file_type` varchar(255) default NULL,
  `file_name` varchar(255) default NULL,
  `file_uuid` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `videoRecorder_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_75587508_12090F57FC940906` (`videoRecorder_uid`),
  CONSTRAINT `FK_NEW_75587508_12090F57FC940906` FOREIGN KEY (`videoRecorder_uid`) REFERENCES `tl_lavidr10_videoRecorder` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_comment` (
  `uid` bigint(20) NOT NULL auto_increment,
  `comment` text,
  `videoRecorder_recording_uid` bigint(20) default NULL,
  `videoRecorder_session_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_conditions` (
  `condition_id` bigint(20) NOT NULL,
  `content_uid` bigint(20) default NULL,
  PRIMARY KEY  (`condition_id`),
  KEY `VideoRecorderConditionToVideoRecorder` (`content_uid`),
  CONSTRAINT `VideoRecorderConditionInheritance` FOREIGN KEY (`condition_id`) REFERENCES `lams_branch_condition` (`condition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `VideoRecorderConditionToVideoRecorder` FOREIGN KEY (`content_uid`) REFERENCES `tl_lavidr10_videoRecorder` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lavidr10_conditions`
--

LOCK TABLES `tl_lavidr10_conditions` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_conditions` DISABLE KEYS */;
INSERT INTO `tl_lavidr10_conditions` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `tl_lavidr10_conditions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavidr10_rating`
--

DROP TABLE IF EXISTS `tl_lavidr10_rating`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_rating` (
  `uid` bigint(20) NOT NULL auto_increment,
  `rating` float default NULL,
  `videoRecorder_recording_uid` bigint(20) default NULL,
  `videoRecorder_session_uid` bigint(20) default NULL,
  `create_by` bigint(20) default NULL,
  `create_date` datetime default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_recording` (
  `uid` bigint(20) NOT NULL auto_increment,
  `tool_content_id` bigint(20) default NULL,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `description` varchar(1027) default NULL,
  `rating` float default NULL,
  `filename` varchar(255) default NULL,
  `is_just_sound` bit(1) default NULL,
  `videoRecorder_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_session` (
  `uid` bigint(20) NOT NULL auto_increment,
  `session_end_date` datetime default NULL,
  `session_start_date` datetime default NULL,
  `status` int(11) default NULL,
  `session_id` bigint(20) default NULL,
  `session_name` varchar(250) default NULL,
  `videoRecorder_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_75587508_B7C198E2FC940906` (`videoRecorder_uid`),
  CONSTRAINT `FK_NEW_75587508_B7C198E2FC940906` FOREIGN KEY (`videoRecorder_uid`) REFERENCES `tl_lavidr10_videoRecorder` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_user` (
  `uid` bigint(20) NOT NULL auto_increment,
  `user_id` bigint(20) default NULL,
  `last_name` varchar(255) default NULL,
  `login_name` varchar(255) default NULL,
  `first_name` varchar(255) default NULL,
  `finishedActivity` bit(1) default NULL,
  `videoRecorder_session_uid` bigint(20) default NULL,
  `entry_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  KEY `FK_NEW_75587508_CB8A58FFA3B0FADF` (`videoRecorder_session_uid`),
  CONSTRAINT `FK_NEW_75587508_CB8A58FFA3B0FADF` FOREIGN KEY (`videoRecorder_session_uid`) REFERENCES `tl_lavidr10_session` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavidr10_videoRecorder` (
  `uid` bigint(20) NOT NULL auto_increment,
  `create_date` datetime default NULL,
  `update_date` datetime default NULL,
  `create_by` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `instructions` text,
  `reflect_on_activity` bit(1) default NULL,
  `reflect_instructions` text,
  `run_offline` bit(1) default NULL,
  `lock_on_finished` bit(1) default NULL,
  `online_instructions` text,
  `offline_instructions` text,
  `content_in_use` bit(1) default NULL,
  `define_later` bit(1) default NULL,
  `tool_content_id` bigint(20) default NULL,
  `allow_use_voice` bit(1) default NULL,
  `allow_use_camera` bit(1) default NULL,
  `allow_learner_video_visibility` bit(1) default NULL,
  `allow_learner_video_export` bit(1) default NULL,
  `allow_comments` bit(1) default NULL,
  `allow_ratings` bit(1) default NULL,
  `export_offline` bit(1) default NULL,
  `export_all` bit(1) default NULL,
  PRIMARY KEY  (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lavidr10_videoRecorder`
--

LOCK TABLES `tl_lavidr10_videoRecorder` WRITE;
/*!40000 ALTER TABLE `tl_lavidr10_videoRecorder` DISABLE KEYS */;
INSERT INTO `tl_lavidr10_videoRecorder` VALUES (1,NULL,NULL,NULL,'VideoRecorder','Instructions',0x00,'',0x00,0x00,'','',0x00,0x00,28,0x01,0x01,0x01,0x01,0x01,0x01,0x01,0x01),(2,'2009-05-04 19:04:52',NULL,NULL,'VideoRecorder','Instructions',0x00,'',0x00,0x00,'','',0x00,0x00,29,0x01,0x01,0x01,NULL,0x01,0x01,0x01,0x01);
/*!40000 ALTER TABLE `tl_lavidr10_videoRecorder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_content`
--

DROP TABLE IF EXISTS `tl_lavote11_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lavote11_content`
--

LOCK TABLES `tl_lavote11_content` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_content` DISABLE KEYS */;
INSERT INTO `tl_lavote11_content` VALUES (1,7,'Voting','Instructions','2009-05-04 18:46:19',NULL,'1',0,0,1,0,0,'','',0,0,0,NULL,1);
/*!40000 ALTER TABLE `tl_lavote11_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_nomination_content`
--

DROP TABLE IF EXISTS `tl_lavote11_nomination_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lavote11_nomination_content` (
  `uid` bigint(20) NOT NULL auto_increment,
  `nomination` text,
  `display_order` int(5) default NULL,
  `vote_content_id` bigint(20) NOT NULL,
  PRIMARY KEY  (`uid`),
  KEY `vote_content_id` (`vote_content_id`),
  CONSTRAINT `FK_tl_lavote11_nomination_content_1` FOREIGN KEY (`vote_content_id`) REFERENCES `tl_lavote11_content` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lavote11_nomination_content`
--

LOCK TABLES `tl_lavote11_nomination_content` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_nomination_content` DISABLE KEYS */;
INSERT INTO `tl_lavote11_nomination_content` VALUES (1,'Sample Nomination 1',1,1),(2,'Sample Nomination 2',2,1);
/*!40000 ALTER TABLE `tl_lavote11_nomination_content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_session`
--

DROP TABLE IF EXISTS `tl_lavote11_session`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lavote11_session`
--

LOCK TABLES `tl_lavote11_session` WRITE;
/*!40000 ALTER TABLE `tl_lavote11_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lavote11_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lavote11_uploadedfile`
--

DROP TABLE IF EXISTS `tl_lavote11_uploadedfile`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lawiki10_session`
--

LOCK TABLES `tl_lawiki10_session` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `tl_lawiki10_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_user`
--

DROP TABLE IF EXISTS `tl_lawiki10_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
SET character_set_client = @saved_cs_client;

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
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lawiki10_wiki`
--

LOCK TABLES `tl_lawiki10_wiki` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki` VALUES (1,NULL,NULL,NULL,'Wiki','Instructions',0x00,0x00,0x01,0x01,0x01,0x00,0x00,'',0,0,'','',0x00,0x00,15,1);
/*!40000 ALTER TABLE `tl_lawiki10_wiki` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tl_lawiki10_wiki_page` (
  `uid` bigint(20) NOT NULL auto_increment,
  `wiki_uid` bigint(20) default NULL,
  `title` varchar(255) default NULL,
  `editable` bit(1) default NULL,
  `wiki_current_content` bigint(20) default NULL,
  `added_by` bigint(20) default NULL,
  `wiki_session_uid` bigint(20) default NULL,
  PRIMARY KEY  (`uid`),
  UNIQUE KEY `UK9406D87760B3B03B` (`wiki_uid`,`title`,`wiki_session_uid`),
  KEY `FK961AFFEAD8004954` (`wiki_session_uid`),
  KEY `FK961AFFEA60B3B03B` (`wiki_uid`),
  KEY `FK961AFFEA36CBA7DB` (`added_by`),
  KEY `FK961AFFEAE48332B4` (`wiki_current_content`),
  CONSTRAINT `FK961AFFEAE48332B4` FOREIGN KEY (`wiki_current_content`) REFERENCES `tl_lawiki10_wiki_page_content` (`uid`),
  CONSTRAINT `FK961AFFEA36CBA7DB` FOREIGN KEY (`added_by`) REFERENCES `tl_lawiki10_user` (`uid`),
  CONSTRAINT `FK961AFFEA60B3B03B` FOREIGN KEY (`wiki_uid`) REFERENCES `tl_lawiki10_wiki` (`uid`),
  CONSTRAINT `FK961AFFEAD8004954` FOREIGN KEY (`wiki_session_uid`) REFERENCES `tl_lawiki10_session` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lawiki10_wiki_page`
--

LOCK TABLES `tl_lawiki10_wiki_page` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page` VALUES (1,1,'Wiki Main Page',0x01,1,NULL,NULL);
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tl_lawiki10_wiki_page_content`
--

DROP TABLE IF EXISTS `tl_lawiki10_wiki_page_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Dumping data for table `tl_lawiki10_wiki_page_content`
--

LOCK TABLES `tl_lawiki10_wiki_page_content` WRITE;
/*!40000 ALTER TABLE `tl_lawiki10_wiki_page_content` DISABLE KEYS */;
INSERT INTO `tl_lawiki10_wiki_page_content` VALUES (1,1,'<div>Wiki Body</div>',NULL,NULL,0);
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

-- Dump completed on 2009-05-04  9:06:27
