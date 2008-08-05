create table forums_reading_info (
    root_message_id integer
                    constraint forum_read_parent_id_fk
                    references forums_messages (message_id)
                    on delete cascade,
    user_id         integer
                    constraint forums_read_user_id_fk
                    references users(user_id)
                    constraint forums_read_user_id_nn
                    not null,
    reading_date    timestamp
                    default current_timestamp
                    constraint forum_read_datetime_nn
                    not null,
		constraint forums_reading_info_pk primary key (root_message_id,user_id)
);

create index forums_reading_info_user_index on forums_reading_info (user_id);
create index forums_reading_info_forum_message_index on forums_reading_info (root_message_id);

create table forums_reading_info_user (
    forum_id        integer
                    constraint forums_read_forum_id_fk
                    references forums_forums (forum_id) on delete cascade,
    user_id         integer
                    constraint forums_read_user_id_fk
                    references users(user_id) on delete cascade
                    constraint forums_read_user_id_nn
                    not null,
    threads_read    integer 
                    default 0 
                    not null,
    constraint forums_reading_info_user_pk primary key (forum_id,user_id)              
);


-- remove reading_info for thread (upon new message, upon message deletion, or state change)
create or replace function forums_reading_info__remove_msg (integer)
returns integer as '
declare
    p_message_id                    alias for $1;
		v_forum_id                      integer;
    v_reading                       RECORD;
begin
    select forum_id from forums_messages where message_id = p_message_id into v_forum_id;
	for v_reading in select user_id
                        from forums_reading_info
                        where root_message_id = p_message_id
		loop
			  delete from forums_reading_info 
        where root_message_id = p_message_id and
              user_id = v_reading.user_id;
	UPDATE forums_reading_info_user SET threads_read=threads_read-1 WHERE forum_id= v_forum_id and user_id = v_reading.user_id;
    end loop;
    
    return 0;
end;
' language 'plpgsql';


-- mark_all_read:
create or replace function forums_reading_info__user_add_forum (integer,integer)
returns integer as '
declare
    p_forum_id                      alias for $1;
    p_user_id                       alias for $2;
    v_message                       RECORD;
    v_read_p			    RECORD;
begin
    for v_message in select message_id
                        from forums_messages_approved
                        where forum_id = p_forum_id
                        and parent_id is null 
    loop
	select into v_read_p * from forums_reading_info where user_id = p_user_id and root_message_id  = v_message.message_id;
	if NOT FOUND
	then
            insert into forums_reading_info 
            (root_message_id,user_id) 
            values 
            (v_message.message_id,p_user_id);
	end if;
    end loop;
		delete from forums_reading_info_user where forum_id = p_forum_id and user_id = p_user_id;
		insert into forums_reading_info_user (forum_id,user_id,threads_read) VALUES (p_forum_id,p_user_id,(select approved_thread_count from forums_forums where forum_id = p_forum_id));
    return 0;
end;
' language 'plpgsql';

-- mark message read for user
create or replace function forums_reading_info__user_add_msg (integer,integer)
returns integer as '
declare
    p_root_message_id               alias for $1;
    p_user_id                       alias for $2;
    v_read_p                        RECORD;
		v_forum_id											integer;
		v_exists												boolean;
begin
		select forum_id from forums_messages where message_id = p_root_message_id into v_forum_id;
		select into v_read_p * from forums_reading_info where user_id = p_user_id and root_message_id  = p_root_message_id;
		if NOT FOUND
				then
        insert into forums_reading_info 
            (root_message_id,user_id) 
            values 
            (p_root_message_id,p_user_id);
				SELECT EXISTS(SELECT 1 FROM forums_reading_info_user WHERE forum_id=v_forum_id AND user_id=p_user_id) INTO v_exists;
				if v_exists = true then
					UPDATE forums_reading_info_user SET threads_read=threads_read+1 WHERE forum_id=v_forum_id AND user_id=p_user_id;
		    else
    	    INSERT INTO forums_reading_info_user(forum_id,user_id,threads_read) VALUES (v_forum_id,p_user_id,1);
    		end if;    
    end if;

    return 0;
end;
' language 'plpgsql';

