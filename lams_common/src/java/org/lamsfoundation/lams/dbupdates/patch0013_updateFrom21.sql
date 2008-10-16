-- SQL statements to update from LAMS 2.1/2.1.1
-- LDEV1909 - Competence Editor Update Scripts ---------------------------------
CREATE TABLE lams_competence (
       competence_id BIGINT NOT NULL UNIQUE auto_increment
     , learning_design_id BIGINT
     , description TEXT
     , title VARCHAR(255) 
	 , UNIQUE KEY (learning_design_id, title)
     , PRIMARY KEY (competence_id)
     , CONSTRAINT LearningDesignCompetenceMap FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design(learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE lams_competence_mapping (
       competence_mapping_id BIGINT NOT NULL UNIQUE auto_increment
     , competence_id BIGINT
     , activity_id BIGINT 
     , PRIMARY KEY (competence_mapping_id)
	 , INDEX (activity_id)
	 , UNIQUE KEY (competence_id, activity_id)
     , CONSTRAINT FK_lams_competence_mapping_1 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT FK_lams_competence_mapping_2 FOREIGN KEY (competence_id)
	                  REFERENCES lams_competence (competence_id) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

-- LDEV-1604 - Text based complex conditions -----------------------------------
CREATE TABLE lams_text_search_condition (
       condition_id BIGINT(20) NOT NULL
	 , text_search_all_words TEXT
	 , text_search_phrase TEXT
	 , text_search_any_words TEXT
	 , text_search_excluded_words TEXT
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT TextSearchConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

--  LDEV1929 - Updated script for lesson creation with presence --------------
CREATE TABLE lams_lesson (
       learner_presence_avail TINYINT(1) DEFAULT 0
     , learner_im_avail TINYINT(1) DEFAULT 0
)TYPE=InnoDB;