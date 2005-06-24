DROP TABLE IF EXISTS lams.tl_laqa11_usr_resp;
DROP TABLE IF EXISTS lams.tl_laqa11_que_usr;
DROP TABLE IF EXISTS lams.tl_laqa11_que_content;
DROP TABLE IF EXISTS lams.tl_laqa11_session;
DROP TABLE IF EXISTS lams.tl_laqa11_content;


CREATE TABLE lams.tl_laqa11_content (
       qa_content_id BIGINT(20) NOT NULL
     , title VARCHAR(100) DEFAULT 'Questions and Answers'
     , instructions VARCHAR(255) DEFAULT 'Please, take a minute to answer the following questions.'
     , creation_date VARCHAR(100)
     , update_date DATETIME
     , questions_sequenced TINYINT(1) NOT NULL
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , monitoring_report_title VARCHAR(100) DEFAULT 'Combined Learner Results'
     , report_title VARCHAR(100) DEFAULT 'Report'
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , synch_in_monitor TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions VARCHAR(255) DEFAULT 'Please, take a minute to fill in offline instructions.'
     , online_instructions VARCHAR(255) DEFAULT 'Please, take a minute to fill in online instructions.'
     , end_learning_message VARCHAR(150) DEFAULT 'Thank you for taking this activity!'
     , qa_session_id BIGINT(20) NOT NULL
     , content_locked TINYINT(1) DEFAULT 0
     , PRIMARY KEY (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_session (
       qa_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_status VARCHAR(100)
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (qa_session_id)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (qa_content_id)
                  REFERENCES lams.tl_laqa11_content (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_que_usr (
       que_usr_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , qa_session_id BIGINT(20) NOT NULL
     , fullname VARCHAR(100)
     , PRIMARY KEY (que_usr_id)
     , INDEX (qa_session_id)
     , CONSTRAINT FK_tl_laqa11_que_usr_2 FOREIGN KEY (qa_session_id)
                  REFERENCES lams.tl_laqa11_session (qa_session_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_que_content (
       qa_que_content_id BIGINT(20) NOT NULL
     , question VARCHAR(255)
     , display_order INT(5)
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (qa_que_content_id)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (qa_content_id)
                  REFERENCES lams.tl_laqa11_content (qa_content_id)
)TYPE=InnoDB;

CREATE TABLE lams.tl_laqa11_usr_resp (
       response_id BIGINT(20) NOT NULL
     , hidden TINYINT(1) DEFAULT 0
     , answer VARCHAR(255)
     , attempt_time DATETIME
     , que_usr_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (response_id)
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_2 FOREIGN KEY (qa_que_content_id)
                  REFERENCES lams.tl_laqa11_que_content (qa_que_content_id)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (que_usr_id)
                  REFERENCES lams.tl_laqa11_que_usr (que_usr_id)
)TYPE=InnoDB;



-- test data for content table
INSERT INTO tl_laqa11_content (qa_content_id, 
								creation_date
								) 
VALUES (${default_content_id},
		NOW());






