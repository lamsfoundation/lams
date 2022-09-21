SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20210731.sql
-- It should upgrade this tool to version 4.6

--LDEV-5225 Add Gallery Walk to Mindmap

ALTER TABLE tl_lamind10_mindmap   ADD COLUMN gallery_walk_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_read_only TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_started TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_finished TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_edit_enabled TINYINT NOT NULL DEFAULT 0,
								  ADD COLUMN gallery_walk_instructions TEXT;
				  
SET FOREIGN_KEY_CHECKS=1;