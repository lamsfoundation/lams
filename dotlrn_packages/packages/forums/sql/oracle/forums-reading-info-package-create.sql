
--
-- The Forums Package
--
-- @author David Arroyo Menéndez darroyo@innova.uned.es
-- @creation-date 2007-05-30
--
-- The Package for Reading Info
--
--

create or replace package forum_reading_info
as
-- remove reading_info for thread (upon new message, upon message deletion, or state change)
	procedure remove_msg (
	        p_message_id in forums_messages.message_id%TYPE
	);

-- mark_all_read
	procedure user_add_forum (
		p_forum_id in forums_forums.forum_id%TYPE,
		p_user_id in users.user_id%TYPE 
	);

-- mark message read for user
	procedure user_add_msg (
		p_root_message_id in forums_messages.message_id%TYPE,
		p_user_id in users.user_id%TYPE
	);

-- move thread to other forum
	procedure move_update (
		p_message_id in forums_messages.message_id%TYPE,
		p_old_forum_id in forums_forums.forum_id%TYPE,
		p_new_forum_id in forums_forums.forum_id%TYPE
	);

-- move thread to other thread
	procedure move_thread_th_update (
		p_source_message_id in forums_messages.message_id%TYPE,
		p_source_forum_id in forums_forums.forum_id%TYPE,
		p_target_message_id in forums_messages.message_id%TYPE
	);

-- move message to other thread
	procedure move_thread_update (
		p_source_message_id in forums_messages.message_id%TYPE,
		p_source_old_root_message_id in forums_messages.message_id%TYPE,
		p_target_message_id in forums_messages.message_id%TYPE
	);

-- recount reading_info_user from reading_info		
	procedure repair_reading_info;

end forum_reading_info;
/
show errors



create or replace package body forum_reading_info
as
-- remove reading_info for thread (upon new message, upon message deletion, or state change)
	procedure remove_msg (
	        p_message_id in forums_messages.message_id%TYPE
	) 
	is
		v_forum_id	forums_messages.forum_id%TYPE;
		cursor c_reading is select user_id from forums_reading_info where root_message_id = p_message_id;

	begin

        --Exception no_data_found if select into hasn't rows
        begin 
        	    select forum_id into v_forum_id from forums_messages where message_id = p_message_id;
                exception
                when no_data_found then
                     v_forum_id := null;
        end;

	    for v_reading in c_reading
	    loop

		  delete from forums_reading_info 
	          where root_message_id = p_message_id and
              		user_id = v_reading.user_id;
	          update forums_reading_info_user set threads_read=threads_read-1 where forum_id=v_forum_id and user_id=v_reading.user_id;

	    end loop;


	end remove_msg;

-- mark_all_read:

        procedure user_add_forum (
                p_forum_id in forums_forums.forum_id%TYPE,
                p_user_id in users.user_id%TYPE
        )
        is
                v_message forums_messages_approved%ROWTYPE;
                v_read_p integer;
        begin

            for v_message in (select message_id
                        from forums_messages_approved
                        where forum_id = p_forum_id
                        and parent_id is null)
            loop
                 select count(*) into v_read_p from forums_reading_info where user_id = p_user_id and root_message_id  = v_message.message_id;
                 
                 if v_read_p = 0 then 
                    insert into forums_reading_info
                    (root_message_id,user_id)
                    values
                    (v_message.message_id,p_user_id);
                 end if;
            end loop;
                
                delete from forums_reading_info_user where forum_id = p_forum_id and user_id = p_user_id;
                insert into forums_reading_info_user (forum_id,user_id,threads_read) VALUES (p_forum_id,p_user_id,(select approved_thread_count from forums_forums where forum_id = p_forum_id));
        end user_add_forum;


-- mark message read for user
        procedure user_add_msg (
                p_root_message_id in forums_messages.message_id%TYPE,
                p_user_id in users.user_id%TYPE
        )
        is
                v_forum_id integer;
                v_read_p integer;
                v_exists integer;
        begin
                begin
                        select forum_id into v_forum_id from forums_messages where message_id = p_root_message_id;
                exception               
                when no_data_found then
                     v_forum_id := null;
                end;

                select count(*) into v_read_p from forums_reading_info where user_id = p_user_id and root_message_id = p_root_message_id;

                 if v_read_p = 0 then

                        insert into forums_reading_info (root_message_id,user_id) values (p_root_message_id,p_user_id);
                        SELECT count(*) into v_exists FROM forums_reading_info_user WHERE forum_id=v_forum_id AND user_id=p_user_id;

                         if v_exists > 0 then

                              UPDATE forums_reading_info_user SET threads_read=threads_read+1 WHERE forum_id=v_forum_id AND user_id=p_user_id;

                         else

                              INSERT INTO forums_reading_info_user(forum_id,user_id,threads_read) VALUES (v_forum_id,p_user_id,1);

                         end if;

                 end if;

        end user_add_msg;

	

