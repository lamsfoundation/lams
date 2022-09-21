INSERT INTO lams_auth_method_type VALUES
(1,'LAMS'),
(2,'WEB_AUTH'),
(3,'LDAP');

INSERT INTO lams_authentication_method VALUES
(1,1,'LAMS-Database'),
(3,3,'MQ-LDAP');

INSERT INTO lams_copy_type VALUES
(1,'NONE'),
(2,'LESSON'),
(3,'PREVIEW');

INSERT INTO lams_ext_server_type VALUES
(1,'INTEGRATED SERVER'),
(2,'LTI TOOL CONSUMER');

INSERT INTO lams_ext_server_org_map VALUES
(1,'moodle','moodle','moodle','moodle','mdl','http://localhost/moodle/mod/lamstwo/userinfo.php?ts=%timestamp%&un=%username%&hs=%hash%','',NULL,0x01,80,0,NULL,1,NULL,0,0,1,1,0,0,1,NULL,NULL);

INSERT INTO lams_gate_activity_level VALUES
(1,'LEARNER'),
(2,'GROUP'),
(3,'CLASS');

INSERT INTO lams_grouping_support_type VALUES
(1,'NONE'),
(2,'OPTIONAL'),
(3,'REQUIRED');

INSERT INTO lams_grouping_type VALUES
(1,'RANDOM_GROUPING'),
(2,'CHOSEN_GROUPING'),
(3,'CLASS_GROUPING'),
(4,'LEARNER_CHOICE_GROUPING');

INSERT INTO lams_learning_activity_type VALUES
(1,'TOOL'),
(2,'GROUPING'),
(3,'GATE_SYNCH'),
(4,'GATE_SCHEDULE'),
(5,'GATE_PERMISSION'),
(6,'PARALLEL'),
(7,'OPTIONS'),
(8,'SEQUENCE'),
(9,'GATE_SYSTEM'),
(10,'BRANCHING_CHOSEN'),
(11,'BRANCHING_GROUP'),
(12,'BRANCHING_TOOL'),
(13,'OPTIONS_WITH_SEQUENCES'),
(14,'GATE_CONDITION'),
(15,'FLOATING'),
(16,'GATE_PASSWORD');

INSERT INTO lams_lesson_state VALUES
(1,'CREATED'),
(2,'NOT_STARTED'),
(3,'STARTED'),
(4,'SUSPENDED'),
(5,'FINISHED'),
(6,'ARCHIVED'),
(7,'REMOVED');

INSERT INTO lams_license VALUES
(1,'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 4.0','by-nc-sa','https://creativecommons.org/licenses/by-nc-sa/4.0/',1,'/images/license/by-nc-sa.eu.svg',1),
(2,'CC Attribution-No Derivatives 4.0','by-nd','https://creativecommons.org/licenses/by-nd/4.0/',0,'/images/license/by-nd.svg',2),
(3,'CC Attribution-Noncommercial-No Derivatives 4.0','by-nc-nd','https://creativecommons.org/licenses/by-nc-nd/4.0/',0,'/images/license/by-nc-nd.svg',3),
(4,'CC Attribution-Noncommercial 4.0','by-nc','https://creativecommons.org/licenses/by-nc/4.0/',0,'/images/license/by-nc.eu.svg',4),
(5,'CC Attribution-ShareAlike 4.0','by-sa','https://creativecommons.org/licenses/by-sa/4.0/',0,'/images/license/by-sa.svg',5),
(6,'Other Licensing Agreement','other','',0,'',8),
(7,'CC Attribution 4.0','by','https://creativecommons.org/licenses/by/4.0/',0,'/images/license/by.svg',6),
(8,'Public Domain','CC0','https://creativecommons.org/publicdomain/zero/1.0/',0,'/images/license/publicdomain.svg',7);

