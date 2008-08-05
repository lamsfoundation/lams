-- Make sure that forums actually have the package_id set.
update acs_objects set package_id = context_id where object_type = 'forums_forum';

-- And now rewrite the new function as old installations will not have this updated version that stores the package_id
select define_function_args('forums_forum__new','forum_id,object_type;forums_forum,name,charter,presentation_type,posting_policy,package_id,creation_date,creation_user,creation_ip,context_id');

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
