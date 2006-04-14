CREATE TABLE tl_vote11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title TEXT NOT NULL
     , posting TEXT
     , instructions TEXT NOT NULL
     , creation_date DATETIME
     , update_date DATETIME
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , monitoring_report_title TEXT NOT NULL
     , report_title TEXT NOT NULL
     , run_offline TINYINT(1) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions TEXT
     , online_instructions TEXT
     , end_learning_message TEXT NOT NULL
     , content_in_use TINYINT(1) NOT NULL DEFAULT 0
     , lock_on_finish TINYINT NOT NULL DEFAULT 0
     , retries TINYINT(1) NOT NULL DEFAULT 0
     , show_report TINYINT(1) NOT NULL DEFAULT 0
     , UNIQUE UQ_tl_lamc11_content_1 (content_id)
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question VARCHAR(255)
     , display_order INT(5)
     , vote_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_vote_que_content_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_vote11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_session (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , vote_session_id BIGINT(20) NOT NULL
     , session_start_date DATETIME
     , session_end_date DATETIME
     , session_name VARCHAR(100)
     , session_status VARCHAR(100)
     , vote_content_id BIGINT(20) NOT NULL
     , UNIQUE UQ_tl_lamc11_session_1 (vote_session_id)
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_vote_session_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_vote11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_que_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , vote_session_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , fullname VARCHAR(100)
     , UNIQUE UQ_tl_lamc11_que_usr_1 (que_usr_id)
     , PRIMARY KEY (uid)
     , INDEX (vote_session_id)
     , CONSTRAINT FK_tl_vote_que_usr_1 FOREIGN KEY (vote_session_id)
                  REFERENCES tl_vote11_session (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , vote_que_content_id BIGINT(20) NOT NULL
     , vote_que_option_id BIGINT(20) NOT NULL
     , attempt_time DATETIME
     , time_zone VARCHAR(255)
     , userEntry VARCHAR(255)
     , isNominated TINYINT NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_vote_usr_attempt_1 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_vote11_que_usr (uid)
     , INDEX (vote_que_content_id)
     , CONSTRAINT FK_tl_vote_usr_attempt_4 FOREIGN KEY (vote_que_content_id)
                  REFERENCES tl_vote11_que_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_uploadedfile (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , vote_content_id BIGINT(20) NOT NULL
     , isOnline_File TINYINT(1) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_vote_uploadedfile_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_vote11_content (uid)
)TYPE=InnoDB;

INSERT INTO tl_vote11_content(uid, content_id , title , instructions , posting, creation_date , questions_sequenced , username_visible , created_by , monitoring_report_title , report_title , run_offline , define_later, offline_instructions, online_instructions, end_learning_message, content_in_use, retries,  show_report) VALUES (1, ${default_content_id} ,'Voting Title','Voting Instructions', 'sample posting', NOW(), 0, 0,1,'Voting Report','Report', 0, 0, 'offline instructions','online instructions','Finished Activity...', 0, 0, 0);

INSERT INTO tl_vote11_que_content  (uid,question,  display_order, vote_content_id) VALUES (1, 'sample nomination', 1, 1);