-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

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

UPDATE lams_tool SET tool_version='20131212' WHERE tool_signature='lascrt11';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;
