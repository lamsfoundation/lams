-- CVS ID: $Id$

-- this is similar to insert test data except that it only load
-- user/organisation and workspace data. it does not load learning design
-- or tool related details.

SET FOREIGN_KEY_CHECKS=0;

insert into lams_workspace values(1,null,null,'ROOT');
insert into lams_workspace values(2,null,null,'Macquarie University');
insert into lams_workspace values(3,null,null,'MELCOE');
insert into lams_workspace values(4,null,null,'LAMS');
insert into lams_workspace values(5,null,null,'MAMS');
insert into lams_workspace values(6,null,null,'Mary Morgan');
insert into lams_workspace values(7,null,null,'One Test');
insert into lams_workspace values(8,null,null,'Two Test');
insert into lams_workspace values(9,null,null,'Three Test');
insert into lams_workspace values(10,null,null,'Four Test');

INSERT INTO lams_organisation VALUES (1, 'Root', 'Root Organisation',null,1,NOW(),1);
INSERT INTO lams_organisation VALUES (2, 'Maquarie Uni', 'Macquarie University',1,2,NOW(),2);
INSERT INTO lams_organisation VALUES (3, 'MELCOE', 'Macquarie E-Learning Centre Of Excellence',2,3,NOW(),3);
INSERT INTO lams_organisation VALUES (4, 'LAMS', 'LAMS Project Team',3,3,NOW(),4);
INSERT INTO lams_organisation VALUES (5, 'MAMS', 'MAMS Project Team',3,3,NOW(),5);


INSERT INTO lams_user_organisation VALUES (1, 1, 1);
INSERT INTO lams_user_organisation VALUES (2, 2, 2);
INSERT INTO lams_user_organisation VALUES (3, 3, 2);
INSERT INTO lams_user_organisation VALUES (4, 4, 2);
INSERT INTO lams_user_organisation VALUES (5, 2, 3);
INSERT INTO lams_user_organisation VALUES (6, 3, 3);

INSERT INTO lams_user_organisation VALUES (7, 4, 4);
INSERT INTO lams_user_organisation VALUES (8, 2, 4);
INSERT INTO lams_user_organisation VALUES (9, 3, 4);
INSERT INTO lams_user_organisation VALUES (10,5, 4);

INSERT INTO lams_user_organisation VALUES (11,4, 5);
INSERT INTO lams_user_organisation VALUES (12,4, 6);
INSERT INTO lams_user_organisation VALUES (13,4, 7);
INSERT INTO lams_user_organisation VALUES (14,4, 8);


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

-- mmm user: author, learner, staff
INSERT INTO lams_user_organisation_role VALUES (15, 7, 3);
INSERT INTO lams_user_organisation_role VALUES (16, 8, 3);
INSERT INTO lams_user_organisation_role VALUES (17, 9, 3);
INSERT INTO lams_user_organisation_role VALUES (18, 10,3);
INSERT INTO lams_user_organisation_role VALUES (19, 7, 4);
INSERT INTO lams_user_organisation_role VALUES (20, 8, 4);
INSERT INTO lams_user_organisation_role VALUES (21, 9, 4);
INSERT INTO lams_user_organisation_role VALUES (22, 10,4);
INSERT INTO lams_user_organisation_role VALUES (23, 7, 5);
INSERT INTO lams_user_organisation_role VALUES (24, 8, 5);
INSERT INTO lams_user_organisation_role VALUES (25, 9, 5);
INSERT INTO lams_user_organisation_role VALUES (26, 10,5);

-- test users: author, learner, staff
INSERT INTO lams_user_organisation_role VALUES (27, 11, 3);
INSERT INTO lams_user_organisation_role VALUES (28, 11, 4);
INSERT INTO lams_user_organisation_role VALUES (29, 11, 5);
INSERT INTO lams_user_organisation_role VALUES (30, 12, 3);
INSERT INTO lams_user_organisation_role VALUES (31, 12, 4);
INSERT INTO lams_user_organisation_role VALUES (32, 12, 5);
INSERT INTO lams_user_organisation_role VALUES (33, 13, 3);
INSERT INTO lams_user_organisation_role VALUES (34, 13, 4);
INSERT INTO lams_user_organisation_role VALUES (35, 13, 5);
INSERT INTO lams_user_organisation_role VALUES (36, 14, 3);
INSERT INTO lams_user_organisation_role VALUES (37, 14, 4);
INSERT INTO lams_user_organisation_role VALUES (38, 14, 5);

-- themes and styles 
INSERT INTO lams_css_style (style_id, theme_ve_id) VALUES (1,1);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (1, 1, "borderStyle", "outset", NULL, 1);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (2, 1, "color", "12452097", "_tf", 3);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (3, 1, "rollOverColor", "16711680", NULL, 2);

