-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5185 Add advanced time limits to Scratchie

ALTER TABLE tl_lascrt11_scratchie CHANGE COLUMN time_limit relative_time_limit SMALLINT UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE tl_lascrt11_scratchie ADD COLUMN absolute_time_limit DATETIME AFTER relative_time_limit;

ALTER TABLE tl_lascrt11_session ADD COLUMN time_limit_adjustment SMALLINT AFTER time_limit_launched_date;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
