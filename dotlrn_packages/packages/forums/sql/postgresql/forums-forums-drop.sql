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

create function inline_0 ()
returns integer as '
begin

    delete from acs_permissions where privilege in (''forum_moderate'');

    -- remove children
    perform acs_privilege__remove_child(''admin'',''forum_moderate'');
    perform acs_privilege__remove_child(''forum_moderate'',''create'');
    perform acs_privilege__remove_child(''forum_moderate'',''delete'');
    perform acs_privilege__remove_child(''forum_moderate'',''read'');
    perform acs_privilege__remove_child(''forum_moderate'',''write'');

    perform acs_privilege__drop_privilege(''forum_moderate'');

    return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0 ();

DROP INDEX forums_forums_pkg_enable_idx;
drop view forums_forums_enabled;
drop table forums_forums;

create function inline_0 ()
returns integer as '
begin
    perform acs_object_type__drop_type (
        ''forums_forum'', ''f''
    );

    return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0 ();
