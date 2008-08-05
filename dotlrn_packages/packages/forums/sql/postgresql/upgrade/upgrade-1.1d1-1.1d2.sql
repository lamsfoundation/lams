-- replace new functions with ones that set acs_object.title, package_id

create or replace function forums_forum__new (integer,varchar,varchar,varchar,varchar,varchar,integer,timestamptz,integer,varchar,integer)
returns integer as '
declare
    p_forum_id                      alias for $1;
    p_object_type                   alias for $2;
    p_name                          alias for $3;
    p_charter                       alias for $4;
    p_presentation_type             alias for $5;
    p_posting_policy                alias for $6;
    p_package_id                    alias for $7;
    p_creation_date                 alias for $8;
    p_creation_user                 alias for $9;
    p_creation_ip                   alias for $10;
    p_context_id                    alias for $11;
    v_forum_id                      integer;
begin
    v_forum_id:= acs_object__new(
        p_forum_id,
        p_object_type,
        p_creation_date,
        p_creation_user,
        p_creation_ip,
        coalesce(p_context_id, p_package_id),
        ''t'',
        p_name,
        p_package_id
    );

    insert into forums_forums
    (forum_id, name, charter, presentation_type, posting_policy, package_id)
    values
    (v_forum_id, p_name, p_charter, p_presentation_type, p_posting_policy, p_package_id);

    return v_forum_id;
end;
' language 'plpgsql';


create or replace function forums_message__new (integer,varchar,integer,varchar,text,char,integer,timestamptz,varchar,integer,timestamptz,integer,varchar,integer)
returns integer as '
declare
    p_message_id                    alias for $1;
    p_object_type                   alias for $2;
    p_forum_id                      alias for $3;
    p_subject                       alias for $4;
    p_content                       alias for $5;
    p_html_p                        alias for $6;
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

    if p_posting_date is null then
        v_posting_date = now();
    else
        v_posting_date = p_posting_date;
    end if;

    insert into forums_messages
    (message_id, forum_id, subject, content, html_p, user_id, posting_date, parent_id, state)
    values
    (v_message_id, p_forum_id, p_subject, p_content, p_html_p, p_user_id, v_posting_date, p_parent_id, v_state);

    update forums_forums
    set last_post = v_posting_date
    where forum_id = p_forum_id;

    update forums_messages
    set last_child_post = v_posting_date
    where message_id = forums_message__root_message_id(v_message_id);
 
    return v_message_id;

end;
' language 'plpgsql';
