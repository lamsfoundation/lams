-- SQL statements to update from LAMS 2.3.5

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

alter table tl_lachat11_chat add column submission_deadline datetime default null;
UPDATE lams_tool SET tool_version = "20110416" WHERE tool_signature = "lachat11";


-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;