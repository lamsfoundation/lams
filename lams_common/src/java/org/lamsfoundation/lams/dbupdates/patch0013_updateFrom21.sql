-- SQL statements to update from LAMS 2.1/2.1.1

-- Turn off autocommit, so nothing is committed if there is an error
SET AUTOCOMMIT = 0;

----------------------Put all sql statements below here-------------------------

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
ALTER TABLE lams_lesson ADD COLUMN learner_presence_avail TINYINT(1) DEFAULT 0;
ALTER TABLE lams_lesson ADD COLUMN learner_im_avail TINYINT(1) DEFAULT 0;

-- LDEV-1299 - Instead of mapping the conditions that open a gate to fictional sequences, there is only a boolean value ------------- 
ALTER TABLE lams_branch_activity_entry ADD COLUMN open_gate TINYINT;
INSERT INTO lams_learning_activity_type VALUES (14, 'GATE_CONDITION');
INSERT INTO lams_system_tool (system_tool_id, learning_activity_type_id, tool_display_name, description, 
	learner_url, learner_preview_url, learner_progress_url, export_pfolio_learner_url, 
	export_pfolio_class_url, monitor_url, contribute_url, create_date_time)
VALUES (10, 14, 'Condition Gate', 'Gate: Opens if conditions are met', 
	'learning/gate.do?method=knockGate', 'learning/gate.do?method=knockGate', null, null, 
	'monitoring/gateExportPortfolio?mode=teacher', 'monitoring/gate.do?method=viewGate', 
	'monitoring/gate.do?method=viewGate', now()	);

-- LDEV-1871 Creating extra column in lams_tool table for tool adapters
ALTER TABLE lams_tool ADD COLUMN ext_lms_id VARCHAR(255);

-- LDEV-1581 Add a column to the lams_grouping table for learner's choice grouping
ALTER TABLE lams_grouping ADD COLUMN equal_number_of_learners_per_group TINYINT DEFAULT 0;

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

----------------------Put all sql statements above here-------------------------

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;