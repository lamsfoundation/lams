-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

--LDEV-5225 Add Gallery Walk to Mindmap

ALTER TABLE tl_lamind10_mindmap   ADD COLUMN gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_read_only TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_started TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_finished TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_edit_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_instructions TEXT;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
