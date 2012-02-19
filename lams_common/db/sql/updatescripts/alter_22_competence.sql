CREATE TABLE lams_competence (
       competence_id BIGINT NOT NULL UNIQUE auto_increment
     , learning_design_id BIGINT
     , description TEXT
     , title VARCHAR(255) 
	 , UNIQUE KEY (learning_design_id, title)
     , PRIMARY KEY (competence_id)
     , CONSTRAINT LearningDesignCompetenceMap FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design(learning_design_id) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

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
)ENGINE=InnoDB;