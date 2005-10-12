-- this is similar to insert test data except that it only load
-- user/organisation and workspace data. it does not load learning design
-- or tool related details.

SET FOREIGN_KEY_CHECKS=0;

insert into lams_workspace values(1,null,'ROOT');
insert into lams_workspace values(2,null,'MACQUIRE UNIVERSITY');
insert into lams_workspace values(3,null,'MELCOE');
insert into lams_workspace values(4,null,'LAMS');
insert into lams_workspace values(5,null,'MAMS');
insert into lams_workspace values(6,null,'Manpreet Minhas');

INSERT INTO lams_organisation VALUES (1, 'Root', 'Root Organisation',null,1,NOW(),1);
INSERT INTO lams_organisation VALUES (2, 'Maquarie Uni', 'Macquarie University',1,2,NOW(),2);
INSERT INTO lams_organisation VALUES (3, 'MELCOE', 'Macquarie E-learning Center',2,3,NOW(),3);
INSERT INTO lams_organisation VALUES (4, 'LAMS', 'Lams Project Team',3,3,NOW(),4);
INSERT INTO lams_organisation VALUES (5, 'MAMS', 'Mams Project Team',3,3,NOW(),5);


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

-- mmm user: author, learner, stagg
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

-- users
INSERT INTO lams_user VALUES(1, 'sysadmin','sysadmin','Mr','Fei','Yang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'fyang@melcoe.mq.edu.au',0,NOW(),1,null,1,'en','au',1);
INSERT INTO lams_user VALUES(2, 'test','test','Dr','Testing','LDAP',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test@xx.xx.xx',0,NOW(),3,null,2,'mi','nz',1);
INSERT INTO lams_user VALUES(3, 'lamskh01','dummy','Mr','Jacky','Fang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'jfang@melcoe.mq.edu.au',0,NOW(),2,null,3,'en','nz',1);

insert into lams_user (user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,
city,state,country,day_phone,evening_phone,mobile_phone,
fax,email,
disabled_flag,create_date,authentication_method_id,workspace_id,
base_organisation_id)
values
(4,'mmm','mmm','Ms','Mary','Morgan','99','First Ave',null,
'Parramatta','NSW','Australia','0295099999','0298939999','0499999999',
'0299999999','mmmmmmm@xx.os',
0,'20041223',1,6,4);

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
values(6,4,'Manpreet Minhas Workspace',6,4,'20041223','20041223',1);

insert into lams_workspace_folder (workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(7,6,'Manpreet Minhas Run Sequences Folder',6,4,'20041223','20041223',2);

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


update lams_workspace set root_folder_id = 1 where workspace_id = 1;
update lams_workspace set root_folder_id = 2 where workspace_id = 2;
update lams_workspace set root_folder_id = 3 where workspace_id = 3;
update lams_workspace set root_folder_id = 4 where workspace_id = 4;
update lams_workspace set root_folder_id = 5 where workspace_id = 5;
update lams_workspace set root_folder_id = 6 where workspace_id = 6;

SET FOREIGN_KEY_CHECKS=1;