-- move thread to other forum
	procedure move_update (
		p_message_id in forums_messages.message_id%TYPE,
		p_old_forum_id in forums_forums.forum_id%TYPE,
		p_new_forum_id in forums_forums.forum_id%TYPE
	) is 
	    v_users             forums_reading_info%ROWTYPE;
	    v_threads           integer;
	begin

		for v_users in (select user_id from forums_reading_info where root_message_id  = p_message_id)

		loop
		  -- down the number of threads read in old forum
		  update forums_reading_info_user set threads_read=threads_read-1 where forum_id=p_old_forum_id and user_id=v_users.user_id;
		  -- up the number of thread read in new forum
		  select count(*) into v_threads from forums_reading_info_user where forum_id = p_new_forum_id and user_id = v_users.user_id;

  	      if v_threads = 0 then

			insert into forums_reading_info_user (forum_id,user_id,threads_read)
			values (p_new_forum_id,v_users.user_id,1);

	   	  else

			update forums_reading_info_user set threads_read = threads_read + 1
			where forum_id = p_new_forum_id and user_id = v_users.user_id;

	 	  end if;

		end loop;
	end move_update;

-- move thread to other thread
	procedure move_thread_th_update (
		p_source_message_id in forums_messages.message_id%TYPE,
		p_source_forum_id in forums_forums.forum_id%TYPE,
		p_target_message_id in forums_messages.message_id%TYPE
	) is
		v_target_forum_id 		forums_forums.forum_id%TYPE;
		v_users             		forums_reading_info%ROWTYPE;
	begin
            begin
                select forum_id into v_target_forum_id from forums_messages where message_id = p_target_message_id;
            exception
                when no_data_found then
                     v_target_forum_id := null;
            end;

		-- for all users that have read target, but not the source, remove target_info

		for v_users in (select user_id from forums_reading_info fri where root_message_id = p_target_message_id and not exists (select 1 from forums_reading_info where root_message_id = p_source_message_id and user_id = fri.user_id))

		loop

			delete from forums_reading_info where root_message_id = p_target_message_id and user_id = v_users.user_id;
			-- down the number of threads read in target forum
			update forums_reading_info_user set threads_read=threads_read-1 where forum_id = v_target_forum_id and user_id = v_users.user_id;

		end loop;
		-- for all users that have read source, down the nummber of thread in source forum and remove reading info four source message since it no longer is root_message_id

		for v_users in (select user_id from forums_reading_info where root_message_id = p_source_message_id)

		loop

			delete from forums_reading_info where root_message_id=p_source_message_id and user_id=v_users.user_id;

			update forums_reading_info_user set threads_read=threads_read-1 where forum_id = p_source_forum_id and user_id = v_users.user_id;

		end loop;

	end move_thread_th_update;

-- move message to other thread
	procedure move_thread_update (
		p_source_message_id in forums_messages.message_id%TYPE,
		p_source_old_root_message_id in forums_messages.message_id%TYPE,
		p_target_message_id in forums_messages.message_id%TYPE
	) is
		v_target_forum_id 			forums_forums.forum_id%TYPE;
		v_users             			forums_reading_info%ROWTYPE;
	begin
            begin
                select forum_id into v_target_forum_id from forums_messages where message_id = p_target_message_id;
            exception
                when no_data_found then
                     v_target_forum_id := null;
            end;


		for v_users in (select user_id from forums_reading_info fri where root_message_id  = p_target_message_id and not exists(select 1 from forums_reading_info where root_message_id = p_source_old_root_message_id and user_id = fri.user_id))
		loop

			delete from forums_reading_info where root_message_id = p_target_message_id and user_id = v_users.user_id;
			-- down the number of threads read in target forum

			update  forums_reading_info_user set threads_read = threads_read-1
				where forum_id = v_target_forum_id and user_id = v_users.user_id;
		end loop;

	end move_thread_update;

    procedure repair_reading_info is
        cursor c1 is 
        select user_id, forum_id, count(root_message_id) as threads_read
        from (
             select user_id,
                    (select forum_id from forums_messages where message_id = root_message_id) as forum_id,
                    root_message_id
             from forums_reading_info
             ) f
        group by forum_id,user_id;
   
    begin
    
        delete from forums_reading_info_user;
    
        for v_users in c1    
        loop

            insert into forums_reading_info_user (forum_id,user_id,threads_read)
            values
            (v_users.forum_id,v_users.user_id,v_users.threads_read);

        end loop;

   end repair_reading_info;

end forum_reading_info;
/
show errors