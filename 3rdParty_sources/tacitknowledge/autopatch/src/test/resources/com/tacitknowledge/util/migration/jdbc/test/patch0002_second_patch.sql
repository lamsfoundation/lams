-- Note that the end of this statement does *not* have a semi-colon; this should
-- be OK since there's only one script in the file
insert into user_role_assoc (user_role_id, application_user_id, role_code, project_id) 
			values (nextval('role_id_seq'),2, 'SYSA', 3)