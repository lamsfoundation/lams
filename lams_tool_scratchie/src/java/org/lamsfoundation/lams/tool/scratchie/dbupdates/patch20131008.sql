-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

-- LKC-34 Scratchie to have a time limitation in monitor
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN submission_deadline DATETIME DEFAULT null;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
