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

-- From 2.2 onwards. Remove lams_qtz event entires
delete from lams_qtz_SIMPLE_TRIGGERS;
delete from lams_qtz_TRIGGERS;
delete from lams_qtz_JOB_DETAILS;

-- From 2.2 onwards. WINDOWS ONLY. Make sure quatz tables are set as capitals
Replace lams_qtz_blob_triggers with lams_qtz_BLOB_TRIGGERS
Replace lams_qtz_calendars with lams_qtz_CALENDARS
Replace lams_qtz_cron_triggers with lams_qtz_CRON_TRIGGERS
Replace lams_qtz_fired_triggers with lams_qtz_FIRED_TRIGGERS
Replace lams_qtz_job_details with lams_qtz_JOB_DETAILS
Replace lams_qtz_job_listeners with lams_qtz_JOB_LISTENERS
Replace lams_qtz_locks with ams_qtz_LOCKS
Replace lams_qtz_paused_trigger_grps with lams_qtz_PAUSED_TRIGGER_GRPS
Replace lams_qtz_scheduler_state with lams_qtz_SCHEDULER_STATE
Replace lams_qtz_simple_triggers with lams_qtz_SIMPLE_TRIGGERS
Replace lams_qtz_triggers with lams_qtz_TRIGGERS
Replace lams_qtz_trigger_listeners with lams_qtz_TRIGGER_LISTENERS

