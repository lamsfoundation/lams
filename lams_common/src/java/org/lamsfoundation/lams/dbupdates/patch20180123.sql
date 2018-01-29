SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
--  LDEV-4501 Revise audit log
ALTER TABLE lams_log_event_type
ADD COLUMN area VARCHAR(255) NULL;

UPDATE lams_log_event_type
SET area = "LESSON";

INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(9, 'TYPE_ACTIVITY_EDIT', 'LESSON');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(10, 'TYPE_USER_ORG_ADMIN', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(11, 'TYPE_LOGIN_AS', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(12, 'TYPE_PASSWORD_CHANGE', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(13, 'TYPE_ROLE_FAILURE', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(14, 'TYPE_ACCOUNT_LOCKED', 'SECURITY');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(15, 'TYPE_NOTIFICATION', 'NOTIFICATION');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(16, 'MARK_UPDATED', 'MARKS');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(17, 'MARK_RELEASED', 'MARKS');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(18, 'LEARNER_CONTENT_UPDATED', 'LEARNER_CONTENT');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(19, 'LEARNER_CONTENT_SHOW_HIDE', 'LEARNER_CONTENT');
INSERT INTO lams_log_event_type (log_event_type_id, description, area) VALUES(20, 'UNKNOWN', 'UNKNOWN');

ALTER TABLE lams_log_event 
DROP FOREIGN KEY FK_lams_event_log_5,
DROP FOREIGN KEY FK_lams_event_log_4,
DROP FOREIGN KEY FK_lams_event_log_3,
DROP FOREIGN KEY FK_lams_event_log_2,
DROP FOREIGN KEY FK_lams_event_log_1;

ALTER TABLE lams_log_event 
CHANGE COLUMN learning_design_id target_id BIGINT(20) NULL DEFAULT NULL ,
ADD COLUMN description TEXT,
DROP INDEX occurred_date_time ,
ADD INDEX event_log_occurred_date_time (occurred_date_time ASC),
ADD INDEX FK_event_log_event_type_idx (log_event_type_id ASC),
DROP INDEX FK_lams_event_log_5 ,
DROP INDEX FK_lams_event_log_4 ,
DROP INDEX FK_lams_event_log_3 ,
DROP INDEX user_id ,
DROP INDEX FK_lams_event_log_1 ;

ALTER TABLE lams_log_event 
ADD CONSTRAINT FK_event_log_event_type
  FOREIGN KEY (log_event_type_id)
  REFERENCES lams_log_event_type (log_event_type_id)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
  
COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
