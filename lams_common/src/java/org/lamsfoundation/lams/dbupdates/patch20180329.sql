SET AUTOCOMMIT = 0;
set FOREIGN_KEY_CHECKS = 0;
 
-- LDEV-4538 Add columns to gate activity to track user and time of opening

ALTER TABLE lams_learning_activity ADD COLUMN gate_open_user BIGINT(20) AFTER gate_open_flag,
								   ADD COLUMN gate_open_time DATETIME AFTER gate_open_user,
								   ADD CONSTRAINT FK_lams_learning_activity_17 FOREIGN KEY (gate_open_user)
								       REFERENCES lams_user (user_id) ON DELETE SET NULL ON UPDATE CASCADE;

COMMIT;
SET AUTOCOMMIT = 1;
set FOREIGN_KEY_CHECKS = 1;
