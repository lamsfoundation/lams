-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LKC-15 adding a constraint to the scratchie user table so no same user_id and session_id can be repetead 
ALTER TABLE tl_lascrt11_user ADD UNIQUE INDEX(user_id, session_uid);

-- LKC-28 Ability to modify marks in monitor 
ALTER TABLE tl_lascrt11_user ADD COLUMN mark INTEGER DEFAULT 0;

-- LKC-22 Remove cloumn that is not used any longer
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN show_results_page;

-- LKC-34 Scratchie to have a time limitation in monitor
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN submission_deadline DATETIME DEFAULT null;

-- LKC-35 Be able import learning design with Scratchie tool exported from another server
ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93758092FB;
ALTER TABLE tl_lascrt11_scratchie_item DROP COLUMN create_by;

-- LKC-40 prevent Massive load of DB when accessing tool with 20+ users
ALTER TABLE tl_lascrt11_user ADD INDEX userIdIndex (user_id);
ALTER TABLE tl_lascrt11_session ADD INDEX sessionIdIndex (session_id);

-- LKC-40 
-- Remove logs belong to all other users except leader
DROP TABLE IF EXISTS temp_select;
CREATE TEMPORARY TABLE temp_select AS SELECT group_leader_uid uid FROM tl_lascrt11_session WHERE group_leader_uid IS NOT NULL;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lascrt11_answer_log WHERE user_uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

--Make ScratchieAnswerVisitLog belong to session and not user, thus being shared by all users
ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY FK_NEW_610529188_693580A441F9365D;
ALTER TABLE tl_lascrt11_answer_log DROP INDEX  FK_NEW_610529188_693580A441F9365D;
ALTER TABLE tl_lascrt11_answer_log DROP COLUMN user_uid;

-- Make mark belong to session and not user, thus being shared by all users
ALTER TABLE tl_lascrt11_session ADD COLUMN mark INTEGER DEFAULT 0;
UPDATE tl_lascrt11_session, tl_lascrt11_user
		SET tl_lascrt11_session.mark = tl_lascrt11_user.mark 
		WHERE tl_lascrt11_user.uid = tl_lascrt11_session.group_leader_uid;
ALTER TABLE tl_lascrt11_user DROP COLUMN mark;

-- Make scratching_finished flag belong to session and not user, thus being shared by all users
ALTER TABLE tl_lascrt11_session ADD COLUMN scratching_finished smallint DEFAULT 0;
UPDATE tl_lascrt11_session, tl_lascrt11_user
		SET tl_lascrt11_session.scratching_finished = tl_lascrt11_user.scratching_finished 
		WHERE tl_lascrt11_user.uid = tl_lascrt11_session.group_leader_uid;
ALTER TABLE tl_lascrt11_user DROP COLUMN scratching_finished;

ALTER TABLE tl_lascrt11_session ADD UNIQUE INDEX session_id_UNIQUE (session_id ASC);
ALTER TABLE tl_lascrt11_answer_log ADD INDEX sessionIdIndex (session_id), ADD CONSTRAINT sessionIdIndex FOREIGN KEY (session_id) REFERENCES tl_lascrt11_session (session_id);
ALTER TABLE tl_lascrt11_scratchie_answer ADD INDEX FK_scratchie_answer_1 (scratchie_item_uid), ADD CONSTRAINT FK_scratchie_answer_1 FOREIGN KEY (scratchie_item_uid) REFERENCES tl_lascrt11_scratchie_item (uid);

--Remove create_by from tl_lascrt11_scratchie
ALTER TABLE tl_lascrt11_scratchie DROP FOREIGN KEY FK_NEW_610529188_89093BF758092FB;
ALTER TABLE tl_lascrt11_scratchie DROP INDEX  FK_NEW_610529188_89093BF758092FB;
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN create_by;

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN online_instructions;
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN offline_instructions;
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lascrt11_attachment;

-- LDEV-3224 Ability to change, add, remove questions even after student have reached it
ALTER TABLE tl_lascrt11_scratchie DROP COLUMN content_in_use;

-- LDEV-3251 Create configurable grading scale for scratchie
CREATE TABLE tl_lascrt11_configuration (
   uid BIGINT NOT NULL auto_increment,
   config_key VARCHAR(30) UNIQUE,
   config_value VARCHAR(255),
   PRIMARY KEY (uid)
)ENGINE=InnoDB;
UPDATE lams_tool SET admin_url='tool/lascrt11/admin/start.do' WHERE tool_signature='lascrt11';

INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES
  ('isEnabledExtraPointOption',	'true');
INSERT INTO `tl_lascrt11_configuration` (`config_key`, `config_value`) VALUES
  ('presetMarks', '4,2,1,0');
  
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

