SET FOREIGN_KEY_CHECKS=0;
SET NAMES utf8mb4 ;

--
-- Dumping data for table `lams_activity_category`
--

LOCK TABLES `lams_activity_category` WRITE;
INSERT INTO `lams_activity_category` VALUES (1,'SYSTEM'),(2,'COLLABORATION'),(3,'ASSESSMENT'),(4,'CONTENT'),(5,'SPLIT'),(6,'RESPONSE');
UNLOCK TABLES;

--
-- Dumping data for table `lams_auth_method_type`
--

LOCK TABLES `lams_auth_method_type` WRITE;
INSERT INTO `lams_auth_method_type` VALUES (1,'LAMS'),(2,'WEB_AUTH'),(3,'LDAP');
UNLOCK TABLES;

--
-- Dumping data for table `lams_authentication_method`
--

LOCK TABLES `lams_authentication_method` WRITE;
INSERT INTO `lams_authentication_method` VALUES (1,1,'LAMS-Database'),(3,3,'MQ-LDAP');
UNLOCK TABLES;

--
-- Dumping data for table `lams_copy_type`
--

LOCK TABLES `lams_copy_type` WRITE;
INSERT INTO `lams_copy_type` VALUES (1,'NONE'),(2,'LESSON'),(3,'PREVIEW');
UNLOCK TABLES;

--
-- Dumping data for table `lams_ext_server_org_map`
--

LOCK TABLES `lams_ext_server_org_map` WRITE;
INSERT INTO `lams_ext_server_org_map` VALUES (1,'moodle','moodle','moodle','moodle','mdl','http://localhost/moodle/mod/lamstwo/userinfo.php?ts=%timestamp%&un=%username%&hs=%hash%','',NULL,0x01,80,0,NULL,1,NULL,0,0,1,1,0,0,1,NULL,NULL);
UNLOCK TABLES;

--
-- Dumping data for table `lams_ext_server_type`
--

LOCK TABLES `lams_ext_server_type` WRITE;
INSERT INTO `lams_ext_server_type` VALUES (1,'INTEGRATED SERVER'),(2,'LTI TOOL CONSUMER');
UNLOCK TABLES;

--
-- Dumping data for table `lams_gate_activity_level`
--

LOCK TABLES `lams_gate_activity_level` WRITE;
INSERT INTO `lams_gate_activity_level` VALUES (1,'LEARNER'),(2,'GROUP'),(3,'CLASS');
UNLOCK TABLES;

--
-- Dumping data for table `lams_grouping_support_type`
--

LOCK TABLES `lams_grouping_support_type` WRITE;
INSERT INTO `lams_grouping_support_type` VALUES (1,'NONE'),(2,'OPTIONAL'),(3,'REQUIRED');
UNLOCK TABLES;

--
-- Dumping data for table `lams_grouping_type`
--

LOCK TABLES `lams_grouping_type` WRITE;
INSERT INTO `lams_grouping_type` VALUES (1,'RANDOM_GROUPING'),(2,'CHOSEN_GROUPING'),(3,'CLASS_GROUPING'),(4,'LEARNER_CHOICE_GROUPING');
UNLOCK TABLES;

--
-- Dumping data for table `lams_learning_activity_type`
--

LOCK TABLES `lams_learning_activity_type` WRITE;
INSERT INTO `lams_learning_activity_type` VALUES (1,'TOOL'),(2,'GROUPING'),(3,'GATE_SYNCH'),(4,'GATE_SCHEDULE'),(5,'GATE_PERMISSION'),(6,'PARALLEL'),(7,'OPTIONS'),(8,'SEQUENCE'),(9,'GATE_SYSTEM'),(10,'BRANCHING_CHOSEN'),(11,'BRANCHING_GROUP'),(12,'BRANCHING_TOOL'),(13,'OPTIONS_WITH_SEQUENCES'),(14,'GATE_CONDITION'),(15,'FLOATING'),(16, 'GATE_PASSWORD');
UNLOCK TABLES;

--
-- Dumping data for table `lams_lesson_state`
--

LOCK TABLES `lams_lesson_state` WRITE;
INSERT INTO `lams_lesson_state` VALUES (1,'CREATED'),(2,'NOT_STARTED'),(3,'STARTED'),(4,'SUSPENDED'),(5,'FINISHED'),(6,'ARCHIVED'),(7,'REMOVED');
UNLOCK TABLES;

--
-- Dumping data for table `lams_license`
--

