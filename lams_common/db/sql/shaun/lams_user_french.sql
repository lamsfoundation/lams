SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(10,null,'French Workspace',10,10,now(),now(),1);
insert into lams_workspace (workspace_id, root_folder_id, name) values (10,null,'French Test');

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_language,locale_country) 
values(10,'french','french','Dr','French','Test','10','French Ave',null,'Paris',null,'France','0211111111','0211111112','0411111111','0211111113','french@xx.os',0,'20041223',1,10,1,3,'fr',null);

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (32, 2, 10);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (33, 3, 10);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,5,6) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (77,32,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (78,32,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (79,32,6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (80,33,5);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (81,33,6);

SET FOREIGN_KEY_CHECKS=1;