INSERT INTO lams_css_style (style_id,theme_ve_id) VALUES (2,2);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (4, 2, "display", "block", "_tf", 1);
INSERT INTO lams_css_property (property_id, style_id, name, value, style_subset, type) VALUES (5, 2, "selectionColor", "16711681", NULL, 3);

INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (1, "ruby", "cut down ruby theme for testing", null, 1);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (2, "button", null, 1, 0);
INSERT INTO lams_css_theme_ve (theme_ve_id, name, description, parent_id, theme_flag) VALUES (3, "aqua", "JSP theme", null, 1);

-- users
INSERT INTO lams_user VALUES(1, 'sysadmin','sysadmin','Mr','Fei','Yang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'fyang@melcoe.mq.edu.au',0,NOW(),1,null,1,'en','au',1,3);
INSERT INTO lams_user VALUES(2, 'test','test','Dr','Testing','LDAP',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test@xx.xx.xx',0,NOW(),3,null,2,'mi','nz',1,3);
INSERT INTO lams_user VALUES(3, 'lamskh01','dummy','Mr','Jacky','Fang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'jfang@melcoe.mq.edu.au',0,NOW(),2,null,3,'en','nz',1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id, flash_theme_id, html_theme_id)
values
(4,'mmm','mmm','Ms','Mary','Morgan','99','First Ave',null,
'Parramatta','NSW','Australia','0295099999','0298939999','0499999999',
'0299999999','mmmmmmm@xx.os',
0,'20041223',1,6,4,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id, flash_theme_id, html_theme_id)
values
(5,'test1','test1','Dr','One','Test','1','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test1@xx.os',
0,'20041223',1,7,4,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id, flash_theme_id, html_theme_id)
values
(6,'test2','test2','Dr','Two','Test','2','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test2@xx.os',
0,'20041223',1,8,4,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id, flash_theme_id, html_theme_id)
values
(7,'test3','test3','Dr','Three','Test','3','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test3@xx.os',
0,'20041223',1,9,4,1,3);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id, flash_theme_id, html_theme_id)
values
(8,'test4','test4','Dr','One','Test','4','Test Ave',null,
'Nowhere','NSW','Australia','0211111111','0211111112','0411111111',
'0211111113','test4@xx.os',
0,'20041223',1,10,4,1,3);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(1,null,'ROOT',1,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(2,1,'MACQUAIRE UNIVERSITY',2,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(3,1,'MELCOE',3,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(4,1,'LAMS',4,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(5,1,'MAMS',5,1,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(6,4,'Mary Morgan Workspace',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(7,6,'Mary Morgan Run Sequences Folder',6,4,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(8,6,'Documents',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(9,6,'Pictures',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(10,6,'Media',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(11,10,'Songs',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(12,10,'Movies',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(13,4,'One Test Workspace',7,5,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(14,13,'One Test Run Sequences Folder',7,5,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(15,4,'Two Test Workspace',8,6,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(16,15,'Two Test Run Sequences Folder',8,6,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(17,4,'Three Test Workspace',9,7,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(18,17,'Three Test Run Sequences Folder',9,7,'20041223','20041223',2);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(19,4,'Four Test Workspace',10,8,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(20,19,'Four Test Run Sequences Folder',10,8,'20041223','20041223',2);

update lams_workspace set root_folder_id = 1 where workspace_id = 1;
update lams_workspace set root_folder_id = 2 where workspace_id = 2;
update lams_workspace set root_folder_id = 3 where workspace_id = 3;
update lams_workspace set root_folder_id = 4 where workspace_id = 4;
update lams_workspace set root_folder_id = 5 where workspace_id = 5;
update lams_workspace set root_folder_id = 6 where workspace_id = 6;
update lams_workspace set root_folder_id = 13 where workspace_id = 7;
update lams_workspace set root_folder_id = 15 where workspace_id = 8;
update lams_workspace set root_folder_id = 17 where workspace_id = 9;
update lams_workspace set root_folder_id = 19 where workspace_id = 10;

update lams_workspace set def_run_seq_fld_id = 7 where workspace_id = 6;
update lams_workspace set def_run_seq_fld_id = 14 where workspace_id = 7;
update lams_workspace set def_run_seq_fld_id = 16 where workspace_id = 8;
update lams_workspace set def_run_seq_fld_id = 18 where workspace_id = 9;
update lams_workspace set def_run_seq_fld_id = 20 where workspace_id = 10;

SET FOREIGN_KEY_CHECKS=1;