-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lalead11_leaderselection DROP COLUMN run_offline;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lalead11';

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lalead11_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lalead11_session DROP FOREIGN KEY FK_lalead11_1;
ALTER TABLE tl_lalead11_session ADD CONSTRAINT FK_lalead11_1 FOREIGN KEY (`group_leader_uid`)
REFERENCES `tl_lalead11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lalead11_session DROP FOREIGN KEY FK_NEW_1316293302_B7C198E2FC940906;
ALTER TABLE tl_lalead11_session ADD CONSTRAINT FK_NEW_1316293302_B7C198E2FC940906 FOREIGN KEY (`leaderselection_uid`)
REFERENCES `tl_lalead11_leaderselection` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lalead11_user DROP FOREIGN KEY FK_NEW_1316293302_CB8A58FFA3B0FADF;
ALTER TABLE tl_lalead11_user ADD CONSTRAINT FK_NEW_1316293302_CB8A58FFA3B0FADF FOREIGN KEY (`leaderselection_session_uid`)
REFERENCES `tl_lalead11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20170101.sql
-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lalead11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;