-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5120 Add column for Doku Gallery Walk

ALTER TABLE tl_ladoku11_dokumaran ADD COLUMN gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_read_only TINYINT NOT NULL DEFAULT 0;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
