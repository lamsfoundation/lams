INSERT INTO lams_privilege VALUES (1,'Z','do anything');
INSERT INTO lams_privilege VALUES (2,'A','add/remove/modify classes within the course');
INSERT INTO lams_privilege VALUES (3,'B','create running instances of sequences and assign those to a class');
INSERT INTO lams_privilege VALUES (4,'C','stop/start running sequences');
INSERT INTO lams_privilege VALUES (5,'D','monitor the progress of learners');
INSERT INTO lams_privilege VALUES (6,'E','participates in sequences');
INSERT INTO lams_privilege VALUES (7,'F','export their progress on each running sequence');
INSERT INTO lams_privilege VALUES (8,'G','write/create/delete permissions in course content folder');
INSERT INTO lams_privilege VALUES (9,'H','read course content folder');
INSERT INTO lams_privilege VALUES (10,'I','create new users');
INSERT INTO lams_privilege VALUES (11,'J','create guest users');
INSERT INTO lams_privilege VALUES (12,'K','change status of course');
INSERT INTO lams_privilege VALUES (13,'L','browse all users in the system');
INSERT INTO lams_privilege VALUES (14,'M','write/create/delete permissions in all course content folders');

INSERT INTO lams_role VALUES (1, 'SYSADMIN', 'LAMS System Adminstrator', NOW());
INSERT INTO lams_role VALUES (2, 'GROUP MANAGER', 'Group Manager', NOW());
INSERT INTO lams_role VALUES (3, 'AUTHOR', 'Authors Learning Designs', NOW());
INSERT INTO lams_role VALUES (4, 'MONITOR', 'Member of Staff', NOW());
INSERT INTO lams_role VALUES (5, 'LEARNER', 'Student', NOW());
INSERT INTO lams_role VALUES (6, 'GROUP ADMIN', 'Group Administrator', NOW());

INSERT INTO lams_role_privilege VALUES (NULL,1,1);
INSERT INTO lams_role_privilege VALUES (NULL,2,2);
INSERT INTO lams_role_privilege VALUES (NULL,2,3);
INSERT INTO lams_role_privilege VALUES (NULL,2,4);
INSERT INTO lams_role_privilege VALUES (NULL,2,5);
INSERT INTO lams_role_privilege VALUES (NULL,2,8);
INSERT INTO lams_role_privilege VALUES (NULL,2,9);
INSERT INTO lams_role_privilege VALUES (NULL,2,10);
INSERT INTO lams_role_privilege VALUES (NULL,2,12);
INSERT INTO lams_role_privilege VALUES (NULL,2,13);
INSERT INTO lams_role_privilege VALUES (NULL,3,8);
INSERT INTO lams_role_privilege VALUES (NULL,3,9);
INSERT INTO lams_role_privilege VALUES (NULL,4,3);
INSERT INTO lams_role_privilege VALUES (NULL,4,4);
INSERT INTO lams_role_privilege VALUES (NULL,4,5);
INSERT INTO lams_role_privilege VALUES (NULL,5,6);
INSERT INTO lams_role_privilege VALUES (NULL,5,7);
INSERT INTO lams_role_privilege VALUES (NULL,6,2);
INSERT INTO lams_role_privilege VALUES (NULL,6,10);
INSERT INTO lams_role_privilege VALUES (NULL,6,12);
INSERT INTO lams_role_privilege VALUES (NULL,6,13);


INSERT INTO lams_auth_method_type VALUES(1, 'LAMS');
INSERT INTO lams_auth_method_type VALUES(2, 'WEB_AUTH');
INSERT INTO lams_auth_method_type VALUES(3, 'LDAP');

INSERT INTO lams_organisation_type VALUES(1, 'ROOT ORGANISATION', 'root all other organisations: controlled by Sysadmin');
INSERT INTO lams_organisation_type VALUES(2, 'COURSE ORGANISATION', 'main organisation level - equivalent to an entire course.');
INSERT INTO lams_organisation_type VALUES(3, 'CLASS', 'runtime organisation level - lessons are run for classes.');

INSERT INTO lams_organisation_state VALUES (1, 'ACTIVE');
INSERT INTO lams_organisation_state VALUES (2, 'HIDDEN');
INSERT INTO lams_organisation_state VALUES (3, 'ARCHIVED');
INSERT INTO lams_organisation_state VALUES (4, 'REMOVED');

