-- SQL statements to update from LAMS 2.3.5

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

alter table tl_lawiki10_wiki add column submission_deadline datetime default null;
UPDATE lams_tool SET tool_version = "20160919" WHERE tool_signature = "lawiki10";


----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;