-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- patch20121024.sql
ALTER TABLE tl_lachat11_user DROP COLUMN jabber_id,
							 CHANGE COLUMN jabber_nickname nickname varchar(255),
							 ADD COLUMN last_presence datetime;
						
ALTER TABLE tl_lachat11_session DROP COLUMN jabber_room,
								DROP COLUMN room_created;

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lachat11_chat DROP COLUMN online_instructions;
ALTER TABLE tl_lachat11_chat DROP COLUMN offline_instructions;
ALTER TABLE tl_lachat11_chat DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lachat11_attachment;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lachat11';

-- patch20150930.sql
--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lachat11_session ADD UNIQUE (session_id);

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DC2AF61E05;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DC2AF61E05 FOREIGN KEY (`to_user_uid`)
REFERENCES `tl_lachat11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DC9C8469FC;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DC9C8469FC FOREIGN KEY (`chat_session_uid`)
REFERENCES `tl_lachat11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lachat11_message DROP FOREIGN KEY FKCC08C1DCCF3BF9B6;
ALTER TABLE tl_lachat11_message ADD CONSTRAINT FKCC08C1DCCF3BF9B6 FOREIGN KEY (`from_user_uid`)
REFERENCES `tl_lachat11_user` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lachat11_session DROP FOREIGN KEY FK96E446B1A3926E3;
ALTER TABLE tl_lachat11_session ADD CONSTRAINT FK96E446B1A3926E3 FOREIGN KEY (`chat_uid`)
REFERENCES `tl_lachat11_chat` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lachat11_user DROP FOREIGN KEY FK4EB82169C8469FC;
ALTER TABLE tl_lachat11_user ADD CONSTRAINT FK4EB82169C8469FC FOREIGN KEY (`chat_session_uid`)
REFERENCES `tl_lachat11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lachat11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;