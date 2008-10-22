-- SQL statements to update from LAMS 2.1/2.1.1

CREATE TABLE tl_lasurv11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT SurveyConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT SurveyConditionToSurvey FOREIGN KEY (content_uid)
                  REFERENCES tl_lasurv11_survey(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE tl_lasurv11_condition_questions (
 	   condition_id BIGINT(20)
 	 , question_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,question_uid)
	 , CONSTRAINT SurveyConditionQuestionToSurveyCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lasurv11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT SurveyConditionQuestionToSurveyQuestion FOREIGN KEY (question_uid)
                  REFERENCES tl_lasurv11_question(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)TYPE=InnoDB;

UPDATE lams_tool SET supports_outputs=1 WHERE tool_signature='lasurv11';