-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-
ALTER TABLE tl_lawiki10_wiki_page ADD COLUMN deleted bit DEFAULT 0 AFTER editable;

-- 	LDEV-3078 Wiki tool should start uploading images to www/secure/runtime/ folder
ALTER TABLE tl_lawiki10_session DROP COLUMN content_folder_id;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lawiki10_wiki DROP COLUMN online_instructions;
ALTER TABLE tl_lawiki10_wiki DROP COLUMN offline_instructions;
ALTER TABLE tl_lawiki10_wiki DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lawiki10_attachment;

-- LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_lawiki10_session ADD UNIQUE (session_id);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_lawiki10_session DROP FOREIGN KEY FKF01D63C260B3B03B;
ALTER TABLE tl_lawiki10_session ADD CONSTRAINT FKF01D63C260B3B03B FOREIGN KEY (`wiki_uid`)
REFERENCES `tl_lawiki10_wiki` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_session DROP FOREIGN KEY FKF01D63C2A3FF7EC0;
ALTER TABLE tl_lawiki10_session ADD CONSTRAINT FKF01D63C2A3FF7EC0 FOREIGN KEY (`wiki_main_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_user DROP FOREIGN KEY FKED5D7A1FD8004954;
ALTER TABLE tl_lawiki10_user ADD CONSTRAINT FKED5D7A1FD8004954 FOREIGN KEY (`wiki_session_uid`)
REFERENCES `tl_lawiki10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki DROP FOREIGN KEY FKED5E3E04A3FF7EC0;
ALTER TABLE tl_lawiki10_wiki ADD CONSTRAINT FKED5E3E04A3FF7EC0 FOREIGN KEY (`wiki_main_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_1;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_1 FOREIGN KEY (`wiki_session_uid`)
REFERENCES `tl_lawiki10_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_2;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_2 FOREIGN KEY (`wiki_uid`)
REFERENCES `tl_lawiki10_wiki` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_3;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_3 FOREIGN KEY (`added_by`)
REFERENCES `tl_lawiki10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page DROP FOREIGN KEY wiki_page_fk_4;
ALTER TABLE tl_lawiki10_wiki_page ADD CONSTRAINT wiki_page_fk_4 FOREIGN KEY (`wiki_current_content`)
REFERENCES `tl_lawiki10_wiki_page_content` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page_content DROP FOREIGN KEY FK528051242D44CCF8;
ALTER TABLE tl_lawiki10_wiki_page_content ADD CONSTRAINT FK528051242D44CCF8 FOREIGN KEY (`wiki_page_uid`)
REFERENCES `tl_lawiki10_wiki_page` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lawiki10_wiki_page_content DROP FOREIGN KEY FK528051243233D952;
ALTER TABLE tl_lawiki10_wiki_page_content ADD CONSTRAINT FK528051243233D952 FOREIGN KEY (`editor`)
REFERENCES `tl_lawiki10_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

-- LDEV-
alter table tl_lawiki10_wiki add column submission_deadline datetime default null;

-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lawiki10';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;