-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS=0;
----------------------Put all sql statements below here-------------------------

-- LDEV-3155 adding all the leader aware tools (lamc,assessment,qa) to regular LAMS
ALTER TABLE tl_laasse10_assessment ADD COLUMN use_select_leader_tool_ouput TINYINT(1) NOT NULL DEFAULT 0;
ALTER TABLE tl_laasse10_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_laasse10_session ADD INDEX FK_laasse10_session_1 (group_leader_uid), ADD CONSTRAINT tl_laasse10_session FOREIGN KEY (group_leader_uid) REFERENCES tl_laasse10_user (uid) ON DELETE CASCADE ON UPDATE CASCADE;

-- Adding a constraint to the assessment user table so no same user_id and session_id can be repetead 
-- (Please, note there are some duplicate users exist (their session_uid is null), they got formed when techers created questions in authoring. It's required to keep track of questions' authors.) 
ALTER TABLE tl_laasse10_user ADD UNIQUE INDEX(user_id, session_uid);

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;