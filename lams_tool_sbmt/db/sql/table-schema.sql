alter table tl_lasbmt11_instruction_files drop foreign key FKA75538F9785A173A;
alter table tl_lasbmt11_session drop foreign key FKEC8C77C9785A173A;
alter table tl_lasbmt11_session_learners drop foreign key FKC56CD05893C861A;
alter table tl_lasbmt11_submission_details drop foreign key FK1411A53C93C861A;
alter table tl_lasbmt11_submission_details drop foreign key FK1411A53C10BBAB1B;
drop table if exists tl_lasbmt11_content;
drop table if exists tl_lasbmt11_instruction_files;
drop table if exists tl_lasbmt11_report;
drop table if exists tl_lasbmt11_session;
drop table if exists tl_lasbmt11_session_learners;
drop table if exists tl_lasbmt11_submission_details;
create table tl_lasbmt11_content (
   content_id bigint not null,
   title varchar(64) not null,
   instruction text,
   define_later bit not null,
   run_offline bit not null,
   offline_instruction text,
   online_instruction text,
   run_offline_instruction text,
   content_in_use bit,
   lock_on_finished bit,
   primary key (content_id)
);
create table tl_lasbmt11_instruction_files (
   uid bigint not null auto_increment,
   uuid bigint,
   version_id bigint,
   type varchar(20),
   name varchar(255),
   content_id bigint,
   primary key (uid)
);
create table tl_lasbmt11_report (
   report_id bigint not null auto_increment,
   comments varchar(250),
   marks bigint,
   date_marks_released datetime,
   primary key (report_id)
);
create table tl_lasbmt11_session (
   session_id bigint not null,
   status integer not null,
   content_id bigint,
   session_name varchar(250),
   primary key (session_id)
);
create table tl_lasbmt11_session_learners (
   learner_id bigint not null auto_increment,
   user_id bigint,
   finished bit,
   session_id bigint,
   primary key (learner_id)
);
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
);
alter table tl_lasbmt11_instruction_files add index FKA75538F9785A173A (content_id), add constraint FKA75538F9785A173A foreign key (content_id) references tl_lasbmt11_content (content_id);
alter table tl_lasbmt11_session add index FKEC8C77C9785A173A (content_id), add constraint FKEC8C77C9785A173A foreign key (content_id) references tl_lasbmt11_content (content_id);
alter table tl_lasbmt11_session_learners add index FKC56CD05893C861A (session_id), add constraint FKC56CD05893C861A foreign key (session_id) references tl_lasbmt11_session (session_id);
alter table tl_lasbmt11_submission_details add index FK1411A53C93C861A (session_id), add constraint FK1411A53C93C861A foreign key (session_id) references tl_lasbmt11_session (session_id);
alter table tl_lasbmt11_submission_details add index FK1411A53C10BBAB1B (learner_id), add constraint FK1411A53C10BBAB1B foreign key (learner_id) references tl_lasbmt11_session_learners (learner_id);
