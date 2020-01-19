-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- patch20130325.sql
ALTER TABLE tl_lamc11_que_usr DROP COLUMN viewSummaryRequested ;

-- patch20130527.sql
-- Turn off autocommit, so nothing is committed if there is an error
--LDEV-3043 remove unused properties
ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN finished;

-- patch20130726.sql
--LDEV-3079 Use CKEditors as MCQ answers tags
ALTER TABLE tl_lamc11_options_content MODIFY COLUMN mc_que_option_text TEXT;

-- patch20130812.sql
--LDEV-3085 Autosave feature for MCQ
CREATE TEMPORARY TABLE temp_select AS SELECT attempt.uid
	FROM tl_lamc11_usr_attempt attempt
	INNER JOIN(
	    SELECT que_usr_id, mc_que_content_id, max(attemptOrder) attemptOrder
	    FROM tl_lamc11_usr_attempt
	    GROUP BY que_usr_id, mc_que_content_id
	) ss ON attempt.que_usr_id = ss.que_usr_id AND attempt.mc_que_content_id = ss.mc_que_content_id AND attempt.attemptOrder = ss.attemptOrder;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN attemptOrder;
ALTER TABLE tl_lamc11_que_usr DROP COLUMN last_attempt_order;
ALTER TABLE tl_lamc11_que_usr ADD COLUMN number_attempts INTEGER DEFAULT 0;

-- patch20131021.sql
--LDEV-3138 Prevent erroneously creating 2 identical user results for 1 question in DB
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid
FROM tl_lamc11_usr_attempt GROUP BY que_usr_id, mc_que_content_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_usr_attempt ADD UNIQUE INDEX attempt_unique_index (que_usr_id, mc_que_content_id);

-- patch20140101.sql
-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_lamc11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lamc11_session ADD COLUMN mc_group_leader_uid BIGINT;
ALTER TABLE tl_lamc11_session ADD INDEX FK_lamc11_session1 (mc_group_leader_uid), ADD CONSTRAINT FK_lamc11_session1 FOREIGN KEY (mc_group_leader_uid) REFERENCES tl_lamc11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Put automatic letters in front of MCQ answers
ALTER TABLE tl_lamc11_content ADD COLUMN prefix_answers_with_letters TINYINT(1) NOT NULL DEFAULT 1;

-- Adding a constraint to the MCQ user table so no same user_id and session_id can be repetead. 
-- In order to achieve this remove duplicate users and according attempts. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_lamc11_que_usr GROUP BY que_usr_id, mc_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lamc11_usr_attempt WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_lamc11_que_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lamc11_que_usr ADD UNIQUE INDEX(que_usr_id, mc_session_id);

-- patch20140102.sql
-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lamc11_content DROP COLUMN online_instructions;
ALTER TABLE tl_lamc11_content DROP COLUMN offline_instructions;
ALTER TABLE tl_lamc11_content DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lamc11_uploadedfile;

UPDATE lams_tool SET tool_version='20140102' WHERE tool_signature='lamc11';

-- patch20140512.sql
-- LDEV-3228 Ability to change, add, remove MCQ questions even after student have reached it
ALTER TABLE tl_lamc11_content DROP COLUMN content_in_use;

ALTER TABLE tl_lamc11_options_content DROP FOREIGN KEY FK_tl_lamc11_options_content_1;
ALTER TABLE tl_lamc11_options_content MODIFY COLUMN mc_que_content_id BIGINT(20), 
  ADD CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (mc_que_content_id) REFERENCES tl_lamc11_que_content(uid);

UPDATE lams_tool SET tool_version='20140512' WHERE tool_signature='lamc11';

-- patch20151217.sql
-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_lamc11_options_content DROP FOREIGN KEY FK_tl_lamc11_options_content_1;
ALTER TABLE tl_lamc11_options_content ADD CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (`mc_que_content_id`)
REFERENCES `tl_lamc11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_que_content DROP FOREIGN KEY FK_tl_lamc11_que_content_1;
ALTER TABLE tl_lamc11_que_content ADD CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (`mc_content_id`)
REFERENCES `tl_lamc11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_que_usr DROP FOREIGN KEY FK_tl_lamc11_que_usr_1;
ALTER TABLE tl_lamc11_que_usr ADD CONSTRAINT FK_tl_lamc11_que_usr_1 FOREIGN KEY (`mc_session_id`)
REFERENCES `tl_lamc11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_session DROP FOREIGN KEY FK_lamc11_session1;
ALTER TABLE tl_lamc11_session ADD CONSTRAINT FK_lamc11_session1 FOREIGN KEY (`mc_group_leader_uid`)
REFERENCES `tl_lamc11_que_usr` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_session DROP FOREIGN KEY FK_tl_lamc_session_1;
ALTER TABLE tl_lamc11_session ADD CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (`mc_content_id`)
REFERENCES `tl_lamc11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_2;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_2 FOREIGN KEY (`mc_que_content_id`)
REFERENCES `tl_lamc11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_3 FOREIGN KEY (`mc_que_option_id`)
REFERENCES `tl_lamc11_options_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_4;
ALTER TABLE tl_lamc11_usr_attempt ADD CONSTRAINT FK_tl_lamc11_usr_attempt_4 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_lamc11_que_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- patch20170101.sql
-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lamc11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;