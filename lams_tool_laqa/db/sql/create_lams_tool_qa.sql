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
-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301-- USA
--
-- http://www.gnu.org/licenses/gpl.txt
-- ****************************************************************
--
-- $Id$

CREATE TABLE tl_laqa11_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , qa_content_id BIGINT(20) NOT NULL
     , title TEXT
     , instructions TEXT
     , creation_date DATETIME
     , update_date DATETIME
     , reflect TINYINT(1) NOT NULL DEFAULT 0
     , questions_sequenced TINYINT(1) NOT NULL DEFAULT 0
     , username_visible TINYINT(1) NOT NULL DEFAULT 0
     , created_by BIGINT(20) NOT NULL DEFAULT 0
     , run_offline TINYINT(1) DEFAULT 0
     , define_later TINYINT(1) NOT NULL DEFAULT 0
     , synch_in_monitor TINYINT(1) NOT NULL DEFAULT 0
     , offline_instructions TEXT
     , online_instructions TEXT
     , content_inUse TINYINT(1) DEFAULT 0
     , reflectionSubject TEXT
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
     , responseFinalized TINYINT(1) NOT NULL DEFAULT 0
     , qa_session_id BIGINT(20) NOT NULL
     , fullname VARCHAR(100)
     , learnerFinished TINYINT(1) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (qa_session_id)
     , CONSTRAINT FK_tl_laqa11_que_usr_1 FOREIGN KEY (qa_session_id)
                  REFERENCES tl_laqa11_session (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_que_content (
       uid BIGINT(20) NOT NULL AUTO_INCREMENT
     , question TEXT
     , feedback TEXT
     , display_order INT(5) DEFAULT 1
     , qa_content_id BIGINT(20) NOT NULL
     , PRIMARY KEY (uid)
     , INDEX (qa_content_id)
     , CONSTRAINT FK_tl_laqa11_que_content_1 FOREIGN KEY (qa_content_id)
                  REFERENCES tl_laqa11_content (uid)
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_usr_resp (
       response_id BIGINT(20) NOT NULL AUTO_INCREMENT
     , hidden TINYINT(1) DEFAULT 0
     , answer TEXT
     , time_zone VARCHAR(255)
     , attempt_time DATETIME
     , que_usr_id BIGINT(20) NOT NULL
     , qa_que_content_id BIGINT(20) NOT NULL
     , visible TINYINT(1) NOT NULL DEFAULT 1
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
INSERT INTO tl_laqa11_content (qa_content_id, title, instructions, creation_date)  VALUES (${default_content_id}, 'Q&A Title', 'Q&A Instructions', NOW());

-- data for content questions table
INSERT INTO tl_laqa11_que_content (question, display_order, qa_content_id) VALUES ('Sample Question 1?',1,1);