INSERT INTO lams_log_event_type VALUES
(1,'TYPE_TEACHER_LEARNING_DESIGN_CREATE','LESSON'),
(2,'TYPE_TEACHER_LESSON_CREATE','LESSON'),
(3,'TYPE_TEACHER_LESSON_START','LESSON'),
(4,'TYPE_TEACHER_LESSON_CHANGE_STATE','LESSON'),
(5,'TYPE_LEARNER_ACTIVITY_START','LESSON'),
(6,'TYPE_LEARNER_ACTIVITY_FINISH','LESSON'),
(7,'TYPE_LEARNER_LESSON_COMPLETE','LESSON'),
(8,'TYPE_LEARNER_LESSON_MARK_SUBMIT','LESSON'),
(9,'TYPE_ACTIVITY_EDIT','LESSON'),
(10,'TYPE_FORCE_COMPLETE','LESSON'),
(11,'TYPE_USER_ORG_ADMIN','SECURITY'),
(12,'TYPE_LOGIN_AS','SECURITY'),
(13,'TYPE_PASSWORD_CHANGE','SECURITY'),
(14,'TYPE_ROLE_FAILURE','SECURITY'),
(15,'TYPE_ACCOUNT_LOCKED','SECURITY'),
(16,'TYPE_NOTIFICATION','NOTIFICATION'),
(17,'TYPE_MARK_UPDATED','MARKS'),
(18,'TYPE_MARK_RELEASED','MARKS'),
(19,'TYPE_LEARNER_CONTENT_UPDATED','LEARNER_CONTENT'),
(20,'TYPE_LEARNER_CONTENT_SHOW_HIDE','LEARNER_CONTENT'),
(21,'TYPE_UNKNOWN','UNKNOWN'),
(22,'TYPE_LIVE_EDIT','LESSON'),
(23,'TYPE_TOOL_MARK_RELEASED','MARKS'),
(24,'TYPE_LOGIN','SECURITY'),
(25,'TYPE_LOGOUT','SECURITY'),
(26,'TYPE_CONFIG_CHANGE','SECURITY'),
(27,'TYPE_QUESTIONS_MERGED','QUESTION_BANK');

INSERT INTO lams_organisation_state VALUES
(1,'ACTIVE'),
(2,'HIDDEN'),
(3,'ARCHIVED'),
(4,'REMOVED');

INSERT INTO lams_organisation_type VALUES
(1,'ROOT ORGANISATION','root all other organisations: controlled by Sysadmin'),
(2,'COURSE ORGANISATION','main organisation level - equivalent to an entire course.'),
(3,'CLASS','runtime organisation level - lessons are run for classes.');

INSERT INTO lams_outcome_scale VALUES
(1,'Default attainment scale','default','Default global scale',1,'2022-09-20 11:38:25');

INSERT INTO lams_outcome_scale_item VALUES
(1,1,0,'Not yet attained'),
(2,1,1,'Attained');

INSERT INTO lams_policy_state VALUES
(1,'ACTIVE'),
(2,'INACTIVE');

INSERT INTO lams_policy_type VALUES
(1,'TYPE_SITE_POLICY'),
(2,'TYPE_PRIVACY_POLICY'),
(3,'TYPE_THIRD_PARTIES_POLICY'),
(4,'TYPE_OTHER');

INSERT INTO lams_qb_question VALUES
(1,0x71B92B1038C811ED8F4600D8618629BB,1,1,1,'2022-09-20 11:39:19','93b97f99-a9ad-471b-a71a-5cc58ab2196e','A Sample question?','A Sample question?',1,NULL,0,0,0,NULL,NULL,NULL,0,0,0,0,0,0,0,NULL,0,0),
(2,0x71EFEAD338C811ED8F4600D8618629BB,1,2,1,'2022-09-20 11:40:08','93b97f99-a9ad-471b-a71a-5cc58ab2196e','Question Title','Question Description',NULL,NULL,0,0,0,NULL,NULL,NULL,0,0,0,0,0,0,0,NULL,0,0),
(3,0x72E874A938C811ED8F4600D8618629BB,6,3,1,'2022-09-20 11:39:26','93b97f99-a9ad-471b-a71a-5cc58ab2196e','Sample Question 1?','Sample Question 1?',1,NULL,0,0,0,NULL,NULL,NULL,0,0,0,0,0,0,0,NULL,0,0);

