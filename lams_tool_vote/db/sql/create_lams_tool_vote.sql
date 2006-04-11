-- Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
-- =============================================================
-- License Information: http://lamsfoundation.org/licensing/lams/2.0/
-- 
-- This program is free software; you can redistribute it and/or modify
-- it under the terms of the GNU General Public License version 2.0 as 
-- published by the Free Software Foundation.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public License
-- along with this program; if not, write to the Free Software
-- Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
-- USA
-- 
-- http://www.gnu.org/licenses/gpl.txt
-- ****************************************************************
--
-- $Id$

CREATE TABLE tl_vote11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , content_id BIGINT(20) NOT NULL
     , title TEXT NOT NULL
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
     , show_feedback TINYINT(1) NOT NULL DEFAULT 0
     , show_report TINYINT(1) NOT NULL DEFAULT 0
     , UNIQUE UQ_tl_lamc11_content_1 (content_id)
     , PRIMARY KEY (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question VARCHAR(255)
     , disabled TINYINT(1) NOT NULL DEFAULT 1
     , vote_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (vote_content_id)
     , CONSTRAINT FK_tl_vote_que_content_1 FOREIGN KEY (vote_content_id)
                  REFERENCES tl_vote11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_vote11_options_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , vote_que_content_id BIGINT(20) NOT NULL
     , vote_que_option_text VARCHAR(250)
     , PRIMARY KEY (uid)
     , INDEX (vote_que_content_id)
     , CONSTRAINT FK_tl_vote_options_content_1 FOREIGN KEY (vote_que_content_id)
                  REFERENCES tl_vote11_que_content (uid)
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
     , INDEX (vote_que_option_id)
     , CONSTRAINT FK_tl_vote_usr_attempt_3 FOREIGN KEY (vote_que_option_id)
                  REFERENCES tl_vote11_options_content (uid)
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


INSERT INTO tl_vote11_content(uid, content_id , title , instructions , creation_date , questions_sequenced , username_visible , created_by , monitoring_report_title , report_title , run_offline , define_later, offline_instructions, online_instructions, end_learning_message, content_in_use, retries, show_feedback, show_report) VALUES (1, ${default_content_id} ,'Voting Title','Voting Instructions', NOW(), 0, 0,1,'Voting Report','Report', 0, 0, 'offline instructions','online instructions','Finished Activity...', 0, 0, 0, 0);

INSERT INTO tl_vote11_que_content  (uid,question,  disabled,  vote_content_id) VALUES (1, 'sample posting', 0, 1);

INSERT INTO tl_vote11_options_content (uid, vote_que_content_id, vote_que_option_text) VALUES (1, 1,'sample nomination');