-- LDEV-3548 Prevent Scratchie tool from storing duplicate scratches done by the same user
ALTER TABLE `tl_lascrt11_answer_log` ADD UNIQUE `FK_NEW_lascrt11_30113BFC309ED321`(`scratchie_answer_uid`, `session_id`);

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_lascrt11_scratchie_answer DROP FOREIGN KEY FK_scratchie_answer_1;
ALTER TABLE tl_lascrt11_scratchie_answer ADD CONSTRAINT FK_scratchie_answer_1 FOREIGN KEY (`scratchie_item_uid`)
REFERENCES `tl_lascrt11_scratchie_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F9330E79035;
ALTER TABLE tl_lascrt11_scratchie_item ADD CONSTRAINT FK_NEW_610529188_F52D1F9330E79035 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93EC0D3147;
ALTER TABLE tl_lascrt11_scratchie_item ADD CONSTRAINT FK_NEW_610529188_F52D1F93EC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lascrt11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_session DROP FOREIGN KEY FK_lalead11_session1;
ALTER TABLE tl_lascrt11_session ADD CONSTRAINT FK_lalead11_session1 FOREIGN KEY (`group_leader_uid`)
REFERENCES `tl_lascrt11_user` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_session DROP FOREIGN KEY FK_NEW_610529188_24AA78C530E79035;
ALTER TABLE tl_lascrt11_session ADD CONSTRAINT FK_NEW_610529188_24AA78C530E79035 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFCEC0D3147;
ALTER TABLE tl_lascrt11_user ADD CONSTRAINT FK_NEW_610529188_30113BFCEC0D3147 FOREIGN KEY (`session_uid`)
REFERENCES `tl_lascrt11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_user DROP FOREIGN KEY FK_NEW_610529188_30113BFC309ED320;
ALTER TABLE tl_lascrt11_user ADD CONSTRAINT FK_NEW_610529188_30113BFC309ED320 FOREIGN KEY (`scratchie_uid`)
REFERENCES `tl_lascrt11_scratchie` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
							 
ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY sessionIdIndex;
ALTER TABLE tl_lascrt11_answer_log ADD CONSTRAINT sessionIdIndex FOREIGN KEY (`session_id`)
REFERENCES `tl_lascrt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;
							 
ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DFE;
ALTER TABLE tl_lascrt11_answer_log ADD CONSTRAINT FK_NEW_610529188_693580A438BF8DFE FOREIGN KEY (`scratchie_answer_uid`)
REFERENCES `tl_lascrt11_scratchie_answer` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
			 	   
ALTER TABLE tl_lascrt11_burning_question DROP FOREIGN KEY sessionIdIndex2;
ALTER TABLE tl_lascrt11_burning_question ADD CONSTRAINT sessionIdIndex2 FOREIGN KEY (`session_id`)
REFERENCES `tl_lascrt11_session` (`session_id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lascrt11_burning_question DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DF2;
ALTER TABLE tl_lascrt11_burning_question ADD CONSTRAINT FK_NEW_610529188_693580A438BF8DF2 FOREIGN KEY (`scratchie_item_uid`)
REFERENCES `tl_lascrt11_scratchie_item` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-
--ALTER TABLE tl_lascrt11_scratchie ADD COLUMN burning_questions_enabled TINYINT DEFAULT 1;
CREATE TABLE tl_lascrt11_burning_que_like (
   uid bigint NOT NULL auto_increment,
   burning_question_uid BIGINT,
   session_id bigint,
   PRIMARY KEY (uid)
)ENGINE=InnoDB;
ALTER TABLE tl_lascrt11_burning_que_like ADD INDEX FK_burning_que_uid (burning_question_uid), ADD CONSTRAINT FK_burning_que_uid FOREIGN KEY (burning_question_uid) REFERENCES tl_lascrt11_burning_question (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-3741 Add a time limit to scratchie
ALTER TABLE tl_lascrt11_scratchie ADD COLUMN time_limit INTEGER DEFAULT 0;

--  LDEV-3801 keep the time limit start time in DB
ALTER TABLE tl_lascrt11_session ADD COLUMN time_limit_launched_date datetime;

-- LDEV-4180
ALTER TABLE tl_lascrt11_scratchie MODIFY COLUMN extra_point TINYINT(1),
								  MODIFY COLUMN define_later TINYINT(1),
							      MODIFY COLUMN reflect_on_activity TINYINT(1),
								  MODIFY COLUMN burning_questions_enabled TINYINT(1);

ALTER TABLE tl_lascrt11_session MODIFY COLUMN status TINYINT(1),
								MODIFY COLUMN scratching_finished TINYINT(1) DEFAULT 0;
								
ALTER TABLE tl_lascrt11_user MODIFY COLUMN session_finished TINYINT(1);

ALTER TABLE tl_lascrt11_scratchie_answer MODIFY COLUMN correct TINYINT(1);	

ALTER TABLE tl_lascrt11_scratchie_item MODIFY COLUMN create_by_author TINYINT(1);	

ALTER TABLE tl_lascrt11_burning_question MODIFY COLUMN general_question TINYINT(1);	

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lascrt11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
