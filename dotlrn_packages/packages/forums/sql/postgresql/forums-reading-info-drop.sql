--
-- The Forums Package
--
-- @author David Arroyo darroyo@innova.uned.es
-- @creation-date 01-06-2007
-- @version $Id$
--
-- This is free software distributed under the terms of the GNU Public
-- License version 2 or later.  Full text of the license is available from the GNU Project:
-- http://www.fsf.org/copyleft/gpl.html

drop function forums_message__repair_reading_info ();
drop function forums_message__move_thread_update_reading_info (integer,integer,integer);
drop function forums_message__move_thread_thread_update_reading_info (integer,integer, integer);
drop function forums_message__move_update_reading_info (integer,integer,integer);
drop function forums_reading_info__user_add_msg (integer,integer);
drop function forums_reading_info__user_add_forum (integer,integer);
drop function forums_reading_info__remove_msg (integer);
drop table forums_reading_info_user;
drop index forums_reading_info_forum_message_index;
drop index forums_reading_info_user_index ;
drop table forums_reading_info;