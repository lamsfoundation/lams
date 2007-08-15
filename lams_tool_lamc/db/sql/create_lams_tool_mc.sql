CREATE TABLE tl_lamc11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title TEXT
     , instructions TEXT
     , creation_date DATETIME
     , update_date DATETIME
     , reflect TINYINT(1) NOT NULL DEFAULT 0
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions TEXT
     , online_instructions TEXT
     , content_in_use TINYINT(1) NOT NULL DEFAULT 0
     , retries TINYINT(1) NOT NULL DEFAULT 0
     , pass_mark INTEGER
     , show_report TINYINT(1) NOT NULL DEFAULT 0
     , reflectionSubject TEXT
     , showMarks TINYINT(1) NOT NULL DEFAULT 0
     , randomize TINYINT(1) NOT NULL DEFAULT 0
     , UNIQUE UQ_tl_lamc11_content_1 (content_id)
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question TEXT
     , mark INT(5) NOT NULL DEFAULT 1
     , display_order INT(5)
     , mc_content_id BIGINT(20) NOT NULL
     , feedback TEXT
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc11_que_content_1 FOREIGN KEY (mc_content_id)
                  REFERENCES tl_lamc11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_options_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , correct_option TINYINT(1) NOT NULL DEFAULT 0
     , mc_que_content_id BIGINT(20) NOT NULL
     , mc_que_option_text VARCHAR(250)
     , displayOrder INT(5)
     , PRIMARY KEY (uid)
     , INDEX (mc_que_content_id)
     , CONSTRAINT FK_tl_lamc11_options_content_1 FOREIGN KEY (mc_que_content_id)
                  REFERENCES tl_lamc11_que_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , mc_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_name VARCHAR(100)
     , session_status VARCHAR(100)
     , mc_content_id BIGINT(20) NOT NULL
     , UNIQUE UQ_tl_lamc11_session_1 (mc_session_id)
     , PRIMARY KEY (uid)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc_session_1 FOREIGN KEY (mc_content_id)
                  REFERENCES tl_lamc11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , mc_session_id BIGINT(20) NOT NULL
     , username VARCHAR(255)
     , fullname VARCHAR(255)
     , responseFinalised TINYINT(1) NOT NULL DEFAULT 0
     , viewSummaryRequested TINYINT(1) NOT NULL DEFAULT 0
     , last_attempt_order INTEGER
     , last_attempt_total_mark INTEGER
     , PRIMARY KEY (uid)
     , INDEX (mc_session_id)
     , CONSTRAINT FK_tl_lamc11_que_usr_1 FOREIGN KEY (mc_session_id)
                  REFERENCES tl_lamc11_session (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , mc_que_content_id BIGINT(20) NOT NULL
     , mc_que_option_id BIGINT(20) NOT NULL
     , attempt_time DATETIME
     , isAttemptCorrect TINYINT(1) NOT NULL DEFAULT 0
     , mark VARCHAR(255)
     , passed TINYINT(1) NOT NULL DEFAULT 0
     , attemptOrder INTEGER NOT NULL DEFAULT 1
     , finished TINYINT(1) DEFAULT 0
     , PRIMARY KEY (uid)
     , INDEX (mc_que_content_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_2 FOREIGN KEY (mc_que_content_id)
                  REFERENCES tl_lamc11_que_content (uid)
     , INDEX (mc_que_option_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_3 FOREIGN KEY (mc_que_option_id)
                  REFERENCES tl_lamc11_options_content (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_lamc11_usr_attempt_4 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_lamc11_que_usr (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lamc11_uploadedfile (
       submissionId BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , mc_content_id BIGINT(20) NOT NULL
     , isOnline_File TINYINT(1) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , PRIMARY KEY (submissionId)
     , INDEX (mc_content_id)
     , CONSTRAINT FK_tl_lamc11_uploadedFile FOREIGN KEY (mc_content_id)
                  REFERENCES tl_lamc11_content (uid)
)TYPE=InnoDB;


INSERT INTO tl_lamc11_content(uid, content_id , title, instructions, creation_date , reflect, questions_sequenced , created_by , run_offline , define_later,  offline_instructions, online_instructions, content_in_use, retries, show_report, pass_mark) VALUES (1, ${default_content_id} , 'MCQ', 'Instructions', NOW(), 0, 0, 1, 0, 0, '','', 0, 0, 0, 0);

INSERT INTO tl_lamc11_que_content  (uid,question, mark, display_order,  mc_content_id) VALUES (1, 'A Sample question?', 1,1,1);
	
INSERT INTO tl_lamc11_options_content (uid,  correct_option,  displayOrder, mc_que_content_id,  mc_que_option_text) VALUES (1, 0, 1, 1,'Candidate Answer 1');
INSERT INTO tl_lamc11_options_content (uid,  correct_option,  displayOrder, mc_que_content_id,  mc_que_option_text) VALUES (2, 1, 2, 1,'Candidate Answer 2');


