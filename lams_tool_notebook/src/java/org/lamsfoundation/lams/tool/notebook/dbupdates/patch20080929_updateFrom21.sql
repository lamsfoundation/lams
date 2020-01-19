-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

CREATE TABLE tl_lantbk11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT NotebookConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT NotebookConditionToNotebook FOREIGN KEY (content_uid)
                  REFERENCES tl_lantbk11_notebook(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

UPDATE lams_tool SET supports_outputs=1 WHERE tool_signature='lantbk11';

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;