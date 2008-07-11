SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lasprd10_attachment;
drop table if exists tl_lasprd10_spreadsheet;
drop table if exists tl_lasprd10_user_modified_spreadsheet;
drop table if exists tl_lasprd10_session;
drop table if exists tl_lasprd10_user;
create table tl_lasprd10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   spreadsheet_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_lasprd10_spreadsheet (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   is_learner_allowed_to_save tinyint,
   is_marking_enabled tinyint,
   lock_on_finished tinyint,
   instructions text,
   code text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint, 
   primary key (uid)
)type=innodb;
create table tl_lasprd10_user_modified_spreadsheet (
   uid bigint not null auto_increment,
   user_modified_spreadsheet text,
   mark_id bigint,
   primary key (uid)
)type=innodb;
create table tl_lasprd10_spreadsheet_mark (
   uid bigint not null auto_increment,
   marks varchar(255),
   comments text,
   date_marks_released datetime,
   primary key (uid)
)type=innodb;
create table tl_lasprd10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   spreadsheet_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)type=innodb;
create table tl_lasprd10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   spreadsheet_uid bigint,
   user_modified_spreadsheet_uid bigint,
   primary key (uid)
)type=innodb;
alter table tl_lasprd10_attachment add index FK_NEW_2065267438_1E7009430E79035 (spreadsheet_uid), add constraint FK_NEW_2065267438_1E7009430E79035 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_spreadsheet add index FK_NEW_2065267438_89093BF758092FB (create_by), add constraint FK_NEW_2065267438_89093BF758092FB foreign key (create_by) references tl_lasprd10_user (uid);
alter table tl_lasprd10_session add index FK_NEW_2065267438_24AA78C530E79035 (spreadsheet_uid), add constraint FK_NEW_2065267438_24AA78C530E79035 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_user add index FK_NEW_2065267438_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_2065267438_30113BFCEC0D3147 foreign key (session_uid) references tl_lasprd10_session (uid);
alter table tl_lasprd10_user add index FK_NEW_2065267438_30113BFC309ED320 (spreadsheet_uid), add constraint FK_NEW_2065267438_30113BFC309ED320 foreign key (spreadsheet_uid) references tl_lasprd10_spreadsheet (uid);
alter table tl_lasprd10_user add index FK_NEW_2065267438_693580A441F9365D (user_modified_spreadsheet_uid), add constraint FK_NEW_2065267438_693580A441F9365D foreign key (user_modified_spreadsheet_uid) references tl_lasprd10_user_modified_spreadsheet (uid);
alter table tl_lasprd10_user_modified_spreadsheet add index FK_tl_lasprd10_user_modified_spreadsheet_1 (mark_id), add constraint FK_tl_lasprd10_user_modified_spreadsheet_1 foreign key (mark_id) references tl_lasprd10_spreadsheet_mark (uid);



INSERT INTO `tl_lasprd10_spreadsheet` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `is_learner_allowed_to_save`, 
  `is_marking_enabled`, `lock_on_finished`, `instructions`, `code`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, 
  `content_id`, `reflect_on_activity`) VALUES
  (1,NULL,NULL,NULL,'Spreadsheet','0','1','0','0','Instructions ','',null,null,0,0,${default_content_id},0);
  
SET FOREIGN_KEY_CHECKS=1;
