-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4259 Add time limit feature
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN time_limit integer DEFAULT 0;
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN time_limit_launched_date datetime;

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;