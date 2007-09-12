-- 
-- packages/lams2int/sql/postgresql/lams2int-package-create.sql
-- 
-- @author Ernie Ghiglione (ErnieG@melcoe.mq.edu.au)
-- @creation-date 2007-04-16
-- @cvs-id $Id$
--

create or replace function lams_lesson__new (
    integer,   -- learning_session_id
    varchar,   -- display_title
    varchar,   -- introduction
    boolean,   -- hide
    timestamp with time zone, -- creation_date
    integer,   -- creation_user
    varchar,    -- creation_ip
    integer,    -- package_id
    integer    -- community_id
)
returns integer as '
declare
    p_learning_session_id   alias for $1;
    p_display_title         alias for $2;
    p_introduction          alias for $3;
    p_hide                  alias for $4;
    p_creation_date         alias for $5;
    p_creation_user         alias for $6;
    p_creation_ip           alias for $7;
    p_package_id            alias for $8;
    p_community_id          alias for $9;

    v_lesson_id       integer;
begin
        v_lesson_id := acs_object__new (
                v_lesson_id,               -- object_id
                ''lams_lesson'',       -- object_type
                p_creation_date,        -- creation_date
                p_creation_user,        -- creation_user
                p_creation_ip,          -- creation_ip
                p_package_id,           -- context_id
                ''t''                   -- security_inherit_p
        );

        insert into lams_lessons
        (lesson_id, user_id, community_id, learning_session_id, display_title, introduction, hide, start_time, package_id)
        values
        (v_lesson_id, p_creation_user, p_community_id, p_learning_session_id, p_display_title, p_introduction, p_hide, p_creation_date, p_package_id);
        return v_lesson_id;
end;
' language 'plpgsql';

create or replace function lams_lesson__delete (
    integer    -- lesson_id
)
returns integer as '
declare 
    p_lesson_id        alias for $1;
begin
        perform acs_object__delete(p_lesson_id);
        delete from lams_lessons where lesson_id = p_lesson_id;
        return 0;
end;
' language 'plpgsql';