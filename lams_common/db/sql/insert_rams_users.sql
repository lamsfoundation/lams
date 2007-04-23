-- CVS ID: $Id$

SET FOREIGN_KEY_CHECKS=0;

-- themes and styles 
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag, image_directory) VALUES (1, "default", "Default Flash style", null, 1, null);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag, image_directory) VALUES (2, "defaultHTML", "Default HTML style", null, 1, "css");
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag, image_directory) VALUES (3, "rams", "RAMS Default Flash style", null, 1, null);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag, image_directory) VALUES  (4,"ramsthemeHTML", "RAMS Default HTML sty;e", null, 1, "ramsthemecss");

insert into lams_workspace (workspace_id, name, default_fld_id) values(1,'ROOT',1);
insert into lams_workspace (workspace_id, name, default_fld_id, def_run_seq_fld_id) values(2,'Developers Playpen',2,22);
insert into lams_workspace (workspace_id, name, default_fld_id) values(5,'One Test',5);
insert into lams_workspace (workspace_id, name, default_fld_id) values(6,'Two Test',6);
insert into lams_workspace (workspace_id, name, default_fld_id) values(7,'Three Test',7);
insert into lams_workspace (workspace_id, name, default_fld_id) values(8,'Four Test',8);
insert into lams_workspace (workspace_id, name, default_fld_id, def_run_seq_fld_id) values(50,'Moodle Test',40,41);
insert into lams_workspace (workspace_id, name, default_fld_id) values(51,'System Administrator',45);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(1,null,'ROOT',1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(2,1,'Developers Playpen',1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(22,2,'Lesson Sequence Folder',1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(23,3,'Lesson Sequence Folder',1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(5,null,'One Test',5,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(6,null,'Two Test',6,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(7,null,'Three Test',7,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(8,null,'Four Test',8,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(40,1,'Moodle Test',1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(41,40,'Lesson Sequence Folder',1,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(45,null,'System Administrator',1,'20061101','20061101',1);

insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (1, 1,1);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (2, 2,2);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (3, 2,22);
--insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (5, 3,23);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (6, 4,4);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (7, 5,5);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (8, 6,6);
--insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (9, 7,7);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (10, 8,8);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (11, 50,40);
insert into lams_wkspc_wkspc_folder (id, workspace_id, workspace_folder_id) values (12, 51,45);

INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, workspace_id, locale_id, organisation_state_id)
	VALUES (1, 'Root', null, 'Root Organisation',null,1,NOW(),1,1,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, workspace_id, locale_id, organisation_state_id)
	VALUES (2, 'Playpen', 'PP101', 'Developers Playpen',1,2,NOW(),1,2,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, workspace_id, locale_id, organisation_state_id)
	VALUES (3, 'Everybody', null, 'All People In Course',2,3,NOW(),1,null,1,1);
INSERT INTO lams_organisation (organisation_id, name, code, description, parent_organisation_id, organisation_type_id, create_date, created_by, workspace_id, locale_id, organisation_state_id)
	VALUES (7, 'Moodle', 'Moodle', 'Moodle Test',1,2,NOW(),1,50,1,2);


-- users
INSERT INTO lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_id, flash_theme_id, html_theme_id)
VALUES(1, 'sysadmin',SHA1('sysadmin'),'The','System','Administrator',null,null,null,
'Sydney','NSW','Australia',null,null,null,null,'sysadmin@x.x',
0,NOW(),1,51,
1,3,4);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_id,flash_theme_id, html_theme_id)
values
(5,'test1',SHA1('test1'),'Dr','One','Test','1','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test1@xx.os',
0,'20041223',1,5,1,3,4);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_id,flash_theme_id, html_theme_id)
values
(6,'test2',SHA1('test2'),'Dr','Two','Test','2','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test2@xx.os',
0,'20041223',1,6,1,3,4);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_id,flash_theme_id, html_theme_id)
values
(7,'test3',SHA1('test3'),'Dr','Three','Test','3','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test3@xx.os',
0,'20041223',1,7,1,3,4);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
locale_id,flash_theme_id, html_theme_id)
values
(8,'test4',SHA1('test4'),'Dr','Four','Test','4','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test4@xx.os',
0,'20041223',1,8,1,3,4);

--- sysadmin only belongs to root
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (1, 1, 1);

--- all other users belong to Playpen
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (5, 2, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (6, 2, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (7, 2, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (8, 2, 8);
--- all other users belong to Playpen's class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (12, 3, 5);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (13, 3, 6);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (14, 3, 7);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (15, 3, 8);

-- sysadmin has sysadmin role only
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (1, 1, 1);

-- test1, test2, test3 and test4 are authors, staff and learners in Playpen
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (5, 5, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (6, 6, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (7, 7, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (8, 8, 3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (12, 5, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (13, 6, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (14, 7, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (15, 8, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (19, 5, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (20, 6, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (21, 7, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (22, 8, 5);

-- test1, test2, test3 and test4 are staff and learners in Everybody
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (26, 12, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (27, 13, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (28, 14, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (29, 15, 4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (33, 12, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (34, 13, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (35, 14, 5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (36, 15, 5);

SET FOREIGN_KEY_CHECKS=1;