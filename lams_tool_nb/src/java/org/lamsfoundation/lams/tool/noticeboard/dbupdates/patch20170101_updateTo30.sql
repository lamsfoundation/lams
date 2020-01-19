-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lanb11_content DROP COLUMN online_instructions;
ALTER TABLE tl_lanb11_content DROP COLUMN offline_instructions;
ALTER TABLE tl_lanb11_content DROP COLUMN force_offline;
DROP TABLE IF EXISTS tl_lanb11_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lanb11';

-- patch20151210.sql
-- LDEV-3631 Use simple commenting widget
ALTER TABLE tl_lanb11_content ADD COLUMN allow_comments TINYINT(1) DEFAULT 0;

UPDATE lams_tool SET tool_version='20151210' WHERE tool_signature='lanb11';

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lanb11_session DROP FOREIGN KEY FK_tl_lanb11_session_1;
ALTER TABLE tl_lanb11_session ADD CONSTRAINT FK_tl_lanb11_session_1 FOREIGN KEY (`nb_content_uid`)
REFERENCES `tl_lanb11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lanb11_user DROP FOREIGN KEY FK_tl_lanb11_user_1;
ALTER TABLE tl_lanb11_user ADD CONSTRAINT FK_tl_lanb11_user_1 FOREIGN KEY (`nb_session_uid`)
REFERENCES `tl_lanb11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20160105.sql
-- LDEV-3631 Use simple commenting widget
ALTER TABLE tl_lanb11_content ADD COLUMN comments_like_dislike TINYINT(1) DEFAULT 0;

UPDATE lams_tool SET tool_version='20160105' WHERE tool_signature='lanb11';

-- patch20170101.sql
-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lanb11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;