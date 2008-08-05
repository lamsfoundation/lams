--
--  Copyright (C) 2001, 2002 MIT
--
--  This file is part of dotLRN.
--
--  dotLRN is free software; you can redistribute it and/or modify it under the
--  terms of the GNU General Public License as published by the Free Software
--  Foundation; either version 2 of the License, or (at your option) any later
--  version.
--
--  dotLRN is distributed in the hope that it will be useful, but WITHOUT ANY
--  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
--  FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
--  details.
--

--
-- Support for searching of forum messages
--
-- @author <a href="mailto:yon@openforce.net">yon@openforce.net</a>
-- @creation-date 2002-07-01
-- @version $Id$
--

-- Call this script like this:
-- 
-- sqlplus /nolog @forums-search-create.sql <ctxsys-password> <openacs-db-user> <openacs-db-password>
-- 
--   &1 = ctxsys password
--   &2 = OpenACS database user
--   &3 = OpenACS database password


connect &2/&3;

drop function im_convert;
drop procedure im_convert_length_check;

declare
begin
    for row in (select job
                from user_jobs
                where what like '%&2..forums_content_idx%')
    loop
        dbms_job.remove(job => row.job);
    end loop;
end;
/
show errors

drop index forums_content_idx;

execute ctx_ddl.unset_attribute('forums_user_datastore', 'procedure');
execute ctx_ddl.drop_preference('forums_user_datastore');

-- as ctxsys
connect ctxsys/&1;

drop procedure s_index_message;

-- as normal user
connect &2/&3;

drop procedure index_message;

exit
