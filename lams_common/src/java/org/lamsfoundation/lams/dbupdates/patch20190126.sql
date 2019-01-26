-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4587 Direction (asc/desc) of ordered branching. NULL means it is non-ordered tool-based branching
ALTER TABLE lams_learning_activity
ADD COLUMN branching_ordered_asc TINYINT(1);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;