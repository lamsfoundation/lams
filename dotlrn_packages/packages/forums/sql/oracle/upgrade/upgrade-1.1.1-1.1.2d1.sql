update acs_permissions
 set privilege = 'read'
 where privilege = 'forum_read'
 and not exists (select 1
    from acs_permissions p1
    where p1.object_id = acs_permissions.object_id
    and p1.grantee_id = acs_permissions.grantee_id
    and p1.privilege = 'read');

delete from acs_permissions
  where privilege = 'forum_read';

update acs_permissions
 set privilege = 'write'
 where privilege = 'forum_write'
    and not exists (select 1
    from acs_permissions p1
    where p1.object_id = acs_permissions.object_id
    and p1.grantee_id = acs_permissions.grantee_id
    and p1.privilege = 'write');

delete from acs_permissions
  where privilege = 'forum_write';

update acs_permissions
 set privilege = 'create'
 where privilege = 'forum_create'
    and not exists (select 1
    from acs_permissions p1
    where p1.object_id = acs_permissions.object_id
    and p1.grantee_id = acs_permissions.grantee_id
    and p1.privilege = 'create');

delete from acs_permissions
  where privilege = 'forum_create';
