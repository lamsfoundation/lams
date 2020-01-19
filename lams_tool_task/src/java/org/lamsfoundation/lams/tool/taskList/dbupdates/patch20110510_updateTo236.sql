-- SQL statements to update from LAMS 2.3.6

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here
-- LDEV-2657
alter table tl_latask10_tasklist add column submission_deadline datetime default null;

UPDATE lams_tool SET tool_version = "20110510" WHERE tool_signature = "latask10";

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
