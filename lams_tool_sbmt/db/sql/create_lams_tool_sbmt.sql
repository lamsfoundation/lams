

SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lasbmt11_content;
drop table if exists tl_lasbmt11_instruction_files;
drop table if exists tl_lasbmt11_report;
drop table if exists tl_lasbmt11_session;
drop table if exists tl_lasbmt11_user;
drop table if exists tl_lasbmt11_submission_details;
create table tl_lasbmt11_content (
   content_id bigint not null,
   title varchar(64) not null,
   instruction text,
   define_later smallint not null,
   run_offline smallint not null,
   offline_instruction text,
   online_instruction text,
   content_in_use smallint,
   lock_on_finished smallint,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint, 
   limit_upload smallint, 
   limit_upload_number integer,
   created datetime, 
   created_by bigint, 
   updated datetime,
   submission_deadline datetime,
   mark_release_notify tinyint DEFAULT 0,
   file_submit_notify tinyint DEFAULT 0,
   primary key (content_id)
)ENGINE=InnoDB;
create table tl_lasbmt11_user (
	uid bigint not null auto_increment, 
	user_id integer, 
	finished bit, 
	session_id bigint, 
	first_name varchar(255), 
	login_name varchar(255), 
	last_name varchar(255), 
	content_id bigint, 
	primary key (uid)
)ENGINE=InnoDB;

create table tl_lasbmt11_instruction_files (
   uid bigint not null auto_increment,
   uuid bigint,
   version_id bigint,
   type varchar(20),
   name varchar(255),
   content_id bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lasbmt11_report (
   report_id bigint not null auto_increment,
   comments text,
   marks float,
   date_marks_released datetime,
   mark_file_uuid bigint,
   mark_file_version_id bigint,
   mark_file_name varchar(255),
   primary key (report_id)
)ENGINE=InnoDB;
create table tl_lasbmt11_session (
   session_id bigint not null,
   status integer not null,
   content_id bigint,
   session_name varchar(250),
   primary key (session_id)
)ENGINE=InnoDB;
create table tl_lasbmt11_submission_details (
   submission_id bigint not null auto_increment,
   filePath varchar(250),
   fileDescription varchar(250),
   date_of_submission datetime,
   uuid bigint,
   version_id bigint,
   session_id bigint,
   learner_id bigint,
   primary key (submission_id)
)ENGINE=InnoDB;

alter table tl_lasbmt11_content add index FKAEF329AC172BC670 (created_by), add constraint FKAEF329AC172BC670 foreign key (created_by) references tl_lasbmt11_user (uid);
alter table tl_lasbmt11_instruction_files add index FKA75538F9785A173A (content_id), add constraint FKA75538F9785A173A foreign key (content_id) references tl_lasbmt11_content (content_id);
alter table tl_lasbmt11_session add index FKEC8C77C9785A173A (content_id), add constraint FKEC8C77C9785A173A foreign key (content_id) references tl_lasbmt11_content (content_id);
alter table tl_lasbmt11_submission_details add index FK1411A53CFFD5A38B (learner_id), add constraint FK1411A53CFFD5A38B foreign key (learner_id) references tl_lasbmt11_user (uid);
alter table tl_lasbmt11_submission_details add index FK1411A53C93C861A (session_id), add constraint FK1411A53C93C861A foreign key (session_id) references tl_lasbmt11_session (session_id);


INSERT INTO `tl_lasbmt11_content` (content_id,title,instruction,define_later,run_offline,content_in_use,lock_on_finished,reflect_on_activity,limit_upload,limit_upload_number) 
values(${default_content_id},"Submit Files","Instructions",0,0,0,0,0,0,1);
SET FOREIGN_KEY_CHECKS=1;