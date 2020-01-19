-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_laqa11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_laqa11_session ADD COLUMN qa_group_leader_uid BIGINT;
ALTER TABLE tl_laqa11_session ADD INDEX FK_laqa11_session1 (qa_group_leader_uid), ADD CONSTRAINT FK_laqa11_session1 FOREIGN KEY (qa_group_leader_uid) REFERENCES tl_laqa11_que_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the QA user table so no same que_usr_id and qa_session_id can be repetead 
-- In order to achieve this remove duplicate users and according responses. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_laqa11_que_usr GROUP BY que_usr_id, qa_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_laqa11_usr_resp WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_laqa11_que_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_laqa11_que_usr ADD UNIQUE INDEX(que_usr_id, qa_session_id);

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_laqa11_content DROP COLUMN online_instructions;
ALTER TABLE tl_laqa11_content DROP COLUMN offline_instructions;
ALTER TABLE tl_laqa11_content DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_laqa11_uploadedfile;

-- LDEV-3236 Ability to change, add, remove Q&A questions even after student have reached it
ALTER TABLE tl_laqa11_content DROP COLUMN content_inUse;
ALTER TABLE tl_laqa11_content DROP COLUMN synch_in_monitor;
ALTER TABLE tl_laqa11_content ADD COLUMN notify_response_submit TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-3292: Add missing indexes
CREATE INDEX tl_laqa11_content_qa_content_id ON tl_laqa11_content(qa_content_id);
CREATE INDEX tl_laqa11_session_qa_session_id ON tl_laqa11_session(qa_session_id);

-- LDEV-3295 Allow questions having user responses with ratings be deleted
ALTER TABLE tl_laqa11_response_rating DROP FOREIGN KEY FK_tl_laqa11_response_rating_2 ;
ALTER TABLE tl_laqa11_response_rating ADD CONSTRAINT FK_tl_laqa11_response_rating_2 FOREIGN KEY (response_id ) REFERENCES tl_laqa11_usr_resp (response_id) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV- Set max and min rates limits
ALTER TABLE tl_laqa11_content ADD COLUMN minimum_rates integer DEFAULT 0; 
ALTER TABLE tl_laqa11_content ADD COLUMN maximum_rates integer DEFAULT 0;
ALTER TABLE tl_laqa11_que_content ADD COLUMN min_words_limit integer DEFAULT 0;

-- LDEV-3487 Don't allow question redos
ALTER TABLE tl_laqa11_content ADD COLUMN no_reedit_allowed TINYINT(1) NOT NULL DEFAULT 0;

--  LDEV-3564 tool session id must be unique in tool's session table
ALTER TABLE tl_laqa11_session ADD UNIQUE (qa_session_id);

--  LDEV-3568 Move old rating data into the new DB model
INSERT INTO lams_rating_criteria (title, rating_criteria_type_id, comments_enabled, order_id, tool_content_id)
SELECT "", 3, false, 1, qa_content_id
FROM tl_laqa11_content WHERE allow_rate_answers=TRUE;

INSERT INTO lams_rating (rating_criteria_id, item_id, user_id, rating)
SELECT criteria.rating_criteria_id, rating.response_id, rating.user_id, rating.rating
FROM tl_laqa11_response_rating rating, tl_laqa11_usr_resp response, tl_laqa11_que_content question, tl_laqa11_content qa, lams_rating_criteria criteria 
	WHERE response.response_id=rating.response_id AND response.qa_que_content_id=question.uid AND qa.uid=question.qa_content_id AND qa.qa_content_id=criteria.tool_content_id AND criteria.order_id=1;

DROP TABLE tl_laqa11_response_rating;

-- LDEV-3640 Add necessary cascades

ALTER TABLE tl_laqa11_que_content DROP FOREIGN KEY FK_tl_laqa11_que_content_1;
ALTER TABLE tl_laqa11_que_content ADD CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (`qa_content_id`)
REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_que_usr DROP FOREIGN KEY FK_tl_laqa11_que_usr_1;
ALTER TABLE tl_laqa11_que_usr ADD CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (`qa_session_id`)
REFERENCES `tl_laqa11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_session DROP FOREIGN KEY FK_laqa11_session1;
ALTER TABLE tl_laqa11_session ADD CONSTRAINT FK_laqa11_session1 FOREIGN KEY (`qa_group_leader_uid`)
REFERENCES `tl_laqa11_que_usr` (`uid`) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_session DROP FOREIGN KEY FK_tl_laqa11_session_1;
ALTER TABLE tl_laqa11_session ADD CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (`qa_content_id`)
REFERENCES `tl_laqa11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_usr_resp MODIFY COLUMN qa_que_content_id BIGINT(20) DEFAULT NULL;
ALTER TABLE tl_laqa11_usr_resp DROP FOREIGN KEY FK_tl_laqa11_usr_resp_2;
ALTER TABLE tl_laqa11_usr_resp ADD CONSTRAINT FK_tl_laqa11_usr_resp_2 FOREIGN KEY (`qa_que_content_id`)
REFERENCES `tl_laqa11_que_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_usr_resp DROP FOREIGN KEY FK_tl_laqa11_usr_resp_3;
ALTER TABLE tl_laqa11_usr_resp ADD CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_laqa11_que_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_wizard_cognitive_skill DROP FOREIGN KEY FK3BA4132BCBB0DC8D;
ALTER TABLE tl_laqa11_wizard_cognitive_skill ADD CONSTRAINT FK3BA4132BCBB0DC8D FOREIGN KEY (`category_uid`)
REFERENCES `tl_laqa11_wizard_category` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_laqa11_wizard_question DROP FOREIGN KEY FKAF08A0C7EFF77FD4;
ALTER TABLE tl_laqa11_wizard_question ADD CONSTRAINT FKAF08A0C7EFF77FD4 FOREIGN KEY (`cognitive_skill_uid`)
REFERENCES `tl_laqa11_wizard_cognitive_skill` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

-- LDEV-3730 while re-editing a question autosaves overwrite submitted reponse
ALTER TABLE tl_laqa11_usr_resp ADD COLUMN answer_autosaved TEXT;

-- LDEV-3751 Prevent students from submitting questions but allow them to view responses
ALTER TABLE tl_laqa11_content ADD COLUMN show_other_answers_after_deadline TINYINT(1) NOT NULL DEFAULT 0;

-- LDEV-4180
UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='laqa11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;