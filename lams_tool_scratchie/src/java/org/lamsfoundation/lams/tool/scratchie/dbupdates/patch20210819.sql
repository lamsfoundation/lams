-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5230 Remove "add extra mark" option

DELETE FROM tl_lascrt11_configuration WHERE config_key = 'isEnabledExtraPointOption';

ALTER TABLE tl_lascrt11_scratchie DROP COLUMN extra_point;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
