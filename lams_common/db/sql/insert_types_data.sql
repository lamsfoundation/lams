# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-03-15 13:54:28
# 
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
INSERT INTO lams_organisation_type VALUES(2, 'BASE ORGANISATION', 'base organization: represents a real seperate organization sucha s a university ');
INSERT INTO lams_organisation_type VALUES(3, 'SUB-ORGANISATION', 'sub organization of a base organization');

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

INSERT into lams_license VALUES (1, 'Attribution-Noncommercial-ShareAlike', 'by-nc-sa', 'http://creativecommons.org/licenses/by-nc-sa/2.0/', 1, '');
INSERT into lams_license VALUES (2, 'Other Licensing Agreement', 'other', '',0, '');
INSERT into lams_license VALUES (3, 'Attribution-No Derivatives', 'by-nd', 'http://creativecommons.org/licenses/by-nd/2.0/',0,'');
INSERT into lams_license VALUES (4, 'Attribution-Noncommercial-No Derivatives', 'by-nc-nd', 'http://creativecommons.org/licenses/by-nc-nd/2.0/',0, '');
INSERT into lams_license VALUES (5, 'Attribution-Noncommercial', 'by-nc', 'http://creativecommons.org/licenses/by-nc/2.0/',0,'');
INSERT into lams_license VALUES (6, 'Attribution-ShareAlike', 'by-sa', 'http://creativecommons.org/licenses/by-sa/2.0/',0,''); 

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





