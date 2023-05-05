-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-5391 Add batches to gallery walk
ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN gallery_walk_batch_size TINYINT UNSIGNED NOT NULL DEFAULT '0' AFTER gallery_walk_read_only;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;