INSERT INTO lams_grouping_type VALUES (1, 'RANDOM_GROUPING');
INSERT INTO lams_grouping_type VALUES (2, 'CHOSEN_GROUPING');
INSERT INTO lams_grouping_type VALUES (3, 'CLASS_GROUPING');
INSERT INTO lams_grouping_type VALUES (4, 'LEARNER_CHOICE_GROUPING');

INSERT INTO lams_tool_session_type VALUES (1, 'NON_GROUPED');
INSERT INTO lams_tool_session_type VALUES (2, 'GROUPED');

INSERT INTO lams_learning_activity_type VALUES (1, 'TOOL');
INSERT INTO lams_learning_activity_type VALUES (2, 'GROUPING');
INSERT INTO lams_learning_activity_type VALUES (3, 'GATE_SYNCH');
INSERT INTO lams_learning_activity_type VALUES (4, 'GATE_SCHEDULE');
INSERT INTO lams_learning_activity_type VALUES (5, 'GATE_PERMISSION');
INSERT INTO lams_learning_activity_type VALUES (6, 'PARALLEL');
INSERT INTO lams_learning_activity_type VALUES (7, 'OPTIONS');
INSERT INTO lams_learning_activity_type VALUES (8, 'SEQUENCE');
INSERT INTO lams_learning_activity_type VALUES (9, 'GATE_SYSTEM');
INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCHING_CHOSEN');
INSERT INTO lams_learning_activity_type VALUES (11, 'BRANCHING_GROUP');
INSERT INTO lams_learning_activity_type VALUES (12, 'BRANCHING_TOOL');
INSERT INTO lams_learning_activity_type VALUES (13, 'OPTIONS_WITH_SEQUENCES');
INSERT INTO lams_learning_activity_type VALUES (14, 'GATE_CONDITION');
INSERT INTO lams_learning_activity_type VALUES (15, 'FLOATING');

INSERT INTO lams_gate_activity_level VALUES (1, 'LEARNER');
INSERT INTO lams_gate_activity_level VALUES (2, 'GROUP');
INSERT INTO lams_gate_activity_level VALUES (3, 'CLASS');

INSERT INTO lams_tool_session_state VALUES  (1, 'STARTED');
INSERT INTO lams_tool_session_state VALUES  (2, 'ENDED');

INSERT INTO lams_lesson_state VALUES (1, 'CREATED');
INSERT INTO lams_lesson_state VALUES (2, 'NOT_STARTED');
INSERT INTO lams_lesson_state VALUES (3, 'STARTED');
INSERT INTO lams_lesson_state VALUES (4, 'SUSPENDED');
INSERT INTO lams_lesson_state VALUES (5, 'FINISHED');
INSERT INTO lams_lesson_state VALUES (6, 'ARCHIVED');
INSERT INTO lams_lesson_state VALUES (7, 'REMOVED');

INSERT into lams_license VALUES (1, 'LAMS Recommended: CC Attribution-Noncommercial-ShareAlike 2.5', 'by-nc-sa', 'http://creativecommons.org/licenses/by-nc-sa/2.5/', 1, '/images/license/byncsa.jpg');
INSERT into lams_license VALUES (2, 'CC Attribution-No Derivatives 2.5', 'by-nd', 'http://creativecommons.org/licenses/by-nd/2.5/',0,'/images/license/bynd.jpg');
INSERT into lams_license VALUES (3, 'CC Attribution-Noncommercial-No Derivatives 2.5', 'by-nc-nd', 'http://creativecommons.org/licenses/by-nc-nd/2.5/',0, '/images/license/byncnd.jpg');
INSERT into lams_license VALUES (4, 'CC Attribution-Noncommercial 2.5', 'by-nc', 'http://creativecommons.org/licenses/by-nc/2.5/',0,'/images/license/bync.jpg');
INSERT into lams_license VALUES (5, 'CC Attribution-ShareAlike 2.5', 'by-sa', 'http://creativecommons.org/licenses/by-sa/2.5/',0,'/images/license/byncsa.jpg'); 
INSERT into lams_license VALUES (6, 'Other Licensing Agreement', 'other', '',0, '');

