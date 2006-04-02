CREATE TABLE tl_laqa11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , qa_content_id BIGINT(20) NOT NULL
     , title TEXT
     , instructions TEXT
     , creation_date VARCHAR(100)
     , update_date DATETIME
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , monitoring_report_title VARCHAR(100) DEFAULT 'Combined Learner Results'
     , report_title VARCHAR(100) DEFAULT 'Report'
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , synch_in_monitor TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions TEXT
     , online_instructions TEXT
     , end_learning_message VARCHAR(150) DEFAULT 'Thank you!'
     , content_inUse TINYINT(1) DEFAULT 0
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , qa_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_name VARCHAR(100)
     , session_status VARCHAR(100)
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_session_1 FOREIGN KEY (qa_content_id)
                  REFERENCES tl_laqa11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , qa_session_id BIGINT(20) NOT NULL
     , fullname VARCHAR(100)
     , PRIMARY KEY (uid)
     , INDEX (qa_session_id)
     , CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (qa_session_id)
                  REFERENCES tl_laqa11_session (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question TEXT
     , display_order INT(5)
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (qa_content_id)
                  REFERENCES tl_laqa11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_usr_resp (
       response_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , hidden TINYINT(1) DEFAULT 0
     , answer VARCHAR(255)
     , time_zone VARCHAR(255)
     , attempt_time DATETIME
     , que_usr_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (response_id)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_3 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_laqa11_que_usr (uid)
     , INDEX (qa_que_content_id)
     , CONSTRAINT FK_tl_laqa11_usr_resp_2 FOREIGN KEY (qa_que_content_id)
                  REFERENCES tl_laqa11_que_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_uploadedfile (
       submissionId BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , isOnline_File TINYINT(1) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (submissionId)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_uploadedfile_1 FOREIGN KEY (qa_content_id)
                  REFERENCES tl_laqa11_content (uid)
)TYPE=InnoDB;


-- data for content table
INSERT INTO tl_laqa11_content (qa_content_id, 	creation_date)  VALUES (${default_content_id}, NOW());

-- data for content questions table
INSERT INTO tl_laqa11_que_content (question, display_order, qa_content_id) VALUES ('What is the largest continent in the world?',1,1);

