SET AUTOCOMMIT = 0;

-- SIF-10 Addding a release date to the lesson table so we can 
-- track release events
alter table lams_lesson add column release_date DATETIME;
alter table lams_lesson add index idx_release_date (release_date);

-- 	LDEV-2564 Option in "Edit configuration settings" for not displaying the "All My Lesson" option in My Profile tab
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required) 
values ('ShowAllMyLessonLink','true', 'config.show.all.my.lesson.link', 'config.header.features', 'BOOLEAN', 1);

-- 	LDEV-2544 Default Timezones
CREATE TABLE lams_timezone (
	   id BIGINT(20) NOT NULL AUTO_INCREMENT
     , timezone_id VARCHAR(255) NOT NULL
     , server_timezone TINYINT DEFAULT 0
     , PRIMARY KEY (id)
)TYPE=InnoDB;

INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+12');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+11');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+10');
INSERT INTO lams_timezone (timezone_id) VALUES ('US/Alaska');
INSERT INTO lams_timezone (timezone_id) VALUES ('America/Vancouver');
INSERT INTO lams_timezone (timezone_id) VALUES ('America/Denver');
INSERT INTO lams_timezone (timezone_id) VALUES ('America/Chicago');
INSERT INTO lams_timezone (timezone_id) VALUES ('America/Detroit');
INSERT INTO lams_timezone (timezone_id) VALUES ('America/Halifax');
INSERT INTO lams_timezone (timezone_id) VALUES ('Canada/Newfoundland');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+3');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+2');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT+1');
INSERT INTO lams_timezone (timezone_id) VALUES ('Europe/London');
INSERT INTO lams_timezone (timezone_id) VALUES ('Europe/Brussels');
INSERT INTO lams_timezone (timezone_id) VALUES ('Europe/Athens');
INSERT INTO lams_timezone (timezone_id) VALUES ('Europe/Moscow');
INSERT INTO lams_timezone (timezone_id) VALUES ('Asia/Tehran');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-4');
INSERT INTO lams_timezone (timezone_id) VALUES ('Asia/Kabul');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-5');
INSERT INTO lams_timezone (timezone_id) VALUES ('Asia/Calcutta');
INSERT INTO lams_timezone (timezone_id) VALUES ('Asia/Katmandu');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-6');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-7');
INSERT INTO lams_timezone (timezone_id) VALUES ('Australia/West');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-9');
INSERT INTO lams_timezone (timezone_id) VALUES ('Australia/Adelaide');
INSERT INTO lams_timezone (timezone_id) VALUES ('Australia/Sydney');
INSERT INTO lams_timezone (timezone_id) VALUES ('Etc/GMT-11');
INSERT INTO lams_timezone (timezone_id) VALUES ('Pacific/Auckland');
INSERT INTO lams_timezone (timezone_id) VALUES ('Pacific/Tongatapu');
INSERT INTO lams_timezone (timezone_id) VALUES ('Pacific/Kiritimati');

ALTER TABLE lams_user MODIFY COLUMN timezone VARCHAR(255);

