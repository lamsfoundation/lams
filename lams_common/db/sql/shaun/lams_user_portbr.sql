SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(15,null,'Portuguese Workspace',15,15, now(),now(),1);
insert into lams_workspace (workspace_id, root_folder_id, name) values (15,null,'Portuguese Test');

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_language,locale_country) 
values(15,'portuguese','portuguese','Dr','Portuguese','Test','15', 'Portuguese Ave',null,'Brasilia',null,'Brazil','0211111111','0211111112','0411111111','0211111113','portuguese@xx.os',0,'20041223',1,15,1,3,'pt','br');

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (42, 2, 15);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (43, 3, 15);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,4,6) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (102,42,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (103,42,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (104,42,6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (105,43,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (106,43,6);

SET FOREIGN_KEY_CHECKS=1;