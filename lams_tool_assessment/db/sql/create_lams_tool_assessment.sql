SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_laasse10_attachment;
drop table if exists tl_laasse10_assessment;
drop table if exists tl_laasse10_assessment_question;
drop table if exists tl_laasse10_question_option;
drop table if exists tl_laasse10_assessment_overall_feedback;
drop table if exists tl_laasse10_assessment_unit;
drop table if exists tl_laasse10_assessment_result;
drop table if exists tl_laasse10_question_result;
drop table if exists tl_laasse10_option_answer;
drop table if exists tl_laasse10_session;
drop table if exists tl_laasse10_user;
create table tl_laasse10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   assessment_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_laasse10_assessment (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   submission_deadline datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   time_limit integer DEFAULT 0,
   attempts_allowed integer DEFAULT 1,
   passing_mark integer DEFAULT 0,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   allow_question_feedback tinyint,
   allow_overall_feedback tinyint,
   allow_right_answers tinyint,
   allow_wrong_answers tinyint,
   allow_grades_after_attempt tinyint,
   allow_history_responses tinyint,
   display_summary tinyint,
   questions_per_page integer DEFAULT 0,
   shuffled tinyint,
   attempt_completion_notify tinyint DEFAULT 0,
   primary key (uid)
)type=innodb;
create table tl_laasse10_assessment_question (
   uid bigint not null auto_increment,
   question_type smallint,
   title varchar(255),
   question text,
   sequence_id integer,
   default_grade integer DEFAULT 1,
   penalty_factor float DEFAULT 0,
   general_feedback text,
   feedback text,
   multiple_answers_allowed tinyint DEFAULT 0,
   feedback_on_correct text,
   feedback_on_partially_correct text,
   feedback_on_incorrect text,
   shuffle tinyint,
   case_sensitive tinyint,
   correct_answer tinyint DEFAULT 0,
   allow_rich_editor tinyint DEFAULT 0,
   create_date datetime,
   create_by bigint,
   assessment_uid bigint,
   session_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_laasse10_question_reference (
   uid bigint not null auto_increment,
   question_uid bigint,
   question_type smallint,
   title varchar(255),
   sequence_id integer,
   default_grade integer DEFAULT 1,
   random_question tinyint DEFAULT 0,
   assessment_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_laasse10_question_option (
   uid bigint not null unique auto_increment,
   question_uid bigint,
   sequence_id integer,
   question text,
   option_string text,
   option_float float,
   accepted_error float,
   grade float,
   feedback text,
   primary key (uid)
)type=innodb;
create table tl_laasse10_assessment_overall_feedback (
   uid bigint not null unique auto_increment,
   assessment_uid bigint,
   sequence_id integer,
   grade_boundary integer,
   feedback text,
   primary key (uid)
)type=innodb;
create table tl_laasse10_assessment_unit (
   uid bigint not null unique auto_increment,
   question_uid bigint,
   sequence_id integer,
   multiplier float,
   unit varchar(255),
   primary key (uid)
)type=innodb;
create table tl_laasse10_assessment_result (
   uid bigint not null auto_increment,
   assessment_uid bigint,
   start_date datetime,
   finish_date datetime,
   user_uid bigint,
   session_id bigint,
   maximum_grade integer,
   grade float,
   primary key (uid)
)type=innodb;
create table tl_laasse10_question_result (
   uid bigint not null auto_increment,
   assessment_question_uid bigint,
   result_uid bigint,
   answer_string text,
   answer_float float,
   answer_boolean boolean,
   submitted_option_uid bigint,
   mark float,
   penalty float,
   finish_date datetime DEFAULT NULL,
   primary key (uid)
)type=innodb;
create table tl_laasse10_option_answer (
   uid bigint not null unique auto_increment,
   question_result_uid bigint,
   question_option_uid bigint,
   answer_boolean boolean,
   answer_int integer,
   primary key (uid)
)type=innodb;
create table tl_laasse10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   assessment_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)type=innodb;
create table tl_laasse10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   assessment_uid bigint,
   primary key (uid)
)type=innodb;
alter table tl_laasse10_attachment add index FK_NEW_1720029621_1E7009430E79035 (assessment_uid), add constraint FK_NEW_1720029621_1E7009430E79035 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_assessment add index FK_NEW_1720029621_89093BF758092FB (create_by), add constraint FK_NEW_1720029621_89093BF758092FB foreign key (create_by) references tl_laasse10_user (uid);
alter table tl_laasse10_assessment_question add index FK_NEW_1720029621_F52D1F93758092FB (create_by), add constraint FK_NEW_1720029621_F52D1F93758092FB foreign key (create_by) references tl_laasse10_user (uid);
alter table tl_laasse10_assessment_question add index FK_NEW_1720029621_F52D1F9330E79035 (assessment_uid), add constraint FK_NEW_1720029621_F52D1F9330E79035 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_assessment_question add index FK_NEW_1720029621_F52D1F93EC0D3147 (session_uid), add constraint FK_NEW_1720029621_F52D1F93EC0D3147 foreign key (session_uid) references tl_laasse10_session (uid);
alter table tl_laasse10_question_reference add index FK_tl_laasse10_question_reference_1 (question_uid), add constraint FK_tl_laasse10_question_reference_1 foreign key (question_uid) references tl_laasse10_assessment_question (uid);
alter table tl_laasse10_question_reference add index FK_tl_laasse10_question_reference_2 (assessment_uid), add constraint FK_tl_laasse10_question_reference_2 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_question_option add index FK_tl_laasse10_question_option_1 (question_uid), add constraint FK_tl_laasse10_question_option_1 foreign key (question_uid) references tl_laasse10_assessment_question (uid);
alter table tl_laasse10_assessment_overall_feedback add index FK_tl_laasse10_assessment_overall_feedback_1 (assessment_uid), add constraint FK_tl_laasse10_assessment_overall_feedback_1 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_assessment_unit add index FK_tl_laasse10_assessment_unit_1 (question_uid), add constraint FK_tl_laasse10_assessment_unit_1 foreign key (question_uid) references tl_laasse10_assessment_question (uid);
alter table tl_laasse10_assessment_result add index FK_tl_laasse10_assessment_result_2 (user_uid), add constraint FK_tl_laasse10_assessment_result_2 foreign key (user_uid) references tl_laasse10_user (uid);
alter table tl_laasse10_assessment_result add index FK_tl_laasse10_assessment_result_3 (assessment_uid), add constraint FK_tl_laasse10_assessment_result_3 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_question_result add index FK_NEW_1720029621_693580A438BF8DFE (assessment_question_uid), add constraint FK_NEW_1720029621_693580A438BF8DFE foreign key (assessment_question_uid) references tl_laasse10_assessment_question (uid);
alter table tl_laasse10_question_result add index FK_tl_laasse10_question_result_1 (result_uid), add constraint FK_tl_laasse10_question_result_1 foreign key (result_uid) references tl_laasse10_assessment_result (uid);
alter table tl_laasse10_option_answer add index FK_tl_laasse10_option_answer_1 (question_result_uid), add constraint FK_tl_laasse10_option_answer_1 foreign key (question_result_uid) references tl_laasse10_question_result (uid);
alter table tl_laasse10_session add index FK_NEW_1720029621_24AA78C530E79035 (assessment_uid), add constraint FK_NEW_1720029621_24AA78C530E79035 foreign key (assessment_uid) references tl_laasse10_assessment (uid);
alter table tl_laasse10_user add index FK_NEW_1720029621_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1720029621_30113BFCEC0D3147 foreign key (session_uid) references tl_laasse10_session (uid);
alter table tl_laasse10_user add index FK_NEW_1720029621_30113BFC309ED320 (assessment_uid), add constraint FK_NEW_1720029621_30113BFC309ED320 foreign key (assessment_uid) references tl_laasse10_assessment (uid);

INSERT INTO `tl_laasse10_assessment` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, 
 `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`, `allow_question_feedback`, 
 `allow_overall_feedback`, `allow_right_answers`, `allow_wrong_answers`, `allow_grades_after_attempt`, `allow_history_responses`, `display_summary`, `shuffled`) VALUES
  (1,NULL,NULL,NULL,'Assessment','0','Instructions ',null,null,0,0,${default_content_id},0,0,0,0,0,0,0,0);

SET FOREIGN_KEY_CHECKS=1;
