-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-4180
ALTER TABLE tl_ladaco10_contents MODIFY COLUMN lock_on_finished TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN content_in_use TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN define_later TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN reflect_on_activity TINYINT(1),
								 MODIFY COLUMN learner_entry_notify TINYINT(1) DEFAULT 0,
								 MODIFY COLUMN record_submit_notify TINYINT(1) DEFAULT 0;
								 
ALTER TABLE tl_ladaco10_questions MODIFY COLUMN is_required tinyint(1) DEFAULT 0;		

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='ladaco10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
