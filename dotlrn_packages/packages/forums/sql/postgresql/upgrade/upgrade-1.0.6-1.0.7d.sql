alter table forums_forums add column thread_count integer;
alter table forums_forums alter thread_count set default 0;

alter table forums_forums add column approved_thread_count integer;
alter table forums_forums alter approved_thread_count set default 0;

update forums_forums
set approved_thread_count = (select count(message_id)
                             from forums_messages_approved fm
                             where fm.forum_id=forums_forums.forum_id
                               and fm.parent_id is null),
    thread_count = (select count(message_id)
                    from forums_messages fm
                    where fm.forum_id=forums_forums.forum_id
                      and fm.parent_id is null);

alter table forums_messages add column reply_count integer;
alter table forums_messages alter reply_count set default 0;

alter table forums_messages add column approved_reply_count integer;
alter table forums_messages alter approved_reply_count set default 0;

-- Need to drop and recreate because Postgres doesn't allow one to change the
-- number of columns in a view when you do a "replace".

drop view forums_forums_enabled;
create view forums_forums_enabled
as
    select *
    from forums_forums
    where enabled_p = 't';

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

update forums_messages
set approved_reply_count = (select count(*)
                            from forums_messages_approved fm1
                            where fm1.tree_sortkey
                              between tree_left(forums_messages.tree_sortkey)
                              and tree_right(forums_messages.tree_sortkey)
                              and forums_messages.forum_id = fm1.forum_id),
     reply_count = (select count(*)
                    from forums_messages fm1
                    where fm1.tree_sortkey
                      between tree_left(forums_messages.tree_sortkey)
                      and tree_right(forums_messages.tree_sortkey)
                      and forums_messages.forum_id = fm1.forum_id)
where parent_id is null;

select define_function_args ('forums_message__new', 'message_id,object_type;forums_message,forum_id,subject,content,format,user_id,state,parent_id,creation_date,creation_user,creation_ip,context_id');

-- Get rid of the old version so we'll throw an error if the admin forgets to reboot
-- OpenACS after the upgrade (package_instantiate_object caches param lists)

drop function forums_message__new (integer,varchar,integer,varchar,text,char,integer,timestamptz,varchar,integer,timestamptz,integer,varchar,integer);

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
begin
    v_message_id := acs_object__new(
        p_message_id,
        p_object_type,
        p_creation_date,
        p_creation_user,
        p_creation_ip,
        coalesce(p_context_id, p_forum_id)
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
    (message_id, forum_id, subject, content, format, user_id, parent_id, state, last_child_post)
    values
    (v_message_id, p_forum_id, p_subject, p_content, p_format, p_user_id, p_parent_id,
     v_state, current_timestamp);

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
          last_child_post = current_timestamp
        where message_id = forums_message__root_message_id(v_message_id);
      else
        update forums_messages
        set reply_count = reply_count + 1,
          last_child_post = current_timestamp
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
      set approved_reply_count = approved_reply_count + 1
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

select define_function_args ('forums_message__delete_thread', 'message_id');

create or replace function forums_message__delete_thread (integer)
returns integer as '
declare
    p_message_id                    alias for $1;
    v_forum_id                      forums_messages.forum_id%TYPE;
    v_sortkey                       forums_messages.tree_sortkey%TYPE;
    v_message                       RECORD;
begin
    select forum_id, tree_sortkey
    into v_forum_id, v_sortkey
    from forums_messages
    where message_id = p_message_id;
           
    -- if it is already deleted
    if v_forum_id is null
    then return 0;
    end if;

    -- delete all children
    -- order by tree_sortkey desc to guarantee
    -- that we never delete a parent before its child
    -- sortkeys are beautiful
    for v_message in select *
                     from forums_messages
                     where forum_id = v_forum_id
                     and tree_sortkey between tree_left(v_sortkey) and tree_right(v_sortkey)
                     order by tree_sortkey desc
    loop
        -- Avoid the count bookkeeping down in forums_message__delete
        perform forums_message__delete(v_message.message_id);
    end loop;

    -- delete the message itself
    perform forums_message__delete(p_message_id);

    return 0;
end;' language 'plpgsql';

