
 
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tl_lavidr10_conditions;
drop table if exists tl_lavidr10_attachment;
drop table if exists tl_lavidr10_videorecorder;
drop table if exists tl_lavidr10_recording;
drop table if exists tl_lavidr10_session;
drop table if exists tl_lavidr10_user;
drop table if exists tl_lavidr10_comment;
drop table if exists tl_lavidr10_rating;

create table tl_lavidr10_attachment (
	uid bigint not null auto_increment,
	file_version_id bigint,
	file_type varchar(255),
	file_name varchar(255),
	file_uuid bigint,
	create_date datetime,
	videoRecorder_uid bigint,
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_videorecorder (
	uid bigint not null auto_increment,
	create_date datetime,
	update_date datetime,
	create_by bigint,
	title varchar(255),
	instructions text,
	reflect_on_activity bit,
	reflect_instructions text,
	run_offline bit,
	lock_on_finished bit,
	online_instructions text,
	offline_instructions text,
	content_in_use bit,
	define_later bit,
	tool_content_id bigint,
	author_recording_id bigint,
	allow_use_voice bit,
	allow_use_camera bit,
	allow_learner_video_visibility bit,
	allow_learner_video_export bit,
	allow_comments bit,
	allow_ratings bit,
	export_offline bit,
	export_all bit,
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_session (
	uid bigint not null auto_increment, 
	session_end_date datetime,
	session_start_date datetime,
	status integer, session_id bigint,
	session_name varchar(250),
	videoRecorder_uid bigint,
	content_folder_id varchar(32),
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_recording (
	uid bigint not null auto_increment,
	tool_content_id bigint,
	create_date datetime,
	update_date datetime,
	create_by bigint,
	title varchar(255),
	description varchar(1027),
	rating float,
	filename varchar(255),
	is_local bit,
	is_just_sound bit,
	videoRecorder_session_uid bigint,
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_user (
	uid bigint not null auto_increment,
	user_id bigint, last_name varchar(255),
	login_name varchar(255),
	first_name varchar(255),
	finishedActivity bit,
	videoRecorder_session_uid bigint,
	entry_uid bigint,
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_comment (
   uid bigint not null auto_increment,
   comment text,
   videoRecorder_recording_uid bigint,
   videoRecorder_session_uid bigint,
   create_by bigint,
   create_date datetime,
   primary key (uid)
)ENGINE=InnoDB;

create table tl_lavidr10_rating (
   uid bigint not null auto_increment,
   rating float,
   videoRecorder_recording_uid bigint,
   videoRecorder_session_uid bigint,
   create_by bigint,
   create_date datetime,
   primary key (uid)
)ENGINE=InnoDB;

alter table tl_lavidr10_attachment add index FK_NEW_75587508_12090F57FC940906 (videoRecorder_uid), add constraint FK_NEW_75587508_12090F57FC940906 foreign key (videoRecorder_uid) references tl_lavidr10_videorecorder (uid);
alter table tl_lavidr10_session add index FK_NEW_75587508_B7C198E2FC940906 (videoRecorder_uid), add constraint FK_NEW_75587508_B7C198E2FC940906 foreign key (videoRecorder_uid) references tl_lavidr10_videorecorder (uid);
alter table tl_lavidr10_user add index FK_NEW_75587508_CB8A58FFA3B0FADF (videoRecorder_session_uid), add constraint FK_NEW_75587508_CB8A58FFA3B0FADF foreign key (videoRecorder_session_uid) references tl_lavidr10_session (uid);

INSERT INTO tl_lavidr10_videorecorder (
	title,
	instructions,
	online_instructions,
	offline_instructions,
	reflect_on_activity, 
	reflect_instructions,
	tool_content_id,
	run_offline,
	lock_on_finished,
	content_in_use,
	define_later,
	allow_use_voice,
	allow_use_camera,
	allow_learner_video_visibility,
	allow_learner_video_export,
	allow_comments,
	allow_ratings,
	export_offline,
	export_all) 
VALUES(
	"VideoRecorder",
	"Instructions",
	"",
	"",
	0,
	"",
	${default_content_id},
	0,
	0,
	0,
	0,
	1,
	1,
	1,
	1,
	1,
	1,
	1,
	1
);

CREATE TABLE tl_lavidr10_conditions (
       condition_id BIGINT(20) NOT NULL
	 , content_uid BIGINT(20)
     , PRIMARY KEY (condition_id)
	 , CONSTRAINT VideoRecorderConditionInheritance FOREIGN KEY (condition_id)
                  REFERENCES lams_branch_condition(condition_id) ON DELETE CASCADE ON UPDATE CASCADE
	 , CONSTRAINT VideoRecorderConditionToVideoRecorder FOREIGN KEY (content_uid)
                  REFERENCES tl_lavidr10_videorecorder(uid) ON DELETE CASCADE ON UPDATE CASCADE
)ENGINE=InnoDB;

SET FOREIGN_KEY_CHECKS=1;