INSERT into lams_copy_type VALUES(1,'NONE');
INSERT into lams_copy_type VALUES(2,'LESSON');
INSERT into lams_copy_type VALUES(3,'PREVIEW');

INSERT into lams_workspace_folder_type VALUES (1, 'NORMAL');
INSERT into lams_workspace_folder_type VALUES (2, 'RUN SEQUENCES');
INSERT into lams_workspace_folder_type VALUES (3, 'PUBLIC SEQUENCES');

INSERT INTO lams_authentication_method VALUES (1, 1, 'LAMS-Database');
INSERT INTO lams_authentication_method VALUES (3, 3, 'MQ-LDAP');

INSERT INTO lams_activity_category VALUES (1 ,'SYSTEM');
INSERT INTO lams_activity_category VALUES (2 ,'COLLABORATION');
INSERT INTO lams_activity_category VALUES (3 ,'ASSESSMENT');
INSERT INTO lams_activity_category VALUES (4 ,'CONTENT');
INSERT INTO lams_activity_category VALUES (5 ,'SPLIT');
INSERT INTO lams_activity_category VALUES (6 ,'RESPONSE');

INSERT INTO lams_grouping_support_type VALUES (1 ,'NONE');
INSERT INTO lams_grouping_support_type VALUES (2 ,'OPTIONAL');
INSERT INTO lams_grouping_support_type VALUES (3 ,'REQUIRED');

