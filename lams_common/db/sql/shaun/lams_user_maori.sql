SET FOREIGN_KEY_CHECKS=0;

-- need a new workspace_id and the name changes for each user.
insert into lams_workspace_folder(workspace_folder_id,parent_folder_id,name,workspace_id,user_id,create_date_time,last_modified_date_time,lams_workspace_folder_type_id) 
values(20,null,'Maori Workspace',20,20, now(),now(),1);
insert into lams_workspace (workspace_id, root_folder_id, name) values (20,null,'Maori Test');

insert into lams_user(user_id,login,password,title,first_name,last_name,address_line_1,address_line_2,address_line_3,city,state,country,day_phone,evening_phone,mobile_phone,fax,email,disabled_flag,create_date,authentication_method_id,workspace_id, flash_theme_id,html_theme_id,locale_language,locale_country) 
values(20,'maori','maori','Dr','Maori','Test','20', 'Maori Ave',null,'Auckland',null,'New Zealand','0211111111','0211111112','0411111111','0211111113','maori@xx.os',0,'20041223',1,20,1,3,'mi','nz');

-- need a new user_organisation_id for each row, user_id must match user_id created in the lams_user
-- belongs to course Playpen, Class Everybody
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (52, 2, 20);
INSERT INTO lams_user_organisation (user_organisation_id, organisation_id, user_id) VALUES (53, 3, 20);

-- need a new user_organisation_role_id for each row, use the same role_ids (3,4,6) for author, learner, teacher
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (127,52,3);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (128,52,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (129,52,6);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (130,53,4);
INSERT INTO lams_user_organisation_role (user_organisation_role_id, user_organisation_id, role_id) VALUES (131,53,6);

SET FOREIGN_KEY_CHECKS=1;