LOCK TABLES `lams_license` WRITE;
INSERT INTO `lams_license` VALUES (1,'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 4.0','by-nc-sa','https://creativecommons.org/licenses/by-nc-sa/4.0/',1,'/images/license/by-nc-sa.eu.svg',1),(2,'CC Attribution-No Derivatives 4.0','by-nd','https://creativecommons.org/licenses/by-nd/4.0/',0,'/images/license/by-nd.svg',2),(3,'CC Attribution-Noncommercial-No Derivatives 4.0','by-nc-nd','https://creativecommons.org/licenses/by-nc-nd/4.0/',0,'/images/license/by-nc-nd.svg',3),(4,'CC Attribution-Noncommercial 4.0','by-nc','https://creativecommons.org/licenses/by-nc/4.0/',0,'/images/license/by-nc.eu.svg',4),(5,'CC Attribution-ShareAlike 4.0','by-sa','https://creativecommons.org/licenses/by-sa/4.0/',0,'/images/license/by-sa.svg',5),(6,'Other Licensing Agreement','other','',0,'',8),(7,'CC Attribution 4.0','by','https://creativecommons.org/licenses/by/4.0/',0,'/images/license/by.svg',6),(8,'Public Domain','CC0','https://creativecommons.org/publicdomain/zero/1.0/',0,'/images/license/publicdomain.svg',7);
UNLOCK TABLES;

--
-- Dumping data for table `lams_log_event_type`
--

LOCK TABLES `lams_log_event_type` WRITE;
INSERT INTO `lams_log_event_type` VALUES (1,'TYPE_TEACHER_LEARNING_DESIGN_CREATE','LESSON'),(2,'TYPE_TEACHER_LESSON_CREATE','LESSON'),(3,'TYPE_TEACHER_LESSON_START','LESSON'),(4,'TYPE_TEACHER_LESSON_CHANGE_STATE','LESSON'),(5,'TYPE_LEARNER_ACTIVITY_START','LESSON'),(6,'TYPE_LEARNER_ACTIVITY_FINISH','LESSON'),(7,'TYPE_LEARNER_LESSON_COMPLETE','LESSON'),(8,'TYPE_LEARNER_LESSON_MARK_SUBMIT','LESSON'),(9,'TYPE_ACTIVITY_EDIT','LESSON'),(10,'TYPE_FORCE_COMPLETE','LESSON'),(11,'TYPE_USER_ORG_ADMIN','SECURITY'),(12,'TYPE_LOGIN_AS','SECURITY'),(13,'TYPE_PASSWORD_CHANGE','SECURITY'),(14,'TYPE_ROLE_FAILURE','SECURITY'),(15,'TYPE_ACCOUNT_LOCKED','SECURITY'),(16,'TYPE_NOTIFICATION','NOTIFICATION'),(17,'TYPE_MARK_UPDATED','MARKS'),(18,'TYPE_MARK_RELEASED','MARKS'),(19,'TYPE_LEARNER_CONTENT_UPDATED','LEARNER_CONTENT'),(20,'TYPE_LEARNER_CONTENT_SHOW_HIDE','LEARNER_CONTENT'),(21,'TYPE_UNKNOWN','UNKNOWN'),(22,'TYPE_LIVE_EDIT','LESSON'),(23,'TYPE_TOOL_MARK_RELEASED','MARKS');
UNLOCK TABLES;


--
-- Dumping data for table `lams_organisation_state`
--

LOCK TABLES `lams_organisation_state` WRITE;
INSERT INTO `lams_organisation_state` VALUES (1,'ACTIVE'),(2,'HIDDEN'),(3,'ARCHIVED'),(4,'REMOVED');
UNLOCK TABLES;

--
-- Dumping data for table `lams_organisation_type`
--

LOCK TABLES `lams_organisation_type` WRITE;
INSERT INTO `lams_organisation_type` VALUES (1,'ROOT ORGANISATION','root all other organisations: controlled by Sysadmin'),(2,'COURSE ORGANISATION','main organisation level - equivalent to an entire course.'),(3,'CLASS','runtime organisation level - lessons are run for classes.');
UNLOCK TABLES;


--
-- Dumping data for table `lams_outcome_scale`
--

LOCK TABLES `lams_outcome_scale` WRITE;
INSERT INTO `lams_outcome_scale` VALUES (1,NULL,'Default attainment scale','default','Default global scale',NULL,1,NOW());
UNLOCK TABLES;

--
-- Dumping data for table `lams_outcome_scale_item`
--

LOCK TABLES `lams_outcome_scale_item` WRITE;
INSERT INTO `lams_outcome_scale_item` VALUES (1,1,0,'Not yet attained'),(2,1,1,'Attained');
UNLOCK TABLES;

--
-- Dumping data for table `lams_policy_state`
--

