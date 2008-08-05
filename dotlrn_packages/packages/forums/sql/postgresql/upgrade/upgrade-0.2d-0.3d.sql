-- forums/sql/postgresql/upgrade-0.2d-0.3d.sql
--
-- Changes for scalability davis@xarg.net

create unique index forums_mess_forum_sk_un on forums_messages(forum_id, tree_sortkey);

create or replace function forums_message__root_message_id (integer)
returns integer as '
declare
    p_message_id                    alias for $1;
    v_message_id                    forums_messages.message_id%TYPE;
    v_forum_id                      forums_messages.forum_id%TYPE;
    v_sortkey                       forums_messages.tree_sortkey%TYPE;
begin
    select forum_id, tree_sortkey
    into v_forum_id, v_sortkey
    from forums_messages
    where message_id = p_message_id;

    select message_id
    into v_message_id
    from forums_messages
    where forum_id = v_forum_id
    and tree_sortkey = tree_ancestor_key(v_sortkey, 1);

    return v_message_id;
end;
' language 'plpgsql' with(isstrict,iscachable);
