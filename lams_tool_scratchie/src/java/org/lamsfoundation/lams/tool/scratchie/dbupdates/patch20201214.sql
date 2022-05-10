-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5136 Require double click, instead of a single click, to reveal an item

ALTER TABLE tl_lascrt11_scratchie ADD COLUMN double_click TINYINT DEFAULT 0 AFTER time_limit;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;