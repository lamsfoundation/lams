SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(11,null,'Bulgarian Workspace',11,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (11,11,'Bulgarian Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (11,11);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(11,'bulgarian','bulgarian','Dr','Bulgarian','Test','11','Bulgarian Ave',null,'Sofia',null,'Bulgaria','0211111111','0211111112','0411111111','0211111113','bulgarian@xx.os',0,'20041223',1,11,1,3,14);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (34, 2, 11);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (35, 3, 11);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (82,34,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (83,34,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (84,34,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (142,35,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (85,35,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (86,35,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(12,null,'Chinese Workspace',12,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (12,12,'Chinese Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (12,12);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(12,'chinese','chinese','Dr','Chinese','Test','12','Chinese Ave',null,'Beijing',null,'China','0211111111','0211111112','0411111111','0211111113','chinese@xx.os',0,'20041223',1,12,1,3,5);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (36, 2, 12);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (37, 3, 12);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (87,36,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (88,36,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (89,36,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (143,37,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (90,37,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (91,37,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(10,null,'French Workspace',10,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (10,10,'French Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (10,10);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(10,'french','french','Dr','French','Test','10','French Ave',null,'Paris',null,'France','0211111111','0211111112','0411111111','0211111113','french@xx.os',0,'20041223',1,10,1,3,6);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (32, 2, 10);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (33, 3, 10);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (77,32,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (78,32,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (79,32,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (144,33,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (80,33,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (81,33,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(13,null,'German Workspace',13,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (13,13,'German Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (13,13);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(13,'german','german','Dr','German','Test','13', 'German Ave',null,'Berlin',null,'Germany','0211111111','0211111112','0411111111','0211111113','german@xx.os',0,'20041223',1,13,1,3,4);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (38, 2, 13);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (39, 3, 13);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (92,38,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (93,38,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (94,38,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (145,39,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (95,39,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (96,39,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(21,null,'Hungarian Workspace',21,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (21,21,'Hungarian Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (21,21);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(21,'hungarian','hungarian','Dr','Hungarian','Test','21', 'Hungarian Ave',null,'Bukarest',null,'Hungry','0211111111','0211111112','0411111111','0211111113','hungarian@xx.os',0,'20041223',1,21,1,3,13);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (54, 2, 21);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (55, 3, 21);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (132,54,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (133,54,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (134,54,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (146,55,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (135,55,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (136,55,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(16,null,'Italian Workspace',16,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (16,16,'Italian Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (16,16);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(16,'italian','italian','Dr','Italian','Test','16', 'Italian Ave',null,'Rome',null,'Italy','0211111111','0211111112','0411111111','0211111113','italian@xx.os',0,'20041223',1,16,1,3,7);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (44, 2, 16);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (45, 3, 16);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (107,44,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (108,44,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (109,44,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (147,45,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (110,45,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (111,45,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(18,null,'Korean Workspace',18, now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (18,18,'Korean Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (18,18);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(18,'korean','korean','Dr','Korean','Test','18', 'Korean Ave',null,'Seoul',null,'Korea','0211111111','0211111112','0411111111','0211111113','korean@xx.os',0,'20041223',1,18,1,3,10);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (48, 2, 18);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (49, 3, 18);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (117,48,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (118,48,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (119,48,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (148,49,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (120,49,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (121,49,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(20,null,'Maori Workspace',20,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (20,20,'Maori Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (20,20);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(20,'maori','maori','Dr','Maori','Test','20', 'Maori Ave',null,'Auckland',null,'New Zealand','0211111111','0211111112','0411111111','0211111113','maori@xx.os',0,'20041223',1,20,1,3,3);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (52, 2, 20);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (53, 3, 20);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (127,52,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (128,52,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (129,52,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (149,53,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (130,53,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (131,53,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(14,null,'Norwegian Workspace',14,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (14,14,'Norwegian Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (14,14);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(14,'norwegian','norwegian','Dr','Norwegian','Test','14', 'Norwegian Ave',null,'Oslo',null,'Norway','0211111111','0211111112','0411111111','0211111113','norwegian@xx.os',0,'20041223',1,14,1,3,8);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (40, 2, 14);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (41, 3, 14);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (97,40,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (98,40,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (99,40,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (150,41,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (100,41,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (101,41,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(17,null,'Polish Workspace',17,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (17,17,'Polish Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (17,17);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(17,'polish','polish','Dr','Polish','Test','17', 'Polish Ave',null,'Warsaw',null,'Poland','0211111111','0211111112','0411111111','0211111113','polish@xx.os',0,'20041223',1,17,1,3,11);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (46, 2, 17);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (47, 3, 17);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (112,46,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (113,46,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (114,46,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (151,47,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (115,47,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (116,47,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(15,null,'Portuguese Workspace',15,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (15,15,'Portuguese Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (15,15);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(15,'portuguese','portuguese','Dr','Portuguese','Test','15', 'Portuguese Ave',null,'Brasilia',null,'Brazil','0211111111','0211111112','0411111111','0211111113','portuguese@xx.os',0,'20041223',1,15,1,3,12);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (42, 5, 15);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (43, 5, 15);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,4,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (102,42,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (103,42,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (104,42,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (152,43,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (105,43,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (106,43,2);

SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(19,null,'Spanish Workspace',19,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (19,19,'Spanish Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (19,19);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(19,'spanish','spanish','Dr','Spanish','Test','19', 'Spanish Ave',null,'Madrid',null,'Spain','0211111111','0211111112','0411111111','0211111113','spanish@xx.os',0,'20041223',1,19,1,3,2);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (50, 2, 19);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (51, 3, 19);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (122,50,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (123,50,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (124,50,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (153,51,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (125,51,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (126,51,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(9,null,'Swedish Workspace',9,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (9,9,'Swedish Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (9,9);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(9,'swedish','swedish','Dr','Sweden','Test','9','Swedish Ave',null,'Stockholm',null,'Sweden','0211111111','0211111112','0411111111','0211111113','swedish@xx.os',0,'20041223',1,9,1,3,9);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (30, 2, 9);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (31, 3, 9);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (72,30,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (73,30,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (74,30,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (154,31,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (75,31,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (76,31,2);


SET FOREIGN_KEY_CHECKS=1;
SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(24,null,'Thai Workspace',24,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (24,24,'Thai Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (24,24);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(24,'thai','thai','Dr','Thai','Test','24', 'Thai Ave',null,'Bangkok',null,'Thailand','0211111111','0211111112','0411111111','0211111113','thai@xx.os',0,'20041223',1,24,1,3,16);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (56, 2, 24);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (57, 3, 24);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (137,56,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (138,56,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (139,56,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (155,57,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (140,57,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (141,57,2);

SET FOREIGN_KEY_CHECKS=1;

SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(25,null,'Greek Workspace',25,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (25,25,'Greek Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (11,11);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(25,'greek','greek','Dr','Greek','Test','25','Greek Ave',null,'Athens',null,'Greece','0211111111','0211111112','0411111111','0211111113','greek@xx.os',0,'20041223',1,25,1,3,17);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (58, 2, 25);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (59, 3, 25);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (156,58,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (157,58,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (158,58,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (159,59,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (160,59,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (161,59,2);

SET FOREIGN_KEY_CHECKS=1;

SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(26,null,'Welsh Workspace',26,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (26,26,'Welsh Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (11,11);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id) 
values(26,'welsh','welsh','Dr','Welsh','Test','26','Welsh Ave',null,'Cardiff',null,'Wales','0211111111','0211111112','0411111111','0211111113','welsh@xx.os',0,'20041223',1,26,1,3,15);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (60, 2, 26);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (61, 3, 26);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (162,60,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (163,60,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (164,60,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (165,61,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (166,61,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (167,61,2);

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id)
values(27,null,'Dutch Workspace',27,now(),now(),1);
insert into lams_workspace (workspace_id, default_fld_id, name) values (27,27,'Dutch Test');
--insert into lams_workspace_folder (workspace_id, workspace_folder_id) values (11,11);

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_id)
values(27,'dutch','dutch','Dr','Dutch','Test','26','Dutch Ave',null,'Amsterdam',null,'Netherlands','0211111111','0211111112','0411111111','0211111113','dutch@xx.os',0,'20041223',1,27,1,3,18);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (62, 2, 27);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (63, 3, 27);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,2) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (168,62,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (169,62,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (170,62,2);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (171,63,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (172,63,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (173,63,2);

SET FOREIGN_KEY_CHECKS=1;


update lams_user set password=SHA1(password) where user_id>8;
