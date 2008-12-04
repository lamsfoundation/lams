-- Need to create the patches table before we start deploying new tools
CREATE TABLE patches (
       system_name VARCHAR(30) NOT NULL
     , patch_level INTEGER(11) NOT NULL
     , patch_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
     , patch_in_progress CHAR(1) NOT NULL DEFAULT 'F'
     , PRIMARY KEY (system_name)
)TYPE=InnoDB;

-- Remove lams_qtz event entries
delete from lams_qtz_SIMPLE_TRIGGERS;
delete from lams_qtz_TRIGGERS;
delete from lams_qtz_JOB_DETAILS;

-- Version strings
update lams_configuration set config_value='2.2' where config_key='Version';
update lams_configuration set config_value='2.2.0.200812021511' where config_key='AuthoringClientVersion' or config_key='MonitorClientVersion' or config_key="LearnerClientVersion" or config_key='ServerVersionNumber';
update lams_configuration set config_value='2008-12-02' where config_key='DictionaryDateCreated';
