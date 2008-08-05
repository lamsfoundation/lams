-- @author Jeff Davis davis@xarg.net
--
-- bug 1807 last_poster rather than first poster should be shown in forums index page
-- add a last_poster to support this and update triggers to support it.

alter table forums_messages add column last_poster integer
                                                   constraint forums_mess_last_poster_fk
                                                   references users(user_id);

-- Now populate the new column
-- this depends on last_child_post being properly set.
-- use min(user_id) just in case there are two that have the same timestamp)

update forums_messages set last_poster = (select min(user_id)
                            from forums_messages fm1
                            where fm1.posting_date = forums_messages.last_child_post
                              and forums_messages.forum_id = fm1.forum_id
                              and fm1.tree_sortkey
                                between tree_left(forums_messages.tree_sortkey)
                                and tree_right(forums_messages.tree_sortkey) )
where parent_id is null;

-- the better method above fails for some things (like notably openacs.org where 
-- the last_child_post may not exist in the child posts due to import and upgrade 
-- glitches.  try this one which will give us a name no matter what.
update forums_messages 
set last_poster = (select user_id
                     from forums_messages fm1
                    where fm1.message_id = (select max(message_id)
                                              from forums_messages fm2
                                             where forums_messages.forum_id = fm2.forum_id
                                               and fm2.tree_sortkey
                                                   between tree_left(forums_messages.tree_sortkey)
                                                   and tree_right(forums_messages.tree_sortkey) ))
where parent_id is null and last_poster is null;


-- Need to drop and recreate because Postgres doesn't allow one to change the
-- number of columns in a view when you do a "replace".
drop view forums_messages_approved;
create or replace view forums_messages_approved
as
    select *
    from forums_messages
    where state = 'approved';

drop view forums_messages_pending;
create or replace view forums_messages_pending
as
    select *
    from forums_messages
    where state= 'pending';


--
-- Replace the procs which manipulate state and new message to save last_poster.
--
create or replace function forums_message__new (integer,varchar,integer,varchar,text,char,integer,varchar,integer,timestamptz,integer,varchar,integer)
returns integer as '
declare
    p_message_id                    alias for $1;
    p_object_type                   alias for $2;
    p_forum_id                      alias for $3;
    p_subject                       alias for $4;
    p_content                       alias for $5;
    p_format                        alias for $6;
    p_user_id                       alias for $7;
    p_state                         alias for $8;
    p_parent_id                     alias for $9;
    p_creation_date                 alias for $10;
    p_creation_user                 alias for $11;
    p_creation_ip                   alias for $12;
    p_context_id                    alias for $13;
    v_message_id                    integer;
    v_forum_policy                  forums_forums.posting_policy%TYPE;
    v_state                         forums_messages.state%TYPE;
    v_posting_date                  forums_messages.posting_date%TYPE;
    v_package_id                    acs_objects.package_id%TYPE;
begin

    select package_id into v_package_id from forums_forums where forum_id = p_forum_id;

    if v_package_id is null then
        raise exception ''forums_message__new: forum_id % not found'', p_forum_id;
    end if;

    v_message_id := acs_object__new(
        p_message_id,
        p_object_type,
        p_creation_date,
        p_creation_user,
        p_creation_ip,
        coalesce(p_context_id, p_forum_id),
        ''t'',
        p_subject,
        v_package_id
    );

    if p_state is null then
        select posting_policy
        into v_forum_policy
        from forums_forums
        where forum_id = p_forum_id;

        if v_forum_policy = ''moderated''
        then v_state := ''pending'';
        else v_state := ''approved'';
        end if;
    else
        v_state := p_state;
    end if;

    insert into forums_messages
    (message_id, forum_id, subject, content, format, user_id, parent_id, state, last_child_post, last_poster)
    values
    (v_message_id, p_forum_id, p_subject, p_content, p_format, p_user_id, p_parent_id,
     v_state, current_timestamp, p_user_id);

    update forums_forums
    set last_post = current_timestamp
    where forum_id = p_forum_id;

    if p_parent_id is null then
      if v_state = ''approved'' then
        update forums_forums
        set thread_count = thread_count + 1,
          approved_thread_count = approved_thread_count + 1
        where forum_id=p_forum_id;
      else
        update forums_forums
        set thread_count = thread_count + 1
        where forum_id=p_forum_id;
      end if;
    else
      if v_state = ''approved'' then
        update forums_messages
        set approved_reply_count = approved_reply_count + 1,
          reply_count = reply_count + 1,
          last_poster = p_user_id,
          last_child_post = current_timestamp
        where message_id = forums_message__root_message_id(v_message_id);
      else
        -- dont update last_poster, last_child_post when not approved
        update forums_messages
        set reply_count = reply_count + 1
        where message_id = forums_message__root_message_id(v_message_id);
      end if;
    end if;

    return v_message_id;

end;' language 'plpgsql';

select define_function_args ('forums_message__set_state', 'message_id,state');

create or replace function forums_message__set_state(integer,varchar) returns integer as '
declare
  p_message_id      alias for $1;
  p_state           alias for $2;
  v_cur             record;
begin

  select into v_cur *
  from forums_messages
  where message_id = p_message_id;

  if v_cur.parent_id is null then
    if p_state = ''approved'' and v_cur.state <> ''approved'' then
      update forums_forums
      set approved_thread_count = approved_thread_count + 1
      where forum_id=v_cur.forum_id;
    elsif p_state <> ''approved'' and v_cur.state = ''approved'' then
      update forums_forums
      set approved_thread_count = approved_thread_count - 1
      where forum_id=v_cur.forum_id;
    end if;
  else
    if p_state = ''approved'' and v_cur.state <> ''approved'' then
      update forums_messages
      set approved_reply_count = approved_reply_count + 1, 
        last_poster = (case when v_cur.posting_date > last_child_post then v_cur.user_id else last_poster end),
        last_child_post = (case when v_cur.posting_date > last_child_post then v_cur.posting_date else last_child_post end)
      where message_id = forums_message__root_message_id(v_cur.message_id);
    elsif p_state <> ''approved'' and v_cur.state = ''approved'' then
      update forums_messages
      set approved_reply_count = approved_reply_count - 1
      where message_id = forums_message__root_message_id(v_cur.message_id);
    end if;
  end if;

  update forums_messages
  set state = p_state
  where message_id = p_message_id;

  return 0;

end;' language 'plpgsql';

select define_function_args ('forums_message__delete', 'message_id');

create or replace function forums_message__delete (integer)
returns integer as '
declare
  p_message_id      alias for $1;    
  v_cur             record;
begin

  -- Maintain the forum thread counts

  select into v_cur *
  from forums_messages
  where message_id = p_message_id;

  if v_cur.parent_id is null then
    if v_cur.state = ''approved'' then
      update forums_forums
      set thread_count = thread_count - 1,
        approved_thread_count = approved_thread_count - 1
      where forum_id=v_cur.forum_id;
    else
      update forums_forums
      set thread_count = thread_count - 1
      where forum_id=v_cur.forum_id;
    end if;
  elsif v_cur.state = ''approved'' then
    update forums_messages
    set approved_reply_count = approved_reply_count - 1,
      reply_count = reply_count - 1
    where message_id = forums_message__root_message_id(v_cur.message_id);
  else
    update forums_messages
    set reply_count = reply_count - 1
    where message_id = forums_message__root_message_id(v_cur.message_id);
  end if;

  perform acs_object__delete(p_message_id);
  return 0;
end;' language 'plpgsql';
