SET FOREIGN_KEY_CHECKS=0;

INSERT INTO lams_organisation VALUES (1, 'Root', 'Root Organisation',null,1,NOW(),null);
INSERT INTO lams_organisation VALUES (2, 'Maquarie Uni', 'Macquarie University',1,2,NOW(),null);
INSERT INTO lams_organisation VALUES (3, 'MELCOE', 'Macquarie E-learning Center',2,3,NOW(),null);
INSERT INTO lams_organisation VALUES (4, 'LAMS', 'Lams Project Team',3,3,NOW(),null);
INSERT INTO lams_organisation VALUES (5, 'MAMS', 'Mams Project Team',3,3,NOW(),null);

INSERT INTO lams_user VALUES(1, 'sysadmin','sysadmin','Mr','Fei','Yang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'fyang@melcoe.mq.edu.au',0,NOW(),1,null,1,1);
INSERT INTO lams_user VALUES(2, 'test','test','Mr','Kevin','Han',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'khan@melcoe.mq.edu.au',0,NOW(),3,null,2,2);
INSERT INTO lams_user VALUES(3, 'lamskh01','dummy','Mr','Jacky','Fang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'jfang@melcoe.mq.edu.au',0,NOW(),2,null,3,3);

INSERT INTO lams_user_organisation VALUES (1, 1, 1);
INSERT INTO lams_user_organisation VALUES (2, 2, 2);
INSERT INTO lams_user_organisation VALUES (3, 3, 2);
INSERT INTO lams_user_organisation VALUES (4, 4, 2);
INSERT INTO lams_user_organisation VALUES (5, 2, 3);
INSERT INTO lams_user_organisation VALUES (6, 3, 3);

INSERT INTO lams_user_organisation_role VALUES (1, 1, 1);
INSERT INTO lams_user_organisation_role VALUES (2, 2, 2);
INSERT INTO lams_user_organisation_role VALUES (3, 2, 3);
INSERT INTO lams_user_organisation_role VALUES (4, 2, 4);
INSERT INTO lams_user_organisation_role VALUES (5, 3, 2);
INSERT INTO lams_user_organisation_role VALUES (6, 3, 3);
INSERT INTO lams_user_organisation_role VALUES (7, 3, 4);
INSERT INTO lams_user_organisation_role VALUES (8, 4, 2);
INSERT INTO lams_user_organisation_role VALUES (9, 4, 3);
INSERT INTO lams_user_organisation_role VALUES (10, 4, 4);
INSERT INTO lams_user_organisation_role VALUES (11, 4, 5);
INSERT INTO lams_user_organisation_role VALUES (12, 5, 4);
INSERT INTO lams_user_organisation_role VALUES (13, 5, 5);
INSERT INTO lams_user_organisation_role VALUES (14, 6, 3);

INSERT INTO lams_authentication_method VALUES (1, 1, 'LAMS-Database');
INSERT INTO lams_authentication_method VALUES (2, 2, 'Oxford-WebAuth');
INSERT INTO lams_authentication_method VALUES (3, 3, 'MQ-LDAP');

SET FOREIGN_KEY_CHECKS=1;