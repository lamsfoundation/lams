alter table forums_forums add (last_post date);
alter table forums_messages add (last_child_post date);

declare
  v_date date; 
begin

  for row in (select forum_id
                  from forums_forums)
  loop

    select last_modified into v_date
    from acs_objects
    where object_id = row.forum_id;

    update forums_forums
    set last_post = v_date
    where forum_id = row.forum_id;

  end loop;

  for row in (select message_id
                  from forums_messages
                  where parent_id is null)
  loop

    -- forums 0.1d did not properly set the last_modified field of the object
    -- row associated with the root message of a thread, so we need to calculate
    -- it here.

    select max(o.last_modified) into v_date
    from acs_objects o, forums_messages fm
    where forums_message.root_message_id(fm.message_id) = row.message_id
      and object_id = fm.message_id;

    update forums_messages
    set last_child_post = v_date
    where message_id = row.message_id;

  end loop;

end;
/
show errors;

drop view forums_messages_approved;
create view forums_messages_approved
as
    select *
    from forums_messages
    where state = 'approved';

drop view forums_messages_pending;
create view forums_messages_pending
as
    select *
    from forums_messages
    where state= 'pending';

drop view forums_forums_enabled;
create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't';

create or replace package body forums_message
as

    function new (
        message_id in forums_messages.message_id%TYPE default null,
        object_type in acs_objects.object_type%TYPE default 'forums_message',
        forum_id in forums_messages.forum_id%TYPE,
        subject in forums_messages.subject%TYPE,
        content in varchar,
        html_p in forums_messages.html_p%TYPE default 'f',
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
        (message_id, forum_id, subject, content, html_p, user_id, posting_date, parent_id, state)
        values
        (v_message_id, forum_id, subject, content, html_p, user_id, posting_date, parent_id, v_state);

        -- DRB: Can't use root_message_id() here because it triggers a "mutating table" error

        select tree_sortkey into v_sortkey
        from forums_messages
        where message_id = v_message_id;

        update forums_forums
        set last_post = posting_date
        where forum_id = forums_message.new.forum_id;

        update forums_messages
        set last_child_post = posting_date
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

    procedure delete (
        message_id in forums_messages.message_id%TYPE
    )
    is
    begin
        acs_object.delete(message_id);
    end delete;

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
            forums_message.delete(v_message.message_id);
        end loop;

        -- delete the message itself
        forums_message.delete(delete_thread.message_id);
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

create or replace package body forums_forum
as

    function new (
        forum_id in forums_forums.forum_id%TYPE  default null,
        object_type in acs_objects.object_type%TYPE default 'forums_forum',
        name in forums_forums.name%TYPE,
        charter in forums_forums.charter%TYPE default null,
        presentation_type in forums_forums.presentation_type%TYPE,
        posting_policy in forums_forums.posting_policy%TYPE,
        package_id in forums_forums.package_id%TYPE,
        creation_date in acs_objects.creation_date%TYPE default sysdate,
        creation_user in acs_objects.creation_user%TYPE,
        creation_ip in acs_objects.creation_ip%TYPE,
        context_id in acs_objects.context_id%TYPE default null
    ) return forums_forums.forum_id%TYPE
    is
        v_forum_id forums_forums.forum_id%TYPE;
    begin
        v_forum_id := acs_object.new(
            object_id => forum_id,
            object_type => object_type,
            creation_date => creation_date,
            creation_user => creation_user,
            creation_ip => creation_ip,
            context_id => nvl(context_id, package_id)
        );
        
        insert into forums_forums
        (forum_id, name, charter, presentation_type, posting_policy, package_id)
        values
        (v_forum_id, name, charter, presentation_type, posting_policy, package_id);

        return v_forum_id;
    end new;

    function name (
        forum_id in forums_forums.forum_id%TYPE
    ) return varchar
    is
        v_name forums_forums.name%TYPE;
    begin
        select name
        into v_name
        from forums_forums
        where forum_id = name.forum_id;

        return v_name;
    end name;
    
    procedure delete (
        forum_id in forums_forums.forum_id%TYPE
    )
    is
    begin
        acs_object.delete(forum_id);
    end delete;

end forums_forum;
/
show errors
