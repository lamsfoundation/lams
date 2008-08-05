-- Update the package ids for projects
create function inline_0 ()
returns integer as '
declare
    ct RECORD;
begin
  for ct in select package_id, forum_id from forums_forums
  loop
	update acs_objects set package_id = ct.package_id where object_id = ct.forum_id;
  end loop;

  return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0();
