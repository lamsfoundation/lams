
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- The Package for Messages
--
-- This code is newly concocted by Ben, but with heavy concepts and heavy code
-- chunks lifted from Gilbert. Thanks Orchard Labs!
--

drop function forums_message__new (integer,varchar,integer,varchar,text,char,integer,varchar,integer,timestamptz,integer,varchar,integer);

drop function forums_message__root_message_id (integer);

drop function forums_message__thread_open (integer);

drop function forums_message__thread_close (integer);

drop function forums_message__delete (integer);

drop function forums_message__delete_thread (integer);

drop function forums_message__name (integer);
