
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
begin;
    -- The standard privilege 'admin' on a package allows a user to
    -- create forums (enforced by URL).
    -- The standard privilege 'create' on a forum allows a user to
    -- create new threads.
    -- The standard privilege 'write' on a message allows a user to
    -- post a follow up message.

    -- forum_moderate lets us grant moderation without granting full admin
    select acs_privilege__create_privilege('forum_moderate',null,null);
    select acs_privilege__add_child('admin','forum_moderate');
    select acs_privilege__add_child('forum_moderate','create');
    select acs_privilege__add_child('forum_moderate','delete');
    select acs_privilege__add_child('forum_moderate','read');
    select acs_privilege__add_child('forum_moderate','write');

    --return null;
end;

create table forums_forums (
    forum_id                        integer
                                    constraint forums_forum_id_nn
                                    not null
                                    constraint forums_forum_id_fk
                                    references acs_objects (object_id)
                                    constraint forums_forum_id_pk
                                    primary key,
    name                            varchar(200)
                                    constraint forums_name_nn
                                    not null,
    charter                         varchar(4000),
    presentation_type               varchar(100) 
                                    constraint forums_presentation_type_nn
                                    not null
                                    constraint forums_presentation_type_ck
                                    check (presentation_type in ('flat','threaded')),
    posting_policy                  varchar(100)
                                    constraint forums_posting_policy_nn
                                    not null
                                    constraint forums_posting_policy_ck
                                    check (posting_policy in ('open','moderated','closed')),
    max_child_sortkey               varbit,
    enabled_p                       char(1)
                                    default 't'
                                    constraint forums_enabled_p_nn
                                    not null
                                    constraint forums_enabled_p_ck
                                    check (enabled_p in ('t','f')),
    package_id                      integer
                                    constraint forums_package_id_nn
                                    not null,
    thread_count                    integer default 0,
    approved_thread_count           integer default 0,
    forums_forums                   integer default 0,
    last_post                       timestamptz
);

CREATE INDEX forums_forums_pkg_enable_idx
  ON forums_forums
  USING btree
  (package_id, enabled_p);

create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't';

create function inline_0 ()
returns integer as'
begin
    perform acs_object_type__create_type(
        ''forums_forum'',
        ''Forums Forum'',
        ''Forums Forums'',
        ''acs_object'',
        ''forums_forums'',
        ''forum_id'',
        ''forums_forum'',
        ''f'',
        null,
        ''forums_forum__name''
    );

    return null;
end;' language 'plpgsql';

select inline_0();
drop function inline_0();
