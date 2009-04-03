-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

CREATE TABLE tl_lamind10_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT MindmapConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT MindmapConditionToMindmap FOREIGN KEY (content_uid)
                  REFERENCES tl_lamind10_mindmap(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

UPDATE lams_tool SET supports_outputs=1 WHERE tool_signature='lamind10';

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
