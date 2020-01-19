-- SQL statements to update to LAMS 2.3.5

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

ALTER TABLE tl_laqa11_que_content ADD COLUMN answer_required TINYINT(1) NOT NULL DEFAULT 0;

UPDATE lams_tool SET tool_version = "20101022" WHERE tool_signature = "laqa11";

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;