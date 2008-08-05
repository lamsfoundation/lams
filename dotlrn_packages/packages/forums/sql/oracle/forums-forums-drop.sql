
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with heavy concepts and heavy code
-- chunks lifted from Gilbert. Thanks Orchard Labs.
--

-- privileges
declare
begin

   delete from acs_permissions where privilege in ('forum_moderate');

   -- remove children
   acs_privilege.remove_child('admin','forum_moderate');
   acs_privilege.remove_child('forum_moderate','create');
   acs_privilege.remove_child('forum_moderate','delete');
   acs_privilege.remove_child('forum_moderate','read');
   acs_privilege.remove_child('forum_moderate','write');
   
   acs_privilege.drop_privilege('forum_moderate');
end;
/
show errors


--
-- The Data Model
--

drop view forums_forums_enabled;

drop table forums_forums;

--
-- Object Type
--

declare
    v_object_id	    integer;
begin

   select MAX(object_id) into v_object_id from acs_objects where object_type='forums_forum';
   While (v_object_id > 0) loop
	delete from ncanotes where object_id=v_object_id;
   	acs_object.del(
		v_object_id -- object_id
  	);
	select MAX(object_id) into v_object_id from acs_objects where object_type='forums_forum';
   End loop;

   acs_object_type.drop_type (
	object_type => 'forums_forum'
   );

end;
/
show errors
