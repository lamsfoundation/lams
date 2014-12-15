SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- LDEV-3337: Update Quartz library to versiON 2.2.1

-- drop tables that are no longer used

DROP TABLE lams_qtz_job_listeners;
DROP TABLE lams_qtz_trigger_listeners;

-- drop columns that are no longer used

ALTER TABLE lams_qtz_job_details DROP COLUMN is_volatile;
ALTER TABLE lams_qtz_triggers DROP COLUMN is_volatile;
ALTER TABLE lams_qtz_fired_triggers DROP COLUMN is_volatile;
ALTER TABLE lams_qtz_scheduler_state DROP COLUMN recoverer;

-- add and modify column definitions to match new schema

ALTER TABLE lams_qtz_job_details MODIFY COLUMN job_name VARCHAR(200);
ALTER TABLE lams_qtz_job_details MODIFY COLUMN job_group VARCHAR(200);
ALTER TABLE lams_qtz_job_details MODIFY COLUMN description VARCHAR(250);
ALTER TABLE lams_qtz_job_details MODIFY COLUMN job_class_name VARCHAR(250);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN trigger_name VARCHAR(200);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN job_name VARCHAR(200);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN job_group VARCHAR(200);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN description VARCHAR(250);
ALTER TABLE lams_qtz_triggers MODIFY COLUMN calendar_name VARCHAR(200);
ALTER TABLE lams_qtz_triggers ADD COLUMN priority INT AFTER prev_fire_time;
ALTER TABLE lams_qtz_simple_triggers MODIFY COLUMN trigger_name VARCHAR(200);
ALTER TABLE lams_qtz_simple_triggers MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_simple_triggers MODIFY COLUMN times_triggered BIGINT(10);
ALTER TABLE lams_qtz_cron_triggers MODIFY COLUMN trigger_name VARCHAR(200);
ALTER TABLE lams_qtz_cron_triggers MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_cron_triggers MODIFY COLUMN cron_expression VARCHAR(120);
ALTER TABLE lams_qtz_blob_triggers MODIFY COLUMN trigger_name VARCHAR(200);
ALTER TABLE lams_qtz_blob_triggers MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_calendars MODIFY COLUMN calendar_name VARCHAR(200);
ALTER TABLE lams_qtz_paused_trigger_grps MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers MODIFY COLUMN trigger_name VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers MODIFY COLUMN trigger_group VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers MODIFY COLUMN instance_name VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers MODIFY COLUMN job_name VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers MODIFY COLUMN job_group VARCHAR(200);
ALTER TABLE lams_qtz_fired_triggers ADD COLUMN sched_time BIGINT(13) NOT NULL AFTER fired_time;
ALTER TABLE lams_qtz_fired_triggers ADD COLUMN priority INTEGER NOT NULL AFTER sched_time;
ALTER TABLE lams_qtz_scheduler_state MODIFY COLUMN instance_name VARCHAR(200);

-- add new columns that replace the 'is_stateful' column

ALTER TABLE lams_qtz_job_details ADD COLUMN is_nonconcurrent bool AFTER is_durable;
ALTER TABLE lams_qtz_job_details ADD COLUMN is_update_data bool AFTER is_nonconcurrent;
UPDATE lams_qtz_job_details SET is_nonconcurrent = is_stateful;
UPDATE lams_qtz_job_details SET is_update_data = is_stateful;
ALTER TABLE lams_qtz_job_details DROP COLUMN is_stateful;
ALTER TABLE lams_qtz_fired_triggers ADD COLUMN is_nonconcurrent bool;
UPDATE lams_qtz_fired_triggers SET is_nonconcurrent = is_stateful;
ALTER TABLE lams_qtz_fired_triggers DROP COLUMN is_stateful;

-- add new 'sched_name' COLUMN to all tables

ALTER TABLE lams_qtz_blob_triggers ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_calendars ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_cron_triggers ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_fired_triggers ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_job_details ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_locks ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_paused_trigger_grps ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_scheduler_state ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_simple_triggers ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;
ALTER TABLE lams_qtz_triggers ADD COLUMN sched_name VARCHAR(120) NOT NULL DEFAULT 'LamsQuartzScheduler' FIRST;

-- drop all primary and foreign key constraints, so that we can define new ones