LOCK TABLES `lams_policy_state` WRITE;
INSERT INTO `lams_policy_state` VALUES (1,'ACTIVE'),(2,'INACTIVE');
UNLOCK TABLES;

--
-- Dumping data for table `lams_policy_type`
--

LOCK TABLES `lams_policy_type` WRITE;
INSERT INTO `lams_policy_type` VALUES (1,'TYPE_SITE_POLICY'),(2,'TYPE_PRIVACY_POLICY'),(3,'TYPE_THIRD_PARTIES_POLICY'),(4,'TYPE_OTHER');
UNLOCK TABLES;


--
-- Dumping data for table `lams_rating_criteria_type`
--

LOCK TABLES `lams_rating_criteria_type` WRITE;
INSERT INTO `lams_rating_criteria_type` VALUES (1,'TOOL_ACTIVITY'),(2,'AUTHORED_ITEM'),(3,'LEARNER_ITEM'),(4,'LESSON');
UNLOCK TABLES;


--
-- Dumping data for table `lams_role`
--

LOCK TABLES `lams_role` WRITE;
INSERT INTO `lams_role` VALUES (1,'SYSADMIN','LAMS System Adminstrator',NOW()),(2,'GROUP MANAGER','Group Manager',NOW()),(3,'AUTHOR','Authors Learning Designs',NOW()),(4,'MONITOR','Member of Staff',NOW()),(5,'LEARNER','Student',NOW());
UNLOCK TABLES;

--
-- Dumping data for table `lams_supported_locale`
--

LOCK TABLES `lams_supported_locale` WRITE;
INSERT INTO `lams_supported_locale` VALUES (1,'en','AU','English (Australia)','LTR','en-au'),(2,'es','ES','Español','LTR','es'),(3,'mi','NZ','Māori','LTR','en-au'),(4,'de','DE','Deutsch','LTR','de'),(5,'zh','CN','简体中文','LTR','zh-cn'),(6,'fr','FR','Français','LTR','fr'),(7,'it','IT','Italiano','LTR','it'),(8,'no','NO','Norsk','LTR','no'),(9,'sv','SE','Svenska','LTR','sv'),(10,'ko','KR','한국어','LTR','ko'),(11,'pl','PL','Polski','LTR','pl'),(12,'pt','BR','Português (Brasil)','LTR','pt-br'),(13,'hu','HU','Magyar','LTR','hu'),(14,'bg','BG','Български','LTR','bg'),(15,'cy','GB','Cymraeg (Cymru)','LTR','cy'),(16,'th','TH','Thai','LTR','th'),(17,'el','GR','Ελληνικά','LTR','el'),(18,'nl','BE','Nederlands (België)','LTR','nl'),(19,'ar','JO','عربي','RTL','ar'),(20,'da','DK','Dansk','LTR','da'),(21,'ru','RU','Русский','LTR','ru'),(22,'vi','VN','Tiếng Việt','LTR','vi'),(23,'zh','TW','Chinese (Taiwan)','LTR','zh'),(24,'ja','JP','日本語','LTR','ja'),(25,'ms','MY','Malay (Malaysia)','LTR','ms'),(26,'tr','TR','Türkçe','LTR','tr'),(27,'ca','ES','Català','LTR','ca'),(28,'sl','SI','Slovenščina','LTR','sl'),(29,'es','MX','Español (México)','LTR','es'),(30,'cs','CZ','Čeština','LTR','cs'),(31,'id','ID','Indonesian','LTR','id');
UNLOCK TABLES;

--
-- Dumping data for table `lams_system_tool`
--

