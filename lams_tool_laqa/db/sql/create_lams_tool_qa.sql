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
     , lockWhenFinished TINYINT(1) NOT NULL DEFAULT 1
     , showOtherAnswers TINYINT(1) NOT NULL DEFAULT 1
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
     , username VARCHAR(255)
     , responseFinalized TINYINT(1) NOT NULL DEFAULT 0
     , qa_session_id BIGINT(20) NOT NULL
     , fullname VARCHAR(255)
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
     , qa_content_id BIGINT(20)
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

CREATE TABLE tl_laqa11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT QaConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionToQaContent FOREIGN KEY (content_uid)
                  REFERENCES tl_laqa11_content(uid) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE tl_laqa11_condition_questions (
 	   condition_id BIGINT(20)
 	 , question_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,question_uid)
	 , CONSTRAINT QaConditionQuestionToQaCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_laqa11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT QaConditionQuestionToQaQuestion FOREIGN KEY (question_uid)
                  REFERENCES tl_laqa11_que_content(uid) ON DELETE CASCADE ON UPDATE CASCADE	
)TYPE=InnoDB;

-- data for content table
INSERT INTO tl_laqa11_content (qa_content_id, title, instructions, creation_date, lockWhenFinished)  VALUES (${default_content_id}, 'Q&A', 'Instructions', NOW() , 0);

-- data for content questions table
INSERT INTO tl_laqa11_que_content (question, display_order, qa_content_id) VALUES ('Sample Question 1?',1,1);

SET FOREIGN_KEY_CHECKS=0;
-- insert scripts for the wizard tables
drop table if exists tl_laqa11_configuration;
drop table if exists tl_laqa11_wizard_category;
drop table if exists tl_laqa11_wizard_cognitive_skill;
drop table if exists tl_laqa11_wizard_question;
create table tl_laqa11_configuration (uid bigint not null auto_increment, config_key varchar(30) unique, config_value varchar(255), primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_category (uid bigint not null auto_increment, title varchar(255) not null, primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_cognitive_skill (uid bigint not null auto_increment, title varchar(255) not null, category_uid bigint, primary key (uid))TYPE=InnoDB;
create table tl_laqa11_wizard_question (uid bigint not null auto_increment, cognitive_skill_uid bigint, title text not null, primary key (uid))TYPE=InnoDB;
alter table tl_laqa11_wizard_cognitive_skill add index FK3BA4132BCBB0DC8D (category_uid), add constraint FK3BA4132BCBB0DC8D foreign key (category_uid) references tl_laqa11_wizard_category (uid);
alter table tl_laqa11_wizard_question add index FKAF08A0C7EFF77FD4 (cognitive_skill_uid), add constraint FKAF08A0C7EFF77FD4 foreign key (cognitive_skill_uid) references tl_laqa11_wizard_cognitive_skill (uid);

-- Inserting the required config item into the config table
INSERT INTO tl_laqa11_configuration (
	config_key, 
	config_value
)
VALUES(
	"enableQaWizard",
	"false"
);
SET FOREIGN_KEY_CHECKS=1;