INSERT INTO lams_log_event_type VALUES (1, 'TYPE_TEACHER_LEARNING_DESIGN_CREATE');
INSERT INTO lams_log_event_type VALUES (2, 'TYPE_TEACHER_LESSON_CREATE');
INSERT INTO lams_log_event_type VALUES (3, 'TYPE_TEACHER_LESSON_START');
INSERT INTO lams_log_event_type VALUES (4, 'TYPE_TEACHER_LESSON_CHANGE_STATE');
INSERT INTO lams_log_event_type VALUES (5, 'TYPE_LEARNER_ACTIVITY_START');
INSERT INTO lams_log_event_type VALUES (6, 'TYPE_LEARNER_ACTIVITY_FINISH');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time,pedagogical_planner_url)
VALUES (1, 2, 'Grouping', 'All types of grouping including random and chosen.', 
	'learning/grouping.do?method=performGrouping', 'learning/grouping.do?method=performGrouping', 
	'learning/grouping.do?method=viewGrouping&mode=teacher',
	'monitoring/grouping.do?method=startGrouping', 
	'monitoring/grouping.do?method=startGrouping', now(),
	'pedagogicalPlanner/grouping.do?method=initGrouping');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (2, 3, 'Sync Gate', 'Gate: Synchronise Learners.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, 
	'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (3, 4, 'Schedule Gate', 'Gate: Opens/shuts at particular times.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null,
	'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (4, 5, 'Permission Gate', 'Gate: Opens under teacher or staff control.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null,
	'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (5, 9, 'System Gate', 'Gate: Opens under system control.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null,
	'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);
	
INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (6, 10, 'Monitor Chosen Branching', 'Select between multiple sequence activities, with the branch chosen in monitoring.', 
	'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching', 
	'monitoring/complexProgress.do', 'monitoring/chosenBranching.do?method=assignBranch', 
	'monitoring/grouping.do?method=startGrouping', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (7, 11, 'Group Based Branching', 'Select between multiple sequence activities, with the branch chosen by an existing group.',
        'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching',
        'monitoring/complexProgress.do', 'monitoring/groupedBranching.do?method=viewBranching',
        'monitoring/groupedBranching.do?method=assignBranch', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (8, 12, 'Tool Output Based Branching', 'Select between multiple sequence activities, with the branch chosen on results of another activity.',
        'learning/branching.do?method=performBranching', 'learning/branching.do?method=performBranching',
        'monitoring/complexProgress.do', 'monitoring/toolBranching.do?method=viewBranching',
        'monitoring/toolBranching.do?method=viewBranching', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description,
        learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (9, 8, 'Sequence', 'A sequence of activities',
        'learning/SequenceActivity.do', 'learning/SequenceActivity.do',
        'monitoring/complexProgress.do', 'monitoring/sequence.do?method=viewSequence',
        'monitoring/sequence.do?method=viewSequence', now());

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (10, 14, 'Condition Gate', 'Gate: Opens if conditions are met', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null,
	'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, monitor_url, contribute_url, create_date_time)
VALUES (11, 15, 'Floating Activities', 'A collection of floating activities', 
	NULL, NULL, null, 'monitoring/floating.do?method=viewFloating', 
	'monitoring/floating.do?method=viewFloating', now()	);

-- Supported Locales
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (1, 'en', 'AU', 'English (Australia)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (2, 'es', 'ES', 'Español', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (3, 'mi', 'NZ', 'Māori', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (4, 'de', 'DE', 'Deutsch', 'LTR', 'de');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (5, 'zh', 'CN', '简体中文', 'LTR', 'zh-cn');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (6, 'fr', 'FR', 'Français', 'LTR', 'fr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (7, 'it', 'IT', 'Italiano', 'LTR', 'it');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (8, 'no', 'NO', 'Norsk', 'LTR', 'no');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (9, 'sv', 'SE', 'Svenska', 'LTR', 'sv');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (10, 'ko', 'KR', '한국어', 'LTR', 'ko');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (11, 'pl', 'PL', 'Polski', 'LTR', 'pl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (12, 'pt', 'BR', 'Português (Brasil)', 'LTR', 'pt-br');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (13, 'hu', 'HU', 'Magyar', 'LTR', 'hu');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (14, 'bg', 'BG', 'Български', 'LTR', 'bg');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (15, 'cy', 'GB', 'Cymraeg (Cymru)', 'LTR', 'en-au');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (16, 'th', 'TH', 'Thai', 'LTR', 'th');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (17, 'el', 'GR', 'Ελληνικά', 'LTR', 'el');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (18, 'nl', 'BE', 'Nederlands (België)', 'LTR', 'nl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (19, 'ar', 'JO', 'عربي', 'RTL', 'ar');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (20, 'da', 'DK', 'Dansk', 'LTR', 'da');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (21, 'ru', 'RU', 'Русский', 'LTR', 'ru');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (22, 'vi', 'VN', 'Tiếng Việt', 'LTR', 'vi');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (23, 'zh', 'TW', 'Chinese (Taiwan)', 'LTR', 'zh');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (24, 'ja', 'JP', '日本語', 'LTR', 'ja');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (25, 'ms', 'MY', 'Malay (Malaysia)', 'LTR', 'ms');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (26, 'tr', 'TR', 'Türkçe', 'LTR', 'tr');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (27, 'ca', 'ES', 'Català', 'LTR', 'ca');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (28, 'sl', 'SI', 'Slovenščina', 'LTR', 'sl');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (29, 'es', 'MX', 'Español (México)', 'LTR', 'es');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (30, 'cs', 'CZ', 'Čeština', 'LTR', 'cs');
INSERT INTO lams_supported_locale (locale_id, language_iso_code, country_iso_code, description, direction, fckeditor_code) 
VALUES (31, 'id', 'ID', 'Indonesian', 'LTR', 'en-au');


-- Default TimeZones
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+14');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+13');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+12');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+11');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+10');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+9');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+8');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+7');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+6');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+5');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+4');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+3');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+2');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+1');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-1');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-2');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-3');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-4');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-5');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-6');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-7');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-8');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-9');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-10');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-11');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-12');

INSERT INTO lams_ext_server_type VALUES (1, 'INTEGRATED SERVER');
INSERT INTO lams_ext_server_type VALUES (2, 'LTI TOOL CONSUMER');

-- external server mapping to a lams organisation
INSERT INTO lams_ext_server_org_map VALUES
  (1,'moodle','moodle','moodle','moodle','mdl','http://localhost/moodle/mod/lamstwo/userinfo.php?ts=%timestamp%&un=%username%&hs=%hash%', '',
   'http://dummy', '', true,80,0,NULL,1,NULL);

-- LDEV-3450 Implement peer review feature
INSERT INTO lams_rating_criteria_type VALUES (1, 'TOOL_ACTIVITY');
INSERT INTO lams_rating_criteria_type VALUES (2, 'AUTHORED_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (3, 'LEARNER_ITEM');
INSERT INTO lams_rating_criteria_type VALUES (4, 'LESSON');
   
-- initialise db version
INSERT INTO patches VALUES ('lams', 20170101, NOW(), 'F');