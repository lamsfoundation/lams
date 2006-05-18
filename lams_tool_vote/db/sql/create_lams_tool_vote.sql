CREATE TABLE tl_lavote11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title TEXT NOT NULL
     , instructions TEXT NOT NULL
     , creation_date DATETIME
     , update_date DATETIME
     , maxNominationCount VARCHAR(20) NOT NULL DEFAULT '1'
     , allowText TINYINT(1) NOT NULL DEFAULT 0
     , voteChangable TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) NOT NULL DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions TEXT
     , online_instructions TEXT
     , content_in_use TINYINT(1) NOT NULL DEFAULT 0
     , lock_on_finish TINYINT(1) NOT NULL DEFAULT 0
     , retries TINYINT(1) NOT NULL DEFAULT 0
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lavote11_session (
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
     , CONSTRAINT FK_tl_lavote11_session_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_lavote11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lavote11_usr (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , user_id BIGINT(20) NOT NULL
     , vote_session_id BIGINT(20) NOT NULL
     , username VARCHAR(100)
     , fullname VARCHAR(20)
     , PRIMARY KEY (uid)
     , INDEX (vote_session_id)
     , CONSTRAINT FK_tl_lavote11_usr_1 FOREIGN KEY (vote_session_id)
                  REFERENCES tl_lavote11_session (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lavote11_nomination_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , nomination TEXT
     , display_order INT(5)
     , vote_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_lavote11_nomination_content_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_lavote11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lavote11_usr_attempt (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , que_usr_id BIGINT(20) NOT NULL
     , vote_nomination_content_id BIGINT(20) NOT NULL
     , attempt_time DATETIME
     , time_zone VARCHAR(255)
     , userEntry VARCHAR(255)
     , singleUserEntry TINYINT(1) NOT NULL DEFAULT 0
     , visible TINYINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (uid)
     , INDEX (que_usr_id)
     , CONSTRAINT FK_tl_lavote11_usr_attempt_2 FOREIGN KEY (que_usr_id)
                  REFERENCES tl_lavote11_usr (uid)
     , INDEX (vote_nomination_content_id)
     , CONSTRAINT FK_tl_lavote11_usr_attempt_3 FOREIGN KEY (vote_nomination_content_id)
                  REFERENCES tl_lavote11_nomination_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_lavote11_uploadedfile (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , uuid VARCHAR(255) NOT NULL
     , vote_content_id BIGINT(20) NOT NULL
     , isOnline_File TINYINT(1) NOT NULL
     , filename VARCHAR(255) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_lavote11_uploadedfile_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_lavote11_content (uid)
)TYPE=InnoDB;

INSERT INTO tl_lavote11_content(uid, content_id , title , instructions , creation_date , created_by , run_offline , define_later, offline_instructions, online_instructions, content_in_use, retries) VALUES (1, ${default_content_id} ,'Voting Title','Voting Instructions', NOW(), 1,0, 0, 'offline instructions','online instructions', 0, 0);

INSERT INTO tl_lavote11_nomination_content  (uid,nomination, display_order, vote_content_id) VALUES (1, 'sample nomination', 1, 1);