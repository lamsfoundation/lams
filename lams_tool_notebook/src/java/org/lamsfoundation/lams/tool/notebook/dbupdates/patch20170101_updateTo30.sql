
-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lantbk11_notebook DROP COLUMN online_instructions;
ALTER TABLE tl_lantbk11_notebook DROP COLUMN offline_instructions;
ALTER TABLE tl_lantbk11_notebook DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lantbk11_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lantbk11';

-- patch20140716.sql
-- LDEV-3193  Notebook monitor UI improvements
ALTER TABLE tl_lantbk11_user ADD COLUMN teachers_comment TEXT;

-- patch20150930.sql
-- Turn off autocommit, so nothing is committed if there is an error
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lantbk11_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lantbk11_session DROP FOREIGN KEY FKB7C198E2FC940906;
ALTER TABLE tl_lantbk11_session ADD CONSTRAINT FKB7C198E2FC940906 FOREIGN KEY (`notebook_uid`)
REFERENCES `tl_lantbk11_notebook` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lantbk11_user DROP FOREIGN KEY FKCB8A58FFA3B0FADF;
ALTER TABLE tl_lantbk11_user ADD CONSTRAINT FKCB8A58FFA3B0FADF FOREIGN KEY (`notebook_session_uid`)
REFERENCES `tl_lantbk11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20161123.sql
-- LDEV-4063 Force student response
ALTER TABLE tl_lantbk11_notebook ADD COLUMN force_response bit DEFAULT 0;

-- patch20170101.sql
-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lantbk11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;