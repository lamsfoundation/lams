-- SQL statements to update from LAMS 2.1/2.1.1

CREATE TABLE tl_lafrum11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT ForumConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionToForum FOREIGN KEY (content_uid)
                  REFERENCES tl_lafrum11_forum(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE tl_lafrum11_condition_topics (
 	   condition_id BIGINT(20)
 	 , topic_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,topic_uid)
	 , CONSTRAINT ForumConditionQuestionToForumCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lafrum11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT ForumConditionQuestionToForumQuestion FOREIGN KEY (topic_uid)
                  REFERENCES tl_lafrum11_message(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)TYPE=InnoDB;