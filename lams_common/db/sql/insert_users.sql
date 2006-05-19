-- CVS ID: $Id$

-- this is similar to insert test data except that it only load
-- user/organisation and workspace data. it does not load learning design
-- or tool related details.

SET FOREIGN_KEY_CHECKS=0;

-- themes and styles 
INSERT INTO lams_css_style (style_id, theme_ve_id) VALUES (1,1);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (1, 1, "borderStyle", "outset", NULL, 1);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (2, 1, "color", "12452097", "_tf", 3);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (3, 1, "rollOverColor", "16711680", NULL, 2);

INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (1, "default", "Default Flash style", null, 1);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (2, "ruby", "Incomplete style for Flash", null, 0);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (3, "aqua", "JSP theme", null, 1);


insert into lams_workspace (workspace_id, root_folder_id, name) values(1,1,'ROOT');
insert into lams_workspace (workspace_id, root_folder_id, name, def_run_seq_fld_id) values(2,2,'Developers Playpen',22);
insert into lams_workspace (workspace_id, root_folder_id, name, def_run_seq_fld_id) values(3,3,'MATH111',23);
insert into lams_workspace (workspace_id, root_folder_id, name) values(4,4,'Mary Morgan');
insert into lams_workspace (workspace_id, root_folder_id, name) values(5,5,'One Test');
insert into lams_workspace (workspace_id, root_folder_id, name) values(6,6,'Two Test');
insert into lams_workspace (workspace_id, root_folder_id, name) values(7,7,'Three Test');
insert into lams_workspace (workspace_id, root_folder_id, name) values(8,8,'Four Test');

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(1,null,'ROOT',1,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(2,1,'Developers Playpen',2,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(22,2,'Lesson Sequence Folder',2,1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(3,1,'MATH111',3,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(23,3,'Lesson Sequence Folder',3,1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(4,null,'Mary Morgan Folder',4,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(5,null,'One Test Workspace',5,5,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(6,null,'Two Test Workspace',6,6,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(7,null,'Three Test Workspace',7,7,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(8,null,'Four Test Workspace',8,8,'20041223','20041223',1);

INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (1, 'Root', 'Root Organisation',null,1,NOW(),1);
INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (2, 'Playpen', 'Developers Playpen',1,2,NOW(),2);
INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (3, 'Everybody', 'All People In Course',2,3,NOW(),null);
INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (4, 'MATH111', 'Mathematics 1',1,2,NOW(),3);
INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (5, 'TUTA', 'Tutorial Group A',4,3,NOW(),null);
INSERT INTO lams_organisation (organisation_id, name, description, parent_organisation_id, organisation_type_id, create_date, workspace_id)
	VALUES (6, 'TUTB', 'Tutorial Group B',4,3,NOW(),null);

-- users
INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_language, locale_country, flash_theme_id, html_theme_id)
VALUES(1, 'sysadmin','sysadmin','Mr','Fei','Yang',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'fyang@melcoe.mq.edu.au',
0,NOW(),1,null,
'en','au',1,3);

INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_language, locale_country, flash_theme_id, html_theme_id)
VALUES(2, 'test','test','Dr','Testing','LDAP',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'test@xx.xx.xx',
0,NOW(),3,null,
'mi','nz',1,3);

INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_language, locale_country, flash_theme_id, html_theme_id)
VALUES(3, 'lamskh01','dummy','Mr','Jacky','Fang',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'jfang@melcoe.mq.edu.au',
0,NOW(),2,null,
'en','nz',1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
flash_theme_id, html_theme_id)
values
(4,'mmm','mmm','Ms','Mary','Morgan','99','First Ave',null,
'Parramatta','NSW','Australia','0295099999','0298939999','0499999999',
'0299999999','mmmmmmm@xx.os',
0,'20041223',1,4,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
flash_theme_id, html_theme_id)
values
(5,'test1','test1','Dr','One','Test','1','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test1@xx.os',
0,'20041223',1,5,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
flash_theme_id, html_theme_id)
values
(6,'test2','test2','Dr','Two','Test','2','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test2@xx.os',
0,'20041223',1,6,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
flash_theme_id, html_theme_id)
values
(7,'test3','test3','Dr','Three','Test','3','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test3@xx.os',
0,'20041223',1,7,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
flash_theme_id, html_theme_id)
values
(8,'test4','test4','Dr','One','Test','4','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test4@xx.os',
0,'20041223',1,8,1,3);

--- sysadmin only belongs to root
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

-- test, lamskh01 and mmm are authors, teachers and learners in Playpen
-- test1, test2, test3 and test4 are authors, staff and learners in Playpen
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (2, 2, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (3, 3, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (4, 4, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (5, 5, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (6, 6, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (7, 7, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (8, 8, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (9, 2, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (10, 3, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (11, 4, 6);
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

-- test, lamskh01 and mmm are teachers and learners in Everybody
-- test1, test2, test3 and test4 are staff and learners in Everybody
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (23, 9, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (24, 10, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (25, 11, 6);
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

-- test, lamskh01 and mmm are authors, teachers and learners in MATH111
-- test1, test2, test3 and test4 are authors, staff and learners in MATH111
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (37, 16, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (38, 17, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (39, 18, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (40, 19, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (41, 20, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (42, 21, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (43, 22, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (44, 16, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (45, 17, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (46, 18, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (47, 19, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (48, 20, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (49, 21, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (50, 22, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (51, 16, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (52, 17, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (53, 18, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (54, 19, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (55, 20, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (56, 21, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (57, 22, 5);

-- test, lamskh01 and mmm are teachers and learners in Tutorial A
-- test1, test2 are staff and learners in Tutorial A
-- test3 and test4 are staff and learners in Tutorial B
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (58, 23, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (59, 24, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (60, 25, 6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (61, 26, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (62, 27, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (63, 28, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (64, 29, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (65, 23, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (66, 24, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (67, 25, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (68, 26, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (69, 27, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (70, 28, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (71, 29, 5);

SET FOREIGN_KEY_CHECKS=1;