SET FOREIGN_KEY_CHECKS=0;
drop table if exists tl_lascrt11_attachment;
drop table if exists tl_lascrt11_scratchie;
drop table if exists tl_lascrt11_scratchie_item;
drop table if exists tl_lascrt11_scratchie_answer;
drop table if exists tl_lascrt11_answer_log;
drop table if exists tl_lascrt11_session;
drop table if exists tl_lascrt11_user;
create table tl_lascrt11_attachment (
   uid bigint not null auto_increment,
   file_version_id bigint,
   file_type varchar(255),
   file_name varchar(255),
   file_uuid bigint,
   create_date datetime,
   scratchie_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_scratchie (
   uid bigint not null auto_increment,
   create_date datetime,
   update_date datetime,
   create_by bigint,
   title varchar(255),
   run_offline tinyint,
   instructions text,
   online_instructions text,
   offline_instructions text,
   content_in_use tinyint,
   define_later tinyint,
   content_id bigint unique,
   reflect_instructions varchar(255), 
   reflect_on_activity smallint,
   extra_point smallint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_scratchie_item (
   uid bigint not null auto_increment,
   title varchar(255),
   description text,
   create_by bigint,
   create_date datetime,
   create_by_author tinyint,
   scratchie_uid bigint,
   session_uid bigint,
   order_id integer,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_scratchie_answer (
   uid bigint not null auto_increment,
   description text,
   correct tinyint,
   scratchie_item_uid bigint,
   order_id integer,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_answer_log (
   uid bigint not null auto_increment,
   access_date datetime,
   scratchie_answer_uid bigint,
   user_uid bigint,
   session_id bigint,
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_session (
   uid bigint not null auto_increment,
   session_end_date datetime,
   session_start_date datetime,
   status integer,
   scratchie_uid bigint,
   session_id bigint,
   session_name varchar(250),
   primary key (uid)
)ENGINE=InnoDB;
create table tl_lascrt11_user (
   uid bigint not null auto_increment,
   user_id bigint,
   last_name varchar(255),
   first_name varchar(255),
   login_name varchar(255),
   session_finished smallint,
   scratching_finished smallint,
   session_uid bigint,
   scratchie_uid bigint,
   primary key (uid)
)ENGINE=InnoDB;
alter table tl_lascrt11_attachment add index FK_NEW_610529188_1E7009430E79035 (scratchie_uid), add constraint FK_NEW_610529188_1E7009430E79035 foreign key (scratchie_uid) references tl_lascrt11_scratchie (uid);
alter table tl_lascrt11_scratchie add index FK_NEW_610529188_89093BF758092FB (create_by), add constraint FK_NEW_610529188_89093BF758092FB foreign key (create_by) references tl_lascrt11_user (uid);
alter table tl_lascrt11_scratchie_item add index FK_NEW_610529188_F52D1F93758092FB (create_by), add constraint FK_NEW_610529188_F52D1F93758092FB foreign key (create_by) references tl_lascrt11_user (uid);
alter table tl_lascrt11_scratchie_item add index FK_NEW_610529188_F52D1F9330E79035 (scratchie_uid), add constraint FK_NEW_610529188_F52D1F9330E79035 foreign key (scratchie_uid) references tl_lascrt11_scratchie (uid);
alter table tl_lascrt11_scratchie_item add index FK_NEW_610529188_F52D1F93EC0D3147 (session_uid), add constraint FK_NEW_610529188_F52D1F93EC0D3147 foreign key (session_uid) references tl_lascrt11_session (uid);
alter table tl_lascrt11_answer_log add index FK_NEW_610529188_693580A438BF8DFE (scratchie_answer_uid), add constraint FK_NEW_610529188_693580A438BF8DFE foreign key (scratchie_answer_uid) references tl_lascrt11_scratchie_answer (uid);
alter table tl_lascrt11_answer_log add index FK_NEW_610529188_693580A441F9365D (user_uid), add constraint FK_NEW_610529188_693580A441F9365D foreign key (user_uid) references tl_lascrt11_user (uid);
alter table tl_lascrt11_session add index FK_NEW_610529188_24AA78C530E79035 (scratchie_uid), add constraint FK_NEW_610529188_24AA78C530E79035 foreign key (scratchie_uid) references tl_lascrt11_scratchie (uid);
alter table tl_lascrt11_user add index FK_NEW_610529188_30113BFCEC0D3147 (session_uid), add constraint FK_NEW_610529188_30113BFCEC0D3147 foreign key (session_uid) references tl_lascrt11_session (uid);
alter table tl_lascrt11_user add index FK_NEW_610529188_30113BFC309ED320 (scratchie_uid), add constraint FK_NEW_610529188_30113BFC309ED320 foreign key (scratchie_uid) references tl_lascrt11_scratchie (uid);



INSERT INTO `tl_lascrt11_scratchie` (`uid`, `create_date`, `update_date`, `create_by`, `title`, `run_offline`,
 `instructions`, `online_instructions`, `offline_instructions`, `content_in_use`, `define_later`, `content_id`, `reflect_on_activity`, `extra_point`) VALUES
  (1,NULL,NULL,NULL,'Scratchie','0','Scenario explanation ', null,null,0,0,${default_content_id},0, 0);

INSERT INTO `tl_lascrt11_scratchie_item` (`uid`, `title`, `description`, `create_by`, `create_date`, `create_by_author`, `scratchie_uid`, `session_uid`, `order_id`) VALUES 
  (1, 'Question Title','Question Description',null,NOW(),1,1,NULL, 1);
  
INSERT INTO `tl_lascrt11_scratchie_answer` (`uid`, `description`, `correct`, `scratchie_item_uid`, `order_id`) VALUES 
  (1, 'Question Answer A',1,1,0);
INSERT INTO `tl_lascrt11_scratchie_answer` (`uid`, `description`, `correct`, `scratchie_item_uid`, `order_id`) VALUES 
  (2, 'Question Answer B',0,1,1);
INSERT INTO `tl_lascrt11_scratchie_answer` (`uid`, `description`, `correct`, `scratchie_item_uid`, `order_id`) VALUES 
  (3, 'Question Answer C',0,1,2);
INSERT INTO `tl_lascrt11_scratchie_answer` (`uid`, `description`, `correct`, `scratchie_item_uid`, `order_id`) VALUES 
  (4, 'Question Answer D',0,1,3);
  
SET FOREIGN_KEY_CHECKS=1;
