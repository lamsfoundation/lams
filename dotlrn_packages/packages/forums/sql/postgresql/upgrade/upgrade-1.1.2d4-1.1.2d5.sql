--
-- Most of the forum_* privileges are now deprecated.  A notable exception is
-- forum_moderate, which is little used but provides a way to grant moderation
-- privileges without granting full admin privileges.
--

update acs_permissions
set privilege = 'read'
where privilege = 'forum_read';

update acs_permissions
set privilege = 'write'
where privilege = 'forum_write';

update acs_permissions
set privilege = 'create'
where privilege = 'forum_create';

update acs_permissions
set privilege = 'delete'
where privilege = 'forum_delete';

-- As best as we can tell this is never used even though it's granted
-- by dotlrn-forums.

delete from acs_permissions where privilege = 'forum_post';

create function inline_0 ()
returns integer as'
begin
  perform acs_privilege__remove_child(''read'',''forum_read'');
  perform acs_privilege__remove_child(''create'',''forum_create'');
  perform acs_privilege__remove_child(''write'',''forum_write'');
  perform acs_privilege__remove_child(''delete'',''forum_delete'');
  perform acs_privilege__remove_child(''forum_moderate'',''forum_read'');
  perform acs_privilege__remove_child(''forum_moderate'',''forum_post'');
  perform acs_privilege__remove_child(''forum_write'',''forum_read'');
  perform acs_privilege__remove_child(''forum_write'',''forum_post'');

  perform acs_privilege__drop_privilege(''forum_read'');
  perform acs_privilege__drop_privilege(''forum_create'');
  perform acs_privilege__drop_privilege(''forum_write'');
  perform acs_privilege__drop_privilege(''forum_post'');
  perform acs_privilege__drop_privilege(''forum_delete'');

  return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0();


