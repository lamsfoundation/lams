# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-26 11:35:47
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-22 11:44:40
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-09 15:04:37
# 
# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2004-11-09 15:01:18
# 
INSERT INTO lams_role VALUES (1, 'SYSADMIN', 'LAMS System Adminstrator', NOW());
INSERT INTO lams_role VALUES (2, 'ADMIN', 'Organization Adminstrator', NOW());
INSERT INTO lams_role VALUES (3, 'AUTHOR', 'Authors Learning Designs', NOW());
INSERT INTO lams_role VALUES (4, 'STAFF', 'Member of Staff', NOW());
INSERT INTO lams_role VALUES (5, 'LEARNER', 'Student', NOW());

INSERT INTO lams_authentication_method_type VALUES(1, 'LAMS');
INSERT INTO lams_authentication_method_type VALUES(2, 'WEB_AUTH');
INSERT INTO lams_authentication_method_type VALUES(3, 'LDAP');

INSERT INTO lams_organisation_type VALUES(1, 'ROOT ORGANISATION', 'root all other organisations: controlled by Sysadmin');
INSERT INTO lams_organisation_type VALUES(2, 'BASE ORGANISATION', 'base organization: represents a real seperate organization sucha s a university ');
INSERT INTO lams_organisation_type VALUES(3, 'SUB-ORGANIZATION', 'sub organization of a base organization');

INSERT INTO lams_grouping_type VALUES (1, 'NORMAL');
INSERT INTO lams_grouping_type VALUES (2, 'CLASS');

INSERT INTO lams_learning_activity_type VALUES (1, 'TOOL');
INSERT INTO lams_learning_activity_type VALUES (2, 'GROUPING_RANDOM');
INSERT INTO lams_learning_activity_type VALUES (3, 'GROUPING_CHOSEN');
INSERT INTO lams_learning_activity_type VALUES (4, 'GATE_SYNCH');
INSERT INTO lams_learning_activity_type VALUES (5, 'GATE_SCHEDULE');
INSERT INTO lams_learning_activity_type VALUES (6, 'GATE_PERMISSION');
INSERT INTO lams_learning_activity_type VALUES (7, 'PARALLEL');
INSERT INTO lams_learning_activity_type VALUES (8, 'OPTIONS');
INSERT INTO lams_learning_activity_type VALUES (9, 'SEQUENCE');
INSERT INTO lams_learning_activity_type VALUES (10, 'BRANCH_GROUP_CONTENT');

INSERT INTO lams_gate_activity_level VALUES (1, 'LEARNER');
INSERT INTO lams_gate_activity_level VALUES (2, 'GROUP');
INSERT INTO lams_gate_activity_level VALUES (3, 'CLASS');

INSERT INTO lams_tool_session_state VALUES  (1, 'NOT ATTEMPTED');
INSERT INTO lams_tool_session_state VALUES  (2, 'INCOMPLETE');
INSERT INTO lams_tool_session_state VALUES  (3, 'COMPLETED');

INSERT INTO lams_user_tool_session_state VALUES (1, 'JOINED');
INSERT INTO lams_user_tool_session_state VALUES (2, 'LEFT' );
INSERT INTO lams_user_tool_session_state VALUES (3, 'SUSPENDED');
INSERT INTO lams_user_tool_session_state VALUES (4, 'RESUMED');
INSERT INTO lams_user_tool_session_state VALUES (5, 'ERROR');



