-- SQL statements to update from LAMS 2.1/2.1.1
CREATE TABLE tl_lantbk11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT NotebookConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT NotebookConditionToNotebook FOREIGN KEY (content_uid)
                  REFERENCES tl_lantbk11_notebook(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;