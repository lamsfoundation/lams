--
-- Most of the forum_* privileges are now deprecated.  A notable exception is
-- forum_moderate, which is little used but provides a way to grant moderation
-- privileges without granting full admin privileges.
--
-- This script, plus accompanying changes in other parts of this package,
-- finishes that job started in upgrade-1.1.1-1.1.2d1.sql.
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

begin
  acs_privilege.remove_child('read','forum_read');
  acs_privilege.remove_child('create','forum_create');
  acs_privilege.remove_child('write','forum_write');
  acs_privilege.remove_child('delete','forum_delete');
  acs_privilege.remove_child('forum_moderate','forum_read');
  acs_privilege.remove_child('forum_moderate','forum_post');
  acs_privilege.remove_child('forum_write','forum_read');
  acs_privilege.remove_child('forum_write','forum_post');

  acs_privilege.drop_privilege('forum_read');
  acs_privilege.drop_privilege('forum_create');
  acs_privilege.drop_privilege('forum_write');
  acs_privilege.drop_privilege('forum_post');
  acs_privilege.drop_privilege('forum_delete');
end;
/
show errors;
