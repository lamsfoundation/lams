-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

ALTER TABLE tl_lasbmt11_content ADD COLUMN mark_release_notify tinyint DEFAULT 0;
ALTER TABLE tl_lasbmt11_content ADD COLUMN file_submit_notify tinyint DEFAULT 0;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;