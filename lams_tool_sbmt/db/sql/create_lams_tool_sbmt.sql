SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE tl_lasbmt11_report (
       report_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , comments VARCHAR(250)
     , marks BIGINT(20)
     , date_marks_released DATETIME
     , PRIMARY KEY (report_id)
);

CREATE TABLE tl_lasbmt11_session_learners (
       learner_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20)
     , finished TINYINT(1)
     , session_id BIGINT(20)
     , PRIMARY KEY (learner_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_content (
       content_id BIGINT(20) NOT NULL DEFAULT 0
     , title VARCHAR(64) NOT NULL
     , instruction TEXT
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) NOT NULL DEFAULT 0
     , offline_instruction TEXT
     , online_instruction TEXT
     , run_offline_instruction TEXT
     , content_in_use TINYINT(1)
     , lock_on_finished TINYINT(1)
     , PRIMARY KEY (content_id)
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_session (
       session_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , status INT(11) NOT NULL DEFAULT 0
     , content_id BIGINT(20)
     , PRIMARY KEY (session_id)
     , INDEX (content_id)
     , CONSTRAINT FKEC8C77C9FC4BEA1 FOREIGN KEY (content_id)
                  REFERENCES tl_lasbmt11_content (content_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_instruction_files (
       file_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid BIGINT(20)
     , version_id BIGINT(20)
     , type VARCHAR(20)
     , name VARCHAR(255)
     , content_id BIGINT(20)
     , PRIMARY KEY (file_id)
     , INDEX (content_id)
     , CONSTRAINT FKA75538F9FC4BEA1 FOREIGN KEY (content_id)
                  REFERENCES tl_lasbmt11_content (content_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

CREATE TABLE tl_lasbmt11_submission_details (
       submission_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , filePath VARCHAR(250)
     , fileDescription VARCHAR(250)
     , date_of_submission DATETIME
     , uuid BIGINT(20)
     , version_id BIGINT(20)
     , session_id BIGINT(20)
     , learner_id BIGINT(20)
     , PRIMARY KEY (submission_id)
     , INDEX (learner_id)
     , CONSTRAINT FK1411A53C3A4D8629 FOREIGN KEY (learner_id)
                  REFERENCES tl_lasbmt11_session_learners (learner_id) ON DELETE NO ACTION ON UPDATE NO ACTION
     , INDEX (session_id)
     , CONSTRAINT FK1411A53C630DDF64 FOREIGN KEY (session_id)
                  REFERENCES tl_lasbmt11_session (session_id) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

INSERT INTO `tl_lasbmt11_content` (content_id,title,instruction,define_later,run_offline,content_in_use,lock_on_finished) values(1,"Java Submission","Submit your java programs",0,0,0,0);
INSERT INTO `tl_lasbmt11_session` (session_id,content_id,status) values(1,1,1);
SET FOREIGN_KEY_CHECKS=1;