ALTER TABLE lams_qtz_triggers DROP FOREIGN KEY lams_qtz_triggers_ibfk_1;
ALTER TABLE lams_qtz_triggers DROP INDEX job_name;
ALTER TABLE lams_qtz_blob_triggers DROP FOREIGN KEY lams_qtz_blob_triggers_ibfk_1;
ALTER TABLE lams_qtz_blob_triggers DROP PRIMARY KEY;
ALTER TABLE lams_qtz_simple_triggers DROP FOREIGN KEY lams_qtz_simple_triggers_ibfk_1;
ALTER TABLE lams_qtz_simple_triggers DROP PRIMARY KEY;
ALTER TABLE lams_qtz_cron_triggers DROP FOREIGN KEY lams_qtz_cron_triggers_ibfk_1;
ALTER TABLE lams_qtz_cron_triggers DROP PRIMARY KEY;
ALTER TABLE lams_qtz_job_details DROP PRIMARY KEY;
ALTER TABLE lams_qtz_job_details ADD PRIMARY KEY (sched_name, job_name, job_group);
ALTER TABLE lams_qtz_triggers DROP PRIMARY KEY;

-- add all primary and foreign key constraints, based on new columns

ALTER TABLE lams_qtz_triggers ADD PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_triggers ADD FOREIGN KEY (sched_name, job_name, job_group) REFERENCES lams_qtz_job_details(sched_name, job_name, job_group);
ALTER TABLE lams_qtz_blob_triggers ADD PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_blob_triggers ADD FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES lams_qtz_triggers(sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_cron_triggers ADD PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_cron_triggers ADD FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES lams_qtz_triggers(sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_simple_triggers ADD PRIMARY KEY (sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_simple_triggers ADD FOREIGN KEY (sched_name, trigger_name, trigger_group) REFERENCES lams_qtz_triggers(sched_name, trigger_name, trigger_group);
ALTER TABLE lams_qtz_fired_triggers DROP PRIMARY KEY;
ALTER TABLE lams_qtz_fired_triggers ADD PRIMARY KEY (sched_name, entry_id);
ALTER TABLE lams_qtz_calendars DROP PRIMARY KEY;
ALTER TABLE lams_qtz_calendars ADD PRIMARY KEY (sched_name, calendar_name);
ALTER TABLE lams_qtz_locks DROP PRIMARY KEY;
ALTER TABLE lams_qtz_locks ADD PRIMARY KEY (sched_name, lock_name);
ALTER TABLE lams_qtz_paused_trigger_grps DROP PRIMARY KEY;
ALTER TABLE lams_qtz_paused_trigger_grps ADD PRIMARY KEY (sched_name, trigger_group);
ALTER TABLE lams_qtz_scheduler_state DROP PRIMARY KEY;
ALTER TABLE lams_qtz_scheduler_state ADD PRIMARY KEY (sched_name, instance_name);

-- add new simprop_triggers table

CREATE TABLE lams_qtz_simprop_triggers
 (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 BOOL NULL,
    BOOL_PROP_2 BOOL NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES lams_qtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
) ENGINE=InnoDB;

-- create indexes for faster queries

CREATE INDEX idx_lams_qtz_j_req_recovery ON lams_qtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX idx_lams_qtz_j_grp ON lams_qtz_job_details(SCHED_NAME,JOB_GROUP);
CREATE INDEX idx_lams_qtz_t_j ON lams_qtz_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX idx_lams_qtz_t_jg ON lams_qtz_triggers(SCHED_NAME,JOB_GROUP);
CREATE INDEX idx_lams_qtz_t_c ON lams_qtz_triggers(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX idx_lams_qtz_t_g ON lams_qtz_triggers(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX idx_lams_qtz_t_state ON lams_qtz_triggers(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX idx_lams_qtz_t_n_state ON lams_qtz_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX idx_lams_qtz_t_n_g_state ON lams_qtz_triggers(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX idx_lams_qtz_t_next_fire_time ON lams_qtz_triggers(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX idx_lams_qtz_t_nft_st ON lams_qtz_triggers(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX idx_lams_qtz_t_nft_misfire ON lams_qtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX idx_lams_qtz_t_nft_st_misfire ON lams_qtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX idx_lams_qtz_t_nft_st_misfire_grp ON lams_qtz_triggers(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX idx_lams_qtz_ft_trig_inst_name ON lams_qtz_fired_triggers(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX idx_lams_qtz_ft_inst_job_req_rcvry ON lams_qtz_fired_triggers(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX idx_lams_qtz_ft_j_g ON lams_qtz_fired_triggers(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX idx_lams_qtz_ft_jg ON lams_qtz_fired_triggers(SCHED_NAME,JOB_GROUP);
CREATE INDEX idx_lams_qtz_ft_t_g ON lams_qtz_fired_triggers(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX idx_lams_qtz_ft_tg ON lams_qtz_fired_triggers(SCHED_NAME,TRIGGER_GROUP);

COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS = 1;