INSERT INTO lams_qb_option VALUES
(1,1,1,'Answer 1',NULL,0,0,0,NULL),
(2,1,2,'Answer 2',NULL,0,1,0,NULL),
(4,2,1,'Question Answer A',NULL,0,1,0,NULL),
(5,2,2,'Question Answer B',NULL,0,0,0,NULL),
(6,2,3,'Question Answer C',NULL,0,0,0,NULL),
(7,2,4,'Question Answer D',NULL,0,0,0,NULL);

INSERT INTO lams_qb_collection VALUES
(1,'Public questions',NULL,0);

INSERT INTO lams_qb_collection_question VALUES
(1,1),
(1,2),
(1,3);

INSERT INTO lams_rating_criteria_type VALUES
(1,'TOOL_ACTIVITY'),
(2,'AUTHORED_ITEM'),
(3,'LEARNER_ITEM'),
(4,'LESSON');

INSERT INTO lams_role VALUES
(1,'SYSADMIN','LAMS System Adminstrator','2022-09-20 11:38:25'),
(2,'GROUP MANAGER','Group Manager','2022-09-20 11:38:25'),
(3,'AUTHOR','Authors Learning Designs','2022-09-20 11:38:25'),
(4,'MONITOR','Member of Staff','2022-09-20 11:38:25'),
(5,'LEARNER','Student','2022-09-20 11:38:25');

INSERT INTO lams_sequence_generator VALUES (3);

INSERT INTO lams_supported_locale VALUES
(1,'en','AU','English (Australia)','LTR','en-au'),
(2,'es','ES','Español','LTR','es'),
(3,'mi','NZ','Māori','LTR','en-au'),
(4,'de','DE','Deutsch','LTR','de'),
(5,'zh','CN','简体中文','LTR','zh-cn'),
(6,'fr','FR','Français','LTR','fr'),
(7,'it','IT','Italiano','LTR','it'),
(8,'no','NO','Norsk','LTR','no'),
(9,'sv','SE','Svenska','LTR','sv'),
(10,'ko','KR','한국어','LTR','ko'),
(11,'pl','PL','Polski','LTR','pl'),
(12,'pt','BR','Português (Brasil)','LTR','pt-br'),
(13,'hu','HU','Magyar','LTR','hu'),
(14,'bg','BG','Български','LTR','bg'),
(15,'cy','GB','Cymraeg (Cymru)','LTR','cy'),
(16,'th','TH','Thai','LTR','th'),
(17,'el','GR','Ελληνικά','LTR','el'),
(18,'nl','BE','Nederlands (België)','LTR','nl'),
(19,'ar','JO','عربي','RTL','ar'),
(20,'da','DK','Dansk','LTR','da'),
(21,'ru','RU','Русский','LTR','ru'),
(22,'vi','VN','Tiếng Việt','LTR','vi'),
(23,'zh','TW','Chinese (Taiwan)','LTR','zh'),
(24,'ja','JP','日本語','LTR','ja'),
(25,'ms','MY','Malay (Malaysia)','LTR','ms'),
(26,'tr','TR','Türkçe','LTR','tr'),
(27,'ca','ES','Català','LTR','ca'),
(28,'sl','SI','Slovenščina','LTR','sl'),
(29,'es','MX','Español (México)','LTR','es'),
(30,'cs','CZ','Čeština','LTR','cs'),
(31,'id','ID','Indonesian','LTR','id');