-- move thread to other forum
create or replace function forums_message__move_update_reading_info (integer,integer,integer)
returns integer as '
declare
    p_message_id        alias for $1;
    p_old_forum_id      alias for $2;
    p_new_forum_id      alias for $3;
    v_message           record;
    v_users             record;
    v_read_p            record;
    v_threads           integer;
begin
		raise notice ''updating for message %'', p_message_id;
		for v_users in select user_id from forums_reading_info where root_message_id  = p_message_id
    loop
				raise notice ''updating for user %'', v_users.user_id;
				-- down the number of threads read in old forum
			  update forums_reading_info_user set threads_read = threads_read - 1
						where forum_id = p_old_forum_id and user_id = v_users.user_id;
				-- up the number of thread read in new forum
				select count(*) into v_threads from forums_reading_info_user
					where forum_id = p_new_forum_id and user_id = v_users.user_id;
				if v_threads = 0 then
					insert into forums_reading_info_user (forum_id,user_id,threads_read)
						values (p_new_forum_id,v_users.user_id,1);
				else
					update forums_reading_info_user set threads_read = threads_read + 1
						where forum_id = p_new_forum_id and user_id = v_users.user_id;
				end if;
		end loop;

    return 1;

end;' language 'plpgsql';


-- move thread to other thread
create or replace function forums_message__move_thread_thread_update_reading_info (integer,integer, integer)
returns integer as '
declare
    p_source_message_id        alias for $1;
		p_source_forum_id					alias for $2;
    p_target_message_id      alias for $3;
		v_target_forum_id 			integer;
		v_users									record;
begin
		select forum_id from forums_messages where message_id = p_target_message_id into v_target_forum_id;
		-- for all users that have read target, but not the source, remove target_info
		for v_users in select user_id from forums_reading_info fri where root_message_id  = p_target_message_id and not exists(select 1 from forums_reading_info where root_message_id = p_source_message_id and user_id = fri.user_id)
    loop
				delete from forums_reading_info where root_message_id = p_target_message_id and user_id = v_users.user_id;
				-- down the number of threads read in target forum
				update  forums_reading_info_user set threads_read = threads_read - 1
					where forum_id = v_target_forum_id and user_id = v_users.user_id;
		end loop;
		-- for all users that have read source, down the nummber of thread in source forum and remove reading info four source message since it no longer is root_message_id
		for v_users in select user_id from forums_reading_info where root_message_id = p_source_message_id
		loop
			delete from forums_reading_info where root_message_id = p_source_message_id and user_id = v_users.user_id;
			update  forums_reading_info_user set threads_read = threads_read - 1
					where forum_id = p_source_forum_id and user_id = v_users.user_id;
		end loop;
    return 1;

end;' language 'plpgsql';

-- move message to other thread
create or replace function forums_message__move_thread_update_reading_info (integer,integer,integer)
returns integer as '
declare
    p_source_message_id        alias for $1;
		p_source_old_root_message_id 	alias for $2;
    p_target_message_id      alias for $3;
		v_target_forum_id 			integer;
		v_users									record;
begin
	select forum_id from forums_messages where message_id = p_target_message_id into v_target_forum_id;
	raise notice ''v_target_forum_id %'', v_target_forum_id;
	-- for all users that have read target, but not the source, remove target_info
	for v_users in select user_id from forums_reading_info fri where root_message_id  = p_target_message_id and not exists(select 1 from forums_reading_info where root_message_id = p_source_old_root_message_id and user_id = fri.user_id)
    loop
			delete from forums_reading_info where root_message_id = p_target_message_id and user_id = v_users.user_id;
			-- down the number of threads read in target forum
			update  forums_reading_info_user set threads_read = threads_read - 1
				where forum_id = v_target_forum_id and user_id = v_users.user_id;
		end loop;
		return 1;

end;' language 'plpgsql';


-- recount reading_info_user from reading_info
create or replace function forums_message__repair_reading_info ()
returns integer as '
declare 
v_users record;
begin
delete from forums_reading_info_user;
for v_users in
select user_id,(select forum_id from forums_messages where message_id = root_message_id) as forum_id, count(root_message_id) as threads_read from forums_reading_info group by forum_id,user_id
loop
insert into forums_reading_info_user (forum_id,user_id,threads_read)
values
(v_users.forum_id,v_users.user_id,v_users.threads_read);
end loop;
return 1;
end;' language 'plpgsql';
