-- Make sure that forums actually have the package_id set.
update acs_objects set package_id = context_id where object_type = 'forums_forum';


create or replace package forums_forum
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
    ) return forums_forums.forum_id%TYPE;
    
    function name (
        forum_id in forums_forums.forum_id%TYPE
    ) return varchar;

    procedure del (
        forum_id in forums_forums.forum_id%TYPE
    );

end forums_forum;
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
            context_id => nvl(context_id, package_id), 
            package_id => forums_forum.new.package_id,
            title => name
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
    
    procedure del (
        forum_id in forums_forums.forum_id%TYPE
    )
    is
    begin
        acs_object.del(forum_id);
    end del;

end forums_forum;
/
show errors