INSERT INTO lams_system_tool VALUES
(1,2,'Grouping','All types of grouping including random and chosen.','learning/grouping/performGrouping.do','learning/grouping/performGrouping.do','learning/grouping/viewGrouping.do?mode=teacher','monitoring/grouping/startGrouping.do','monitoring/grouping/startGrouping.do',NULL,'2022-09-20 11:38:25',NULL),
(2,3,'Sync Gate','Gate: Synchronise Learners.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL),
(3,4,'Schedule Gate','Gate: Opens/shuts at particular times.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL),
(4,5,'Permission Gate','Gate: Opens under teacher or staff control.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL),
(5,9,'System Gate','Gate: Opens under system control.','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL),
(6,10,'Monitor Chosen Branching','Select between multiple sequence activities, with the branch chosen in monitoring.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/chosenBranching.do?method=assignBranch','monitoring/grouping/startGrouping.do',NULL,'2022-09-20 11:38:25',NULL),
(7,11,'Group Based Branching','Select between multiple sequence activities, with the branch chosen by an existing group.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/groupedBranching.do?method=viewBranching','monitoring/groupedBranching.do?method=assignBranch',NULL,'2022-09-20 11:38:25',NULL),
(8,12,'Tool Output Based Branching','Select between multiple sequence activities, with the branch chosen on results of another activity.','learning/branching/performBranching.do','learning/branching/performBranching.do','monitoring/complexProgress.do','monitoring/toolBranching.do?method=viewBranching','monitoring/toolBranching.do?method=viewBranching',NULL,'2022-09-20 11:38:25',NULL),
(9,8,'Sequence','A sequence of activities','learning/SequenceActivity.do','learning/SequenceActivity.do','monitoring/complexProgress.do','monitoring/sequence/viewSequence.do','monitoring/sequence/viewSequence.do',NULL,'2022-09-20 11:38:25',NULL),
(10,14,'Condition Gate','Gate: Opens if conditions are met','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL),
(11,15,'Floating Activities','A collection of floating activities',NULL,NULL,NULL,'monitoring/floating.do?method=viewFloating','monitoring/floating.do?method=viewFloating',NULL,'2022-09-20 11:38:25',NULL),
(12,16,'Password Gate','Gate: Opens if learner provides correct password','learning/gate/knockGate.do','learning/gate/knockGate.do',NULL,'monitoring/gate/viewGate.do','monitoring/gate/viewGate.do',NULL,'2022-09-20 11:38:25',NULL);


INSERT INTO lams_timezone VALUES
(1,'Etc/GMT+14',0),
(2,'Etc/GMT+13',0),
(3,'Etc/GMT+12',0),
(4,'Etc/GMT+11',0),
(5,'Etc/GMT+10',0),
(6,'Etc/GMT+9',0),
(7,'Etc/GMT+8',0),
(8,'Etc/GMT+7',0),
(9,'Etc/GMT+6',0),
(10,'Etc/GMT+5',0),
(11,'Etc/GMT+4',0),
(12,'Etc/GMT+3',0),
(13,'Etc/GMT+2',0),
(14,'Etc/GMT+1',0),
(15,'Etc/GMT',0),
(16,'Etc/GMT-1',0),
(17,'Etc/GMT-2',0),
(18,'Etc/GMT-3',0),
(19,'Etc/GMT-4',0),
(20,'Etc/GMT-5',0),
(21,'Etc/GMT-6',0),
(22,'Etc/GMT-7',0),
(23,'Etc/GMT-8',0),
(24,'Etc/GMT-9',0),
(25,'Etc/GMT-10',0),
(26,'Etc/GMT-11',0),
(27,'Etc/GMT-12',0);

INSERT INTO lams_tool_session_state VALUES
(1,'STARTED'),
(2,'ENDED');

INSERT INTO lams_tool_session_type VALUES
(1,'NON_GROUPED'),
(2,'GROUPED');

INSERT INTO lams_workspace_folder_type VALUES
(1,'NORMAL'),
(2,'RUN SEQUENCES'),
(3,'PUBLIC SEQUENCES');

INSERT INTO patches VALUES
('lams', 20211025, NOW(), 'F');