-- This file removes any configs left by the dump so the dump can be used
-- directly without the need to remove anything (apart from unused users)

-- Remove configs 
update lams_configuration set config_value="" where config_key="SMTPServer";
update lams_configuration set config_value="" where config_key="XmppPassword";
update lams_configuration set config_value="ldap://192.158.1.1" where config_key="LDAPProviderURL";

-- From 2.2 onwards. Remove gmap key
update tl_lagmap10_configuration set config_value="" where config_key="GmapKey";

-- From 2.2 onwards. Ensure dimdim tool is set to disabled
update lams_learning_library set valid_flag=0 where title="Dimdim";

-- From 2.2 onwards. Remove lams_qtz event entries
delete from lams_qtz_SIMPLE_TRIGGERS;
delete from lams_qtz_TRIGGERS;
delete from lams_qtz_JOB_DETAILS;

-- From 2.2 onwards. WINDOWS ONLY. Make sure quartz tables are set as capitals
rename table lams_qtz_blob_triggers to lams_qtz_BLOB_TRIGGERS;
rename table lams_qtz_calendars to lams_qtz_CALENDARS;
rename table lams_qtz_cron_triggers to lams_qtz_CRON_TRIGGERS;
rename table lams_qtz_fired_triggers to lams_qtz_FIRED_TRIGGERS;
rename table lams_qtz_job_details to lams_qtz_JOB_DETAILS;
rename table lams_qtz_job_listeners to lams_qtz_JOB_LISTENERS;
rename table lams_qtz_locks to lams_qtz_LOCKS;
rename table lams_qtz_paused_trigger_grps to lams_qtz_PAUSED_TRIGGER_GRPS;
rename table lams_qtz_scheduler_state to lams_qtz_SCHEDULER_STATE;
rename table lams_qtz_simple_triggers to lams_qtz_SIMPLE_TRIGGERS;
rename table lams_qtz_triggers to lams_qtz_TRIGGERS;
rename table lams_qtz_trigger_listeners to lams_qtz_TRIGGER_LISTENERS;

-- From 2.3 onwards. Remove any red5 configs
update lams_configuration set config_value="" where config_key="Red5ServerUrl";
update lams_configuration set config_value="" where config_key="Red5RecordingsUrl";

-- From 2.3 onwards. Ensure video recorder tool is set to disabled
update lams_learning_library set valid_flag=0 where title="VideoRecorder";

