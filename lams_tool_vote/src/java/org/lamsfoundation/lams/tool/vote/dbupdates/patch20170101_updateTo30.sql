-- SQL statements to update to LAMS 3.0

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
-- Put all sql statements below here

-- LDEV
ALTER TABLE tl_lavote11_content ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_lavote11_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_lavote11_session ADD INDEX FK_lavote11_session_1 (group_leader_uid), ADD CONSTRAINT tl_lavote11_session FOREIGN KEY (group_leader_uid) REFERENCES tl_lavote11_usr (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the vote user table so no same user_id and vote_session_id can be repetead 
-- In order to achieve this remove duplicate users and according attempts. 
CREATE TEMPORARY TABLE temp_select AS SELECT MAX(uid) uid FROM tl_lavote11_usr GROUP BY user_id, vote_session_id;
ALTER TABLE temp_select ADD INDEX index1 (uid ASC);
DELETE FROM tl_lavote11_usr_attempt WHERE que_usr_id NOT IN (SELECT uid FROM temp_select);
DELETE FROM tl_lavote11_usr WHERE uid NOT IN (SELECT uid FROM temp_select);
DROP TEMPORARY TABLE temp_select;

ALTER TABLE tl_lavote11_usr ADD UNIQUE INDEX(user_id, vote_session_id);

-- LDEV-3147 Simplify tools: get rid of instructions tab, define in monitor and offline activity options
ALTER TABLE tl_lavote11_content DROP COLUMN online_instructions;
ALTER TABLE tl_lavote11_content DROP COLUMN offline_instructions;
ALTER TABLE tl_lavote11_content DROP COLUMN run_offline;
DROP TABLE IF EXISTS tl_lavote11_uploadedfile;

-- LDEV-3228 Ability to change, add, remove MCQ questions even after student have reached it
ALTER TABLE tl_lavote11_content DROP COLUMN content_in_use;

-- LDEV-3640 Add necessary cascades
ALTER TABLE tl_lavote11_nomination_content DROP FOREIGN KEY FK_tl_lavote11_nomination_content_1;
ALTER TABLE tl_lavote11_nomination_content ADD CONSTRAINT FK_tl_lavote11_nomination_content_1 FOREIGN KEY (`vote_content_id`)
REFERENCES `tl_lavote11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lavote11_usr DROP FOREIGN KEY FK_tl_lavote11_usr_1;
ALTER TABLE tl_lavote11_usr ADD CONSTRAINT FK_tl_lavote11_usr_1 FOREIGN KEY (`vote_session_id`)
REFERENCES `tl_lavote11_session` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lavote11_session DROP FOREIGN KEY FK_tl_lavote11_session_1;
ALTER TABLE tl_lavote11_session ADD CONSTRAINT FK_tl_lavote11_session_1 FOREIGN KEY (`vote_content_id`)
REFERENCES `tl_lavote11_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lavote11_usr_attempt DROP FOREIGN KEY FK_tl_lavote11_usr_attempt_2;
ALTER TABLE tl_lavote11_usr_attempt ADD CONSTRAINT FK_tl_lavote11_usr_attempt_2 FOREIGN KEY (`que_usr_id`)
REFERENCES `tl_lavote11_usr` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tl_lavote11_usr_attempt DROP FOREIGN KEY FK_tl_lavote11_usr_attempt_3;
ALTER TABLE tl_lavote11_usr_attempt ADD CONSTRAINT FK_tl_lavote11_usr_attempt_3 FOREIGN KEY (`vote_nomination_content_id`)
REFERENCES `tl_lavote11_nomination_content` (`uid`) ON DELETE CASCADE ON UPDATE CASCADE;
-- This has to be added as Hibernate is not aware of the cascade
ALTER TABLE tl_lavote11_usr_attempt MODIFY COLUMN vote_nomination_content_id bigint(20) DEFAULT NULL;

-- LDEV-3984 Rename actions to have more simple names
UPDATE lams_tool SET learner_url = 'tool/lavote11/learning.do?dispatch=start&mode=learner',
					 learner_preview_url = 'tool/lavote11/learning.do?dispatch=start&mode=author',
					 learner_progress_url = 'tool/lavote11/learning.do?dispatch=start&mode=teacher'
	WHERE tool_signature = 'lavote11';

UPDATE lams_tool SET monitor_url = 'tool/lavote11/monitoring.do?dispatch=start'
	WHERE tool_signature = 'lavote11';

UPDATE lams_tool SET author_url = 'tool/lavote11/authoring.do?dispatch=start'
	WHERE tool_signature = 'lavote11';

-- LDEV-4180
ALTER TABLE tl_lavote11_content MODIFY COLUMN maxNominationCount SMALLINT NOT NULL DEFAULT 1,
								MODIFY COLUMN minNominationCount SMALLINT NOT NULL DEFAULT 1;

UPDATE lams_tool SET tool_version='20170101' WHERE tool_signature='lavote11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;