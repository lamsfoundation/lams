-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-LDEV-5425 Remove "End of activity" notebook from *all* tools
ALTER TABLE tl_ladoku11_dokumaran DROP COLUMN reflect_on_activity;
ALTER TABLE tl_ladoku11_dokumaran DROP COLUMN reflect_instructions;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;