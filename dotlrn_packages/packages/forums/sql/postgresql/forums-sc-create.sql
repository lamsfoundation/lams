-- forums service contracts for Search package
-- dave bauer <dave@thedesignexperience.org>
-- August 7, 2002

-- jcd: 2004-04-01 moved the sc create to the tcl callbacks, and added one for forum_forum objtype
-- TODO-JCD: trigger for forums_forums

-- til: only indexing full threads. changes to child messages will be treated as 
-- change to the thread.

create or replace function forums_message_search__itrg ()
returns trigger as '
begin
    if new.parent_id is null then
        perform search_observer__enqueue(new.message_id,''INSERT'');
    else
        perform search_observer__enqueue(forums_message__root_message_id(new.parent_id),''UPDATE'');
    end if;
    return new;
end;' language 'plpgsql';

create or replace function forums_message_search__dtrg ()
returns trigger as '
declare
     v_root_message_id          forums_messages.message_id%TYPE;
begin
    -- if the deleted msg has a parent then its an UPDATE to a thread, otherwise a DELETE.

    if old.parent_id is null then
        perform search_observer__enqueue(old.message_id,''DELETE'');
    else
        v_root_message_id := forums_message__root_message_id(old.parent_id);
        if not v_root_message_id is null then
            perform search_observer__enqueue(v_root_message_id,''UPDATE'');
        end if;
    end if;

    return old;
end;' language 'plpgsql';

create or replace function forums_message_search__utrg ()
returns trigger as '
begin
    perform search_observer__enqueue(forums_message__root_message_id (old.message_id),''UPDATE'');
    return old;
end;' language 'plpgsql';


create trigger forums_message_search__itrg after insert on forums_messages
for each row execute procedure forums_message_search__itrg (); 

create trigger forums_message_search__dtrg after delete on forums_messages
for each row execute procedure forums_message_search__dtrg (); 

create trigger forums_message_search__utrg after update on forums_messages
for each row execute procedure forums_message_search__utrg (); 



-- forums_forums indexing trigger
create or replace function forums_forums_search__itrg ()
returns trigger as '
begin
    perform search_observer__enqueue(new.forum_id,''INSERT'');

    return new;
end;' language 'plpgsql';

create or replace function forums_forums_search__utrg ()
returns trigger as '
begin
    perform search_observer__enqueue(new.forum_id,''UPDATE'');

    return new;
end;' language 'plpgsql';

create or replace function forums_forums_search__dtrg ()
returns trigger as '
begin
    perform search_observer__enqueue(old.forum_id,''DELETE'');

    return old;
end;' language 'plpgsql';



create trigger forums_forums_search__itrg after insert on forums_forums
for each row execute procedure forums_forums_search__itrg (); 

create trigger forums_forums_search__utrg after update on forums_forums
for each row execute procedure forums_forums_search__utrg (); 

create trigger forums_forums_search__dtrg after delete on forums_forums
for each row execute procedure forums_forums_search__dtrg (); 

