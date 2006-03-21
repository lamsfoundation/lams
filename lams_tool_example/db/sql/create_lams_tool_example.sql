SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS tl_laex11_user;
DROP TABLE IF EXISTS tl_laex11_attachment;
DROP TABLE IF EXISTS tl_laex11_session;
DROP TABLE IF EXISTS tl_laex11_example;

CREATE TABLE tl_laex11_example (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , create_date DATETIME
     , update_date DATETIME
     , create_by BIGINT(20)
     , title VARCHAR(255)
     , instructions TEXT
     , run_offline TINYINT(1)
     , lock_on_finished TINYINT(1)
     , online_instructions TEXT
     , offline_instructions TEXT
     , content_in_use TINYINT(1)
     , define_later TINYINT(1)
     , tool_content_id BIGINT(20)
     , PRIMARY KEY (uid)
)TYPE=InnoDB;
CREATE INDEX content_id ON tl_laex11_example (tool_content_id ASC);

CREATE TABLE tl_laex11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , session_end_date DATETIME
     , session_start_date DATETIME
     , status INT(11)
     , example_uid BIGINT(20)
     , session_id BIGINT(20)
     , session_name VARCHAR(250)
     , PRIMARY KEY (uid)
     , INDEX (example_uid)
     , CONSTRAINT FK_SESSION FOREIGN KEY (example_uid)
                  REFERENCES tl_laex11_example (uid) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

CREATE TABLE tl_laex11_attachment (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , file_version_id BIGINT(20)
     , file_type VARCHAR(255)
     , file_name VARCHAR(255)
     , file_uuid BIGINT(20)
     , create_date DATETIME
     , example_uid BIGINT(20)
     , PRIMARY KEY (uid)
     , INDEX (example_uid)
     , CONSTRAINT FK_ATTACHMENT FOREIGN KEY (example_uid)
                  REFERENCES tl_laex11_example (uid) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

CREATE TABLE tl_laex11_user (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20)
     , last_name VARCHAR(255)
     , first_name VARCHAR(255)
     , session_id BIGINT(20)
     , PRIMARY KEY (uid)
     , INDEX (session_id)
     , CONSTRAINT FK_SESSION_USER FOREIGN KEY (session_id)
                  REFERENCES tl_laex11_session (uid) ON DELETE NO ACTION ON UPDATE NO ACTION
)TYPE=InnoDB;

--INSERT INTO tl_laex11_example (title,instructions,online_instructions,offline_instructions,tool_content_id,run_offline,lock_on_finished,content_in_use,define_later) 
--VALUES("Example Title","Example Instruction","Online instructions","Offline instructions",${default_content_id},0,0,0,0);

SET FOREIGN_KEY_CHECKS=1;