UPDATE lams_user SET timezone='Etc/GMT+12' WHERE timezone='0';
UPDATE lams_user SET timezone='Etc/GMT+11' WHERE timezone='1';
UPDATE lams_user SET timezone='Etc/GMT+10' WHERE timezone='2';
UPDATE lams_user SET timezone='US/Alaska' WHERE timezone='3';
UPDATE lams_user SET timezone='America/Vancouver' WHERE timezone='4';
UPDATE lams_user SET timezone='America/Denver' WHERE timezone='5';
UPDATE lams_user SET timezone='America/Chicago' WHERE timezone='6';
UPDATE lams_user SET timezone='America/Detroit' WHERE timezone='7';
UPDATE lams_user SET timezone='America/Halifax' WHERE timezone='8';
UPDATE lams_user SET timezone='Canada/Newfoundland' WHERE timezone='9';
UPDATE lams_user SET timezone='Etc/GMT+3' WHERE timezone='10';
UPDATE lams_user SET timezone='Etc/GMT+2' WHERE timezone='11';
UPDATE lams_user SET timezone='Etc/GMT+1' WHERE timezone='12';
UPDATE lams_user SET timezone='Europe/London' WHERE timezone='13';
UPDATE lams_user SET timezone='Europe/Brussels' WHERE timezone='14';
UPDATE lams_user SET timezone='Europe/Athens' WHERE timezone='15';
UPDATE lams_user SET timezone='Europe/Moscow' WHERE timezone='16';
UPDATE lams_user SET timezone='Asia/Tehran' WHERE timezone='17';
UPDATE lams_user SET timezone='Etc/GMT-4' WHERE timezone='18';
UPDATE lams_user SET timezone='Asia/Kabul' WHERE timezone='19';
UPDATE lams_user SET timezone='Etc/GMT-5' WHERE timezone='20';
UPDATE lams_user SET timezone='Asia/Calcutta' WHERE timezone='21';
UPDATE lams_user SET timezone='Asia/Katmandu' WHERE timezone='22';
UPDATE lams_user SET timezone='Etc/GMT-6' WHERE timezone='23';
UPDATE lams_user SET timezone='Etc/GMT-7' WHERE timezone='24';
UPDATE lams_user SET timezone='Australia/West' WHERE timezone='25';
UPDATE lams_user SET timezone='Etc/GMT-9' WHERE timezone='26';
UPDATE lams_user SET timezone='Australia/Adelaide' WHERE timezone='27';
UPDATE lams_user SET timezone='Australia/Sydney' WHERE timezone='28';
UPDATE lams_user SET timezone='Etc/GMT-11' WHERE timezone='29';
UPDATE lams_user SET timezone='Pacific/Auckland' WHERE timezone='30';

-- LDEV-2550

DROP TABLE IF EXISTS lams_log_event;
CREATE TABLE lams_log_event (
       id BIGINT(20) NOT NULL AUTO_INCREMENT
     , log_event_type_id INT(5) NOT NULL
     , user_id BIGINT(20) NOT NULL
     , occurred_date_time DATETIME NOT NULL
     , learning_design_id BIGINT(20)
     , lesson_id BIGINT(20)
     , activity_id BIGINT(20)
     , PRIMARY KEY (id)
     , INDEX (occurred_date_time)
     , CONSTRAINT FK_lams_event_log_1 FOREIGN KEY (log_event_type_id)
                  REFERENCES lams_log_event_type (log_event_type_id)
	 , INDEX (user_id)                  
     , CONSTRAINT FK_lams_event_log_2 FOREIGN KEY (user_id)
                  REFERENCES lams_user (user_id)           
     , CONSTRAINT FK_lams_event_log_3 FOREIGN KEY (learning_design_id)
                  REFERENCES lams_learning_design (learning_design_id)
     , CONSTRAINT FK_lams_event_log_4 FOREIGN KEY (lesson_id)
                  REFERENCES lams_lesson (lesson_id)
     , CONSTRAINT FK_lams_event_log_5 FOREIGN KEY (activity_id)
                  REFERENCES lams_learning_activity (activity_id)
)TYPE=InnoDB;

DELETE FROM lams_log_event_type;
INSERT INTO lams_log_event_type VALUES (1, 'TYPE_TEACHER_LEARNING_DESIGN_CREATE');
INSERT INTO lams_log_event_type VALUES (2, 'TYPE_TEACHER_LESSON_CREATE');
INSERT INTO lams_log_event_type VALUES (3, 'TYPE_TEACHER_LESSON_START');
INSERT INTO lams_log_event_type VALUES (4, 'TYPE_TEACHER_LESSON_CHANGE_STATE');
INSERT INTO lams_log_event_type VALUES (5, 'TYPE_LEARNER_ACTIVITY_START');
INSERT INTO lams_log_event_type VALUES (6, 'TYPE_LEARNER_ACTIVITY_FINISH');

-- LDEV-2639 Make print button in LAMS learner optional 
insert into lams_configuration (config_key, config_value, description_key, header_name, format, required)  
values ('DisplayPrintButton','false', 'config.display.print.button', 'config.header.features', 'BOOLEAN', 1);

COMMIT;
SET AUTOCOMMIT = 1;