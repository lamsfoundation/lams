SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_eueadv10_attachment;
drop table if exists tl_eueadv10_eadventure;
drop table if exists tl_eueadv10_eadventure_item_visit_log;
drop table if exists tl_eueadv10_session;
drop table if exists tl_eueadv10_user;
drop table if exists tl_eueadv10_var;
drop table if exists tl_eueadv10_param;
drop table if exists tl_eueadv10_condition;
drop table if exists tl_eueadv10_condition_expression;
create table tl_eueadv10_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   eadventure_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_eadventure (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   lock_on_finished tinyint,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint,
   ims_schema varchar(255),
   organization_xml text,
   init_item varchar(255),
   file_uuid bigint,
   file_version_id bigint,
   file_name varchar(255),
   file_type varchar(255),
   complete tinyint,
   define_complete tinyint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   eadventure_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)type=innodb;
create table tl_eueadv10_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   session_uid bigint,
   eadventure_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_item_log (
   uid bigint not null auto_increment,
   access_date datetime,
   eadventure_item_uid bigint,
   user_uid bigint,
   complete tinyint,
   session_id bigint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_var (
   uid bigint not null auto_increment,
   name varchar(255),
   type varchar(255),
   value text,
   visit_log_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_param (
   uid bigint not null auto_increment,
   name varchar(255),
   type varchar(255),
   input tinyint,
   eadventure_uid bigint,
   primary key (uid)
)type=innodb;
create table tl_eueadv10_condition (
   uid bigint not null auto_increment,
   sequence_id integer,
   eadventure_uid bigint,
   name varchar(255),
   primary key (uid)
)type=innodb;
create table tl_eueadv10_condition_expression (
	uid bigint not null auto_increment,
	condition_uid bigint,
	first_op bigint,
	value_introduced varchar(255),
	var_introduced bigint,
	expresion_op varchar(255),
	sequence_id integer,
	next_op varchar(255),
	primary key (uid)	
)type=innodb;
alter table tl_eueadv10_attachment add index FK_NEW_1226715514_1E7009430E79035 (eadventure_uid), add constraint FK_NEW_1226715514_1E7009430E79035 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_eadventure add index FK_NEW_1226715514_89093BF758092FB (create_by), add constraint FK_NEW_1226715514_89093BF758092FB foreign key (create_by) references tl_eueadv10_user (uid);
alter table tl_eueadv10_item_log add index FK_NEW_1226715514_693580A438BF8DFE (eadventure_item_uid), add constraint FK_NEW_1226715514_693580A438BF8DFE foreign key (eadventure_item_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_item_log add index FK_NEW_1226715514_693580A441F9365D (user_uid), add constraint FK_NEW_1226715514_693580A441F9365D foreign key (user_uid) references tl_eueadv10_user (uid);
alter table tl_eueadv10_session add index FK_NEW_1226715514_24AA78C530E79035 (eadventure_uid), add constraint FK_NEW_1226715514_24AA78C530E79035 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_user add index FK_NEW_1226715514_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_1226715514_30113BFCEC0D3147 foreign key (session_uid) references tl_eueadv10_session (uid);
alter table tl_eueadv10_user add index FK_NEW_1226715514_30113BFC309ED320 (eadventure_uid), add constraint FK_NEW_1226715514_30113BFC309ED320 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_var add index FK_NEW_1226715514_42637763767637E6(visit_log_uid), add constraint FK_NEW_1226715514_42637763767637E6 foreign key (visit_log_uid) references tl_eueadv10_item_log (uid);
alter table tl_eueadv10_param add index FK_NEW_1226715514_56566830263527E3(eadventure_uid), add constraint FK_NEW_1226715514_56566830263527E3 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_condition add index FK_NEW_1226715514_5656683026352999 (eadventure_uid), add constraint FK_NEW_1226715514_5656683026352999 foreign key (eadventure_uid) references tl_eueadv10_eadventure (uid);
alter table tl_eueadv10_condition_expression add index FK_NEW_1226715514_5656683026352390 (condition_uid), add constraint FK_NEW_1226715514_5656683026352390 foreign key (condition_uid) references tl_eueadv10_condition (uid);
alter table tl_eueadv10_condition_expression add index FK_NEW_1226715514_5656683026352EEE (first_op), add constraint FK_NEW_1226715514_5656683026352EEE foreign key (first_op) references tl_eueadv10_param (uid);
alter table tl_eueadv10_condition_expression add index FK_NEW_1226715514_5656683026352FFF (var_introduced), add constraint FK_NEW_1226715514_5656683026352FFF foreign key (var_introduced) references tl_eueadv10_param (uid);

INSERT INTO `tl_eueadv10_eadventure` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`, `lock_on_finished`,
 `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`,`reflect_on_activity`, `file_uuid`, `file_version_id` , `file_name`,`file_type`,`complete`,`define_complete`) VALUES
  (1,NULL,NULL,NULL,'eAdventure','0',0,'Instructions ',null,null,0,0,${default_content_id},0, NULL, NULL, NULL,NULL ,0,0);
  
SET FOREIGN_KEY_CHECKS=1;
