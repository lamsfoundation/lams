CREATE TABLE lams.tl_lamc11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title VARCHAR(250) NOT NULL DEFAULT 'Multiple Choice'
     , instructions VARCHAR(250) NOT NULL DEFAULT 'Please answer the questions until you reach the passmark.'
     , creation_date VARCHAR(100)
     , update_date DATETIME
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , monitoring_report_title VARCHAR(100) NOT NULL DEFAULT 'Combined Learner Results'
     , report_title VARCHAR(100) NOT NULL DEFAULT 'Multiple Choice'
     , run_offline TINYINT(1) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , synch_in_monitor TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions VARCHAR(250) NOT NULL DEFAULT 'offline instructions'
     , online_instructions VARCHAR(250) NOT NULL DEFAULT 'online instructions'
     , end_learning_message VARCHAR(150) NOT NULL DEFAULT 'Thank you!'
     , content_in_use TINYINT(1) NOT NULL DEFAULT 0
     , retries TINYINT(1) NOT NULL DEFAULT 0
     , show_feedback TINYINT(1) NOT NULL DEFAULT 0
     , pass_mark INTEGER
     , show_report TINYINT(1) NOT NULL DEFAULT 0
     , UNIQUE UQ_tl_lamc11_content_1 (content_id)
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question VARCHAR(255)
     , weight INT(5) NOT NULL DEFAULT 0
     , disabled TINYINT(1) NOT NULL DEFAULT 1
     , feedbackCorrect VARCHAR(255)
     , feedbackIncorrect VARCHAR(255)
     , display_order INT(5)
     , mc_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (mc_content_id)
                  REFERENCES lams.tl_lamc11_content (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_options_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , correct_option TINYINT(1) NOT NULL DEFAULT 0
     , mc_que_content_id BIGINT(20) NOT NULL
     , mc_que_option_text VARCHAR(250)
     , PRIMARY KEY (uid)
     , INDEX (mc_que_content_id)
     , CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (mc_que_content_id)
                  REFERENCES lams.tl_lamc11_que_content (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , mc_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , mc_content_id BIGINT(20) NOT NULL
     , UNIQUE UQ_tl_lamc11_session_1 (mc_session_id)
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (mc_content_id)
                  REFERENCES lams.tl_lamc11_content (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , mc_session_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , fullname VARCHAR(100)
     , UNIQUE UQ_tl_lamc11_que_usr_1 (que_usr_id)
     , PRIMARY KEY (uid)
     , INDEX (mc_session_id)
     , CONSTRAINT FK_tl_lamc_tool_usr_1 FOREIGN KEY (mc_session_id)
                  REFERENCES lams.tl_lamc11_session (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , mc_que_content_id BIGINT(20) NOT NULL
     , mc_que_option_id BIGINT(20) NOT NULL
     , attempt_time DATETIME
     , time_zone VARCHAR(255)
     , PRIMARY KEY (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_1 FOREIGN KEY (que_usr_id)
                  REFERENCES lams.tl_lamc11_que_usr (uid)
     , INDEX (mc_que_content_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_2 FOREIGN KEY (mc_que_content_id)
                  REFERENCES lams.tl_lamc11_que_content (uid)
     , INDEX (mc_que_option_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_3 FOREIGN KEY (mc_que_option_id)
                  REFERENCES lams.tl_lamc11_options_content (uid)
)TYPE=InnoDB;

CREATE TABLE lams.tl_lamc11_uploadedFile (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , mc_content_id BIGINT(20) NOT NULL
     , isOnline_File TINYINT(1) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc11_uploadedFile FOREIGN KEY (mc_content_id)
                  REFERENCES lams.tl_lamc11_content (uid)
)TYPE=InnoDB;

