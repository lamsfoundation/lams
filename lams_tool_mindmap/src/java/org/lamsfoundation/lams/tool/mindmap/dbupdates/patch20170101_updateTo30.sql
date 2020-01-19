-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here


-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lamind10_mindmap DROP COLUMN online_instructions;
ALTER TABLE tl_lamind10_mindmap DROP COLUMN offline_instructions;
ALTER TABLE tl_lamind10_mindmap DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lamind10_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lamind10';

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lamind10_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lamind10_node DROP FOREIGN KEY MindmapNode_Mindmap_FK;
ALTER TABLE tl_lamind10_node ADD CONSTRAINT MindmapNode_Mindmap_FK FOREIGN KEY (`mindmap_id`)
REFERENCES `tl_lamind10_mindmap` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamind10_node DROP FOREIGN KEY MindmapNode_Parent_FK;
ALTER TABLE tl_lamind10_node ADD CONSTRAINT MindmapNode_Parent_FK FOREIGN KEY (`parent_id`)
REFERENCES `tl_lamind10_node` (`node_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamind10_node DROP FOREIGN KEY MindmapNode_User_FK;
ALTER TABLE tl_lamind10_node ADD CONSTRAINT MindmapNode_User_FK FOREIGN KEY (`user_id`)
REFERENCES `tl_lamind10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;


ALTER TABLE tl_lamind10_request DROP FOREIGN KEY tl_lamind10_request_fk;
ALTER TABLE tl_lamind10_request ADD CONSTRAINT tl_lamind10_request_fk FOREIGN KEY (`mindmap_id`)
REFERENCES `tl_lamind10_mindmap` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamind10_request DROP FOREIGN KEY tl_lamind10_request_fk1;
ALTER TABLE tl_lamind10_request ADD CONSTRAINT tl_lamind10_request_fk1 FOREIGN KEY (`user_id`)
REFERENCES `tl_lamind10_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamind10_session DROP FOREIGN KEY FK_NEW_972920762_B7C198E2FC940906;
ALTER TABLE tl_lamind10_session ADD CONSTRAINT FK_NEW_972920762_B7C198E2FC940906 FOREIGN KEY (`mindmap_uid`)
REFERENCES `tl_lamind10_mindmap` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamind10_user DROP FOREIGN KEY FK_NEW_972920762_CB8A58FFA3B0FADF;
ALTER TABLE tl_lamind10_user ADD CONSTRAINT FK_NEW_972920762_CB8A58FFA3B0FADF FOREIGN KEY (`mindmap_session_uid`)
REFERENCES `tl_lamind10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20170101.sql
-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lamind10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;