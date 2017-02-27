

SET FOREIGN_KEY_CHECKS=0;

-- themes and styles 
INSERT INTO lams_theme (theme_id, name, description, image_directory) VALUES (1, "defaultHTML", "Default HTML style", "css");

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(1,null,'ROOT',1,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(2,1,'Developers Playpen',1,2,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(22,2,'Lesson Sequence Folder',1,2,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(3,1,'MATH111',1,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(23,3,'Lesson Sequence Folder',1,4,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(4,null,'Mary Morgan',4,NULL,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(5,null,'One Test',5,NULL,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(6,null,'Two Test',6,NULL,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(7,null,'Three Test',7,NULL,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(8,null,'Four Test',8,NULL,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(40,1,'Moodle Test',1,7,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(41,40,'Lesson Sequence Folder',1,7,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(45,null,'System Administrator',1,NULL,'20061101','20061101',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,organisation_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(46,1,'Public Folder',1,NULL,'20041223','20041223',3);

INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id)
	VALUES (1, 'Root', null, 'Root Organisation',null,1,NOW(),1,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id, enable_course_notifications, enable_monitor_gradebook, enable_learner_gradebook)
	VALUES (2, 'Playpen', 'PP101', 'Developers Playpen',1,2,NOW(),1,2,1,1,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id)
	VALUES (3, 'Everybody', null, 'All People In Course',2,3,NOW(),1,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id, enable_course_notifications, enable_monitor_gradebook, enable_learner_gradebook)
	VALUES (4, 'Mathematics 1', 'MATH111', 'Mathematics 1',1,2,NOW(),1,2,1,1,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id)
	VALUES (5, 'Tutorial Group A', 'TUTA', 'Tutorial Group A',4,3,NOW(),1,2,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id)
	VALUES (6, 'Tutorial Group B', 'TUTB', 'Tutorial Group B',4,3,NOW(),1,2,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, locale_id, organisation_state_id)
	VALUES (7, 'Moodle', 'Moodle', 'Moodle Test',1,2,NOW(),1,1,2);
	
-- users
INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
VALUES(1, 'sysadmin',SHA1('sysadmin'),'The','System','Administrator',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'sysadmin@x.x',
0,NOW(),1,45,
1,1,'Etc/GMT');

INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
VALUES(2, 'test',SHA1('test'),'Dr','Testing','LDAP',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'test@xx.xx.xx',
0,NOW(),3,null,
3,1,'Etc/GMT');

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
values
(4,'mmm',SHA1('mmm'),'Ms','Mary','Morgan','99','First Ave',null,
'Parramatta','NSW','Australia','0295099999','0298939999','0499999999',
'0299999999','mmmmmmm@xx.os',
0,'20041223',1,4,1,1,'Etc/GMT');

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
values
(5,'test1',SHA1('test1'),'Dr','One','Test','1','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test1@xx.os',
0,'20041223',1,5,1,1,'Etc/GMT');

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
values
(6,'test2',SHA1('test2'),'Dr','Two','Test','2','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test2@xx.os',
0,'20041223',1,6,1,1,'Etc/GMT');

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
values
(7,'test3',SHA1('test3'),'Dr','Three','Test','3','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test3@xx.os',
0,'20041223',1,7,1,1,'Etc/GMT');

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_folder_id,
locale_id, theme_id, timezone)
values
(8,'test4',SHA1('test4'),'Dr','Four','Test','4','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test4@xx.os',
0,'20041223',1,8,1,1,'Etc/GMT');

--- sysadmin only belongs to root and public
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (1, 1, 1);

--- all other users belong to Playpen
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (2, 2, 2);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (3, 2, 3);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (4, 2, 4);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (5, 2, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (6, 2, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (7, 2, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (8, 2, 8);
--- all other users belong to Playpen's class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (9, 3, 2);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (10, 3, 3);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (11, 3, 4);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (12, 3, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (13, 3, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (14, 3, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (15, 3, 8);
--- all other users belong to MATH111
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (16, 4, 2);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (17, 4, 3);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (18, 4, 4);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (19, 4, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (20, 4, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (21, 4, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (22, 4, 8);
--- all test3, test4 users belong to Tutorial B, the other users belong to Tutorial A
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (23, 5, 2);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (24, 5, 3);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (25, 5, 4);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (26, 5, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (27, 5, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (28, 6, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (29, 6, 8);

-- sysadmin has sysadmin role only
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (1, 1, 1);

-- test, lamskh01 and mmm are authors, staff and learners in Playpen
-- test1, test2, test3 and test4 are authors, staff and learners in Playpen
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (2, 2, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (3, 3, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (4, 4, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (5, 5, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (6, 6, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (7, 7, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (8, 8, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (9, 2, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (10, 3, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (11, 4, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (12, 5, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (13, 6, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (14, 7, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (15, 8, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (16, 2, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (17, 3, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (18, 4, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (19, 5, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (20, 6, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (21, 7, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (22, 8, 5);

-- test, lamskh01 and mmm are staff and learners in Everybody
-- test1, test2, test3 and test4 are staff and learners in Everybody
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (23, 9, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (24, 10, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (25, 11, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (26, 12, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (27, 13, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (28, 14, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (29, 16, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (30, 9, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (31, 10, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (32, 11, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (33, 12, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (34, 13, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (35, 14, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (36, 15, 5);

-- test, lamskh01 and mmm are authors, staff and learners in MATH111
-- test1, test2, test3 and test4 are authors, staff and learners in MATH111
-- test4 is group manager, mmm is group admin in MATH111
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (37, 22, 2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (38, 16, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (39, 17, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (40, 18, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (41, 19, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (42, 20, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (43, 21, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (44, 22, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (45, 16, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (46, 17, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (47, 18, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (48, 19, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (49, 20, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (50, 21, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (51, 22, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (52, 16, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (53, 17, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (54, 18, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (55, 19, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (56, 20, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (57, 21, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (58, 22, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (59, 18, 6);

-- test, lamskh01 and mmm are staff and learners in Tutorial A
-- test1, test2 are staff and learners in Tutorial A
-- test3 and test4 are staff and learners in Tutorial B
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (60, 23, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (61, 24, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (62, 25, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (63, 26, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (64, 27, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (65, 28, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (66, 29, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (67, 23, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (68, 24, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (69, 25, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (70, 26, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (71, 27, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (72, 28, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (73, 29, 5);

SET FOREIGN_KEY_CHECKS=1;
