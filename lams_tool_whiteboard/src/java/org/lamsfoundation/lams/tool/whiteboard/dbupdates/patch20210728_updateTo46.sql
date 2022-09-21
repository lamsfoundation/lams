SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20210623.sql to 20210728.sql
-- It should upgrade this tool to version 4.6

-- LDEV-5210 Add optional prefix to Whiteboard canvas IDs
INSERT INTO tl_lawhiteboard11_configuration (config_key, config_value) VALUES ('WhiteboardIdPrefix', NULL);

--LDEV-5222 Allow reedit in Whiteboard tool
ALTER TABLE tl_lawhiteboard11_whiteboard ADD COLUMN gallery_walk_edit_enabled TINYINT NOT NULL DEFAULT 0 AFTER gallery_walk_finished;
  
SET FOREIGN_KEY_CHECKS=1;