LOCK TABLES `lams_system_tool` WRITE;
INSERT INTO `lams_system_tool` VALUES (1,2,'Grouping','All types of grouping including random and chosen.','learning/grouping/performGrouping.do','learning/grouping/performGrouping.do','learning/grouping/viewGrouping.do?mode=teacher','monitoring/grouping/startGrouping.do','monitoring/grouping/startGrouping.do',NULL,NOW(),NULL,'pedagogicalPlanner/grouping.do?method=initGrouping'),(2,3,'Sync Gate','Gate: Synchronise Learners.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,NOW(),NULL,NULL),(3,4,'Schedule Gate','Gate: Opens/shuts at particular times.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,NOW(),NULL,NULL),(4,5,'Permission Gate','Gate: Opens under teacher or staff control.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,NOW(),NULL,NULL),(5,9,'System Gate','Gate: Opens under system control.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,NOW(),NULL,NULL),(6,10,'Monitor Chosen Branching','Select between multiple sequence activities, with the branch chosen in monitoring.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/chosenBranching.do?method=assignBranch','monitoring/grouping/startGrouping.do',NULL,NOW(),NULL,NULL),(7,11,'Group Based Branching','Select between multiple sequence activities, with the branch chosen by an existing group.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/groupedBranching.do?method=viewBranching','monitoring/groupedBranching.do?method=assignBranch',NULL,NOW(),NULL,NULL),(8,12,'Tool Output Based Branching','Select between multiple sequence activities, with the branch chosen on results of another activity.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/toolBranching.do?method=viewBranching','monitoring/toolBranching.do?method=viewBranching',NULL,NOW(),NULL,NULL),(9,8,'Sequence','A sequence of activities','learning/SequenceActivity.do','learning/SequenceActivity.do','monitoring/complexProgress.do','monitoring/sequence/viewSequence.do','monitoring/sequence/viewSequence.do',NULL,NOW(),NULL,NULL),(10,14,'Condition Gate','Gate: Opens if conditions are met','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,NOW(),NULL,NULL),(11,15,'Floating Activities','A collection of floating activities',NULL,NULL,NULL,'monitoring/floating.do?method=viewFloating','monitoring/floating.do?method=viewFloating',NULL,NOW(),NULL,NULL),(12, 16, 'Password Gate', 'Gate: Opens if learner provides correct password','learning/gate/knockGate.do', 'learning/gate/knockGate.do', NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do', NULL, now(), NULL, NULL);
UNLOCK TABLES;


--
-- Dumping data for table `lams_timezone`
--

