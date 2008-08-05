
--
-- The Forums Package
--
-- @author gwong@orchardlabs.com,ben@openforce.biz
-- @creation-date 2002-05-16
--
-- This code is newly concocted by Ben, but with significant concepts and code
-- lifted from Gilbert. Thanks Orchard Labs!
--

-- privileges
-- NO PRIVILEGES FOR MESSAGES
-- we don't individually permission messages

--
-- The Data Model
--

drop table forums_messages;

--
-- Object Type
--

declare
begin
        acs_object_type.drop_type (
            object_type => 'forums_message'
        );
end;
/
show errors

drop view forums_messages_pending;
drop view forums_messages_approved;