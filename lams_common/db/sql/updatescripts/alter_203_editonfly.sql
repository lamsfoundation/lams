alter table lams_learning_activity
ADD COLUMN read_only TINYINT DEFAULT 0
,ADD COLUMN initialised TINYINT DEFAULT 0;

alter table lams_learning_design
ADD COLUMN edit_override_lock TINYINT DEFAULT 0
,ADD COLUMN edit_override_user_id BIGINT(20)
,ADD COLUMN design_version INTEGER DEFAULT 1
,ADD CONSTRAINT FK_lams_learning_design_7 FOREIGN KEY (edit_override_user_id)
  	                   REFERENCES lams_user (user_id);

alter table lams_lesson
ADD COLUMN locked_for_edit TINYINT DEFAULT 0
,ADD COLUMN version INTEGER DEFAULT 1;

UPDATE lams_learning_activity
SET read_only = 1
WHERE learning_design_id IN (SELECT learning_design_id FROM lams_learning_design WHERE copy_type_id IN (2,3));

insert into lams_configuration (config_key, config_value) values ('AllowLiveEdit','true');
insert into lams_configuration (config_key, config_value) values ('ServerURLContextPath','lams/');
insert into lams_learning_activity_type values (9, 'GATE_SYSTEM');
