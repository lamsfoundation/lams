
   insert into user_role_assoc (user_role_id, application_user_id, role_code, project_id) 
			values (nextval('role_id_seq'),2, 'SYSA', 3);    

// Testing
insert into user_role_assoc (user_role_id, application_user_id, role_code, project_id) 
			values (nextval('role_id_seq'),3, 'SYSA', 3);

   -- This is a comment
insert into user_role_assoc (user_role_id, application_user_id, role_code, project_id) 
			values (nextval('role_--id_seq;'),4, 'SYSA', 3);
			
	