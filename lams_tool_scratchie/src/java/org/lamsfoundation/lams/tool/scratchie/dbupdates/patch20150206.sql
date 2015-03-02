-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN burning_questions_enabled TINYINT DEFAULT 1;
CREATE TABLE tl_lascrt11_burning_question (
   uid bigint NOT NULL auto_increment,
   access_date DATETIME,
   scratchie_item_uid BIGINT,
   session_id BIGINT,
   question TEXT,
   general_question tinyint,
   PRIMARY KEY (uid)
)ENGINE=InnoDB;
ALTER TABLE tl_lascrt11_burning_question ADD INDEX FK_NEW_610529188_693580A438BF8DF2 (scratchie_item_uid), ADD CONSTRAINT FK_NEW_610529188_693580A438BF8DF2 FOREIGN KEY (scratchie_item_uid) REFERENCES tl_lascrt11_scratchie_item (uid);
ALTER TABLE tl_lascrt11_burning_question ADD INDEX sessionIdIndex2 (session_id), ADD CONSTRAINT sessionIdIndex2 FOREIGN KEY (session_id) REFERENCES tl_lascrt11_session (session_id);

UPDATE lams_tool SET tool_version='20150206' WHERE tool_signature='lascrt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
