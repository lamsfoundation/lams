alter table forums_messages add format varchar2(30) default 'text/plain';

update forums_messages
set format = 'text/html'
where html_p = 't';
update forums_messages
set format = 'text/plain'
where html_p = 'f';

alter table forums_messages add constraint forums_mess_format_ck check (format in ('text/enhanced', 'text/plain', 'text/fixed-width', 'text/html'));
alter table forums_messages drop column html_p;

-- forums-messages-package-create.sql
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- The Package for Messages
--
-- This code is newly concocted by Ben, but with heavy concepts and heavy code
-- chunks lifted from Gilbert. Thanks Orchard Labs!
--

create or replace package forums_message
as

    function new (
        message_id in forums_messages.message_id%TYPE default null,
        object_type in acs_objects.object_type%TYPE default 'forums_message',
        forum_id in forums_messages.forum_id%TYPE,
        subject in forums_messages.subject%TYPE,
        content in varchar,
        format in forums_messages.format%TYPE default 'text/plain',
        user_id in forums_messages.user_id%TYPE,
        posting_date in forums_messages.posting_date%TYPE default sysdate,
        state in forums_messages.state%TYPE default null,
        parent_id in forums_messages.parent_id%TYPE default null,
        creation_date in acs_objects.creation_date%TYPE default sysdate,
        creation_user in acs_objects.creation_user%TYPE,
        creation_ip in acs_objects.creation_ip%TYPE,
        context_id in acs_objects.context_id%TYPE default null
    ) return forums_messages.message_id%TYPE;

    function root_message_id (
        message_id in forums_messages.message_id%TYPE
    ) return forums_messages.message_id%TYPE;

    procedure thread_open (
        message_id in forums_messages.message_id%TYPE
    );

    procedure thread_close (
        message_id in forums_messages.message_id%TYPE
    );

    procedure del (
        message_id in forums_messages.message_id%TYPE
    );

    procedure delete_thread (
        message_id in forums_messages.message_id%TYPE
    );

    function name (
        message_id in forums_messages.message_id%TYPE
    ) return varchar;

end forums_message;
/
show errors

