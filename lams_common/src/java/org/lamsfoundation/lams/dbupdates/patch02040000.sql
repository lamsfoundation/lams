-- SQL statements to update from LAMS 2.3.1

SET AUTOCOMMIT = 0;

-- LDEV-2369
ALTER TABLE lams_user ADD COLUMN tutorials_disabled TINYINT(1) DEFAULT 0;
ALTER TABLE lams_user ADD COLUMN first_login TINYINT(1) DEFAULT 1;

CREATE TABLE lams_user_disabled_tutorials (
     user_id BIGINT(20) NOT NULL
   , page_str CHAR(5) NOT NULL
   , CONSTRAINT FK_lams_user_disabled_tutorials_1 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
   , PRIMARY KEY (user_id,page_str)
)ENGINE=InnoDB;

-- LDEV-2374
ALTER TABLE lams_learning_activity ADD COLUMN transition_to_id BIGINT(20);
ALTER TABLE lams_learning_activity ADD COLUMN transition_from_id BIGINT(20);

ALTER TABLE lams_learning_transition MODIFY COLUMN learning_design_id BIGINT(20);
ALTER TABLE lams_learning_transition DROP INDEX UQ_transition_activities;
ALTER TABLE lams_learning_transition ADD COLUMN transition_type TINYINT NOT NULL DEFAULT 0;
ALTER TABLE lams_learning_activity  
	  ADD CONSTRAINT FK_lams_learning_activity_15 FOREIGN KEY (transition_to_id)
                  REFERENCES lams_learning_transition (transition_id)
     , ADD CONSTRAINT FK_lams_learning_activity_16 FOREIGN KEY (transition_from_id)
                  REFERENCES lams_learning_transition (transition_id);

CREATE TABLE lams_data_flow (
	  data_flow_object_id BIGINT(20) NOT NULL auto_increment
	, transition_id BIGINT(20) NOT NULL
	, order_id INT(11) 
	, name VARCHAR(255) NOT NULL
	, display_name VARCHAR(255)
	, tool_assigment_id INT(11)
	, CONSTRAINT FK_lams_learning_transition_1 FOREIGN KEY (transition_id)
                  REFERENCES lams_learning_transition (transition_id) ON DELETE CASCADE ON UPDATE CASCADE
	, PRIMARY KEY (data_flow_object_id)
)ENGINE=InnoDB;

COMMIT;
SET AUTOCOMMIT = 1;
