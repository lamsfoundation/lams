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

INSERT INTO lams_organisation_type VALUES(1, 'ROOT ORGANISATION');
INSERT INTO lams_organisation_type VALUES(2, 'BASE ORGANISATION');
INSERT INTO lams_organisation_type VALUES(3, 'SUB-ORGANIZATION');

