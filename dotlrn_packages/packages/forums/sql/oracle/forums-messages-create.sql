--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert. Thanks Orchard Labs!
--

create table forums_messages (
    message_id                      integer
                                    constraint forums_message_id_fk
                                    references acs_objects (object_id)
                                    constraint forums_messages_pk
                                    primary key,
    forum_id                        integer
                                    constraint forums_mess_forum_id_fk
                                    references forums_forums (forum_id),
    subject                         varchar(200),
    content                         clob,
    user_id                         integer
                                    constraint forums_mess_user_id_fk
                                    references users(user_id)
                                    constraint forums_mess_user_id_nn
                                    not null,
    posting_date                    date
                                    default sysdate
                                    constraint forum_mess_post_date_nn
                                    not null,
    state                           varchar(100)
                                    constraint forum_mess_state_ck
                                    check (state in ('pending','approved','rejected')),
    format                          varchar2(30)
                                    default 'text/plain',
    -- Hierarchy of messages
    parent_id                       integer
                                    constraint forum_mess_parent_id_fk
                                    references forums_messages (message_id),
    open_p                          char(1)
                                    default 't'
                                    constraint forum_mess_open_p_nn
                                    not null
                                    constraint forum_mess_open_p_ck
                                    check (open_p in ('t','f')),
    tree_sortkey                    raw(240),
    max_child_sortkey               raw(100),
    last_child_post                 date,
    reply_count                     integer default 0
                                    constraint forums_mess_reply_count_ck
                                    check (reply_count >= 0),
    approved_reply_count            integer default 0
                                    constraint forums_mess_app_rep_count_ck
                                    check (approved_reply_count >= 0),
    last_poster                     integer
                                    constraint forums_mess_last_poster_fk
                                    references users(user_id),
    constraint forums_mess_sk_forum_un
    unique (tree_sortkey, forum_id)
);

-- We do a some big queries on forum_id (thread count on index.tcl) so create a second index 
-- ordered so it's useful for them
create unique index forums_mess_forum_sk_un on forums_messages(forum_id, tree_sortkey);
-- Need these for RI checks 
create index forums_messages_user_id_idx on forums_messages(user_id);
create index forums_messages_parent_id_idx on forums_messages(parent_id);
create index forum_messages_date_idx on forums_messages (forum_id, posting_date) compress 1;


create view forums_messages_approved as
    select *
    from forums_messages
    where state = 'approved';

create view forums_messages_pending as
    select *
    from forums_messages
    where state = 'pending';

declare
begin
    acs_object_type.create_type (
        supertype => 'acs_object',
        object_type => 'forums_message',
        pretty_name => 'Forums Message',
        pretty_plural => 'Forums Messages',
        table_name => 'forums_messages',
        id_column => 'message_id',
        package_name => 'forums_message',
        name_method => 'forums_message.name'
    );
end;
/
show errors
