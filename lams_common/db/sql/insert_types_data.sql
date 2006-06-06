-- CVS ID: $Id$

INSERT INTO lams_role VALUES (1, 'SYSADMIN', 'LAMS System Adminstrator', NOW());
INSERT INTO lams_role VALUES (2, 'ADMIN', 'Organization Adminstrator', NOW());
INSERT INTO lams_role VALUES (3, 'AUTHOR', 'Authors Learning Designs', NOW());
INSERT INTO lams_role VALUES (4, 'STAFF', 'Member of Staff', NOW());
INSERT INTO lams_role VALUES (5, 'LEARNER', 'Student', NOW());
INSERT INTO lams_role VALUES (6, 'TEACHER', 'Student', NOW());

INSERT INTO lams_authentication_method_type VALUES(1, 'LAMS');
INSERT INTO lams_authentication_method_type VALUES(2, 'WEB_AUTH');
INSERT INTO lams_authentication_method_type VALUES(3, 'LDAP');

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

INSERT into lams_license VALUES (1, 'Attribution-Noncommercial-ShareAlike 2.5', 'by-nc-sa', 'http://creativecommons.org/licenses/by-nc-sa/2.5/', 1, '/images/license/byncsa.jpg');
INSERT into lams_license VALUES (2, 'Other Licensing Agreement', 'other', '',0, '');
INSERT into lams_license VALUES (3, 'Attribution-No Derivatives 2.5', 'by-nd', 'http://creativecommons.org/licenses/by-nd/2.5/',0,'/images/license/bynd.jpg');
INSERT into lams_license VALUES (4, 'Attribution-Noncommercial-No Derivatives 2.5', 'by-nc-nd', 'http://creativecommons.org/licenses/by-nc-nd/2.5/',0, '/images/license/byncnd.jpg');
INSERT into lams_license VALUES (5, 'Attribution-Noncommercial 2.5', 'by-nc', 'http://creativecommons.org/licenses/by-nc/2.5/',0,'/images/license/bync.jpg');
INSERT into lams_license VALUES (6, 'Attribution-ShareAlike 2.5', 'by-sa', 'http://creativecommons.org/licenses/by-sa/2.5/',0,'/images/license/byncsa.jpg'); 

INSERT into lams_copy_type VALUES(1,'NONE');
INSERT into lams_copy_type VALUES(2,'LESSON');
INSERT into lams_copy_type VALUES(3,'PREVIEW');

INSERT into lams_workspace_folder_type VALUES (1, 'NORMAL');
INSERT into lams_workspace_folder_type VALUES (2, 'RUN SEQUENCES');

INSERT INTO lams_authentication_method VALUES (1, 1, 'LAMS-Database');
INSERT INTO lams_authentication_method VALUES (2, 2, 'Oxford-WebAuth');
INSERT INTO lams_authentication_method VALUES (3, 3, 'MQ-LDAP');

INSERT INTO lams_activity_category VALUES (1 ,'SYSTEM');
INSERT INTO lams_activity_category VALUES (2 ,'COLLABORATION');
INSERT INTO lams_activity_category VALUES (3 ,'ASSESSMENT');
INSERT INTO lams_activity_category VALUES (4 ,'CONTENT');
INSERT INTO lams_activity_category VALUES (5 ,'SPLIT');

INSERT INTO lams_grouping_support_type VALUES (1 ,'NONE');
INSERT INTO lams_grouping_support_type VALUES (2 ,'OPTIONAL');
INSERT INTO lams_grouping_support_type VALUES (3 ,'REQUIRED');

INSERT INTO lams_log_event_type VALUES (1, 'LEARNER_LESSON_START');
INSERT INTO lams_log_event_type VALUES (2, 'LEARNER_LESSON_FINISH');
INSERT INTO lams_log_event_type VALUES (3, 'LEARNER_LESSON_EXIT');
INSERT INTO lams_log_event_type VALUES (4, 'LEARNER_LESSON_RESUME');
INSERT INTO lams_log_event_type VALUES (5, 'LEARNER_ACTIVITY_START');
INSERT INTO lams_log_event_type VALUES (6, 'LEARNER_ACTIVITY_FINISH');

INSERT INTO lams_workspace_folder_content_type VALUES (1,'FILE');
INSERT INTO lams_workspace_folder_content_type VALUES (2,'PACKAGE');

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (1, 2, 'Grouping', 'All types of grouping including random and chosen.', 
	'learning/grouping.do?method=performGrouping', 'learning/grouping.do?method=performGrouping', 
	'learning/grouping.do?method=viewGrouping', 'learning/grouping.do?method=viewGrouping', 
	'learning/grouping.do?method=viewGrouping', 
	'monitoring/grouping.do?method=startGrouping', 
	'monitoring/grouping.do?method=startGrouping', now() );

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (2, 3, 'Sync Gate', 'Gate: Synchronise Learners.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, null, 
	null, 'monitoring/gate.do?method=viewGate', 'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (3, 4, 'Schedule Gate', 'Gate: Opens/shuts at particular times.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, null, 
	null, 'monitoring/gate.do?method=viewGate', 'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (4, 5, 'Permission Gate', 'Gate: Opens under teacher or staff control.', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, null, 
	null, 'monitoring/gate.do?method=viewGate', 'monitoring/gate.do?method=viewGate', now()	);

INSERT INTO lams_country VALUES (1, 'AU');
INSERT INTO lams_country VALUES (2, 'US');
INSERT INTO lams_country VALUES (3, 'CN');

INSERT INTO lams_language VALUES (1, 'en');
INSERT INTO lams_language VALUES (2, 'zh');