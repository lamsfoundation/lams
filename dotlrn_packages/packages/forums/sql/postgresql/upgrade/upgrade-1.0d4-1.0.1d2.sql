alter table forums_messages add format varchar(30);
alter table forums_messages alter column format set default 'text/plain';
alter table forums_messages add constraint forums_mess_format_ck check (format in ('text/enhanced', 'text/plain', 'text/fixed-width', 'text/html'));

update forums_messages
set format = 'text/html'
where html_p = 't';
update forums_messages
set format = 'text/plain'
where html_p = 'f';

alter table forums_messages drop column html_p cascade;

-- recreate the views 
create or replace view forums_messages_approved
as
    select *
    from forums_messages
    where state = 'approved';

create or replace view forums_messages_pending
as
    select *
    from forums_messages
    where state= 'pending';


-- taken from forums-messages-package-create.sql

select define_function_args ('forums_message__new', 'message_id,object_type;forums_message,forum_id,subject,content,format,user_id,posting_date,state,parent_id,creation_date,creation_user,creation_ip,context_id');

create or replace function forums_message__new (integer,varchar,integer,varchar,text,char,integer,timestamptz,varchar,integer,timestamptz,integer,varchar,integer)
returns integer as '
declare
    p_message_id                    alias for $1;
    p_object_type                   alias for $2;
    p_forum_id                      alias for $3;
    p_subject                       alias for $4;
    p_content                       alias for $5;
    p_format                        alias for $6;
    p_user_id                       alias for $7;
    p_posting_date                  alias for $8;
    p_state                         alias for $9;
    p_parent_id                     alias for $10;
    p_creation_date                 alias for $11;
    p_creation_user                 alias for $12;
    p_creation_ip                   alias for $13;
    p_context_id                    alias for $14;
    v_message_id                    integer;
    v_forum_policy                  forums_forums.posting_policy%TYPE;
    v_state                         forums_messages.state%TYPE;
    v_posting_date                  forums_messages.posting_date%TYPE;
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

    if p_posting_date is null then
        v_posting_date = now();
    else
        v_posting_date = p_posting_date;
    end if;

    insert into forums_messages
    (message_id, forum_id, subject, content, format, user_id, posting_date, parent_id, state)
    values
    (v_message_id, p_forum_id, p_subject, p_content, p_format, p_user_id, v_posting_date, p_parent_id, v_state);

    update forums_forums
    set last_post = v_posting_date
    where forum_id = p_forum_id;

    update forums_messages
    set last_child_post = v_posting_date
    where message_id = forums_message__root_message_id(v_message_id);
 
    return v_message_id;

end;
' language 'plpgsql';
