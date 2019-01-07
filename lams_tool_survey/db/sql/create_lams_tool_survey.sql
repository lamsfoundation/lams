SET FOREIGN_KEY_CHECKS=0;

create table tl_lasurv11_survey (
uid bigint not null auto_increment, 
title varchar(255), 
lock_on_finished TINYINT(1), 
instructions MEDIUMTEXT, 
submission_deadline datetime,
content_in_use TINYINT(1), 
define_later TINYINT(1), 
content_id bigint, 
reflect_instructions MEDIUMTEXT, 
reflect_on_activity TINYINT(1), 
show_questions_on_one_page TINYINT(1), 
create_date datetime, 
update_date datetime, 
create_by bigint,
answer_submit_notify TINYINT(1) DEFAULT 0,
show_other_users_answers tinyint(1) NOT NULL DEFAULT 0,
PRIMARY KEY (uid),
UNIQUE KEY content_id (content_id)
);

create table tl_lasurv11_session (
uid bigint not null auto_increment, 
session_end_date datetime, 
session_start_date datetime, 
survey_uid bigint, 
session_id bigint, 
session_name varchar(250), 
primary key (uid),
UNIQUE KEY session_id (session_id),
CONSTRAINT FKF08793B9D14146E5 FOREIGN KEY (survey_uid)
		REFERENCES tl_lasurv11_survey (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasurv11_user (
uid bigint not null auto_increment, 
user_id bigint, 
last_name varchar(255), 
first_name varchar(255), 
login_name varchar(255), 
session_uid bigint, 
survey_uid bigint, 
session_finished TINYINT(1), 
response_finalized tinyint(1) NOT NULL DEFAULT 0,
primary key (uid),
CONSTRAINT FK633F25884F803F63 FOREIGN KEY (session_uid)
		REFERENCES tl_lasurv11_session (uid) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT FK633F2588D14146E5 FOREIGN KEY (survey_uid)
		REFERENCES tl_lasurv11_survey (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasurv11_question (
uid bigint not null auto_increment, 
sequence_id integer,
description MEDIUMTEXT,
create_by bigint, 
create_date datetime, 
question_type smallint, 
append_text TINYINT(1), 
optional TINYINT(1), 
allow_multiple_answer TINYINT(1),
survey_uid bigint, 
primary key (uid),
CONSTRAINT FK872D4F23D14146E5 FOREIGN KEY (survey_uid)
		REFERENCES tl_lasurv11_survey (uid) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT FK872D4F23E4C99A5F FOREIGN KEY (create_by)
		REFERENCES tl_lasurv11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE
);

create table tl_lasurv11_answer (
uid bigint not null auto_increment, 
question_uid bigint, 
user_uid bigint, 
answer_choices varchar(255), 
udpate_date datetime, 
answer_text MEDIUMTEXT, 
primary key (uid),
CONSTRAINT FK6DAAFE3B25F3BB77 FOREIGN KEY (question_uid)
		REFERENCES tl_lasurv11_question (uid) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT FK6DAAFE3BB1423DC1 FOREIGN KEY (user_uid)
		REFERENCES tl_lasurv11_user (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

create table tl_lasurv11_option (
uid bigint not null auto_increment, 
description MEDIUMTEXT,
sequence_id integer, 
question_uid bigint, 
primary key (uid),
CONSTRAINT FK85AB46F26966134F FOREIGN KEY (question_uid)
		REFERENCES tl_lasurv11_question (uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lasurv11_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT SurveyConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT SurveyConditionToSurvey FOREIGN KEY (content_uid)
                  REFERENCES tl_lasurv11_survey(uid) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE tl_lasurv11_condition_questions (
 	   condition_id BIGINT(20)
 	 , question_uid BIGINT(20)
 	 , PRIMARY KEY (condition_id,question_uid)
	 , CONSTRAINT SurveyConditionQuestionToSurveyCondition FOREIGN KEY (condition_id)
                  REFERENCES tl_lasurv11_conditions(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT SurveyConditionQuestionToSurveyQuestion FOREIGN KEY (question_uid)
                  REFERENCES tl_lasurv11_question(uid) ON DELETE CASCADE ON UPDATE CASCADE	
);


ALTER TABLE tl_lasurv11_survey ADD CONSTRAINT FK8CC465D7E4C99A5F FOREIGN KEY (create_by)
		REFERENCES tl_lasurv11_user (uid) ON DELETE SET NULL ON UPDATE CASCADE;
		
		
INSERT INTO tl_lasurv11_survey (uid, title, 
  lock_on_finished, instructions, 
 content_in_use, define_later, content_id,show_questions_on_one_page,reflect_on_activity) VALUES
  (1,'Survey','1','Instructions',0,0,${default_content_id},1,0);
  
INSERT INTO tl_lasurv11_question (uid, sequence_id, description,create_date, question_type, append_text, optional, allow_multiple_answer, survey_uid) VALUES 
  (1,1,'Sample Multiple choice - only one response allowed?',NOW(),1,0,0,0,1);
INSERT INTO tl_lasurv11_question (uid, sequence_id, description,create_date, question_type, append_text, optional, allow_multiple_answer, survey_uid) VALUES   
  (2,2,'Sample Multiple choice - multiple response allowed?',NOW(),2,0,0,1,1);
INSERT INTO tl_lasurv11_question (uid, sequence_id, description, create_date, question_type, append_text, optional, allow_multiple_answer, survey_uid) VALUES   
  (3,3,'Sample Free text question?',NOW(),3,0,0,0,1);
  

INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES 
  (1,'Option 1',0,1);
INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES   
  (2,'Option 2',1,1);
INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES 
  (3,'Option 3',2,1);
INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES 
  (4,'Option 1',0,2);
INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES 
  (5,'Option 2',1,2);
INSERT INTO tl_lasurv11_option (uid, description, sequence_id, question_uid) VALUES 
  (6,'Option 3',2,2);  
  
SET FOREIGN_KEY_CHECKS=1;