LOCK TABLES `lams_timezone` WRITE;
INSERT INTO `lams_timezone` VALUES (1,'Africa/Abidjan',0),(2,'Africa/Accra',0),(3,'Africa/Addis_Ababa',0),(4,'Africa/Algiers',0),(5,'Africa/Asmara',0),(6,'Africa/Asmera',0),(7,'Africa/Bamako',0),(8,'Africa/Bangui',0),(9,'Africa/Banjul',0),(10,'Africa/Bissau',0),(11,'Africa/Blantyre',0),(12,'Africa/Brazzaville',0),(13,'Africa/Bujumbura',0),(14,'Africa/Cairo',0),(15,'Africa/Casablanca',0),(16,'Africa/Ceuta',0),(17,'Africa/Conakry',0),(18,'Africa/Dakar',0),(19,'Africa/Dar_es_Salaam',0),(20,'Africa/Djibouti',0),(21,'Africa/Douala',0),(22,'Africa/El_Aaiun',0),(23,'Africa/Freetown',0),(24,'Africa/Gaborone',0),(25,'Africa/Harare',0),(26,'Africa/Johannesburg',0),(27,'Africa/Juba',0),(28,'Africa/Kampala',0),(29,'Africa/Khartoum',0),(30,'Africa/Kigali',0),(31,'Africa/Kinshasa',0),(32,'Africa/Lagos',0),(33,'Africa/Libreville',0),(34,'Africa/Lome',0),(35,'Africa/Luanda',0),(36,'Africa/Lubumbashi',0),(37,'Africa/Lusaka',0),(38,'Africa/Malabo',0),(39,'Africa/Maputo',0),(40,'Africa/Maseru',0),(41,'Africa/Mbabane',0),(42,'Africa/Mogadishu',0),(43,'Africa/Monrovia',0),(44,'Africa/Nairobi',0),(45,'Africa/Ndjamena',0),(46,'Africa/Niamey',0),(47,'Africa/Nouakchott',0),(48,'Africa/Ouagadougou',0),(49,'Africa/Porto-Novo',0),(50,'Africa/Sao_Tome',0),(51,'Africa/Timbuktu',0),(52,'Africa/Tripoli',0),(53,'Africa/Tunis',0),(54,'Africa/Windhoek',0),(55,'America/Adak',0),(56,'America/Anchorage',0),(57,'America/Anguilla',0),(58,'America/Antigua',0),(59,'America/Araguaina',0),(60,'America/Argentina/Buenos_Aires',0),(61,'America/Argentina/Catamarca',0),(62,'America/Argentina/ComodRivadavia',0),(63,'America/Argentina/Cordoba',0),(64,'America/Argentina/Jujuy',0),(65,'America/Argentina/La_Rioja',0),(66,'America/Argentina/Mendoza',0),(67,'America/Argentina/Rio_Gallegos',0),(68,'America/Argentina/Salta',0),(69,'America/Argentina/San_Juan',0),(70,'America/Argentina/San_Luis',0),(71,'America/Argentina/Tucuman',0),(72,'America/Argentina/Ushuaia',0),(73,'America/Aruba',0),(74,'America/Asuncion',0),(75,'America/Atikokan',0),(76,'America/Atka',0),(77,'America/Bahia',0),(78,'America/Bahia_Banderas',0),(79,'America/Barbados',0),(80,'America/Belem',0),(81,'America/Belize',0),(82,'America/Blanc-Sablon',0),(83,'America/Boa_Vista',0),(84,'America/Bogota',0),(85,'America/Boise',0),(86,'America/Buenos_Aires',0),(87,'America/Cambridge_Bay',0),(88,'America/Campo_Grande',0),(89,'America/Cancun',0),(90,'America/Caracas',0),(91,'America/Catamarca',0),(92,'America/Cayenne',0),(93,'America/Cayman',0),(94,'America/Chicago',0),(95,'America/Chihuahua',0),(96,'America/Coral_Harbour',0),(97,'America/Cordoba',0),(98,'America/Costa_Rica',0),(99,'America/Creston',0),(100,'America/Cuiaba',0),(101,'America/Curacao',0),(102,'America/Danmarkshavn',0),(103,'America/Dawson',0),(104,'America/Dawson_Creek',0),(105,'America/Denver',0),(106,'America/Detroit',0),(107,'America/Dominica',0),(108,'America/Edmonton',0),(109,'America/Eirunepe',0),(110,'America/El_Salvador',0),(111,'America/Ensenada',0),(112,'America/Fort_Nelson',0),(113,'America/Fort_Wayne',0),(114,'America/Fortaleza',0),(115,'America/Glace_Bay',0),(116,'America/Godthab',0),(117,'America/Goose_Bay',0),(118,'America/Grand_Turk',0),(119,'America/Grenada',0),(120,'America/Guadeloupe',0),(121,'America/Guatemala',0),(122,'America/Guayaquil',0),(123,'America/Guyana',0),(124,'America/Halifax',0),(125,'America/Havana',0),(126,'America/Hermosillo',0),(127,'America/Indiana/Indianapolis',0),(128,'America/Indiana/Knox',0),(129,'America/Indiana/Marengo',0),(130,'America/Indiana/Petersburg',0),(131,'America/Indiana/Tell_City',0),(132,'America/Indiana/Vevay',0),(133,'America/Indiana/Vincennes',0),(134,'America/Indiana/Winamac',0),(135,'America/Indianapolis',0),(136,'America/Inuvik',0),(137,'America/Iqaluit',0),(138,'America/Jamaica',0),(139,'America/Jujuy',0),(140,'America/Juneau',0),(141,'America/Kentucky/Louisville',0),(142,'America/Kentucky/Monticello',0),(143,'America/Knox_IN',0),(144,'America/Kralendijk',0),(145,'America/La_Paz',0),(146,'America/Lima',0),(147,'America/Los_Angeles',0),(148,'America/Louisville',0),(149,'America/Lower_Princes',0),(150,'America/Maceio',0),(151,'America/Managua',0),(152,'America/Manaus',0),(153,'America/Marigot',0),(154,'America/Martinique',0),(155,'America/Matamoros',0),(156,'America/Mazatlan',0),(157,'America/Mendoza',0),(158,'America/Menominee',0),(159,'America/Merida',0),(160,'America/Metlakatla',0),(161,'America/Mexico_City',0),(162,'America/Miquelon',0),(163,'America/Moncton',0),(164,'America/Monterrey',0),(165,'America/Montevideo',0),(166,'America/Montreal',0),(167,'America/Montserrat',0),(168,'America/Nassau',0),(169,'America/New_York',0),(170,'America/Nipigon',0),(171,'America/Nome',0),(172,'America/Noronha',0),(173,'America/North_Dakota/Beulah',0),(174,'America/North_Dakota/Center',0),(175,'America/North_Dakota/New_Salem',0),(176,'America/Nuuk',0),(177,'America/Ojinaga',0),(178,'America/Panama',0),(179,'America/Pangnirtung',0),(180,'America/Paramaribo',0),(181,'America/Phoenix',0),(182,'America/Port-au-Prince',0),(183,'America/Port_of_Spain',0),(184,'America/Porto_Acre',0),(185,'America/Porto_Velho',0),(186,'America/Puerto_Rico',0),(187,'America/Punta_Arenas',0),(188,'America/Rainy_River',0),(189,'America/Rankin_Inlet',0),(190,'America/Recife',0),(191,'America/Regina',0),(192,'America/Resolute',0),(193,'America/Rio_Branco',0),(194,'America/Rosario',0),(195,'America/Santa_Isabel',0),(196,'America/Santarem',0),(197,'America/Santiago',0),(198,'America/Santo_Domingo',0),(199,'America/Sao_Paulo',0),(200,'America/Scoresbysund',0),(201,'America/Shiprock',0),(202,'America/Sitka',0),(203,'America/St_Barthelemy',0),(204,'America/St_Johns',0),(205,'America/St_Kitts',0),(206,'America/St_Lucia',0),(207,'America/St_Thomas',0),(208,'America/St_Vincent',0),(209,'America/Swift_Current',0),(210,'America/Tegucigalpa',0),(211,'America/Thule',0),(212,'America/Thunder_Bay',0),(213,'America/Tijuana',0),(214,'America/Toronto',0),(215,'America/Tortola',0),(216,'America/Vancouver',0),(217,'America/Virgin',0),(218,'America/Whitehorse',0),(219,'America/Winnipeg',0),(220,'America/Yakutat',0),(221,'America/Yellowknife',0),(222,'Antarctica/Casey',0),(223,'Antarctica/Davis',0),(224,'Antarctica/DumontDUrville',0),(225,'Antarctica/Macquarie',0),(226,'Antarctica/Mawson',0),(227,'Antarctica/McMurdo',0),(228,'Antarctica/Palmer',0),(229,'Antarctica/Rothera',0),(230,'Antarctica/South_Pole',0),(231,'Antarctica/Syowa',0),(232,'Antarctica/Troll',0),(233,'Antarctica/Vostok',0),(234,'Arctic/Longyearbyen',0),(235,'Asia/Aden',0),(236,'Asia/Almaty',0),(237,'Asia/Amman',0),(238,'Asia/Anadyr',0),(239,'Asia/Aqtau',0),(240,'Asia/Aqtobe',0),(241,'Asia/Ashgabat',0),(242,'Asia/Ashkhabad',0),(243,'Asia/Atyrau',0),(244,'Asia/Baghdad',0),(245,'Asia/Bahrain',0),(246,'Asia/Baku',0),(247,'Asia/Bangkok',0),(248,'Asia/Barnaul',0),(249,'Asia/Beirut',0),(250,'Asia/Bishkek',0),(251,'Asia/Brunei',0),(252,'Asia/Calcutta',0),(253,'Asia/Chita',0),(254,'Asia/Choibalsan',0),(255,'Asia/Chongqing',0),(256,'Asia/Chungking',0),(257,'Asia/Colombo',0),(258,'Asia/Dacca',0),(259,'Asia/Damascus',0),(260,'Asia/Dhaka',0),(261,'Asia/Dili',0),(262,'Asia/Dubai',0),(263,'Asia/Dushanbe',0),(264,'Asia/Famagusta',0),(265,'Asia/Gaza',0),(266,'Asia/Harbin',0),(267,'Asia/Hebron',0),(268,'Asia/Ho_Chi_Minh',0),(269,'Asia/Hong_Kong',0),(270,'Asia/Hovd',0),(271,'Asia/Irkutsk',0),(272,'Asia/Istanbul',0),(273,'Asia/Jakarta',0),(274,'Asia/Jayapura',0),(275,'Asia/Jerusalem',0),(276,'Asia/Kabul',0),(277,'Asia/Kamchatka',0),(278,'Asia/Karachi',0),(279,'Asia/Kashgar',0),(280,'Asia/Kathmandu',0),(281,'Asia/Katmandu',0),(282,'Asia/Khandyga',0),(283,'Asia/Kolkata',0),(284,'Asia/Krasnoyarsk',0),(285,'Asia/Kuala_Lumpur',0),(286,'Asia/Kuching',0),(287,'Asia/Kuwait',0),(288,'Asia/Macao',0),(289,'Asia/Macau',0),(290,'Asia/Magadan',0),(291,'Asia/Makassar',0),(292,'Asia/Manila',0),(293,'Asia/Muscat',0),(294,'Asia/Nicosia',0),(295,'Asia/Novokuznetsk',0),(296,'Asia/Novosibirsk',0),(297,'Asia/Omsk',0),(298,'Asia/Oral',0),(299,'Asia/Phnom_Penh',0),(300,'Asia/Pontianak',0),(301,'Asia/Pyongyang',0),(302,'Asia/Qatar',0),(303,'Asia/Qostanay',0),(304,'Asia/Qyzylorda',0),(305,'Asia/Rangoon',0),(306,'Asia/Riyadh',0),(307,'Asia/Saigon',0),(308,'Asia/Sakhalin',0),(309,'Asia/Samarkand',0),(310,'Asia/Seoul',0),(311,'Asia/Shanghai',0),(312,'Asia/Singapore',0),(313,'Asia/Srednekolymsk',0),(314,'Asia/Taipei',0),(315,'Asia/Tashkent',0),(316,'Asia/Tbilisi',0),(317,'Asia/Tehran',0),(318,'Asia/Tel_Aviv',0),(319,'Asia/Thimbu',0),(320,'Asia/Thimphu',0),(321,'Asia/Tokyo',0),(322,'Asia/Tomsk',0),(323,'Asia/Ujung_Pandang',0),(324,'Asia/Ulaanbaatar',0),(325,'Asia/Ulan_Bator',0),(326,'Asia/Urumqi',0),(327,'Asia/Ust-Nera',0),(328,'Asia/Vientiane',0),(329,'Asia/Vladivostok',0),(330,'Asia/Yakutsk',0),(331,'Asia/Yangon',0),(332,'Asia/Yekaterinburg',0),(333,'Asia/Yerevan',0),(334,'Atlantic/Azores',0),(335,'Atlantic/Bermuda',0),(336,'Atlantic/Canary',0),(337,'Atlantic/Cape_Verde',0),(338,'Atlantic/Faeroe',0),(339,'Atlantic/Faroe',0),(340,'Atlantic/Jan_Mayen',0),(341,'Atlantic/Madeira',0),(342,'Atlantic/Reykjavik',0),(343,'Atlantic/South_Georgia',0),(344,'Atlantic/St_Helena',0),(345,'Atlantic/Stanley',0),(346,'Australia/ACT',0),(347,'Australia/Adelaide',0),(348,'Australia/Brisbane',0),(349,'Australia/Broken_Hill',0),(350,'Australia/Canberra',0),(351,'Australia/Currie',0),(352,'Australia/Darwin',0),(353,'Australia/Eucla',0),(354,'Australia/Hobart',0),(355,'Australia/LHI',0),(356,'Australia/Lindeman',0),(357,'Australia/Lord_Howe',0),(358,'Australia/Melbourne',0),(359,'Australia/NSW',0),(360,'Australia/North',0),(361,'Australia/Perth',0),(362,'Australia/Queensland',0),(363,'Australia/South',0),(364,'Australia/Sydney',0),(365,'Australia/Tasmania',0),(366,'Australia/Victoria',0),(367,'Australia/West',0),(368,'Australia/Yancowinna',0),(369,'Brazil/Acre',0),(370,'Brazil/DeNoronha',0),(371,'Brazil/East',0),(372,'Brazil/West',0),(373,'Canada/Atlantic',0),(374,'Canada/Central',0),(375,'Canada/Eastern',0),(376,'Canada/Mountain',0),(377,'Canada/Newfoundland',0),(378,'Canada/Pacific',0),(379,'Canada/Saskatchewan',0),(380,'Canada/Yukon',0),(381,'Chile/Continental',0),(382,'Chile/EasterIsland',0),(383,'Europe/Amsterdam',0),(384,'Europe/Andorra',0),(385,'Europe/Astrakhan',0),(386,'Europe/Athens',0),(387,'Europe/Belfast',0),(388,'Europe/Belgrade',0),(389,'Europe/Berlin',0),(390,'Europe/Bratislava',0),(391,'Europe/Brussels',0),(392,'Europe/Bucharest',0),(393,'Europe/Budapest',0),(394,'Europe/Busingen',0),(395,'Europe/Chisinau',0),(396,'Europe/Copenhagen',0),(397,'Europe/Dublin',0),(398,'Europe/Gibraltar',0),(399,'Europe/Guernsey',0),(400,'Europe/Helsinki',0),(401,'Europe/Isle_of_Man',0),(402,'Europe/Istanbul',0),(403,'Europe/Jersey',0),(404,'Europe/Kaliningrad',0),(405,'Europe/Kiev',0),(406,'Europe/Kirov',0),(407,'Europe/Lisbon',0),(408,'Europe/Ljubljana',0),(409,'Europe/London',0),(410,'Europe/Luxembourg',0),(411,'Europe/Madrid',0),(412,'Europe/Malta',0),(413,'Europe/Mariehamn',0),(414,'Europe/Minsk',0),(415,'Europe/Monaco',0),(416,'Europe/Moscow',0),(417,'Europe/Nicosia',0),(418,'Europe/Oslo',0),(419,'Europe/Paris',0),(420,'Europe/Podgorica',0),(421,'Europe/Prague',0),(422,'Europe/Riga',0),(423,'Europe/Rome',0),(424,'Europe/Samara',0),(425,'Europe/San_Marino',0),(426,'Europe/Sarajevo',0),(427,'Europe/Saratov',0),(428,'Europe/Simferopol',0),(429,'Europe/Skopje',0),(430,'Europe/Sofia',0),(431,'Europe/Stockholm',0),(432,'Europe/Tallinn',0),(433,'Europe/Tirane',0),(434,'Europe/Tiraspol',0),(435,'Europe/Ulyanovsk',0),(436,'Europe/Uzhgorod',0),(437,'Europe/Vaduz',0),(438,'Europe/Vatican',0),(439,'Europe/Vienna',0),(440,'Europe/Vilnius',0),(441,'Europe/Volgograd',0),(442,'Europe/Warsaw',0),(443,'Europe/Zagreb',0),(444,'Europe/Zaporozhye',0),(445,'Europe/Zurich',0),(446,'Indian/Antananarivo',0),(447,'Indian/Chagos',0),(448,'Indian/Christmas',0),(449,'Indian/Cocos',0),(450,'Indian/Comoro',0),(451,'Indian/Kerguelen',0),(452,'Indian/Mahe',0),(453,'Indian/Maldives',0),(454,'Indian/Mauritius',0),(455,'Indian/Mayotte',0),(456,'Indian/Reunion',0),(457,'Mexico/BajaNorte',0),(458,'Mexico/BajaSur',0),(459,'Mexico/General',0),(460,'Pacific/Apia',0),(461,'Pacific/Auckland',0),(462,'Pacific/Bougainville',0),(463,'Pacific/Chatham',0),(464,'Pacific/Chuuk',0),(465,'Pacific/Easter',0),(466,'Pacific/Efate',0),(467,'Pacific/Enderbury',0),(468,'Pacific/Fakaofo',0),(469,'Pacific/Fiji',0),(470,'Pacific/Funafuti',0),(471,'Pacific/Galapagos',0),(472,'Pacific/Gambier',0),(473,'Pacific/Guadalcanal',0),(474,'Pacific/Guam',0),(475,'Pacific/Honolulu',0),(476,'Pacific/Johnston',0),(477,'Pacific/Kiritimati',0),(478,'Pacific/Kosrae',0),(479,'Pacific/Kwajalein',0),(480,'Pacific/Majuro',0),(481,'Pacific/Marquesas',0),(482,'Pacific/Midway',0),(483,'Pacific/Nauru',0),(484,'Pacific/Niue',0),(485,'Pacific/Norfolk',0),(486,'Pacific/Noumea',0),(487,'Pacific/Pago_Pago',0),(488,'Pacific/Palau',0),(489,'Pacific/Pitcairn',0),(490,'Pacific/Pohnpei',0),(491,'Pacific/Ponape',0),(492,'Pacific/Port_Moresby',0),(493,'Pacific/Rarotonga',0),(494,'Pacific/Saipan',0),(495,'Pacific/Samoa',0),(496,'Pacific/Tahiti',0),(497,'Pacific/Tarawa',0),(498,'Pacific/Tongatapu',0),(499,'Pacific/Truk',0),(500,'Pacific/Wake',0),(501,'Pacific/Wallis',0),(502,'Pacific/Yap',0),(503,'US/Alaska',0),(504,'US/Aleutian',0),(505,'US/Arizona',0),(506,'US/Central',0),(507,'US/East-Indiana',0),(508,'US/Eastern',0),(509,'US/Hawaii',0),(510,'US/Indiana-Starke',0),(511,'US/Michigan',0),(512,'US/Mountain',0),(513,'US/Pacific',0),(514,'US/Samoa',0),(515,'UTC',0);
UNLOCK TABLES;

--
-- Dumping data for table `lams_tool_session_state`
--

LOCK TABLES `lams_tool_session_state` WRITE;
INSERT INTO `lams_tool_session_state` VALUES (1,'STARTED'),(2,'ENDED');
UNLOCK TABLES;

--
-- Dumping data for table `lams_tool_session_type`
--

LOCK TABLES `lams_tool_session_type` WRITE;
INSERT INTO `lams_tool_session_type` VALUES (1,'NON_GROUPED'),(2,'GROUPED');
UNLOCK TABLES;



--
-- Dumping data for table `lams_workspace_folder_type`
--

LOCK TABLES `lams_workspace_folder_type` WRITE;
INSERT INTO `lams_workspace_folder_type` VALUES (1,'NORMAL'),(2,'RUN SEQUENCES'),(3,'PUBLIC SEQUENCES');
UNLOCK TABLES;

--
-- Dumping data for table `patches`
--

LOCK TABLES `patches` WRITE;
INSERT INTO patches VALUES ('lams', 20190103, NOW(), 'F');
UNLOCK TABLES;

SET FOREIGN_KEY_CHECKS=1;
