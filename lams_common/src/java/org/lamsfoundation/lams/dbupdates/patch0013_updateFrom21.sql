-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

-- Put all sql statements below here

-- LDEV1893 - Create notifications/events service ------------------------------
CREATE TABLE lams_events (
       uid BIGINT NOT NULL UNIQUE auto_increment
     , scope VARCHAR(255) NOT NULL
     , name VARCHAR(255) NOT NULL
     , event_session_id BIGINT
     , triggered TINYINT
     , default_subject VARCHAR(255)
     , default_message TEXT
	 , subject VARCHAR(255)
	 , message TEXT
	 , fail_time DATETIME
	 , INDEX (scope,name,event_session_id)
     , PRIMARY KEY (uid)
)ENGINE=InnoDB;

CREATE TABLE lams_event_subscriptions (
       uid BIGINT NOT NULL UNIQUE auto_increment
     , user_id BIGINT
     , event_uid BIGINT
     , delivery_method_id TINYINT UNSIGNED
     , periodicity BIGINT
     , last_operation_time DATETIME
     , last_operation_message TEXT
     , PRIMARY KEY (uid)
     , CONSTRAINT EventSubscriptionsToUsers FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (event_uid)
     , CONSTRAINT EventSubscriptionsToEvent FOREIGN KEY (event_uid)
                  REFERENCES lams_events (uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

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
)ENGINE=InnoDB;

--  LDEV1929 - Updated script for lesson creation with presence --------------
ALTER TABLE lams_lesson ADD COLUMN learner_presence_avail TINYINT(1) DEFAULT 0;
ALTER TABLE lams_lesson ADD COLUMN learner_im_avail TINYINT(1) DEFAULT 0;

-- LDEV-1299 - Instead of mapping the conditions that open a gate to fictional sequences, there is only a boolean value ------------- 
ALTER TABLE lams_branch_activity_entry MODIFY COLUMN sequence_activity_id BIGINT(20);
ALTER TABLE lams_branch_activity_entry ADD COLUMN open_gate TINYINT;
INSERT INTO lams_learning_activity_type VALUES (14, 'GATE_CONDITION');
INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (10, 14, 'Condition Gate', 'Gate: Opens if conditions are met', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, null, 
	'monitoring/gateExportPortfolio?mode=teacher', 'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

-- LDEV-1896 - extend permission gate
ALTER TABLE lams_activity_learners ADD COLUMN allowed_to_pass TINYINT NOT NULL DEFAULT 0;

-- LDEV-1871 Creating extra column in lams_tool table for tool adapters
ALTER TABLE lams_tool ADD COLUMN ext_lms_id VARCHAR(255);

-- LDEV-1581 Add a column to the lams_grouping table for learner's choice grouping
ALTER TABLE lams_grouping ADD COLUMN equal_number_of_learners_per_group TINYINT DEFAULT 0;
INSERT INTO lams_grouping_type VALUES (4, 'LEARNER_CHOICE_GROUPING');

-- LDEV-2006 - make configuration keys truststorePath and truststorePassword system wide
UPDATE lams_configuration SET config_key='TruststorePath', header_name='config.header.system' WHERE config_key='LDAPTruststorePath';
UPDATE lams_configuration SET config_key='TruststorePassword', header_name='config.header.system' WHERE config_key='LDAPTruststorePassword';

-- LDEV-1260 - local live edit field added
ALTER TABLE lams_lesson ADD COLUMN live_edit_enabled TINYINT(1) DEFAULT 0;

-- LDEV-2028 - ldap now uses a general search filter
UPDATE lams_configuration SET config_key='LDAPSearchFilter', config_value='(cn={0})', description_key='config.ldap.search.filter' WHERE config_key='LDAPPrincipalDNPrefix';
UPDATE lams_configuration SET config_key='LDAPBaseDN', config_value='ou=Users,dc=melcoe,dc=mq,dc=edu,dc=au', description_key='config.ldap.base.dn' WHERE config_key='LDAPPrincipalDNSuffix';

-- LDEV-2029 - configurable initial bind user for ldap
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPBindUserDN','', 'config.ldap.bind.user.dn', 'config.header.ldap', 'STRING', 0);
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('LDAPBindUserPassword','', 'config.ldap.bind.user.password', 'config.header.ldap', 'STRING', 0);

-- Put all sql statements above here

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;