
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert. Thanks Orchard Labs!
--

--
-- This is the sortkey code
--

drop trigger forums_mess_insert_tr on forums_messages;
drop function forums_mess_insert_tr ();