create or replace package body forums_message
as

    function new (
        message_id in forums_messages.message_id%TYPE default null,
        object_type in acs_objects.object_type%TYPE default 'forums_message',
        forum_id in forums_messages.forum_id%TYPE,
        subject in forums_messages.subject%TYPE,
        content in varchar,
        format in forums_messages.format%TYPE default 'text/plain',
        user_id in forums_messages.user_id%TYPE,
        posting_date in forums_messages.posting_date%TYPE default sysdate,
        state in forums_messages.state%TYPE default null,
        parent_id in forums_messages.parent_id%TYPE default null,
        creation_date in acs_objects.creation_date%TYPE default sysdate,
        creation_user in acs_objects.creation_user%TYPE,
        creation_ip in acs_objects.creation_ip%TYPE,
        context_id in acs_objects.context_id%TYPE default null
    ) return forums_messages.message_id%TYPE
    is
        v_message_id acs_objects.object_id%TYPE;
        v_sortkey forums_messages.tree_sortkey%TYPE;
        v_forum_policy forums_forums.posting_policy%TYPE;
        v_state forums_messages.state%TYPE;
    begin

        v_message_id := acs_object.new(
            object_id => message_id,
            object_type => object_type,
            creation_date => creation_date,
            creation_user => creation_user,
            creation_ip => creation_ip,
            context_id => nvl(context_id, forum_id)
        );

        if state is null
        then
            select posting_policy
            into v_forum_policy
            from forums_forums
            where forum_id= new.forum_id;

            if v_forum_policy = 'moderated' then
                v_state := 'pending';
            else
                v_state := 'approved';
            end if;
        else
            v_state := state;
        end if;

        insert into forums_messages
        (message_id, forum_id, subject, content, format, user_id, posting_date, parent_id, state)
        values
        (v_message_id, forum_id, subject, content, format, user_id, posting_date, parent_id, v_state);

        -- DRB: Can't use root_message_id() here because it triggers a "mutating table" error

        select tree_sortkey into v_sortkey
        from forums_messages
        where message_id = v_message_id;

        update forums_forums
        set last_post = forums_message.new.posting_date
        where forum_id = forums_message.new.forum_id;

        update forums_messages
        set last_child_post = forums_message.new.posting_date
        where forum_id = forums_message.new.forum_id
          and tree_sortkey = tree.ancestor_key(v_sortkey, 1);
        
        return v_message_id;
    end new;

    function root_message_id (
        message_id in forums_messages.message_id%TYPE
    ) return forums_messages.message_id%TYPE
    is
        v_message_id forums_messages.message_id%TYPE;
        v_forum_id forums_messages.forum_id%TYPE;
        v_sortkey forums_messages.tree_sortkey%TYPE;
    begin
        select forum_id, tree_sortkey
        into v_forum_id, v_sortkey
        from forums_messages
        where message_id = root_message_id.message_id;

        select message_id
        into v_message_id
        from forums_messages
        where forum_id = v_forum_id
        and tree_sortkey = tree.ancestor_key(v_sortkey, 1);

        return v_message_id;
    end root_message_id;

    procedure thread_open (
        message_id in forums_messages.message_id%TYPE
    )
    is
        v_forum_id forums_messages.forum_id%TYPE;
        v_sortkey forums_messages.tree_sortkey%TYPE;
    begin
        select forum_id, tree_sortkey
        into v_forum_id, v_sortkey
        from forums_messages
        where message_id = thread_open.message_id;

        update forums_messages
        set open_p = 't'
        where tree_sortkey between tree.left(v_sortkey) and tree.right(v_sortkey)
        and forum_id = v_forum_id;

        update forums_messages
        set open_p = 't'
        where message_id = thread_open.message_id;
    end thread_open;

    procedure thread_close (
        message_id in forums_messages.message_id%TYPE
    )
    is
        v_forum_id forums_messages.forum_id%TYPE;
        v_sortkey forums_messages.tree_sortkey%TYPE;
    begin
        select forum_id, tree_sortkey
        into v_forum_id, v_sortkey
        from forums_messages
        where message_id = thread_close.message_id;

        update forums_messages
        set open_p = 'f'
        where tree_sortkey between tree.left(v_sortkey) and tree.right(v_sortkey)
        and forum_id = v_forum_id;

        update forums_messages
        set open_p = 'f'
        where message_id = thread_close.message_id;
    end thread_close;

    procedure del (
        message_id in forums_messages.message_id%TYPE
    )
    is
    begin
        acs_object.del(message_id);
    end del;

    procedure delete_thread (
        message_id in forums_messages.message_id%TYPE
    )
    is
        v_forum_id forums_messages.forum_id%TYPE;
        v_sortkey forums_messages.tree_sortkey%TYPE;
        v_message forums_messages%ROWTYPE;
    begin
        select forum_id, tree_sortkey
        into v_forum_id, v_sortkey
        from forums_messages
        where message_id = delete_thread.message_id;

        -- if it's already deleted
        if SQL%NOTFOUND then
            return;
        end if;

        -- delete all children
        -- order by tree_sortkey desc to guarantee
        -- that we never delete a parent before its child
        -- sortkeys are beautiful
        for v_message in (select *
                          from forums_messages
                          where forum_id = v_forum_id
                          and tree_sortkey between tree.left(v_sortkey) and tree.right(v_sortkey)
                          order by tree_sortkey desc)
        loop
            forums_message.del(v_message.message_id);
        end loop;

        -- delete the message itself
        forums_message.del(delete_thread.message_id);
    end delete_thread;

    function name (
        message_id in forums_messages.message_id%TYPE
    ) return varchar
    is
        v_name forums_messages.subject%TYPE;
    begin
        select subject
        into v_name
        from forums_messages
        where message_id = forums_message.name.message_id;

        return v_name;
    end name;

end forums_message;
/
show errors


create or replace view forums_messages_approved as
    select *
    from forums_messages
    where state = 'approved';

create or replace view forums_messages_pending as
    select *
    from forums_messages
    where state = 'pending';

