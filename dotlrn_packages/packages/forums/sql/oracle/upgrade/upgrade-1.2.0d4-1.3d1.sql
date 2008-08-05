-- Update the package ids for projects
declare
	cursor ct is
	select package_id, forum_id from forums_forums;
begin
	for v_ct in ct loop
		update acs_objects set package_id = v_ct.package_id where object_id = v_ct.forum_id;
	end loop;
end;
/
show errors
