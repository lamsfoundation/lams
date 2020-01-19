-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LKC-4 Modify Scratchie Tool to use Select Leader Tool Outputs
ALTER TABLE tl_lascrt11_session ADD COLUMN group_leader_uid BIGINT;
ALTER TABLE tl_lascrt11_session ADD INDEX FK_lalead11_session1 (group_leader_uid), ADD CONSTRAINT FK_lalead11_session1 FOREIGN KEY (group_leader_uid) REFERENCES tl_lascrt11_user (uid);
ALTER TABLE tl_lascrt11_user DROP COLUMN leader;

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;