
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
    -- The standard privilege 'admin' on a package allows a user to
    -- create forums (enforced by URL).
    -- The standard privilege 'create' on a forum allows a user to
    -- create new threads.
    -- The standard privilege 'write' on a message allows a user to
    -- post a follow up message.

    -- forum_moderate lets us grant moderation without granting full admin
    acs_privilege.create_privilege('forum_moderate',null,null);
    acs_privilege.add_child('admin','forum_moderate');
    acs_privilege.add_child('forum_moderate','create');
    acs_privilege.add_child('forum_moderate','delete');
    acs_privilege.add_child('forum_moderate','read');
    acs_privilege.add_child('forum_moderate','write');

end;
/
show errors

create table forums_forums (
    forum_id                        integer
                                    constraint forums_forum_id_fk
                                    references acs_objects (object_id)
                                    constraint forums_forums_pk
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
    max_child_sortkey               raw(100),
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
    last_post                       date
);

create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't';

declare
begin
    acs_object_type.create_type(
        supertype => 'acs_object',
        object_type => 'forums_forum',
        pretty_name => 'Forums Forum',
        pretty_plural => 'Forums Forums',
        table_name => 'forums_forums',
        id_column => 'forum_id',
        package_name => 'forums_forum',
        name_method => 'forums_forum.name'
    );
end;
/
show errors
