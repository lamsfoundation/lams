-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

CREATE TABLE tl_lafrum11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ForumConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionToForum FOREIGN KEY (content_uid)
                  REFERENCES tl_lafrum11_forum(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

CREATE TABLE tl_lafrum11_condition_topics (
 	   condition_id BIGINT(20)
 	 , topic_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,topic_uid)
	 , CONSTRAINT ForumConditionQuestionToForumCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lafrum11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionQuestionToForumQuestion FOREIGN KEY (topic_uid)
                  REFERENCES tl_lafrum11_message(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)ENGINE=InnoDB;

ALTER TABLE tl_lafrum11_forum ADD COLUMN mark_release_notify tinyint DEFAULT 0;
-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;